package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class MoodListActivity extends BaseMidMenuActivity implements
		OnRefreshListener, OnLoadMoreListener {
	private TextView textView;
	private RefreshListView listView;

	private LoadInfoTask task;
	private boolean noMore;
	private boolean isLoading = true;
	private ArrayList<ResourceTrendBean> moods;
	private InfoAdapter moodAdapter;
	private String trendId_mood, createMoodTime;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		setContent(R.layout.layout_file_list);
		findViews();
		initData();
	}

	@Override
	void initData() {
		if (userSelf) {
			setTitle(MM_MOOD, R.drawable.title_fanhui,
					R.drawable.title_edit_mood);
//			slide.setScrollEnable(true);
		} else {
			setTitle(MM_MOOD, R.drawable.title_fanhui, null);
//			slide.setScrollEnable(false);
		}
		loadInfo(true);
	}

	private void findViews() {
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("");
		textView.setVisibility(View.GONE);
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		moods = new ArrayList<ResourceTrendBean>();
		moodAdapter = new InfoAdapter(this, moods, listView);
		listView.setAdapter(moodAdapter);
		setMidChosen(5);

	}

	@Override
	protected void onRefreshClick(View v) {
		super.onRefreshClick(v);
		loadInfo(true);
	}

	@Override
	protected void onRightClick(View v) {
		Intent mIntent = new Intent(MoodListActivity.this, EditActivity.class);
		Bundle b = new Bundle();
		b.putString("id", id);
		b.putInt("type", TYPE_MBER);
		b.putInt("typedata", EditActivity.TYPE_MOOD);
		mIntent.putExtras(b);
		startActivityForResult(mIntent, EditActivity.TYPE_MOOD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理拍照返回数据
		if (resultCode == RESULT_OK) {
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
			hintHeaderProgress();
			listView.showFinishLoadFooter();
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (1 == mcResult.getResultCode()) {
					ArrayList<ResourceTrendBean> requestMoods = (ArrayList<ResourceTrendBean>) mcResult
							.getResult();
					if (null == requestMoods || requestMoods.size() == 0) {
						if (moods.size() == 0) {
							textView.setVisibility(View.VISIBLE);
							textView.setText("还没有心情哦！");
						} else {
							showTip("最后一页");
						}
						noMore = true;
					} else {
						if (isRefresh || null == trendId_mood
								|| "".equals(trendId_mood)) {
							moods.clear();
							moods.addAll(requestMoods);
							ResourceTrendBean rBean = moods
									.get(moods.size() - 1);
							trendId_mood = rBean.getTrendId() + "";
							createMoodTime = DateTimeUtil.getLongTime(rBean
									.getCreateTime()) + "";
							listView.setAdapter(moodAdapter);
						} else {
							moods.addAll(requestMoods);
							ResourceTrendBean rBean = moods
									.get(moods.size() - 1);
							trendId_mood = rBean.getTrendId() + "";
							createMoodTime = DateTimeUtil.getLongTime(rBean
									.getCreateTime()) + "";
							moodAdapter.notifyDataSetChanged();
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
		if (TYPE_MBER == sType) {
			if (refresh) {
				trendId_mood = "0";
				createMoodTime = "0";
			}
			mcResult = APIRequestServers.otherMemberTrends(
					MoodListActivity.this, id,
					ResourceTypeEnum.OBJ_MEMBER_RES_MOOD, trendId_mood,
					createMoodTime, "20", true);
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
