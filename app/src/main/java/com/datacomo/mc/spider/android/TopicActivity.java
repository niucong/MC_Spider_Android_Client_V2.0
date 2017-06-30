package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.GroupQuuboAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class TopicActivity extends BasicActionBarActivity implements
		OnLoadMoreListener, OnRefreshListener {
	private final String TAG = "TopicActivity";

	private RefreshListView listView;
	private ArrayList<ResourceBean> quubos;
	private boolean isLoading;
	private GroupQuuboAdapter adapter;
	private int topicId;
	private int groupId;
	private String trendId;
	private LoadTopicQbTask task;
	private String joinState;
	private ProgressDialog pd;
	private String f1 = " + 关注";
	private String f2 = "取消关注";
	private String shareWord;
	private TextView focus;
	private TopicHandler mHandler;
	private AlertDialog.Builder buidler;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_topic);
		setViews();
		mHandler = new TopicHandler();
		loadInfo(true);
	}

	private void setViews() {
		Bundle b = getIntent().getExtras();
		if (null == b) {
			showTip("获取数据错误！");
			finish();
		}
		GroupThemeBean bean = (GroupThemeBean) b.getSerializable("data");
		if (null == bean) {
			showTip("获取数据错误！");
			finish();
		}
		joinState = b.getString("joinState") == null ? "" : b
				.getString("joinState");

		topicId = bean.getThemeId();
		groupId = bean.getGroupId();
		ViewGroup topicEntity = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.item_group_topic, null);
		LinearLayout topicContent = (LinearLayout) topicEntity
				.findViewById(R.id.content);
		topicEntity.removeView(topicContent);
		((FrameLayout) findViewById(R.id.topic_layout)).addView(topicContent);

		TextView ownerName = (TextView) topicContent.findViewById(R.id.name);
		TextView topicTitle = (TextView) topicContent.findViewById(R.id.topic);
		TextView topicDate = (TextView) topicContent
				.findViewById(R.id.publishdate);
		ImageView ownerHead = (ImageView) topicContent.findViewById(R.id.head);

		int ownerId = bean.getMemberId();

		String name = bean.getMemberName();
		if (ownerId == GetDbInfoUtil.getMemberId(this)) {
			name = "我";
		}
		ownerName.setText(name + " 发起");

		String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getMemberFullPath(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setHeader(this, ownerHead, headUrl);

		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getCreateTime()));
		topicDate.setText(date);

		topicContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString(BundleKey.ID_DISSERTATION, topicId + "");
				LogicUtil.enter(TopicActivity.this,
						DissertationInfoActivity.class, b, false);
			}
		});

		String content = bean.getThemeContent();
		LayoutParams linearLp = (LayoutParams) topicTitle
				.getLayoutParams();
		linearLp.rightMargin = DensityUtil.dip2px(this, 105);
		topicTitle.setLayoutParams(linearLp);
		topicTitle.setText("#" + content + "#");
		if (content.length() > 10) {
			content = content.substring(0, 10) + "...";
		}
		// setTitle("#" + content + "#", R.drawable.title_fanhui,
		// R.drawable.icon_share);
		ab.setTitle("#" + content + "#");

		listView = (RefreshListView) findViewById(R.id.listview);
		quubos = new ArrayList<ResourceBean>();
		adapter = new GroupQuuboAdapter(this, quubos, joinState);
		listView.setAdapter(adapter);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		listView.setDivider(getResources().getDrawable(R.drawable.divider_gray));
		listView.setDividerHeight(15);
		listView.setHeaderDividersEnabled(true);
		listView.setFooterDividersEnabled(false);

		((TextView) findViewById(R.id.numOfQuubo)).setText("共 "
				+ bean.getQuuboNum() + " 个圈博");
		focus = (TextView) findViewById(R.id.focus);
		setFocusState(bean.isHasFocus());
		focus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ((Boolean) v.getTag()) {
					new SetFocusTask(TopicActivity.this, "2").execute();
				} else {
					new SetFocusTask(TopicActivity.this, "1").execute();
				}
			}
		});
	}

	class SetFocusTask extends AsyncTask<String, Integer, MCResult> {
		private Context c;
		private String action;

		public SetFocusTask(Context context, String actionType) {
			c = context;
			action = actionType;
			isShowProgressDLG(true, "1".equals(action) ? "正在关注..."
					: "正在取消关注...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIThemeRequestServers.attentionOrCancelTheme(c,
						topicId + "", action);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				Object o = focus.getTag();
				if ((Boolean) o) { // o为之前的状态 这里设为相反状态
					setFocusState(false);
				} else {
					setFocusState(true);
				}
			}
			isShowProgressDLG(false, null);
		}
	}

	/**
	 * 显示或取消dialog
	 * 
	 * @param isShow
	 *            是否显示dialog
	 * @param msg
	 *            isShow = true msg为提示内容
	 * 
	 */
	private void isShowProgressDLG(boolean isShow, String msg) {
		if (isShow) {
			if (null == pd) {
				pd = new ProgressDialog(this);
				pd.setCancelable(false);
				pd.setMessage(msg);
				pd.show();
			}
		} else {
			if (null != pd && pd.isShowing()) {
				pd.cancel();
				pd = null;
			}
		}
	}

	private void setFocusState(boolean blean) {
		if (blean) {
			focus.setTag(true);
			focus.setText(f2);
			focus.setBackgroundResource(R.drawable.bg_light_yellow);
		} else {
			focus.setTag(false);
			focus.setText(f1);
			focus.setBackgroundResource(R.drawable.bg_light_green);
		}
	}

	class LoadTopicQbTask extends AsyncTask<String, Integer, MCResult> {
		private boolean isRefresh;

		public LoadTopicQbTask(boolean isRefresh) {
			isLoading = true;
			this.isRefresh = isRefresh;
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
				result = loadData(isRefresh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			listView.showFinishLoadFooter();
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				showResult(isRefresh, mcResult);
				if (isRefresh) {
					listView.setSelection(0);
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

	private MCResult loadData(boolean isRefresh) throws Exception {
		if (isRefresh) {
			trendId = "0";
		}
		return APIThemeRequestServers.quuboListForGroupByTheme(this, groupId
				+ "", topicId + "", trendId, "20");
	}

	private void showResult(boolean isRefresh, MCResult mcResult) {
		MapResourceBean topicResult = (MapResourceBean) mcResult.getResult();
		ArrayList<ResourceBean> requestInfos1 = (ArrayList<ResourceBean>) topicResult
				.getLIST();
		if (null == requestInfos1 || requestInfos1.size() == 0) {
			if (quubos.size() == 0) {
				// textView.setText("目前还没有文件！");
				// textView.setVisibility(View.VISIBLE);
			} else {
				showTip("最后一页");
			}
		} else {
			if (isRefresh) {
				quubos.clear();
			}
			quubos.addAll(requestInfos1);
			trendId = String.valueOf(quubos.size());
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_share).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_share:
			Bundle b = new Bundle();
			b.putInt(BundleKey.ID_GROUP, groupId);
			b.putSerializable(BundleKey.TYPE_REQUEST, Type.GROUPLEAGUER);
			LogicUtil.enter(this, GroupLeaguerChooseActivity.class, b,
					GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// Bundle b = new Bundle();
	// b.putInt(BundleKey.ID_GROUP, groupId);
	// b.putSerializable(BundleKey.TYPE_REQUEST, Type.GROUPLEAGUER);
	// LogicUtil.enter(this, GroupLeaguerChooseActivity.class, b,
	// GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER);
	// }

	@Override
	public void onLoadMore() {
		if (!isLoading)
			loadInfo(false);
	}

	void loadInfo(boolean isRefresh) {
		try {
			stopTask();
			task = new LoadTopicQbTask(isRefresh);
			task.execute();
		} catch (RejectedExecutionException re) {
			re.printStackTrace();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	@Override
	public void onRefresh() {
		loadInfo(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle result = data.getExtras();
			if (null == result) {
				return;
			}
			@SuppressWarnings("unchecked")
			HashMap<String, String> ids = (HashMap<String, String>) result
					.getSerializable(BundleKey.CHOOSEDS);
			int size = ids.size();
			final boolean isSelectAll = data.getBooleanExtra(
					BundleKey.ISSELECTALL, false);
			L.d(TAG, "onActivityResult isSelectAll=" + isSelectAll + ",size="
					+ size + android.os.Build.BRAND);
			if (!isSelectAll && 0 == size) {
				showTip("您未选择好友，请重新选择！");
				return;
			}

			final String[] memberIds = new String[size];
			Iterator<String> iterator = ids.keySet().iterator();
			EditText edit = new EditText(this);
			edit.setHint("顺便说点什么吧");
			edit.setMinLines(3);
			edit.setMaxLines(5);
			edit.setTextSize(14);
			edit.setGravity(Gravity.TOP);
			edit.setTextColor(Color.BLACK);
			int i = 0;
			while (iterator.hasNext()) {
				memberIds[i] = iterator.next();
				i++;
			}
			buidler = new AlertDialog.Builder(this)
					.setTitle("分享主题")
					.setView(edit)
					.setCancelable(true)
					.setPositiveButton("分享",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isShowProgressDLG(true, "正在分享");
									new Thread(new Runnable() {
										@Override
										public void run() {
											MCResult mcResult = null;
											try {
												mcResult = APIThemeRequestServers
														.shareThemeToMember(
																TopicActivity.this,
																groupId + "",
																topicId + "",
																memberIds,
																""
																		+ isSelectAll,
																shareWord);
											} catch (Exception e) {
												e.printStackTrace();
											}
											if (null != mcResult
													&& mcResult.getResultCode() == 1) {
												HandlerUtil.sendMsgToHandler(
														mHandler, 0, "已成功！", 0);
											} else {
												HandlerUtil.sendMsgToHandler(
														mHandler, 0, T.ErrStr,
														0);
											}
										}
									}).start();
								}
							}).setNegativeButton("取消", null);
			buidler.show();
		}
	}

	@SuppressLint("HandlerLeak")
	class TopicHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				showTip((String) msg.obj);
				isShowProgressDLG(false, null);
				break;
			default:
				break;
			}
		}
	}
}
