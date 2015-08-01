package com.xposed.sak.modules;

import android.content.res.XModuleResources;
import android.util.Log;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

import com.luyuan.xposed.R;

public class HelloLayoutReplace {
    private final static String TAG = "HelloLayoutReplace";
    
    private static String MODULE_PATH = null;

    public static void initZygote(StartupParam startupParam) throws Throwable {
        Log.d(TAG, "initZygote");
        MODULE_PATH = startupParam.modulePath;
        Log.d(TAG, "MODULE_PATH: " + MODULE_PATH);
    }

    public static void handleInitPackageResources(InitPackageResourcesParam resparam)
            throws Throwable {
        if (!resparam.packageName.equals("com.example.helloandroid"))
            return;
        Log.d(TAG, "handleInitPackageResources, resparam.packageName: " + resparam.packageName);
        Log.d(TAG, "MODULE_PATH: " + MODULE_PATH);
        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH,
                resparam.res);
        resparam.res.setReplacement("com.example.helloandroid", "layout",
                "activity_main", modRes.fwd(R.layout.hello_main));
        resparam.res.setReplacement("com.example.helloandroid", "drawable",
                "ic_launcher", modRes.fwd(R.drawable.hello_launcher));
        resparam.res.setReplacement("com.example.helloandroid", "string",
                "app_name", modRes.fwd(R.string.hello_title));
    }
}