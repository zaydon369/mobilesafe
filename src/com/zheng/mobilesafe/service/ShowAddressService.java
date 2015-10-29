package com.zheng.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.zheng.mobilesafe.db.dao.AddressDBDao;

public class ShowAddressService extends Service {
	private static final String Tag = "MyPhoneListener";
	private OutCallReceiver receiver;
	TelephonyManager tm;
	MyPhoneListener listener;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		//ȥ��״̬����.���ù㲥
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		//����״̬����
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		//ȡ��ȥ�����
		unregisterReceiver(receiver);
		receiver=null;
		//ȡ���������
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:

				break;
			case TelephonyManager.CALL_STATE_RINGING://
				Log.i(Tag, "�绰����");
				String address=AddressDBDao.findLocation(getApplicationContext(), incomingNumber);
			Toast.makeText(getApplicationContext(), address, 0).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}
	/**
	 * ȥ��㲥����
	 * @author asus
	 *
	 */
	private class OutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//��ȡ�����ȥ�ĺ���
			String address=	AddressDBDao.findLocation(context, getResultData());
			Log.i(Tag, "ȥ�����:"+address);
			Toast.makeText(context, address, 0).show();
		}
		
	}
	
}
