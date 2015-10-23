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
	// ����ListView������ʾ������ϵ��
	private ListView lv_select_contact;
	private LinearLayout ll_loading;
	// ������ϵ���б�,���ڴ洢�ֻ���������ϵ��
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		// �����ڼ��صĲ�����ʾ����
		ll_loading.setVisibility(View.VISIBLE); 
		// ��ʼ��ListView
		lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
		//��ȡ��ϵ��ʱ��ʱ����,ͨ�����̻߳�õ�
		new Thread(){
			@Override
			public void run() {
				final Long startTime=SystemClock.uptimeMillis();
				// ��ʼ����ϵ���б���Ϣ
				infos = ContactInfoUtils.getAllContactInfos(SelectContactActivity.this);
				//super.run();
				//���̲߳�����ʾUI,ֻ��ͨ��UI���߳�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i("��ʱ", SystemClock.uptimeMillis()-startTime+"");
						//�ص����ڼ��ضԻ���
						ll_loading.setVisibility(View.INVISIBLE);
						//����ϵ��д��ListView
						lv_select_contact.setAdapter(new ContactAdapter());
					}
				});
			}
			
		}.start();
		
	}

	class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// ������ϵ��
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
			/*�˴���������Log��ӡ,�����ָ��,��Ϊ��ϵ������������
			//Log.i("��ϵ������", infos.get(position).getName());
			//Log.i("��ϵ�˵绰", infos.get(position).getPhone());
			  */
			name.setText(infos.get(position).getName() + "");
			phone.setText(infos.get(position).getPhone() + "");
			return view;

		}

	}
}
