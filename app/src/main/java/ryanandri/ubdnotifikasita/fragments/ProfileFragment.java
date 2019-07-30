package ryanandri.ubdnotifikasita.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
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
    private TextView namaMahasiswa, nimMahasiswa, pbb1, pbb2, jmlSks;
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
        jmlSks = view.findViewById(R.id.mhsSKS);
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
                        lakukanRefreshData(sessionConfig.getNIM(),
                                sessionConfig.getPASSWORD(), view.getContext());
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

    public void lakukanRefreshData(final String nim, final String pass, final Context context) {

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
                                JSONArray arrJson = jsonObject.getJSONArray("data");
                                for (int i = 0; i < arrJson.length(); i++) {
                                    jsonObject = arrJson.getJSONObject(i);
                                    sessionConfig.setNamaMHS(jsonObject.getString("nama_mhs"));
                                    sessionConfig.setJumlahSKS(jsonObject.getInt("total_sks"));
                                    sessionConfig.setPembimbing1(jsonObject.getString("nama_pbb1"));
                                    sessionConfig.setPembimbing2(jsonObject.getString("nama_pbb2"));
                                }
                                setDataProfile();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
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
                params.put("nim_mhs", nimTrim);
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
        int sesiSks = sessionConfig.getJmlSks();
        String sesiPbb1 = sessionConfig.getPembimbing1();
        String sesiPbb2 = sessionConfig.getPembimbing2();

        if (sesiPbb1.isEmpty()) {
            FirebaseMessaging.getInstance().subscribeToTopic("pembimbing");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("pembimbing");
        }

        namaMahasiswa.setText(sesiNama);
        nimMahasiswa.setText(sesiNim);
        jmlSks.setText(sesiSks);
        pbb1.setText(sesiPbb1);
        pbb2.setText(sesiPbb2);
    }

    public void logout(final Context context) {
        unsubNotifikasi();
        sessionConfig.setUserLogout();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        ((Activity) context).finish();
    }

    public void unsubNotifikasi() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pembimbing");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_kompre");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_up");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("nilai_kompre");
    }
}
