package com.macbitsgoa.ard.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.macbitsgoa.ard.R;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class NotificationHelper {

    private Context context;
    private Random random;
    private NotificationManager notificationManager;

    public NotificationHelper(Context ctx) {
        random = new Random();
        context = ctx;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void smallTextNotif(String title, String message) {//TODO : add icon
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);

        notificationManager.notify(random.nextInt(), mBuilder.build());
    }

    public void urlNotif(String title, String url) {//TODO : add icon
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText("Click to open url");

        Intent openUrl = new Intent(Intent.ACTION_VIEW);
        openUrl.setData(Uri.parse(url));

        PendingIntent urlIntent = PendingIntent.getActivity(context, random.nextInt(), openUrl, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(urlIntent);

        notificationManager.notify(random.nextInt(), mBuilder.build());
    }


    public void listNotif(String[] messages, String title) {
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle(title);
        for (int i = 0; i < (messages.length < 4 ? messages.length : 4); i++)
            inboxStyle.addLine(messages[i]);

        int rem = messages.length - 4;
        if (rem > 0)
            inboxStyle.setSummaryText("+" + rem + "more..");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setStyle(inboxStyle)
                .setContentTitle(title)
                .setContentText(context.getString(R.string.newListMessage));
        mBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);

        ///  .addAction("View messages",TODO: pending intent here);
        notificationManager.notify(random.nextInt(), mBuilder.build());
    }

    public void bigTextNotif(String title, String message, String summaryText) {
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText(summaryText);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        //     .setSmallIcon(add icon here )
                        //   .setLargeIcon(add icon here)
                        .setStyle(bigText)
                        .setContentText(message.substring(0, 50) + (message.length() > 50 ? "....read more" : ""));
        mBuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);

        notificationManager.notify(random.nextInt(), mBuilder.build());
    }

    public void pictureNotif(Bitmap image, String title) {
        Uri imageUri = getImageUri(image);
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_SEND);
        intent.setData(imageUri);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        PendingIntent viewPI = PendingIntent.getActivity(context, random.nextInt(), Intent.createChooser(intent, title), PendingIntent.FLAG_CANCEL_CURRENT);

        intent.setType(Intent.ACTION_VIEW);
        PendingIntent sharePI = PendingIntent.getActivity(context, random.nextInt(), Intent.createChooser(intent, title), PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(image).build();

        NotificationCompat.Builder mbuilder =
                new NotificationCompat.Builder(context)
                        //.setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setStyle(bigPictureStyle)
                        .addAction(android.R.drawable.ic_menu_share, "show image", sharePI)  //change icon
                        .addAction(android.R.drawable.ic_menu_view, "Share", viewPI);                     // change icon
        mbuilder.setSmallIcon(android.R.mipmap.sym_def_app_icon);

        notificationManager.notify(random.nextInt(), mbuilder.build());

    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
