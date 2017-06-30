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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.util.BaseData;

public abstract class BaseAct extends BaseActivity implements OnClickListener {
	// private static final String TAG = "BaseAct";

	public LinearLayout layout;
	private LinearLayout net_change;
	protected ImageView left, right, second, third;
	protected TextView title;
	protected Resources res;
	protected ProgressBar header_progress;

	private MsgNumBroadcastReceiver numReceiver;
	private IntentFilter intentFilter;
	private TextView num_tv;
	private boolean isShowNum;

	// @Override
	// protected void onDestroy() {
	// L.d(TAG, "onDestroy..." + this);
	// super.onDestroy();
	// layout = null;
	// left = null;
	// right = null;
	// second = null;
	// title = null;
	// third = null;
	// header_progress = null;
	// num_tv = null;
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		res = getResources();
		findView();

		numReceiver = new MsgNumBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.MSG_NUMBERS);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	}

	private void findView() {
		layout = (LinearLayout) findViewById(R.id.out);
		layout.setBackgroundResource(R.drawable.bg_main_white);
		left = (ImageView) findViewById(R.id.left);
		second = (ImageView) findViewById(R.id.second);
		right = (ImageView) findViewById(R.id.right);
		third = (ImageView) findViewById(R.id.third);
		title = (TextView) findViewById(R.id.title);
		findViewById(R.id.header).setOnClickListener(this);

		header_progress = (ProgressBar) findViewById(R.id.header_progress);
		num_tv = (TextView) findViewById(R.id.left_num);

		net_change = (LinearLayout) findViewById(R.id.net_change);
		net_change.setOnClickListener(this);
	}

	public class MsgNumBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.MSG_NUMBERS.equals(action)) {
				Bundle b = intent.getExtras();
				int[] num = b.getIntArray("nums");
				if (isShowNum) {
					if (num[0] > 0 || num[1] > 0 || num[2] > 0) {
						if (!num_tv.isShown())
							num_tv.setVisibility(View.VISIBLE);
					} else {
						num_tv.setVisibility(View.GONE);
					}
				}
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

		if (!IsNetworkConnected.checkNetworkInfo(this)) {
			net_change.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(numReceiver);

		net_change.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			onLeftClick(v);
			break;
		case R.id.right:
			onRightClick(v);
			break;
		case R.id.second:
			onSecondClick(v);
			break;
		case R.id.third:
			onThirdClick(v);
			break;
		case R.id.net_change:
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 复杂header
	 * 
	 * @param text
	 *            title文字
	 * @param leftDrawable
	 *            左边图标
	 * @param secondDrawable
	 *            第二个图标（动态墙有刷新按钮，其他页面不需要）
	 * @param thirdDrawable
	 *            第三个图标
	 * @param thirdText
	 *            第三个图标上的文字
	 * @param rightDrawable
	 *            右边的图标
	 */
	public void setTitle(String text, Integer leftDrawable,
			boolean isShowRefreshIcon, Integer thirdDrawable,
			Integer rightDrawable) {
		if (null == text)
			text = "";
		try {
			title.setText(text);
			if (null != leftDrawable) {
				left.setBackgroundResource(R.drawable.title_button);
				left.setImageResource(leftDrawable);
				left.setOnClickListener(this);

				if (leftDrawable == R.drawable.title_menu) {
					isShowNum = true;
				} else {
					isShowNum = false;
				}
			}
			if (null != rightDrawable) {
				right.setBackgroundResource(R.drawable.title_button);
				right.setImageResource(rightDrawable);
				right.setOnClickListener(this);
				right.setVisibility(View.VISIBLE);
			}

			second.setBackgroundResource(R.drawable.title_refresh);
			second.setOnClickListener(this);
			if (isShowRefreshIcon) {
				second.setVisibility(View.VISIBLE);
			}

			if (null != thirdDrawable) {
				third.setBackgroundResource(R.drawable.title_button);
				third.setImageResource(thirdDrawable);
				third.setOnClickListener(this);
				third.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTitleText(String text) {
		title.setText(text);
	}

	/*
	 * 左边和右边都是图片，左窄右宽
	 */
	public void setTitle(String text, Integer leftDrawable,
			Integer rightDrawable) {
		if (null == text)
			text = "";
		try {
			title.setText(text);
			if (null != leftDrawable) {
				left.setBackgroundResource(R.drawable.title_button);
				left.setImageResource(leftDrawable);
				left.setOnClickListener(this);

				if (leftDrawable == R.drawable.title_menu) {
					isShowNum = true;
				} else {
					isShowNum = false;
				}
			}
			if (null != rightDrawable) {
				right.setBackgroundResource(R.drawable.title_button);
				right.setImageResource(rightDrawable);
				right.setVisibility(View.VISIBLE);
				right.setOnClickListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContent(int contentLayout) {
		if (0 < contentLayout) {
			layout.addView(LayoutInflater.from(this).inflate(contentLayout,
					null));
		}
	}

	public void setContent(View v) {
		if (null != v) {
			layout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
	}

	protected void showHeaderProgress() {
		second.setVisibility(View.GONE);
		header_progress.setVisibility(View.VISIBLE);
	}

	protected void hintHeaderProgress() {
		try {
			second.setVisibility(View.VISIBLE);
			header_progress.setVisibility(View.GONE);
		} catch (Exception e) {
		}
	}

	protected void setTitle(String text) {
		title.setText(text);
	}

	protected abstract void onLeftClick(View v);

	protected abstract void onRightClick(View v);

	protected void onSecondClick(View v) {
	}

	protected void onThirdClick(View v) {
	}

	@Override
	public void finish() {
		super.finish();
		BaseData.hideKeyBoard(this);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_rihgt_out);
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// /* 回收图片 */
	// try {
	// new RecycleBitmapInLayout(true).recycle(layout);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// layout.removeAllViews();
	// }
}