package com.zheng.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

	private static final String TAG = "CallSmsSafeService";
	private BlackNumberDao dao;
	//�绰����ļ���
	MyListener listener;
	TelephonyManager tm;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "ɧ�����ط����ѿ���");
		// ����dao
		dao = new BlackNumberDao(getApplicationContext());
		// ��õ绰������
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		// ͨ���绰�����߼����绰�ĺ���״̬
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "ɧ�����ط����ѹر�");
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		//��������Ϊ��
		listener=null;
		super.onDestroy();
	}
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// ����״̬

				break;
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
				//ͨ�����ݿ�鿴�������ĺ����Ƿ��Ǻ���������
				String mode = dao.find(incomingNumber);
				// �ж�����ģʽ,�����1����3˵���ǵ绰����
				if("1".equals(mode)||"3".equals(mode)){
					Log.i(TAG, "�Ҷϵ绰");
				}

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// ��ͨ�绰״̬

				break;

			}

			super.onCallStateChanged(state, incomingNumber);
		}
	}


}
