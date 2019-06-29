package ryanandri.ubdnotifikasita.firebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ryanandri.ubdnotifikasita.R;

public class ExpandNotifikasiFCM extends AppCompatActivity {
    private TextView Head, Tgl, Isi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_notif);

        Head = findViewById(R.id.head);
        Tgl = findViewById(R.id.tgl);
        Isi = findViewById(R.id.isi);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if ((bundle.getInt("key_notif") == 1)) {
                foregroundNotif();
            } else {
                backgroundNotif();
            }
        }
    }

    public void foregroundNotif() {
        Bundle bundle = getIntent().getExtras();
        String head = bundle.getString("head");
        String tgl = bundle.getString("tgl");
        String isi = bundle.getString("isi");

        Head.setText(head);
        Tgl.setText(tgl);
        Isi.setText(isi);
    }

    public void backgroundNotif() {
        for (String key : getIntent().getExtras().keySet()) {
            if (key.equals("head")) {
                String head = getIntent().getExtras().getString(key);
                Head.setText(head);
            } else if (key.equals("tgl")) {
                String tgl = getIntent().getExtras().getString(key);
                Tgl.setText(tgl);
            } else if (key.equals("body")){
                String isi = getIntent().getExtras().getString(key);
                Isi.setText(isi);
            }
        }
    }
}