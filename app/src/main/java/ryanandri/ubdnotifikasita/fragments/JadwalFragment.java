package ryanandri.ubdnotifikasita.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.interfaces.JadwalCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class JadwalFragment extends Fragment {
    private VolleySingleExecute volleySingleExecute;
    private SessionConfig sessionConfig;
    private SwipeRefreshLayout swipeRefreshLayoutJadwal;

    private TextView tglUP, wktUP, rgnUP, pgj1UP, pgj2UP;
    private TextView tglUK, wktUK, rgnUK, pgj1UK, pgj2UK;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        volleySingleExecute = new VolleySingleExecute(view.getContext());
        sessionConfig = SessionConfig.getInstance(view.getContext());

        setTextView(view);

        swipeRefreshLayoutJadwal = view.findViewById(R.id.refreshJadwal);

        syncDataJadwalUjian(false);

        swipeRefreshLayoutJadwal.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayoutJadwal.setRefreshing(true);
                        syncDataJadwalUjian(true);
                    }
                }
        );

        return view;
    }

    private void setTextView(View view) {
        tglUP = view.findViewById(R.id.tglUp);
        wktUP = view.findViewById(R.id.wktUp);
        rgnUP = view.findViewById(R.id.ruangUp);
        pgj1UP = view.findViewById(R.id.penguji1Up);
        pgj2UP = view.findViewById(R.id.penguji2Up);
        tglUK = view.findViewById(R.id.tglUk);
        wktUK = view.findViewById(R.id.wktUk);
        rgnUK = view.findViewById(R.id.ruangUk);
        pgj1UK = view.findViewById(R.id.penguji1Uk);
        pgj2UK = view.findViewById(R.id.penguji2Uk);
    }

    private void syncDataJadwalUjian(final boolean refreh) {
        volleySingleExecute.asyncJadwal(sessionConfig.getNIM(), new JadwalCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray arrJson = jsonObject.getJSONArray("jadwal_ujian");
                    jsonObject = arrJson.getJSONObject(0);

                    // jadwal Ujian Proposal
                    String tgl_up = jsonObject.getString("tanggal_up");
                    String waktu_up = jsonObject.getString("waktu_up");
                    String ruangan_up = jsonObject.getString("ruangan_up");
                    String penguji1_up = jsonObject.getString("penguji1_up");
                    String penguji2_up = jsonObject.getString("penguji2_up");

                    // jadwal Ujian Komprehensif
                    String tanggal_kompre = jsonObject.getString("tanggal_kompre");
                    String waktu_kompre = jsonObject.getString("waktu_kompre");
                    String ruangan_kompre = jsonObject.getString("ruangan_kompre");
                    String penguji1_kompre = jsonObject.getString("penguji1_kompre");
                    String penguji2_kompre = jsonObject.getString("penguji2_kompre");

                    sessionConfig.setJadwalUP(tgl_up, waktu_up, ruangan_up,
                            penguji1_up, penguji2_up);
                    sessionConfig.setJadwalKOMPRE(tanggal_kompre, waktu_kompre, ruangan_kompre,
                            penguji1_kompre, penguji2_kompre);

                    // set Jadwal Ujian UP
                    if (!tgl_up.isEmpty()) {
                        setJadwalUP(tgl_up, waktu_up,
                                ruangan_up, penguji1_up, penguji2_up, refreh);
                    } else {
                        setJadwalUP("Belum Ada", "Belum Ada",
                                "Belum Ada","Belum Ada", "Belum Ada", refreh);
                    }

                    // set Jadwal Ujian UK
                    if (!tanggal_kompre.isEmpty()) {
                        setJadwalKompre(tanggal_kompre, waktu_kompre,
                                ruangan_kompre, penguji1_kompre, penguji2_kompre, refreh);
                    } else {
                        setJadwalKompre("Belum Ada", "Belum Ada",
                                "Belum Ada","Belum Ada", "Belum Ada", refreh);
                    }

                    swipeRefreshLayoutJadwal.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (refreh) swipeRefreshLayoutJadwal.setRefreshing(false);
                }
            }

            @Override
            public void onErrorJadwal(VolleyError error) {
                if (refreh) swipeRefreshLayoutJadwal.setRefreshing(false);
            }
        });
    }

    private void setJadwalUP(String tgl, String wkt,
                             String ruang, String pgj1, String pgj2, boolean refresh) {
        tglUP.setText(tgl);
        wktUP.setText(wkt);
        rgnUP.setText(ruang);
        pgj1UP.setText(pgj1);
        pgj2UP.setText(pgj2);

    }

    private void setJadwalKompre(String tgl, String wkt,
                                 String ruang, String pgj1, String pgj2, boolean refresh) {
        tglUK.setText(tgl);
        wktUK.setText(wkt);
        rgnUK.setText(ruang);
        pgj1UK.setText(pgj1);
        pgj2UK.setText(pgj2);
    }

}
