package com.xposed.sak.modules;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CanBusLauncherIcon {

	private static String TAG = "CanBusLauncherIcon";

	private static String packageName = "com.luyuan.vehicleinformation";
	private static String[] activitys;
	private static XSharedPreferences xSharedPreferences = getSharedPreferences();

	private static XSharedPreferences getSharedPreferences() {
		XSharedPreferences xSharedPreferences = new XSharedPreferences(packageName, "setting");
		xSharedPreferences.makeWorldReadable();
		return xSharedPreferences;
	}

	private static void initActivityList() {
		String activityClass = xSharedPreferences.getString("activityClass", null);
		if (activityClass == null)
			return;
		activitys = activityClass.split(",");
	}

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!lpparam.packageName.equals("com.android.launcher"))
			return;

		initActivityList();

		Xposed.hook_methods("com.android.launcher2.AllAppsList", lpparam.classLoader, "add",
				new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.beforeHookedMethod(param);
						String activityShow = xSharedPreferences.getString("activityShow", null);
						if (activityShow == null)
							return;

						ComponentName componentName = (ComponentName) XposedHelpers.findField(
								param.args[0].getClass(), "componentName").get(param.args[0]);
						String className = componentName.getClassName();

						for (int i = 0; i < activitys.length; i++) {
							String name = packageName + "." + activitys[i];

							if (className.equals(name)) {
								if (activityShow.charAt(i) == '0') {
									Log.d(TAG, "add skip " + name);
									param.setResult(null);
								}
								break;
							}
						}
					}
				});

		Xposed.hook_methods("com.android.launcher2.LauncherModel", lpparam.classLoader,
				"getShortcutInfo", new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.beforeHookedMethod(param);

						String activityShow = xSharedPreferences.getString("activityShow", null);
						if (activityShow == null)
							return;

						Intent intent = (Intent) param.args[1];
						String className = intent.getComponent().getClassName();

						for (int i = 0; i < activitys.length; i++) {
							String name = packageName + "." + activitys[i];

							if (className.equals(name)) {
								if (activityShow.charAt(i) == '0') {
									Log.d(TAG, "getShortcutInfo skip " + name);
									param.setResult(null);
								}
								break;
							}
						}
					}
				});
	}
}
