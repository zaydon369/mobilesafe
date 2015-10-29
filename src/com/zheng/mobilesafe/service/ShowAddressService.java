package com.zheng.mobilesafe.service;

import com.zheng.mobilesafe.db.dao.AddressDBDao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ShowAddressService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		MyPhoneListener listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private class MyPhoneListener extends PhoneStateListener {

		private static final String Tag = "MyPhoneListener";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:

				break;
			case TelephonyManager.CALL_STATE_RINGING://
				Log.i(Tag, "µÁª∞¡ÂœÏ");
				String address=AddressDBDao.findLocation(getApplicationContext(), incomingNumber);
			Toast.makeText(getApplicationContext(), address, 0).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}
}
