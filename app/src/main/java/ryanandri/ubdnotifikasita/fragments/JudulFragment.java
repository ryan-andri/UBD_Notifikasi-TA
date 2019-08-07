package ryanandri.ubdnotifikasita.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.R;
import ryanandri.ubdnotifikasita.VolleySingleExecute;
import ryanandri.ubdnotifikasita.interfaces.JudulCallBack;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class JudulFragment extends Fragment {
    private SessionConfig sessionConfig;

    private ConstraintLayout snackJudul;
    private ConstraintLayout formJudul, loadingJudul;
    private CardView arsipJudul;
    private EditText judul1, judul2, judul3;

    private TextView arsipJudul1, arsipJudul2, arsipJudul3;

    private VolleySingleExecute volleySingleExecute;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_judul, null);

        context = view.getContext();

        volleySingleExecute = new VolleySingleExecute(view.getContext());

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

        // resync data pengajuan judul
        sessionConfig = SessionConfig.getInstance(view.getContext());
        String nim = sessionConfig.getNIM();
        synDataJudul(nim, "", "", "", false);

        Button kirimJudul = view.findViewById(R.id.kirimJudul);
        kirimJudul.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadFormPengajuanJudul();
                    }
                }
        );

        return view;
    }

    private void loadFormPengajuanJudul() {
        final String nim = sessionConfig.getNIM();
        final String pembimbing1 = sessionConfig.getPembimbing1();
        final String pembimbing2 = sessionConfig.getPembimbing2();
        final int jmlSks = sessionConfig.getJumlahSKS();

        final String J1 = judul1.getText().toString();
        final String J2 = judul2.getText().toString();
        final String J3 = judul3.getText().toString();

        if (J1.isEmpty() || J2.isEmpty() || J3.isEmpty()) {
            tampilkanSnackBar("Semua kolom harus di isi !", context);
            return;
        }

        if ((pembimbing1.isEmpty() && pembimbing2.isEmpty()) || jmlSks < 134) {
            tampilkanSnackBar("Tidak dapat mengirim karna anda belum memenuhi syarat!", context);
            return;
        }

        formJudul.setVisibility(View.GONE);
        loadingJudul.setVisibility(View.VISIBLE);
        synDataJudul(nim, J1, J2, J3, true);
    }

    private void synDataJudul(final String nim, final String judul1,
                              final String judul2, final String judul3,
                              final boolean input) {

        final String nimTrim = nim.trim();
        final String judul1Trim = judul1.trim();
        final String judul2Trim = judul2.trim();
        final String judul3Trim = judul3.trim();

        volleySingleExecute.judulSync(nimTrim, input, judul1Trim, judul2Trim, judul3Trim, new JudulCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");

                    if (input) {
                        if (success.equals("1")) {
                            loadingJudul.setVisibility(View.GONE);
                            arsipJudul.setVisibility(View.VISIBLE);
                            tampilkanSnackBar("Judul berhasil dikirim!", context);
                            sessionConfig.setJudul(judul1Trim, judul2Trim, judul3Trim);
                            loadFormArsipJudul();
                            FirebaseMessaging.getInstance().subscribeToTopic("jadwal_up");
                        } else {
                            formJudul.setVisibility(View.VISIBLE);
                            loadingJudul.setVisibility(View.GONE);
                            tampilkanSnackBar("ada kesalahan!", context);
                        }
                    } else {
                        if (success.equals("1")) {
                            String judul1 = "", judul2 = "", judul3 = "";
                            JSONArray arrJson = jsonObject.getJSONArray("judul");
                            for (int i = 0; i < arrJson.length(); i++) {
                                jsonObject = arrJson.getJSONObject(i);
                                judul1 = jsonObject.getString("judul_1");
                                judul2 = jsonObject.getString("judul_2");
                                judul3 = jsonObject.getString("judul_3");
                            }
                            if (!judul1.isEmpty() && !judul2.isEmpty() && !judul3.isEmpty()) {
                                sessionConfig.setJudul(judul1, judul2, judul3);
                                FirebaseMessaging.getInstance().subscribeToTopic("jadwal_up");
                                formJudul.setVisibility(View.GONE);
                                arsipJudul.setVisibility(View.VISIBLE);
                            } else {
                                sessionConfig.deleteDataJudul();
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("jadwal_up");
                            }
                            loadFormArsipJudul();
                        } else {
                            sessionConfig.deleteDataJudul();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (input) {
                        formJudul.setVisibility(View.VISIBLE);
                        loadingJudul.setVisibility(View.GONE);
                        tampilkanSnackBar("ada kesalahan!", context);
                    } else {
                        sessionConfig.deleteDataJudul();
                    }
                }
            }

            @Override
            public void onErrorJudul(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    private void loadFormArsipJudul() {
        String arsipJ1 = sessionConfig.getJudul1();
        String arsipJ2 = sessionConfig.getJudul2();
        String arsipJ3 = sessionConfig.getJudul3();

        if (!arsipJ1.isEmpty() && !arsipJ2.isEmpty() && !arsipJ3.isEmpty()) {
            arsipJudul1.setText(arsipJ1);
            arsipJudul2.setText(arsipJ2);
            arsipJudul3.setText(arsipJ3);
        }
    }

    private void tampilkanSnackBar(String isiPesan, Context context) {
        Snackbar snackbar = Snackbar.make(snackJudul, isiPesan, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(context.getColor(R.color.colorRed));
        snackbar.show();
    }
}
