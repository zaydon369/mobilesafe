package com.zheng.mobilesafe.domain;

public class BlackNumberInfo {
	/**
	 * �Զ����
	 */
	private String id;
	/**
	 * �绰����
	 */
	private String phone;
	/**
	 * ����ģʽ
	 */
	private String mode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumberInfo [id=" + id + ", phone=" + phone + ", mode="
				+ mode + "]";
	}
	
	
}
