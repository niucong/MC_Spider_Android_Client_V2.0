package com.datacomo.mc.spider.android.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.url.L;

public class DebugUtil {
	private static String TAG = "DEBUG";
	public static void printMemryInfo(){
		ActivityManager am = (ActivityManager) new App().getSystemService(Activity.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		L.i(TAG, " memoryInfo.availMem    " + mi.availMem + "\n" );
		L.i(TAG, " memoryInfo.lowMemory " + mi.lowMemory + "\n" );
		L.i(TAG, " memoryInfo.threshold     " + mi.threshold + "\n" );

		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

		Map<Integer, String> pidMap = new TreeMap<Integer, String>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
		{
		    pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
		}

		Collection<Integer> keys = pidMap.keySet();

		for(int key : keys)
		{
		    int pids[] = new int[1];
		    pids[0] = key;
		    android.os.Debug.MemoryInfo[] memoryInfoArray = am.getProcessMemoryInfo(pids);
		    for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
		    {
		        L.i(TAG, String.format("** MEMINFO in pid %d [%s] **\n",pids[0],pidMap.get(pids[0])));
		        L.i(TAG, " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
		        L.i(TAG, " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
		        L.i(TAG, " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
		    }
		}
	}
}
