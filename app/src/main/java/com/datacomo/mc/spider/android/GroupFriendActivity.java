package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;
import com.datacomo.mc.spider.android.adapter.FriendsAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class GroupFriendActivity extends BasicActionBarActivity implements
		OnRefreshListener, OnLoadMoreListener {
	private final String TAG = "GroupFriendActivity";

	// 声明变量
	private final int CHOOSERECEIVEFRIENDS = 0;
	// private final int CHOOSEFRIENDS = 1;
	private ArrayList<FriendBean> friends;
	// 声明引用类
	private FriendsAdapter fAdapter;
	// 声明组件
	private RefreshListView listView;
	private GridView faces;
	private PopupWindow facePPW;
	private loadFriendTask task;

	private String groupId;
	private String groupName;
	private int num;
	private boolean isLoading;
	// private TransitionBean mCurrentBean;
	private LinearLayout llayout_AddFriendBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_group_friend);

		Bundle b = getIntent().getExtras();
		if (null != b) {
			groupId = b.getString("id_Group");
			groupName = b.getString("name_Group");
			num = b.getInt("num_Total");
		} else {
			finish();
		}
		findViews();
	}

	private void findViews() {
		// setTitle(groupName + "（" + num + "）", R.drawable.title_fanhui, true,
		// null, null);// R.drawable.icon_share
		ab.setTitle(groupName + "（" + num + "）");
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setFastScrollEnabled(true);
		listView.setonLoadMoreListener(this);
		listView.setonRefreshListener(this);
		initFaces();

		friends = new ArrayList<FriendBean>();
		fAdapter = new FriendsAdapter(GroupFriendActivity.this, friends,
				listView, itemMenuListener);
		listView.setAdapter(fAdapter);
		llayout_AddFriendBox = (LinearLayout) findViewById(R.id.layout_group_friend_llayout_addfriendbox);
		llayout_AddFriendBox.setOnClickListener(this);
		loadInfo(false);
	}

	private void initFaces() {
		faces = new GridView(this);
		faces.setNumColumns(5);
		faces.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		faces.setSelector(R.drawable.nothing);
		faces.setAdapter(new FaceTableAdapter(this));
		faces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				spdDialog.showProgressDialog("正在处理中...");
				new GreetTask(arg2, fAdapter.getCurId()).execute();
			}
		});
		facePPW = new PopupWindow(faces, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		facePPW.setBackgroundDrawable(res.getDrawable(R.drawable.nothing));
		facePPW.setAnimationStyle(R.style.midmenu_ani_bottom);
		facePPW.setFocusable(true);
		facePPW.setOutsideTouchable(true);

	}

	OnClickListener itemMenuListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final TransitionBean bean = (TransitionBean) v.getTag();
			fAdapter.ppwDismiss();
			switch (v.getId()) {
			case R.id.menu_friend_phone: // 电话
				String number = bean.getPhone();
				if (CharUtil.isValidPhone(number)) {
					new MemberContactUtil(GroupFriendActivity.this)
							.callPhone(number);

					new Thread() {
						public void run() {
							FriendListService.getService(
									GroupFriendActivity.this)
									.saveContactTime(
											new String[] { String.valueOf(bean
													.getId()) });
						};
					}.start();
				} else {
					showTip("该用户未绑定手机号！");
				}
				break;
			case R.id.menu_friend_greet: // 招呼
				facePPW.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.menu_friend_mail: // 私信
				Bundle secret = new Bundle();
				secret.putString("memberId", String.valueOf(bean.getId()));
				secret.putString("name", bean.getName());
				secret.putString("head", bean.getPath());
				LogicUtil.enter(GroupFriendActivity.this, QChatActivity.class,
						secret, false);
				break;
			case R.id.content:
			case R.id.head_img: // 主页
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", String.valueOf(bean.getId()));
				b.putString("name", bean.getName());
				LogicUtil.enter(GroupFriendActivity.this, HomePgActivity.class,
						b, false);
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		fAdapter.ppwDismiss();
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		stopLoadFriendTask();
	}

	void loadInfo(boolean isRefresh) {
		stopLoadFriendTask();
		task = new loadFriendTask(isRefresh);
		task.execute();
	}

	private void stopLoadFriendTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_group_friend_llayout_addfriendbox:
			Bundle bundle = new Bundle();
			bundle.putSerializable(BundleKey.TYPE_REQUEST,
					Type.ADDFRIENDTOGROUP);
			bundle.putString(BundleKey.ID_GROUP, groupId);
			LogicUtil.enter(this, FindGroupFriendActivity.class, bundle, false);
			break;
		}
	}

	/**
	 * 朋友
	 */
	class loadFriendTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public loadFriendTask(boolean refresh) {
			isRefresh = refresh;
			isLoading = true;
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mc = null;
			int start = 0;
			try {
				if (!isRefresh) {
					start = friends.size();
				}
				mc = APIRequestServers.friendListByGroup(
						GroupFriendActivity.this, String.valueOf(groupId),
						String.valueOf(start), "30");

			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult mc) {
			super.onPostExecute(mc);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null == mc || 1 != mc.getResultCode()) {
				showTip(T.ErrStr);
				return;
			}
			@SuppressWarnings("unchecked")
			ArrayList<FriendBean> objects = (ArrayList<FriendBean>) mc
					.getResult();
			if (null == objects || 0 == objects.size()) {
				showTip("最后一页");
			} else {
				if (isRefresh) {
					friends.clear();
				}
				friends.addAll(objects);
				fAdapter.notifyDataSetChanged();
			}
			listView.onRefreshComplete();
			isLoading = false;
		}
	}

	/**
	 * 朋友
	 */
	class RequestTask extends AsyncTask<Object, Integer, MCResult> {
		private int mType;
		private Object[] mParams;

		public RequestTask(int type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			switch (mType) {
			case 0:
				try {
					mcResult = APIRequestServers.recommendFriend(App.app, null,
							(String[]) mParams[0], null, null,
							(String[]) mParams[1], null, null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// case 1:
			// try {
			// mcResult = APIRequestServers.recommendFriend(App.app,
			// (String[]) mParams[0], null, null, null,
			// (String[]) mParams[1], null, null, null);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// break;
			}

			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (null == result || result.getResultCode() != 1) {
				showTip(T.ErrStr);
				return;
			}
			switch (mType) {
			case 0:
			case 1:
				try {
					showTip("已分享！");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onSecondClick(View v) {
	// super.onSecondClick(v);
	// loadInfo(true);
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// Bundle bundle = new Bundle();
	// bundle.putInt("type", 5);
	// bundle.putString("sendInfo", "");
	// LogicUtil.enter(this, FriendsChooserActivity.class, bundle,
	// CHOOSERECEIVEFRIENDS);
	// }

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 != RESULT_OK)
			return;
		switch (arg0) {
		case CHOOSERECEIVEFRIENDS:
			String[] receiveFriendIds = arg2.getStringArrayExtra("ids");
			if (null != receiveFriendIds && receiveFriendIds.length > 0) {
				spdDialog.showProgressDialog("推荐中...");
				L.d(TAG, "groupId:" + groupId);
				new RequestTask(0).execute(new String[] { groupId },
						receiveFriendIds);
			}
			break;
		// case CHOOSEFRIENDS:
		// String[] shareFriendIds = arg2.getStringArrayExtra("ids");
		// if (null != shareFriendIds && shareFriendIds.length > 0) {
		// spdDialog.showProgressDialog("推荐中...");
		// L.d(TAG, "groupId:" + groupId);
		// new RequestTask(1).execute(shareFriendIds,
		// new String[] { String.valueOf(mCurrentBean.getId()) });
		// }
		// break;
		}

	}

	class GreetTask extends AsyncTask<Void, Integer, MCResult> {
		private int greetId;
		private String mberId;

		public GreetTask(int greetId, String memberId) {
			this.greetId = greetId;
			mberId = memberId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.greetMamber(
						GroupFriendActivity.this, mberId, "0",
						String.valueOf(GreetUtil.GREET_CONFIG_ID[greetId]));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null != mcResult && 1 == mcResult.getResultCode()) {
				showTip("打招呼成功！");
			} else {
				showTip(T.ErrStr);
			}

			facePPW.dismiss();
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (facePPW.isShowing()) {
				facePPW.dismiss();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadInfo(false);

	}

	@Override
	public void onRefresh() {
		loadInfo(true);
	}

}
