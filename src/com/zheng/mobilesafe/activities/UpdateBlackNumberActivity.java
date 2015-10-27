package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class UpdateBlackNumberActivity extends Activity {
	// �����������
	EditText et_update_number;
	// ģʽ��ѡ��
	RadioGroup rg_update_mode;
//ԭʼ����
	String phone;
	String mode;
	int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_blacknumber);
		// ��ʼ���ؼ�
		et_update_number = (EditText) findViewById(R.id.et_update_number);
		rg_update_mode = (RadioGroup) findViewById(R.id.rg_update_mode);
		//�����ת��������ͼ
		Intent intent=getIntent();
		//���ݻ���
		 phone=intent.getStringExtra("phone");
		 mode=intent.getStringExtra("mode");
		 position=intent.getIntExtra("position",-1);
		et_update_number.setText(phone);
		if("1".equals(mode)){
			rg_update_mode.check(R.id.rb_update_phone);
		}else if("2".equals(mode)){
			rg_update_mode.check(R.id.rb_update_sms);
		}else if("3".equals(mode)){
			rg_update_mode.check(R.id.rb_update_all);
		}
		
	}

	public void save(View view) {
		//��ȡ�����ĺ����ģʽ
		String newPhone=et_update_number.getText().toString().trim();
		if(TextUtils.isEmpty(newPhone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
			return ;
		}
		int id=rg_update_mode.getCheckedRadioButtonId();
		String newMode="1";
		switch (id) {
		case R.id.rb_update_phone:
			newMode="1";
			break;

		case R.id.rb_update_sms:
			newMode="2";
			break;
		case R.id.rb_update_all:
			newMode="3";
			break;
		}
		
		BlackNumberDao dao=new BlackNumberDao(this);
		//�����뷢���ı�ʱ,�ȼ��ı��ĺ������������Ƿ����
		if(!newPhone.equals(phone)){
			//������ݿ���������
			if(!TextUtils.isEmpty(dao.find(newPhone))){
				Toast.makeText(this, "�����Ѵ���", 0).show();
				return;
			}else{
				//������벻���ھ����,
				// TODO ���ʹ������,�´��п�����
				if(dao.add(newPhone, newMode)){
					//��ӳɹ���,ɾ����ԭ��������
					if(dao.delete(phone)){
						Toast.makeText(this, "���³ɹ�", 0).show();
					}
				}
			}
		}else{
		//������û�з����ı�ʱ,newPhone����phone��һ��
			if(dao.updateMode(newPhone, newMode)){
				Toast.makeText(this, "���³ɹ�", 0).show();	
				
			}else{
				Toast.makeText(this, "����ʧ��", 0).show();
				return;
			}
		}
		//���,����һ����ͼ,�����ݴ浽Result����,������һ������
		Intent data=new Intent();
		data.putExtra("position", position);
		data.putExtra("newPhone", newPhone);
		data.putExtra("newMode", newMode);
		setResult(1, data);
		finish();
	}

	public void cancel(View view) {
		finish();
	}

}
