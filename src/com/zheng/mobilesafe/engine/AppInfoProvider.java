package com.zheng.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.zheng.mobilesafe.domain.AppInfo;

/**
 * 业务方法,用于获取所有的系统程序信息
 */
public class AppInfoProvider {
	public static List<AppInfo> getAllAppInfos(Context context){
		//通过上下文直接获取包管理器,管理手机里面的用用程序信息
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		List<AppInfo> appList=new ArrayList<AppInfo>();
		for(PackageInfo info:packages){
		String packageName = info.packageName;//包名
		Drawable icon = info.applicationInfo.loadIcon(pm);//app图标
		String appName = info.applicationInfo.loadLabel(pm).toString();//app名称
		String apkPath = info.applicationInfo.sourceDir;//安装路径
		AppInfo appInfo=new AppInfo();
		appInfo.setApkPath(apkPath);
		appInfo.setAppName(appName);
		appInfo.setIcon(icon);
		appInfo.setPackageName(packageName);
		appList.add(appInfo);
		}
		
		
		
		return appList;
	}

}
