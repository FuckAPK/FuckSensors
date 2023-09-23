package org.baiyu.fucksensors;

import android.hardware.Sensor;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class MainHook implements IXposedHookLoadPackage{

    private static final Set<Integer> sensorsList = Set.of(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LINEAR_ACCELERATION
            );
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.hookAllMethods(
                XposedHelpers.findClass("android.hardware.SensorManager ", lpparam.classLoader),
                "registerListener",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        if (param.args[1] instanceof Sensor s) {
                            if (sensorsList.contains(s.getType())) {
                                param.setResult(true);
                                XposedBridge.log("Sensor Fucked: " + lpparam.packageName + ": " + s.getName() + ", " + s.getStringType());
                            }
                        }
                    }
                }
        );
    }
}
