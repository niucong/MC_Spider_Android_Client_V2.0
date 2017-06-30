package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.InterGroupAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

public class InterGroupActivity extends BasicMenuActivity {
	private final String TAG = "InterGroupActivity";

	private RefreshListView listView;

	private ArrayList<GroupEntity> managerGroups, searchGroups;
	private InterGroupAdapter managerAdapter, searchAdapter;

	private int cut;
	private LoadDataTask task;

	private boolean searchState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_GROUPS;
		titleName = "交流圈";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_inter_groups, null);
		fl.addView(rootView);

		findView(rootView);
		setView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "2");
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	private void findView(View rootView) {
		listView = (RefreshListView) rootView.findViewById(R.id.listview);
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				L.d(TAG, "onRefresh");
				searchState = false;
				listView.setAdapter(managerAdapter);
				loadData(true);
			}
		});
	}

	private void setView() {
		try {
			managerGroups = LocalDataService.getInstense().getLocGroups(this,
					LocalDataService.TXT_GROUP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (managerGroups == null) {
			managerGroups = new ArrayList<GroupEntity>();
		}

		managerAdapter = new InterGroupAdapter(this, managerGroups, listView,
				false);
		searchGroups = new ArrayList<GroupEntity>();
	}

	private void loadData(boolean refresh) {
		if (!searchState) {
			listView.onRefreshComplete();
			if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
				task.cancel(true);
			}
			task = new LoadDataTask(cut, refresh);
			task.execute();
		}
	}

	class LoadDataTask extends AsyncTask<String, Integer, Object[]> {
		private boolean isRefresh;

		public LoadDataTask(int cut, boolean isRefresh) {
			this.isRefresh = isRefresh;

			setLoadingState(true);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
		}

		private Object[] commGroupList() {
			try {
				return APIRequestServers.commGroupList(InterGroupActivity.this,
						"", "" + managerGroups.size(), "20", true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected Object[] doInBackground(String... params) {
			return commGroupList();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object[] objects) {
			super.onPostExecute(objects);
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}

			if (null == objects) {
				return;
			}
			if ((Integer) objects[0] == 1) {
				ArrayList<GroupEntity> lists = (ArrayList<GroupEntity>) objects[1];
				if (null == lists) {
					showTip(T.ErrStr);
				} else if (lists.size() == 0) {
					showTip("您尚未加入任何圈子");
				} else {
					if (isRefresh) {
						managerGroups.clear();
					}
					managerGroups.addAll(lists);
					managerAdapter.notifyDataSetChanged();
				}
			} else {
				showTip(T.ErrStr);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(
			final com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		// searchView.setIconifiedByDefault(false);
		// ImageView iv = (ImageView) searchView
		// .findViewById(com.actionbarsherlock.R.id.abs__search_mag_icon);
		// iv.setImageResource(R.drawable.action_search);
		// searchView.setIconified(false);

		// searchView.setSubmitButtonEnabled(true);
		// try {
		// Field field = searchView.getClass().getDeclaredField(
		// "mSubmitButton");
		// field.setAccessible(true);
		// ImageView sv = (ImageView) field.get(searchView);
		// sv.setImageDrawable(this.getResources().getDrawable(
		// R.drawable.action_search));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);
		// searchView.setQueryHint("搜索");

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
				searchState = false;
				searchGroups(s);
				searchState = true;
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
					searchState = false;
					listView.setAdapter(managerAdapter);
					if (managerGroups.size() == 0) {
						loadData(true);
					}
				} else {
					cv.setVisibility(View.VISIBLE);
					searchState = false;
					searchGroups(s);
				}
				searchState = true;
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				searchState = false;
				listView.setAdapter(managerAdapter);
				if (managerGroups.size() == 0) {
					loadData(true);
				}
				showMenu();
				return false;
			}
		});

		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			searchState = false;
			listView.setAdapter(managerAdapter);
			loadData(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_groups).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		menu.findItem(R.id.action_message).setVisible(drawerOpen);
		menu.findItem(R.id.action_write).setVisible(drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				searchState = false;
				listView.setAdapter(managerAdapter);
				return false;
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_groups:
			LogicUtil.enter(InterGroupActivity.this, GroupCreateActivity.class,
					null, false);
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
				showMenu();
				searchView.onActionViewCollapsed();
				searchState = false;
				listView.setAdapter(managerAdapter);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void searchGroups(String keyWords) {
		int mSize = managerGroups.size();
		searchGroups.clear();
		if (mSize > 0) {
			for (int i = mSize; i > 0; i--) {
				GroupEntity mEntity = managerGroups.get(i - 1);
				String gName = mEntity.getName();
				String gPYName = mEntity.getGroupNamePy();
				String gJPName = mEntity.getGroupNameJp();
				if ((gName != null && gName.toLowerCase().startsWith(
						keyWords.toLowerCase()))
						|| (gPYName != null && gPYName.toLowerCase()
								.startsWith(keyWords.toLowerCase()))
						|| (gJPName != null && gJPName.toLowerCase()
								.startsWith(keyWords.toLowerCase()))) {
					searchGroups.add(mEntity);
				}
			}
			if (searchGroups.size() > 0) {
				searchAdapter = new InterGroupAdapter(this, searchGroups,
						listView, false);
				listView.setAdapter(searchAdapter);
			} else {
				showTip("未搜索到相应圈子");
			}
		} else {
			// TODO
		}
	}

	@Override
	protected void refresh() {
		if (searchView.findViewById(
				com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
			showMenu();
			searchView.onActionViewCollapsed();
			searchState = false;
			listView.setAdapter(managerAdapter);
		}
		loadData(true);
	}
}
