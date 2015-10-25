package com.zheng.mobilesafe.activities.utils;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	/**
	 * MD5�������
	 * @param password ԭ����
	 * @return ���ܺ������
	 */
public static String encode(String password){
	try {
		//����md5���ܼ���
		MessageDigest digest = MessageDigest.getInstance("md5");
		//������ת����2��������
		byte[] bys = digest.digest(password.getBytes());
		//����stringbuffer�洢����
		StringBuffer sb=new StringBuffer();
		//����ȡ��,��ת�����ַ���
		for(byte by:bys){
			//b&0xff����00000000(8��0),1��byte��8λ(1���ֽ�),int��32λ4���ֽ�
			//���Խ�2����תint�����b&0xff
			//��byteת��int
			int i=by&0xff-5;
			String str = Integer.toHexString(i);
			//ת�ɽ��������1λҲ��������λ,�����һλ����ǰ�油��
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
