package ryanandri.ubdnotifikasita.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.ListItemNotifikasi;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.NotifikasiAdapter;

public class NotifikasiFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItemNotifikasi> listItemNotifikasis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, null);

        recyclerView = view.findViewById(R.id.recycleNotif);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItemNotifikasis = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ListItemNotifikasi listItemNotifikasi = new ListItemNotifikasi("Notifikasi "+i,
                    "01/01/1111", "Silahkan check pada bagian menu yang bersangkutan");

            listItemNotifikasis.add(listItemNotifikasi);
        }

        adapter = new NotifikasiAdapter(listItemNotifikasis, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
