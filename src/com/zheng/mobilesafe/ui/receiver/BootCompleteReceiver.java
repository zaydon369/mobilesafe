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
		// ��������,�ж��Ƿ�����������
		if (sp.getBoolean("protecting", false)) {
			// ������������.�鿴��ǰSIM���ͱ�����Ƿ�һ��
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			String SIM = tm.getSimSerialNumber();
			if (SIM.equals(sp.getString("SIM", ""))) {
				// ���SIM��һ��,���ò���
			} else {

				// �����һ�¸���ȫ���뷢�͸�֪����
				SmsManager smsManager = SmsManager.getDefault();
				// �ռ��˵�ַ,�������ĵ�ַ,��������,����,����
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
