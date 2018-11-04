package ryanandri.ubdnotifikasita.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import ryanandri.ubdnotifikasita.R;

public class DosenFragment extends Fragment {
    private ViewFlipper viewFlipper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dosen, null);

        viewFlipper = view.findViewById(R.id.slideView);

        int gambar[] = {R.drawable.bidar1, R.drawable.bidar4, R.drawable.bidar3};
        // looping untuk array gambar
        for (int slide: gambar) {
            slideshow(slide);
        }

        return view;
    }

    public void slideshow(int gambar) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(gambar);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000); //4 detik
        viewFlipper.setAutoStart(true);

        //animasi untuk gambar
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

    }
}
