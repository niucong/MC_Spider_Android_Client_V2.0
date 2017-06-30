package com.datacomo.mc.spider.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;

public abstract class BaseFragAct extends BaseUMengActivity implements
		OnClickListener {
	public static final String TAG = "BaseFragAct";

	public LinearLayout layout;
	private LinearLayout net_change;
	// public LinearLayout layout;
	// protected ImageView left, right, second, third;
	// private TextView title;
	protected Resources res;
	// protected ProgressBar header_progress;

	private MsgNumBroadcastReceiver numReceiver;
	private ExitBroadcastReceiver exitReceiver;

	private IntentFilter intentFilter, exIntentFilter;
	// private TextView num_tv;
	// private boolean isShowNum;

	protected ActionBar ab;
	protected Menu menu;

	protected SpinnerProgressDialog spdDialog = new SpinnerProgressDialog(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// VMRuntime.getRuntime().setTargetHeapUtilization(
		// BaseActivity.TARGET_HEAP_UTILIZATION);
		setContentView(R.layout.main);
		res = getResources();

		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true); // 显示返回的箭头
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		ab.setIcon(R.drawable.nothing);
		ab.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.title_bg));// 设置ActionBar的背景

		findView();

		// num_tv = (TextView) findViewById(R.id.left_num);
		numReceiver = new MsgNumBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.MSG_NUMBERS);

		exitReceiver = new ExitBroadcastReceiver();
		exIntentFilter = new IntentFilter();
		exIntentFilter.addAction(BootBroadcastReceiver.EXIT_APP);

	}

	private void findView() {
		findViewById(R.id.header).setVisibility(View.GONE);

		layout = (LinearLayout) findViewById(R.id.out);
		layout.setBackgroundResource(R.drawable.bg_main_white);
		// left = (ImageView) findViewById(R.id.left);
		// second = (ImageView) findViewById(R.id.second);
		// right = (ImageView) findViewById(R.id.right);
		// third = (ImageView) findViewById(R.id.third);
		// title = (TextView) findViewById(R.id.title);
		// findViewById(R.id.header).setOnClickListener(this);
		// header_progress = (ProgressBar) findViewById(R.id.header_progress);

		net_change = (LinearLayout) findViewById(R.id.net_change);
		net_change.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setLoadingState(boolean refreshing) {
		if (menu != null) {
			MenuItem mProgressMenu = menu.findItem(R.id.action_refresh);
			if (refreshing) {
				mProgressMenu
						.setActionView(R.layout.actionbar_indeterminate_progress);
			} else {
				mProgressMenu.setActionView(null);
			}
		}
	}

	public class MsgNumBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.MSG_NUMBERS.equals(action)) {
				// Bundle b = intent.getExtras();
				// if (isShowNum) {
				// int num = b.getInt("num");
				// if (num > 0) {
				// if (!num_tv.isShown())
				// num_tv.setVisibility(View.VISIBLE);
				// } else {
				// num_tv.setVisibility(View.GONE);
				// }
				// }
				// setNum(b.getIntArray("nums"));
			} else if (ConnectivityManager.CONNECTIVITY_ACTION.endsWith(action)) {
				if (!IsNetworkConnected.checkNetworkInfo(context)) {
					net_change.setVisibility(View.VISIBLE);
				} else {
					net_change.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(numReceiver, intentFilter);
		registerReceiver(exitReceiver, exIntentFilter);

		if (App.app.share.getBooleanMessage("isOtherLogin", "isOtherLogin",
				false)) {
			finish();
		} else if ("".equals(App.app.share.getSessionKey())) {
			// logout(false);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(numReceiver);
		unregisterReceiver(exitReceiver);
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// exitFlag = false;
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// switch (event.getKeyCode()) {
	// case KeyEvent.KEYCODE_BACK:
	// if (this.isTaskRoot() && !exitFlag) {
	// exitFlag = !exitFlag;
	// showTip("再按一次返回键退出！");
	// return true;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.left:
		// onLeftClick(v);
		// break;
		// case R.id.right:
		// onRightClick(v);
		// break;
		// case R.id.second:
		// onSecondClick(v);
		// break;
		// case R.id.third:
		// onRightClick(v);
		// break;
		case R.id.net_change:
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// /**
	// * 针对非左窄右宽的复杂header
	// *
	// * @param text
	// * title文字
	// * @param leftDrawable
	// * 左边图标
	// * @param secondDrawable
	// * 第二个图标（动态墙有刷新按钮，其他页面不需要）
	// * @param thirdDrawable
	// * 第三个图标
	// * @param thirdText
	// * 第三个图标上的文字
	// * @param rightDrawable
	// * 右边的图标
	// */
	// public void setTitle(String text, Integer leftDrawable,
	// boolean showRefreshIcon, Integer thirdDrawable,
	// Integer rightDrawable) {
	// if (null == text) {
	// text = "";
	// }
	// title.setText(text);
	// if (null != leftDrawable) {
	// left.setBackgroundResource(leftDrawable);
	// left.setOnClickListener(this);
	//
	// if (leftDrawable == R.drawable.title_menu) {
	// isShowNum = true;
	// } else {
	// isShowNum = false;
	// }
	// }
	// if (null != leftDrawable) {
	// left.setBackgroundResource(R.drawable.title_button);
	// left.setImageResource(leftDrawable);
	// left.setOnClickListener(this);
	//
	// if (leftDrawable == R.drawable.title_menu) {
	// isShowNum = true;
	// } else {
	// isShowNum = false;
	// }
	// }
	// if (null != rightDrawable) {
	// right.setBackgroundResource(R.drawable.title_button);
	// right.setImageResource(rightDrawable);
	// right.setOnClickListener(this);
	// right.setVisibility(View.VISIBLE);
	// }
	//
	// if (null != thirdDrawable) {
	// third.setBackgroundResource(R.drawable.title_button);
	// third.setImageResource(thirdDrawable);
	// third.setOnClickListener(this);
	// third.setVisibility(View.VISIBLE);
	// }
	// }
	//
	// public void setTitleText(String text) {
	// title.setText(text);
	// }
	//
	// /*
	// * 左边和右边都是图片，左窄右宽
	// */
	// public void setTitle(String text, Integer leftDrawable,
	// Integer rightDrawable) {
	// if (null == text) {
	// text = "";
	// }
	// title.setText(text);
	// if (null != leftDrawable) {
	// left.setBackgroundResource(R.drawable.title_button);
	// left.setImageResource(leftDrawable);
	// left.setOnClickListener(this);
	//
	// if (leftDrawable == R.drawable.title_menu) {
	// isShowNum = true;
	// } else {
	// isShowNum = false;
	// }
	// }
	// if (null != rightDrawable) {
	// right.setBackgroundResource(R.drawable.title_button);
	// right.setImageResource(rightDrawable);
	// right.setOnClickListener(this);
	// right.setVisibility(View.VISIBLE);
	// }
	// }
	//
	public void setContent(int contentLayout) {
		if (0 < contentLayout) {
			layout.addView(
					LayoutInflater.from(this).inflate(contentLayout, null),
					new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT));
		}
	}

	public void setContent(View v) {
		if (null != v) {
			layout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
	}

	// public void showHeaderProgress() {
	// second.setVisibility(View.GONE);
	// header_progress.setVisibility(View.VISIBLE);
	// }
	//
	// public void hintHeaderProgress() {
	// second.setVisibility(View.VISIBLE);
	// header_progress.setVisibility(View.GONE);
	// }

	public void showTip(String text) {
		T.show(getApplicationContext(), text);
	}

	// protected void setTitle(String text) {
	// title.setText(text);
	// }
	//
	// protected abstract void onLeftClick(View v);
	//
	// protected abstract void onRightClick(View v);
	//
	// protected void onSecondClick(View v) {
	// }
	//
	// protected void setNum(int[] nums) {
	// }

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_rihgt_out);
	}

	public class ExitBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			L.d(TAG, "ExitBroadcastReceiver onReceive action=" + action);
			if (BootBroadcastReceiver.EXIT_APP.equals(action)) {
				BaseFragAct.this.finish();
			}
		}
	}
}