package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.ui.SwitchImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity {
	//定义布局控件
	private SwitchImageView iv_setting_update;
	RelativeLayout rl_setting_update;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//初始化控件
		iv_setting_update=(SwitchImageView) findViewById(R.id.iv_setting_update);
		rl_setting_update=(RelativeLayout) findViewById(R.id.rl_setting_update);
		//给相对布局设置点击事件
		rl_setting_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
			}});
//		//设置图片点击事件,用于更改状态
//		iv_setting_update.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				iv_setting_update.changedSwitchStatus();
//			}});
		
	}

}
