package com.xposed.sak.modules;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import de.robv.android.xposed.IXposedHookCmdInit.StartupParam;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

import com.luyuan.xposed.R;

public class BaiduLauncherIcon {
    
    private final static String PACKAGE_NAME = "com.baidu.navi";
    
    public static void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE_NAME))
            return;
        Log.d("BaiduLauncherIcon", "package name: " + resparam.packageName);
        XModuleResources modRes = XModuleResources.createInstance(PACKAGE_NAME, resparam.res);
        resparam.res.setReplacement(PACKAGE_NAME, "drawable", "ic_launcher", modRes.fwd(R.drawable.ic_launcher));
    }
}
