package com.datacomo.mc.spider.android.application;

import java.util.Stack;

import android.app.Activity;

import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.url.L;

public class ScreenManager {
	private static final String TAG = "ScreenManager";

	private static ScreenManager inctance;
	private static Stack<Activity> activityStack;
	private int maxSize = 4;

	private ScreenManager() {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
	}

	public static ScreenManager getInctance() {
		if (inctance == null) {
			inctance = new ScreenManager();
		}
		return inctance;
	}

	public void popActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void removeActivity(Activity activity) {
		L.i(TAG, "removeActivity size=" + activityStack.size() + ",activity="
				+ activity);
		if (activity != null) {
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void closeAllActivity() {
		L.i(TAG, "closeAllActivity size=" + activityStack.size());
		while (activityStack.size() > 0) {
			popActivity(lastActivity());
		}
	}

	@SuppressWarnings("rawtypes")
	public void popAllActivityExceptOne(Class cls) {
		L.i(TAG, "popAllActivityExceptOne size=" + activityStack.size()
				+ ",cls=" + cls);
		while (true) {
			Activity activity = lastActivity();
			L.i(TAG, "popAllActivityExceptOne activity=" + activity);
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	public Activity currentActivity() {
		return activityStack.firstElement();
	}

	public Activity lastActivity() {
		return activityStack.lastElement();
	}

	public void pushActivity(Activity activity) {
		if (activity != null) {
			activityStack.add(activity);
		}
		L.i(TAG, "pushActivity activity=" + activity);

		int size = activityStack.size();
		L.i(TAG, "pushActivity size=" + size);
		if (size > maxSize) {
			for (Activity act : activityStack) {
				if (!act.getClass().equals(InfoWallActivity.class)) {
					popActivity(act);
					break;
				}
			}
		}
	}
}
