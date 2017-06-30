package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.MailListAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;
import com.umeng.analytics.MobclickAgent;

public class MailListActivity extends BasicMenuActivity implements
		OnSearchListener, OnClearListener {
	public static String REFRESH = "MailListActivity" + "_REFRESH";

	private RefreshListView listView;
	private TextView mail_num;

	private ArrayList<MailContactBean> mails;
	private MailListAdapter adapter;
	private LoadMailsTask task;
	private SearchBar sBar;
	private String key = "";

	private boolean isLoading, isSearchState;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_MAIL;
		titleName = "邮件管家";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_mail_list, null);
		fl.addView(rootView);

		registeRefreshReceiver(REFRESH);
		findView();
		setView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "8");
	}

	@Override
	void onDataChanged() {
		loadInfo(true);
	}

	private void findView() {
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadInfo(true);
			}
		});

		listView.setonLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				if (!isLoading) {
					loadInfo(false);
				}
			}
		});

		sBar = (SearchBar) findViewById(R.id.search_mail);
		sBar.setVisibility(View.GONE);
		mail_num = (TextView) findViewById(R.id.mail_num);
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	private void setView() {
		getLocMailLists();
		adapter = new MailListAdapter(this, mails, listView);
		adapter.setLocal(true);
		listView.setAdapter(adapter);

		sBar.setOnClearListener(this);
		sBar.setOnSearchListener(this);
		sBar.setHint("搜索联系人");
	}

	private void getLocMailLists() {
		try {
			mails = (ArrayList<MailContactBean>) LocalDataService.getInstense()
					.getLocMails(this, LocalDataService.TXT_MAILWITH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mails == null) {
			mails = new ArrayList<MailContactBean>();
		}
	}

	void loadInfo(boolean isRefresh) {
		stopTask();
		task = new LoadMailsTask(isRefresh);
		task.execute();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadMailsTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadMailsTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
			setLoadingState(true);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			int trendId = 0;
			if (!isRefresh) {
				trendId = adapter.getCount();
			}

			try {
				if (!isSearchState) {
					result = APIMailRequestServers.contactLeaguers(
							MailListActivity.this, String.valueOf(trendId),
							"20");
				} else {
					result = APIMailRequestServers.searchMyContactLeaguers(
							MailListActivity.this, key,
							String.valueOf(trendId), "20", "false");
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
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}

			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					if (isRefresh) {
						mails.clear();
					}
					MapMailContactBean resultInfos0 = (MapMailContactBean) mcResult
							.getResult();
					List<MailContactBean> requestInfos0 = resultInfos0
							.getCONTACTLIST();
					if (!isSearchState && resultInfos0.getTOTALNUM() > 0) {
						mail_num.setVisibility(View.VISIBLE);
						String source = "<span>"
								+ "您和&nbsp;"
								+ "<font color=#5aa11f>%d</font>&nbsp;人之间共有&nbsp;"
								+ "<font color=#5aa11f>%d</font>&nbsp;封邮件&nbsp;"
								+ "<font color=#5aa11f>%d</font>&nbsp;封未读</span>";
						// 您和128人之间共有1181封邮件0封未读
						mail_num.setText(Html.fromHtml(String.format(source,
								resultInfos0.getTOTALNUM(),
								resultInfos0.getEMAILS_NUM(),
								resultInfos0.getUNREAD_EMAILS_NUM())));
					}
					if (null == resultInfos0 || null == requestInfos0) {
						if (isRefresh) {
							showTip("暂时没有新邮件");
						} else {
							showTip("最后一页");
						}
					} else {
						mails.addAll(requestInfos0);
						adapter.setLocal(false);
						adapter.notifyDataSetChanged();
					}

					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			listView.onRefreshComplete();
			isLoading = false;
			isSearchState = false;
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
				isSearchState = true;
				loadInfo(true);
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
				loadInfo(true);
				showMenu();
				return false;
			}
		});

		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			loadInfo(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_mail_write).setVisible(!drawerOpen);
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
				loadInfo(true);
				return false;
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_mail_write:
			LogicUtil.enter(MailListActivity.this, MailCreateActivity.class,
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
				loadInfo(true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void refresh() {
		loadInfo(true);
	}

	private String getKeyWords() {
		return sBar.getKeyWords();
	}

	@Override
	public void onSearch(String keyWords) {
		isSearchState = true;
		key = getKeyWords();
		loadInfo(true);
	}

	@Override
	public void onClear(String keyWords) {
		isSearchState = false;
		loadInfo(true);
	}
}
