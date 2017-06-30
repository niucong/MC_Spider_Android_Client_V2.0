package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datacomo.mc.spider.android.EditActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class HomeLeaveFragment extends BaseGroupFragment implements
		OnRefreshListener, OnLoadMoreListener {
	private TextView textView;
	private RefreshListView listView;

	private LoadInfoTask task;
	private boolean noMore;
	private boolean isLoading = true;
	private ArrayList<ResourceTrendBean> leaves;
	private InfoAdapter leaveAdapter;
	private String trendId_leave, createLeaveTime;

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
		loadInfo(true);
		return view;
	}

	private void findViews(ViewGroup view) {
		if (homePg.menu != null) {
			if (homePg.menu.findItem(R.id.action_search) != null)
				homePg.menu.findItem(R.id.action_search).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write) != null)
				homePg.menu.findItem(R.id.action_write).setVisible(false);
			if (homePg.menu.findItem(R.id.action_more) != null)
				homePg.menu.findItem(R.id.action_more).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write_mood) != null)
				if (homePg.userSelf) {
					homePg.menu.findItem(R.id.action_write_mood).setVisible(
							true);
				} else {
					homePg.menu.findItem(R.id.action_write_mood).setVisible(
							false);
				}
		}

		textView = (TextView) view.findViewById(R.id.textView);
		textView.setText("");
		textView.setVisibility(View.GONE);
		listView = (RefreshListView) view.findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		leaves = new ArrayList<ResourceTrendBean>();
		leaveAdapter = new InfoAdapter(homePg, leaves, listView);
		listView.setAdapter(leaveAdapter);
		// setMidChosen(6);
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
		loadInfo(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == Activity.RESULT_OK) {
			try {
				switch (requestCode) {
				case EditActivity.TYPE_MOOD:
					String newMood = data.getStringExtra("feelword");
					if (newMood != null && !newMood.equals("")) {
						loadInfo(true);
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
				result = loadDate(isRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			homePg.setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			listView.showFinishLoadFooter();
			if (null == mcResult) {
				homePg.showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					homePg.showTip(T.ErrStr);
				}
				if (1 == mcResult.getResultCode()) {
					ArrayList<ResourceTrendBean> requestLeaves = (ArrayList<ResourceTrendBean>) mcResult
							.getResult();
					if (null == requestLeaves || requestLeaves.size() == 0) {
						if (leaves.size() == 0) {
							textView.setVisibility(View.VISIBLE);
							textView.setText("还没有留言哦！");
						} else {
							homePg.showTip("最后一页");
						}
						noMore = true;
					} else {
						if (isRefresh || null == trendId_leave
								|| "".equals(trendId_leave)) {
							leaves.clear();
							leaves.addAll(requestLeaves);
							ResourceTrendBean rBean = leaves
									.get(leaves.size() - 1);
							trendId_leave = rBean.getTrendId() + "";
							createLeaveTime = DateTimeUtil.getLongTime(rBean
									.getCreateTime()) + "";
							listView.setAdapter(leaveAdapter);
						} else {
							leaves.addAll(requestLeaves);
							ResourceTrendBean rBean = leaves
									.get(leaves.size() - 1);
							trendId_leave = rBean.getTrendId() + "";
							createLeaveTime = DateTimeUtil.getLongTime(rBean
									.getCreateTime()) + "";
							leaveAdapter.notifyDataSetChanged();
						}
					}
					if (isRefresh) {
						listView.setSelection(0);
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
		if (HomePgActivity.TYPE_MBER == homePg.sType) {
			if (refresh) {
				trendId_leave = "0";
				createLeaveTime = "0";
			}

			mcResult = APIRequestServers.otherMemberTrends(App.app, homePg.id,
					ResourceTypeEnum.OBJ_MEMBER_RES_LEAVEMESSAGE,
					trendId_leave, createLeaveTime, "20", true);
		} else if (HomePgActivity.TYPE_OPAGE == homePg.sType) {
			if (refresh) {
				trendId_leave = "0";
			}
			mcResult = APIOpenPageRequestServers.openPageResourceTrends(
					App.app, "OBJ_OPEN_PAGE_LEAVEMESSAGE", "true", homePg.id,
					"false", trendId_leave, "true", "20");
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

	@Override
	public void search(String s) {
		// TODO
	}

}
