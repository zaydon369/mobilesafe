package com.zheng.mobilesafe.activities;

import java.io.File;
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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
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
	// �������ص�ַ�������汾��APK��ַ
	private String downloadpath;
	// �������ؽ�����
	ProgressDialog pd;
	//���ÿ�ʼ���ʷ�������ϵͳʱ��(���ڿ���ͣ��ʱ��)
	private long startTime;
	//����׼������UI��ϵͳʱ��(���ڿ���ͣ��ʱ��)
	private long endTime;
	// ����Message,�������̺߳����̼߳�����ݴ���
	private Message msg = Message.obtain();
	// ʹ��handler�������̴߳��ݵ���Ϣ,���ж�Ӧ��UI����
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// �ж���Ϣ����
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "�յ��������¶Ի������Ϣ!");
				// TODO
				showUpdateDialog((String) msg.obj);
				break;
			case ERROR:
				Log.i(TAG, "���ִ���");
				Toast.makeText(SplashActivity.this, "�������:" + msg.obj, 0)
						.show();
				// ��ʾ��ҳ��
				loadMainUI();
				break;
			}

		}

	};
	/**
	 * ��ʾ������ʾ��
	 * 
	 * @param dest
	 *            ����
	 */
	protected void showUpdateDialog(String dest) {
		AlertDialog.Builder builder = new Builder(this);
		//����ֻ�ܵ��ȷ������ȡ����ť
		builder.setCancelable(false);
		// ���ñ���
		builder.setTitle("��������");
		// ��������
		builder.setMessage(dest);
		// ѡ����������,����������������
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ���ļ����ص�sdk��,�ǵ���Ӷ�дSD����Ȩ��
				// �ж�SD���Ƿ����
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// �������,�������ذ�װ
					File sdcard = Environment.getExternalStorageDirectory();
					// ���·��,�ļ���Ϊϵͳ����ʱ��
					File file = new File(sdcard, SystemClock.uptimeMillis()
							+ ".apk");
					// ͨ����Դ���xUtils��������apk��װ��
					HttpUtils http = new HttpUtils();
					// �������ؽ�����,��Ϊ�ڹرս������п��ܿ�ָ��,�����ᵽ����
					pd = new ProgressDialog(SplashActivity.this);
					http.download(downloadpath, file.getAbsolutePath(),
							new RequestCallBack<File>() {
								/**
								 * ��������,���ڸ��ٽ���
								 */
								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									// ���ý���������
									pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
									// �������ֵ
									pd.setMax((int) total);
									// ���õ�ǰ����ֵ
									pd.setProgress((int) current);
									pd.show();
									super.onLoading(total, current, isUploading);
								}
								/**
								 * ����ʧ��
								 */
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// �ر���ʾ��
									pd.dismiss();
									// ��ʾ����ʧ��,����home
									Toast.makeText(SplashActivity.this,
											"�ļ�����ʧ��", 0).show();
									loadMainUI();
								}
								/**
								 * ���سɹ�
								 */
								@Override
								public void onSuccess(
										ResponseInfo<File> fileInfo) {
									// �ر���ʾ��
									pd.dismiss();
									Toast.makeText(SplashActivity.this, "���سɹ�",
											0).show();
									// ��ʾ��װ
									/*
									 * ϵͳ��װ����packageinstaller��Դ�� <action
									 * android:name="android.intent.action.VIEW"
									 * /> <category android:name=
									 * "android.intent.category.DEFAULT" />
									 * <data android:scheme="content" /> <data
									 * android:scheme="file" /> <data
									 * android:mimeType
									 * ="application/vnd.android.package-archive"
									 * />
									 */
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(
											Uri.fromFile(fileInfo.result),
											"application/vnd.android.package-archive");
									//��ʼ��ʾ�滻��װ
									startActivity(intent);
								}
							});
				} else {// ����home����
					loadMainUI();
				}

			}
		});
		// ѡ���´���˵,����������home
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ʾ��ҳ��
				loadMainUI();
			}
		});
		// ������Ϣ��
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
		//���忪ʼ��ʱʱ��
		startTime=SystemClock.uptimeMillis();
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
				//���ӳ�ʱʱ��..
				conn.setConnectTimeout(2000);
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
					String description = json.getString("description");
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
						// �������ذ汾��ַ
						downloadpath = json.getString("downloadpath");
						// ����Message����
						msg.what = SHOW_UPDATE_DIALOG;
						// ������Ϣ����
						msg.obj = description;
					}
				} else {// ����������ʧ��,��ʾ�������

					msg.what = ERROR;
					msg.obj = "000";
					Log.i(TAG, "���ӷ����벻��200");
				}
			//	 Log.i(TAG, "���ӳ�ʱ...");
			} catch (MalformedURLException e) {

				e.printStackTrace();
				msg.what = ERROR;
				msg.obj = "404";
				Log.i(TAG, "URL���Ӵ���!!");
			} catch (IOException e) {
				// IO��ȡʧ��
				e.printStackTrace();

				msg.what = ERROR;
				msg.obj = "606";
				Log.i(TAG, "IO����������!!");
			} catch (JSONException e) {
				// ����jsonʧ��
				e.printStackTrace();
				msg.what = ERROR;
				msg.obj = "808";
				
				Log.i(TAG, "json��������!!");
			}finally{
				//�������UIhome��ʱ��
				endTime=SystemClock.uptimeMillis();
				Log.i(TAG, "��ʼʱ��"+startTime+"����ʱ��:"+endTime);
				Log.i(TAG,"��ʱ:"+(endTime-startTime));
				if((endTime-startTime)<2000){
					
				SystemClock.sleep(2000-(endTime-startTime));
				
				}
				//ͳһ��finally������Ϣ,�����ظ�����
				handler.sendMessage(msg);
			}
		}

	}
	/**
	 * ������UI
	 */
	private void loadMainUI() {
		// ����home����
		// ������ת��home�������ͼ
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	};
}
