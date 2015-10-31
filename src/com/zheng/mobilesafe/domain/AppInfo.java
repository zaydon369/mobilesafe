package com.zheng.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	/**包名*/
	String packageName;
	/**图标*/
	Drawable icon;
	/**应用的名称*/
	String appName;
	/**安装路径*/
	String apkPath;
	/**app大小*/
	long apkSize;
	public long getApkSize() {
		return apkSize;
	}
	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getApkPath() {
		return apkPath;
	}
	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}
	@Override
	public String toString() {
		return "AppInfo [packageName=" + packageName + ", appName=" + appName
				+ ", apkPath=" + apkPath + "]";
	}
	
	
}
