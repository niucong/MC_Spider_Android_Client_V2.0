package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.url.L;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MemberContactUtil {
	private Context context;

	public MemberContactUtil(Context context) {
		this.context = context;
	}

	public void callPhone(String tel) {
		Uri telUri = Uri.parse("tel:" + tel);
		// Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
		// context.startActivity(intent);
		Intent intent = new Intent(Intent.ACTION_CALL, telUri);
		try {
			context.startActivity(intent);
		} catch (SecurityException e) {
			L.i("MemberContactUtil", "permission denial");
		} catch (Exception e) {
		}
	}

}
