package com.xposed.sak.modules;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class StopAppStart {

	private static String TAG = "StopAppStart";
	private static String packageName;
	private static ArrayList<ActivityInfo> mActivityInfos;
	private static Context mContext;
	private static Object mObject;

	private final static String ACTIVITY_INFO_CHANGED = "com.luyuan.activity_info_changed";
	private static String[] whitePackages = { "com.sohu.inputmethod.sogou",
			"com.luyuan.vehicleinformation" };

	private static BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initActivityInfos();
		}
	};

	private static void initActivityInfos() {
		mActivityInfos = new ArrayList<ActivityInfo>();
		ArrayList<ActivityInfo> mInfos = new ArrayList<ActivityInfo>();
		List<Object> mActivityVisibleInfos = (List<Object>) XposedHelpers.getObjectField(mObject,
				"mActivityVisibleInfos");
		for (Object object : mActivityVisibleInfos) {
			String className = (String) XposedHelpers.callMethod(object, "getClassName");
			String visibility = (String) XposedHelpers.callMethod(object, "getVisibility");
			ActivityInfo mActivityInfo = new ActivityInfo();
			mActivityInfo.setClassName(className);
			mActivityInfo.setVisible(visibility.equals("visible") ? true : false);
			mInfos.add(mActivityInfo);
		}

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager manager = mContext.getPackageManager();
		List<ResolveInfo> mResolveInfos = manager.queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : mResolveInfos) {
			ActivityInfo mActivityInfo = new ActivityInfo();
			mActivityInfo.setPackageName(resolveInfo.activityInfo.packageName);
			mActivityInfo.setClassName(resolveInfo.activityInfo.name);
			boolean isVisible = true;
			String name = resolveInfo.activityInfo.name;
			for (int i = 0; i < mInfos.size(); i++) {
				ActivityInfo mInfo = mInfos.get(i);
				if (name.equals(mInfo.getClassName())) {
					isVisible = mInfo.isVisible();
					break;
				}
			}
			mActivityInfo.setVisible(isVisible);
			mActivityInfos.add(mActivityInfo);
		}

		saveActivityInfos();

		for (ActivityInfo mActivityInfo : mActivityInfos) {
			Log.d(TAG, mActivityInfo.getPackageName() + " , " + mActivityInfo.getClassName()
					+ " , " + mActivityInfo.isVisible());
		}
	}

	private static boolean isInWhitePackages(String packageName) {
		for (int i = 0; i < whitePackages.length; i++) {
			if (packageName.equals(whitePackages[i]))
				return true;
		}
		return false;
	}

	private static void saveActivityInfos() {
		File file = new File("/data/activityinfos");
		while (file.exists()) {
			file.delete();
		}

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(mActivityInfos);
			oos.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (Exception e) {
			// TODO Auto-generated catch block
		} finally {
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
	}

	private static void loadActivityInfos() {
		File file = new File("/data/activityinfos");
		if (!file.exists()) {
			return;
		}

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			Object o = ois.readObject();
			mActivityInfos = (ArrayList<ActivityInfo>) o;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (EOFException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (Exception e) {
			// TODO Auto-generated catch block
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}

			if (mActivityInfos == null) {
				file.delete();
			}
		}
	}

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		packageName = lpparam.packageName;

		if (packageName.equals("com.luyuan.mcu")) {
			XposedBridge.hookAllConstructors(XposedHelpers.findClass(
					"com.luyuan.system_configuration.SystemDefaultConfiguration",
					lpparam.classLoader), new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					// TODO Auto-generated method stub
					super.afterHookedMethod(param);
					mObject = param.thisObject;
					Log.d(TAG, "SystemDefaultConfiguration" + " mObject = " + mObject);
					Log.d(TAG, "SystemDefaultConfiguration" + " mContext = " + mContext);
					initActivityInfos();
				}
			});
		}

		XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				super.afterHookedMethod(param);
				if (packageName.equals("com.luyuan.mcu")) {
					mContext = (Context) param.thisObject;
					mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(
							ACTIVITY_INFO_CHANGED));
				} else {
					if (mActivityInfos == null) {
						loadActivityInfos();
					}
				}
			}
		});

		Xposed.hook_methods("com.android.server.am.ActivityStack", XposedBridge.BOOTCLASSLOADER,
				"startActivityLocked", new XC_MethodHook() {

					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.beforeHookedMethod(param);
						if (mActivityInfos == null || mActivityInfos.size() == 0)
							return;
						if (param.args[1] instanceof Intent) {
							Intent intent = (Intent) param.args[1];
							ComponentName mComponentName = intent.getComponent();
							if (mComponentName != null) {
								String packageName = intent.getComponent().getPackageName();
								String className = intent.getComponent().getClassName();
								Log.d(TAG, "start activity: " + packageName + "/" + className);
								for (int i = 0; i < mActivityInfos.size(); i++) {
									ActivityInfo activityInfo = mActivityInfos.get(i);
									if (className.equals(activityInfo.getClassName())
											&& !activityInfo.isVisible()) {
										activityInfo.setVisible(true);
										break;
									}
								}
							}
						}
					}
				});

		Xposed.hook_methods("com.android.server.am.ActiveServices", XposedBridge.BOOTCLASSLOADER,
				"retrieveServiceLocked", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub
						super.beforeHookedMethod(param);
						if (mActivityInfos == null || mActivityInfos.size() == 0)
							return;
						if (param.args[0] instanceof Intent) {
							Intent intent = (Intent) param.args[0];
							ComponentName mComponentName = intent.getComponent();
							if (mComponentName != null) {
								String packageName = intent.getComponent().getPackageName();
								String className = intent.getComponent().getClassName();
								Log.d(TAG, "start service: " + packageName + "/" + className);
								if (isStopPackage(packageName)) {
									Log.d(TAG, "stop service: " + packageName + "/" + className);
									param.setResult(null);
								}
							}
						}
					}
				});
	}

	public static boolean isStopPackage(String packageName) {
		boolean stop = false;
		if (isInWhitePackages(packageName)) {
			return stop;
		}

		for (ActivityInfo activityInfo : mActivityInfos) {
			if (packageName.equals(activityInfo.getPackageName())) {
				stop = true;
				if (activityInfo.isVisible()) {
					stop = false;
					break;
				}
			}
		}
		return stop;
	}

	private static class ActivityInfo implements Serializable {
		private String packageName;
		private String className;
		private boolean isVisible;

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public boolean isVisible() {
			return isVisible;
		}

		public void setVisible(boolean isVisible) {
			this.isVisible = isVisible;
		}
	}
}
