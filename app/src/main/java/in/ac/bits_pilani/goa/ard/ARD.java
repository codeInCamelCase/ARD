package in.ac.bits_pilani.goa.ard;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Custom class extending the Application class.
 * @author vikramaditya
 */

public class ARD extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*
            to enable offline firebase database access
            - only data that is requested/downloaded while user is online is cached
            - to force download data at specific locations even when it's not used by user while
                online and may use while offline
                follow this example-
                ```
                DatabaseReference importantData = FirebaseDatabase.getInstance().getReference("impData");
                importantData.keepSynced(true);
                ```
            - related documentation: https://firebase.google.com/docs/database/android/offline-capabilities
        */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
