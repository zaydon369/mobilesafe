package com.zheng.mobilesafe.ui;

import com.zheng.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * ���ص�imageview
 * @author asus
 *
 */
public class SwitchImageView extends ImageView {
	/**
	 * ���ص�״̬,trueΪ��,falseΪ�ر�
	 */
	private boolean switchStatus = true;
	/** ��ȡ���ص�״̬
	 * @return the switchStatus
	 */
	public boolean getSwitchStatus() {
		return switchStatus;
	}

	/** ���ÿ��ص�״̬
	 * @param switchStatus the switchStatus to set
	 */
	public void setSwitchStatus(boolean switchStatus) {
		this.switchStatus = switchStatus;
		//����״̬��ʱ�����ͼƬ
		if(switchStatus){
			setImageResource(R.drawable.on);
		}else{
			setImageResource(R.drawable.off);
		}
	}
	/**
	 * ���Ŀ��ص�״̬
	 */
	public void changedSwitchStatus() {
		//ͨ������״̬�ķ���������ͼƬ״̬
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
