package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.adapter.OpenGroupAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class OpenHomePage extends BasicMenuActivity implements
		OnTabClickListener, OnLoadMoreListener, OnRefreshListener {
	private final String TAG = "OpenHomePage";

	private RefreshListView list1, list2;
	private TabLinearLayout tabs2;
	private InfoAdapter infoAdapter;
	private OpenGroupAdapter groupAdapter;
	private int cut;
	private boolean page = true;
	private boolean isLoading;
	private String id_infos, id_loves, id_hots;
	private String idg_infos = "0", idg_loves = "0", idg_hots = "0";
	private ArrayList<ResourceTrendBean> datas;
	@SuppressWarnings("rawtypes")
	private ArrayList[] data = new ArrayList[3];
	private ArrayList<GroupBean> groups;
	@SuppressWarnings("rawtypes")
	private ArrayList[] group = new ArrayList[3];
	private LoadInfoTask task;
	private LoadGroupTask task2;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "11");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_OPEN;
		titleName = "开放主页-动态";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_open_page, null);
		fl.addView(rootView);

		findViews();
		setView();

	}

	private void findViews() {
		list1 = (RefreshListView) findViewById(R.id.infos);
		list2 = (RefreshListView) findViewById(R.id.clubs);
		tabs2 = (TabLinearLayout) findViewById(R.id.tabs2);

		datas = new ArrayList<ResourceTrendBean>();
		infoAdapter = new InfoAdapter(OpenHomePage.this, datas, list1);
		list1.setAdapter(infoAdapter);

		groups = new ArrayList<GroupBean>();
		groupAdapter = new OpenGroupAdapter(OpenHomePage.this, groups, list2);
		list2.setAdapter(groupAdapter);
	}

	private void setView() {
		tabs2.changeText(new String[] { "随便看看", "我关注的", "热门的" });
		tabs2.refresh(0);
		tabs2.setOnTabClickListener(this);

		list1.setonRefreshListener(this);
		list1.setonLoadMoreListener(this);
		list2.setonRefreshListener(this);
		list2.setonLoadMoreListener(this);
	}

	@Override
	public void onRefresh() {
		loadDatas(true);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.openpages, menu);
		this.menu = menu;
		// if (isFrist) {
		// isFrist = false;
		// loadDatas(true);
		// }
		setDatas(0);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		try {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
			menu.findItem(R.id.action_openpages).setVisible(!drawerOpen);
			menu.findItem(R.id.action_message).setVisible(drawerOpen);
			menu.findItem(R.id.action_write).setVisible(drawerOpen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_openpages:
			page = !page;
			if (page) {
				titleName = "开放主页-动态";
				ab.setTitle(titleName);
				item.setIcon(R.drawable.action_openpages_1);
				list1.setVisibility(View.VISIBLE);
				list2.setVisibility(View.GONE);
				tabs2.changeSpecilText("随便看看", 0);
				list2.onRefreshComplete();
			} else {
				titleName = "开放主页-主页";
				ab.setTitle(titleName);
				item.setIcon(R.drawable.action_openpages_2);
				list2.setVisibility(View.VISIBLE);
				list1.setVisibility(View.GONE);
				tabs2.changeSpecilText("最新入驻", 0);
				list1.onRefreshComplete();
			}
			tabs2.refresh(0, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void refresh() {
		loadDatas(true);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			loadDatas(false);
		}
	}

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		stopTask();
		setDatas(cut);
	}

	private void setDatas(int index) {
		L.i(TAG, "setDatas page=" + page + ",index=" + index);
		if (page) {
			datas.clear();
			ArrayList<ResourceTrendBean> beans = getCertainData(index);
			if (null == beans || 0 == beans.size()) {
				L.i(TAG, "setDatas beans=null");
				try {
					String fileName = "";
					if (index == 0) {
						fileName = LocalDataService.TXT_OPENALL;
					} else if (index == 2) {
						fileName = LocalDataService.TXT_OPENHOT;
					} else if (index == 1) {
						fileName = LocalDataService.TXT_OPENATTEN;
					}
					beans = LocalDataService.getInstense().getLocTrends(
							App.app, fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (beans == null)
					beans = new ArrayList<ResourceTrendBean>();
				L.i(TAG, "setDatas size=" + beans.size());
				loadDatas(true);
			}
			datas.addAll(beans);
			infoAdapter.notifyDataSetChanged();
		} else {
			groups.clear();
			ArrayList<GroupBean> beans = getCertainGroup(index);
			if (null == beans || 0 == beans.size()) {
				L.d(TAG, "setDatas beans=null");
				try {
					String fileName = "";
					if (index == 0) {
						fileName = LocalDataService.TXT_OPENGROUPNEW;
					} else if (index == 2) {
						fileName = LocalDataService.TXT_OPENGROUPHOT;
					} else if (index == 1) {
						fileName = LocalDataService.TXT_OPENGROUPATTEN;
					}
					beans = LocalDataService.getInstense().getLocGroupBeans(
							App.app, fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (beans == null)
					beans = new ArrayList<GroupBean>();
				L.d(TAG, "setDatas size=" + beans.size());
				loadDatas(true);
			}
			groups.addAll(beans);
			groupAdapter.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ResourceTrendBean> getCertainData(int which) {
		return data[which];
	}

	@SuppressWarnings("unchecked")
	private ArrayList<GroupBean> getCertainGroup(int which) {
		return group[which];
	}

	private void loadDatas(boolean refresh) {
		stopTask();
		if (page) {
			task = new LoadInfoTask(refresh, cut);
			task.execute();
		} else {
			task2 = new LoadGroupTask(refresh, cut);
			task2.execute();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}

		if (null != task2 && task2.getStatus() == Status.RUNNING) {
			task2.cancel(true);
		}
	}

	class LoadInfoTask extends AsyncTask<Void, Integer, MCResult> {
		private boolean isRefresh;
		private int which;

		public LoadInfoTask(boolean isRefresh, int which) {
			this.isRefresh = isRefresh;
			this.which = which;
			signLoad(isRefresh);
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = getData(which, isRefresh);
			return mc;
		}

		private MCResult getData(int c, boolean isRefresh) {
			MCResult mcResult = null;
			switch (c) {
			case 0:
				if (isRefresh) {
					id_infos = "0";
				}
				try {
					mcResult = APIOpenPageRequestServers
							.openPageResourceTrends(OpenHomePage.this,
									"OBJ_GROUP_ALL", "false", "0", "true",
									id_infos, "true", "10");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh) {
					id_loves = "0";
				}
				try {
					mcResult = APIOpenPageRequestServers
							.myAttentionOpenPageResourceTrends(
									OpenHomePage.this, id_loves, "10", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh) {
					id_hots = "0";
				}
				try {
					mcResult = APIOpenPageRequestServers
							.popOpenPageResourceTrends(OpenHomePage.this,
									id_hots, "10", true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mcResult;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			signLoadEnd(isRefresh);
			if (null == result || 1 != result.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				if (isRefresh) {
					datas.clear();
				}
				data[which] = (ArrayList<ResourceTrendBean>) result.getResult();
				if (null != data[which]) {
					if (0 == data[which].size()) {
						if (isRefresh) {
							showTip("亲，先随便看看吧，看到感兴趣的主页赶紧关注哦！");
						} else {
							showTip("您关注的主页就是这些了！");
						}
					} else {
						datas.addAll(data[which]);
						ResourceTrendBean resBean = datas.get(datas.size() - 1);
						if (null != resBean) {
							resetTrendId(0, which, resBean.getTrendId() + "");
						}
					}
				} else {
					if (isRefresh) {
						showTip("亲，先随便看看吧，看到感兴趣的主页赶紧关注哦！");
					} else {
						showTip("您关注的主页就是这些了！");
					}
				}
				infoAdapter.notifyDataSetChanged();
			}
		}
	}

	private void resetTrendId(int page, int which2, String id) {
		if (0 == page) {
			switch (which2) {
			case 0:
				id_infos = id;
				break;
			case 1:
				id_loves = id;
				break;
			case 2:
				id_hots = id;
				break;
			}
		} else if (1 == page) {
			switch (which2) {
			case 0:
				idg_infos = id;
				break;
			case 1:
				idg_hots = id;
				break;
			case 2:
				idg_loves = id;
				break;
			}
		}

	}

	class LoadGroupTask extends AsyncTask<Void, Integer, MCResult> {
		private boolean isRefresh;
		private int which;

		public LoadGroupTask(boolean isRefresh, int which) {
			this.isRefresh = isRefresh;
			this.which = which;
			signLoad(isRefresh);
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				String type = "1";
				String id_groups = "0";
				if (0 == which) {
					type = "1";
					if (isRefresh) {
						idg_infos = "0";
					}
					id_groups = idg_infos;
				} else if (1 == which) {
					type = "3";
					if (isRefresh) {
						idg_hots = "0";
					}
					id_groups = idg_hots;
				} else if (2 == which) {
					type = "2";
					if (isRefresh) {
						idg_loves = "0";
					}
					id_groups = idg_loves;
				}
				mc = APIOpenPageRequestServers.openPageList(OpenHomePage.this,
						type, id_groups, "20", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			signLoadEnd(isRefresh);
			if (null == result || 1 != result.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				if (isRefresh) {
					groups.clear();
				}
				group[which] = (ArrayList<GroupBean>) result.getResult();
				if (null != group[which]) {
					if (0 == group[which].size()) {
						if (isRefresh) {
							showTip("亲，先随便看看吧，看到感兴趣的主页赶紧关注哦！");
						} else {
							showTip("您关注的主页就是这些了！");
						}
					} else {
						groups.addAll(group[which]);
						resetTrendId(1, which, groups.size() + "");
					}
				} else {
					if (isRefresh) {
						showTip("亲，先随便看看吧，看到感兴趣的主页赶紧关注哦！");
					} else {
						showTip("您关注的主页就是这些了！");
					}
				}
				groupAdapter.notifyDataSetChanged();
			}
		}
	}

	private void signLoad(boolean isRefresh) {
		isLoading = true;
		setLoadingState(true);
		if (page) {
			if (isRefresh) {
				list1.refreshing();
			} else {
				list1.showLoadFooter();
			}
		} else {
			if (isRefresh) {
				list2.refreshing();
			} else {
				list2.showLoadFooter();
			}
		}
	}

	private void signLoadEnd(boolean isRefresh) {
		isLoading = false;
		setLoadingState(false);
		if (page) {
			if (isRefresh) {
				list1.refreshed();
			} else {
				list1.showFinishLoadFooter();
			}
		} else {
			if (isRefresh) {
				list2.refreshed();
			} else {
				list2.showFinishLoadFooter();
			}
		}
	}

}
