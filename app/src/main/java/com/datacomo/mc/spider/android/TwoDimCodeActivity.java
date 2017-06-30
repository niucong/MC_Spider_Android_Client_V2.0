package com.datacomo.mc.spider.android;

import android.os.Bundle;

import com.datacomo.mc.spider.android.application.ScreenManager;

public class TwoDimCodeActivity extends BasicActionBarActivity {

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.two_dimension_code);

		// setTitle("扫描二维码下载", R.drawable.title_fanhui, null);
		ab.setTitle("扫描二维码下载");
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }
}