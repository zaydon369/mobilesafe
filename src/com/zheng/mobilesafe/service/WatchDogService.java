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
	// ���ڿ���ѭ��,һֱ��������ջ
	private boolean flag;
	// ����Ӧ�ó����б�
	private ArrayList<String> allLockapps;
	private IntentReceiver receiver;
	//��ʱȡ���������б�
	ArrayList<String> whiteApps;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		dao = new AppLockDao(getApplicationContext());
		allLockapps = dao.findAllLockapps();
		// ��û�м�������ʱ��Ϊfalse,ֹͣѭ��,����cup����
		flag = !(allLockapps.isEmpty());
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//��ʼ���㲥������
		receiver = new IntentReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.zheng.mobilesafe.tempstopprotect");
		//ע��㲥������
		registerReceiver(receiver, filter);
		//��ʼ���������б�
		whiteApps=new ArrayList<String>();
		new Thread() {
			//��ѭ��д�����߳���,����anr,Ӧ�ó�������Ӧ
			public void run() {
				// ��ǰ�������е�ջ���б�,��ȡ����,����ÿ��ѭ��������,�����ڴ濪��
				List<RunningTaskInfo> runningTasks;
				String packageName;
				// ѭ�����ҵ�ǰ��ջ
				while (flag) {
					//˯��,������Ƶ������˷�
					SystemClock.sleep(30);
					// ��ȡ�������е�activity��ջ
					runningTasks = am.getRunningTasks(1);
					// �õ���ǰ�������еİ���
					packageName = runningTasks.get(0).topActivity
							.getPackageName();
					// ����ǰ���еĳ����ڼ����б�ʱ,������ʾ
					if (allLockapps.contains(packageName)) {
						//�ж��Ƿ��ڰ���������
						if(whiteApps!=null && whiteApps.contains(packageName)){
							//����ڰ���������ֱ����������ѭ��
							continue;
						};
						//�����Ի���,��ʾ��������
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
	 * ��������򴫹�����ȡ����ʱ�����İ���
	 * @author asus
	 *
	 */
class IntentReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(whiteApps.size()>1){//���ҽ��������б�����Ϊ1
			whiteApps.remove(0);
		}
		whiteApps.add(intent.getStringExtra("packageName"));
	}}
}
