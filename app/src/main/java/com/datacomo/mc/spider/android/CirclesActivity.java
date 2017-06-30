package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.GridView;

import com.datacomo.mc.spider.android.adapter.CircleAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;

public class CirclesActivity extends BasicActionBarActivity {
	private GridView grid;
	private ArrayList<String> ids;
	private ArrayList<String> names;
	private ArrayList<String> urls;
	private String groupName = "";

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_circle_group);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			ids = b.getStringArrayList("ids");
			names = b.getStringArrayList("names");
			urls = b.getStringArrayList("urls");
			int type = b.getInt("type");
			groupName = b.getString("groupName");
			if (null != groupName) {
				if (0 == type) {
					// setTitle(groupName + "-下级圈子", R.drawable.title_fanhui,
					// R.drawable.title_home);
					ab.setTitle("-下级圈子");
				} else if (1 == type) {
					// setTitle(groupName + "-合作圈子", R.drawable.title_fanhui,
					// R.drawable.title_home);
					ab.setTitle("-合作圈子");
				}
			}
		}
		grid = (GridView) findViewById(R.id.table);
		if (ids != null && names != null && urls != null) {
			int screenWidth = BaseData.getScreenWidth();
			;
			CircleAdapter adapter = new CircleAdapter(this, ids, names, urls,
					grid, screenWidth);
			grid.setAdapter(adapter);
		} else {
			showTip(T.ErrStr);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

}
