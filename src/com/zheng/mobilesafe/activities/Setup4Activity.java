package com.zheng.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.zheng.mobilesafe.R;

/**
 * 设置向导,界面1
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
		// 重写父类的下一步方法
		super.showNext(view);
		Editor edit = sp.edit();
		// 标记已经走过设置向导流程
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
