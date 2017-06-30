package com.datacomo.mc.spider.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.url.L;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";

    public static final String MSG_NUMBERS = "MSG_NUMBERS";
    public static final String CHAT_NUMBER = "CHAT_NUMBER";
    public static final String QUUCHAT_NUMBER = "QUUCHAT_NUMBER";
    public static final String QUUCHAT = "QUUCHAT";
    public static final String QUUCHAT_REMOVE = "QUUCHAT_REMOVE";
    public static final String EXIT_APP = "EXITAPP";
    public static final String REFERSH_ACTION = "REFERSH_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("isSilence", false);
        context.startService(service);
        L.i(TAG, "BootBroadcastReceiver : service started.");
    }
}
