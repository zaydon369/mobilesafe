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
	// 适配器,全局是为了更新方便
	private MyAdapter adapter;
	// 数据库
	BlackNumberDao dao;
	// 集合
	List<BlackNumberInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		//
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		adapter = new MyAdapter();

		// 获得数据库连接
		dao = new BlackNumberDao(this);
		// 得到黑名单集合信息,
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
			// 第一步优化,回收View对象
			View view;
			// 第二步优化,将布局对象抽取出来
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
				//将子布局放进view中
				view.setTag(vh);
			} else {
				view = convertView;
				vh=(ViewHolder) view.getTag();
			}
			String phone = info.getPhone();
			vh.tv_item_phone.setText(phone);
			String mode = info.getMode();
			//判断模式,转换成字符
			if ("1".equals(mode)) {
				vh.tv_item_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				vh.tv_item_mode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				vh.tv_item_mode.setText("全部拦截");
			}

			return view;
		}

	}

	/**
	 * ListView的子布局
	 * 
	 * @author asus
	 * 
	 */
	class ViewHolder {
		// 电话,模式,删除的小图标
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
			// 将传过来的数据,读取,存到list集合
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("phone"));
			info.setMode(data.getExtras().getString("mode"));
			infos.add(info);
			// 通过Adapter刷新listview
			adapter.notifyDataSetChanged();

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
