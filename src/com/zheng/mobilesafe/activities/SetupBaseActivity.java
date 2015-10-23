package com.zheng.mobilesafe.activities;

import com.zheng.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 设置向导的抽象类
 * 
 * @author asus
 * 
 */
public abstract class SetupBaseActivity extends Activity {
	protected static final String TAG = "SetupBaseActivity";
	/**
	 * 共享参数,子类可以自己调用sp数据
	 */
	SharedPreferences sp;

	/**
	 * 手势识别器
	 */
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 得到SP文件
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 初始化手势识别器
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// 当用户手指在屏幕上滑动的时候调用的方法
					// e1 手指第一次触摸到屏幕的事件
					// e2 手指离开屏幕一瞬间对应的事件
					// velocityX 水平方向的速度
					// velocityY 垂直方向的速度 单位像素/s
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// 判断滑动的速度,如果速度小于50就不切换
						if (Math.abs(velocityY) < 50) {
							Log.i(TAG, "滑动的速度,如果速度小于100就不切换");
							return true;
						}
						// 如果垂直方向幅度过大则无效果
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 200) {
							Log.i(TAG, "垂直方向幅度过大则无效果");
							return true;
						}
						// 向左移动,显示下一页
						if ((e1.getRawX() - e2.getRawX()) > 100) {
							next();
							overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
							return true;
						}
						// 向右移动,显示上一页
						if ((e2.getRawX() - e1.getRawX()) > 100) {
							pre();
							overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
							return true;
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
		
		
	}
	
	/**
	 * 当用户手指在屏幕触摸调用的方法
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//让手指识别器识别传进来的事件
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	/**
	 * 跳转到上一页
	 */
	public abstract void pre();

	/**
	 * 跳转到下一页
	 */
	public abstract void next();

	/**
	 * 打开新界面,并且关闭当前界面
	 * 
	 * @param cls
	 */
	public void openNewActivityAndFinish(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	};

	/**
	 * 上一页,按钮的方法
	 * 
	 * @param view
	 */
	public void showPre(View view) {
		pre();
		overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
	}

	/**
	 * 下一页,按钮的方法
	 * 
	 * @param view
	 */
	public void showNext(View view) {
		next();
		
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

}
