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
		/** 文件的总数 */
		void fileCounts(int count);

		/** 当前的进度 */
		void fileProgress(int pro);

		/** 病毒文件 */
		void virusFile(String virusFile);

		/** 安全文件 */
		void safetyFile(String safetyFile);
	}

	/**
	 * 扫描文件夹
	 */
	public static void scanVirus(final Context context, final CallBackup backup) {

		new Thread() {
			public void run() {
				// 得到所有app的安装路径
				PackageManager pm = context.getPackageManager();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				// 进度的最大值,也就是有多少个APP文件目录
				backup.fileCounts(infos.size());
				// 扫描的进度值
				int pro = 1;
				// 遍历所有App路径
				for (PackageInfo info : infos) {
					String path = info.applicationInfo.sourceDir;
					String md5 = Md5Utils.getFileMd5(path);
					// 查询数据库是不是有这个记录
					String virus = ScanVirusDao.ScanVirus(context, md5);
					// 判断文件是否是病毒
					if (TextUtils.isEmpty(virus)) {
						// 如果为空说明没病毒
						backup.safetyFile(info.applicationInfo.loadLabel(pm)+"");
					} else {
						// 否则说明是病毒
						backup.virusFile(info.applicationInfo.loadLabel(pm)+"");
					}
					// 进度自身+1
					backup.fileProgress(pro++);
				}
			};
		}.start();

	}

}
