package com.zheng.mobilesafe.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.PackageInfoUtils;
import com.zheng.mobilesafe.activities.utils.StreamTools;

public class SplashActivity extends Activity {
	// ����TAGΪ��ǰactivity������,����鿴log
	public static final String TAG = "SplashActivity";
	// ��Ϣ����:��ʾ����
	public static final int SHOW_UPDATE_DIALOG = 1;
	// ��Ϣ����:���ִ���
	protected static final int ERROR = 0;
	// activity���ֿؼ�
	private TextView tv_splash_version;
//	// ����Message,�������̺߳����̼߳�����ݴ���
//	private Message msg = Message.obtain();
	// ʹ��handler�������̴߳��ݵ���Ϣ,���ж�Ӧ��UI����
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// �ж���Ϣ����
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "�յ��������¶Ի������Ϣ!");
				//TODO
				showUpdateDialog((String)msg.obj);
				break;
			case ERROR:
				Log.i(TAG, "���ִ���");
				Toast.makeText(SplashActivity.this, "�������:" + msg.obj, 0)
						.show();
				break;
			}

		};
	};
	/**
	 * ��ʾ������ʾ��
	 * @param dest ����
	 */
	protected void  showUpdateDialog(String dest) {
		AlertDialog.Builder builder=new Builder(this);
		//���ñ���
		builder.setTitle("��������");
		//��������
		builder.setMessage(dest);
		//ѡ����������,����������������
		builder.setPositiveButton("��������", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}});
		//ѡ���´���˵,����������home
		builder.setNegativeButton("�´���˵", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}});
		//������Ϣ��
		builder.show();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// ��ʼ���ؼ�
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		// ��ȡ��ǰ�汾��
		String version = PackageInfoUtils.getPackageVersion(this);
		// �Ѱ汾����ʾ��splash��
		tv_splash_version.setText("�汾:" + version);
		// �������̻߳�ȡ�����������°汾
		new Thread(new CheckVersionTack()).start();
	}

	/**
	 * ��ȡ�����������°汾��
	 */
	private class CheckVersionTack implements Runnable {
		@Override
		public void run() {
			// ͨ����Դ�ļ���ȡurl·��
			String path = getResources().getString(R.string.url);
			try {
				URL url = new URL(path);
				// ������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// ��������ʽΪGET
				conn.setRequestMethod("GET");
				// ���÷��ʳ�ʱʱ��2��
				conn.setReadTimeout(2000);
				// �õ�������
				int code = conn.getResponseCode();
				// �������200˵�����ӳɹ�
				if (code == 200) {
					// �õ���Դ�ļ���������
					InputStream is = conn.getInputStream();
					// ����������,��ȡ������,�õ�json�ַ���
					String result = StreamTools.readStream(is);
					// ����json�ַ�
					JSONObject json = new JSONObject(result);
					// ��ȡ�������İ汾��
					String version = json.getString("version");
					String description=json.getString("description");
					Log.i(TAG, "���������°汾:" + version);
					// ��ȡ���صİ汾��
					String localVersion = PackageInfoUtils
							.getPackageVersion(SplashActivity.this);
					Log.i(TAG, "���ذ汾��:" + localVersion);
					if (localVersion.equals(version)) {
						// ����汾����ͬ,���ø���ֱ�ӽ�����ҳ��home
						Log.i(TAG, "�汾����ͬ");
					} else {
						Log.i(TAG, "�����°汾,��ʾ����!!");
						Message msg = Message.obtain();
						// ����Message����
						msg.what = SHOW_UPDATE_DIALOG;
						//������Ϣ����
						msg.obj=description;
						// ����Message
						handler.sendMessage(msg);
					}
				} else {// ����������ʧ��,��ʾ�������
					Message msg = Message.obtain();
					msg.what=ERROR;
					msg.obj=000;
					handler.sendMessage(msg);
					Log.i(TAG, "���ӷ����벻��200");
				}
				//Log.i(TAG, "���ӳ�ʱ...");
			} catch (MalformedURLException e) {
				Message msg = Message.obtain();
				e.printStackTrace();
				msg.what=ERROR;
				msg.obj=404;
				handler.sendMessage(msg);
				Log.i(TAG, "URL���Ӵ���!!");
			} catch (IOException e) {
				//IO��ȡʧ��
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what=ERROR;
				msg.obj=606;
				handler.sendMessage(msg);
				Log.i(TAG, "IO����������!!");
			} catch (JSONException e) {
				// ����jsonʧ��
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what=ERROR;
				msg.obj=808;
				handler.sendMessage(msg);
				Log.i(TAG, "json��������!!");
			}
		}

	}

}
