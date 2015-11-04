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

	// 利用消息管理
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case VIRUS_FILE://发现病毒文件
				TextView tv_virus = new TextView(getApplicationContext());
				tv_virus.setTextColor(Color.RED);
				tv_virus.setText("发现病毒:" + msg.obj.toString());
				ll_anti_container.addView(tv_virus, 0);
				break;

			case SAFE_FILE://安全文件
				TextView tv_safe = new TextView(getApplicationContext());
				tv_safe.setTextColor(Color.BLACK);
				tv_safe.setText("扫描安全:" + msg.obj.toString());
				ll_anti_container.addView(tv_safe, 0);
				break;
			case SCAN_FINISH://扫描完成
				//将旋转动画图片移除
				iv_scan.clearAnimation();
				iv_scan.setVisibility(View.GONE);
				tv_anti_scan_status.setText("扫描完成");
				//将进度条隐藏
				pb_anti_scan_status.setVisibility(View.GONE);
				System.out.println("执行完上面方法后,关闭提示框..");
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

		// 给扫描图片设置旋转
		RotateAnimation ra = new RotateAnimation(0, 3600 + 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(20000);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_scan.startAnimation(ra);

		// 开始查杀病毒
		ScanVirus();
	}

	/**
	 * 开始查杀病毒
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
								System.out.println("文件总数:" + count);
								//设置进度条的最大值
								pb_anti_scan_status.setMax(count);
							}

							@Override
							public void fileProgress(int pro) {
								//设置进度条的进度
								pb_anti_scan_status.setProgress(pro);
								System.out.println("当前进度:" + pro);
								try {
									//扫描一个,休息0.1秒
									sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void virusFile(String virusFile) {
								System.out.println("病毒文件:" + virusFile);
								Message msg = Message.obtain();
								msg.what = VIRUS_FILE;
								msg.obj = virusFile;
								handler.sendMessage(msg);

							}

							@Override
							public void safetyFile(String safetyFile) {
								System.out.println("安全文件:" + safetyFile);
								Message msg = Message.obtain();
								msg.what = SAFE_FILE;
								msg.obj = safetyFile;
								handler.sendMessage(msg);
							}
						});
				// 执行完上面方法后,关闭提示框
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);
				super.run();
			}
		}.start();

	}
}
