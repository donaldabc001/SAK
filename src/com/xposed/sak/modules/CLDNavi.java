package com.xposed.sak.modules;

import com.xposed.sak.Xposed;

import android.app.Application;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CLDNavi {

	private static String TAG = "CLDNavi";
	private static boolean isFirst = true;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!lpparam.packageName.startsWith("cld.navi.c"))
			return;

		Xposed.hook_methods(Application.class, "onCreate", new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				isFirst = true;
			}

		});

		XposedBridge.hookAllConstructors(AudioTrack.class, new XC_MethodHook() {

			@Override
			protected void beforeHookedMethod(MethodHookParam param)
					throws Throwable {
				// TODO Auto-generated method stub
				param.args[0] = AudioManager.STREAM_SYSTEM;
			}
		});

		XposedBridge.hookAllConstructors(MediaPlayer.class,
				new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						// TODO Auto-generated method stub
						super.afterHookedMethod(param);
						final MediaPlayer mediaPlayer = (MediaPlayer) param.thisObject;
						mediaPlayer
								.setAudioStreamType(AudioManager.STREAM_SYSTEM);
					}
				});

		Xposed.hook_methods("android.media.AudioManager",
				XposedBridge.BOOTCLASSLOADER, "setStreamVolume",
				new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						super.beforeHookedMethod(param);
						if (isFirst) {
							isFirst = false;
							param.setResult(null);
						}
					}

				});

	}

}
