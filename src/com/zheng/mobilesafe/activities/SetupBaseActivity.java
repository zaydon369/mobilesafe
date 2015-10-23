package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
/**
 * �����򵼵ĳ�����
 * @author asus
 *
 */
public abstract  class SetupBaseActivity extends Activity {
	/**
	 * �������,��������Լ�����sp����
	 */
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//�õ�SP�ļ�
		sp=getSharedPreferences("config", MODE_PRIVATE);
	}
	/**
	 * ��ת����һҳ
	 */
	public abstract void pre();
	/**
	 * ��ת����һҳ
	 */
	public abstract void next();
	/**
	 * ���½���,���ҹرյ�ǰ����
	 * @param cls
	 */
	public void openNewActivityAndFinish(Class<?> cls){
		Intent intent=new Intent(this,cls);
		startActivity(intent);
		finish();
	};
	
}
