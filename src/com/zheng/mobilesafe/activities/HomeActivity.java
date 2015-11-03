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
	// ����Logo�ؼ�
	private ImageView iv_home_logo;
	// ����gridview���ܽ���Ŀؼ�
	private GridView gv_home_item;
	// ���干�����sp
	private SharedPreferences sp;
	// �Ի���
	private AlertDialog dialog;
	// ���ù��ܵ�����
	String names[] = new String[] { "�ֻ�����", "ɧ������", "����ܼ�", "���̹���", "����ͳ��",
			"�ֻ�ɱ��", "ϵͳ����", "���ù���" };
	// ���ù��ܵ�ͼ��
	int icons[] = new int[] { R.drawable.sjfd, R.drawable.srlj,
			R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd,
			R.drawable.xtjs, R.drawable.cygj };
	// ���ù��ܵ�����
	String descs[] = new String[] { "Զ�̶�λ�ֻ�", "ȫ������ɧ��", "�����������", "������������",
			"����һĿ��Ȼ", "�����޴�����", "ϵͳ������", "���ù��ߴ�ȫ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// ��ʼ��logoͼƬ�ؼ�
		iv_home_logo = (ImageView) findViewById(R.id.iv_home_logo);
		// ��ʼ��gridview
		gv_home_item = (GridView) findViewById(R.id.gv_home_item);
		// ��ʼ���������sp
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// iv_home_logo.setRotationY(rotationY);
		// �������Զ���
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				new float[] { 0, 60, 120, 180, 240, 300 });
		// ����ʱ��
		oa.setDuration(1000);
		// ���ö�������,���޲���
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		// ���ö���ģʽ,���¿�ʼ
		oa.setRepeatMode(ObjectAnimator.RESTART);
		// ��������
		oa.start();
		// ����gv����
		gv_home_item.setAdapter(new HomeAdapter());
		// ����gv����Ŀ����¼�
		gv_home_item.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent=null;
				switch (position) {
				case 0:// "�ֻ�����"
						// �ж��Ƿ����ù�����
					if (isSetupPwd()) {// ���ù�����,�����Ի���,��ʾ��������
						Log.i(TAG, "��ʾ��������");
						showEnterPwdDialog();
					} else {// û���ù�����,�����Ի���,��ʾ��������
						Log.i(TAG, "��ʾ��������");
						showSetupPwdDialog();
					}
					break;

				case 1:// "ɧ������"
					 intent=new Intent (HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);

					break;
				case 2:// "����ܼ�"
						 intent=new Intent(getApplicationContext(),AppManagerActivity.class);
						startActivity(intent);
					break;
				case 3:// "���̹���"
					 intent=new Intent(getApplicationContext(),ProcessManagerActivity.class);
					startActivity(intent);
					break;
				case 4:// "����ͳ��"
					 intent=new Intent(getApplicationContext(),TrafficManagerActivity.class);
						startActivity(intent);
					break;
				case 5:// "�ֻ�ɱ��"

					break;
				case 6:// "ϵͳ����"

					break;
				case 7:// "���ù���"
					Intent cti=new Intent(getApplicationContext(),CommonToolsActivity.class);
					startActivity(cti);
					break;
				}
			}
		});

	}

	/**
	 * ��ʾ��������Ի���
	 */
	private void showEnterPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		final View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		builder.setView(view);
		dialog = builder.show();
		// ��ȡ��ť����¼�
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
		// ���ȡ����ť
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button bt_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		// ���ȷ����ť
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// �����û����������
				EditText et_dialog_enterPwd = (EditText) view.findViewById(R.id.et_dialog_enterPwd);
				String enterPwd = et_dialog_enterPwd.getText().toString().trim();
				if (TextUtils.isEmpty(enterPwd)) {
					Toast.makeText(HomeActivity.this, "����������", 0).show();
					return;
				}
				// ����������ļ��Ĳ�һ��,��������������md5����
				if (!Md5Utils.encode(enterPwd).equals(sp.getString("password", null))) {
					Toast.makeText(HomeActivity.this, "�����������", 0).show();
					return;
				}
				dialog.dismiss();
				// �ж��Ƿ�������ý����
				if(sp.getBoolean("configed", false)){
					//�����ֻ�����UI����LostFindActivity
					Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else{
					//������
					Intent intent=new Intent(HomeActivity.this,Setup1Activity.class);
					startActivity(intent);
				}
				
			}
		});

	
	}

	/**
	 * ��ʾ��������Ի���,��������
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
		// ��ȡ��ť����¼�
		Button bt_ok = (Button) view.findViewById(R.id.bt_dialog_ok);
		// ���ȷ����ť
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ҵ����������,ƥ�����������Ƿ�һ��
				String pwd = et_pwd.getText().toString().trim();
				String pwd_confirm = et_pwd_confirm.getText().toString().trim();
				// �����һ��Ϊ��
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_confirm)) {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", 0).show();
					return;
				}
				if (!pwd.equals(pwd_confirm)) {
					Toast.makeText(HomeActivity.this, "�������벻һ��", 0).show();
					return;
				}
				Editor editor = sp.edit();
				//���������MD5����
				editor.putString("password", Md5Utils.encode(pwd));
				editor.commit();
				dialog.dismiss();
				//�����������,�����Ի���,����������
				showEnterPwdDialog();
			}
		});
		Button bt_cancel = (Button) view.findViewById(R.id.bt_dialog_cancel);
		// ���ȡ����ť
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * �Ƿ����ù�����
	 * 
	 * @return
	 */
	protected boolean isSetupPwd() {
		// ��ȡ�����ļ�
		String password = sp.getString("password", null);
		if (TextUtils.isEmpty(password)) {// �������Ϊ��
			return false;
		} else {
			return true;
		}

	}

	/**
	 * gridview�Ĳ���������
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
			// �ô���Ͳ��item_home��ʾ��������
			View view = View.inflate(HomeActivity.this, R.layout.item_home,
					null);
			// ����view����Ŀؼ�,��ס��view.find,�����ָ��
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