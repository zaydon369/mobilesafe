package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.ui.SwitchImageView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity {
	//贡献参数,用于存储配置文件
	private SharedPreferences sp;
	//定义布局控件
	private SwitchImageView iv_setting_update;
	RelativeLayout rl_setting_update;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//初始化配置文件
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//初始化控件
		iv_setting_update=(SwitchImageView) findViewById(R.id.iv_setting_update);
		//设置图片的选择状态
		iv_setting_update.setSwitchStatus(sp.getBoolean("update", true));
		rl_setting_update=(RelativeLayout) findViewById(R.id.rl_setting_update);
		//给相对布局设置点击事件
		rl_setting_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
				Editor editor=sp.edit();
				editor.putBoolean("update", iv_setting_update.getSwitchStatus());
				editor.commit();
			}});
//		//设置图片点击事件,用于更改状态
//		iv_setting_update.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				iv_setting_update.changedSwitchStatus();
//			}});
		
	}

}
