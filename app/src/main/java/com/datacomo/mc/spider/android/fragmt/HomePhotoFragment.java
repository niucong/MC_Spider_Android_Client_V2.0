package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.RemoteAdapter;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;

@SuppressLint("ValidFragment")
public class HomePhotoFragment extends BaseGroupFragment {

	private boolean searchState, noMore;
	private boolean isLoading = true;

	private GridView grid;
	private LoadInfoTask task;
	private ArrayList<ResourceBean> photos;
	private RemoteAdapter photoAdapter;
	private TextView textView;

	private String trendId_photo;

	private HomePgActivity homePg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homePg = (HomePgActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_grid_img,
				null);
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
			if (homePg.menu.findItem(R.id.action_write_mood) != null)
				homePg.menu.findItem(R.id.action_write_mood).setVisible(false);
			if (homePg.menu.findItem(R.id.action_more) != null)
				homePg.menu.findItem(R.id.action_more).setVisible(false);
		}

		int screenWidth = BaseData.getScreenWidth();
		textView = (TextView) view.findViewById(R.id.textView);
		grid = (GridView) view.findViewById(R.id.table);
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
		photoAdapter = new RemoteAdapter(homePg, photos, screenWidth, grid,
				false);
		grid.setAdapter(photoAdapter);
		// setMidChosen(2);
		loadInfo(true);
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
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
			homePg.setLoadingState(true);
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
			homePg.setLoadingState(false);
			if (null == mcResult) {
				homePg.showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					homePg.showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					if (!searchState) {
						MapResourceBean photoResult = (MapResourceBean) mcResult
								.getResult();
						photoAdapter.setAamount(photoResult.getTOTALNUM());
						if (HomePgActivity.TYPE_MBER == homePg.sType) {
							photoAdapter.setFrom(From.MEMBER);
						} else if (HomePgActivity.TYPE_GROUP == homePg.sType) {
							photoAdapter.setFrom(From.GROUP);
						} else if (HomePgActivity.TYPE_OPAGE == homePg.sType) {
							photoAdapter.setFrom(From.OPENPAGE);
						}
						photoAdapter.setId(homePg.id);
						ArrayList<ResourceBean> requestInfos2 = (ArrayList<ResourceBean>) photoResult
								.getLIST();
						if (null == requestInfos2 || requestInfos2.size() == 0) {
							if (photos.size() == 0) {
								textView.setText("图库还没有图片哦！");
							} else {
								homePg.showTip("最后一页");
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
		if (HomePgActivity.TYPE_MBER == homePg.sType) {
			mcResult = APIRequestServers.singleResourceList(homePg, homePg.id,
					ResourceTypeEnum.OBJ_GROUP_PHOTO, trendId_photo, "28",
					false);
		} else if (HomePgActivity.TYPE_GROUP == homePg.sType) {
			mcResult = APIRequestServers.groupResourceLists(homePg, homePg.id,
					ResourceTypeEnum.OBJ_GROUP_PHOTO, trendId_photo, "28",
					false);
		} else if (HomePgActivity.TYPE_OPAGE == homePg.sType) {
			mcResult = APIRequestServers.groupResourceLists(homePg, homePg.id,
					ResourceTypeEnum.OBJ_OPEN_PAGE_PHOTO, trendId_photo, "28",
					false);
		}

		return mcResult;
	}

	@Override
	public void search(String s) {
		// TODO
	}
}
