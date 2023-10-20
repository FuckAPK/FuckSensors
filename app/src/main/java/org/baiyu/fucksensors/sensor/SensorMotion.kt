package org.baiyu.fucksensors.sensor;

import android.hardware.Sensor;

public enum SensorMotion implements SensorType {
    TYPE_ACCELEROMETER(Sensor.TYPE_ACCELEROMETER),
    TYPE_ACCELEROMETER_UNCALIBRATED(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED),
    TYPE_GRAVITY(Sensor.TYPE_GRAVITY),
    TYPE_GYROSCOPE(Sensor.TYPE_GYROSCOPE),
    TYPE_GYROSCOPE_UNCALIBRATED(Sensor.TYPE_GYROSCOPE_UNCALIBRATED),
    TYPE_LINEAR_ACCELERATION(Sensor.TYPE_LINEAR_ACCELERATION),
    TYPE_ROTATION_VECTOR(Sensor.TYPE_ROTATION_VECTOR),
    TYPE_SIGNIFICANT_MOTION(Sensor.TYPE_SIGNIFICANT_MOTION),
    TYPE_STEP_COUNTER(Sensor.TYPE_STEP_COUNTER),
    TYPE_STEP_DETECTOR(Sensor.TYPE_STEP_DETECTOR);

    private final int TYPE;

    SensorMotion(int type) {
        this.TYPE = type;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
