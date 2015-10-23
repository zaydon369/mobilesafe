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
 * �����򵼵ĳ�����
 * 
 * @author asus
 * 
 */
public abstract class SetupBaseActivity extends Activity {
	protected static final String TAG = "SetupBaseActivity";
	/**
	 * �������,��������Լ�����sp����
	 */
	SharedPreferences sp;

	/**
	 * ����ʶ����
	 */
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// �õ�SP�ļ�
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ��ʼ������ʶ����
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// ���û���ָ����Ļ�ϻ�����ʱ����õķ���
					// e1 ��ָ��һ�δ�������Ļ���¼�
					// e2 ��ָ�뿪��Ļһ˲���Ӧ���¼�
					// velocityX ˮƽ������ٶ�
					// velocityY ��ֱ������ٶ� ��λ����/s
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// �жϻ������ٶ�,����ٶ�С��50�Ͳ��л�
						if (Math.abs(velocityY) < 50) {
							Log.i(TAG, "�������ٶ�,����ٶ�С��100�Ͳ��л�");
							return true;
						}
						// �����ֱ������ȹ�������Ч��
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 200) {
							Log.i(TAG, "��ֱ������ȹ�������Ч��");
							return true;
						}
						// �����ƶ�,��ʾ��һҳ
						if ((e1.getRawX() - e2.getRawX()) > 100) {
							next();
							overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
							return true;
						}
						// �����ƶ�,��ʾ��һҳ
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
	 * ���û���ָ����Ļ�������õķ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//����ָʶ����ʶ�𴫽������¼�
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	/**
	 * ��ת����һҳ
	 */
	public abstract void pre();

	/**
	 * ��ת����һҳ
	 */
	public abstract void next();

	/**
	 * ���½���,���ҹرյ�ǰ����
	 * 
	 * @param cls
	 */
	public void openNewActivityAndFinish(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		finish();
	};

	/**
	 * ��һҳ,��ť�ķ���
	 * 
	 * @param view
	 */
	public void showPre(View view) {
		pre();
		overridePendingTransition(R.anim.anim_pre_in, R.anim.anim_pre_out);
	}

	/**
	 * ��һҳ,��ť�ķ���
	 * 
	 * @param view
	 */
	public void showNext(View view) {
		next();
		
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

}
