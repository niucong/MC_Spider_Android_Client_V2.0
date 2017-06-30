package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseActivity;
import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.MemberListAdapter;
import com.datacomo.mc.spider.android.animation.AnimationManager;
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

public class HomeVisitorFragment extends BaseGroupFragment implements
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

	private HomePgActivity homePg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homePg = (HomePgActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_file_list, null);
		findViews(view);
		loadInfo(true);
		return view;
	}

	private void findViews(ViewGroup view) {
		if (homePg.menu != null) {
			if (homePg.menu.findItem(R.id.action_search) != null)
				homePg.menu.findItem(R.id.action_search).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write) != null)
				homePg.menu.findItem(R.id.action_write).setVisible(false);
			if (homePg.menu.findItem(R.id.action_write_mood) != null)
				homePg.menu.findItem(R.id.action_write_mood).setVisible(false);
			if (homePg.menu.findItem(R.id.action_more) != null)
				homePg.menu.findItem(R.id.action_more).setVisible(false);
		}
		// setMidChosen(4);
		textView = (TextView) view.findViewById(R.id.textView);
		textView.setText("");
		textView.setVisibility(View.GONE);
		listView = (RefreshListView) view.findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setEmptyView(textView);
		beans = new ArrayList<Object>();
		visitorAdapter = new MemberListAdapter(homePg, beans, Type.VISITFRIEND);
		listView.setAdapter(visitorAdapter);

		bindListener();
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
				new MemberOperateAlertManager(homePg).showAlertDialog(
						Type.DEFAULT, mCurrentBean, img,
						AnimationManager.getArrowRotateDown(),
						new MemberOperateAlertManager.OnItemClickListener() {

							@Override
							public void onItemClick(DialogInterface dialog,
									int which) {
								onAlertItemClick(which, mCurrentBean, position,
										dialog);
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
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	@Override
	public void onFragmentRefresh(FragmentActivity fContext) {
		super.onFragmentRefresh(fContext);
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
			homePg.setLoadingState(true);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
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
			homePg.setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			listView.showFinishLoadFooter();
			if (null == mcResult) {
				homePg.showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					homePg.showTip(T.ErrStr);
				}
				if (null != mcResult && 1 == mcResult.getResultCode()) {
					List<FriendBean> requestInfos = (List<FriendBean>) mcResult
							.getResult();
					if (null == requestInfos || requestInfos.size() == 0) {
						if (beans.size() == 0) {
							textView.setVisibility(View.VISIBLE);
							textView.setText("还没有访客哦！");
						} else {
							homePg.showTip("最后一页");
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
		if (HomePgActivity.TYPE_MBER == homePg.sType) {
			mcResult = APIRequestServers.visitFriendListForIPhone(homePg,
					String.valueOf(homePg.id), String.valueOf(startRecord),
					String.valueOf(PageSizeUtil.SIZEPAGE_MEMBERLIST));
		} else if (HomePgActivity.TYPE_GROUP == homePg.sType) {
		} else if (HomePgActivity.TYPE_OPAGE == homePg.sType) {
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
				new MemberContactUtil(homePg).callPhone(tel);

				new Thread() {
					public void run() {
						FriendListService.getService(homePg).saveContactTime(
								new String[] { String.valueOf(bean_Transition
										.getId()) });
					};
				}.start();
			} else {
				homePg.showTip("该用户未绑定手机号！");
			}
			break;
		case 1:// 打招呼
			if (null == popMenu) {
				popMenu = new PopMenu(homePg);
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
								BaseActivity.spdDialog
										.showProgressDialog("正在处理中...");
								new GreetMemberTask(homePg).execute(String
										.valueOf(bean_Transition.getId()));
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
			LogicUtil.enter(homePg, QChatActivity.class, secret, false);
			break;
		case 3:// 个人主页
			Bundle b = new Bundle();
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			b.putString("id", String.valueOf(bean_Transition.getId()));
			b.putString("name", bean_Transition.getName());
			LogicUtil.enter(homePg, HomePgActivity.class, b, false);
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
			BaseActivity.spdDialog.cancelProgressDialog(null);
			if (result == null) {
				homePg.showTip(T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					homePg.showTip(T.ErrStr);
				} else {
					homePg.showTip("打招呼成功！");
				}
			}
		}
	}

	@Override
	public void search(String s) {
		// TODO
	}
}
