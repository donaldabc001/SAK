package com.xposed.sak.modules;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ComponentTimeOut {

	private final static String TAG = "ComponentTimeOut";

	private static int enable = (Integer) Xposed.getSystemProperties("getInt",
			"persist.sys.timeout.long", 0);

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (enable == 0)
			return;

		XposedBridge.hookAllConstructors(XposedHelpers.findClass(
				"com.android.server.am.BroadcastQueue", XposedBridge.BOOTCLASSLOADER),
				new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.beforeHookedMethod(param);
						if (param.args[2] instanceof Long) {
							param.args[2] = 3600000;
						}
					}

				});

		XposedBridge.hookAllConstructors(XposedHelpers.findClass(
				"com.android.server.am.ActiveServices", XposedBridge.BOOTCLASSLOADER),
				new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						XposedHelpers.setStaticIntField(XposedHelpers.findClass(
								"com.android.server.am.ActiveServices",
								XposedBridge.BOOTCLASSLOADER), "SERVICE_TIMEOUT", 3600000);
					}

				});
	}
}
