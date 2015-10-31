package com.zheng.mobilesafe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
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
	// ��װӦ�ó�����Ϣ��ʾ
	ListView lv_appmanager_app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_appmanager_internal = (TextView) findViewById(R.id.tv_appmanager_internal);
		tv_appmanager_sdcard = (TextView) findViewById(R.id.tv_appmanager_sdcard);
		// ��ȡϵͳ�ڴ�,��tv����ֵ
		Long internal = SystemInfoUtils.getInternalStorageFreeSize();
		Long sd = SystemInfoUtils.getSDStorageFreeSize();
		// ���ļ���С��ʽ�����׶�����
		tv_appmanager_internal.setText("����洢ʣ��:"
				+ Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD��ʣ��:"
				+ Formatter.formatFileSize(getApplicationContext(), sd));

		lv_appmanager_app = (ListView) findViewById(R.id.lv_appmanager_app);
		// �õ�ϵͳApp�İ�װ��Ϣ
		ArrayList<AppInfo> allAppInfos = (ArrayList<AppInfo>) AppInfoProvider
				.getAllAppInfos(getApplicationContext());
		ArrayList<AppInfo> userAppInfos = new ArrayList<AppInfo>();

		userAppInfos.add(new AppInfo());
		// ϵͳӦ��
		ArrayList<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
		systemAppInfos.add(new AppInfo());
		// �������е�Ӧ��,�ж��Ƿ���ϵͳӦ��
		for (AppInfo appInfo : allAppInfos) {
			if (appInfo.isSystemApp()) {
				systemAppInfos.add(appInfo);
			} else {
				userAppInfos.add(appInfo);
			}
		}
		// �������ļ��ϴ���µļ�����
		ArrayList<AppInfo> newAppInfos = new ArrayList<AppInfo>();
		newAppInfos.addAll(userAppInfos);

		newAppInfos.addAll(systemAppInfos);

		MyAdapter adapter = new MyAdapter(newAppInfos);
		lv_appmanager_app.setAdapter(adapter);
	}

	class MyAdapter extends MyBaseAdapter {
		// TextView tv;

		public MyAdapter(ArrayList mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {
			return new MyHolder();
		}

	}

	class MyHolder extends MyBaseHolder {
		ImageView iv_itemapp_icont;
		TextView tv_itemapp_name;
		TextView tv_itemapp_size;
		View view;

		protected View initView() {
			view = View.inflate(getApplicationContext(),
					R.layout.item_app_infos, null);
			iv_itemapp_icont = (ImageView) view
					.findViewById(R.id.iv_itemapp_icont);
			tv_itemapp_name = (TextView) view
					.findViewById(R.id.tv_itemapp_name);
			tv_itemapp_size = (TextView) view
					.findViewById(R.id.tv_itemapp_size);

			return view;
		}

		@Override
		protected void refreshView() {
			AppInfo appInfo = (AppInfo) getmData();
			iv_itemapp_icont.setImageDrawable(appInfo.getIcon());
			tv_itemapp_name.setText(appInfo.getAppName());
			tv_itemapp_size.setText(Formatter.formatFileSize(
					getApplicationContext(), appInfo.getApkSize()));
		}

	}

}
