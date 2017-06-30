package com.datacomo.mc.spider.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.UrlPagerAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ImageStateEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerPermissionBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.service.DownLoadCloudFileThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.FacesView;
import com.datacomo.mc.spider.android.view.FacesView.OnFaceChosenListner;
import com.datacomo.mc.spider.android.view.GalleryViewPager;
import com.datacomo.mc.spider.android.view.PopMenu;
import com.datacomo.mc.spider.android.view.UrlTouchImageView;

@SuppressLint("HandlerLeak")
public class PhotoGalleryActivity extends BaseActivity implements
		OnClickListener, HandlerNumberUtil, OnFaceChosenListner {
	// 声明静态TAG
	private static final String TAG = "PhotoGalleryActivity";
	// private static final String LOG_DOWN = "PhotoGalleryActivity_Down";
	// public static final int REQUESTSHOWIMG = 201;
	public static final int IDDELETE = 201;
	// 声明变量
	/**
	 * if local the url=path_id if id is null url=path+(
	 * {@code ThumbnailImgUtil.SUFFIX})
	 */
	public static final String URL = "url";
	private final String SHARE = "share";
	// private final String PRAISE = "praise";
	private final String MORE = "more";
	private final String TYPE_MORE = "type_more";
	private final String TYPE_ONLY = "type_only";
	private final String TYPE_LOCAL = "type_local";
	private final String TYPE_TOPIC = "type_topic";
	private final String TYPE_NONE = "type_none";
	private final String TYPE_DEFAULT = "type_default";
	private final int HASAUTHORITY = 1;
	private final int NOAUTHORITY = 0;
	private final int NULLAUTHORITY = -1;
	private int state_Authority;
	private String type;
	private Context mContext;
	private List<Object> beans;
	private int mIndex; // 当前图片index
	private int amount; // 总数
	private int num_visitor, num_Praise, num_Comment;
	private boolean menuIsShow;// 顶部信息栏与底部输入框
	private boolean isComment;// 评论栏和输入框
	// private boolean canReleaseComment = true;
	// private boolean isDeleted = false;
	private boolean hasPraise = false, hasDelete = false;
	/** 是否有权限操作 */
	// private boolean keyIsShow;
	private boolean fromPhotoGrid; // true 来自照片表格 需要获取更过信息
	/** 是否收藏过 */
	private boolean hasCollect;
	private boolean mIsOpenPage;
	private int openPageId = 0;
	private boolean mIsCooperationLeaguer;
	private String info_comment;
	private String id_Group, id_Photo, type_Object;
	private String id_ObjectGroup;// 图片所属圈子
	private List<String> mOChooseds; // 原图地址
	private List<String> mChooseds; // 原图地址
	// 声明引用类
	private UrlPagerAdapter adapter_UrlPager;
	private ImageTask imageTask;
	private PopMenu popMenu;
	private MoreHandler moreHandler;
	private ShareHandler shareHandler;

	// 声明组件
	private GalleryViewPager vp_Gallery;
	private LinearLayout mLlayout_Title;// 标题按钮框
	private LinearLayout mLlayout_Menu;
	private LinearLayout mLlayout_CommentBottomBox;
	private LinearLayout mLlayout_CommentTitleBox;
	private LinearLayout mLlayout_MenuBox;
	private LinearLayout mLlayout_TitleBox;// 标题外框
	private TextView mTv_TiTle;
	private TextView mTv_Index;
	private TextView mTv_Commentnum;
	private ImageView mIv_Comment;
	private ImageView mIv_Share;
	private ImageView mIv_Praise;
	private ImageView mIv_More;
	private ImageView mIv_Face;
	private ImageView mIv_Title_left;
	private ImageView mIv_CommentTitle_left;
	private FacesView faceView;
	private EditText mEdit_CommentInfo;
	private ImageView mBtn_Release;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		moreHandler.removeCallbacksAndMessages(null);
		shareHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_photo);
		mContext = this;
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 **/
	private void initView() {
		vp_Gallery = (GalleryViewPager) findViewById(R.id.layout_photo_gallery);
		mLlayout_Title = (LinearLayout) findViewById(R.id.layout_photo_title_llayout_titlebox);
		mLlayout_TitleBox = (LinearLayout) findViewById(R.id.layout_photo_llayout_title);
		mLlayout_Menu = (LinearLayout) findViewById(R.id.layout_photo_menubox_llayout_menu);
		mLlayout_CommentBottomBox = (LinearLayout) findViewById(R.id.layout_photo_menubox_llayout_commentbottombox);
		mLlayout_CommentTitleBox = (LinearLayout) findViewById(R.id.layout_photo_title_llayout_commentitlebox);
		mLlayout_MenuBox = (LinearLayout) findViewById(R.id.layout_photo_llayout_menubox);
		mTv_TiTle = (TextView) findViewById(R.id.layout_photo_txt_title);
		mTv_Index = (TextView) findViewById(R.id.layout_photo_txt_index);
		mTv_Commentnum = (TextView) findViewById(R.id.layout_photo_title_commenttitle_tv_commentnum);
		Drawable temp = ImageDealUtil.getPxToDpDrawable(mContext,
				R.drawable.mail_down_icon);
		mTv_Commentnum.setCompoundDrawables(temp, null, null, null);
		mIv_Comment = (ImageView) findViewById(R.id.layout_photo_menubox_menu_img_comment);
		mIv_Share = (ImageView) findViewById(R.id.layout_photo_menubox_menu_img_share);
		mIv_Praise = (ImageView) findViewById(R.id.layout_photo_menubox_menu_img_praise);
		mIv_Title_left = (ImageView) findViewById(R.id.left);
		mIv_CommentTitle_left = (ImageView) findViewById(R.id.layout_photo_title_commenttitle_iv_left);
		mIv_More = (ImageView) findViewById(R.id.layout_photo_menubox_menu_img_more);
		faceView = (FacesView) findViewById(R.id.facesview);
		faceView.setOnFaceChosenListner(this);

		mEdit_CommentInfo = (EditText) findViewById(R.id.comment_edit);
		mBtn_Release = (ImageView) findViewById(R.id.comment_release);
		mIv_Face = (ImageView) findViewById(R.id.comment_face);
	}

	/**
	 * 初始化数据
	 **/
	@SuppressWarnings("unchecked")
	private void initData() {
		Bundle bundle = getIntentMsg();
		moreHandler = new MoreHandler();
		shareHandler = new ShareHandler();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		popMenu = new PopMenu(mContext);
		popMenu.setWidth(dm.widthPixels);
		popMenu.setHigh(dm.heightPixels);

		Object temp_Bean = null;
		amount = bundle.getInt("amount", 1);
		L.d(TAG, "initData amount=" + amount);
		mIndex = bundle.getInt("index", -1);
		if (mIndex < 0)
			mIndex = 0;
		setIndex(mIndex + 1);
		id_Group = String.valueOf(bundle.getInt("groupId", 0));
		type = bundle.getString("type");
		state_Authority = bundle.getInt("authority", NULLAUTHORITY);
		fromPhotoGrid = false;
		mIsOpenPage = bundle.getBoolean("isOpenPage", false);
		mIsCooperationLeaguer = bundle
				.getBoolean("isCooperationLeaguer", false);
		L.d(TAG, "initData type=" + type);
		boolean isOriginal = bundle.getBoolean("isOriginal", false);
		if (TYPE_MORE.equals(type)) {
			fromPhotoGrid = true;
			temp_Bean = ((HashMap<String, Object>) bundle.get("map"))
					.get("beans");
			beans = (List<Object>) temp_Bean;
			From from = (From) bundle.getSerializable("from");

			if (amount > PageSizeUtil.SIZEPAGE_RESOURCELIST) {

				adapter_UrlPager = new UrlPagerAdapter(mContext, beans,
						getHandler(), true, true, amount, type);
				imageTask = new ImageTask(mContext);
				imageTask.execute(from, id_Group, beans.size());
			} else {
				adapter_UrlPager = new UrlPagerAdapter(mContext, beans,
						getHandler(), true, type);
			}
			vp_Gallery.setAdapter(adapter_UrlPager);
			L.d(TAG, "mIndex" + mIndex);
			if (mIndex < 0)
				mIndex = 0;
			setIndex(mIndex + 1);
			vp_Gallery.setCurrentItem(mIndex);
			bindListener();
		} else if (TYPE_DEFAULT.equals(type)) {
			temp_Bean = ((HashMap<String, Object>) bundle.get("map"))
					.get("beans");
			new ScreenTask().execute(temp_Bean);
		} else {
			List<Object> tempBeans = new ArrayList<Object>();
			String url = bundle.getString("url");
			ObjectInfoBean bean = new ObjectInfoBean();
			bean.setObjectUrl(url);
			bean.setObjectPath("");
			bean.setObjectName("");
			bean.setResourceBean(new ResourceBean());
			tempBeans.add(bean);
			mTv_TiTle.setText(url.substring(url.lastIndexOf("/") + 1));
			if (mIndex < 0)
				mIndex = 0;
			setIndex(mIndex + 1);
			adapter_UrlPager = new UrlPagerAdapter(mContext, tempBeans,
					getHandler());
			adapter_UrlPager.setIsOriginal(isOriginal);
			vp_Gallery.setAdapter(adapter_UrlPager);
			vp_Gallery.setCurrentItem(mIndex);
			mLlayout_TitleBox.setVisibility(View.GONE);
			mLlayout_MenuBox.setVisibility(View.GONE);
			bindListener();
		}
		menuIsShow = true;
		isComment = false;
		// keyIsShow = false;
		menuCanUse(false);
		// getAuthority();

		new Thread() {
			public void run() {
				try {
					MCResult mcResult = APIRequestServers
							.leaguerPermissionInfo(App.app, id_Group + "");
					if (mcResult != null && 1 == mcResult.getResultCode()) {
						GroupLeaguerPermissionBean bean = (GroupLeaguerPermissionBean) mcResult
								.getResult();
						String leaguerStatus = bean.getLeaguerStatus();
						if ("1".equals(leaguerStatus)
								|| "2".equals(leaguerStatus)
								|| "GROUP_OWNER".equals(leaguerStatus)
								|| "GROUP_MANAGER".equals(leaguerStatus)) {
							hasDelete = true;

							MCResult mc = APIRequestServers.existOpenPage(
									App.app, id_Group + "");
							if (mc != null && 1 == mc.getResultCode()) {
								JSONObject json = new JSONObject(mc.getResult()
										.toString());
								L.i(TAG, "initData json=" + json);
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

	private void showSharePop(View anchor) {
		// popMenu.CreatSmailContent("您觉得赞", PRAISE);
		List<String> infos = new ArrayList<String>();
		if (mIsOpenPage) {
			infos.add("分享给朋友");
		} else {
			infos.add("圈内分享");
		}
		infos.add("分享到交流圈");
		if (openPageId != 0) {
			infos.add("分享到开放主页");
		}
		// popMenu.CreatShareContent(infos, SHARE, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// popMenu.popDismiss();
		// try {
		// shareHandler.sendEmptyMessage(Integer.parseInt(v.getTag()
		// .toString()));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// TODO
		popMenu.showSharePop(SHARE, anchor, infos, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu.popDismiss();
				try {
					shareHandler.sendEmptyMessage(Integer.parseInt(v.getTag()
							.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 绑定监听
	 */
	private void bindListener() {
		vp_Gallery.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setDefaultInfo();
				mIndex = arg0;
				L.d(TAG, "index" + arg0 + "");
				setIndex(mIndex + 1);
				if (!TYPE_ONLY.equals(type) && !TYPE_LOCAL.equals(type)
						&& !TYPE_TOPIC.equals(type) && !TYPE_NONE.equals(type))
					setInfo(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		// vp_Gallery.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// L.d(TAG,"chilevp");
		// menuManage();
		// }
		// });
		mIv_Comment.setOnClickListener(this);
		// 选表情
		// rlayout_FatherBox
		// .setOnKeyboardStateChangedListener(new
		// OnKeyboardStateChangedListener() {
		//
		// @Override
		// public void onKeyboardStateChanged(int state) {
		// switch (state) {
		// case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE:// 软键盘隐藏
		// keyIsShow = false;
		// break;
		// case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW:// 软键盘显示
		// keyIsShow = true;
		// break;
		// default:
		// break;
		// }
		// L.d(TAG, "keyIsShow" + keyIsShow);
		//
		// }
		// });
		mIv_Face.setOnClickListener(this);
		mBtn_Release.setOnClickListener(this);
		mEdit_CommentInfo.setOnClickListener(this);
		mIv_Comment.setOnClickListener(this);
		mIv_Praise.setOnClickListener(this);
		mIv_Share.setOnClickListener(this);
		mIv_More.setOnClickListener(this);
		mIv_Title_left.setOnClickListener(this);
		mIv_CommentTitle_left.setOnClickListener(this);
		mTv_Commentnum.setOnClickListener(this);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// L.d(TAG, "canShowMenu()" + canShowMenu());
	// if (!canShowMenu())
	// return false;
	// if (TYPE_ONLY.equals(type))
	// menu.add(Menu.NONE, 0, 1, "保存到手机");
	// else if (TYPE_TOPIC.equals(type))
	// menu.add(Menu.NONE, 0, 1, "删         除");
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (TYPE_ONLY.equals(type)) {
	// Object[] temp = (Object[]) vp_Gallery.mCurrentView.getTag();
	// L.d(TAG, "path:" + (String) temp[0]);
	// new RequestTask(mContext).execute(2, (String) temp[0], "");
	// } else if (TYPE_TOPIC.equals(type)) {
	// new AlertDialog.Builder(mContext)
	// .setTitle("删除提示")
	// .setMessage("是否删除当前图片")
	// .setPositiveButton("删除",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// setResult(RESULT_OK, getIntent());
	// finish();
	// }
	// }).setNegativeButton("取消", null).show();
	// }
	// return false;
	// }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		if (!canShowMenu())
			return false;
		if (TYPE_ONLY.equals(type))
			menu.add(Menu.NONE, 0, 1, "保存到手机");
		else if (TYPE_TOPIC.equals(type))
			menu.add(Menu.NONE, 0, 1, "删         除");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		if (TYPE_ONLY.equals(type)) {
			Object[] temp = (Object[]) vp_Gallery.mCurrentView.getTag();
			L.d(TAG, "path:" + (String) temp[0]);
			new RequestTask(mContext).execute(2, (String) temp[0], "");
		} else if (TYPE_TOPIC.equals(type)) {
			new AlertDialog.Builder(mContext)
					.setTitle("删除提示")
					.setMessage("是否删除当前图片")
					.setPositiveButton("删除",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setResult(RESULT_OK, getIntent());
									finish();
								}
							}).setNegativeButton("取消", null).show();
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean canShowMenu() {
		Object[] temp = (Object[]) vp_Gallery.mCurrentView.getTag();
		boolean canShow = false;
		if (TYPE_ONLY.equals(type)) {
			canShow = true;
			canShow = canShow && ((String) temp[0]).startsWith("http");
			canShow = canShow && null != temp[1]
					&& temp[1] == ImageStateEnum.LOADED;
		} else if (TYPE_TOPIC.equals(type)) {
			canShow = true;
			canShow = canShow && !((String) temp[0]).startsWith("http");
		}
		return canShow;
	}

	@Override
	public void onClick(View v) {
		L.d(TAG, "click");
		switch (v.getId()) {
		case R.id.comment_release:// 评论
			if (!isHasAuthority())
				break;
			info_comment = mEdit_CommentInfo.getText().toString();
			if (info_comment != null && !"".equals(info_comment)) {
				hideKeyBoardFace();
				mBtn_Release.setEnabled(false);
				spdDialog.showProgressDialog("发布中...");
				new RequestTask(mContext).execute(1, id_ObjectGroup, id_Photo,
						type_Object, info_comment, mIndex);
			} else {
				T.show(mContext, "评论内容不能为空");
			}
			break;
		case R.id.comment_edit:// 点击输入评论框
			showKeyBoard(mEdit_CommentInfo);
			break;
		case R.id.comment_face: // 表情、键盘
			if (faceView.isShown()) {
				showKeyBoard(mEdit_CommentInfo);
			} else {
				mEdit_CommentInfo.requestFocus();
				showFace();
			}
			break;
		case R.id.layout_photo_menubox_menu_img_comment:
			CommentManage();
			break;
		case R.id.layout_photo_menubox_menu_img_praise:
			if (!isHasAuthority())
				break;
			if (!hasPraise) {
				new RequestTask(mContext).execute(0, id_ObjectGroup, id_Photo,
						type_Object);
			}
			break;
		case R.id.layout_photo_menubox_menu_img_share:
			showSharePop(v);
			// popMenu.showSharePop(SHARE, v);
			break;
		case R.id.layout_photo_menubox_menu_img_more:
			// creatPopMenu();
			showMorePop(v);
			break;
		case R.id.left:
			finish();
			break;
		case R.id.layout_photo_title_commenttitle_iv_left:
			CommentManage();
			break;
		case R.id.layout_photo_title_commenttitle_tv_commentnum:
			if (num_Comment > 0)
				showComment(num_Comment);
			break;
		}
	}

	/**
	 * 获取intent
	 * 
	 * @return Bundle
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
	 * 创建handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_ZERO:// 显示信息
				T.show(mContext, (String) msg.obj);
				break;
			case HANDLER_TWO:
				L.d(TAG, "createHandler menuIsShow=" + menuIsShow);
				if (!TYPE_ONLY.equals(type) && !TYPE_LOCAL.equals(type)
						&& !TYPE_TOPIC.equals(type) && !TYPE_NONE.equals(type)) {
					if (!isComment)
						menuManage();
				} else {
					finish();
				}
				break;
			case HANDLER_THREE:// 加载附加信息
				ObjectInfoBean bean = null;
				if (fromPhotoGrid) {
					bean = ((ResourceBean) msg.obj).getObjectInfo().get(0);
				} else {
					bean = (ObjectInfoBean) msg.obj;
				}
				ResourceBean bean_Resource = bean.getResourceBean();
				if (null != bean_Resource) {
					setDetailsInfo(bean_Resource);
				} else {
					setDefaultInfo();
				}
				break;
			case HANDLER_FOUR:// 刷新适配器
				adapter_UrlPager.notifyDataSetChanged();
				break;
			case HANDLER_FIVE://
				break;
			case HANDLER_SIX: // 加载
				imageTask = imageTask.doInBackground();
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private class ShareHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_ZERO:
				if (!isHasAuthority())
					break;
				if (mIsOpenPage) {
					Intent intent = new Intent(PhotoGalleryActivity.this,
							FriendsChooserActivity.class);
					intent.putExtra("tpye", 3);
					startActivityForResult(intent,
							FriendsChooserActivity.RESCODE);
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt(BundleKey.ID_GROUP,
							Integer.parseInt(id_ObjectGroup));
					bundle.putSerializable(BundleKey.TYPE_REQUEST,
							Type.GROUPLEAGUER);
					LogicUtil.enter(mContext, GroupLeaguerChooseActivity.class,
							bundle,
							GroupLeaguerChooseActivity.CHOOSEGROUPLEAGUER);
				}
				break;
			case HANDLER_ONE:
				if (!isHasAuthority())
					break;
				Bundle b = new Bundle();
				b.putString("btnString", "分享");
				LogicUtil.enter(mContext, GroupsChooserActivity.class, b,
						GroupsChooserActivity.RESCODE);
				break;
			case HANDLER_TWO:
				spdDialog.showProgressDialog("分享中...");
				new RequestTask(mContext).execute(7, openPageId + "",
						String.valueOf(id_Group), String.valueOf(id_Photo),
						String.valueOf(type_Object));
				break;
			}
		}

	}

	private class MoreHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_ZERO:// 收藏
				new RequestTask(mContext).execute(3, hasCollect,
						id_ObjectGroup, id_Photo, type_Object);
				break;
			case HANDLER_ONE:// 保存
				Object[] temp = (Object[]) vp_Gallery.mCurrentView.getTag();
				L.d(TAG, "path:" + temp[0]);
				new RequestTask(mContext).execute(2, temp[0], mTv_TiTle
						.getText().toString());
				break;
			case HANDLER_TWO:// 评论列表
				if (num_Comment > 0)
					showComment(num_Comment);
				break;
			case HANDLER_THREE:// 访客列表
				if (num_visitor > 0)
					startActivityByMember(Type.VISIT, num_visitor);
				break;
			case HANDLER_FOUR:// 赞列表
				if (num_Praise > 0)
					startActivityByMember(Type.PRAISE, num_Praise);
				break;
			case HANDLER_FIVE:// 删除
				new AlertDialog.Builder(mContext)
						.setTitle("提示")
						.setMessage("您确定要删当前图片吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										spdDialog.showProgressDialog("删除中...");
										new RequestTask(mContext).execute(4,
												id_ObjectGroup, id_Photo,
												type_Object);
									}
								}).setNegativeButton("取消", null).show();
				break;
			}
		}
	}

	/**
	 * 获取handler
	 * 
	 * @return
	 */
	private Handler getHandler() {
		return this.handler;
	}

	/**
	 * 设置详细信息
	 */
	private void setDetailsInfo(ResourceBean bean) {
		if (bean.getIsDeleteResource() != 2 && bean.getObjectId() != 0) {
			List<ObjectInfoBean> tempBeans = (List<ObjectInfoBean>) bean
					.getObjectInfo();
			num_visitor = bean.getVisitMemberNum();
			num_Praise = bean.getPraiseNum();
			num_Comment = bean.getCommentNum();
			hasPraise = bean.isHasPraise();
			hasCollect = bean.isHasCollect();
			if (type.equals(TYPE_MORE)) {
				state_Authority = NOAUTHORITY;
				if (bean.isHasAuthority())
					state_Authority = HASAUTHORITY;
			}
			if (hasPraise) {
				mIv_Praise.setImageResource(R.drawable.praise_down);
			} else {
				mIv_Praise.setImageResource(R.drawable.praise);
			}
			L.d(TAG, "index" + mIndex + " " + "num_visitor:" + num_visitor
					+ " " + "num_Praise:" + num_Praise + " " + "num_Comment:"
					+ num_Comment + "hasCollect" + hasCollect + "tempsize"
					+ tempBeans.size());
			if (tempBeans.size() > 0) {
				menuCanUse(true);
				String name = tempBeans.get(0).getObjectName();
				mTv_TiTle.setText(name);
				// T.show(mContext, name);
				type_Object = tempBeans.get(0).getObjSourceType();
				id_Photo = String.valueOf(tempBeans.get(0).getObjectId());
				id_ObjectGroup = String.valueOf(tempBeans.get(0).getGroupId());
				L.d(TAG, "id_Photo+has" + id_Photo);

				if (type_Object.startsWith("OBJ_OPEN_PAGE_")) {
					mIsOpenPage = true;
				}
			} else {
				menuCanUse(false);
				mTv_TiTle.setText("");
			}
		} else {
			// isDeleted = true;
			T.show(mContext, "该图片已被删除");
			setDefaultInfo();
		}
	}

	/**
	 * 设置详细信息
	 */
	private void setDefaultInfo() {
		mTv_TiTle.setText("");
		num_Comment = 0;
		num_Praise = 0;
		num_visitor = 0;
		menuCanUse(false);
	}

	/**
	 * 设置详细信息
	 */
	private boolean canDeleteAble(int position) {
		ObjectInfoBean bean = null;
		ResourceBean bean_Resource = null;
		if (fromPhotoGrid) {
			bean_Resource = (ResourceBean) adapter_UrlPager.getItem(position);
			if (null != bean_Resource) {
				bean = bean_Resource.getObjectInfo().get(0);
			}
		} else {
			bean = (ObjectInfoBean) adapter_UrlPager.getItem(position);
		}
		if (null != bean) {
			bean_Resource = bean.getResourceBean();
		}
		int sendMemberId = 0;
		if (null != bean) {
			sendMemberId = bean_Resource.getSendMemberId();
		}
		int memberId = GetDbInfoUtil.getMemberId(mContext);
		boolean canDeleteAble = false;
		if (sendMemberId == memberId)
			canDeleteAble = true;
		return canDeleteAble;
	}

	private void showMorePop(View anchor) {
		List<String> infos = new ArrayList<String>();
		if (hasCollect)
			infos.add("取消收藏");
		else
			infos.add("收藏");
		infos.add("保存到手机");
		infos.add("评论（" + num_Comment + "）");
		infos.add("浏览（" + num_visitor + "）");
		infos.add("觉的赞（" + num_Praise + "）");
		if (canDeleteAble(mIndex) || hasDelete)
			infos.add("删除");
		// popMenu.CreatMoreContent(infos, MORE, new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// popMenu.popDismiss();
		// try {
		// moreHandler.sendEmptyMessage(Integer.parseInt(v.getTag()
		// .toString()));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// TODO
		popMenu.showMorePop(MORE, anchor, infos, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu.popDismiss();
				try {
					moreHandler.sendEmptyMessage(Integer.parseInt(v.getTag()
							.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 判定属性栏是否显示
	 * 
	 * @return
	 */
	public boolean isShow() {
		return menuIsShow;
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
			if (0 == shareGroupIds.length) {
				showTip("您没有选择任何圈子！");
			} else {
				spdDialog.showProgressDialog("分享中...");
				new RequestTask(mContext).execute(5, shareGroupIds,
						id_ObjectGroup, id_Photo, type_Object);
			}
			break;
		case FriendsChooserActivity.RESCODE:
			String[] shareFriendIds = data.getStringArrayExtra("ids");
			spdDialog.showProgressDialog("分享中...");
			new RequestTask(mContext).execute(6, shareFriendIds, false,
					id_ObjectGroup, id_Photo, type_Object,
					shareFriendIds.length);
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
			new RequestTask(mContext).execute(6, shareGroupLeaguerIds,
					isSelectAll, id_ObjectGroup, id_Photo, type_Object, size);
			break;
		default:
			break;
		}
	}

	public void showTip(String text) {
		T.show(App.app, text);
	}

	class ImageTask extends AsyncTask<Object, Integer, MCResult> {
		private List<Object> beans_Temp;
		private Context mContext;
		private Object[] mParams = null;
		private int mStartIndex = 0;

		public ImageTask(Context context) {
			mContext = context;
		}

		public ImageTask doInBackground() {
			mStartIndex = mStartIndex + PageSizeUtil.SIZEPAGE_RESOURCELIST;
			if (null == mParams) {
				L.d(TAG, "onPostExecute" + mParams[0]);
			}
			L.d(TAG, "onPostExecute" + mParams[0]);
			L.d(TAG, "onPostExecute" + mParams[1]);
			mParams = new Object[] { mParams[0], mParams[1], mStartIndex };
			ImageTask imageTask = new ImageTask(mContext);
			imageTask.execute(mParams);
			return imageTask;
		};

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			mStartIndex = (Integer) mParams[2];
			try {
				L.d(TAG, "doInBackground" + mStartIndex);
				switch ((From) mParams[0]) {
				case MEMBER:
					mcResult = APIRequestServers.singleResourceList(mContext,
							(String) mParams[1],
							ResourceTypeEnum.OBJ_GROUP_PHOTO,
							String.valueOf(mStartIndex),
							String.valueOf(PageSizeUtil.SIZEPAGE_RESOURCELIST),
							false);
					break;
				case GROUP:
					mcResult = APIRequestServers.groupResourceLists(mContext,
							(String) mParams[1],
							ResourceTypeEnum.OBJ_GROUP_PHOTO,
							String.valueOf(mStartIndex),
							String.valueOf(PageSizeUtil.SIZEPAGE_RESOURCELIST),
							false);
					break;
				case ENSHRINED:
					mcResult = APIRequestServers.collectionResourceList(
							mContext, ResourceTypeEnum.OBJ_GROUP_PHOTO,
							String.valueOf(mStartIndex),
							String.valueOf(PageSizeUtil.SIZEPAGE_RESOURCELIST),
							false);
					break;
				case OPENPAGE:
					mcResult = APIRequestServers.groupResourceLists(mContext,
							(String) mParams[1],
							ResourceTypeEnum.OBJ_OPEN_PAGE_PHOTO,
							String.valueOf(mStartIndex),
							String.valueOf(PageSizeUtil.SIZEPAGE_RESOURCELIST),
							false);
					break;

				default:
					break;
				}
			} catch (Exception e) {
				mcResult = null;
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (null != mcResult) {
				L.d(TAG, "onPostExecute" + mStartIndex);
				if (mcResult.getResultCode() == 1) {
					setInfo(mcResult);
					adapter_UrlPager.setCanLoad(true);
				} else {
					sendHandlerErrStrMsg();
				}
			} else {
				sendHandlerErrStrMsg();
			}
		}

		@SuppressWarnings("unchecked")
		private void setInfo(MCResult mcResult) {
			MapResourceBean mapResourceBean = (MapResourceBean) mcResult
					.getResult();
			if (null != mapResourceBean) {
				Object temp_Bean = mapResourceBean.getLIST();
				beans_Temp = (List<Object>) temp_Bean;
				if (null != beans_Temp && beans_Temp.size() > 0) {
					adapter_UrlPager.addTransitionBeans(beans_Temp,
							HANDLER_FOUR);
					beans_Temp = null;
				}
			}
		}
	}

	class ScreenTask extends AsyncTask<Object, Integer, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			@SuppressWarnings("unchecked")
			List<ObjectInfoBean> beans = (List<ObjectInfoBean>) params[0];
			List<ObjectInfoBean> temp = null;
			for (ObjectInfoBean bean : beans) {
				String type = bean.getObjSourceType();
				if ("OBJ_GROUP_PHOTO".equals(type)
						|| "OBJ_OPEN_PAGE_PHOTO".equals(type)) {
					if (null == temp) {
						temp = new ArrayList<ObjectInfoBean>();
					}
					temp.add(bean);
				}
			}
			beans = null;
			return temp;
		}

		@Override
		protected void onPostExecute(Object beans) {
			super.onPostExecute(beans);
			@SuppressWarnings("unchecked")
			List<Object> tempBeans = (List<Object>) beans;
			amount = tempBeans.size();
			beans = null;
			adapter_UrlPager = new UrlPagerAdapter(mContext, tempBeans,
					getHandler());
			vp_Gallery.setAdapter(adapter_UrlPager);
			vp_Gallery.setCurrentItem(mIndex);
			if (mIndex < 0)
				mIndex = 0;
			setIndex(mIndex + 1);
			bindListener();
		}

	}

	class RequestTask extends AsyncTask<Object, Integer, MCResult> {
		private Context mContext;
		private Object[] mParams = null;

		public RequestTask(Context context) {
			mContext = context;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult mcResult = null;
			mParams = params;
			switch ((Integer) mParams[0]) {
			case 0:// 赞
				try {
					mcResult = APIRequestServers.praiseResource(mContext,
							(String) mParams[1], (String) mParams[2],
							(String) mParams[3]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:// 评论
				try {
					mcResult = APIRequestServers.commentResource(mContext,
							(String) mParams[1], (String) mParams[2],
							(String) mParams[3], (String) mParams[4], null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:// 保存
				String fileUrl = (String) mParams[1];
				int start = fileUrl.lastIndexOf("/") + 1;
				String tempName = fileUrl.substring(start);
				L.d(TAG, "fileUrl:" + fileUrl + " tempName:" + tempName);
				File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
				if (myFile != null && myFile.exists()) {
					sendHandlerObjMsg(HANDLER_ZERO, "图片已存在");
				} else if (ConstantUtil.downloadingList.contains(tempName)) {
					sendHandlerObjMsg(HANDLER_ZERO, "图片正在下载中…");
				} else {
					ConstantUtil.downloadingList.add(tempName);
					new DownLoadCloudFileThread(PhotoGalleryActivity.this,
							fileUrl, 0L, tempName, true, 0).start();
				}
				break;
			case 3:// 收藏
				try {
					L.d(TAG, "收藏" + (Boolean) mParams[1]);
					mcResult = APIRequestServers.collectResource(mContext,
							!(Boolean) mParams[1], (String) mParams[2],
							(String) mParams[3], (String) mParams[4]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 4:// 删除
				try {
					mcResult = APIRequestServers.deleteResource(mContext,
							(String) mParams[1], (String) mParams[2],
							(String) mParams[3]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:// 圈子分享
				try {
					String[] groups = (String[]) mParams[1];
					mcResult = APIRequestServers.shareResourceToGroup(mContext,
							(String) mParams[2], (String) mParams[3],
							(String) mParams[4], groups);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 6:// 圈子成员分享
				int size = 0;
				String[] sharedId_To = (String[]) mParams[1];
				size = sharedId_To.length;
				if ((Boolean) mParams[2]) {
					try {
						mcResult = APIRequestServers.shareResource(mContext,
								(String) mParams[3], (String) mParams[4],
								(String) mParams[5], true, sharedId_To);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					MCResult result = null;
					try {
						result = APIRequestServers.groupInfo(mContext,
								(String) mParams[3]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (result != null && result.getResultCode() == 1) {
						GroupBean groupBean = (GroupBean) result.getResult();
						L.i(TAG, "sharedToMember unchooseize=" + size);
						if (size != 0)
							size = groupBean.getLeaguerNum() - size - 1;
						else
							size = groupBean.getLeaguerNum() - 1;
						L.i(TAG, "sharedToMember size=" + size);
					}
				} else {
					L.d(TAG, "sharedToMember size=" + size);
					try {
						mcResult = APIRequestServers.shareResource(mContext,
								(String) mParams[3], (String) mParams[4],
								(String) mParams[5], false, sharedId_To);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				mParams[6] = size;
				break;
			case 7:
				try {
					mcResult = APIRequestServers.forwardToOpenPage(App.app,
							(String) mParams[2], (String) mParams[3],
							(String) mParams[4], (String) mParams[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			mBtn_Release.setEnabled(true);
			switch ((Integer) mParams[0]) {
			case 0:
				if (null != mcResult) {
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						num_Praise += 1;
						hasPraise = true;
						ChangeLocalInfo(2, num_Praise, true);
						mIv_Praise.setImageResource(R.drawable.praise_down);
						InfoWallActivity.isNeedRefresh = true;
						// TODO popMenu.showPraisePop(PRAISE, mIv_Praise);
					}
				} else {
					sendHandlerErrStrMsg();
				}
				break;
			case 1:
				spdDialog.cancelProgressDialog(null);
				if (null != mcResult) {
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						num_Comment += 1;
						ChangeLocalInfo(0, num_Comment, false);
						InfoWallActivity.isNeedRefresh = true;
						CommentManage();
						mEdit_CommentInfo.setText("");
						T.show(App.app,
								getResourcesString(R.string.comment_success));
					} else {
						sendHandlerErrStrMsg();
					}
				} else {
					sendHandlerErrStrMsg();
				}
				break;
			case 3:
				if (null != mcResult) {
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						if (!(Boolean) mParams[1]) {
							hasCollect = !(Boolean) mParams[1];
							ChangeLocalInfo(1, 0, hasCollect);
							T.show(App.app,
									getResourcesString(R.string.collectresource_succeed));
						} else {
							hasCollect = !(Boolean) mParams[1];
							ChangeLocalInfo(1, 0, hasCollect);
							T.show(App.app, "取消收藏");
						}
						InfoWallActivity.isNeedRefresh = true;
					} else {
						sendHandlerErrStrMsg();
					}
				} else {
					sendHandlerErrStrMsg();
				}
				break;
			case 4:
				spdDialog.cancelProgressDialog(null);
				if (null != mcResult) {
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						InfoWallActivity.isNeedRefresh = true;
						// adapter_UrlPager.removeResourece(mIndex);
						// adapter_UrlPager.notifyDataSetChanged();
						T.show(App.app,
								getResourcesString(R.string.deleteresource_succeed));
						ChangeLocalInfo(3, 0, false);

					} else {
						sendHandlerErrStrMsg();
					}
				} else {
					sendHandlerErrStrMsg();
				}
				break;
			case 5:
				spdDialog.cancelProgressDialog(null);
				if (null == mcResult || 1 != mcResult.getResultCode()) {
					sendHandlerErrStrMsg();
				} else {
					InfoWallActivity.isNeedRefresh = true;
					showTip("已分享！");
				}
				break;
			case 6:
				spdDialog.cancelProgressDialog(null);
				if (null != mcResult && mcResult.getResultCode() == 1) {
					if ((Integer) mParams[6] > 0)
						if (mIsOpenPage) {
							T.show(App.app, "已分享，若接收人不在线，将使用您" + mParams[6]
									+ "个圈币");
						} else {
							T.show(App.app, "已分享，若接收人不在线，将使用圈子" + mParams[6]
									+ "个圈币");
						}
					else
						T.show(App.app, "已分享！");
				} else {
					sendHandlerErrStrMsg();
				}
				break;
			}
		}
	}

	/**
	 * 表情输入
	 */
	private void showFace() {
		BaseData.hideKeyBoard(this);
		mIv_Face.setImageResource(R.drawable.keyboardbtn);
		faceView.setVisibility(View.VISIBLE);
		if (0 == faceView.getChildCount()) {
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
		mIv_Face.setImageResource(R.drawable.icon_face);
		BaseData.showKeyBoard(this, view);
	}

	/**
	 * 无输入
	 * 
	 */
	private void hideKeyBoardFace() {
		faceView.setVisibility(View.GONE);
		mIv_Face.setImageResource(R.drawable.icon_face);
		BaseData.hideKeyBoard(this);

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
			} else if (isComment) {
				CommentManage();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerErrStrMsg() {
		L.d(TAG, T.ErrStr);
		Message msg = Message.obtain();
		msg.what = HANDLER_ONE;
		msg.obj = T.ErrStr;
		this.getHandler().sendMessage(msg);
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
		this.getHandler().sendMessage(msg);
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
	 * 确认sd是否存在
	 * 
	 * @return
	 */
	protected boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 跳转界面
	 * 
	 * @param GroupId
	 *            需要传递的信息
	 **/
	private void startActivityByMember(Type type, int num) {
		Bundle bundle = new Bundle();
		try {
			bundle.putInt(BundleKey.ID_GROUP, Integer.parseInt(id_ObjectGroup));
			bundle.putInt(BundleKey.ID_QUUBOO, Integer.parseInt(id_Photo));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		bundle.putString(BundleKey.TYPE_OBJECT, type_Object);
		bundle.putSerializable(BundleKey.TYPE_REQUEST, type);
		LogicUtil.enter(this, MemberListActivity.class, bundle, 0);
	}

	/**
	 * 跳转界面
	 * 
	 * @param GroupId
	 *            需要传递的信息
	 **/
	private void showComment(int num) {
		Bundle bundle = new Bundle();
		bundle.putString("groupId", id_ObjectGroup);
		bundle.putString("photoId", id_Photo);
		bundle.putString("type_Object", type_Object);
		bundle.putInt("num_Total", num);
		LogicUtil.enter(this, CommentListActivity.class, bundle, 0);
	}

	private void setInfo(int position) {
		ObjectInfoBean bean = null;
		ResourceBean bean_Resource = null;
		L.d(TAG, "setInfo" + "position" + position);
		if (fromPhotoGrid) {
			bean_Resource = (ResourceBean) adapter_UrlPager.getItem(position);
			if (null != bean_Resource) {
				if (bean_Resource.getObjectInfo().size() > 0) {
					bean = bean_Resource.getObjectInfo().get(0);
				}
			}
		} else {
			bean = (ObjectInfoBean) adapter_UrlPager.getItem(position);
		}
		if (null != bean) {
			bean_Resource = bean.getResourceBean();
		}
		if (null != bean_Resource) {
			setDetailsInfo(bean_Resource);
		} else {
			setDefaultInfo();
		}
	}

	/**
	 * 
	 * @param type
	 *            0评论数,1收藏,2觉得赞,3删除
	 * @param num
	 * @param flag
	 */
	private void ChangeLocalInfo(int type, int num, boolean flag) {
		switch (type) {
		case 0:// 评论数
			if (fromPhotoGrid) {
				((ResourceBean) adapter_UrlPager.getItem(mIndex))
						.getObjectInfo().get(0).getResourceBean()
						.setCommentNum(num);
			} else {
				((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
						.getResourceBean().setCommentNum(num);
			}
			break;
		case 1:// 收藏
			if (fromPhotoGrid) {
				// ((ResourceBean) adapter_UrlPager.getItem(mIndex))
				// .getObjectInfo().get(0).getResourceBean()
				// .setHasCollect(flag);
				adapter_UrlPager.remove(mIndex);
			} else {
				// ((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
				// .getResourceBean().setHasCollect(flag);
				adapter_UrlPager.remove(mIndex);
			}
			break;
		case 2:// 觉得赞
			L.d(TAG, "praise" + num + flag + "index" + mIndex);
			if (fromPhotoGrid) {
				((ResourceBean) adapter_UrlPager.getItem(mIndex))
						.getObjectInfo().get(0).getResourceBean()
						.setHasPraise(flag);
				((ResourceBean) adapter_UrlPager.getItem(mIndex))
						.getObjectInfo().get(0).getResourceBean()
						.setPraiseNum(num);
			} else {
				((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
						.getResourceBean().setHasPraise(flag);
				((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
						.getResourceBean().setPraiseNum(num);
			}
			break;
		case 3:// delete
			if (fromPhotoGrid) {
				((ResourceBean) adapter_UrlPager.getItem(mIndex))
						.getObjectInfo().get(0).getResourceBean()
						.setIsDeleteResource(2);
				((ResourceBean) adapter_UrlPager.getItem(mIndex))
						.getObjectInfo().get(0).getResourceBean()
						.setObjectId(0);
			} else {
				((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
						.getResourceBean().setIsDeleteResource(2);
				((ObjectInfoBean) adapter_UrlPager.getItem(mIndex))
						.getResourceBean().setObjectId(0);
			}
			setDefaultInfo();
			break;
		case 4:// delete choosed
			UrlTouchImageView iv = (UrlTouchImageView) vp_Gallery.mCurrentView
					.getParent();
			ViewGroup vg = (ViewGroup) iv.getParent();
			mOChooseds.remove(mIndex);
			mChooseds.remove(mIndex);
			adapter_UrlPager.remove(mIndex);
			if (mOChooseds.size() > 0) {
				if (mIndex == mOChooseds.size() - 1) {
					vp_Gallery.setCurrentItem(mIndex - 1);
					adapter_UrlPager.destroyItem(vg, mIndex, iv);
				} else {
					vp_Gallery.setCurrentItem(mIndex + 1);
					adapter_UrlPager.destroyItem(vg, mIndex, iv);
					adapter_UrlPager.instantiateItem(vg, mIndex - 1);
					adapter_UrlPager.notifyDataSetChanged();
					// adapter_UrlPager.instantiateItem(vg, mIndex-1);
				}
			} else {
				Intent data = getIntent();
				Bundle bundle = data.getExtras();
				bundle.putStringArrayList("oChooseds",
						(ArrayList<String>) mOChooseds);
				bundle.putStringArrayList("chooseds",
						(ArrayList<String>) mChooseds);
				data.putExtras(bundle);
				setResult(RESULT_OK, data);
				finish();
			}
			break;
		}
	}

	public void menuManage() {
		if (!menuIsShow) {
			mLlayout_TitleBox.setVisibility(View.VISIBLE);
			mLlayout_MenuBox.setVisibility(View.VISIBLE);
			menuIsShow = true;
		} else {
			mLlayout_TitleBox.setVisibility(View.GONE);
			mLlayout_MenuBox.setVisibility(View.GONE);
			menuIsShow = false;
		}
	}

	public void CommentManage() {
		if (!isComment) {
			mLlayout_Menu.setVisibility(View.GONE);
			mLlayout_Title.setVisibility(View.GONE);
			mLlayout_CommentBottomBox.setVisibility(View.VISIBLE);
			mLlayout_CommentTitleBox.setVisibility(View.VISIBLE);
			mTv_Commentnum.setText(String.valueOf(num_Comment));
			if (num_Comment == 0)
				mTv_Commentnum.setVisibility(View.GONE);
			else
				mTv_Commentnum.setVisibility(View.VISIBLE);
			isComment = true;
		} else {
			mLlayout_Menu.setVisibility(View.VISIBLE);
			mLlayout_Title.setVisibility(View.VISIBLE);
			mLlayout_CommentBottomBox.setVisibility(View.GONE);
			mLlayout_CommentTitleBox.setVisibility(View.GONE);
			isComment = false;
		}
	}

	private void menuCanUse(boolean canUse) {
		if (canUse) {
			mIv_Comment.setEnabled(true);
			mIv_Praise.setEnabled(true);
			mIv_Share.setEnabled(true);
			mIv_More.setEnabled(true);
		} else {
			mIv_Comment.setEnabled(false);
			mIv_Praise.setEnabled(false);
			mIv_Share.setEnabled(false);
			mIv_More.setEnabled(false);
		}
	}

	private void setIndex(int index) {
		mTv_Index.setText(index + "/" + amount);
	}

	private boolean isHasAuthority() {
		L.d(TAG + ":mIsCooperationLeaguer", "mIsCooperationLeaguer:"
				+ mIsCooperationLeaguer);
		if (mIsOpenPage || mIsCooperationLeaguer)
			return true;
		boolean isHasAuthority = true;
		L.d(TAG, "state_Authority" + state_Authority);
		if (state_Authority != HASAUTHORITY) {
			isHasAuthority = false;
			showTip("您不是该圈子成员，请先加入圈子");
		}
		return isHasAuthority;
	}

	@Override
	public void onChosen(String text, int resId) {
		FacesView.doEditChange(this, mEdit_CommentInfo, text, resId);
	}

}
