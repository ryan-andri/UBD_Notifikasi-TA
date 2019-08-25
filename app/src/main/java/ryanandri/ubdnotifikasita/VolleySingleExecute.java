package ryanandri.ubdnotifikasita;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ryanandri.ubdnotifikasita.interfaces.JadwalCallBack;
import ryanandri.ubdnotifikasita.interfaces.LoginCallBack;
import ryanandri.ubdnotifikasita.interfaces.NilaiCallBack;
import ryanandri.ubdnotifikasita.interfaces.NotifikasiCallBack;
import ryanandri.ubdnotifikasita.session.Constant;

public class VolleySingleExecute {
    private Context context;

    public VolleySingleExecute(Context ctx) {
        this.context = ctx;
    }

    // async login activity
    public void asyncLoginFetchData(final String nim, final String pass,
                                    final LoginCallBack interfaceRespones) {

        final String nimTrim = nim.trim();
        final String passTrim = pass.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.login,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        interfaceRespones.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        interfaceRespones.onErrorLogin(error);
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

    // async login activity
    public void asyncJadwal(final String nim, final JadwalCallBack interfaceRespones) {
        final String nimTrim = nim.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.jadwal_ujian,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        interfaceRespones.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        interfaceRespones.onErrorJadwal(error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nimTrim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // async nilai fragment
    public void asyncNilai(final String nim, final NilaiCallBack interfaceRespones) {
        final String nimTrim = nim.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL+Constant.jadwal_ujian,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        interfaceRespones.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        interfaceRespones.onErrorNilai(error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nim", nimTrim);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // async nilai fragment
    public void asyncNotifikasiList(final NotifikasiCallBack interfaceRespones) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL+Constant.data_notifikasi,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        interfaceRespones.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        interfaceRespones.onErrorNotifikasi(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
