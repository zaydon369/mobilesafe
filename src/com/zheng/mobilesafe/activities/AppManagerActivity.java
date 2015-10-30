package com.zheng.mobilesafe.activities;

import java.text.Format;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.SystemInfoUtils;

public class AppManagerActivity extends Activity {
	TextView tv_appmanager_internal;
	TextView tv_appmanager_sdcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_appmanager_internal = (TextView) findViewById(R.id.tv_appmanager_internal);
		tv_appmanager_sdcard = (TextView) findViewById(R.id.tv_appmanager_sdcard);
		//��ȡϵͳ�ڴ�,��tv����ֵ
		Long internal=SystemInfoUtils.getInternalStorageFreeSize();
		Long sd=SystemInfoUtils.getSDStorageFreeSize();
		//���ļ���С��ʽ�����׶�����
		tv_appmanager_internal.setText("����洢ʣ��:"+Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD��ʣ��:"+Formatter.formatFileSize(getApplicationContext(), sd));
	}
}
