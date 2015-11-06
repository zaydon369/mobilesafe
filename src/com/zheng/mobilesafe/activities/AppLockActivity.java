package com.zheng.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.baseadapter.MyBaseAdapter;
import com.zheng.mobilesafe.baseholder.MyBaseHolder;
import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.engine.AppInfoProvider;

public class AppLockActivity extends Activity {
	Context mcontext;
	private PackageManager pm;
	private ListView lv_applock_unlock;
	private ListView lv_applock_locked;
	private TextView tv_applock_unlock_count;
	private TextView tv_applock_locked_count;
	private List<AppInfo> allAppInfos;
	private List<AppInfo> unlockAppInfos;
	private List<AppInfo> lockedAppInfos;
	private TextView tv_applock_show_unlock;
	private TextView tv_applock_show_locked;
	private LinearLayout ll_applock_unlock;
	private LinearLayout ll_applock_locked;
	private RelativeLayout rl_applock_pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mcontext = getApplicationContext();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		lv_applock_unlock = (ListView) findViewById(R.id.lv_applock_unlock);
		lv_applock_locked = (ListView) findViewById(R.id.lv_applock_locked);
		tv_applock_unlock_count = (TextView) findViewById(R.id.tv_applock_unlock_count);
		tv_applock_locked_count = (TextView) findViewById(R.id.tv_applock_locked_count);
		tv_applock_show_unlock = (TextView) findViewById(R.id.tv_applock_show_unlock);
		tv_applock_show_locked = (TextView) findViewById(R.id.tv_applock_show_locked);
		ll_applock_unlock = (LinearLayout) findViewById(R.id.ll_applock_unlock);
		ll_applock_locked = (LinearLayout) findViewById(R.id.ll_applock_locked);
		rl_applock_pro = (RelativeLayout) findViewById(R.id.rl_applock_pro);
		
