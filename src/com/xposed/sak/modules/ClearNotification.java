package com.xposed.sak.modules;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import android.app.Notification;
import android.os.Build;
import android.util.Log;

public class ClearNotification {

	private final static String TAG = "ClearNotification";
	private static String packageName;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		packageName = lpparam.packageName;
		Xposed.hook_methods("com.android.server.NotificationManagerService",
				XposedBridge.BOOTCLASSLOADER, "enqueueNotificationWithTag", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						super.beforeHookedMethod(param);
						Notification mNotification = (Notification) param.args[Build.VERSION.SDK_INT < 19 ? 3
								: 4];
						mNotification.flags = 0;
						Log.d(TAG, "packageName =  " + packageName);
						Log.d(TAG, "mNotification =  " + mNotification.toString());
					}
				});
	}
}
