package com.zheng.mobilesafe.ui.receiver;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

public class MyAdmin extends DeviceAdminReceiver {
	/**
	 * �򿪹���ԱȨ�����ý���
	 * 
	 * @param context
	 *            ������
	 * @return �Ƿ�ɹ�������������ԱȨ��
	 */
	public static void addAdmin(final Context context) {
		// ����һ����Ϣ�Ի���
		 AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("����ԱȨ������:");
		builder.setMessage("�Ƿ�ȥ�������ԱȨ��");
		//���ȷ����ť,�������öԻ���
		builder.setPositiveButton("ȥ����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				//DevicePolicyManager.
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						new ComponentName(context, MyAdmin.class));
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("�´ΰ�", null);
		builder.show();

		

	}
	/**
	 * �Ƴ�����ԱȨ��
	 * @param context ������
	 */
	public static void removeAdmin(final Context context) {
		// ����һ����Ϣ�Ի���
				 AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("����ԱȨ������:");
				builder.setMessage("�Ƿ��Ƴ�����ԱȨ��");
				//���ȷ����ť,�������öԻ���
				builder.setPositiveButton("�����Ƴ�", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//�Ƴ�����ԱȨ��
						DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
						ComponentName who = new ComponentName(context, MyAdmin.class);
						dpm.removeActiveAdmin(who);
					}
				});
				builder.setNegativeButton("�´ΰ�", null);
				builder.show();
		
	}
	/**
	 * �жϵ�ǰ�����Ƿ��й���ԱȨ��
	 * 
	 * @param context
	 *            ������
	 * @return �Ƿ�ӵ�й���ԱȨ��,true��,falseû��
	 */
	public static boolean isAdmin(Context context) {
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(context.DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(context, MyAdmin.class);
		return dpm.isAdminActive(who);
	}
}
