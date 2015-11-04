package com.zheng.mobilesafe.activities.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class CopyFileUtils {

	public static void copyAssetsFile(Context context, String assetsFileName) {
		String TAG = "copyAssetsFile";
		File file = new File(context.getFilesDir(), assetsFileName);
		if (file.exists() && file.length() > 0) {

			Log.i(TAG, assetsFileName + "已存在,无需拷贝");
		} else {

			try {
				InputStream is = context.getAssets().open(assetsFileName);
				// File file = new File(context.getFilesDir(), assetsFileName);
				FileOutputStream fos;
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
				Log.i(TAG, assetsFileName + "复制完毕");
			} catch (IOException e) {
				Log.i(TAG, assetsFileName + "复制出现错误!!");
				e.printStackTrace();
			}
		}

	}

}
