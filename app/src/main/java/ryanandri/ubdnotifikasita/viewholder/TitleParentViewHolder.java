package ryanandri.ubdnotifikasita.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import ryanandri.ubdnotifikasita.R;

public class TitleParentViewHolder extends ParentViewHolder{

    public TextView _textView;
    public ImageButton _imageButton;


    public TitleParentViewHolder(View itemView) {
        super(itemView);
        _textView = itemView.findViewById(R.id.parentTitle);
        _imageButton = itemView.findViewById(R.id.expandableArrow);
    }
}
