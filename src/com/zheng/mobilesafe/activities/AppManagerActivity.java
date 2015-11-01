package com.zheng.mobilesafe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.SystemInfoUtils;
import com.zheng.mobilesafe.baseadapter.MyBaseAdapter;
import com.zheng.mobilesafe.baseholder.MyBaseHolder;
import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.engine.AppInfoProvider;

public class AppManagerActivity extends Activity implements OnClickListener {
	TextView tv_appmanager_internal;
	TextView tv_appmanager_sdcard;
	// ��װӦ�ó�����Ϣ��ʾ
	ListView lv_appmanager_app;

	LinearLayout ll_loading;
	// popup�����ʾapp�������
	PopupWindow popup;
	// app��Ϣ����
	ArrayList<AppInfo> newAppInfos;
	ArrayList<AppInfo> userAppInfos;
	// popup�ӿؼ���Ϣ
	LinearLayout ll_uninstall;
	LinearLayout ll_start;
	LinearLayout ll_share;
	LinearLayout ll_showinfo;
	// ��ǰѡ�е�APP����Ϣ
	AppInfo clickedAppInfo;
	// ListVIew������
	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_appmanager_internal = (TextView) findViewById(R.id.tv_appmanager_internal);
		tv_appmanager_sdcard = (TextView) findViewById(R.id.tv_appmanager_sdcard);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

		// ��ȡϵͳ�ڴ�,��tv����ֵ
		Long internal = SystemInfoUtils.getInternalStorageFreeSize();
		Long sd = SystemInfoUtils.getSDStorageFreeSize();
		// ���ļ���С��ʽ�����׶�����
		tv_appmanager_internal.setText("����洢ʣ��:"
				+ Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD��ʣ��:"
				+ Formatter.formatFileSize(getApplicationContext(), sd));
		lv_appmanager_app = (ListView) findViewById(R.id.lv_appmanager_app);
		// ����listview�������߳�
		ll_loading.setVisibility(View.VISIBLE);
		// ������Ŀ����¼�
		setAppInfoItemClickListener();
		// ����listview��������
		lv_appmanager_app.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// �����popup���ھ͹ص�
				if (popup != null) {
					popup.dismiss();
					popup = null;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		new Thread() {
			public void run() {
				// �õ�ϵͳApp�İ�װ��Ϣ
				ArrayList<AppInfo> allAppInfos = (ArrayList<AppInfo>) AppInfoProvider
						.getAllAppInfos(getApplicationContext());
				userAppInfos = new ArrayList<AppInfo>();

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
				newAppInfos = new ArrayList<AppInfo>();
				newAppInfos.addAll(userAppInfos);

				newAppInfos.addAll(systemAppInfos);
				// �����߳�������Ū��һ��UI�߳�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_loading.setVisibility(View.GONE);
						adapter = new MyAdapter(newAppInfos);
						lv_appmanager_app.setAdapter(adapter);
					}
				});
			};

		}.start();

	}

	/**
	 * ListView ��Ŀ����¼�
	 */
	private void setAppInfoItemClickListener() {
		lv_appmanager_app
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						if (popup != null) {
							popup.dismiss();
							popup = null;
						}
						// ����ǵ�һ����Ŀ,��Ϊ��һ������Ϊ����,������ʾ
						if (position == 0) {
							return;
						}
						if (position == (userAppInfos.size())) {
							return;
						}
						// ��ȡ��ǰѡ�е�app��Ϣ,
						clickedAppInfo = newAppInfos.get(position);
						// ���popup����С����
						View contentView = View.inflate(
								AppManagerActivity.this,
								R.layout.item_popup_appinfos, null);
						// ��ʼ������������ӿؼ�
						ll_uninstall = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_uninstall);
						ll_start = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_start);
						ll_share = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_share);
						ll_showinfo = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_showinfo);
						// ������������ӿؼ����õ���¼�
						ll_uninstall
								.setOnClickListener(AppManagerActivity.this);
						ll_start.setOnClickListener(AppManagerActivity.this);
						ll_share.setOnClickListener(AppManagerActivity.this);
						ll_showinfo.setOnClickListener(AppManagerActivity.this);

						popup = new PopupWindow(contentView, -2, -2);
						int[] location = new int[2];
						view.getLocationInWindow(location);
						// �趨����͸��
						popup.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						popup.showAtLocation(parent,
								Gravity.TOP + Gravity.LEFT, 65, location[1]);
						// ָ�����Ŷ���
						ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f,
								0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0.5f);
						// ���ö�����ʱ��
						sa.setDuration(250);
						contentView.startAnimation(sa);
					}

				});

	}

	// ʹ��holder�����Զ����������
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

		// ��ʼ����ͼ,holder��ȡ�ķ���
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

		// ˢ����ͼ,holder��ȡ�ķ���
		@Override
		protected void refreshView() {
			AppInfo appInfo = (AppInfo) getmData();
			iv_itemapp_icont.setImageDrawable(appInfo.getIcon());
			tv_itemapp_name.setText(appInfo.getAppName());
			tv_itemapp_size.setText(Formatter.formatFileSize(
					getApplicationContext(), appInfo.getApkSize()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (popup != null) {
			popup.dismiss();
			popup = null;
		}
		super.onDestroy();
	}

	/**
	 * �������¼�
	 */
	@Override
	public void onClick(View v) {
		//������Ͱ�popup�ر�
		if (popup != null) {
			popup.dismiss();
			popup = null;
		}
		//�жϵ������Ŀ,���ж�Ӧ����
		switch (v.getId()) {
		case R.id.ll_item_popup_uninstall:// ж��
			uninstallApplication();
			break;

		case R.id.ll_item_popup_start:// ����
			startApplication();
			break;
		case R.id.ll_item_popup_share:// ����

			break;
		case R.id.ll_item_popup_showinfo:// ��Ϣ

			break;
		}

	}
	/**
	 * ����Ӧ�ó���
	 */
	private void startApplication() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(clickedAppInfo.getPackageName());
		if(intent!=null){
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "��Ӧ���޷�����",0).show();
		}
	}

	/**
	 * ж��Ӧ�ó���
	 */
	private void uninstallApplication() {
		// ������������ж����Ϣ
		AppUninstallReceiver receiver = new AppUninstallReceiver();
		// �����ͼ������
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		// ����ǰ׺Ϊpackage
		filter.addDataScheme("package");
		// ע�����
		registerReceiver(receiver, filter);
		// ������ͼ,ͨ������ж��
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + clickedAppInfo.getPackageName()));
		System.out.println("����" + clickedAppInfo.getPackageName());
		startActivity(intent);

	}

	/**
	 * �㲥,����Ӧ�ó����ж����Ϣ
	 */
	private class AppUninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = intent.getData().toString();
			// ��Ϊ�õ��İ�����ǰ׺
			String packname = data.replace("package:", "");
			//AppInfo defaultApp = null;
			stop:for (AppInfo appInfo : newAppInfos) {
				// ��ж�ص�APP��������Ƴ�
				if (packname.equals(appInfo.getPackageName())) {
			//		defaultApp = appInfo;
					newAppInfos.remove(appInfo);
					break stop;
				}
			}
			// ����UI����
//			if (defaultApp != null) {
//				newAppInfos.remove(defaultApp);
//				
//			}
			adapter.notifyDataSetChanged();
			// ȡ��ע�����
			unregisterReceiver(this);
		}

	}
}
