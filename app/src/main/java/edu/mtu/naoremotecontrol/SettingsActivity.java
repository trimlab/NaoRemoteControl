package edu.mtu.naoremotecontrol;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by EricMVasey on 10/12/2016.
 */

public class SettingsActivity extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_settings);
    }
}
