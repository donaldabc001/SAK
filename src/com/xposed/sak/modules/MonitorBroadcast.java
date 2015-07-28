package com.xposed.sak.modules;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MonitorBroadcast {

	private static String TAG = "MonitorBroadcast";
	private static boolean enable = true;
	private static String monitorPackageName ;
	private static String packageName;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!enable)
			return;

		if (monitorPackageName != null && !lpparam.packageName.equals(monitorPackageName))
			return;

		packageName = lpparam.packageName;

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendBroadcast", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendBroadcast");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendBroadcastAsUser", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendBroadcastAsUser");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendOrderedBroadcast", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendOrderedBroadcast");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendOrderedBroadcastAsUser", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendOrderedBroadcastAsUser");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendStickyBroadcast", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendStickyBroadcast");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendStickyBroadcastAsUser", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendStickyBroadcastAsUser");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendStickyOrderedBroadcast", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendStickyOrderedBroadcast");
					}
				});

		Xposed.hook_methods("android.content.ContextWrapper", XposedBridge.BOOTCLASSLOADER,
				"sendStickyOrderedBroadcastAsUser", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						hookedSendBroadcast(param, "sendStickyOrderedBroadcastAsUser");
					}
				});

	}

	private static void hookedSendBroadcast(MethodHookParam param, String tag) {
		Intent intent = (Intent) param.args[0];
		Log.d(TAG, "----------");
		Log.d(TAG, tag + " package = " + packageName);
		Log.d(TAG, tag + " intent = " + intent.toString());
		Bundle mBundle = intent.getExtras();
		if (mBundle != null) {
			Log.d(TAG, tag + " mBundle = " + mBundle.toString());
		}
		Log.d(TAG, "----------");
	}
}
