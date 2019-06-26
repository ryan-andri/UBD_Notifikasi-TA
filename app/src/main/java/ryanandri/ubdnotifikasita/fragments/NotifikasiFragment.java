package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.ListItemNotifikasi;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.NotifikasiAdapter;
import ryanandri.ubdnotifikasita.session.Constant;

public class NotifikasiFragment extends Fragment {
    private List<ListItemNotifikasi> listItemNotifikasis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, null);

        RecyclerView recyclerView = view.findViewById(R.id.recycleNotif);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // lakukakan sync list notifikasi
        listItemNotifikasis = new ArrayList<>();
        syncNotifikasiList(getContext());

        RecyclerView.Adapter adapter = new NotifikasiAdapter(listItemNotifikasis, view.getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void syncNotifikasiList(Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL+Constant.login,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                JSONArray arrJson = jsonObject.getJSONArray("list_notifikasi");
                                for (int i = 0; i < arrJson.length(); i++) {
                                    jsonObject = arrJson.getJSONObject(i);
                                    String headNotif = jsonObject.getString("head");
                                    String tanggalNotif = jsonObject.getString("tanggal");
                                    String bodyNotif = jsonObject.getString("isi");
                                    ListItemNotifikasi listItemNotifikasi = new ListItemNotifikasi(headNotif, tanggalNotif, bodyNotif);
                                    listItemNotifikasis.add(listItemNotifikasi);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}