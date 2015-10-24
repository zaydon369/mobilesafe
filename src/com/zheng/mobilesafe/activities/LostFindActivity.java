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
		// 初始化共享数据
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 初始化图标控件
		iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
		tv_lostfind_safeNumber=(TextView) findViewById(R.id.tv_lostfind_safeNumber);
		tv_lostfind_safeNumber.setText(sp.getString("safeNumber", "number"));
		//查看配置文件的防盗保护状态,进行图标回显
		if(sp.getBoolean("protecting", false)){
			iv_lostfind_status.setImageResource(R.drawable.lock);
		}else{
			iv_lostfind_status.setImageResource(R.drawable.unlock);
		}
	}

	/**
	 * 重新进入向导
	 * 
	 * @param view
	 */
	public void reseting(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();//之前设置单栈,后来发现跳转不能更新数据
	}
	/**
	 * 更改防盗保护状态
	 */
	public void changeProtectStatus(View view) {
		if(sp.getBoolean("protecting", false)){//如果当前选中,点击则取消
			Editor edit = sp.edit();
			edit.putBoolean("protecting", false);
			edit.commit();
			iv_lostfind_status.setImageResource(R.drawable.unlock);
			Toast.makeText(LostFindActivity.this, "关闭防盗保护", 0).show();
			
		}else{//如果当前状态关闭,点击则开启
			Editor edit = sp.edit();
			edit.putBoolean("protecting", true);
			edit.commit();
			iv_lostfind_status.setImageResource(R.drawable.lock);
			Toast.makeText(LostFindActivity.this, "防盗保护已开启", 0).show();
		}
	}
}
