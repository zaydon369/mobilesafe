package com.zheng.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.zheng.mobilesafe.activities.utils.AppCacheUtils;
import com.zheng.mobilesafe.activities.utils.AppCacheUtils.IcacheCallBack;
import com.zheng.mobilesafe.activities.utils.PackageInfoUtils;
import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.engine.AppInfoProvider;

public class TestPackageInfoUtils extends AndroidTestCase {

	public void testAppCacheUtils() {
	AppCacheUtils appCacheUtils=new AppCacheUtils();
	appCacheUtils.getCache(0,getContext(), "com.android.browser", new CacheCallBack());
		
		
		
	}
class CacheCallBack implements	IcacheCallBack{

	@Override
	public void getCacheSize(int index,String packname,long cacheSize) {
		System.out.println(packname+"»º´æÎª:"+cacheSize);
		
	}
	
} 
}
