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
		//��ʼ���ؼ�
		tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
		//��ȡ��ǰ�汾��
		String version = PackageInfoUtils.getPackageVersion(this);
		//�Ѱ汾����ʾ��splash��
		tv_splash_version.setText("�汾:"+version);
	}
	

}
