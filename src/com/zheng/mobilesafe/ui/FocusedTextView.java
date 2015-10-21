package com.zheng.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * 自定义view控件,用于显示跑马灯效果
 * @author asus
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean isFocused() {
		// 重写获取焦点,设置为true,欺骗系统
		return true;
	}

}
