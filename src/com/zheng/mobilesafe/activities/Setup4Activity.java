package com.zheng.mobilesafe.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.zheng.mobilesafe.R;

/**
 * 设置向导,界面1
 */
public class Setup4Activity extends SetupBaseActivity {
	// 共享数据文件
	SharedPreferences sp;
	CheckBox cb_setup4_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化SP
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_setup4);
		cb_setup4_status = (CheckBox) findViewById(R.id.cb_setup4_status);
		// 回显状态
		cb_setup4_status.setChecked(sp.getBoolean("protecting", false));
		// 给单线框设置更改事件监听
		cb_setup4_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
							Editor edit = sp.edit();
							edit.putBoolean("protecting", isChecked);
							edit.commit();
							if(isChecked){
								Toast.makeText(Setup4Activity.this, "开启防盗保护", 0).show();
							}else{
								Toast.makeText(Setup4Activity.this, "取消防盗保护", 0).show();
							}
					}
				});

	}

	@Override
	public void showNext(View view) {
		// 重写父类的下一步方法
		super.showNext(view);
		Editor edit = sp.edit();
		// 标记已经走过设置向导流程
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
