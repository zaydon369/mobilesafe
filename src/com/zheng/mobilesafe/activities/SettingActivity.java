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
	//���ײ���,���ڴ洢�����ļ�
	private SharedPreferences sp;
	//���岼�ֿؼ�
	private SwitchImageView iv_setting_update;
	RelativeLayout rl_setting_update;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//��ʼ�������ļ�
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//��ʼ���ؼ�
		iv_setting_update=(SwitchImageView) findViewById(R.id.iv_setting_update);
		//����ͼƬ��ѡ��״̬
		iv_setting_update.setSwitchStatus(sp.getBoolean("update", true));
		rl_setting_update=(RelativeLayout) findViewById(R.id.rl_setting_update);
		//����Բ������õ���¼�
		rl_setting_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
				Editor editor=sp.edit();
				editor.putBoolean("update", iv_setting_update.getSwitchStatus());
				editor.commit();
			}});
//		//����ͼƬ����¼�,���ڸ���״̬
//		iv_setting_update.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				iv_setting_update.changedSwitchStatus();
//			}});
		
	}

}
