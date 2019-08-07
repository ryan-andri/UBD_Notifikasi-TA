package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.interfaces.NilaiCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class NilaiFragment extends Fragment {
    private Context context;
    private VolleySingleExecute volleySingleExecute;
    private TextView tglUP, nilaiUP, statusUP;
    private TextView tglKompre, nilaiKompre, statusKompre;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionConfig sessionConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_nilai, null);

        context = view.getContext();

        volleySingleExecute = new VolleySingleExecute(context);
        sessionConfig = SessionConfig.getInstance(context);

        // Proposal
        tglUP = view.findViewById(R.id.tanggalUjianUP);
        nilaiUP = view.findViewById(R.id.nilaiUjianUP);
        statusUP = view.findViewById(R.id.statusUP);
        // kompre
        tglKompre = view.findViewById(R.id.tanggalUjianKompre);
        nilaiKompre = view.findViewById(R.id.nilaiUjianKompre);
        statusKompre = view.findViewById(R.id.statusKompre);

        //set view
        syncDataNilaiUjian(false);

        swipeRefreshLayout = view.findViewById(R.id.refreshStatus);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        syncDataNilaiUjian(true);
                    }
                }
        );

        return view;
    }

    private void syncDataNilaiUjian(final boolean refresh) {
        volleySingleExecute.asyncNilai(sessionConfig.getNIM(), new NilaiCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arrJson = jsonObject.getJSONArray("nilai_ujian");
                    jsonObject = arrJson.getJSONObject(0);

                    String nilai_up = jsonObject.getString("nilai_up");
                    String nilai_kompre = jsonObject.getString("nilai_kompre");

                    sessionConfig.setNilaiUp(nilai_up);
                    sessionConfig.setNilaiKompre(nilai_kompre);

                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);

                    setHasilUjian();

                } catch (JSONException e) {
                    e.printStackTrace();

                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);

                    setHasilUjian();
                }
            }

            @Override
            public void onErrorNilai(VolleyError error) {
                if (refresh)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setHasilUjian() {
        String sesiTglUP = sessionConfig.getTglUP();
        String sesiNilaiUP = sessionConfig.getNilaiUP();
        String sesiTglKompre = sessionConfig.getTglKOMPRE();
        String sesiNilaiKompre = sessionConfig.getNilaiKompre();

        if (!sesiTglUP.isEmpty()) {
            tglUP.setText(sesiTglUP);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
            FirebaseMessaging.getInstance().subscribeToTopic("nilai_up");
        }

        if(!sesiTglKompre.isEmpty()) {
            tglKompre.setText(sesiTglKompre);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
            FirebaseMessaging.getInstance().subscribeToTopic("nilai_kompre");
        }

        // status UP
        if (!sesiNilaiUP.isEmpty()) {
            nilaiUP.setText(sesiNilaiUP);
            if (sesiNilaiUP.equals("A") | sesiNilaiUP.equals("B") | sesiNilaiUP.equals("C"))
                statusUP.setText("LULUS");
            else
                statusUP.setText("TIDAK LULUS");

            FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_up");
            FirebaseMessaging.getInstance().subscribeToTopic("jadwal_kompre");
        }

        // status kompre
        if (!sesiNilaiKompre.isEmpty()) {
            nilaiKompre.setText(sesiNilaiKompre);
            if (sesiNilaiKompre.equals("A") | sesiNilaiKompre.equals("B") | sesiNilaiKompre.equals("C"))
                statusKompre.setText("LULUS");
            else
                statusKompre.setText("TIDAK LULUS");

            FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_kompre");
        }
    }
}
