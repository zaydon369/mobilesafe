package com.zheng.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.baseadapter.MyBaseAdapter;
import com.zheng.mobilesafe.baseholder.MyBaseHolder;
import com.zheng.mobilesafe.domain.ProcessInfo;
import com.zheng.mobilesafe.engine.ProcessInfoProvider;

public class ProcessManagerActivity extends Activity {
	TextView tv_process_desc;
	ListView lv_process_infos;
	List<ProcessInfo> runningProcessInfos;
	// 进度加载提示
	LinearLayout ll_itemprocess_progress;
	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		tv_process_desc = (TextView) findViewById(R.id.tv_process_desc);
		lv_process_infos = (ListView) findViewById(R.id.lv_process_infos);
		ll_itemprocess_progress = (LinearLayout) findViewById(R.id.ll_itemprocess_progress);
		tv_process_desc=(TextView) findViewById(R.id.tv_process_desc);
		// 子线程加载UI
		new Thread() {
			public void run() {
				// 显示正在加载中
				ll_itemprocess_progress.setVisibility(View.VISIBLE);
				// 初始化全部进程的信息
				runningProcessInfos = ProcessInfoProvider
						.getRunningProcessInfos(getApplicationContext());
				// 开启UI子线程
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_process_desc.setText("进程数量"+runningProcessInfos.size()+"个");
						ll_itemprocess_progress.setVisibility(View.GONE);
						adapter = new MyAdapter(runningProcessInfos);
						lv_process_infos.setAdapter(adapter);
					}
				});
			};

		}.start();

	}

	class MyAdapter extends MyBaseAdapter {

		public MyAdapter(List mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {
			return new MyHolder();
		}

	}

	class MyHolder extends MyBaseHolder {
		View view;
		ImageView iv_itemprocess_icont;
		TextView tv_itemprocess_name;
		TextView tv_itemprocess_size;
		CheckBox cb_itemProcess_status;

		@Override
		protected View initView() {
			view = View.inflate(getApplicationContext(),
					R.layout.item_process_infos, null);
			iv_itemprocess_icont = (ImageView) view
					.findViewById(R.id.iv_itemprocess_icont);
			tv_itemprocess_name = (TextView) view
					.findViewById(R.id.tv_itemprocess_name);
			tv_itemprocess_size = (TextView) view
					.findViewById(R.id.tv_itemprocess_size);
			cb_itemProcess_status = (CheckBox) view
					.findViewById(R.id.cb_itemProcess_status);
			return view;
		}

		@Override
		protected void refreshView() {
			ProcessInfo info=(ProcessInfo) getmData();
			if((info.getAppIcon())!=null){
			 iv_itemprocess_icont.setBackgroundDrawable(info.getAppIcon());
			}else{
				iv_itemprocess_icont.setBackgroundResource(R.drawable.ic_launcher);
			}
			 tv_itemprocess_name.setText(info.getAppName());
			 tv_itemprocess_size.setText(Formatter.formatFileSize(getApplicationContext(), info.getMemSize()));
			 cb_itemProcess_status.setChecked(info.isChecked());

		}

	}
}
