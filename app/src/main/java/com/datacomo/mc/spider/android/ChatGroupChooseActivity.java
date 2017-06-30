package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.ChatGroupChooseAdapter;
import com.datacomo.mc.spider.android.adapter.ChatGroupChooseAdapter.ViewHolder;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FooterListView;
import com.datacomo.mc.spider.android.view.FooterListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;

public class ChatGroupChooseActivity extends BasicActionBarActivity implements
		OnClickListener, OnLoadMoreListener {
	// 声明静态TAG
	private static final String TAG = "GroupLeaguerChooseActivity";
	private static final String LOG_LEAGUER = "GroupLeaguerChooseActivity_Leaguer";
	// 声明变量
	public static final int CHOOSECHATGROUP = 31;

	private boolean mIsSearch;
	private boolean mIsMore;
	private boolean mIsMore_Search;

	private String mKeyWords;
	private Context context;

	private HashMap<String, String> mMap_Choosed;

	private Type enum_sharedTo;
	// 声明引用类
	private ChatGroupChooseAdapter adapter_SharedTo;
	private ChatGroupChooseAdapter adapter_SharedTo__SearchResult;
	private GroupRequsetTask task;
	// 声明组件
	private FooterListView lv_SharedTo;

	private SearchBar searchBar;

	@Override
	protected void onDestroy() {
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

	/**
	 * 初始化组件
	 **/
	private void initView() {
		lv_SharedTo = (FooterListView) findViewById(R.id.sharedblog_lv_sharedto);
		searchBar = (SearchBar) findViewById(R.id.sharedblog_searchbar);
		searchBar.setAutoSearch(true);
		searchBar.setVisibility(View.GONE);
	}

	/**
	 * 初始化数据
	 **/
	private void initData() {
		mIsSearch = false;
		mMap_Choosed = new HashMap<String, String>();
		Bundle bundle = getIntentMsg();
		enum_sharedTo = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		String lTemp = "";
		switch (enum_sharedTo) {
		case CHATGROUP:
			lTemp = "选择圈聊";
			break;
		default:
			break;
		}
		// setTitle(lTemp, R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle(lTemp);
		TransitionBean.setIsAllSelect(false);
		adapter_SharedTo = new ChatGroupChooseAdapter(new ArrayList<Object>(),
				this);
		adapter_SharedTo__SearchResult = new ChatGroupChooseAdapter(
				new ArrayList<Object>(), this);
		mIsMore = true;
		lv_SharedTo.setAdapter(adapter_SharedTo);
		loadInfo(true, mIsSearch);
		bindListener();
	}

	/**
	 * 绑定监听事件
	 **/
	private void bindListener() {
		lv_SharedTo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.d(TAG, "mMap_Choosed.size:" + mMap_Choosed.size());
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
		lv_SharedTo.setonLoadMoreListener(this);
	}

	private void search(String keyWords) {
		mKeyWords = keyWords;
		if (null == mKeyWords)
			mKeyWords = "";
		mIsSearch = true;
		lv_SharedTo.setAdapter(adapter_SharedTo__SearchResult);
		if (adapter_SharedTo__SearchResult.getCount() > 0)
			adapter_SharedTo__SearchResult.clear();
		mIsMore_Search = true;
		loadInfo(true, mIsSearch);
	}

	private void clear() {
		if (mIsSearch) {
			mIsSearch = false;
			lv_SharedTo.setAdapter(adapter_SharedTo);
			if (adapter_SharedTo.getCount() == 0)
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
			if (mMap_Choosed.size() == 0) {
				T.show(getApplicationContext(), "请选择圈聊");
				return true;
			}
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			bundle.putSerializable(BundleKey.CHOOSEDS, mMap_Choosed);
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
		L.d(LOG_LEAGUER, "position:" + position);
		int id = bean_Transition.getId();
		int type_Select = isSelect(id);
		if (type_Select == 0) {
			viewHolder.check.setImageResource(R.drawable.choice_yes);
			type_Select = 1;
			L.d(TAG, "choose:" + id);
			mMap_Choosed.put(String.valueOf(id), bean_Transition.getName());
		} else if (type_Select == 1) {
			viewHolder.check.setImageResource(R.drawable.choice_no);
			type_Select = 0;
			L.d(TAG, "unchoose:" + id);
			mMap_Choosed.remove(String.valueOf(id));
		}
	}

	// /**
	// * 处理列表选项选中
	// *
	// * @param parent
	// * @param view
	// * @param position
	// */
	// private void itemCheckedOnAll(AdapterView<?> parent, View view, int
	// position) {
	// ViewHolder viewHolder = (ViewHolder) view.getTag();
	// TransitionBean bean_Transition = null;
	// try {
	// bean_Transition = new TransitionBean(
	// parent.getItemAtPosition(position));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// int id = bean_Transition.getId();
	// int type_Select = isUnSelect(id);
	// L.d(TAG, "type_Select:" + type_Select);
	// if (type_Select == 0) {
	// viewHolder.check.setImageResource(R.drawable.choice_yes);
	// type_Select = 1;
	// L.d(TAG, "choose:" + id);
	// mMap_Choosed.remove(String.valueOf(id));
	// } else if (type_Select == 1) {
	// viewHolder.check.setImageResource(R.drawable.choice_no);
	// type_Select = 0;
	// L.d(TAG, "unchoose:" + id);
	// mMap_Choosed.put(String.valueOf(id), bean_Transition.getName());
	// }
	// }

	class GroupRequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_MEMBERLIST;
		private Object[] mParams;
		private boolean mIsSearch;

		/**
		 * 
		 */
		public GroupRequsetTask() {
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			L.d(LOG_LEAGUER, "doInBackground");
			mParams = params;
			MCResult mcResult = null;
			try {
				switch ((Integer) mParams[0]) {
				case 0:
					mcResult = APIRequestServers.myGroupChatList(App.app,
							(String) mParams[1], String.valueOf(SIZE_PAGE),
							"true");
					break;
				case 1:
					mIsSearch = true;
					mcResult = APIRequestServers.searchGroupChat(context,
							(String) mParams[1], (String) mParams[2],
							String.valueOf(SIZE_PAGE), "false");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult result) {
			L.d(LOG_LEAGUER, "onPostExecute");
			lv_SharedTo.hideLoadFooter();
			if (null == result || result.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			List<Object> beans_Temp = null;
			beans_Temp = (List<Object>) result.getResult();
			if (null == beans_Temp || beans_Temp.size() == 0) {
				mIsMore = false;
				if (mIsSearch)
					mIsMore_Search = false;
				else
					mIsMore = false;
				int size = 0;
				if (mIsSearch)
					size = adapter_SharedTo__SearchResult.getCount();
				else
					size = adapter_SharedTo.getCount();
				if (size == 0) {
					return;
				}
				if (!mIsSearch)
					T.show(App.app, "最后一页");
				return;
			}
			L.d(LOG_LEAGUER,
					"onPostExecute beans_Temp.size()" + beans_Temp.size());
			if (mIsSearch) {
				adapter_SharedTo__SearchResult.add(beans_Temp);
				L.d(LOG_LEAGUER,
						"onPostExecute adapter_SharedTo__SearchResult.size()"
								+ adapter_SharedTo__SearchResult.getCount());
			} else {
				adapter_SharedTo.add(beans_Temp);
				L.d(LOG_LEAGUER, "onPostExecute adapter_SharedTo.size()"
						+ adapter_SharedTo.getCount());
			}
			if (mIsSearch)
				mIsMore_Search = beans_Temp.size() >= SIZE_PAGE;
			else
				mIsMore = beans_Temp.size() >= SIZE_PAGE;
			beans_Temp.clear();
			L.d(LOG_LEAGUER, "mIsMore:" + mIsMore);
			L.d(LOG_LEAGUER, "mIsMore_Search:" + mIsMore_Search);
			if (!mIsSearch && !mIsMore)
				T.show(App.app, "最后一页");
		}

	}

	/**
	 * 判定适配器中的item是否为选中项
	 * 
	 * @param id
	 * @return
	 */
	public int isSelect(int id) {
		int check = 0;
		if (mMap_Choosed.containsKey(String.valueOf(id))) {
			check = 1;
		}
		return check;
	}

	/**
	 * 判定适配器中的item是否为选中项
	 * 
	 * @param id
	 * @return
	 */
	public int isUnSelect(int id) {
		int check = 0;
		L.d(TAG, "containskey:" + mMap_Choosed.containsKey(String.valueOf(id)));
		if (!mMap_Choosed.containsKey(String.valueOf(id))) {
			check = 1;
		}
		return check;
	}

	/**
	 * 
	 * @param isSearch
	 */
	private void loadInfo(boolean start, boolean isSearch) {
		if (!start && !mIsMore)
			return;
		stopTask();
		int startRecord = 0;
		if (isSearch)
			startRecord = adapter_SharedTo__SearchResult.getCount();
		else
			startRecord = adapter_SharedTo.getCount();
		L.d(LOG_LEAGUER, "start:" + start + " isSearch:" + isSearch
				+ " startRecord:" + startRecord);
		task = new GroupRequsetTask();
		if (!isSearch)
			task.execute(0, String.valueOf(startRecord));
		else
			task.execute(1, mKeyWords, String.valueOf(startRecord));
		lv_SharedTo.showLoadFooter();
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
