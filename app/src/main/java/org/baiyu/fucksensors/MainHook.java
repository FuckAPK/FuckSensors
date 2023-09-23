package org.baiyu.fucksensors;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class MainHook implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.hookAllMethods(
                XposedHelpers.findClass("android.hardware.SensorManager ", lpparam.classLoader),
                "registerListener",
                XC_MethodReplacement.returnConstant(true)
        );
    }
}
