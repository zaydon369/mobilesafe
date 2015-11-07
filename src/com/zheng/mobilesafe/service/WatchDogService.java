package com.zheng.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import com.zheng.mobilesafe.activities.EnterPasswordActivity;
import com.zheng.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchDogService extends Service {

	private ActivityManager am;
	private AppLockDao dao;
	// 用于控制循环,一直监视最新栈
	private boolean flag;
	// 加锁应用程序列表
	private ArrayList<String> allLockapps;
	private IntentReceiver receiver;
	//临时取消保护的列表
	ArrayList<String> whiteApps;
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
		//初始化广播接受者
		receiver = new IntentReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.zheng.mobilesafe.tempstopprotect");
		//注册广播接受者
		registerReceiver(receiver, filter);
		//初始化白名单列表
		whiteApps=new ArrayList<String>();
		new Thread() {
			//将循环写在子线程里,避免anr,应用程序无响应
			public void run() {
				// 当前正在运行的栈的列表,提取出来,不用每次循环都创建,减少内存开销
				List<RunningTaskInfo> runningTasks;
				String packageName;
				// 循环查找当前的栈
				while (flag) {
					//睡眠,避免监控频繁造成浪费
					SystemClock.sleep(30);
					// 获取正在运行的activity的栈
					runningTasks = am.getRunningTasks(1);
					// 得到当前正在运行的包名
					packageName = runningTasks.get(0).topActivity
							.getPackageName();
					// 当当前运行的程序在加锁列表时,弹出提示
					if (allLockapps.contains(packageName)) {
						//判断是否在白名单里面
						if(whiteApps!=null && whiteApps.contains(packageName)){
							//如果在白名单里面直接跳过本次循环
							continue;
						};
						//弹出对话框,提示输入密码
						Intent intent=new Intent(getApplicationContext(), EnterPasswordActivity.class);
						intent.putExtra("packageName", packageName);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					
				}
			};
		}.start();

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		flag = false;
		if(receiver!=null){
		unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
	/**
	 * 接收密码框传过来的取消暂时保护的包名
	 * @author asus
	 *
	 */
class IntentReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(whiteApps.size()>1){//暂且将白名单列表设置为1
			whiteApps.remove(0);
		}
		whiteApps.add(intent.getStringExtra("packageName"));
	}}
}
