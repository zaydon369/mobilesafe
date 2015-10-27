package com.zheng.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zheng.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zheng.mobilesafe.domain.BlackNumberInfo;

/**
 * 黑名单管理的DAO
 * 
 */
public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	/**
	 * 在构造方法初始化help
	 * 
	 * @param context
	 */
	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);

	}

	/**
	 * 添加黑名单
	 * 
	 * @param phone
	 *            电话
	 * @param mode
	 *            模式,1电话拦截,2短信拦截,3全部拦截
	 * @return 是否添加成功
	 */
	public boolean add(String phone, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long l = db.insert("blacknumber", null, values);
		db.close();
		if (l != -1) {
			// 添加成功
			return true;
		}
		return false;

	}

	/**
	 * 根据号码删除黑名单
	 * 
	 * @param phone
	 * @return
	 */
	public boolean delete(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int i = db.delete("blacknumber", "phone=?", new String[] { phone });
		db.close();
		if (i != 0) {
			// 不等于0则删除成功
			return true;
		}
		return false;
	}

	/**
	 * 更改号码的拦截模式
	 * 
	 * @param phone
	 * @param newMode
	 * @return
	 */
	public boolean updateMode(String phone, String newMode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		int i = db.update("blacknumber", values, "phone=?",
				new String[] { phone });
		db.close();
		if (i > 0) {
			// 结果大于0,说明更新成功
			return true;
		}
		return false;
	}

	/**
	 *  查找手机号码的拦截模式
	 * @param phone
	 * @return 手机号的拦截模式
	 */

	public String find(String phone) {
		String mode = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"phone=?", new String[] { phone }, null, null, null);
		
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		db.close();
		cursor.close();
		return mode;
	}
	/**
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		List<BlackNumberInfo> list =new ArrayList<BlackNumberInfo>();
		//连接数据库
		SQLiteDatabase db = helper.getReadableDatabase();
		//查询所有
		Cursor cursor = db.query("blacknumber",new String[] { "_id","phone","mode" }, null, null, null, null, null);
		//遍历读取
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			info.setId(cursor.getString(0));
			info.setPhone(cursor.getString(1));
			info.setMode(cursor.getString(2));
			list.add(info);
		}
		db.close();
		cursor.close();
		return list;
	}
}
