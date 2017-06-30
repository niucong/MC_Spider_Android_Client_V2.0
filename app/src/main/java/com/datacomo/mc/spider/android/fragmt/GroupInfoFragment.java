package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.adapter.InfoSearchedAdapter;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class GroupInfoFragment extends BaseGroupFragment implements
		OnRefreshListener, OnLoadMoreListener {
	private final String TAG = "GroupInfoFragment";

	private RefreshListView listView;
	private ArrayList<ResourceTrendBean> infos;
	private InfoAdapter adapter;
	private ArrayList<ResultMessageBean> searchInfos;
	private InfoSearchedAdapter infoSearchAdapter;
	private String groupId;
	private HomeGpActivity mActivity;
	private boolean searchState, isLoading;
	private String trendId_info;
	private int searchStart = 1;
	private LoadInfoTask task;

	private String joinGroupStatus;

	// private int lastScrollY;//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupId = getArguments().getString("groupId");
		joinGroupStatus = getArguments().getString("memberState");
		mActivity = (HomeGpActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new RefreshListView(mActivity);
		listView.setonLoadMoreListener(this);
		listView.setonRefreshListener(this);
		if (null == infos) {
			infos = new ArrayList<ResourceTrendBean>();
		}
		if (null == adapter) {
			adapter = new InfoAdapter(getActivity(), infos, listView, groupId);
			adapter.setFragment(this);
		}
		listView.setAdapter(adapter);

		searchInfos = new ArrayList<ResultMessageBean>();
		infoSearchAdapter = new InfoSearchedAdapter(getActivity(), searchInfos,
				listView, groupId, joinGroupStatus);

		if (infos.size() == 0) {
			loadInfo(true);
		} else {
			int lastPositon = getArguments().getInt("lastPositon");
			listView.setSelection(lastPositon);
		}

		// if(lastScrollY > 0){
		// listView.setScrollY(lastScrollY);
		// }
		return listView;
	}

	@Override
	public void onPause() {
		super.onPause();
		int lastPositon = listView.getFirstVisiblePosition();
		getArguments().putInt("lastPositon", lastPositon);
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadInfoTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
			mActivity.setLoadingState(true);
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
					result = loadData(isRefresh);
				} else {
					result = loadSearchResult(isRefresh);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			mActivity.setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				if (!searchState) {
					showResult(isRefresh, mcResult);
				} else {
					showSearchResult(isRefresh, mcResult);
				}
				if (isRefresh) {
					listView.setSelection(0);
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

	private void showResult(boolean isRefresh, MCResult mcResult) {
		if (isRefresh || null == trendId_info || "".equals(trendId_info)) {
			infos.clear();
		}
		@SuppressWarnings("unchecked")
		ArrayList<ResourceTrendBean> requestInfos = (ArrayList<ResourceTrendBean>) mcResult
				.getResult();
		if (null == requestInfos || requestInfos.size() == 0) {
			if (infos.size() == 0) {
				showTip("目前还没有动态！");
			} else {
				showTip("最后一页");
			}
		} else {
			infos.addAll(requestInfos);
			ResourceTrendBean rBean = infos.get(infos.size() - 1);
			trendId_info = rBean.getTrendId() + "";
			adapter.notifyDataSetChanged();
		}
	}

	private void showSearchResult(Boolean isRefresh, MCResult mcResult) {
		if (isRefresh || searchStart == 1) {
			searchInfos.clear();
		}
		ResultAll resultInfos0 = (ResultAll) mcResult.getResult();
		if (null == resultInfos0) {
			showTip("没有更多数据！");
		} else {
			List<ResultMessageBean> requestInfos0 = resultInfos0.getRmList();
			if (requestInfos0 == null || 0 == requestInfos0.size()) {
				showTip("没有更多数据！");
			} else {
				searchInfos.addAll(requestInfos0);
				infoSearchAdapter.notifyDataSetChanged();
			}
		}
	}

	private void showTip(String text) {
		if (this.isVisible()) {
			mActivity.showTip(text);
		}
	}

	private MCResult loadData(boolean refresh) throws Exception {
		if (refresh) {
			trendId_info = "0";
		}
		return APIRequestServers.singleGroupResourceTrends(getActivity(),
				groupId, trendId_info, "10", true);
	}

	private MCResult loadSearchResult(boolean refresh) throws Exception {
		if (refresh) {
			searchStart = 1;
		} else {
			searchStart = searchInfos.size() / 20 + 1;
		}
		return APIRequestServers.searchResource(getActivity(), keyword,
				groupId, searchStart, 20, "sg", "1", "");
	}

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

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadInfo(false);
	}

	@Override
	public void onRefresh() {
		loadInfo(true);
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
		loadInfo(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == Activity.RESULT_OK && requestCode == 10) {
			onRefersh(data);
		}
	}

	private void onRefersh(Intent data) {
		int position = data.getIntExtra("position", -1);
		if (position != -1) {
			ResourceTrendBean tBean = (ResourceTrendBean) data
					.getSerializableExtra("Trend");
			if (tBean != null) {
				infos.remove(position);
				infos.add(position, tBean);
				adapter.notifyDataSetChanged();
			}
		}
	}

	String keyword;

	@Override
	public void search(String s) {
		L.d(TAG, "search..." + s);
		keyword = s;
		if (s != null && !"".equals(s)) {
			search();
		} else {
			clear();
		}
	}

	private void search() {
		listView.setAdapter(infoSearchAdapter);
		searchState = true;
		loadInfo(true);
	}

	private void clear() {
		listView.setAdapter(adapter);
		searchState = false;
	}
}
