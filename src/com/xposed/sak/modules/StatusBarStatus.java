package com.xposed.sak.modules;

import android.app.Activity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class StatusBarStatus {

	public static void handleLoadPackage(LoadPackageParam lpparam) {

		XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				Activity mActivity = (Activity) param.thisObject;
				LayoutParams attrs = mActivity.getWindow().getAttributes();
				if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
					Xposed.RootCommand("setprop persist.sys.statusbar.show 0");
				} else {
					Xposed.RootCommand("setprop persist.sys.statusbar.show 1");
				}
			}
		});
	}

}
