package in.ac.bits_pilani.goa.ard.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.activities.MainActivity;

public class HandleFirebaseMessages extends FirebaseMessagingService {


    final int DEFAULT_ID = 42;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            Log.e("TAG", "Failed to retrieve bitmap from URL inside HandleFirebaseMessages:   " + e.getMessage());
            return null;
        }
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            //In case data is wrongly formatted
            //we use try catch block

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            int notificationId = DEFAULT_ID;
            String type = data.get("type");
            String id = data.get("id");
            String smallTitle = data.get("smallTitle");
            String smallSubTitle = data.get("smallSubTitle");
            String contentInfo = data.get("contentInfo");
            String ticker = data.get("ticker");
            String link = data.get("link");
            String className = data.get("className");

            if (type != null) {
                if (type.compareTo("1") == 0) {
                    //Large Text Style corresponds to "1"
                    String bigTitle = data.get("bigTitle");
                    String bigSubTitle = data.get("bigSubTitle");
                    String bigSummaryText = data.get("bigSummaryText");

                    NotificationCompat.BigTextStyle notificationBigText = new NotificationCompat.BigTextStyle();

                    if (bigTitle != null) notificationBigText.setBigContentTitle(bigTitle);
                    if (bigSubTitle != null) notificationBigText.bigText(bigSubTitle);
                    if (bigSummaryText != null) notificationBigText.setSummaryText(bigSummaryText);

                    builder.setStyle(notificationBigText);
                } else if (type.compareTo("2") == 0) {
                    //BigPicture style specific

                    String imageUrl = data.get("imageUrl");
                    String bigSummaryText = data.get("bigSummaryText");
                    String bigTitle = data.get("bigTitle");

                    NotificationCompat.BigPictureStyle notificationBigPicture = new NotificationCompat.BigPictureStyle();
                    if (imageUrl != null) {
                        Bitmap image = getBitmapFromURL(imageUrl);
                        if (image != null)
                            notificationBigPicture.bigPicture(image);
                        else {
                            //TODO Image is null bt url wasn;t!
                        }
                    }

                    if (bigSummaryText != null)
                        notificationBigPicture.setSummaryText(bigSummaryText);
                    if (bigTitle != null) notificationBigPicture.setBigContentTitle(bigTitle);
                    //TODO icon
                    builder.setStyle(notificationBigPicture);

                }
            }


            //General things to be added for any kind of notification
            if (smallTitle != null) builder.setContentTitle(smallTitle);
            if (smallSubTitle != null) builder.setContentText(smallSubTitle);
            if (id != null) notificationId = Integer.parseInt(id);
            builder.setContentIntent(addWebsiteLinkPendingIntent(notificationId, link, className));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(Notification.CATEGORY_SOCIAL);
            }

            builder.setSmallIcon(R.drawable.ic_stat_d);
            builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            builder.setAutoCancel(true);

            if (ticker != null) builder.setTicker(ticker);
            else builder.setTicker("New ARD news!");

            if (contentInfo != null) builder.setSubText(contentInfo);
            else builder.setSubText("ARD");

            NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
            mNotificationManager.notify(notificationId, builder.build());

        }
    }

    private PendingIntent addWebsiteLinkPendingIntent(int id, String link, String className) {
        Intent intent;

        if (link != null) {
            //TODO Change to ChromeCustomTabs later
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        } else if (className != null) {
            try {
                intent = new Intent(this, Class.forName("in.ac.bits_pilani.goa.ard." + className));
                //TODO check for page number
            } catch (ClassNotFoundException e) {
                intent = new Intent(this, MainActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        return PendingIntent.getActivity(
                this,
                id,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }


}
