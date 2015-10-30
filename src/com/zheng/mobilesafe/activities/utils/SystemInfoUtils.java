package com.zheng.mobilesafe.activities.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * ϵͳ��Ϣ����
 */
public class SystemInfoUtils {
	/**
	 * ��ȡ�ڲ��洢�ռ���ܴ�С
	 * 
	 * @return
	 */
	public static long getInternalStorageSize() {
		// �õ�ϵͳĿ¼
		File file = Environment.getDataDirectory();
		// �õ�ϵͳĿ¼���ܴ�С
		return file.getTotalSpace();
	}

	/**
	 * 
	 * @return
	 */
	public static long getInternalStorageFreeSize() {
		// �õ�ϵͳĿ¼
		File file = Environment.getDataDirectory();
		// �õ�ϵͳĿ¼���ܴ�С
		return file.getFreeSpace();
	}

	/**
	 * ��ȡSD�����ܴ�С
	 * 
	 * @return
	 */
	public static long getSDStorageSize() {
		File file = Environment.getExternalStorageDirectory();
		return file.getTotalSpace();
	}

	/**
	 * ��ȡSD��ʣ��ռ�
	 * 
	 * @return
	 */
	public static long getSDStorageFreeSize() {
		File file = Environment.getExternalStorageDirectory();
		return file.getFreeSpace();

	}
	
}
