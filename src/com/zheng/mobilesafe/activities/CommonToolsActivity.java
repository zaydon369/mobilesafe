package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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
	 * ¶ÌÐÅ±¸·Ý
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
}
