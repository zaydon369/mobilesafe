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
 * 获取进程的方法
 */
public class ProcessInfoProvider {
	/**
	 * 获取正在运行的进程信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<ProcessInfo> getRunningProcessInfos(Context context) {
		// 要返回的所有信息列表
		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		// 通过窗口管理器得到正在运行的进程信息
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos = am
				.getRunningAppProcesses();
		//获取包管理者,等下根据进程名称获取应用程序信息
		PackageManager pm = context.getPackageManager();
		//将得到的信息进行封装成我们想要的信息
		
		for(RunningAppProcessInfo info:runningAppProcessInfos){
			ProcessInfo processInfo=new ProcessInfo();
			//进程名字
			String processName = info.processName;
			processInfo.setAppName(processName);
			//这个看的有点晕,C语言实现的
			long memSize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty()*1024;
			processInfo.setMemSize(memSize);
			//通过进程名称得到安装程序的信息
			try {
				PackageInfo packInfo = pm.getPackageInfo(processName, 0);
				//图标
				Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
				processInfo.setAppIcon(appIcon);
				//应用名
				String appName = packInfo.applicationInfo.loadLabel(pm).toString();
				processInfo.setAppName(appName);
				//包名
				String packName=packInfo.applicationInfo.packageName;
				processInfo.setPackName(packName);
				if((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0){
					//系统进程
					processInfo.setUserProcess(false);
				}else{
					//用户进程
					processInfo.setUserProcess(true);
				}
			} catch (NameNotFoundException e) {

				e.printStackTrace();
			}
			//根据进程名称获取安装包信息
			processInfos.add(processInfo);
		}
		return processInfos;

	}
}
