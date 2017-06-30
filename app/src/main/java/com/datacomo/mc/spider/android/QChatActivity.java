package com.datacomo.mc.spider.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
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
import com.datacomo.mc.spider.android.adapter.QChatAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChatSendBean;
import com.datacomo.mc.spider.android.bean.MyQMessageBean;
import com.datacomo.mc.spider.android.db.ChatMessageBeanService;
import com.datacomo.mc.spider.android.db.QChatSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.CreateFile;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.manager.MediaManager;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MessageBean;
import com.datacomo.mc.spider.android.net.been.MessageResourceInfo;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnSizeChangeListener;
import com.umeng.analytics.MobclickAgent;

public class QChatActivity extends BasicActionBarActivity implements
		OnSizeChangeListener, OnFaceChosenListner {
	public static QChatActivity instance = null;
	private static final String TAG = "QChatActivity";

	private RefreshListView listView;
	private Button inputAudio;
	private EditText edit;
	private ImageView iv_audio, iv_input, iv_menu, iv_send, iv_face;
	private ArrayList<MyQMessageBean> infos;
	private QChatAdapter adapter;
	private LoadMsgTask task;
	private LoadUnReadMsgTask task2;
	private FacesView faceView;
	private TableLayout tb;
	private boolean isLoading;
	private RelativeLayout mRlayout_RecordBox;
	private ProgressBar mPro_Loading;
	private LinearLayout mLlayout_DbBox;
	private LinearLayout mLlayout_MarkBox;
	private LinearLayout mLlayout_TrashBox;
	private ImageView mIv_Db;
	private DbHandler dbHandler;

	SurfaceView sv;
	SurfaceHolder sh;
	Camera camera;

	private String friendName, friendUrl, myId, myName, myHeadPath, myHeadUrl;

	private CreateFile cFile;

	private MessageBroadcastReceiver messageReceiver;
	private IntentFilter intentFilter;

	private boolean receiveUnRead = false;
	private String session_key;

	public int size;

	public String friendId, startMessagesId;

	public static MessageBean bean;

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
		messageReceiver = new MessageBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.CHAT_NUMBER);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "20");
		registerReceiver(messageReceiver, intentFilter); // 注册监听
		startNService(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(messageReceiver); // 取消监听
		startNService(false);
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		hideKeyBoardFace();
		SimpleReceiver.sendBoardcast(QChatActivity.this, MsgActivity.REFRESH);
		friendId = null;
		instance = null;
		bean = null;
	}

	private void findViews() {
		Bundle b = getIntent().getExtras();
		friendId = b.getString("memberId");
		friendName = b.getString("name");
		friendUrl = b.getString("head");
		ab.setTitle("与" + friendName + "的对话");

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
				BaseData.hideKeyBoard(QChatActivity.this);
				if (scrollState == SCROLL_STATE_IDLE
						&& listView.getFirstVisiblePosition() == 0) {
					if (!isLoading) {
						L.d(TAG, "findViews startMessagesId=" + startMessagesId);
						ArrayList<Object> requestInfo = ChatMessageBeanService
								.getService(QChatActivity.this).queryChat(
										startMessagesId, friendId);
						if (requestInfo != null && requestInfo.size() > 0) {
							for (Object object : requestInfo) {
								MyQMessageBean myBean = new MyQMessageBean(
										friendId, (MessageBean) object, false);
								myBean.setSuccess(true);
								infos.add(0, myBean);
							}
							try {
								startMessagesId = infos.get(0).getMsgBean()
										.getMessageId()
										+ "";
							} catch (Exception e) {
								e.printStackTrace();
							}
							L.i(TAG, "findViews startMessagesId="
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
					MediaManager.startRecord(QChatActivity.this, dbHandler);
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
							addAudio((String) result[0], (Integer) result[1]);
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

		// sv = (SurfaceView) findViewById(R.id.camera_view);
		// sh = sv.getHolder();
		// sh.addCallback(sc);
		// sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//
		// if (sv.isShown()) {
		// sv.setVisibility(View.GONE);
		// } else {
		// sv.setVisibility(View.VISIBLE);
		// }
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

	SurfaceHolder.Callback sc = new SurfaceHolder.Callback() {

		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();
			try {
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				camera.release();
				camera = null;
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// Camera.Parameters parameters = camera.getParameters();
			// parameters.setPictureFormat(PixelFormat.JPEG);
			camera.setDisplayOrientation(90);
			// camera.setParameters(parameters);
			camera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	};

	private MyQMessageBean getEmptyMessage(
			ArrayList<MessageResourceInfo> content, String contentStr) {
		MessageBean newBean = new MessageBean();
		MemberHeadBean head = new MemberHeadBean();
		head.setHeadPath(myHeadPath);
		head.setHeadUrl(myHeadUrl);
		newBean.setSenderHead(head);
		newBean.setMessageContent(contentStr);
		try {
			newBean.setSenderId(Integer.valueOf(myId));
			newBean.setMessageResourceInfoList(content);
			MemberHeadBean rhead = new MemberHeadBean();
			rhead.setFullPath(friendUrl);
			newBean.setReceiverHead(rhead);
			newBean.setReceiverName(friendName);
			newBean.setReceiverId(Integer.valueOf(friendId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return new MyQMessageBean(friendId, newBean, true, false, true);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.send:
			String text = edit.getText().toString();
			if (null != text && !"".equals(text.trim())) {
				// sendBtn.setEnabled(false);
				MessageResourceInfo info = new MessageResourceInfo();
				info.setObjectType("OBJ_TEXT");
				text = StringUtil.trimInnerSpaceStr(text);
				info.setMessageContent(text);
				ArrayList<MessageResourceInfo> content = new ArrayList<MessageResourceInfo>();
				content.add(info);
				sendInfo(content, null, text);
				edit.setText("");

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
			// faceView.setVisibility(View.GONE);
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
		case R.id.text_num:
			listView.smoothScrollToPosition(infos.size());
			tv_newNum.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void onInputMenuClick() {
		iv_input.setVisibility(View.GONE);
		iv_audio.setVisibility(View.VISIBLE);
		inputAudio.setVisibility(View.GONE);
		findViewById(R.id.inputBar).setVisibility(View.VISIBLE);
		showKeyBoard(edit);
	}

	private void onAudioMenuClick() {
		BaseData.hideKeyBoard(this);
		iv_audio.setVisibility(View.GONE);
		iv_input.setVisibility(View.VISIBLE);
		findViewById(R.id.inputBar).setVisibility(View.GONE);
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
					File picture = new File(saveFile, cFile.pictureName);
					uri = Uri.fromFile(picture);
					L.i(TAG, "onActivityResult uri2=" + uri);
					filePath = uri.getPath();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			addPhoto(filePath);
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
		}
	}

	@SuppressLint("HandlerLeak")
	class DbHandler extends Handler {
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
					T.show(QChatActivity.this, "path" + result[0]);
					addAudio((String) result[0], (Integer) result[1]);
				}
				break;
			case 3:
				mIv_Db.setImageResource(msg.arg1);
				break;
			case 4:
				L.d("time", "" + msg.arg1);
				T.show(QChatActivity.this, "录音时间还有" + msg.arg1 + "秒");
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

			default:
				break;
			}
		}
	}

	private void addPhoto(String photoPath) {
		MessageResourceInfo info = new MessageResourceInfo();
		// info.setMessageType(1);
		info.setObjectType("OBJ_PHOTO");
		info.setObjectPath(photoPath);
		info.setObjectUrl("");
		ArrayList<MessageResourceInfo> content = new ArrayList<MessageResourceInfo>();
		content.add(info);
		sendInfo(content, photoPath, null);
	}

	private void addAudio(String audioPath, long time) {
		MessageResourceInfo info = new MessageResourceInfo();
		// info.setMessageType(1);
		info.setObjectType("OBJ_VOICE");
		info.setObjectPath(audioPath);
		info.setObjectUrl("");
		info.setObjectBak1(time);
		ArrayList<MessageResourceInfo> content = new ArrayList<MessageResourceInfo>();
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
		myId = business.getMemberId(session_key);
		myName = business.getName(session_key);
		L.d(TAG, "setView myId=" + myId + ",myName=" + myName);

		infos = new ArrayList<MyQMessageBean>();
		queryFailMsg(true);

		adapter = new QChatAdapter(this, infos, listView, myId, friendId);
		listView.setAdapter(adapter);
		listView.removeFooterView();
		initTable();
		dbHandler = new DbHandler();

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
				if (!hasFocus) {
					showKeyBoard(v);
				}
			}
		});

		String eStr = App.app.share
				.getStringMessage("QChat", friendId + "", "");
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
		L.d(TAG, "queryFailMsg showLoc=" + showLoc);
		ArrayList<ChatSendBean> list = QChatSendService.getService(this)
				.queryById(session_key, friendId, 0);
		if (list != null) {
			for (ChatSendBean chatSendBean : list) {
				MessageResourceInfo info = new MessageResourceInfo();
				String oType = chatSendBean.getmType();
				L.d(TAG, "setView oType=" + oType);
				info.setObjectType(oType);
				if ("OBJ_VOICE".equals(oType) || "OBJ_PHOTO".equals(oType)) {
					info.setObjectPath(chatSendBean.getContent());
					info.setObjectUrl("");
				} else if ("OBJ_TEXT".equals(oType)) {
					info.setMessageContent(chatSendBean.getContent());
				}

				info.setObjectBak1(chatSendBean.gettLong());
				ArrayList<MessageResourceInfo> content = new ArrayList<MessageResourceInfo>();
				content.add(info);
				MyQMessageBean bean = getEmptyMessage(content,
						chatSendBean.getContent());
				bean.setC(this);
				bean.setTime(chatSendBean.getTime());
				if ("OBJ_VOICE".equals(oType)) {
					bean.setFilePath(chatSendBean.getContent());
				} else if ("OBJ_PHOTO".equals(oType)) {
					bean.setFilePath(chatSendBean.getContent());
				} else if ("OBJ_TEXT".equals(oType)) {

				}
				bean.setMemberId(friendId);
				bean.setSendState(false);
				bean.setSuccess(false);
				bean.setSendable(false);
				infos.add(bean);
			}
		}

		if (showLoc) {
			ArrayList<Object> requestInfo = ChatMessageBeanService.getService(
					this).queryChat("0", friendId);
			if (requestInfo != null)
				for (int i = 0; i < requestInfo.size(); i++) {
					try {
						MyQMessageBean myBean = new MyQMessageBean(friendId,
								(MessageBean) requestInfo.get(i), false);
						myBean.setSuccess(true);
						infos.add(0, myBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			try {
				startMessagesId = infos.get(0).getMsgBean().getMessageId() + "";
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
						cFile = new CreateFile(QChatActivity.this, true);
					}
					cFile.getLocalImg();
				} else if (tag.equals("photo")) {
					if (cFile == null) {
						cFile = new CreateFile(QChatActivity.this, true);
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

	private void sendInfo(ArrayList<MessageResourceInfo> data, String filePath,
			String contentStr) {
		ChatSendBean cBean = new ChatSendBean();
		long time = System.currentTimeMillis();
		cBean.setTime(time);
		cBean.setSession_key(session_key);
		try {
			cBean.setId(Integer.valueOf(friendId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		cBean.setName(friendName);
		cBean.setHead(friendUrl);
		cBean.setContent(contentStr);
		cBean.setcType(0);
		MessageResourceInfo info = data.get(0);
		String oType = info.getObjectType();
		cBean.setmType(oType);
		if ("OBJ_TEXT".equals(oType)) {
			cBean.setContent(info.getMessageContent());
		} else {
			cBean.setContent(filePath);
		}
		cBean.settLong(info.getObjectBak1());
		L.i(TAG, "sendInfo time=" + time);
		QChatSendService.getService(this).save(cBean);

		MyQMessageBean bean = getEmptyMessage(data, contentStr);
		bean.setC(this);
		bean.setTime(time);
		bean.setMemberId(friendId);
		bean.setSuccess(false);
		bean.setFilePath(filePath);
		infos.add(bean);
		adapter.notifyDataSetChanged();
		listView.setSelection(listView.getAdapter().getCount());
	}

	public class MessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.CHAT_NUMBER.equals(action)
					&& receiveUnRead) {
				getNewInfo(intent.getIntExtra("LETTER_NUM", 0));
			}
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
				MessageResourceInfo mrInfo = bean.getMessageResourceInfoList()
						.get(0);
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
				if (GetDbInfoUtil.getMemberId(App.app) == bean.getSenderId()) {
					name = "我";
				} else {
					name = bean.getSenderName();
				}
				if (objectType == null || "OBJ_TEXT".equals(objectType)) {
					info = "【转】" + name + "：" + bean.getMessageContent() + "【"
							+ date + "】";
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

	class LoadUnReadMsgTask extends AsyncTask<String, Integer, MCResult> {

		private int LETTER_NUM;

		public LoadUnReadMsgTask(int LETTER_NUM) {
			this.LETTER_NUM = LETTER_NUM;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			isLoading = true;
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.contactMemberMessages(
						QChatActivity.this, friendId, "0", LETTER_NUM + "",
						true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else if (1 == mcResult.getResultCode()) {
					@SuppressWarnings("unchecked")
					ArrayList<Object> requestInfo = (ArrayList<Object>) mcResult
							.getResult();
					if (null != requestInfo) {
						int rSize = requestInfo.size();
						size += rSize;
						if (0 == rSize) {
							// showTip("没有更多数据！");
						} else if (rSize > 0) {
							for (int i = 0; i < size; i++) {
								try {
									MyQMessageBean myBean = new MyQMessageBean(
											friendId,
											(MessageBean) requestInfo.get(i),
											false);
									myBean.setSuccess(true);
									infos.add(myBean);
									newIds.put(myBean.getMsgBean()
											.getMessageId(), myBean
											.getMsgBean().getMessageId());
								} catch (Exception e) {
									e.printStackTrace();
								}
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
			isLoading = false;
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
				if (isRefresh) {
					result = APIRequestServers.contactMemberMessages(
							QChatActivity.this, friendId, "0", "20", false,
							null);
				} else {
					result = APIRequestServers.contactMemberMessages(
							QChatActivity.this, friendId, "0", "20", false,
							startMessagesId);
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
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else if (1 == mcResult.getResultCode()) {
					@SuppressWarnings("unchecked")
					ArrayList<Object> requestInfo = (ArrayList<Object>) mcResult
							.getResult();
					if (null != requestInfo) {
						int rSize = requestInfo.size();
						size += rSize;
						L.d(TAG, "LoadMsgTask rSize=" + rSize + ",size=" + size
								+ ",isRefresh=" + isRefresh);
						if (rSize > 0) {
							if (isRefresh) {
								infos.clear();
								queryFailMsg(false);
							}
							for (int i = 0; i < rSize; i++) {
								try {
									MyQMessageBean myBean = new MyQMessageBean(
											friendId,
											(MessageBean) requestInfo.get(i),
											false);
									myBean.setSuccess(true);
									infos.add(0, myBean);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							try {
								startMessagesId = infos.get(0).getMsgBean()
										.getMessageId()
										+ "";
							} catch (Exception e) {
								e.printStackTrace();
							}
							L.d(TAG, "LoadMsgTask startMessagesId="
									+ startMessagesId);
							adapter.notifyDataSetChanged();
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
			isLoading = false;
			receiveUnRead = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
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
			App.app.share.saveStringMessage("QChat", friendId + "",
					System.currentTimeMillis() + "#"
							+ edit.getText().toString());
			setResult(RESULT_OK, new Intent());
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResize(int w, int h, int oldw, int oldh) {
		listView.setSelection(listView.getAdapter().getCount());
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
				App.app.share.saveStringMessage("QChat", friendId + "",
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
		iv_face.setImageResource(R.drawable.keyboardbtn);
		faceView.setVisibility(View.VISIBLE);
		inputAudio.setVisibility(View.GONE);
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
		tb.setVisibility(View.GONE);
		iv_face.setImageResource(R.drawable.icon_face);
		try {
			BaseData.showKeyBoard(this, edit);
			edit.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, edit, text, resId);
	}
}
