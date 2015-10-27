package com.zheng.mobilesafe.activities;

import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		lv_callsms_safe.setAdapter(adapter);
		// �����޸�ģʽ
		lv_callsms_safe
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// �õ���ǰѡ����Ŀ����Ϣ,��������ҳ��
						String phone = infos.get(position).getPhone();
						String mode = infos.get(position).getMode();
						// ������ͼ
						Intent intent = new Intent(getApplicationContext(),
								UpdateBlackNumberActivity.class);
						// ����ͼ�������
						intent.putExtra("phone", phone);
						intent.putExtra("mode", mode);
						intent.putExtra("position", position);
						startActivityForResult(intent, 1);
						return true;
					}
				});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			ImageView iv_empty = (ImageView) findViewById(R.id.iv_callsms_empty);
			if (infos.size() > 0) {
				// ��ͼƬ����
				iv_empty.setVisibility(View.INVISIBLE);
			} else {
				// ��ʾ
				iv_empty.setVisibility(View.VISIBLE);
			}

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
			final BlackNumberInfo info = infos.get(position);
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
				vh.iv_item_delete = (ImageView) view
						.findViewById(R.id.iv_item_delete);
				// ���Ӳ��ַŽ�view��
				view.setTag(vh);
			} else {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			}
			final String phone = info.getPhone();
			vh.tv_item_phone.setText(phone);
			String mode = info.getMode();
			// �ж�ģʽ,ת�����ַ�
			if ("1".equals(mode)) {
				vh.tv_item_mode.setText("�绰����");
			} else if ("2".equals(mode)) {
				vh.tv_item_mode.setText("��������");
			} else if ("3".equals(mode)) {
				vh.tv_item_mode.setText("ȫ������");
			}
			// ��imgͼƬ���ɾ������¼�
			vh.iv_item_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (dao.delete(phone)) {
						// �����б�
						infos.remove(info);
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "ɾ���ɹ�", 0)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "ɾ��ʧ��", 0)
								.show();
					}
				}

			});

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
		if (data != null && resultCode == 0) {
			// ��������������,��ȡ,�浽list����
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("phone"));
			info.setMode(data.getExtras().getString("mode"));
			infos.add(info);
			// ͨ��Adapterˢ��listview
			adapter.notifyDataSetChanged();

		}
		if (data != null && resultCode == 1) {
			//���ݵ�ǰlist���±�
			int position=data.getIntExtra("position", -1);
			//�Ƴ��ɵ���Ϣ
			infos.remove(position);
			//����µ���Ϣ
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("newPhone"));
			info.setMode(data.getExtras().getString("newMode"));
			infos.add(info);
			// ͨ��Adapterˢ��listview
			adapter.notifyDataSetChanged();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
