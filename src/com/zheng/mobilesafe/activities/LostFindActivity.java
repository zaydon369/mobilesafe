package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zheng.mobilesafe.R;

public class LostFindActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind);
		
	}
	/**
	 * 重新进入向导
	 * @param view
	 */
	public void reseting(View view){
		Intent intent =new Intent(this,Setup1Activity.class);
		startActivity(intent);
		
	}
}
