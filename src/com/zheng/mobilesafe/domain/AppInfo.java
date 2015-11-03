package com.zheng.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	/**APP��UID*/
	int uid;
	/**����*/
	String packageName;
	/**ͼ��*/
	Drawable icon;
	/**Ӧ�õ�����*/
	String appName;
	/**��װ·��*/
	String apkPath;
	/**app��С*/
	long apkSize;
	
	/**
	 * �Ƿ�ϵͳӦ��
	 */
	boolean systemApp;
	/**
	 * �Ƿ�װ���ֻ��ڴ�
	 */
	boolean inRom;
	
	
	
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
	
	
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the systemApp
	 */
	public boolean isSystemApp() {
		return systemApp;
	}
	/**
	 * @param systemApp the systemApp to set
	 */
	public void setSystemApp(boolean systemApp) {
		this.systemApp = systemApp;
	}
	/**
	 * @return the inRom
	 */
	public boolean isInRom() {
		return inRom;
	}
	/**
	 * @param inRom the inRom to set
	 */
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppInfo [packageName=" + packageName + ", appName=" + appName
				+ ", apkPath=" + apkPath + ", apkSize=" + apkSize
				+ ", systemApp=" + systemApp + ", inRom=" + inRom + "]";
	}
	
	
}
