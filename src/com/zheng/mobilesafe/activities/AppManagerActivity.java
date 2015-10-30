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
		//获取系统内存,给tv设置值
		Long internal=SystemInfoUtils.getInternalStorageFreeSize();
		Long sd=SystemInfoUtils.getSDStorageFreeSize();
		//将文件大小格式化成易懂数据
		tv_appmanager_internal.setText("机身存储剩余:"+Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD卡剩余:"+Formatter.formatFileSize(getApplicationContext(), sd));
	}
}
