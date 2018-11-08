package ryanandri.ubdnotifikasita.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ryanandri.ubdnotifikasita.LoginActivity;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class ProfileFragment extends Fragment {
    private SessionConfig sessionConfig;
    private TextView namaMahasiswa;

    private Button buttonLogout;

    private long mLastClickTime = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);

        swipeRefreshLayout = view.findViewById(R.id.refreshProfile);

        // Session Configuration.
        sessionConfig = SessionConfig.getInstance(getContext());

        namaMahasiswa = view.findViewById(R.id.mhsNIM);
        namaMahasiswa.setText(sessionConfig.getNIM());

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

    public void logout() {
        AlertDialog.Builder dialogLogout = new AlertDialog.Builder(getActivity());
        dialogLogout.setMessage("Anda ingin logout ?");
        dialogLogout.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
