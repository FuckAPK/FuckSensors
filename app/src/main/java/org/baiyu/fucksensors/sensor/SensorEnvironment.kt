package org.baiyu.fucksensors.sensor;

import android.hardware.Sensor;

public enum SensorEnvironment implements SensorType {

    TYPE_AMBIENT_TEMPERATURE(Sensor.TYPE_AMBIENT_TEMPERATURE),
    TYPE_LIGHT(Sensor.TYPE_LIGHT),
    TYPE_PRESSURE(Sensor.TYPE_PRESSURE),
    TYPE_RELATIVE_HUMIDITY(Sensor.TYPE_RELATIVE_HUMIDITY),
    // TYPE_TEMPERATURE(Sensor.TYPE_TEMPERATURE),

    ;
    private final int TYPE;

    SensorEnvironment(int type) {
        TYPE = type;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
