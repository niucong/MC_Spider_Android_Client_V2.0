package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.datacomo.mc.spider.android.adapter.MailWithAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailsByLeaguer;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class MailWithActivity extends BasicActionBarActivity {
	private final String TAG = "MailWithActivity";
	public static String REFRESH = "MailWithActivity" + "_REFRESH";
	private RefreshListView listView;
	private ArrayList<MailContactBean> mails;
	private MailWithAdapter adapter;
	private LoadMailsTask task;
	private boolean isLoading;
	private String leaguerId;

	public String memberName;
	public int memberId;

	public int unReadNum = 0;
	private int allNum = 0;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_mail_list);
		Bundle b = getIntent().getExtras();
		leaguerId = b.getString("leaguerId");
		memberName = b.getString("name");
		memberId = b.getInt("memberId");
		if (memberName == null)
			memberName = "";
		ab.setTitle(memberName);

		registeRefreshReceiver(REFRESH);
		findView();
		setView();
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
		findViewById(R.id.search_mail).setVisibility(View.GONE);
	}

	private void setView() {
		getLocMailLists();
		adapter = new MailWithAdapter(MailWithActivity.this, mails);
		listView.setAdapter(adapter);
		loadInfo(true);
	}

	private void getLocMailLists() {
		try {
			mails = (ArrayList<MailContactBean>) LocalDataService.getInstense()
					.getLocMailList(leaguerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mails == null) {
			mails = new ArrayList<MailContactBean>();
		}
		L.d(TAG, "getLocMailLists size=" + mails.size());
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
				result = APIMailRequestServers.mailsByLeaguer(
						MailWithActivity.this, leaguerId, "false", "0", ""
								+ trendId, "20");
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					if (isRefresh) {
						mails.clear();
					}
					MapMailsByLeaguer resultInfos0 = (MapMailsByLeaguer) mcResult
							.getResult();
					List<MailContactBean> requestInfos0 = resultInfos0
							.getLIST();
					if (null == resultInfos0 || null == requestInfos0) {
						showTip("最后一页");
					} else {
						mails.addAll(requestInfos0);
						adapter.notifyDataSetChanged();

						if (requestInfos0.size() > 0) {
							MailContactBean mailContactBean = requestInfos0
									.get(0);
							allNum = mailContactBean.getTotalNum();
							unReadNum = mailContactBean.getNewNum();

							// setTitle(memberName + "(" + unReadNum + "/"
							// + allNum + ")", R.drawable.title_fanhui,
							// R.drawable.title_sendmail);
							ab.setTitle(memberName + "(" + unReadNum + "/"
									+ allNum + ")");
						}
					}

					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			listView.onRefreshComplete();
			isLoading = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_mail_write).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_mail_write:
			Intent intent = new Intent(this, MailCreateActivity.class);
			intent.putExtra("friendName", memberName);
			intent.putExtra("friendId", memberId);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// Intent intent = new Intent(this, MailCreateActivity.class);
	// intent.putExtra("friendName", memberName);
	// intent.putExtra("friendId", memberId);
	// startActivity(intent);
	// }

	public static int index = -1;

	@Override
	protected void onRestart() {
		super.onRestart();
		if (index != -1 && index < mails.size()) {
			mails.remove(index);
			adapter.notifyDataSetChanged();
			index = -1;

			--allNum;
		}
		// setTitle(memberName + "(" + unReadNum + "/" + allNum + ")",
		// R.drawable.title_fanhui, R.drawable.title_sendmail);
		ab.setTitle(memberName + "(" + unReadNum + "/" + allNum + ")");
	}
}
