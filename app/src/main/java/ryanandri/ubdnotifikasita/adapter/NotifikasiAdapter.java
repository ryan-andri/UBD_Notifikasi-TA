package ryanandri.ubdnotifikasita.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ryanandri.ubdnotifikasita.ExpandNotif;
import ryanandri.ubdnotifikasita.ListItemNotifikasi;
import ryanandri.ubdnotifikasita.LoginActivity;
import ryanandri.ubdnotifikasita.R;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private List<ListItemNotifikasi> listItemNotifikasis;
    private Context context;

    public NotifikasiAdapter(List<ListItemNotifikasi> listItemNotifikasis, Context context) {
        this.listItemNotifikasis = listItemNotifikasis;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_notifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItemNotifikasi listItemNotifikasi = listItemNotifikasis.get(position);

        holder.headNotif.setText(listItemNotifikasi.getHeadNotifikasi());
        holder.tglNotif.setText(listItemNotifikasi.getTglNotif());
        holder.isiNotif.setText(listItemNotifikasi.getIsiNotif());

        holder.linearLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String headNotif = listItemNotifikasi.getHeadNotifikasi();
                        String tanggalNotif = listItemNotifikasi.getTglNotif();
                        String isiNotifikasi = listItemNotifikasi.getIsiNotif();

                        Intent intent = new Intent(context, ExpandNotif.class);
                        intent.putExtra("head", headNotif);
                        intent.putExtra("tgl", tanggalNotif);
                        intent.putExtra("isi", isiNotifikasi);
                        context.startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return listItemNotifikasis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView headNotif;
        public TextView tglNotif;
        public TextView isiNotif;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            headNotif = itemView.findViewById(R.id.headNotif);
            tglNotif = itemView.findViewById(R.id.tanggalNotif);
            isiNotif = itemView.findViewById(R.id.isiNotif);

            linearLayout = itemView.findViewById(R.id.notifikasiLinierLayout);
        }
    }

}
