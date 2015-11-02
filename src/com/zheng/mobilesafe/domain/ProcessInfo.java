package com.zheng.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * ������Ϣ��ҵ��bean 
 */
public class ProcessInfo {
	
	/**
	 * checkbox��״̬
	 */
	private boolean checked;
	
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * ����ͼ�꣬Ӧ�ó���ͼ��
	 */
	private Drawable appIcon;
	/**
	 * Ӧ�ó�������
	 */
	private String appName;
	/**
	 * Ӧ�ó������
	 */
	private String packName;
	/**
	 * �ڴ�ռ�õĴ�С
	 */
	private long memSize;
	/**
	 * �Ƿ����û�����
	 */
	private boolean userProcess;
	/**
	 * @return the appIcon
	 */
	public Drawable getAppIcon() {
		return appIcon;
	}
	/**
	 * @param appIcon the appIcon to set
	 */
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the packName
	 */
	public String getPackName() {
		return packName;
	}
	/**
	 * @param packName the packName to set
	 */
	public void setPackName(String packName) {
		this.packName = packName;
	}
	/**
	 * @return the memSize
	 */
	public long getMemSize() {
		return memSize;
	}
	/**
	 * @param memSize the memSize to set
	 */
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	/**
	 * @return the userProcess
	 */
	public boolean isUserProcess() {
		return userProcess;
	}
	/**
	 * @param userProcess the userProcess to set
	 */
	public void setUserProcess(boolean userProcess) {
		this.userProcess = userProcess;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessInfo [checked=" + checked + ", appName=" + appName
				+ ", packName=" + packName + ", memSize=" + memSize
				+ ", userProcess=" + userProcess + "]";
	}
	
	

}
