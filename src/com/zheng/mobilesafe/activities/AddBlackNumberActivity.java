package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.zheng.mobilesafe.R;

public class AddBlackNumberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blacknumber);
	}

	public void save(View view) {

	}

	public void cancel(View view) {
		finish();
	}

}
