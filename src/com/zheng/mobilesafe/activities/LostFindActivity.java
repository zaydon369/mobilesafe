package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private ImageView iv_lostfind_status;
	private TextView tv_lostfind_safeNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind);
		// ��ʼ����������
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ��ʼ��ͼ��ؼ�
		iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
		tv_lostfind_safeNumber=(TextView) findViewById(R.id.tv_lostfind_safeNumber);
		tv_lostfind_safeNumber.setText(sp.getString("safeNumber", "number"));
		//�鿴�����ļ��ķ�������״̬,����ͼ�����
		if(sp.getBoolean("protecting", false)){
			iv_lostfind_status.setImageResource(R.drawable.lock);
		}else{
			iv_lostfind_status.setImageResource(R.drawable.unlock);
		}
	}

	/**
	 * ���½�����
	 * 
	 * @param view
	 */
	public void reseting(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();//֮ǰ���õ�ջ,����������ת���ܸ�������
	}
	/**
	 * ���ķ�������״̬
	 */
	public void changeProtectStatus(View view) {
		if(sp.getBoolean("protecting", false)){//�����ǰѡ��,�����ȡ��
			Editor edit = sp.edit();
			edit.putBoolean("protecting", false);
			edit.commit();
			iv_lostfind_status.setImageResource(R.drawable.unlock);
			Toast.makeText(LostFindActivity.this, "�رշ�������", 0).show();
			
		}else{//�����ǰ״̬�ر�,�������
			Editor edit = sp.edit();
			edit.putBoolean("protecting", true);
			edit.commit();
			iv_lostfind_status.setImageResource(R.drawable.lock);
			Toast.makeText(LostFindActivity.this, "���������ѿ���", 0).show();
		}
	}
}
