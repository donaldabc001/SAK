package com.xposed.sak.modules;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GaodeNavi {

	private static String TAG = "GaodeNavi";

	private final static String ACTION_GPS_PLAY = "android.NaviOne.voiceprotocol";
	private static Context mContext;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!lpparam.packageName.equals("com.autonavi.xmgd.navigator"))
			return;

		XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				mContext = (Context) param.thisObject;
			}

		});

		Xposed.hook_methods("android.media.AudioManager", XposedBridge.BOOTCLASSLOADER,
				"requestAudioFocus", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						mContext.sendBroadcast(new Intent(ACTION_GPS_PLAY).putExtra(
								"VOICEPROTOCOL", "play"));
						return 1;
					}
				});

		Xposed.hook_methods("android.media.AudioManager", XposedBridge.BOOTCLASSLOADER,
				"abandonAudioFocus", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						mContext.sendBroadcast(new Intent(ACTION_GPS_PLAY).putExtra(
								"VOICEPROTOCOL", "stop"));
						return 1;
					}
				});

		XposedBridge.hookAllConstructors(MediaPlayer.class, new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				final MediaPlayer mediaPlayer = (MediaPlayer) param.thisObject;
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
			}
		});
	}

}
