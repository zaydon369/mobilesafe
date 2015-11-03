package com.zheng.mobilesafe.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.TrafficStats;

import com.zheng.mobilesafe.domain.AppInfo;
import com.zheng.mobilesafe.domain.TrafficInfo;

public class TrafficInfoProvider {
	/**
	 * 获取所有程序的流量信息
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<TrafficInfo> getTrafficInfos(Context context) {
		ArrayList<TrafficInfo> infos = new ArrayList<TrafficInfo>();
		// 得到系统应用的ID
		List<AppInfo> allAppInfos = AppInfoProvider.getAllAppInfos(context);
		// 根据id遍历查找流量信息
		for (AppInfo appInfo : allAppInfos) {
			TrafficInfo trafficInfo = new TrafficInfo();
			int uid = appInfo.getUid();
			trafficInfo.setAppName(appInfo.getAppName());
			trafficInfo.setRxBytes(TrafficStats.getUidRxBytes(uid));
			trafficInfo.setTxBytes(TrafficStats.getUidTxBytes(uid));
			trafficInfo.setTotalBytes(TrafficStats.getUidRxBytes(uid)
					+ TrafficStats.getUidTxBytes(uid));
			infos.add(trafficInfo);
		}
		ComparatorTraffic comparator = new ComparatorTraffic();
		Collections.sort(infos, comparator);
		return infos;
	}

	static class ComparatorTraffic implements Comparator {
		ComparatorTraffic() {
		}

		@Override
		public int compare(Object lhs, Object rhs) {
			TrafficInfo info0 = (TrafficInfo) lhs;
			TrafficInfo info1 = (TrafficInfo) rhs;

			// 首先比较年龄，如果年龄相同，则比较名字

			int flag = (int) (info1.getTotalBytes() - info0.getTotalBytes());
			if (flag == 0) {
				return info0.getAppName().compareTo(info1.getAppName());
			} else {
				return flag;
			}
		}
	}
}
