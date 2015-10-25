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
	//λ�ù�����
	LocationManager lm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG,"����λ�÷���..");//��ӡ����λ�÷���
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//��ʼ��������
		listener=new MyListener();
		//ͨ��ϵͳ����õ�λ�ù���
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		//��ȡ���ṩ������λ�÷���
		List<String> allProviders = lm.getAllProviders();
		Log.i(TAG,"֧�ֵ�λ�÷�����:"+allProviders.toString());//��ӡ����λ�÷���
		//�ж��ṩ�ķ����Ƿ�Ϊ��,�Ƿ����GPS
		if(allProviders.size()>0 && allProviders.contains("gps")){
			lm.requestLocationUpdates("gps",0, 0,  listener);
		}
		
		super.onCreate();
	}
	private class MyListener implements LocationListener{
		//��λ�ø���ʱ
		@Override
		public void onLocationChanged(Location location) {
			//�õ���γ��
			String latitude = "latitude:"+location.getLatitude();//ά��
			String longitude="longitude"+location.getLongitude();//����
			//����γ�ȵ���Ϣ��������������
			SmsManager sm=SmsManager.getDefault();
			sm.sendTextMessage(sp.getString("gpsNumber", ""), null, latitude+"\n"+longitude, null, null);
			//�Ƴ�����
			lm.removeUpdates(listener);
			listener=null;
			//���������ر�
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
