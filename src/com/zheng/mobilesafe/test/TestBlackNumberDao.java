package com.zheng.mobilesafe.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.zheng.mobilesafe.db.dao.BlackNumberDao;

public class TestBlackNumberDao extends AndroidTestCase {
/**
 * �����������
 */
	public void add() throws Exception{
		// TODO Auto-generated method stub
		BlackNumberDao dao=new BlackNumberDao(getContext());
		Random random=new Random();
		boolean add = dao.add("13500000000", random.nextInt(3)+1+"");
		
	}
	/**
	 * ���Բ�ѯ����
	 */
	public void find() throws Exception{
		// TODO Auto-generated method stub
		BlackNumberDao dao=new BlackNumberDao(getContext());
		System.out.println(dao.find("13500000000"));
	}
	/**
	 * ���Ը�������
	 */
	public void update(){
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.updateMode("13500000000", "2");
	}
	
	/**
	 * ����ɾ������
	 */
	public  void testDelete() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.delete("13500000000");
	}
}
