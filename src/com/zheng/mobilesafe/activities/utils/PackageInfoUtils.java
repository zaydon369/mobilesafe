package com.zheng.mobilesafe.activities.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.RemoteException;

/**
 * 包信息的工具类
 */
public class PackageInfoUtils {
	/**
	 * 获取应用的版本号信息
	 * 
	 * @param context
	 *            上下文
	 * @return 当前的版本号信息
	 */
	public static String getPackageVersion(Context context) {
		try {
			// 根据上下文得到包管理器,得到包信息
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			// 通过包信息得到版本名称
			String version = packageInfo.versionName;

			return version;
		} catch (NameNotFoundException e) {
			// 一般不会出现异常,因为是通过上下文获取到包名
			e.printStackTrace();
			return "解析版本号失败";
		}
	}

}
