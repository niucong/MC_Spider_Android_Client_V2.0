package com.datacomo.mc.spider.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.umeng.analytics.MobclickAgent;

public class FindFriendActivity extends BasicMenuActivity {
	private final int CHOOSEFRIEND = 6;
	private LinearLayout find1, find2, find3;
	// private SlideMenuView slide;
	// private MenuPage menus;
	private Type mType;
	private String mId_Group;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "4");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_ADDFRIEND;
		titleName = "添加朋友";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		// setContent(R.layout.layout_find_friend);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_find_friend, null);
		fl.addView(rootView);

		setView(rootView);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// slide.sliding(menus);
	// return false;
	// }
	//
	// @Override
	// public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
	// {
	// slide.sliding(menus);
	// return super.onCreateOptionsMenu(menu);
	// }

	private void setView(View rootView) {
		Bundle bundle = getBundle();
		if (null != bundle) {
			mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		} else {
			return;
		}
		find1 = (LinearLayout) rootView.findViewById(R.id.find1);
		find2 = (LinearLayout) rootView.findViewById(R.id.find2);
		find3 = (LinearLayout) rootView.findViewById(R.id.find3);
		find1.setOnClickListener(this);
		find2.setOnClickListener(this);
		find3.setOnClickListener(this);
		TextView text = null;
		switch (mType) {
		case ADDFRIEND:
			text = (TextView) rootView.findViewById(R.id.find1_txt_text);
			text.setVisibility(View.VISIBLE);
			text.setText("输入朋友的手机号，把TA加为优优工作圈朋友");
			text = (TextView) rootView.findViewById(R.id.find2_txt_text);
			text.setVisibility(View.VISIBLE);
			text.setText("输入朋友的手机号查找并添加");
			find3.setVisibility(View.GONE);
			break;
		case ADDFRIENDTOGROUP:
			text = (TextView) rootView.findViewById(R.id.find1_txt_text);
			text.setVisibility(View.VISIBLE);
			text.setText("输入朋友的手机号，把TA加为优优工作圈朋友");
			text = (TextView) rootView.findViewById(R.id.find2_txt_text);
			text.setVisibility(View.VISIBLE);
			text.setText("从您的手机通讯录中选择朋友添加");
			find3.setVisibility(View.VISIBLE);
			mId_Group = bundle.getString(BundleKey.ID_GROUP);
			break;
		default:
			break;
		}
		// menus = new MenuPage(this);
		// ((ViewGroup) layout.getParent()).removeView(layout);
		// slide = new SlideMenuView(this, menus, layout);
		// slide.setGap(layout.findViewById(R.id.left), 15);
		// setContentView(slide);
		// setTitle("添加朋友", R.drawable.title_menu, R.drawable.title_home);
	}

	private Bundle getBundle() {
		Intent intent = getIntent();
		if (null != intent)
			return intent.getExtras();
		return null;

	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// closeMenuPage(mType);
	// }

	public void closeMenuPage(Type type) {
		if (type == Type.ADDFRIEND) {
			TextView text = null;
			text = (TextView) findViewById(R.id.find1_txt_text);
			text.setText("输入朋友的手机号，把TA加为优优工作圈朋友");
			text = (TextView) findViewById(R.id.find2_txt_text);
			text.setText("输入朋友的手机号查找并添加");
			find3.setVisibility(View.GONE);
		}
		// slide.close();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.find1:
			LogicUtil.enter(this, Find1Activity.class, getBundle(), 0);
			break;
		case R.id.find2:
			LogicUtil.enter(this, FindResult2Activity.class, getBundle(), 0);
			break;
		case R.id.find3:
			LogicUtil.enter(this, FriendsChooserActivity.class, null,
					CHOOSEFRIEND);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		try {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
			menu.findItem(R.id.action_message).setVisible(drawerOpen);
			menu.findItem(R.id.action_write).setVisible(drawerOpen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 != RESULT_OK)
			return;
		switch (arg0) {
		case CHOOSEFRIEND:
			String[] receiveFriendIds = arg2.getStringArrayExtra("ids");
			if (null != receiveFriendIds && receiveFriendIds.length > 0) {
				spdDialog.showProgressDialog("正在添加中...");
				new AddFriendTask().execute(receiveFriendIds,
						new String[] { mId_Group });
			}
			break;
		}
	}

	class AddFriendTask extends AsyncTask<Object, Integer, MCResult> {

		public AddFriendTask() {
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.addFriendsToGroup(App.app,
						(String[]) params[0], (String[]) params[1], "false");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			T.show(App.app, "已添加！");

		}
	}

	@Override
	protected void refresh() {
		// TODO
	}
}
