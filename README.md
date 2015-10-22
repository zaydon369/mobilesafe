# Mobilesafe
##1,添加splash界面
	设置全屏
	显示当前版本号
	检查更新(添加Internet访问权限)
		通过配置文件设置ip地址访问服务器
		添加StreamTools将流转换成字符串
		判断当前版本号和服务器的是否相同
		(发现BUG**服务器没有开启是,没有消息提示,有待解决:已解决,conn连接超时**)
	下载更新(添加SD卡的写入权限)
		使用xutil工具进行下载
		设置下载进度提示
		完成后替换安装
	控制进入UI的时间
		解决访问服务器超时bug,修改api为conn.setConnectTimeout(2000);
		在finally统一使用handler发送消息
		设置在splash界面统一停留2秒
		设置升级对话框,只能选择升级或者下次
##2,添加UI界面的头部
	标题(直接textview对齐,没什么好说的)
	logo旋转(属性动画)
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_logo, "rotationY",
				new float[] {0,60,120,180,240,300});
		//设置时长 
		oa.setDuration(1000);
		//设置动画次数,无限播放
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		//设置动画模式,重新开始
		oa.setRepeatMode(ObjectAnimator.RESTART);
		//开启动画
		oa.start();
		
	跑马灯
		自定义textview重写isFocused()返回true
		设置控件属性:
		android:ellipsize="marquee"
        android:focusableInTouchMode="true"
			
##3,设置home的功能界面
	使用gridview+打气筒填充功能界面
	设置不同背景色产生边框效果
	添加设置图标按钮
	设置setting界面的布局
##4,setting界面的优化
	使用自定义开关控件对开关进行优化
	布局的状态选择器(点击改变背景颜色)
		(将原来的点击按钮改变状态转成点击背景就可以改变状态)
	设置自动更新逻辑,通过sp配置文件读写判断
	



















