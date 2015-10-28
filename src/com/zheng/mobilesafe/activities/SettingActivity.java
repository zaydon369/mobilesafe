package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.ServiceStatusUtils;
import com.zheng.mobilesafe.service.CallSmsSafeService;
import com.zheng.mobilesafe.ui.SwitchImageView;

public class SettingActivity extends Activity {
	// 共享参数,用于存储配置文件
	private SharedPreferences sp;
	// 定义自动更新布局控件
	private SwitchImageView iv_setting_update;
	private RelativeLayout rl_setting_update;
	// 定义拦截骚扰布局控件
	private SwitchImageView iv_setting_callsmssafe;
	private RelativeLayout rl_setting_callsmssafe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 初始化配置文件
		sp = getSharedPreferences("config", MODE_PRIVATE);
		/* 初始化自动更新控件:图片,布局 */
		iv_setting_update = (SwitchImageView) findViewById(R.id.iv_setting_update);
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		/* 初始化拦截骚扰控件:图片,布局 */
		iv_setting_callsmssafe = (SwitchImageView) findViewById(R.id.iv_setting_callsmssafe);
		rl_setting_callsmssafe = (RelativeLayout) findViewById(R.id.rl_setting_callsmssafe);
		/* 设置自动更新图片的选择状态 */
		iv_setting_update.setSwitchStatus(sp.getBoolean("update", true));

		/* 设置拦截骚扰图片的选择状态 */// 因为是服务进程,判断不能简单根据配置文件,这个后期优化
		// iv_setting_callsmssafe.setSwitchStatus(sp.getBoolean("callsmssafe",
		// false));
		// 判断服务是否打开,如果服务开启则显示开,如果没开启则显示关闭
		iv_setting_callsmssafe.setSwitchStatus(ServiceStatusUtils
				.isServiceRunning(getApplicationContext(),
						"com.zheng.mobilesafe.service.CallSmsSafeService"));
		/* 给自动更新的相对布局设置点击事件 */
		rl_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
				Editor editor = sp.edit();
				editor.putBoolean("update", iv_setting_update.getSwitchStatus());
				editor.commit();
				if (iv_setting_update.getSwitchStatus()) {
					Toast.makeText(getApplicationContext(), "自动更新已打开..", 0)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), "自动更新已关闭..", 0)
							.show();
				}
			}
		});

		/* 给骚扰拦截设置对应的点击事件 */
		rl_setting_callsmssafe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 改变图标
				iv_setting_callsmssafe.changedSwitchStatus();
				// 创建一个骚扰拦截的服务意图
				Intent service = new Intent(getApplicationContext(),
						CallSmsSafeService.class);
				Editor editor = sp.edit();
				editor.putBoolean("callsmssafe", iv_setting_callsmssafe.getSwitchStatus());
				editor.commit();
				if (iv_setting_callsmssafe.getSwitchStatus()) {
					// 开启服务
					startService(service);
					Toast.makeText(getApplicationContext(), "骚扰拦截已打开..", 0)
							.show();
				} else {
					// 停止服务
					stopService(service);
					Toast.makeText(getApplicationContext(), "骚扰拦截已关闭..", 0)
							.show();
				}
			}

		});

	}

}
