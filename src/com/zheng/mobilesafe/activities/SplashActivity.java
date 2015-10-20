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
	// 设置TAG为当前activity的类名,方便查看log
	public static final String TAG = "SplashActivity";
	// 消息代码:提示升级
	public static final int SHOW_UPDATE_DIALOG = 1;
	// 消息代码:出现错误
	protected static final int ERROR = 0;
	// activity布局控件
	private TextView tv_splash_version;
//	// 定义Message,用于子线程和主线程间的数据传递
//	private Message msg = Message.obtain();
	// 使用handler接收子线程传递的消息,进行对应的UI更改
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 判断消息类型
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "收到弹出更新对话框的消息!");
				//TODO
				showUpdateDialog((String)msg.obj);
				break;
			case ERROR:
				Log.i(TAG, "出现错误");
				Toast.makeText(SplashActivity.this, "错误代码:" + msg.obj, 0)
						.show();
				break;
			}

		};
	};
	/**
	 * 显示升级提示框
	 * @param dest 描述
	 */
	protected void  showUpdateDialog(String dest) {
		AlertDialog.Builder builder=new Builder(this);
		//设置标题
		builder.setTitle("升级提醒");
		//设置内容
		builder.setMessage(dest);
		//选择立即升级,进行下载升级操作
		builder.setPositiveButton("立即升级", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}});
		//选择下次再说,进入主界面home
		builder.setNegativeButton("下次再说", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}});
		//弹出消息框
		builder.show();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 初始化控件
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		// 获取当前版本号
		String version = PackageInfoUtils.getPackageVersion(this);
		// 把版本号显示到splash中
		tv_splash_version.setText("版本:" + version);
		// 开启子线程获取服务器的最新版本
		new Thread(new CheckVersionTack()).start();
	}

	/**
	 * 获取服务器的最新版本号
	 */
	private class CheckVersionTack implements Runnable {
		@Override
		public void run() {
			// 通过资源文件获取url路径
			String path = getResources().getString(R.string.url);
			try {
				URL url = new URL(path);
				// 打开连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 设置请求方式为GET
				conn.setRequestMethod("GET");
				// 设置访问超时时间2秒
				conn.setReadTimeout(2000);
				// 得到返回码
				int code = conn.getResponseCode();
				// 如果返回200说明连接成功
				if (code == 200) {
					// 得到资源文件的输入流
					InputStream is = conn.getInputStream();
					// 借助工具类,读取输入流,得到json字符串
					String result = StreamTools.readStream(is);
					// 解析json字符
					JSONObject json = new JSONObject(result);
					// 获取服务器的版本号
					String version = json.getString("version");
					String description=json.getString("description");
					Log.i(TAG, "服务器最新版本:" + version);
					// 获取本地的版本号
					String localVersion = PackageInfoUtils
							.getPackageVersion(SplashActivity.this);
					Log.i(TAG, "本地版本号:" + localVersion);
					if (localVersion.equals(version)) {
						// 如果版本号相同,不用更新直接进入主页面home
						Log.i(TAG, "版本号相同");
					} else {
						Log.i(TAG, "发现新版本,提示更新!!");
						Message msg = Message.obtain();
						// 设置Message代码
						msg.what = SHOW_UPDATE_DIALOG;
						//设置消息内容
						msg.obj=description;
						// 发送Message
						handler.sendMessage(msg);
					}
				} else {// 服务器连接失败,提示错误代码
					Message msg = Message.obtain();
					msg.what=ERROR;
					msg.obj=000;
					handler.sendMessage(msg);
					Log.i(TAG, "连接返回码不是200");
				}
				//Log.i(TAG, "连接超时...");
			} catch (MalformedURLException e) {
				Message msg = Message.obtain();
				e.printStackTrace();
				msg.what=ERROR;
				msg.obj=404;
				handler.sendMessage(msg);
				Log.i(TAG, "URL连接错误!!");
			} catch (IOException e) {
				//IO读取失败
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what=ERROR;
				msg.obj=606;
				handler.sendMessage(msg);
				Log.i(TAG, "IO流解析错误!!");
			} catch (JSONException e) {
				// 解析json失败
				e.printStackTrace();
				Message msg = Message.obtain();
				msg.what=ERROR;
				msg.obj=808;
				handler.sendMessage(msg);
				Log.i(TAG, "json解析错误!!");
			}
		}

	}

}
