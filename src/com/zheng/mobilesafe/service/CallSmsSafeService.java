package com.zheng.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

	private static final String TAG = "CallSmsSafeService";
	private BlackNumberDao dao;
	//电话服务的监听
	MyListener listener;
	TelephonyManager tm;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "骚扰拦截服务已开启");
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
		//将监听设为空
		listener=null;
		super.onDestroy();
	}
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态

				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				//通过数据库查看拨进来的号码是否是黑名单号码
				String mode = dao.find(incomingNumber);
				// 判断拦截模式,如果是1或者3说明是电话拦截
				if("1".equals(mode)||"3".equals(mode)){
					//挂断电话
					endCall();
					Log.i(TAG, "挂断电话");
				}

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话状态

				break;

			}

			super.onCallStateChanged(state, incomingNumber);
		}
		/**
		 * 挂断电话的方法
		 */
		private void endCall() {
			
			try {
				//通过反射得到系统隐藏的挂断电话的方法
				Class clazz=Class.forName("android.os.ServiceManager");
				//通过字节码文件得到隐藏的方法
				Method method=clazz.getDeclaredMethod("getService", String.class);
				method.setAccessible(true);
				//调用得到的隐藏方法.得到getService(TELEPHONY_SERVICE);
				IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
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
