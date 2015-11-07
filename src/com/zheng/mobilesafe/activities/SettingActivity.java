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
import com.zheng.mobilesafe.service.LocationService;
import com.zheng.mobilesafe.service.ShowAddressService;
import com.zheng.mobilesafe.service.WatchDogService;
import com.zheng.mobilesafe.ui.SwitchImageView;

public class SettingActivity extends Activity {
	// �������,���ڴ洢�����ļ�
	private SharedPreferences sp;
	// �����Զ����²��ֿؼ�
	private SwitchImageView iv_setting_update;
	private RelativeLayout rl_setting_update;
	// ��������ɧ�Ų��ֿؼ�
	private SwitchImageView iv_setting_callsmssafe;
	private RelativeLayout rl_setting_callsmssafe;
	// ������ʾ��������ʾ
	private SwitchImageView iv_setting_showLocation;
	private RelativeLayout rl_setting_showlocation;
	// ���忴�Ź�
	private SwitchImageView iv_setting_applock;
	private RelativeLayout rl_setting_applock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// ��ʼ�������ļ�
		sp = getSharedPreferences("config", MODE_PRIVATE);
		/* ��ʼ���Զ����¿ؼ�:ͼƬ,���� */
		iv_setting_update = (SwitchImageView) findViewById(R.id.iv_setting_update);
		rl_setting_update = (RelativeLayout) findViewById(R.id.rl_setting_update);
		/* ��ʼ������ɧ�ſؼ�:ͼƬ,���� */
		iv_setting_callsmssafe = (SwitchImageView) findViewById(R.id.iv_setting_callsmssafe);
		rl_setting_callsmssafe = (RelativeLayout) findViewById(R.id.rl_setting_callsmssafe);
		/* ������ʾ��������ʾ: */
		iv_setting_showLocation = (SwitchImageView) findViewById(R.id.iv_setting_showLocation);
		rl_setting_showlocation = (RelativeLayout) findViewById(R.id.rl_setting_showlocation);
		/* �ҵ����Ź������ÿؼ� */
		iv_setting_applock = (SwitchImageView) findViewById(R.id.iv_setting_applock);
		rl_setting_applock = (RelativeLayout) findViewById(R.id.rl_setting_applock);

		/* �����Զ�����ͼƬ��ѡ��״̬ */
		iv_setting_update.setSwitchStatus(sp.getBoolean("update", true));

		/* ��������ɧ��ͼƬ��ѡ��״̬ */// ��Ϊ�Ƿ������,�жϲ��ܼ򵥸��������ļ�,��������Ż�
		// iv_setting_callsmssafe.setSwitchStatus(sp.getBoolean("callsmssafe",
		// false));
		// �жϷ����Ƿ��,�������������ʾ��,���û��������ʾ�ر�
		iv_setting_callsmssafe.setSwitchStatus(ServiceStatusUtils
				.isServiceRunning(getApplicationContext(),
						"com.zheng.mobilesafe.service.CallSmsSafeService"));
		/* �ж���ʾ�����ط����Ƿ��,��ʾ��ť״̬ */
		iv_setting_showLocation.setSwitchStatus(ServiceStatusUtils
				.isServiceRunning(getApplicationContext(),
						"com.zheng.mobilesafe.service.LocationService"));
		/*�жϿ��Ź������Ƿ��,��ʾ��ť״̬*/
		iv_setting_applock.setSwitchStatus(ServiceStatusUtils.isServiceRunning(getApplicationContext(), "com.zheng.mobilesafe.service.WatchDogService"));
		/* ���Զ����µ���Բ������õ���¼� */
		rl_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_setting_update.changedSwitchStatus();
				Editor editor = sp.edit();
				editor.putBoolean("update", iv_setting_update.getSwitchStatus());
				editor.commit();
				if (iv_setting_update.getSwitchStatus()) {
					Toast.makeText(getApplicationContext(), "�Զ������Ѵ�..", 0)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), "�Զ������ѹر�..", 0)
							.show();
				}
			}
		});

		/* ��ɧ���������ö�Ӧ�ĵ���¼� */
		rl_setting_callsmssafe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ı�ͼ��
				iv_setting_callsmssafe.changedSwitchStatus();
				// ����һ��ɧ�����صķ�����ͼ
				Intent service = new Intent(getApplicationContext(),
						CallSmsSafeService.class);
				Editor editor = sp.edit();
				editor.putBoolean("callsmssafe",
						iv_setting_callsmssafe.getSwitchStatus());
				editor.commit();
				if (iv_setting_callsmssafe.getSwitchStatus()) {
					// ��������
					startService(service);
					Toast.makeText(getApplicationContext(), "ɧ�������Ѵ�..", 0)
							.show();
				} else {
					// ֹͣ����
					stopService(service);
					Toast.makeText(getApplicationContext(), "ɧ�������ѹر�..", 0)
							.show();
				}
			}

		});
		/* ����������ʾ�ĵ���¼� */
		rl_setting_showlocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_setting_showLocation.changedSwitchStatus();
				Intent service = new Intent(getApplicationContext(),
						ShowAddressService.class);
				if (iv_setting_showLocation.getSwitchStatus()) {
					// ��ʼ��ʾ����
					startService(service);

				} else {
					stopService(service);
				}

			}

		});
		/* ���ÿ��Ź�����ĵ���¼� */
		rl_setting_applock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_setting_applock.changedSwitchStatus();
				Intent service = new Intent(getApplicationContext(),
						WatchDogService.class);
				if (iv_setting_applock.getSwitchStatus()) {
					// ��ʼ��ʾ����
					startService(service);

				} else {
					stopService(service);
				}
				
			}
			
		});
	}

}
