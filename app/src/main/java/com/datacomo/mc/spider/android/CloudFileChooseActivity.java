package com.datacomo.mc.spider.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.CloudFileChooseAdapter;
import com.datacomo.mc.spider.android.adapter.CloudFileChooseAdapter.ViewHolder;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFileInfoBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FooterListView;
import com.datacomo.mc.spider.android.view.FooterListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;

public class CloudFileChooseActivity extends BasicActionBarActivity implements
		OnClickListener, OnLoadMoreListener {
	private static final String LOG_TAG = "CloudFileChooseActivity";
	private static final String LOG_LOADING = "CloudFileChooseActivity_loading";

	private boolean mIsSearch;
	private boolean mIsMore;
	private boolean mIsMore_Search;

	private String mKeyWords;
	private Context context;

	private List<Object> mList_Choosed;

	// 声明引用类
	private CloudFileChooseAdapter adapter_File;
	private CloudFileChooseAdapter adapter_File__SearchResult;
	private CloudFileRequsetTask task;
	// 声明组件
	private FooterListView lv_CloudFile;

	private SearchBar searchBar;

	private LinearLayout lLayout_Footer;
	private LinearLayout lLayout_Footer_Search;

	@Override
	protected void onDestroy() {
		handler_SharedBlog.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.sharedblog);
		context = this;
		initView();
		initData();
	}

	private void initView() {
		lv_CloudFile = (FooterListView) findViewById(R.id.sharedblog_lv_sharedto);
		lLayout_Footer = (LinearLayout) getLayoutInflater().inflate(
				R.layout.foot, null);
		lLayout_Footer.setFocusable(false);
		lLayout_Footer.setFocusableInTouchMode(false);
		lLayout_Footer_Search = (LinearLayout) getLayoutInflater().inflate(
				R.layout.foot, null);
		lLayout_Footer_Search.setFocusable(false);
		lLayout_Footer_Search.setFocusableInTouchMode(false);
		searchBar = (SearchBar) findViewById(R.id.sharedblog_searchbar);
		searchBar.setAutoSearch(true);
		searchBar.setVisibility(View.GONE);
	}

	private void initData() {
		mIsSearch = false;
		mList_Choosed = new ArrayList<Object>();
		Bundle bundle = getIntentMsg();
		String lTemp = "选择云文件";
		@SuppressWarnings("unchecked")
		List<Object> tempChoosed = (List<Object>) bundle
				.getSerializable(BundleKey.CHOOSEDS);
		if (null != tempChoosed)
			mList_Choosed.addAll(tempChoosed);
		L.d(LOG_TAG, "init mMap_Choosed.size:" + mList_Choosed.size());
		// setTitle(lTemp, R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle(lTemp);
		TransitionBean.setIsAllSelect(false);
		adapter_File = new CloudFileChooseAdapter(
				new ArrayList<FileInfoBean>(), this);
		adapter_File__SearchResult = new CloudFileChooseAdapter(
				new ArrayList<FileInfoBean>(), this);
		mIsMore = true;
		lv_CloudFile.setAdapter(adapter_File);
		loadInfo(true, mIsSearch);
		bindListener();
	}

	private void bindListener() {
		lv_CloudFile.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemChecked(parent, view, position);
			}
		});
		searchBar.setOnSearchListener(new OnSearchListener() {

			@Override
			public void onSearch(String keyWords) {
				search(keyWords);
			}
		});
		searchBar.setOnClearListener(new OnClearListener() {

			@Override
			public void onClear(String keyWords) {
				clear();
			}
		});
		lv_CloudFile.setonLoadMoreListener(this);
	}

	private void search(String keyWords) {
		mKeyWords = keyWords;
		if (null == mKeyWords)
			mKeyWords = "";
		mIsSearch = true;
		lv_CloudFile.setAdapter(adapter_File__SearchResult);
		if (adapter_File__SearchResult.getCount() > 0)
			adapter_File__SearchResult.clear();
		mIsMore_Search = true;
		loadInfo(true, mIsSearch);
	}

	private void clear() {
		if (mIsSearch) {
			mIsSearch = false;
			lv_CloudFile.setAdapter(adapter_File);
			if (adapter_File.getCount() == 0)
				loadInfo(true, mIsSearch);
		}
	}

	/**
	 * 获取intent
	 * 
	 * @return Bundle
	 **/
	private Bundle getIntentMsg() {
		Bundle bundle = null;
		Intent intent = getIntent();
		if (intent != null) {
			bundle = intent.getExtras();
		}
		return bundle;
	}

	/**
	 * 创建Handler消息队列
	 * 
	 * @return Handler
	 **/
	@SuppressLint("HandlerLeak")
	private Handler handler_SharedBlog = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: //
				showTip((String) msg.obj);
				break;
			case 1: // 接收成员返回数据

				break;
			case 2: // 处理转发或分享成功返回
				showTip((String) msg.obj);
				finish();
				break;
			case 3: // 刷新适配器
				if (mIsSearch) {
					adapter_File__SearchResult.notifyDataSetChanged();
				} else {
					adapter_File.notifyDataSetChanged();
				}
				break;
			case 4: //
				break;
			case 11:
				showTip((String) msg.obj);
				Intent intent = new Intent(context, LoginActivity.class);
				intent.putExtra("CreateGroupTopic", true);
				startActivity(intent);
				break;
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		MenuItem mi = menu.findItem(R.id.action_search);
		searchView = (SearchView) mi.getActionView();
		mi.setVisible(true);
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
				search(s);
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

		menu.findItem(R.id.action_send).setVisible(true);
		menu.findItem(R.id.action_send).setIcon(R.drawable.action_select_over);
		this.menu = menu;
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
		case R.id.action_send:
			if (mList_Choosed.size() == 0) {
				T.show(getApplicationContext(), "您还没有选择文件");
				return true;
			}
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			bundle.putSerializable(BundleKey.CHOOSEDS,
					(Serializable) mList_Choosed);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理列表选项选中
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 */
	private void itemChecked(AdapterView<?> parent, View view, int position) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		TransitionBean bean_Transition = null;
		try {
			bean_Transition = new TransitionBean(
					parent.getItemAtPosition(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int id = bean_Transition.getId();
		int index = isSelect(id);
		if (index == -1) {
			viewHolder.check.setImageResource(R.drawable.choice_yes);
			L.d(LOG_TAG, "choose:" + id);
			mList_Choosed.add(0, bean_Transition.getObject());
		} else {
			viewHolder.check.setImageResource(R.drawable.choice_no);
			L.d(LOG_TAG, "unchoose:" + id + " index:" + index);
			mList_Choosed.remove(index);
		}
	}

	class CloudFileRequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private final int SIZE_PAGE = PageSizeUtil.THIRTY;
		private Object[] mParams;
		private boolean mIsSearch;

		@Override
		protected MCResult doInBackground(Object... params) {
			L.d(LOG_LOADING, "doInBackground");
			mParams = params;
			MCResult mcResult = null;
			try {
				switch ((Integer) mParams[0]) {
				case 0:
					mcResult = APIFileRequestServers.getFileList(App.app,
							FileListTypeEnum.ALL_FILE, (Integer) mParams[1],
							SIZE_PAGE, false);
					break;
				case 1:
					mIsSearch = true;
					mcResult = APIFileRequestServers.searchAllFileList(App.app,
							(String) mParams[1], FileListTypeEnum.ALL_FILE,
							(Integer) mParams[2], SIZE_PAGE, false);
					break;
				}
				L.d(LOG_LOADING, "end");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			L.d(LOG_LOADING, "onPostExecute");
			lv_CloudFile.hideLoadFooter();
			if (null == result || result.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			List<FileInfoBean> beans_Temp = null;
			if (mIsSearch)
				beans_Temp = ((MapFileInfoBean) result.getResult())
						.getFILE_LIST();
			else
				beans_Temp = ((MapFileInfoBean) result.getResult())
						.getFILE_LIST();
			if (null == beans_Temp || beans_Temp.size() == 0) {
				mIsMore = false;
				if (mIsSearch)
					mIsMore_Search = false;
				else
					mIsMore = false;
				int size = 0;
				if (mIsSearch)
					size = adapter_File__SearchResult.getCount();
				else
					size = adapter_File.getCount();
				if (size == 0) {
					return;
				}
				if (!mIsSearch)
					T.show(App.app, "最后一页");
				return;
			}
			L.d(LOG_TAG, "onPostExecute beans_Temp.size()" + beans_Temp.size());
			if (mIsSearch) {
				adapter_File__SearchResult.add(beans_Temp);
				L.d(LOG_TAG,
						"onPostExecute adapter_SharedTo__SearchResult.size()"
								+ adapter_File__SearchResult.getCount());
			} else {
				adapter_File.add(beans_Temp);
				L.d(LOG_TAG, "onPostExecute adapter_SharedTo.size()"
						+ adapter_File.getCount());
			}
			int size_Page = SIZE_PAGE;
			if (mIsSearch)
				mIsMore_Search = beans_Temp.size() >= size_Page;
			else
				mIsMore = beans_Temp.size() >= size_Page;
			beans_Temp.clear();
			L.d(LOG_TAG, "mIsMore:" + mIsMore);
			L.d(LOG_TAG, "mIsMore_Search:" + mIsMore_Search);
			if (!mIsSearch && !mIsMore)
				T.show(App.app, "最后一页");
		}

	}

	/**
	 * 判定适配器中的item是否为选中项」
	 * 
	 * @param id
	 * @return
	 */
	public int isSelect(int id) {
		int index = -1;
		for (Object file : mList_Choosed) {
			index++;
			TransitionBean bean = null;
			try {
				bean = new TransitionBean(file);
				Type type = bean.getType();
				if (type == Type.CLOUDFILE) {
					if (bean.getId() == id) {
						return index;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param isSearch
	 */
	private void loadInfo(boolean start, boolean isSearch) {
		if (!start && !mIsMore)
			return;
		stopTask();
		if (start) {
			if (isSearch)
				adapter_File__SearchResult.setIsHasDeleteOwn(false);
			else
				adapter_File__SearchResult.setIsHasDeleteOwn(false);
		}
		int startRecord = 0;
		if (isSearch)
			startRecord = adapter_File__SearchResult.size();
		else
			startRecord = adapter_File.size();
		L.d(LOG_TAG, "start:" + start + " isSearch:" + isSearch
				+ " startRecord:" + startRecord);
		task = new CloudFileRequsetTask();
		if (!isSearch)
			task.execute(0, startRecord);
		else
			task.execute(1, mKeyWords, startRecord);
		lv_CloudFile.showLoadFooter();
	}

	@SuppressLint("NewApi")
	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	@Override
	public void onLoadMore() {
		loadInfo(false, mIsSearch);
	}

}
