package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.adapter.RemoteAdapter;
import com.datacomo.mc.spider.android.adapter.SelectFileAdapter;
import com.datacomo.mc.spider.android.adapter.SelectTopicAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class SelectActivity extends BasicMenuActivity implements
		OnTabClickListener, OnLoadMoreListener {

	private TabLinearLayout mTabLinearLayout;
	private RefreshListView listView;
	private GridView photoTable;
	private List<ResourceBean> infos;
	private List<ResourceBean> files;
	private List<ResourceBean> photos;
	private SelectTopicAdapter infoAdapter;
	private SelectFileAdapter fileAdapter;
	private RemoteAdapter photoAdapter;
	private LinearLayout footer, header;
	private int cut;
	private LoadInfoTask task;
	private boolean isLoading;
	private String trendId_info = "0", trendId_file = "0", trendId_photo = "0";
	private int screenWidth;
	private LinearLayout pics;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "10");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_COLLECTION;
		titleName = "收藏";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_select, null);
		fl.addView(rootView);

		findView();
		setView();
	}

	private void findView() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;

		listView = (RefreshListView) findViewById(R.id.listview);
		pics = (LinearLayout) findViewById(R.id.picture);
		mTabLinearLayout = (TabLinearLayout) findViewById(R.id.tabs);

		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadInfo(cut, true);
			}
		});

		photoTable = (GridView) findViewById(R.id.table);
		header = (LinearLayout) findViewById(R.id.header);
		footer = (LinearLayout) findViewById(R.id.footer);
		photoTable.setEmptyView(footer);
		photoTable.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int sum = view.getAdapter().getCount();
				if (scrollState == SCROLL_STATE_IDLE
						&& sum > 9
						&& view.getFirstVisiblePosition()
								+ view.getVisibility() == sum - 1) {
					loadInfo(cut, false);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		listView.setonLoadMoreListener(this);
	}

	private void setView() {
		mTabLinearLayout.changeText(new String[] { "圈博", "文件", "照片" });
		mTabLinearLayout.refresh(cut);
		mTabLinearLayout.setOnTabClickListener(this);
	}

	void loadInfo(int which, boolean isRefresh) {
		try {
			cut = which;
			stopTask();
			task = new LoadInfoTask(which, isRefresh);
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
		private final int cur;
		private boolean isRefresh;

		public LoadInfoTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;
			setLoadingState(true);
			if (cur == 2) {
				if (isRefresh) {
					header.setVisibility(View.VISIBLE);
				} else {
					footer.setVisibility(View.VISIBLE);
				}
			} else {
				if (isRefresh) {
					listView.refreshing();
				} else {
					listView.showLoadFooter();
				}
			}
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = loadDate(cur, isRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (cur == 2) {
				if (isRefresh) {
					header.setVisibility(View.GONE);
				} else {
					footer.setVisibility(View.GONE);
				}
			} else {
				if (isRefresh) {
					listView.refreshed();
				} else {
					listView.showFinishLoadFooter();
				}
			}
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (cur == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					switch (cur) {
					case 0:
						if (isRefresh || null == trendId_info
								|| "".equals(trendId_info)
								|| "0".equals(trendId_info)) {
							infos.clear();
						}

						MapResourceBean infoResult = (MapResourceBean) mcResult
								.getResult();
						ArrayList<ResourceBean> requestInfos = (ArrayList<ResourceBean>) infoResult
								.getLIST();
						if (null == requestInfos || requestInfos.size() == 0) {
							if (null == infos || infos.size() == 0) {
								showTip("暂无收藏圈博！");
							} else {
								showTip("最后一页");
							}
						} else {
							infos.addAll(requestInfos);
							trendId_info = String.valueOf(infos.size());
							infoAdapter.notifyDataSetChanged();
						}
						listView.onRefreshComplete();
						break;
					case 1:
						if (isRefresh || null == trendId_file
								|| "".equals(trendId_file)
								|| "0".equals(trendId_file)) {
							files.clear();
						}
						MapResourceBean fileResult = (MapResourceBean) mcResult
								.getResult();
						ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) fileResult
								.getLIST();
						if (null == requestInfos1 || requestInfos1.size() == 0) {
							if (null == files || files.size() == 0) {
								showTip("暂无收藏文件！");
							} else {
								showTip("最后一页");
							}
						} else {
							files.addAll(requestInfos1);
							trendId_file = String.valueOf(files.size());
							fileAdapter.notifyDataSetChanged();
						}
						listView.onRefreshComplete();
						break;
					case 2:
						if (isRefresh || null == trendId_photo
								|| "".equals(trendId_photo)
								|| "0".equals(trendId_photo)) {
							photos.clear();
						}

						MapResourceBean photoResult = (MapResourceBean) mcResult
								.getResult();
						ArrayList<ResourceBean> requestInfos2 = (ArrayList<ResourceBean>) photoResult
								.getLIST();
						photoAdapter.setAamount(photoResult.getTOTALNUM());
						photoAdapter.setFrom(From.ENSHRINED);
						if (null == requestInfos2 || requestInfos2.size() == 0) {
							footer.setVisibility(View.GONE);
							if (null == photos || photos.size() == 0) {
								showTip("暂无收藏图片！");
							} else {
								showTip("最后一页");
							}
						} else {
							photos.addAll(requestInfos2);
							trendId_photo = String.valueOf(photos.size());
							photoAdapter.notifyDataSetChanged();
						}
						break;
					default:
						break;
					}

					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			isLoading = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLoading = false;
	}

	private MCResult loadDate(int which, boolean refresh) throws Exception {
		MCResult mcResult = null;
		switch (which) {
		case 0:// 动态
			if (refresh) {
				trendId_info = "0";
			}
			mcResult = APIRequestServers.collectionResourceList(
					SelectActivity.this, ResourceTypeEnum.OBJ_GROUP_QUUBO,
					trendId_info, "10", false);
			break;
		case 1:// 文件
			if (refresh) {
				trendId_file = "0";
			}
			mcResult = APIRequestServers.collectionResourceList(
					SelectActivity.this, ResourceTypeEnum.OBJ_GROUP_FILE,
					trendId_file, "20", false);
			break;
		case 2:// 照片
			if (refresh) {
				trendId_photo = "0";
			}
			mcResult = APIRequestServers.collectionResourceList(
					SelectActivity.this, ResourceTypeEnum.OBJ_GROUP_PHOTO,
					trendId_photo, "28", false);
			break;
		default:
			break;
		}
		return mcResult;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			setInfo();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		try {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
			menu.findItem(R.id.action_message).setVisible(drawerOpen);
			menu.findItem(R.id.action_write).setVisible(drawerOpen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void refresh() {
		loadInfo(cut, true);
	}

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		listView.onRefreshComplete();
		setInfo();
	}

	private void setInfo() {
		switch (cut) {
		case 0:
			showList();
			if (infoAdapter == null) {
				try {
					infos = LocalDataService
							.getInstense()
							.getLocCollection(this, LocalDataService.TXT_STAR,
									ResourceTypeEnum.OBJ_GROUP_QUUBO).getLIST();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (infos == null) {
					infos = new ArrayList<ResourceBean>();
				}
				infoAdapter = new SelectTopicAdapter(this, infos, listView);
				listView.setAdapter(infoAdapter);
				loadInfo(cut, true);
			} else {
				listView.setAdapter(infoAdapter);
			}
			break;
		case 1:
			showList();
			if (fileAdapter == null) {
				try {
					files = LocalDataService
							.getInstense()
							.getLocCollection(this, LocalDataService.TXT_STAR,
									ResourceTypeEnum.OBJ_GROUP_FILE).getLIST();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (files == null) {
					files = new ArrayList<ResourceBean>();
				}
				fileAdapter = new SelectFileAdapter(this, files);
				listView.setAdapter(fileAdapter);
				loadInfo(cut, true);
			} else {
				listView.setAdapter(fileAdapter);
			}
			break;
		case 2:
			showTable();
			if (photoAdapter == null) {
				MapResourceBean photoResult = null;
				int num = 0;
				try {
					photoResult = LocalDataService.getInstense()
							.getLocCollection(this, LocalDataService.TXT_STAR,
									ResourceTypeEnum.OBJ_GROUP_PHOTO);
					photos = photoResult.getLIST();
					num = photoResult.getTOTALNUM();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (photos == null) {
					photos = new ArrayList<ResourceBean>();
				}
				photoAdapter = new RemoteAdapter(this, photos, screenWidth,
						photoTable, false);
				photoAdapter.setAamount(num);
				photoAdapter.setFrom(From.ENSHRINED);
				photoTable.setAdapter(photoAdapter);
				loadInfo(cut, true);
			} else {
				photoTable.setAdapter(photoAdapter);
			}
			break;
		default:
			break;
		}
		loadInfo(cut, true);
	}

	private void showList() {
		listView.setVisibility(View.VISIBLE);
		pics.setVisibility(View.GONE);
	}

	private void showTable() {
		listView.setVisibility(View.GONE);
		pics.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadInfo(cut, false);
	}
}
