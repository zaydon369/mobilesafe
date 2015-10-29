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
		// 去电状态监听.利用广播
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
		// 来电状态监听
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// 取消去电监听
		unregisterReceiver(receiver);
		receiver = null;
		// 取消来电监听
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
				Log.i(Tag, "电话铃响");
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
	 * 去电广播监听
	 * 
	 * @author asus
	 * 
	 */
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取拨打出去的号码
			String address = AddressDBDao
					.findLocation(context, getResultData());
			Log.i(Tag, "去电号码:" + address);
			// Toast.makeText(context, address, 0).show();
			showMyToast(address);
		}

	}

	void showMyToast(String text) {
		// 将自定义好的xml布局文件加载到view
		view = View.inflate(getApplicationContext(), R.layout.toast_address,
				null);
		TextView tv_toast_address = (TextView) view
				.findViewById(R.id.tv_toast_address);

		// 给View设置一个触摸的监听器
		view.setOnTouchListener(new OnTouchListener() {
			int startX ;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN://按下
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://移动
					//Log.i(Tag,"手在屏幕上的偏移量startX:"+startX);
					//Log.i(Tag,"手在屏幕上的偏移量startY:"+startY);
					int newX = (int) event.getRawX();
					Log.i(Tag,"手在屏幕上的偏移量newX:"+newX);
					int newY = (int) event.getRawY();
					Log.i(Tag,"手在屏幕上的偏移量newY:"+newY);
					int dx = newX - startX;
					int dy = newY - startY;
					params.x+=dx;
					params.y+=dy;
					wm.updateViewLayout(view, params);
					Log.i(Tag,"手在屏幕上的偏移量dx:"+dx);
					Log.i(Tag,"手在屏幕上的偏移量dy:"+dy);
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
					//将当前位置设为开始位置
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://离开

					break;
				}
				return true;
			}
		});
		tv_toast_address.setText(text);
		params = new LayoutParams();
		//坐标系为窗体的左上角,
		params.gravity=Gravity.LEFT+Gravity.TOP;
		params.x = 20;//水平方向的距离
		params.y = 20;//竖直方向的距离
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				//| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		//窗体类型,可以被触摸和点击的系统级别窗体,清单文件声明权限,SYSTEM_ALERT_WINDOW
				params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		//params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}
}
