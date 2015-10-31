package com.zheng.mobilesafe.baseholder;

import android.util.Log;
import android.view.View;

/**
 * holder的主要作用就是获得view的子控件,设置子控件的内容
 * 
 * @param <T>
 */
public abstract class MyBaseHolder<T> {
	private View view;
	private T mData;

	public T getmData() {
		return mData;
	}

	public void setmData(T mData) {
		this.mData = mData;
		refreshView();
	}

	public MyBaseHolder() {
		view = initView();
		view.setTag(this);
	}

	/**
	 * 获取view对象
	 * 
	 * @return
	 */
	public View getView() {
		Log.i("MyBaseHolder","getView"+mData.toString());
		
		return view;
	}

	/**
	 * 初始化view,不同view不同功能
	 */
	protected abstract View initView();

	/**
	 * 刷新view对象
	 */
	protected abstract void refreshView();
}
