package ryanandri.ubdnotifikasita.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import ryanandri.ubdnotifikasita.LoginActivity;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class ProfileFragment extends Fragment {
    private SessionConfig sessionConfig;

    private TextView namaMahasiswa;
    private TextView nimMahasiswa;

    private TextView sksMahasiswa;
    private TextView pbb1, pbb2;

    private Button buttonLogout;

    private long mLastClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);

        // Session Configuration.
        sessionConfig = SessionConfig.getInstance(getContext());

        String sesiNama = sessionConfig.getNamaMHS();
        String sesiNim = sessionConfig.getNIM();
        int sesiSks = sessionConfig.getJumlahSKS();
        String sesiPbb1 = sessionConfig.getPembimbing1();
        String sesiPbb2 = sessionConfig.getPembimbing2();

        namaMahasiswa = view.findViewById(R.id.mhsNAMA);
        namaMahasiswa.setText(sesiNama);

        nimMahasiswa = view.findViewById(R.id.mhsNIM);
        nimMahasiswa.setText(sesiNim);

        sksMahasiswa = view.findViewById(R.id.mhsSKS);
        sksMahasiswa.setText(String.valueOf(sesiSks));

        pbb1 = view.findViewById(R.id.pembimbing1);
        pbb1.setText(sesiPbb1);
        pbb2 = view.findViewById(R.id.pembimbing2);
        pbb2.setText(sesiPbb2);

        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                            return;
                        mLastClickTime = SystemClock.elapsedRealtime();
                        logout();
                    }
                }
        );

        return view;
    }

    public void logout() {
        AlertDialog.Builder dialogLogout = new AlertDialog.Builder(getActivity());
        dialogLogout.setMessage("Anda ingin logout ?");
        dialogLogout.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("notifikasi");
                sessionConfig.setUserLogout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        dialogLogout.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        AlertDialog alertDialog = dialogLogout.create();
        alertDialog.show();
    }
}
