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
		//去电状态监听.利用广播
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		//来电状态监听
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		//取消去电监听
		unregisterReceiver(receiver);
		receiver=null;
		//取消来电监听
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
				Log.i(Tag, "电话铃响");
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
	 * 去电广播监听
	 * @author asus
	 *
	 */
	private class OutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//获取拨打出去的号码
			String address=	AddressDBDao.findLocation(context, getResultData());
			Log.i(Tag, "去电号码:"+address);
			Toast.makeText(context, address, 0).show();
		}
		
	}
	
}
