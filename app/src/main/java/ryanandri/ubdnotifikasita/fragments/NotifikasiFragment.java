package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.ListItemNotifikasi;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.adapter.NotifikasiAdapter;
import ryanandri.ubdnotifikasita.interfaces.NotifikasiCallBack;

public class NotifikasiFragment extends Fragment {
    private Context context;
    private VolleySingleExecute volleySingleExecute;
    private List<ListItemNotifikasi> listItemNotifikasis;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        context = view.getContext();

        volleySingleExecute = new VolleySingleExecute(context);

        recyclerView = view.findViewById(R.id.recycleNotif);
        swipeRefreshLayout = view.findViewById(R.id.refreshListNotifikasi);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // lakukakan sync list notifikasi
        listItemNotifikasis = new ArrayList<>();
        syncNotifikasiList(false);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);

                        // cegah duplikasi listview
                        if (listItemNotifikasis.size() > 0)
                            listItemNotifikasis.clear();

                        syncNotifikasiList(true);
                    }
                }
        );

        return view;
    }

    private void syncNotifikasiList(final boolean refresh) {
        volleySingleExecute.asyncNotifikasiList(new NotifikasiCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray arrJson = new JSONArray(result);
                    for (int i = 0; i < arrJson.length(); i++) {
                        JSONObject jsonObject  = arrJson.getJSONObject(i);
                        String headNotif = jsonObject.getString("head");
                        String tanggalNotif = jsonObject.getString("tanggal");
                        String bodyNotif = jsonObject.getString("isi");
                        ListItemNotifikasi listItemNotifikasi = new ListItemNotifikasi(headNotif, tanggalNotif, bodyNotif);
                        listItemNotifikasis.add(listItemNotifikasi);
                    }
                    setAdapter();
                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();

                    setAdapter();
                    if (refresh)
                        swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onErrorNotifikasi(VolleyError error) {
                setAdapter();
                if (refresh)
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setAdapter() {
        RecyclerView.Adapter adapter = new NotifikasiAdapter(listItemNotifikasis, context);
        recyclerView.setAdapter(adapter);
    }
}