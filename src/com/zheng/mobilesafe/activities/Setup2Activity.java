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
 * 设置向导,界面1
 */
public class Setup2Activity extends SetupBaseActivity {
	private LinearLayout ll_setup2_bind;
	private ImageView iv_setup2_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		// 获得绑定SIM卡的布局
		ll_setup2_bind = (LinearLayout) findViewById(R.id.ll_setup2_bind);
		iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
		//判断之前是否绑定SIM卡,进行对应的图标设置
		if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}else{
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
		// 点击绑定SIM
		ll_setup2_bind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断是否绑定SIM,如果绑定就解绑,没绑定就绑定

				if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
					// SIM卡为空,进行绑定SIM卡
					// 通过电话管理服务获取SIM卡的序列
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String sim = tm.getSimSerialNumber();
					if(!TextUtils.isEmpty(sim)){
					//将SIM卡的数据写进配置文件
					Editor editor=sp.edit();
					editor.putString("SIM", sim);
					editor.commit();
					//更改状态锁图标
					iv_setup2_status.setImageResource(R.drawable.lock);
					Toast.makeText(Setup2Activity.this, "SIM卡已成功绑定", 100).show();
					}else{
						Toast.makeText(Setup2Activity.this, "读取不到SIM卡的信息", 100).show();	
					}
				}else{
					//解绑,把SIM卡设为空
					Editor editor=sp.edit();
					editor.putString("SIM", "");
					editor.commit();
					//更换状态锁的图标
					iv_setup2_status.setImageResource(R.drawable.unlock);
					Toast.makeText(Setup2Activity.this, "SIM卡已解绑", 100).show();
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
		//判断是否绑定SIM卡,如果没有绑定则不允许下一步
				if (TextUtils.isEmpty(sp.getString("SIM", ""))) {
					Toast.makeText(Setup2Activity.this, "请绑定SIM卡后继续", 100).show();
				return ;
				}
		openNewActivityAndFinish(Setup3Activity.class);
		
	}
}
