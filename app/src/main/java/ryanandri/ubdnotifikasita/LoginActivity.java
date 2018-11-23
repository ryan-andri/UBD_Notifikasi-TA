package ryanandri.ubdnotifikasita;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ryanandri.ubdnotifikasita.session.SessionConfig;

public class LoginActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private EditText loginNIM;
    private EditText loginPASS;
    private Button btnLogin;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = findViewById(R.id.CoordinatorLayout);

        loginNIM = findViewById(R.id.loginNIM);
        loginPASS = findViewById(R.id.loginPASS);

        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // avoid double click!
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                            return;
                        mLastClickTime = SystemClock.elapsedRealtime();
                        lakukanLogin();
                    }
                }
        );
    }

    public void lakukanLogin() {
        String NIM = loginNIM.getText().toString();
        String PASS = loginPASS.getText().toString();

        if (NIM.isEmpty() || PASS.isEmpty()) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Semua kolom harus di isi !", Snackbar.LENGTH_SHORT);
            View snackView = snackbar.getView();
            snackView.setBackgroundColor(getResources().getColor(R.color.colorRed));
            snackbar.show();
            return;
        }
        SessionConfig sessionConfig = SessionConfig.getInstance(this);
        sessionConfig.setUserLogin(NIM, PASS);
        menujuMainActivity();
    }

    public void menujuMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
