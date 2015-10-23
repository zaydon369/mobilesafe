package com.zheng.mobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zheng.mobilesafe.R;

/**
 * ������,����1
 */
public class Setup3Activity extends SetupBaseActivity {
	Button bt_setup3_selectContacts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		//�ҵ���Ӱ�ȫ����İ�ť
		bt_setup3_selectContacts=(Button) findViewById(R.id.bt_setup3_selectContacts);
		//����ť����¼�
		bt_setup3_selectContacts.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//���ϵͳ��ϵ��
				//��ѡ����ϵ�˽���
				Intent intent =new Intent(Setup3Activity.this,SelectContactActivity.class);
				startActivity(intent);
				
			}});

	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup2Activity.class);
	}

	@Override
	public void next() {
		openNewActivityAndFinish(Setup4Activity.class);
	}

}
