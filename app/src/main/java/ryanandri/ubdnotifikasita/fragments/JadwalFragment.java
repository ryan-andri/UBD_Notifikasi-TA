package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.ListItemJadwal;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.adapter.JadwalAdapter;
import ryanandri.ubdnotifikasita.interfaces.JadwalCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class JadwalFragment extends Fragment {
    private Context context;
    private VolleySingleExecute volleySingleExecute;
    private SessionConfig sessionConfig;
    private RecyclerView recyclerView;
    private List<ListItemJadwal> listItemJadwals;
    private SwipeRefreshLayout swipeRefreshLayoutJadwal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_jadwal, null);

        context = view.getContext();

        volleySingleExecute = new VolleySingleExecute(view.getContext());

        sessionConfig = SessionConfig.getInstance(view.getContext());

        recyclerView = view.findViewById(R.id.recycleJadwal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if (!sessionConfig.getPembimbing1().isEmpty()
                && !sessionConfig.getJudul1().isEmpty())
            FirebaseMessaging.getInstance().subscribeToTopic("jadwal_up");

        listItemJadwals = new ArrayList<>();
        syncDataJadwalUjian(false);

        swipeRefreshLayoutJadwal = view.findViewById(R.id.refreshJadwal);
        swipeRefreshLayoutJadwal.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayoutJadwal.setRefreshing(true);

                        // cegah duplikasi
                        if (listItemJadwals.size() > 0) listItemJadwals.clear();

                        syncDataJadwalUjian(true);
                    }
                }
        );

        return view;
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

                    sessionConfig.setJadwalKOMPRE(tanggal_kompre, waktu_kompre,ruangan_kompre,
                            penguji1_kompre, penguji2_kompre);

                    if (tgl_up.isEmpty()) {
                        ListItemJadwal listItemJadwalUP = new ListItemJadwal(
                                "UJIAN PROPOSAL", "Belum Ada", "Belum Ada",
                                "Belum Ada", "Belum Ada", "Belum Ada"
                        );
                        listItemJadwals.add(listItemJadwalUP);
                    } else {
                        ListItemJadwal listItemJadwalUP = new ListItemJadwal(
                                "UJIAN PROPOSAL", tgl_up, waktu_up, ruangan_up, penguji1_up, penguji2_up
                        );
                        listItemJadwals.add(listItemJadwalUP);
                    }

                    if (tanggal_kompre.isEmpty()) {
                        ListItemJadwal listItemJadwalKompre = new ListItemJadwal(
                                "UJIAN KOMPREHENSIF", "Belum Ada", "Belum Ada",
                                "Belum Ada", "Belum Ada", "Belum Ada"
                        );
                        listItemJadwals.add(listItemJadwalKompre);
                    } else {
                        ListItemJadwal listItemJadwalKompre = new ListItemJadwal(
                                "UJIAN KOMPREHENSIF", tanggal_kompre, waktu_kompre,
                                ruangan_kompre, penguji1_kompre, penguji2_kompre
                        );
                        listItemJadwals.add(listItemJadwalKompre);
                    }
                    setAdapterJadwal(refreh);
                } catch (JSONException e) {
                    e.printStackTrace();
                    setJadwalBelumAda(refreh);
                }
            }

            @Override
            public void onErrorJadwal(VolleyError error) {
                setJadwalBelumAda(refreh);
            }
        });
    }

    private void setJadwalBelumAda(boolean refreh) {
        ListItemJadwal listItemJadwalUP = new ListItemJadwal(
                "UJIAN PROPOSAL", "Belum Ada", "Belum Ada",
                "Belum Ada", "Belum Ada", "Belum Ada"
        );
        ListItemJadwal listItemJadwalKompre = new ListItemJadwal(
                "UJIAN KOMPREHENSIF", "Belum Ada", "Belum Ada",
                "Belum Ada", "Belum Ada", "Belum Ada"
        );
        listItemJadwals.add(listItemJadwalUP);
        listItemJadwals.add(listItemJadwalKompre);
        setAdapterJadwal(refreh);
    }

    private void setAdapterJadwal(boolean refreh) {
        RecyclerView.Adapter adapter = new JadwalAdapter(listItemJadwals, context);
        recyclerView.setAdapter(adapter);

        if (refreh)
            swipeRefreshLayoutJadwal.setRefreshing(false);
    }

}
