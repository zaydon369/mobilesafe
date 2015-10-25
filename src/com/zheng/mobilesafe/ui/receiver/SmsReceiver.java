package com.zheng.mobilesafe.ui.receiver;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
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
	private SharedPreferences sp;
	//收到短信是调用
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("config",context.MODE_PRIVATE);
		Log.i(TAG, "接收到短信.....SMS..");
		// TODO Auto-generated method stub
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object obj :objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body=smsMessage.getMessageBody();
			if("#*gps*#".equalsIgnoreCase(body)){
				Log.i(TAG, "位置追踪");
				//得到发送人的号码,将号码保存到配置文件中
				Editor edit = sp.edit();
				edit.putString("gpsNumber", smsMessage.getDisplayOriginatingAddress());
				edit.commit();
				Log.i(TAG, "开启Location服务....");
				//调用服务,将位置发给号码
				Intent service=new Intent(context,LocationService.class);
				context.startService(service);
				abortBroadcast();//将广播终止
			}else if("#*bjyy*#".equalsIgnoreCase(body)){
				Log.i(TAG, "报警音乐");
				//播放月亮之上的报警音乐
				MediaPlayer.create(context, R.raw.ylzs).start();
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
