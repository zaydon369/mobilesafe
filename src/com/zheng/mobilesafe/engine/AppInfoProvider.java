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
 * ҵ�񷽷�,���ڻ�ȡ���е�ϵͳ������Ϣ
 */
public class AppInfoProvider {
	public static List<AppInfo> getAllAppInfos(Context context){
		//ͨ��������ֱ�ӻ�ȡ��������,�����ֻ���������ó�����Ϣ
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		List<AppInfo> appList=new ArrayList<AppInfo>();
		for(PackageInfo info:packages){
		String packageName = info.packageName;//����
		Drawable icon = info.applicationInfo.loadIcon(pm);//appͼ��
		String appName = info.applicationInfo.loadLabel(pm).toString();//app����
		String apkPath = info.applicationInfo.sourceDir;//��װ·��
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
