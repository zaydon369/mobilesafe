package com.zheng.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

	/**
	 * 内部的短信广播接收者,用来监控短信的状态
	 */
	class InnerSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "接收到短信");
			// 得到短信的内容,判断发件人是否在黑名单内
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//得到短信发件人
				String sender = smsMessage.getOriginatingAddress();
				//到系统查询看发件人在黑名单中是否在模式2或3
				String mode=dao.find(sender);
				if("2".equals(mode)||"3".equals(mode)){
					//如在的话
					Log.i(TAG,"发现黑名单短信,拦截");
					abortBroadcast();
				}
			}
		}

	}

	private static final String TAG = "CallSmsSafeService";
	private BlackNumberDao dao;
	// 电话服务的监听
	MyListener listener;
	TelephonyManager tm;
	// 短信服务
	InnerSmsReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "骚扰拦截服务已开启");
		// 开启短信拦截服务

		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		// 创建dao
		dao = new BlackNumberDao(getApplicationContext());
		// 获得电话管理者
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		// 通过电话管理者监听电话的呼叫状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "骚扰拦截服务已关闭");
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		// 将监听设为空
		listener = null;
		super.onDestroy();
	}

	/**
	 * 电话状态监听
	 */
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态

				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				// 通过数据库查看拨进来的号码是否是黑名单号码
				String mode = dao.find(incomingNumber);
				// 判断拦截模式,如果是1或者3说明是电话拦截
				if ("1".equals(mode) || "3".equals(mode)) {
					// 挂断电话
					endCall();
					Log.i(TAG, "挂断电话");
					// 挂断的时候,到数据库把来电号码删除
					deleteCallLog(incomingNumber);
				}

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话状态

				break;

			}

			super.onCallStateChanged(state, incomingNumber);
		}

		/**
		 * 删除呼叫记录
		 * 
		 * @param incomingNumber
		 */
		private void deleteCallLog(final String incomingNumber) {
			// 内容观察者
			final ContentResolver resolver = getContentResolver();
			final Uri uri = Uri.parse("content://call_log/calls");
			// 利用内容提供者观察数据库变化
			resolver.registerContentObserver(uri, true, new ContentObserver(
					new Handler()) {
				@Override
				public void onChange(boolean selfChange) {
					// 当内容观察者观察到数据库的内容变化的时候调用的方法.
					// 数据库名:contacts2,表名:calls,字段number(号码),type(1,呼进),new(1,新纪录)
					resolver.delete(uri, "number=? and type='1' and new='1'",
							new String[] { incomingNumber });
					super.onChange(selfChange);

				}

			});

		}

		/**
		 * 挂断电话的方法
		 */
		private void endCall() {

			try {
				// 通过反射得到系统隐藏的挂断电话的方法
				Class clazz = Class.forName("android.os.ServiceManager");
				// 通过字节码文件得到隐藏的方法
				Method method = clazz.getDeclaredMethod("getService",
						String.class);
				method.setAccessible(true);
				// 调用得到的隐藏方法.得到getService(TELEPHONY_SERVICE);
				IBinder iBinder = (IBinder) method.invoke(null,
						Context.TELEPHONY_SERVICE);
				//
				ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

				iTelephony.endCall();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
