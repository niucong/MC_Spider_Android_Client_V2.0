package com.datacomo.mc.spider.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public abstract class SimpleReceiver extends BroadcastReceiver{
	public static final String RECEIVER_ADD_MEMBER = "RECEIVER_ADD_MEMBER";
	public static final String RECEIVER_EDIT_DESCRIPTION = "RECEIVER_EDIT_DESCRIPTION";
	public static final String RECEIVER_DATA_CHANGED = "RECEIVER_DATA_CHANGED";
	public static void sendBoardcast(Context context, String action){
		Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}
	
	public static void sendBoardcast(Context context, Intent intent){
		context.sendBroadcast(intent);
	}
	
	public static void sendBoardcast(Context context, String action, Bundle bundle){
		Intent intent = new Intent(action);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}
	
	@Override
	public abstract void onReceive(Context arg0, Intent arg1);
	
	public static void registerReceiver(Context context, BroadcastReceiver receiver, String... action){
		IntentFilter intentFilter = new IntentFilter();
		for (String a : action) {
			intentFilter.addAction(a);
		}
		context.registerReceiver(receiver, intentFilter);
	}
}
