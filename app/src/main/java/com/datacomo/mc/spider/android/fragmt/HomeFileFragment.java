package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.FileAdapter;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;

@SuppressLint("ValidFragment")
public class HomeFileFragment extends BaseGroupFragment implements
		OnLoadMoreListener, OnClickListener {
	private TextView textView;
	private RefreshListView listView;

	private LoadInfoTask task;
	private boolean noMore, searchState;
	private boolean isLoading = true;
	private ArrayList<ResourceBean> files;
	private FileAdapter fileAdapter;
	private String trendId_file;

	private HomePgActivity homePg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homePg = (HomePgActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_file_list, null);
		findViews(view);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		// int lastPositon = listView.getFirstVisiblePosition();
		// getArguments().putInt("lastPositon", lastPositon);
	}

	private void findViews(ViewGroup view) {
		if (homePg.menu != null) {
			if (homePg.menu.findItem(R.id.action_search) != null)
				homePg.menu.findItem(R.id.action_search).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write) != null)
				homePg.menu.findItem(R.id.action_write).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write_mood) != null)
				homePg.menu.findItem(R.id.action_write_mood).setVisible(false);
			if (homePg.menu.findItem(R.id.action_more) != null)
				homePg.menu.findItem(R.id.action_more).setVisible(false);
		}

		listView = (RefreshListView) view.findViewById(R.id.listview);
		textView = (TextView) view.findViewById(R.id.textView);
		// setMidChosen(1);
		textView.setText("");
		textView.setVisibility(View.GONE);
		// listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		files = new ArrayList<ResourceBean>();
		fileAdapter = new FileAdapter(homePg, files, "GROUP_LEAGUER");
		listView.setAdapter(fileAdapter);

		loadInfo(true);
	}

	// @Override
	// protected void onRefreshClick(View v) {
	// super.onRefreshClick(v);
	// loadInfo(true);
	// }

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadInfoTask(isRefresh);
			task.execute();
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

			homePg.setLoadingState(true);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
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
			homePg.setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null == mcResult) {
				homePg.showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					homePg.showTip(T.ErrStr);
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
								homePg.showTip("最后一页");
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
		if (HomePgActivity.TYPE_MBER == homePg.sType) {
			mcResult = APIRequestServers.singleResourceList(homePg, homePg.id,
					ResourceTypeEnum.OBJ_GROUP_FILE, trendId_file, "20", false);
		} else if (HomePgActivity.TYPE_GROUP == homePg.sType) {
			mcResult = APIRequestServers.groupResourceLists(homePg, homePg.id,
					ResourceTypeEnum.OBJ_GROUP_FILE, trendId_file, "20", false);
		} else if (HomePgActivity.TYPE_OPAGE == homePg.sType) {
			mcResult = APIRequestServers.groupResourceLists(homePg, homePg.id,
					ResourceTypeEnum.OBJ_OPEN_PAGE_FILE, trendId_file, "20",
					false);
		}
		return mcResult;
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
		loadInfo(true);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading && !noMore) {
			loadInfo(false);
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void search(String s) {
		// TODO
	}

}
