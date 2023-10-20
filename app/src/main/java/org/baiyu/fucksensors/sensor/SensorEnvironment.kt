package org.baiyu.fucksensors.sensor

import android.hardware.Sensor

enum class SensorEnvironment(override val type: Int) : SensorType {
    TYPE_AMBIENT_TEMPERATURE(Sensor.TYPE_AMBIENT_TEMPERATURE),
    TYPE_LIGHT(Sensor.TYPE_LIGHT),
    TYPE_PRESSURE(Sensor.TYPE_PRESSURE),

    // TYPE_TEMPERATURE(Sensor.TYPE_TEMPERATURE),
    TYPE_RELATIVE_HUMIDITY(Sensor.TYPE_RELATIVE_HUMIDITY);
}