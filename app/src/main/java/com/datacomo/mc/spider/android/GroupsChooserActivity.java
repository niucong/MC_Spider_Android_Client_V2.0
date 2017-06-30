package com.datacomo.mc.spider.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.GroupsChooserAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.service.UpdateGroupListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.view.MyLetterListView;
import com.datacomo.mc.spider.android.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.datacomo.mc.spider.android.view.SearchBar2.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar2.OnSearchListener;
import com.datacomo.mc.spider.android.view.SignTextView;

public class GroupsChooserActivity extends BasicActionBarActivity implements
		OnSearchListener, OnClearListener, OnTouchingLetterChangedListener {
	private String TAG = "GroupsChooserActivity";

	public static final int RESCODE = 21;

	private RelativeLayout layout;
	private ListView listView, lv_search;
	private ArrayList<GroupEntity> groups, allGroups, sGroups;
	private GroupsChooserAdapter adapter, sAdapter;
	public static LinkedHashMap<String, GroupEntity> chooseIds;
	private GroupListService service;
	private MyLetterListView letterView;
	private SignTextView signText;

	private loadGroupsTask loadTask;

	public final String TITLE = "选择交流圈";

	private String mText = "确定";

	AlertDialog.Builder builder;

	private int exceptId;

	@Override
	protected void onDestroy() {
		reHandler.removeCallbacksAndMessages(null);
		BaseData.hideKeyBoard(this);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_chooser);
		// setTitle(TITLE, R.drawable.title_fanhui, true, null, null);
		ab.setTitle(TITLE);

		List<GroupEntity> datas = null;
		try {
			Bundle b = getIntent().getExtras();
			try {
				mText = b.getString("btnString", "确定");
			} catch (NoSuchMethodError e) {
			}
			exceptId = b.getInt("id", 0);
			datas = (List<GroupEntity>) b.getSerializable("datas");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != datas && datas.size() > 0) {
			chooseIds = GroupEntity.listToMap(datas);
		} else {
			chooseIds = new LinkedHashMap<String, GroupEntity>();
		}
		findViews();

	}

	private void findViews() {
		service = GroupListService.getService(GroupsChooserActivity.this);
		layout = (RelativeLayout) findViewById(R.id.layout);
		lv_search = (ListView) findViewById(R.id.listview_search);
		sGroups = new ArrayList<GroupEntity>();
		lv_search.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				L.i(TAG, "findViews onScrollStateChanged scrollState="
						+ scrollState);
				BaseData.hideKeyBoard(GroupsChooserActivity.this);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				L.d(TAG, "findViews onScroll firstVisibleItem="
						+ firstVisibleItem + ",visibleItemCount="
						+ visibleItemCount + ",totalItemCount="
						+ totalItemCount);
				// BaseData.hideKeyBoard(FriendsChooserActivity.this);
			}
		});

		letterView = (MyLetterListView) findViewById(R.id.letterView);
		letterView.setOnTouchingLetterChangedListener(this);
		signText = (SignTextView) findViewById(R.id.sign);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				BaseData.hideKeyBoard(GroupsChooserActivity.this);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		groups = new ArrayList<GroupEntity>();
		adapter = new GroupsChooserAdapter(GroupsChooserActivity.this, groups,
				listView, false, mText);
		listView.setAdapter(adapter);
		TextView btn = (TextView) findViewById(R.id.ok);
		btn.setOnClickListener(this);
		loadInfo(false);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ok:
			doChoose();
			break;
		}
	}

	private void searchGroup(ArrayList<GroupEntity> lists, String str) {
		if (null == lists || lists.size() == 0) {
			showTip("请先刷新获取圈子列表");
			return;
		}

		sGroups.clear();
		for (int i = 0; i < lists.size(); i++) {
			GroupEntity groupEntity = lists.get(i);
			if (groupEntity.isRecentCheck()) {
				continue;
			}
			String name = groupEntity.getName();
			int gId = 0;
			try {
				gId = Integer.valueOf(groupEntity.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			L.i(TAG, "searchGroup name=" + name);
			if (gId != 0 && gId != -1)
				if (name != null
						&& (name.toLowerCase().startsWith(str.toLowerCase()))) {
					if (!sGroups.contains(groupEntity))
						sGroups.add(groupEntity);
				} else {
					boolean isStrWith = false;

					String groupNamePy = groupEntity.getGroupNamePy();
					L.i(TAG, "searchGroup groupNamePy=" + groupNamePy);
					if (null == groupNamePy) {
						continue;
					}
					if (groupNamePy.contains(",")) {
						String[] pys = groupNamePy.split(",");
						int size = 0;
						if (pys != null) {
							size = pys.length;
							L.i(TAG, "searchGroup groupNamePy.Size=" + size);
							for (int j = 0; j < size; j++) {
								String py = pys[j];
								L.i(TAG,
										"searchGroup:" + j + ":"
												+ py.toLowerCase() + ";"
												+ str.toLowerCase());
								if (py != null
										&& (py.toLowerCase().startsWith(str
												.toLowerCase()))) {
									if (!sGroups.contains(groupEntity))
										sGroups.add(groupEntity);
									break;
								}
							}
						}
					} else {
						if (groupNamePy.toLowerCase().startsWith(
								str.toLowerCase())) {
							if (!sGroups.contains(groupEntity))
								sGroups.add(groupEntity);
						}
					}

					if (!isStrWith) {
						String groupNameJp = groupEntity.getGroupNameJp();
						L.i(TAG, "searchGroup groupNameJp=" + groupNameJp);
						if (groupNameJp.contains(",")) {
							String[] jps = groupNameJp.split(",");
							int size = 0;
							if (jps != null) {
								size = jps.length;
								L.i(TAG, "searchGroup groupNameJp.Size=" + size);
								for (int j = 0; j < size; j++) {
									String jp = jps[j];
									if (jp != null
											&& (jp.toLowerCase().startsWith(str
													.toLowerCase()))) {
										if (!sGroups.contains(groupEntity))
											sGroups.add(groupEntity);
										break;
									}
								}
							}
						} else {
							if (groupNameJp.toLowerCase().startsWith(
									str.toLowerCase())) {
								if (!sGroups.contains(groupEntity))
									sGroups.add(groupEntity);
							}
						}
					}
				}
		}
		sAdapter = new GroupsChooserAdapter(GroupsChooserActivity.this,
				sGroups, lv_search, false, mText);
		lv_search.setAdapter(sAdapter);
	}

	private void loadInfo(boolean isRefresh) {
		stopTask();
		loadTask = new loadGroupsTask(isRefresh);
		loadTask.execute();
	}

	private void stopTask() {
		if (null != loadTask
				&& loadTask.getStatus() == AsyncTask.Status.RUNNING) {
			loadTask.cancel(true);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler reHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				spdDialog.showProgressDialog("正在刷新圈子...");
				break;
			case 1:
				spdDialog.cancelProgressDialog(null);
				break;
			case 2:
				updateList();
				break;
			default:
				break;
			}
		};
	};

	boolean isUpdate = false;

	class loadGroupsTask extends
			AsyncTask<String, Integer, ArrayList<GroupEntity>> {

		private boolean isRefersh = false;

		public loadGroupsTask(boolean isRefersh) {
			this.isRefersh = isRefersh;
			setLoadingState(true);
		}

		@Override
		protected ArrayList<GroupEntity> doInBackground(String... params) {
			try {
				if (isRefersh) {
					if (allGroups != null)
						allGroups.clear();
					UpdateGroupListThread.updateGroupList(
							GroupsChooserActivity.this, null);
					allGroups = service.queryGroupListsByPy();
				} else {
					if (null == allGroups || allGroups.size() == 0) {
						allGroups = service.queryGroupListsByPy();
						if (allGroups == null || allGroups.size() == 0) {
							try {
								UpdateGroupListThread.updateGroupList(
										GroupsChooserActivity.this, null);
							} catch (Exception e) {
								e.printStackTrace();
							}
							allGroups = service.queryGroupListsByPy();
						} else {
							if (!isUpdate) {
								isUpdate = true;
								new Thread() {
									public void run() {
										try {
											UpdateGroupListThread
													.updateGroupList(
															GroupsChooserActivity.this,
															null);
											allGroups = service
													.queryGroupListsByPy();
											ArrayList<GroupEntity> recentEntities = service
													.queryGroupListsByContactTime();
											if (null != recentEntities) {
												allGroups.addAll(0,
														recentEntities);
											}
											reHandler.sendEmptyMessage(2);

											Object[] objects = APIRequestServers
													.commGroupList(
															GroupsChooserActivity.this,
															"", "0", "10",
															false);
											if ((Integer) objects[0] == 1) {
												@SuppressWarnings("unchecked")
												ArrayList<GroupEntity> lists = (ArrayList<GroupEntity>) objects[1];
												int size = lists.size();
												String[] ids = new String[size];
												for (int i = 0; i < size; i++) {
													ids[size - i - 1] = lists
															.get(i).getId()
															+ "";
												}
												service.saveContactTime(ids);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									};
								}.start();
							}
						}
					}
				}

				ArrayList<GroupEntity> recentEntities = service
						.queryGroupListsByContactTime();
				if (null != recentEntities) {
					allGroups.addAll(0, recentEntities);
				}
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
			return allGroups;
		}

		@Override
		protected void onPostExecute(ArrayList<GroupEntity> objects) {
			super.onPostExecute(objects);
			setLoadingState(false);
			if (null == allGroups || 0 == allGroups.size()) {
				showTip("获取交流圈失败");
			} else {
				updateList();
			}
		}
	}

	private void updateList() {
		groups.clear();
		groups.addAll(allGroups);
		adapter.notifyTags();
	}

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
				layout.setVisibility(View.GONE);
				lv_search.setVisibility(View.VISIBLE);

				searchGroup(adapter.getData(), s);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				layout.setVisibility(View.GONE);
				lv_search.setVisibility(View.VISIBLE);

				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
				} else {
					cv.setVisibility(View.VISIBLE);
					searchGroup(adapter.getData(), s);
				}
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				showAll();
				return false;
			}
		});

		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	private void showAll() {
		layout.setVisibility(View.VISIBLE);
		lv_search.setVisibility(View.GONE);
		// listView.setSelection(0);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				showAll();
			} else {
				setResult(RESULT_CANCELED);
				finish();
			}
			return true;
		case R.id.action_refresh:
			loadInfo(true);
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
				listView.setSelection(0);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void doChoose() {
		Type type = (Type) getIntent().getExtras().getSerializable(
				BundleKey.TYPE_REQUEST);
		if (adapter.getSelectedIds().length == 0) {
			finish();
			return;
		}

		Intent intent = getIntent();
		String shareTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT);
		String shareStr = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (shareStr == null || "".equals(shareStr)) {
			CharSequence localCharSequence = intent
					.getCharSequenceExtra(Intent.EXTRA_TEXT);
			if (localCharSequence != null)
				shareStr = localCharSequence.toString();
		}
		Uri shareUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if ((shareTitle != null && !"".equals(shareTitle))
				|| (shareStr != null && !"".equals(shareStr))
				|| shareUri != null)
			type = Type.STARTCREATGROUPTOPIC;

		if (null != type && type == Type.STARTCREATGROUPTOPIC) {
			Bundle bundle = new Bundle();
			// bundle.putSerializable("ids", adapter.getSelectedIds());
			bundle.putString(Intent.EXTRA_SUBJECT, shareTitle);
			bundle.putString(Intent.EXTRA_TEXT, shareStr);
			bundle.putParcelable(Intent.EXTRA_STREAM, shareUri);

			bundle.putSerializable("datas", (Serializable) GroupEntity
					.mapToArray(adapter.getSelected()));
			bundle.putSerializable(BundleKey.TYPE_REQUEST, type);
			LogicUtil.enter(this, CreateGroupTopicActivity.class, bundle, true);
		} else {
			Intent i = new Intent();
			i.putExtra("ids", adapter.getSelectedIds());
			i.putExtra(Intent.EXTRA_SUBJECT, shareTitle);
			i.putExtra(Intent.EXTRA_TEXT, shareStr);
			i.putExtra("datas", (Serializable) GroupEntity.mapToArray(adapter
					.getSelected()));
			i.putExtra("id", exceptId);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	@Override
	public void onSearch(String keyWords) {
		if (keyWords != null) {
			searchGroup(adapter.getData(), keyWords);
		}
	}

	@Override
	public void onClear(String keyWords) {
		listView.setSelection(0);
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// if ("#".equals(s)) {
		// listView.setSelection(0);
		// signText.showSign(s, 2000);
		// } else {
		for (int i = 0; i < adapter.getCount(); i++) {
			GroupEntity entity = (GroupEntity) adapter.getItem(i);
			if (entity.getId().equals(adapter.TAG)
					&& s.equals(entity.getName())) {
				listView.setSelection(i + listView.getHeaderViewsCount());
				signText.showSign(s, 2000);
			}
		}
		// }
	}
}
