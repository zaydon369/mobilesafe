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
 * 设置向导,界面1
 */
public class Setup3Activity extends SetupBaseActivity {
	// 数据共享sp
	private SharedPreferences sp;
	// 定义需要用到的控件
	private Button bt_setup3_selectContacts;
	private EditText et_setup3_safeNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		// 初始化sp
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 找到添加安全号码的按钮
		bt_setup3_selectContacts = (Button) findViewById(R.id.bt_setup3_selectContacts);
		et_setup3_safeNumber = (EditText) findViewById(R.id.et_setup3_safeNumber);
		// 给edit回显数据
		et_setup3_safeNumber.setText(sp.getString("safeNumber", ""));
		// 给按钮添加事件
		bt_setup3_selectContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获得系统联系人
				// 打开选择联系人界面
				Intent intent = new Intent(Setup3Activity.this,
						SelectContactActivity.class);
				startActivityForResult(intent, 0);
			}
		});

	}

	/**
	 * 获取上一个activity的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 如果数据不为空,就把它回显到edit输入框
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
		//下一步之前先保存安全号码
		String safeNumber=et_setup3_safeNumber.getText().toString().trim();
		if(TextUtils.isEmpty(safeNumber)){
			//提示安全号码不能为空
			Toast.makeText(Setup3Activity.this, "安全号码不能为空", 0).show();
			return;
		}else{//将安全号码保存到配置文件
			Editor edit = sp.edit();
			edit.putString("safeNumber", safeNumber);
			edit.commit();
		}
		
		openNewActivityAndFinish(Setup4Activity.class);
	}

}
