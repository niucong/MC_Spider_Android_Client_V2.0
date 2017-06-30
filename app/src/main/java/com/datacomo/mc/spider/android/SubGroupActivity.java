package com.datacomo.mc.spider.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.FileAdapter;
import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.adapter.InfoSearchedAdapter;
import com.datacomo.mc.spider.android.adapter.RemoteAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;

public class SubGroupActivity extends BasicActionBarActivity implements
		OnTabClickListener, OnLoadMoreListener, OnSearchListener,
		OnClearListener, OnFaceChosenListner {
	protected static final String TAG = "SubGroupActivity";

	private TabLinearLayout mTabLinearLayout;
	private RefreshListView listView;
	private GridView photoTable;
	private ArrayList<ResourceTrendBean> infos;
	private ArrayList<ResourceBean> files;
	private ArrayList<ResourceBean> photos;

	private ArrayList<ResultMessageBean> searchInfos;

	private InfoAdapter infoAdapter;
	private FileAdapter fileAdapter;
	private RemoteAdapter photoAdapter;
	private InfoSearchedAdapter infoSearchAdapter;

	private ArrayList<ResourceTrendBean> moods;
	private InfoAdapter moodAdapter;
	private ArrayList<ResourceTrendBean> leaves;
	private InfoAdapter leaveAdapter;

	private SearchBar sBar;
	private LinearLayout footer;
	private int cut;
	private String groupName, groupId;
	private LoadInfoTask task;
	private boolean isLoading;
	private String trendId_info, trendId_file, trendId_photo, createTime;
	private String trendId_mood, trendId_leave, createMoodTime,
			createLeaveTime;
	private int screenWidth;
	private boolean searchState;
	private int searchStart;

	private RelativeLayout pics;

	private int sType;
	private final int TYPE_MBER = 0;
	private final int TYPE_GROUP = 1;
	private final int TYPE_OPAGE = 2;

	private FacesView faceView;
	private Button sendBtn;
	private EditText edit;
	private ImageView face, talk;
	private LeaveMsgTask lmTask;

	private TextView textView;

	private RadioGroup radioGroup;
	private String messageType = "1";

	/**
	 * 微领地类型：OpenPageId开放主页、GroupId圈子、个人
	 */
	private String homeType;

	private String joinGroupStatus;

	@Override
	protected void onDestroy() {
		enterHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		setContent(R.layout.layout_sub_group);

		findView();
		setView();
	}

	private void findView() {
		textView = (TextView) findViewById(R.id.textView);
		screenWidth = BaseData.getScreenWidth();

		pics = (RelativeLayout) findViewById(R.id.picture);
		mTabLinearLayout = (TabLinearLayout) findViewById(R.id.tabs);
		sBar = (SearchBar) findViewById(R.id.search_bar);
		sBar.setOnSearchListener(this);
		sBar.setOnClearListener(this);
		sBar.setVisibility(View.GONE);

		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadInfo(cut, true);
			}
		});
		photoTable = (GridView) findViewById(R.id.table);
		footer = (LinearLayout) findViewById(R.id.footer);
		photoTable.setEmptyView(footer);
		photoTable.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount >= totalItemCount - 2) {
					if (!isLoading && 2 == cut)
						loadInfo(cut, false);
				}
			}
		});
		listView.setonLoadMoreListener(this);

		radioGroup = (RadioGroup) findViewById(R.id.subgroup_radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_liuyan:
					messageType = "1";
					break;
				case R.id.radio_zixun:
					messageType = "2";
					break;
				case R.id.radio_tousu:
					messageType = "3";
					break;
				case R.id.radio_biaoyang:
					messageType = "4";
					break;
				}
			}
		});

		edit = (EditText) findViewById(R.id.edit);
		edit.setOnClickListener(this);
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					showKeyBoard(v);
					// radioGroup.setVisibility(View.GONE);
				} else {
					// radioGroup.setVisibility(View.VISIBLE);
				}
			}
		});
		sendBtn = (Button) findViewById(R.id.send);
		sendBtn.setOnClickListener(this);
		talk = (ImageView) findViewById(R.id.talk);
		face = (ImageView) findViewById(R.id.face);
		talk.setOnClickListener(this);
		talk.setVisibility(View.GONE);
		face.setOnClickListener(this);
		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);
	}

	/**
	 * 键盘输入
	 * 
	 * @param view
	 */
	private void showKeyBoard(View view) {
		faceView.setVisibility(View.GONE);
		face.setImageResource(R.drawable.icon_face);
		BaseData.showKeyBoard(this, edit);
	}

	private void setView() {
		Bundle b = getIntent().getExtras();
		groupName = b.getString("name");
		groupId = b.getString("id");
		cut = b.getInt("type");
		homeType = b.getString("to");
		joinGroupStatus = b.getString("joinGroupStatus");

		if ("OpenPageId".equals(homeType)) {
			sType = TYPE_OPAGE;
			// setTitle(groupName, R.drawable.title_fanhui,
			// R.drawable.title_home);
			mTabLinearLayout
					.changeText(new String[] { "圈博墙", "文件", "图库", "留言" });
			sBar.setVisibility(View.GONE);
		} else if ("GroupId".equals(homeType)) {
			sType = TYPE_GROUP;
			// TODO setTitle(groupName, R.drawable.title_fanhui,
			// R.drawable.title_header_relase);
			mTabLinearLayout.changeText(new String[] { "圈博墙", "文件", "图库" });
			// sBar.setVisibility(View.VISIBLE);
		} else {
			sType = TYPE_MBER;
			// setTitle(groupName, R.drawable.title_fanhui,
			// R.drawable.title_home);
			mTabLinearLayout.changeText(new String[] { "圈博墙", "文件", "图库", "心情",
					"留言" });
			sBar.setVisibility(View.GONE);
		}
		ab.setTitle(groupName);

		mTabLinearLayout.refresh(cut);
		mTabLinearLayout.setOnTabClickListener(this);

		infos = new ArrayList<ResourceTrendBean>();
		files = new ArrayList<ResourceBean>();
		photos = new ArrayList<ResourceBean>();

		searchInfos = new ArrayList<ResultMessageBean>();

		if ("OpenPageId".equals(homeType)) {
			infoAdapter = new InfoAdapter(this, infos, listView, groupId);
		} else if ("GroupId".equals(homeType)) {
			infoAdapter = new InfoAdapter(this, infos, listView, groupId,
					joinGroupStatus);
		} else {
			infoAdapter = new InfoAdapter(this, infos, listView);
		}

		fileAdapter = new FileAdapter(this, files, joinGroupStatus, true);
		if ("OpenPageId".equals(homeType)) {
			photoAdapter = new RemoteAdapter(this, photos, screenWidth,
					photoTable, true, joinGroupStatus);
		} else {
			photoAdapter = new RemoteAdapter(this, photos, screenWidth,
					photoTable, false, joinGroupStatus);
		}
		infoSearchAdapter = new InfoSearchedAdapter(this, searchInfos,
				listView, groupId, joinGroupStatus);

		if ("MemberId".equals(homeType)) {
			moods = new ArrayList<ResourceTrendBean>();
			moodAdapter = new InfoAdapter(this, moods, listView);
		}

		leaves = new ArrayList<ResourceTrendBean>();
		if ("OpenPageId".equals(homeType)) {
			L.i(TAG, "setView type=" + homeType + ",groupId=" + groupId);
			leaveAdapter = new InfoAdapter(this, leaves, listView, groupId);
		} else {
			leaveAdapter = new InfoAdapter(this, leaves, listView);
		}
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
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.send:
			String text = edit.getText().toString();
			if (null != text && !"".equals(text)) {
				leaveMsg(text);
			} else {
				showTip("内容不能为空");
			}
			break;
		case R.id.face:
			if (faceView.isShown()) {
				showKeyBoard(faceView);
			} else {
				showFace();
			}
			break;
		case R.id.edit:
			showKeyBoard(v);
			break;
		default:
			break;
		}
	}

	/**
	 * 无输入
	 */
	private void hideKeyBoardFace() {
		face.setImageResource(R.drawable.icon_face);
		BaseData.hideKeyBoard(this);
		faceView.setVisibility(View.GONE);
	}

	/**
	 * 表情输入
	 */
	private void showFace() {
		BaseData.hideKeyBoard(this);
		face.setImageResource(R.drawable.keyboardbtn);
		faceView.setVisibility(View.VISIBLE);
		if (faceView.getChildCount() == 0) {
			faceView.setFaces();
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public LoadInfoTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;
			textView.setVisibility(View.GONE);
			setLoadingState(true);
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
					result = loadDate(cur, isRefresh);
				} else {
					result = loadSearchResult(cur, isRefresh);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
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
						if (!searchState) {
							if (isRefresh || null == trendId_info
									|| "".equals(trendId_info)) {
								infos.clear();
							}
							ArrayList<ResourceTrendBean> requestInfos = (ArrayList<ResourceTrendBean>) mcResult
									.getResult();
							if (null == requestInfos
									|| requestInfos.size() == 0) {
								// listView.removeFooter();
								if (infos.size() == 0) {
									textView.setText("目前还没有动态！");
									textView.setVisibility(View.VISIBLE);
								} else {
									showTip("最后一页");
								}
							} else {
								infos.addAll(requestInfos);
								ResourceTrendBean rBean = infos.get(infos
										.size() - 1);
								trendId_info = rBean.getTrendId() + "";
								createTime = DateTimeUtil.getLongTime(rBean
										.getCreateTime()) + "";
								infoAdapter.notifyDataSetChanged();
							}
						} else {
							showSearch(isRefresh, mcResult);
						}
						break;
					case 1:
						if (!searchState) {
							if (isRefresh || null == trendId_file
									|| "".equals(trendId_file)) {
								files.clear();

							}
							MapResourceBean fileResult = (MapResourceBean) mcResult
									.getResult();
							ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) fileResult
									.getLIST();
							if (null == requestInfos1
									|| requestInfos1.size() == 0) {
								// listView.removeFooter();
								if (files.size() == 0) {
									textView.setText("目前还没有文件！");
									textView.setVisibility(View.VISIBLE);
								} else {
									showTip("最后一页");
								}
							} else {
								files.addAll(requestInfos1);
								trendId_file = String.valueOf(files.size());
								fileAdapter.notifyDataSetChanged();
							}
						} else {
							showSearch(isRefresh, mcResult);
						}
						break;
					case 2:
						if (!searchState) {
							if (isRefresh || null == trendId_photo
									|| "".equals(trendId_photo)) {
								photos.clear();

							}
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
							photoAdapter.setId(groupId);
							ArrayList<ResourceBean> requestInfos2 = (ArrayList<ResourceBean>) photoResult
									.getLIST();
							if (null == requestInfos2
									|| requestInfos2.size() == 0) {
								if (photos.size() == 0) {
									textView.setText("目前还没有图片！");
									textView.setVisibility(View.VISIBLE);
									footer.setVisibility(View.GONE);
								} else {
									showTip("最后一页");
									footer.setVisibility(View.GONE);
								}

							} else {
								photos.addAll(requestInfos2);
								trendId_photo = String.valueOf(photos.size());
								photoAdapter.notifyDataSetChanged();
							}
						} else {
							showSearch(isRefresh, mcResult);
						}
						break;
					case 3:
						if (TYPE_OPAGE == sType) {
							showLeaves(isRefresh, mcResult);
						} else {
							showMood(isRefresh, mcResult);
						}
						break;
					case 4:
						showLeaves(isRefresh, mcResult);
						break;
					default:
						break;
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

	private void showSearch(Boolean isRefresh, MCResult mcResult) {
		if (isRefresh || searchStart == 1) {
			searchInfos.clear();
		}
		ResultAll resultInfos0 = (ResultAll) mcResult.getResult();
		if (null == resultInfos0) {
			// listView.removeFooter();
			showTip("没有更多数据！");
		} else {
			List<ResultMessageBean> requestInfos0 = resultInfos0.getRmList();
			if (0 == requestInfos0.size()) {
				// listView.removeFooter();
				showTip("没有更多数据！");
			} else {
				searchInfos.addAll(requestInfos0);
				infoAdapter.notifyDataSetChanged();
			}
		}
	}

	private void showMood(boolean isRefresh, MCResult mcResult) {
		if (isRefresh || null == trendId_mood || "".equals(trendId_mood)) {
			moods.clear();
		}
		@SuppressWarnings("unchecked")
		ArrayList<ResourceTrendBean> requestMoods = (ArrayList<ResourceTrendBean>) mcResult
				.getResult();
		if (null == requestMoods || requestMoods.size() == 0) {
			// listView.removeFooter();
			if (moods.size() == 0) {
				textView.setText("尚未发布过心情！");
				textView.setVisibility(View.VISIBLE);
			} else {
				showTip("最后一页");
			}
		} else {
			moods.addAll(requestMoods);
			ResourceTrendBean rBean = moods.get(moods.size() - 1);
			trendId_mood = rBean.getTrendId() + "";
			createMoodTime = DateTimeUtil.getLongTime(rBean.getCreateTime())
					+ "";
			moodAdapter.notifyDataSetChanged();
		}
	}

	private void showLeaves(boolean isRefresh, MCResult mcResult) {
		if (isRefresh || null == trendId_leave || "".equals(trendId_leave)) {
			leaves.clear();
		}
		@SuppressWarnings("unchecked")
		ArrayList<ResourceTrendBean> requestLeaves = (ArrayList<ResourceTrendBean>) mcResult
				.getResult();
		if (null == requestLeaves || requestLeaves.size() == 0) {
			// listView.removeFooter();
			if (leaves.size() == 0) {
				textView.setText("目前还没有留言！");
				textView.setVisibility(View.VISIBLE);
			} else {
				showTip("最后一页");
			}
		} else {
			leaves.addAll(requestLeaves);
			ResourceTrendBean rBean = leaves.get(leaves.size() - 1);
			trendId_leave = rBean.getTrendId() + "";
			createLeaveTime = DateTimeUtil.getLongTime(rBean.getCreateTime())
					+ "";
			leaveAdapter.notifyDataSetChanged();
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
				createTime = "0";
			}
			if (TYPE_MBER == sType) {
				mcResult = APIRequestServers.otherMemberTrends(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_GROUP_ALL, trendId_info,
						createTime, "10", true);
			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.singleGroupResourceTrends(
						SubGroupActivity.this, groupId, trendId_info, "10",
						true);
			} else if (TYPE_OPAGE == sType) {
				mcResult = APIOpenPageRequestServers.openPageResourceTrends(
						SubGroupActivity.this, "OBJ_GROUP_ALL", "true",
						groupId, "false", trendId_info, "true", "10");
			}
			break;
		case 1:// 文件
			if (refresh) {
				trendId_file = "0";
			}
			if (TYPE_MBER == sType) {
				mcResult = APIRequestServers.singleResourceList(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_GROUP_FILE, trendId_file, "20",
						false);
			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.groupResourceLists(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_GROUP_FILE, trendId_file, "20",
						false);
			} else if (TYPE_OPAGE == sType) {
				mcResult = APIRequestServers.groupResourceLists(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_OPEN_PAGE_FILE, trendId_file,
						"20", false);
			}
			break;
		case 2:// 照片
			if (refresh) {
				trendId_photo = "0";
			}
			if (TYPE_MBER == sType) {
				mcResult = APIRequestServers.singleResourceList(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_GROUP_PHOTO, trendId_photo, "28",
						false);
			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.groupResourceLists(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_GROUP_PHOTO, trendId_photo, "28",
						false);
			} else if (TYPE_OPAGE == sType) {
				mcResult = APIRequestServers.groupResourceLists(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_OPEN_PAGE_PHOTO, trendId_photo,
						"28", false);
			}
			break;
		case 3:// 心情
			if (TYPE_MBER == sType) {
				if (refresh) {
					trendId_mood = "0";
					createMoodTime = "0";
				}
				mcResult = APIRequestServers.otherMemberTrends(
						SubGroupActivity.this, groupId,
						ResourceTypeEnum.OBJ_MEMBER_RES_MOOD, trendId_mood,
						createMoodTime, "10", true);
			} else if (TYPE_OPAGE == sType) {
				if (refresh) {
					trendId_leave = "0";
				}
				mcResult = APIOpenPageRequestServers.openPageResourceTrends(
						SubGroupActivity.this, "OBJ_OPEN_PAGE_LEAVEMESSAGE",
						"true", groupId, "false", trendId_leave, "true", "10");
			}
			break;
		case 4:// 留言
			if (refresh) {
				trendId_leave = "0";
				createLeaveTime = "0";
			}

			mcResult = APIRequestServers.otherMemberTrends(
					SubGroupActivity.this, groupId,
					ResourceTypeEnum.OBJ_MEMBER_RES_LEAVEMESSAGE,
					trendId_leave, createLeaveTime, "10", true);
			break;
		default:
			break;
		}
		return mcResult;
	}

	private MCResult loadSearchResult(int which, boolean refresh)
			throws Exception {
		MCResult mcResult = null;
		if (refresh) {
			searchStart = 1;
		} else {
			searchStart = searchInfos.size();
		}
		switch (which) {
		case 0:// 动态
			if (TYPE_MBER == sType) {

			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.searchResource(
						SubGroupActivity.this, key, groupId, searchStart, 20,
						"sg", "1", "");
			} else if (TYPE_OPAGE == sType) {
				mcResult = APIRequestServers.searchResource(
						SubGroupActivity.this, key, groupId, searchStart, 20,
						"gto", "1", "");
			}
			break;
		case 1:// 文件
			if (refresh) {
				trendId_file = "0";
			}
			if (TYPE_MBER == sType) {

			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.searchResource(
						SubGroupActivity.this, key, groupId, searchStart, 20,
						"gf", "1", "");
			} else if (TYPE_OPAGE == sType) {

			}
			break;
		case 2:// 照片
			if (refresh) {
				trendId_photo = "0";
			}
			if (TYPE_MBER == sType) {

			} else if (TYPE_GROUP == sType) {
				mcResult = APIRequestServers.searchResource(
						SubGroupActivity.this, key, groupId, searchStart, 20,
						"gp", "1", "");
			} else if (TYPE_OPAGE == sType) {

			}
			break;
		default:
			break;
		}
		return mcResult;
	}

	String key;

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		MenuItem m = menu.findItem(R.id.action_search);
		searchView = (SearchView) m.getActionView();
		m.setVisible(true);
		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);
		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);

		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
						.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
				if (mQueryTextView.isShown()
						&& "".equals(mQueryTextView.getText().toString())) {
					cv.setVisibility(View.GONE);
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				key = s;
				search();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
				} else {
					cv.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				clear();
				return false;
			}
		});

		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		setInfo();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
			} else {
				setResult(RESULT_CANCELED);
				finish();
			}
			return true;
		case R.id.action_refresh:
			loadInfo(cut, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler enterHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				showTip(T.ErrStr);
				break;
			case 1:
				Intent intent = new Intent(SubGroupActivity.this,
						CreateGroupTopicActivity.class);
				ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
				GroupEntity entity = new GroupEntity(groupId, groupName, null,
						"", false, false);
				list.add(entity);
				intent.putExtra(BundleKey.TYPE_REQUEST, Type.DEFAULT);
				intent.putExtra("datas", (Serializable) list);
				startActivity(intent);
				break;
			case 2:
				showTip("您不是该圈子成员，请先加入圈子！");
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		listView.onRefreshComplete();
		searchState = false;
		setInfo();
	}

	private void setInfo() {
		if ("OpenPageId".equals(homeType)) {
			if (cut == 0) {
				// sBar.setVisibility(View.VISIBLE);
			} else {
				sBar.setVisibility(View.GONE);
			}
		} else if ("GroupId".equals(homeType)) {
			// if (cut != 2) {
			// sBar.setVisibility(View.VISIBLE);
			// }
			if (cut == 0) {
				// sBar.setVisibility(View.VISIBLE);
			} else {
				sBar.setVisibility(View.GONE);
			}
		}
		switch (cut) {
		case 0:
			showList();
			listView.setAdapter(infoAdapter);
			if (infos.size() == 0) {
				loadInfo(cut, true);
			} else {
				textView.setVisibility(View.GONE);
			}
			break;
		case 1:
			showList();
			listView.setAdapter(fileAdapter);
			if (files.size() == 0) {
				loadInfo(cut, true);
			} else {
				textView.setVisibility(View.GONE);
			}
			break;
		case 2:
			showTable();
			photoTable.setAdapter(photoAdapter);
			loadInfo(cut, true);
			break;
		case 3:
			showList();
			if (TYPE_OPAGE == sType) {
				listView.setAdapter(leaveAdapter);
				if (leaves.size() == 0) {
					loadInfo(cut, true);
				} else {
					textView.setVisibility(View.GONE);
				}
			} else {
				listView.setAdapter(moodAdapter);
				if (moods.size() == 0) {
					loadInfo(cut, true);
				} else {
					textView.setVisibility(View.GONE);
				}
			}
			break;
		case 4:
			showList();
			listView.setAdapter(leaveAdapter);
			if (leaves.size() == 0) {
				loadInfo(cut, true);
			} else {
				textView.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
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

	class LeaveMsgTask extends AsyncTask<Void, Integer, MCResult> {
		String msg;

		public LeaveMsgTask(String message) {
			msg = message;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult result = null;
			try {
				result = APIOpenPageRequestServers.leaveMessageForOpenPage(
						SubGroupActivity.this, "" + groupId, msg, messageType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null != result && 1 == result.getResultCode()) {
				showTip("已发送！");
				edit.setText("");
				setInfo();
			} else {
				showTip(T.ErrStr);
			}
		}
	}

	@Override
	public void onSearch(String keyWords) {
		search();
	}

	@Override
	public void onClear(String keyWords) {
		clear();
	}

	private void search() {
		showList();
		listView.setAdapter(infoSearchAdapter);
		searchState = true;
		loadInfo(cut, true);
	}

	private void clear() {
		searchState = false;
		setInfo();
		loadInfo(cut, true);
	}

	private void leaveMsg(String msg) {
		if (null != lmTask && lmTask.getStatus() == Status.RUNNING) {
			return;
		}
		lmTask = new LeaveMsgTask(msg);
		lmTask.execute();
		hideKeyBoardFace();
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (faceView.isShown()) {
				hideKeyBoardFace();
				return true;
			}
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, edit, text, resId);
	}
}
