package in.ac.bits_pilani.goa.ard.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class HandleFirebaseMessages extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("One: ","entered onMessageReceived() ");
        final Map<String, String> data = (Map<String, String>) remoteMessage.getData();
        final String type = data.get("type");
        Log.e("TAG ","Type= "+type);

    }

}
