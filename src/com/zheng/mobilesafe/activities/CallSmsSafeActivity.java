package com.zheng.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
		if(infos.size()>0){
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
			View view = View.inflate(CallSmsSafeActivity.this, R.layout.item_callsms_safe,
					null);
			TextView tv_item_phone = (TextView) view
					.findViewById(R.id.tv_item_phone);
			TextView tv_item_mode = (TextView) view
					.findViewById(R.id.tv_item_mode);
			String phone = info.getPhone();
			tv_item_phone.setText(phone);
			String mode = info.getMode();
			
			if ("1".equals(mode)) {
				tv_item_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				tv_item_mode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				tv_item_mode.setText("全部拦截");
			}

			return view;
		}

	}

	public void addBlackNumber(View view) {

		Intent intent = new Intent(this, AddBlackNumberActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			//将传过来的数据,读取,存到list集合
			BlackNumberInfo info=new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("phone"));
			info.setMode(data.getExtras().getString("mode"));
			infos.add(info);
			//通过Adapter刷新listview
			adapter.notifyDataSetChanged();
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
