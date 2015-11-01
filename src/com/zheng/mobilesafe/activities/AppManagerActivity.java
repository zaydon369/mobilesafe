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
	// 安装应用程序信息显示
	ListView lv_appmanager_app;

	LinearLayout ll_loading;
	// popup点击显示app更多操作
	PopupWindow popup;
	// app信息数组
	ArrayList<AppInfo> newAppInfos;
	ArrayList<AppInfo> userAppInfos;
	// popup子控件信息
	LinearLayout ll_uninstall;
	LinearLayout ll_start;
	LinearLayout ll_share;
	LinearLayout ll_showinfo;
	// 当前选中的APP的信息
	AppInfo clickedAppInfo;
	// ListVIew适配器
	MyAdapter adapter;

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
		// 单个条目点击事件
		setAppInfoItemClickListener();
		// 设置listview滚动监听
		lv_appmanager_app.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 如果有popup窗口就关掉
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
						adapter = new MyAdapter(newAppInfos);
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

						if (popup != null) {
							popup.dismiss();
							popup = null;
						}
						// 如果是第一个条目,因为第一个设置为标题,不能显示
						if (position == 0) {
							return;
						}
						if (position == (userAppInfos.size())) {
							return;
						}
						// 获取当前选中的app信息,
						clickedAppInfo = newAppInfos.get(position);
						// 添加popup悬浮小窗体
						View contentView = View.inflate(
								AppManagerActivity.this,
								R.layout.item_popup_appinfos, null);
						// 初始化悬浮窗体的子控件
						ll_uninstall = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_uninstall);
						ll_start = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_start);
						ll_share = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_share);
						ll_showinfo = (LinearLayout) contentView
								.findViewById(R.id.ll_item_popup_showinfo);
						// 给悬浮窗体的子控件设置点击事件
						ll_uninstall
								.setOnClickListener(AppManagerActivity.this);
						ll_start.setOnClickListener(AppManagerActivity.this);
						ll_share.setOnClickListener(AppManagerActivity.this);
						ll_showinfo.setOnClickListener(AppManagerActivity.this);

						popup = new PopupWindow(contentView, -2, -2);
						int[] location = new int[2];
						view.getLocationInWindow(location);
						// 设定背景透明
						popup.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						popup.showAtLocation(parent,
								Gravity.TOP + Gravity.LEFT, 65, location[1]);
						// 指定缩放动画
						ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f,
								0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0.5f);
						// 设置动画的时长
						sa.setDuration(250);
						contentView.startAnimation(sa);
					}

				});

	}

	// 使用holder方法自定义的适配器
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

		// 初始化视图,holder抽取的方法
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

		// 刷新视图,holder抽取的方法
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
	 * 窗体点击事件
	 */
	@Override
	public void onClick(View v) {
		//点击到就把popup关闭
		if (popup != null) {
			popup.dismiss();
			popup = null;
		}
		//判断点击的条目,进行对应操作
		switch (v.getId()) {
		case R.id.ll_item_popup_uninstall:// 卸载
			uninstallApplication();
			break;

		case R.id.ll_item_popup_start:// 启动
			startApplication();
			break;
		case R.id.ll_item_popup_share:// 分享

			break;
		case R.id.ll_item_popup_showinfo:// 信息

			break;
		}

	}
	/**
	 * 开启应用程序
	 */
	private void startApplication() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(clickedAppInfo.getPackageName());
		if(intent!=null){
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "该应用无法启动",0).show();
		}
	}

	/**
	 * 卸载应用程序
	 */
	private void uninstallApplication() {
		// 创建传播监听卸载信息
		AppUninstallReceiver receiver = new AppUninstallReceiver();
		// 添加意图过滤器
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		// 设置前缀为package
		filter.addDataScheme("package");
		// 注册监听
		registerReceiver(receiver, filter);
		// 创建意图,通过包名卸载
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + clickedAppInfo.getPackageName()));
		System.out.println("包名" + clickedAppInfo.getPackageName());
		startActivity(intent);

	}

	/**
	 * 广播,接收应用程序的卸载信息
	 */
	private class AppUninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = intent.getData().toString();
			// 因为得到的包名有前缀
			String packname = data.replace("package:", "");
			//AppInfo defaultApp = null;
			stop:for (AppInfo appInfo : newAppInfos) {
				// 将卸载的APP从类表中移除
				if (packname.equals(appInfo.getPackageName())) {
			//		defaultApp = appInfo;
					newAppInfos.remove(appInfo);
					break stop;
				}
			}
			// 更新UI界面
//			if (defaultApp != null) {
//				newAppInfos.remove(defaultApp);
//				
//			}
			adapter.notifyDataSetChanged();
			// 取消注册监听
			unregisterReceiver(this);
		}

	}
}
