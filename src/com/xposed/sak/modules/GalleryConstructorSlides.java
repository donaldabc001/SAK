package com.xposed.sak.modules;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GalleryConstructorSlides {

	private final static String TAG = "GalleryConstructorSlides";
	private static ClassLoader mClassLoader;
	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals("com.android.gallery3d"))
			return;
		
		mClassLoader = lpparam.classLoader;
		XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.android.gallery3d.app.SlideshowPage", mClassLoader), new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				super.afterHookedMethod(param);
				XposedHelpers.setStaticLongField(XposedHelpers.findClass(
						"com.android.gallery3d.app.SlideshowPage",
						mClassLoader), "SLIDESHOW_DELAY", 4500);
			}
		});
		XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.android.gallery3d.ui.SlideshowView", mClassLoader), new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				super.afterHookedMethod(param);
				XposedHelpers.setStaticIntField(XposedHelpers.findClass(
						"com.android.gallery3d.ui.SlideshowView",
						mClassLoader), "SLIDESHOW_DURATION", 4500);
				XposedHelpers.setStaticFloatField(XposedHelpers.findClass(
						"com.android.gallery3d.ui.SlideshowView",
						mClassLoader), "SCALE_SPEED", 0.80f);
				XposedHelpers.setStaticFloatField(XposedHelpers.findClass(
						"com.android.gallery3d.ui.SlideshowView",
						mClassLoader), "MOVE_SPEED", 0.80f);
			}
		});
	}
}
