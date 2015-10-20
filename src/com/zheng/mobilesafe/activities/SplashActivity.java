package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.R.layout;
import com.zheng.mobilesafe.R.menu;
import com.zheng.mobilesafe.activities.utils.PackageInfoUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class SplashActivity extends Activity {
	TextView tv_splash_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//初始化控件
		tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
		//获取当前版本号
		String version = PackageInfoUtils.getPackageVersion(this);
		//把版本号显示到splash中
		tv_splash_version.setText("版本:"+version);
	}
	

}
