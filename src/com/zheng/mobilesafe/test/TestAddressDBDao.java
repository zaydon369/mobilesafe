package com.zheng.mobilesafe.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.zheng.mobilesafe.db.dao.AddressDBDao;
import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class TestAddressDBDao extends AndroidTestCase {
	/**
	 * 查询所有数据
	 */
	public void testFindAll(){
		System.out.println(AddressDBDao.findLocation(getContext(), "15059595959"));
	}
}
