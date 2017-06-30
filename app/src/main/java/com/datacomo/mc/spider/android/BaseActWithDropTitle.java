package com.datacomo.mc.spider.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.SearchBar;

public abstract class BaseActWithDropTitle extends BaseActivity implements
		OnClickListener {

	public LinearLayout layout;
	protected ImageView left, right, t_load, goSearch;
	private TextView title, backTitle;
	protected Resources res;
	// public boolean exitFlag;
	protected ProgressBar header_progress;
	// protected SlideMenuView slide;
	// protected MenuPage menus;

	private MsgNumBroadcastReceiver numReceiver;
	private IntentFilter intentFilter;
	private TextView num_tv;
	private boolean isShowNum;
	protected SearchBar tSearchBar;
	private LinearLayout title_plan, search_plan;
	private boolean showSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drop_title);
		res = getResources();
		findView();

		num_tv = (TextView) findViewById(R.id.left_num);
		numReceiver = new MsgNumBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.MSG_NUMBERS);
	}

	private void findView() {
		layout = (LinearLayout) findViewById(R.id.out);
		layout.setBackgroundResource(R.drawable.bg_main_white);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		t_load = (ImageView) findViewById(R.id.second);
		t_load.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		title.setOnClickListener(this);
		findViewById(R.id.header).setOnClickListener(this);
		goSearch = (ImageView) findViewById(R.id.go_search);
		goSearch.setOnClickListener(this);
		tSearchBar = (SearchBar) findViewById(R.id.title_search);
		backTitle = (TextView) findViewById(R.id.stop_search);
		backTitle.setOnClickListener(this);
		header_progress = (ProgressBar) findViewById(R.id.header_progress);
		search_plan = (LinearLayout) findViewById(R.id.search_plan);
		title_plan = (LinearLayout) findViewById(R.id.title_plan);
	}

	public class MsgNumBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.MSG_NUMBERS.equals(action)) {
				Bundle b = intent.getExtras();
				if (isShowNum) {
					int num = b.getInt("num");
					if (num > 0) {
						if (!num_tv.isShown())
							num_tv.setVisibility(View.VISIBLE);
					} else {
						num_tv.setVisibility(View.GONE);
					}
				}
				// MenuPage.refreshNum(b.getIntArray("nums"));
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(numReceiver, intentFilter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(numReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			onTitleClick();
			break;
		case R.id.left:
			onLeftClick(v);
			break;
		case R.id.right:
			onRightClick(v);
			break;
		case R.id.go_search:
			showSearch();
			break;
		case R.id.third:
			onRightClick(v);
			break;
		case R.id.stop_search:
			showTitle();
			break;
		case R.id.second:
			onRefreshClick(t_load);
			break;
		default:
			break;
		}
	}

	protected void onTitleClick() {
	};

	private void showSearch() {
		search_plan.setVisibility(View.VISIBLE);
		title_plan.setVisibility(View.GONE);
	}

	private void showTitle() {
		search_plan.setVisibility(View.GONE);
		title_plan.setVisibility(View.VISIBLE);
	}

	/**
	 * 针对非左窄右宽的复杂header
	 * 
	 * @param text
	 *            title文字
	 * @param leftDrawable
	 *            左边图标
	 * @param rightDrawable
	 *            右边的图标
	 * @param needSearch
	 *            是否需要搜索功能
	 */
	public void setTitle(String text, Integer leftDrawable,
			Integer rightDrawable, boolean needSearchBar) {
		setTitle(text, leftDrawable, rightDrawable);
		showSearch = needSearchBar;
		if (needSearchBar) {
			goSearch.setVisibility(View.VISIBLE);
		} else {
			t_load.setVisibility(View.VISIBLE);
		}
		title.requestFocus();
	}

	public void setTitleText(String text) {
		if (null == text) {
			text = "";
			title.setVisibility(View.GONE);
		} else {
			title.setText(text);
			title.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * 左边和右边都是图片，左窄右宽
	 */
	public void setTitle(String text, Integer leftDrawable,
			Integer rightDrawable) {
		setTitleText(text);
		if (null != leftDrawable) {
			left.setBackgroundResource(R.drawable.title_button);
			left.setImageResource(leftDrawable);
			left.setVisibility(View.VISIBLE);
			left.setOnClickListener(this);

			if (leftDrawable == R.drawable.title_menu) {
				isShowNum = true;
			} else {
				isShowNum = false;
			}
		} else {
			left.setVisibility(View.GONE);
		}

		if (null != rightDrawable) {
			right.setBackgroundResource(R.drawable.title_button);
			right.setImageResource(rightDrawable);
			right.setVisibility(View.VISIBLE);
			right.setOnClickListener(this);
		} else {
			right.setVisibility(View.GONE);
		}
	}

	public void setContent(int contentLayout) {
		if (0 < contentLayout) {
			layout.addView(LayoutInflater.from(this).inflate(contentLayout,
					null));
			// ((ViewGroup) layout.getParent()).removeView(layout);
			// menus = new MenuPage(this);
			// slide = new SlideMenuView(this, menus, layout);
			// slide.setGap(layout.findViewById(R.id.left), 15);
			// setContentView(slide);
		}
	}

	public void setContent(View v) {
		if (null != v) {
			layout.addView(v);
			// ((ViewGroup) layout.getParent()).removeView(layout);
			// menus = new MenuPage(this);
			// slide = new SlideMenuView(this, menus, layout);
			// slide.setGap(layout.findViewById(R.id.left), 15);
			// setContentView(slide);
		}
	}

	protected void showHeaderProgress() {
		goSearch.setVisibility(View.GONE);
		t_load.setVisibility(View.GONE);
		header_progress.setVisibility(View.VISIBLE);
	}

	protected void hintHeaderProgress() {
		header_progress.setVisibility(View.GONE);
		if (showSearch) {
			goSearch.setVisibility(View.VISIBLE);
		} else {
			t_load.setVisibility(View.VISIBLE);
		}
	}

	public void showTip(String text) {
		T.show(getApplicationContext(), text);
	}

	protected void setTitle(String text) {
		title.setText(text);
	}

	protected void onLeftClick(View v) {

	}

	protected void onRefreshClick(View v) {

	}

	protected void onRightClick(View v) {

	}

	// protected void setNum(int[] nums) {
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// switch (event.getKeyCode()) {
	// case KeyEvent.KEYCODE_BACK:
	// if (slide.isMenuOpen()) {
	// slide.sliding(menus);
	// // exitFlag = false;
	// return true;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// slide.sliding(menus);
	// return false;
	// }

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_rihgt_out);
	}
}