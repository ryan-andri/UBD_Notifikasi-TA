package ryanandri.ubdnotifikasita.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionConfig {
    private static SessionConfig sessionConfig  = new SessionConfig();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String SESSION_CONFIG = "UserSession";
    private static String IS_LOGIN = "UserLoged";

    // nim dan password
    private static String NIM = "nim";
    private static String PASSWORD = "password";

    private SessionConfig() {}

    public static SessionConfig getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SESSION_CONFIG, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        return sessionConfig;
    }

    // START Login User
    public static void setUserLogin(String nim, String password) {
        editor.putString(NIM, nim);
        editor.putString(PASSWORD, nim);
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public static boolean IsLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setUserLogout() {
        editor.clear();
        editor.commit();
    }
    // END Login User

    public static String getNIM() {
        return sharedPreferences.getString(NIM, "");
    }

    public static String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD, "");
    }
}
