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
	// ���ȼ�����ʾ
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
		// ���̼߳���UI
		new Thread() {
			public void run() {
				// ��ʾ���ڼ�����
				ll_itemprocess_progress.setVisibility(View.VISIBLE);
				// ��ʼ��ȫ�����̵���Ϣ
				runningProcessInfos = ProcessInfoProvider
						.getRunningProcessInfos(getApplicationContext());
				// ����UI���߳�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_process_desc.setText("��������"
								+ runningProcessInfos.size() + "��");
						ll_itemprocess_progress.setVisibility(View.GONE);
						adapter = new MyAdapter(runningProcessInfos);
						lv_process_infos.setAdapter(adapter);
						// ��listview������Ŀ����¼�
						lv_process_infos
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										ProcessInfo info = runningProcessInfos
												.get(position);
										// ���ĵ��е���Ŀ��Ӧ��check״̬,���ı�bean�����е�ֵ
										if (info.isChecked()) {
											info.setChecked(false);
										} else {
											info.setChecked(true);
										}
										// ��ȡ��ǰѡ��״̬����ͼ
										View viewItem = (View) lv_process_infos
												.getItemAtPosition(position);
										// �ҵ���ǰ��check
										CheckBox cb = (CheckBox) viewItem
												.findViewById(R.id.cb_itemProcess_status);
										// ��cb���ó�bean�е�ֵ
										cb.setChecked(info.isChecked());
										// ������,ˢ��listview,��ʾ����
										adapter.notifyDataSetChanged();
									}
								});

					}
				});
			};

		}.start();

	}

	/**
	 * ��ʾ�����б��������
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
	 * �����б�listview�̳е���ϸ�ؼ�
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

		// ��ʼ�������б�Ŀؼ�
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

		// �������б�Ŀؼ�ˢ������
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
	 * һ������ѡ�еĽ���
	 * 
	 * @param view
	 */
	public void onekeyClear(View view) {
		// ���ݰ���,ȫ��ɾ��,ɾ��������Ҳ�Ƴ��б�
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ArrayList<ProcessInfo> killedInfos=new ArrayList<ProcessInfo>();
		//����ɱ������
		for (ProcessInfo info : runningProcessInfos) {
			if(info.isChecked()){
				String packageName = info.getPackName();
				am.killBackgroundProcesses(packageName);
				//��ɱ���Ľ��̴浽�µļ�����
				killedInfos.add(info);
			}
			
		}
		//��ɱ���Ľ��̴Ӽ������Ƴ�,��ˢ��
		runningProcessInfos.removeAll(killedInfos);
		int total=0;
		for (ProcessInfo info : killedInfos) {
			total+=info.getMemSize();
		}
		Toast.makeText(getApplicationContext(), "������"+killedInfos.size()+"������,�ͷ���"+Formatter.formatFileSize(getApplicationContext(), total)+"�ڴ�", 0).show();
		tv_process_desc.setText("��������"
				+runningProcessInfos.size()+ "��");
		adapter.notifyDataSetChanged();
	}

	/**
	 * ѡ��ȫ������
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		// ��list���ϱ���һ��,���Ƿ�ѡ��ȫ��ѡtrue,Ȼ��ˢ��������
		// List<ProcessInfo> runningProcessInfos;
		for (ProcessInfo info : runningProcessInfos) {
			if (info != null) {
				info.setChecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * ѡ��δѡ�еĽ���
	 * 
	 * @param view
	 */
	public void selectOther(View view) {
		// ��list���ϱ���һ��,���Ƿ�ѡ��ȫ��ȡ��,Ȼ��ˢ��������
		// List<ProcessInfo> runningProcessInfos;
		for (ProcessInfo info : runningProcessInfos) {
			if (info != null) {
				info.setChecked(!info.isChecked());
			}
		}
		adapter.notifyDataSetChanged();
	}
}
