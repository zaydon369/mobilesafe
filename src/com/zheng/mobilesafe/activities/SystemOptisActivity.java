package com.zheng.mobilesafe.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.fragment.CleanCacheFragment;
import com.zheng.mobilesafe.fragment.CleanSDFragment;

public class SystemOptisActivity extends FragmentActivity implements
		OnClickListener {
	private LinearLayout ll_system_optis_fragment;
	private LinearLayout ll_system_optis_cache;
	private LinearLayout ll_system_optis_sdcar;
	private TextView tv_system_optis_title;
	private ImageView iv_system_optis_cache;
	private TextView tv_system_optis_cache;
	private ImageView iv_system_optis_sdcar;
	private TextView tv_system_optis_sdcar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_optis);
		ll_system_optis_cache = (LinearLayout) findViewById(R.id.ll_system_optis_cache);
		ll_system_optis_sdcar = (LinearLayout) findViewById(R.id.ll_system_optis_sdcar);
		tv_system_optis_title = (TextView) findViewById(R.id.tv_system_optis_title);
		ll_system_optis_fragment = (LinearLayout) findViewById(R.id.ll_system_optis_fragment);
		iv_system_optis_cache = (ImageView) findViewById(R.id.iv_system_optis_cache);
		tv_system_optis_cache = (TextView) findViewById(R.id.tv_system_optis_cache);
		iv_system_optis_sdcar = (ImageView) findViewById(R.id.iv_system_optis_sdcar);
		tv_system_optis_sdcar = (TextView) findViewById(R.id.tv_system_optis_sdcar);

		// ����ťע�����¼�
		ll_system_optis_cache.setOnClickListener(this);
		ll_system_optis_sdcar.setOnClickListener(this);

		// ����Ĭ��Ϊ����������
		getCleanCacheFragment();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_system_optis_cache:// �������
			getCleanCacheFragment();
			break;

		case R.id.ll_system_optis_sdcar:// ���sd��
			getCleanSdCarFragment();
			break;
		}

	}

	/**
	 * �������sd����Fragment
	 */
	private void getCleanSdCarFragment() {
		//���ı���
		tv_system_optis_title.setText("SD������");
		//������һ����ť�ı���ͼƬ��������ɫ
				ll_system_optis_cache.setBackgroundResource(0);
				 iv_system_optis_cache.setBackgroundResource(R.drawable.clean_sdcard_icon);
				 tv_system_optis_cache.setTextColor(0xbb000000);
				 ll_system_optis_sdcar.setBackgroundResource(R.drawable.dg_cancel_selector);
				iv_system_optis_sdcar.setBackgroundResource(R.drawable.clean_cache_icon_pressed);
				tv_system_optis_sdcar.setTextColor(0xFF7bB226);
	

		//���Fragment
		FragmentTransaction ft_sdcar = getSupportFragmentManager().beginTransaction();
		ft_sdcar.replace(R.id.ll_system_optis_fragment, new CleanSDFragment());
		ft_sdcar.commit();
	}

	/**
	 * ��������������Fragment
	 */
	private void getCleanCacheFragment() {
		tv_system_optis_title.setText("��������");
		
		//������һ����ť�ı���ͼƬ��������ɫ
		ll_system_optis_cache.setBackgroundResource(R.drawable.dg_cancel_selector);
		 iv_system_optis_cache.setBackgroundResource(R.drawable.clean_sdcard_icon_pressed);
		 tv_system_optis_cache.setTextColor(0xFF7bB226);
		 ll_system_optis_sdcar.setBackgroundResource(0);
		iv_system_optis_sdcar.setBackgroundResource(R.drawable.clean_cache_icon);
		tv_system_optis_sdcar.setTextColor(0xbb000000);

		FragmentTransaction ft_cache = getSupportFragmentManager()
				.beginTransaction();
		ft_cache.replace(R.id.ll_system_optis_fragment,
				new CleanCacheFragment());
		ft_cache.commit();
	}

}

