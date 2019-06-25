package ryanandri.ubdnotifikasita.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.session.Constant;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class JudulFragment extends Fragment {
    private SessionConfig sessionConfig;

    private CoordinatorLayout snackJudul;
    private ConstraintLayout formJudul, arsipJudul, loadingJudul;
    private EditText judul1, judul2, judul3;

    private TextView arsipJudul1, arsipJudul2, arsipJudul3;
    private String arsipJ1, arsipJ2, arsipJ3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judul, null);

        // Parent dengan 2 layout berbeda
        formJudul = view.findViewById(R.id.formJudul);
        arsipJudul = view.findViewById(R.id.arsipJudul);
        loadingJudul =  view.findViewById(R.id.loadingJudul);

        // Snackbar popup
        snackJudul = view.findViewById(R.id.snackJudul);

        judul1 = view.findViewById(R.id.inputJudul1);
        judul2 = view.findViewById(R.id.inputJudul2);
        judul3 = view.findViewById(R.id.inputJudul3);

        arsipJudul1 = view.findViewById(R.id.arsipJudul1);
        arsipJudul2 = view.findViewById(R.id.arsipJudul2);
        arsipJudul3 = view.findViewById(R.id.arsipJudul3);

        sessionConfig = SessionConfig.getInstance(getContext());

        arsipJ1 = sessionConfig.getJudul1();
        arsipJ2 = sessionConfig.getJudul2();
        arsipJ3 = sessionConfig.getJudul3();

        if (!arsipJ1.isEmpty() && !arsipJ2.isEmpty() && !arsipJ3.isEmpty()) {
            formJudul.setVisibility(View.GONE);
            arsipJudul.setVisibility(View.VISIBLE);
            loadFormArsipJudul();
        }

        Button kirimJudul = view.findViewById(R.id.kirimJudul);
        kirimJudul.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { loadFormPengajuanJudul(); }
                }
        );

        return view;
    }

    public void loadFormPengajuanJudul() {
        final String nim = sessionConfig.getNIM();
        final String nama = sessionConfig.getNamaMHS();
        final String pembimbing1 = sessionConfig.getPembimbing1();
        final String pembimbing2 = sessionConfig.getPembimbing2();

        final String J1 = judul1.getText().toString();
        final String J2 = judul2.getText().toString();
        final String J3 = judul3.getText().toString();

        if (J1.isEmpty() || J2.isEmpty() || J3.isEmpty()) {
            tampilkanSnackBar("Semua kolom harus di isi !");
            return;
        }

        if (pembimbing1.isEmpty() && pembimbing2.isEmpty()) {
            tampilkanSnackBar("Tidak dapat mengirim karna anda belum mendapatkan pembimbing!");
            return;
        }

        formJudul.setVisibility(View.GONE);
        loadingJudul.setVisibility(View.VISIBLE);
        pushDataPengajuanJudul(nim, nama, J1, J2, J3);
    }

    public void pushDataPengajuanJudul(final String nim, final String nama, final String judul1,
                                       final String judul2, final String judul3) {

        final String nimTrim = nim.trim();
        final String namaTrim = nama.trim();
        final String judul1Trim = judul1.trim();
        final String judul2Trim = judul2.trim();
        final String judul3Trim = judul3.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.ajukan_judul,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                loadingJudul.setVisibility(View.GONE);
                                arsipJudul.setVisibility(View.VISIBLE);
                                tampilkanSnackBar("Data berhasil di masukan!");
                                sessionConfig.setJudul(judul1Trim, judul2Trim, judul3Trim);
                                loadFormArsipJudul();
                            } else {
                                formJudul.setVisibility(View.VISIBLE);
                                loadingJudul.setVisibility(View.GONE);
                                tampilkanSnackBar("ada kesalahan!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            formJudul.setVisibility(View.VISIBLE);
                            loadingJudul.setVisibility(View.GONE);
                            tampilkanSnackBar("ada kesalahan!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        formJudul.setVisibility(View.VISIBLE);
                        loadingJudul.setVisibility(View.GONE);
                        tampilkanSnackBar("Server tidak merespon!");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nimTrim);
                params.put("nama", namaTrim);
                params.put("judul1", judul1Trim);
                params.put("judul2", judul2Trim);
                params.put("judul3", judul3Trim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void loadFormArsipJudul() {
        arsipJudul1.setText(arsipJ1);
        arsipJudul2.setText(arsipJ2);
        arsipJudul3.setText(arsipJ3);
    }

    public void tampilkanSnackBar(String isiPesan) {
        Snackbar snackbar = Snackbar.make(snackJudul, isiPesan, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.colorRed));
        snackbar.show();
    }
}
