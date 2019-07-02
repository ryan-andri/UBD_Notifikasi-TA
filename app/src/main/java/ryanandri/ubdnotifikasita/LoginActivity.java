package ryanandri.ubdnotifikasita;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

import ryanandri.ubdnotifikasita.session.Constant;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class LoginActivity extends AppCompatActivity {
    private SessionConfig sessionConfig;

    private ConstraintLayout constraintLayoutLogin;
    private ConstraintLayout formLogin;
    private ConstraintLayout formLoading;

    private EditText loginNIM;
    private EditText loginPASS;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout login
        setContentView(R.layout.activity_login);

        // untuk snackbar pop-up
        constraintLayoutLogin = findViewById(R.id.constraintLayoutLogin);

        formLogin = findViewById(R.id.formLogin);
        formLoading = findViewById(R.id.loadingWidget);

        loginNIM = findViewById(R.id.loginNIM);
        loginPASS = findViewById(R.id.loginPASS);

        // cek pengguna sudah login sebelumnya
        sessionConfig = SessionConfig.getInstance(this);
        if (sessionConfig.IsLogin()) {
            String nimSesi = sessionConfig.getNIM();
            String passSesi = sessionConfig.getPASSWORD();
            asyncLoginFetchData(nimSesi, passSesi);
        }

        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                            return;
                        mLastClickTime = SystemClock.elapsedRealtime();

                        lakukanLogin();
                    }
                }
        );
    }

    public void lakukanLogin() {
        String nim = loginNIM.getText().toString();
        String pass = loginPASS.getText().toString();

        if (nim.isEmpty() || pass.isEmpty()) {
            tampilkanSnackBar("Semua kolom harus di isi.");
            return;
        }

        // lakukan async dan fetch data
        asyncLoginFetchData(nim, pass);
    }

    public void asyncLoginFetchData(final String nim, final String pass) {

        final String nimTrim = nim.trim();
        final String passTrim = pass.trim();

        loadLoadingProgress(true);

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

                                // simpan sesi pengguna jika sukses login
                                sessionConfig.setUserLogin(nim, pass);

                                menujuMainActivity(pembimbing1, pembimbing2);
                            } else {
                                sessionConfig.setUserLogout();
                                loadLoadingProgress(false);
                                tampilkanSnackBar("Nim atau password salah.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadLoadingProgress(false);
                            tampilkanSnackBar("Nim atau password salah.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loadLoadingProgress(false);
                        tampilkanSnackBar("Koneksi bermasalah = " + error.toString());
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void menujuMainActivity(String pembimbing1, String pembimbing2) {
        if (pembimbing1.isEmpty() && pembimbing2.isEmpty())
            FirebaseMessaging.getInstance().subscribeToTopic("pembimbing");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadLoadingProgress(boolean loading) {
        if (loading) {
            formLogin.setVisibility(View.GONE);
            formLoading.setVisibility(View.VISIBLE);
        } else {
            formLogin.setVisibility(View.VISIBLE);
            formLoading.setVisibility(View.GONE);
        }
    }

    public void tampilkanSnackBar(String isiPesan) {
        Snackbar snackbar = Snackbar.make(constraintLayoutLogin, isiPesan, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getColor(R.color.colorRed));
        snackbar.show();
    }
}
