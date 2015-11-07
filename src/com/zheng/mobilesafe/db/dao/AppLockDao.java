package com.zheng.mobilesafe.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zheng.mobilesafe.db.AppLockDBOpenHelper;

public class AppLockDao {
	private AppLockDBOpenHelper helper;

	public AppLockDao(Context context) {
		helper = new AppLockDBOpenHelper(context);

	}

	/**
	 * 添加一个加锁程序
	 * 
	 * @param packName
	 *            包名
	 * @return 是否添加成功
	 */
	public boolean addLockapp(String packName) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packName);
		flag = (db.insert("lockinfo", null, values) > 0);
		db.close();
		return flag;
	}

	/**
	 * 查看应用程序是否加锁
	 * 
	 * @param packName
	 * @return
	 */
	public boolean findLockapp(String packName) {
		boolean flag = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.query("lockinfo", new String[] { "packname" }, "packname=?",
						new String[] { packName }, null, null, null, null);
		flag = cursor.moveToNext();
		cursor.close();
		db.close();
		return flag;

	}

	/**
	 * 将应用程序从列表中删除
	 * 
	 * @param packName
	 * @return
	 */
	public boolean deleteLockapp(String packName) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		flag = (db.delete("lockinfo", "packname=?", new String[] { packName }) > 0);
		db.close();
		return flag;
	}

	public ArrayList findAllLockapps() {
		ArrayList<String> allLockapps = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("lockinfo", new String[] { "packname" }, null,
				null, null, null, null, null);
		while(cursor.moveToNext()){
			allLockapps.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return allLockapps;
	}
}
