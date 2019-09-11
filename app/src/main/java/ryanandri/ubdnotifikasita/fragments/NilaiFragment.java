package ryanandri.ubdnotifikasita.fragments;

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

import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.interfaces.NilaiCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class NilaiFragment extends Fragment {
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

        final View view = inflater.inflate(R.layout.fragment_nilai, container, false);

        volleySingleExecute = new VolleySingleExecute(view.getContext());
        sessionConfig = SessionConfig.getInstance(view.getContext());

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

                    JSONObject dataUP = jsonObject.getJSONObject("jadwal_up");
                    String tgl_up = dataUP.getString("tanggal");
                    String nilai_up = dataUP.getString("nilai");
                    String pelaksanaan_up = dataUP.getString("pelaksanaan");

                    JSONObject dataKompre = jsonObject.getJSONObject("jadwal_kompre");
                    String tanggal_kompre = dataKompre.getString("tanggal");
                    String nilai_kompre = dataKompre.getString("nilai");
                    String pelaksanaan_kompre = dataKompre.getString("pelaksanaan");

                    sessionConfig.setNilaiUp(nilai_up);
                    sessionConfig.setPelaksanaanUP(pelaksanaan_up);
                    sessionConfig.setNilaiKompre(nilai_kompre);
                    sessionConfig.setPelaksanaanKompre(pelaksanaan_kompre);

                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);

                    setHasilUjian(tgl_up, tanggal_kompre);

                } catch (JSONException e) {
                    e.printStackTrace();

                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);

                    setHasilUjian("", "");
                }
            }

            @Override
            public void onErrorNilai(VolleyError error) {
                if (refresh)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setHasilUjian(String TglUp, String TglKompre) {
        String sesiNilaiUP = sessionConfig.getNilaiUP();
        String sesiNilaiKompre = sessionConfig.getNilaiKompre();

        tglUP.setText((TglUp.equals("") || TglUp.isEmpty()) ? "Belum Ada" : TglUp);
        tglKompre.setText((TglKompre.equals("") || TglKompre.isEmpty()) ? "Belum Ada" : TglKompre);

        // status UP
        if (!sesiNilaiUP.isEmpty()) {
            nilaiUP.setText(sesiNilaiUP);
            statusUP.setText((sesiNilaiUP.equals("A") | sesiNilaiUP.equals("B") | sesiNilaiUP.equals("C")) ?
                        "LULUS" : "TIDAK LULUS");
        }

        // status kompre
        if (!sesiNilaiKompre.isEmpty()) {
            nilaiKompre.setText(sesiNilaiKompre);
            statusKompre.setText((sesiNilaiKompre.equals("A") | sesiNilaiKompre.equals("B") | sesiNilaiKompre.equals("C")) ?
                    "LULUS" : "TIDAK LULUS");
        }
    }
}
