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
	// 黑名单输入框
	EditText et_addblack_number;
	// 模式单选框
	RadioGroup rg_addblack_mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blacknumber);
		// 初始化控件
		et_addblack_number = (EditText) findViewById(R.id.et_addblack_number);
		rg_addblack_mode = (RadioGroup) findViewById(R.id.rg_addblack_mode);
	}

	public void save(View view) {
		//获取输入框的号码和模式
		String phone=et_addblack_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
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
		//将数据存到数据库,存之前判断手机号码是否存在
		BlackNumberDao dao=new BlackNumberDao(this);
		if(TextUtils.isEmpty(dao.find(phone))){
			//如果为空就添加
			if(dao.add(phone, mode)){
				Toast.makeText(this, "添加成功", 0).show();	
				finish();
			}else{
				Toast.makeText(this, "添加失败", 0).show();
				return;
			}
			
		}else{
			//否则提示号码已存在
			Toast.makeText(this, "号码已存在", 0).show();
			return;
		}
	}

	public void cancel(View view) {
		finish();
	}

}
