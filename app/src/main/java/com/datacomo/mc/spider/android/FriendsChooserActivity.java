package com.datacomo.mc.spider.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.FriendsChooserAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.view.MyLetterListView;
import com.datacomo.mc.spider.android.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.datacomo.mc.spider.android.view.SearchBar2.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar2.OnSearchListener;
import com.datacomo.mc.spider.android.view.SignTextView;

public class FriendsChooserActivity extends BasicActionBarActivity implements
		OnSearchListener, OnClearListener, OnTouchingLetterChangedListener {
	private String TAG = "FriendsChooserActivity";
	public static final int RESCODE = 11;

	private RelativeLayout layout;
	private ListView listView, lv_search;
	private ArrayList<FriendSimpleBean> allMbers, mbers, sMbers;
	private FriendsChooserAdapter adapter, sAdapter;
	public static LinkedHashMap<String, FriendSimpleBean> oriBeans;
	private FriendListService service;
	private MyLetterListView letterView;
	private SignTextView signText;
	private LoadFriendsTask loadTask;
	public final String TITLE = "选择朋友";
	AlertDialog.Builder builder;

	// 选人发私信
	// private boolean isSendMsg = false;
	// 转发私信、圈聊
	// private boolean isForward = false;

	/**
	 * 1：发邮件，2：发秘信，3：文件、笔记 分享，4：私信、圈聊 转发,5：推荐朋友圈,6:从优优工作圈加入朋友到指定圈子,7:开放主页圈博
	 */
	private int type = 0;
	private int exceptId;

	@Override
	protected void onDestroy() {
		reHandler.removeCallbacksAndMessages(null);
		BaseData.hideKeyBoard(FriendsChooserActivity.this);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_chooser);
		@SuppressWarnings("unchecked")
		List<FriendSimpleBean> datas = (List<FriendSimpleBean>) getIntent()
				.getSerializableExtra("datas");
		if (null != datas && datas.size() > 0) {
			oriBeans = FriendSimpleBean.listToMap(datas);
		} else {
			oriBeans = new LinkedHashMap<String, FriendSimpleBean>();
		}
		try {
			Bundle b = getIntent().getExtras();
			type = b.getInt("type", 0);
			exceptId = b.getInt("id", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ab.setTitle(TITLE);

		if (type == 2) {
			findViewById(R.id.posters).setVisibility(View.GONE);
		} else {
		}
		findViews();
	}

	private void findViews() {
		service = FriendListService.getService(FriendsChooserActivity.this);

		layout = (RelativeLayout) findViewById(R.id.layout);
		lv_search = (ListView) findViewById(R.id.listview_search);
		sMbers = new ArrayList<FriendSimpleBean>();
		sAdapter = new FriendsChooserAdapter(FriendsChooserActivity.this,
				sMbers, lv_search, false, type);
		sAdapter.setSearch(true);
		lv_search.setAdapter(sAdapter);
		lv_search.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				L.i(TAG, "findViews onScrollStateChanged scrollState="
						+ scrollState);
				BaseData.hideKeyBoard(FriendsChooserActivity.this);
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
		TextView btn = (TextView) findViewById(R.id.ok);
		btn.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listview);
		listView.setFastScrollEnabled(false);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				BaseData.hideKeyBoard(FriendsChooserActivity.this);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		mbers = new ArrayList<FriendSimpleBean>();
		adapter = new FriendsChooserAdapter(FriendsChooserActivity.this, mbers,
				listView, true, type);

		listView.setAdapter(adapter);
		loadInfo(false);
	}

	private void loadInfo(boolean isRefresh) {
		stopTask();
		loadTask = new LoadFriendsTask(isRefresh);
		loadTask.execute();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ok:
			if (type == 2) {
				LogicUtil.enter(this, InfoWallActivity.class, null, true);
			} else {
				doChoose();
			}
			break;
		}
	}

	private void stopTask() {
		if (null != loadTask
				&& loadTask.getStatus() == AsyncTask.Status.RUNNING) {
			loadTask.cancel(true);
		}
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

		searchView.setOnSearchClickListener(new View.OnClickListener() {

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
				searchFriend(adapter.getData(), s);
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
					searchFriend(adapter.getData(), s);
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
				showAll();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void doChoose() {
		if (adapter.getSelectedIds().length > 0) {
			Intent intent = getIntent();
			intent.putExtra("ids", adapter.getSelectedIds());
			intent.putExtra("datas", (Serializable) FriendSimpleBean
					.mapToArray(adapter.getSelected()));
			intent.putExtra("id", exceptId);
			setResult(RESULT_OK, intent);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
	}

	@SuppressWarnings("unused")
	private void showResultDialog() {
		ListView list = new ListView(this);
		list.setFadingEdgeLength(0);
		list.setCacheColorHint(0);
		list.setSelector(R.color.transparent);
		builder = new AlertDialog.Builder(this);
		builder.setTitle("确认选择").setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				doChoose();
			}
		}).setNegativeButton("继续选择", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				adapter.notifyDataSetChanged();
				if (null == adapter.getSelected()
						|| adapter.getSelected().size() == 0) {
					setTitle(TITLE + "(" + mbers.size() + ")");
				} else {
					setTitle(TITLE + "(" + adapter.getSelected().size() + "/"
							+ mbers.size() + ")");
				}
			}
		}).setAdapter(adapter.getChoosedAdapter(list), null);
		builder.show();
		BaseData.hideKeyBoard(this);
	}

	@Override
	public void onSearch(String keyWords) {
		if (null != keyWords) {
			searchFriend(adapter.getData(), keyWords);
		}
	}

	boolean isUpdate = false;

	class LoadFriendsTask extends
			AsyncTask<String, Integer, ArrayList<FriendSimpleBean>> {

		private boolean isRefersh = false;

		public LoadFriendsTask(boolean isRefersh) {
			this.isRefersh = isRefersh;
			// TODO showHeaderProgress();
			setLoadingState(true);
		}

		@Override
		protected ArrayList<FriendSimpleBean> doInBackground(String... params) {
			if (isRefersh) {
				if (allMbers != null)
					allMbers.clear();
				UpdateFriendListThread.updateFriendList(
						FriendsChooserActivity.this, null);
				allMbers = service.queryFriendListsByPy(exceptId);
			} else {
				if (null == allMbers || allMbers.size() == 0) {
					allMbers = service.queryFriendListsByPy(exceptId);
					if (allMbers == null || allMbers.size() == 0) {
						try {
							UpdateFriendListThread.updateFriendList(
									FriendsChooserActivity.this, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						allMbers = service.queryFriendListsByPy(exceptId);
					} else {
						if (!isUpdate) {
							isUpdate = true;
							new Thread() {
								public void run() {
									try {
										UpdateFriendListThread
												.updateFriendList(
														FriendsChooserActivity.this,
														null);
										allMbers = service
												.queryFriendListsByPy(exceptId);
										ArrayList<FriendSimpleBean> recentEntities = FriendListService
												.getService(
														FriendsChooserActivity.this)
												.queryFriendListsByContactTime();
										if (null != recentEntities) {
											allMbers.addAll(0, recentEntities);
										}
										reHandler.sendEmptyMessage(2);

										MCResult result = APIRequestServers
												.friendList(
														FriendsChooserActivity.this,
														"0", "0", "10");
										@SuppressWarnings("unchecked")
										List<FriendBean> objects = (List<FriendBean>) result
												.getResult();
										int size = objects.size();
										String[] ids = new String[size];
										for (int i = 0; i < size; i++) {
											ids[size - i - 1] = objects.get(i)
													.getMemberId() + "";
										}
										service.saveContactTime(ids);
									} catch (Exception e) {
										e.printStackTrace();
									}
								};
							}.start();
						}
					}
				}
			}

			// handler.sendEmptyMessage(0);
			ArrayList<FriendSimpleBean> recentEntities = FriendListService
					.getService(FriendsChooserActivity.this)
					.queryFriendListsByContactTime();
			// try {
			// MCResult mcResult = APIRequestServers.recentlyFriendList(
			// FriendsChooserActivity.this, "", "0", "5");
			// recentEntities = (ArrayList<FriendSimpleBean>) mcResult
			// .getResult();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			if (null != recentEntities) {
				// for (FriendSimpleBean friendSimpleBean : recentEntities) {
				// friendSimpleBean.setRecentCheck(true);
				// }
				allMbers.addAll(0, recentEntities);
			}

			return allMbers;
		}

		@Override
		protected void onPostExecute(ArrayList<FriendSimpleBean> objects) {
			super.onPostExecute(objects);
			// TODO hintHeaderProgress();
			setLoadingState(false);
			if (null == allMbers || 0 == allMbers.size()) {
				showTip("获取朋友失败");
			} else {
				updateList();
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler reHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				updateList();
				break;
			default:
				break;
			}
		}
	};

	private void updateList() {
		mbers.clear();
		mbers.addAll(allMbers);
		adapter.notifyTags();
	}

	@Override
	public void onClear(String keyWords) {
		listView.setSelection(0);
	}

	private void searchFriend(ArrayList<FriendSimpleBean> lists, String str) {
		if (null == lists || null == str) {
			return;
		}
		sMbers.clear();
		for (int i = 0; i < lists.size(); i++) {
			FriendSimpleBean bean = lists.get(i);
			if (bean.isRecentCheck()) {
				continue;
			}
			String name = bean.getMemberName();
			String fname = bean.getFriendName();
			L.i(TAG, "searchGroup name=" + name);
			int mId = bean.getMemberId();
			if (mId != 0 && mId != -1)
				if ((name != null && (name.toLowerCase().startsWith(str
						.toLowerCase())))
						|| (fname != null && (fname.toLowerCase()
								.startsWith(str.toLowerCase())))) {
					if (!sMbers.contains(bean))
						sMbers.add(bean);
				} else {
					boolean isStrWith = false;

					String namePy = bean.getMemberNamePY();
					String fnamePy = bean.getFriendNamePY();
					if (null == namePy && null == fnamePy) {
						continue;
					}
					if (namePy.contains(",") || fnamePy.contains(",")) {
						String[] pys = namePy.split(",");
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
									isStrWith = true;
									if (!sMbers.contains(bean))
										sMbers.add(bean);
									break;
								}
							}
						}
						String[] fpys = fnamePy.split(",");
						int fsize = 0;
						if (pys != null) {
							fsize = fpys.length;
							L.i(TAG, "searchGroup groupNamePy.Size=" + size);
							for (int j = 0; j < fsize; j++) {
								String py = fpys[j];
								L.i(TAG,
										"searchGroup:" + j + ":"
												+ py.toLowerCase() + ";"
												+ str.toLowerCase());
								if (py != null
										&& (py.toLowerCase().startsWith(str
												.toLowerCase()))) {
									isStrWith = true;
									if (!sMbers.contains(bean))
										sMbers.add(bean);
									break;
								}
							}
						}
					} else {
						if (namePy.toLowerCase().startsWith(str.toLowerCase())
								|| fnamePy.toLowerCase().startsWith(
										str.toLowerCase())) {
							if (!sMbers.contains(bean))
								sMbers.add(bean);
						}
					}

					if (!isStrWith) {
						String nameJp = bean.getMemberNameJP();
						String fnameJp = bean.getFriendNameJP();
						if (nameJp.contains(",") || fnameJp.contains(",")) {
							String[] jps = nameJp.split(",");
							int size = 0;
							if (jps != null) {
								size = jps.length;
								for (int j = 0; j < size; j++) {
									String jp = jps[j];
									if (jp != null
											&& (jp.toLowerCase().startsWith(str
													.toLowerCase()))) {
										isStrWith = true;
										if (!sMbers.contains(bean))
											sMbers.add(bean);
										break;
									}
								}
							}

							if (!isStrWith) {
								String[] fjps = fnameJp.split(",");
								int fsize = 0;
								if (fjps != null) {
									fsize = fjps.length;
									for (int j = 0; j < fsize; j++) {
										String jp = fjps[j];
										if (jp != null
												&& (jp.toLowerCase()
														.startsWith(str
																.toLowerCase()))) {
											if (!sMbers.contains(bean))
												sMbers.add(bean);
											break;
										}
									}
								}
							}
						} else {
							if (nameJp.toLowerCase().startsWith(
									str.toLowerCase())
									|| fnameJp.toLowerCase().startsWith(
											str.toLowerCase())) {
								if (!sMbers.contains(bean))
									sMbers.add(bean);
							}
						}
					}
				}
		}
		sAdapter.notifyDataSetChanged();
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// if ("↑".equals(s)) {
		// listView.setSelection(0);
		// signText.showSign(s, 2000);
		// } else {
		for (int i = 0; i < adapter.getCount(); i++) {
			FriendSimpleBean entity = (FriendSimpleBean) adapter.getItem(i);
			if ((entity.getMemberId() == adapter.TAG)
					&& s.equals(entity.getMemberName())) {
				listView.setSelection(i + listView.getHeaderViewsCount());
				signText.showSign(s, 2000);
			}
		}
		// }
	}
}
