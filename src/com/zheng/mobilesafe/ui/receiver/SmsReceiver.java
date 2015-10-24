package com.zheng.mobilesafe.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * 短信指令处理
 * @author asus
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";

	//收到短信是调用
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "接收到短信.....SMS..");
		// TODO Auto-generated method stub
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object obj :objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body=smsMessage.getMessageBody();
			if("#*wzzz*#".equalsIgnoreCase(body)){
				Log.i(TAG, "位置追踪");
				abortBroadcast();//将广播终止
			}else if("#*bjyy*#".equalsIgnoreCase(body)){
				Log.i(TAG, "报警音乐");
				abortBroadcast();//将广播终止
			}else if("#*qksj*#".equalsIgnoreCase(body)){
				Log.i(TAG, "清空数据...");
				abortBroadcast();//将广播终止
			}else if("#*ycsp*#".equalsIgnoreCase(body)){
				Log.i(TAG, "远程锁屏..");
				abortBroadcast();//将广播终止
			}
		}
	}

}
