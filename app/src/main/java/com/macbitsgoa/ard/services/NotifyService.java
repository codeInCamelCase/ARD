package com.macbitsgoa.ard.services;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.macbitsgoa.ard.keys.ChatItemKeys;
import com.macbitsgoa.ard.keys.MessageItemKeys;
import com.macbitsgoa.ard.models.ChatsItem;
import com.macbitsgoa.ard.models.MessageItem;
import com.macbitsgoa.ard.types.MessageStatusType;
import com.macbitsgoa.ard.utils.AHC;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Intent service used to notify other user that their messages have been read.
 *
 * @author Vikramaditya Kukreja
 */
public class NotifyService extends BaseIntentService {

    /**
     * Tag for this class.
     */
    public static final String TAG = NotifyService.class.getSimpleName();

    public NotifyService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (getUser() == null) return;
        final String otherUserId = intent.getStringExtra(MessageItemKeys.OTHER_USER_ID);
        if (otherUserId == null) {
            Log.e(TAG, "Receiver id was null");
            return;
        }

        AHC.logd(TAG, "Inside notify service. Notifying " + otherUserId);

        final Realm database = Realm.getDefaultInstance();
        database.executeTransaction(r -> {
            final ChatsItem ci = r
                    .where(ChatsItem.class)
                    .equalTo(ChatItemKeys.DB_ID, otherUserId)
                    .findFirst();
            Log.e(TAG, "Chat item count set to 0");
            if (ci != null)
                ci.setUnreadCount(0);
            //TODO what if null
        });

        final RealmList<MessageItem> notifyList = new RealmList<>();
        notifyList.addAll(database
                .where(MessageItem.class)
                .equalTo(MessageItemKeys.OTHER_USER_ID, otherUserId)
                .equalTo(MessageItemKeys.MESSAGE_RECEIVED, true)
                .lessThanOrEqualTo(MessageItemKeys.MESSAGE_STATUS, MessageStatusType.MSG_RCVD)
                .findAll());

        AHC.logd(TAG, "For id " + otherUserId + ", messages unread = " + notifyList.size());
        final DatabaseReference readStatusRef = getRootReference()
                .child(AHC.FDR_CHAT)
                .child(otherUserId)
                .child(ChatItemKeys.PRIVATE_MESSAGES)
                .child(getUser().getUid())
                .child(ChatItemKeys.MESSAGE_STATUS);

        for (final MessageItem mi : notifyList) {
            readStatusRef.child(mi.getMessageId()).setValue(MessageStatusType.MSG_READ);
            AHC.logd(TAG, "Message read notif sent for " + mi.getMessageId());
            database.executeTransaction(r -> {
                final MessageItem mItem = r
                        .where(MessageItem.class)
                        .equalTo(MessageItemKeys.MESSAGE_ID, mi.getMessageId())
                        .findFirst();
                mItem.setMessageStatus(MessageStatusType.MSG_READ);
            });
        }

        database.close();
    }
}
