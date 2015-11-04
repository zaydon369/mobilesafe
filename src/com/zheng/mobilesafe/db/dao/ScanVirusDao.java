package com.zheng.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScanVirusDao {

	/**
	 * …®√Ë≤°∂æ
	 * 
	 * @param context
	 */
	public static String ScanVirus(Context context,String md5) {
		String path = context.getFilesDir() + "/antivirus.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
		String desc="";
		if(cursor.moveToNext()){
			 desc = cursor.getString(0);
		}
		return desc;

	}

}
