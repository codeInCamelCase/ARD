package in.ac.bits_pilani.goa.ard.activities;

import android.app.Activity;
import android.os.Bundle;


import in.ac.bits_pilani.goa.ard.Fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


    }
}
