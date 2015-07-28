package com.xposed.sak.modules;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class StatusBarOutTemp {

	private static String TAG = "StatusBarOutTemp";
	private final static String ACTION_CAN_SET_CHANGED = "com.luyuan.vehicleinformation.canset.changed";
	private final static String ACTION_OUT_TEMP_CHANGED = "com.luyuan.vehicleinformation.outtemp.changed";
	private static Context mContext;
	private static OutTempBroadcastReceiver mBroadcastReceiver;
	private static float outTemp;
	private static TextView mOutTempView;

	private static boolean enable = false;

	private static String packageName = "com.luyuan.vehicleinformation";
	private static XSharedPreferences xSharedPreferences = getSharedPreferences();

	private static XSharedPreferences getSharedPreferences() {
		XSharedPreferences xSharedPreferences = new XSharedPreferences(packageName, "setting");
		xSharedPreferences.makeWorldReadable();
		return xSharedPreferences;
	}

	private static boolean isShowOutTemp() {
		xSharedPreferences.reload();
		return xSharedPreferences.getBoolean("showOutTemp", false);
	}

	public static void handleLoadPackage(LoadPackageParam lpparam) {

		if (!enable || !lpparam.packageName.equals("com.android.systemui"))
			return;

		XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {

			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				mContext = (Context) param.thisObject;
				mBroadcastReceiver = new OutTempBroadcastReceiver();
				IntentFilter mFilter = new IntentFilter();
				mFilter.addAction(ACTION_CAN_SET_CHANGED);
				mFilter.addAction(ACTION_OUT_TEMP_CHANGED);
				mContext.registerReceiver(mBroadcastReceiver, mFilter);
			}

		});
	}

	private static class OutTempBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (ACTION_CAN_SET_CHANGED.equals(intent.getAction())) {
				if (isShowOutTemp()) {
					mOutTempView.setVisibility(View.VISIBLE);
				} else {
					mOutTempView.setVisibility(View.GONE);
				}
			} else if (ACTION_OUT_TEMP_CHANGED.equals(intent.getAction())) {
				outTemp = intent.getFloatExtra("outTemp", 0);
				mOutTempView.setText(String.format("%.1f℃", outTemp));
			}
		}

	}

	public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
		if (!enable || !resparam.packageName.equals("com.android.systemui"))
			return;

		resparam.res.hookLayout("com.android.systemui", "layout", "status_bar",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
						// TODO Auto-generated method stub
						ViewGroup mViewGroup = (ViewGroup) liparam.view.findViewById(liparam.res
								.getIdentifier("status_bar_contents", "id", "com.android.systemui"));
						View mView1 = mViewGroup.findViewById(liparam.res.getIdentifier(
								"ly_current_activity_icon", "id", "com.android.systemui"));
						View mView2 = mViewGroup.findViewById(liparam.res.getIdentifier(
								"ly_current_activity_label", "id", "com.android.systemui"));
						int index = 0;
						if (mView1 != null && mView1.getVisibility() != View.GONE) {
							index++;
						}

						if (mView2 != null && mView2.getVisibility() != View.GONE) {
							index++;
						}

						mOutTempView = new TextView(mContext);
						mOutTempView.setVisibility(View.GONE);
						mOutTempView.setText(String.format("%.1f℃", outTemp));
						mOutTempView.setTextSize(14);
						LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						mParams.leftMargin = 20;
						mViewGroup.addView(mOutTempView, index, mParams);
					}
				});
	}
}
