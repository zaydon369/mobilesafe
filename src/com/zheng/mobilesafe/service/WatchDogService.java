package com.zheng.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import com.zheng.mobilesafe.activities.EnterPasswordActivity;
import com.zheng.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchDogService extends Service {

	private ActivityManager am;
	private AppLockDao dao;
	// 用于控制循环,一直监视最新栈
	private boolean flag;
	// 加锁应用程序列表
	private ArrayList<String> allLockapps;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		dao = new AppLockDao(getApplicationContext());
		allLockapps = dao.findAllLockapps();
		// 当没有加锁程序时就为false,停止循环,减少cup负担
		flag = !(allLockapps.isEmpty());
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		new Thread() {
			//将循环写在子线程里,避免anr,应用程序无响应
			public void run() {
				// 当前正在运行的栈的列表,提取出来,不用每次循环都创建,减少内存开销
				List<RunningTaskInfo> runningTasks;
				String packageName;
				// 循环查找当前的栈
				while (flag) {
					// 获取正在运行的activity的栈
					runningTasks = am.getRunningTasks(1);
					// 得到当前正在运行的包名
					packageName = runningTasks.get(0).topActivity
							.getPackageName();
					// 当当前运行的程序在加锁列表时,弹出提示
					if (allLockapps.contains(packageName)) {
						//System.out.println(packageName + "在加锁列表");
						//弹出对话框,提示输入密码
						Intent intent=new Intent(getApplicationContext(), EnterPasswordActivity.class);
						intent.putExtra("packageName", packageName);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					//睡眠,避免监控频繁造成浪费
					SystemClock.sleep(30);
				}
			};
		}.start();

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		flag = false;
		super.onDestroy();
	}

}
