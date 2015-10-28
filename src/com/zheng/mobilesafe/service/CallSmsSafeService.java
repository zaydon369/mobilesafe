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
	 * �ڲ��Ķ��Ź㲥������,������ض��ŵ�״̬
	 */
	class InnerSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "���յ�����");
			// �õ����ŵ�����,�жϷ������Ƿ��ں�������
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//�õ����ŷ�����
				String sender = smsMessage.getOriginatingAddress();
				//��ϵͳ��ѯ���������ں��������Ƿ���ģʽ2��3
				String mode=dao.find(sender);
				if("2".equals(mode)||"3".equals(mode)){
					//���ڵĻ�
					Log.i(TAG,"���ֺ���������,����");
					abortBroadcast();
				}
			}
		}

	}

	private static final String TAG = "CallSmsSafeService";
	private BlackNumberDao dao;
	// �绰����ļ���
	MyListener listener;
	TelephonyManager tm;
	// ���ŷ���
	InnerSmsReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "ɧ�����ط����ѿ���");
		// �����������ط���

		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
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
		// ��������Ϊ��
		listener = null;
		super.onDestroy();
	}

	/**
	 * �绰״̬����
	 */
	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// ����״̬

				break;
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
				// ͨ�����ݿ�鿴�������ĺ����Ƿ��Ǻ���������
				String mode = dao.find(incomingNumber);
				// �ж�����ģʽ,�����1����3˵���ǵ绰����
				if ("1".equals(mode) || "3".equals(mode)) {
					// �Ҷϵ绰
					endCall();
					Log.i(TAG, "�Ҷϵ绰");
					// �Ҷϵ�ʱ��,�����ݿ���������ɾ��
					deleteCallLog(incomingNumber);
				}

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// ��ͨ�绰״̬

				break;

			}

			super.onCallStateChanged(state, incomingNumber);
		}

		/**
		 * ɾ�����м�¼
		 * 
		 * @param incomingNumber
		 */
		private void deleteCallLog(final String incomingNumber) {
			// ���ݹ۲���
			final ContentResolver resolver = getContentResolver();
			final Uri uri = Uri.parse("content://call_log/calls");
			// ���������ṩ�߹۲����ݿ�仯
			resolver.registerContentObserver(uri, true, new ContentObserver(
					new Handler()) {
				@Override
				public void onChange(boolean selfChange) {
					// �����ݹ۲��߹۲쵽���ݿ�����ݱ仯��ʱ����õķ���.
					// ���ݿ���:contacts2,����:calls,�ֶ�number(����),type(1,����),new(1,�¼�¼)
					resolver.delete(uri, "number=? and type='1' and new='1'",
							new String[] { incomingNumber });
					super.onChange(selfChange);

				}

			});

		}

		/**
		 * �Ҷϵ绰�ķ���
		 */
		private void endCall() {

			try {
				// ͨ������õ�ϵͳ���صĹҶϵ绰�ķ���
				Class clazz = Class.forName("android.os.ServiceManager");
				// ͨ���ֽ����ļ��õ����صķ���
				Method method = clazz.getDeclaredMethod("getService",
						String.class);
				method.setAccessible(true);
				// ���õõ������ط���.�õ�getService(TELEPHONY_SERVICE);
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
