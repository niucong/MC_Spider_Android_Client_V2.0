package com.datacomo.mc.spider.android.fragmt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.TopicActivity;
import com.datacomo.mc.spider.android.adapter.GroupTopicAdapter;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupThemeBean;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class GroupTopicFragment extends BaseGroupFragment implements
		OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

	private TextView tv_num;
	private RefreshListView listView;

	private ArrayList<GroupThemeBean> topics;
	private GroupTopicAdapter adapter;
	private ArrayList<GroupThemeBean> stopics;
	private GroupTopicAdapter sadapter;
	private String groupId;
	private HomeGpActivity mActivity;
	private boolean searchState, isLoading;
	private String trendId_tpc;
	private LoadTopicTask task;
	private String memberState;

	private int num, snum;
	private final String source = "共 <font color=#5aa11f>%d</font> 个专题";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupId = getArguments().getString("groupId");
		memberState = getArguments().getString("memberState");
		mActivity = (HomeGpActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater
				.inflate(R.layout.frag_group, null);
		tv_num = (TextView) view.findViewById(R.id.num);
		listView = (RefreshListView) view.findViewById(R.id.listview);
		listView.setonLoadMoreListener(this);
		listView.setonRefreshListener(this);
		listView.setOnItemClickListener(this);
		if (null == topics) {
			topics = new ArrayList<GroupThemeBean>();
		}
		if (null == adapter) {
			adapter = new GroupTopicAdapter(getActivity(), topics);
		}
		listView.setAdapter(adapter);
		if (null == stopics) {
			stopics = new ArrayList<GroupThemeBean>();
		}
		if (null == sadapter) {
			sadapter = new GroupTopicAdapter(getActivity(), stopics);
		}
		if (topics.size() == 0) {
			loadInfo(true);
		} else {
			if (searchState) {
				setNums(snum);
			} else {
				setNums(num);
			}
			listView.setSelection(getArguments().getInt("lastPosition"));
		}
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		int lastPositon = listView.getFirstVisiblePosition();
		getArguments().putInt("lastPositon", lastPositon);
	}

	class LoadTopicTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadTopicTask(boolean isRefresh) {
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
				if (searchState) {
					result = loadSearchResult(isRefresh);
				} else {
					result = loadData(isRefresh);
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
				if (searchState) {
					showSearchResult(isRefresh, mcResult);
				} else {
					showResult(isRefresh, mcResult);
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

	private void setNums(int n) {
		if (n > 0) {
			tv_num.setVisibility(View.VISIBLE);
			tv_num.setText(Html.fromHtml(String.format(source, n)));
		}
	}

	private void showResult(boolean isRefresh, MCResult mcResult) {
		if (isRefresh || null == trendId_tpc || "".equals(trendId_tpc)) {
			topics.clear();
		}
		MapGroupThemeBean topicResult = (MapGroupThemeBean) mcResult
				.getResult();

		if (topicResult == null)
			return;

		num = topicResult.getTHEME_NUM();
		setNums(num);

		ArrayList<GroupThemeBean> requestInfos1 = (ArrayList<GroupThemeBean>) topicResult
				.getTHEME_LIST();
		if (null == requestInfos1 || requestInfos1.size() == 0) {
			if (topics.size() == 0) {
				showTip("目前还没有专题！");
			} else {
				showTip("最后一页");
			}
		} else {
			if (isRefresh) {
				topics.clear();
			}
			topics.addAll(requestInfos1);
			trendId_tpc = topics.size() + "";
			adapter.notifyDataSetChanged();
		}
	}

	private void showSearchResult(Boolean isRefresh, MCResult mcResult) {
		if (isRefresh) {
			stopics.clear();
		}
		MapGroupThemeBean mapGroupThemeBean = (MapGroupThemeBean) mcResult
				.getResult();
		if (null == mapGroupThemeBean) {
			showTip("没有更多数据！");
		} else {
			snum = mapGroupThemeBean.getTHEME_NUM();
			setNums(snum);

			List<GroupThemeBean> requestInfos0 = mapGroupThemeBean
					.getTHEME_LIST();
			if (requestInfos0 == null || 0 == requestInfos0.size()) {
				showTip("没有更多数据！");
			} else {
				stopics.addAll(requestInfos0);
				sadapter.notifyDataSetChanged();
			}
		}
	}

	private void showTip(String text) {
		if (this.isVisible())
			mActivity.showTip(text);
	}

	private MCResult loadData(boolean refresh) throws Exception {
		if (refresh) {
			trendId_tpc = "0";
		}
		return APIThemeRequestServers.themesList(getActivity(), groupId,
				trendId_tpc, "20");
	}

	private MCResult loadSearchResult(boolean refresh) throws Exception {
		String startRecord = "0";
		if (!refresh)
			startRecord = "" + stopics.size();
		return APIThemeRequestServers.searchThemesList(getActivity(), groupId,
				keyword, startRecord, "20", true);
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadTopicTask(isRefresh);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle b = new Bundle();
		b.putSerializable(
				"data",
				(Serializable) adapter.getItem(arg2
						- listView.getHeaderViewsCount()));
		b.putString("joinState", memberState);
		LogicUtil.enter(getActivity(), TopicActivity.class, b, false);
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
		loadInfo(true);
	}

	String keyword;

	@Override
	public void search(String s) {
		keyword = s;
		if (s != null && !"".equals(s)) {
			search();
		} else {
			clear();
		}
	}

	private void search() {
		listView.setAdapter(sadapter);
		searchState = true;
		loadInfo(true);
		setNums(snum);
	}

	private void clear() {
		listView.setAdapter(adapter);
		searchState = false;
		setNums(num);
	}
}
