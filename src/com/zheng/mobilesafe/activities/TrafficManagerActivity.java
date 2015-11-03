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
		// 计算总流量
		long totalRxBytes = TrafficStats.getTotalRxBytes();// 下载
		long totalTxBytes = TrafficStats.getTotalTxBytes();// 上传
		tv_total_traffic.setText("总下载:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalRxBytes)
				+ "\n总上传:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalTxBytes)
				+ "\n合    计:"
				+ Formatter.formatFileSize(getApplicationContext(),
						totalRxBytes + totalTxBytes));
		// 下载
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		tv_moblie_traffic.setText("总下载:"
				+ Formatter.formatFileSize(getApplicationContext(),
						mobileRxBytes)
				+ "\n总上传:"
				+ Formatter.formatFileSize(getApplicationContext(),
						mobileTxBytes)
				+ "\n合    计:"
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
				holder.appName=(TextView) view.findViewById(R.id.tv_item_traffic_appName);// app名
				holder.txBytes=(TextView) view.findViewById(R.id.tv_item_traffic_txBytes);// 上传
				holder.rxBytes=(TextView) view.findViewById(R.id.tv_item_traffic_rxBytes);// 下载
				holder.totalBytes=(TextView) view.findViewById(R.id.tv_item_traffic_totalBytes);// 总计
				view.setTag(holder);
			}
			TrafficInfo trafficInfo=trafficInfos.get(position);
			String appName=trafficInfo.getAppName();
			String tx=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getTxBytes());
			String rx=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getRxBytes());
			String total=Formatter.formatFileSize(getApplicationContext(), trafficInfo.getTotalBytes());
			
			holder.appName.setText(appName);
			holder.txBytes.setText("上传:"+tx+"  ");
			holder.rxBytes.setText("下载:"+rx+"  ");
			holder.totalBytes.setText("共:"+total);
			
			
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
