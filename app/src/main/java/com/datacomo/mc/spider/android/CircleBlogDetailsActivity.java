package com.datacomo.mc.spider.android;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.AdditiveFileListAdapter;
import com.datacomo.mc.spider.android.adapter.CommentAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.CommentSendBean;
import com.datacomo.mc.spider.android.bean.TransitionByGroupBlogBean;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.FileUrlService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.QuuBoCommentService;
import com.datacomo.mc.spider.android.db.QuuBoInfoService;
import com.datacomo.mc.spider.android.db.QuuBoPraiseService;
import com.datacomo.mc.spider.android.db.QuuBoVisitService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupTopicBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.ResourceVisitBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceCommentBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceVisitBean;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.url.URLProperties;
import com.datacomo.mc.spider.android.util.AnimationFrameUtil;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FaceUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LinkDealUtil;
import com.datacomo.mc.spider.android.util.LoadSdCard;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.StringUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.TypeUtil;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.FileListView;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class CircleBlogDetailsActivity extends BasicActionBarActivity implements
		HandlerNumberUtil, OnFaceChosenListner, OnLoadMoreListener {
	private static final String TAG = "CircleBlogDetailsActivity";

	private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_CIRCLEBLOGDETAILSACTIVITY;
	private int num_Total, num_Praise, num_Visit, id_Member, id_Default_Member,
			id_Group, id_Quubo;
	// private String groupName;
	private int num_Details = 99;
	private HashMap<String, String> receiveReplyIds;

	private String info_comment, type_Object, type_From, name_Member,
			name_Group;

	private String type_Resource = TypeUtil.type_Resource;
	private String type_ResultMessage = TypeUtil.type_ResultMessage;
	private String type_Praise = TypeUtil.type_Praise;
	private String type_Visit = TypeUtil.type_Visit;
	private String name_Reply;

	private boolean hasPraise, hasDelete, isOpenPage, canSelectAll;
	private int openPageId = 0;
	// isReply;
	// private boolean flag_friendSharing;
	// private boolean flag_groupSharing;
	private MapResourceGreatBean bean_MapResourceGreat;
	private MapResourceVisitBean bean_MapResourceVisit;

	// private HashMap<String, Integer> map_PathId_Head;
	private List<ImageView> img_Praises, img_Visitors;
	// private List<String> urls; // 保存图片链接
	private List<ObjectInfoBean> beans_Info; // 保存图片链接
	private String shareTopic, shareImage, shareImageUrl;
	// 声明引用类
	private CommentAdapter adapter_Comment;
	// private ProgressDialog pDialog_CircleBlog;
	// private ButtomMenuAnimation animation_ButtomMenu;
	// 声明组件
	private EditText edit_CommentInfo;
	private WebView web_Info_Blog;
	private ImageView btn_Release;
	private FacesView faceView;
	private LinearLayout llayout_Foot;
	private LinearLayout llayout_Header, llayout_AddByImg;
	// llayout_AddByFile llayout_PersonHead

	private ViewStub mVs_FileLv;
	private FileListView mAdditiveFileLv_ChoosedImg;
	private AdditiveFileListAdapter mAdditiveFileLvAdapter_Choosed;

	private CommentRequsetTask task;
	// ,llayout_ButtomMenu;
	// private RelativeLayout rlayout_Header;
	private RefreshListView lv_Comment;
	private ImageView img_Headimg_Member, img_Headimg_next, img_Face, shareImg,
			iv_praise;
	private TextView txt_Num_Praise, txt_Num_Visitor, txt_Name_Member,
			txt_Name_group, txt_BaseInfo_Blog, txt_BlogTitle;
	// private TextView txt_Title_Blog;
	// private TextView txt_Title;

	private LinearLayout ll_visitor, ll_praise, ll_visitor_show,
			ll_praise_show;
	private View v_visitor, v_praise;
	private TextView tv_visitor, tv_praise;

	public static CircleBlogDetailsActivity circleBlogDetailsActivity;
	private GroupListService groupService;

	public boolean isSelection = false;

	/** 是否有权限操作 */
	private boolean hasAuthority = true;
	/** 是否收藏过 */
	private boolean hasCollect;

	private String strType = "资源";

	// private String filePath;
	// private String imgName;
	private HashMap<Integer, OnMorePreDrawListener> mapOnMorePreDrawListener;
	// private ImageView img_Img_Blog;

	private int bottom, dex, hSize;
	private boolean mIsMore;

	private boolean isInfoRefresh;
	private ResourceBean bean_Resource;
	private ResourceTrendBean tBean;
	private int position;

	private boolean hasMoreView = true;

	@Override
	protected void onDestroy() {
		handler_CircleBlog.removeCallbacksAndMessages(null);
		infoHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "15");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.circleblogdetails);
		initView();
		initData();

		groupService = GroupListService.getService(App.app);
		circleBlogDetailsActivity = this;

		edit_CommentInfo.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// L.i(TAG, "setOnFocusChangeListener hasFocus=" + hasFocus);
				if (hasFocus) {
					showKeyBoard(edit_CommentInfo);
					new Thread() {
						public void run() {
							try {
								sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler_CircleBlog.sendEmptyMessage(200);
						};
					}.start();
				}
			}
		});

		// web_Info_Blog.setOnTouchListener(new OnTouchListener() {
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_UP) {
		// // 为false表示父类的焦点可用；
		// lv_Comment.requestDisallowInterceptTouchEvent(false);
		// } else {
		// // 为true表示父类的焦点不可用；
		// lv_Comment.requestDisallowInterceptTouchEvent(true);
		// }
		// return false;
		// }
		// });

	}

	/**
	 * 初始化组件
	 **/
	private void initView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		llayout_Header = (LinearLayout) inflater.inflate(
				R.layout.circleblogdetailsheadform, null);
		img_Headimg_Member = (ImageView) llayout_Header
				.findViewById(R.id.head_img);
		img_Headimg_next = (ImageView) llayout_Header
				.findViewById(R.id.circleblogdetails_imageview);
		txt_Name_group = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetails_group);
		txt_Name_Member = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetails_name);
		txt_BaseInfo_Blog = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetails_baseinfo);
		// llayout_PersonHead = (LinearLayout) llayout_Header
		// .findViewById(R.id.circleblogdetails_head);
		txt_BlogTitle = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_blogtitle);
		web_Info_Blog = (WebView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_bloginfo);
		llayout_Foot = (LinearLayout) inflater
				.inflate(R.layout.form_foot, null);
		((TextView) llayout_Foot.findViewById(R.id.form_foot_txt))
				.setText("没有更多评论");
		// web_Info_Blog.setBackgroundColor(0);
		web_Info_Blog.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		WebSettings webSettings = web_Info_Blog.getSettings();
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setDefaultFontSize(16);
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// webSettings.setBuiltInZoomControls(true); // 显示放大缩小
		// webSettings.setSupportZoom(true); // 可以缩放

		web_Info_Blog.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (downLoadNoteFile(url)) {
					return true;
				}
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		llayout_AddByImg = (LinearLayout) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_addimglayout);
		// llayout_AddByFile = (LinearLayout) llayout_Header
		// .findViewById(R.id.circleblogdetailsheadform_addfilelayout);

		mAdditiveFileLvAdapter_Choosed = new AdditiveFileListAdapter(this,
				new ArrayList<Object>());
		mVs_FileLv = (ViewStub) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_filelist);
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
								openFile((ObjectInfoBean) parent
										.getItemAtPosition(position));
							}
						});
				mAdditiveFileLv_ChoosedImg
						.setAdapter(mAdditiveFileLvAdapter_Choosed);
			}
		});

		ll_visitor_show = (LinearLayout) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor);
		ll_praise_show = (LinearLayout) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise);

		ll_visitor = (LinearLayout) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_layout);
		ll_praise = (LinearLayout) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_layout);
		v_visitor = llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_bg);
		v_praise = llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_bg);
		tv_visitor = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_tv);
		tv_praise = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_tv);
		ll_visitor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v_visitor.setBackgroundResource(R.drawable.quubo_1);
				v_praise.setBackgroundResource(R.drawable.quubo_2);
				tv_visitor.setTextColor(getResources()
						.getColor(R.color.quubo_1));
				tv_praise
						.setTextColor(getResources().getColor(R.color.quubo_2));
				ll_visitor_show.setVisibility(View.VISIBLE);
				ll_praise_show.setVisibility(View.GONE);
			}
		});
		ll_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v_visitor.setBackgroundResource(R.drawable.quubo_2);
				v_praise.setBackgroundResource(R.drawable.quubo_1);
				tv_visitor.setTextColor(getResources()
						.getColor(R.color.quubo_2));
				tv_praise
						.setTextColor(getResources().getColor(R.color.quubo_1));
				ll_visitor_show.setVisibility(View.GONE);
				ll_praise_show.setVisibility(View.VISIBLE);
			}
		});

		txt_Num_Praise = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praisenum);
		llayout_Header.findViewById(R.id.imageview1).setOnClickListener(this);
		txt_Num_Visitor = (TextView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitornum);
		llayout_Header.findViewById(R.id.imageview2).setOnClickListener(this);

		Drawable drawable = getResources().getDrawable(R.drawable.visitor);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, 1 * dw / 2, 1 * dh / 2);
		txt_Num_Visitor.setCompoundDrawables(drawable, null, null, null);
		txt_Num_Visitor.setCompoundDrawablePadding(5);

		drawable = getBroudDrawable(R.drawable.icon_great);
		txt_Num_Praise.setCompoundDrawables(drawable, null, null, null);
		txt_Num_Praise.setCompoundDrawablePadding(5);

		img_Praises = new ArrayList<ImageView>();
		img_Visitors = new ArrayList<ImageView>();
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img1));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img2));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img3));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img4));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img5));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img6));
		img_Praises.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_praise_img7));

		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img1));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img2));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img3));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img4));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img5));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img6));
		img_Visitors.add((ImageView) llayout_Header
				.findViewById(R.id.circleblogdetailsheadform_visitor_img7));

		lv_Comment = (RefreshListView) findViewById(R.id.circleblogdetails_list);
		lv_Comment.addFooter(llayout_Foot, true);
		edit_CommentInfo = (EditText) findViewById(R.id.comment_edit);
		btn_Release = (ImageView) findViewById(R.id.comment_release);
		iv_praise = (ImageView) findViewById(R.id.comment_praises);
		img_Face = (ImageView) findViewById(R.id.comment_face);
		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);
		edit_CommentInfo.getTop();

		iv_praise.setOnClickListener(this);
	}

	public void showFileBox() {
		if (mAdditiveFileLvAdapter_Choosed.isInit())
			mVs_FileLv.inflate();
	}

	private void openFile(final ObjectInfoBean bean_ObjectInfo) {
		final int gId = bean_ObjectInfo.getGroupId();
		final int fId = bean_ObjectInfo.getObjectId();
		String fileUrl = FileUrlService.getService(
				CircleBlogDetailsActivity.this).queryFileUrl(gId, fId);
		String tempName = null;
		if (fileUrl != null && fileUrl.contains("/"))
			tempName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
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
						showTip(CircleBlogDetailsActivity.this.getResources()
								.getString(R.string.downloading));
						break;
					case 2:
						T.show(App.app, "您不是圈子的成员，没有权限下载");
						break;
					default:
						break;
					}
				};
			};
			ConstantUtil.downloadingList.add(tempName);
			new Thread() {
				public void run() {
					String fileUrl = null;
					try {
						MCResult mcResult = APIRequestServers.fileDownloadPath(
								App.app, gId + "", fId + "");
						if (mcResult != null && mcResult.getResultCode() == 1) {
							fileUrl = mcResult.getResult().toString();
							if (fileUrl == null || "".equals(fileUrl)) {
								openHandler.sendEmptyMessage(0);
								return;
							}

							if ("2".equals(fileUrl)) {
								openHandler.sendEmptyMessage(2);
								return;
							} else {
								FileUrlService.getService(
										CircleBlogDetailsActivity.this).save(
										gId, fId, fileUrl);
							}
						} else {
							openHandler.sendEmptyMessage(0);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						openHandler.sendEmptyMessage(0);
						return;
					}

					if (fileUrl != null && !"".equals(fileUrl)) {
						final String tempName = fileUrl.substring(fileUrl
								.lastIndexOf("/") + 1);
						File myFile = new File(ConstantUtil.CLOUD_PATH
								+ tempName);
						if (myFile != null && myFile.exists()) {
							new FileUtil().openFile(
									CircleBlogDetailsActivity.this, myFile);
							openHandler.sendEmptyMessage(3);
							return;
						} else if (ConstantUtil.downloadingList
								.contains(tempName)) {
							openHandler.sendEmptyMessage(1);
							return;
						}

						ConstantUtil.downloadingList.add(tempName);
						new DownLoadFileThread(CircleBlogDetailsActivity.this,
								fileUrl, bean_ObjectInfo.getObjectSize(),
								bean_ObjectInfo.getObjectName(), true).start();
						openHandler.sendEmptyMessage(3);
					}
				};
			}.start();
		}
	}

	/**
	 * 下载笔记附件
	 * 
	 * @param url
	 * @return
	 */
	private boolean downLoadNoteFile(String url) {
		String downloadUrl = "";
		String name = "";
		if (url != null && url.contains("downloadDiaryAttachment.do")) {
			try {
				String str2 = url.substring(url.indexOf("?") + 1);
				String[] strs = str2.split("&");
				downloadUrl = URLProperties.FILE_URL
						+ strs[0].substring(strs[0].indexOf("=") + 1);
				name = strs[1].substring(strs[1].indexOf("=") + 1);
				name = URLDecoder.decode(name, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}

		if ("".equals(downloadUrl))
			return false;

		final String tempName = downloadUrl.substring(downloadUrl
				.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			new FileUtil().openFile(this, myFile);
			return true;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			showTip(this.getResources().getString(R.string.downloading));
			return true;
		}

		ConstantUtil.downloadingList.add(tempName);
		new DownLoadFileThread(this, downloadUrl, 0, name, true).start();
		return true;
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
		// L.d(TAG, "isRefresh:" + isRefresh + " location:" + location
		// + " startRecord:" + startRecord + " maxResults:" + maxResults);
		task = new CommentRequsetTask(isRefresh, location);
		task.execute(String.valueOf(id_Group), String.valueOf(id_Quubo),
				type_Object, String.valueOf(startRecord),
				String.valueOf(maxResults));
		// L.d(TAG, "!(location == 0 && adapter_Comment.getCount() > 0):"
		// + !(location == 0 && adapter_Comment.getCount() > 0));
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
	 * 初始化数据
	 **/
	private void initData() {
		// animation_ButtomMenu = new ButtomMenuAnimation(false,
		// llayout_ButtomMenu);
		Bundle bundle = getIntentMsg();
		type_From = bundle.getString("type_From");
		id_Default_Member = GetDbInfoUtil.getMemberId(App.app);
		final boolean isCooperationLeaguer = bundle.getBoolean(
				"isCooperationLeaguer", false);
		if (type_Resource.equals(type_From)) {
			bean_Resource = (ResourceBean) bundle.getSerializable("info");
			position = bundle.getInt("position");
			// hasAuthority = bean_Resource.isHasAuthority();
			if (bean_Resource != null) {
				hasCollect = bean_Resource.isHasCollect();
				id_Member = bean_Resource.getSendMemberId();
				id_Group = bean_Resource.getObjectOwnerId();
				name_Group = bean_Resource.getObjOwnerMemberInfo().getName();
				L.d(TAG, "initData name_Group=" + name_Group);
				setDetailsInfo(bean_Resource, type_From);
			}
		} else if (type_ResultMessage.equals(type_From)) {
			final QuuboInfoBean bean_QuuboInfo = (QuuboInfoBean) bundle
					.getSerializable("info");
			type_Object = bundle.getString("type_Object");
			setDetailsInfo(bean_QuuboInfo, type_From);
			hasCollect = bean_QuuboInfo.isHasCollect();
			id_Member = bean_QuuboInfo.getSendMemberId();
			id_Group = bean_QuuboInfo.getGroupId();
			name_Group = bean_QuuboInfo.getGroupInfoBean().getName();
			L.i(TAG, "initData groupName=" + name_Group);
		}

		if (type_Object != null) {
			if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
					|| "OBJ_MEMBER_RES_MOOD".equals(type_Object)
					|| type_Object.startsWith("OBJ_OPEN_PAGE_")
					|| isCooperationLeaguer) {
				hasAuthority = true;
			}

			if (type_Object.startsWith("OBJ_OPEN_PAGE_")) {
				isOpenPage = true;
			}
		}

		if (id_Default_Member == id_Member) {
			hasDelete = true;
			hasAuthority = true;
		}
		new Thread() {
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.groupInfo(App.app,
							id_Group + "");
					if (mcResult != null && 1 == mcResult.getResultCode()) {
						GroupBean bean = (GroupBean) mcResult.getResult();
						String leaguerStatus = bean.getJoinGroupStatus();
						// L.i(TAG, "initData leaguerStatus=" + leaguerStatus);
						if ("6".equals(leaguerStatus)
								|| "NO_RELATION".equals(leaguerStatus)
								|| "COOPERATION_LEAGUER".equals(leaguerStatus))
							if ("OBJ_MEMBER_RES_LEAVEMESSAGE"
									.equals(type_Object)
									|| "OBJ_MEMBER_RES_MOOD"
											.equals(type_Object)
									|| type_Object.startsWith("OBJ_OPEN_PAGE_")
									|| isCooperationLeaguer
									|| id_Default_Member == id_Member) {
							} else {
								hasAuthority = false;
							}
						if ("1".equals(leaguerStatus)
								|| "2".equals(leaguerStatus)
								|| "GROUP_OWNER".equals(leaguerStatus)
								|| "GROUP_MANAGER".equals(leaguerStatus)) {
							hasDelete = true;
							adapter_Comment.setHasDelete(hasDelete);
							sendHandlerStrMsg(2000);

							int groupType = bean.getGroupType();
							// L.i(TAG, "initData groupType=" + groupType);
							if (groupType != 1) {
								canSelectAll = true;
							}

							MCResult mc = APIRequestServers.existOpenPage(
									App.app, id_Group + "");
							if (mc != null && 1 == mc.getResultCode()) {
								JSONObject json = new JSONObject(mc.getResult()
										.toString());
								// L.i(TAG, "initData json=" + json);
								int RESULT = json.getInt("RESULT");
								if (RESULT == 1 || RESULT == 6) {
									openPageId = json.getInt("OPEN_PAGE_ID");
								}
							}
						}
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
		edit_CommentInfo.setOnClickListener(this);
		// 发评论
		btn_Release.setOnClickListener(this);
		// 表情、键盘
		img_Face.setOnClickListener(this);
		// 赞
		txt_Num_Praise.setOnClickListener(this);
		// 查看浏览列表
		txt_Num_Visitor.setOnClickListener(this);
		// 发送者头像
		img_Headimg_Member.setOnClickListener(this);
		// 圈博列表
		img_Headimg_next.setOnClickListener(this);
		txt_Name_group.setOnClickListener(this);
		lv_Comment.setonLoadMoreListener(this);
		lv_Comment.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadInfo(true, -1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.comment_praises:
			L.d(TAG, "onClick hasPraise=" + hasPraise);
			if (hasPraise) {
				showTip("已赞过");
			} else {
				praiseResource();
			}
			break;
		case R.id.comment_edit:
			showKeyBoard(edit_CommentInfo);
			new Thread() {
				public void run() {
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler_CircleBlog.sendEmptyMessage(200);
				};
			}.start();
			break;
		case R.id.comment_release:// 发评论
			hideKeyBoardFace();
			String comment_str = edit_CommentInfo.getText().toString();
			if (comment_str != null && !"".equals(comment_str.trim())) {
				comment_str = StringUtil.trimInnerSpaceStr(comment_str);
				if (null != name_Reply && comment_str.startsWith(name_Reply)) {
					if (comment_str.length() > name_Reply.length()) {
						name_Reply = null;
						if (!comment_str.equals(info_comment)) {
							info_comment = comment_str;
							releaseComment(info_comment);
						}
					} else {
						showKeyBoard(edit_CommentInfo);
						showTip("回复内容不能为空");
					}
				} else {
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
		case R.id.comment_face:// 表情、键盘
			if (faceView.isShown()) {
				showKeyBoard(edit_CommentInfo);
			} else {
				edit_CommentInfo.requestFocus();
				showFace();
			}
			break;
		case R.id.imageview1:
			if (num_Praise > 0) {
				startActivityByMember(Type.PRAISE, num_Praise);
			}
			break;
		case R.id.circleblogdetailsheadform_praisenum:// 赞
			// L.d(TAG, "hasPraise=" + hasPraise);
			// if (!hasPraise) {
			// praiseResource();
			// } else {
			if (num_Praise > 0) {
				startActivityByMember(Type.PRAISE, num_Praise);
			}
			// }
			break;
		case R.id.imageview2:
		case R.id.circleblogdetailsheadform_visitornum:// 查看浏览列表
			if (num_Visit > 0)
				startActivityByMember(Type.VISIT, num_Visit);
			break;
		case R.id.head_img:// 发送者头像
			try {
				String temp = v.getTag().toString();
				startActivityByMemberId(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.circleblogdetails_imageview:// 圈博列表
			if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
					|| "OBJ_MEMBER_RES_MOOD".equals(type_Object)
					|| (type_Object != null && type_Object
							.startsWith("OBJ_OPEN_PAGE_"))) {
				return;
			}
			try {
				String[] temps = v.getTag().toString().split("@");
				Bundle bundle = new Bundle();
				bundle.putString("name_Person", temps[0]);
				bundle.putString("path_Head", temps[1]);
				bundle.putInt("id_Member", id_Member);
				bundle.putInt("id_Group", id_Group);
				bundle.putString("groupName", name_Group);
				LogicUtil.enter(CircleBlogDetailsActivity.this,
						PersonBlogByGroupActivity.class, bundle, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.circleblogdetails_group://
			try {
				String temp1 = v.getTag().toString();
				if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
						|| "OBJ_MEMBER_RES_MOOD".equals(type_Object)) {
					startActivityByMemberId(temp1);
				} else {
					Bundle b = new Bundle();
					b.putString("Id", temp1);
					if (type_Object.contains("OBJ_OPEN_PAGE")) {
						b.putString("To", "OpenPageId");
						LogicUtil.enter(CircleBlogDetailsActivity.this,
								HomePageActivity.class, b, false);
					} else {
						LogicUtil.enter(CircleBlogDetailsActivity.this,
								HomeGpActivity.class, b, false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
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
	private Handler handler_CircleBlog = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			spdDialog.cancelProgressDialog(null);
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case HANDLER_ZERO: //
					showTip((String) msg.obj);
					break;
				case HANDLER_ONE: // 显示赞成员列表
					setPraiseInfo((MapResourceGreatBean) msg.obj, type_Praise,
							img_Praises);
					break;
				case HANDLER_TWO: // 显示浏览成员列表
					setVisitInfo((MapResourceVisitBean) msg.obj, type_Visit,
							img_Visitors);
					break;
				case HANDLER_THREE:
					if (adapter_Comment.getCount() == 0)
						lv_Comment.removeFooterView();
					break;
				case HANDLER_FOUR: // 更新圈博
					QuuboInfoBean qBean = ((QuuboInfoBean) msg.obj);
					GroupTopicBean bean_GroupTopic = qBean.getGroupTopicBean();
					if (!hasPraise)
						hasPraise = qBean.isHasPraise();
					if (bean_GroupTopic != null) {
						String info_Blog = bean_GroupTopic.getTopicContent();
						if (null == info_Blog || "null".equals(info_Blog)) {
							info_Blog = bean_GroupTopic.getTopicTitle();
							if (null == info_Blog || "null".equals(info_Blog)) {
								info_Blog = "";
							}
						}
						// L.i(TAG, "info_Blog0=" + info_Blog);
						shareTopic = info_Blog;
						web_Info_Blog.setTag(info_Blog);
						info_Blog = dealDec(info_Blog);

						// L.d(TAG, "info_Blog=" + info_Blog);
						web_Info_Blog.loadDataWithBaseURL(null, info_Blog
								+ "<br/ ><br/ >", "text/html", "utf-8", null);
						web_Info_Blog.setVisibility(View.VISIBLE);
					}
					break;
				case HANDLER_FIVE: // 更新赞人数
					// showToast((String) msg.obj);
					num_Praise = num_Praise + 1;
					hasPraise = true;
					Drawable drawable = getBroudDrawable(R.drawable.icon_great_2);
					txt_Num_Praise.setCompoundDrawables(drawable, null, null,
							null);
					iv_praise.setImageResource(R.drawable.send_praise_b);
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append("赞");
					if (num_Praise > 0) {
						if (num_Praise > num_Details) {
							strBuffer.append(num_Details);
							strBuffer.append("+");
						} else {
							strBuffer.append(num_Praise);
						}
						strBuffer.append("人觉得赞");
						txt_Num_Praise.setText(strBuffer.toString());

						tv_praise.setText("赞(" + num_Praise + ")");
					}
					int length = strBuffer.length();
					strBuffer.delete(0, length);
					strBuffer = null;
					if (bean_MapResourceGreat == null) {
						bean_MapResourceGreat = new MapResourceGreatBean();
					}

					try {
						List<ResourceGreatBean> beanList = bean_MapResourceGreat
								.getPRAISELIST();
						bean_MapResourceGreat.setTOTALNUM(bean_MapResourceGreat
								.getTOTALNUM() + 1);
						UserBusinessDatabase business = new UserBusinessDatabase(
								App.app);
						String session_key = App.app.share.getSessionKey();
						String headUrlPath = business
								.getHeadUrlPath(session_key);
						ResourceGreatBean greatBean = new ResourceGreatBean(
								id_Default_Member, headUrlPath);
						beanList.add(0, greatBean);
						bean_MapResourceGreat.setPRAISELIST(beanList);
						setPraiseInfo(bean_MapResourceGreat, type_Praise,
								img_Praises);
					} catch (Exception e) {
						e.printStackTrace();
					}

					txt_Num_Praise.setTag(type_Praise + "@"
							+ String.valueOf(num_Praise));
					break;
				case HANDLER_SIX: // 刷新评论
					// T.show(App.app, "已评论!");
					num_Total = num_Total + 1;
					loadInfo(false, 0);
					break;
				case HANDLER_SEVEN: // 回复评论
					showKeyBoard(edit_CommentInfo);
					hSize = lv_Comment.getHeaderViewsCount();
					dex = msg.arg1;
					bottom = msg.arg2;
					// lv_Comment.setSelection(hSize + dex);

					String[] info = ((String[]) msg.obj);
					name_Reply = "@" + info[0] + "：";
					if (receiveReplyIds == null) {
						// receiveReplyIds = new ArrayList<String>();
						receiveReplyIds = new HashMap<String, String>();
					}
					if (!"0".equals(info[1])
							&& !receiveReplyIds.containsValue(info[1])) {
						// receiveReplyIds.add(info[1]);
						receiveReplyIds.put(info[1], name_Reply);
					}
					String eText = edit_CommentInfo.getText().toString();
					if (!eText.contains(name_Reply)) {
						edit_CommentInfo.getText().append(name_Reply);
						edit_CommentInfo.setSelection((eText + name_Reply)
								.length());
					} else {
						edit_CommentInfo.setSelection(eText.indexOf(name_Reply)
								+ name_Reply.length());
					}

					new Thread() {
						public void run() {
							try {
								sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler_CircleBlog.sendEmptyMessage(100);
						};
					}.start();
					break;
				case HANDLER_EIGHT: // 处理删除资源
					showTip((String) msg.obj);
					finish();
					break;
				case HANDLER_ELEVEN:
					Object[] objs = (Object[]) msg.obj;
					ImageView iv = (ImageView) objs[0];
					File file = (File) objs[1];
					OnMorePreDrawListener onMorePrewDrawListener = mapOnMorePreDrawListener
							.get(Integer.parseInt(iv.getTag().toString()));
					// L.d(TAG, "handler_CircleBlog creat_iv_tag" + iv.getTag()
					// + file.getAbsolutePath());
					onMorePrewDrawListener.setAnimDraw(AnimationFrameUtil
							.getFrame(file.getAbsolutePath(), iv, 2));
					mapOnMorePreDrawListener.put(
							Integer.parseInt(iv.getTag().toString()),
							onMorePrewDrawListener);
					break;
				case 100:
					int top = ((View) edit_CommentInfo.getParent()).getTop();
					// L.i(TAG, "handler_CircleBlog hSize=" + hSize + ",dex="
					// + dex + ",top=" + top + ",bottom=" + bottom);
					lv_Comment.setSelectionFromTop(hSize + dex + 1, top + 2);
					showKeyBoard(edit_CommentInfo);
					break;
				case 200:
					hSize = lv_Comment.getHeaderViewsCount();
					top = ((View) edit_CommentInfo.getParent()).getTop();
					bottom = llayout_Header.getHeight();
					// L.d(TAG, "handler_CircleBlog hSize=" + hSize + ",top="
					// + top + ",bottom=" + bottom + ",Count="
					// + lv_Comment.getCount());
					if (hSize == lv_Comment.getCount()) {
						lv_Comment.setSelectionFromTop(hSize, top - bottom + 4);
					} else {
						lv_Comment.setSelectionFromTop(hSize, top + 4);
					}
					// lv_Comment.requestPositionToScreen(hSize, true);
					// showKeyBoard(edit_CommentInfo);
					break;
				case 2000:
					adapter_Comment.notifyDataSetChanged();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showMenuDialog() {
		if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
				|| "OBJ_MEMBER_RES_MOOD".equals(type_Object)) {
			LogicUtil.enter(this, InfoWallActivity.class, null, true);
		} else {
			if (!hasAuthority) {
				showTip("您不是该圈子成员，请先加入圈子！");
				return;
			}

			ArrayList<String> datalist = new ArrayList<String>();
			final HashMap<String, Integer> map = new HashMap<String, Integer>();

			String item0;
			if (isOpenPage) {
				item0 = "分享给朋友";
			} else {
				item0 = "圈内分享";
			}
			datalist.add(item0);
			map.put(item0, 0);

			String item1 = "分享到交流圈";
			datalist.add(item1);
			map.put(item1, 1);

			// L.i(TAG, "showMenuDialog isOpenPage=" + isOpenPage +
			// ",openPageId="
			// + openPageId);
			if (openPageId != 0) {
				String item8 = "分享到开放主页";
				datalist.add(item8);
				map.put(item8, 8);
			}

			String groupType = null;
			try {
				groupType = groupService.queryGroupType(id_Group + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!"2".equals(groupType)) {
				if (shareTopic != null || isSharePhoto()) {
					String item2 = "分享到第三方";
					datalist.add(item2);
					map.put(item2, 2);

					String item6 = "分享到微信";
					datalist.add(item6);
					map.put(item6, 6);

					if (!"OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)) {
						String item3 = "一键分享";
						datalist.add(item3);
						map.put(item3, 3);
					}
				}
			}

			String item4 = "";
			if (!hasCollect) {
				item4 = "收藏";
			} else {
				item4 = "取消收藏";
			}
			datalist.add(item4);
			map.put(item4, 4);

			// String item7 = "复制";
			// datalist.add(item7);
			// map.put(item7, 7);

			if (hasDelete) {
				String item5 = "删除";
				datalist.add(item5);
				map.put(item5, 5);
			}
			int size = datalist.size();
			final String[] datas = new String[size];
			for (int i = 0; i < size; i++) {
				datas[i] = datalist.get(i);
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					CircleBlogDetailsActivity.this)
			// .setItems(datas, new DialogInterface.OnClickListener() {
					.setAdapter(
							new ArrayAdapter(CircleBlogDetailsActivity.this,
									R.layout.choice_item, datas),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String title = null;
									try {
										title = txt_BlogTitle.getText()
												.toString();
									} catch (Exception e) {
										e.printStackTrace();
									}
									String otherShar = "";
									if (title != null && !"".equals(title)) {
										otherShar = "【"
												+ title
												+ "】"
												+ CharUtil
														.filterText(shareTopic);
									} else if (shareTopic != null
											&& !"".equals(shareTopic)) {
										otherShar = CharUtil
												.filterText(shareTopic);
									}
									if (otherShar.length() > 90) {
										otherShar = otherShar.substring(0, 40);
									}
									if (!"".equals(otherShar))
										otherShar += "http://t.yuuquu.com/download-client";
									switch (map.get(datas[which])) {
									case 0:// 分享给圈内朋友
										if (isOpenPage) {
											Intent intent = new Intent(
													CircleBlogDetailsActivity.this,
													FriendsChooserActivity.class);
											intent.putExtra("tpye", 3);
											startActivityForResult(
													intent,
													FriendsChooserActivity.RESCODE);
										} else {
											startActivityByToType();
										}
										break;
									case 1:// 分享到交流圈
										Bundle b = new Bundle();
										b.putString("btnString", "分享");
										LogicUtil.enter(
												CircleBlogDetailsActivity.this,
												GroupsChooserActivity.class, b,
												GroupsChooserActivity.RESCODE);
										break;
									case 2:// 分享到第三方
										share(otherShar, isSharePhoto());
										break;
									case 3:// 一键分享
										shareOtherApp(title);
										break;
									case 4:// 收藏、取消收藏
										if (!hasCollect) {
											collectResource(!hasCollect);
										} else {
											collectResource(!hasCollect);
										}
										hasCollect = !hasCollect;
										break;
									case 5:// 删除
										deleteResource();
										break;
									case 6:// 分享到微信
										shareToWeiXin(otherShar);
										break;
									case 7:// 复制到剪切板
										ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
										clipboardManager.setText(shareTopic);
										showTip("已复制到剪切板");
										break;
									case 8:// 分享到开放主页
											// L.d(TAG,
											// "showMenuDialog openPageId="
											// + openPageId);
										spdDialog.showProgressDialog("分享中...");
										new ShareBlogToTask().execute(3,
												openPageId + "",
												String.valueOf(id_Group),
												String.valueOf(id_Quubo),
												String.valueOf(type_Object));
										break;
									}
								}
							}).setOnCancelListener(null);
			AlertDialog ad = builder.create();
			ad.setCanceledOnTouchOutside(true);
			ad.show();
		}
	}

	/**
	 * 一键分享
	 * 
	 * @param title
	 */
	private void shareOtherApp(String title) {
		Intent intent = new Intent(CircleBlogDetailsActivity.this,
				ShareOtherAppActivity.class);
		if (title != null && !"".equals(title)) {
			intent.putExtra("shareTopic", "【" + title + "】" + shareTopic);
		} else {
			intent.putExtra("shareTopic", shareTopic);
		}
		// L.i(TAG, "showMenuDialog shareTopic=" + shareTopic + ",shareImage="
		// + shareImage);
		intent.putExtra("shareImage", shareImage);
		intent.putExtra("groupId", id_Group + "");
		intent.putExtra("objectId", id_Quubo + "");
		if ("OBJ_MEMBER_RES_MOOD".equals(type_Object)) {
			intent.putExtra("temp", "3");
		} else if ("OBJ_GROUP_PHOTO".equals(type_Object)) {
			intent.putExtra("temp", "2");
		} else {
			intent.putExtra("temp", "1");
		}
		startActivity(intent);
	}

	/**
	 * 分享到微信
	 * 
	 * @param otherShar
	 */
	private void shareToWeiXin(String otherShar) {
		// L.i(TAG, "shareToWeiXin otherShar=" + otherShar + ",shareImage="
		// + shareImageUrl + shareImage);
		IWXAPI api = WXAPIFactory.createWXAPI(CircleBlogDetailsActivity.this,
				ConstantUtil.APP_ID, false);
		api.registerApp(ConstantUtil.APP_ID);
		if (api.isWXAppInstalled()) {
			Intent wIntent = new Intent(CircleBlogDetailsActivity.this,
					WXEntryActivity.class);
			if (otherShar != null && !"".equals(otherShar)) {
				wIntent.putExtra("shareTopic", otherShar);
				wIntent.putExtra("objectId", id_Quubo + "");
				if (shareImage != null && !"".equals(shareImage)) {
					wIntent.putExtra("type", 101);
					wIntent.putExtra("shareImageUrl", shareImageUrl
							+ shareImage);
					wIntent.putExtra(
							"data",
							ImageDealUtil.getBitmapBytes(
									shareImg.getDrawingCache(), false));
				}
			} else if (shareImage != null && !"".equals(shareImage)) {
				wIntent.putExtra("shareTopic", "分享照片");
				wIntent.putExtra("type", 1);
				wIntent.putExtra("shareImageUrl", shareImageUrl + shareImage);
				wIntent.putExtra("data", ImageDealUtil.getBitmapBytes(
						shareImg.getDrawingCache(), false));
				// wIntent.putExtra("data", ImageDealUtil.bmpToByteArray(
				// shareImg.getDrawingCache(), false));
			}
			startActivity(wIntent);
		} else {
			showTip("您还未安装微信客户端！");
		}
	}

	/**
	 * 处理圈博详情
	 * 
	 * @param info_Blog
	 * @return
	 */
	private String dealDec(String info_Blog) {
		if (info_Blog == null || "".equals(info_Blog)) {
			return "";
		}

		SpannableString s = SpannableString.valueOf(info_Blog);
		info_Blog = FaceUtil.faceToAssetsHtml(info_Blog);
		if (info_Blog.contains(LinkDealUtil.FORMAT_LINK_END_PARAMTER)
				&& info_Blog.contains(LinkDealUtil.FORMAT_LINK_START_PARAMTER)) {
			info_Blog = LinkDealUtil.subLink(info_Blog)
					+ LinkDealUtil.dealLink(info_Blog);
		}
		if (Linkify.addLinks(s, Linkify.WEB_URLS)
				&& !info_Blog.contains("</a>"))
			info_Blog = CharUtil.getUrl(info_Blog);
		info_Blog = "<head><style type=\"text/css\"><!--body{line-height:150%}--></style></head><body>"
				+ info_Blog + "</body>";
		return info_Blog;
	}

	String title_Blog = null;

	/**
	 * 初始化圈博详情
	 * 
	 * @param bean_ResourceTrend
	 *            ResourceTrendBean类 单一圈博圈博信息
	 **/
	@SuppressLint("UseSparseArrays")
	private void setDetailsInfo(Object bean, String type) {
		TransitionByGroupBlogBean bean_Transition = new TransitionByGroupBlogBean(
				type, bean);
		if (bean instanceof ResourceBean) {
			type_Object = ((ResourceBean) bean).getObjectType();
		} else if (bean instanceof QuuboInfoBean) {
		} else {
			type_Object = bean_Transition.getObjectType();
		}
		setTitles();
		MemberOrGroupInfoBean bean_SendMemberInfo = bean_Transition
				.getSendMemberInfoBean();
		if (!hasPraise)
			hasPraise = bean_Transition.isHasPraise();

		if (bean_SendMemberInfo != null) {
			id_Member = bean_SendMemberInfo.getId();
			name_Member = bean_SendMemberInfo.getName();
			if (id_Member == id_Default_Member)
				name_Member = "我";
		}

		txt_Name_Member.setText(name_Member);

		try {
			name_Group = bean_Transition.getObjOwnerMemberInfoBean().getName();
			txt_Name_group.setTag(""
					+ bean_Transition.getObjOwnerMemberInfoBean().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (name_Group != null && !"".equals(name_Group))
			if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
					|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type_Object)) {
				txt_Name_group.setText(Html
						.fromHtml("<font color='#19a97b'> @ </font>"
								+ name_Group));
			} else if ("OBJ_MEMBER_RES_MOOD".equals(type_Object)) {

			} else {
				txt_Name_group.setText(Html
						.fromHtml("<font color='#19a97b'> ▶ </font>"
								+ name_Group));
			}
		img_Headimg_Member.setTag(id_Member);

		if (bean_SendMemberInfo != null) {
			String path_Head = bean_SendMemberInfo.getFullHeadPath();
			path_Head = ThumbnailImageUrl.getThumbnailHeadUrl(path_Head,
					HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			img_Headimg_next.setTag(name_Member + "@" + path_Head);
			MyFinalBitmap.setHeader(this, img_Headimg_Member, path_Head);
		}
		final List<ObjectInfoBean> beans_ObjectInfo = bean_Transition
				.getObjectInfoBeans();

		String data_Update = bean_Transition.getUpdateTime();
		id_Quubo = bean_Transition.getQuuboId();
		id_Group = bean_Transition.getGroupId();
		name_Group = bean_Transition.getObjOwnerMemberInfoBean().getName();
		L.i(TAG, "setDetailsInfo name_Group=" + name_Group);

		String publishWay = bean_Transition.getPublishWay();
		txt_BaseInfo_Blog.setText(DateTimeUtil.cTimeFormat(DateTimeUtil
				.getLongTime(data_Update))
				+ " "
				+ SendWay.resoureSendWay(publishWay));

		int i = 0;
		// urls = new ArrayList<String>();
		beans_Info = new ArrayList<ObjectInfoBean>();
		int index = 0;
		if (beans_ObjectInfo != null) {
			List<Object> beans = new ArrayList<Object>();
			while (i < beans_ObjectInfo.size()) {
				final ObjectInfoBean bean_ObjectInfo = beans_ObjectInfo.get(i);
				String type_ObjSource = bean_ObjectInfo.getObjSourceType();

				if ("OBJ_GROUP_TOPIC".equals(type_ObjSource)
						|| "OBJ_OPEN_PAGE_TOPIC".equals(type_ObjSource)) {
					String info_Blog = null;
					if (type_Resource.equals(type)) {
						info_Blog = bean_ObjectInfo.getObjectDescription();
						if (null == info_Blog || "null".equals(info_Blog)) {
							info_Blog = "";
						}
						title_Blog = bean_ObjectInfo.getObjectName();
						uploadTopic();
					} else if (type_ResultMessage.equals(type)) {
						info_Blog = bean_Transition.getInfo_Blog();
						title_Blog = bean_Transition.getTitle_Blog();
					}
					if (info_Blog == null || "".equals(info_Blog)) {
						info_Blog = title_Blog;
					} else {
						if (null != title_Blog && !title_Blog.equals("")) {
							txt_BlogTitle.setText(title_Blog);
							txt_BlogTitle.setVisibility(View.VISIBLE);
						}
					}
					shareTopic = info_Blog;
					web_Info_Blog.setTag(info_Blog);
					info_Blog = dealDec(info_Blog);

					web_Info_Blog.loadDataWithBaseURL(null, info_Blog
							+ "<br/ ><br/ >", "text/html", "utf-8", null);
					web_Info_Blog.setVisibility(View.VISIBLE);
				} else if ("OBJ_GROUP_PHOTO".equals(type_ObjSource)
						|| "OBJ_OPEN_PAGE_PHOTO".equals(type_ObjSource)) {

					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout llayout_img = (LinearLayout) inflater
							.inflate(
									R.layout.circleblogdetailsheadform_addlayoutform_img,
									null);
					ImageView img_Img_Blog = (ImageView) llayout_img
							.findViewById(R.id.circleblogdetailsheadform_addlayoutform_img_blogimg);
					img_Img_Blog.setDrawingCacheEnabled(true);
					final String originalurl = bean_ObjectInfo.getFullImgPath();
					// L.i(TAG, "setDetailsInfo path_Img=" + originalurl);
					shareImage = bean_ObjectInfo.getObjectPath();
					shareImageUrl = bean_ObjectInfo.getObjectUrl();
					final String thumbnailUrl = ThumbnailImageUrl
							.getThumbnailImageUrl(originalurl,
									ImageSizeEnum.EIGHT_HUNDRED);
					// urls.add(thumbnailUrl);
					beans_Info.add(bean_ObjectInfo);
					index = index + 1;
					// L.d(TAG, "setDetailsInfo index=" + index);
					img_Img_Blog.setTag(index);
					img_Img_Blog.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putInt("groupId", id_Group);
							String str_Index = v.getTag().toString();
							int index = 0;
							try {
								if (null != str_Index && !"".equals(str_Index)) {
									index = Integer.parseInt(str_Index);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							bundle.putInt("index", (index - 1));
							bundle.putString("type", "type_default");
							HashMap<String, Object> hashMap = new HashMap<String, Object>();
							hashMap.put("beans", beans_ObjectInfo);
							bundle.putSerializable("map", hashMap);
							if (hasAuthority)
								bundle.putInt("authority", 1);
							else
								bundle.putInt("authority", 0);
							hashMap = null;
							LogicUtil.enter(CircleBlogDetailsActivity.this,
									PhotoGalleryActivity.class, bundle, false);
						}
					});
					if (originalurl.endsWith(".gif")) {
						// L.i(TAG, "setDetailsInfo thumbnailUrl=" +
						// thumbnailUrl);
						String filePath = ConstantUtil.SYSTEM_FILEPATH;
						String imgName = originalurl.split("/")[originalurl
								.split("/").length - 1];
						// L.i(TAG, "setDetailsInfo url=" + imgName);
						OnMorePreDrawListener onMorePreDrawListener = new OnMorePreDrawListener(
								img_Img_Blog);
						if (null == mapOnMorePreDrawListener)
							mapOnMorePreDrawListener = new HashMap<Integer, OnMorePreDrawListener>();
						mapOnMorePreDrawListener.put(index,
								onMorePreDrawListener);
						img_Img_Blog.getViewTreeObserver()
								.addOnPreDrawListener(onMorePreDrawListener);
						File gifFile = new File(filePath + imgName);

						// L.d(TAG, "setDetailsInfo imgName=" + imgName);
						if (!gifFile.exists()) {
							LoadSdCard.loadGif(originalurl, filePath, imgName,
									handler_CircleBlog, img_Img_Blog,
									HANDLER_ELEVEN);
						} else {
							Message msg = Message.obtain();
							msg.what = HANDLER_ELEVEN;
							msg.obj = new Object[] { img_Img_Blog, gifFile };
							handler_CircleBlog.sendMessage(msg);
						}
					} else {
						MyFinalBitmap
								.setImage(this, img_Img_Blog, thumbnailUrl);
					}
					llayout_AddByImg.addView(llayout_img);
					llayout_AddByImg.setVisibility(View.VISIBLE);
					if (null == shareImg) {
						shareImg = img_Img_Blog;
					}
				} else if ("OBJ_GROUP_FILE".equals(type_ObjSource)
						|| "OBJ_OPEN_PAGE_FILE".equals(type_ObjSource)) {
					beans.add(bean_ObjectInfo);
				} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_ObjSource)
						|| "OBJ_MEMBER_RES_MOOD".equals(type_ObjSource)
						|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type_ObjSource)) {
					String info_Blog = bean_ObjectInfo.getObjectDescription();
					if (null == info_Blog || "null".equals(info_Blog)) {
						info_Blog = bean_ObjectInfo.getObjectName();
						if (null == info_Blog || "null".equals(info_Blog)) {
							info_Blog = "";
						}
					}
					if (null == shareTopic) {
						shareTopic = info_Blog;
					}
					info_Blog = dealDec(info_Blog);
					web_Info_Blog.loadDataWithBaseURL(null, info_Blog
							+ "<br/ ><br/ >", "text/html", "utf-8", null);
					web_Info_Blog.setVisibility(View.VISIBLE);
				} else if ("OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type_ObjSource)) {
					String info_Blog = bean_ObjectInfo.getObjectDescription();
					if (null == info_Blog || "null".equals(info_Blog)) {
						info_Blog = bean_ObjectInfo.getObjectName();
						if (null == info_Blog || "null".equals(info_Blog)) {
							info_Blog = "";
						}
					}
					if (null == shareTopic) {
						shareTopic = info_Blog;
					}
					int guestbookType = bean_Transition.getGuestbookType();
					// 留言类型：1-留言 2-咨询 3-投诉 4-表扬
					if (guestbookType == 1) {
						info_Blog = "【留言】" + dealDec(info_Blog);
					} else if (guestbookType == 2) {
						info_Blog = "【咨询】" + dealDec(info_Blog);
					} else if (guestbookType == 3) {
						info_Blog = "【投诉】" + dealDec(info_Blog);
					} else if (guestbookType == 4) {
						info_Blog = "【表扬】" + dealDec(info_Blog);
					} else {
						info_Blog = dealDec(info_Blog);
					}
					web_Info_Blog.loadDataWithBaseURL(null, info_Blog
							+ "<br/ ><br/ >", "text/html", "utf-8", null);
					web_Info_Blog.setVisibility(View.VISIBLE);
				}
				i++;
			}

			if (beans.size() > 0) {
				showFileBox();
				mAdditiveFileLvAdapter_Choosed.flush(beans);
			}
		}

		num_Praise = bean_Transition.getPraiseNum();
		if (num_Praise > 0) {
			StringBuffer strBuffer = new StringBuffer();
			if (num_Praise > num_Details) {
				strBuffer.append(num_Details);
				strBuffer.append("+");
			} else {
				strBuffer.append(num_Praise);
			}
			strBuffer.append("人觉得赞");
			txt_Num_Praise.setText(strBuffer.toString());
			tv_praise.setText("赞(" + num_Praise + ")");
		}
		int lId = R.drawable.icon_great;
		L.d(TAG, "setDetailsInfo hasPraise=" + hasPraise);
		if (hasPraise) {
			lId = R.drawable.icon_great_2;
			iv_praise.setImageResource(R.drawable.send_praise_b);
		} else {
			lId = R.drawable.icon_great;
			iv_praise.setImageResource(R.drawable.send_praise_a);
		}
		txt_Num_Praise.setCompoundDrawables(getBroudDrawable(lId), null, null,
				null);
		getPraiseInfo(type_Object, 0, 7);
		num_Visit = bean_Transition.getVisitMemberNum();
		StringBuffer strBuffer = new StringBuffer();
		if (num_Visit > 0) {
			if (num_Visit > num_Details) {
				strBuffer.append(num_Details);
				strBuffer.append("+");
			} else {
				strBuffer.append(num_Visit);
			}
			strBuffer.append("人看过");
			txt_Num_Visitor.setText(strBuffer.toString());
			tv_visitor.setText("浏览(" + num_Visit + ")");
		}
		getVisitInfo(type_Object, 0, 7);
		int num_Comment = bean_Transition.getCommentNum();
		num_Total = num_Comment;
		comments = new ArrayList<ResourceCommentBean>();
		adapter_Comment = new CommentAdapter(comments, this,
				handler_CircleBlog, id_Default_Member, new int[] { 7, 3 },
				String.valueOf(id_Group), String.valueOf(id_Quubo), type_Object);
		adapter_Comment.setInfoHandler(infoHandler);
		lv_Comment.addHeaderView(llayout_Header, null, false);
		lv_Comment.setAdapter(adapter_Comment);
		mIsMore = true;

		getCommentInfo();
		loadInfo(true, -1);
		bindListener();
	}

	private void getCommentInfo() {
		getFailComment();

		MapResourceCommentBean bean_MapResourceComment = QuuBoCommentService
				.getService(App.app).queryQuubo(id_Group, id_Quubo);
		if (bean_MapResourceComment == null)
			return;
		List<ResourceCommentBean> beans_Temp = bean_MapResourceComment
				.getLIST();
		if (beans_Temp != null && beans_Temp.size() > 0) {
			comments.addAll(beans_Temp);
			adapter_Comment.notifyDataSetChanged();
			// lv_Comment.setSelection(lv_Comment.getHeaderViewsCount());
		}
	}

	private void getFailComment() {
		ArrayList<CommentSendBean> csbs = CommentSendService.getService(this)
				.queryById(App.app.share.getSessionKey(), id_Default_Member,
						id_Group, id_Quubo, type_Object);
		if (csbs != null && csbs.size() > 0) {
			for (CommentSendBean csb : csbs) {
				ResourceCommentBean rcb = new ResourceCommentBean();
				rcb.setCommentContent(csb.getContent());
				rcb.setCommentTime(csb.getTime());
				L.d(TAG, "releaseComment CommentTime=" + rcb.getCommentTime());
				rcb.setSendStatus(-2);
				rcb.setCommentWay("client=android");
				rcb.setMemberId(id_Default_Member);
				rcb.setMemberHeadUrl(csb.getHead());
				rcb.setMemberHeadPath("");
				String ats = csb.getAt();
				if (ats != null && ats.length() > 0) {
					String[] rIds = ats.split("#");
					rcb.setReceiveIds(rIds);
				}
				String atidname = csb.getAtidname();
				if (atidname != null && atidname.length() > 0) {
					atidname = atidname.substring(0, atidname.length() - 1);
					List<FriendSimpleBean> atMemberInfos = new ArrayList<FriendSimpleBean>();
					if (atidname.contains("&")) {
						String[] atins = atidname.split("&");
						for (String atin : atins) {
							try {
								FriendSimpleBean fsb = new FriendSimpleBean();
								fsb.setMemberId(Integer.valueOf(atin.substring(
										0, atin.indexOf("#"))));
								fsb.setMemberName(atin.substring(atin
										.indexOf("#") + 1));
								atMemberInfos.add(fsb);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						try {
							FriendSimpleBean fsb = new FriendSimpleBean();
							fsb.setMemberId(Integer.valueOf(atidname.substring(
									0, atidname.indexOf("#"))));
							fsb.setMemberName(atidname.substring(atidname
									.indexOf("#") + 1));
							atMemberInfos.add(fsb);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					rcb.setAtMemberInfos(atMemberInfos);
				}
				comments.add(rcb);
			}
		}
		adapter_Comment.notifyDataSetChanged();
	}

	private ArrayList<ResourceCommentBean> comments;

	/**
	 * 设置标题
	 */
	@SuppressLint("NewApi")
	private void setTitles() {
		if ("OBJ_GROUP_QUUBO".equals(type_Object)) {
			strType = "圈博";
		} else if ("OBJ_GROUP_PHOTO".equals(type_Object)) {
			strType = "照片";
		} else if ("OBJ_GROUP_FILE".equals(type_Object)) {
			strType = "文件";
		} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
				|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type_Object)) {
			strType = "留言";
		} else if ("OBJ_MEMBER_RES_MOOD".equals(type_Object)) {
			strType = "心情";
		} else {
			strType = "资源";
		}
		ab.setTitle(strType);

		if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(type_Object)
				|| "OBJ_MEMBER_RES_MOOD".equals(type_Object)) {
			hasMoreView = false;
			invalidateOptionsMenu();
		} else {
		}
	}

	/**
	 * 显示赞信息
	 * 
	 * @param bean_MapResourceGreat
	 *            MapResourceGreatBean类 接受赞成员信息类
	 * @param type
	 *            String类 数据类型 praise：赞
	 * @param img_s
	 *            List<ImageView>集合 传入赞放置头像的imageview列表集合
	 **/
	private void setPraiseInfo(MapResourceGreatBean bean_MapResourceGreat,
			String type, List<ImageView> img_s) {
		if (bean_MapResourceGreat == null) {
			return;
		}
		num_Praise = bean_MapResourceGreat.getTOTALNUM();
		StringBuffer strBuffer = new StringBuffer();
		if (num_Praise > 0) {
			if (num_Praise > num_Details) {
				strBuffer.append(num_Details);
				strBuffer.append("+");
			} else {
				strBuffer.append(num_Praise);
			}
			strBuffer.append("人觉得赞");
			txt_Num_Praise.setText(strBuffer.toString());
			tv_praise.setText("赞(" + num_Praise + ")");
		}
		int length = strBuffer.length();
		strBuffer.delete(0, length);
		strBuffer = null;
		List<ResourceGreatBean> beans_MapResourceGreat = bean_MapResourceGreat
				.getPRAISELIST();
		for (int i = 0; i < beans_MapResourceGreat.size(); i++) {
			final ResourceGreatBean rgBean = beans_MapResourceGreat.get(i);
			if (!img_s.get(i).isDrawingCacheEnabled()) {
				String info = ThumbnailImageUrl.getThumbnailHeadUrl(
						rgBean.getFullHeadPath(),
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				MyFinalBitmap.setHeader(this, img_s.get(i), info);
			}
			img_s.get(i).setTag(rgBean.getMemberId());
			img_s.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String temp = v.getTag().toString();
					startActivityByMemberId(temp);
				}
			});
			img_s.get(i).setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					String[] temp = new String[] { rgBean.getMemberName(),
							v.getTag().toString(), "0" };
					Message msg = Message.obtain();
					msg.what = 7;
					msg.obj = temp;
					msg.arg1 = 0;
					msg.arg2 = 0;
					handler_CircleBlog.sendMessage(msg);
					return true;
				}
			});
			img_s.get(i).setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 显示浏览成员信息
	 * 
	 * @param bean_MapResourceVisit
	 *            MapResourceVisitBean类 接受浏览成员信息类
	 * @param type
	 *            String类 数据类型 visit：浏览
	 * @param img_s
	 *            List<ImageView>集合 传入成员放置头像的imageview列表集合
	 **/
	private void setVisitInfo(MapResourceVisitBean bean_MapResourceVisit,
			String type, List<ImageView> img_s) {
		if (bean_MapResourceVisit == null) {
			return;
		}
		num_Visit = bean_MapResourceVisit.getTOTALMEMBERNUM();
		StringBuffer strBuffer = new StringBuffer();
		if (num_Visit > 0) {
			if (num_Visit > num_Details) {
				strBuffer.append(num_Details);
				strBuffer.append("+");
			} else {
				strBuffer.append(num_Visit);
			}
			strBuffer.append("人看过");
			txt_Num_Visitor.setText(strBuffer.toString());
			tv_visitor.setText("浏览(" + num_Visit + ")");
		}
		int length = strBuffer.length();
		strBuffer.delete(0, length);
		strBuffer = null;
		List<ResourceVisitBean> beans_ResourceVisit = bean_MapResourceVisit
				.getLIST();
		if (beans_ResourceVisit != null) {
			for (int i = 0; i < beans_ResourceVisit.size(); i++) {
				final ResourceVisitBean rvBean = beans_ResourceVisit.get(i);
				if (!img_s.get(i).isDrawingCacheEnabled()) {
					String info = ThumbnailImageUrl.getThumbnailHeadUrl(
							rvBean.getFullHeadPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
					MyFinalBitmap.setHeader(this, img_s.get(i), info);
				}
				img_s.get(i).setTag(rvBean.getMemberId());
				img_s.get(i).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String temp = v.getTag().toString();
						startActivityByMemberId(temp);
					}
				});
				img_s.get(i).setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						String[] temp = new String[] { rvBean.getMemberName(),
								v.getTag().toString(), "0" };
						Message msg = Message.obtain();
						msg.what = 7;
						msg.obj = temp;
						msg.arg1 = 0;
						msg.arg2 = 0;
						handler_CircleBlog.sendMessage(msg);
						return true;
					}
				});
				img_s.get(i).setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 获取赞成员的信息
	 **/
	private void getPraiseInfo(final String objectType, final int startRecord,
			final int maxResults) {
		bean_MapResourceGreat = QuuBoPraiseService.getService(App.app)
				.queryQuubo(id_Group, id_Quubo);
		sendHandlerObjMsg(HANDLER_ONE, bean_MapResourceGreat);

		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.praiseRecords(
							App.app, String.valueOf(id_Group),
							String.valueOf(id_Quubo), objectType, "false",
							String.valueOf(startRecord),
							String.valueOf(maxResults));
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						bean_MapResourceGreat = (MapResourceGreatBean) mcResult
								.getResult();
						sendHandlerObjMsg(HANDLER_ONE, bean_MapResourceGreat);
					} else {
						// sendHandlerStrMsg(, R.string.getinfo_err);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// sendHandlerStrMsg(HANDLERZERO, R.string.getinfo_err);
				}
			}

		}.start();
	}

	/**
	 * 获取访问成员的信息
	 * 
	 * @param objectType
	 *            final String类型
	 * @param startRecord
	 *            final int类型 开始位置
	 * @param startRecord
	 *            final int类型 结束位置
	 **/
	private void getVisitInfo(final String objectType, final int startRecord,
			final int maxResults) {
		bean_MapResourceVisit = QuuBoVisitService.getService(App.app)
				.queryQuubo(id_Group, id_Quubo);
		sendHandlerObjMsg(HANDLER_TWO, bean_MapResourceVisit);

		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.resourceVisitList(
							App.app, String.valueOf(id_Group),
							String.valueOf(id_Quubo), objectType, "false",
							String.valueOf(startRecord),
							String.valueOf(maxResults));
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						bean_MapResourceVisit = (MapResourceVisitBean) mcResult
								.getResult();
						sendHandlerObjMsg(HANDLER_TWO, bean_MapResourceVisit);
					} else {
						// sendHandlerStrMsg(HANDLERZERO, R.string.getinfo_err);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// sendHandlerStrMsg(HANDLERZERO, R.string.getinfo_err);
				}
			}

		}.start();
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
			lv_Comment.hideLoadFooter();
			if (null == result || result.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				if (adapter_Comment.getCount() == 0)
					lv_Comment.removeFooterView();
				return;
			}
			// L.d(TAG, "CommentRequsetTask ResultCode=" +
			// result.getResultCode());
			MapResourceCommentBean bean_MapResourceComment = (MapResourceCommentBean) result
					.getResult();
			List<ResourceCommentBean> beans_Temp = bean_MapResourceComment
					.getLIST();
			if (null == beans_Temp || beans_Temp.size() == 0) {
				mIsMore = false;
				if (mIsRefresh) {
					lv_Comment.removeFooterView();
					lv_Comment.onRefreshComplete();
					return;
				}
				if (adapter_Comment.getCount() == 0) {
					lv_Comment.removeFooterView();
					return;
				}
				lv_Comment.showTempFooter();
				return;
			}
			if (mIsRefresh) {
				comments.clear();
				getFailComment();
			}
			// L.d(TAG, "onPostExecute beans_Temp.size()" + beans_Temp.size());
			// adapter_Comment.add(beans_Temp, mLocation, mIsRefresh);
			comments.addAll(beans_Temp);
			adapter_Comment.notifyDataSetChanged();
			if (mIsRefresh && comments.size() != beans_Temp.size())
				lv_Comment.setSelection(lv_Comment.getHeaderViewsCount());
			if (mLocation != 0) {
				mIsMore = beans_Temp.size() >= SIZE_PAGE;
			} else {
				// int top = ((View) edit_CommentInfo.getParent()).getTop();
				// lv_Comment.setSelectionFromTop(
				// lv_Comment.getHeaderViewsCount() + 1, top);
			}
			beans_Temp.clear();
			// L.d(TAG, "mIsMore:" + mIsMore);
			if (!mIsMore) {
				lv_Comment.showTempFooter();
			}
			if (mIsRefresh) {
				lv_Comment.onRefreshComplete(false);
			}
		}

	}

	/**
	 * 评论资源
	 * 
	 * @param info
	 *            final String类型 评论内容
	 **/
	public void releaseComment(final String strComment) {
		if (!hasAuthority) {
			showTip("您不是该圈子成员，请先加入圈子！");
			return;
		}

		String[] receiveIds = null;
		if (null != receiveReplyIds)
			receiveIds = receiveReplyIds.keySet().toArray(new String[] {});
		List<FriendSimpleBean> atMemberInfos = new ArrayList<FriendSimpleBean>();
		if (null != receiveIds && receiveIds.length > 0) {
			for (String id : receiveIds) {
				FriendSimpleBean fsb = new FriendSimpleBean();
				fsb.setMemberId(Integer.valueOf(id));
				String ats = receiveReplyIds.get(id);
				fsb.setMemberName(ats.substring(1, ats.length() - 1));
				atMemberInfos.add(fsb);

				if (!strComment.contains(receiveReplyIds.get(id))) {
					receiveReplyIds.remove(id);
				}
			}
			receiveIds = receiveReplyIds.keySet().toArray(new String[] {});
		}
		final String[] rIds = receiveIds;
		// spdDialog.showProgressDialog("正在处理中...");
		final ResourceCommentBean rcb = new ResourceCommentBean();
		rcb.setCommentContent(strComment);
		rcb.setCommentTime(System.currentTimeMillis());
		L.d(TAG, "releaseComment CommentTime=" + rcb.getCommentTime());
		rcb.setSendStatus(-1);
		rcb.setCommentWay("client=android");
		rcb.setMemberId(id_Default_Member);
		rcb.setMemberName("我");
		rcb.setAtMemberInfos(atMemberInfos);
		UserBusinessDatabase business = new UserBusinessDatabase(App.app);
		String session_key = App.app.share.getSessionKey();
		final String headUrlPath = business.getHeadUrlPath(session_key);
		rcb.setMemberHeadUrl(headUrlPath);
		rcb.setMemberHeadPath("");
		if (null != rIds && rIds.length > 0) {
			rcb.setReceiveIds(rIds);
			receiveReplyIds.clear();
		}
		saveFailComment(rcb, false);
		comments.add(0, rcb);
		adapter_Comment.notifyDataSetChanged();
		edit_CommentInfo.setText("");

		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = null;
					if (null != rIds && rIds.length > 0) {
						mcResult = APIRequestServers.commentResource(App.app,
								String.valueOf(id_Group),
								String.valueOf(id_Quubo), type_Object,
								strComment, rIds);
					} else {
						mcResult = APIRequestServers.commentResource(App.app,
								String.valueOf(id_Group),
								String.valueOf(id_Quubo), type_Object,
								strComment, null);
					}
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						CommentSendService.getService(App.app).deleteComment(
								rcb.getCommentTime());

						if (bean_Resource != null) {
							isInfoRefresh = true;
							Message msg = new Message();
							msg.obj = new String[] { strComment, headUrlPath };
							infoHandler.sendMessage(msg);
						} else {
							InfoWallActivity.isNeedRefresh = true;
						}

						isSelection = true;
						// sendHandlerStrMsg(HANDLER_SIX,
						// R.string.comment_success);
						comments.remove(rcb);
						rcb.setSendStatus(0);
						try {
							JSONObject jsonObject = new JSONObject(mcResult
									.getResult().toString());
							int commentId = jsonObject.getInt("COMMENT_ID");
							rcb.setCommentId(commentId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						isSelection = false;
						// sendHandlerStrMsg(HANDLER_ZERO);
						comments.remove(rcb);
						rcb.setSendStatus(-2);
						info_comment = "";

						saveFailComment(rcb, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					isSelection = false;
					// sendHandlerStrMsg(HANDLER_ZERO);
					comments.remove(rcb);
					rcb.setSendStatus(-2);
					info_comment = "";

					saveFailComment(rcb, true);
				}
				comments.add(0, rcb);
				handler_CircleBlog.sendEmptyMessage(2000);
			}
		}.start();
	}

	private void saveFailComment(ResourceCommentBean rcb, boolean isFail) {
		if (isFail) {
			CommentSendService.getService(this).updateSendStatus(
					rcb.getCommentTime(), -2);
			return;
		}

		CommentSendBean scb = new CommentSendBean();
		scb.setTime(rcb.getCommentTime());
		L.d(TAG, "saveFailComments CommentTime=" + rcb.getCommentTime());
		scb.setSession_key(App.app.share.getSessionKey());
		scb.setId(id_Default_Member);
		scb.setName("我");
		scb.setHead(rcb.getMemberHeadUrl());
		scb.setGid(id_Group);
		scb.setGname(name_Group);
		scb.setRid(id_Quubo);
		scb.setmType(type_Object);
		scb.setContent(rcb.getCommentContent());
		scb.setSendStatus(-1);
		if (rcb.getReceiveIds() != null) {
			String ats = "";
			for (String receiveId : rcb.getReceiveIds()) {
				ats += receiveId + "#";
			}
			if (!"".equals(ats)) {
				scb.setAt(ats.substring(0, ats.length() - 1));
			}
		}
		if (null != title_Blog && !title_Blog.equals("")) {
			scb.setTitle(title_Blog);
		} else {
			scb.setTitle(shareTopic);
		}
		scb.setRname(name_Member);

		String atidname = "";
		List<FriendSimpleBean> atMemberInfos = rcb.getAtMemberInfos();
		if (atMemberInfos != null && atMemberInfos.size() > 0) {
			for (FriendSimpleBean fsb : atMemberInfos) {
				atidname += fsb.getMemberId() + "#" + fsb.getMemberName() + "&";
			}
		}
		scb.setAtidname(atidname);

		CommentSendService.getService(this).save(scb);
	}

	// /**
	// * 是否有正在评论或者评论失败
	// *
	// * @return -1：评论中，-2：评论失败
	// */
	// private long hasSendingFail() {
	// for (ResourceCommentBean rcb : comments) {
	// if (rcb.getCommentTime() < 0)
	// return rcb.getCommentTime();
	// }
	// return 0;
	// }

	private Handler infoHandler = new Handler() {
		public void handleMessage(Message msg) {
			String[] strs = (String[]) msg.obj;
			if (bean_Resource == null)
				return;
			tBean = new ResourceTrendBean();
			bean_Resource.setCommentNum(bean_Resource.getCommentNum() + 1);
			tBean.setResourceBean(bean_Resource);
			tBean.setActinContent(strs[0]);
			tBean.setHasGreat(bean_Resource.isHasPraise());
			MemberOrGroupInfoBean sendMemberInfo = new MemberOrGroupInfoBean();
			sendMemberInfo.setId(id_Default_Member);
			sendMemberInfo.setName("我");
			sendMemberInfo.setHeadUrl(strs[1]);
			sendMemberInfo.setHeadPath("");
			sendMemberInfo.setId(id_Default_Member);
			tBean.setSendMemberInfo(sendMemberInfo);
			tBean.setActionType("COMMENT_RESOURCE");
			tBean.setPublishWay("client=android");
			tBean.setCreateTime(System.currentTimeMillis() + "");
		};
	};

	/**
	 * 更多圈博
	 **/
	private void uploadTopic() {
		QuuboInfoBean qib = null;
		try {
			qib = QuuBoInfoService.getService(this).queryQuubo(id_Group,
					id_Quubo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (qib != null) {
			sendHandlerObjMsg(HANDLER_FOUR, qib);
		} else {
			new Thread() {
				@Override
				public void run() {
					try {
						MCResult mcResult = APIRequestServers.quuboInfo(
								App.app, String.valueOf(id_Group),
								String.valueOf(id_Quubo));
						int Code_result = mcResult.getResultCode();
						if (Code_result == 1) {
							QuuboInfoBean bean_QuuboInfo = (QuuboInfoBean) mcResult
									.getResult();
							if (bean_QuuboInfo != null) {
								sendHandlerObjMsg(HANDLER_FOUR, bean_QuuboInfo);
							}
						} else {
							// sendHandlerStrMsg(HANDLER_ZERO,
							// R.string.getinfo_err);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// sendHandlerStrMsg(HANDLER_ZERO,
						// R.string.getinfo_err);
					}
				}
			}.start();
		}
	}

	/**
	 * 赞资源
	 */
	private void praiseResource() {
		if (!hasAuthority) {
			showTip("您不是该圈子成员，请先加入圈子！");
			return;
		}
		sendHandlerStrMsg(HANDLER_FIVE, R.string.praiseresource_succeed);

		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.praiseResource(
							App.app, String.valueOf(id_Group),
							String.valueOf(id_Quubo), type_Object);
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						if (bean_Resource != null) {
							isInfoRefresh = true;
							tBean = new ResourceTrendBean();
							bean_Resource.addPraise();
							tBean.setResourceBean(bean_Resource);
							tBean.setHasGreat(true);
							// tBean.setActionMemberId(id_Default_Member);
							MemberOrGroupInfoBean sendMemberInfo = new MemberOrGroupInfoBean();
							UserBusinessDatabase business = new UserBusinessDatabase(
									App.app);
							String session_key = App.app.share.getSessionKey();
							String headUrlPath = business
									.getHeadUrlPath(session_key);
							sendMemberInfo.setHeadUrl(headUrlPath);
							sendMemberInfo.setHeadPath("");
							sendMemberInfo.setId(id_Default_Member);
							tBean.setSendMemberInfo(sendMemberInfo);
							tBean.setActionType("PRAISE_RESOURCE");
							tBean.setPublishWay("client=android");
							tBean.setCreateTime(System.currentTimeMillis() + "");
						} else {
							InfoWallActivity.isNeedRefresh = true;
						}

						// sendHandlerStrMsg(HANDLER_FIVE,
						// R.string.praiseresource_succeed);
					} else {
						// sendHandlerStrMsg(HANDLER_ZERO,
						// R.string.praiseresource_lose);
					}
				} catch (Exception e) {
					e.printStackTrace();
					// sendHandlerStrMsg(HANDLER_ZERO,
					// R.string.praiseresource_lose);
				}
			}
		}.start();
	}

	/**
	 * 删除圈博
	 * 
	 * @param id_Group
	 * @param id_Quubo
	 */
	private void deleteResource() {
		new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您确定要删除本" + strType + "吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread() {
							@Override
							public void run() {
								try {
									MCResult mcResult = APIRequestServers
											.deleteResource(App.app,
													String.valueOf(id_Group),
													String.valueOf(id_Quubo),
													type_Object);
									int Code_result = mcResult.getResultCode();
									if (Code_result == 1) {
										InfoWallActivity.isNeedRefresh = true;
										sendHandlerStrMsg(HANDLER_EIGHT,
												R.string.deleteresource_succeed);
									} else {
										sendHandlerStrMsg(HANDLER_ZERO);
									}
								} catch (Exception e) {
									e.printStackTrace();
									sendHandlerStrMsg(HANDLER_ZERO);
								}
							}
						}.start();
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 收藏、取消收藏
	 * 
	 * @param flag
	 */
	private void collectResource(final boolean flag) {
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.collectResource(
							App.app, flag, String.valueOf(id_Group),
							String.valueOf(id_Quubo), type_Object);
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						if (flag) {
							hasCollect = true;
							sendHandlerStrMsg(HANDLER_ZERO,
									R.string.collectresource_succeed);
						} else {
							hasCollect = false;
							sendHandlerStrMsg(HANDLER_ZERO,
									R.string.collectresource_cancel_succeed);
						}
						InfoWallActivity.isNeedRefresh = true;
					} else {
						sendHandlerStrMsg(HANDLER_ZERO);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendHandlerStrMsg(HANDLER_ZERO);
					hasCollect = !flag;
				}
			}
		}.start();
	}

	/**
	 * 跳转界面
	 * 
	 * @param GroupId
	 *            需要传递的信息
	 **/
	private void startActivityByMember(Type type, int num) {
		Bundle bundle = new Bundle();
		bundle.putInt(BundleKey.ID_GROUP, id_Group);
		bundle.putInt(BundleKey.ID_QUUBOO, id_Quubo);
		bundle.putInt(BundleKey.ID_MEMBER, id_Member);
		bundle.putBoolean("isReply", true);
		bundle.putString(BundleKey.TYPE_OBJECT, type_Object);
		bundle.putSerializable(BundleKey.TYPE_REQUEST, type);
		LogicUtil.enter(this, MemberListActivity.class, bundle, 41);
	}

	/**
	 * 按成员id跳转界面
	 * 
	 * @param id_Member
	 */
	private void startActivityByMemberId(String id_Member) {
		Bundle b = new Bundle();
		b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
		b.putString("id", id_Member);
		b.putString("name", name_Member);
		LogicUtil.enter(this, HomePgActivity.class, b, false);
	}

	/**
	 * 按分享类型跳转界面
	 * 
	 * @param type
	 */
	private void startActivityByToType() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("canSelectAll", canSelectAll);
		bundle.putInt(BundleKey.ID_GROUP, id_Group);
		bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.GROUPLEAGUER);
		LogicUtil.enter(this, GroupLeaguerChooseActivity.class, bundle,
				GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER);
	}

	/**
	 * 处理文字中的图片
	 */
	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String id) {
			Drawable drawable = null;
			try {
				drawable = getResources().getDrawable(Integer.valueOf(id));
				int width = drawable.getIntrinsicWidth();
				int height = drawable.getIntrinsicHeight();
				drawable.setBounds(0, 0, width, height);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return drawable;
		}
	};

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (faceView.isShown()) {
				hideKeyBoardFace();
				return true;
			} else if (!"".equals(edit_CommentInfo.getText().toString())) {
				isExit("是否放弃评论？");
				return true;
				// } else if (hasSendingFail() == -1) {
				// isExit("正在评论中，退出可能评论失败，是否退出？");
				// return true;
				// } else if (hasSendingFail() == -2) {
				// isExit("评论失败，是否退出？");
				// return true;
			}
			if (isInfoRefresh) {
				Intent i = new Intent();
				i.putExtra("position", position);
				i.putExtra("Trend", tBean);
				setResult(RESULT_OK, i);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 无输入
	 */
	@SuppressLint("NewApi")
	private void hideKeyBoardFace() {
		try {
			faceView.setVisibility(View.GONE);
			img_Face.setImageResource(R.drawable.icon_face);
			BaseData.hideKeyBoard(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 表情输入
	 */
	@SuppressLint("NewApi")
	private void showFace() {
		BaseData.hideKeyBoard(this);
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
	@SuppressLint("NewApi")
	private void showKeyBoard(View view) {
		faceView.setVisibility(View.GONE);
		img_Face.setImageResource(R.drawable.icon_face);
		BaseData.showKeyBoard(this, view);
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerStrMsg(int id) {
		Message msg = Message.obtain();
		msg.what = id;
		msg.obj = T.ErrStr;
		handler_CircleBlog.sendMessage(msg);
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
		handler_CircleBlog.sendMessage(msg);
	}

	/**
	 * 发送对象信息
	 * 
	 * @param object
	 *            需要发送的对象信息
	 **/
	private void sendHandlerObjMsg(int id, Object object) {
		Message msg = Message.obtain();
		msg.what = id;
		msg.obj = object;
		handler_CircleBlog.sendMessage(msg);
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

	private void share(String contextStr, boolean sharePhoto) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (sharePhoto) {
			intent.setType("image/*");
			try {
				Uri u = Uri.parse(MediaStore.Images.Media.insertImage(
						getContentResolver(), shareImg.getDrawingCache(), null,
						null));
				intent.putExtra(Intent.EXTRA_STREAM, u);
			} catch (Exception e) {
				e.printStackTrace();
			}
			intent.putExtra(Intent.EXTRA_TEXT, contextStr);
		} else {
			intent.setType("text/*");
			// intent.putExtra(Intent.EXTRA_SUBJECT, titleStr);
			intent.putExtra(Intent.EXTRA_TEXT, contextStr);
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}

	private Drawable getBroudDrawable(int aid) {
		Drawable drawable = getResources().getDrawable(aid);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, 2 * dw / 3, 2 * dh / 3);
		return drawable;
	}

	private boolean isSharePhoto() {
		if (null != shareImg) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		if (hasMoreView) {
			getSupportMenuInflater().inflate(R.menu.main, menu);
			menu.findItem(R.id.action_more).setVisible(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!"".equals(edit_CommentInfo.getText().toString())) {
				isExit("是否放弃评论？");
				return true;
				// } else if (hasSendingFail() == -1) {
				// isExit("正在评论中，退出可能评论失败，是否退出？");
				// return true;
				// } else if (hasSendingFail() == -2) {
				// isExit("评论失败，是否退出？");
				// return true;
			}
			if (isInfoRefresh) {
				Intent i = new Intent();
				i.putExtra("position", position);
				i.putExtra("Trend", tBean);
				setResult(RESULT_OK, i);
			}
			finish();
			return true;
		case R.id.action_more:
			showMenuDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case GroupsChooserActivity.RESCODE:
			String[] shareGroupIds = data.getStringArrayExtra("ids");
			if (null == shareGroupIds || 0 == shareGroupIds.length) {
				showTip("您没有选择任何圈子！");
			} else {
				spdDialog.showProgressDialog("分享中...");
				new ShareBlogToTask().execute(1, shareGroupIds,
						String.valueOf(id_Group), String.valueOf(id_Quubo),
						String.valueOf(type_Object));
			}
			break;
		case GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER:
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) data
					.getSerializableExtra(BundleKey.CHOOSEDS);
			String[] shareGroupLeaguerIds = map.keySet().toArray(new String[0]);
			boolean isSelectAll = data.getBooleanExtra(BundleKey.ISSELECTALL,
					false);
			int size = data.getIntExtra(BundleKey.SIZE, 0);
			spdDialog.showProgressDialog("分享中...");
			new ShareBlogToTask().execute(2, shareGroupLeaguerIds, isSelectAll,
					String.valueOf(id_Group), String.valueOf(id_Quubo),
					String.valueOf(type_Object), size);
			break;
		case FriendsChooserActivity.RESCODE:
			String[] shareFriendIds = data.getStringArrayExtra("ids");
			spdDialog.showProgressDialog("分享中...");
			new ShareBlogToTask().execute(2, shareFriendIds, false,
					String.valueOf(id_Group), String.valueOf(id_Quubo),
					String.valueOf(type_Object), shareFriendIds.length);
			break;
		case 41:
			String[] temp = new String[] { data.getStringExtra("name"),
					data.getStringExtra("id"), "0" };
			Message msg = Message.obtain();
			msg.what = 7;
			msg.obj = temp;
			msg.arg1 = 0;
			msg.arg2 = 0;
			handler_CircleBlog.sendMessage(msg);
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	class ShareBlogToTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;

		public ShareBlogToTask() {
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			try {
				switch ((Integer) mParams[0]) {
				case 1:
					mcResult = APIRequestServers.shareResourceToGroup(App.app,
							(String) mParams[2], (String) mParams[3],
							(String) mParams[4], (String[]) mParams[1]);
					break;
				case 2:
					int size = 0;
					String[] sharedId_To = (String[]) mParams[1];
					size = sharedId_To.length;
					if ((Boolean) mParams[2]) {
						try {
							mcResult = APIRequestServers.shareResource(App.app,
									(String) mParams[3], (String) mParams[4],
									(String) mParams[5], true, sharedId_To);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						MCResult result = null;
						try {
							result = APIRequestServers.groupInfo(App.app,
									(String) mParams[3]);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (result != null && result.getResultCode() == 1) {
							GroupBean groupBean = (GroupBean) result
									.getResult();
							// L.i(TAG, "sharedToMember unchooseize=" + size);
							if (size != 0)
								size = groupBean.getLeaguerNum() - size - 1;
							else
								size = groupBean.getLeaguerNum() - 1;
							// L.i(TAG, "sharedToMember size=" + size);
						}
					} else {
						// L.d(TAG, "sharedToMember size=" + size);
						try {
							mcResult = APIRequestServers.shareResource(App.app,
									(String) mParams[3], (String) mParams[4],
									(String) mParams[5], false, sharedId_To);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mParams[6] = size;
					break;
				case 3:
					mcResult = APIRequestServers.forwardToOpenPage(App.app,
							(String) mParams[2], (String) mParams[3],
							(String) mParams[4], (String) mParams[1]);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (null == result || 1 != result.getResultCode()) {
				showTip(T.ErrStr);
				return;
			}
			switch ((Integer) mParams[0]) {
			case 0:
			case 1:
				InfoWallActivity.isNeedRefresh = true;
			case 3:
				showTip("已分享！");
				break;
			case 2:
				if (null != result && 1 == result.getResultCode()) {
					if ((Integer) mParams[6] > 0)
						if (isOpenPage) {
							showTip("已分享，若接收人不在线，将使用您" + mParams[6] + "个圈币");
						} else {
							showTip("已分享，若接收人不在线，将使用圈子" + mParams[6] + "个圈币");
						}
					else
						showTip("已分享！");
				}
				break;
			}
		}

	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, edit_CommentInfo, text, resId);
	}

	class OnMorePreDrawListener implements OnPreDrawListener {
		private AnimationDrawable mAnimDraw;
		private ImageView mIv;

		public OnMorePreDrawListener(ImageView iv) {
			mIv = iv;
		}

		public ImageView getIv() {
			return mIv;
		}

		public void setAnimDraw(AnimationDrawable animDraw) {
			mAnimDraw = animDraw;
		}

		@Override
		public boolean onPreDraw() {
			if (null != mAnimDraw)
				mAnimDraw.start();
			return true;
		}

	}

	@Override
	public void onLoadMore() {
		loadInfo(false, -1);
	}
}
