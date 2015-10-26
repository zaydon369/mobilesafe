package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zheng.mobilesafe.R;

public class CallSmsSafeActivity extends Activity {
	//listview
	private ListView lv_callsms_safe;
	//适配器,全局是为了更新方便
	 private MyAdapter adapter;
	//集合
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		//
		lv_callsms_safe =(ListView) findViewById(R.id.lv_callsms_safe);
		adapter=new MyAdapter();
		lv_callsms_safe.setAdapter(adapter);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public void addBlackNumber(View view){
	
		Intent intent=new Intent(this,AddBlackNumberActivity.class);
		startActivityForResult(intent, 0);
	}
}
