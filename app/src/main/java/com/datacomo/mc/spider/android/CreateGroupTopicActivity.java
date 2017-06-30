package com.datacomo.mc.spider.android;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.AdditiveFileListAdapter;
import com.datacomo.mc.spider.android.adapter.ImageChoosedGridViewAdapter;
import com.datacomo.mc.spider.android.adapter.ImageSingleGridViewAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.AdditiveFileBean;
import com.datacomo.mc.spider.android.bean.CommentSendBean;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.AdditiveFileDialog;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.params.UploadFileParams;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.APIMethodName;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.StreamUtil;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.datacomo.mc.spider.android.view.DialogView;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.FileListView;
import com.datacomo.mc.spider.android.view.GroupChoosedsHorScrollView;
import com.datacomo.mc.spider.android.view.GroupNameView;
import com.datacomo.mc.spider.android.view.ImageGridView;
import com.datacomo.mc.spider.android.view.Panel;
import com.datacomo.mc.spider.android.view.Panel.OnPanelListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
public class CreateGroupTopicActivity extends BasicActionBarActivity implements
		OnFaceChosenListner, OnPanelListener {
	private static final String TAG = "CreateGroupTopicActivity";

	private final int INSERT_PHOTO = 0;
	private int mItemWh;
	private int mSpace;
	private int num = 0;
	private int repId;
	private boolean allowedPublish = true;
	private boolean isSingleGroup = false;
	// private boolean getCommGroupListRun = false;
	// public List<GroupEntity> groupEntityList = null;
	// private ArrayList<GroupEntity> matchGroupList;
	private String title, content;
	private String shareStr, shareTitle;
	private String pictureName = null;
	private Uri shareUri;
	// 已上传成功的path与时间戳
	private LinkedHashMap<String, String> fileTempsList;
	private LinkedHashMap<String, String> photoTempsList;
	/**
	 * @某人：K(成员Id)、V(@name：)
	 */
	private HashMap<String, String> atMap;
	private ArrayList<GroupEntity> choosedBean = new ArrayList<GroupEntity>();
	// class
	private AdditiveFileDialog mCreateFile;
	private AlertDialog pDialog = null;
	private DialogView dialogContent = null;
	private ImageSingleGridViewAdapter mImgGvAdapter_Choosed;
	private AdditiveFileListAdapter mAdditiveFileLvAdapter_Choosed;
	// view
	private CheckBox checkBox;
	private ImageView location_iv, photo_iv, file_iv, face_iv, at_iv;
	private FacesView faceView;

	// private GroupView groupView;select_iv,
	// private GroupAutoCompleteTextView add_et;

	private EditText topic_title, topic_content;
	private ImageGridView mImgGv_ChoosedImg;
	private FileListView mAdditiveFileLv_ChoosedImg;
	private ViewStub mVs_FileLv, mVs_ImgGv;

	private Panel group_panel;
	private GroupChoosedsHorScrollView mHv_GroupChooseds;
	private ImageView mImg_Arrow;

	private ArrayList<GroupEntity> ges = null;
	private Type type = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.create_topic);
		fileTempsList = new LinkedHashMap<String, String>();
		photoTempsList = new LinkedHashMap<String, String>();
		// matchGroupList = new ArrayList<GroupEntity>();

		findView();

		// String session_key = share.getSessionKey();
		// if (session_key == null || "".equals(session_key)) {
		// getGroupListHandler.sendEmptyMessage(11);
		// }
		// getCommGroupList();
		// new PreHandler().postDelayed(null, 100);

		setView();
	}

	/**
	 * 加载草稿
	 * 
	 * @param csb
	 */
	private void loadDraft(CommentSendBean csb) {
		try {
			JSONArray array = new JSONArray(csb.getGidname());
			L.d(TAG, "loadDraft ges=" + array.toString());
			ges = new ArrayList<GroupEntity>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				GroupEntity ge = new GroupEntity(json.getString("id") + "",
						json.getString("name"), json.getString("head"), "", "",
						"");
				ges.add(ge);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		shareTitle = csb.getTitle();
		shareStr = csb.getContent();

		try {
			String ats = csb.getAtidname();
			if (ats != null && !"".equals(ats)) {
				type = Type.DEFAULT;
				atMap = new LinkedHashMap<String, String>();
				ats = ats.substring(0, ats.length() - 1);
				L.d(TAG, "onCreate ats=" + ats);
				if (ats.contains("&")) {
					String[] as = ats.split("&");
					for (String a : as) {
						atMap.put(a.substring(0, a.indexOf("#")),
								a.substring(a.indexOf("#") + 1));
					}
				} else {
					atMap.put(ats.substring(0, ats.indexOf("#")),
							ats.substring(ats.indexOf("#") + 1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			JSONArray array = new JSONArray(csb.getFilePaths());
			List<String> photoPath = new ArrayList<String>();
			List<Object> beans = new ArrayList<Object>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				int type = json.getInt("type");
				if (type == 0) {
					photoPath.add(json.getString("path"));
				} else if (type == 1) {
					AdditiveFileBean file = new AdditiveFileBean();
					String filePath = json.getString("path");
					L.d(TAG, "loadDraft filePath=" + filePath);
					file.setUri(filePath);
					int length = filePath.lastIndexOf("/");
					file.setName(filePath.substring(length + 1));
					File temp = new File(filePath);
					file.setSize(temp.length());
					file.setMime_type(FileUtil.getMIMEType(temp));
					filePath = null;
					temp = null;
					showFileBox();
					mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
				} else if (type == 2) {
					FileInfoBean file = new FileInfoBean();
					JSONObject fj = new JSONObject(json.getString("path"));
					L.d(TAG, "loadDraft fj=" + fj.toString());
					file.setFileId(fj.getInt("id"));
					String name = fj.getString("name");
					file.setFileName(name.substring(0, name.lastIndexOf(".")));
					file.setFormatName(name.substring(name.lastIndexOf(".") + 1));
					file.setFileSize(fj.getLong("size"));
					beans.add(file);
				}
			}
			L.d(TAG, "loadDraft beans.size=" + beans.size());
			if (photoPath.size() > 0)
				addPhoto(photoPath);
			if (beans.size() > 0) {
				showFileBox();
				for (Object object : beans) {
					mAdditiveFileLvAdapter_Choosed.addAtFirst(object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "19");
		L.d(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideFace();
	}

	private Handler addGroupHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle b = new Bundle();
			b.putSerializable("datas",
					(ArrayList<GroupEntity>) mHv_GroupChooseds.getGroups());
			LogicUtil.enter(CreateGroupTopicActivity.this,
					GroupsChooserActivity.class, b,
					GroupsChooserActivity.RESCODE);
		}

	};

	// class PreHandler extends Handler {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// refreshGroups();
	// }
	// }
	//
	// private void refreshGroups() {
	// if (null == choosedBean) {
	// return;
	// }
	// // groupView.removeAllViews();
	// for (int i = 0; i < choosedBean.size(); i++) {
	// addView(choosedBean.get(i));
	// }
	// }

	private void findView() {
		// groupView = (GroupView) findViewById(R.id.create_topic_groupView);
		// select_iv = (ImageView) findViewById(R.id.create_topic_selectGroup);
		// add_et = (GroupAutoCompleteTextView)
		// findViewById(R.id.create_topic_inputGroup);

		group_panel = (Panel) findViewById(R.id.create_topic_panel);
		mHv_GroupChooseds = (GroupChoosedsHorScrollView) findViewById(R.id.create_topic_groupchooseds);
		mImg_Arrow = (ImageView) findViewById(R.id.layout_create_topic_img_arrow);

		topic_title = (EditText) findViewById(R.id.create_topic_title);
		topic_content = (EditText) findViewById(R.id.create_topic_content);

		location_iv = (ImageView) findViewById(R.id.create_topic_location);
		photo_iv = (ImageView) findViewById(R.id.create_topic_photo);
		file_iv = (ImageView) findViewById(R.id.create_topic_file);
		face_iv = (ImageView) findViewById(R.id.create_topic_face);
		at_iv = (ImageView) findViewById(R.id.create_topic_at);
		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);
		mVs_ImgGv = (ViewStub) findViewById(R.id.create_topic_vs_imagegrid);
		mVs_ImgGv.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				boolean upload_original_image = App.app.share
						.getBooleanMessage("program", "upload_original_image",
								false);
				checkBox = (CheckBox) inflated
						.findViewById(R.id.create_topic_checkBox);
				checkBox.setChecked(upload_original_image);
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						showPhotoSize(mImgGvAdapter_Choosed.getUrls());
						App.app.share.saveBooleanMessage("program",
								"upload_original_image", isChecked);
					}
				});
				mImgGv_ChoosedImg = (ImageGridView) findViewById(R.id.layout_createtopic_choosedimg);
				mImgGv_ChoosedImg.setColumnWidth(mItemWh);
				mImgGv_ChoosedImg.setHorizontalSpacing(mSpace);
				mImgGv_ChoosedImg.setPadding(mSpace, mSpace, mSpace, mSpace);
				mImgGv_ChoosedImg.setVerticalSpacing(mSpace);
				mImgGv_ChoosedImg
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								if (position > mImgGvAdapter_Choosed.getUrls()
										.size() - 1) {
									hideKeyBoardFace();
									choosePicture();
								} else {
									showPhoto(mImgGvAdapter_Choosed.getUrls()
											.get(position));
								}
							}
						});
				mImgGv_ChoosedImg
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								if (position <= mImgGvAdapter_Choosed.getUrls()
										.size() - 1)
									deleteDialog(mImgGvAdapter_Choosed
											.getUrls().get(position), position);
								return true;
							}
						});
				mImgGvAdapter_Choosed.setUrls(new ArrayList<String>());
				mImgGv_ChoosedImg.setAdapter(mImgGvAdapter_Choosed);
			}
		});
		mVs_FileLv = (ViewStub) findViewById(R.id.create_topic_vs_filelist);
		mVs_FileLv.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				mAdditiveFileLv_ChoosedImg = (FileListView) inflated
						.findViewById(R.id.layout_createtopic_filelist);
				mAdditiveFileLv_ChoosedImg
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								TransitionBean bean = null;
								try {
									bean = new TransitionBean(parent
											.getItemAtPosition(position));
								} catch (Exception e) {
									e.printStackTrace();
								}
								switch (bean.getType()) {
								case CLOUDFILE:
									String name = StringUtil.subString(
											bean.getPath(), "/", null, 1, 0);
									L.d(TAG, "name:" + name);
									File myFile = new File(
											ConstantUtil.CLOUD_PATH + name);
									if (myFile != null && myFile.exists()) {
										new FileUtil()
												.openFile(App.app, myFile);
									} else if (ConstantUtil.downloadingList
											.contains(name)) {
										T.show(App.app, R.string.downloading);
									} else {
										openFile(name, bean.getId(),
												bean.getFileOriginalSize());
									}
									break;
								case ADDITIVEFILE:
									new FileUtil().openFile(
											CreateGroupTopicActivity.this,
											new File(bean.getPath()),
											bean.getMimeType());
									break;
								default:
									break;
								}
							}
						});
				mAdditiveFileLv_ChoosedImg
						.setAdapter(mAdditiveFileLvAdapter_Choosed);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void setView() {
		group_panel.setOnPanelListener(this);
		topic_title.setOnClickListener(this);
		topic_title.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				group_panel.setOpen(false, true);
				hideFace();
			}
		});
		topic_content.setOnClickListener(this);
		topic_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				group_panel.setOpen(false, true);
				hideFace();
			}
		});

		location_iv.setOnClickListener(this);
		photo_iv.setOnClickListener(this);
		file_iv.setOnClickListener(this);
		face_iv.setOnClickListener(this);
		at_iv.setOnClickListener(this);

		int screenWidth = BaseData.getScreenWidth();
		mSpace = (int) (screenWidth * 0.02);
		mItemWh = (screenWidth - mSpace * 5) / 4;
		mImgGvAdapter_Choosed = new ImageSingleGridViewAdapter(
				new ArrayList<String>(), mItemWh);
		mAdditiveFileLvAdapter_Choosed = new AdditiveFileListAdapter(
				CreateGroupTopicActivity.this, new ArrayList<Object>());

		Intent intent = getIntent();
		CommentSendBean csb = (CommentSendBean) intent
				.getSerializableExtra("CommentSendBean");
		if (csb != null) {
			loadDraft(csb);
		} else {
			ges = (ArrayList<GroupEntity>) intent.getSerializableExtra("datas");
			type = (Type) intent.getSerializableExtra(BundleKey.TYPE_REQUEST);

			shareTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT);
			shareStr = intent.getStringExtra(Intent.EXTRA_TEXT);
			// if (shareStr == null || "".equals(shareStr)) {
			// CharSequence localCharSequence = intent
			// .getCharSequenceExtra(Intent.EXTRA_TEXT);
			// if (localCharSequence != null)
			// shareStr = localCharSequence.toString();
			// }
			shareUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

			repId = intent.getIntExtra("repId", 0);
			atMap = new LinkedHashMap<String, String>();
			if (repId > 0)
				atMap.put(repId + "", shareStr);
			L.d(TAG, "onCreate shareTitle=" + shareTitle + ",shareStr="
					+ shareStr + ",shareUri=" + shareUri + ",repId=" + repId);
		}
		if (null != ges && !ges.isEmpty()) {
			L.d(TAG, "onCreate ges.size=" + ges.size());
			choosedBean.addAll(ges);
			if (null != type && type != Type.STARTCREATGROUPTOPIC)
				isSingleGroup = true;
		}

		mHv_GroupChooseds.setHandler(addGroupHandle);
		if (null != ges && !ges.isEmpty())
			mHv_GroupChooseds.setGroups(ges);
		else
			mHv_GroupChooseds.setGroups(new ArrayList<GroupEntity>());
		mHv_GroupChooseds.initGroupBox();
		group_panel.setOpen(!group_panel.isOpen(), false);

		String title = "发圈博";
		if (isSingleGroup) {
			String name = choosedBean.get(0).getName();
			if (name != null && !"".equals(name)) {
				title = name;
			}

			at_iv.setVisibility(View.VISIBLE);
			group_panel.setVisibility(View.GONE);
		}
		ab.setTitle(title);

		if (shareTitle != null && !"".equals(shareTitle)) {
			topic_title.setText(shareTitle);
			topic_title.requestFocus();
			group_panel.setOpen(false, true);
		}

		if (shareStr != null && !"".equals(shareStr)) {
			topic_content.setText(shareStr);
			Editable eb = topic_content.getEditableText();
			for (int i = 0; i < FaceUtil.FACE_TEXTS.length; i++) {
				String face = FaceUtil.FACE_TEXTS[i];
				if (shareStr.contains(face)) {
					int k = shareStr.length() - face.length() + 1;
					L.i(TAG, "setView face=" + face + ",k=" + k);
					for (int j = 0; j < k; j++) {
						if (shareStr.substring(j).startsWith(face)) {
							L.d(TAG, "setView j=" + j + ",k=" + k);
							Drawable drawable = getResources().getDrawable(
									FaceUtil.FACE_RES_IDS[i]);
							int dw = drawable.getIntrinsicWidth();
							int dh = drawable.getIntrinsicHeight();
							drawable.setBounds(0, 0, dw / 2, dh / 2);
							// 需要处理的文本，[smile]是需要被替代的文本
							SpannableString spannable = new SpannableString(
									face);
							// 要让图片替代指定的文字就要用ImageSpan
							ImageSpan span = new ImageSpan(drawable,
									ImageSpan.ALIGN_BOTTOM);

							spannable.setSpan(span, 0, face.length(),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							eb.replace(j, j + face.length(), spannable);
						}
					}
				}

			}
			topic_content.setText(eb);

			topic_content.requestFocus();
			group_panel.setOpen(false, true);
		}

		if (shareUri != null) {
			L.d(TAG, "setView shareUri.getPath=" + shareUri.getPath());
			try {
				String sharePath = queryPhoto(shareUri);
				String type = FileUtil.getMIMEType(new File(sharePath));
				if (type.startsWith("image/")) {
					changeImg(sharePath);
				} else {
					if (FileUtil.checkImage(new File(sharePath))) {
						changeImg(sharePath);
					} else {
						sharePath = shareUri.getPath();
						L.i(TAG, "setView filepath=" + sharePath);
						if (mAdditiveFileLvAdapter_Choosed.contains(sharePath) != -1) {
							T.show(this, "该文件已加入过");
						} else {
							AdditiveFileBean file = new AdditiveFileBean();
							file.setUri(sharePath);
							int length = sharePath.lastIndexOf("/");
							file.setName(sharePath.substring(length + 1));
							File temp = new File(sharePath);
							file.setSize(temp.length());
							file.setMime_type(FileUtil.getMIMEType(temp));
							sharePath = null;
							temp = null;
							mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {

			}
		}
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

	// private TextWatcher watcher = new TextWatcher() {
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	//
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// String str = s.toString();
	// L.i(TAG, "watcher str=" + str);
	// matchGroupList.clear();
	// if (str != null && !"".equals(str)) {
	// if (groupEntityList != null) {
	// for (GroupEntity groupEntity : groupEntityList) {
	// String name = groupEntity.getName();
	// L.i(TAG, "watcher name=" + name);
	// if (name != null
	// && (name.toLowerCase().startsWith(str
	// .toLowerCase()))) {
	// matchGroupList.add(groupEntity);
	// } else {
	// boolean isStrWith = false;
	//
	// String groupNamePy = groupEntity.getGroupNamePy();
	// L.i(TAG, "watcher groupNamePy=" + groupNamePy);
	// if (groupNamePy.contains(",")) {
	// String[] pys = groupNamePy.split(",");
	// int size = 0;
	// if (pys != null) {
	// size = pys.length;
	// for (int i = 0; i < size; i++) {
	// String py = pys[i];
	// if (py != null
	// && (py.toLowerCase()
	// .startsWith(str
	// .toLowerCase()))) {
	// matchGroupList.add(groupEntity);
	// isStrWith = true;
	// break;
	// }
	// }
	// }
	// } else {
	// if (groupNamePy.toLowerCase().startsWith(
	// str.toLowerCase())) {
	// matchGroupList.add(groupEntity);
	// isStrWith = true;
	// }
	// }
	//
	// if (!isStrWith) {
	// String groupNameJp = groupEntity
	// .getGroupNameJp();
	// L.i(TAG, "watcher groupNameJp=" + groupNameJp);
	// if (groupNameJp.contains(",")) {
	// String[] jps = groupNameJp.split(",");
	// int size = 0;
	// if (jps != null) {
	// size = jps.length;
	// for (int i = 0; i < size; i++) {
	// String jp = jps[i];
	// if (jp != null
	// && (jp.toLowerCase()
	// .startsWith(str
	// .toLowerCase()))) {
	// matchGroupList.add(groupEntity);
	// break;
	// }
	// }
	// }
	// } else {
	// if (groupNameJp.toLowerCase().startsWith(
	// str.toLowerCase())) {
	// matchGroupList.add(groupEntity);
	// }
	// }
	// }
	// }
	// }
	// } else {
	// L.i(TAG, "watcher groupEntityList=null");
	// }
	//
	// int mSize = matchGroupList.size();
	// if (str.length() > 0 && !(mSize > 0)) {
	// showToast(str);
	// }
	// L.i(TAG, "watcher size=" + mSize);
	// }
	// }
	// };
	//
	// private void showToast(String str) {
	// ShowToast.getToast(CreateGroupTopicActivity.this).show(
	// "你没有在名字以“" + str + "”开始的圈子里");
	// }

	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			String filePath = null;
			Bundle bundle = null;
			switch (requestCode) {
			case INSERT_PHOTO:// 插入图片
				try {
					if (data != null) {
						uri = data.getData();
						L.i(TAG, "onActivityResult uri0=" + uri);
						if (uri != null) {
							filePath = queryPhoto(uri);
						} else {
							File saveFile = new File(ConstantUtil.CAMERA_PATH);
							File picture = new File(saveFile, pictureName);
							uri = Uri.fromFile(picture);
							L.i(TAG, "onActivityResult uri1=" + uri);
							filePath = uri.getPath();
							pictureName = null;
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, pictureName);
						uri = Uri.fromFile(picture);
						L.i(TAG, "onActivityResult uri2=" + uri);
						filePath = uri.getPath();
						pictureName = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						uri));
				L.i(TAG, "onActivityResult filepath=" + filePath);
				changeImg(filePath);
				break;
			case AdditiveFileDialog.VIDEO:
				if (null != data) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (null != uri) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(
									uri,
									new String[] { Media.DATA,
											Media.DISPLAY_NAME, Media.SIZE,
											Media.MIME_TYPE,
											Video.VideoColumns.DURATION },
									null, null, null);
							if (null == cursor || cursor.getCount() < 0) {
								filePath = uri.getPath();
								String whereClause = Media.DATA + " = '"
										+ filePath + "'";
								cursor = getContentResolver().query(
										Media.EXTERNAL_CONTENT_URI,
										new String[] { Media.DATA,
												Media.DISPLAY_NAME, Media.SIZE,
												Media.MIME_TYPE,
												Video.VideoColumns.DURATION },
										whereClause, null, null);
							}
							AdditiveFileBean file = new AdditiveFileBean();
							if (null != cursor && cursor.getCount() > 0) {
								cursor.moveToFirst();
								file.setName(cursor.getString(cursor
										.getColumnIndex(Media.DISPLAY_NAME)));
								file.setUri(cursor.getString(cursor
										.getColumnIndex(Media.DATA)));
								file.setTime(cursor.getLong(cursor
										.getColumnIndex(Video.VideoColumns.DURATION)));
								file.setSize(cursor.getLong(cursor
										.getColumnIndex(Media.SIZE)));
								file.setMime_type(cursor.getString(cursor
										.getColumnIndex(Media.MIME_TYPE)));
							} else {
								filePath = uri.getPath();
								file.setUri(filePath);
								int length = filePath.lastIndexOf("/");
								file.setName(filePath.substring(length + 1));
								File temp = new File(filePath);
								file.setSize(temp.length());
								file.setMime_type(FileUtil.getMIMEType(temp));
								filePath = null;
								temp = null;
							}
							showFileBox();
							if (mAdditiveFileLvAdapter_Choosed.contains(file
									.getUri()) != -1) {
								T.show(App.app,
										StringUtil.merge(file.getName(), "已存在"));
								file = null;
							} else {
								mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (null != cursor) {
								cursor.close();
								cursor = null;
							}
						}
					}
				}
				break;
			case AdditiveFileDialog.AUDIO:
				if (null != data) {
					uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (null != uri) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(
									uri,
									new String[] { Media.DATA,
											Media.DISPLAY_NAME, Media.SIZE,
											Media.MIME_TYPE,
											Audio.AudioColumns.DURATION },
									null, null, null);
							if (null == cursor || cursor.getCount() < 0) {
								filePath = uri.getPath();
								String whereClause = Media.DATA + " = '"
										+ filePath + "'";
								try {
									cursor = getContentResolver()
											.query(Media.EXTERNAL_CONTENT_URI,
													new String[] {
															Media.DATA,
															Media.DISPLAY_NAME,
															Media.SIZE,
															Media.MIME_TYPE,
															Audio.AudioColumns.DURATION },
													whereClause, null, null);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							AdditiveFileBean file = new AdditiveFileBean();
							if (null != cursor && cursor.getCount() > 0) {
								cursor.moveToFirst();
								file.setName(cursor.getString(cursor
										.getColumnIndex(Media.DISPLAY_NAME)));
								file.setUri(cursor.getString(cursor
										.getColumnIndex(Media.DATA)));
								file.setTime(cursor.getLong(cursor
										.getColumnIndex(Video.VideoColumns.DURATION)));
								file.setSize(cursor.getLong(cursor
										.getColumnIndex(Media.SIZE)));
								file.setMime_type(cursor.getString(cursor
										.getColumnIndex(Media.MIME_TYPE)));
							} else {
								filePath = uri.getPath();
								file.setUri(filePath);
								int length = filePath.lastIndexOf("/");
								file.setName(filePath.substring(length + 1));
								File temp = new File(filePath);
								file.setSize(temp.length());
								file.setMime_type(FileUtil.getMIMEType(temp));
								MediaPlayer mp = new MediaPlayer();
								mp.reset();
								mp.setDataSource(filePath);
								mp.prepare();
								file.setTime(mp.getDuration());
								filePath = null;
								temp = null;
							}
							showFileBox();
							if (mAdditiveFileLvAdapter_Choosed.contains(file
									.getUri()) != -1) {
								T.show(App.app,
										StringUtil.merge(file.getName(), "已存在"));
								file = null;
							} else {
								mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (null != cursor) {
								cursor.close();
								cursor = null;
							}
						}
					}
				}
				break;
			case AdditiveFileDialog.CLOUDFILE:
				showFileBox();
				List<Object> beans = (List<Object>) data
						.getSerializableExtra(BundleKey.CHOOSEDS);
				mAdditiveFileLvAdapter_Choosed.flush(beans);
				break;
			case AdditiveFileDialog.OTHAR:
				AdditiveFileBean file = new AdditiveFileBean();
				filePath = data.getStringExtra("filePath");
				file.setUri(filePath);
				int length = filePath.lastIndexOf("/");
				file.setName(filePath.substring(length + 1));
				File temp = new File(filePath);
				file.setSize(temp.length());
				file.setMime_type(FileUtil.getMIMEType(temp));
				filePath = null;
				temp = null;
				showFileBox();
				mAdditiveFileLvAdapter_Choosed.addAtFirst(file);
				break;
			case GroupsChooserActivity.RESCODE:
				// choosedBean = (ArrayList<GroupEntity>) data
				// .getSerializableExtra("datas");
				// refreshGroups();
				choosedBean = (ArrayList<GroupEntity>) data
						.getSerializableExtra("datas");
				mHv_GroupChooseds.flush(choosedBean);
				break;
			case PhotoGalleryActivity.IDDELETE:
				if (null != data)
					bundle = data.getExtras();
				if (null == bundle)
					break;
				// photoPathList.remove(bundle.getString("oUrl"));
				StringBuffer str = new StringBuffer();
				str.append(bundle.getString("url"));
				str.append(ThumbnailImgUtil.MARK);
				str.append(bundle.getString("id"));
				mImgGvAdapter_Choosed.remove(str.toString().trim());
				int length1 = str.length();
				str.delete(0, length1);
				str = null;
				showPhotoSize(mImgGvAdapter_Choosed.getUrls());
				break;
			case ImageBucketActivity.CHOOSEIMG:
			case ImageChooseActivity.CHOOSEIMG:
				if (null != data)
					bundle = data.getExtras();
				if (null == bundle)
					break;
				// changeImg(CreateGroupTopicActivity.this, bundle);
				addPhoto(bundle.getStringArrayList("chooseds"));
				break;
			case GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER:
				if (null != data)
					bundle = data.getExtras();
				if (null == bundle)
					break;
				HashMap<String, String> map = (HashMap<String, String>) bundle
						.getSerializable(BundleKey.CHOOSEDS);
				if (null != atMap && atMap.size() > 0) {
					content = topic_content.getText().toString();
					for (Iterator<?> iterator = atMap.keySet().iterator(); iterator
							.hasNext();) {
						String key = (String) iterator.next();
						if (!map.containsKey(key)) {
							String value = atMap.get(key);
							if (content.contains(value)) {
								content = content.replaceAll(value, "");
							}
						}
					}
					topic_content.setText(content);
					topic_content.setSelection(content.length());
				}
				atMap.clear();
				atMap.putAll(map);
				if (null != atMap && atMap.size() > 0) {
					content = topic_content.getText().toString();
					for (Iterator<?> iterator = atMap.keySet().iterator(); iterator
							.hasNext();) {
						String id = (String) iterator.next();
						if (!content.contains(atMap.get(id))) {
							content += atMap.get(id);
						}
					}
					topic_content.setText(content);
					topic_content.setSelection(content.length());
				}
				break;
			}
		}
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
		return filepath;
	}

	/**
	 * 添加图片
	 * 
	 * @param filepath
	 */
	private void addPhoto(String photoPath) {
		showImageBox();
		mImgGvAdapter_Choosed.add(photoPath);
		showPhotoSize(mImgGvAdapter_Choosed.getUrls());
	}

	/**
	 * 添加图片
	 * 
	 * @param filepath
	 */
	private void addPhoto(List<String> photoPath) {
		showImageBox();
		mImgGvAdapter_Choosed.flush(photoPath);
		showPhotoSize(mImgGvAdapter_Choosed.getUrls());
	}

	/**
	 * 显示原图上传大小
	 */
	public void showPhotoSize(List<String> urls) {

		if (urls.size() > 0) {
			if (checkBox.getVisibility() != View.VISIBLE) {
				checkBox.setVisibility(View.VISIBLE);
			}
		} else {
			checkBox.setVisibility(View.GONE);
		}
		if (!checkBox.isChecked()) {
			checkBox.setText("原图上传");
			return;
		}
		long size = 0L;
		for (String filePath : urls) {
			L.d(TAG, "showPhotoSize filePath=" + filePath);
			if (filePath != null) {
				File file = new File(ThumbnailImgUtil.getData(filePath));
				size += file.length();
			}
		}
		if (size > 0)
			checkBox.setText("原图上传（" + FileUtil.computeFileSize(size) + "）");
	}

	@Override
	public void onClick(View v) {
		group_panel.setOpen(false, true);
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.create_topic_inputGroup:// 输入圈子
		// showKeyBoard(v);
		// break;
		case R.id.create_topic_title:// 圈博内容
			showKeyBoard(v);
			break;
		case R.id.create_topic_content:// 圈博内容
			showKeyBoard(v);
			break;
		// case R.id.create_topic_selectGroup:// 选择圈子
		// Bundle b = new Bundle();
		// b.putSerializable("datas", choosedBean);
		// LogicUtil.enter(this, GroupsChooserActivity.class, b, true,
		// GroupsChooserActivity.RESCODE, false);
		// break;
		// case R.id.create_topic_showLocation:// 更多位置
		// if (hasLocation && moreLocation) {
		// moreLocation = false;
		// L.d(TAG, "onClick 经度=" + mLon + ",纬度=" + mlat + ",位置=" + loc);
		//
		// final Handler handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// switch (msg.what) {
		// case 1:
		// @SuppressWarnings("unchecked")
		// ArrayList<String> lists = (ArrayList<String>) msg.obj;
		// if (lists != null) {
		// int size = lists.size();
		// L.i(TAG, "onClick size=" + size);
		// final String[] datas = new String[size];
		// for (int i = 0; i < size; i++) {
		// datas[i] = lists.get(i);
		// }
		// new AlertDialog.Builder(
		// CreateGroupTopicActivity.this)
		// .setTitle("更多位置")
		// .setItems(
		// datas,
		// new DialogInterface.OnClickListener() {
		// public void onClick(
		// DialogInterface dialog,
		// int which) {
		// loc = datas[which];
		// L.d(TAG,
		// "showListDialog data="
		// + loc);
		// showLoc_tv.setText(loc);
		// }
		// }).show();
		// }
		// break;
		// default:
		// break;
		// }
		// }
		// };
		//
		// new Thread() {
		// public void run() {
		// ArrayList<String> locList = new MoreLocationInfo()
		// .getMoreLocation(mLon, mlat);
		// if (locList != null && locList.size() > 0) {
		// Message msg = new Message();
		// msg.what = 1;
		// msg.obj = locList;
		// handler.sendMessage(msg);
		// }
		// moreLocation = true;
		// };
		// }.start();
		// }
		// break;
		// case R.id.create_topic_location:// 位置
		// hideKeyBoardFace();
		// if (showLocLayout.isShown()) {
		// deleteDialog(0, "是否删除位置信息？", null);
		// } else {
		// hasLocation = true;
		// showLocLayout.setVisibility(View.VISIBLE);
		// showLoc_iv.setVisibility(View.GONE);
		// showLoc_pb.setVisibility(View.VISIBLE);
		// showLoc_tv.setText("正在定位中...");
		// addLocation();
		// }
		// break;
		case R.id.create_topic_photo:// 图片
			hideKeyBoardFace();
			choosePicture();
			break;
		case R.id.create_topic_file:// 文件
			hideKeyBoardFace();
			if (mCreateFile == null) {
				mCreateFile = new AdditiveFileDialog(this);
			}
			mCreateFile.setFiles(mAdditiveFileLvAdapter_Choosed.getBeans());
			mCreateFile.createDialog();
			break;
		case R.id.create_topic_face:// 表情
			// if (add_et.isFocused()) {
			// topic_content.requestFocus();
			// }
			if (faceView.isShown()) {
				showKeyBoard(topic_content);
			} else {
				showFace();
			}
			break;
		case R.id.create_topic_at:// @某人
			GroupEntity groupEntity = choosedBean.get(0);
			Bundle bundle = new Bundle();
			try {
				bundle.putInt(BundleKey.ID_GROUP,
						Integer.parseInt(groupEntity.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			bundle.putSerializable(BundleKey.CHOOSEDS, atMap);
			bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.ATGROUPLEAGUER);
			LogicUtil.enter(CreateGroupTopicActivity.this,
					GroupLeaguerChooseActivity.class, bundle,
					GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER);
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
			} else {
				backPrompt();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 发圈博
	 */
	private void sendTopic() {
		if (choosedBean.size() == 0) {
			T.show(this, "请先选择圈子");
		} else {
			sendGroupTopic(getGroupIds(), ++num);
		}
	}

	/**
	 * 返回提示
	 */
	private void backPrompt() {
		title = topic_title.getEditableText().toString();
		content = topic_content.getEditableText().toString();
		if ((title != null && !title.equals(""))
				|| (content != null && !content.equals(""))) {
			// 已输入过内容
			ConfirmExit();
			return;
		}
		if (// ((null != choosedBean && choosedBean.size() != 0) &&
			// !isSingleGroup) ||
		!mImgGvAdapter_Choosed.getUrls().isEmpty()
				|| !mAdditiveFileLvAdapter_Choosed.getBeans().isEmpty()) {
			// 已选过圈子或上传过图片文件
			ConfirmExit();
			return;
		}
		hideKeyBoardFace();
		finish();
	}

	/** 退出确认 */
	private void ConfirmExit() {
		if (choosedBean.size() != 0) {
			saveLocal();
			showTip("已保存到草稿箱");
			CreateGroupTopicActivity.this.finish();
		} else {

		}

		new AlertDialog.Builder(this).setTitle("放弃发送")
				.setMessage("放弃发送会导致已输入的内容丢失。")
				.setPositiveButton("放弃", new DialogInterface.OnClickListener() {// 退出按钮
							@Override
							public void onClick(DialogInterface dialog, int i) {
								hideKeyBoardFace();
								CreateGroupTopicActivity.this.finish();
							}
						}).setNegativeButton("取消", null).show();// 显示对话框
	}

	/**
	 * 
	 */
	private void saveLocal() {
		try {
			CommentSendBean csb = new CommentSendBean();
			UserBusinessDatabase business = new UserBusinessDatabase(App.app);
			String session_key = App.app.share.getSessionKey();
			csb.setTime(System.currentTimeMillis());
			csb.setSession_key(session_key);
			csb.setId(GetDbInfoUtil.getMemberId(App.app));
			csb.setName("我");
			final String headUrlPath = business.getHeadUrlPath(session_key);
			csb.setHead(headUrlPath);
			csb.setmType("OBJ_SEND_QUUBO");
			csb.setTitle(title);
			csb.setContent(content);
			csb.setSendStatus(-1);
			// @某人
			if (atMap != null && !atMap.isEmpty()) {
				String atidname = "";
				for (Iterator<?> iterator = atMap.keySet().iterator(); iterator
						.hasNext();) {
					String id = (String) iterator.next();
					atidname += id + "#" + atMap.get(id) + "&";
				}
				L.d(TAG, "saveLocal atidname=" + atidname);
				csb.setAtidname(atidname);
			}
			// 圈子
			JSONArray ga = new JSONArray();
			for (GroupEntity ge : choosedBean) {
				JSONObject json = new JSONObject();
				json.put("id", ge.getId());
				json.put("name", ge.getName());
				json.put("head", ge.getHead());
				ga.put(json);
			}
			L.d(TAG, "saveLocal ges=" + ga.toString());
			csb.setGidname(ga.toString());

			JSONArray fa = new JSONArray();
			// 图片
			for (String path : mImgGvAdapter_Choosed.getUrls()) {
				JSONObject json = new JSONObject();
				json.put("path", path);
				json.put("type", 0);// 0:图片，1:文件,2:云文件
				fa.put(json);
			}
			// 本地文件
			mAdditiveFileLvAdapter_Choosed.classify();
			for (Object file : mAdditiveFileLvAdapter_Choosed
					.getAdditiveFile(false)) {
				JSONObject json = new JSONObject();
				json.put("path", new TransitionBean(file).getPath());
				json.put("type", 1);
				fa.put(json);
			}
			// 云文件
			for (Object file : mAdditiveFileLvAdapter_Choosed
					.getCloudFile(false)) {
				TransitionBean tb = new TransitionBean(file);
				JSONObject fj = new JSONObject();
				fj.put("id", tb.getId());
				fj.put("name", tb.getName());
				fj.put("size", ((FileInfoBean) tb.getObject()).getFileSize());
				JSONObject json = new JSONObject();
				json.put("path", fj.toString());
				json.put("type", 2);
				fa.put(json);
			}
			L.d(TAG, "saveLocal fa=" + fa.toString());
			csb.setFilePaths(fa.toString());

			CommentSendService.getService(this).save(csb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除对话框
	 * 
	 * @param i
	 * @param str
	 * @param view
	 */
	private void deleteDialog(String filePath, final int position) {
		int index = filePath.lastIndexOf("/") + 1;
		String filename = filePath.substring(index);
		String msg = StringUtil.merge("是否删除照片", "“", filename, "”？");
		new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mImgGvAdapter_Choosed.remove(position);
						showPhotoSize(mImgGvAdapter_Choosed.getUrls());
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 选择图片
	 */
	private void choosePicture() {
		if (!ConstantUtil.IsCanUseSdCard()) {
			T.show(getApplicationContext(), "请检查SD卡");
			return;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"上传图片")
		// .setItems(new String[] { "本地上传", "拍照上传" },// , "取消上传"
		// new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(this, R.layout.choice_item,
								new String[] { "本地上传", "拍照上传" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Bundle bundle = new Bundle();
									if (null == mImgGvAdapter_Choosed)
										bundle.putStringArrayList("chooseds",
												new ArrayList<String>());
									else {
										bundle.putStringArrayList(
												"chooseds",
												(ArrayList<String>) mImgGvAdapter_Choosed
														.getUrls());
									}
									LogicUtil.enter(
											CreateGroupTopicActivity.this,
											ImageBucketActivity.class, bundle,
											ImageBucketActivity.CHOOSEIMG);
									break;
								case 1:
									if (mImgGvAdapter_Choosed.getUrls().size() < ImageChoosedGridViewAdapter
											.getCanChoosedNum()) {
										Intent intent1 = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										pictureName = System
												.currentTimeMillis() + ".jpg";
										File saveFile = new File(
												ConstantUtil.CAMERA_PATH);
										if (!saveFile.exists()) {
											saveFile.mkdirs();
										}

										intent1.putExtra(
												Media.ORIENTATION,
												0);
										intent1.putExtra(
												MediaStore.EXTRA_OUTPUT, Uri
														.fromFile(new File(
																saveFile,
																pictureName)));
										startActivityForResult(intent1,
												INSERT_PHOTO);
									} else {
										T.show(getApplicationContext(),
												"最多添加9张图");
										return;
									}
									break;
								case 2:
									dialog.dismiss();
									break;
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 开启获取位置线程
	 */
	// private void addLocation() {
	// new Thread() {
	// public void run() {
	// try {
	// try {
	// loc = sdkLocation();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// L.d(TAG, "addLocation loc=" + loc);
	// if (loc == null || loc.equals("")) {
	// try {
	// loc = googleCellLocation();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// L.i(TAG, "addLocation loc=" + loc);
	//
	// if (loc != null && !loc.equals("")) {
	// refHandler.sendEmptyMessage(1);
	// } else {
	// refHandler.sendEmptyMessage(0);
	// }
	// } catch (Exception e) {
	// refHandler.sendEmptyMessage(0);
	// e.printStackTrace();
	// }
	// };
	// }.start();
	// }

	/**
	 * sdk定位
	 * 
	 * @return
	 * @throws Exception
	 */
	// private String sdkLocation() throws Exception {
	// LocationInfo info = LocationInfo
	// .getInstance(CreateGroupTopicActivity.this);
	// mLon = info.getLongitude();
	// mlat = info.getLatitude();
	// L.d(TAG, "sdkLocation Longitude=" + mLon + ",Latitude=" + mlat);
	// if (mlat == 0.0 || mLon == 0.0) {
	// return null;
	// }
	// Geocoder coder = new Geocoder(this);
	// List<Address> ads = coder.getFromLocation(mlat, mLon, 1);
	// String myLocation = "";
	// if (ads != null && ads.size() > 0) {
	// Address ad = ads.get(0);
	//
	// String thoroughfare = ad.getThoroughfare();
	// L.i(TAG, "sdkLocation thoroughfare=" + thoroughfare);
	// if (thoroughfare != null && !"".equals(thoroughfare)) {
	// myLocation = thoroughfare;
	// } else {
	// String subLocality = ad.getSubLocality();
	// L.i(TAG, "sdkLocation subLocality=" + subLocality);
	// if (subLocality != null && !"".equals(subLocality)) {
	// myLocation = subLocality;
	// } else {
	// String locality = ad.getLocality();
	// L.i(TAG, "sdkLocation locality=" + locality);
	// if (locality != null && !"".equals(locality))
	// myLocation = locality;
	// }
	// }
	// }
	// return myLocation;
	// }

	/**
	 * Google基站定位
	 * 
	 * @throws Exception
	 */
	// private String googleCellLocation() throws Exception {
	// String myLocation = "";
	// String jsonStr = CellLocationInfo.getInstance(
	// CreateGroupTopicActivity.this).getLocationStr();
	// //
	// {"location":{"latitude":39.9731814,"longitude":116.3338698,"address":{"country":"中国","country_code":"CN","region":"北京市","city":"北京市","street":"中关村东路","street_number":""},"accuracy":1419.0},"access_token":"2:3Xr06vx-fzhzbW0f:spSx6vc4lieiF_iS"}
	// // http://ugc.map.soso.com/rgeoc/?lnglat=116.3338698,39.9731814&reqsrc=wb
	// JSONObject jsonObject = new JSONObject(jsonStr);
	// JSONObject locJsonObject = jsonObject.getJSONObject("location");
	// mLon = locJsonObject.getDouble("longitude");
	// mlat = locJsonObject.getDouble("latitude");
	// JSONObject addJsonObject = locJsonObject.getJSONObject("address");
	// try {
	// String city = addJsonObject.getString("city");
	// L.i(TAG, "googleCellLocation city=" + city);
	// if (city != null && !city.equals("")) {
	// myLocation += city;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// try {
	// String street = addJsonObject.getString("street");
	// L.i(TAG, "googleCellLocation street=" + street);
	// if (street != null && !street.equals("")) {
	// myLocation += street;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// try {
	// String street_number = addJsonObject.getString("street_number");
	// L.i(TAG, "googleCellLocation street_number=" + street_number);
	// if (street_number != null && !street_number.equals("")) {
	// myLocation += street_number;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// L.i(TAG, "googleCellLocation myLocation=" + myLocation);
	// if (myLocation == null || myLocation.equals("")) {
	// myLocation = addJsonObject.getString("region");
	// }
	// L.d(TAG, "googleCellLocation myLocation=" + myLocation);
	// return myLocation;
	// }

	// private Handler refHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// showLocLayout.setVisibility(View.GONE);
	// sToast("暂时无法获取您的位置信息");
	// break;
	// case 1:
	// showLoc_pb.setVisibility(View.GONE);
	// showLoc_iv.setVisibility(View.VISIBLE);
	// showLoc_tv.setText(loc);
	// break;
	// default:
	// break;
	// }
	// };
	// };

	/**
	 * 无输入
	 */
	private void hideKeyBoardFace() {
		try {
			faceView.setVisibility(View.GONE);
			face_iv.setImageResource(R.drawable.icon_face);
			BaseData.hideKeyBoard(this);
		} catch (Exception e) {
		}
	}

	/**
	 * 表情输入
	 */
	private void showFace() {
		topic_content.requestFocus();
		faceView.setVisibility(View.VISIBLE);
		if (faceView.getChildCount() == 0) {
			faceView.setFaces();
		}
		face_iv.setImageResource(R.drawable.keyboardbtn);
		BaseData.hideKeyBoard(this);
	}

	private void hideFace() {
		faceView.setVisibility(View.GONE);
		face_iv.setImageResource(R.drawable.icon_face);
	}

	/**
	 * 键盘输入
	 * 
	 * @param view
	 */
	private void showKeyBoard(View view) {
		faceView.setVisibility(View.GONE);
		face_iv.setImageResource(R.drawable.icon_face);
		view.requestFocus();
		BaseData.showKeyBoard(this, view);
	}

	/** 获取选中的圈子Id */
	private String[] getGroupIds() {
		String[] groupIds;
		groupIds = new String[choosedBean.size()];
		for (int i = 0; i < choosedBean.size(); i++) {
			groupIds[i] = choosedBean.get(i).getId();
		}
		// if (isSingleGroup) {
		// groupIds = new String[] { choosedBean.get(0).getId() };
		// } else {
		// groupIds = mHv_GroupChooseds.getGroupIds().toArray(new String[0]);
		// }
		return groupIds;
	}

	// /**
	// * ListView选圈子
	// *
	// * @param view
	// * @param position
	// */
	// private void listViewItemClick(final int position) {
	// L.i(TAG, "listViewItemClick size=" + matchGroupList.size()
	// + ",position=" + position);
	// GroupEntity entity = matchGroupList.get(position);
	// final String groupId = entity.getId();
	// if (-1 == isContain(groupId)) {
	// addView(entity);
	// choosedBean.add(entity);
	// }
	// }

	private int isContain(String id) {
		if (null == id) {
			return -1;
		}
		for (int i = 0; i < choosedBean.size(); i++) {
			GroupEntity entity = choosedBean.get(i);
			if (id.equals(entity.getId())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 添加View
	 * 
	 * @param position
	 * @param textView
	 * @param iv
	 */
	public void addView(final GroupEntity entity) {
		final String groupId = entity.getId();
		GroupNameView groupNameView = new GroupNameView(this, entity.getName());
		View gView = groupNameView.getView();
		gView.setTag(groupId);
		gView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeView(groupId);
				choosedBean.remove(entity);
			}
		});
		// groupView.addView(gView);
		//
		// add_et.setText("");
		// add_et.setHint("+添加更多交流圈…");
	}

	/**
	 * 删除View
	 * 
	 * @param position
	 * @param v
	 * @param iv
	 */
	public void removeView(String groupId) {
		int index = isContain(groupId);
		if (-1 == index) {
			return;
		}
		// groupView.removeViewAt(index);
		choosedBean.remove(index);
		// if (choosedBean.isEmpty()) {
		// add_et.setHint("输入交流圈名称…");
		// } else {
		// add_et.setHint("添加更多交流圈…");
		// }
	}

	@Override
	protected void onDestroy() {
		sendHandler.removeCallbacksAndMessages(null);
		if (null != pDialog && pDialog.isShowing()) {
			pDialog.cancel();
		}
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	// /**
	// * 获得交流圈列表
	// */
	// private void getCommGroupList() {
	// L.i(TAG, "getCommGroupList...");
	// if (getCommGroupListRun) {
	// return;
	// }
	// new Thread() {
	// public void run() {
	// getCommGroupListRun = true;
	// try {
	// try {
	// GroupListService service = GroupListService
	// .getService(CreateGroupTopicActivity.this);
	// groupEntityList = service.queryGroupLists();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (groupEntityList != null && groupEntityList.size() > 0) {
	// getGroupListHandler.sendEmptyMessage(1);
	// getCommGroupListRun = false;
	// return;
	// }
	// } catch (Exception e) {
	// getGroupListHandler.sendEmptyMessage(0);
	// e.printStackTrace();
	// }
	// getCommGroupListRun = false;
	// };
	// }.start();
	// }

	// private Handler getGroupListHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// updateUI(T.ErrStr);
	// break;
	// case 1:
	// // L.v(TAG, "getGroupListHandler size=" +
	// // groupEntityList.size());
	// // GroupNamesAdapter<GroupEntity> adapter = new
	// // GroupNamesAdapter<GroupEntity>(
	// // CreateGroupTopicActivity.this,
	// // android.R.layout.simple_dropdown_item_1line,
	// // groupEntityList);
	// // add_et.setAdapter(adapter);
	// break;
	// case 11:
	// updateUI("请先登录");
	// Intent intent = new Intent(CreateGroupTopicActivity.this,
	// LoginActivity.class);
	// intent.putExtra("CreateGroupTopic", true);
	// startActivity(intent);
	// break;
	// case 12:
	// updateUI("获取交流圈失败，您是否已加入或创建了交流圈");
	// break;
	// default:
	// break;
	// }
	// }
	// };

	/**
	 * 开启发圈博线程
	 * 
	 * @param groupIds
	 * @param title
	 * @param content
	 */
	private void sendGroupTopic(final String[] groupIds, final int cur) {
		hideKeyBoardFace();
		title = topic_title.getEditableText().toString();
		content = topic_content.getEditableText().toString();
		L.d(TAG, "onClick title=" + title + ",content=" + content);
		if (((content == null || "".endsWith(content.trim())) && (title == null || ""
				.endsWith(title.trim())))
				&& (mImgGvAdapter_Choosed.getUrls().isEmpty() && mAdditiveFileLvAdapter_Choosed
						.getBeans().isEmpty())) {
			T.show(this, "请先输入圈博内容");
			return;
		}

		content = StringUtil.trimInnerSpaceStr(content);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		TextView dTitle = new TextView(this);
		dTitle.setGravity(Gravity.CENTER);
		dTitle.setPadding(0, 15, 0, 0);
		dTitle.setTextSize(22);
		dTitle.setText("发送中...");
		builder.setCustomTitle(dTitle);
		dialogContent = new DialogView(this);
		String allInfo = null;
		int numFile = mAdditiveFileLvAdapter_Choosed.getCount();
		int numPhoto = mImgGvAdapter_Choosed.getUrls().size();
		if (numFile > 0 && numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片，" + numFile + " 个文件";
		} else if (numFile > 0) {
			allInfo = "共 " + numFile + " 个文件";
		} else if (numPhoto > 0) {
			allInfo = "共 " + numPhoto + " 张图片";
		} else {
			allInfo = null;
		}
		dialogContent.setAllFileInfo(allInfo);
		builder.setView(dialogContent);
		builder.setCancelable(false);
		builder.setNegativeButton("取消上传",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						pDialog.cancel();
						allowedPublish = false;
					}
				});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (pDialog.getButton(Dialog.BUTTON_NEGATIVE).getTag() != null) {
					finish();
				}
			}
		});
		pDialog = builder.show();
		new Thread() {
			public void run() {
				allowedPublish = true;
				try {
					if (content == null || "".equals(content)) {
						content = title;
					}

					// 发送失败后，如果重选 去除已上传成功的
					if (!photoTempsList.isEmpty()) {
						for (Entry<String, String> entry : photoTempsList
								.entrySet()) {
							if (!mImgGvAdapter_Choosed.getUrls().contains(
									entry.getKey())) {
								photoTempsList.remove(entry.getKey());
							}
						}
					}

					// 发送失败后，如果重选 去除已上传成功的
					if (!fileTempsList.isEmpty()) {
						for (Entry<String, String> entry : fileTempsList
								.entrySet()) {
							if (mAdditiveFileLvAdapter_Choosed.contains(entry
									.getKey()) == -1) {
								fileTempsList.remove(entry.getKey());
							}
						}
					}

					int ffsize = fileTempsList.size();
					int ppsize = photoTempsList.size();
					String oriToast = null;
					if (ppsize > 0 && ffsize > 0) {
						oriToast = "已上传 " + ppsize + " 张图片，" + ffsize + " 个文件";
					} else if (ffsize > 0) {
						oriToast = "已上传 " + ffsize + " 个文件";
					} else if (ppsize > 0) {
						oriToast = "已上传 " + ppsize + " 张图片";
					}
					if (null != oriToast) {
						HandlerUtil.sendMsgToHandler(sendHandler, 4, oriToast,
								0);
					}

					mAdditiveFileLvAdapter_Choosed.classify();
					List<String> id_CloudFile = new ArrayList<String>();
					for (Object file : mAdditiveFileLvAdapter_Choosed
							.getCloudFile(false)) {
						int id = new TransitionBean(file).getId();
						id_CloudFile.add(String.valueOf(id));
						L.d(TAG, "cloud file:" + id);
					}
					for (String path : mImgGvAdapter_Choosed.getUrls()) {
						if (!allowedPublish || num != cur) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !photoTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size()
										+ id_CloudFile.size();
								int psize = photoTempsList.size();
								if (fsize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}

								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + psize),
												" 张图片..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2, path, 0, -1));
							}
							startThread(path, true, toast, cur);
						}
					}
					for (Object file : mAdditiveFileLvAdapter_Choosed
							.getAdditiveFile(false)) {
						String path = new TransitionBean(file).getPath();
						if (!allowedPublish || num != cur) {
							break;
						}
						L.d(TAG, "imgPath" + path);
						if (null != path && !"".equals(path)
						// 发送失败后，如果重传 去除已上传成功的
								&& !fileTempsList.containsKey(path)) {
							String toast = null;
							if (null != pDialog && pDialog.isShowing()) {
								int fsize = fileTempsList.size()
										+ id_CloudFile.size();
								int psize = photoTempsList.size();
								if (psize == 0) {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 个文件");
								} else {
									toast = StringUtil.merge("已上传 ",
											(1 + psize), " 张图片，", (fsize),
											" 个文件");
								}
								HandlerUtil.sendMsgToHandler(sendHandler, 3,
										StringUtil.merge("正在上传第 ", (1 + fsize),
												" 个文件..."), 0);
								HandlerUtil.sendMsgToHandler(sendHandler,
										HandlerUtil.getMessage(2,
												new File(path), 0, 1));
							}
							startThread(path, false, toast, cur);
						}
					}
					if (!allowedPublish || num != cur) {
						return;
					}
					HandlerUtil.sendMsgToHandler(sendHandler, 5, "正在发圈博...",
							View.GONE);

					int i = photoTempsList.size();
					L.d(TAG, "photoTempsList.size" + photoTempsList.size());
					String[] photoTemps = new String[i];
					for (Entry<String, String> entry : photoTempsList
							.entrySet()) {
						photoTemps[i - 1] = entry.getValue();
						i--;
					}
					i = fileTempsList.size();
					String[] fileTemps = new String[i];
					for (Entry<String, String> entry : fileTempsList.entrySet()) {
						fileTemps[i - 1] = entry.getValue();
						i--;
					}
					String[] fileId = id_CloudFile.toArray(new String[0]);
					id_CloudFile.clear();
					id_CloudFile = null;
					String tags = "";
					if (null != atMap && atMap.size() > 0) {
						for (Iterator<?> iterator = atMap.keySet().iterator(); iterator
								.hasNext();) {
							String id = (String) iterator.next();
							if (!content.contains(atMap.get(id))) {
								atMap.remove(id);
							}
						}

						int size = atMap.size();
						if (size > 0) {
							for (Iterator<?> iterator = atMap.keySet()
									.iterator(); iterator.hasNext();) {
								String id = (String) iterator.next();
								tags += id + ",";
							}
							tags = tags.substring(0, tags.length() - 1);
						}
					}

					if (content == null || content.equals("")) {
						if (photoTemps.length > 0) {
							content = "【分享照片】";
							if (checkBox.isChecked())
								content += "(原图)";
						} else if (fileTemps.length > 0
								|| (fileId != null && fileId.length > 0)) {
							content = "【分享文件】";
						}
					}

					content = content.replaceAll("\n", "<br />");

					MCResult response = APIRequestServers.createGroupTopic(
							App.app, groupIds, fileTemps, photoTemps, fileId,
							title, content, tags, null, null);
					int resultCode = response.getResultCode();
					L.i(TAG, "sendGroupTopic resultCode=" + resultCode);
					if (!allowedPublish || num != cur) {
						return;
					}
					if (resultCode == 1) {
						sendHandler.sendEmptyMessage(1);
					} else {
						sendHandler.sendEmptyMessage(0);
					}
					// }
				} catch (SocketTimeoutException e) {
					sendHandler.sendEmptyMessage(10);
					e.printStackTrace();
				} catch (Exception e) {
					sendHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void startThread(String filepath, boolean isPhoto,
			String finishToast, int cur) {
		String url = URLProperties.MEMBER_JSON;
		String method = "";
		// String name = null;
		File file = null;
		if (isPhoto) {
			file = new File(ThumbnailImgUtil.getData(filepath));
			L.d(TAG, "filepath:" + filepath);
			method = APIMethodName.UPLOAD_PHOTO;
			file = FileUtil.ChangeImage(file, checkBox.isChecked());
		} else {
			file = new File(filepath);
			method = APIMethodName.UPLOAD_FILE;
		}
		L.d(TAG, "filepath:" + file.getAbsolutePath());
		String params = new UploadFileParams(App.app, method, file.getName(),
				null).getParams();
		L.d(TAG, "startThread url=" + url + "?" + params);
		try {
			String result = httpUpload(url + "?" + params, file, isPhoto,
					finishToast, cur);
			L.d(TAG, "startThread result=" + result);
			if (!allowedPublish || cur != num) {
				return;
			}
			if (result != null) {
				final JSONObject object = new JSONObject(result);
				if (object.getInt("resultCode") == 1) {
					String temps = object.getString("result");
					if (isPhoto) {
						photoTempsList.put(filepath, temps);
						try {
							String message = object.getString("resultMessage");
							JSONObject jsonObject = new JSONObject(message);
							String path = jsonObject.getString("fileUrl")
									+ jsonObject.getString("filePath");
							path = path.replace("com:80", "com");
//							path = path.replace("http://", "https://");
							path = ThumbnailImageUrl.getThumbnailImageUrl(path,
									ImageSizeEnum.THREE_HUNDRED);
							L.i(TAG, "path=" + path);
							MyFinalBitmap.cacheDiscImage(this, path, file,
									new ImageSize(300, 300));
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						fileTempsList.put(file.getPath(), temps);
					}
				} else {
					onUploadFailed(file);
				}
			} else {
				onUploadFailed(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			onUploadFailed(file);
		}

		// if (tempFile != null && tempFile.exists()) {
		// try {
		// Runtime.getRuntime().exec("rm -r " + file);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}

	private void onUploadFailed(File file) {
		allowedPublish = false;
		HandlerUtil.sendMsgToHandler(sendHandler, 9, file.getName()
				+ "上传失败，请重新上传！", 0);
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file, boolean isPhoto,
			String finishToast, int cur) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = HttpRequestServers
					.getHttpURLConnection(url);

			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: application/octet-stream" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			L.d(TAG, "path=" + file.getAbsolutePath());
			fis = new FileInputStream(file);
			L.d(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				if (!allowedPublish || num != cur) {
					return null;
				}
				dos.write(buffer, 0, count);
				uploadSize += count;
				progress = (int) (uploadSize * 100 / totalLength);
				if (progress > 0) {
					L.d(TAG, "httpUpload uploadSize=" + uploadSize
							+ ",progress=" + progress);
				}

				HandlerUtil.sendMsgToHandler(sendHandler, 2, null, progress);
				if (progress >= 100) {
					HandlerUtil.sendMsgToHandler(sendHandler,
							HandlerUtil.getMessage(4, finishToast, 0, 0));
				}
			}

			dos.writeBytes(reqEnder);
			dos.flush();

			L.d(TAG, "httpUpload dos.size=" + dos.size());
			result = StreamUtil.readData(httpURLConnection.getInputStream());
			L.d(TAG, "httpUpload result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	private Handler sendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				saveLocal();
				showTip("发送失败，已保存到草稿箱");
				CreateGroupTopicActivity.this.finish();
				break;
			case 10:
				// updateUI(T.TimeOutStr);
				saveLocal();
				showTip("发送失败，已保存到草稿箱");
				CreateGroupTopicActivity.this.finish();
				break;
			case 1:
				InfoWallActivity.isNeedRefresh = true;
				// updateUI("已发布！");
				LogicUtil.enter(CreateGroupTopicActivity.this,
						InfoWallActivity.class, null, true);
				break;
			case 2:
				if (null != dialogContent) {
					dialogContent.updatePBar(msg.arg1);
					if (null != msg.obj && 0 == msg.arg1) {
						if (1 == msg.arg2) { // 文件
							dialogContent.setFileIcon((File) msg.obj);
						} else if (-1 == msg.arg2) {
							dialogContent.setIcon((String) msg.obj);
						}
					}
				}
				break;

			case 3:
				if (null != dialogContent) {
					dialogContent.startNewFile((String) msg.obj);
				}
				break;

			case 4:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
				}
				break;
			case 5:
				if (null != dialogContent) {
					dialogContent.finishFile((String) msg.obj);
					dialogContent.setPublishProgress(msg.arg1);
					dialogContent.showDialogBar();
				}

				if (msg.arg1 == View.GONE) {
					Button b = pDialog.getButton(Dialog.BUTTON_NEGATIVE);
					b.setTag(new Object());
					b.setText("后台发送");
				}
				break;
			case 9:
				showTip((String) msg.obj);
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.cancel();
				}
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 加载完成提醒
	 * 
	 * @param msg
	 */
	private void updateUI(String msg) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
		T.show(this, msg);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			backPrompt();
			return true;
		case R.id.action_send:
			sendTopic();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, topic_content, text, resId);
	}

	private void showPhoto(String url) {
		Bundle bundle = new Bundle();
		String[] bean = ThumbnailImgUtil.split(url);
		bundle.putString("url", bean[0]);
		bundle.putString("id", bean[1]);
		L.d(TAG, "url:" + ThumbnailImgUtil.getData(url));
		// bundle.putString("oUrl", "");
		bundle.putInt("index", 1);
		bundle.putString("type", "type_topic");
		LogicUtil.enter(CreateGroupTopicActivity.this,
				PhotoGalleryActivity.class, bundle,
				PhotoGalleryActivity.IDDELETE);
	}

	private void changeImg(String path) {
		L.i(TAG, "image=" + path);
		List<String> urls = new ArrayList<String>();
		urls.add(path + ThumbnailImgUtil.SUFFIX);
		spdDialog.showProgressDialog("正在获取图片...");
		new RequestTask(App.app, urls, 0).execute("");
	}

	class RequestTask extends AsyncTask<Object, Integer, List<String>> {
		private Context mContext;
		private List<String> mUrls;
		private int mType;

		/**
		 * 
		 * @param context
		 * @param urls
		 * @param type
		 *            0 is from camera 1 is from bucket
		 */
		public RequestTask(Context context, List<String> urls, int type) {
			mContext = context;
			mUrls = urls;
			mType = type;
		}

		@Override
		protected List<String> doInBackground(Object... params) {
			String[] urls = mUrls.toArray(new String[] {});
			mUrls.removeAll(mUrls);
			L.d(TAG, "mUrls.size:" + mUrls.size());
			L.d(TAG, "urls.size:" + urls.length);
			for (String url : urls) {
				if (ThumbnailImgUtil.isUrlEmpty(url)) {
					String[] bean = null;
					do {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						bean = ThumbnailImgUtil.split(mContext, url);
						L.d(TAG, "data:" + bean[0] + " id:" + bean[1]);
					} while (ThumbnailImgUtil.isIdEmpty(bean[1]));
					url = bean[0] + ThumbnailImgUtil.MARK + bean[1];
					mUrls.add(url);
				} else {
					mUrls.add(url);
				}
			}
			return mUrls;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			spdDialog.cancelProgressDialog(null);
			switch (mType) {
			case 0:
				addPhoto(result.get(0));
				break;
			case 1:
				addPhoto(result);
				break;
			}
		}

	}

	public void showImageBox() {
		if (mImgGvAdapter_Choosed.isInit())
			mVs_ImgGv.inflate();
	}

	public void showFileBox() {
		if (mAdditiveFileLvAdapter_Choosed.isInit())
			mVs_FileLv.inflate();
	}

	private void openFile(final String filePath, final int fileId,
			final long fileLength) {
		final String tempName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(this, myFile);
			return;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			T.show(App.app, R.string.downloading);
			return;
		} else {
			spdDialog.showProgressDialog("正在处理中...");
			final Handler openHandler = new Handler() {
				public void handleMessage(Message msg) {
					spdDialog.cancelProgressDialog(null);
					switch (msg.what) {
					case 0:
						T.show(App.app, T.ErrStr);
						break;
					case 1:
						break;
					default:
						break;
					}
				};
			};
			ConstantUtil.downloadingList.add(tempName);
			new Thread() {
				public void run() {
					try {
						MCResult mcResult = APIFileRequestServers.getFilePath(
								App.app, fileId);
						if (mcResult != null && mcResult.getResultCode() == 1) {
							String fileUrl = mcResult.getResult().toString();
							new DownLoadFileThread(
									CreateGroupTopicActivity.this, fileUrl,
									fileLength, tempName, true).start();
							openHandler.sendEmptyMessage(1);
						} else {
							ConstantUtil.downloadingList.remove(tempName);
							openHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ConstantUtil.downloadingList.remove(tempName);
						openHandler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	public void onPanelClosed(Panel panel) {
		mImg_Arrow.setImageResource(R.drawable.arrow_down_0);
	}

	@Override
	public void onPanelOpened(Panel panel) {
		mImg_Arrow.setImageResource(R.drawable.arrow_up_0);
		hideKeyBoardFace();
	}

}
