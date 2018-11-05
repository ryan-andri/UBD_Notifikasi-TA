package ryanandri.ubdnotifikasita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.models.TitleChild;
import ryanandri.ubdnotifikasita.models.TitleParent;
import ryanandri.ubdnotifikasita.viewholder.TitleChildViewHolder;
import ryanandri.ubdnotifikasita.viewholder.TitleParentViewHolder;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {
    LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.list_parent, viewGroup, false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent titleParent = (TitleParent)o;
        titleParentViewHolder._textView.setText(titleParent.getTitle());

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHoldert, int i, Object o) {
        TitleChild titleChild = (TitleChild)o;
        titleChildViewHoldert._option1.setText(titleChild.getOption1());
    }
}
