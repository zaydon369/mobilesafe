package com.zheng.mobilesafe.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationService extends Service {
	private static final String TAG = "LocationService";
	private SharedPreferences sp;
	//位置管理者
	LocationManager lm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG,"开启位置服务..");//打印所有位置服务
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//初始化监听器
		listener=new MyListener();
		//通过系统服务得到位置管理
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		//获取所提供的所有位置服务
		List<String> allProviders = lm.getAllProviders();
		Log.i(TAG,"支持的位置服务有:"+allProviders.toString());//打印所有位置服务
		//判断提供的服务是否为空,是否包含GPS
		if(allProviders.size()>0 && allProviders.contains("gps")){
			lm.requestLocationUpdates("gps",0, 0,  listener);
		}
		
		super.onCreate();
	}
	private class MyListener implements LocationListener{
		//当位置更改时
		@Override
		public void onLocationChanged(Location location) {
			//得到经纬度
			String latitude = "latitude:"+location.getLatitude();//维度
			String longitude="longitude"+location.getLongitude();//经度
			//将经纬度的信息发给请求号码的人
			SmsManager sm=SmsManager.getDefault();
			sm.sendTextMessage(sp.getString("gpsNumber", ""), null, latitude+"\n"+longitude, null, null);
			//移除监听
			lm.removeUpdates(listener);
			listener=null;
			//把自身服务关闭
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}}
}
