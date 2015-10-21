package com.zheng.mobilesafe.ui;

import com.zheng.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 开关的imageview
 * @author asus
 *
 */
public class SwitchImageView extends ImageView {
	/**
	 * 开关的状态,true为打开,false为关闭
	 */
	private boolean switchStatus = true;
	/** 获取开关的状态
	 * @return the switchStatus
	 */
	public boolean getSwitchStatus() {
		return switchStatus;
	}

	/** 设置开关的状态
	 * @param switchStatus the switchStatus to set
	 */
	public void setSwitchStatus(boolean switchStatus) {
		this.switchStatus = switchStatus;
		//设置状态的时候更改图片
		if(switchStatus){
			setImageResource(R.drawable.on);
		}else{
			setImageResource(R.drawable.off);
		}
	}
	/**
	 * 更改开关的状态
	 */
	public void changedSwitchStatus() {
		//通过设置状态的反码来更改图片状态
		setSwitchStatus(!switchStatus);
	}
	public SwitchImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SwitchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SwitchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
