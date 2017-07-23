package com.macbitsgoa.ard.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HandleFirebaseMessages extends FirebaseMessagingService {

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
            Log.e("TAG", "fucked man " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        if (data != null) {

            String type = data.get("type");
            NotificationHelper helper = new NotificationHelper(this);
            switch (type) {
                case "picture":
                    Bitmap image = getBitmapFromURL(data.get("imageURL"));
                    helper.pictureNotif(image, data.get("title"));
                    break;
                case "url":
                    helper.urlNotif(data.get("title"), data.get("url"));
                    break;
                case "textLong":
                    helper.bigTextNotif(data.get("title"), data.get("message"), data.get("author"));
                    break;
                case "textShort":
                    helper.smallTextNotif(data.get("title"), data.get("message"));
                    break;
                case "list":
                    String[] listItems = data.get("list").split("\n");
                    helper.listNotif(listItems, data.get("title"));
                    break;

            }

        }
    }

}
