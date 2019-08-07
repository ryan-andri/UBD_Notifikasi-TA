package ryanandri.ubdnotifikasita.interfaces;

import com.android.volley.VolleyError;

public interface JadwalCallBack {
    void onSuccess(String result);
    void onErrorJadwal(VolleyError error);
}
