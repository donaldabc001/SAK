package com.xposed.sak;

import com.xposed.sak.modules.BaiduNavi;
import com.xposed.sak.modules.CLDNavi;
import com.xposed.sak.modules.CanBusLauncherIcon;
import com.xposed.sak.modules.ClearNotification;
import com.xposed.sak.modules.ComponentTimeOut;
import com.xposed.sak.modules.EnableWifi;
import com.xposed.sak.modules.GalleryConstructorSlides;
import com.xposed.sak.modules.GaodeNavi;
import com.xposed.sak.modules.MonitorBroadcast;
import com.xposed.sak.modules.MonitorStreamValue;
import com.xposed.sak.modules.StatusBarOutTemp;
import com.xposed.sak.modules.StatusBarStatus;
import com.xposed.sak.modules.StopAppStart;

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
		ComponentTimeOut.handleLoadPackage(lpparam);
		GalleryConstructorSlides.handleLoadPackage(lpparam);
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		// TODO Auto-generated method stub
		StatusBarOutTemp.handleInitPackageResources(resparam);
	}
}
