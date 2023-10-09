package org.baiyu.fucksensors;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Map;

import de.robv.android.xposed.XposedBridge;

public class SettingsActivity extends AppCompatActivity {

    private Settings settings;

    /** @noinspection deprecation*/
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        try {
            SharedPreferences prefs = getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_WORLD_READABLE);
            settings = Settings.getInstance(prefs);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new MySettingsFragment())
                .commit();
    }

    private class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            PreferenceScreen preferenceScreen = this.getPreferenceScreen();
            settings.getSensorTypes().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        SwitchPreferenceCompat switchPreferenceCompat = new SwitchPreferenceCompat(requireContext());
                        switchPreferenceCompat.setTitle(entry.getKey());
                        switchPreferenceCompat.setDefaultValue(false);
                        switchPreferenceCompat.setKey(entry.getKey());
                        switchPreferenceCompat.setIconSpaceReserved(false);
                        preferenceScreen.addPreference(switchPreferenceCompat);
                    });
        }
    }

}
