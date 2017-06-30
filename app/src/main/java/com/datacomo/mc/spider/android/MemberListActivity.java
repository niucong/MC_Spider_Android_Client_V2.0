package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.MemberListAdapter;
import com.datacomo.mc.spider.android.animation.AnimationManager;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.GroupLeaguerManageEnum;
import com.datacomo.mc.spider.android.enums.GroupResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.manager.MemberOperateAlertManager;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapGroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapOpenPageFanBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceVisitBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FooterListView;
import com.datacomo.mc.spider.android.view.FooterListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.PopMenu;

public class MemberListActivity extends BasicActionBarActivity implements
		HandlerNumberUtil, OnLoadMoreListener {
	// 声明静态TAG
	private static final String TAG = "MemberListActivity";
	// 声明变量
	private final String KEY_FACE = "face";
	private String type_Object;
	private String id_Group;
	private String id_Quubo;
	private String id_OpenPage;
	private String id_FileMembers;
	private String str_Title;
	private String id_Disstertaion;

	private Type mType;
	private boolean isReply;

	private int num_Total;
	private int id_Member;
	private int id_Owner;

	private boolean mIsMore;
	private String joinGroupStatus = "";
	private Context context;
	private TransitionBean mCurrentBean;

	// 声明引用类
	private MemberListAdapter adapter_MemberList;
	private MemberListAdapter adapter_MemberList_Search;

	private PopMenu popMenu;
	private MemberRequsetTask task;
	// 组件
	private FooterListView mLv_MemberList;

	private boolean mIsSearch;
	private String mKeyWords;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_memberlist);
		context = this;
		initView();
	}

	MenuItem mi;

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		mi = menu.findItem(R.id.action_search);
		searchView = (SearchView) mi.getActionView();
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

				if (null != task
						&& task.getStatus() == AsyncTask.Status.RUNNING) {
					task.cancel(true);
				}
				task = new MemberRequsetTask(mType, true);
				task.execute(id_Group, String.valueOf(0));
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

		// menu.findItem(R.id.action_send).setVisible(true);

		initData();
		bindListener();
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
				finish();
			}
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

	private void clear() {
		mLv_MemberList.setAdapter(adapter_MemberList);
		if (mIsSearch) {
			mIsSearch = false;
			adapter_MemberList_Search.clear();
			((TextView) searchView
					.findViewById(com.actionbarsherlock.R.id.abs__search_src_text))
					.setText("");
		}
	}

	/**
	 * 初始化组件
	 **/
	private void initView() {
		mLv_MemberList = (FooterListView) findViewById(R.id.layout_memberlist_lv);
	}

	/**
	 * 初始化数据
	 **/
	private void initData() {
		Bundle bundle = getIntentMsg();
		mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		isReply = bundle.getBoolean("isReply", false);
		str_Title = "";
		joinGroupStatus = "";
		mi.setVisible(false);
		switch (mType) {
		case VISITFRIEND:
			try {
				id_Member = Integer.valueOf(bundle
						.getString(BundleKey.ID_MEMBER));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			str_Title = "访客";
			break;
		case GROUPLEAGUER:
			mi.setVisible(true);
			id_Group = bundle.getString(BundleKey.ID_GROUP);
			joinGroupStatus = bundle.getString(BundleKey.JOINGROUPSTATUS);
			str_Title = getResourcesString(R.string.title_groupleaguer);
			num_Total = bundle.getInt(BundleKey.NUM);
			break;
		case FANS:
			id_OpenPage = bundle.getString(BundleKey.ID_OPENPAGE);
			str_Title = "粉丝";
			break;
		case NOTESHAREFRIEND:// 云笔记分享的人
			id_FileMembers = bundle.getString(BundleKey.ID_FILEMEMBERS);
			str_Title = "分享过的人";
			break;
		case NOTESHAREGROUP:// 云笔记分享的圈子
			id_FileMembers = bundle.getString(BundleKey.ID_FILEMEMBERS);
			str_Title = "分享过的圈子";
			break;
		case FILESHARE:
			id_FileMembers = bundle.getString(BundleKey.ID_FILEMEMBERS);
			str_Title = "分享过的人";
			break;
		case VISIT:
		case PRAISE:
			id_Group = String.valueOf(bundle.getInt(BundleKey.ID_GROUP));
			id_Quubo = String.valueOf(bundle.getInt(BundleKey.ID_QUUBOO));
			type_Object = bundle.getString("type_Object");
			if (mType == Type.VISIT)
				str_Title = "浏览";
			else if (mType == Type.PRAISE)
				str_Title = "赞";
			break;
		case BROWSE:
			id_Disstertaion = bundle.getString(BundleKey.ID_DISSERTATION);
			str_Title = "浏览";
			break;
		case REGARD:
			id_Disstertaion = bundle.getString(BundleKey.ID_DISSERTATION);
			str_Title = "关注";
			break;
		case SHARED:
			id_Disstertaion = bundle.getString(BundleKey.ID_DISSERTATION);
			str_Title = "分享过的人";
			break;
		default:
			break;
		}
		try {
			UserBusinessDatabase business = new UserBusinessDatabase(this);
			String session_key = App.app.share.getSessionKey();
			id_Owner = Integer.valueOf(business.getMemberId(session_key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// setTitle(str_Title, R.drawable.title_fanhui, R.drawable.title_home);
		ab.setTitle(str_Title);
		adapter_MemberList = new MemberListAdapter(context,
				new ArrayList<Object>(), mType, isReply);
		mLv_MemberList.setAdapter(adapter_MemberList);
		loadInfo(true);

		adapter_MemberList_Search = new MemberListAdapter(context,
				new ArrayList<Object>(), mType, isReply);
	}

	/**
	 * 绑定监听事件
	 **/
	private void bindListener() {
		mLv_MemberList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				try {
					mCurrentBean = new TransitionBean(parent
							.getItemAtPosition(position), mType);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mType == Type.NOTESHAREGROUP) {
					Bundle b = new Bundle();
					b.putString("Id", mCurrentBean.getId() + "");
					LogicUtil.enter(context, HomeGpActivity.class, b, false);
				} else if (mCurrentBean.isShowMenu()) {
					final ImageView img = (ImageView) view
							.findViewById(R.id.memberlist_item_arrow);
					L.d(TAG, "bindListener joinGroupStatus=" + joinGroupStatus);
					Type tpye = Type.DEFAULT;
					if (id_Owner == mCurrentBean.getId())
						tpye = Type.OWNER;
					else if ("GROUP_OWNER".equals(joinGroupStatus))
						tpye = Type.GROUPOWNER;
					else if ("GROUP_MANAGER".equals(joinGroupStatus))
						tpye = Type.GROUPMANAGER;
					L.d(TAG, "id_Owner:" + id_Owner + " mCurrentBean.getId():"
							+ mCurrentBean.getId());
					new MemberOperateAlertManager(context).showAlertDialog(
							tpye,
							mCurrentBean,
							img,
							AnimationManager.getArrowRotateDown(),
							new MemberOperateAlertManager.OnItemClickListener() {

								@Override
								public void onItemClick(DialogInterface dialog,
										int which) {
									onAlertItemClick(which, mCurrentBean,
											position, dialog);
								}

							});
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					bundle.putString("id", String.valueOf(mCurrentBean.getId()));
					bundle.putString("name", mCurrentBean.getName());
					LogicUtil.enter(context, HomePgActivity.class, bundle,
							false);
				}
			}
		});
		mLv_MemberList.setonLoadMoreListener(this);
	}

	/**
	 * 获取intent
	 * 
	 * @return String[]
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
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_ZERO: //
				showTip((String) msg.obj);
				break;
			case HANDLER_ONE: // 赞、访客、成员返回
				break;
			case HANDLER_TWO: // 刷新适配器
				adapter_MemberList.notifyDataSetChanged();
				break;
			case HANDLER_THREE: // 刷新人数
				num_Total--;
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(str_Title);
				strBuffer.append(" ");
				strBuffer.append("(");
				strBuffer.append(num_Total);
				strBuffer.append(")");
				setTitle(strBuffer.toString());
				int length = strBuffer.length();
				strBuffer.delete(0, length);
				strBuffer = null;
				break;
			case 4: //

				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * @param start
	 */
	private void loadInfo(boolean start) {
		if (!start && !mIsMore)
			return;
		stopTask();
		int startRecord = adapter_MemberList.getCount();
		L.d(TAG, "loadInfo start=" + start + ",startRecord=" + startRecord
				+ ",mType=" + mType);
		task = new MemberRequsetTask(mType, start);
		switch (mType) {
		case VISITFRIEND:
			task.execute(String.valueOf(id_Member), String.valueOf(startRecord));
			break;
		case GROUPLEAGUER:
			task.execute(id_Group, String.valueOf(startRecord));
			break;
		case VISIT:
			task.execute(id_Group, id_Quubo, type_Object,
					String.valueOf(startRecord));
			break;
		case PRAISE:
			task.execute(id_Group, id_Quubo, type_Object,
					String.valueOf(startRecord));
			break;
		case FANS:
			task.execute(id_OpenPage, String.valueOf(startRecord));
			break;
		case FILESHARE:
		case NOTESHAREFRIEND:
		case NOTESHAREGROUP:
			task.execute(id_FileMembers, startRecord);
			break;
		case BROWSE:
			task.execute(id_Disstertaion, String.valueOf(2),
					String.valueOf(startRecord));
			break;
		case REGARD:
			task.execute(id_Disstertaion, String.valueOf(1),
					String.valueOf(startRecord));
			break;
		case SHARED:
			task.execute(id_Disstertaion, String.valueOf(3),
					String.valueOf(startRecord));
			break;
		default:
			break;
		}
		mLv_MemberList.showLoadFooter();
	}

	@SuppressLint("NewApi")
	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	/**
	 * 获取string文件中的文字 返回值：获取到得文字
	 * 
	 * @param id
	 *            int類型 string文件中的id
	 * @return String
	 **/
	private String getResourcesString(int id) {
		return getResources().getString(id);
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

	public void onAlertItemClick(int which,
			final TransitionBean bean_Transition, int position,
			DialogInterface dialog) {
		switch (which) {
		case 0:// 拨打
			String tel = bean_Transition.getPhone();
			if (CharUtil.isValidPhone(tel)) {
				new MemberContactUtil(MemberListActivity.this).callPhone(tel);

				new Thread() {
					public void run() {
						FriendListService.getService(MemberListActivity.this)
								.saveContactTime(
										new String[] { String
												.valueOf(bean_Transition
														.getId()) });
					};
				}.start();
			} else {
				showTip("该用户未绑定手机号！");
			}
			break;
		case 1:// 打招呼
			if (null == popMenu)
				popMenu = new PopMenu(MemberListActivity.this);
			popMenu.showGridFace(KEY_FACE, R.layout.layout_memberlist,
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							popMenu.popDismiss();
							spdDialog.showProgressDialog("正在处理中...");
							new GreetMamberTask(position).execute(String
									.valueOf(bean_Transition.getId()));
						}
					});
			break;
		case 2:// 写私信
			Bundle secret = new Bundle();
			secret.putString("memberId",
					String.valueOf(bean_Transition.getId()));
			secret.putString("name", bean_Transition.getName());
			secret.putString("head", bean_Transition.getPath());
			LogicUtil.enter(MemberListActivity.this, QChatActivity.class,
					secret, false);
			break;
		case 3:// 个人主页
			Bundle b = new Bundle();
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			b.putString("id", String.valueOf(bean_Transition.getId()));
			b.putString("name", bean_Transition.getName());
			LogicUtil.enter(this, HomePgActivity.class, b, false);

			break;
		case 6:// 撤销管理
			new LeaguerManageTask().execute(bean_Transition,
					adapter_MemberList, GroupLeaguerManageEnum.REVOKEMANAGER,
					position);
			break;
		case 7:// 任命管理
			new LeaguerManageTask().execute(bean_Transition,
					adapter_MemberList,
					GroupLeaguerManageEnum.APPOINTGROUPMANAGER, position);
			break;
		case 8:// 移出圈子
			new LeaguerManageTask().execute(bean_Transition,
					adapter_MemberList, GroupLeaguerManageEnum.REMOVEFROMGROUP);
			break;
		case 4:// 取消
			dialog.dismiss();
			break;
		}
	}

	@Override
	public void onLoadMore() {
		loadInfo(false);
	}

	/**
	 * 接口数据的线程加载类
	 * 
	 * @author zhangkai
	 * 
	 */
	class GreetMamberTask extends AsyncTask<String, Integer, MCResult> {
		private int greetId;

		public GreetMamberTask(int greetId) {
			this.greetId = greetId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers
						.greetMamber(App.app, params[0], "0", String
								.valueOf(GreetUtil.GREET_CONFIG_ID[greetId]));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result == null) {
				showTip(T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					showTip(T.ErrStr);
				} else {
					showTip("打招呼成功！");
				}
			}
		}
	}

	/**
	 * 处理成员操作
	 * 
	 * @author zhuqiang
	 * 
	 */
	class LeaguerManageTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;

		public LeaguerManageTask() {
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			TransitionBean bean = (TransitionBean) mParams[0];
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.groupLeaguerManage(App.app,
						(GroupLeaguerManageEnum) mParams[2],
						String.valueOf(bean.getGroupId()),
						String.valueOf(bean.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			switch ((GroupLeaguerManageEnum) mParams[2]) {
			case APPOINTGROUPMANAGER:// 任命管理
			case REVOKEMANAGER:// 撤销管理
				((MemberListAdapter) mParams[1]).changeLeaguerStatus(
						(GroupLeaguerManageEnum) mParams[2],
						(TransitionBean) mParams[0], (Integer) mParams[3]);
				break;
			case REMOVEFROMGROUP:// 移出圈子
				((MemberListAdapter) mParams[1])
						.deleteMember((TransitionBean) mParams[0]);
				handler.sendEmptyMessage(HandlerNumberUtil.HANDLER_THREE);
				break;
			}
		}

	}

	class MemberRequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_MEMBERLIST;
		private Object[] mParams;
		private Type mType;
		private boolean mIsStart;

		/**
		 * 
		 * @param context
		 * @param isRefresh
		 */
		public MemberRequsetTask(Type type, boolean isStart) {
			mType = type;
			mIsStart = isStart;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			L.d(TAG, "doInBackground");
			mParams = params;
			MCResult result = null;
			try {
				switch (mType) {
				case VISITFRIEND:
					result = APIRequestServers.visitFriendListForIPhone(
							App.app, (String) mParams[0], (String) mParams[1],
							String.valueOf(SIZE_PAGE));
					break;
				case GROUPLEAGUER:
					if (mIsSearch) {
						result = APIRequestServers.searchGroupResource(App.app,
								GroupResourceTypeEnum.MEMBER,
								(String) mParams[0], mKeyWords,
								(String) mParams[1], String.valueOf(SIZE_PAGE),
								"false");
					} else {
						result = APIRequestServers.groupLeaguerList(App.app,
								(String) mParams[0], (String) mParams[1],
								String.valueOf(SIZE_PAGE), false);
					}
					break;
				case VISIT:
					result = APIRequestServers.resourceVisitList(App.app,
							(String) mParams[0], (String) mParams[1],
							(String) mParams[2], "false", (String) mParams[3],
							String.valueOf(SIZE_PAGE));
					break;
				case PRAISE:
					result = APIRequestServers.praiseRecords(App.app,
							(String) mParams[0], (String) mParams[1],
							(String) mParams[2], "false", (String) mParams[3],
							String.valueOf(SIZE_PAGE));
					break;
				case FANS:
					result = APIOpenPageRequestServers.openPageFansList(
							App.app, (String) mParams[0], "false",
							(String) mParams[1], String.valueOf(SIZE_PAGE));
					break;
				case NOTESHAREFRIEND:
					result = APINoteRequestServers.shareMemberList(App.app,
							(String) mParams[0], mParams[1] + "", SIZE_PAGE
									+ "");
					break;
				case NOTESHAREGROUP:
					result = APINoteRequestServers.shareGroupList(App.app,
							(String) mParams[0], mParams[1] + "", SIZE_PAGE
									+ "");
					break;
				case FILESHARE:
					result = APIFileRequestServers.fileShareMembers(App.app,
							(String) mParams[0], (Integer) mParams[1],
							SIZE_PAGE, true);
					break;
				case BROWSE:
				case REGARD:
				case SHARED:
					result = APIThemeRequestServers
							.themeFocusOrBrowseOrShareList(App.app,
									(String) mParams[0], (String) mParams[1],
									(String) mParams[2],
									String.valueOf(SIZE_PAGE));

					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings({ "null", "unchecked" })
		@Override
		protected void onPostExecute(MCResult result) {
			L.d(TAG, "onPostExecute");
			mLv_MemberList.hideLoadFooter();
			if (null == result || result.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			L.d(TAG,
					"onPostExecute result.getResultCode():"
							+ result.getResultCode());
			Object object = null;
			switch (mType) {
			case VISITFRIEND:
				object = result.getResult();
				break;
			case GROUPLEAGUER:
				object = result.getResult();
				break;
			case VISIT:
				MapResourceVisitBean bean_MapResourceVisit = (MapResourceVisitBean) result
						.getResult();
				if (null == bean_MapResourceVisit) {
					T.show(App.app, T.ErrStr);
					return;
				}
				if (mIsStart)
					num_Total = bean_MapResourceVisit.getTOTALMEMBERNUM();
				object = bean_MapResourceVisit.getLIST();
				break;
			case PRAISE:
				MapResourceGreatBean bean_MapResourceGreat = (MapResourceGreatBean) result
						.getResult();
				if (null == bean_MapResourceGreat) {
					T.show(App.app, T.ErrStr);
					return;
				}
				if (mIsStart)
					num_Total = bean_MapResourceGreat.getTOTALNUM();
				object = bean_MapResourceGreat.getPRAISELIST();
				break;
			case FANS:
				MapOpenPageFanBean groupBean = (MapOpenPageFanBean) result
						.getResult();
				if (null == groupBean) {
					T.show(App.app, T.ErrStr);
					return;
				}
				if (mIsStart)
					num_Total = groupBean.getOPEN_PAGE_FANS_NUM();
				object = groupBean.getOPEN_PAGE_FANS_LIST();
				break;
			case NOTESHAREFRIEND:
			case NOTESHAREGROUP:
				object = result.getResult();
				break;
			case FILESHARE:
				MapFileShareLeaguerBean mapBean = (MapFileShareLeaguerBean) result
						.getResult();
				if (null == mapBean) {
					T.show(App.app, T.ErrStr);
					return;
				}
				if (mIsStart)
					num_Total = mapBean.getSHARE_LEAGUERS_NUM();
				object = mapBean.getSHARE_LEAGUERS();
				break;
			case BROWSE:
			case REGARD:
			case SHARED:
				MapResourceGreatBean mapResourceGreatBean = (MapResourceGreatBean) result
						.getResult();
				if (mIsStart)
					num_Total = mapResourceGreatBean.getCOUNT();
				object = mapResourceGreatBean.getLIST();
				break;
			default:
				break;
			}
			if (mIsStart) {
				switch (mType) {
				case VISIT:
				case PRAISE:
				case FANS:
				case GROUPLEAGUER:
				case FILESHARE:
				case NOTESHAREFRIEND:
				case NOTESHAREGROUP:
				case BROWSE:
				case REGARD:
				case SHARED:
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(str_Title);
					if (num_Total > 0) {
						strBuffer.append("(");
						strBuffer.append(num_Total);
						strBuffer.append(")");
					}
					setTitle(strBuffer.toString());
					int length = strBuffer.length();
					strBuffer.delete(0, length);
					strBuffer = null;
					break;
				default:
					break;
				}
			}
			L.d(TAG, "onPostExecute num_Total" + num_Total);
			List<Object> beans_Temp = null;
			if (mIsSearch) {
				MapGroupLeaguerBean map = (MapGroupLeaguerBean) result
						.getResult();
				beans_Temp = new ArrayList<Object>();
				if (map != null && map.getSEARCHNUM() > 0)
					beans_Temp.addAll(map.getSEARCHLIST());
			} else {
				try {
					beans_Temp = (List<Object>) object;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null == beans_Temp || beans_Temp.size() == 0) {
				mIsMore = false;
				if (adapter_MemberList.getCount() == 0
						|| adapter_MemberList_Search.getCount() == 0) {
					return;
				}
				T.show(App.app, T.ErrStr);
				return;
			}
			L.d(TAG, "onPostExecute beans_Temp.size()" + beans_Temp.size());
			if (mIsSearch) {
				mLv_MemberList.setAdapter(adapter_MemberList_Search);
				adapter_MemberList_Search.clear();
				adapter_MemberList_Search.add(beans_Temp);
			} else {
				adapter_MemberList.add(beans_Temp);
			}

			mIsMore = beans_Temp.size() >= SIZE_PAGE;
			beans_Temp.clear();
			L.d(TAG, "mIsMore:" + mIsMore);
			if (!mIsMore) {
				T.show(App.app, "最后一页");
			}
		}

	}

}
