package ryanandri.ubdnotifikasita.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ryanandri.ubdnotifikasita.R;

public class JudulFragment extends Fragment {
    private CoordinatorLayout snackJudul;
    private ConstraintLayout formJudul, arsipJudul;
    private EditText judul1, judul2, judul3;
    private Button kirimJudul;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judul, null);

        // Parent dengan 2 layout berbeda
        formJudul = view.findViewById(R.id.formJudul);
        arsipJudul = view.findViewById(R.id.arsipJudul);

        // Snackbar popup
        snackJudul = view.findViewById(R.id.snackJudul);

        judul1 = view.findViewById(R.id.inputJudul1);
        judul2 = view.findViewById(R.id.inputJudul2);
        judul3 = view.findViewById(R.id.inputJudul3);

        kirimJudul = view.findViewById(R.id.kirimJudul);
        kirimJudul.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkInput();
                    }
                }
        );

        //arsipJudul.setVisibility(View.GONE);

        return view;
    }

    public void checkInput() {
        String J1 = judul1.getText().toString();
        String J2 = judul2.getText().toString();
        String J3 = judul3.getText().toString();
        if (J1.isEmpty() || J2.isEmpty() || J3.isEmpty()) {
            Snackbar snackbar = Snackbar.make(snackJudul, "Semua kolom harus di isi !", Snackbar.LENGTH_SHORT);
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(getResources().getColor(R.color.colorRed));
            snackbar.show();
            return;
        }

        formJudul.setVisibility(View.GONE);
        arsipJudul.setVisibility(View.VISIBLE);
    }
}
