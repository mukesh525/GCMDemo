package androidwarriors.gcmdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PushNotificationService extends GcmListenerService {

    private NotificationManager mNotificationManager;
    public static int NOTIFICATION_ID = 1;
    private String TAG = "GCMPRO";
    private String url;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        url = data.getString("img");
      //  sendNotification(message);
        sendStickyNotification(message);

    }

    private void sendNotification(String msg) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(MainActivity.class);
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //Create notification object and set the content.
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.ic_backup);
        nb.setContentTitle("Set your title");
        nb.setContentText("Set Content text");
        nb.setTicker("Set Ticker text");
        nb.addAction(R.drawable.ic_share_24dp, "Share", resultPendingIntent);
        //  nb.setContent(new RemoteViews(new Tex))
        nb.setContentText(msg);

        //get the bitmap to show in notification bar
        // Bitmap bitmap_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.drawerr);
        // Bitmap bitmap_image = getBitmapFromURL("http://images.landscapingnetwork.com/pictures/images/500x500Max/front-yard-landscaping_15/front-yard-hillside-banyon-tree-design-studio_1018.jpg");
        Bitmap bitmap_image = getBitmapFromURL(url);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText("Summary text appears on expanding the notification");
        nb.setStyle(s);

//        Intent resultIntent = new Intent(this, MainActivity.class);
//        TaskStackBuilder TSB = TaskStackBuilder.create(this);
//        TSB.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack


        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());


    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendNotification1(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_backup)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendStickyNotification(String message) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_backup)
                .setContentTitle("title")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }
}
