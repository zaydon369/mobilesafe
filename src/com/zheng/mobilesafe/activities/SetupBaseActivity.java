package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
/**
 * 设置向导的抽象类
 * @author asus
 *
 */
public abstract  class SetupBaseActivity extends Activity {
	/**
	 * 共享参数,子类可以自己调用sp数据
	 */
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//得到SP文件
		sp=getSharedPreferences("config", MODE_PRIVATE);
	}
	/**
	 * 跳转到上一页
	 */
	public abstract void pre();
	/**
	 * 跳转到下一页
	 */
	public abstract void next();
	/**
	 * 打开新界面,并且关闭当前界面
	 * @param cls
	 */
	public void openNewActivityAndFinish(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
		finish();
	};
	
}
