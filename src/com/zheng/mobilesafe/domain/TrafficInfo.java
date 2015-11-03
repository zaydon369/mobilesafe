package com.zheng.mobilesafe.domain;

/**
 * ������Ϣ
 * 
 * @author asus
 * 
 */
public class TrafficInfo {
	/** Ӧ������ */
	String appName;
	/** �ϴ����� */
	long TxBytes;
	/** �������� */
	long RxBytes;
	/**�����ܼ�*/
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
