package ryanandri.ubdnotifikasita;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ExpandNotif extends AppCompatActivity {
    private TextView Head, Tgl, Isi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_notif);

        Head = findViewById(R.id.head);
        Tgl = findViewById(R.id.tgl);
        Isi = findViewById(R.id.isi);

        Bundle bundle = getIntent().getExtras();
        String head = bundle.getString("head");
        String tgl = bundle.getString("tgl");
        String isi = bundle.getString("isi");

        Head.setText(head);
        Tgl.setText(tgl);
        Isi.setText(isi);
    }
}
