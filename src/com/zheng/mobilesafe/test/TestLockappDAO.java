package com.zheng.mobilesafe.test;

import android.test.AndroidTestCase;

import com.zheng.mobilesafe.db.dao.AppLockDao;

public class TestLockappDAO extends AndroidTestCase{
	
	
	public void testAddLock(){
		AppLockDao dao=new AppLockDao(getContext());
		System.out.println(dao.addLockapp("packname1"));
	}
	
	public void testDeleteLock(){
		AppLockDao dao=new AppLockDao(getContext());
		System.out.println(dao.deleteLockapp("packname1"));
	}
	
	public void testFindLock(){
		AppLockDao dao=new AppLockDao(getContext());
		System.out.println(dao.findLockapp("packname1"));
	}
	

}
