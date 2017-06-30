package com.datacomo.mc.spider.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.url.L;

public class NetStateReceiver extends BroadcastReceiver {
	private final String TAG = "NetStateReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!IsNetworkConnected.checkNetworkInfo(context)) {
			// network closed
			L.i(TAG, "onReceive network closed");
			// context.stopService(new Intent(context,
			// NotificationService.class));
		} else {
			// network opend
			L.d(TAG, "onReceive network opend");
			Intent service = new Intent(context, NotificationService.class);
			service.putExtra("isSilence", false);
			context.startService(service);
		}
	}

}