package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.RemoteAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;

public class ImgGridActivity extends BaseMidMenuActivity {

	private boolean searchState, noMore;
	private boolean isLoading = true;

	private GridView grid;
	private LoadInfoTask task;
	private ArrayList<ResourceBean> photos;
	private RemoteAdapter photoAdapter;
	private TextView textView;

	private String trendId_photo;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_grid_img);
		findViews();
		initData();
	}

	@Override
	void initData() {
		// if (userSelf) {
		// setTitle(MM_IMG, R.drawable.title_menu, null, false);
		// slide.setScrollEnable(true);
		// }else{
		setTitle(MM_IMG, R.drawable.title_fanhui, null, false);
		// slide.setScrollEnable(false);
		// }
		loadInfo(true);
	}

	private void findViews() {
		Bundle b = getIntent().getExtras();
		name = b.getString("name");
		id = b.getString("id");
		sType = b.getInt("type");
		int screenWidth = BaseData.getScreenWidth();
		textView = (TextView) findViewById(R.id.textView);
		grid = (GridView) findViewById(R.id.table);
		grid.setEmptyView(textView);
		grid.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount >= totalItemCount - 2) {
					if (!isLoading && !noMore) {
						loadInfo(false);
					}
				}
			}
		});
		photos = new ArrayList<ResourceBean>();
		photoAdapter = new RemoteAdapter(this, photos, screenWidth, grid, false);
		grid.setAdapter(photoAdapter);
		setMidChosen(2);
	}

	@Override
	protected void onRefreshClick(View v) {
		super.onRefreshClick(v);
		loadInfo(true);
	}

	void loadInfo(boolean isRefresh) {
		textView.setText("正在加载...");
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
					// result = loadSearchResult(cur, isRefresh);
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
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					if (!searchState) {
						MapResourceBean photoResult = (MapResourceBean) mcResult
								.getResult();
						photoAdapter.setAamount(photoResult.getTOTALNUM());
						if (TYPE_MBER == sType) {
							photoAdapter.setFrom(From.MEMBER);
						} else if (TYPE_GROUP == sType) {
							photoAdapter.setFrom(From.GROUP);
						} else if (TYPE_OPAGE == sType) {
							photoAdapter.setFrom(From.OPENPAGE);
						}
						photoAdapter.setId(id);
						ArrayList<ResourceBean> requestInfos2 = (ArrayList<ResourceBean>) photoResult
								.getLIST();
						if (null == requestInfos2 || requestInfos2.size() == 0) {
							if (photos.size() == 0) {
								textView.setText("图库还没有图片哦！");
							} else {
								showTip("最后一页");
							}
							noMore = true;
						} else {
							if (isRefresh || null == trendId_photo
									|| "".equals(trendId_photo)) {
								photos.clear();
								photos.addAll(requestInfos2);
								trendId_photo = String.valueOf(photos.size());
								grid.setAdapter(photoAdapter);
							} else {
								photos.addAll(requestInfos2);
								trendId_photo = String.valueOf(photos.size());
								photoAdapter.notifyDataSetChanged();
							}
						}
					} else {

					}

				}
			}
			isLoading = false;
		}
	}

	private MCResult loadDate(boolean refresh) throws Exception {
		MCResult mcResult = null;
		if (refresh) {
			trendId_photo = "0";
		}
		if (TYPE_MBER == sType) {
			mcResult = APIRequestServers.singleResourceList(
					ImgGridActivity.this, id, ResourceTypeEnum.OBJ_GROUP_PHOTO,
					trendId_photo, "28", false);
		} else if (TYPE_GROUP == sType) {
			mcResult = APIRequestServers.groupResourceLists(
					ImgGridActivity.this, id, ResourceTypeEnum.OBJ_GROUP_PHOTO,
					trendId_photo, "28", false);
		} else if (TYPE_OPAGE == sType) {
			mcResult = APIRequestServers.groupResourceLists(
					ImgGridActivity.this, id,
					ResourceTypeEnum.OBJ_OPEN_PAGE_PHOTO, trendId_photo, "28",
					false);
		}

		return mcResult;
	}
}
