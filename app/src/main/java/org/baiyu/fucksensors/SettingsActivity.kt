package org.baiyu.fucksensors;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import org.baiyu.fucksensors.sensor.SensorEnvironment;
import org.baiyu.fucksensors.sensor.SensorLocation;
import org.baiyu.fucksensors.sensor.SensorMotion;

import java.util.stream.Stream;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        try {
            //noinspection deprecation
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_WORLD_READABLE);
        } catch (Exception e) {
            getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", MODE_PRIVATE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new MySettingsFragment())
                .commit();
    }

    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            PreferenceScreen preferenceScreen = this.getPreferenceScreen();

            // Motion
            PreferenceCategory MotionCate = new PreferenceCategory(requireContext());
            MotionCate.setTitle(R.string.title_motion_cate);
            MotionCate.setIconSpaceReserved(false);
            preferenceScreen.addPreference(MotionCate);


            final String KEY_MOTION = Settings.PREF_MOTION;
            final String KEY_LOCATION = Settings.PREF_LOCATION;
            final String KEY_ENVIRONMENT = Settings.PREF_ENVIRONMENT;
            final String Title = getResources().getString(R.string.title_all);

            SwitchPreferenceCompat sm = new SwitchPreferenceCompat(requireContext());
            sm.setKey(KEY_MOTION);
            sm.setTitle(Title);
            sm.setDefaultValue(true);
            sm.setIconSpaceReserved(false);
            MotionCate.addPreference(sm);

            Stream.of(SensorMotion.values()).sorted().forEach(
                    it -> {
                        SwitchPreferenceCompat s = new SwitchPreferenceCompat(requireContext());
                        s.setKey(String.valueOf(it.getType()));
                        s.setTitle(it.name().substring(5));
                        s.setDefaultValue(true);
                        s.setIconSpaceReserved(false);
                        MotionCate.addPreference(s);
                    }
            );
            for (int i = 1; i < MotionCate.getPreferenceCount(); i++) {
                MotionCate.getPreference(i).setDependency(KEY_MOTION);
            }

            // Location
            PreferenceCategory LocationCate = new PreferenceCategory(requireContext());
            LocationCate.setTitle(R.string.title_location_cate);
            LocationCate.setIconSpaceReserved(false);
            preferenceScreen.addPreference(LocationCate);


            SwitchPreferenceCompat sl = new SwitchPreferenceCompat(requireContext());
            sl.setKey(KEY_LOCATION);
            sl.setTitle(Title);
            sl.setDefaultValue(true);
            sl.setIconSpaceReserved(false);
            LocationCate.addPreference(sl);

            Stream.of(SensorLocation.values()).sorted().forEach(
                    it -> {
                        SwitchPreferenceCompat s = new SwitchPreferenceCompat(requireContext());
                        s.setKey(String.valueOf(it.getType()));
                        s.setTitle(it.name().substring(5));
                        s.setDefaultValue(true);
                        s.setIconSpaceReserved(false);
                        LocationCate.addPreference(s);
                    }
            );

            for (int i = 1; i < LocationCate.getPreferenceCount(); i++) {
                LocationCate.getPreference(i).setDependency(KEY_LOCATION);
            }

            // Environment
            PreferenceCategory EnvironmentCate = new PreferenceCategory(requireContext());
            EnvironmentCate.setTitle(R.string.title_environment_cate);
            EnvironmentCate.setIconSpaceReserved(false);
            preferenceScreen.addPreference(EnvironmentCate);

            SwitchPreferenceCompat se = new SwitchPreferenceCompat(requireContext());
            se.setKey(KEY_ENVIRONMENT);
            se.setTitle(Title);
            se.setDefaultValue(true);
            se.setIconSpaceReserved(false);
            EnvironmentCate.addPreference(se);

            Stream.of(SensorEnvironment.values()).sorted().forEach(
                    it -> {
                        SwitchPreferenceCompat s = new SwitchPreferenceCompat(requireContext());
                        s.setKey(String.valueOf(it.getType()));
                        s.setTitle(it.name().substring(5));
                        s.setDefaultValue(true);
                        s.setIconSpaceReserved(false);
                        EnvironmentCate.addPreference(s);
                    }
            );
            for (int i = 1; i < EnvironmentCate.getPreferenceCount(); i++) {
                EnvironmentCate.getPreference(i).setDependency(KEY_ENVIRONMENT);
            }
        }
    }

}
