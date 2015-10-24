package com.zheng.mobilesafe.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * ����ָ���
 * @author asus
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";

	//�յ������ǵ���
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "���յ�����.....SMS..");
		// TODO Auto-generated method stub
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object obj :objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body=smsMessage.getMessageBody();
			if("#*wzzz*#".equalsIgnoreCase(body)){
				Log.i(TAG, "λ��׷��");
				abortBroadcast();//���㲥��ֹ
			}else if("#*bjyy*#".equalsIgnoreCase(body)){
				Log.i(TAG, "��������");
				abortBroadcast();//���㲥��ֹ
			}else if("#*qksj*#".equalsIgnoreCase(body)){
				Log.i(TAG, "�������...");
				abortBroadcast();//���㲥��ֹ
			}else if("#*ycsp*#".equalsIgnoreCase(body)){
				Log.i(TAG, "Զ������..");
				abortBroadcast();//���㲥��ֹ
			}
		}
	}

}
