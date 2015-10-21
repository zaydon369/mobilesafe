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
	// 定义Logo控件
	private ImageView iv_home_logo;
	// 定义gridview功能界面的控件
	private GridView gv_home_item;
	// 设置功能的名称
	String names[] = new String[] { "手机防盗", "骚扰拦截", "软件管家", "进程管理", "流量统计",
			"手机杀毒", "系统加速", "常用工具" };
	// 设置功能的图标
	int icons[] = new int[] { R.drawable.sjfd, R.drawable.srlj,
			R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj,
			R.drawable.sjsd, R.drawable.xtjs, R.drawable.cygj };
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
			//用打气筒将item_home显示到主界面
			View view=View.inflate(HomeActivity.this, R.layout.item_home, null);
			//查找view里面的控件,记住用view.find,否则空指针
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