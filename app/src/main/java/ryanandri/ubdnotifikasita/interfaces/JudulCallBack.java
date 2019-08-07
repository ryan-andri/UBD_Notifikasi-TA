package ryanandri.ubdnotifikasita.interfaces;

import com.android.volley.VolleyError;

public interface JudulCallBack {
    void onSuccess(String result);
    void onErrorJudul(VolleyError error);
}
