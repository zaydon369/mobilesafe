# Mobilesafe
##1,添加splash界面
	设置全屏
	显示当前版本号
	检查更新(添加Internet访问权限)
		通过配置文件设置ip地址访问服务器
		添加StreamTools将流转换成字符串
		判断当前版本号和服务器的是否相同
		(发现BUG**服务器没有开启是,没有消息提示,有待解决**)
	下载更新(添加SD卡的写入权限)
		使用xutil工具进行下载
		设置下载进度提示
		完成后替换安装
	控制进入UI的时间
		解决访问服务器超时bug,修改api为conn.setConnectTimeout(2000);
		在finally统一使用handler发送消息
		设置在splash界面统一停留2秒
##2,添加UI界面的头部
	标题
	logo旋转
	跑马灯



