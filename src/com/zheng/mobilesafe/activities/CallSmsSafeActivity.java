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
		lv_callsms_safe.setAdapter(adapter);
		// 长按修改模式
		lv_callsms_safe
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// 得到当前选中条目的信息,传到更改页面
						String phone = infos.get(position).getPhone();
						String mode = infos.get(position).getMode();
						// 创建意图
						Intent intent = new Intent(getApplicationContext(),
								UpdateBlackNumberActivity.class);
						// 往意图添加数据
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
				// 将图片隐藏
				iv_empty.setVisibility(View.INVISIBLE);
			} else {
				// 显示
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
				vh.iv_item_delete = (ImageView) view
						.findViewById(R.id.iv_item_delete);
				// 将子布局放进view中
				view.setTag(vh);
			} else {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			}
			final String phone = info.getPhone();
			vh.tv_item_phone.setText(phone);
			String mode = info.getMode();
			// 判断模式,转换成字符
			if ("1".equals(mode)) {
				vh.tv_item_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				vh.tv_item_mode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				vh.tv_item_mode.setText("全部拦截");
			}
			// 给img图片添加删除点击事件
			vh.iv_item_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (dao.delete(phone)) {
						// 更新列表
						infos.remove(info);
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "删除成功", 0)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "删除失败", 0)
								.show();
					}
				}

			});

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
		if (data != null && resultCode == 0) {
			// 将传过来的数据,读取,存到list集合
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("phone"));
			info.setMode(data.getExtras().getString("mode"));
			infos.add(info);
			// 通过Adapter刷新listview
			adapter.notifyDataSetChanged();

		}
		if (data != null && resultCode == 1) {
			//根据当前list的下标
			int position=data.getIntExtra("position", -1);
			//移除旧的信息
			infos.remove(position);
			//添加新的信息
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(data.getExtras().getString("newPhone"));
			info.setMode(data.getExtras().getString("newMode"));
			infos.add(info);
			// 通过Adapter刷新listview
			adapter.notifyDataSetChanged();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
