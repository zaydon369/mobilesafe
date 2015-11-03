package com.zheng.mobilesafe.ui.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.zheng.mobilesafe.service.UpdateWidgetService;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {

		System.out.println("����Widget");
		//����������·���
		Intent service =new Intent(context,UpdateWidgetService.class);
		context.startService(service);
		super.onEnabled(context);
	}

	
	@Override
	public void onDisabled(Context context) {
		System.out.println("�ص�Widget");
		//ֹͣ����
		Intent service =new Intent(context,UpdateWidgetService.class);
		context.stopService(service);
		super.onDisabled(context);
	}
	

}
