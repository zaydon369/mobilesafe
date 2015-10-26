package com.zheng.mobilesafe.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class TestBlackNumberDao extends AndroidTestCase {
/**
 * 测试添加数据
 */
	public void add() throws Exception{
		// TODO Auto-generated method stub
		BlackNumberDao dao=new BlackNumberDao(getContext());
		Random random=new Random();
		boolean add = dao.add("13500000000", random.nextInt(3)+1+"");
		
	}
	/**
	 * 测试查询数据
	 */
	public void find() throws Exception{
		// TODO Auto-generated method stub
		BlackNumberDao dao=new BlackNumberDao(getContext());
		System.out.println(dao.find("13500000000"));
	}
	/**
	 * 测试更改数据
	 */
	public void update(){
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.updateMode("13500000000", "2");
	}
	
	/**
	 * 测试删除数据
	 */
	public  void testDelete() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.delete("13500000000");
	}
}
