package com.zheng.mobilesafe.baseadapter;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zheng.mobilesafe.baseholder.MyBaseHolder;

public abstract class MyBaseAdapter<T> extends BaseAdapter{
	//��Ϊ��֪����������������,�����÷���
	private ArrayList <T> mData;
	private MyBaseHolder holder;
	
	public MyBaseAdapter(ArrayList<T> mData) {
		this.mData=mData;
	}
//	private MyBaseAdapter() {}
	@Override
	public int getCount() {
		
		return mData.size();
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
		//ͨ��convertView,���holder����
		if(convertView!=null){
			//�����Ϊ��,ֱ���ó�holder
			holder  = (MyBaseHolder) convertView.getTag();
			if(holder==null){
				holder=getHolder();
			}
		
		}else{
			//��һ���µ�holder��ֵ��ȥ
			holder=getHolder();
		}
		//��holder������ʾ����
		holder.setmData(mData.get(position));
		Log.i("getView", position+"");
		return holder.getView();
	}
	protected abstract MyBaseHolder getHolder();

}
