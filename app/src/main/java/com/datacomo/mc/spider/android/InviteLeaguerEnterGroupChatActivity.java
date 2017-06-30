package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.MemberSimpleAdatper;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.ApiTypeEnum;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.MapInvitableGroupChatMemberList;
import com.datacomo.mc.spider.android.net.been.groupchat.MemberSimpleBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;

public class InviteLeaguerEnterGroupChatActivity extends BasicActionBarActivity
		implements HandlerNumberUtil {
	// static TAG
	private static final String TAG_LOG = "InviteLeagerEnterGroupChatActivity";

	// variable
	private Context mContext;
	private String mKeyWord;
	private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_MEMBERSIMPLE;
	int groupId;
	int chatId;
	private boolean isSearch;

	// import class
	private MemberSimpleAdatper mAdapter_MemberSimple,
			mAdapter_SearchMemberSimple;
	private RequestTask searchTask;

	// view
	private SearchBar cv_SearchBar;
	private ListView lv_InvitableLeaguer, lv_SearchResult;
	// private LinearLayout footer;
	private LinearLayout lLayout_SearchBox, lLayout_Box;
	private Button btn_SearchResultBox_Submit;

	// private LinearLayout waitToast; // 等待提示

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_inviteleaguerenterchat);
		// setTitle("添加圈聊成员", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("添加圈聊成员");
		mContext = this;
		initView();
		initData();
		bindListener();
	}

	private void initView() {
		cv_SearchBar = (SearchBar) findViewById(R.id.cv_searchbar);
		cv_SearchBar.setVisibility(View.GONE);
		lv_InvitableLeaguer = (ListView) findViewById(R.id.layout_inviteleaguerentergroupchat_lv_invitableleaguer);
		lLayout_SearchBox = (LinearLayout) findViewById(R.id.layout_inviteleaguerentergroupchat_llayout_searchbox);
		lLayout_Box = (LinearLayout) findViewById(R.id.layout_inviteleaguerentergroupchat_llayout);
		lv_SearchResult = (ListView) findViewById(R.id.layout_inviteleaguerentergroupchat_searchresultbox_lv_searchresult);
		btn_SearchResultBox_Submit = (Button) findViewById(R.id.layout_inviteleaguerentergroupchat_searchresultbox_btn_submit);
		// footer = (LinearLayout) getLayoutInflater()
		// .inflate(R.layout.foot, null);
		// waitToast = (LinearLayout) findViewById(R.id.footer);
	}

	private ArrayList<Object> beans, sBeans;

	private void initData() {
		Bundle bundle = getIntentMag();
		groupId = bundle.getInt("groupId");
		chatId = groupId;
		isSearch = false;

		beans = new ArrayList<Object>();
		mAdapter_MemberSimple = new MemberSimpleAdatper(mContext, beans);
		lv_InvitableLeaguer.setAdapter(mAdapter_MemberSimple);

		sBeans = new ArrayList<Object>();
		mAdapter_SearchMemberSimple = new MemberSimpleAdatper(mContext, sBeans);
		lv_SearchResult.setAdapter(mAdapter_SearchMemberSimple);
		requestUploadInfo(ApiTypeEnum.INVITABLEGROUPCHATMEMBERLIST);
	}

	private void bindListener() {
		lv_InvitableLeaguer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemChecked(parent, view, position, isSearch);
			}
		});
		lv_SearchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemChecked(parent, view, position, isSearch);
			}
		});
		btn_SearchResultBox_Submit.setOnClickListener(this);
		cv_SearchBar.setOnSearchListener(new OnSearchListener() {

			@Override
			public void onSearch(String keyWords) {
				mKeyWord = keyWords;
				requestUploadInfo(ApiTypeEnum.SEARCHINVITABLEGROUPCHATMEMBERLIST);
			}
		});
		lLayout_SearchBox.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSearechBox();
				return true;
			}
		});
	}

	private Bundle getIntentMag() {
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		if (null != intent)
			bundle = intent.getExtras();
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
				mKeyWord = s;
				isSearch = true;
				requestUploadInfo(ApiTypeEnum.SEARCHINVITABLEGROUPCHATMEMBERLIST);
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
				isSearch = false;
				lLayout_Box.setVisibility(View.VISIBLE);
				lLayout_SearchBox.setVisibility(View.GONE);
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
				isSearch = false;
				lLayout_Box.setVisibility(View.VISIBLE);
				lLayout_SearchBox.setVisibility(View.GONE);
			} else {
				finish();
			}
			return true;
		case R.id.action_send:
			String[] checkedIds = mAdapter_MemberSimple.getChosenIds();
			if (checkedIds.length > 0) {
				spdDialog.showProgressDialog("正在邀请中...");
				new RequestTask(mContext).execute(
						ApiTypeEnum.INVITELEAGUERENTERCHAT, checkedIds, chatId,
						groupId);
			} else {
				showTip("请先选择朋友");
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void itemChecked(AdapterView<?> parent, View view, int position,
			Boolean isSearch) {
		MemberSimpleBean bean = null;
		try {
			bean = (MemberSimpleBean) parent.getItemAtPosition(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isSearch) {
			mAdapter_SearchMemberSimple.chooseChage(bean, view);
		} else {
			mAdapter_MemberSimple.chooseChage(bean, view);
		}
	}

	/**
	 * 联网获取数据
	 **/
	private void requestUploadInfo(ApiTypeEnum apiTypeEnum) {
		switch (apiTypeEnum) {
		case INVITABLEGROUPCHATMEMBERLIST:
			new RequestTask(mContext).execute(apiTypeEnum, chatId, groupId,
					mAdapter_MemberSimple.getCount());
			break;
		case SEARCHINVITABLEGROUPCHATMEMBERLIST:
			if (null != searchTask
					&& searchTask.getStatus() == AsyncTask.Status.RUNNING) {
				searchTask.cancel(true);
			}
			searchTask = new RequestTask(mContext);
			searchTask.execute(apiTypeEnum, chatId, groupId, mKeyWord,
					mAdapter_SearchMemberSimple.getCount());
			break;
		default:
			break;
		}
	}

	/**
	 * request
	 * 
	 * @author no 287
	 * 
	 */
	class RequestTask extends AsyncTask<Object, Integer, Object> {
		private Object[] mParams;
		private Context mContext;

		public RequestTask(Context context) {
			mContext = context;
		}

		@Override
		protected Object doInBackground(Object... params) {
			mParams = params;
			Object result = null;
			MCResult mcResult = null;
			switch ((ApiTypeEnum) mParams[0]) {
			case INVITABLEGROUPCHATMEMBERLIST:
				try {
					mcResult = APIGroupChatRequestServers
							.getInvitableGroupChatMemberList(mContext,
									(Integer) mParams[1], (Integer) mParams[2],
									(Integer) mParams[3], SIZE_PAGE, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = mcResult;
				break;
			case SEARCHINVITABLEGROUPCHATMEMBERLIST:
				try {
					mcResult = APIGroupChatRequestServers
							.searchInvitableGroupChatMemberList(mContext,
									(Integer) mParams[1], (Integer) mParams[2],
									(String) mParams[3], (Integer) mParams[4],
									SIZE_PAGE, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = mcResult;
				break;
			case INVITELEAGUERENTERCHAT:
				try {
					mcResult = APIGroupChatRequestServers
							.inviteLeaguerEnterChat(mContext,
									(String[]) mParams[1],
									(Integer) mParams[2], (Integer) mParams[3]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = mcResult;
				break;
			default:
				break;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			MCResult mcResult = null;
			switch ((ApiTypeEnum) mParams[0]) {
			case INVITABLEGROUPCHATMEMBERLIST:
				lLayout_Box.setVisibility(View.VISIBLE);
				lLayout_SearchBox.setVisibility(View.GONE);
				mcResult = (MCResult) result;
				if (null != mcResult && mcResult.getResultCode() == 1) {
					MapInvitableGroupChatMemberList map = (MapInvitableGroupChatMemberList) mcResult
							.getResult();
					if (null != map) {
						List<MemberSimpleBean> msbs = map
								.getINVITABLEGROUPCHATMEMBERLIST();
						if (null == msbs || msbs.size() == 0) {
							if (sBeans.size() == 0) {
								T.show(mContext, "亲，圈子成员已经都在这里了");
								InviteLeaguerEnterGroupChatActivity.this
										.finish();
							} else {
								T.show(mContext, "最后一页");
							}
						} else {
							L.i(TAG_LOG, "RequestTask size=" + msbs.size()
									+ ",size0=" + sBeans.size());
							beans.clear();
							beans.addAll(msbs);
							mAdapter_MemberSimple.notifyDataSetChanged();
						}
					}
				}
				break;
			case SEARCHINVITABLEGROUPCHATMEMBERLIST:
				lLayout_Box.setVisibility(View.GONE);
				lLayout_SearchBox.setVisibility(View.VISIBLE);
				mcResult = (MCResult) result;
				if (null != mcResult && mcResult.getResultCode() == 1) {
					MapInvitableGroupChatMemberList map = (MapInvitableGroupChatMemberList) mcResult
							.getResult();
					if (null != map) {
						List<MemberSimpleBean> msbs = map
								.getINVITABLE_GROUPCHAT_MEMBER();
						if (null == msbs || msbs.size() == 0) {
							if (sBeans.size() == 0) {
								T.show(mContext, "没有找到相应的成员");
							} else {
								T.show(mContext, "最后一页");
							}
						} else {
							L.d(TAG_LOG, "RequestTask size=" + msbs.size()
									+ ",size0=" + sBeans.size());
							sBeans.clear();
							sBeans.addAll(msbs);
							mAdapter_SearchMemberSimple.notifyDataSetChanged();
						}
					}
				}
				break;
			case INVITELEAGUERENTERCHAT:
				spdDialog.cancelProgressDialog(null);
				mcResult = (MCResult) result;
				if (null != mcResult && mcResult.getResultCode() == 1) {
					// {"result":{"RESULT":1},"resultStaus":true,"resultCode":1,"resultMessage":"RESULT 0:失败1:成功2:社员不属于该圈子","version":"v1.0"}
					int RESULT = 0;
					RESULT = (Integer) mcResult.getResult();
					if (RESULT == 1) {
						T.show(mContext, "已邀请！");
						finish();
					} else if (RESULT == 3) {
						T.show(mContext, "当前圈聊人数已达上线");
					} else {
						T.show(mContext, "邀请失败");
					}
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_inviteleaguerentergroupchat_searchresultbox_btn_submit:
			hideSearechBox();
			break;
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				lLayout_SearchBox.setVisibility(View.GONE);
				lLayout_Box.setVisibility(View.VISIBLE);
				isSearch = false;
				searchView.onActionViewCollapsed();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				lLayout_SearchBox.setVisibility(View.GONE);
				lLayout_Box.setVisibility(View.VISIBLE);
				isSearch = false;
				searchView.onActionViewCollapsed();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 隐藏搜索界面
	 */
	private void hideSearechBox() {
		lLayout_SearchBox.setVisibility(View.GONE);
		lLayout_Box.setVisibility(View.VISIBLE);

		isSearch = false;
		mAdapter_MemberSimple.setCheckIds(mAdapter_SearchMemberSimple
				.getCheckIds());
		mAdapter_MemberSimple.notifyDataSetChanged();
		searchView.onActionViewCollapsed();
	}

}
