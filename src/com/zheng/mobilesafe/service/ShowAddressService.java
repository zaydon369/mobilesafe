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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.db.dao.AddressDBDao;

public class ShowAddressService extends Service {
	private WindowManager wm;
	WindowManager.LayoutParams params = null ;
	/**
	 * ��ʾ�ڴ����ϵ�view
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
		// ��ȡ�������ķ���
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		// ȥ��״̬����.���ù㲥
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		// ����״̬����
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// ȡ��ȥ�����
		unregisterReceiver(receiver);
		receiver = null;
		// ȡ���������
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					wm.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING://
				Log.i(Tag, "�绰����");
				String address = AddressDBDao.findLocation(
						getApplicationContext(), incomingNumber);
				// Toast.makeText(getApplicationContext(), address, 0).show();
				showMyToast(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * ȥ��㲥����
	 * 
	 * @author asus
	 * 
	 */
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ��ȡ�����ȥ�ĺ���
			String address = AddressDBDao
					.findLocation(context, getResultData());
			Log.i(Tag, "ȥ�����:" + address);
			// Toast.makeText(context, address, 0).show();
			showMyToast(address);
		}

	}

	void showMyToast(String text) {
		// ���Զ���õ�xml�����ļ����ص�view
		view = View.inflate(getApplicationContext(), R.layout.toast_address,
				null);
		TextView tv_toast_address = (TextView) view
				.findViewById(R.id.tv_toast_address);

		// ��View����һ�������ļ�����
		view.setOnTouchListener(new OnTouchListener() {
			int startX ;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN://����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://�ƶ�
					//Log.i(Tag,"������Ļ�ϵ�ƫ����startX:"+startX);
					//Log.i(Tag,"������Ļ�ϵ�ƫ����startY:"+startY);
					int newX = (int) event.getRawX();
					Log.i(Tag,"������Ļ�ϵ�ƫ����newX:"+newX);
					int newY = (int) event.getRawY();
					Log.i(Tag,"������Ļ�ϵ�ƫ����newY:"+newY);
					int dx = newX - startX;
					int dy = newY - startY;
					params.x+=dx;
					params.y+=dy;
					wm.updateViewLayout(view, params);
					Log.i(Tag,"������Ļ�ϵ�ƫ����dx:"+dx);
					Log.i(Tag,"������Ļ�ϵ�ƫ����dy:"+dy);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
					if(params.x>wm.getDefaultDisplay().getWidth()-view.getWidth()){
						params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y>wm.getDefaultDisplay().getHeight()-view.getHeight()){
						params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					wm.updateViewLayout(view, params);
					//����ǰλ����Ϊ��ʼλ��
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://�뿪

					break;
				}
				return true;
			}
		});
		tv_toast_address.setText(text);
		params = new LayoutParams();
		//����ϵΪ��������Ͻ�,
		params.gravity=Gravity.LEFT+Gravity.TOP;
		params.x = 20;//ˮƽ����ľ���
		params.y = 20;//��ֱ����ľ���
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				//| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//��������,���Ա������͵����ϵͳ������,�嵥�ļ�����Ȩ��,SYSTEM_ALERT_WINDOW
				params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		//params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}
}
