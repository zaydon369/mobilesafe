package com.zheng.mobilesafe.baseadapter;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zheng.mobilesafe.baseholder.MyBaseHolder;

public abstract class MyBaseAdapter<T> extends BaseAdapter{
	//因为不知道传进的数据类型,所以用泛型
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
		//通过convertView,填充holder返回
		if(convertView!=null){
			//如果不为空,直接拿出holder
			holder  = (MyBaseHolder) convertView.getTag();
			if(holder==null){
				holder=getHolder();
			}
		
		}else{
			//将一个新的holder赋值进去
			holder=getHolder();
		}
		//给holder设置显示数据
		holder.setmData(mData.get(position));
		Log.i("getView", position+"");
		return holder.getView();
	}
	protected abstract MyBaseHolder getHolder();

}
