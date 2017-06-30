package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
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
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class GroupFileFragment extends BaseGroupFragment implements
		OnRefreshListener, OnLoadMoreListener {

	private TextView tv_num;
	private RefreshListView listView;

	private ArrayList<ResourceBean> files;
	private FileAdapter adapter;

	private ArrayList<ResourceBean> squubos;
	private FileAdapter sadapter;

	private String groupId;
	private HomeGpActivity mActivity;
	private boolean searchState, isLoading;
	private String trendId_file, sstart;
	private LoadFileTask task;
	private String memberState;

	private int num, snum;
	private final String source = "共 <font color=#5aa11f>%d</font> 个文件";

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
		if (null == files) {
			files = new ArrayList<ResourceBean>();
		}
		if (null == adapter) {
			adapter = new FileAdapter(getActivity(), files, memberState, false);
		}
		listView.setAdapter(adapter);

		if (null == squubos) {
			squubos = new ArrayList<ResourceBean>();
		}
		if (null == sadapter) {
			sadapter = new FileAdapter(getActivity(), squubos, memberState,
					false);
		}

		if (files.size() == 0) {
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

	class LoadFileTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadFileTask(boolean isRefresh) {
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
					showSearch(isRefresh, mcResult);
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
		if (isRefresh || null == trendId_file || "".equals(trendId_file)) {
			files.clear();
		}
		MapResourceBean fileResult = (MapResourceBean) mcResult.getResult();

		num = fileResult.getTOTALNUM();
		setNums(num);

		ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) fileResult
				.getLIST();
		if (null == requestInfos1 || requestInfos1.size() == 0) {
			if (files.size() == 0) {
				showTip("目前还没有文件！");
			} else {
				showTip("已经是最后一条！");
			}
		} else {
			files.addAll(requestInfos1);
			trendId_file = files.size() + "";
			adapter.notifyDataSetChanged();
		}
	}

	private void showTip(String text) {
		if (this.isVisible())
			mActivity.showTip(text);
	}

	private MCResult loadData(boolean refresh) throws Exception {
		if (refresh)
			trendId_file = "0";

		return APIRequestServers.groupResourceLists(getActivity(), groupId,
				ResourceTypeEnum.OBJ_GROUP_FILE, trendId_file, "20", false);
	}

	private MCResult loadSearchResult(boolean refresh) throws Exception {
		if (refresh) {
			sstart = "0";
		} else {
			sstart = squubos.size() + "";
		}
		// return APIRequestServers.searchResource(getActivity(), keyword,
		// groupId, searchStart, 20, "gf", "1");
		return APIRequestServers.searchGroupResourceLists(getActivity(),
				groupId, keyword, ResourceTypeEnum.OBJ_GROUP_FILE, sstart,
				"20", false);
	}

	private void showSearch(Boolean isRefresh, MCResult mcResult) {
		if (isRefresh || null == sstart || "".equals(sstart)) {
			squubos.clear();
		}
		MapResourceBean fileResult = (MapResourceBean) mcResult.getResult();

		if (null == fileResult) {
			return;
		}

		snum = fileResult.getTOTALNUM();
		setNums(snum);

		ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) fileResult
				.getLIST();
		if (null == requestInfos1 || requestInfos1.size() == 0) {
			if (squubos.size() == 0) {
				showTip("没有搜索到相应文件！");
			} else {
				showTip("最后一页");
			}
		} else {
			squubos.addAll(requestInfos1);
			sstart = String.valueOf(squubos.size());
			sadapter.notifyDataSetChanged();
		}
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadFileTask(isRefresh);
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
