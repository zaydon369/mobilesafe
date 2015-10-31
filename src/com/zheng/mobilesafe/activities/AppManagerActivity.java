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
	// 安装应用程序信息显示
	ListView lv_appmanager_app;
	
	LinearLayout ll_loading;
	//popup点击显示app更多操作
	PopupWindow popup;
	//app信息数组
	ArrayList<AppInfo> newAppInfos;
	ArrayList<AppInfo> userAppInfos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		tv_appmanager_internal = (TextView) findViewById(R.id.tv_appmanager_internal);
		tv_appmanager_sdcard = (TextView) findViewById(R.id.tv_appmanager_sdcard);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

		// 获取系统内存,给tv设置值
		Long internal = SystemInfoUtils.getInternalStorageFreeSize();
		Long sd = SystemInfoUtils.getSDStorageFreeSize();
		// 将文件大小格式化成易懂数据
		tv_appmanager_internal.setText("机身存储剩余:"
				+ Formatter.formatFileSize(getApplicationContext(), internal));
		tv_appmanager_sdcard.setText("SD卡剩余:"
				+ Formatter.formatFileSize(getApplicationContext(), sd));
		lv_appmanager_app = (ListView) findViewById(R.id.lv_appmanager_app);
		// 加载listview放在子线程
		ll_loading.setVisibility(View.VISIBLE);
		//单个条目点击事件
		setAppInfoItemClickListener();
		//设置listview滚动监听
		lv_appmanager_app.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//如果有popup窗口就关掉
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
				// 得到系统App的安装信息
				ArrayList<AppInfo> allAppInfos = (ArrayList<AppInfo>) AppInfoProvider
						.getAllAppInfos(getApplicationContext());
				 userAppInfos = new ArrayList<AppInfo>();

				userAppInfos.add(new AppInfo());
				// 系统应用
				ArrayList<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
				systemAppInfos.add(new AppInfo());
				// 遍历所有的应用,判断是否是系统应用
				for (AppInfo appInfo : allAppInfos) {
					if (appInfo.isSystemApp()) {
						systemAppInfos.add(appInfo);
					} else {
						userAppInfos.add(appInfo);
					}
				}
				// 将整理后的集合存进新的集合中
				newAppInfos = new ArrayList<AppInfo>();
				newAppInfos.addAll(userAppInfos);

				newAppInfos.addAll(systemAppInfos);
				// 在子线程里面再弄出一条UI线程
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
	 * ListView 条目点击事件
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
						//如果是第一个条目,因为第一个设置为标题,不能显示
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
