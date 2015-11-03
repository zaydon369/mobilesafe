package com.zheng.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.engine.ProcessInfoProvider;
import com.zheng.mobilesafe.ui.receiver.MyWidget;

public class UpdateWidgetService extends Service {

	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// 得到桌面小控件管理器
		final AppWidgetManager awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				// 由于要更新桌面进程的view对象,所以要把手机卫士里面的view传递给桌面
				// 要求对象是一种特殊的类型,Remoteviews
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				// 更改views的内容
				views.setTextViewText(
						R.id.tv_widget_process_count,
						"正在运行的程序:"
								+ ProcessInfoProvider.getRunningProcessInfos(
										getApplicationContext()).size());

				views.setTextViewText(
						R.id.tv_widget_process_memory,
						"剩余内存空间:"
								+ Formatter.formatFileSize(
										getApplicationContext(),
										getAvailMemory()));
				System.out.println("刷新内存");
				//
				ComponentName provider = new ComponentName(
						getApplicationContext(), MyWidget.class);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 3000);
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		task.cancel();
		task=null;
		timer.cancel();
		timer=null;
		super.onDestroy();
	}

	// 获取剩余内存
	/**
	 * 获取手机可用的内存空间
	 * 
	 * @return
	 */
	private long getAvailMemory() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		// 获取系统当前的内存信息，数据放在outInfo对象里面
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

}
