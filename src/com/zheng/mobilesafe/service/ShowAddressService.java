package com.zheng.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.db.dao.AddressDBDao;

public class ShowAddressService extends Service {
	private WindowManager wm;
	/**
	 * 显示在窗体上的view
	 */
	private View view;
	
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
		// 获取窗体管理的服务
				wm = (WindowManager) getSystemService(WINDOW_SERVICE);
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
				if(view!=null){
					wm.removeView(view);
					view=null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING://
				Log.i(Tag, "电话铃响");
				String address=AddressDBDao.findLocation(getApplicationContext(), incomingNumber);
			//Toast.makeText(getApplicationContext(), address, 0).show();
				showMyToast(address);
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
			//Toast.makeText(context, address, 0).show();
			showMyToast(address);
		}
		
	}
	void showMyToast(String text){
		//将自定义好的xml布局文件加载到view
		view = View.inflate(getApplicationContext(), R.layout.toast_address, null);
		TextView tv_toast_address = (TextView) view.findViewById(R.id.tv_toast_address);
		tv_toast_address.setText(text);
		WindowManager.LayoutParams params = new LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}
}
