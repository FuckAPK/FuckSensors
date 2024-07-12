package org.lyaaz.fucksensors

import android.content.SharedPreferences
import de.robv.android.xposed.XSharedPreferences
import org.lyaaz.fucksensors.sensor.SensorEnvironment
import org.lyaaz.fucksensors.sensor.SensorLocation
import org.lyaaz.fucksensors.sensor.SensorMotion

class Settings private constructor(private val prefs: SharedPreferences) {
    val blockSensorsSet: Set<Int>
        get() {
            (prefs as? XSharedPreferences)?.reload()

            val handledSensorTypes: MutableSet<Int> = HashSet()
            if (prefs.getBoolean(PREF_MOTION, true)) {
                handledSensorTypes.addAll(SensorMotion.values().map { it.type })
            }
            if (prefs.getBoolean(PREF_LOCATION, true)) {
                handledSensorTypes.addAll(SensorLocation.values().map { it.type })
            }
            if (prefs.getBoolean(PREF_ENVIRONMENT, true)) {
                handledSensorTypes.addAll(SensorEnvironment.values().map { it.type })
            }

            val blockSensorsSet = handledSensorTypes.asSequence()
                .filter { prefs.getBoolean(it.toString(), true) }
                .toMutableSet()
            if (prefs.getBoolean(PREF_OTHERS, true)) {
                val otherSensorTypes = (1..100).filterNot { it in handledSensorTypes }
                blockSensorsSet.addAll(otherSensorTypes)
            }
            return blockSensorsSet.toSet()
        }

    companion object {
        const val PREF_OTHERS = "others"
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