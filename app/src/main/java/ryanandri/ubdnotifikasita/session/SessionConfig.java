package ryanandri.ubdnotifikasita.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionConfig {
    private static SessionConfig sessionConfig  = new SessionConfig();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String SESSION_CONFIG = "UserSession";
    private static String IS_LOGIN = "UserLoged";

    // nim dan password/sks
    private static String NIM = "nim";
    private static String NAMA_MHS = "nama_mahasiswa";
    private static String PASSWORD = "password";
    private static String JML_SKS = "sks";

    // pembimbing
    private static String PEMBIMBING1 = "pembimbing1";
    private static String PEMBIMBING2 = "pembimbing2";

    // Judul
    private static String JUDUL1 = "judul1";
    private static String JUDUL2 = "judul2";
    private static String JUDUL3 = "judul3";

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
        editor.putString(PASSWORD, password);
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

    // get nama nim dan passowrd
    public static String getNamaMHS() {
        return sharedPreferences.getString(NAMA_MHS, "");
    }
    public static void setNamaMHS(String namaMhs) {
        editor.putString(NAMA_MHS, namaMhs);
        editor.commit();
    }

    public static String getNIM() {
        return sharedPreferences.getString(NIM, "");
    }

    public static String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    // set dan get nama pembimbing 1 dan 2
    public static void setPembimbing1(String pbb1) {
        editor.putString(PEMBIMBING1, pbb1);
        editor.commit();
    }

    public static void setPembimbing2(String pbb2) {
        editor.putString(PEMBIMBING2, pbb2);
        editor.commit();
    }

    public static String getPembimbing1() {
        return sharedPreferences.getString(PEMBIMBING1, "");
    }

    public static String getPembimbing2() {
        return sharedPreferences.getString(PEMBIMBING2, "");
    }

    // set dan get jumlah sks
    public static void setJumlahSKS(int sks) {
        editor.putInt(JML_SKS, sks);
        editor.commit();
    }

    public static int getJumlahSKS() {
       return sharedPreferences.getInt(JML_SKS, 0);
    }

    // set dan get pengajuan judul
    public static void setJudul(String judul1, String judul2, String judul3) {
        editor.putString(JUDUL1, judul1);
        editor.putString(JUDUL2, judul2);
        editor.putString(JUDUL3, judul3);
        editor.commit();
    }

    public static String getJudul1() {
        return sharedPreferences.getString(JUDUL1, "");
    }

    public static String getJudul2() {
        return sharedPreferences.getString(JUDUL2, "");
    }

    public static String getJudul3() {
        return sharedPreferences.getString(JUDUL3, "");
    }
}
