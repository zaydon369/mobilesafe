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
	// ���ڿ���ѭ��,һֱ��������ջ
	private boolean flag;
	// ����Ӧ�ó����б�
	private ArrayList<String> allLockapps;
	private IntentReceiver receiver;
	// ��ʱȡ���������б�
	ArrayList<String> whiteApps;
	private AppLockObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// ��ȡ���ݿ�����,�������ݿ��Ǻ�ʱ����,�ڳ�ʼ��ʱ����һ�ξ͹���
		dao = new AppLockDao(getApplicationContext());
		allLockapps = dao.findAllLockapps();
		initWatchDog();

		super.onCreate();
	}

	/**
	 * ��ʼ�����Ź�����
	 */
	private void initWatchDog() {
		// ��ʼ�����ݹ۲���
		observer = new AppLockObserver(new Handler());
		// �������ݹ۲��ߵ�Uri
		Uri uri = Uri.parse("content://com.zheng.mobilesafe.applockdb");
		getContentResolver().registerContentObserver(uri, true, observer);
		// ��û�м�������ʱ��Ϊfalse,ֹͣѭ��,����cup����
		flag = !(allLockapps.isEmpty());
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// ��ʼ���㲥������
		receiver = new IntentReceiver();
		IntentFilter filter = new IntentFilter();
		// ����������ʾ�򷢳��Ĺ㲥
		filter.addAction("com.zheng.mobilesafe.tempstopprotect");
		// ���������㲥
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		// ���������㲥
		filter.addAction(Intent.ACTION_SCREEN_ON);
		// ע��㲥������
		registerReceiver(receiver, filter);
		// ��ʼ���������б�
		whiteApps = new ArrayList<String>();
		//��ʼѲ��
		startWatchDog();
	}

	/**
	 * ���Ź���ʼ����,��Ϊ��whileѭ��ֹͣ��,���̻߳���ʧ,flag��Ϊtrueʱ,������������
	 */
	private void startWatchDog() {
		new Thread() {
			// ��ѭ��д�����߳���,����anr,Ӧ�ó�������Ӧ
			public void run() {
				// ��ǰ�������е�ջ���б�,��ȡ����,����ÿ��ѭ��������,�����ڴ濪��
				List<RunningTaskInfo> runningTasks;
				String packageName;
				// ѭ�����ҵ�ǰ��ջ
				while (flag) {
					// ˯��,������Ƶ������˷�
					SystemClock.sleep(30);
					// ��ȡ�������е�activity��ջ
					runningTasks = am.getRunningTasks(1);
					// �õ���ǰ�������еİ���
					packageName = runningTasks.get(0).topActivity
							.getPackageName();
					System.out.println("���Ź�����ɨ����...");
					// ����ǰ���еĳ����ڼ����б�ʱ,������ʾ
					if (allLockapps.contains(packageName)) {
						// �ж��Ƿ��ڰ���������
						if (whiteApps != null
								&& whiteApps.contains(packageName)) {
							// ����ڰ���������ֱ����������ѭ��
							continue;
						}
						;
						// �����Ի���,��ʾ��������
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
			// ������ֹͣʱ,ֹͣ���ݹ۲��ߵļ���
			getContentResolver().unregisterContentObserver(observer);
		}
		super.onDestroy();
	}

	/**
	 * ��������򴫹�����ȡ����ʱ�����İ���
	 * 
	 * @author asus
	 * 
	 */
	class IntentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// ���յ���������򷢳���ȷ�Ϲ㲥��,������Ϊ������
			if ("com.zheng.mobilesafe.tempstopprotect".equals(action)) {
				if (whiteApps.size() >= 1) {// ���ҽ��������б�����Ϊ1
					whiteApps.remove(0);
				}
				System.out.println("���ڽ�XXX����Ϊ������");
				whiteApps.add(intent.getStringExtra("packageName"));
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				// ������Ļʱ,�ٿ�������,ʡ��
				initWatchDog();

			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				// ��Ļ�ر�ʱ��ֹͣ
				onDestroy();
			}

		}

	}

	class AppLockObserver extends ContentObserver {

		public AppLockObserver(Handler handler) {
			super(handler);

		}

		/**
		 * ���ݹ۲��߷������ݱ仯���õķ���
		 */
		@Override
		public void onChange(boolean selfChange) {
			// ���²������ݿ�,ˢ�¼����б�
			allLockapps = dao.findAllLockapps();
			// ��û�м�������ʱ��Ϊfalse,ֹͣѭ��,����cup����
			flag = !(allLockapps.isEmpty());
			if (flag) {
				System.out.println("�����б�Ϊ��,���ڼ���");
				startWatchDog();
			} else {//���ǲ���ֹͣ����,��Ȼ���г���ʱ���޷��Զ�����
				System.out.println("�����б�Ϊ��,ֹͣ����");
			}
			System.out.println("���ݹ۲��߷������ݿ�仯,����ˢ�����ݿ�..");
			super.onChange(selfChange);
		}
	}
}
