package ryanandri.ubdnotifikasita.interfaces;

import com.android.volley.VolleyError;

public interface LoginCallBack {
    void onSuccess(String result);
    void onErrorLogin(VolleyError error);
}
