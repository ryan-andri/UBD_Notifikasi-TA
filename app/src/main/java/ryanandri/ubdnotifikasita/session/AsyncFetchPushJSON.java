package ryanandri.ubdnotifikasita.session;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AsyncFetchPushJSON {
    private Context context;
    private SessionConfig sessionConfig;

    public AsyncFetchPushJSON (Context context) {
        this.context = context;
    }

    public void SyncLogin(final String nim, final String pass) {
        sessionConfig = SessionConfig.getInstance(context);

        final String nimTrim = nim.trim();
        final String passTrim = pass.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.login,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        String namaMhs = "";
                        int jmlSks = 0;
                        String pembimbing1 = "", pembimbing2 = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
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

                                if (!pembimbing1.isEmpty())
                                    sessionConfig.setPembimbing1(pembimbing1);
                                if (!pembimbing2.isEmpty())
                                    sessionConfig.setPembimbing2(pembimbing2);

                                // simpan sesi pengguna jika sukses login
                                sessionConfig.setUserLogin(nim, pass);
                            } else {
                                Toast.makeText(context, "Nim atau password salah.", Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Server tidak merespone.", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error request = " +error.toString(), Toast.LENGTH_SHORT);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
