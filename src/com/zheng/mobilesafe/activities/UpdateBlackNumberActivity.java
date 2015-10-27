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
	// 黑名单输入框
	EditText et_update_number;
	// 模式单选框
	RadioGroup rg_update_mode;
//原始数据
	String phone;
	String mode;
	int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_blacknumber);
		// 初始化控件
		et_update_number = (EditText) findViewById(R.id.et_update_number);
		rg_update_mode = (RadioGroup) findViewById(R.id.rg_update_mode);
		//获得跳转过来的意图
		Intent intent=getIntent();
		//数据回显
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
		//获取输入框的号码和模式
		String newPhone=et_update_number.getText().toString().trim();
		if(TextUtils.isEmpty(newPhone)){
			Toast.makeText(this, "号码为空", 0).show();
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
		//当号码发生改变时,先检查改变后的号码在数据中是否存在
		if(!newPhone.equals(phone)){
			//如果数据库中有数据
			if(!TextUtils.isEmpty(dao.find(newPhone))){
				Toast.makeText(this, "号码已存在", 0).show();
				return;
			}else{
				//如果号码不存在就添加,
				// TODO 最好使用事务,下次有空再做
				if(dao.add(newPhone, newMode)){
					//添加成功后,删除掉原来的数据
					if(dao.delete(phone)){
						Toast.makeText(this, "更新成功", 0).show();
					}
				}
			}
		}else{
		//当号码没有发生改变时,newPhone或者phone都一样
			if(dao.updateMode(newPhone, newMode)){
				Toast.makeText(this, "更新成功", 0).show();	
				
			}else{
				Toast.makeText(this, "更新失败", 0).show();
				return;
			}
		}
		//最后,创建一个意图,将数据存到Result里面,传到上一个界面
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
