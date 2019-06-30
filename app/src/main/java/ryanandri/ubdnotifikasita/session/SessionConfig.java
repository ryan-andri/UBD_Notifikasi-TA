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

    // jadwal proposal
    private static String TGL_UP = "tanggal_up";
    private static String WAKTU_UP = "waktu_up";
    private static String RUANG_UP = "ruang_up";
    private static String PENGUJI1_UP = "penguji1_up";
    private static String PENGUJI2_UP = "penguji2_up";

    // jadwal proposal
    private static String TGL_KOMPRE = "tanggal_kompre";
    private static String WAKTU_KOMPRE = "waktu_kompre";
    private static String RUANG_KOMPRE = "ruang_kompre";
    private static String PENGUJI1_KOMPRE = "penguji1_kompre";
    private static String PENGUJI2_KOMPRE = "penguji2_kompre";

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

    public static void deleteDataJudul() {
        editor.remove(JUDUL1);
        editor.remove(JUDUL2);
        editor.remove(JUDUL3);
        editor.commit();
    }

    // set dan get data jadwal proposal
    public static void setJadwalUP(String tgl, String waktu, String ruang, String penguji1, String penguji2) {
        editor.putString(TGL_UP, tgl);
        editor.putString(WAKTU_UP, waktu);
        editor.putString(RUANG_UP, ruang);
        editor.putString(PENGUJI1_UP, penguji1);
        editor.putString(PENGUJI2_UP, penguji2);
        editor.commit();
    }

    public static void deleteJadwalUP() {
        editor.remove(TGL_UP);
        editor.remove(WAKTU_UP);
        editor.remove(RUANG_UP);
        editor.remove(PENGUJI1_UP);
        editor.remove(PENGUJI2_UP);
        editor.commit();
    }

    public static String getTglUP() {
        return sharedPreferences.getString(TGL_UP, "");
    }
    public static String getWktUP() {
        return sharedPreferences.getString(WAKTU_UP, "");
    }
    public static String getRuangUP() {
        return sharedPreferences.getString(RUANG_UP, "");
    }
    public static String getPengji1UP() {
        return sharedPreferences.getString(PENGUJI1_UP, "");
    }
    public static String getPengji2UP() {
        return sharedPreferences.getString(PENGUJI2_UP, "");
    }

    // set dan get data jadwal proposal
    public static void setJadwalKOMPRE(String tgl, String waktu, String ruang, String penguji1, String penguji2) {
        editor.putString(TGL_KOMPRE, tgl);
        editor.putString(WAKTU_KOMPRE, waktu);
        editor.putString(RUANG_KOMPRE, ruang);
        editor.putString(PENGUJI1_KOMPRE, penguji1);
        editor.putString(PENGUJI2_KOMPRE, penguji2);
        editor.commit();
    }

    public static String getTglKOMPRE() {
        return sharedPreferences.getString(TGL_KOMPRE, "");
    }
    public static String getWktKOMPRE() {
        return sharedPreferences.getString(WAKTU_KOMPRE, "");
    }
    public static String getRuangKOMPRE() {
        return sharedPreferences.getString(RUANG_KOMPRE, "");
    }
    public static String getPengji1KOMPRE() {
        return sharedPreferences.getString(PENGUJI1_KOMPRE, "");
    }
    public static String getPengji2KOMPRE() {
        return sharedPreferences.getString(PENGUJI2_KOMPRE, "");
    }
}
