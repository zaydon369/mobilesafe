package com.zheng.mobilesafe.engine;

import java.io.File;
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
	public static List<AppInfo> getAllAppInfos(Context context) {
		// 通过上下文直接获取包管理器,管理手机里面的用用程序信息
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		List<AppInfo> appList = new ArrayList<AppInfo>();
		for (PackageInfo info : packages) {
			String packageName = info.packageName;// 包名
			Drawable icon = info.applicationInfo.loadIcon(pm);// Amp图标
			String appName = info.applicationInfo.loadLabel(pm).toString();// app名称
			String apkPath = info.applicationInfo.sourceDir;// 安装路径
			int flags = info.applicationInfo.flags;
			//给APP添加Uid信息
			int uid = info.applicationInfo.uid;
			
			// 添加APP信息
			AppInfo appInfo = new AppInfo();
			appInfo.setUid(uid);
			appInfo.setApkPath(apkPath);
			appInfo.setAppName(appName);
			appInfo.setIcon(icon);
			appInfo.setPackageName(packageName);
			// 通过APP安装的位置大小来判断应用程序的大小
			File file = new File(appInfo.getApkPath());
			long apkSize = file.length();
			appInfo.setApkSize(apkSize);
			// 判断是否系统应用
			if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				appInfo.setSystemApp(true);
			} else {
				appInfo.setSystemApp(false);
			}

			// 判断是否安装在内存
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				appInfo.setInRom(false);
			} else {
				appInfo.setInRom(true);
			}

			// 将APP信息存到list
			appList.add(appInfo);

		}

		return appList;
	}

}
