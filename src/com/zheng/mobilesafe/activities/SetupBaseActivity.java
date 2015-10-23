package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	/**
	 * ��һҳ,��ť�ķ���
	 * @param view
	 */
	public void showPre(View view){
		pre();
	}
	/**
	 * ��һҳ,��ť�ķ���
	 * @param view
	 */
	public void showNext(View view){
		next();
	}

}
