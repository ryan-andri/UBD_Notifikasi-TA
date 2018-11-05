package ryanandri.ubdnotifikasita.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {

    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();

        for (int i=0; i < 2; i++) {
            TitleParent title = new TitleParent(String.format("Pembimbing "+(i+1), i));
            _titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context) {
        if (_titleCreator == null)
            _titleCreator = new TitleCreator(context);

        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
