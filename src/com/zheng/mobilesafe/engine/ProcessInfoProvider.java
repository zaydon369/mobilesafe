package com.zheng.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.zheng.mobilesafe.domain.ProcessInfo;

/**
 * ��ȡ���̵ķ���
 */
public class ProcessInfoProvider {
	/**
	 * ��ȡ�������еĽ�����Ϣ
	 * 
	 * @param context
	 * @return
	 */
	public static List<ProcessInfo> getRunningProcessInfos(Context context) {
		// Ҫ���ص�������Ϣ�б�
		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		// ͨ�����ڹ������õ��������еĽ�����Ϣ
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos = am
				.getRunningAppProcesses();
		//��ȡ��������,���¸��ݽ������ƻ�ȡӦ�ó�����Ϣ
		PackageManager pm = context.getPackageManager();
		//���õ�����Ϣ���з�װ��������Ҫ����Ϣ
		
		for(RunningAppProcessInfo info:runningAppProcessInfos){
			ProcessInfo processInfo=new ProcessInfo();
			//��������
			String processName = info.processName;
			processInfo.setAppName(processName);
			//��������е���,C����ʵ�ֵ�
			long memSize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty()*1024;
			processInfo.setMemSize(memSize);
			//ͨ���������Ƶõ���װ�������Ϣ
			try {
				PackageInfo packInfo = pm.getPackageInfo(processName, 0);
				//ͼ��
				Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
				processInfo.setAppIcon(appIcon);
				//Ӧ����
				String appName = packInfo.applicationInfo.loadLabel(pm).toString();
				processInfo.setAppName(appName);
				//����
				String packName=packInfo.applicationInfo.packageName;
				processInfo.setPackName(packName);
				if((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0){
					//ϵͳ����
					processInfo.setUserProcess(false);
				}else{
					//�û�����
					processInfo.setUserProcess(true);
				}
			} catch (NameNotFoundException e) {

				e.printStackTrace();
			}
			//���ݽ������ƻ�ȡ��װ����Ϣ
			processInfos.add(processInfo);
		}
		return processInfos;

	}
}
