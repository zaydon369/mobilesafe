package com.zheng.mobilesafe.activities.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

import org.apache.http.util.ByteArrayBuffer;

/**
 * 流的工具类
 */
public class StreamTools {
	/**
	 * 读取一个输入流,将流转成字符串
	 * 
	 * @param is
	 *            输入流
	 * @return 转成的字符串
	 */
	public static String readStream(InputStream is) throws IOException {
		//用来存储读取的字符串
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//每次读取的字节的大小
		byte[] buffer = new byte[1024];
		//每次读取到的字符的数量,没有字符为-1
		int len = -1;
		//循环读取直到结束
		while((len = is.read(buffer)) != -1) {
			//将读取到的字节存进baos
			baos.write(buffer, 0, len);
		}
		is.close();
		return baos.toString();
	}
}
