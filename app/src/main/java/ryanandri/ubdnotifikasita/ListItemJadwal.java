package ryanandri.ubdnotifikasita;

public class ListItemJadwal {
    private String headJadwal;
    private String tglJadwal, wktJadwal, ruangJadwal, penguji1Jadwal, penguji2Jadwal;

    public ListItemJadwal(String headJadwal, String tglJadwal,
                          String wktJadwal, String ruangJadwal,
                          String penguji1Jadwal, String getPenguji2Jadwal) {
        this.headJadwal = headJadwal;
        this.tglJadwal = tglJadwal;
        this.wktJadwal = wktJadwal;
        this.ruangJadwal = ruangJadwal;
        this.penguji1Jadwal = penguji1Jadwal;
        this.penguji2Jadwal = getPenguji2Jadwal;
    }

    public String getHeadJadwal() {
        return headJadwal;
    }

    public String getTglJadwal() {
        return tglJadwal;
    }

    public String getWktJadwal() {
        return wktJadwal;
    }

    public String getRuangJadwal() {
        return ruangJadwal;
    }

    public String getPenguji1Jadwal() {
        return penguji1Jadwal;
    }

    public String getPenguji2Jadwal() {
        return penguji2Jadwal;
    }
}
