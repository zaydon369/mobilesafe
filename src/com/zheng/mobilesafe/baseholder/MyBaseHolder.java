package com.zheng.mobilesafe.baseholder;

import android.util.Log;
import android.view.View;

/**
 * holder����Ҫ���þ��ǻ��view���ӿؼ�,�����ӿؼ�������
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
	 * ��ȡview����
	 * 
	 * @return
	 */
	public View getView() {
		Log.i("MyBaseHolder","getView"+mData.toString());
		
		return view;
	}

	/**
	 * ��ʼ��view,��ͬview��ͬ����
	 */
	protected abstract View initView();

	/**
	 * ˢ��view����
	 */
	protected abstract void refreshView();
}
