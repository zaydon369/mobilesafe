package com.zheng.mobilesafe.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
	/**
	 * ���췽��,����������
	 * 
	 * @param context
	 */
	public BlackNumberDBOpenHelper(Context context) {
		super(context, "mobilesafe.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// id ����������, phone �绰���� mode ����ģʽ
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, phone varchar(20),mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
