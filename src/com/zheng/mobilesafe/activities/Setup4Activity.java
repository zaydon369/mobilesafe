package com.zheng.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.zheng.mobilesafe.R;

/**
 * ������,����1
 */
public class Setup4Activity extends SetupBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

	}

	@Override
	public void showNext(View view) {
		// ��д�������һ������
		super.showNext(view);
		Editor edit = sp.edit();
		// ����Ѿ��߹�����������
		edit.putBoolean("configed", true);
		edit.commit();
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
