package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryanandri.ubdnotifikasita.ListItemJadwal;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.JadwalAdapter;
import ryanandri.ubdnotifikasita.session.Constant;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class JadwalFragment extends Fragment {

    private SessionConfig sessionConfig;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemJadwal> listItemJadwals;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_jadwal, null);

        sessionConfig = SessionConfig.getInstance(view.getContext());

        recyclerView = view.findViewById(R.id.recycleJadwal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if (!sessionConfig.getPembimbing1().isEmpty()
                && !sessionConfig.getJudul1().isEmpty())
            FirebaseMessaging.getInstance().subscribeToTopic("jadwal_up");

        listItemJadwals = new ArrayList<>();
        syncDataJadwalUjian(view.getContext());

        return view;
    }

    public void syncDataJadwalUjian(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.jadwal_ujian,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arrJson = jsonObject.getJSONArray("jadwal_ujian");
                            jsonObject = arrJson.getJSONObject(0);

                            // jadwal Ujian Proposal
                            String tgl_up = jsonObject.getString("tanggal_up");
                            String waktu_up = jsonObject.getString("waktu_up");
                            String ruangan_up = jsonObject.getString("ruangan_up");
                            String penguji1_up = jsonObject.getString("penguji1_up");
                            String penguji2_up = jsonObject.getString("penguji2_up");
                            String nilai_up = jsonObject.getString("nilai_up");

                            sessionConfig.setJadwalUP(tgl_up, waktu_up, ruangan_up,
                                                                                penguji1_up, penguji2_up, nilai_up);

                            if (!tgl_up.isEmpty()) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
                            }

                            // jadwal Ujian Komprehensif
                            String tanggal_kompre = jsonObject.getString("tanggal_kompre");
                            String waktu_kompre = jsonObject.getString("waktu_kompre");
                            String ruangan_kompre = jsonObject.getString("ruangan_kompre");
                            String penguji1_kompre = jsonObject.getString("penguji1_kompre");
                            String penguji2_kompre = jsonObject.getString("penguji2_kompre");
                            String nilai_kompre = jsonObject.getString("nilai_kompre");

                            sessionConfig.setJadwalKOMPRE(tanggal_kompre, waktu_kompre,ruangan_kompre,
                                                                                penguji1_kompre, penguji2_kompre, nilai_kompre);

                            if (!tanggal_kompre.isEmpty()) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
                            }

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
                            setAdapterJadwal(context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setJadwalBelumAda(context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setJadwalBelumAda(context);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim_mhs", sessionConfig.getNIM());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void setJadwalBelumAda(Context context) {
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
        setAdapterJadwal(context);
    }

    public void setAdapterJadwal(Context context) {
        adapter = new JadwalAdapter(listItemJadwals, context);
        recyclerView.setAdapter(adapter);
    }

}
