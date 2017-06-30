package com.datacomo.mc.spider.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.adapter.QuuChatAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChatSendBean;
import com.datacomo.mc.spider.android.bean.MyQuuMessageBean;
import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.CreateFile;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.manager.MediaManager;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.GroupChatMessageBean;
import com.datacomo.mc.spider.android.net.been.groupchat.MapGroupChatMessage;
import com.datacomo.mc.spider.android.net.been.groupchat.ObjectInfoBean;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnSizeChangeListener;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class QuuChatActivity extends BasicActionBarActivity implements
		OnSizeChangeListener, OnFaceChosenListner {
	public static QuuChatActivity instance = null;
	private static final String TAG = "QuuChatActivity";
	private RefreshListView listView;
	private Button inputAudio;
	public EditText edit;
	private TextView tv_num;
	private ImageView iv_audio, iv_input, iv_menu, iv_send, iv_face;
	private ArrayList<MyQuuMessageBean> infos;
	private QuuChatAdapter adapter;
	private LoadMsgTask task;
	private LoadUnReadMsgTask task2;
	private FacesView faceView;
	private TableLayout tb;
	private boolean isLoading;
	private String headUrlPath, mberId, mName;
	private Context mContext;
	private RelativeLayout mRlayout_RecordBox;
	private ProgressBar mPro_Loading;
	private LinearLayout mLlayout_DbBox, mLlayout_MarkBox, mLlayout_TrashBox;
	private ImageView mIv_Db;
	ArrayList<ObjectInfoBean> content = new ArrayList<ObjectInfoBean>();

	private int groupId, startMessagesId;
	private String groupName, groupUrl;
	private boolean isManager;
	private int chatId, chatMemberNum;

	private CreateFile cFile;

	private int messageId, position;

	LinearLayout layout;

	private MessageBroadcastReceiver messageReceiver;
	private IntentFilter intentFilter;

	private boolean receiveUnRead = false;
	private String session_key;

	public static GroupChatMessageBean bean;

	private boolean showNums;

	private boolean noShowTip = true;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> newIds = new HashMap<Integer, Integer>();
	private TextView tv_newNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_chat_quu_list);
		instance = this;
		session_key = App.app.share.getSessionKey();
		findViews();
		setView();
		mContext = this;
		messageReceiver = new MessageBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.QUUCHAT_REMOVE);
		intentFilter.addAction(BootBroadcastReceiver.QUUCHAT);
		registerReceiver(messageReceiver, intentFilter); // 注册监听

		ab.setTitle(groupName);
		// new SendRequestThread().start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "21");
		// NotificationService.isChat = true;

		// 避免聊天时手机老响
		startNService(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// NotificationService.isChat = false;

		startNService(false);
	}

	@Override
	protected void onDestroy() {
		dbHandler.removeCallbacksAndMessages(null);
		unregisterReceiver(messageReceiver); // 取消监听
		hideKeyBoardFace();
		SimpleReceiver.sendBoardcast(QuuChatActivity.this, MsgActivity.REFRESH);
		bean = null;
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	private void findViews() {
		Bundle b = getIntent().getExtras();
		String id = b.getString("memberId");
		position = b.getInt("position", -1);
		if (null == id || "".equals(id)) {
			showTip("获取圈子信息失败！");
			finish();
		} else {
			try {
				groupId = Integer.valueOf(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			chatId = groupId;
			groupName = b.getString("name");
			groupUrl = b.getString("url");
			isManager = b.getBoolean("isManager");
			inviteChatDialog(b.getInt("num"));
		}
		L.i(TAG, "findViews id=" + id + ",groupId=" + groupId + ",groupName="
				+ groupName + ",isManager=" + isManager);

		tv_num = (TextView) findViewById(R.id.layout_chat_quu_list_num);
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.showFinishLoadFooter();
		listView.setDivider(null);

		edit = (EditText) findViewById(R.id.edit);
		edit.setOnClickListener(this);

		tv_newNum = (TextView) findViewById(R.id.text_num);

		iv_send = (ImageView) findViewById(R.id.send);
		iv_send.setOnClickListener(this);
		inputAudio = (Button) findViewById(R.id.inputAudio);
		iv_audio = (ImageView) findViewById(R.id.audio);
		iv_menu = (ImageView) findViewById(R.id.menu);
		iv_input = (ImageView) findViewById(R.id.input);
		iv_face = (ImageView) findViewById(R.id.face);
		iv_audio.setOnClickListener(this);
		iv_menu.setOnClickListener(this);
		iv_input.setOnClickListener(this);
		iv_face.setOnClickListener(this);

		layout = (LinearLayout) findViewById(R.id.inputBar);

		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);
		tb = (TableLayout) findViewById(R.id.tblayout);
		mRlayout_RecordBox = (RelativeLayout) findViewById(R.id.layout_chat_quu_list_rlayout_recordbox);
		mPro_Loading = (ProgressBar) findViewById(R.id.layout_chat_quu_list_recordbox_pro);
		mLlayout_DbBox = (LinearLayout) findViewById(R.id.layout_chat_quu_list_recordbox_llayout_dbbox);
		mIv_Db = (ImageView) findViewById(R.id.layout_chat_quu_list_recordbox_dbbox_iv_db);
		mLlayout_MarkBox = (LinearLayout) findViewById(R.id.layout_chat_quu_list_recordbox_llayout_markbox);
		mLlayout_TrashBox = (LinearLayout) findViewById(R.id.layout_chat_quu_list_recordbox_llayout_trashbox);
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				BaseData.hideKeyBoard(QuuChatActivity.this);
				if (scrollState == SCROLL_STATE_IDLE
						&& listView.getFirstVisiblePosition() == 0) {
					if (!isLoading) {
						L.d(TAG, "onScrollStateChanged startMessagesId="
								+ startMessagesId);
						ArrayList<GroupChatMessageBean> objectList = ChatGroupMessageBeanService
								.getService(QuuChatActivity.this).queryChat(
										startMessagesId, groupId + "");
						if (objectList != null && objectList.size() > 0) {
							for (int i = 0; i < objectList.size(); i++) {
								try {
									MyQuuMessageBean myBean = new MyQuuMessageBean(
											groupId, objectList.get(i), false);
									myBean.setSuccess(true);
									infos.add(0, myBean);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							try {
								startMessagesId = infos.get(0).getMsgBean()
										.getMessageId();
							} catch (Exception e) {
								e.printStackTrace();
							}
							L.i(TAG, "onScrollStateChanged startMessagesId="
									+ startMessagesId);
							adapter.notifyDataSetChanged();
						} else {
							loadInfo(false);
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				listView.setFirstItem(firstVisibleItem);
				if ((totalItemCount > 0)
						&& (view.getLastVisiblePosition() == totalItemCount - 1)) {
					if (view.getBottom() == view.getChildAt(
							view.getChildCount() - 1).getBottom()) {
						noShowTip = true;
						newIds.clear();
						tv_newNum.setVisibility(View.GONE);
						return;
					}
				}
				noShowTip = false;
			}
		});
		inputAudio.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getY();
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					((Button) v).setText("松开   结束");
					v.setBackgroundResource(R.drawable.btn_say_down);
					((Button) v).setTextColor(getResources().getColor(
							R.color.white));
					// ((Button) v).setShadowLayer(1, 0, 1, getResources()
					// .getColor(R.color.shadow_black));
					mRlayout_RecordBox.setVisibility(View.VISIBLE);
					mPro_Loading.setVisibility(View.VISIBLE);
					mLlayout_DbBox.setVisibility(View.GONE);
					mLlayout_MarkBox.setVisibility(View.GONE);
					mLlayout_TrashBox.setVisibility(View.GONE);
					MediaManager.init();
					MediaManager.startRecord(mContext, dbHandler);
					break;
				case MotionEvent.ACTION_MOVE:
					if (y < -10 || y > v.getMeasuredHeight()) {
						mLlayout_DbBox.setVisibility(View.GONE);
						mLlayout_TrashBox.setVisibility(View.VISIBLE);
					} else {
						mLlayout_DbBox.setVisibility(View.VISIBLE);
						mLlayout_TrashBox.setVisibility(View.GONE);
					}
					break;
				case MotionEvent.ACTION_UP:
					((Button) v).setText("按住   说话");
					v.setBackgroundResource(R.drawable.btn_say_up);
					((Button) v).setTextColor(getResources().getColor(
							R.color.black));
					// ((Button) v).setShadowLayer(1, 0, 1, getResources()
					// .getColor(R.color.shadow_white));
					if (y >= -10 && y <= v.getMeasuredHeight()) {
						Object[] result = MediaManager.stopRecord();
						if (null == result) {
							dbHandler.sendEmptyMessage(5);
							break;
						}
						if (!(Boolean) result[2]) {
							mRlayout_RecordBox.setVisibility(View.GONE);
							try {
								addAudio((String) result[0],
										(Integer) result[1]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						mRlayout_RecordBox.setVisibility(View.GONE);
						MediaManager.Release();
					}
					break;
				}
				return true;
			}
		});
		listView.setonSizeChangeListener(this);
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String id) {
			Drawable drawable = null;
			try {
				drawable = getResources().getDrawable(Integer.valueOf(id));
				int width = drawable.getIntrinsicWidth();
				int height = drawable.getIntrinsicHeight();
				drawable.setBounds(0, 0, width / 2, height / 2);
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
				// e.printStackTrace();
			}
			return drawable;
		}
	};

	public void refreshNewNum(int messageId) {
		if (newIds.containsKey(messageId)) {
			newIds.remove(messageId);
			if (newIds.size() == 0)
				tv_newNum.setVisibility(View.GONE);
			tv_newNum.setText(newIds.size() + "");
		}
	}

	/**
	 * 当前圈聊人数提醒
	 * 
	 * @param num
	 */
	private void inviteChatDialog(int num) {
		if (num == 0)
			return;
		if (num != chatMemberNum) {
			chatMemberNum = num;
			if (chatMemberNum > 0) {
				// setTitle(groupName + "(" + chatMemberNum + ")",
				// R.drawable.title_fanhui, R.drawable.title_grouptalk);
				ab.setTitle(groupName + "(" + chatMemberNum + ")");
			}
			if (chatMemberNum == 1) {
				new AlertDialog.Builder(QuuChatActivity.this).setTitle("提示")
						.setMessage("当前圈聊成员只有您一人，赶快邀请圈子成员加入圈聊吧！")
						.setPositiveButton("马上邀请", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								inviteChat();
							}
						}).show();
			}
		}
	}

	private MyQuuMessageBean getEmptyMessage(ArrayList<ObjectInfoBean> content) {
		GroupChatMessageBean newBean = new GroupChatMessageBean();
		newBean.setSendMemberUrl("");
		try {
			newBean.setSendMemberId(Integer.valueOf(mberId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		newBean.setSendMemberPath(headUrlPath);
		newBean.setMessageList(content);
		return new MyQuuMessageBean(groupId, newBean, true, false, true);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.send:
			String text = edit.getText().toString();
			if (null != text && !"".equals(text.trim())) {
				iv_send.setEnabled(false);
				ObjectInfoBean info = new ObjectInfoBean();
				info.setMessageType(1);
				info.setObjectType("OBJ_TEXT");
				text = StringUtil.trimInnerSpaceStr(text);
				info.setMessageContent(text);
				ArrayList<ObjectInfoBean> content = new ArrayList<ObjectInfoBean>();
				content.add(info);
				sendInfo(content, null, text);
				edit.setText("");
				iv_send.setEnabled(true);

				MsgActivity.refresh = true;
			} else {
				showTip("内容不能为空");
			}
			break;
		case R.id.menu:
			if (!tb.isShown()) {
				showMenu();
			} else {
				hideKeyBoardFace();
			}
			break;
		case R.id.edit:
			showKeyBoard(v);
			break;
		case R.id.input:
			faceView.setVisibility(View.GONE);
			onInputMenuClick();
			break;
		case R.id.audio:
			faceView.setVisibility(View.GONE);
			onAudioMenuClick();
			break;
		case R.id.face:
			if (faceView.isShown()) {
				showKeyBoard(edit);
			} else {
				edit.requestFocus();
				showFace();
			}
			break;
		case R.id.title:
			Bundle b = new Bundle();
			b.putString("Id", groupId + "");
			LogicUtil.enter(this, HomeGpActivity.class, b, true);
			break;
		case R.id.text_num:
			listView.smoothScrollToPosition(infos.size());
			tv_newNum.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	public void onInputMenuClick() {
		iv_input.setVisibility(View.GONE);
		iv_audio.setVisibility(View.VISIBLE);
		inputAudio.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		showKeyBoard(edit);
	}

	private void onAudioMenuClick() {
		BaseData.hideKeyBoard(this);
		iv_audio.setVisibility(View.GONE);
		iv_input.setVisibility(View.VISIBLE);
		layout.setVisibility(View.GONE);
		inputAudio.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (RESULT_OK != resultCode)
			return;
		switch (requestCode) {
		case CreateFile.IMAGE:// 插入图片
			Uri uri = null;
			String filePath = null;
			try {
				if (data != null) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri0=" + uri);
					if (uri != null) {
						filePath = queryPhoto(uri);
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, cFile.pictureName);
						uri = Uri.fromFile(picture);
						L.i(TAG, "onActivityResult uri1=" + uri);
						filePath = uri.getPath();
					}
				} else {
					File saveFile = new File(ConstantUtil.CAMERA_PATH);
					if (cFile == null) {
						cFile = new CreateFile(QuuChatActivity.this, true);
					}
					File picture = new File(saveFile, cFile.pictureName);
					uri = Uri.fromFile(picture);
					L.i(TAG, "onActivityResult uri2=" + uri);
					filePath = uri.getPath();
				}
				addPhoto(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CreateFile.VIDEO:
		case CreateFile.AUDIO:
			// case CreateFile.TEXT:
		case CreateFile.OTHAR:
			filePath = data.getStringExtra("filePath");
			L.i(TAG, "onActivityResult filepath=" + filePath);
			break;
		case FriendsChooserActivity.RESCODE:
			String[] shareFriendIds = data.getStringArrayExtra("ids");
			int type = data.getIntExtra(BundleKey.TYPE_SENDINFO, 0);
			if (type == 0) {
				String info = data.getStringExtra("sendInfo");
				spdDialog.showProgressDialog("转发中...");
				new RequsetTask(Type.CHAT).execute(shareFriendIds, info);
			} else {
				String objectType = data.getStringExtra("objectType");
				long l = data.getLongExtra("l", 0);
				String uri1 = data.getStringExtra("uri");
				String path = data.getStringExtra("path");
				spdDialog.showProgressDialog("转发中...");
				new RequsetTask(Type.CHAT).execute(shareFriendIds, null,
						objectType, l, uri1, path);
			}
			break;
		case ChatGroupChooseActivity.CHOOSECHATGROUP:
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) data
					.getSerializableExtra(BundleKey.CHOOSEDS);
			String[] shareGroupChatIds = map.keySet().toArray(new String[0]);
			int type1 = data.getIntExtra(BundleKey.TYPE_SENDINFO, 0);
			String memberName = data.getStringExtra(BundleKey.NAME);
			if (type1 == 0) {
				String text = data.getStringExtra(BundleKey.SENDINFO);
				spdDialog.showProgressDialog("转发中...");
				new RequsetTask(Type.CHATGROUP).execute(shareGroupChatIds,
						text, memberName);
			} else {
				String objectType1 = data.getStringExtra(BundleKey.TYPE_OBJECT);
				String path1 = data.getStringExtra(BundleKey.PATH);
				String uri2 = data.getStringExtra(BundleKey.URL);
				long lenght = data.getLongExtra(BundleKey.LENGTH, 0);
				spdDialog.showProgressDialog("转发中...");
				new RequsetTask(Type.CHATGROUP).execute(shareGroupChatIds,
						null, memberName, objectType1, lenght, uri2, path1);
			}
			break;
		case GroupsChooserActivity.RESCODE:
			String[] groupIds = data.getStringArrayExtra("ids");
			new RequsetTask(Type.GROUP).execute(groupIds, null, null);
			break;
		case 41:
			String str = "@" + data.getStringExtra("name") + "：";
			String eText = edit.getText().toString();
			if (!eText.contains(str)) {
				edit.getText().append(str);
				edit.setSelection((eText + str).length());
			} else {
				edit.setSelection(eText.indexOf(str) + str.length());
			}
			new Thread() {
				public void run() {
					try {
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dbHandler.sendEmptyMessage(100);
				};
			}.start();
			break;
		}
	}

	private Handler dbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				L.d(TAG, "start");
				mPro_Loading.setVisibility(View.GONE);
				mLlayout_DbBox.setVisibility(View.VISIBLE);
				break;
			case 2:
				Object[] result = MediaManager.stopRecord();
				if (!(Boolean) result[2]) {
					mRlayout_RecordBox.setVisibility(View.GONE);
					T.show(mContext, "path" + result[0]);
					addAudio((String) result[0], (Integer) result[1]);
				}
				break;
			case 3:
				mIv_Db.setImageResource(msg.arg1);
				break;
			case 4:
				L.d("time", "" + msg.arg1);
				T.show(mContext, "录音时间还有" + msg.arg1 + "秒");
				break;
			case 5:
				mPro_Loading.setVisibility(View.GONE);
				mLlayout_DbBox.setVisibility(View.GONE);
				mLlayout_MarkBox.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							dbHandler.sendEmptyMessage(6);
						}
					}
				}.start();
				break;
			case 6:
				mRlayout_RecordBox.setVisibility(View.GONE);
				break;
			case 100:
				onInputMenuClick();
				break;
			default:
				break;
			}
		}
	};

	private void addPhoto(String photoPath) {
		ObjectInfoBean info = new ObjectInfoBean();
		info.setMessageType(1);
		info.setObjectType("OBJ_PHOTO");
		info.setObjectPath(photoPath);
		info.setObjectUrl("");
		ArrayList<ObjectInfoBean> content = new ArrayList<ObjectInfoBean>();
		content.add(info);
		sendInfo(content, photoPath, null);
	}

	private void addAudio(String audioPath, long time) {
		ObjectInfoBean info = new ObjectInfoBean();
		info.setMessageType(1);
		info.setObjectType("OBJ_VOICE");
		info.setObjectPath(audioPath);
		info.setObjectUrl("");
		info.setObjectLength(time);
		ArrayList<ObjectInfoBean> content = new ArrayList<ObjectInfoBean>();
		content.add(info);
		sendInfo(content, audioPath, null);
	}

	/**
	 * 查找本地图片
	 * 
	 * @param uri
	 * @return
	 */
	private String queryPhoto(Uri uri) {
		String filepath = "";
		Cursor cursor = getContentResolver()
				.query(uri,
						new String[] { "_data", "_display_name", "_size",
								"mime_type" }, null, null, null);
		// Thumbnails.EXTERNAL_CONTENT_URI
		if (cursor != null) {
			cursor.moveToFirst();
			filepath = cursor.getString(0); // 图片文件路径
			cursor.close();
		} else {
			filepath = uri.getPath();
		}
		L.d(TAG, "queryPhoto filepath=" + filepath);
		return filepath;
	}

	private void setView() {
		UserBusinessDatabase business = new UserBusinessDatabase(App.app);
		try {
			headUrlPath = business.getHeadUrlPath(session_key);
			mberId = business.getMemberId(session_key);
			mName = business.getName(session_key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		App.app.share.saveIntMessage("group_chat_unread", groupId + "", 0);

		infos = new ArrayList<MyQuuMessageBean>();
		queryFailMsg(true);

		adapter = new QuuChatAdapter(this, infos, listView, mberId, groupId
				+ "");
		listView.setAdapter(adapter);
		listView.removeFooterView();
		initTable();

		tv_newNum.setVisibility(View.GONE);
		tv_newNum.setOnClickListener(this);

		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if ("".equals(s.toString())) {
					iv_menu.setVisibility(View.VISIBLE);
					iv_send.setVisibility(View.GONE);
				} else {
					iv_menu.setVisibility(View.GONE);
					iv_send.setVisibility(View.VISIBLE);
				}
			}
		});

		edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showKeyBoard(v);
				}
			}
		});

		String eStr = App.app.share.getStringMessage("QuuChat", groupId + "",
				"");
		if (eStr != null && eStr.contains("#")) {
			try {
				eStr = eStr.substring(eStr.indexOf("#") + 1);
				SpannableStringBuilder ssb = new SpannableStringBuilder(eStr);
				for (int i = 0; i < FaceUtil.FACE_TEXTS.length; i++) {
					String face = FaceUtil.FACE_TEXTS[i];
					if (eStr.contains(face)) {
						for (int j = 0; j <= eStr.length() - face.length(); j++) {
							if (eStr.substring(j, j + face.length()).equals(
									face)) {
								Drawable drawable = getResources().getDrawable(
										FaceUtil.FACE_RES_IDS[i]);
								int width = drawable.getIntrinsicWidth();
								int height = drawable.getIntrinsicHeight();
								drawable.setBounds(0, 0, width / 2, height / 2);
								ImageSpan span = new ImageSpan(drawable,
										ImageSpan.ALIGN_BASELINE);
								ssb.setSpan(span, j, j + face.length(),
										Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
							}
						}
					}
				}
				edit.setText(ssb);
				edit.requestFocusFromTouch();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void queryFailMsg(boolean showLoc) {
		ArrayList<ChatSendBean> list = null;
		try {
			list = QChatSendService.getService(this).queryById(session_key,
					groupId + "", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list != null) {
			for (ChatSendBean chatSendBean : list) {
				ObjectInfoBean info = new ObjectInfoBean();
				String oType = chatSendBean.getmType();
				info.setObjectType(oType);
				if ("OBJ_VOICE".equals(oType) || "OBJ_PHOTO".equals(oType)) {
					info.setObjectPath(chatSendBean.getContent());
					info.setObjectUrl("");
				} else if ("OBJ_TEXT".equals(oType)) {
					info.setMessageContent(chatSendBean.getContent());
				}
				info.setMessageType(1);
				info.setObjectLength(chatSendBean.gettLong());
				ArrayList<ObjectInfoBean> content = new ArrayList<ObjectInfoBean>();
				content.add(info);
				MyQuuMessageBean bean = getEmptyMessage(content);
				bean.setC(this);
				bean.setTime(chatSendBean.getTime());
				GroupChatMessageBean newBean = bean.getMsgBean();
				newBean.setCreateTime("" + chatSendBean.getTime());
				bean.setMsgBean(newBean);
				if ("OBJ_VOICE".equals(oType)) {
					bean.setFilePath(chatSendBean.getContent());
				} else if ("OBJ_PHOTO".equals(oType)) {
					bean.setFilePath(chatSendBean.getContent());
				} else if ("OBJ_TEXT".equals(oType)) {

				}
				bean.setGroupId(groupId);
				bean.setSendState(false);
				bean.setSuccess(false);
				bean.setSendable(false);
				infos.add(bean);
			}
		}

		if (showLoc) {
			ArrayList<GroupChatMessageBean> objectList = ChatGroupMessageBeanService
					.getService(this).queryChat(0, groupId + "");
			if (objectList != null)
				for (int i = 0; i < objectList.size(); i++) {
					try {
						MyQuuMessageBean myBean = new MyQuuMessageBean(groupId,
								objectList.get(i), false);
						myBean.setSuccess(true);
						infos.add(0, myBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			try {
				startMessagesId = infos.get(0).getMsgBean().getMessageId();
			} catch (Exception e) {
				e.printStackTrace();
			}
			L.d(TAG, "queryFailMsg startMessagesId=" + startMessagesId);
		}
	}

	View.OnClickListener tbListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null != v.getTag()) {
				String tag = (String) v.getTag();
				if (tag.equals("pic")) {
					if (cFile == null) {
						cFile = new CreateFile(QuuChatActivity.this, true);
					}
					cFile.getLocalImg();
				} else if (tag.equals("photo")) {
					if (cFile == null) {
						cFile = new CreateFile(QuuChatActivity.this, true);
					}
					cFile.takePhoto();
					// } else if (tag.equals("face")) {
					// showFace();
					// } else if (tag.equals("video")) {

				}
			}

		}
	};

	private void initTable() {
		@SuppressWarnings("deprecation")
		int w = getWindowManager().getDefaultDisplay().getWidth() / (4 + 2);
		LayoutParams lp = new TableRow.LayoutParams(0, w);
		lp.weight = 1;
		TableRow tr1 = (TableRow) tb.findViewById(R.id.row1);
		tr1.setWeightSum(4);
		tr1.addView(getTbItem("pic", R.drawable.btn_photo_bg, tbListener), lp);
		tr1.addView(getTbItem("photo", R.drawable.btn_video_bg, tbListener), lp);
		// tr1.addView(getTbItem("face", R.drawable.btn_face_bg, tbListener),
		// lp);
	}

	private View getTbItem(String tag, int drawable,
			View.OnClickListener listener) {
		ImageView ib = new ImageView(this);
		ib.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		ib.setScaleType(ScaleType.CENTER_INSIDE);
		ib.setImageResource(drawable);
		ib.setTag(tag);
		ib.setOnClickListener(listener);
		return ib;
	}

	public void loadInfo(boolean isRefresh) {
		if (!isLoading) {
			stopTask();
			task = new LoadMsgTask(isRefresh);
			task.execute();
		}
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	private void sendInfo(ArrayList<ObjectInfoBean> data, String filePath,
			String contentStr) {
		ChatSendBean cBean = new ChatSendBean();
		long time = System.currentTimeMillis();
		cBean.setTime(time);
		cBean.setSession_key(session_key);
		try {
			cBean.setId(Integer.valueOf(groupId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cBean.setName(groupName);
		cBean.setHead(groupUrl);
		cBean.setContent(contentStr);
		cBean.setcType(1);
		ObjectInfoBean info = data.get(0);
		String oType = info.getObjectType();
		cBean.setmType(oType);
		if ("OBJ_TEXT".equals(oType)) {
			cBean.setContent(info.getMessageContent());
		} else {
			cBean.setContent(filePath);
		}
		cBean.settLong(info.getObjectLength());
		L.i(TAG, "sendInfo time=" + time);
		QChatSendService.getService(this).save(cBean);

		MyQuuMessageBean bean = getEmptyMessage(data);
		bean.setC(this);
		bean.setTime(time);
		bean.setGroupId(groupId);
		bean.setSuccess(false);
		bean.setFilePath(filePath);
		bean.setName(mName, groupName);
		infos.add(bean);
		adapter.notifyDataSetChanged();
		listView.setSelection(listView.getAdapter().getCount());
		content.clear();
	}

	public class MessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			L.d(TAG, "MessageBroadcastReceiver action=" + action);
			// TODO
			if (BootBroadcastReceiver.QUUCHAT_NUMBER.equals(action)
					&& receiveUnRead) {
				getNewInfo(intent.getIntExtra("LETTER_NUM", 0));
			}
			// if (BootBroadcastReceiver.QUUCHAT.equals(action)) {
			// int gid = intent.getIntExtra("gId", 0);
			// if (gid != groupId)
			// return;
			//
			// App.app.share.saveIntMessage("group_chat_unread", gid + "", 0);
			//
			// L.d(TAG,
			// "MessageBroadcastReceiver gid="
			// + gid
			// + ",group_chat_unread="
			// + App.app.share.getAllMessage(
			// "group_chat_unread", 0));
			//
			// GroupChatMessageBean bean = (GroupChatMessageBean) intent
			// .getSerializableExtra("Chat");
			// MyQuuMessageBean myBean = new MyQuuMessageBean(groupId, bean,
			// false);
			// myBean.setSuccess(true);
			// infos.add(myBean);
			// messageId = bean.getMessageId();
			// newIds.put(messageId, messageId);
			// adapter.notifyDataSetChanged();
			//
			// L.i(TAG,
			// "MessageBroadcastReceiver SendMemberName="
			// + bean.getSendMemberName() + ",noShowTip="
			// + noShowTip);
			// if (noShowTip) {
			// listView.smoothScrollToPosition(infos.size());
			// } else {
			// tv_newNum.setText(newIds.size() + "");
			// tv_newNum.setVisibility(View.VISIBLE);
			// }
			// } else if (BootBroadcastReceiver.QUUCHAT_REMOVE.equals(action)) {
			// int gid = intent.getIntExtra("gId", 0);
			// L.i(TAG, "MessageBroadcastReceiver gid=" + gid);
			// if (gid == groupId) {
			// Builder builder = new AlertDialog.Builder(
			// QuuChatActivity.this).setTitle("提示")
			// .setMessage("管理员把您请出了圈聊")
			// .setPositiveButton("知道了", new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// if (position != -1) {
			// Intent ii = new Intent();
			// ii.putExtra("position", position);
			// QuuChatActivity.this.setResult(
			// RESULT_OK, ii);
			// }
			// QuuChatActivity.this.finish();
			// }
			// });
			// AlertDialog ad = builder.create();
			// ad.setCancelable(false);
			// ad.show();
			// }
			// }
		}
	}

	private void getNewInfo(int LETTER_NUM) {
		if (!isLoading) {
			stopNewTask();
			task2 = new LoadUnReadMsgTask(LETTER_NUM);
			task2.execute();
		}
	}

	private void stopNewTask() {
		if (null != task2 && task2.getStatus() == AsyncTask.Status.RUNNING) {
			task2.cancel(true);
		}
	}

	class LoadMsgTask extends AsyncTask<String, Integer, MCResult> {

		private boolean isRefresh = false;

		public LoadMsgTask(boolean isRefresh) {
			this.isRefresh = isRefresh;
			setLoadingState(true);
		}

		@Override
		protected MCResult doInBackground(String... params) {
			isLoading = true;
			MCResult result = null;
			try {
				messageId = 0;
				int newOrOld = 1;
				if (!isRefresh && infos.size() > 0)
					messageId = infos.get(0).getMsgBean().getMessageId();
				if (messageId == 0)
					newOrOld = 2;

				L.i(TAG, "LoadMsgTask chatId=" + chatId + ",id=" + messageId);
				result = APIGroupChatRequestServers.getGroupChatMessage(
						QuuChatActivity.this, chatId, messageId, newOrOld, 20,
						true);
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
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else if (1 == mcResult.getResultCode()) {
					MapGroupChatMessage result = (MapGroupChatMessage) mcResult
							.getResult();
					if (null != result) {
						// inviteChatDialog(result.getLEAGUER_NUM());
						int COUNT = result.getCOUNT();
						if (!showNums && COUNT > 0) {
							showNums = true;
							tv_num.setText("当前共有" + COUNT + "条对话");
							Animation show = AnimationUtils.loadAnimation(
									QuuChatActivity.this, R.anim.p_enter_up);
							tv_num.setVisibility(View.VISIBLE);
							tv_num.startAnimation(show);

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									Animation sa = AnimationUtils
											.loadAnimation(
													QuuChatActivity.this,
													R.anim.p_leave_up);
									tv_num.setVisibility(View.GONE);
									tv_num.startAnimation(sa);
								}
							}, 5 * 1000);
						}
						ArrayList<GroupChatMessageBean> requestInfo = (ArrayList<GroupChatMessageBean>) result
								.getLIST();

						if (null != requestInfo) {
							int rSize = requestInfo.size();
							if (rSize > 0) {
								if (isRefresh) {
									// TODO
									// App.app.share.saveIntMessage(
									// "group_chat_unread", groupId + "",
									// 0);
									infos.clear();
									queryFailMsg(false);
								}
								boolean flag = true;
								int oldSize = infos.size();
								for (int i = 0; i < rSize; i++) {
									GroupChatMessageBean bean = requestInfo
											.get(i);
									MyQuuMessageBean myBean = new MyQuuMessageBean(
											groupId, bean, false);
									myBean.setSuccess(true);
									infos.add(0, myBean);

									try {
										if (oldSize == 0
												&& flag
												&& bean.getSendMemberId() != Integer
														.valueOf(mberId)) {
											messageId = bean.getMessageId();
											flag = false;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								adapter.notifyDataSetChanged();
								L.d(TAG, "LoadMsgTask size=" + infos.size()
										+ ",rSize=" + rSize);
								if (infos.size() > rSize) {
									listView.setSelection(rSize + 1);
								} else {
									listView.setSelection(rSize);
								}
							} else {
								showTip("没有更多数据！");
							}
						}
					}
				}
			}
			isLoading = false;
			receiveUnRead = true;
		}
	}

	class LoadUnReadMsgTask extends AsyncTask<String, Integer, MCResult> {

		private int LETTER_NUM;

		public LoadUnReadMsgTask(int LETTER_NUM) {
			this.LETTER_NUM = LETTER_NUM;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				L.i(TAG, "LoadUnReadMsgTask groupId=" + groupId + ",messageId="
						+ messageId + ",LETTER_NUM=" + LETTER_NUM);
				mcResult = APIGroupChatRequestServers.otherMemberNewMessage(
						QuuChatActivity.this, groupId, messageId, 0,
						LETTER_NUM, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (mcResult != null && mcResult.getResultCode() == 1) {
				MapGroupChatMessage result = (MapGroupChatMessage) mcResult
						.getResult();
				if (null != result) {
					// inviteChatDialog(result.getLEAGUER_NUM());
					ArrayList<GroupChatMessageBean> requestInfo = (ArrayList<GroupChatMessageBean>) result
							.getLIST();
					if (null != requestInfo) {
						int rSize = requestInfo.size();
						if (rSize > 0) {
							for (int i = rSize; i > 0; i--) {
								GroupChatMessageBean bean = requestInfo
										.get(i - 1);
								MyQuuMessageBean myBean = new MyQuuMessageBean(
										groupId, bean, false);
								myBean.setSuccess(true);
								infos.add(myBean);
								messageId = bean.getMessageId();

								newIds.put(messageId, messageId);
							}
							adapter.notifyDataSetChanged();

							if (noShowTip) {
								listView.smoothScrollToPosition(infos.size());
							} else {
								tv_newNum.setText(newIds.size() + "");
								tv_newNum.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			}
		}
	}

	class RequsetTask extends AsyncTask<Object, Integer, MCResult> {
		private Type mType;

		public RequsetTask(Type type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult result = null;
			switch (mType) {
			case CHAT:
				if ((String) params[1] != null
						&& !"".equals((String) params[1])) {
					try {
						result = APIRequestServers.sendMessage(App.app,
								(String[]) params[0], null, null, null,
								(String) params[1], "true", "OBJ_TEXT", null,
								null, null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						result = APIRequestServers.sendMessage(App.app,
								(String[]) params[0], null, null, null,
								(String) params[1], "false",
								(String) params[2],
								String.valueOf((Long) params[3]), null,
								(String) params[4], (String) params[5]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case CHATGROUP:
				try {
					if (null != (String) params[1]
							&& !"".equals((String) params[1])) {
						result = APIGroupChatRequestServers.sendGroupMessage(
								App.app, (String[]) params[0], true,
								(String) params[1], null, (String) params[2],
								null, "OBJ_TEXT", null, null, null, null);
					} else {
						result = APIGroupChatRequestServers.sendGroupMessage(
								App.app, (String[]) params[0], false,
								(String) params[1], null, (String) params[2],
								null, (String) params[3], null,
								String.valueOf((Long) params[4]),
								(String) params[5], (String) params[6]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GROUP:
				ObjectInfoBean mrInfo = bean.getMessageList().get(0);
				String objectType = mrInfo.getObjectType();
				String info = "";
				String createTime = bean.getCreateTime();
				String date = "";
				if (null == createTime) {
					date = DateTimeUtil.cTimeFormat(System.currentTimeMillis());
				} else {
					date = DateTimeUtil.cTimeFormat(DateTimeUtil
							.getLongTime(bean.getCreateTime()));
				}
				String name = "";
				if (GetDbInfoUtil.getMemberId(App.app) == bean
						.getSendMemberId()) {
					name = "我";
				} else {
					name = bean.getSendMemberName();
				}
				if (objectType == null || "OBJ_TEXT".equals(objectType)) {
					info = "【转】" + name + "：" + mrInfo.getMessageContent()
							+ "【" + date + "】";
					try {
						result = APIRequestServers.createGroupTopic(App.app,
								(String[]) params[0], null, null, null, null,
								info, null, null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						JSONObject json = new JSONObject();
						json.put("fileName", mrInfo.getObjectName());
						json.put("fileUrl", mrInfo.getObjectUrl());
						json.put("filePath", mrInfo.getObjectPath());
						json.put("fileSize", mrInfo.getObjectSize());
						info = json.toString();

						if ("OBJ_PHOTO".equals(objectType)) {
							result = APIRequestServers.createGroupTopic(
									App.app, (String[]) params[0], null, null,
									null, null, "【转】" + name + "：分享图片【" + date
											+ "】", null, null,
									new String[] { info });
						} else {
							result = APIRequestServers.createGroupTopic(
									App.app, (String[]) params[0], null, null,
									null, null, "【转】" + name + "：分享文件【" + date
											+ "】", null, new String[] { info },
									null);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				L.i(TAG, "RequsetTask objectType=" + objectType + ",info="
						+ info);
				break;
			default:
				break;
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				showTip(T.ErrStr);
				return;
			}
			showTip("已转发！");
			if (mType == Type.GROUP)
				InfoWallActivity.isNeedRefresh = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_quuchat_members).setVisible(true);
		MenuItem mi = menu.findItem(R.id.action_refresh);
		mi.setVisible(true);
		mi.setIcon(R.drawable.nothing);
		this.menu = menu;
		loadInfo(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			App.app.share.saveStringMessage("QuuChat", groupId + "",
					System.currentTimeMillis() + "#"
							+ edit.getText().toString());
			setResult(RESULT_OK, new Intent());
			finish();
			return true;
		case R.id.action_quuchat_members:
			inviteChat();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void inviteChat() {
		Bundle b = new Bundle();
		b.putInt("chatId", chatId);
		b.putInt("groupId", groupId);
		b.putBoolean("isManager", isManager);
		LogicUtil.enter(this, GroupChatActivity.class, b, 41);
	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		listView.setSelection(listView.getAdapter().getCount() - 1);
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (faceView.isShown() || tb.isShown()) {
				hideKeyBoardFace();
				return true;
			} else {
				App.app.share.saveStringMessage("QuuChat", groupId + "",
						System.currentTimeMillis() + "#"
								+ edit.getText().toString());
				setResult(RESULT_OK, new Intent());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MediaManager.stopAudio();
		adapter.resetWaveView();
	}

	/**
	 * 无输入
	 */
	private void hideKeyBoardFace() {
		BaseData.hideKeyBoard(this);
		faceView.setVisibility(View.GONE);
		tb.setVisibility(View.GONE);
	}

	/**
	 * 表情输入
	 */
	private void showFace() {
		BaseData.hideKeyBoard(this);
		iv_audio.setVisibility(View.VISIBLE);
		iv_input.setVisibility(View.GONE);
		tb.setVisibility(View.GONE);
		faceView.setVisibility(View.VISIBLE);
		inputAudio.setVisibility(View.GONE);
		iv_face.setImageResource(R.drawable.keyboardbtn);
		edit.requestFocus();
		findViewById(R.id.inputBar).setVisibility(View.VISIBLE);
		if (faceView.getChildCount() == 0) {
			faceView.setFaces();
		}
	}

	/**
	 * 插入菜单栏
	 */
	private void showMenu() {
		BaseData.hideKeyBoard(this);
		faceView.setVisibility(View.GONE);
		tb.setVisibility(View.VISIBLE);
	}

	/**
	 * 键盘输入
	 * 
	 * @param view
	 */
	private void showKeyBoard(View view) {
		faceView.setVisibility(View.GONE);
		iv_face.setImageResource(R.drawable.icon_face);
		tb.setVisibility(View.GONE);
		edit.requestFocus();
		BaseData.showKeyBoard(this, view);
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, edit, text, resId);
	}
}
