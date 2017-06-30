package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.CallAdapter;
import com.datacomo.mc.spider.android.adapter.CommentFailAdapter;
import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;
import com.datacomo.mc.spider.android.adapter.NoticeAdapter;
import com.datacomo.mc.spider.android.adapter.PletterAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChatSendBean;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.CommentSendBean;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.interfaces.OnRecallListener;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class MsgActivity extends BasicMenuActivity implements
		OnTabClickListener, OnRecallListener, OnLoadMoreListener,
		OnSearchListener, OnClearListener {
	private final String TAG = "MsgActivity";

	public static String REFRESH = "MsgActivity" + "_REFRESH";
	public final static int MSG_NOTICE = 0;
	public final static int MSG_PLETTER = 1;
	public final static int MSG_GROUPCHAT = 2;
	public final static int MSG_GREET = 3;

	public static boolean refresh = false;

	private String[] tabContent = new String[] { "通知", "私信", "圈聊", "招呼", "草稿箱" };//
	private int[] tabNums = new int[] { 0, 0, 0, 0 };
	private TabLinearLayout mTabLinearLayout;
	// private SlideMenuView slide;
	private RefreshListView listView;
	// private SearchBar searchBar;
	private int cut;
	private CallAdapter callAdapter;
	private PletterAdapter pletterAdapter, chatAdapter;
	private NoticeAdapter noticeAdapter;
	private boolean isLoading;
	private boolean frist_notice = true, frist_pletter = true,
			frist_msg = true, frist_chat = true;
	private String trendid_notice, trendId_pletter, trendId_msg, trendId_chat;
	private ArrayList<MessageGreetBean> calls;
	private ArrayList<MessageContacterBean> pletters, groupchat;
	private ArrayList<MessageNoticeBean> notices;
	private GridView greets;
	private int greetPosition = -1;
	private String memberId, memberName, noticeGreetId;
	private boolean searchState;
	private boolean isFrist = true;
	private boolean isShowTip = true;

	private String key;

	private MsgBroadcastReceiver msgReceiver;

	private ProgressDialog dialog = null;

	public static MsgActivity msgActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		titleName = "消息";
		super.onCreate(savedInstanceState);
		msgActivity = this;
		refresh = false;
		ab.setTitle(titleName);

		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(R.layout.layout_msg,
				null);
		fl.addView(rootView);

		findView();
		setView();

		registeRefreshReceiver(REFRESH);

		msgReceiver = new MsgBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.MSG_NUMBERS);
		intentFilter.addAction(BootBroadcastReceiver.QUUCHAT);
		intentFilter.addAction(BootBroadcastReceiver.QUUCHAT_REMOVE);
		registerReceiver(msgReceiver, intentFilter); // 注册监听
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "12");
		refreshNum();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (cut == 4) {
			getFailComment();
		}
	}

	private void refreshNum() {
		String nums = App.app.share.getStringMessage("isOtherLogin", "newNums",
				"0#0#0#0");
		try {
			String[] ns = nums.split("#");
			for (int i = ns.length - 1; i >= 0; i--) {
				// if (i != 2)
				mTabLinearLayout.changeSpecilText(
						formatText(tabContent[i], Integer.valueOf(ns[i])), i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int groupChatNum = App.app.share.getAllMessage("group_chat_unread", 0);
		mTabLinearLayout.changeSpecilText(
				formatText(tabContent[2], groupChatNum), 2);
		L.i(TAG, "refreshNum nums=" + nums + ",groupChatNum=" + groupChatNum);
	}

	private void findView() {
		mTabLinearLayout = (TabLinearLayout) findViewById(R.id.tabs);
		// searchBar = (SearchBar) findViewById(R.id.search_bar);
		// searchBar.setOnSearchListener(this);
		// searchBar.setOnClearListener(this);
		// searchBar.setVisibility(View.GONE);
		greets = (GridView) findViewById(R.id.table);
		greets.setAdapter(new FaceTableAdapter(this));
		greets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				L.d(TAG, "findView arg2=" + arg2 + ",cut=" + cut);
				hideGreets();
				if (null != memberId) {
					if (dialog != null && !dialog.isShowing()) {
						dialog.show();
					}
					new Thread() {
						@Override
						public void run() {
							super.run();
							try {
								MCResult mc = APIRequestServers.greetMamber(
										App.app,
										memberId,
										noticeGreetId,
										String.valueOf(GreetUtil.GREET_CONFIG_ID[arg2]));
								if (mc != null && mc.getResultCode() == 1) {
									Message msg = Message.obtain();
									msg.what = 11;
									String greetTxt = GreetUtil.GREET_TEXTS[arg2]
											.replace("[name]", memberName);
									msg.obj = greetTxt;
									handler.sendMessage(msg);
								} else {
									handler.sendEmptyMessage(10);
								}
							} catch (Exception e) {
								e.printStackTrace();
								handler.sendEmptyMessage(10);
							}
							memberId = null;
							noticeGreetId = null;
						}
					}.start();
				}

			}
		});

		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonLoadMoreListener(this);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadMsgInfo(cut, true);
			}
		});
	}

	@Override
	void onDataChanged() {
		if (refresh) {
			refresh = false;
			if (cut == MSG_PLETTER || cut == MSG_GROUPCHAT) {
				loadMsgInfo(cut, true);
			}
		}
	}

	private void setView() {
		String nums = App.app.share.getStringMessage("isOtherLogin", "newNums",
				"0#0#0#0");
		if (nums != null && !nums.startsWith("0#0#0#")) {
			try {
				String[] ns = nums.split("#");
				String[] tabs = new String[5];
				for (int i = ns.length - 1; i >= 0; i--) {
					tabs[i] = formatText(tabContent[i], Integer.valueOf(ns[i]));
					if (i != 3 && !"0".equals(ns[i]))
						cut = i;
				}
				tabs[4] = tabContent[4];
				mTabLinearLayout.changeText(tabs);
			} catch (Exception e) {
				e.printStackTrace();
				mTabLinearLayout.changeText(tabContent);
			}
		} else {
			mTabLinearLayout.changeText(tabContent);
		}
		mTabLinearLayout.setOnTabClickListener(this);

		refreshCommentNum();
		Bundle b = getIntent().getExtras();
		if (null != b) {
			cut = b.getInt("cut");
		}
	}

	private void refreshCommentNum() {
		int n = CommentSendService.getService(App.app).getCount(
				App.app.share.getSessionKey());
		String nums = App.app.share.getStringMessage("isOtherLogin", "newNums",
				"0#0#0#0");
		if (n > 0) {
			if (cut == 0 && (nums == null || nums.startsWith("0#0#0#")))
				cut = 4;
		}
		mTabLinearLayout.changeSpecilText(formatText(tabContent[4], n), 4);
	}

	private void setData(final int cutIndex) {
		L.d(TAG, "setData cut=" + cut);
		cut = cutIndex;
		mTabLinearLayout.refresh(cutIndex);
		if (MSG_NOTICE == cutIndex) {
			setNotices();
		} else if (MSG_PLETTER == cutIndex) {
			setLetters();
		} else if (MSG_GROUPCHAT == cutIndex) {
			setGroupChat();
		} else if (MSG_GREET == cutIndex) {
			setCalls();
		} else {
			getFailComment();
		}

	}

	private void setNotices() {
		if (noticeAdapter == null) {
			getLocNotices();
			noticeAdapter = new NoticeAdapter(this, notices, listView, this);
			noticeAdapter.setLocal(true);
		}
		listView.setAdapter(noticeAdapter);
		int num = 0;
		if (nums != null && nums.length > 0)
			num = nums[MSG_NOTICE];
		if (frist_notice || num > 0)
			loadMsgInfo(MSG_NOTICE, true);
	}

	private void getLocNotices() {
		try {
			notices = (ArrayList<MessageNoticeBean>) LocalDataService
					.getInstense().getLocNotices(App.app,
							LocalDataService.TXT_NOTICE);
			// TODO
			// notices = MessageNoticeBeanService.getService(this).queryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (notices == null) {
			notices = new ArrayList<MessageNoticeBean>();
		}
	}

	private void setLetters() {
		if (pletterAdapter == null) {
			getLocMessages();
			pletterAdapter = new PletterAdapter(this, pletters, listView, true);
			pletterAdapter.setLocal(true);
		}
		listView.setAdapter(pletterAdapter);
		int num = 0;
		if (nums != null && nums.length > 1)
			num = nums[MSG_PLETTER];
		if (frist_pletter || num > 0)
			loadMsgInfo(MSG_PLETTER, true);
	}

	private ArrayList<Integer> failLettersId = new ArrayList<Integer>();

	private boolean getFailLetters() {
		ArrayList<ChatSendBean> list = QChatSendService.getService(this)
				.queryOnlyId(App.app.share.getSessionKey(), 0);
		failLettersId.clear();
		pletters.clear();
		if (list != null && list.size() > 0) {
			for (ChatSendBean cBean : list) {
				MessageContacterBean bean = new MessageContacterBean();
				failLettersId.add(cBean.getId());
				bean.setContacterId(cBean.getId());
				bean.setContacterName(cBean.getName());
				bean.setLastMessageTime(cBean.getTime() + "");
				bean.setContacterType(1);
				String oType = cBean.getmType();
				if ("OBJ_VOICE".equals(oType)) {
					bean.setLastMessageContent("发了一段语音");
				} else if ("OBJ_PHOTO".equals(oType)) {
					bean.setLastMessageContent("分享照片");
				} else if ("OBJ_TEXT".equals(oType)) {
					bean.setLastMessageContent(cBean.getContent());
				}
				MemberHeadBean head = new MemberHeadBean();
				head.setHeadPath(cBean.getHead());
				bean.setContacterHead(head);
				pletters.add(bean);
			}
			return true;
		}
		return false;
	}

	private void getLocMessages() {
		try {
			pletters = LocalDataService.getInstense().getLocMessages(App.app,
					LocalDataService.TXT_PLETTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (pletters == null) {
			pletters = new ArrayList<MessageContacterBean>();
		} /*
		 * else { try { getFailLetters(); } catch (Exception e) {
		 * e.printStackTrace(); } for (MessageContacterBean bean : pletters) {
		 * if (failLettersId.contains(bean .getContacterId())) { petterNum++;
		 * int num = bean.getNewMessageNum(); if (num > 0) { int i =
		 * failLettersId.indexOf(bean .getContacterId()); MessageContacterBean
		 * bean2 = pletters .get(i); bean2.setNewMessageNum(num);
		 * pletters.remove(i); pletters.add(i, bean2); } } } }
		 */
	}

	private void setGroupChat() {
		if (chatAdapter == null) {
			getLocQuuChat();
			chatAdapter = new PletterAdapter(this, groupchat, listView, false);
			chatAdapter.setLocal(true);
		}
		listView.setAdapter(chatAdapter);
		int num = 0;
		if (nums != null && nums.length > 2)
			num = nums[MSG_GROUPCHAT];
		if (frist_chat || num > 0)
			loadMsgInfo(MSG_GROUPCHAT, true);
	}

	private ArrayList<Integer> failchatsId = new ArrayList<Integer>();

	private boolean getFailChats() {
		ArrayList<ChatSendBean> list = QChatSendService.getService(this)
				.queryOnlyId(App.app.share.getSessionKey(), 1);
		failchatsId.clear();
		groupchat.clear();
		if (list != null && list.size() > 0) {
			for (ChatSendBean cBean : list) {
				MessageContacterBean bean = new MessageContacterBean();
				failchatsId.add(cBean.getId());
				bean.setContacterId(cBean.getId());
				bean.setContacterName(cBean.getName());
				bean.setLastMessageTime(cBean.getTime() + "");
				bean.setContacterType(0);
				String oType = cBean.getmType();
				String name = new UserBusinessDatabase(App.app)
						.getName(App.app.share.getSessionKey());
				if ("OBJ_VOICE".equals(oType)) {
					bean.setLastMessageContent(name + ": [语音]");
				} else if ("OBJ_PHOTO".equals(oType)) {
					bean.setLastMessageContent(name + ": [分享照片]");
				} else if ("OBJ_TEXT".equals(oType)) {
					bean.setLastMessageContent(name + ": " + cBean.getContent());
				}
				MemberHeadBean head = new MemberHeadBean();
				head.setHeadPath(cBean.getHead());
				bean.setContacterHead(head);
				groupchat.add(bean);
			}
			return true;
		}
		return false;
	}

	private void getLocQuuChat() {
		try {
			groupchat = LocalDataService.getInstense().getLocMessages(App.app,
					LocalDataService.TXT_QUUCHAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (groupchat == null) {
			groupchat = new ArrayList<MessageContacterBean>();
		}
	}

	private void setCalls() {
		if (callAdapter == null) {
			getLocGreets();
			callAdapter = new CallAdapter(this, calls, listView, this, handler);
		}
		listView.setAdapter(callAdapter);
		int num = 0;
		if (nums != null && nums.length > 3)
			num = nums[MSG_GREET];
		if (frist_msg || num > 0)
			loadMsgInfo(MSG_GREET, true);
	}

	private int hSize, dex, top, bottom;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				hSize = listView.getHeaderViewsCount();
				dex = msg.arg1;
				bottom = msg.arg2;

				new Thread() {
					public void run() {
						try {
							sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(1);
					};
				}.start();
				break;
			case 1:
				top = greets.getTop();
				L.i(TAG, "creatHandler hSize=" + hSize + ",dex=" + dex
						+ ",top=" + top + ",bottom=" + bottom);
				listView.setSelectionFromTop(hSize + dex, top - bottom);
				break;
			case 10:
				if (dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
				showTip(T.ErrStr);
				break;
			case 11:
				if (dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
				try {
					if (cut == MSG_GREET) {
						calls.remove(greetPosition);
						changeCallNum(cut, 1);
						callAdapter.notifyDataSetChanged();
						greetPosition = -1;
						callAdapter.refersh = true;
					} else if (cut == MSG_NOTICE) {
						noticeAdapter.deleteNotices(notices.get(greetPosition));
						greetPosition = -1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				showTip((String) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	private void getLocGreets() {
		try {
			calls = LocalDataService.getInstense().getLocGreets(App.app,
					LocalDataService.TXT_GREET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (calls == null) {
			calls = new ArrayList<MessageGreetBean>();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 == null)
			return;
		if (RESULT_OK != arg1)
			return;
		if (arg0 == ChooseGroupsDialogActivity.CHOOSEGROUPOFFRIEND) {
			Bundle bundle = arg2.getExtras();
			String[] chosenIds = bundle.getStringArray(BundleKey.CHOOSEDS);
			ChooseGroupBean bean = (ChooseGroupBean) bundle
					.getSerializable(BundleKey.CHOOSEGROUPBEAN);
			spdDialog.showProgressDialog("正在添加中...");
			new AddFriendTask().execute(bean.getMemberId(), chosenIds,
					ChooseGroupsDialogActivity.mCurrent);
		} else if (arg0 == 22) {
			L.d(TAG, "onActivityResult cut=" + cut);
			if (cut == 1 && pletterAdapter != null) {
				pletterAdapter.notifyDataSetChanged();
			} else if (cut == 2 && chatAdapter != null) {
				Bundle bundle = arg2.getExtras();
				if (bundle != null) {
					int position = bundle.getInt("position", -1);
					if (position != -1 && groupchat.size() > position)
						groupchat.remove(position);
				}
				chatAdapter.notifyDataSetChanged();
			}
			refreshNum();
		}
	}

	class AddFriendTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;

		public AddFriendTask() {
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult result = null;
			try {
				result = APIRequestServers.addFriendToGroup(App.app,
						String.valueOf((Integer) mParams[0]),
						(String[]) mParams[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			T.show(App.app, "已添加！");
			ChooseGroupsDialogActivity.setIsNeedRefresh(true);
			noticeAdapter.remove((MessageNoticeBean) mParams[2]);
			new Thread(new Runnable() {
				@Override
				public void run() {
					UpdateFriendListThread.updateFriendList(MsgActivity.this,
							null);
				}
			}).start();
		}
	}

	private LoadNoticeTask noticeTask;
	private LoadPetterTask petterTask;
	private LoadGChatTask chatTask;
	private LoadGreetTask greetTask;

	private void loadMsgInfo(int which, boolean isRefresh) {
		cut = which;
		switch (which) {
		case MSG_NOTICE:
			if (null != noticeTask
					&& noticeTask.getStatus() == AsyncTask.Status.RUNNING) {
				if (searchState) {
					noticeTask.cancel(true);
					noticeTask = new LoadNoticeTask(isRefresh);
					noticeTask.execute();
				}
			} else {
				noticeTask = new LoadNoticeTask(isRefresh);
				noticeTask.execute();
			}
			break;
		case MSG_PLETTER:
			if (null != petterTask
					&& petterTask.getStatus() == AsyncTask.Status.RUNNING) {
				if (searchState) {
					petterTask.cancel(true);
					petterTask = new LoadPetterTask(isRefresh);
					petterTask.execute();
				}
			} else {
				petterTask = new LoadPetterTask(isRefresh);
				petterTask.execute();
			}
			break;
		case MSG_GROUPCHAT:
			if (null != chatTask
					&& chatTask.getStatus() == AsyncTask.Status.RUNNING) {
				if (searchState) {
					chatTask.cancel(true);
					chatTask = new LoadGChatTask(isRefresh);
					chatTask.execute();
				}
			} else {
				chatTask = new LoadGChatTask(isRefresh);
				chatTask.execute();
			}
			break;
		case MSG_GREET:
			if (null != greetTask
					&& greetTask.getStatus() == AsyncTask.Status.RUNNING) {
			} else {
				greetTask = new LoadGreetTask(isRefresh);
				greetTask.execute();
			}
			break;
		case 4:
			getFailComment();
			listView.refreshed();
			break;
		default:
			break;
		}
		// listView.showLoadFooter();
	}

	class LoadNoticeTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadNoticeTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
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
					result = loadMsg(isRefresh);
				} else {
					result = loadSearchResult(isRefresh);
				}
			} catch (Exception e) {
				result = null;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (MSG_NOTICE == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					if (isRefresh) {
						notices.clear();
					}
					MapMessageNoticeBean resultInfos0 = (MapMessageNoticeBean) mcResult
							.getResult();
					List<MessageNoticeBean> requestInfos0 = null;
					if (resultInfos0 != null)
						requestInfos0 = resultInfos0.getLIST();
					if (null == resultInfos0 || null == requestInfos0) {
						if (notices.size() != 0) {
							showTip("最后一页");
						} else {
							if (!searchState) {
								showTip("暂无通知！");
							} else {
								showTip("没有和 '" + key + "' 有关的搜索结果！");// getKey()
							}
						}
					} else {
						// TODO
						// if (isRefresh) {
						// MessageNoticeBeanService.getService(
						// MsgActivity.this).save(requestInfos0);
						// }

						mTabLinearLayout.changeSpecilText(
								tabContent[MSG_NOTICE], MSG_NOTICE);
						notices.addAll(requestInfos0);
						trendid_notice = String.valueOf(notices.size());
						noticeAdapter.setLocal(false);
						noticeAdapter.notifyDataSetChanged();
						changeCallNum(MSG_NOTICE, tabNums[MSG_NOTICE]);
					}
					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			if (isRefresh) {
				listView.onRefreshComplete();
			}
			isLoading = false;
			searchState = false;
		}
	}

	private int petterNum = 0, chatNum = 0;

	class LoadPetterTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadPetterTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
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
					result = loadMsg(isRefresh);
				} else {
					result = loadSearchResult(isRefresh);
				}
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
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (MSG_PLETTER == cut && 1 == mcResult.getResultCode()) {
					if (isRefresh) {
						pletters.clear();
						if (!searchState) {
							try {
								getFailLetters();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						petterNum = 0;
					}
					ArrayList<MessageContacterBean> requestInfos1 = (ArrayList<MessageContacterBean>) mcResult
							.getResult();
					if (null == requestInfos1 || 0 == requestInfos1.size()) {
						if (pletters.size() != 0) {
							showTip("最后一页");
						} else {
							if (!searchState) {
								showTip("暂无私信！");
							} else {
								showTip("没有和 '" + key + "' 有关的搜索结果！");// getKey()
							}
						}
					} else {
						if (!searchState) {
							ArrayList<MessageContacterBean> beans = new ArrayList<MessageContacterBean>();
							for (MessageContacterBean bean : requestInfos1) {
								if (failLettersId.contains(bean
										.getContacterId())) {
									petterNum++;
									beans.add(bean);
									int num = bean.getNewMessageNum();
									if (num > 0) {
										int i = failLettersId.indexOf(bean
												.getContacterId());
										MessageContacterBean bean2 = pletters
												.get(i);
										bean2.setNewMessageNum(num);
										pletters.remove(i);
										pletters.add(i, bean2);
									}
								}
							}

							for (MessageContacterBean bean : beans) {
								requestInfos1.remove(bean);
							}
						}
						pletters.addAll(requestInfos1);

						L.d(TAG,
								"LoadPetterTask pletters.size="
										+ pletters.size() + ",petterNum="
										+ petterNum + ",failLettersId.size="
										+ failLettersId.size());
						trendId_pletter = String.valueOf(pletters.size()
								+ petterNum - failLettersId.size());
						pletterAdapter.setLocal(false);
						pletterAdapter.notifyDataSetChanged();
					}
					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			if (isRefresh) {
				listView.onRefreshComplete();
			}
			isLoading = false;
			searchState = false;
		}
	}

	class LoadGChatTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadGChatTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
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
					result = loadMsg(isRefresh);
				} else {
					result = loadSearchResult(isRefresh);
				}
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
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (MSG_GROUPCHAT == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					if (isRefresh) {
						groupchat.clear();
						if (!searchState) {
							try {
								getFailChats();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						chatNum = 0;
					}
					ArrayList<MessageContacterBean> requestInfos1 = (ArrayList<MessageContacterBean>) mcResult
							.getResult();
					if (null == requestInfos1 || 0 == requestInfos1.size()) {
						if (groupchat.size() != 0) {
							showTip("最后一页");
						} else {
							if (isShowTip)
								if (!searchState) {
									showTip("暂无圈聊信息！");
								} else {
									showTip("没有和 '" + key + "' 有关的搜索结果！");// getKey()
								}
							isShowTip = true;
						}
					} else {
						if (!searchState) {
							ArrayList<MessageContacterBean> beans = new ArrayList<MessageContacterBean>();
							for (MessageContacterBean bean : requestInfos1) {
								if (failchatsId.contains(bean.getContacterId())) {
									chatNum++;
									beans.add(bean);
									int num = bean.getNewMessageNum();
									if (num > 0) {
										int i = failchatsId.indexOf(bean
												.getContacterId());
										MessageContacterBean bean2 = groupchat
												.get(i);
										bean2.setNewMessageNum(num);
										groupchat.remove(i);
										groupchat.add(i, bean2);
									}
								}
							}
							for (MessageContacterBean bean : beans) {
								requestInfos1.remove(bean);
							}
						}
						groupchat.addAll(requestInfos1);
						L.d(TAG, "groupchat.size=" + groupchat.size()
								+ ",chatNum=" + chatNum + ",failchatsId.size="
								+ failchatsId.size());
						trendId_chat = String.valueOf(groupchat.size()
								+ chatNum - failchatsId.size());
						chatAdapter.setLocal(false);
						chatAdapter.notifyDataSetChanged();
					}
					if (isRefresh) {
						listView.setSelection(0);
					}
				}
			}
			if (isRefresh) {
				listView.onRefreshComplete();
			}
			isLoading = false;
			searchState = false;
		}
	}

	class LoadGreetTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadGreetTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
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
					result = loadMsg(isRefresh);
				} else {
					result = loadSearchResult(isRefresh);
				}
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
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (MSG_GREET == cut && null != mcResult
						&& 1 == mcResult.getResultCode()) {
					if (isRefresh) {
						calls.clear();
					}
					ArrayList<MessageGreetBean> requestInfos2 = (ArrayList<MessageGreetBean>) mcResult
							.getResult();
					if (null == requestInfos2 || 0 == requestInfos2.size()) {
						if (calls.size() != 0) {
							showTip("最后一页");
						} else {
							if (!searchState) {
								showTip("暂无招呼！");
							} else {
								showTip("没有和 '" + key + "' 有关的搜索结果！");// getKey()
							}
						}
					} else {
						calls.addAll(requestInfos2);
						trendId_msg = String.valueOf(calls.size());
						callAdapter.notifyDataSetChanged();
					}
				}

				if (isRefresh) {
					listView.setSelection(0);
				}
			}
			if (isRefresh) {
				listView.onRefreshComplete();
			}
			isLoading = false;

		}
	}

	private void showGreets() {
		greets.setVisibility(View.VISIBLE);
		if (greetPosition >= 0) {
			listView.smoothScrollToPosition(greetPosition + 1);
		}
	}

	private void hideGreets() {
		greets.setVisibility(View.GONE);
	}

	private MCResult loadMsg(boolean refresh) throws Exception {
		MCResult mcResult = null;
		switch (cut) {
		case MSG_NOTICE:// 通知
			if (refresh) {
				trendid_notice = "0";
				frist_notice = false;
			}
			mcResult = APIRequestServers.noticeList(App.app, trendid_notice,
					"20");
			break;
		case MSG_PLETTER:// 私信
			L.i(TAG, "loadMsg refresh=" + refresh + ",trendId_pletter="
					+ trendId_pletter);
			if (refresh) {
				trendId_pletter = "0";
				frist_pletter = false;
			}
			mcResult = APIRequestServers.myMessageList(App.app,
					trendId_pletter, "20");
			break;
		case MSG_GREET:// 招呼
			if (refresh) {
				trendId_msg = "0";
				frist_msg = false;
			}
			mcResult = APIRequestServers.greetInfoList(App.app, trendId_msg,
					"20");
			break;
		case MSG_GROUPCHAT:// 圈聊
			if (refresh) {
				trendId_chat = "0";
				frist_chat = false;
			}
			mcResult = APIRequestServers.myGroupChatList(App.app, trendId_chat,
					"20", "true");
			break;
		default:
			break;
		}

		return mcResult;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.delete:
			searchState = false;
			loadMsgInfo(cut, true);
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

		// searchView.setIconifiedByDefault(false);
		// try {
		// Field field = searchView.getClass().getDeclaredField(
		// "mSearchHintIcon");
		// field.setAccessible(true);
		// ImageView iv = (ImageView) field.get(searchView);
		// iv.setImageResource(R.drawable.action_search);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

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
					showBack();
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				key = s;
				searchState = true;
				loadMsgInfo(cut, true);
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
				searchState = false;
				loadMsgInfo(cut, true);
				showMenu();
				return false;
			}
		});

		menu.findItem(R.id.action_message_send).setVisible(true);
		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		L.d(TAG, "onCreateOptionsMenu isFrist=" + isFrist + ",cut=" + cut);
		if (isFrist) {
			isFrist = false;
			setData(cut);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_message_send).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		menu.findItem(R.id.action_message).setVisible(drawerOpen);
		menu.findItem(R.id.action_write).setVisible(drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				searchState = false;
				loadMsgInfo(cut, true);
				return false;
				// } else {
				// finish();
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_message_send:
			Bundle b = new Bundle();
			b.putBoolean("isSendMsg", true);
			b.putInt("type", 2);
			LogicUtil.enter(this, FriendsChooserActivity.class, b, false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabClick(View tab) {
		if (greets.getVisibility() == View.VISIBLE) {
			hideGreets();
		}
		// searchBar.clearText();
		listView.onRefreshComplete();
		isLoading = false;
		searchState = false;
		cut = (Integer) tab.getTag();
		L.i(TAG, "onTabClick cut=" + cut);
		getData(cut);
		if (cut == MSG_NOTICE || cut == MSG_PLETTER || cut == MSG_GROUPCHAT) {
			// searchBar.setVisibility(View.VISIBLE);
			searchView.setVisibility(View.VISIBLE);
		} else {
			// searchBar.setVisibility(View.GONE);
			searchView.setVisibility(View.GONE);
		}
		showMenu();
		searchView.onActionViewCollapsed();
		searchState = false;
	}

	public void performOnTabClick(int tag) {
		mTabLinearLayout.refresh(tag);
	}

	private void getData(int which) {
		switch (which) {
		case MSG_NOTICE:
			setNotices();
			break;
		case MSG_PLETTER:
			setLetters();
			break;
		case MSG_GROUPCHAT:
			setGroupChat();
			break;
		case MSG_GREET:
			setCalls();
			break;
		case 4:
			getFailComment();
			break;
		default:
			break;
		}
	}

	private void getFailComment() {
		setLoadingState(true);
		ArrayList<CommentSendBean> csbs = CommentSendService.getService(this)
				.queryAll(App.app.share.getSessionKey());
		if (csbs == null) {
			csbs = new ArrayList<CommentSendBean>();
			showTip("暂无草稿");
		}
		CommentFailAdapter cfa = new CommentFailAdapter(this, csbs);
		cfa.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				refreshCommentNum();
				super.onChanged();
			}
		});
		listView.setAdapter(cfa);
		setLoadingState(false);
		listView.onRefreshComplete();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchState) {
				searchState = false;
				loadMsgInfo(cut, true);
				return true;
			} else if (greets.getVisibility() == View.VISIBLE) {
				hideGreets();
				return true;
			} else if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				searchState = false;
				loadMsgInfo(cut, true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		msgActivity = null;
		unregisterReceiver(msgReceiver); // 取消监听
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onRecall(int position, String memberId, String memberName,
			String noticeGreetId) {
		if (cut == MSG_GREET || cut == MSG_NOTICE)
			greetPosition = position;
		showGreets();
		this.memberId = memberId;
		this.memberName = memberName;
		this.noticeGreetId = noticeGreetId;
	}

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadMsgInfo(cut, false);
	}

	private MCResult loadSearchResult(boolean refresh) throws Exception {
		MCResult mcResult = null;
		switch (cut) {
		case MSG_NOTICE:// 通知
			if (refresh) {
				trendid_notice = "0";
			}
			L.d(TAG, "loadSearchResult refresh=" + refresh + ",trendid_notice="
					+ trendid_notice);
			mcResult = APIRequestServers.searchNotices(App.app, key,
					trendid_notice, "20", "false");
			break;
		case MSG_PLETTER:// 私信
			if (refresh) {
				trendId_pletter = "0";
			}
			mcResult = APIRequestServers.searchMessage(App.app, key,
					trendId_pletter, "10", "true");
			break;
		case MSG_GROUPCHAT:// 圈聊
			if (refresh) {
				trendId_chat = "0";
			}
			mcResult = APIRequestServers.searchGroupChat(App.app, key,
					trendId_chat, "10", "true");
			break;
		case MSG_GREET:// 招呼

			break;
		default:
			break;
		}

		return mcResult;
	}

	// private String getKey() {
	// return searchBar.getKeyWords();
	// }

	@Override
	public void onSearch(String keyWords) {
		searchState = true;
		loadMsgInfo(cut, true);
	}

	@Override
	public void onClear(String keyWords) {
		searchState = false;
		loadMsgInfo(cut, true);
	}

	private int[] nums;

	public class MsgBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.MSG_NUMBERS.equals(action)) {
				Bundle b = intent.getExtras();
				nums = b.getIntArray("nums");
				if (nums != null) {
					for (int i = 0; i < nums.length; i++) {
						if (i != 2) {
							int num = nums[i];
							// L.d(TAG, "MsgBroadcastReceiver num=" + num);
							try {
								mTabLinearLayout.changeSpecilText(
										formatText(tabContent[i], num), i);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			// TODO
			// if (BootBroadcastReceiver.QUUCHAT.equals(action)) {
			// int groupChatNum = App.app.share.getAllMessage(
			// "group_chat_unread", 0);
			// L.d(TAG, "MsgBroadcastReceiver groupChatNum=" + groupChatNum);
			// mTabLinearLayout.changeSpecilText(
			// formatText(tabContent[2], groupChatNum), 2);
			//
			// if (cut != MSG_GROUPCHAT) {
			// frist_chat = true;
			// return;
			// }
			// GroupChatMessageBean bean = (GroupChatMessageBean) intent
			// .getSerializableExtra("Chat");
			// String lastMessageContent = bean.getSendMemberName() + ":";
			// ObjectInfoBean ob = bean.getMessageList().get(0);
			// if (ob.getMessageType() == 1) {
			// String ot = ob.getObjectType();
			// if ("OBJ_TEXT".equals(ot)) {
			// lastMessageContent += ob.getMessageContent();
			// } else if ("OBJ_PHOTO".equals(ot)) {
			// lastMessageContent += "分享照片";
			// } else if ("OBJ_VOICE".equals(ot)) {
			// lastMessageContent += "分享语音";
			// }
			// }
			// int gid = intent.getIntExtra("gId", 0);
			// if (gid > 0 && groupchat != null) {
			// for (int i = 0; i < groupchat.size(); i++) {
			// MessageContacterBean mb = groupchat.get(i);
			// mb.setContacterLeaguerId(1);
			// // mb.setNewMessageNum(App.app.share.getIntMessage(
			// // "group_chat_unread", gid + "", 0));
			// if (gid == mb.getContacterId()) {
			// mb.setLastMessageContent(lastMessageContent);
			// groupchat.remove(i);
			// groupchat.add(i, mb);
			// chatAdapter.setLocal(false);
			// chatAdapter.notifyDataSetChanged();
			// return;
			// }
			// }
			// loadMsgInfo(MSG_GROUPCHAT, true);
			// }
			// } else if (BootBroadcastReceiver.QUUCHAT_REMOVE.equals(action)) {
			// isShowTip = false;
			// loadMsgInfo(MSG_GROUPCHAT, true);
			// }
		}
	}

	private String formatText(String cutName, int num) {
		if (null == cutName || num < 0)
			return "";
		if (num == 0) {
			return cutName;
		} else if (num < 100) {
			return cutName + "(" + num + ")";
		} else {
			return cutName + "(99+)";
		}
	}

	public void changeCallNum(int which, int num) {
		if (tabNums[which] > 99) {
			if (tabNums[which] > num) {
				tabNums[which] = tabNums[which] - num;
			} else {
				tabNums[which] = 0;
			}
		}
		if (0 != tabNums[which]) {
			mTabLinearLayout.changeSpecilText(
					formatText(tabContent[which], num), which);
		} else {
			mTabLinearLayout.changeSpecilText(tabContent[which], which);
		}
	}

	@Override
	protected void refresh() {
		if (!isLoading) {
			listView.refreshUI();
			loadMsgInfo(cut, true);
		}
		listView.setSelection(0);
	}

}
