package in.ac.bits_pilani.goa.ard.services;

//This serice is used for custom firebase notifications
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

/**
 * Provides custom firebase notifications service.
 * @author Nirvan Anjirbag
 * */
public class HandleFirebaseMessages extends FirebaseMessagingService {

    /**
     * default notification id.
     * */
    final int default_id = 42;

    /**
     * default constructor.
     */
    public HandleFirebaseMessages() {
        super();
    }

    /**
     * returns the bitmap image at location mentioned in the src.
     * @author Nirvan Anjirbag
     * @param src is the url of the image
     * @return Bitmap image located at given url.
     */
    public static Bitmap getBitmapFromURL(final String src) {
        try {
            final URL url = new URL(src);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (final IOException e) {
            // Log exception
            Log.e("TAG", "Failed to retrieve bitmap from URL inside HandleFirebaseMessages:   "
                                        + e.getMessage());
            return null;
        }
    }

    /**
     * Builds notification when type is 1 i.e Big text Style.
     * @param data  : data received in Json file, passed on from onMessageReceived
     * @param builder  notification builder object
     */
    public void buildNotificationBigText(final Map<String, String> data, final NotificationCompat.Builder builder) {
        //Large Text Style corresponds to "1"
        final String bigTitle = data.get(R.string.handleFirebaseMessages_bigTitle);
        final String bigSubTitle = data.get(R.string.handleFirebaseMessages_bigSubTitle);
        final String bigSummaryText = data.get(R.string.handleFirebaseMessages_bigSummaryText);

        final NotificationCompat.BigTextStyle notificationBigText
                = new NotificationCompat.BigTextStyle();

        if (bigTitle != null) {
            notificationBigText.setBigContentTitle(bigTitle);
        }
        if (bigSubTitle != null) {
            notificationBigText.bigText(bigSubTitle);
        }
        if (bigSummaryText != null) {
            notificationBigText.setSummaryText(bigSummaryText);
        }

        builder.setStyle(notificationBigText);
    }

    /**
     * Builds notification when type is 2 i.e Big Picture style.
     * @param data  data received in Json file, passed on from onMessageReceived
     * @param builder notification builder object
     */
    public void buildNotificationBigPicture(final Map<String, String> data, final NotificationCompat.Builder builder) {
        final String imageUrl = data.get(R.string.handleFirebaseMessages_imageUrl);
        final String bigSummaryText = data.get(R.string.handleFirebaseMessages_bigSummaryText);
        final String bigTitle = data.get(R.string.handleFirebaseMessages_bigTitle);

        final NotificationCompat.BigPictureStyle notificationBigPicture
                = new NotificationCompat.BigPictureStyle();
        if (imageUrl != null) {
            final Bitmap image = getBitmapFromURL(imageUrl);
            if (image != null) {
                notificationBigPicture.bigPicture(image);
            } else {
                //TODO Image is null bt url wasn;t!
            }
        }

        if (bigSummaryText != null) {
            notificationBigPicture.setSummaryText(bigSummaryText);
        }
        if (bigTitle != null) {
            notificationBigPicture.setBigContentTitle(bigTitle);
        }
        //TODO icon
        builder.setStyle(notificationBigPicture);

    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        final Map<String, String> data = remoteMessage.getData();

        if (data == null) {
            return;
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        int notificationId = default_id;
        final String type = data.get(R.string.handleFirebaseMessages_type);
        final String id = data.get(R.string.handleFirebaseMessages_id);
        final String smallTitle = data.get(R.string.handleFirebaseMessages_smallTitle);
        final String smallSubTitle = data.get(R.string.handleFirebaseMessages_smallSubTitle);
        final String contentInfo = data.get(R.string.handleFirebaseMessages_contentInfo);
        final String ticker = data.get(R.string.handleFirebaseMessages_ticker);
        final String link = data.get(R.string.handleFirebaseMessages_link);
        final String className = data.get(R.string.handleFirebaseMessages_className);

        if (type != null && type.compareTo("1") == 0) {
            //Large Text Style corresponds to "1"
            buildNotificationBigText(data, builder);
        } else if (type != null && type.compareTo("2") == 0) {
            //BigPicture style specific
            buildNotificationBigPicture(data, builder);

        }

        //General things to be added for any kind of notification
        if (smallTitle != null) {
            builder.setContentTitle(smallTitle);
        }
        if (smallSubTitle != null) {
            builder.setContentText(smallSubTitle);
        }
        if (id != null) {
            notificationId = Integer.parseInt(id);
        }
        builder.setContentIntent(addWebsiteLinkPendingIntent(notificationId, link, className));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_SOCIAL);
        }

        builder.setSmallIcon(R.drawable.ic_stat_d);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.setAutoCancel(true);

        /*if (ticker != null) {
            builder.setTicker(ticker);
        } else {
            builder.setTicker("New ARD news!");
        }
        if (contentInfo != null) {
            builder.setSubText(contentInfo);
        } else {
            builder.setSubText("ARD");
        }*/

        final NotificationManagerCompat mNotificationManager
                = NotificationManagerCompat.from(this);
        mNotificationManager.notify(notificationId, builder.build());

    }

    /**
     *returns a pending intent for a notification.
     * @author Nirvan Anjirbag
     * @param id this is the notification id
     * @param link  url
     * @param className  The class for which the intent is called
     * @return PendingIntent to either an internet page or MainActivity or other class
     */
    private PendingIntent addWebsiteLinkPendingIntent(
                            final int id, final String link, final String className) {

        //intent to either an internt Url, or mainactivity or another class in the app
        Intent intent;

        if (link != null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        } else if (className != null) {
            try {
                intent = new Intent(this, Class.forName("in.ac.bits_pilani.goa.ard." + className));
            } catch (final ClassNotFoundException e) {
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
