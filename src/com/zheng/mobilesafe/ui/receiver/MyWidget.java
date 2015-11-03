package com.zheng.mobilesafe.ui.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.zheng.mobilesafe.service.UpdateWidgetService;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {

		System.out.println("启用Widget");
		//启动界面更新服务
		Intent service =new Intent(context,UpdateWidgetService.class);
		context.startService(service);
		super.onEnabled(context);
	}

	
	@Override
	public void onDisabled(Context context) {
		System.out.println("关掉Widget");
		//停止服务
		Intent service =new Intent(context,UpdateWidgetService.class);
		context.stopService(service);
		super.onDisabled(context);
	}
	

}
