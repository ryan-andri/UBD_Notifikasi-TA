package ryanandri.ubdnotifikasita.fragments;

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

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.ListItemJadwal;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.JadwalAdapter;

public class JadwalFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemJadwal> listItemJadwals;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jadwal, null);

        swipeRefreshLayout = view.findViewById(R.id.refreshJadwal);

        recyclerView = view.findViewById(R.id.recycleJadwal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItemJadwals = new ArrayList<>();
        ListItemJadwal listItemJadwalUP = new ListItemJadwal(
                "UJIAN PROPOSAL", "01/01/1111",
                "08:00", "404",
                "Nama Dosen penguji 1",
                "Nama Dosen penguji 2"
        );
        ListItemJadwal listItemJadwalKOM = new ListItemJadwal(
                "UJIAN KOMPREHENSIF", "02/02/2222",
                "09:00", "403",
                "Nama Dosen penguji 1",
                "Nama Dosen penguji 2"
        );

        listItemJadwals.add(listItemJadwalUP);
        listItemJadwals.add(listItemJadwalKOM);

        adapter = new JadwalAdapter(listItemJadwals, getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
        );

        return view;
    }
}
