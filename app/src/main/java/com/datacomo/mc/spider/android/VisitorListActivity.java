package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.MemberListAdapter;
import com.datacomo.mc.spider.android.animation.AnimationManager;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.manager.MemberOperateAlertManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.PopMenu;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class VisitorListActivity extends BaseMidMenuActivity implements
		OnRefreshListener, OnLoadMoreListener {
	private TextView textView;
	private RefreshListView listView;

	private LoadInfoTask task;
	private boolean noMore;
	private boolean isLoading = true;
	private List<Object> beans;
	private MemberListAdapter visitorAdapter;
	private int startRecord;
	private TransitionBean mCurrentBean;
	private PopMenu popMenu;
	private final String KEY_FACE = "face";

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_file_list);
		findViews();
		initData();
		bindListener();
	}

	@Override
	void initData() {
		String num = "";
		if (0 != visitMemberNum) {
			num = "(" + visitMemberNum + ")";
		}
		if (userSelf) {
			setTitle(MM_VISITOR + num, R.drawable.title_fanhui, null, false);
			// slide.setScrollEnable(true);
		} else {
			setTitle(MM_VISITOR + num, R.drawable.title_fanhui, null, false);
			// slide.setScrollEnable(false);
		}
		loadInfo(true);
	}

	private void findViews() {
		setMidChosen(4);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("");
		textView.setVisibility(View.GONE);
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		beans = new ArrayList<Object>();
		visitorAdapter = new MemberListAdapter(this, beans, Type.VISITFRIEND);
		listView.setAdapter(visitorAdapter);

	}

	public void bindListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				try {
					mCurrentBean = new TransitionBean(parent
							.getItemAtPosition(position));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!mCurrentBean.isShowMenu())
					return;
				final ImageView img = (ImageView) view
						.findViewById(R.id.memberlist_item_arrow);
				new MemberOperateAlertManager(VisitorListActivity.this)
						.showAlertDialog(
								Type.DEFAULT,
								mCurrentBean,
								img,
								AnimationManager.getArrowRotateDown(),
								new MemberOperateAlertManager.OnItemClickListener() {

									@Override
									public void onItemClick(
											DialogInterface dialog, int which) {
										onAlertItemClick(which, mCurrentBean,
												position, dialog);
									}

								});

			}

		});
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadInfoTask(isRefresh);
			task.execute();
			listView.showLoadFooter();
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	@Override
	protected void onRefreshClick(View v) {
		super.onRefreshClick(v);
		loadInfo(true);
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadInfoTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
			textView.setVisibility(View.GONE);
			showHeaderProgress();
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = loadDate(isRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			hintHeaderProgress();
			listView.showFinishLoadFooter();
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					List<FriendBean> requestInfos = (List<FriendBean>) mcResult
							.getResult();
					if (null == requestInfos || requestInfos.size() == 0) {
						if (beans.size() == 0) {
							textView.setVisibility(View.VISIBLE);
							textView.setText("还没有访客哦！");
						} else {
							showTip("最后一页");
						}
						noMore = true;
					} else {
						if (isRefresh || 0 == startRecord) {
							beans.clear();
							beans.addAll(requestInfos);
							startRecord = startRecord + requestInfos.size();
							listView.setAdapter(visitorAdapter);
						} else {
							beans.addAll(requestInfos);
							startRecord = startRecord + requestInfos.size();
							visitorAdapter.notifyDataSetChanged();
						}
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

	private MCResult loadDate(boolean refresh) throws Exception {
		MCResult mcResult = null;
		if (refresh) {
			startRecord = 0;
			noMore = false;
		}
		if (TYPE_MBER == sType) {
			mcResult = APIRequestServers.visitFriendListForIPhone(
					VisitorListActivity.this, String.valueOf(id),
					String.valueOf(startRecord),
					String.valueOf(PageSizeUtil.SIZEPAGE_MEMBERLIST));
		} else if (TYPE_GROUP == sType) {
		} else if (TYPE_OPAGE == sType) {
		}
		return mcResult;
	}

	@Override
	public void onRefresh() {
		loadInfo(true);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading && !noMore) {
			loadInfo(false);
		}
	}

	public void onAlertItemClick(int which,
			final TransitionBean bean_Transition, int position,
			DialogInterface dialog) {
		switch (which) {
		case 0:// 拨打
			String tel = bean_Transition.getPhone();
			if (CharUtil.isValidPhone(tel)) {
				new MemberContactUtil(VisitorListActivity.this).callPhone(tel);

				new Thread() {
					public void run() {
						FriendListService.getService(VisitorListActivity.this)
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
			if (null == popMenu) {
				popMenu = new PopMenu(VisitorListActivity.this);
				// popMenu.CreatGridFace(KEY_FACE, new OnItemClickListener() {
				//
				// @Override
				// public void onItemClick(AdapterView<?> parent, View view,
				// int position, long id) {
				// popMenu.popDismiss();
				// spdDialog.showProgressDialog("正在处理中...");
				// new GreetMemberTask(VisitorListActivity.this).execute(String
				// .valueOf(bean_Transition.getId()));
				// }
				// });
				popMenu.showGridFace(KEY_FACE, R.layout.layout_memberlist,
						new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								popMenu.popDismiss();
								spdDialog.showProgressDialog("正在处理中...");
								new GreetMemberTask(VisitorListActivity.this)
										.execute(String.valueOf(bean_Transition
												.getId()));
							}
						});
				// } else {
				// popMenu.showGridFace(KEY_FACE, R.layout.layout_memberlist);
			}
			break;
		case 2:// 写私信
			Bundle secret = new Bundle();
			secret.putString("memberId",
					String.valueOf(bean_Transition.getId()));
			secret.putString("name", bean_Transition.getName());
			secret.putString("head", bean_Transition.getPath());
			LogicUtil.enter(VisitorListActivity.this, QChatActivity.class,
					secret, false);
			break;
		case 3:// 个人主页
			Bundle b = new Bundle();
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			b.putString("id", String.valueOf(bean_Transition.getId()));
			b.putString("name", bean_Transition.getName());
			LogicUtil.enter(this, HomePgActivity.class, b, false);
			break;
		case 4:// 取消
			dialog.dismiss();
			break;
		}
	}

	/**
	 * 接口数据的线程加载类
	 * 
	 * @author zhangkai
	 * 
	 */
	class GreetMemberTask extends AsyncTask<String, Integer, MCResult> {
		private Context mContext;
		private int greetId;

		public GreetMemberTask(Context context) {
			mContext = context;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers
						.greetMamber(mContext, params[0], "0", String
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
}
