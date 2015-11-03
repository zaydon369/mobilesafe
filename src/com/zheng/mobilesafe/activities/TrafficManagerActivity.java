package com.zheng.mobilesafe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.domain.TrafficInfo;
import com.zheng.mobilesafe.engine.TrafficInfoProvider;

public class TrafficManagerActivity extends Activity {
	private ArrayList<TrafficInfo> trafficInfos;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		TextView tv_total_traffic = (TextView) findViewById(R.id.tv_total_traffic);
		TextView tv_moblie_traffic = (TextView) findViewById(R.id.tv_moblie_traffic);
		lv = (ListView) findViewById(R.id.lv_traffic_details);
		// ����������
		long totalRxBytes = TrafficStats.getTotalRxBytes();// ����
		long totalTxBytes = TrafficStats.getTotalTxBytes();// �ϴ�
		tv_total_traffic.setText("������:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalRxBytes)
				+ "\n���ϴ�:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalTxBytes)
				+ "\n��    ��:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalRxBytes + totalTxBytes));
		// ����
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		tv_moblie_traffic.setText("������:"
				+ Formatter.formatFileSize(getApplicationContext(),
						mobileRxBytes)
				+ "\n���ϴ�:"
				+ Formatter.formatFileSize(getApplicationContext(),
						mobileTxBytes)
				+ "\n��    ��:"
				+ Formatter.formatFileSize(getApplicationContext(),
						mobileRxBytes + mobileTxBytes));
		new Thread(){
			public void run() {
				trafficInfos = TrafficInfoProvider
						.getTrafficInfos(getApplicationContext());
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						MyAdapter adapter = new MyAdapter();
						lv.setAdapter(adapter);
						
					}
					
				});
				
				
			};
			
		}.start();
		
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return trafficInfos.size();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			Holder holder=null;
			if (convertView != null) {
				view = convertView;
				holder=(Holder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_traffic_details, null);
				holder=new Holder();
				holder.appName=(TextView) view.findViewById(R.id.tv_item_traffic_appName);// app��
				holder.txBytes=(TextView) view.findViewById(R.id.tv_item_traffic_txBytes);// �ϴ�
				holder.rxBytes=(TextView) view.findViewById(R.id.tv_item_traffic_rxBytes);// ����
				holder.totalBytes=(TextView) view.findViewById(R.id.tv_item_traffic_totalBytes);// �ܼ�
				view.setTag(holder);
			}
			TrafficInfo trafficInfo=trafficInfos.get(position);
			String appName=trafficInfo.getAppName();
			String tx=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getTxBytes());
			String rx=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getRxBytes());
			String total=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getTotalBytes());
			
			holder.appName.setText(appName);
			holder.txBytes.setText("�ϴ�:"+tx+"  ");
			holder.rxBytes.setText("����:"+rx+"  ");
			holder.totalBytes.setText("��:"+total);
			
			
			return view;
		}
	}

	class Holder {
		TextView appName;
		TextView txBytes;
		TextView rxBytes;
		TextView totalBytes;

	}

}
