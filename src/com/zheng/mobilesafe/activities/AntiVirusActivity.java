package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.ScanVirusUtil;
import com.zheng.mobilesafe.activities.utils.ScanVirusUtil.CallBackup;

public class AntiVirusActivity extends Activity {
	protected static final int VIRUS_FILE = 0;
	protected static final int SAFE_FILE = 1;
	protected static final int SCAN_FINISH = 2;
	private TextView tv_anti_scan_status;
	private ProgressBar pb_anti_scan_status;
	private LinearLayout ll_anti_container;
	private ImageView iv_scan;

	// ������Ϣ����
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case VIRUS_FILE://���ֲ����ļ�
				TextView tv_virus = new TextView(getApplicationContext());
				tv_virus.setTextColor(Color.RED);
				tv_virus.setText("���ֲ���:" + msg.obj.toString());
				ll_anti_container.addView(tv_virus, 0);
				break;

			case SAFE_FILE://��ȫ�ļ�
				TextView tv_safe = new TextView(getApplicationContext());
				tv_safe.setTextColor(Color.BLACK);
				tv_safe.setText("ɨ�谲ȫ:" + msg.obj.toString());
				ll_anti_container.addView(tv_safe, 0);
				break;
			case SCAN_FINISH://ɨ�����
				//����ת����ͼƬ�Ƴ�
				iv_scan.clearAnimation();
				iv_scan.setVisibility(View.GONE);
				tv_anti_scan_status.setText("ɨ�����");
				//������������
				pb_anti_scan_status.setVisibility(View.GONE);
				System.out.println("ִ�������淽����,�ر���ʾ��..");
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		iv_scan = (ImageView) findViewById(R.id.iv_anti_scan);
		tv_anti_scan_status = (TextView) findViewById(R.id.tv_anti_scan_status);
		pb_anti_scan_status = (ProgressBar) findViewById(R.id.pb_anti_scan_status);
		ll_anti_container = (LinearLayout) findViewById(R.id.ll_anti_container);

		// ��ɨ��ͼƬ������ת
		RotateAnimation ra = new RotateAnimation(0, 3600 + 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(20000);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_scan.startAnimation(ra);

		// ��ʼ��ɱ����
		ScanVirus();
	}

	/**
	 * ��ʼ��ɱ����
	 */
	private void ScanVirus() {
		new Thread() {
			@Override
			public void run() {
				ScanVirusUtil.scanVirus(getApplicationContext(),
						new CallBackup() {
							@Override
							public void fileCounts(int count) {
								int close = count;
								System.out.println("�ļ�����:" + count);
								//���ý����������ֵ
								pb_anti_scan_status.setMax(count);
							}

							@Override
							public void fileProgress(int pro) {
								//���ý������Ľ���
								pb_anti_scan_status.setProgress(pro);
								System.out.println("��ǰ����:" + pro);
								try {
									//ɨ��һ��,��Ϣ0.1��
									sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void virusFile(String virusFile) {
								System.out.println("�����ļ�:" + virusFile);
								Message msg = Message.obtain();
								msg.what = VIRUS_FILE;
								msg.obj = virusFile;
								handler.sendMessage(msg);

							}

							@Override
							public void safetyFile(String safetyFile) {
								System.out.println("��ȫ�ļ�:" + safetyFile);
								Message msg = Message.obtain();
								msg.what = SAFE_FILE;
								msg.obj = safetyFile;
								handler.sendMessage(msg);
							}
						});
				// ִ�������淽����,�ر���ʾ��
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);
				super.run();
			}
		}.start();

	}
}
