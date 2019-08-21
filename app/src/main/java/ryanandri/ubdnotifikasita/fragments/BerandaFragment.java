package ryanandri.ubdnotifikasita.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.LoginActivity;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.interfaces.LoginCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class BerandaFragment extends Fragment {
    private Context context;
    private TextView namaMahasiswa, nimMahasiswa, sksMahasiswa, pbb1, pbb2;
    private SessionConfig sessionConfig;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VolleySingleExecute volleySingleExecute;

    private long mLastClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = view.getContext();

        volleySingleExecute = new VolleySingleExecute(context);

        // Session Configuration.
        sessionConfig = SessionConfig.getInstance(context);

        namaMahasiswa = view.findViewById(R.id.mhsNAMA);
        nimMahasiswa = view.findViewById(R.id.mhsNIM);
        sksMahasiswa = view.findViewById(R.id.mhsSKS);
        pbb1 = view.findViewById(R.id.pembimbing1);
        pbb2 = view.findViewById(R.id.pembimbing2);

        setDataProfile();

        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                            return;
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Konfirmasilogout();
                    }
                }
        );

        swipeRefreshLayout = view.findViewById(R.id.refreshProfile);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        lakukanRefreshData(sessionConfig.getNIM(), sessionConfig.getPASSWORD());
                    }
                }
        );

        return view;
    }

    private void Konfirmasilogout() {
        AlertDialog.Builder dialogLogout = new AlertDialog.Builder(context);
        dialogLogout.setMessage("Anda ingin logout ?");
        dialogLogout.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        dialogLogout.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        AlertDialog alertDialog = dialogLogout.create();
        alertDialog.show();
    }

    private void lakukanRefreshData(final String nim, final String pass) {
        volleySingleExecute.asyncLoginFetchData(nim, pass, new LoginCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        String namaMhs = "", pembimbing1 = "", pembimbing2 = "";
                        int jmlSks = 0;
                        JSONArray arrJson = jsonObject.getJSONArray("data");
                        for (int i = 0; i < arrJson.length(); i++) {
                            jsonObject = arrJson.getJSONObject(i);
                            namaMhs = jsonObject.getString("nama_mhs");
                            jmlSks = jsonObject.getInt("total_sks");
                            pembimbing1 = jsonObject.getString("nama_pbb1");
                            pembimbing2 = jsonObject.getString("nama_pbb2");
                        }

                        sessionConfig.setNamaMHS(namaMhs);
                        sessionConfig.setJumlahSKS(jmlSks);
                        sessionConfig.setPembimbing1(pembimbing1);
                        sessionConfig.setPembimbing2(pembimbing2);

                        setDataProfile();
                    } else {
                        // logout jika ada perubahan
                        logout();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                    logout();
                }
            }

            @Override
            public void onErrorLogin(VolleyError error) {
                error.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setDataProfile() {
        String sesiNama = sessionConfig.getNamaMHS();
        String sesiNim = sessionConfig.getNIM();
        int sesiSks = sessionConfig.getJumlahSKS();
        String sesiPbb1 = sessionConfig.getPembimbing1();
        String sesiPbb2 = sessionConfig.getPembimbing2();

        // hentikan notifikasi pembimbing jika sudah ada pembimbing
        if (!sesiPbb1.isEmpty()) FirebaseMessaging.getInstance().unsubscribeFromTopic("pembimbing");

        namaMahasiswa.setText(sesiNama);
        nimMahasiswa.setText(sesiNim);
        sksMahasiswa.setText(String.valueOf(sesiSks));
        pbb1.setText(sesiPbb1);
        pbb2.setText(sesiPbb2);
    }

    private void logout() {
        unsubNotifikasi();
        sessionConfig.setUserLogout();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        ((Activity) context).finish();
    }

    private void unsubNotifikasi() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pembimbing");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_up");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_kompre");
    }
}
