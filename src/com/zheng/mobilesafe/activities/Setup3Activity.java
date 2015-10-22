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
public class Setup3Activity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_setup3);
	Button bt_next=	(Button) findViewById(R.id.bt_next);
	bt_next.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
			startActivity(intent);
		}});
	Button bt_pre=	(Button) findViewById(R.id.bt_pre);
	bt_pre.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
			startActivity(intent);
		}});
	
}

}
