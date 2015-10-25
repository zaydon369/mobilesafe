package com.zheng.mobilesafe.activities.utils;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	/**
	 * MD5密码加密
	 * @param password 原密码
	 * @return 加密后的密码
	 */
public static String encode(String password){
	try {
		//采用md5加密技术
		MessageDigest digest = MessageDigest.getInstance("md5");
		//将密码转换成2进制数组
		byte[] bys = digest.digest(password.getBytes());
		//采用stringbuffer存储密码
		StringBuffer sb=new StringBuffer();
		//遍历取出,并转换成字符串
		for(byte by:bys){
			//b&0xff即是00000000(8个0),1个byte是8位(1个字节),int是32位4个字节
			//所以讲2进制转int最好用b&0xff
			//将byte转成int
			int i=by&0xff-5;
			String str = Integer.toHexString(i);
			//转成结果可能是1位也可能是两位,如果是一位则在前面补零
			if(str.length()==1){
				sb.append("0");
			}
			sb.append(str);
		}
		return sb.toString();
	} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
	}
	return "";
		
	}

}
