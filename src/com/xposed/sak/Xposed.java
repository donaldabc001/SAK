package com.xposed.sak;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Xposed {

	public static void hook_methods(Class<?> clazz, String methodName, XC_MethodHook xmh) {
		try {
			for (Method method : clazz.getDeclaredMethods())
				if (method.getName().equals(methodName)) {
					XposedBridge.hookMethod(method, xmh);
				}
		} catch (Exception e) {
			XposedBridge.log(e);
		}
	}

	public static void hook_methods(String className, ClassLoader classLoader, String methodName,
			XC_MethodHook xmh) {
		Class<?> clazz = XposedHelpers.findClass(className, classLoader);
		hook_methods(clazz, methodName, xmh);
	}

	public static Object getSystemProperties(String methodName, String propName, Object defaultValue) {
		Class<?> clazz = XposedHelpers.findClass("android.os.SystemProperties",
				XposedBridge.BOOTCLASSLOADER);
		Object object = XposedHelpers.callStaticMethod(clazz, methodName, propName, defaultValue);
		return object;
	}
	
	public static void RootCommand(String command) {
		java.lang.Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			new DataInputStream(process.getInputStream());
			os.writeBytes(command + "\n");
			os.flush();
			os.writeBytes("exit \n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != process) {
					process.destroy();
				}

				if (null != os) {
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
