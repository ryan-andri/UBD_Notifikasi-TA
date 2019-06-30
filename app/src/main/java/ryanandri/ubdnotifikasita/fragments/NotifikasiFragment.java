package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ryanandri.ubdnotifikasita.ListItemNotifikasi;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.NotifikasiAdapter;
import ryanandri.ubdnotifikasita.session.Constant;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class NotifikasiFragment extends Fragment {
    private List<ListItemNotifikasi> listItemNotifikasis;

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private ConstraintLayout emptyLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SessionConfig sessionConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifikasi, null);

        sessionConfig = SessionConfig.getInstance(view.getContext());

        recyclerView = view.findViewById(R.id.recycleNotif);
        relativeLayout = view.findViewById(R.id.listNotifikasi);
        swipeRefreshLayout = view.findViewById(R.id.refreshListNotifikasi);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // lakukakan sync list notifikasi
        listItemNotifikasis = new ArrayList<>();
        syncNotifikasiList(view.getContext(), false);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);

                        // cegah duplikasi listview
                        if (listItemNotifikasis.size() > 0)
                            listItemNotifikasis.clear();

                        syncNotifikasiList(view.getContext(), true);
                    }
                }
        );

        return view;
    }

    public void syncNotifikasiList(final Context context, final boolean refresh) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL+Constant.ambil_data_notifikasi,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arrJson = new JSONArray(response);
                            for (int i = 0; i < arrJson.length(); i++) {
                                JSONObject jsonObject  = arrJson.getJSONObject(i);
                                String headNotif = jsonObject.getString("head");
                                String tanggalNotif = jsonObject.getString("tanggal");
                                String bodyNotif = jsonObject.getString("isi");
                                ListItemNotifikasi listItemNotifikasi = new ListItemNotifikasi(headNotif, tanggalNotif, bodyNotif);
                                listItemNotifikasis.add(listItemNotifikasi);
                            }
                            setAdapter(context);
                            if (refresh)
                                swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            setAdapter(context);
                            if (refresh)
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        setAdapter(context);
                        if (refresh)
                            swipeRefreshLayout.setRefreshing(false);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void setAdapter(Context context) {
        RecyclerView.Adapter adapter = new NotifikasiAdapter(listItemNotifikasis, context);
        recyclerView.setAdapter(adapter);
    }
}