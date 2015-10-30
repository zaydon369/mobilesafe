package com.zheng.mobilesafe.activities.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

public class SmsTools {

	/**
	 * ����һ���ӿڣ������ṩ���ݶ��ŵĻص�����
	 */
	public interface BackupCallback {
		/**
		 * ���ű���ǰ���õĴ���
		 * @param max
		 *            һ��������������Ҫ����
		 */
		public void beforeSmsBackup(int max);

		/**
		 * ���ű��ݹ����е��õĴ���
		 * @param progress
		 *            ��ǰ���ݵĽ���
		 */
		public void onSmsBackup(int progress);
	}

	public static void backUpSms(Context context, BackupCallback callback) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(),
				"smsbackup.xml");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			serializer.setOutput(fos, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "infos");
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"body", "type", "date" }, null, null, null);
			int progress = 0;
			callback.beforeSmsBackup(cursor.getCount());
			while(cursor.moveToNext()){
				serializer.startTag(null, "info");
				String address = cursor.getString(0);
				serializer.startTag(null, "address");
				serializer.text(address);
				serializer.endTag(null, "address");
				String body = cursor.getString(1);
				serializer.startTag(null, "body");
				serializer.text(body);
				serializer.endTag(null, "body");
				String type = cursor.getString(2);
				serializer.startTag(null, "type");
				serializer.text(type);
				serializer.endTag(null, "type");
				String date = cursor.getString(3);
				serializer.startTag(null, "date");
				serializer.text(date);
				serializer.endTag(null, "date");
				serializer.endTag(null, "info");
				SystemClock.sleep(1500);
				progress++;
				callback.onSmsBackup(progress);
			}
			cursor.close();
			serializer.endTag(null, "infos");
			serializer.endDocument();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
