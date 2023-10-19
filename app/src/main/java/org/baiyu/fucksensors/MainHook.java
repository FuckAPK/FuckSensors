package org.baiyu.fucksensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;

import java.util.List;
import java.util.stream.Collectors;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    private static final Settings settings = Settings.getInstance(
            new XSharedPreferences(BuildConfig.APPLICATION_ID)
    );

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (BuildConfig.APPLICATION_ID.equals(lpparam.packageName)) {
            return;
        }

        try {
            XposedBridge.hookAllMethods(
                    SensorManager.class,
                    "registerListener",
                    new ListenerHook(lpparam)
            );
        } catch (Throwable t) {
            XposedBridge.log("Fuck Sensors: registerListener hook failed.");
            XposedBridge.log(t);
        }

        try {
            XposedBridge.hookMethod(
                    SensorManager.class.getMethod("requestTriggerSensor", TriggerEventListener.class, Sensor.class),
                    new ListenerHook(lpparam)
            );
        } catch (Throwable t) {
            XposedBridge.log("Fuck Sensors: requestTriggerSensor hook failed.");
            XposedBridge.log(t);
        }

        try {
            XposedBridge.hookMethod(
                    SensorManager.class.getMethod("getSensorList", int.class),
                    new SensorListHook(lpparam)
            );
        } catch (Throwable t) {
            XposedBridge.log("Fuck Sensors: getSensorList hook failed.");
            XposedBridge.log(t);
        }

        try {
            XposedBridge.hookMethod(
                    SensorManager.class.getMethod("getDynamicSensorList", int.class),
                    new SensorListHook(lpparam)
            );
        } catch (Throwable t) {
            XposedBridge.log("Fuck Sensors: getDynamicSensorList hook failed.");
            XposedBridge.log(t);
        }

        try {
            XposedBridge.hookMethod(
                    SensorManager.class.getMethod("getDefaultSensor", int.class),
                    new DefaultSensorHook(lpparam)
            );
            XposedBridge.hookMethod(
                    SensorManager.class.getMethod("getDefaultSensor", int.class, boolean.class),
                    new DefaultSensorHook(lpparam)
            );
        } catch (Throwable t) {
            XposedBridge.log("Fuck Sensors: getDefaultSensor hook failed.");
            XposedBridge.log(t);
        }
    }

    private static class DefaultSensorHook extends XC_MethodHook {
        private final XC_LoadPackage.LoadPackageParam lpparam;

        private DefaultSensorHook(XC_LoadPackage.LoadPackageParam lpparam) {
            this.lpparam = lpparam;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) {
            if (param.getResult() instanceof Sensor s && s != null && settings.getBlockSensorsSet().contains(s.getType())) {
                param.setResult(null);
                XposedBridge.log("Sensor Fucked(Default Hook): " + lpparam.packageName + ": " + s.getName() + ", " + s.getStringType());
            }
        }
    }

    private static class SensorListHook extends XC_MethodHook {
        private final XC_LoadPackage.LoadPackageParam lpparam;

        public SensorListHook(XC_LoadPackage.LoadPackageParam lpparam) {
            this.lpparam = lpparam;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) {
            if (param.getResult() instanceof List<?> sensorList && sensorList != null) {
                param.setResult(sensorList.stream()
                        .filter(sensor -> !settings.getBlockSensorsSet().contains(((Sensor) sensor).getType()))
                        .collect(Collectors.toList()));
                //noinspection unchecked
                List<Sensor> hiddenSensors = (List<Sensor>) sensorList.stream()
                        .filter(sensor -> settings.getBlockSensorsSet().contains(((Sensor) sensor).getType()))
                        .collect(Collectors.toList());

                if (hiddenSensors.isEmpty()) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (Sensor sensor : hiddenSensors) {
                    sb.append(sensor.getName()).append(", ").append(sensor.getStringType()).append("; ");
                }
                XposedBridge.log("Sensor Fucked(List Hook): " + lpparam.packageName + ": " + sb.toString());
            }
        }
    }

    private static class ListenerHook extends XC_MethodHook {
        private final XC_LoadPackage.LoadPackageParam lpparam;

        public ListenerHook(XC_LoadPackage.LoadPackageParam lpparam) {
            this.lpparam = lpparam;
        }

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            if (param.args[1] instanceof Sensor s && settings.getBlockSensorsSet().contains(s.getType())) {
                param.setResult(true);
                XposedBridge.log("Sensor Fucked(Listener Hook): " + lpparam.packageName + ": " + s.getName() + ", " + s.getStringType());
            }
        }
    }

}
