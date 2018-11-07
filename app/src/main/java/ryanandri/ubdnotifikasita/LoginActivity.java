package ryanandri.ubdnotifikasita;

import android.content.Intent;
import android.os.Bundle;
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

    private SessionConfig sessionConfig;

    private CoordinatorLayout coordinatorLayout;
    private EditText loginNIM;
    private EditText loginPASS;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionConfig = SessionConfig.getInstance(getApplicationContext());
        if (sessionConfig.IsLogin()) {
            menujuMainActivity();
        }

        coordinatorLayout = findViewById(R.id.CoordinatorLayout);

        loginNIM = findViewById(R.id.loginNIM);
        loginPASS = findViewById(R.id.loginPASS);

        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        sessionConfig.setUserLogin(NIM, PASS);
        menujuMainActivity();
    }

    public void menujuMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
