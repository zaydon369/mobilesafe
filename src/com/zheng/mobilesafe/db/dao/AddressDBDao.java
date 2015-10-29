package com.zheng.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDBDao {

	public static String findLocation(Context context, String number) {
		String path = context.getFilesDir() + "/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		String location="Î´ÖªºÅÂë";
		Cursor cursor = db
				.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",
						new String[] { number.substring(0, 7) });
		if(cursor.moveToNext()){
			location=cursor.getString(0);
		}
		
		return location;
	}

}
