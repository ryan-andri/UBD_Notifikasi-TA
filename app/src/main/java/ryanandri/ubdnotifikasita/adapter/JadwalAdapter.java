package ryanandri.ubdnotifikasita.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ryanandri.ubdnotifikasita.ListItemJadwal;
import ryanandri.ubdnotifikasita.R;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolderJadwal> {

    private List<ListItemJadwal> listItemJadwals;
    private Context context;

    public JadwalAdapter(List<ListItemJadwal> listItemJadwals, Context context) {
        this.listItemJadwals = listItemJadwals;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderJadwal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_jadwal, parent, false);
        return new ViewHolderJadwal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJadwal holder, int position) {
        ListItemJadwal listItemJadwal = listItemJadwals.get(position);

        holder.headJadwal.setText(listItemJadwal.getHeadJadwal());
        holder.tglJadwal.setText(listItemJadwal.getTglJadwal());
        holder.wktJadwal.setText(listItemJadwal.getWktJadwal());
        holder.ruangJadwal.setText(listItemJadwal.getRuangJadwal());
        holder.penguji1Jadwal.setText(listItemJadwal.getPenguji1Jadwal());
        holder.penguji2Jadwal.setText(listItemJadwal.getPenguji2Jadwal());
    }

    @Override
    public int getItemCount() {
        return listItemJadwals.size();
    }

    public class ViewHolderJadwal extends RecyclerView.ViewHolder {
        private TextView headJadwal, tglJadwal, wktJadwal,
                ruangJadwal, penguji1Jadwal, penguji2Jadwal;

        public ViewHolderJadwal(View itemView) {
            super(itemView);
            headJadwal = itemView.findViewById(R.id.headJadwal);
            tglJadwal = itemView.findViewById(R.id.tglJadwal);
            wktJadwal = itemView.findViewById(R.id.waktuJadwal);
            ruangJadwal = itemView.findViewById(R.id.ruangJadwal);
            penguji1Jadwal = itemView.findViewById(R.id.penguji1Jadwal);
            penguji2Jadwal = itemView.findViewById(R.id.penguji2Jadwal);
        }
    }
}
