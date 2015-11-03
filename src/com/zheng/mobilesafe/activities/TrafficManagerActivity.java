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
		//����������
		long totalRxBytes = TrafficStats.getTotalRxBytes();//����
		long totalTxBytes = TrafficStats.getTotalTxBytes();//�ϴ�
		tv_total_traffic.setText("������:"+Formatter.formatFileSize(getApplicationContext(), totalRxBytes)+"\n���ϴ�:"+
				Formatter.formatFileSize(getApplicationContext(), totalTxBytes)+"\n��    ��:"+
				Formatter.formatFileSize(getApplicationContext(), totalRxBytes+totalTxBytes));
		//����
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		tv_moblie_traffic.setText("������:"+Formatter.formatFileSize(getApplicationContext(), mobileRxBytes)+"\n���ϴ�:"+
				Formatter.formatFileSize(getApplicationContext(), mobileTxBytes)+"\n��    ��:"+
				Formatter.formatFileSize(getApplicationContext(), mobileRxBytes+mobileTxBytes));
	}
	
}
