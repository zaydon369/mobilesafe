package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class HomeActivity extends Activity {
	// 定义Logo控件
	ImageView iv_home_logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// 初始化logo图片控件
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		// iv_home_logo.setRotationY(rotationY);
		// 设置属性动画
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				new float[] {0,60,120,180,240,300});
		//设置时长 
		oa.setDuration(1000);
		//设置动画次数,无限播放
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		//设置动画模式,重新开始
		oa.setRepeatMode(ObjectAnimator.RESTART);
		//开启动画
		oa.start();
	}

}
