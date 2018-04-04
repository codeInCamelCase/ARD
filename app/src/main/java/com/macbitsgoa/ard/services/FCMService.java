package com.macbitsgoa.ard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.activities.AnnActivity;
import com.macbitsgoa.ard.keys.AnnItemKeys;
import com.macbitsgoa.ard.keys.FCMKeys;
import com.macbitsgoa.ard.utils.AHC;

import java.util.Map;

/**
 * FCM service.
 * Supports
 * <ol>
 * <li>Deletion of item (HomeItem/AnnItem) from database</li>
 * <li>Notification that opens a url</li>
 * <li>Starting of Home Service in case of a new news item</li>
 * </ol>
 *
 * @author Vikramaditya Kukreja
 */
public class FCMService extends FirebaseMessagingService {

    /**
     * Tag for this class.
     */
    public static final String TAG = FCMService.class.getSimpleName();

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> data = remoteMessage.getData();
        if (data.size() == 0) {
            AHC.logd(TAG, "No data in FCM message");
            return;
        }
        //Data message
        final String action = data.get(FCMKeys.ACTION);
        AHC.logd(TAG, "Action received from FCM " + action);
        //TODO send fcm for deletes also in ARDI. then remove this line
        AHC.startService(this, MaintenanceService.class, MaintenanceService.TAG);
        switch (action) {
            case FCMKeys.ACTION_SERVICE: {
                try {
                    scheduleJob(data);
                } catch (ActivityNotFoundException | ClassNotFoundException e) {
                    AHC.logd(TAG, e.toString());
                }
                break;
            }
            case FCMKeys.ACTION_VIEW: {
                createNotification(data);
                break;
            }
            case FCMKeys.ACTION_ANNOUNCEMENT: {
                final Bundle extras = new Bundle();
                extras.putString(AnnItemKeys.AUTHOR, data.get(AnnItemKeys.AUTHOR));
                extras.putString(AnnItemKeys.DATA, data.get(AnnItemKeys.DATA));
                AHC.startService(this, AnnNotifyService.class, AnnNotifyService.TAG, extras);
                AHC.startService(this, HomeService.class, HomeService.TAG);
                break;
            }
            case FCMKeys.ACTION_DELETE: {
                AHC.startService(this, MaintenanceService.class, MaintenanceService.TAG);
                break;
            }
            default: {
                AHC.logd(TAG, "Unrecognised action " + action);
                break;
            }
        }
    }

    private void createNotification(final Map<String, String> data) {
        AHC.logd(TAG, "Received a notification");
        AHC.logd(TAG, data.toString());

        final int _ID = 143;

        Uri uri = null;
        if (data.containsKey(FCMKeys.ACTION_VIEW_URI))
            uri = Uri.parse(data.get(FCMKeys.ACTION_VIEW_URI));
        final String title = data.get(FCMKeys.ACTION_VIEW_TITLE);
        final String text = data.get(FCMKeys.ACTION_VIEW_TEXT);
        final int notificationId = Integer.parseInt(data.get(FCMKeys.ID));

        final PendingIntent pi;
        if (uri == null) {
            pi = PendingIntent.getActivity(this, _ID,
                    new Intent(this, AnnActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pi = PendingIntent.getActivity(this, _ID,
                    new Intent(Intent.ACTION_VIEW, uri), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        final NotificationManager nm
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AHC.createChannels(nm);
        final NotificationCompat.Builder ncb = new NotificationCompat.Builder(this, AHC.ARD)
                .setContentTitle(title)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setContentText(text)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(AHC.ARD))
                .setSmallIcon(R.mipmap.ic_launcher);

        nm.notify(notificationId, ncb.build());
    }

    /**
     * Schedule job depending on remote message data.
     *
     * @param data Map data from remote message of FCM.
     * @throws ClassNotFoundException In case the required service was not found.
     */
    private void scheduleJob(final Map<String, String> data) throws ClassNotFoundException {
        AHC.logd(TAG, "Action is " + FCMKeys.ACTION_SERVICE);
        final String service = data.get(FCMKeys.ACTION_SERVICE_NAME);
        if (!service.contains("Service")) {
            AHC.logd(TAG, "Action does not have a valid service. Found " + service);
            throw new ClassNotFoundException("Not a valid service");
        }

        if (service.equals(HomeService.TAG)) {
            AHC.startService(this, HomeService.class, HomeService.TAG);
            //} else if (service.equals(MessagingService.TAG)) {
            //    AHC.startService(this, MessagingService.class, MessagingService.TAG);
            //    AHC.logd(TAG, "Messaging service will be started from FCM");
        } else {
            AHC.logd(TAG, "Requested service is not yet supported. Service was " + service);
            throw new ClassNotFoundException("Currently not supporting " + service);
        }
    }
}
