package com.xposed.sak.modules;

import android.util.Log;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MonitorStreamValue {

	private static String TAG = "MonitorStreamValue";
	private static boolean enable = false;
	private static String monitorPackageName;
	private static String packageName;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!enable)
			return;

		if (monitorPackageName != null && !lpparam.packageName.equals(monitorPackageName))
			return;

		packageName = lpparam.packageName;

		Xposed.hook_methods("android.media.AudioManager", XposedBridge.BOOTCLASSLOADER,
				"setStreamVolume", new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

						super.beforeHookedMethod(param);

						int streamType, index, flags;

						streamType = (Integer) param.args[0];
						index = (Integer) param.args[1];
						flags = (Integer) param.args[2];

						Log.d(TAG, "----------");
						Log.d(TAG, "setStreamVolume " + "package = " + packageName);
						Log.d(TAG, "setStreamVolume " + "streamType = " + streamType);
						Log.d(TAG, "setStreamVolume " + "index = " + index);
						Log.d(TAG, "setStreamVolume " + "flags = " + flags);
						Log.d(TAG, "----------");
					}

				});
	}
}
