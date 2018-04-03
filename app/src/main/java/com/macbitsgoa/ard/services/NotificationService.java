package com.macbitsgoa.ard.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.activities.ChatActivity;
import com.macbitsgoa.ard.keys.ChatItemKeys;
import com.macbitsgoa.ard.keys.MessageItemKeys;
import com.macbitsgoa.ard.models.ChatsItem;
import com.macbitsgoa.ard.models.MessageItem;
import com.macbitsgoa.ard.types.MessageStatusType;
import com.macbitsgoa.ard.utils.AHC;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Service to show various section updates. Currently chats and announcements are shown.
 * For chats those that have {@link MessageStatusType} as {@code MessageStatusType.MSG_RCVD} or
 * from other senders is shown.
 *
 * @author Vikramaditya Kukreja
 */

public class NotificationService extends BaseJobService {

    /**
     * TAG for class.
     */
    public static final String TAG = NotificationService.class.getSimpleName();

    /**
     * Realm database.
     */
    private Realm database;


    /**
     * Notification manager.
     */
    private NotificationManager nm;

    @Override
    public boolean onStartJob(JobParameters job) {
        if (getUser() == null) return super.onStartJob(job);
        database = Realm.getDefaultInstance();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createChannels();
        chatNotifications();
        database.close();
        return super.onStartJob(job);
    }

    private void createChannels() {
        String channelId = AHC.ARD;
        CharSequence channelName = AHC.ARD;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel nc = new NotificationChannel(channelId, channelName, importance);
            nm.createNotificationChannel(nc);
        }
    }

    private void chatNotifications() {
        final RealmResults<MessageItem> unreadMessagesUsers = database.where(MessageItem.class)
                .equalTo(MessageItemKeys.MESSAGE_RECEIVED, true)
                .lessThanOrEqualTo(MessageItemKeys.MESSAGE_STATUS, MessageStatusType.MSG_RCVD)
                .distinct(MessageItemKeys.OTHER_USER_ID);

        final List<ChatsItem> chatsItems = new ArrayList<>();
        for (final MessageItem mi : unreadMessagesUsers) {
            final ChatsItem ci = database
                    .where(ChatsItem.class)
                    .equalTo(ChatItemKeys.DB_ID, mi.getOtherUserId())
                    .findFirst();
            if (ci == null) continue;
            //TODO handle null case. is it req?
            chatsItems.add(ci);
        }
        for (final ChatsItem ci : chatsItems) {
            final RealmResults<MessageItem> unreadMessages = database.where(MessageItem.class)
                    .equalTo(MessageItemKeys.OTHER_USER_ID, ci.getId())
                    .equalTo(MessageItemKeys.MESSAGE_RECEIVED, true)
                    .lessThanOrEqualTo(MessageItemKeys.MESSAGE_STATUS, MessageStatusType.MSG_RCVD)
                    .findAllSorted(new String[]{
                                    MessageItemKeys.MESSAGE_RECEIVED_TIME,
                                    MessageItemKeys.DB_MESSAGE_TIME},
                            new Sort[]{Sort.DESCENDING, Sort.DESCENDING});
            if (unreadMessages.size() == 0) continue;
            final Intent piIntent = new Intent(this, ChatActivity.class);
            piIntent.putExtra("title", ci.getName());
            piIntent.putExtra(MessageItemKeys.OTHER_USER_ID, ci.getId());
            piIntent.putExtra("photoUrl", ci.getPhotoUrl());

            final PendingIntent pi = PendingIntent
                    .getActivity(this,
                            123,
                            piIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle(ci.getName())
                    .addLine(AHC.getSimpleTime(unreadMessages.get(0).getMessageTime())
                            + ": "
                            + unreadMessages.get(0).getMessageData())
                    .setSummaryText(unreadMessages.size() + " new message");
            if (unreadMessages.size() > 1)
                inboxStyle = inboxStyle
                        .addLine(AHC.getSimpleTime(unreadMessages.get(1).getMessageTime())
                                + ": "
                                + unreadMessages.get(1).getMessageData())
                        .setSummaryText(unreadMessages.size() + " new messages");
            if (unreadMessages.size() > 2)
                inboxStyle = inboxStyle
                        .addLine(AHC.getSimpleTime(unreadMessages.get(2).getMessageTime())
                                + ": "
                                + unreadMessages.get(2).getMessageData());

            final NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(this, AHC.ARD)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(ci.getName())
                    .setContentText(unreadMessages.size()
                            + " new " + (unreadMessages.size() > 1 ? "messages" : "message"))
                    .setShowWhen(true)
                    .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("New from " + ci.getName())
                    //.setDefaults(Notification.DEFAULT_SOUND)
                    .setStyle(inboxStyle);

            Log.e(TAG, "Notification id -> "
                    + ci.getId().hashCode());

            //TODO improve not showing notif for current uesr
            if (ChatActivity.visible) {
                if (ChatActivity.otherUserId != null
                        && !ChatActivity.otherUserId.equals(ci.getId())) {
                    nm.notify(ci.getId().hashCode(), builder.build());
                }
            } else nm.notify(ci.getId().hashCode(), builder.build());

            final Intent notificationBC = new Intent(ChatItemKeys.NOTIFICATION_ACTION);
            notificationBC.putExtra(MessageItemKeys.OTHER_USER_ID, ci.getId());
            sendBroadcast(notificationBC);
        }
    }
}