		lockedAppInfos = new ArrayList<AppInfo>();
		unlockAppInfos = new ArrayList<AppInfo>();
		//提示正在加载...
		// 在子线程初始化数据
		new Thread() {
			public void run() {
				allAppInfos = AppInfoProvider.getAllAppInfos(mcontext);
				pm = getPackageManager();
				String packageName;
				for (int i = 0; i < allAppInfos.size(); i++) {
					packageName = allAppInfos.get(i).getPackageName();
					// 如果没有界面就过滤掉
					if (pm.getLaunchIntentForPackage(packageName) == null) {
						allAppInfos.remove(i);
						i--;

					}
				}
				// 剔除没有界面的APP后,将剩余的APP赋值给未加锁
				unlockAppInfos = allAppInfos;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						rl_applock_pro.setVisibility(View.GONE);
						showUnlock();

					}

				});

			};
		}.start();

	}

	/**
	 * 切换到未加锁的listview列表
	 * 
	 * @param view
	 */
	public void showUnlock(View view) {
		showUnlock();
	}

	/**
	 * 切换到加锁的listview列表
	 * 
	 * @param view
	 */
	public void showLocked(View view) {
		showLocked();
	}

	/**
	 * 显示未加锁的listview列表
	 */
	private void showUnlock() {
		// 设置按钮标签的背景色
		tv_applock_show_unlock.setBackgroundColor(Color.parseColor("#0f0f00"));
		tv_applock_show_locked.setBackgroundColor(Color.parseColor("#ff00ff"));
		// 显示未加锁
		ll_applock_unlock.setVisibility(View.VISIBLE);
		// 把已加锁的隐藏
		ll_applock_locked.setVisibility(View.INVISIBLE);
		// 加载listView
		final UnlockAdaper unlockAdaper = new UnlockAdaper(unlockAppInfos);
		tv_applock_unlock_count
				.setText("未加锁程序共:" + unlockAppInfos.size() + "个");
		lv_applock_unlock.setAdapter(unlockAdaper);
		// 给条目添加点击事件
		lv_applock_unlock.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lockedAppInfos.add(unlockAppInfos.get(position));
				unlockAppInfos.remove(position);
				// 修改数据完后,通知适配器更新数据
				unlockAdaper.notifyDataSetChanged();
				// 修改标题
				tv_applock_unlock_count.setText("未加锁程序共:"
						+ unlockAppInfos.size() + "个");
			}

		});
	}

	/**
	 * 显示加锁的listview
	 */
	private void showLocked() {
		// 设置按钮标签的背景色
		tv_applock_show_unlock.setBackgroundColor(Color.parseColor("#ff00ff"));
		tv_applock_show_locked.setBackgroundColor(Color.parseColor("#0f0f00"));
		// 把未加锁的隐藏掉
		ll_applock_unlock.setVisibility(View.INVISIBLE);
		// 显示已加锁
		ll_applock_locked.setVisibility(View.VISIBLE);
		// 加载listView
		final LockedAdaper lockedAdaper = new LockedAdaper(lockedAppInfos);
		tv_applock_locked_count
				.setText("已加锁程序共:" + lockedAppInfos.size() + "个");
		lv_applock_locked.setAdapter(lockedAdaper);
		// 给已加锁的list条目添加点击事件
		lv_applock_locked.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				unlockAppInfos.add(lockedAppInfos.get(position));
				lockedAppInfos.remove(position);
				// 修改数据完后,通知适配器更新数据
				lockedAdaper.notifyDataSetChanged();
				// 修改标题
				tv_applock_locked_count.setText("已加锁程序共:"
						+ lockedAppInfos.size() + "个");
			}

		});
	}

	/**
	 * 未加锁的listview的适配器
	 * 
	 * @author asus
	 * 
	 */
	class UnlockAdaper extends MyBaseAdapter {

		public UnlockAdaper(List mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {

			return new UnlocHolder();
		}

		class UnlocHolder extends MyBaseHolder {

			private ImageView iv_unlock_icon;
			private TextView tv_unlock_appname;
			private ImageView iv_unlock_image;
			private AppInfo appInfo;

			@Override
			protected View initView() {
				View view = View.inflate(mcontext,
						R.layout.item_applock_unlock, null);
				iv_unlock_icon = (ImageView) view
						.findViewById(R.id.iv_item_applock_unlock_icon);
				tv_unlock_appname = (TextView) view
						.findViewById(R.id.tv_item_applock_unlock_appname);
				iv_unlock_image = (ImageView) view
						.findViewById(R.id.iv_item_applock_unlock_image);

				return view;
			}

			@Override
			protected void refreshView() {
				appInfo = (AppInfo) getmData();
				iv_unlock_icon.setImageDrawable(appInfo.getIcon());
				tv_unlock_appname.setText(appInfo.getAppName());
			}

		}
	}

	/**
	 * 已加锁的listview的适配器
	 * 
	 * @author asus
	 * 
	 */
	class LockedAdaper extends MyBaseAdapter {

		public LockedAdaper(List mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {

			return new LockedHolder();
		}

		class LockedHolder extends MyBaseHolder {

			private ImageView iv_locked_icon;
			private TextView tv_locked_appname;
			private ImageView iv_locked_image;
			private AppInfo appInfo;

			@Override
			protected View initView() {
				View view = View.inflate(mcontext,
						R.layout.item_applock_locked, null);
				iv_locked_icon = (ImageView) view
						.findViewById(R.id.iv_item_applock_locked_icon);
				tv_locked_appname = (TextView) view
						.findViewById(R.id.tv_item_applock_locked_appname);
				iv_locked_image = (ImageView) view
						.findViewById(R.id.iv_item_applock_locked_image);

				return view;
			}

			@Override
			protected void refreshView() {
				appInfo = (AppInfo) getmData();
				iv_locked_icon.setImageDrawable(appInfo.getIcon());
				tv_locked_appname.setText(appInfo.getAppName());
			}

		}

	}

}
