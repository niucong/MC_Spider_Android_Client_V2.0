package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;
import com.datacomo.mc.spider.android.adapter.FriendClubAdapter;
import com.datacomo.mc.spider.android.adapter.FriendsAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFriendBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.MyLetterListView;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class FriendGroupActivity extends BasicMenuActivity implements
		OnTabClickListener, OnSearchListener, OnClearListener,
		OnLoadMoreListener, OnRefreshListener {

	// 声明静态TAG
	private String TAG = "FriendGroupActivity";
	// 声明变量
	private int cut;
	private ArrayList<FriendGroupBean> clubs;
	private ArrayList<FriendBean> friends;
	private ArrayList<FriendBean> schResults;
	// 声明引用类
	private FriendsAdapter fAdapter;
	private FriendClubAdapter tAdapter;
	// 声明组件
	private TabLinearLayout mTabLinearLayout;
	private MyLetterListView letterView;
	private RefreshListView listview;
	private TextView tv_num;
	private GridView faces;
	private PopupWindow facePPW;
	private SearchBar sBar;
	private GridView grid;
	private LinearLayout headView;

	private String[] tabContent;

	private loadFriendTask task;
	private LoadClubTask task2;

	private String trendId_club, trendId_friend, searchRecord;
	private int screenWidth;

	private String key;

	private boolean searchState;

	public static boolean isRefreashClub = false;
	private boolean isRefreash = false;

	private View rootView;

	private String num = " ";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_FRIENDS;
		titleName = "朋友圈";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_friend_group, null);
		fl.addView(rootView);

		findViews(rootView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "3");
		L.d(TAG, "onStart");
	}

	private void findViews(View rootView) {
		// service = FriendListService.getService(this);
		searchRecord = "0";
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;

		letterView = (MyLetterListView) rootView.findViewById(R.id.letterView);
		letterView.setVisibility(View.GONE);

		sBar = (SearchBar) rootView.findViewById(R.id.searchs);
		sBar.setOnSearchListener(this);
		sBar.setOnClearListener(this);
		sBar.setHint("名字/手机号");
		sBar.setVisibility(View.GONE);

		tv_num = (TextView) findViewById(R.id.num_tv);
		tv_num.setText("共有" + num + "个朋友");

		headView = (LinearLayout) rootView.findViewById(R.id.progress_layout);
		headView.setVisibility(View.GONE);
		listview = (RefreshListView) rootView.findViewById(R.id.listview);
		listview.setonRefreshListener(this);
		listview.setonLoadMoreListener(this);

		initFaces();

		mTabLinearLayout = (TabLinearLayout) rootView.findViewById(R.id.tabs);
		tabContent = new String[] { "全部朋友", "朋友圈" };
		mTabLinearLayout.changeText(tabContent);
		mTabLinearLayout.refresh(0);
		mTabLinearLayout.setOnTabClickListener(this);
		grid = (GridView) rootView.findViewById(R.id.table);

		try {
			friends = LocalDataService.getInstense().getLocFriends(this,
					LocalDataService.TXT_FRIEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (friends == null)
			friends = new ArrayList<FriendBean>();
		schResults = new ArrayList<FriendBean>();

		fAdapter = new FriendsAdapter(FriendGroupActivity.this, friends,
				listview, itemMenuListener);
		clubs = new ArrayList<FriendGroupBean>();
		tAdapter = new FriendClubAdapter(FriendGroupActivity.this, clubs,
				screenWidth);
		listview.setAdapter(fAdapter);
		grid.setAdapter(tAdapter);
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
		facePPW.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.nothing));
		facePPW.setAnimationStyle(R.style.midmenu_ani_bottom);
		facePPW.setFocusable(true);
		facePPW.setOutsideTouchable(true);

	}

	@Override
	protected void onRestart() {
		super.onRestart();

		if (isRefreashClub) {
			loadClub(cut, true);
			isRefreashClub = false;
			isRefreash = true;
		}
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		stopLoadFriendTask();
		stopLoadClubTask();
	}

	void loadInfo(int which, boolean isRefresh) {
		cut = which;
		stopLoadFriendTask();
		task = new loadFriendTask(which, isRefresh, searchState);
		task.execute();
	}

	void loadClub(int which, boolean isRefresh) {
		cut = which;
		stopLoadClubTask();
		task2 = new LoadClubTask(which, isRefresh);
		task2.execute();
		listview.showLoadFooter();
	}

	private void stopLoadFriendTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	private void stopLoadClubTask() {
		if (null != task2 && task2.getStatus() == AsyncTask.Status.RUNNING) {
			task2.cancel(true);
		}
	}

	/**
	 * 朋友个数
	 */
	class FriendNumTask extends AsyncTask<Object, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.friendNum(
						FriendGroupActivity.this, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (result != null && result.getResultCode() == 1) {
				num = (Integer) result.getResult() + "";
				tv_num.setText("共有" + num + "个朋友");
				// mTabLinearLayout.changeSpecilText(tabContent[0] + "(" + num
				// + ")", 0);
			}
		}
	}

	/**
	 * 获取朋友列表、搜索朋友
	 */
	class loadFriendTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh, isSearch;

		public loadFriendTask(int cut, boolean refresh, boolean search) {
			isRefresh = refresh;
			isSearch = search;
			setLoadingState(true);
			if (refresh) {
				listview.refreshing();
			} else {
				listview.showLoadFooter();
			}
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if (isRefresh) {
					searchRecord = "0";
					trendId_friend = "0";
				}
				if (isSearch) {
					result = APIRequestServers.searchFriendsForIPhone(
							FriendGroupActivity.this, key, searchRecord, "20",
							"true");
				} else {
					result = APIRequestServers
							.friendList(FriendGroupActivity.this, "0",
									trendId_friend, "30");
				}
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (isRefresh) {
				listview.refreshed();
			} else {
				listview.showFinishLoadFooter();
			}
			if (null != mcResult && 1 == mcResult.getResultCode()) {
				if (isSearch) {
					List<FriendBean> objects = null;
					MapFriendBean requests = (MapFriendBean) mcResult
							.getResult();
					if (null != requests) {
						objects = requests.getFRIEND_LIST();
					}
					if (isRefresh || searchRecord == null
							|| "".equals(searchRecord)
							|| "0".equals(searchRecord)) {
						schResults.clear();
					}

					if (null == objects || 0 == objects.size()) {
						if (schResults.size() == 0) {
							showTip("没有搜索到相应结果！");
						} else {
							showTip("没有更多数据！");
						}
					} else {
						schResults.addAll(objects);
						searchRecord = String.valueOf(schResults.size());
						fAdapter.notifyDataSetChanged();

						L.d(TAG, "loadFriendTask oSize=" + objects.size()
								+ ",sSize=" + schResults.size());
					}
				} else {
					if (isRefresh || null == trendId_friend
							|| "".equals(trendId_friend)) {
						friends.clear();
					}
					@SuppressWarnings("unchecked")
					List<FriendBean> objects = (List<FriendBean>) mcResult
							.getResult();
					if (null == objects) {
						if (friends.size() == 0) {
							showTip("没有相应数据！");
						} else {
							showTip("没有更多数据！");
						}
					} else {
						friends.addAll(objects);
						trendId_friend = String.valueOf(friends.size());
						fAdapter.notifyDataSetChanged();
					}
				}

				if (isRefresh) {
					listview.setSelection(0);
				}
			} else {
				showTip(T.ErrStr);
			}
			listview.onRefreshComplete();
			searchState = false;
		}
	}

	/**
	 * 朋友圈
	 */
	class LoadClubTask extends AsyncTask<String, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public LoadClubTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			this.isRefresh = isRefresh;

			setLoadingState(true);
			listview.refreshing();
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers
						.friendGroupList(FriendGroupActivity.this);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			listview.refreshed();
			listview.showFinishLoadFooter();
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (cur == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					if (isRefresh || null == trendId_club
							|| "".equals(trendId_club)) {
						clubs.clear();
					}
					List<FriendGroupBean> objects = (List<FriendGroupBean>) mcResult
							.getResult();
					if (null == objects || objects.size() == 0) {
					} else {
						clubs.addAll(objects);
						trendId_club = String.valueOf(friends.size());
						tAdapter.notifyDataSetChanged();
					}
				}

				if (isRefresh) {
					listview.setSelection(0);
				}
			}
			headView.setVisibility(View.GONE);
			listview.onRefreshComplete();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);
		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);

		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
						.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
				if (mQueryTextView.isShown()
						&& "".equals(mQueryTextView.getText().toString())) {
					cv.setVisibility(View.GONE);
					showBack();
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				key = s;
				searchState = true;
				fAdapter = new FriendsAdapter(FriendGroupActivity.this,
						schResults, listview, itemMenuListener);
				fAdapter.setSearch(true);
				listview.setAdapter(fAdapter);
				loadInfo(cut, true);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
				} else {
					cv.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				searchState = false;
				stopLoadFriendTask();
				fAdapter = new FriendsAdapter(FriendGroupActivity.this,
						friends, listview, itemMenuListener);
				listview.setAdapter(fAdapter);
				showMenu();
				return false;
			}
		});

		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			loadInfo(cut, true);
			new FriendNumTask().execute();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		menu.findItem(R.id.action_message).setVisible(drawerOpen);
		menu.findItem(R.id.action_write).setVisible(drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		listview.onRefreshComplete();
		switch (cut) {
		case 0:
			searchView.setVisibility(View.VISIBLE);
			tv_num.setVisibility(View.VISIBLE);
			showList();
			if (friends.size() == 0 || isRefreash) {
				isRefreash = false;
				loadInfo(cut, true);
			}
			break;
		case 1:
			searchView.setVisibility(View.GONE);
			tv_num.setVisibility(View.GONE);
			showTable();
			if (clubs.size() == 0) {
				headView.setVisibility(View.VISIBLE);
				loadClub(cut, true);
			}
			break;
		default:
			break;
		}
	}

	private void showList() {
		// signText.setVisibility(View.GONE);
		// letterView.setVisibility(View.VISIBLE);
		listview.setVisibility(View.VISIBLE);
		// sBar.setVisibility(View.VISIBLE);
		grid.setVisibility(View.GONE);
	}

	private void showTable() {
		// signText.setVisibility(View.GONE);
		// letterView.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		sBar.setVisibility(View.GONE);
		grid.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		fAdapter.ppwDismiss();
	}

	@Override
	public void onClear(String keyWords) {
		searchState = false;
		stopLoadFriendTask();
		fAdapter = new FriendsAdapter(FriendGroupActivity.this, friends,
				listview, itemMenuListener);
		listview.setAdapter(fAdapter);
		fAdapter.notifyDataSetChanged();
		listview.setSelection(0);
	}

	@Override
	public void onSearch(String keyWords) {
		L.i(TAG, "onsearch!");
		searchState = true;
		fAdapter = new FriendsAdapter(FriendGroupActivity.this, schResults,
				listview, itemMenuListener);
		listview.setAdapter(fAdapter);
		loadInfo(cut, true);
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
					new MemberContactUtil(FriendGroupActivity.this)
							.callPhone(number);

					new Thread() {
						public void run() {
							FriendListService.getService(
									FriendGroupActivity.this)
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
				facePPW.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
				break;
			case R.id.menu_friend_mail: // 私信
				Bundle secret = new Bundle();
				secret.putString("memberId", String.valueOf(bean.getId()));
				secret.putString("name", bean.getName());
				secret.putString("head", bean.getPath());
				LogicUtil.enter(FriendGroupActivity.this, QChatActivity.class,
						secret, false);
				break;
			case R.id.content:
			case R.id.head_img: // 主页
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", String.valueOf(bean.getId()));
				b.putString("name", bean.getName());
				LogicUtil.enter(FriendGroupActivity.this, HomePgActivity.class,
						b, false);
				break;
			}
		}
	};

	class GreetTask extends AsyncTask<Void, Integer, MCResult> {
		private int greetId;
		private String mberId;

		public GreetTask(int greetId, String memberId) {
			listview.refreshing();
			this.greetId = greetId;
			mberId = memberId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.greetMamber(
						FriendGroupActivity.this, mberId, "0",
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
		if (0 == cut) {
			loadInfo(cut, false);
		}
	}

	@Override
	public void onRefresh() {
		if (0 == cut) {
			loadInfo(cut, true);
		}
	}

	@Override
	protected void refresh() {
		if (cut == 0) {
			loadInfo(cut, true);
		} else if (1 == cut) {
			loadClub(cut, true);
		}
	}

}
