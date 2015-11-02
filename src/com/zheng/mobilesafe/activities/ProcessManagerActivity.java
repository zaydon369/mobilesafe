package com.zheng.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		tv_process_desc = (TextView) findViewById(R.id.tv_process_desc);
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
						tv_process_desc.setText("进程数量"
								+ runningProcessInfos.size() + "个");
						ll_itemprocess_progress.setVisibility(View.GONE);
						adapter = new MyAdapter(runningProcessInfos);
						lv_process_infos.setAdapter(adapter);
						// 给listview设置条目点击事件
						lv_process_infos
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										ProcessInfo info = runningProcessInfos
												.get(position);
										// 更改点中的条目对应的check状态,并改变bean属性中的值
										if (info.isChecked()) {
											info.setChecked(false);
										} else {
											info.setChecked(true);
										}
										// 获取当前选中状态的视图
										View viewItem = (View) lv_process_infos
												.getItemAtPosition(position);
										// 找到当前的check
										CheckBox cb = (CheckBox) viewItem
												.findViewById(R.id.cb_itemProcess_status);
										// 将cb设置成bean中的值
										cb.setChecked(info.isChecked());
										// 点击完后,刷新listview,显示数据
										adapter.notifyDataSetChanged();
									}
								});

					}
				});
			};

		}.start();

	}

	/**
	 * 显示进程列表的适配器
	 * 
	 * @author asus
	 * 
	 */
	class MyAdapter extends MyBaseAdapter {

		public MyAdapter(List mData) {
			super(mData);
		}

		@Override
		protected MyBaseHolder getHolder() {
			return new MyHolder();
		}

	}

	/**
	 * 进程列表listview继承的详细控件
	 * 
	 * @author asus
	 * 
	 */
	class MyHolder extends MyBaseHolder {
		View view;
		ImageView iv_itemprocess_icont;
		TextView tv_itemprocess_name;
		TextView tv_itemprocess_size;
		CheckBox cb_itemProcess_status;

		// 初始化进程列表的控件
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

		// 给进程列表的控件刷新数据
		@Override
		protected void refreshView() {
			ProcessInfo info = (ProcessInfo) getmData();
			if ((info.getAppIcon()) != null) {
				iv_itemprocess_icont.setBackgroundDrawable(info.getAppIcon());
			} else {
				iv_itemprocess_icont
						.setBackgroundResource(R.drawable.ic_launcher);
			}
			tv_itemprocess_name.setText(info.getAppName());
			tv_itemprocess_size.setText(Formatter.formatFileSize(
					getApplicationContext(), info.getMemSize()));
			cb_itemProcess_status = (CheckBox) view
					.findViewById(R.id.cb_itemProcess_status);
			cb_itemProcess_status.setChecked(info.isChecked());

		}

	}

	/**
	 * 一键清理选中的进程
	 * 
	 * @param view
	 */
	public void onekeyClear(View view) {
		// 根据包名,全部删除,删除不掉的也移除列表
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ArrayList<ProcessInfo> killedInfos=new ArrayList<ProcessInfo>();
		//遍历杀死进程
		for (ProcessInfo info : runningProcessInfos) {
			if(info.isChecked()){
				String packageName = info.getPackName();
				am.killBackgroundProcesses(packageName);
				//将杀死的进程存到新的集合中
				killedInfos.add(info);
			}
			
		}
		//将杀死的进程从集合中移除,并刷新
		runningProcessInfos.removeAll(killedInfos);
		int total=0;
		for (ProcessInfo info : killedInfos) {
			total+=info.getMemSize();
		}
		Toast.makeText(getApplicationContext(), "结束了"+killedInfos.size()+"个进程,释放了"+Formatter.formatFileSize(getApplicationContext(), total)+"内存", 0).show();
		tv_process_desc.setText("进程数量"
				+runningProcessInfos.size()+ "个");
		adapter.notifyDataSetChanged();
	}

	/**
	 * 选择全部进程
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		// 将list集合遍历一下,将是否选中全部选true,然后刷新适配器
		// List<ProcessInfo> runningProcessInfos;
		for (ProcessInfo info : runningProcessInfos) {
			if (info != null) {
				info.setChecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 选择未选中的进程
	 * 
	 * @param view
	 */
	public void selectOther(View view) {
		// 将list集合遍历一下,将是否选中全部取反,然后刷新适配器
		// List<ProcessInfo> runningProcessInfos;
		for (ProcessInfo info : runningProcessInfos) {
			if (info != null) {
				info.setChecked(!info.isChecked());
			}
		}
		adapter.notifyDataSetChanged();
	}
}
