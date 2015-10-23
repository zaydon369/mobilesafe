package com.zheng.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zheng.mobilesafe.R;

/**
 * ������,����1
 */
public class Setup2Activity extends SetupBaseActivity {
	private LinearLayout ll_setup2_bind;
	private ImageView iv_setup2_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		// ��ð�SIM���Ĳ���
		ll_setup2_bind = (LinearLayout) findViewById(R.id.ll_setup2_bind);
		iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
		//�ж�֮ǰ�Ƿ��SIM��,���ж�Ӧ��ͼ������
		if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}else{
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
		// �����SIM
		ll_setup2_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ж��Ƿ��SIM,����󶨾ͽ��,û�󶨾Ͱ�

				if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
					// SIM��Ϊ��,���а�SIM��
					// ͨ���绰��������ȡSIM��������
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String sim = tm.getSimSerialNumber();
					if(!TextUtils.isEmpty(sim)){
					//��SIM��������д�������ļ�
					Editor editor=sp.edit();
					editor.putString("SIM", sim);
					editor.commit();
					//����״̬��ͼ��
					iv_setup2_status.setImageResource(R.drawable.lock);
					Toast.makeText(Setup2Activity.this, "SIM���ѳɹ���", 100).show();
					}else{
						Toast.makeText(Setup2Activity.this, "��ȡ����SIM������Ϣ", 100).show();	
					}
				}else{
					//���,��SIM����Ϊ��
					Editor editor=sp.edit();
					editor.putString("SIM", "");
					editor.commit();
					//����״̬����ͼ��
					iv_setup2_status.setImageResource(R.drawable.unlock);
					Toast.makeText(Setup2Activity.this, "SIM���ѽ��", 100).show();
				}

			}
		});

	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		openNewActivityAndFinish(Setup1Activity.class);
	}

	@Override
	public void next() {
		//�ж��Ƿ��SIM��,���û�а���������һ��
				if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
					Toast.makeText(Setup2Activity.this, "���SIM�������", 100).show();
				return ;
				}
		openNewActivityAndFinish(Setup3Activity.class);
		
	}
}
