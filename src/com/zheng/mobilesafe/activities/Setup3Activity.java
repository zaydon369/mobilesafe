package com.zheng.mobilesafe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zheng.mobilesafe.R;

/**
 * ������,����1
 */
public class Setup3Activity extends SetupBaseActivity {
	// ���ݹ���sp
	private SharedPreferences sp;
	// ������Ҫ�õ��Ŀؼ�
	private Button bt_setup3_selectContacts;
	private EditText et_setup3_safeNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		// ��ʼ��sp
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �ҵ���Ӱ�ȫ����İ�ť
		bt_setup3_selectContacts = (Button) findViewById(R.id.bt_setup3_selectContacts);
		et_setup3_safeNumber = (EditText) findViewById(R.id.et_setup3_safeNumber);
		// ��edit��������
		et_setup3_safeNumber.setText(sp.getString("safeNumber", ""));
		// ����ť����¼�
		bt_setup3_selectContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ���ϵͳ��ϵ��
				// ��ѡ����ϵ�˽���
				Intent intent = new Intent(Setup3Activity.this,
						SelectContactActivity.class);
				startActivityForResult(intent, 0);
			}
		});

	}

	/**
	 * ��ȡ��һ��activity������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ������ݲ�Ϊ��,�Ͱ������Ե�edit�����
		if (data != null) {
			et_setup3_safeNumber.setText(data.getStringExtra("safeNumber"));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void pre() {
		openNewActivityAndFinish(Setup2Activity.class);
	}

	@Override
	public void next() {
		//��һ��֮ǰ�ȱ��氲ȫ����
		String safeNumber=et_setup3_safeNumber.getText().toString().trim();
		if(TextUtils.isEmpty(safeNumber)){
			//��ʾ��ȫ���벻��Ϊ��
			Toast.makeText(Setup3Activity.this, "��ȫ���벻��Ϊ��", 0).show();
			return;
		}else{//����ȫ���뱣�浽�����ļ�
			Editor edit = sp.edit();
			edit.putString("safeNumber", safeNumber);
			edit.commit();
		}
		
		openNewActivityAndFinish(Setup4Activity.class);
	}

}
