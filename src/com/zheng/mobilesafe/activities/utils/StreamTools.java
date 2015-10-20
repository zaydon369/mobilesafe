package com.zheng.mobilesafe.activities.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

import org.apache.http.util.ByteArrayBuffer;

/**
 * ���Ĺ�����
 */
public class StreamTools {
	/**
	 * ��ȡһ��������,����ת���ַ���
	 * 
	 * @param is
	 *            ������
	 * @return ת�ɵ��ַ���
	 */
	public static String readStream(InputStream is) throws IOException {
		//�����洢��ȡ���ַ���
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//ÿ�ζ�ȡ���ֽڵĴ�С
		byte[] buffer = new byte[1024];
		//ÿ�ζ�ȡ�����ַ�������,û���ַ�Ϊ-1
		int len = -1;
		//ѭ����ȡֱ������
		while((len = is.read(buffer)) != -1) {
			//����ȡ�����ֽڴ��baos
			baos.write(buffer, 0, len);
		}
		is.close();
		return baos.toString();
	}
}
