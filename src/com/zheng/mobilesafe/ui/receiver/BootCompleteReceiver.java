package com.zheng.mobilesafe.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		// 开机启动,判断是否开启防盗保护
		if (sp.getBoolean("protecting", false)) {
			// 开启防盗保护.查看当前SIM卡和保存的是否一致
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			String SIM = tm.getSimSerialNumber();
			if (SIM.equals(sp.getString("SIM", ""))) {
				// 如果SIM卡一致,不用操作
			} else {

				// 如果不一致给安全号码发送告知短信
				SmsManager smsManager = SmsManager.getDefault();
				// 收件人地址,服务中心地址,短信内容,发送,到达
				smsManager
						.sendTextMessage(
								sp.getString("safeNumber", ""),
								null,
								"SOS,this number...remember...please...My phone maybe lost....",
								null, null);
			}

		}

	}

}
