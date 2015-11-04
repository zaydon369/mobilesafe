package com.zheng.mobilesafe.activities;

import android.app.Activity;
import android.os.Bundle;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		//�ҵ���Ҫ�õ��Ŀؼ�
		ImageView iv_scan = (ImageView) findViewById(R.id.iv_anti_scan);
		TextView tv_anti_scan_status=(TextView) findViewById(R.id.tv_anti_scan_status);
		ProgressBar pb_anti_scan_status= (ProgressBar) findViewById(R.id.pb_anti_scan_status);
		LinearLayout ll_anti_container=(LinearLayout) findViewById(R.id.ll_anti_container);
		
		//��ɨ��ͼƬ������ת
		RotateAnimation ra=new RotateAnimation(0, 3600+1, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(20000);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_scan.startAnimation(ra);
		
		//��ʼ��ɱ����
		new Thread (){
			public void run() {
				ScanVirus();
				try {
					sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
		
	}
/**
 * ��ʼ��ɱ����
 */
	private void ScanVirus() {
		ScanVirusUtil.scanVirus(getApplicationContext(), new CallBackup (){

			@Override
			public void fileCounts(int count) {
				
				System.out.println("�ļ�����:"+count);
				
			}

			@Override
			public void fileProgress(int pro) {
				System.out.println("��ǰ����:"+pro);
				
			}

			@Override
			public void virusFile(String virusFile) {
				System.out.println("�����ļ�:"+virusFile);
				
			}

			@Override
			public void safetyFile(String safetyFile) {
				System.out.println("��ȫ�ļ�"+safetyFile);
				
			}});
		
	}

}
