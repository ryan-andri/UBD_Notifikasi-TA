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
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.adapter.MyAdapter;
import ryanandri.ubdnotifikasita.models.TitleChild;
import ryanandri.ubdnotifikasita.models.TitleCreator;
import ryanandri.ubdnotifikasita.models.TitleParent;

public class DosenFragment extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dosen, null);

        recyclerView = view.findViewById(R.id.mRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyAdapter myAdapter = new MyAdapter(getContext(), initData());
        myAdapter.setParentClickableViewAnimationDefaultDuration();
        myAdapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(getContext());
        List<TitleParent> titleParents = titleCreator.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();
        for (TitleParent titleParent : titleParents) {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Nama Pembimbing 1"));
            titleParent.setChildObjectList(childList);
            parentObjects.add(titleParent);
        }
        return parentObjects;
    }
}
