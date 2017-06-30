package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ImageDealUtil;

public abstract class BaseActivity extends BaseUMengActivity {
	private final String BASETAG = "BaseActivity";
	private ExitBroadcastReceiver exitReceiver;
	private IntentFilter intentFilter;
	private static Class<?> rootAct;
	private Intent service;
	public ArrayList<ImageView> imageViewsWithBitmaps = new ArrayList<ImageView>();

	// public final static float TARGET_HEAP_UTILIZATION = 0.75f;

	private BroadcastReceiver appReceiver;
	public static SpinnerProgressDialog spdDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// VMRuntime.getRuntime()
		// .setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);

		exitReceiver = new ExitBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.EXIT_APP);

		service = new Intent(this, NotificationService.class);
		spdDialog = new SpinnerProgressDialog(this);
		setRootAct();
	}

	private void setRootAct() {
		if (this.isTaskRoot()) {
			rootAct = this.getClass();
		}
	}

	public static Class<?> getRootAct() {
		return rootAct;
	}

	@Override
	protected void onStart() {
		super.onStart();
		L.i(BASETAG, "onStart...");
		registerReceiver(exitReceiver, intentFilter);
		if (App.app.share.getBooleanMessage("isOtherLogin", "isOtherLogin",
				false)) {
			for (Activity a : activityList) {
				if (a != null) {
					a.finish();
				}
			}

			Intent i = new Intent(BaseActivity.this, LoginActivity.class);
			i.putExtra("otherLogin", true);
			startActivity(i);
			BaseActivity.this.finish();
			App.app.share.saveBooleanMessage("isOtherLogin", "isOtherLogin",
					false);
		} else if ("".equals(App.app.share.getSessionKey())) {
		}
	}

	void onDataChanged() {
	}

	void registeRefreshReceiver(final String action) {
		if (null == appReceiver) {
			appReceiver = new SimpleReceiver() {

				@Override
				public void onReceive(Context arg0, Intent arg1) {
					if (action.equals(arg1.getAction())) {
						onDataChanged();
					}
				}
			};
			SimpleReceiver.registerReceiver(this, appReceiver, action);
		}
	}

	@Override
	protected void onStop() {
		L.d(BASETAG, "onStop...");
		super.onStop();
		try {
			unregisterReceiver(exitReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class ExitBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			L.d(BASETAG, "ExitBroadcastReceiver action=" + action);
			if (BootBroadcastReceiver.EXIT_APP.equals(action)) {
				for (Activity a : activityList) {
					if (a != null) {
						a.finish();
					}
				}

				Intent i = new Intent(BaseActivity.this, LoginActivity.class);
				i.putExtra("otherLogin", true);
				startActivity(i);
				BaseActivity.this.finish();
				App.app.share.saveBooleanMessage("isOtherLogin",
						"isOtherLogin", false);
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(null);
		L.d(BASETAG, "BaseActivity  onNewIntent..." + intent);
	}

	public void startNService(boolean isSilence) {
		if (service != null) {
			service.putExtra("isSilence", isSilence);
			startService(service);
		}
	}

	// public void stopNService() {
	// if (service != null)
	// stopService(service);
	// }

	/**
	 * 通过Service的类名来判断是否启动某个服务
	 * 
	 * @return
	 */
	protected boolean notificationServiceIsStart() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
				.getRunningServices(30);
		for (int i = 0; i < mServiceList.size(); i++) {
			if (NotificationService.class.getName().equals(
					mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	protected void addBmpList(ImageView v) {
		imageViewsWithBitmaps.add(v);
	}

	public void releaseBmps() {
		ImageDealUtil.releaseImageDrawables(imageViewsWithBitmaps, true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != appReceiver)
			unregisterReceiver(appReceiver);
		releaseBmps();
		imageViewsWithBitmaps.clear();
	}
}
