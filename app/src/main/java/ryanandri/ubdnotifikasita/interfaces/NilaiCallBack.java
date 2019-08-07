package ryanandri.ubdnotifikasita.interfaces;

import com.android.volley.VolleyError;

public interface NilaiCallBack {
    void onSuccess(String result);
    void onErrorNilai(VolleyError error);
}
