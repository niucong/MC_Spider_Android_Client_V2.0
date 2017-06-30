package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.FileAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class FileListActivity extends BaseMidMenuActivity implements
		OnRefreshListener, OnLoadMoreListener {
	private TextView textView;
	private RefreshListView listView;

	private LoadInfoTask task;
	private boolean noMore, searchState;
	private boolean isLoading = true;
	private ArrayList<ResourceBean> files;
	private FileAdapter fileAdapter;
	private String trendId_file;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_file_list);
		findViews();
		initData();
	}

	@Override
	void initData() {
		setTitle(MM_FILE, R.drawable.title_fanhui, null, false);
		loadInfo(true);
	}

	private void findViews() {
		setMidChosen(1);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("");
		textView.setVisibility(View.GONE);
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		files = new ArrayList<ResourceBean>();
		fileAdapter = new FileAdapter(this, files);
		listView.setAdapter(fileAdapter);
	}

	@Override
	protected void onRefreshClick(View v) {
		super.onRefreshClick(v);
		loadInfo(true);
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadInfoTask(isRefresh);
			task.execute();
			listView.showLoadFooter();
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadInfoTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
			textView.setVisibility(View.GONE);
			if (isRefresh) {
				noMore = false;
			}
			showHeaderProgress();
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if (!searchState) {
					result = loadDate(isRefresh);
				} else {
					// result = loadSearchResult(isRefresh);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			hintHeaderProgress();
			listView.showFinishLoadFooter();
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					if (!searchState) {
						MapResourceBean fileResult = (MapResourceBean) mcResult
								.getResult();
						ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) fileResult
								.getLIST();
						if (null == requestInfos1 || requestInfos1.size() == 0) {
							if (files.size() == 0) {
								textView.setVisibility(View.VISIBLE);
								textView.setText("还没有文件哦！");
							} else {
								showTip("最后一页");
							}
							noMore = true;
						} else {
							if (isRefresh || null == trendId_file
									|| "".equals(trendId_file)) {
								files.clear();
								files.addAll(requestInfos1);
								trendId_file = String.valueOf(files.size());
								listView.setAdapter(fileAdapter);
							} else {
								files.addAll(requestInfos1);
								trendId_file = String.valueOf(files.size());
								fileAdapter.notifyDataSetChanged();
							}
						}
					} else {
						// showSearch(isRefresh, mcResult);
					}
				}
			}
			try {
				listView.onRefreshComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isLoading = false;
		}
	}

	private MCResult loadDate(boolean refresh) throws Exception {
		MCResult mcResult = null;
		if (refresh) {
			trendId_file = "0";
		}
		if (TYPE_MBER == sType) {
			mcResult = APIRequestServers.singleResourceList(
					FileListActivity.this, id, ResourceTypeEnum.OBJ_GROUP_FILE,
					trendId_file, "20", false);
		} else if (TYPE_GROUP == sType) {
			mcResult = APIRequestServers.groupResourceLists(
					FileListActivity.this, id, ResourceTypeEnum.OBJ_GROUP_FILE,
					trendId_file, "20", false);
		} else if (TYPE_OPAGE == sType) {
			mcResult = APIRequestServers.groupResourceLists(
					FileListActivity.this, id,
					ResourceTypeEnum.OBJ_OPEN_PAGE_FILE, trendId_file, "20",
					false);
		}
		return mcResult;
	}

	@Override
	public void onRefresh() {
		loadInfo(true);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading && !noMore) {
			loadInfo(false);
		}
	}

}
