package com.datacomo.mc.spider.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.application.ScreenManager;

public class SquareManActivity extends BasicActionBarActivity {
	// private static final String TAG = "SquareManActivity";

	private LinearLayout phone, finds;
	private String groupId;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_squareman);
		// setTitle("邀请朋友", R.drawable.title_fanhui, null);
		ab.setTitle("邀请朋友");
		setView();
	}

	private void setView() {
		groupId = getIntent().getStringExtra("groupId");
		phone = (LinearLayout) findViewById(R.id.phone);
		finds = (LinearLayout) findViewById(R.id.finds);
		phone.setOnClickListener(this);
		finds.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.phone:
			Intent intent = new Intent(SquareManActivity.this,
					PhoneActivity.class);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			finish();
			break;
		case R.id.finds:
			Intent intent2 = new Intent(SquareManActivity.this,
					FriendsIdActivity.class);
			intent2.putExtra("groupId", groupId);
			startActivity(intent2);
			finish();
			break;
		}
	}

}
