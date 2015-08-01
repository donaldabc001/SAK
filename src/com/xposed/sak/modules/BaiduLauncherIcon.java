package com.xposed.sak.modules;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

import com.luyuan.xposed.R;

public class BaiduLauncherIcon {
    private final static String TAG = "BaiduLauncherIcon";
    
    private final static String PACKAGE_NAME = "com.baidu.navi";
    private static String MODULE_PATH = null;

    public static void initZygote(StartupParam startupParam) throws Throwable {
        Log.d(TAG, "initZygote");
        MODULE_PATH = startupParam.modulePath;
        Log.d(TAG, "MODULE_PATH: " + MODULE_PATH);
    }
    
    public static void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE_NAME))
            return;
        Log.d(TAG, "package name: " + resparam.packageName);
        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.setReplacement(PACKAGE_NAME, "drawable", "ic_launcher", modRes.fwd(R.drawable.ic_launcher));
    }
}
