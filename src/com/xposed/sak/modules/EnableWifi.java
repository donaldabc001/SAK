package com.xposed.sak.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.xposed.sak.Xposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class EnableWifi {

	public static int index = 0;
	public static int count = getRepeatCount();

	public static int getRepeatCount() {
		return Build.VERSION.SDK_INT < 19 ? 1 : 2;
	}

	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if (!lpparam.packageName.equals("com.android.providers.settings"))
			return;

		XposedHelpers.findAndHookMethod(
				Build.VERSION.SDK_INT < 19 ? "com.android.server.WifiService"
						: "com.android.server.wifi.WifiSettingsStore",
				XposedBridge.BOOTCLASSLOADER, "getPersistedWifiState", new XC_MethodHook() {

					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						// TODO Auto-generated method stub

						int open = (Integer) Xposed.getSystemProperties("getInt", "persist.sys.wifi.open", 0);
						if (open == 0) {
							index++;
							if (index == count) {
								Xposed.RootCommand("setprop persist.sys.wifi.open 1");
							}
							String mProject = getProjectName();
							if (mProject != null && mProject.startsWith("aixing")) {
								param.setResult(1);
							}
						}
					}
				});
	}

	public static String getProjectName() {
		File sdCardDir = Environment.getExternalStorageDirectory();
		File saveDir = new File(sdCardDir, "Android/luyuan");
		File xmlFile = new File("/system/etc/luyuan_system_configuration.xml");
		if (saveDir.exists()) {
			File customFile = new File(saveDir, "luyuan_system_configuration.xml");
			if (customFile.exists()) {
				xmlFile = customFile;
			}
		}
		try {
			FileInputStream inputStream = new FileInputStream(xmlFile);
			String string = readXML(inputStream);
			inputStream.close();
			return string;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String readXML(InputStream inStream) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if (name.equalsIgnoreCase("ProjectName")) {
						return parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parser.next();
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
