package ryanandri.ubdnotifikasita.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import ryanandri.ubdnotifikasita.R;

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView _option1;
    public TitleChildViewHolder(View itemView) {
        super(itemView);
        _option1 = itemView.findViewById(R.id.option1);
    }
}
