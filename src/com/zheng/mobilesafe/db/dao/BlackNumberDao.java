package com.zheng.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zheng.mobilesafe.db.BlackNumberDBOpenHelper;

/**
 * ������������DAO
 * 
 */
public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	/**
	 * �ڹ��췽����ʼ��help
	 * 
	 * @param context
	 */
	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);

	}

	/**
	 * ���Ӻ�����
	 * 
	 * @param phone
	 *            �绰
	 * @param mode
	 *            ģʽ,1�绰����,2��������,3ȫ������
	 * @return �Ƿ����ӳɹ�
	 */
	public boolean add(String phone, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long l = db.insert("blacknumber", null, values);
		db.close();
		if (l != -1) {
			// ���ӳɹ�
			return true;
		}
		return false;

	}

	/**
	 * ���ݺ���ɾ��������
	 * 
	 * @param phone
	 * @return
	 */
	public boolean delete(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int i = db.delete("blacknumber", "phone=?", new String[] { phone });
		db.close();
		if (i != 0) {
			// ������0��ɾ���ɹ�
			return true;
		}
		return false;
	}

	/**
	 * ���ĺ��������ģʽ
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
		if (1 > 0) {
			// �������0,˵�����³ɹ�
			return true;
		}
		return false;
	}

	/**
	 *  �����ֻ����������ģʽ
	 * @param phone
	 * @return �ֻ��ŵ�����ģʽ
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
}