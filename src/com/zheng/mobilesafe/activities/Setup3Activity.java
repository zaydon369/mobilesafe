package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 设置向导,界面1
 */
public class Setup3Activity extends SetupBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		Button bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});
		Button bt_pre = (Button) findViewById(R.id.bt_pre);
		bt_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pre();
			}
		});

	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup2Activity.class);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		openNewActivityAndFinish(Setup4Activity.class);
	}

}
