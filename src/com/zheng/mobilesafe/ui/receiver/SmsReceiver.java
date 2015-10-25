package com.zheng.mobilesafe.ui.receiver;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.Setup4Activity;
import com.zheng.mobilesafe.service.LocationService;

import android.app.admin.DevicePolicyManager;
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
 * ����ָ���
 * 
 * @author asus
 * 
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";
	private SharedPreferences sp;

	// �յ������ǵ���
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		Log.i(TAG, "���յ�����.....SMS..");
		//�жϷ��������Ƿ���,
		if(!sp.getBoolean("protecting", false)){
			//���û�������������Ͳ�ִ�������ָ��
			return;
		}
		// TODO Auto-generated method stub
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String body = smsMessage.getMessageBody();
			if ("#*gps*#".equalsIgnoreCase(body)) {
				Log.i(TAG, "λ��׷��");
				// �õ������˵ĺ���,�����뱣�浽�����ļ���
				Editor edit = sp.edit();
				edit.putString("gpsNumber",
						smsMessage.getDisplayOriginatingAddress());
				edit.commit();
				Log.i(TAG, "����Location����....");
				// ���÷���,��λ�÷�������
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				abortBroadcast();// ���㲥��ֹ
			} else if ("#*bjyy*#".equalsIgnoreCase(body)) {
				Log.i(TAG, "��������");
				// ��������֮�ϵı�������
				MediaPlayer.create(context, R.raw.ylzs).start();
				abortBroadcast();// ���㲥��ֹ
			} else if ("#*qksj*#".equalsIgnoreCase(body)) {
				Log.i(TAG, "�������...");
				//�ж��Ƿ��й���ԱȨ��
				if(MyAdmin.isAdmin(context)){
				//�õ��豸������
				DevicePolicyManager dpm = (DevicePolicyManager) context
						.getSystemService(context.DEVICE_POLICY_SERVICE);
				//�������
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				}
				abortBroadcast();// ���㲥��ֹ
			} else if ((boolean) "#*ycsp*#".equalsIgnoreCase(body)) {
				Log.i(TAG, "Զ������..");
				//�ж��Ƿ��й���ԱȨ��
				if(MyAdmin.isAdmin(context)){
				//�õ��豸������
				DevicePolicyManager dpm = (DevicePolicyManager) context
						.getSystemService(context.DEVICE_POLICY_SERVICE);
				dpm.resetPassword("123", 0);
				}
				abortBroadcast();// ���㲥��ֹ
			}
		}
	}

}
