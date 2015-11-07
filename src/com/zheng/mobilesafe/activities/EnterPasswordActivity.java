package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;

/**
 * 程序锁,输入密码提示框
 * 
 * @author asus
 * 
 */
public class EnterPasswordActivity extends Activity {
	private ImageView iv_enter_password_icon;
	private TextView tv_enter_password_appname;
	private EditText et_enter_password_pwd;
	private Button bt_enter_password_confirm;
	private String packageName;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_password);
		iv_enter_password_icon = (ImageView) findViewById(R.id.iv_enter_password_icon);
		tv_enter_password_appname = (TextView) findViewById(R.id.tv_enter_password_appname);
		et_enter_password_pwd = (EditText) findViewById(R.id.et_enter_password_pwd);
		bt_enter_password_confirm = (Button) findViewById(R.id.bt_enter_password_confirm);
		pm = getPackageManager();
		
	}
	@Override
	protected void onStart() {
		Intent intent = getIntent();
		packageName = intent.getStringExtra("packageName");
		//System.out.println("意图得到的包名:" + packageName);
		
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(
					packageName, 0);
			iv_enter_password_icon.setBackgroundDrawable(applicationInfo
					.loadIcon(pm));
			tv_enter_password_appname.setText(applicationInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		bt_enter_password_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_enter_password_pwd.getText().toString().trim();
				if (pwd.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请输入密码", 0).show();
					return;
				}
				if (pwd.equals("123")) {
					// 密码正确,发送广播,通知看门狗,不要拦截
					Intent intent = new Intent();
					intent.setAction("com.zheng.mobilesafe.tempstopprotect");
					intent.putExtra("packageName", packageName);
					sendBroadcast(intent);

					finish();
				} else {
					Toast.makeText(getApplicationContext(), "密码错误", 0).show();
					return;
				}

			}
		});
		super.onStart();
	}
	/**
	 * 重写返回键,修改返回为回到桌面,避免看到加锁程序的内容
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		startActivity(intent);
		finish();
	}
	/**
	 * 在不可见时,将本窗口消掉,避免多个程序同一个图标问题
	 */
	@Override
	protected void onStop() {
		super.onStop();
		finish();
		
	}

}
