package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

import com.zheng.mobilesafe.R;

public class TrafficManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		TextView tv_total_traffic = (TextView) findViewById(R.id.tv_total_traffic);
		TextView tv_moblie_traffic = (TextView) findViewById(R.id.tv_moblie_traffic);
		//计算总流量
		long totalRxBytes = TrafficStats.getTotalRxBytes();//下载
		long totalTxBytes = TrafficStats.getTotalTxBytes();//上传
		tv_total_traffic.setText("总下载:"+Formatter.formatFileSize(getApplicationContext(), totalRxBytes)+"\n总上传:"+
				Formatter.formatFileSize(getApplicationContext(), totalTxBytes)+"\n合    计:"+
				Formatter.formatFileSize(getApplicationContext(), totalRxBytes+totalTxBytes));
		//下载
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		tv_moblie_traffic.setText("总下载:"+Formatter.formatFileSize(getApplicationContext(), mobileRxBytes)+"\n总上传:"+
				Formatter.formatFileSize(getApplicationContext(), mobileTxBytes)+"\n合    计:"+
				Formatter.formatFileSize(getApplicationContext(), mobileRxBytes+mobileTxBytes));
	}
	
}
