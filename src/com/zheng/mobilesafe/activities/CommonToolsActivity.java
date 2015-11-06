package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.SmsTools;
import com.zheng.mobilesafe.activities.utils.SmsTools.BackupCallback;

public class CommonToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_common_tools);
		
		super.onCreate(savedInstanceState);
	}
	
	
	/**
	 * 短信备份
	 * @param view
	 */
	public void smsBackup(View view){
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		new Thread(){
			public void run() {
				SmsTools.backUpSms(CommonToolsActivity.this, new BackupCallback() {
					@Override
					public void onSmsBackup(int progress) {
						pd.setProgress(progress);
					}
					
					@Override
					public void beforeSmsBackup(int max) {
						pd.setMax(max);
					}
				});
				pd.dismiss();
			};
		}.start();
		
		
		
	}
	/**
	 * 程序锁
	 * @param view
	 */
	public  void appClock(View view){
		//打开程序锁窗口
		Intent intent=new Intent (this,AppLockActivity.class);
		startActivity(intent);
		
	}
}
