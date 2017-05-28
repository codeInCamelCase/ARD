package in.ac.bits_pilani.goa.ard.Fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import in.ac.bits_pilani.goa.ard.R;


public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
