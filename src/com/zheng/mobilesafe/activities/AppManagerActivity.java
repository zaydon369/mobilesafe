package com.zheng.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.SystemInfoUtils;
import com.zheng.mobilesafe.baseadapter.MyBaseAdapter;
import com.zheng.mobilesafe.baseholder.MyBaseHolder;
import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.engine.AppInfoProvider;

public class AppManagerActivity extends Activity {
	TextView tv_appmanager_internal;
	TextView tv_appmanager_sdcard;
	// 安装应用程序信息显示
	ListView lv_appmanager_app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_appmanager_internal = (TextView) findViewById(R.id.tv_appmanager_internal);
		tv_appmanager_sdcard = (TextView) findViewById(R.id.tv_appmanager_sdcard);
		// 获取系统内存,给tv设置值
		Long internal = SystemInfoUtils.getInternalStorageFreeSize();
		Long sd = SystemInfoUtils.getSDStorageFreeSize();
		// 将文件大小格式化成易懂数据
		tv_appmanager_internal.setText("机身存储剩余:"
				+ Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD卡剩余:"
				+ Formatter.formatFileSize(getApplicationContext(), sd));

		lv_appmanager_app = (ListView) findViewById(R.id.lv_appmanager_app);
		// 得到系统App的安装信息
		ArrayList<AppInfo> allAppInfos = (ArrayList<AppInfo>) AppInfoProvider
				.getAllAppInfos(getApplicationContext());
		MyAdapter adapter = new MyAdapter(allAppInfos);
		lv_appmanager_app.setAdapter(adapter);
	}

	class MyAdapter extends MyBaseAdapter<AppInfo> {
		TextView tv;

		public MyAdapter(ArrayList<AppInfo> mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {
			// TODO Auto-generated method stub
			return new MyBaseHolder() {

				@Override
				protected View initView() {
					// View view=View.inflate(getApplicationContext(), resource,
					// root);
					tv = new TextView(getApplicationContext());

					return tv;
				}

				@Override
				protected void refreshView() {
					tv.setText(getmData().toString());

				}
			};
		}

	}
}
