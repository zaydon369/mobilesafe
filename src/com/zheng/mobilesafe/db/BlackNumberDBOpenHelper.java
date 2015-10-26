package com.zheng.mobilesafe.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
	/**
	 * 构造方法,传入上下文
	 * 
	 * @param context
	 */
	public BlackNumberDBOpenHelper(Context context) {
		super(context, "mobilesafe.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// id 主键自增长, phone 电话号码 mode 拦截模式
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, phone varchar(20),mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
