package com.zheng.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zheng.mobilesafe.R;

/**
 * ������,����1
 */
public class Setup1Activity extends SetupBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void pre() {

	}

	@Override
	public void next() {
		openNewActivityAndFinish(Setup2Activity.class);

	}

}
