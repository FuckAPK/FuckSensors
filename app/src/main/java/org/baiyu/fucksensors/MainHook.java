package org.baiyu.fucksensors;

import android.hardware.Sensor;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class MainHook implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass("android.hardware.SensorManager", lpparam.classLoader),
                    "registerListener",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if (param.args[1] instanceof Sensor s) {
                                param.setResult(true);
                                XposedBridge.log("Sensor Fucked: " + lpparam.packageName + ": " + s.getName() + ", " + s.getStringType());
                            }
                        }
                    }
            );
        } catch (XposedHelpers.ClassNotFoundError e) {
            XposedBridge.log("Fuck Sensors: " + lpparam.packageName + ": android.hardware.SensorManager class not found");
        }
    }
}
