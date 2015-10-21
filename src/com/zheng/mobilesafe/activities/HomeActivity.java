package com.zheng.mobilesafe.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheng.mobilesafe.R;

public class HomeActivity extends Activity {
	// ����Logo�ؼ�
	private ImageView iv_home_logo;
	// ����gridview���ܽ���Ŀؼ�
	private GridView gv_home_item;
	// ���ù��ܵ�����
	String names[] = new String[] { "�ֻ�����", "ɧ������", "����ܼ�", "���̹���", "����ͳ��",
			"�ֻ�ɱ��", "ϵͳ����", "���ù���" };
	// ���ù��ܵ�ͼ��
	int icons[] = new int[] { R.drawable.sjfd, R.drawable.srlj,
			R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
			R.drawable.sjsd, R.drawable.xtjs, R.drawable.cygj };
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

	}
/**
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
			//�ô���Ͳ��item_home��ʾ��������
			View view=View.inflate(HomeActivity.this, R.layout.item_home, null);
			//����view����Ŀؼ�,��ס��view.find,�����ָ��
			ImageView icon=(ImageView) view.findViewById(R.id.iv_homeitem_icon);
			icon.setImageResource(icons[position]);
			TextView title=(TextView) view.findViewById(R.id.tv_homeitem_title);
			title.setText(names[position]);
			TextView desc=(TextView) view.findViewById(R.id.tv_homeitem_desc);
			desc.setText(descs[position]);
			return view;
		}
	}

}