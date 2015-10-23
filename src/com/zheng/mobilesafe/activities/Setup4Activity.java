package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ������,����1
 */
public class Setup4Activity extends SetupBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		// ��һ���İ�ť
		Button bt_pre = (Button) findViewById(R.id.bt_pre);
		bt_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pre();
			}
		});
		Button bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
				// �������,����ϵͳ�Ѿ��߹�������
				Editor edit = sp.edit();
				edit.putBoolean("configed", true);
				edit.commit();
			}
		});
	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup3Activity.class);
	}

	@Override
	public void next() {
		openNewActivityAndFinish(LostFindActivity.class);
	}
}
