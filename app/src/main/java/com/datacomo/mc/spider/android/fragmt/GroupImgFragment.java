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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.RemoteAdapter;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;

public class GroupImgFragment extends BaseGroupFragment {

	private TextView tv_num;
	private GridView mGrid;
	private LinearLayout footer;

	private ArrayList<ResourceBean> photos;
	private RemoteAdapter adapter;

	private ArrayList<ResourceBean> squubos;
	private RemoteAdapter sadapter;

	private String groupId;
	private HomeGpActivity mActivity;
	private String trendId_photo, sstart;
	private LoadPhotoTask task;
	private String memberState;
	private int screenWidth;
	private boolean searchState, isLoading;

	private int num, snum;
	private final String source = "共 <font color=#5aa11f>%d</font> 张图片";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupId = getArguments().getString("groupId");
		memberState = getArguments().getString("memberState");
		screenWidth = BaseData.getScreenWidth();
		mActivity = (HomeGpActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.frag_group_photo, null);
		mGrid = (GridView) view.findViewById(R.id.table);
		footer = (LinearLayout) view.findViewById(R.id.footer);
		tv_num = (TextView) view.findViewById(R.id.num);
		mGrid.setEmptyView(footer);
		mGrid.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount >= totalItemCount - 2) {
					if (!isLoading)
						loadInfo(false);
				}
			}
		});
		if (photos == null) {
			photos = new ArrayList<ResourceBean>();
		}
		if (null == adapter) {
			adapter = new RemoteAdapter(getActivity(), photos, screenWidth,
					mGrid, false, memberState, 3, ImageSizeEnum.THREE_HUNDRED,
					ScaleType.CENTER_CROP);
		}
		mGrid.setAdapter(adapter);

		if (squubos == null) {
			squubos = new ArrayList<ResourceBean>();
		}
		if (null == sadapter) {
			sadapter = new RemoteAdapter(getActivity(), squubos, screenWidth,
					mGrid, false, memberState, 3, ImageSizeEnum.THREE_HUNDRED,
					ScaleType.CENTER_CROP);
		}
		if (photos.size() == 0) {
			loadInfo(true);
		} else {
			if (searchState) {
				setNums(snum);
			} else {
				setNums(num);
			}
			mGrid.setSelection(getArguments().getInt("lastPosition"));
		}
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		int lastPositon = mGrid.getFirstVisiblePosition();
		getArguments().putInt("lastPositon", lastPositon);
	}

	class LoadPhotoTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadPhotoTask(boolean isRefresh) {
			this.isRefresh = isRefresh;
			mActivity.setLoadingState(true);
			isLoading = true;
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
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				if (searchState) {
					showSearchResult(isRefresh, mcResult);
				} else {
					showResult(isRefresh, mcResult);
				}
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
		if (isRefresh || null == trendId_photo || "".equals(trendId_photo)) {
			photos.clear();
		}
		MapResourceBean photoResult = (MapResourceBean) mcResult.getResult();
		adapter.setAamount(photoResult.getTOTALNUM());
		adapter.setFrom(From.GROUP);
		adapter.setId(groupId);

		num = photoResult.getTOTALNUM();
		setNums(num);

		ArrayList<ResourceBean> requestInfos2 = (ArrayList<ResourceBean>) photoResult
				.getLIST();
		if (null == requestInfos2 || requestInfos2.size() == 0) {
			if (photos.size() == 0) {
				showTip("目前还没有图片");
				footer.setVisibility(View.GONE);
			} else {
				showTip("已到达底部");
			}
		} else {
			photos.addAll(requestInfos2);
			trendId_photo = String.valueOf(photos.size());
			adapter.notifyDataSetChanged();
		}
	}

	private void showSearchResult(boolean isRefresh, MCResult mcResult) {
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
				showTip("没有搜索到相应照片！");
			} else {
				showTip("最后一页");
			}
		} else {
			squubos.addAll(requestInfos1);
			sstart = String.valueOf(squubos.size());
			sadapter.notifyDataSetChanged();
		}
	}

	private void showTip(String text) {
		if (this.isVisible()) {
			mActivity.showTip(text);
		}
	}

	private MCResult loadData(boolean refresh) throws Exception {
		if (refresh) {
			trendId_photo = "0";
		}
		return APIRequestServers.groupResourceLists(getActivity(), groupId,
				ResourceTypeEnum.OBJ_GROUP_PHOTO, trendId_photo, "28", false);
	}

	private MCResult loadSearchResult(boolean refresh) throws Exception {
		if (refresh) {
			sstart = "0";
		} else {
			sstart = "" + squubos.size();
		}
		return APIRequestServers.searchGroupResourceLists(getActivity(),
				groupId, keyword, ResourceTypeEnum.OBJ_GROUP_PHOTO, sstart,
				"20", false);
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadPhotoTask(isRefresh);
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
		mGrid.setAdapter(sadapter);
		searchState = true;
		loadInfo(true);
		setNums(snum);
	}

	private void clear() {
		mGrid.setAdapter(adapter);
		searchState = false;
		setNums(num);
	}
}
