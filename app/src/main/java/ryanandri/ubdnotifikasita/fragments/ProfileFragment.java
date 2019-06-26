package ryanandri.ubdnotifikasita.fragments;

import android.app.Activity;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ryanandri.ubdnotifikasita.LoginActivity;
import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.session.Constant;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class ProfileFragment extends Fragment {
    private TextView namaMahasiswa, nimMahasiswa, sksMahasiswa;
    private TextView pbb1, pbb2;

    private SessionConfig sessionConfig;

    private long mLastClickTime = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, null);

        // Session Configuration.
        sessionConfig = SessionConfig.getInstance(view.getContext());

        namaMahasiswa = view.findViewById(R.id.mhsNAMA);
        nimMahasiswa = view.findViewById(R.id.mhsNIM);
        sksMahasiswa = view.findViewById(R.id.mhsSKS);
        pbb1 = view.findViewById(R.id.pembimbing1);
        pbb2 = view.findViewById(R.id.pembimbing2);

        setDataProfile();

        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                            return;
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Konfirmasilogout(v.getContext());
                    }
                }
        );

        swipeRefreshLayout = view.findViewById(R.id.refreshProfile);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        lakukanRefresData(sessionConfig.getNIM(), sessionConfig.getPASSWORD(), view.getContext());
                    }
                }
        );

        return view;
    }

    public void Konfirmasilogout(final Context context) {
        AlertDialog.Builder dialogLogout = new AlertDialog.Builder(context);
        dialogLogout.setMessage("Anda ingin logout ?");
        dialogLogout.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout(context);
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

    public void lakukanRefresData(final String nim, final String pass, final Context context) {

        final String nimTrim = nim.trim();
        final String passTrim = pass.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.login,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                String namaMhs = "", pembimbing1 = "", pembimbing2 = "";
                                int jmlSks = 0;
                                JSONArray arrJson = jsonObject.getJSONArray("data");
                                for (int i = 0; i < arrJson.length(); i++) {
                                    jsonObject = arrJson.getJSONObject(i);
                                    namaMhs = jsonObject.getString("nama");
                                    jmlSks = jsonObject.getInt("sks");
                                    pembimbing1 = jsonObject.getString("pembimbing1");
                                    pembimbing2 = jsonObject.getString("pembimbing2");
                                }

                                sessionConfig.setNamaMHS(namaMhs);
                                sessionConfig.setJumlahSKS(jmlSks);
                                sessionConfig.setPembimbing1(pembimbing1);
                                sessionConfig.setPembimbing2(pembimbing2);

                                setDataProfile();
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                                logout(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                            logout(context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nimTrim);
                params.put("password", passTrim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void setDataProfile() {
        String sesiNama = sessionConfig.getNamaMHS();
        String sesiNim = sessionConfig.getNIM();
        int sesiSks = sessionConfig.getJumlahSKS();
        String sesiPbb1 = sessionConfig.getPembimbing1();
        String sesiPbb2 = sessionConfig.getPembimbing2();

        namaMahasiswa.setText(sesiNama);
        nimMahasiswa.setText(sesiNim);
        sksMahasiswa.setText(String.valueOf(sesiSks));
        pbb1.setText(sesiPbb1);
        pbb2.setText(sesiPbb2);
    }

    public void logout(final Context context) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("notifikasi");
        sessionConfig.setUserLogout();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        ((Activity) context).finish();
    }
}
