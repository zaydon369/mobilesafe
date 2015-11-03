package com.zheng.mobilesafe.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.Md5Utils;

public class HomeActivity extends Activity {
	private static final String TAG = "HomeActivity";
	// 定义Logo控件
	private ImageView iv_home_logo;
	// 定义gridview功能界面的控件
	private GridView gv_home_item;
	// 定义共享参数sp
	private SharedPreferences sp;
	// 对话框
	private AlertDialog dialog;
	// 设置功能的名称
	String names[] = new String[] { "手机防盗", "骚扰拦截", "软件管家", "进程管理", "流量统计",
			"手机杀毒", "系统加速", "常用工具" };
	// 设置功能的图标
	int icons[] = new int[] { R.drawable.sjfd, R.drawable.srlj,
			R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd,
			R.drawable.xtjs, R.drawable.cygj };
	// 设置功能的描述
	String descs[] = new String[] { "远程定位手机", "全面拦截骚扰", "管理您的软件", "管理正在运行",
			"流量一目了然", "病毒无处藏身", "系统快如火箭", "常用工具大全" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// 初始化logo图片控件
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		// 初始化gridview
		gv_home_item = (GridView) findViewById(R.id.gv_home_item);
		// 初始化共享参数sp
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// iv_home_logo.setRotationY(rotationY);
		// 设置属性动画
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				new float[] { 0, 60, 120, 180, 240, 300 });
		// 设置时长
		oa.setDuration(1000);
		// 设置动画次数,无限播放
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		// 设置动画模式,重新开始
		oa.setRepeatMode(ObjectAnimator.RESTART);
		// 开启动画
		oa.start();
		// 设置gv布局
		gv_home_item.setAdapter(new HomeAdapter());
		// 设置gv的条目点击事件
		gv_home_item.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent=null;
				switch (position) {
				case 0:// "手机防盗"
						// 判断是否设置过密码
					if (isSetupPwd()) {// 设置过密码,弹出对话框,提示输入密码
						Log.i(TAG, "提示输入密码");
						showEnterPwdDialog();
					} else {// 没设置过密码,弹出对话框,提示设置密码
						Log.i(TAG, "提示设置密码");
						showSetupPwdDialog();
					}
					break;

				case 1:// "骚扰拦截"
					 intent=new Intent (HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);

					break;
				case 2:// "软件管家"
						 intent=new Intent(getApplicationContext(),AppManagerActivity.class);
						startActivity(intent);
					break;
				case 3:// "进程管理"
					 intent=new Intent(getApplicationContext(),ProcessManagerActivity.class);
					startActivity(intent);
					break;
				case 4:// "流量统计"
					 intent=new Intent(getApplicationContext(),TrafficManagerActivity.class);
						startActivity(intent);
					break;
				case 5:// "手机杀毒"

					break;
				case 6:// "系统加速"

					break;
				case 7:// "常用工具"
					Intent cti=new Intent(getApplicationContext(),CommonToolsActivity.class);
					startActivity(cti);
					break;
				}
			}
		});

	}

	/**
	 * 显示输入密码对话框
	 */
	private void showEnterPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		final View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		builder.setView(view);
		dialog = builder.show();
		// 获取按钮点击事件
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
		// 点击取消按钮
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button bt_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		// 点击确定按钮
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 查找用户输入的密码
				EditText et_dialog_enterPwd = (EditText) view.findViewById(R.id.et_dialog_enterPwd);
				String enterPwd = et_dialog_enterPwd.getText().toString().trim();
				if (TextUtils.isEmpty(enterPwd)) {
					Toast.makeText(HomeActivity.this, "请输入密码", 0).show();
					return;
				}
				// 如果和配置文件的不一致,将输入的密码进行md5加密
				if (!Md5Utils.encode(enterPwd).equals(sp.getString("password", null))) {
					Toast.makeText(HomeActivity.this, "密码输入错误", 0).show();
					return;
				}
				dialog.dismiss();
				// 判断是否进入设置界面过
				if(sp.getBoolean("configed", false)){
					//进入手机防盗UI界面LostFindActivity
					Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else{
					//新手向导
					Intent intent=new Intent(HomeActivity.this,Setup1Activity.class);
					startActivity(intent);
				}
				
			}
		});

	
	}

	/**
	 * 显示设置密码对话框,设置密码
	 */
	private void showSetupPwdDialog() {

		Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		builder.setView(view);
		dialog = builder.show();
		final EditText et_pwd = (EditText) view
				.findViewById(R.id.et_dialog_pwd);
		final EditText et_pwd_confirm = (EditText) view
				.findViewById(R.id.et_dialog_pwd_confirm);
		// 获取按钮点击事件
		Button bt_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		// 点击确定按钮
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 找到密码输入框,匹配两次密码是否一致
				String pwd = et_pwd.getText().toString().trim();
				String pwd_confirm = et_pwd_confirm.getText().toString().trim();
				// 如果有一个为空
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_confirm)) {
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
					return;
				}
				if (!pwd.equals(pwd_confirm)) {
					Toast.makeText(HomeActivity.this, "两次密码不一致", 0).show();
					return;
				}
				Editor editor = sp.edit();
				//将密码进行MD5加密
				editor.putString("password", Md5Utils.encode(pwd));
				editor.commit();
				dialog.dismiss();
				//设置完密码后,弹出对话框,进入设置向导
				showEnterPwdDialog();
			}
		});
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
		// 点击取消按钮
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 是否设置过密码
	 * 
	 * @return
	 */
	protected boolean isSetupPwd() {
		// 读取设置文件
		String password = sp.getString("password", null);
		if (TextUtils.isEmpty(password)) {// 如果密码为空
			return false;
		} else {
			return true;
		}

	}

	/**
	 * gridview的布局适配器
	 * 
	 * @author asus
	 * 
	 */
	private class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 用打气筒将item_home显示到主界面
			View view = View.inflate(HomeActivity.this, R.layout.item_home,
					null);
			// 查找view里面的控件,记住用view.find,否则空指针
			ImageView icon = (ImageView) view
					.findViewById(R.id.iv_homeitem_icon);
			icon.setImageResource(icons[position]);
			TextView title = (TextView) view
					.findViewById(R.id.tv_homeitem_title);
			title.setText(names[position]);
			TextView desc = (TextView) view.findViewById(R.id.tv_homeitem_desc);
			desc.setText(descs[position]);
			return view;
		}
	}

	public void enterSettingActivity(View view) {
		Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
		startActivity(intent);
	}

}