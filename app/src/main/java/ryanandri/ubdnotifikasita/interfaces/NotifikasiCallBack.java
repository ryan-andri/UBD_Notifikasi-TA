package ryanandri.ubdnotifikasita.interfaces;

import com.android.volley.VolleyError;

public interface NotifikasiCallBack {
    void onSuccess(String result);
    void onErrorNotifikasi(VolleyError error);
}
