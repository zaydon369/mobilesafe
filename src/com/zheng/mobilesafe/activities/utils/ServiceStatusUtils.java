package com.zheng.mobilesafe.activities.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	/**
	 * �жϷ����Ƿ�����
	 * @param Context ������
	 * @param serviceFullName �������ȫ��
	 * @return ����״̬
	 */
	public  static boolean isServiceRunning(Context context,String serviceFullName){
		
		//�õ�ϵͳ�������е����з���
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		//list����Ŷ��ٸ�����
		List<RunningServiceInfo> infos=am.getRunningServices(250);
		//��������������Ĳ�����˵���÷���������״̬
		for(RunningServiceInfo info:infos){
			//��ȡ��ǰ�����class�ļ�,����ʹ�������
			if(info.service.getClassName().equals(serviceFullName)){
				return true;
			}
		}
		return false;
		
	}

}
