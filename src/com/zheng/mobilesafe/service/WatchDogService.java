package com.zheng.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.zheng.mobilesafe.activities.EnterPasswordActivity;
import com.zheng.mobilesafe.db.dao.AppLockDao;

public class WatchDogService extends Service {

	private ActivityManager am;
	private AppLockDao dao;
	// 用于控制循环,一直监视最新栈
	private boolean flag;
	// 加锁应用程序列表
	private ArrayList<String> allLockapps;
	private IntentReceiver receiver;
	// 临时取消保护的列表
	ArrayList<String> whiteApps;
	private AppLockObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// 获取数据库数据,操作数据库是耗时操作,在初始化时操作一次就够了
		dao = new AppLockDao(getApplicationContext());
		allLockapps = dao.findAllLockapps();
		initWatchDog();

		super.onCreate();
	}

	/**
	 * 初始化看门狗服务
	 */
	private void initWatchDog() {
		// 初始化内容观察者
		observer = new AppLockObserver(new Handler());
		// 就是内容观察者的Uri
		Uri uri = Uri.parse("content://com.zheng.mobilesafe.applockdb");
		getContentResolver().registerContentObserver(uri, true, observer);
		// 当没有加锁程序时就为false,停止循环,减少cup负担
		flag = !(allLockapps.isEmpty());
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// 初始化广播接受者
		receiver = new IntentReceiver();
		IntentFilter filter = new IntentFilter();
		// 接收密码提示框发出的广播
		filter.addAction("com.zheng.mobilesafe.tempstopprotect");
		// 接收锁屏广播
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		// 接听开屏广播
		filter.addAction(Intent.ACTION_SCREEN_ON);
		// 注册广播接受者
		registerReceiver(receiver, filter);
		// 初始化白名单列表
		whiteApps = new ArrayList<String>();
		//开始巡视
		startWatchDog();
	}

	/**
	 * 看门狗开始监听,因为当while循环停止后,子线程会消失,flag再为true时,不会自行启动
	 */
	private void startWatchDog() {
		new Thread() {
			// 将循环写在子线程里,避免anr,应用程序无响应
			public void run() {
				// 当前正在运行的栈的列表,提取出来,不用每次循环都创建,减少内存开销
				List<RunningTaskInfo> runningTasks;
				String packageName;
				// 循环查找当前的栈
				while (flag) {
					// 睡眠,避免监控频繁造成浪费
					SystemClock.sleep(30);
					// 获取正在运行的activity的栈
					runningTasks = am.getRunningTasks(1);
					// 得到当前正在运行的包名
					packageName = runningTasks.get(0).topActivity
							.getPackageName();
					System.out.println("看门狗正在扫描中...");
					// 当当前运行的程序在加锁列表时,弹出提示
					if (allLockapps.contains(packageName)) {
						// 判断是否在白名单里面
						if (whiteApps != null
								&& whiteApps.contains(packageName)) {
							// 如果在白名单里面直接跳过本次循环
							continue;
						}
						;
						// 弹出对话框,提示输入密码
						Intent intent = new Intent(getApplicationContext(),
								EnterPasswordActivity.class);
						intent.putExtra("packageName", packageName);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}

				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		flag = false;
		if (observer != null) {
			// 当服务停止时,停止内容观察者的监听
			getContentResolver().unregisterContentObserver(observer);
		}
		super.onDestroy();
	}

	/**
	 * 接收密码框传过来的取消暂时保护的包名
	 * 
	 * @author asus
	 * 
	 */
	class IntentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// 当收到密码输入框发出的确认广播后,将包设为白名单
			if ("com.zheng.mobilesafe.tempstopprotect".equals(action)) {
				if (whiteApps.size() >= 1) {// 暂且将白名单列表设置为1
					whiteApps.remove(0);
				}
				System.out.println("正在将XXX设置为白名单");
				whiteApps.add(intent.getStringExtra("packageName"));
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				// 开启屏幕时,再开启服务,省电
				initWatchDog();

			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				// 屏幕关闭时就停止
				onDestroy();
			}

		}

	}

	class AppLockObserver extends ContentObserver {

		public AppLockObserver(Handler handler) {
			super(handler);

		}

		/**
		 * 内容观察者发现数据变化调用的方法
		 */
		@Override
		public void onChange(boolean selfChange) {
			// 重新查找数据库,刷新加锁列表
			allLockapps = dao.findAllLockapps();
			// 当没有加锁程序时就为false,停止循环,减少cup负担
			flag = !(allLockapps.isEmpty());
			if (flag) {
				System.out.println("程序列表不为空,正在监听");
				startWatchDog();
			} else {//但是不能停止服务,不然当有程序时就无法自动监听
				System.out.println("程序列表为空,停止监听");
			}
			System.out.println("内容观察者发现数据库变化,正在刷新数据库..");
			super.onChange(selfChange);
		}
	}
}
