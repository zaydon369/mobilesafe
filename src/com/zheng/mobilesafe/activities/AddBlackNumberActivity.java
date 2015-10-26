package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class AddBlackNumberActivity extends Activity {
	// �����������
	EditText et_addblack_number;
	// ģʽ��ѡ��
	RadioGroup rg_addblack_mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blacknumber);
		// ��ʼ���ؼ�
		et_addblack_number = (EditText) findViewById(R.id.et_addblack_number);
		rg_addblack_mode = (RadioGroup) findViewById(R.id.rg_addblack_mode);
	}

	public void save(View view) {
		//��ȡ�����ĺ����ģʽ
		String phone=et_addblack_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
			return ;
		}
		int id=rg_addblack_mode.getCheckedRadioButtonId();
		String mode="1";
		switch (id) {
		case R.id.rb_addblack_phone:
			mode="1";
			break;

		case R.id.rb_addblack_sms:
			mode="2";
			break;
		case R.id.rb_addblack_all:
			mode="3";
			break;
		}
		//�����ݴ浽���ݿ�,��֮ǰ�ж��ֻ������Ƿ����
		BlackNumberDao dao=new BlackNumberDao(this);
		if(TextUtils.isEmpty(dao.find(phone))){
			//���Ϊ�վ����
			if(dao.add(phone, mode)){
				Toast.makeText(this, "��ӳɹ�", 0).show();	
				finish();
			}else{
				Toast.makeText(this, "���ʧ��", 0).show();
				return;
			}
			
		}else{
			//������ʾ�����Ѵ���
			Toast.makeText(this, "�����Ѵ���", 0).show();
			return;
		}
	}

	public void cancel(View view) {
		finish();
	}

}
