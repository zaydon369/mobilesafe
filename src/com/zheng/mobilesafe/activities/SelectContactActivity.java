package com.zheng.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.ContactInfoUtils;
import com.zheng.mobilesafe.ui.domain.ContactInfo;

public class SelectContactActivity extends Activity {
	// 定义ListView用于显示所有联系人
	private ListView lv_select_contact;
	private LinearLayout ll_loading;
	// 定义联系人列表,用于存储手机的所有联系人
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		// 把正在加载的布局显示出来
		ll_loading.setVisibility(View.VISIBLE); 
		// 初始化ListView
		lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
		//读取联系人时耗时操作,通过子线程会好点
		new Thread(){
			@Override
			public void run() {
				final Long startTime=SystemClock.uptimeMillis();
				// 初始化联系人列表信息
				infos = ContactInfoUtils.getAllContactInfos(SelectContactActivity.this);
				//super.run();
				//子线程不能显示UI,只能通过UI子线程
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i("耗时", SystemClock.uptimeMillis()-startTime+"");
						//关掉正在加载对话框
						ll_loading.setVisibility(View.INVISIBLE);
						//将联系人写到ListView
						lv_select_contact.setAdapter(new ContactAdapter());
					}
				});
			}
			
		}.start();
		
	}

	class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// 所有联系人
			return infos.size();
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
			View view;
			if (convertView == null) {
				view = View.inflate(SelectContactActivity.this,
						R.layout.item_contact, null);
			} else {
				view = convertView;
			}
			TextView name = (TextView) view
					.findViewById(R.id.tv_contactitem_name);
			TextView phone = (TextView) view
					.findViewById(R.id.tv_contactitem_phone);
			/*此处不能设置Log打印,否则空指针,因为联系人数据量过大
			//Log.i("联系人姓名", infos.get(position).getName());
			//Log.i("联系人电话", infos.get(position).getPhone());
			  */
			name.setText(infos.get(position).getName() + "");
			phone.setText(infos.get(position).getPhone() + "");
			return view;

		}

	}
}
