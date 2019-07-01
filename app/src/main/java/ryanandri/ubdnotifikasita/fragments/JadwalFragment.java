package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ryanandri.ubdnotifikasita.ListItemNotifikasi;
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

        if (!sessionConfig.getTglUP().isEmpty()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
            FirebaseMessaging.getInstance().subscribeToTopic("jadwal_kompre");
        }

        listItemJadwals = new ArrayList<>();
        syncDataJadwalUP(view.getContext());
        syncDataJadwalKOMPRE(view.getContext());

        return view;
    }

    public void syncDataJadwalUP(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.data_jadwal_up,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                JSONArray arrJson = jsonObject.getJSONArray("jadwal_up");
                                jsonObject = arrJson.getJSONObject(0);
                                String tgl = jsonObject.getString("tanggal");
                                String waktu = jsonObject.getString("waktu");
                                String ruangan = jsonObject.getString("ruangan");
                                String penguji1 = jsonObject.getString("penguji1");
                                String penguji2 = jsonObject.getString("penguji2");

                                sessionConfig.setJadwalUP(tgl, waktu, ruangan, penguji1, penguji2);

                                if (!tgl.isEmpty()) {
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
                                    FirebaseMessaging.getInstance().subscribeToTopic("jadwal_kompre");
                                }

                                ListItemJadwal listItemJadwal = new ListItemJadwal(
                                        "UJIAN PROPOSAL", tgl, waktu, ruangan, penguji1, penguji2
                                );
                                listItemJadwals.add(listItemJadwal);
                                setAdapterJadwal(context);
                            } else {
                                sessionConfig.deleteJadwalUP();
                                ListItemJadwal listItemJadwal = new ListItemJadwal(
                                        "UJIAN PROPOSAL", "Belum Ada", "Belum Ada",
                                        "Belum Ada", "Belum Ada", "Belum Ada"
                                );
                                listItemJadwals.add(listItemJadwal);
                                setAdapterJadwal(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ListItemJadwal listItemJadwal = new ListItemJadwal(
                                    "UJIAN PROPOSAL", "Belum Ada", "Belum Ada",
                                    "Belum Ada", "Belum Ada", "Belum Ada"
                            );
                            listItemJadwals.add(listItemJadwal);
                            setAdapterJadwal(context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ListItemJadwal listItemJadwal = new ListItemJadwal(
                                "UJIAN PROPOSAL", "Belum Ada", "Belum Ada",
                                "Belum Ada", "Belum Ada", "Belum Ada"
                        );
                        listItemJadwals.add(listItemJadwal);
                        setAdapterJadwal(context);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", sessionConfig.getNIM());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void syncDataJadwalKOMPRE(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.data_jadwal_kompre,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                JSONArray arrJson = jsonObject.getJSONArray("jadwal_kompre");
                                jsonObject = arrJson.getJSONObject(0);
                                String tgl = jsonObject.getString("tanggal");
                                String waktu = jsonObject.getString("waktu");
                                String ruangan = jsonObject.getString("ruangan");
                                String penguji1 = jsonObject.getString("penguji1");
                                String penguji2 = jsonObject.getString("penguji2");

                                sessionConfig.setJadwalKOMPRE(tgl, waktu, ruangan, penguji1, penguji2);

                                if (!tgl.isEmpty()) {
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
                                }

                                ListItemJadwal listItemJadwal = new ListItemJadwal(
                                        "UJIAN KOMPREHENSIF", tgl, waktu, ruangan, penguji1, penguji2
                                );
                                listItemJadwals.add(listItemJadwal);
                                setAdapterJadwal(context);
                            } else {
                                sessionConfig.deleteJadwalKOMPRE();
                                ListItemJadwal listItemJadwal = new ListItemJadwal(
                                        "UJIAN KOMPREHENSIF", "Belum Ada", "Belum Ada",
                                        "Belum Ada", "Belum Ada", "Belum Ada"
                                );
                                listItemJadwals.add(listItemJadwal);
                                setAdapterJadwal(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ListItemJadwal listItemJadwal = new ListItemJadwal(
                                    "UJIAN KOMPREHENSIF", "Belum Ada", "Belum Ada",
                                    "Belum Ada", "Belum Ada", "Belum Ada"
                            );
                            listItemJadwals.add(listItemJadwal);
                            setAdapterJadwal(context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ListItemJadwal listItemJadwal = new ListItemJadwal(
                                "UJIAN KOMPREHENSIF", "Belum Ada", "Belum Ada",
                                "Belum Ada", "Belum Ada", "Belum Ada"
                        );
                        listItemJadwals.add(listItemJadwal);
                        setAdapterJadwal(context);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", sessionConfig.getNIM());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void setAdapterJadwal(Context context) {
        adapter = new JadwalAdapter(listItemJadwals, context);
        recyclerView.setAdapter(adapter);
    }
}
