package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.FriendsIdAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;

public class FriendsIdActivity extends BasicActionBarActivity implements
		OnSearchListener, OnClearListener {
	private static final String TAG = "FriendsIdActivity";
	private SearchBar sBar;
	private RefreshListView listView;
	private String groupId;
	private ArrayList<GroupLeaguerBean> objectList;
	private ArrayList<GroupLeaguerBean> allObjectList;
	private FriendsIdAdapter friendsIdAdapter;
	private int startRecord = 0;
	private int maxResults = 20;

	private boolean isLoadMore = true;
	private boolean isFrist = true;

	// private ArrayList<GroupLeaguerBean> selectedList;
	// private ArrayList<Integer> selectedIndex;
	private String[] friendsId;

	private LoadFriendsTask task;

	private boolean searchState;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.friends_id_layout);
		// setTitle("邀请朋友", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("邀请朋友");
		init();
		// spdDialog.showProgressDialog("正在加载中...");
		startTask(1, friendsId);
	}

	private void init() {
		objectList = new ArrayList<GroupLeaguerBean>();
		allObjectList = new ArrayList<GroupLeaguerBean>();
		// selectedList = new ArrayList<GroupLeaguerBean>();
		// selectedIndex = new ArrayList<Integer>();
		TextView btn = (TextView) findViewById(R.id.ok);
		btn.setOnClickListener(this);

		getIntentMes();
		listView = (RefreshListView) findViewById(R.id.friends_listView);
		friendsIdAdapter = new FriendsIdAdapter(FriendsIdActivity.this,
				allObjectList, listView);
		listView.setAdapter(friendsIdAdapter);
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				resetStartRecord();
				if (!searchState) {
					startTask(1, friendsId);
				} else {
					startTask(3, friendsId);
				}
			}
		});
		listView.setonLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				if (isFrist) {
					if (isLoadMore) {
						if (!searchState) {
							startTask(1, friendsId);
						} else {
							startTask(3, friendsId);
						}
						isFrist = false;
					}
				} else {
					isFrist = true;
				}
			}
		});
		sBar = (SearchBar) findViewById(R.id.search_bar);
		sBar.setOnSearchListener(this);
		sBar.setOnClearListener(this);
		sBar.setVisibility(View.GONE);
		listView.requestFocus();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ok:
			HashMap<Integer, GroupLeaguerBean> map = friendsIdAdapter.getMap();
			int size = map.size();
			if (size > 0) {
				friendsId = new String[size];
				int i = 0;
				for (Iterator<?> iterator = map.keySet().iterator(); iterator
						.hasNext();) {
					friendsId[i] = (Integer) iterator.next() + "";
					i++;
				}
				spdDialog.showProgressDialog("正在邀请中...");
				startTask(2, friendsId);
			} else {
				showTip("请先选择朋友");
			}
			break;
		}
	}

	private void getIntentMes() {
		Bundle bundle = getIntent().getExtras();
		groupId = bundle.getString("groupId");
	}

	private void startTask(int label, String[] ids) {
		task = new LoadFriendsTask(label, ids);
		task.execute();
	}

	// private String key() {
	// return sBar.getKeyWords();
	// }

	private void onSuccess(int num) {
		Bundle b = new Bundle();
		b.putInt(BundleKey.SIZE, num);
		try {
			b.putInt(BundleKey.ID_GROUP, Integer.valueOf(groupId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleReceiver.sendBoardcast(this, SimpleReceiver.RECEIVER_ADD_MEMBER,
				b);
	}

	class LoadFriendsTask extends AsyncTask<String, Integer, MCResult> {

		private int label;
		private String[] friendsId;

		public LoadFriendsTask(int label, String[] friendsId) {
			this.label = label;
			this.friendsId = friendsId;
			if (label == 1 || label == 3)
				if (startRecord == 0) {
					listView.refreshing();
				} else {
					listView.showLoadFooter();
				}
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if (label == 1) {
					result = APIRequestServers.inviteMemberList(
							FriendsIdActivity.this, groupId, startRecord + "",
							maxResults + "", false);
				} else if (label == 2) {
					result = APIRequestServers.addLeaguer(
							FriendsIdActivity.this, groupId, friendsId, null);
				} else if (label == 3) {
					result = APIRequestServers
							.searchInviteMemberListForAndroid(
									FriendsIdActivity.this, groupId, key,
									startRecord + "", maxResults + "", false);
				}
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
			listView.onRefreshComplete();
			spdDialog.cancelProgressDialog(null);
			if (label == 1 || label == 3)
				if (startRecord == 0) {
					listView.refreshed();
				} else {
					listView.showFinishLoadFooter();
				}
			if (label == 1 || label == 3) {
				if (null != mcResult) {
					if (1 != mcResult.getResultCode()) {
						showTip(T.ErrStr);
					}
					if (null != mcResult && 1 == mcResult.getResultCode()) {
						objectList = (ArrayList<GroupLeaguerBean>) mcResult
								.getResult();
						if (objectList == null || 0 == objectList.size()) {
							isLoadMore = false;
						} else {
							findViewById(R.id.posters).setVisibility(
									View.VISIBLE);
							if (0 == startRecord) {
								allObjectList.clear();
							}
							allObjectList.addAll(objectList);
							friendsIdAdapter.notifyDataSetChanged();

							startRecord = allObjectList.size();
							if (objectList.size() < 20) {
								isLoadMore = false;
							}
							L.d(TAG, "objectList  =" + objectList.size());
						}
					} else {
						showTip("加载失败");
					}
				} else {
					showTip("加载失败");
				}
			} else if (label == 2) {
				try {
					JSONObject person = new JSONObject(mcResult.getResult()
							.toString());
					int RESULT = person.getInt("RESULT");
					if (RESULT == 1) {
						HashMap<Integer, GroupLeaguerBean> map = friendsIdAdapter
								.getMap();
						// for (Iterator<?> iterator = map.keySet().iterator();
						// iterator
						// .hasNext();) {
						// allObjectList.remove(map.get(iterator.next()));
						// }
						// friendsIdAdapter.clearIndexList();
						// friendsIdAdapter.notifyDataSetChanged();
						onSuccess(map.size());
						finish();
					} else {
						showTip("圈人失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	String key;

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		MenuItem mi = menu.findItem(R.id.action_search);
		searchView = (SearchView) mi.getActionView();
		mi.setVisible(true);
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
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				key = s;
				searchState = true;
				resetStartRecord();
				startTask(3, null);
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
				clear();
				return false;
			}
		});

		// menu.findItem(R.id.action_send).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
			} else {
				finish();
			}
			return true;
		case R.id.action_send:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void clear() {
		searchState = false;
		isLoadMore = true;
		resetStartRecord();
		startTask(1, null);
	}

	@Override
	public void onSearch(String keyWords) {
		searchState = true;
		resetStartRecord();
		startTask(3, null);
	}

	@Override
	public void onClear(String keyWords) {
		searchState = false;
		resetStartRecord();
		startTask(1, null);
	}

	private void resetStartRecord() {
		startRecord = 0;
	}

}
