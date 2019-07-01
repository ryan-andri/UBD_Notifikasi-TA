package ryanandri.ubdnotifikasita.firebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.RemoteMessage;

import ryanandri.ubdnotifikasita.ExpandNotif;
import ryanandri.ubdnotifikasita.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String tanggal = remoteMessage.getData().get("tanggal");
        String isi = remoteMessage.getData().get("isi");
        TampilkanNotifikasi(title, isi, tanggal);
    }

    public void TampilkanNotifikasi(String title, String Isi, String tanggal) {
        Intent intent = new Intent(getApplicationContext(), ExpandNotif.class);
        intent.putExtra("head", title);
        intent.putExtra("tgl", tanggal);
        intent.putExtra("isi", Isi);
        intent.putExtra("key_notif", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundNotif = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.castor);
        String id_channel = getString(R.string.id_channel);
        long[] vibrate = {0, 600};
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this, id_channel)
                        .setSmallIcon(R.drawable.ic_notifikasi_black_24dp)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(Isi)
                        .setContentIntent(pendingIntent)
                        .setSound(soundNotif)
                        .setVibrate(vibrate)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, nBuilder.build());
    }
}
