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
 * 设置向导,界面1
 */
public class Setup4Activity extends Activity {
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		//
		sp=getSharedPreferences("config", MODE_PRIVATE);
		//上一个的按钮
		Button bt_pre = (Button) findViewById(R.id.bt_pre);
		bt_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setup4Activity.this,
						Setup3Activity.class);
				startActivity(intent);
			}
		});
		Button bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setup4Activity.this,
						LostFindActivity.class);
				startActivity(intent);
				//做个标记,告诉系统已经走过设置向导
				Editor edit = sp.edit();
				edit.putBoolean("configed", true);
				edit.commit();
				
			}
		});
	}
}

