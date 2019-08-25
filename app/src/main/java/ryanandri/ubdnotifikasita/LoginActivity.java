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

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.session.SessionConfig;
import ryanandri.ubdnotifikasita.interfaces.LoginCallBack;

public class LoginActivity extends AppCompatActivity {
    private VolleySingleExecute volleySingleExecute;
    private SessionConfig sessionConfig;
    private ConstraintLayout constraintLayoutLogin, formLogin, formLoading;
    private EditText loginNIM, loginPASS;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleExecute = new VolleySingleExecute(this);

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
            antrianLogin(nimSesi, passSesi);
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

        antrianLogin(nim, pass);
    }

    public void antrianLogin(final String nim, final String pass) {
        loadLoadingProgress(true);
        volleySingleExecute.asyncLoginFetchData(nim, pass, new LoginCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        String namaMhs = "", pembimbing1 = "", pembimbing2 = "";
                        String judul_penelitian = "";
                        JSONArray arrJson = jsonObject.getJSONArray("data_login");
                        for (int i = 0; i < arrJson.length(); i++) {
                            jsonObject = arrJson.getJSONObject(i);
                            namaMhs = jsonObject.getString("nama");
                            judul_penelitian = jsonObject.getString("judul_penelitian");
                            pembimbing1 = jsonObject.getString("pembimbing_1");
                            pembimbing2 = jsonObject.getString("pembimbing_2");
                        }

                        sessionConfig.setNamaMHS(namaMhs);
                        sessionConfig.setJudulPenelitian(judul_penelitian);
                        sessionConfig.setPembimbing1(pembimbing1);
                        sessionConfig.setPembimbing2(pembimbing2);

                        // simpan sesi pengguna jika sukses login
                        sessionConfig.setUserLogin(nim, pass);

                        menujuMainActivity();
                    } else {
                        sessionConfig.setUserLogout();
                        loadLoadingProgress(false);
                        tampilkanSnackBar("Nim atau Password salah, silahkan hubungi PPM.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadLoadingProgress(false);
                    tampilkanSnackBar("Nim atau Password salah, silahkan hubungi PPM.");
                }
            }

            @Override
            public void onErrorLogin(VolleyError error) {
                error.printStackTrace();
                loadLoadingProgress(false);
            }
        });
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
