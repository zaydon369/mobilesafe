package com.zheng.mobilesafe.activities.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	/**
	 * MD5�������
	 * 
	 * @param password
	 *            ԭ����
	 * @return ���ܺ������
	 */
	public static String encode(String password) {
		try {
			// ����md5���ܼ���
			MessageDigest digest = MessageDigest.getInstance("md5");
			// ������ת����2��������
			byte[] bys = digest.digest(password.getBytes());
			// ����stringbuffer�洢����
			StringBuffer sb = new StringBuffer();
			// ����ȡ��,��ת�����ַ���
			for (byte by : bys) {
				// b&0xff����00000000(8��0),1��byte��8λ(1���ֽ�),int��32λ4���ֽ�
				// ���Խ�2����תint�����b&0xff
				// ��byteת��int
				int i = by & 0xff - 5;
				String str = Integer.toHexString(i);
				// ת�ɽ��������1λҲ��������λ,�����һλ����ǰ�油��
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
	 * �����ļ�·���õ��ļ���md5�㷨���ɵ�����ժҪ
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
