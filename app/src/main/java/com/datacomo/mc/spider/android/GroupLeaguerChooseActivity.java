package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.GroupLeaguerChooseAdapter;
import com.datacomo.mc.spider.android.adapter.GroupLeaguerChooseAdapter.ViewHolder;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.GroupResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupLeaguerBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FooterListView;
import com.datacomo.mc.spider.android.view.FooterListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;

public class GroupLeaguerChooseActivity extends BasicActionBarActivity
		implements OnClickListener, OnLoadMoreListener {
	private static final String TAG = "GroupLeaguerChooseActivity";
	private static final String LOG_LEAGUER = "GroupLeaguerChooseActivity_Leaguer";

	public static final int CHOOSEGROUPLEAGUER = 31;

	private int id_Group;
	private int mberId = 0;
	private boolean mIsSearch, canSelectAll;
	private boolean mIsMore;
	private boolean mIsMore_Search;

	private String mKeyWords;
	private Context context;

	private HashMap<String, String> mMap_Choosed;

	private Type enum_sharedTo;
	// 声明引用类
	private GroupLeaguerChooseAdapter adapter_SharedTo;
	private GroupLeaguerChooseAdapter adapter_SharedTo__SearchResult;
	private LeaguerRequsetTask task;
	// 声明组件
	private FooterListView lv_SharedTo;

	private SearchBar searchBar;

	private ViewStub vs_BtnBox;
	private Button btn;// 全选或确定

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
		try {
			UserBusinessDatabase business = new UserBusinessDatabase(this);
			String session_key = App.app.share.getSessionKey();
			mberId = Integer.valueOf(business.getMemberId(session_key));
		} catch (Exception e) {
			e.printStackTrace();
		}

		initView();
		initData();
	}

	private void initView() {
		lv_SharedTo = (FooterListView) findViewById(R.id.sharedblog_lv_sharedto);
		vs_BtnBox = (ViewStub) findViewById(R.id.sharedblog_btnbox);
		vs_BtnBox.setInflatedId(R.id.sharedblog_btnbox);
		vs_BtnBox.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				btn = (Button) inflated.findViewById(R.id.btn);
				btn.setText("全			选");
				btn.setOnClickListener(GroupLeaguerChooseActivity.this);
			}
		});
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
		mMap_Choosed = new HashMap<String, String>();
		Bundle bundle = getIntentMsg();
		id_Group = bundle.getInt(BundleKey.ID_GROUP);
		canSelectAll = bundle.getBoolean("canSelectAll", false);
		L.i(TAG, "initData canSelectAll=" + canSelectAll);
		enum_sharedTo = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		String lTemp = "";
		switch (enum_sharedTo) {
		case GROUPLEAGUER:
			lTemp = "圈内分享";
			vs_BtnBox.inflate();
			if (!canSelectAll) {
				vs_BtnBox.setVisibility(View.GONE);
			}
			break;
		case ATGROUPLEAGUER:
			lTemp = "圈子成员";
			@SuppressWarnings("unchecked")
			HashMap<String, String> tempChoosed = (HashMap<String, String>) bundle
					.getSerializable(BundleKey.CHOOSEDS);
			if (null != tempChoosed)
				mMap_Choosed.putAll(tempChoosed);
			L.d(TAG, "init mMap_Choosed.size:" + mMap_Choosed.size());
			break;
		default:
			break;
		}
		// setTitle(lTemp, R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle(lTemp);
		TransitionBean.setIsAllSelect(false);
		adapter_SharedTo = new GroupLeaguerChooseAdapter(
				new ArrayList<GroupLeaguerBean>(), this);
		adapter_SharedTo__SearchResult = new GroupLeaguerChooseAdapter(
				new ArrayList<GroupLeaguerBean>(), this);
		mIsMore = true;
		lv_SharedTo.setAdapter(adapter_SharedTo);
		loadInfo(true, mIsSearch);
		bindListener();
	}

	private void bindListener() {
		lv_SharedTo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!TransitionBean.isAllSelect())
					itemChecked(parent, view, position);
				else
					itemCheckedOnAll(parent, view, position);

				// int s = mMap_Choosed.size();
				// String t = "圈内分享";
				// if (enum_sharedTo == Type.ATGROUPLEAGUER) {
				// t = "圈子成员";
				// }
				// if (s > 0) {
				// t += "(" + s + ")";
				// }
				// ab.setTitle(t);
			}
		});
		searchBar.setOnSearchListener(new OnSearchListener() {

			@Override
			public void onSearch(String keyWords) {
				mKeyWords = keyWords;
				if (null == mKeyWords)
					mKeyWords = "";
				mIsSearch = true;
				lv_SharedTo.setAdapter(adapter_SharedTo__SearchResult);
				if (adapter_SharedTo__SearchResult.getCount() > 0)
					adapter_SharedTo__SearchResult.clear();
				if (null != btn)
					btn.setText("确		定");
				mIsMore_Search = true;
				loadInfo(true, mIsSearch);
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

	private void clear() {
		if (mIsSearch) {
			mIsSearch = false;
			if (TransitionBean.isAllSelect()) {
				if (null != btn)
					btn.setText("取    消    全    选");
			} else {
				if (null != btn)
					btn.setText("全		选");
			}
			lv_SharedTo.setAdapter(adapter_SharedTo);
			if (adapter_SharedTo.getCount() == 0)
				loadInfo(true, mIsSearch);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn:
			if (mIsSearch) {
				searchBar.clearText();
				if (TransitionBean.isAllSelect()) {
					if (null != btn)
						btn.setText("取    消    全    选");
				} else {
					if (null != btn)
						btn.setText("全		选");
				}
				mIsSearch = false;
				lv_SharedTo.setAdapter(adapter_SharedTo);
				if (adapter_SharedTo.getCount() == 0)
					loadInfo(true, mIsSearch);
				// String t = "圈内分享";
				// if (enum_sharedTo == Type.ATGROUPLEAGUER) {
				// t = "圈子成员";
				// }
				// ab.setTitle(t);
			} else {
				select();
			}
			break;
		default:
			break;
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
					adapter_SharedTo__SearchResult.notifyDataSetChanged();
				} else {
					adapter_SharedTo.notifyDataSetChanged();
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
				mKeyWords = s;
				if (null == mKeyWords)
					mKeyWords = "";
				mIsSearch = true;
				lv_SharedTo.setAdapter(adapter_SharedTo__SearchResult);
				if (adapter_SharedTo__SearchResult.getCount() > 0)
					adapter_SharedTo__SearchResult.clear();
				if (null != btn)
					btn.setText("确		定");
				mIsMore_Search = true;
				loadInfo(true, mIsSearch);
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
			share();
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

	private void share() {
		Intent intent = null;
		Bundle bundle = null;
		switch (enum_sharedTo) {
		case ATGROUPLEAGUER:
			if (mMap_Choosed.size() == 0) {
				T.show(getApplicationContext(), "您还没有选择成员");
				return;
			}
			intent = getIntent();
			bundle = intent.getExtras();
			bundle.putSerializable(BundleKey.CHOOSEDS, mMap_Choosed);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case GROUPLEAGUER:
			if (!TransitionBean.isAllSelect() && mMap_Choosed.size() == 0) {
				T.show(getApplicationContext(), "请选择圈子成员");
				return;
			}
			intent = getIntent();
			bundle = intent.getExtras();
			bundle.putSerializable(BundleKey.CHOOSEDS, mMap_Choosed);
			bundle.putBoolean(BundleKey.ISSELECTALL,
					TransitionBean.isAllSelect());
			bundle.putInt(BundleKey.SIZE, adapter_SharedTo.getBeans().size());
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;

		}
	}

	/**
	 * 处理选择状态
	 */
	private void select() {
		if (adapter_SharedTo.getCount() == 0)
			return;
		mMap_Choosed.clear();
		if (TransitionBean.isAllSelect()) {
			if (null != btn)
				btn.setText("全		选");
			TransitionBean.setIsAllSelect(false);
		} else {
			if (null != btn)
				btn.setText("取    消    全    选");
			TransitionBean.setIsAllSelect(true);
		}
		handler_SharedBlog.sendEmptyMessage(3);
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
			mMap_Choosed.put(String.valueOf(id),
					"@" + bean_Transition.getName() + "：");
		} else if (type_Select == 1) {
			viewHolder.check.setImageResource(R.drawable.choice_no);
			type_Select = 0;
			L.d(TAG, "unchoose:" + id);
			mMap_Choosed.remove(String.valueOf(id));
		}
	}

	/**
	 * 处理列表选项选中
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 */
	private void itemCheckedOnAll(AdapterView<?> parent, View view, int position) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		TransitionBean bean_Transition = null;
		try {
			bean_Transition = new TransitionBean(
					parent.getItemAtPosition(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int id = bean_Transition.getId();
		int type_Select = isUnSelect(id);
		L.d(TAG, "type_Select:" + type_Select);
		if (type_Select == 0) {
			viewHolder.check.setImageResource(R.drawable.choice_yes);
			type_Select = 1;
			L.d(TAG, "choose:" + id);
			mMap_Choosed.remove(String.valueOf(id));
		} else if (type_Select == 1) {
			viewHolder.check.setImageResource(R.drawable.choice_no);
			type_Select = 0;
			L.d(TAG, "unchoose:" + id);
			mMap_Choosed.put(String.valueOf(id),
					"@" + bean_Transition.getName() + "：");
		}
	}

	class LeaguerRequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_MEMBERLIST;
		private Object[] mParams;
		private boolean mIsDeleteOwn;
		private boolean mIsSearch;

		@Override
		protected MCResult doInBackground(Object... params) {
			L.d(LOG_LEAGUER, "doInBackground");
			mParams = params;
			MCResult mcResult = null;
			try {
				switch ((Integer) mParams[0]) {
				case 0:
					mcResult = APIRequestServers.groupLeaguerList(App.app,
							(String) mParams[1], (String) mParams[2],
							String.valueOf(SIZE_PAGE), false);
					break;
				case 1:
					mIsSearch = true;
					mcResult = APIRequestServers.searchGroupResource(App.app,
							GroupResourceTypeEnum.MEMBER, (String) mParams[1],
							(String) mParams[2], (String) mParams[3],
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
			List<GroupLeaguerBean> beans_Temp = null;
			if (mIsSearch)
				beans_Temp = ((MapGroupLeaguerBean) result.getResult())
						.getSEARCHLIST();
			else
				beans_Temp = (List<GroupLeaguerBean>) result.getResult();
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
			for (GroupLeaguerBean groupLeaguerBean : beans_Temp) {
				if (groupLeaguerBean.getMemberId() == mberId) {
					mIsDeleteOwn = true;
					beans_Temp.remove(groupLeaguerBean);
					break;
				}
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
			int size_Page = 0;
			if (mIsDeleteOwn) {
				mIsDeleteOwn = false;
				if (mIsSearch)
					adapter_SharedTo__SearchResult.setIsHasDeleteOwn(true);
				else
					mIsMore = beans_Temp.size() >= size_Page;
				adapter_SharedTo.setIsHasDeleteOwn(true);
				size_Page = SIZE_PAGE - 1;
			} else {
				size_Page = SIZE_PAGE;
			}
			if (mIsSearch)
				mIsMore_Search = beans_Temp.size() >= size_Page;
			else
				mIsMore = beans_Temp.size() >= size_Page;
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
		if (start) {
			if (isSearch)
				adapter_SharedTo__SearchResult.setIsHasDeleteOwn(false);
			else
				adapter_SharedTo__SearchResult.setIsHasDeleteOwn(false);
		}
		int startRecord = 0;
		if (isSearch)
			startRecord = adapter_SharedTo__SearchResult.size();
		else
			startRecord = adapter_SharedTo.size();
		L.d(LOG_LEAGUER, "start:" + start + " isSearch:" + isSearch
				+ " startRecord:" + startRecord);
		task = new LeaguerRequsetTask();
		if (!isSearch)
			task.execute(0, String.valueOf(id_Group),
					String.valueOf(startRecord));
		else
			task.execute(1, String.valueOf(id_Group), mKeyWords,
					String.valueOf(startRecord));
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
