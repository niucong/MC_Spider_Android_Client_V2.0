package com.datacomo.mc.spider.android.util;

import android.view.View;
import android.view.View.OnClickListener;

public class SimpleUtil {

	public static void setAllOnClickLisener(OnClickListener listener, View... views){
		for (View view : views) {
			view.setOnClickListener(listener);
		}
	}
}
