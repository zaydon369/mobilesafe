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
	//���岼�ֿؼ�
	private SwitchImageView iv_setting_update;
	RelativeLayout rl_setting_update;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//��ʼ���ؼ�
		iv_setting_update=(SwitchImageView) findViewById(R.id.iv_setting_update);
		rl_setting_update=(RelativeLayout) findViewById(R.id.rl_setting_update);
		//����Բ������õ���¼�
		rl_setting_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
			}});
//		//����ͼƬ����¼�,���ڸ���״̬
//		iv_setting_update.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				iv_setting_update.changedSwitchStatus();
//			}});
		
	}

}
