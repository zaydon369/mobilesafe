package com.zheng.mobilesafe.activities.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * ����Ϣ�Ĺ�����
 */
public class PackageInfoUtils {
	/**
	 * ��ȡӦ�õİ汾����Ϣ
	 * @param context ������
	 * @return ��ǰ�İ汾����Ϣ
	 */
	public static String getPackageVersion(Context context) {
		try {
			// ���������ĵõ���������,�õ�����Ϣ
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			// ͨ������Ϣ�õ��汾����
			String version = packageInfo.versionName;

			return version;
		} catch (NameNotFoundException e) {
			//һ�㲻������쳣,��Ϊ��ͨ�������Ļ�ȡ������
			e.printStackTrace();
			return "�����汾��ʧ��";
		}
	}

}
