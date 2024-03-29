package ryanandri.ubdnotifikasita.session;

import android.annotation.SuppressLint;
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

    // pembimbing
    private static String PEMBIMBING1 = "pembimbing1";
    private static String PEMBIMBING2 = "pembimbing2";
    private static String JUDUL_PENELITIAN = "judul_penelitian";

    // jadwal proposal
    private static String TGL_UP = "tanggal_up";
    private static String WAKTU_UP = "waktu_up";
    private static String RUANG_UP = "ruang_up";
    private static String PENGUJI1_UP = "penguji1_up";
    private static String PENGUJI2_UP = "penguji2_up";
    private static String NILAI_UP = "nilai_up";

    // jadwal Komprehensif
    private static String TGL_KOMPRE = "tanggal_kompre";
    private static String WAKTU_KOMPRE = "waktu_kompre";
    private static String RUANG_KOMPRE = "ruang_kompre";
    private static String PENGUJI1_KOMPRE = "penguji1_kompre";
    private static String PENGUJI2_KOMPRE = "penguji2_kompre";
    private static String NILAI_KOMPRE = "nilai_kompre";

    private static String PELAKSANAAN_UP = "pelaksanaan_up";
    private static String PELAKSANAAN_KOMPRE = "pelaksanaan_kompre";

    private static String FIREBASE_TOKEN = "firebase_token";

    private SessionConfig() {}

    @SuppressLint("CommitPrefEdits")
    public static SessionConfig getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SESSION_CONFIG, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        return sessionConfig;
    }

    // START Login User
    public void setUserLogin(String nim, String password) {
        editor.putString(NIM, nim);
        editor.putString(PASSWORD, password);
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public boolean IsLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setUserLogout() {
        editor.clear();
        editor.commit();
    }
    // END Login User

    // get nama nim dan passowrd
    public String getNamaMHS() {
        return sharedPreferences.getString(NAMA_MHS, "");
    }
    public void setNamaMHS(String namaMhs) {
        editor.putString(NAMA_MHS, namaMhs);
        editor.commit();
    }

    public String getNIM() {
        return sharedPreferences.getString(NIM, "");
    }

    public String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    // set dan get nama pembimbing 1 dan 2
    public void setPembimbing1(String pbb1) {
        editor.putString(PEMBIMBING1, pbb1);
        editor.commit();
    }

    public void setPembimbing2(String pbb2) {
        editor.putString(PEMBIMBING2, pbb2);
        editor.commit();
    }

    public void setJudulPenelitian(String judul) {
        editor.putString(JUDUL_PENELITIAN, judul);
        editor.commit();
    }

    public String getJudulPenelitian() {
        return sharedPreferences.getString(JUDUL_PENELITIAN, "");
    }

    public String getPembimbing1() {
        return sharedPreferences.getString(PEMBIMBING1, "");
    }

    public String getPembimbing2() {
        return sharedPreferences.getString(PEMBIMBING2, "");
    }

    // set dan get data jadwal proposal
    public void setJadwalUP(String tgl, String waktu, String ruang,
                                   String penguji1, String penguji2) {
        editor.putString(TGL_UP, tgl);
        editor.putString(WAKTU_UP, waktu);
        editor.putString(RUANG_UP, ruang);
        editor.putString(PENGUJI1_UP, penguji1);
        editor.putString(PENGUJI2_UP, penguji2);
        editor.commit();
    }

    public String getTglUP() { return sharedPreferences.getString(TGL_UP, ""); }

    public void setNilaiUp(String nilaiUp) {
        editor.putString(NILAI_UP, nilaiUp);
        editor.commit();
    }

    public String getNilaiUP() { return sharedPreferences.getString(NILAI_UP, ""); }

    // set dan get data jadwal Komprehensif
    public void setJadwalKOMPRE(String tgl, String waktu, String ruang,
                                       String penguji1, String penguji2) {
        editor.putString(TGL_KOMPRE, tgl);
        editor.putString(WAKTU_KOMPRE, waktu);
        editor.putString(RUANG_KOMPRE, ruang);
        editor.putString(PENGUJI1_KOMPRE, penguji1);
        editor.putString(PENGUJI2_KOMPRE, penguji2);
        editor.commit();
    }

    public String getTglKOMPRE() { return sharedPreferences.getString(TGL_KOMPRE, ""); }

    public void setNilaiKompre(String nilaiKompre) {
        editor.putString(NILAI_KOMPRE, nilaiKompre);
        editor.commit();
    }

    public String getNilaiKompre() { return sharedPreferences.getString(NILAI_KOMPRE, ""); }


    public void setPelaksanaanUP(String pelaksanaan) {
        editor.putString(PELAKSANAAN_UP, pelaksanaan);
        editor.commit();
    }
    public String getPelaksanaanUP() { return sharedPreferences.getString(PELAKSANAAN_UP, ""); }

    public void setPelaksanaanKompre(String pelaksanaan) {
        editor.putString(PELAKSANAAN_KOMPRE, pelaksanaan);
        editor.commit();
    }
    public String getPelaksanaanKompre() { return sharedPreferences.getString(PELAKSANAAN_KOMPRE, ""); }

    public void simpanFbToken(String token) {
        editor.putString(FIREBASE_TOKEN, token);
        editor.commit();
    }

    public String ambilFbToken() { return sharedPreferences.getString(FIREBASE_TOKEN, ""); }
}
