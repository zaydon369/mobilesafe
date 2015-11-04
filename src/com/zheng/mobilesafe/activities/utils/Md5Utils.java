package com.zheng.mobilesafe.activities.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	/**
	 * MD5密码加密
	 * 
	 * @param password
	 *            原密码
	 * @return 加密后的密码
	 */
	public static String encode(String password) {
		try {
			// 采用md5加密技术
			MessageDigest digest = MessageDigest.getInstance("md5");
			// 将密码转换成2进制数组
			byte[] bys = digest.digest(password.getBytes());
			// 采用stringbuffer存储密码
			StringBuffer sb = new StringBuffer();
			// 遍历取出,并转换成字符串
			for (byte by : bys) {
				// b&0xff即是00000000(8个0),1个byte是8位(1个字节),int是32位4个字节
				// 所以讲2进制转int最好用b&0xff
				// 将byte转成int
				int i = by & 0xff - 5;
				String str = Integer.toHexString(i);
				// 转成结果可能是1位也可能是两位,如果是一位则在前面补零
				if (str.length() == 1) {
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

	/**
	 * 根据文件路径得到文件的md5算法生成的数字摘要
	 */
	public static String getFileMd5(String path) {
		File file = new File(path);
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer,0,len);
				
			}
			byte[] result=digest.digest();
			StringBuilder sb=new StringBuilder();
			for(byte b:result){
				int number=b&0xff;
				String str=Integer.toHexString(number);
				if(str.length()==1){
					sb.append("0");
				}
						sb.append(str);
			}
			return sb.toString();
			

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "";
	}
}
