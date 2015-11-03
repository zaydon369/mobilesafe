package com.zheng.mobilesafe.domain;

/**
 * 流量信息
 * 
 * @author asus
 * 
 */
public class TrafficInfo {
	/** 应用名称 */
	String appName;
	/** 上传流量 */
	long TxBytes;
	/** 下载流量 */
	long RxBytes;
	/**流量总计*/
	long totalBytes;
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
	 * @return the txBytes
	 */
	public long getTxBytes() {
		return TxBytes;
	}
	/**
	 * @param txBytes the txBytes to set
	 */
	public void setTxBytes(long txBytes) {
		TxBytes = txBytes;
	}
	/**
	 * @return the rxBytes
	 */
	public long getRxBytes() {
		return RxBytes;
	}
	/**
	 * @param rxBytes the rxBytes to set
	 */
	public void setRxBytes(long rxBytes) {
		RxBytes = rxBytes;
	}
	/**
	 * @return the totalBytes
	 */
	public long getTotalBytes() {
		return totalBytes;
	}
	/**
	 * @param totalBytes the totalBytes to set
	 */
	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TrafficInfo [appName=" + appName + ", TxBytes=" + TxBytes
				+ ", RxBytes=" + RxBytes + ", totalBytes=" + totalBytes + "]";
	}
	
}
