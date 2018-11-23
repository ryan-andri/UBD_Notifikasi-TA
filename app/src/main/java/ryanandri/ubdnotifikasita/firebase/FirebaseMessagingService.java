package ryanandri.ubdnotifikasita.firebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ryanandri.ubdnotifikasita.ExpandNotif;
import ryanandri.ubdnotifikasita.MainActivity;
import ryanandri.ubdnotifikasita.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            String Title = remoteMessage.getNotification().getTitle();
            String Isi = remoteMessage.getNotification().getBody();
            String Tgl = "01/01/0111";

            String click_action =  remoteMessage.getNotification().getClickAction();
            TampilkanNotifikasi(Title, Isi, Tgl, click_action);
        }
    }

    public void TampilkanNotifikasi(String title, String Isi, String tanggal, String click_action) {
        Intent intent;
        if (click_action.equals("ExpandNotifikasi")) {
            intent = new Intent(this, ExpandNotif.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("head", title);
        intent.putExtra("tgl", tanggal);
        intent.putExtra("isi", Isi);
        intent.putExtra("key_notif", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String id_channel = getString(R.string.id_channel);
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this, id_channel)
                .setSmallIcon(R.drawable.ic_notifikasi_black_24dp)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(Isi)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, nBuilder.build());
    }
}
