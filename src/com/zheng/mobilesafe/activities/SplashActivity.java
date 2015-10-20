package com.zheng.mobilesafe.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.R.layout;
import com.zheng.mobilesafe.R.menu;
import com.zheng.mobilesafe.activities.utils.PackageInfoUtils;
import com.zheng.mobilesafe.activities.utils.StreamTools;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SplashActivity extends Activity {
	//����TAGΪ��ǰactivity������,����鿴log
	public static final String TAG = "SplashActivity";
	TextView tv_splash_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//��ʼ���ؼ�
		tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
		//��ȡ��ǰ�汾��
		String version = PackageInfoUtils.getPackageVersion(this);
		//�Ѱ汾����ʾ��splash��
		tv_splash_version.setText("�汾:"+version);
		//�������̻߳�ȡ�����������°汾
		new Thread(new CheckVersionTack()).start();
	}
	/**
	 * ��ȡ�����������°汾�� 
	 */
	private class CheckVersionTack implements Runnable{
		@Override
		public void run() {
			// ͨ����Դ�ļ���ȡurl·��
			String path = getResources().getString(R.string.url);
			try {
				URL url=new URL(path);
				//������
				HttpURLConnection conn= (HttpURLConnection) url.openConnection();
				//��������ʽΪGET
				conn.setRequestMethod("GET");
				//���÷��ʳ�ʱʱ��2��
				conn.setReadTimeout(2000);
				//�õ�������
				int code = conn.getResponseCode();
				//�������200˵�����ӳɹ�
				if(code == 200){
					//�õ���Դ�ļ���������
					InputStream is = conn.getInputStream();
					//����������,��ȡ������,�õ�json�ַ���
					String result = StreamTools.readStream(is);
					//����json�ַ�
					JSONObject json=new JSONObject(result);
					//��ȡ�������İ汾��
					String version=json.getString("version");
					Log.i(TAG, "���������°汾:"+version);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// ����jsonʧ��
				e.printStackTrace();
			}
		}
		
	}

}
