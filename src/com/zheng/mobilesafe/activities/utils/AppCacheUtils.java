package com.zheng.mobilesafe.activities.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;

/**
 * 获取App缓存的工具类
 * 
 * @author asus
 * 
 */
public class AppCacheUtils {
	/**
	 * 回调接口,用于传出包名和对应的缓存大小
	 * 
	 * @author asus
	 * 
	 */
	public interface IcacheCallBack {
		/**
		 * 回调获取缓存大小,需要权限:android.permission.GET_PACKAGE_SIZE
		 * 
		 * @param index
		 *            下标,用于for循环记录当前的位置,如果不需要,写0
		 * @param packName
		 *            包名
		 * @param cacheSize
		 *            缓存大小
		 */
		public void getCacheSize(int index, String packName, long cacheSize);
	}

	/**
	 * 获取缓存大小的方法
	 * 
	 * @param index
	 *            下标,用于for循环记录当前的位置
	 * @param context
	 * @param packName
	 * @param icacheCallBack
	 */
	public static void getCache(final int index, Context context,
			final String packName, final IcacheCallBack icacheCallBack) {
		//
		PackageManager pm = context.getPackageManager();
		// 得到包管理器的字节码文件
		Class c = PackageManager.class;
		// 通过字节码文件获取方法
		try {
			Method method = c.getDeclaredMethod("getPackageSizeInfo",
					String.class,
					android.content.pm.IPackageStatsObserver.class);
			//设置为允许连接
			method.setAccessible(true);
			// 使用包管理器对象pm的getPackageSizeInfo的方法
			android.content.pm.IPackageStatsObserver.Stub stub = new android.content.pm.IPackageStatsObserver.Stub() {

				@Override
				public void onGetStatsCompleted(PackageStats pStats,
						boolean succeeded) throws RemoteException {
					// 将从反射方式得到的缓存大小,传出去
					if(icacheCallBack!=null){
					icacheCallBack.getCacheSize(index, packName,
							pStats.cacheSize);
					}
				}
			};
			// 调用方法
			method.invoke(pm, packName, stub);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 一键清理系统所有的缓存,需要权限android.permission.CLEAR_APP_CACHE
	 * @param context 上下文
	 * @param cleanCachePackage 接口回调,用于记录被清除的包名
	 */
	public static void cleanAllCache(Context context,final ICleanCachePackage cleanCachePackage) {
		// 通过反射调用包管理者的freeStorageAndNotify方法
		PackageManager pm = context.getPackageManager();
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			//设置为允许连接
			method.setAccessible(true);
			IPackageDataObserver.Stub stub=new  IPackageDataObserver.Stub() {
				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded)
						throws RemoteException {
					System.out.println(packageName+"是否成功"+succeeded);
					if(cleanCachePackage!=null){
						//将执行的结果传输出去
					cleanCachePackage.cleanCacheInfo(packageName, succeeded);
					}
				}
			};
			method.invoke(pm,Long.MAX_VALUE,stub );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 回调接口,用于传输数据
	 * @author asus
	 *
	 */
	interface ICleanCachePackage{
		/**
		 * 被清理缓存的包
		 * @param packageName 包名
		 * @param isSuccee 是否清除
		 */
		void cleanCacheInfo(String packageName , boolean isSuccee);
	}

}
