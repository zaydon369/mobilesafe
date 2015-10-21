package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class HomeActivity extends Activity {
	// ����Logo�ؼ�
	ImageView iv_home_logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// ��ʼ��logoͼƬ�ؼ�
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		// iv_home_logo.setRotationY(rotationY);
		// �������Զ���
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				new float[] {0,60,120,180,240,300});
		//����ʱ�� 
		oa.setDuration(1000);
		//���ö�������,���޲���
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		//���ö���ģʽ,���¿�ʼ
		oa.setRepeatMode(ObjectAnimator.RESTART);
		//��������
		oa.start();
	}

}
