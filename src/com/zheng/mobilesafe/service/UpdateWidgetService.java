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
		// �õ�����С�ؼ�������
		final AppWidgetManager awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				// ����Ҫ����������̵�view����,����Ҫ���ֻ���ʿ�����view���ݸ�����
				// Ҫ�������һ�����������,Remoteviews
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				// ����views������
				views.setTextViewText(
						R.id.tv_widget_process_count,
						"�������еĳ���:"
								+ ProcessInfoProvider.getRunningProcessInfos(
										getApplicationContext()).size());

				views.setTextViewText(
						R.id.tv_widget_process_memory,
						"ʣ���ڴ�ռ�:"
								+ Formatter.formatFileSize(
										getApplicationContext(),
										getAvailMemory()));
				System.out.println("ˢ���ڴ�");
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

	// ��ȡʣ���ڴ�
	/**
	 * ��ȡ�ֻ����õ��ڴ�ռ�
	 * 
	 * @return
	 */
	private long getAvailMemory() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		// ��ȡϵͳ��ǰ���ڴ���Ϣ�����ݷ���outInfo��������
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

}
