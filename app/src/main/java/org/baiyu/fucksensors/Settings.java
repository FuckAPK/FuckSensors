package org.baiyu.fucksensors;

import android.content.SharedPreferences;
import android.hardware.Sensor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.robv.android.xposed.XSharedPreferences;

public class Settings {

    private final SharedPreferences prefs;
    private volatile static Settings INSTANCE;
    private Map<String, Integer> sensorTypes;

    private Settings(SharedPreferences prefs) {
        this.prefs = prefs;
        initSensorTypes();
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

    public Map<String, Integer> getSensorTypes() {
        return sensorTypes;
    }

    public List<Integer> getBlockSensorsList() {
        if (prefs instanceof XSharedPreferences) {
            ((XSharedPreferences) prefs).reload();
        }
        return sensorTypes.entrySet().stream()
                .filter(entry -> prefs.getBoolean(entry.getKey(), false))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

    }

    private void initSensorTypes() {
        sensorTypes = new HashMap<>();
        Arrays.stream(Sensor.class.getDeclaredFields())
                .filter(field -> field.getType() == int.class)
                .filter(field -> field.getName().startsWith("TYPE_"))
                .forEach(field -> {
                    try {
                        sensorTypes.put(
                                (String) Sensor.class.getField("STRING_" + field.getName()).get(null),
                                field.getInt(null));
                    } catch (IllegalAccessException | NoSuchFieldException ignored) {
                    }
                });
    }
}
