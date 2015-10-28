package com.zheng.mobilesafe.activities.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	/**
	 * 判断服务是否运行
	 * @param Context 上下文
	 * @param serviceFullName 服务类的全名
	 * @return 运行状态
	 */
	public  static boolean isServiceRunning(Context context,String serviceFullName){
		
		//得到系统正在运行的所有服务
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		//list最多存放多少个服务
		List<RunningServiceInfo> infos=am.getRunningServices(250);
		//如果包含传进来的参数则说明该服务处于运行状态
		for(RunningServiceInfo info:infos){
			//获取当前服务的class文件,如果和传进来的
			if(info.service.getClassName().equals(serviceFullName)){
				return true;
			}
		}
		return false;
		
	}

}
