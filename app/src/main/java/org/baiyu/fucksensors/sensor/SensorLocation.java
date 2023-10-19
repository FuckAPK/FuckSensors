package org.baiyu.fucksensors.sensor;

import android.hardware.Sensor;

public enum SensorLocation implements SensorType {
    TYPE_GAME_ROTATION_VECTOR(Sensor.TYPE_GAME_ROTATION_VECTOR),
    TYPE_GEOMAGNETIC_ROTATION_VECTOR(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR),
    TYPE_MAGNETIC_FIELD(Sensor.TYPE_MAGNETIC_FIELD),
    TYPE_MAGNETIC_FIELD_UNCALIBRATED(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
    //    TYPE_ORIENTATION(Sensor.TYPE_ORIENTATION),
    TYPE_PROXIMITY(Sensor.TYPE_PROXIMITY),


    ;
    private final int TYPE;

    SensorLocation(int type) {
        TYPE = type;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
