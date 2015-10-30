package com.zheng.mobilesafe.activities.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * 系统信息工具
 */
public class SystemInfoUtils {
	/**
	 * 获取内部存储空间的总大小
	 * 
	 * @return
	 */
	public static long getInternalStorageSize() {
		// 得到系统目录
		File file = Environment.getDataDirectory();
		// 得到系统目录的总大小
		return file.getTotalSpace();
	}

	/**
	 * 
	 * @return
	 */
	public static long getInternalStorageFreeSize() {
		// 得到系统目录
		File file = Environment.getDataDirectory();
		// 得到系统目录的总大小
		return file.getFreeSpace();
	}

	/**
	 * 获取SD卡的总大小
	 * 
	 * @return
	 */
	public static long getSDStorageSize() {
		File file = Environment.getExternalStorageDirectory();
		return file.getTotalSpace();
	}

	/**
	 * 获取SD卡剩余空间
	 * 
	 * @return
	 */
	public static long getSDStorageFreeSize() {
		File file = Environment.getExternalStorageDirectory();
		return file.getFreeSpace();

	}
	
}
