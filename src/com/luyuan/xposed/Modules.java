package com.luyuan.xposed;

import com.luyuan.xposed.modules.BaiduNavi;
import com.luyuan.xposed.modules.CLDNavi;
import com.luyuan.xposed.modules.ClearNotification;
import com.luyuan.xposed.modules.EnableWifi;
import com.luyuan.xposed.modules.GaodeNavi;
import com.luyuan.xposed.modules.CanBusLauncherIcon;
import com.luyuan.xposed.modules.MonitorBroadcast;
import com.luyuan.xposed.modules.MonitorStreamValue;
import com.luyuan.xposed.modules.StatusBarOutTemp;
import com.luyuan.xposed.modules.StatusBarStatus;
import com.luyuan.xposed.modules.StopAppStart;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Modules implements IXposedHookZygoteInit, IXposedHookLoadPackage,
		IXposedHookInitPackageResources {

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		// TODO Auto-generated method stub
		MonitorBroadcast.handleLoadPackage(lpparam);
		MonitorStreamValue.handleLoadPackage(lpparam);
		BaiduNavi.handleLoadPackage(lpparam);
		GaodeNavi.handleLoadPackage(lpparam);
		CLDNavi.handleLoadPackage(lpparam);
		StatusBarStatus.handleLoadPackage(lpparam);
		ClearNotification.handleLoadPackage(lpparam);
		CanBusLauncherIcon.handleLoadPackage(lpparam);
		StatusBarOutTemp.handleLoadPackage(lpparam);
		EnableWifi.handleLoadPackage(lpparam);
		StopAppStart.handleLoadPackage(lpparam);
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		// TODO Auto-generated method stub
		StatusBarOutTemp.handleInitPackageResources(resparam);
	}
}
