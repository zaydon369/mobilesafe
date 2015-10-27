package com.zheng.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;
import com.zheng.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
	// listview
	private ListView lv_callsms_safe;
	// ������,ȫ����Ϊ�˸��·���
	private MyAdapter adapter;
	// ���ݿ�
	BlackNumberDao dao;
	// ����
	List<BlackNumberInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		//
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		adapter = new MyAdapter();

		// ������ݿ�����
		dao = new BlackNumberDao(this);
		// �õ�������������Ϣ,
		infos = dao.findAll();
		if (infos.size() > 0) {
			lv_callsms_safe.setAdapter(adapter);
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			BlackNumberInfo info = infos.get(position);
			// ��һ���Ż�,����View����
			View view;
			// �ڶ����Ż�,�����ֶ����ȡ����
			ViewHolder vh;
			if (convertView == null) {
				view = View.inflate(CallSmsSafeActivity.this,
						R.layout.item_callsms_safe, null);
				vh = new ViewHolder();
				vh.tv_item_phone = (TextView) view
						.findViewById(R.id.tv_item_phone);
				vh.tv_item_mode = (TextView) view
						.findViewById(R.id.tv_item_mode);
				vh.iv_item_delete = (ImageView) findViewById(R.id.iv_item_delete);
				//���Ӳ��ַŽ�view��
				view.setTag(vh);
			} else {
				view = convertView;
				vh=(ViewHolder) view.getTag();
			}
			String phone = info.getPhone();
			vh.tv_item_phone.setText(phone);
			String mode = info.getMode();
			//�ж�ģʽ,ת�����ַ�
			if ("1".equals(mode)) {
				vh.tv_item_mode.setText("�绰����");
			} else if ("2".equals(mode)) {
				vh.tv_item_mode.setText("��������");
			} else if ("3".equals(mode)) {
				vh.tv_item_mode.setText("ȫ������");
			}

			return view;
		}

	}

	/**
	 * ListView���Ӳ���
	 * 
	 * @author asus
	 * 
	 */
	class ViewHolder {
		// �绰,ģʽ,ɾ����Сͼ��
		TextView tv_item_phone;
		TextView tv_item_mode;
		ImageView iv_item_delete;
	}

	public void addBlackNumber(View view) {

		Intent intent = new Intent(this, AddBlackNumberActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			// ��������������,��ȡ,�浽list����
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("phone"));
			info.setMode(data.getExtras().getString("mode"));
			infos.add(info);
			// ͨ��Adapterˢ��listview
			adapter.notifyDataSetChanged();

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
