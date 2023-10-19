package org.baiyu.fucksensors;

import android.content.SharedPreferences;

import org.baiyu.fucksensors.sensor.SensorEnvironment;
import org.baiyu.fucksensors.sensor.SensorLocation;
import org.baiyu.fucksensors.sensor.SensorMotion;
import org.baiyu.fucksensors.sensor.SensorType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import de.robv.android.xposed.XSharedPreferences;

public class Settings {

    public static final String PREF_MOTION = "motion";
    public static final String PREF_LOCATION = "location";
    public static final String PREF_ENVIRONMENT = "environment";
    private volatile static Settings INSTANCE;
    private final SharedPreferences prefs;

    private Settings(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public static Settings getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            synchronized (Settings.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Settings(prefs);
                }
            }
        }
        return INSTANCE;
    }

    public Set<Integer> getBlockSensorsSet() {
        if (prefs instanceof XSharedPreferences xPrefs) {
            xPrefs.reload();
        }
        Set<SensorType> allSensorTypes = new HashSet<>();
        if (prefs.getBoolean(PREF_MOTION, true)) {
            Collections.addAll(allSensorTypes, SensorMotion.values());
        }
        if (prefs.getBoolean(PREF_LOCATION, true)) {
            Collections.addAll(allSensorTypes, SensorLocation.values());
        }
        if (prefs.getBoolean(PREF_ENVIRONMENT, true)) {
            Collections.addAll(allSensorTypes, SensorEnvironment.values());
        }

        return allSensorTypes.stream()
                .map(SensorType::getType)
                .filter(it -> prefs.getBoolean(String.valueOf(it), true))
                .collect(Collectors.toSet());
    }
}
