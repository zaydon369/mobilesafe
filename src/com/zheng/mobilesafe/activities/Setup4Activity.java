package com.zheng.mobilesafe.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.ui.receiver.MyAdmin;

/**
 * ������,����1
 */
public class Setup4Activity extends SetupBaseActivity {
	CheckBox cb_setup4_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_setup4_status = (CheckBox) findViewById(R.id.cb_setup4_status);
		// ����״̬
		cb_setup4_status.setChecked(sp.getBoolean("protecting", false));
		// �����߿����ø����¼�����
		cb_setup4_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
							if(isChecked){
								//ѡ��ʱ,�ж��Ƿ��г�������ԱȨ��,���û����ʾ����
								if(MyAdmin.isAdmin(Setup4Activity.this)){	
									//����г�������Ա,��ʾ���������ɹ�����
								Toast.makeText(Setup4Activity.this, "������������", 0).show();
								}else{
									MyAdmin.openAdmin(Setup4Activity.this);
								}
							}else{
								Toast.makeText(Setup4Activity.this, "ȡ����������", 0).show();
							}
							//��ѡ��״̬���浽�����ļ���
							Editor edit = sp.edit();
							edit.putBoolean("protecting", isChecked);
							edit.commit();
					}
				});

	}

	@Override
	public void showNext(View view) {
		// ��д�������һ������
		super.showNext(view);
		Editor edit = sp.edit();
		// ����Ѿ��߹�����������
		edit.putBoolean("configed", true);
		edit.commit();
	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup3Activity.class);
	}

	@Override
	public void next() {
		openNewActivityAndFinish(LostFindActivity.class);
	}
}
