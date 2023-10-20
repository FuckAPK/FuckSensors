package org.baiyu.fucksensors

import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.TriggerEventListener
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (BuildConfig.APPLICATION_ID == lpparam.packageName) {
            return
        }
        try {
            XposedBridge.hookAllMethods(
                SensorManager::class.java,
                "registerListener",
                ListenerHook(lpparam)
            )
        } catch (t: Throwable) {
            XposedBridge.log("Fuck Sensors: registerListener hook failed.")
            XposedBridge.log(t)
        }
        try {
            XposedBridge.hookMethod(
                SensorManager::class.java.getMethod(
                    "requestTriggerSensor",
                    TriggerEventListener::class.java,
                    Sensor::class.java
                ),
                ListenerHook(lpparam)
            )
        } catch (t: Throwable) {
            XposedBridge.log("Fuck Sensors: requestTriggerSensor hook failed.")
            XposedBridge.log(t)
        }
        try {
            XposedBridge.hookMethod(
                SensorManager::class.java.getMethod("getSensorList", Int::class.javaPrimitiveType),
                SensorListHook(lpparam)
            )
        } catch (t: Throwable) {
            XposedBridge.log("Fuck Sensors: getSensorList hook failed.")
            XposedBridge.log(t)
        }
        try {
            XposedBridge.hookMethod(
                SensorManager::class.java.getMethod(
                    "getDynamicSensorList",
                    Int::class.javaPrimitiveType
                ),
                SensorListHook(lpparam)
            )
        } catch (t: Throwable) {
            XposedBridge.log("Fuck Sensors: getDynamicSensorList hook failed.")
            XposedBridge.log(t)
        }
        try {
            XposedBridge.hookMethod(
                SensorManager::class.java.getMethod(
                    "getDefaultSensor",
                    Int::class.javaPrimitiveType
                ),
                DefaultSensorHook(lpparam)
            )
            XposedBridge.hookMethod(
                SensorManager::class.java.getMethod(
                    "getDefaultSensor",
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType
                ),
                DefaultSensorHook(lpparam)
            )
        } catch (t: Throwable) {
            XposedBridge.log("Fuck Sensors: getDefaultSensor hook failed.")
            XposedBridge.log(t)
        }
    }

    private class DefaultSensorHook(private val lpparam: LoadPackageParam) : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) {
            val s = (param.result as? Sensor) ?: return

            if (settings.blockSensorsSet.contains(s.type)) {
                param.result = null
                XposedBridge.log("Sensor Fucked(Default Hook): " + lpparam.packageName + ": " + s.name + ", " + s.stringType)
            }
        }
    }

    private class SensorListHook(private val lpparam: LoadPackageParam) : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) {
            val sensorList = (param.result as? List<*>) ?: return

            param.result = sensorList.asSequence()
                .map { it as Sensor }
                .filter { !settings.blockSensorsSet.contains(it.type) }
                .toList()

            val hiddenSensors = sensorList.asSequence()
                .map { it as Sensor }
                .filter { settings.blockSensorsSet.contains(it.type) }
                .toList()
            if (hiddenSensors.isEmpty()) {
                return
            }
            val sb = StringBuilder()
            for (sensor in hiddenSensors) {
                sb.append(sensor.name).append(", ").append(sensor.stringType).append("; ")
            }
            XposedBridge.log("Sensor Fucked(List Hook): " + lpparam.packageName + ": " + sb.toString())
        }
    }

    private class ListenerHook(private val lpparam: LoadPackageParam) : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: MethodHookParam) {
            val s = (param.args[1] as? Sensor) ?: return

            if (settings.blockSensorsSet.contains(s.type)) {
                param.result = true
                XposedBridge.log("Sensor Fucked(Listener Hook): " + lpparam.packageName + ": " + s.name + ", " + s.stringType)
            }
        }
    }

    companion object {
        private val settings: Settings = Settings.getInstance(
            XSharedPreferences(BuildConfig.APPLICATION_ID)
        )
    }
}