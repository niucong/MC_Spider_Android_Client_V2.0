package com.datacomo.mc.spider.android.util;

import android.app.Activity;
import android.os.Build;

public class ApiDiffCoding {
	public static void hardwareAcceleerated(Activity context){
		if(Build.VERSION.SDK_INT >= 11){
			context.getWindow().setFlags(16777216, 16777216);
		}
	}
	
	public static boolean IsSdkNewerThan(int version){
		if(Build.VERSION.SDK_INT >= version){
			return true;
		}
		return false;
	}
}
