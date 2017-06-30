package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnCloseListener;
import com.datacomo.mc.spider.android.adapter.AutoWordAdapter;
import com.datacomo.mc.spider.android.adapter.FindResultAdapter;
import com.datacomo.mc.spider.android.adapter.GroupCheckAdapter;
import com.datacomo.mc.spider.android.adapter.InfoSearchedAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.SearchRecordBean;
import com.datacomo.mc.spider.android.db.SearchRecordService;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.ResultAll;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.net.been.map.MapMemberBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class SearchActivity extends BasicActionBarActivity implements
		OnClickListener, OnTabClickListener, OnRefreshListener,
		OnLoadMoreListener, OnClearListener {// , OnTextChosenListener
												// ,OnSearchListener
	private static String TAG = "SearchActivity";
	private SearchBar searchBar;
	private TabLinearLayout tabs;
	private TextView cancel;
	private RefreshListView listView;

	private ArrayList<String> autoWords;
	private AutoWordAdapter adapter;
	private ArrayList<ResultMessageBean> infos;
	private ArrayList<MemberBean> mbers;
	private ArrayList<GroupBasicBean> groups;
	private InfoSearchedAdapter infoAdapter;
	// private MemberAdapter mberAdapter;
	private FindResultAdapter findResultAdapter;
	private GroupCheckAdapter groupAdapter;
	private String trendId_mber, trendId_gourp;
	private int trendId_info = 1;
	private EditText edit;
	private ImageView delete;
	private LoadSearchTask task;
	private int cut;
	// private boolean inputStatus;
	private String key;
	private SearchRecordService searchRecord;
	private boolean isLoading;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		setContentView(R.layout.layout_searchs);

		ab.setTitle("");
		// ActionBar ab = getActionBar();
		// ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
		// | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
		// ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// LayoutInflater inflater = (LayoutInflater)
		// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View view = inflater.inflate(R.layout.search_view, null);
		// SearchView sv = (SearchView) view.findViewById(R.id.search_view);
		// // sv.onActionViewExpanded();
		// sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		// @Override
		// public boolean onQueryTextSubmit(String s) {
		// startSearch();
		// return true;
		// }
		//
		// @Override
		// public boolean onQueryTextChange(String s) {
		// return false;
		// }
		// });
		// ab.setCustomView(view);
		//
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		// | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		findViews();
		setViews();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_refresh).setVisible(false);

		MenuItem mi = menu.findItem(R.id.action_search);
		mi.setVisible(true);
		mi.setIcon(R.drawable.action_search);
		searchView = (SearchView) mi.getActionView();
		// searchView.setIconifiedByDefault(false);
		searchView.setIconified(false);

		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);
		AutoCompleteTextView searchText = (AutoCompleteTextView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
		searchText.setHintTextColor(getResources().getColor(
				R.color.white_search));
		searchView.setQueryHint("搜索");// 搜圈博▪搜人▪搜圈子

		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);
		cv.setVisibility(View.GONE);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				startSearch(s);
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

		searchView.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				// try {
				// // 用Intent来传递语音识别的模式,并且开启语音模式
				// Intent intent = new Intent(
				// RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				// // 语言模式和自由形式的语音识别
				// intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				// RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// // 提示语言开始
				// intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speaking");
				// // 开始语音识别
				// startActivityForResult(intent, 2);
				// } catch (ActivityNotFoundException e) {
				// T.show(App.app, "找不到语音设备");
				// }
				return true;
			}
		});

		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "5");
	}

	private void findViews() {
		tabs = (TabLinearLayout) findViewById(R.id.tabs);
		tabs.changeText(new String[] { "搜圈博", "搜人", "搜圈子" });
		tabs.refresh(0);
		tabs.setOnTabClickListener(this);
		searchBar = (SearchBar) findViewById(R.id.search_bar);
		searchBar.setKeyHint("搜圈博▪搜人▪搜圈子");
		searchBar.setOnClearListener(this);
		// searchBar.setOnSearchListener(this);
		cancel = (TextView) findViewById(R.id.cacel);
		cancel.setOnClickListener(this);
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.showFinishLoadFooter();
		edit = searchBar.getEditText();
		delete = (ImageView) findViewById(R.id.delete);
		edit.removeTextChangedListener(searchBar.getTextWatcher());
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// startSearch();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() > 0) {
					setAutoText();
					delete.setVisibility(View.VISIBLE);
				} else {
					delete.setVisibility(View.GONE);
				}
			}
		});
	}

	private void setViews() {
		mbers = new ArrayList<MemberBean>();
		findResultAdapter = new FindResultAdapter(this, mbers);
		infos = new ArrayList<ResultMessageBean>();
		infoAdapter = new InfoSearchedAdapter(this, infos, listView);
		groups = new ArrayList<GroupBasicBean>();
		groupAdapter = new GroupCheckAdapter(this, groups, listView);

		searchRecord = new SearchRecordService(this);
		autoWords = new ArrayList<String>();
		// adapter = new AutoWordAdapter(this, autoWords, this);
	}

	private void setAutoText() {
		autoWords.clear();
		infos.clear();
		mbers.clear();
		groups.clear();
		String keyWds = getKeyWords().trim();
		if (null == keyWds || "".equals(keyWds)) {
			adapter.notifyDataSetChanged();
			return;
		}
		if (!(listView.getAdapter() instanceof AutoWordAdapter)) {
			listView.setAdapter(adapter);
		}
		String key = searchBar.getKeyWords().trim();
		autoWords.clear();
		ArrayList<SearchRecordBean> result = searchRecord.query(keyWds);
		autoWords.add(key);

		for (int i = 0; i < result.size(); i++) {
			String record = result.get(i).getRecord();
			if (record.startsWith(key)) {
				autoWords.add(record);
			}
		}
		adapter.notifyDataSetChanged();
	}

	void loadInfo(int which, boolean isRefresh) {
		cut = which;
		stopTask();
		task = new LoadSearchTask(which, isRefresh, key);
		task.execute();
		listView.showLoadFooter();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadSearchTask extends AsyncTask<String, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;
		private String key;

		public LoadSearchTask(int curCut, boolean isRefresh, String keyWords) {
			this.cur = curCut;
			this.isRefresh = isRefresh;
			this.key = keyWords;
			isLoading = true;
			BaseData.hideKeyBoard(SearchActivity.this);
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = loadSearchResult(cur, isRefresh, key);
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			L.i(TAG, "key words" + key);
			listView.showFinishLoadFooter();
			if (isCancelled()) {
				return;
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else if (cur == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					switch (cur) {
					case 0:
						if (isRefresh || 1 == trendId_info) {
							infos.clear();
						}
						ResultAll resultInfos0 = (ResultAll) mcResult
								.getResult();
						if (null != resultInfos0) {
							List<ResultMessageBean> requestInfos0 = resultInfos0
									.getRmList();
							if (null == requestInfos0
									|| requestInfos0.size() == 0) {
								showTip("没有更多数据！");
							} else {
								// for (int i = 0; i < requestInfos0.size();
								// i++) {
								// ResultMessageBean bean = requestInfos0
								// .get(i);
								// if ("group_topic_info".equals(bean
								// .getIndexname())) {
								// infos.add(bean);
								// // }
								// }
								// ++trendId_info;
								infos.addAll(requestInfos0);
								trendId_info = infos.size();
								infoAdapter.notifyDataSetChanged();
							}
						} else {
							showTip("没有更多数据！");
						}
						break;
					case 1:
						if (isRefresh || null == trendId_mber
								|| "".equals(trendId_mber)) {
							mbers.clear();
						}
						MapMemberBean resultInfos1 = (MapMemberBean) mcResult
								.getResult();
						List<MemberBean> requestInfos1 = resultInfos1
								.getMEMBER_LIST();
						if (null == requestInfos1 || 0 == requestInfos1.size()) {
							showTip("没有更多数据！");
						} else {
							mbers.addAll(requestInfos1);
							trendId_mber = String.valueOf(mbers.size());
							findResultAdapter.notifyDataSetChanged();
						}
						break;
					case 2:
						if (isRefresh || null == trendId_gourp
								|| "".equals(trendId_gourp)) {
							groups.clear();
						}
						ArrayList<GroupBasicBean> requestInfos2 = (ArrayList<GroupBasicBean>) mcResult
								.getResult();
						if (null == requestInfos2) {
							showTip("没有更多数据！");
						} else {
							groups.addAll(requestInfos2);
							trendId_gourp = String.valueOf(groups.size());
							groupAdapter.notifyDataSetChanged();
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
			listView.onRefreshComplete();
			isLoading = false;
		}
	}

	MCResult loadSearchResult(int which, boolean refresh, final String key)
			throws Exception {
		MCResult mcResult = null;
		switch (which) {
		case 0:
			if (refresh) {
				trendId_info = 1;
			}
			mcResult = APIRequestServers.searchResource(SearchActivity.this,
					key, null, trendId_info, 10, "sa", "0", "");
			break;
		case 1:
			if (refresh) {
				trendId_mber = "0";
			}
			mcResult = APIRequestServers.searchMembersByName(
					SearchActivity.this, key, trendId_mber, "20");
			break;
		case 2:
			if (refresh) {
				trendId_gourp = "0";
			}
			mcResult = APIRequestServers.searchGroups(SearchActivity.this, key,
					trendId_gourp, "20");
			break;
		default:
			break;
		}

		return mcResult;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private String getKeyWords() {
		return searchBar.getKeyWords();
	}

	@Override
	public void onTabClick(View tab) {
		searchBar.clearText();
		onClear(searchBar.getKeyWords());
		cut = (Integer) tab.getTag();
		listView.onRefreshComplete();
		// startSearch();
	}

	private void startSearch(String s) {
		switch (cut) {
		case 0:
			listView.setAdapter(infoAdapter);
			break;
		case 1:
			listView.setAdapter(findResultAdapter);
			break;
		case 2:
			listView.setAdapter(groupAdapter);
			break;
		default:
			break;
		}
		key = s;
		if (null != key && !"".equals(key)) {
			loadInfo(cut, true);
		} else {
			listView.onRefreshComplete();
		}
	}

	// @Override
	// public void onSearch(String keyWords) {
	// // key = keyWords;
	// startSearch();
	// }

	public void showTip(String text) {
		T.show(getApplicationContext(), text);
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		stopTask();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cacel:
			stopTask();
			BaseData.hideKeyBoard(this);
			hide();
			break;
		default:
			break;
		}
	}

	// @Override
	// public void onChosen(String chosenString, boolean b) {
	// if (chosenString != null) {
	// key = chosenString;
	// edit.setText(chosenString);
	// edit.setSelection(chosenString.length());
	// startSearch();
	// if (b) {
	// addKeyWord(chosenString);
	// }
	// }
	// }
	//
	// private void addKeyWord(String key) {
	// searchRecord.insert(key);
	// }

	@Override
	public void onRefresh() {
		if (null == key || "".equals(key)) {
			listView.onRefreshComplete();
		} else {
			loadInfo(cut, true);
		}
	}

	private void hide() {
		finish();
		overridePendingTransition(R.anim.dark_in, R.anim.push_down_out);
	}

	@Override
	public void onClear(String keyWords) {
		stopTask();
	}

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadInfo(cut, false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			hide();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Bundle bundle = data.getExtras();
				String[] chosenIds = bundle.getStringArray(BundleKey.CHOOSEDS);
				ChooseGroupBean bean = (ChooseGroupBean) bundle
						.getSerializable(BundleKey.CHOOSEGROUPBEAN);
				int position = bundle.getInt(BundleKey.POSITION);
				findResultAdapter.changeGroups(chosenIds, position,
						String.valueOf(bean.getMemberId()));
				// } else if (requestCode == 2) {
				// // 取得语音的字符
				// ArrayList<String> results = data
				// .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				// // 设置视图的更新
				// String resultsString = "";
				// if (results != null)
				// for (int i = 0; i < results.size(); i++) {
				// resultsString += results.get(i);
				// }
				// L.d(TAG, "onActivityResult resultsString=" + resultsString);
				// T.show(App.app, resultsString);
			}
		}
	}
}
