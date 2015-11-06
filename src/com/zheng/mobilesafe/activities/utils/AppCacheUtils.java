package com.zheng.mobilesafe.activities.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;

/**
 * ��ȡApp����Ĺ�����
 * 
 * @author asus
 * 
 */
public class AppCacheUtils {
	/**
	 * �ص��ӿ�,���ڴ��������Ͷ�Ӧ�Ļ����С
	 * 
	 * @author asus
	 * 
	 */
	public interface IcacheCallBack {
		/**
		 * �ص���ȡ�����С,��ҪȨ��:android.permission.GET_PACKAGE_SIZE
		 * 
		 * @param index
		 *            �±�,����forѭ����¼��ǰ��λ��,�������Ҫ,д0
		 * @param packName
		 *            ����
		 * @param cacheSize
		 *            �����С
		 */
		public void getCacheSize(int index, String packName, long cacheSize);
	}

	/**
	 * ��ȡ�����С�ķ���
	 * 
	 * @param index
	 *            �±�,����forѭ����¼��ǰ��λ��
	 * @param context
	 * @param packName
	 * @param icacheCallBack
	 */
	public static void getCache(final int index, Context context,
			final String packName, final IcacheCallBack icacheCallBack) {
		//
		PackageManager pm = context.getPackageManager();
		// �õ������������ֽ����ļ�
		Class c = PackageManager.class;
		// ͨ���ֽ����ļ���ȡ����
		try {
			Method method = c.getDeclaredMethod("getPackageSizeInfo",
					String.class,
					android.content.pm.IPackageStatsObserver.class);
			//����Ϊ��������
			method.setAccessible(true);
			// ʹ�ð�����������pm��getPackageSizeInfo�ķ���
			android.content.pm.IPackageStatsObserver.Stub stub = new android.content.pm.IPackageStatsObserver.Stub() {

				@Override
				public void onGetStatsCompleted(PackageStats pStats,
						boolean succeeded) throws RemoteException {
					// ���ӷ��䷽ʽ�õ��Ļ����С,����ȥ
					if(icacheCallBack!=null){
					icacheCallBack.getCacheSize(index, packName,
							pStats.cacheSize);
					}
				}
			};
			// ���÷���
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
	 * һ������ϵͳ���еĻ���,��ҪȨ��android.permission.CLEAR_APP_CACHE
	 * @param context ������
	 * @param cleanCachePackage �ӿڻص�,���ڼ�¼������İ���
	 */
	public static void cleanAllCache(Context context,final ICleanCachePackage cleanCachePackage) {
		// ͨ��������ð������ߵ�freeStorageAndNotify����
		PackageManager pm = context.getPackageManager();
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			//����Ϊ��������
			method.setAccessible(true);
			IPackageDataObserver.Stub stub=new  IPackageDataObserver.Stub() {
				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded)
						throws RemoteException {
					System.out.println(packageName+"�Ƿ�ɹ�"+succeeded);
					if(cleanCachePackage!=null){
						//��ִ�еĽ�������ȥ
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
	 * �ص��ӿ�,���ڴ�������
	 * @author asus
	 *
	 */
	interface ICleanCachePackage{
		/**
		 * ��������İ�
		 * @param packageName ����
		 * @param isSuccee �Ƿ����
		 */
		void cleanCacheInfo(String packageName , boolean isSuccee);
	}

}
