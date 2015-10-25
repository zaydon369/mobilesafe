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
	 * 打开管理员权限设置界面
	 * 
	 * @param context
	 *            上下文
	 * @return 是否成功开启超级管理员权限
	 */
	public static void openAdmin(final Context context) {
		// 定义一个消息对话框
		 AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("管理员权限提醒:");
		builder.setMessage("是否去设置管理员权限");
		//点击确定按钮,弹出设置对话框
		builder.setPositiveButton("去设置", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						new ComponentName(context, MyAdmin.class));
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("下次吧", null);
		builder.show();

		

	}

	//
	/**
	 * 判断当前程序是否有管理员权限
	 * 
	 * @param context
	 *            上下文
	 * @return 是否拥有管理员权限,true有,false没有
	 */
	public static boolean isAdmin(Context context) {
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(context.DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(context, MyAdmin.class);
		return dpm.isAdminActive(who);
	}
}
