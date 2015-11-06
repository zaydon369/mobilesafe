package com.zheng.mobilesafe.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zheng.mobilesafe.R;
import com.zheng.mobilesafe.activities.utils.AppCacheUtils;
import com.zheng.mobilesafe.activities.utils.AppCacheUtils.IcacheCallBack;
import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.engine.AppInfoProvider;

public class CleanCacheFragment extends Fragment {
	/** 发现缓存 */
	public static final int FIND_CACHE = 1;
	/** 没有缓存 */
	public static final int NOT_CACHE = 0;
	/**扫描完成*/
	public static final int FINISH=2;
	private TextView tv_fragment_cache;
	private ProgressBar pb_fragment_cache;
	private LinearLayout ll_fragment_cache;
	private PackageManager pm;
	List<AppInfo> allAppInfos;
	Message message;
	//定义上下文
	Context mcontext;
	long cacheSize=0;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			CacheInfo cacheInfo=new CacheInfo();
			//打开清理缓存的应用的意图
			final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			
			cacheInfo=(CacheInfo) msg.obj;
			if(cacheInfo!=null){
			tv_fragment_cache.setText("正在扫描:"+cacheInfo.appName);
			}
			switch (msg.what) {
			case FIND_CACHE:
				TextView tv=new TextView(mcontext);
				 cacheSize += cacheInfo.cacheSize;
				 final String packName=cacheInfo.packName;
				tv.setText(cacheInfo.appName+"缓存大小:"+Formatter.formatFileSize(getActivity(), cacheInfo.cacheSize));
				tv.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						intent.setData(Uri.parse("package:"+packName));
						startActivity(intent);
						
					}});
				ll_fragment_cache.addView(tv,0);
				break;
			case NOT_CACHE:

				break;
			case FINISH:
				tv_fragment_cache.setText("扫描完成,共发现:"+Formatter.formatFileSize(mcontext, cacheSize)+"缓存");
				break;
			
			}
		};
	};

	@Override
	public void onStart() {
		new Thread() {
		public void run() {
			full();
		};
	}.start();
		
	
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//获得上下文
		mcontext=getActivity();
		View view = View.inflate(mcontext, R.layout.fragment_cache_clean,
				null);
	
		pb_fragment_cache = (ProgressBar) view
				.findViewById(R.id.pb_fragment_cache);
		tv_fragment_cache = (TextView) view
				.findViewById(R.id.tv_fragment_cache);
		ll_fragment_cache = (LinearLayout) view
				.findViewById(R.id.ll_fragment_cache);
		allAppInfos = AppInfoProvider.getAllAppInfos(mcontext);

		return view;

	}

	// 得到系统所有的安装包,遍历所有安装包的缓存大小
	public void full() {

		int size = allAppInfos.size();
		pb_fragment_cache.setMax(size);
		CacheCallBack cacheCallBack = new CacheCallBack();
		for (int i = 0; i < size; i++) {
			
			AppCacheUtils.getCache(i, mcontext, allAppInfos.get(i)
					.getPackageName(), cacheCallBack);
			pb_fragment_cache.setProgress(i + 1);
			SystemClock.sleep(100);
		}
		//扫描完后发个消息通知
		message = Message.obtain();
		message.what = FINISH;
		handler.sendMessage(message);
			
	}

	class CacheCallBack implements IcacheCallBack {

		@Override
		public void getCacheSize(int index, String packName, long cacheSize) {
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.packName = packName;
			cacheInfo.appName = allAppInfos.get(index).getAppName();
			cacheInfo.cacheSize = cacheSize;
			message = Message.obtain();
			message.obj = cacheInfo;
			if (cacheSize > 0) {
				message.what = FIND_CACHE;
			} else {
				message.what = NOT_CACHE;
			}
			handler.sendMessage(message);

		}

	}

	/**
	 * cache的javabean
	 * 
	 * @author asus
	 * 
	 */
	class CacheInfo {
		String packName;
		String appName;
		long cacheSize;
	}

}
