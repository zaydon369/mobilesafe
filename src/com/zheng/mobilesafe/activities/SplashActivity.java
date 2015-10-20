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
	//设置TAG为当前activity的类名,方便查看log
	public static final String TAG = "SplashActivity";
	TextView tv_splash_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//初始化控件
		tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
		//获取当前版本号
		String version = PackageInfoUtils.getPackageVersion(this);
		//把版本号显示到splash中
		tv_splash_version.setText("版本:"+version);
		//开启子线程获取服务器的最新版本
		new Thread(new CheckVersionTack()).start();
	}
	/**
	 * 获取服务器的最新版本号 
	 */
	private class CheckVersionTack implements Runnable{
		@Override
		public void run() {
			// 通过资源文件获取url路径
			String path = getResources().getString(R.string.url);
			try {
				URL url=new URL(path);
				//打开连接
				HttpURLConnection conn= (HttpURLConnection) url.openConnection();
				//设置请求方式为GET
				conn.setRequestMethod("GET");
				//设置访问超时时间2秒
				conn.setReadTimeout(2000);
				//得到返回码
				int code = conn.getResponseCode();
				//如果返回200说明连接成功
				if(code == 200){
					//得到资源文件的输入流
					InputStream is = conn.getInputStream();
					//借助工具类,读取输入流,得到json字符串
					String result = StreamTools.readStream(is);
					//解析json字符
					JSONObject json=new JSONObject(result);
					//获取服务器的版本号
					String version=json.getString("version");
					Log.i(TAG, "服务器最新版本:"+version);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// 解析json失败
				e.printStackTrace();
			}
		}
		
	}

}
