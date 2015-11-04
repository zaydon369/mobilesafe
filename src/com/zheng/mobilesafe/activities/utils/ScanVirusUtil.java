package com.zheng.mobilesafe.activities.utils;

import java.io.ObjectInputStream.GetField;
import java.util.List;

import com.zheng.mobilesafe.db.dao.ScanVirusDao;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class ScanVirusUtil {
	public interface CallBackup {
		/** �ļ������� */
		void fileCounts(int count);

		/** ��ǰ�Ľ��� */
		void fileProgress(int pro);

		/** �����ļ� */
		void virusFile(String virusFile);

		/** ��ȫ�ļ� */
		void safetyFile(String safetyFile);
	}

	/**
	 * ɨ���ļ���
	 */
	public static void scanVirus(final Context context, final CallBackup backup) {

		new Thread() {
			public void run() {
				// �õ�����app�İ�װ·��
				PackageManager pm = context.getPackageManager();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				// ���ȵ����ֵ,Ҳ�����ж��ٸ�APP�ļ�Ŀ¼
				backup.fileCounts(infos.size());
				// ɨ��Ľ���ֵ
				int pro = 1;
				// ��������App·��
				for (PackageInfo info : infos) {
					String path = info.applicationInfo.sourceDir;
					String md5 = Md5Utils.getFileMd5(path);
					// ��ѯ���ݿ��ǲ����������¼
					String virus = ScanVirusDao.ScanVirus(context, md5);
					// �ж��ļ��Ƿ��ǲ���
					if (TextUtils.isEmpty(virus)) {
						// ���Ϊ��˵��û����
						backup.safetyFile(info.applicationInfo.loadLabel(pm)+"");
					} else {
						// ����˵���ǲ���
						backup.virusFile(info.applicationInfo.loadLabel(pm)+"");
					}
					// ��������+1
					backup.fileProgress(pro++);
				}
			};
		}.start();

	}

}
