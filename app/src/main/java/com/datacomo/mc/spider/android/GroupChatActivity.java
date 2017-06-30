package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.adapter.GroupChatAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.MapGroupChatMemberList;
import com.datacomo.mc.spider.android.net.been.groupchat.MemberSimpleBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.T;

public class GroupChatActivity extends BasicActionBarActivity {
	private static final String TAG = "GroupChatActivity";
	private GridView gv;
	private ArrayList<MemberSimpleBean> chatList;
	private GroupChatAdapter groupChatAdapter;
	// private MemberSimpleBean memberSimpleBean;
	private Button exit_btn;
	private int groupId;
	private boolean isManager;
	private int chatId;
	private LinearLayout footer;

	private int startRecord = 0;
	private int maxResults = 20;
	private int screenWidth;

	private boolean isRefresh = true;
	private boolean isFrist = true;

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
		setContent(R.layout.group_chat_member);
		init();
	}

	private void init() {
		// setTitle("圈聊成员", R.drawable.title_fanhui, R.drawable.title_home);
		ab.setTitle("圈聊成员");
		getIntentMsg();
		gv = (GridView) findViewById(R.id.group_gridView);
		footer = (LinearLayout) findViewById(R.id.footer);
		exit_btn = (Button) findViewById(R.id.exit_btn);
		chatList = new ArrayList<MemberSimpleBean>();

		screenWidth = BaseData.getScreenWidth();

		groupChatAdapter = new GroupChatAdapter(GroupChatActivity.this,
				chatList, screenWidth, groupId, isManager);
		// memberSimpleBean = new MemberSimpleBean();
		gv.setAdapter(groupChatAdapter);

		new loadInfoTask(GroupChatActivity.this, chatId).execute();
		gv.setEmptyView(footer);
		// 分页
		gv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
					if (groupChatAdapter.inDelete()) {
						groupChatAdapter.cancelDelete();
						return true;
					}
				}
				return false;
			}
		});
		gv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE
						&& gv.getFirstVisiblePosition() == 0) {
					if (isRefresh) {
						// groupChatAdapter.notifyDataSetChanged();
						new loadInfoTask(GroupChatActivity.this, chatId)
								.execute();
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// if (isRefresh) {
				// if (firstVisibleItem + visibleItemCount >= totalItemCount -
				// 2) {
				// new loadInfoTask(GroupChatActivity.this, chatId)
				// .execute();
				// }
				// }
				// gv.smoothScrollToPosition(chatList.size()-1);
			}
		});
		// 退出圈聊
		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				spdDialog.showProgressDialog("正在处理中...");
				new exitGroupChat(GroupChatActivity.this, groupId).execute();
			}
		});
	}

	/**
	 * 创建Handler消息队列
	 * 
	 * @return Handler
	 **/
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: //
				gv.smoothScrollToPosition(chatList.size() - 1);
				isFrist = false;
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 获取intent
	 * 
	 * @return String
	 **/
	private void getIntentMsg() {
		Bundle b = getIntent().getExtras();
		chatId = b.getInt("chatId");
		groupId = b.getInt("groupId");
		isManager = b.getBoolean("isManager");
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

	/**
	 * 异步获取圈聊成员
	 * 
	 * @author Administrator
	 * 
	 */
	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;
		private int chatId;

		public loadInfoTask(Context context, int chatId) {
			this.context = context;
			this.chatId = chatId;

		}

		@Override
		protected MCResult doInBackground(String... arg0) {
			isRefresh = false;
			MCResult result = null;
			try {
				result = APIGroupChatRequestServers.getGroupChatMemberList(
						context, chatId, startRecord, maxResults, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			footer.setVisibility(View.GONE);
			isRefresh = true;
			if (result != null && result.getResultCode() == 1) {
				MapGroupChatMemberList chatmemberList = (MapGroupChatMemberList) result
						.getResult();
				List<MemberSimpleBean> CHATMEMBERLIST = chatmemberList
						.getCHATMEMBERLIST();
				if (CHATMEMBERLIST == null || CHATMEMBERLIST.size() == 0) {
					// showTip("没有更多成员");
				} else {
					int size = chatList.size();
					if (size != 0) {
						chatList.remove(size - 1);
					}
					// chatList.addAll(CHATMEMBERLIST);
					chatList.addAll(0, CHATMEMBERLIST);
					chatList.add(new MemberSimpleBean());
					chatList.add(new MemberSimpleBean());
					groupChatAdapter.notifyDataSetChanged();
					if (isFrist) {
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

					}
					// if (startRecord == 0) {
					// gv.setSelection(size);
					// }
					startRecord = startRecord + maxResults;
				}
			} else {
				showTip(T.ErrStr);
			}
		}
	}

	/**
	 * 退出圈聊
	 * 
	 * @author Administrator
	 * 
	 */
	class exitGroupChat extends AsyncTask<String, Integer, MCResult> {

		private int groupId;
		private Context context;

		public exitGroupChat(Context context, int groupId) {
			this.groupId = groupId;
			this.context = context;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			L.d(TAG, "GroupChatActivity groupId" + groupId);
			try {
				result = APIGroupChatRequestServers.exitGroupChat(context,
						groupId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result != null && result.getResultCode() == 1) {
				try {
					if ((Integer) result.getResult() == 1) {
						finish();
						QuuChatActivity.instance.finish();
					} else {
						T.show(context, T.ErrStr);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				T.show(context, T.ErrStr);
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (groupChatAdapter.inDelete()) {
				groupChatAdapter.cancelDelete();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
