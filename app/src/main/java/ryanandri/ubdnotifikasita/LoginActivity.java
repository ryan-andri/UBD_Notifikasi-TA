package ryanandri.ubdnotifikasita;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private ConstraintLayout constraintLayoutLogin, formLogin, formLoading;
    private EditText loginNIM, loginPASS;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionConfig = SessionConfig.getInstance(this);
        if (sessionConfig.IsLogin()) menujuMainActivity();

        // set layout login
        setContentView(R.layout.activity_login);

        // buat channel untuk oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id_channel = getString(R.string.id_channel);
            String nama_channel = getString(R.string.nama_channel);
            String desc_channel = getString(R.string.deskripsi_channel);

            long[] vibrate = {0, 600};
            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.voila);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            NotificationChannel channel = new NotificationChannel(id_channel,
                    nama_channel, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(desc_channel);
            channel.setSound(uri, audioAttributes);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(vibrate);

            NotificationManager notificationMgr = getSystemService(NotificationManager.class);
            if (notificationMgr != null) {
                notificationMgr.createNotificationChannel(channel);
            }
        }

        // untuk snackbar pop-up
        constraintLayoutLogin = findViewById(R.id.constraintLayoutLogin);

        formLogin = findViewById(R.id.formLogin);
        formLoading = findViewById(R.id.loadingWidget);
        loginNIM = findViewById(R.id.loginNIM);
        loginPASS = findViewById(R.id.loginPASS);

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
                                JSONArray arrJson = jsonObject.getJSONArray("data");

                                for (int i = 0; i < arrJson.length(); i++) {
                                    jsonObject = arrJson.getJSONObject(i);
                                    // data untuk profile
                                    sessionConfig.setNamaMHS(jsonObject.getString("nama_mhs"));
                                    sessionConfig.setJumlahSKS(jsonObject.getInt("total_sks"));
                                    sessionConfig.setPembimbing1(jsonObject.getString("nama_pbb1"));
                                    sessionConfig.setPembimbing2(jsonObject.getString("nama_pbb2"));

                                    // data untuk judul
                                    sessionConfig.setJudul(jsonObject.getString("judul_1"),
                                            jsonObject.getString("judul_2"),
                                            jsonObject.getString("judul_3"));
                                }

                                // simpan sesi pengguna jika sukses login
                                sessionConfig.setUserLogin(nim, pass);

                                menujuMainActivity();
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
                params.put("nim_mhs", nimTrim);
                params.put("password", passTrim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void menujuMainActivity() {
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
