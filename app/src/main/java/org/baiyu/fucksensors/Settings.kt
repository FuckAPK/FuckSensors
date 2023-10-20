package org.baiyu.fucksensors

import android.content.SharedPreferences
import de.robv.android.xposed.XSharedPreferences
import org.baiyu.fucksensors.sensor.SensorEnvironment
import org.baiyu.fucksensors.sensor.SensorLocation
import org.baiyu.fucksensors.sensor.SensorMotion
import org.baiyu.fucksensors.sensor.SensorType

class Settings private constructor(private val prefs: SharedPreferences) {
    val blockSensorsSet: Set<Int>
        get() {
            (prefs as? XSharedPreferences)?.reload()
            val allSensorTypes: MutableSet<SensorType> = HashSet()
            if (prefs.getBoolean(PREF_MOTION, true)) {
                allSensorTypes.addAll(SensorMotion.values())
            }
            if (prefs.getBoolean(PREF_LOCATION, true)) {
                allSensorTypes.addAll(SensorLocation.values())
            }
            if (prefs.getBoolean(PREF_ENVIRONMENT, true)) {
                allSensorTypes.addAll(SensorEnvironment.values())
            }
            return allSensorTypes.asSequence()
                .map { it.type }
                .filter { prefs.getBoolean(it.toString(), true) }
                .toSet()
        }

    companion object {
        const val PREF_MOTION = "motion"
        const val PREF_LOCATION = "location"
        const val PREF_ENVIRONMENT = "environment"

        @Volatile
        private var INSTANCE: Settings? = null
        fun getInstance(prefs: SharedPreferences): Settings {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Settings(prefs).also { INSTANCE = it }
            }
        }
    }
}