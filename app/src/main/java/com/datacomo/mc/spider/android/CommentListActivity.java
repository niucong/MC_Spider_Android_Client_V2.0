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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.CommentAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerPermissionBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceCommentBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.KeyboardListenRelativeLayout;
import com.datacomo.mc.spider.android.view.KeyboardListenRelativeLayout.OnKeyboardStateChangedListener;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;

public class CommentListActivity extends BasicActionBarActivity implements
		HandlerNumberUtil, OnFaceChosenListner, OnLoadMoreListener {
	// 声明静态TAG
	// private static final String TAG = "CommentListActivity";
	private static final String LOG_COMMENT = "CommentListActivity_Comment";
	// 声明变量
	private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_CIRCLEBLOGDETAILSACTIVITY;
	private String type_Object;
	private String info_comment;
	private String name_Reply;

	private String id_Group, id_Photo;
	private int num_Total;

	private boolean hasAuthority = true;
	private boolean canReleaseComment = true;
	private boolean keyIsShow;
	boolean mIsMore;

	private Context context;
	private HashMap<String, String> receiveReplyIds;

	// 声明引用类
	private CommentAdapter adapter_Comment;
	private CommentRequsetTask task;

	// 组件
	private RefreshListView lv_Comment;
	private LinearLayout lLayout_ReleaseCommentBox;
	private LinearLayout llayout_Foot;
	private FacesView faceView;
	private EditText edit_CommentInfo;
	private ImageView btn_Release;
	private ImageView img_Face;
	private KeyboardListenRelativeLayout rlayout_FatherBox; // 父容器

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
		setContent(R.layout.layout_commentlist);
		context = this;
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 **/
	private void initView() {
		lv_Comment = (RefreshListView) findViewById(R.id.layout_commentlist_lv_commentlist);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		llayout_Foot = (LinearLayout) inflater
				.inflate(R.layout.form_foot, null);
		((TextView) llayout_Foot.findViewById(R.id.form_foot_txt))
				.setText("没有更多评论");
		lv_Comment.addFooter(llayout_Foot, true);
		lLayout_ReleaseCommentBox = (LinearLayout) findViewById(R.id.llayout_commentbox);
		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);
		edit_CommentInfo = (EditText) findViewById(R.id.comment_edit);
		btn_Release = (ImageView) findViewById(R.id.comment_release);
		img_Face = (ImageView) findViewById(R.id.comment_face);
		rlayout_FatherBox = (KeyboardListenRelativeLayout) findViewById(R.id.rlayout_keyboardlisten);
	}

	private ArrayList<ResourceCommentBean> comments;

	/**
	 * 初始化数据
	 **/
	private void initData() {
		Bundle bundle = getIntentMsg();
		type_Object = bundle.getString("type_Object");
		id_Group = bundle.getString("groupId");
		id_Photo = bundle.getString("photoId");
		num_Total = bundle.getInt("num_Total");
		int id_Default_Member = GetDbInfoUtil.getMemberId(this);
		// setTitle("评论" + "（" + num_Total + "条" + "）", R.drawable.title_fanhui,
		// null);
		ab.setTitle("评论" + "（" + num_Total + "条" + "）");

		comments = new ArrayList<ResourceCommentBean>();
		adapter_Comment = new CommentAdapter(comments, context, handler,
				id_Default_Member, new int[] { 7, 3 }, id_Group, id_Photo,
				type_Object);
		lv_Comment.setAdapter(adapter_Comment);
		mIsMore = true;
		loadInfo(false, -1);
		bindListener();
		new Thread() {
			public void run() {
				try {
					MCResult mcResult = APIRequestServers
							.leaguerPermissionInfo(App.app, id_Group + "");
					if (mcResult != null && 1 == mcResult.getResultCode()) {
						GroupLeaguerPermissionBean bean = (GroupLeaguerPermissionBean) mcResult
								.getResult();
						String leaguerStatus = bean.getLeaguerStatus();
						if ("6".equals(leaguerStatus)
								|| "NO_RELATION".equals(leaguerStatus)
								|| "COOPERATION_LEAGUER".equals(leaguerStatus))
							hasAuthority = false;
						if ("1".equals(leaguerStatus)
								|| "2".equals(leaguerStatus)
								|| "GROUP_OWNER".equals(leaguerStatus)
								|| "GROUP_MANAGER".equals(leaguerStatus))
							adapter_Comment.setHasDelete(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 绑定监听事件
	 **/
	private void bindListener() {
		lv_Comment.setonLoadMoreListener(this);
		lv_Comment.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadInfo(true, -1);
			}
		});
		rlayout_FatherBox
				.setOnKeyboardStateChangedListener(new OnKeyboardStateChangedListener() {

					@Override
					public void onKeyboardStateChanged(int state) {
						switch (state) {
						case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE:// 软键盘隐藏
							keyIsShow = false;
							break;
						case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW:// 软键盘显示
							keyIsShow = true;
							break;
						default:
							break;
						}
						// L.d(TAG, "keyIsShow" + keyIsShow);

					}
				});
		edit_CommentInfo.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					showKeyBoard(v);
				}
			}
		});
		img_Face.setOnClickListener(this);
		btn_Release.setOnClickListener(this);
		edit_CommentInfo.setOnClickListener(this);
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
	 * 
	 * @param isRefresh
	 * @param location
	 *            the index at which to insert when location is zero
	 */
	private void loadInfo(boolean isRefresh, int location) {
		if (!isRefresh && location != 0 && !mIsMore)
			return;
		stopTask();
		if (isRefresh) {
			lv_Comment.refreshUI();
			lv_Comment.hideTempFooter();
		}
		int maxResults = 1;
		if (location != 0)
			maxResults = SIZE_PAGE;
		int startRecord = 0;
		if (location != 0 && !isRefresh)
			startRecord = adapter_Comment.getCount();
		L.d(LOG_COMMENT, "isRefresh:" + isRefresh + " location:" + location
				+ " startRecord:" + startRecord + " maxResults:" + maxResults);
		task = new CommentRequsetTask(isRefresh, location);
		task.execute(String.valueOf(id_Group), String.valueOf(id_Photo),
				type_Object, String.valueOf(startRecord),
				String.valueOf(maxResults));
		L.d(LOG_COMMENT, "!(location == 0 && adapter_Comment.getCount() > 0):"
				+ !(location == 0 && adapter_Comment.getCount() > 0));
		if ((location != 0 && !isRefresh)
				|| (location == 0 && adapter_Comment.getCount() == 0))// 除去刷新和当已有评论是更新发布的评论
			lv_Comment.showLoadFooter();
	}

	@SuppressLint("NewApi")
	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
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
				showToast((String) msg.obj);
				break;
			case HANDLER_ONE: // 刷新适配器
				adapter_Comment.notifyDataSetChanged();
				break;
			case HANDLER_THREE:
				num_Total--;
				// setTitleText("评论" + "（" + num_Total + "条" + "）");
				ab.setTitle("评论" + "（" + num_Total + "条" + "）");
				if (num_Total == 0)
					lv_Comment.removeFooterView();
				break;
			case HANDLER_FOUR:
				T.show(App.app, "已回复！");
				lLayout_ReleaseCommentBox.setVisibility(View.GONE);
				edit_CommentInfo.setText("");
				showToast((String) msg.obj);
				loadInfo(false, 0);
				break;
			case HANDLER_FIVE:
				break;
			case HANDLER_SEVEN: // 处理回复评论
				String[] info = ((String[]) msg.obj);
				name_Reply = "@" + info[0] + "：";
				if (receiveReplyIds == null) {
					receiveReplyIds = new HashMap<String, String>();
				}
				if (!"0".equals(info[1])
						&& !receiveReplyIds.containsValue(info[1])) {
					receiveReplyIds.put(info[1], name_Reply);
				}
				if (lLayout_ReleaseCommentBox.getVisibility() == View.GONE)
					lLayout_ReleaseCommentBox.setVisibility(View.VISIBLE);

				edit_CommentInfo.requestFocus();
				showKeyBoard(edit_CommentInfo);
				String eText = edit_CommentInfo.getText().toString();
				if (!eText.contains(name_Reply)) {
					edit_CommentInfo.getText().append(name_Reply);
					edit_CommentInfo
							.setSelection((eText + name_Reply).length());
				} else {
					edit_CommentInfo.setSelection(eText.indexOf(name_Reply)
							+ name_Reply.length());
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 获得handler
	 * 
	 * @return
	 */
	private Handler getHandler() {
		return handler;
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

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 *            需要显示的信息
	 **/
	private void showToast(String text) {
		T.show(this, text);
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }

	/**
	 * 评论资源
	 * 
	 * @param info
	 *            final String类型 评论内容
	 **/
	public void releaseComment(final String strComment) {
		if (!hasAuthority) {
			showToast("您不是该圈子成员，请先加入圈子！");
			return;
		}
		if (!canReleaseComment) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				try {
					canReleaseComment = false;
					MCResult mcResult = null;
					String[] receiveIds = null;
					if (null != receiveReplyIds)
						receiveIds = receiveReplyIds.keySet().toArray(
								new String[] {});
					if (null != receiveIds && receiveIds.length > 0) {
						for (String id : receiveIds) {
							if (!strComment.contains(receiveReplyIds.get(id))) {
								receiveReplyIds.remove(id);
							}
						}
						receiveIds = receiveReplyIds.keySet().toArray(
								new String[] {});
					}
					if (null != receiveIds && receiveIds.length > 0) {
						mcResult = APIRequestServers.commentResource(App.app,
								String.valueOf(id_Group),
								String.valueOf(id_Photo), type_Object,
								strComment, receiveIds);
						receiveReplyIds.clear();
					} else {
						mcResult = APIRequestServers.commentResource(App.app,
								String.valueOf(id_Group),
								String.valueOf(id_Photo), type_Object,
								strComment, null);
					}
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						InfoWallActivity.isNeedRefresh = true;
						sendHandlerStrMsg(HANDLER_FOUR,
								R.string.comment_success);
					} else {
						sendHandlerErrStrMsg();
						info_comment = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendHandlerErrStrMsg();
					info_comment = "";
				}
				canReleaseComment = true;
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.comment_edit:
			showKeyBoard(v);
			break;
		case R.id.comment_release:
			hideKeyBoardFace();
			// info_comment = edit_CommentInfo.getText().toString();
			String comment_str = edit_CommentInfo.getText().toString();
			if (comment_str != null && !"".equals(comment_str)) {
				if (comment_str.startsWith(name_Reply)) {
					if (comment_str.length() > name_Reply.length()) {
						name_Reply = null;
						hideKey();
						if (!comment_str.equals(info_comment)) {
							info_comment = comment_str;
							releaseComment(info_comment);
						}
					} else {
						showKeyBoard(edit_CommentInfo);
						showTip("回复内容不能为空");
					}
				} else {
					hideKey();
					if (!comment_str.equals(info_comment)) {
						info_comment = comment_str;
						releaseComment(info_comment);
					}
				}
			} else {
				showKeyBoard(edit_CommentInfo);
				showTip("评论内容不能为空");
			}
			break;
		case R.id.comment_face:
			if (faceView.isShown()) {
				showKeyBoard(edit_CommentInfo);
			} else {
				edit_CommentInfo.requestFocus();
				showFace();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (faceView.isShown()) {
				hideKeyBoardFace();
				return true;
			}
			if (lLayout_ReleaseCommentBox.getVisibility() == View.VISIBLE) {
				lLayout_ReleaseCommentBox.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 表情输入
	 */
	private void showFace() {
		if (keyIsShow) {
			BaseData.hideKeyBoard(this);
		}
		img_Face.setImageResource(R.drawable.keyboardbtn);
		faceView.setVisibility(View.VISIBLE);
		if (faceView.getChildCount() == 0) {
			faceView.setFaces();
		}
	}

	/**
	 * 键盘输入
	 * 
	 * @param view
	 */
	private void showKeyBoard(View view) {
		// keyIsShow = true;
		faceView.setVisibility(View.GONE);
		img_Face.setImageResource(R.drawable.icon_face);
		BaseData.showKeyBoard(this, view);
	}

	/**
	 * 无输入
	 * 
	 */
	private void hideKeyBoardFace() {
		faceView.setVisibility(View.GONE);
		img_Face.setImageResource(R.drawable.icon_face);
		BaseData.hideKeyBoard(this);
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKey() {
		if (keyIsShow) {
			BaseData.hideKeyBoard(this);
		} else if (faceView.isShown()) {
			faceView.setVisibility(View.GONE);
		}
		img_Face.setImageResource(R.drawable.icon_face);
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerErrStrMsg() {
		Message msg = Message.obtain();
		msg.what = HANDLER_ZERO;
		msg.obj = T.ErrStr;
		this.getHandler().sendMessage(msg);
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerStrMsg(int id, int msgid) {
		Message msg = Message.obtain();
		msg.what = id;
		msg.obj = getResourcesString(msgid);
		this.getHandler().sendMessage(msg);
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, edit_CommentInfo, text, resId);
	}

	class CommentRequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;
		private boolean mIsRefresh;
		private int mLocation;

		/**
		 * 
		 * @param context
		 * @param isRefresh
		 * @param location
		 *            the index at which to insert when location is zero
		 */
		public CommentRequsetTask(boolean isRefresh, int location) {
			mIsRefresh = isRefresh;
			mLocation = location;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			L.d(LOG_COMMENT, "doInBackground");
			mParams = params;
			MCResult result = null;
			try {
				result = APIRequestServers.commentRecords(App.app,
						(String) mParams[0], (String) mParams[1],
						(String) mParams[2], "false", (String) mParams[3],
						(String) mParams[4]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			L.d(LOG_COMMENT, "onPostExecute");
			lv_Comment.hideLoadFooter();
			if (null == result || result.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			L.d(LOG_COMMENT,
					"onPostExecute result.getResultCode():"
							+ result.getResultCode());
			MapResourceCommentBean bean_MapResourceComment = (MapResourceCommentBean) result
					.getResult();
			List<ResourceCommentBean> beans_Temp = bean_MapResourceComment
					.getLIST();
			if (null == beans_Temp || beans_Temp.size() == 0) {
				mIsMore = false;
				if (mIsRefresh) {
					lv_Comment.onRefreshComplete(false);
					return;
				}
				if (adapter_Comment.getCount() == 0) {
					return;
				}
				lv_Comment.showTempFooter();
				return;
			}
			L.d(LOG_COMMENT,
					"onPostExecute beans_Temp.size()" + beans_Temp.size());
			comments.addAll(beans_Temp);
			adapter_Comment.notifyDataSetChanged();
			// adapter_Comment.add(beans_Temp, mLocation, mIsRefresh);
			if (mIsRefresh || mLocation == 0)
				lv_Comment.setSelection(0);
			if (mLocation == 0) {
				num_Total++;
				// setTitleText("评论" + "（" + num_Total + "条" + "）");
				ab.setTitle("评论" + "（" + num_Total + "条" + "）");
			}
			if (mLocation != 0)
				mIsMore = beans_Temp.size() >= SIZE_PAGE;
			beans_Temp.clear();
			L.d(LOG_COMMENT, "mIsMore:" + mIsMore);
			if (!mIsMore) {
				lv_Comment.showTempFooter();
			}
			if (mIsRefresh) {
				num_Total = bean_MapResourceComment.getTOTALNUM();
				lv_Comment.onRefreshComplete(false);
			}
		}

	}

	@Override
	public void onLoadMore() {
		loadInfo(false, -1);
	}
}
