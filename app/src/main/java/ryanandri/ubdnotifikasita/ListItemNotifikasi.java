package ryanandri.ubdnotifikasita;

public class ListItemNotifikasi {
    private String headNotifikasi;
    private String tglNotif, isiNotif;

    public ListItemNotifikasi(String headNotifikasi, String tglNotif, String isiNotif) {
        this.headNotifikasi = headNotifikasi;
        this.tglNotif = tglNotif;
        this.isiNotif = isiNotif;
    }

    public String getHeadNotifikasi() {
        return headNotifikasi;
    }

    public String getTglNotif() {
        return tglNotif;
    }

    public String getIsiNotif() {
        return isiNotif;
    }
}