package com.xposed.sak.modules;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class BaiduNavi {

	private static String TAG = "BaiduNavi";

	private final static String ACTION_GPS_PLAY = "android.NaviOne.voiceprotocol";
	private static Context mContext;

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!lpparam.packageName.equals("com.baidu.navi"))
			return;

		XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				mContext = (Context) param.thisObject;
			}

		});

		XposedHelpers.findAndHookMethod(View.class, "setMeasuredDimension", int.class, int.class,
				new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						if (param.thisObject instanceof ListView) {
							int width = (Integer) param.args[0];
							ListView mListView = (ListView) param.thisObject;
							String string = mListView.toString();

							if (string.contains("app:id/listView1") && width < 600) {
								if ((Integer) param.args[1] > 220)
									param.args[1] = 220;
							}
						}
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

		XposedBridge.hookAllConstructors(AudioTrack.class, new XC_MethodHook() {

			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				param.args[0] = AudioManager.STREAM_SYSTEM;
			}
		});
	}

}
