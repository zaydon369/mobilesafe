package com.zheng.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zheng.mobilesafe.R;

/**
 * 设置向导,界面1
 */
public class Setup3Activity extends SetupBaseActivity {
	Button bt_setup3_selectContacts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		//找到添加安全号码的按钮
		bt_setup3_selectContacts=(Button) findViewById(R.id.bt_setup3_selectContacts);
		//给按钮添加事件
		bt_setup3_selectContacts.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//获得系统联系人
				//打开选择联系人界面
				Intent intent =new Intent(Setup3Activity.this,SelectContactActivity.class);
				startActivity(intent);
				
			}});

	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup2Activity.class);
	}

	@Override
	public void next() {
		openNewActivityAndFinish(Setup4Activity.class);
	}

}
