package com.zheng.mobilesafe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
	
	LinearLayout ll_loading;
	//popup�����ʾapp�������
	PopupWindow popup;
	//app��Ϣ����
	ArrayList<AppInfo> newAppInfos;
	ArrayList<AppInfo> userAppInfos;
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
		//������Ŀ����¼�
		setAppInfoItemClickListener();
		//����listview��������
		lv_appmanager_app.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//�����popup���ھ͹ص�
				if(popup!=null){
					popup.dismiss();
					popup=null;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}});
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
						MyAdapter adapter = new MyAdapter(newAppInfos);
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
						if(popup!=null){
							popup.dismiss();
							popup=null;
						}
						//����ǵ�һ����Ŀ,��Ϊ��һ������Ϊ����,������ʾ
						if(position==0){
							return;
						}
						if(position==(userAppInfos.size())){
							return;
						}
						View contentView = View.inflate(
								AppManagerActivity.this,
								R.layout.item_popup_appinfos, null);
						
						popup = new PopupWindow(contentView, -2, -2);
						int[] location = new int[2];
						view.getLocationInWindow(location);
						popup.showAtLocation(parent,
								Gravity.TOP + Gravity.LEFT, 65, location[1]);

					}

				});

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

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if(popup!=null){
			popup.dismiss();
			popup=null;
		}
		super.onDestroy();
	}
	

}
