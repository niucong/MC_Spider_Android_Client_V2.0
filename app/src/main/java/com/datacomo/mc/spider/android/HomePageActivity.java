package com.datacomo.mc.spider.android;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.enums.ApiTypeEnum;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.fragmt.EnterGroupChat;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIOpenPageRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.GroupBackGroundBean;
import com.datacomo.mc.spider.android.net.been.GroupBasicTrendBean;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerPermissionBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberAddressBean;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberContactBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MemberStatusBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.groupchat.ChatLeaguerUnreadNumberBean;
import com.datacomo.mc.spider.android.net.been.map.MapOpenPageFanBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.GreetUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class HomePageActivity extends BasicActionBarActivity {
	private final static String TAG = "HomePageActivity";

	private static final int FROM_GALLERY = 0;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;
	public static final int SEND_MOOD = 3;
	private static final int ADD_GROUP = 4;

	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private File headPic;
	private Bitmap headimgbit;

	private int startY;

	private LinearLayout homepage_up, home_page_up, box_leaveWord;
	private RelativeLayout home_down;

	private ImageView home_head;
	private TextView home_feelword_tv, tv_dynamic, tv_message, visitors,
			dynamic, personal_message, tv_leaveWord;
	private ImageView homeSkin;
	private LinearLayout homepage_down_visitors, homepage_down_newpic,
			homepage_down_dynamic, homepage_down_pmsg, homepage_down_leaveWord;
	private ImageView visitor_pic_1, visitor_pic_2, visitor_pic_3,
			visitor_pic_4, visitor_pic_5;
	private ImageView new_pic_1, new_pic_2, new_pic_3, new_pic_4, new_pic_5;
	private ImageView visitors_image, newPic_image, dynamic_image,
			perInfo_image, leaveWord_image;
	private Button home_button, home_contact_button;
	private Button btn_phone, btn_secret, btn_message, btn_hellow;
	private LinearLayout home_contact;
	private GridView home_faces;
	// private LinearLayout squareman;
	private TextView quanren_btn;
	// private TextView exit_btn;
	private RelativeLayout rlayout_join_circle_chat;
	private TextView txt_GroupChat_UnreadNum;
	private TextView txt_GroupChat_Json_GroupChat;

	private Animation mScaleIn, mScaleOut, faces_in, faces_out;

	private ArrayList<String> visitor_memberId = new ArrayList<String>();
	private ArrayList<ImageView> visitor_pic = new ArrayList<ImageView>();
	private ArrayList<ImageView> new_pic = new ArrayList<ImageView>();
	private ArrayList<String> visitor_head = new ArrayList<String>();
	private ArrayList<String> new_head = new ArrayList<String>();

	private boolean isGone = true;
	private boolean isFaces = true;
	private boolean isFans = true;
	private boolean canExit;
	private String[] s;
	private int visitMemberNum;
	private int fansNumbers;
	private MemberBasicBean memberBasicBean;
	private GroupBean groupBean;
	private String phone_Member;
	// private MemberContactUtil contact_Member;

	private String memberId, groupId, openPageId;
	private String groupName, groupPost, feelword;
	private int upGroupNum, downGroupNum, cooGroupNum;
	private String joinGroupStatus;

	public static HomePageActivity homePage;

	@Override
	protected void onDestroy() {
		enterHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.homepage);
		homePage = this;

		init();
		// 初始化标头信息
		// setTitle("主页信息", R.drawable.homepage_return, R.drawable.home_member);
		// setTitle("", R.drawable.title_fanhui, null);
		ab.setTitle("");
		loadData();
	}

	private void loadData() {
		s = getIntentMsg();
		setOnClick();
		try {
			if (s[1].equals("MemberId")) {
				memberId = s[0];
				new loadInfoTask(ApiTypeEnum.GETMEMBERBASICINFO,
						HomePageActivity.this, memberId).execute();
				new loadInfoTask(ApiTypeEnum.OTHERMEMBERTRENDS,
						HomePageActivity.this, memberId).execute();
				new loadInfoTask(ApiTypeEnum.VISITFRIENDLISTFORIPHONE,
						HomePageActivity.this, memberId).execute();
				new loadInfoTask(ApiTypeEnum.GETMEMBERPHOTOSLISTS,
						HomePageActivity.this, memberId).execute();
				new loadInfoTask(ApiTypeEnum.MEMBERSKIN, HomePageActivity.this,
						memberId).execute();
			} else if (s[1].equals("GroupId")) {
				groupId = s[0];
				home_down.setVisibility(View.GONE);
				new loadInfoTask(ApiTypeEnum.GROUPINFO, HomePageActivity.this,
						groupId).execute();
				new loadInfoTask(ApiTypeEnum.GROUPSKIN, HomePageActivity.this,
						groupId).execute();
			} else if (s[1].equals("OpenPageId")) {
				openPageId = s[0];
				box_leaveWord.setVisibility(View.VISIBLE);
				new loadInfoTask(ApiTypeEnum.OPENPAGEINFO,
						HomePageActivity.this, openPageId).execute();
				new loadInfoTask(ApiTypeEnum.OPENPAGERESOURCE,
						HomePageActivity.this, openPageId).execute();
				new loadInfoTask(ApiTypeEnum.OPENPAGEPHOTO,
						HomePageActivity.this, openPageId).execute();
				new loadInfoTask(ApiTypeEnum.OPENPAGELEAVEMESSAGE,
						HomePageActivity.this, openPageId).execute();
				new loadInfoTask(ApiTypeEnum.OPENPAGEFANS,
						HomePageActivity.this, openPageId).execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		mScaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
		mScaleOut = AnimationUtils.loadAnimation(this, R.anim.scale_out);
		faces_in = AnimationUtils.loadAnimation(this, R.anim.faces_in);
		faces_out = AnimationUtils.loadAnimation(this, R.anim.faces_out);
		home_page_up = (LinearLayout) findViewById(R.id.homepage_up);
		home_down = (RelativeLayout) findViewById(R.id.home_down);

		LayoutInflater inflater = LayoutInflater.from(HomePageActivity.this);
		homepage_up = (LinearLayout) inflater.inflate(R.layout.homepage_up,
				null);
		homepage_up.setPadding(0, -65, 0, -65);
		home_page_up.addView(homepage_up);

		home_head = (ImageView) homepage_up.findViewById(R.id.home_head);

		home_feelword_tv = (TextView) homepage_up
				.findViewById(R.id.home_feelword_tv);
		home_feelword_tv.setMovementMethod(ScrollingMovementMethod
				.getInstance());

		visitors = (TextView) findViewById(R.id.visitors);
		dynamic = (TextView) findViewById(R.id.dynamic);
		personal_message = (TextView) findViewById(R.id.personal_message);
		visitor_pic_1 = (ImageView) findViewById(R.id.visitor_pic_1);
		visitor_pic_2 = (ImageView) findViewById(R.id.visitor_pic_2);
		visitor_pic_3 = (ImageView) findViewById(R.id.visitor_pic_3);
		visitor_pic_4 = (ImageView) findViewById(R.id.visitor_pic_4);
		visitor_pic_5 = (ImageView) findViewById(R.id.visitor_pic_5);
		new_pic_1 = (ImageView) findViewById(R.id.new_pic_1);
		new_pic_2 = (ImageView) findViewById(R.id.new_pic_2);
		new_pic_3 = (ImageView) findViewById(R.id.new_pic_3);
		new_pic_4 = (ImageView) findViewById(R.id.new_pic_4);
		new_pic_5 = (ImageView) findViewById(R.id.new_pic_5);
		visitors_image = (ImageView) findViewById(R.id.visitors_image);
		newPic_image = (ImageView) findViewById(R.id.newPic_image);
		dynamic_image = (ImageView) findViewById(R.id.dynamic_image);
		perInfo_image = (ImageView) findViewById(R.id.perInfo_image);
		leaveWord_image = (ImageView) findViewById(R.id.leaveWord_image);
		home_button = (Button) homepage_up.findViewById(R.id.home_button);
		home_contact_button = (Button) homepage_up
				.findViewById(R.id.home_contact_button);
		btn_phone = (Button) homepage_up.findViewById(R.id.btn_phone);
		btn_secret = (Button) homepage_up.findViewById(R.id.btn_secret);
		btn_message = (Button) homepage_up.findViewById(R.id.btn_message);
		btn_hellow = (Button) homepage_up.findViewById(R.id.btn_hellow);
		tv_dynamic = (TextView) findViewById(R.id.tv_dynamic);
		tv_message = (TextView) findViewById(R.id.tv_message);
		tv_leaveWord = (TextView) findViewById(R.id.tv_leaveWord);
		homeSkin = (ImageView) homepage_up.findViewById(R.id.home_skin);
		home_contact = (LinearLayout) findViewById(R.id.home_contact);
		home_faces = (GridView) findViewById(R.id.home_faces);

		box_leaveWord = (LinearLayout) findViewById(R.id.box_leaveWord);

		// squareman = (LinearLayout) findViewById(R.id.squareman);
		quanren_btn = (TextView) findViewById(R.id.quanren_btn);
		// exit_btn = (TextView) findViewById(R.id.exit_btn);
		rlayout_join_circle_chat = (RelativeLayout) findViewById(R.id.rlayout_join_circle_chatbox);

		txt_GroupChat_Json_GroupChat = (TextView) findViewById(R.id.txt_join_circle_chat);
		Drawable temp_Drawable = getBroudDrawable(R.drawable.icon_ingrouptalk);
		txt_GroupChat_Json_GroupChat.setCompoundDrawables(temp_Drawable, null,
				null, null);
		txt_GroupChat_Json_GroupChat.setCompoundDrawablePadding(1);

		txt_GroupChat_UnreadNum = (TextView) findViewById(R.id.txt_circle_chat_unreadnum);
		homepage_down_visitors = (LinearLayout) findViewById(R.id.homepage_down_visitors);
		homepage_down_newpic = (LinearLayout) findViewById(R.id.homepage_down_newpic);
		homepage_down_dynamic = (LinearLayout) findViewById(R.id.homepage_down_dynamic);
		homepage_down_pmsg = (LinearLayout) findViewById(R.id.homepage_down_pmsg);
		homepage_down_leaveWord = (LinearLayout) findViewById(R.id.homepage_down_leaveWord);

		visitor_pic.add(visitor_pic_1);
		visitor_pic.add(visitor_pic_2);
		visitor_pic.add(visitor_pic_3);
		visitor_pic.add(visitor_pic_4);
		visitor_pic.add(visitor_pic_5);

		new_pic.add(new_pic_1);
		new_pic.add(new_pic_2);
		new_pic.add(new_pic_3);
		new_pic.add(new_pic_4);
		new_pic.add(new_pic_5);

		// contact_Member = new MemberContactUtil(HomePageActivity.this);
	}

	private void setOnClick() {
		home_head.setOnClickListener(this);
		home_button.setOnClickListener(this);
		home_contact_button.setOnClickListener(this);
		btn_phone.setOnClickListener(this);
		btn_secret.setOnClickListener(this);
		btn_message.setOnClickListener(this);
		btn_hellow.setOnClickListener(this);

		visitor_pic_1.setOnClickListener(this);
		visitor_pic_2.setOnClickListener(this);
		visitor_pic_3.setOnClickListener(this);
		visitor_pic_4.setOnClickListener(this);
		visitor_pic_5.setOnClickListener(this);

		homepage_down_visitors.setOnClickListener(this);
		homepage_down_newpic.setOnClickListener(this);
		homepage_down_dynamic.setOnClickListener(this);
		// homepage_down_pmsg.setOnClickListener(this);
		homepage_down_leaveWord.setOnClickListener(this);

		newPic_image.setOnClickListener(this);

		perInfo_image.setOnClickListener(this);
		leaveWord_image.setOnClickListener(this);

		home_feelword_tv.setOnClickListener(this);
	}

	/**
	 * 根据memberId获取社员基本信息。
	 * 
	 * @param context
	 * @param memberId
	 * @throws Exception
	 */
	private void getMemberBasicInfo(MCResult mcResult) {
		// MCResult mcResult = APIRequestServers.getMemberBasicInfo(context,
		// memberId);
		MemberBean memberBean = (MemberBean) mcResult.getResult();
		// 获取联系人的电话。
		MemberContactBean bean_MemberContact = memberBean.getContactInfo();
		if (null != bean_MemberContact)
			phone_Member = bean_MemberContact.getMemberPhone();
		// 朋友状态 :0：自己 1：朋友 2：朋友的朋友 3：申请状态 4：陌生人
		int friendStatus = memberBean.getFriendStatus();
		L.d(TAG, "getMemberBasicInfoTest  friendStatus" + friendStatus);
		if (friendStatus == 0) {
			// 获取自己的圈币数量
			int memberGold = memberBean.getAccountInfo().getMemberGold();
			home_button.setText("圈币(" + memberGold + ")");
			home_button.setBackgroundResource(R.drawable.quanbi_bg);
			home_button.setVisibility(View.VISIBLE);
			home_button.setClickable(false);
		} else if (friendStatus == 1) {
			home_contact_button.setVisibility(View.VISIBLE);
		} else {
			home_button.setVisibility(View.VISIBLE);
			home_button.setText("加朋友圈");
		}
		// 获取现居住地址信息
		MemberAddressBean memberAddressBean = (MemberAddressBean) memberBean
				.getResidenceAddress();
		String mAddress = memberAddressBean.getAddressName();
		if (mAddress == null) {
			mAddress = "";
		}

		// 获取基本信息
		memberBasicBean = (MemberBasicBean) memberBean.getBasicInfo();
		dynamic_image.setOnClickListener(this);
		// 获取生日信息
		String birthday = memberBasicBean.getBirthday();

		int sex = 0;
		try {
			sex = memberBasicBean.getMemberSex();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String birth = "", address = "";
		try {
			birth = ConstantUtil.YYYYMMDD.format(DateTimeUtil
					.getLongTime(birthday));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			address = memberAddressBean.getAddressName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显示个人信息
		String message = isSex(sex) + isNull(birth) + "  " + isNull(address);

		tv_message.setText(message);
		// 显示个人心情
		feelword = memberBasicBean.getFeelingWord();
		if (feelword != null && !feelword.equals("")) {
			feelword = feelword.replace("&nbsp;", "");
			home_feelword_tv.setText(feelword);
		} else {
			if (sex == 1) {
				home_feelword_tv.setText("他还没有发表过心情哦~~~~");
			} else if (sex == 2) {
				home_feelword_tv.setText("她还没有发表过心情哦~~~~");
			} else {
				home_feelword_tv.setText("TA还没有发表过心情哦~~~~");
			}
		}

		// 显示title
		// setTitle(memberBasicBean.getMemberName(), R.drawable.title_fanhui,
		// R.drawable.title_home);
		ab.setTitle(memberBasicBean.getMemberName());
		// 获取头像图片地址并用异步加载显示
		MemberHeadBean memberHeadBean = memberBasicBean.getHeadImage();
		String head_Url = memberHeadBean.getHeadUrl()
				+ memberHeadBean.getHeadPath();
		head_Url = ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		setHeadUrl(head_Url);
		// home_head.setImageDrawable(drawable);
		// 获取访客的数量并显示
		MemberStatusBean statusBean = memberBean.getStatusInfo();
		visitMemberNum = statusBean.getVisitMemberNum();
		if (visitMemberNum > 0) {
			if (visitMemberNum > 99) {
				visitors.setText("访客(99+)");
			} else {
				visitors.setText("访客(" + visitMemberNum + ")");
			}
		} else {
			visitors.setText("访客");
		}
		visitors_image.setOnClickListener(this);
		// 显示动态信息的总数量
		int trendNum = memberBean.getFriendTrendNum();
		if (trendNum > 0) {
			if (trendNum > 99) {
				dynamic.setText("动态(99+)");
			} else {
				dynamic.setText("动态(" + trendNum + ")");
			}
		} else {
			dynamic.setText("动态");
		}
	}

	public void setHeadUrl(String head_Url) {
		// Drawable drawable = new AsyncImageDownLoad(head_Url,
		// new String[] { head_Url }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
		// ConstantUtil.HEAD_PATH, HomePageActivity.this,
		// "homepageactivity_1", new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// home_head.setImageDrawable((Drawable) object);
		// }
		// }).getDrawable();
		// home_head.setImageDrawable(drawable);
		// TODO
		MyFinalBitmap.setHeader(this, home_head, head_Url);
	}

	/**
	 * 根据传进来的参数判断性别
	 * 
	 * @param i
	 * @return
	 */
	private String isSex(int i) {
		if (i == 1) {
			return "男   ";
		} else if (i == 2) {
			return "女   ";
		}
		return "";
	}

	private String isNull(String info) {
		if (info == null) {
			return "";
		}
		return info;
	}

	/**
	 * 获取动态列表
	 * 
	 * @throws Exception
	 */
	private void myTrends(MCResult mcResult, TextView tv) {
		L.i(TAG, "myTrends...");
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null && objectList.size() > 0) {
			ResourceTrendBean resourceTrendBean = (ResourceTrendBean) objectList
					.get(0);
			String actionType = resourceTrendBean.getActionType();
			// if ("PRAISE_RESOURCE".equals(actionType)) {
			// tv.setText("觉得赞");
			// } else
			final GroupBasicTrendBean resBean = resourceTrendBean
					.getGroupBasicTrendBean();
			if ("COMMENT_RESOURCE".equals(actionType)) {
				String actinContent = resourceTrendBean.getActinContent();
				if (actinContent == null || "".equals(actinContent)) {
					actinContent = "评论资源";
				}
				tv.setText(actinContent);
			} else if ("JOIN_GROUP".equals(actionType)) {
				tv.setText(resBean.getGroupName() + " 有新成员 "
						+ resBean.getMemberName());
			} else if ("APPOINT_MANAGER".equals(actionType)) {
				tv.setText(resBean.getMemberName() + " 成为 "
						+ resBean.getGroupName() + " 管理员");
			} else if ("CHANGE_POSTER".equals(actionType)) {
				tv.setText(resBean.getGroupName() + " 更换海报");
			} else if ("COOPERATE_GROUP".equals(actionType)) {
				tv.setText(resBean.getGroupName() + " 和 "
						+ resBean.getSecondGroupName() + " 合作");
			} else if ("BECOME_SUBGROUP".equals(actionType)) {
				tv.setText(resBean.getGroupName() + " 成为 "
						+ resBean.getSecondGroupName() + " 的下级圈子");
			} else if ("MERGER_GROUP".equals(actionType)) {
				tv.setText(resBean.getSecondGroupName() + " 合并了 "
						+ resBean.getGroupName());
			} else {
				ResourceBean resourceBean = resourceTrendBean.getResourceBean();
				List<ObjectInfoBean> objectInfo = resourceBean.getObjectInfo();
				if (objectInfo != null && objectInfo.size() > 0) {

					for (int i = 0; i < objectInfo.size(); i++) {
						// 取第一个动态信息并显示
						ObjectInfoBean objectInfoBean = objectInfo.get(i);
						String objSourceType = objectInfoBean
								.getObjSourceType();
						if ("OBJ_GROUP_TOPIC".equals(objSourceType)
								|| "OBJ_OPEN_PAGE_TOPIC".equals(objSourceType)) {
							String msg = objectInfoBean.getObjectDescription();
							if (msg == null || msg.equals("null")
									|| msg.equals("")) {
								msg = objectInfoBean.getObjectName();
								if (msg == null || msg.equals("null")
										|| msg.equals("")) {
									msg = "分享资源";
								}
							}
							try {
								msg = CharUtil.filterText(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}
							tv.setText(msg);
							break;
						} else if ("OBJ_GROUP_FILE".equals(objSourceType)) {
							tv.setText("分享文件");
							break;
						} else if ("OBJ_GROUP_PHOTO".equals(objSourceType)) {
							tv.setText("分享照片");
							break;
						} else if ("OBJ_OPEN_PAGE_LEAVEMESSAGE"
								.equals(objSourceType)) {
							int guestbookType = resourceBean.getGuestbookType();
							String msg = "";
							// 留言类型：1-留言 2-咨询 3-投诉 4-表扬
							if (guestbookType == 1) {
								msg = "【留言】";
							} else if (guestbookType == 2) {
								msg = "【咨询】";
							} else if (guestbookType == 3) {
								msg = "【投诉】";
							} else if (guestbookType == 4) {
								msg = "【表扬】";
							}
							msg += objectInfoBean.getObjectDescription();
							try {
								msg = CharUtil.filterText(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}
							tv.setText(msg);
							break;
						} else if ("OBJ_MEMBER_RES_LEAVEMESSAGE"
								.equals(objSourceType)) {
							String msg = objectInfoBean.getObjectDescription();
							if (msg != null && !"".equals(msg)) {
								tv.setText(msg);
								break;
							}
						} else if ("OBJ_MEMBER_RES_MOOD".equals(objSourceType)) {
							String msg = objectInfoBean.getObjectDescription();
							if (msg != null && !"".equals(msg)) {
								tv.setText(msg);
								break;
							}
						}
					}
				} else {
					tv.setText("暂无动态");
				}
			}
		} else {
			tv.setText("暂无动态");
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// L.d(TAG, "canExit" + canExit);
	// if (!canExit)
	// return false;
	// menu.add(Menu.NONE, 0, 1, "退出圈子");// 必须创建一项
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// new AlertDialog.Builder(HomePageActivity.this).setTitle("提示")
	// .setMessage("您确定要退出 " + groupName + " 圈子吗？")
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// spdDialog.showProgressDialog("正在处理中...");
	// new loadInfoTask(ApiTypeEnum.EXITGROUP,
	// HomePageActivity.this, groupId).execute();
	// }
	// }).setNegativeButton("取消", null).show();
	// return false;
	//
	// }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		if (!canExit)
			return false;
		menu.add(Menu.NONE, 0, 1, "退出圈子");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		new AlertDialog.Builder(HomePageActivity.this).setTitle("提示")
				.setMessage("您确定要退出 " + groupName + " 圈子吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						spdDialog.showProgressDialog("正在处理中...");
						new loadInfoTask(ApiTypeEnum.EXITGROUP,
								HomePageActivity.this, groupId).execute();
					}
				}).setNegativeButton("取消", null).show();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 获取最近访问人的列表
	 * 
	 * @throws Exception
	 */
	private void getVisitFriendList(MCResult mcResult) {
		L.i(TAG, "getVisitFriendList...");
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			for (Object object : objectList) {
				FriendBean friendBean = (FriendBean) object;
				String info = friendBean.getMemberHeadUrl()
						+ friendBean.getMemberHeadPath();
				info = ThumbnailImageUrl.getThumbnailHeadUrl(info,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				String memberId = friendBean.getMemberId() + "";
				visitor_head.add(info);
				visitor_memberId.add(memberId);
			}
		}
		setHeadInfo(visitor_pic, visitor_head);
	}

	/**
	 * 获取成员的最新图片地址
	 * 
	 * @param context
	 * @param memberId
	 * @throws Exception
	 */
	private void getPhotosList(MCResult mcResult) {
		L.i(TAG, "getPhotosList...");
		MapResourceBean mapBean = (MapResourceBean) mcResult.getResult();
		ArrayList<ResourceBean> rbList = (ArrayList<ResourceBean>) mapBean
				.getLIST();
		if (rbList == null) {

		} else {
			for (ResourceBean resourceBean : rbList) {
				String newPic_Url = resourceBean.getObjectInfo().get(0)
						.getObjectUrl()
						+ resourceBean.getObjectInfo().get(0).getObjectPath();
				newPic_Url = ThumbnailImageUrl.getThumbnailImageUrl(newPic_Url,
						ImageSizeEnum.SEVENTY_TWO);
				new_head.add(newPic_Url);
				L.d(TAG, "getMemberPhotos new_head:" + new_head);
			}
			setImageInfo(new_pic, new_head);
		}
	}

	/**
	 * 设定圈子的主要信息
	 * 
	 * @param groupBean
	 */
	private void getGroupMainInfo(GroupBean groupBean) {
		dynamic_image.setOnClickListener(this);

		groupName = groupBean.getGroupName();

		GroupBackGroundBean groupBackGroundBean = groupBean
				.getGroupBackGroundBean();
		groupId = groupBackGroundBean.getGroupId() + "";

		// 获取圈子海报地址并显示
		groupPost = groupBean.getGroupPosterUrl()
				+ groupBean.getGroupPosterPath();
		String groupPostTh = ThumbnailImageUrl.getThumbnailPostUrl(groupPost,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setPoster(this, home_head, groupPostTh);

		// 显示圈子描述
		String gourpDescription = groupBean.getGroupDescription();
		if (gourpDescription != null && !gourpDescription.equals("")) {
			home_feelword_tv.setText(gourpDescription);
		} else {
			home_feelword_tv.setText("管理员还没有填写简介。简介可以是介绍圈子的一句话，也可以是共同目标或口号哦。");
		}
	}

	/**
	 * 获取圈子详情
	 * 
	 * @param groupId
	 *            圈子Id
	 * @throws Exception
	 */
	private void getGroupInfo(MCResult mcResult) {
		groupBean = (GroupBean) mcResult.getResult();
		getGroupMainInfo(groupBean);
		// 显示title
		// setTitle(groupName, R.drawable.title_fanhui,
		// R.drawable.title_header_relase);

		joinGroupStatus = groupBean.getJoinGroupStatus();

		if ("GROUP_LEAGUER".equals(joinGroupStatus)
				|| "GROUP_MANAGER".equals(joinGroupStatus)
				|| "APPLY_MANAGER".equals(joinGroupStatus)) {
			canExit = true;
		}
		L.d(TAG, "getGroupMainInfo joinGroupStatus=" + joinGroupStatus);
		if (!s[1].equals("OpenPageId") && joinGroupStatus != null
				&& !"".equals(joinGroupStatus)
				&& !"NO_RELATION".equals(joinGroupStatus)) {
			rlayout_join_circle_chat.setVisibility(View.VISIBLE);
			txt_GroupChat_Json_GroupChat.setOnClickListener(this);
			new loadInfoTask(ApiTypeEnum.CHATLEAGUERUNREADNUMBER,
					getApplicationContext(), groupId).execute();
		} else {
			rlayout_join_circle_chat.setVisibility(View.GONE);
		}

		if ("NO_RELATION".equals(joinGroupStatus)) {
			int i = groupBean.getOpenStatus();
			if (i == 1) {
				home_button.setText("申请加入");
				new loadInfoTask(ApiTypeEnum.SQUAREMAN, HomePageActivity.this,
						groupId).execute();
				new loadInfoTask(ApiTypeEnum.GETGROUPPHOTOSLISTS,
						HomePageActivity.this, groupId).execute();
				new loadInfoTask(ApiTypeEnum.SINGLEGROUPRESOURCETRENDS,
						HomePageActivity.this, groupId).execute();
				new loadInfoTask(ApiTypeEnum.GROUPLEAGUERLIST,
						HomePageActivity.this, groupId).execute();
				home_down.setVisibility(View.VISIBLE);
			} else {
				home_button.setText("联系圈主");
				showTip("该圈子是私密圈子，您没有浏览权限，如需加入，请联系圈主。");
			}
			home_button.setVisibility(View.VISIBLE);
			home_button.setClickable(true);
		} else {
			new loadInfoTask(ApiTypeEnum.SQUAREMAN, HomePageActivity.this,
					groupId).execute();
			new loadInfoTask(ApiTypeEnum.GETGROUPPHOTOSLISTS,
					HomePageActivity.this, groupId).execute();
			new loadInfoTask(ApiTypeEnum.SINGLEGROUPRESOURCETRENDS,
					HomePageActivity.this, groupId).execute();
			new loadInfoTask(ApiTypeEnum.GROUPLEAGUERLIST,
					HomePageActivity.this, groupId).execute();
			home_down.setVisibility(View.VISIBLE);

			if (groupBean.getGroupType() != 1) {
				// 显示圈币数量
				home_button.setText("圈币(" + groupBean.getGoldNum() + ")");
				home_button.setBackgroundResource(R.drawable.quanbi_bg);
				home_button.setVisibility(View.VISIBLE);
				home_button.setClickable(false);
			}

			// if (upGroupNum > 0 || downGroupNum > 0 || cooGroupNum > 0) {
			home_contact_button.setText("圈子图谱");
			home_contact_button.setVisibility(View.VISIBLE);
			home_contact_button.setClickable(true);
			// home_contact_button.setVisibility(View.GONE);
			// }
		}
		visitMemberNum = groupBean.getLeaguerNum();
		// 圈子成员数量
		if (visitMemberNum > 0) {
			if (visitMemberNum > 99) {
				visitors.setText("成员(99+)");
			} else {
				visitors.setText("成员(" + visitMemberNum + ")");
			}
		} else {
			visitors.setText("成员");
		}

		visitors_image.setOnClickListener(this);
		// 更改个人信息为圈子信息
		personal_message.setText("圈子信息");
		// 显示圈子信息
		// tv_message.setText("圈子短号：" + ConstantUtil.GROUPSHORT
		// + groupBean.getGroupShort());

		// 圈子动态数量
		int trendNum = groupBean.getPosterNum();
		if (trendNum > 0) {
			if (trendNum > 99) {
				dynamic.setText("动态(99+)");
			} else {
				dynamic.setText("动态(" + trendNum + ")");
			}
		} else {
			dynamic.setText("动态");
		}
	}

	/**
	 * 获取开放主页基本信息
	 * 
	 * @param mcResult
	 */
	private void getOpenPageInfo(MCResult mcResult) {
		GroupBean groupBean = (GroupBean) mcResult.getResult();
		getGroupMainInfo(groupBean);
		// 显示title
		// setTitle(groupName, R.drawable.title_fanhui, R.drawable.title_home);
		ab.setTitle(groupName);
		isFans = groupBean.isFans();
		if (isFans) {
			home_button.setText("取消关注");
			home_button.setVisibility(View.VISIBLE);
			home_button.setClickable(true);
		} else {
			home_button.setText("我要关注");
			home_button.setVisibility(View.VISIBLE);
			home_button.setClickable(true);
		}

		// tv_leaveWord.setText("留言("+groupBean.get+")");

		visitors_image.setOnClickListener(this);
		// 更改个人信息为主页信息
		personal_message.setText("主页信息");
		// 显示主页信息
		tv_message.setText("标签：" + groupBean.getGroupTag());
		// 圈子动态数量
		int trendNum = groupBean.getPosterNum();
		if (trendNum > 0) {
			if (trendNum > 99) {
				dynamic.setText("动态(99+)");
			} else {
				dynamic.setText("动态(" + trendNum + ")");
			}
		} else {
			dynamic.setText("动态");
		}

		String openPageUrl = groupBean.getGroupBackGroundBean().getBgUrl()
				+ groupBean.getGroupBackGroundBean().getBgPath();

		// Drawable vis_drawable = PublicLoadPicture.getDrawable(
		// HomePageActivity.this, openPageUrl, homeSkin,
		// R.drawable.bg_translucent);
		// Drawable vis_drawable = PublicLoadPicture.loadOther(openPageUrl,
		// null,
		// new int[] { R.drawable.bg_translucent,
		// R.drawable.bg_translucent }, ConstantUtil.IMAGE_PATH,
		// HomePageActivity.this, "homepageactivity_3",
		// new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// if (null != object)
		// homeSkin.setImageDrawable((Drawable) object);
		// }
		// });
		// homeSkin.setImageDrawable(vis_drawable);
		// TODO
		MyFinalBitmap.setPoster(this, homeSkin, openPageUrl);
	}

	/**
	 * 获取开放主页粉丝列表
	 * 
	 * @param mcResult
	 */
	private void getOpenPageFans(MCResult mcResult) {
		MapOpenPageFanBean groupBean = (MapOpenPageFanBean) mcResult
				.getResult();
		ArrayList<GroupLeaguerBean> groupLeagureBeans = (ArrayList<GroupLeaguerBean>) groupBean
				.getOPEN_PAGE_FANS_LIST();
		fansNumbers = groupBean.getOPEN_PAGE_FANS_NUM();
		// 圈子成员数量
		if (fansNumbers > 0) {
			if (fansNumbers > 99) {
				visitors.setText("粉丝(99+)");
			} else {
				visitors.setText("粉丝(" + fansNumbers + ")");
			}
		} else {
			visitors.setText("粉丝");
		}
		if (groupLeagureBeans != null) {
			for (GroupLeaguerBean groupLeagureBean : groupLeagureBeans) {
				String info = groupLeagureBean.getMemberHeadUrl()
						+ groupLeagureBean.getMemberHeadPath();
				info = ThumbnailImageUrl.getThumbnailHeadUrl(info,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				String fansId = groupLeagureBean.getMemberId() + "";
				visitor_head.add(info);
				visitor_memberId.add(fansId);
			}
		}
		setImageInfo(visitor_pic, visitor_head);
	}

	/**
	 * 获取圈子成员列表
	 * 
	 * @param mcResult
	 */
	private void getGroupLeaguers(MCResult mcResult) {
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			for (Object object : objectList) {
				GroupLeaguerBean groupLeaguer = (GroupLeaguerBean) object;
				String info = groupLeaguer.getMemberHeadUrl()
						+ groupLeaguer.getMemberHeadPath();
				info = ThumbnailImageUrl.getThumbnailHeadUrl(info,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				String groupVisitorId = groupLeaguer.getMemberId() + "";
				visitor_head.add(info);
				visitor_memberId.add(groupVisitorId);
			}
		}
		setHeadInfo(visitor_pic, visitor_head);
	}

	/**
	 * 对图片进行异步加载显示
	 */
	private void setImageInfo(ArrayList<ImageView> pic_Views,
			ArrayList<String> pic_Url) {
		int j = pic_Url.size() - 1;
		if (j >= 4) {
			j = 4;
		}
		for (int i = 0; i <= j; i++) {
			// Drawable vis_drawable = PublicLoadPicture.getDrawable(
			// HomePageActivity.this, pic_Url.get(i), pic_Views.get(i),
			// R.drawable.defaultimg);
			// Drawable vis_drawable = PublicLoadPicture.loadImage(
			// HomePageActivity.this, pic_Url.get(i), pic_Views.get(i),
			// "homepageactivity_4");
			// pic_Views.get(i).setImageDrawable(vis_drawable);
			// TODO
			MyFinalBitmap.setImage(this, pic_Views.get(i), pic_Url.get(i));
		}
	}

	/**
	 * <<<<<<< HEAD 对头像进行异步加载显示 ======= 对图片进行异步加载显示 >>>>>>>
	 * 6cc971a1667f548aa351ba00026d13a3260b14d8
	 */
	private void setHeadInfo(ArrayList<ImageView> pic_Views,
			ArrayList<String> pic_Url) {
		int j = pic_Url.size() - 1;
		if (j >= 4) {
			j = 4;
		}
		for (int i = 0; i <= j; i++) {
			// Drawable vis_drawable = PublicLoadPicture.loadHead(
			// HomePageActivity.this, pic_Url.get(i), pic_Views.get(i),
			// "homepageactivity_5");
			// pic_Views.get(i).setImageDrawable(vis_drawable);
			// TODO
			MyFinalBitmap.setHeader(this, pic_Views.get(i), pic_Url.get(i));
		}
	}

	/**
	 * 加载成员空间背景皮肤
	 * 
	 * @param mcResult
	 */
	private void getMemberSkin(MCResult mcResult) {
		String memberSkin = (String) mcResult.getResult();
		// Drawable vis_drawable = PublicLoadPicture.getDrawable(
		// HomePageActivity.this, memberSkin, homeSkin,
		// R.drawable.bg_translucent);
		// Drawable vis_drawable = PublicLoadPicture.loadOther(memberSkin, null,
		// new int[] { R.drawable.bg_translucent,
		// R.drawable.bg_translucent }, ConstantUtil.IMAGE_PATH,
		// HomePageActivity.this, "homepageactivity_6",
		// new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// if (null != object)
		// homeSkin.setImageDrawable((Drawable) object);
		// }
		// });
		// homeSkin.setImageDrawable(vis_drawable);
		// TODO
		MyFinalBitmap.setSkin(this, homeSkin, memberSkin);
	}

	/**
	 * 加载圈子空间背景皮肤
	 * 
	 * @param mcResult
	 */
	private void getGroupSkin(MCResult mcResult) {
		String groupSkin = (String) mcResult.getResult();
		// Drawable vis_drawable = PublicLoadPicture.getDrawable(
		// HomePageActivity.this, groupSkin, homeSkin,
		// R.drawable.bg_translucent);
		// Drawable vis_drawable = PublicLoadPicture.loadOther(groupSkin, null,
		// new int[] { R.drawable.bg_translucent,
		// R.drawable.bg_translucent }, ConstantUtil.IMAGE_PATH,
		// HomePageActivity.this, "homepageactivity_7",
		// new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// if (null != object)
		// homeSkin.setImageDrawable((Drawable) object);
		// }
		// });
		// homeSkin.setImageDrawable(vis_drawable);
		// TODO
		MyFinalBitmap.setSkin(this, homeSkin, groupSkin);
	}

	/**
	 * 获取intent
	 * 
	 * @return String
	 **/
	private String[] getIntentMsg() {
		String[] s = new String[2];
		Bundle b = getIntent().getExtras();
		String Id = b.getString("Id");
		String To = b.getString("To");
		s[0] = Id;
		s[1] = To;
		return s;
	}

	/**
	 * 个人资料跳转
	 * 
	 * @param memcls
	 * @param groupcls
	 * @param s
	 */
	private void setIntentMsg(Class<?> memcls, Class<?> groupcls, String[] s) {
		if (s[1].equals("MemberId")) {
			Bundle b = new Bundle();
			b.putString("id", String.valueOf(s[0]));
			b.putString("name", memberBasicBean.getMemberName());
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			LogicUtil.enter(HomePageActivity.this, memcls, b, false);
			// if ((GetDbInfoUtil.getMemberId(HomePageActivity.this) + "")
			// .equals(memberId)) {
			// showUpHeadDialog();
			// }
		} else {
			Bundle b0 = new Bundle();
			b0.putString("Id", String.valueOf(s[0]));
			b0.putString("To", s[1]);
			LogicUtil.enter(HomePageActivity.this, groupcls, b0, false);
			// if (groupBean.getMemberId() == GetDbInfoUtil
			// .getMemberId(HomePageActivity.this)) {
			// showUpHeadDialog();
			// }
		}

	}

	/**
	 * 个人资料跳转
	 * 
	 * @param memcls
	 * @param groupcls
	 * @param s
	 */
	@SuppressWarnings("unused")
	private void setAlterHead() {
		Bundle b = new Bundle();
		b.putString("Id", String.valueOf(s[0]));
		b.putString("To", s[1]);
		if (s[1].equals("MemberId")) {
			// LogicUtil.enter(HomePageActivity.this, memcls, b, false);
			if ((GetDbInfoUtil.getMemberId(HomePageActivity.this) + "")
					.equals(memberId)) {
				showUpHeadDialog();
			}
		} else {
			if ("GROUP_OWNER".equals(joinGroupStatus)
					|| "GROUP_MANAGER".equals(joinGroupStatus)) {
				showUpHeadDialog();
			}
		}

	}

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showUpHeadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (s[1].equals("MemberId")) {
			builder.setTitle("修改头像");
		} else {
			builder.setTitle("修改海报");
		}

		String[] dialogMsg = new String[] {
				getResourcesString(R.string.selectImgdialog_fromlocality),
				getResourcesString(R.string.selectImgdialog_fromcamera)
		// , getResourcesString(R.string.selectImgdialog_cancel)
		};
		builder
		// .setItems(dialogMsg, new DialogInterface.OnClickListener() {
		.setAdapter(new ArrayAdapter(this, R.layout.choice_item, dialogMsg),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(
									Intent.createChooser(intent, "选择图片"),
									FROM_GALLERY);
							break;
						case 1:
							Intent intent1 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File saveFile = new File(headimg_file);
							if (saveFile.exists()) {
								// Log.d(TAG,"目录已存在");
							} else {
								saveFile.mkdirs();
							}
							intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
									.fromFile(new File(saveFile, headimg_name)));
							startActivityForResult(intent1, FROM_CAMERA);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		// 处理拍照返回数据
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			try {
				switch (requestCode) {
				case FROM_CAMERA:
					File tempFile = new File(headimg_file);
					File picture = new File(tempFile, headimg_name);
					uri = Uri.fromFile(picture);
					startPhotoZoom(uri);
					break;
				case FROM_GALLERY:
					uri = data.getData();
					startPhotoZoom(uri);
					break;
				case FROM_CROP:
					L.d(TAG, "onActivityResult uri=" + uri);
					File saveFile = new File(headimg_file);
					headPic = new File(saveFile, headimg_name);
					getHeadFilePath(Uri.fromFile(headPic));
					home_head.setImageDrawable(ImageDealUtil
							.bitmapToDrawable(headimgbit));

					if (headPic.exists()) {
						L.d(TAG, "onActivityResult headPic...");
						uploadHead(headPic);
					} else {
						L.i(TAG, "onActivityResult headPic。。。");
					}
					break;
				case SEND_MOOD:
					String mood = data.getStringExtra("feelword");
					L.d(TAG, "onActivityResult mood=" + mood);
					if (mood != null && !mood.equals("")
							&& !mood.equals(feelword)) {
						home_feelword_tv.setText(mood);
						feelword = mood;
					}
					break;
				case ADD_GROUP:
					Bundle bundle = data.getExtras();
					String[] chosenIds = bundle
							.getStringArray(BundleKey.CHOOSEDS);
					spdDialog.showProgressDialog("正在处理中...");
					new AddFriendTask(chosenIds).execute(memberId);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class AddFriendTask extends AsyncTask<String, Integer, MCResult> {
		private String[] groupIds;

		public AddFriendTask(String[] groupIds) {
			this.groupIds = groupIds;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.addFriendToGroup(
						HomePageActivity.this, params[0], groupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.cancel();
			}
			if (null == mcResult) {
				showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else {
					home_button.setVisibility(View.GONE);
					home_contact_button.setVisibility(View.VISIBLE);
					new Thread(new Runnable() {
						@Override
						public void run() {
							UpdateFriendListThread.updateFriendList(
									HomePageActivity.this, null);
						}
					}).start();
				}
			}
		}
	}

	/**
	 * 或许图片路径并上传
	 * 
	 * @param uriString
	 **/
	private void uploadHead(final File file) {
		if (s[1].equals("MemberId")) {
			APIRequestServers.uploadFile(HomePageActivity.this, file,
					UploadMethodEnum.UPLOADHEAD, null);
		} else if (s[1].equals("GroupId")) {
			APIRequestServers.uploadFile(HomePageActivity.this, file,
					UploadMethodEnum.UPLOADPOSTER, groupId);
		}
	}

	/**
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	private void getHeadFilePath(Uri uri) {
		String sUri = uri.toString();
		String imgPath = null;
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				imgPath = cursor.getString(1); // 图片文件路径
			} else {
				L.i(TAG, "getHeadFilePath cursor...");
			}
		} else if (sUri.startsWith("file://")
				&& (sUri.endsWith(".png") || sUri.endsWith(".jpg") || sUri
						.endsWith(".gif"))) {
			// 如果从某些第三方软件中选取文件，返回的是一个文件具体路径。
			imgPath = sUri.replace("file://", "");
		} else if (sUri.startsWith(ConstantUtil.SDCARD_PATH)) {
			// 直接获取临时图片地址
			imgPath = sUri;
		} else {
			// 返回的uri不合法或者文件不是图片。
			L.i(TAG, "getHeadFilePath uri...");
		}
		L.i(TAG, "getHeadFilePath imgPath=" + imgPath);
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		headimgbit = bitmap;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 *            图片路径
	 **/
	public void startPhotoZoom(Uri uri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");// 设置要裁剪的图片
		cropIntent.putExtra("crop", "true");// crop=true 裁剪页面.
		// 设置其他信息：
		cropIntent.putExtra("aspectX", 1);// 设置裁剪框的比例.
		cropIntent.putExtra("aspectY", 1);// x:y=1:1
		// outputX outputY 是裁剪图片宽高
		// cropIntent.putExtra("outputX", 1200);
		// cropIntent.putExtra("outputY", 1200);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(headimg_file);
		File picture = new File(saveFile, headimg_name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, FROM_CROP);
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
	 * 按成员id跳转界面
	 * 
	 * @param id_Member
	 */
	private void startActivityByMemberId(String id_Member) {
		Bundle bundle = new Bundle();
		bundle.putString("id", id_Member);
		bundle.putString("To", "MemberId");
		LogicUtil.enter(HomePageActivity.this, HomePgActivity.class, bundle,
				false);
	}

	private ProgressDialog pDialog = null;

	/**
	 * 展示带有表情的PopupWindow。
	 * 
	 * @param v
	 */
	private void showPopupWindow() {
		home_faces.setAdapter(new FaceTableAdapter(this));
		home_faces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				spdDialog.showProgressDialog("正在处理中...");
				new loadInfoTask(ApiTypeEnum.GREETMAMBER,
						HomePageActivity.this, memberId, arg2).execute();
				home_faces.setVisibility(View.GONE);
				isFaces = true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		try {
			switch (v.getId()) {
			case R.id.home_head:// 头像/海报
				// setAlterHead();
				if (groupId != null && !"".equals(groupId)) {
					Bundle gb = new Bundle();
					gb.putString("Id", groupId);
					LogicUtil.enter(this, HomeGpActivity.class, gb, false);
				}
				break;
			case R.id.home_button:// 加朋友圈、申请加入、圈币
				if (s[1].equals("MemberId")) {
					ChooseGroupBean bean = new ChooseGroupBean();
					bean.setName(
							new String[] { memberBasicBean.getMemberName() },
							memberBasicBean.getMemberPhone());
					bean.setTitle("加入朋友圈");
					bean.setChosenGroupMap(null);
					Bundle bundle = new Bundle();
					bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
					LogicUtil
							.enter(HomePageActivity.this,
									ChooseGroupsDialogActivity.class, bundle,
									ADD_GROUP);
				} else if (s[1].equals("GroupId")) {
					int i = groupBean.getOpenStatus();
					if (i == 1) {
						new loadInfoTask(ApiTypeEnum.JOINGROUP,
								HomePageActivity.this, groupId).execute();
					} else {
						Bundle secret = new Bundle();
						secret.putString("memberId", groupBean.getMemberId()
								+ "");
						secret.putString("name", groupBean.getMemberName());
						secret.putString("head", groupBean.getMemberHeadUrl()
								+ groupBean.getMemberHeadPath());
						LogicUtil.enter(this, QChatActivity.class, secret,
								false);
					}
				} else if (s[1].equals("OpenPageId")) {
					// 开放主页加关注点击事件
					spdDialog.showProgressDialog("正在处理中...");
					new loadInfoTask(ApiTypeEnum.ISATTENTION,
							HomePageActivity.this, openPageId).execute();
				}
				break;
			case R.id.home_contact_button:// 联系一下、圈子图谱
				if (s[1].equals("MemberId")) {
					if (isGone) {
						home_contact.setVisibility(View.VISIBLE);
						home_contact.startAnimation(mScaleIn);
						isGone = false;
					} else {
						home_contact.startAnimation(mScaleOut);
						mScaleOut.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {

							}

							@Override
							public void onAnimationRepeat(Animation animation) {

							}

							@Override
							public void onAnimationEnd(Animation animation) {
								home_contact.setVisibility(View.GONE);
							}
						});

						isGone = true;
					}
				} else if (s[1].equals("GroupId")) {
					upGroupNum = groupBean.getUpGroupNum();
					downGroupNum = groupBean.getDownGroupNum();
					cooGroupNum = groupBean.getCooGroupNum();

					Bundle b = new Bundle();
					b.putString("groupId", groupId);
					b.putString("groupName", groupName);
					b.putString("groupPost", groupPost);
					b.putInt("upGroupNum", upGroupNum);
					b.putInt("downGroupNum", downGroupNum);
					b.putInt("cooGroupNum", cooGroupNum);
					LogicUtil.enter(HomePageActivity.this,
							CircleFamilyActivity.class, b, false);
				}
				break;
			case R.id.visitors_image:
			case R.id.homepage_down_visitors:// 访客、圈子成员、粉丝
				if (!s[1].equals("MemberId") && !s[1].equals("OpenPageId")) {
					if ("NO_RELATION".equals(joinGroupStatus)) {
						enterHandler.sendEmptyMessage(2);
						return;
					}
				}
				Bundle b = new Bundle();
				if (s[1].equals("MemberId")) {
					b.putSerializable(BundleKey.TYPE_REQUEST, Type.VISITFRIEND);
					b.putString(BundleKey.ID_MEMBER, memberId);
					LogicUtil.enter(HomePageActivity.this,
							MemberListActivity.class, b, false);
				} else if (s[1].equals("GroupId")) {
					b.putSerializable(BundleKey.TYPE_REQUEST, Type.GROUPLEAGUER);
					b.putString(BundleKey.ID_GROUP, groupId);
					joinGroupStatus = groupBean.getJoinGroupStatus();
					b.putString(BundleKey.JOINGROUPSTATUS, joinGroupStatus);
					LogicUtil.enter(HomePageActivity.this,
							MemberListActivity.class, b, false);
				} else if (s[1].equals("OpenPageId")) {
					b.putSerializable(BundleKey.TYPE_REQUEST, Type.FANS);
					b.putString(BundleKey.ID_OPENPAGE, openPageId);
					LogicUtil.enter(HomePageActivity.this,
							MemberListActivity.class, b, false);
				}
				break;
			case R.id.newPic_image:
			case R.id.homepage_down_newpic:// 最新图片
				if (!s[1].equals("MemberId") && !s[1].equals("OpenPageId")) {
					if ("NO_RELATION".equals(joinGroupStatus)) {
						enterHandler.sendEmptyMessage(2);
						return;
					}
				}
				Bundle b_image = new Bundle();
				b_image.putString("id", String.valueOf(s[0]));
				b_image.putString("to", s[1]);
				b_image.putInt("type", 2);
				if (s[1].equals("MemberId")) {
					b_image.putString("name", memberBasicBean.getMemberName());
				} else if (s[1].equals("GroupId")) {
					b_image.putString("name", groupName);
					b_image.putString("joinGroupStatus", joinGroupStatus);
				} else if (s[1].equals("OpenPageId")) {
					b_image.putString("name", groupName);
				}
				LogicUtil.enter(HomePageActivity.this, SubGroupActivity.class,
						b_image, false);
				break;
			case R.id.dynamic_image:
			case R.id.homepage_down_dynamic:// 动态
				if (!s[1].equals("MemberId") && !s[1].equals("OpenPageId")) {
					if ("NO_RELATION".equals(joinGroupStatus)) {
						enterHandler.sendEmptyMessage(2);
						return;
					}
				}
				Bundle b_dynamic = new Bundle();
				b_dynamic.putString("id", String.valueOf(s[0]));
				b_dynamic.putString("to", s[1]);
				b_dynamic.putInt("type", 0);
				if (s[1].equals("MemberId")) {
					b_dynamic
							.putString("name", memberBasicBean.getMemberName());
				} else if (s[1].equals("GroupId")) {
					b_dynamic.putString("name", groupName);
					b_dynamic.putString("joinGroupStatus", joinGroupStatus);
				} else if (s[1].equals("OpenPageId")) {
					b_dynamic.putString("name", groupName);
				}
				LogicUtil.enter(HomePageActivity.this, SubGroupActivity.class,
						b_dynamic, false);
				break;
			case R.id.perInfo_image:
			case R.id.homepage_down_pmsg:// 个人信息
				if (!s[1].equals("MemberId") && !s[1].equals("OpenPageId")) {
					if ("NO_RELATION".equals(joinGroupStatus)) {
						enterHandler.sendEmptyMessage(2);
						return;
					}
				}

				setIntentMsg(PerInformationActivity.class,
						CircleInformationActivity.class, s);
				break;
			case R.id.leaveWord_image:
			case R.id.homepage_down_leaveWord:// 开放主页留言
				Bundle b_leaveWord = new Bundle();
				b_leaveWord.putString("id", String.valueOf(s[0]));
				b_leaveWord.putString("to", s[1]);
				b_leaveWord.putInt("type", 3);
				b_leaveWord.putString("name", groupName);
				LogicUtil.enter(HomePageActivity.this, SubGroupActivity.class,
						b_leaveWord, false);
				break;
			case R.id.btn_phone:// 拨打
				if (CharUtil.isValidPhone(phone_Member)) {
					new MemberContactUtil(HomePageActivity.this)
							.callPhone(phone_Member);

					new Thread() {
						public void run() {
							FriendListService.getService(HomePageActivity.this)
									.saveContactTime(new String[] { memberId });
						};
					}.start();
				} else {
					showTip("该用户未绑定手机号！");
				}
				break;
			case R.id.btn_secret:// 写秘信
				Bundle secret = new Bundle();
				secret.putString("memberId", memberId);
				secret.putString("name", memberBasicBean.getMemberName());
				LogicUtil.enter(this, QChatActivity.class, secret, false);
				break;
			case R.id.btn_message:// 留言
				Bundle message = new Bundle();
				message.putString("id", memberId);
				message.putInt("typedata", EditActivity.TYPE_MSG);
				message.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				LogicUtil.enter(HomePageActivity.this, EditActivity.class,
						message, false);
				break;
			case R.id.btn_hellow:// 打招呼
				if (isFaces) {
					showPopupWindow();
					home_faces.setVisibility(View.VISIBLE);
					home_faces.startAnimation(faces_in);
					isFaces = false;
				} else {
					home_faces.startAnimation(faces_out);
					faces_out.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							home_faces.setVisibility(View.GONE);
							isFaces = true;
						}
					});
				}
				break;
			case R.id.visitor_pic_1:
				startActivityByMemberId(visitor_memberId.get(0));
				break;
			case R.id.visitor_pic_2:
				startActivityByMemberId(visitor_memberId.get(1));
				break;
			case R.id.visitor_pic_3:
				startActivityByMemberId(visitor_memberId.get(2));
				break;
			case R.id.visitor_pic_4:
				startActivityByMemberId(visitor_memberId.get(3));
				break;
			case R.id.visitor_pic_5:
				startActivityByMemberId(visitor_memberId.get(4));
				break;
			case R.id.quanren_btn:
				Intent intent = new Intent(HomePageActivity.this,
						SquareManActivity.class);
				intent.putExtra("groupId", groupId);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in,
						R.anim.friendgroupbg);
				break;
			case R.id.txt_join_circle_chat:// 进入圈聊
				new EnterGroupChat(HomePageActivity.this, groupId,
						groupBean.getGroupName(), groupPost);
				txt_GroupChat_UnreadNum.setVisibility(View.GONE);
				break;
			case R.id.home_feelword_tv:
				if ((GetDbInfoUtil.getMemberId(HomePageActivity.this) + "")
						.equals(s[0])) {
					Intent mIntent = new Intent(HomePageActivity.this,
							EditActivity.class);
					Bundle mb = new Bundle();
					mb.putString("ori", feelword);
					mb.putInt("typedata", EditActivity.TYPE_MOOD);
					mb.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					mIntent.putExtras(mb);
					startActivityForResult(mIntent, SEND_MOOD);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接口数据的线程加载类
	 * 
	 * @author zhangkai
	 * 
	 */
	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;
		private String Id;
		private ApiTypeEnum label;

		private int greetId;

		public loadInfoTask(ApiTypeEnum interName, Context context, String Id) {
			this.label = interName;
			this.context = context;
			this.Id = Id;
		}

		public loadInfoTask(ApiTypeEnum interName, Context context, String Id,
				int greetId) {
			this.label = interName;
			this.context = context;
			this.Id = Id;

			this.greetId = greetId;
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				switch (label) {
				case GETMEMBERBASICINFO:// 获取社员基本信息
					mcResult = APIRequestServers
							.getMemberBasicInfo(context, Id);
					break;
				case OTHERMEMBERTRENDS:// 动态列表
					mcResult = APIRequestServers
							.otherMemberTrends(context, Id,
									ResourceTypeEnum.OBJ_GROUP_ALL, "0", "0",
									"1", true);
					break;
				case VISITFRIENDLISTFORIPHONE:// 最近访问人列表
					mcResult = APIRequestServers.visitFriendListForIPhone(
							context, Id, "0", "5");
					break;
				case GETMEMBERPHOTOSLISTS:// 获得成员图片列表
					mcResult = APIRequestServers.getMemberPhotosLists(context,
							Id, "0", "5", false);
					break;
				case GROUPINFO:// 获取圈子详情
					mcResult = APIRequestServers.groupInfo(
							HomePageActivity.this, Id);
					break;
				case GETGROUPPHOTOSLISTS:// 获取圈子最新图片
					mcResult = APIRequestServers.groupResourceLists(context,
							Id, ResourceTypeEnum.OBJ_GROUP_PHOTO, "0", "5",
							false);
					break;
				case SINGLEGROUPRESOURCETRENDS:// 某个圈子动态列表
					mcResult = APIRequestServers.singleGroupResourceTrends(
							context, Id, "0", "1", true);
					break;
				case GROUPLEAGUERLIST:// 交流圈成员列表
					mcResult = APIRequestServers.groupLeaguerList(context, Id,
							"0", "5", false);
					break;
				case JOINGROUP:// 加入圈子
					mcResult = APIRequestServers.joinGroup(context, Id);
					break;
				case MEMBERSKIN:// 成员皮肤
					mcResult = APIRequestServers.memberSkin(context, Id);
					break;
				case GROUPSKIN:// 圈子皮肤
					mcResult = APIRequestServers.groupSkin(context, Id);
					break;
				case GREETMAMBER:// 打招呼
					mcResult = APIRequestServers.greetMamber(
							HomePageActivity.this, Id, "0",
							String.valueOf(GreetUtil.GREET_CONFIG_ID[greetId]));
					break;
				case SQUAREMAN:// 圈人
					mcResult = APIRequestServers.leaguerPermissionInfo(context,
							Id);
					break;
				case OPENPAGEINFO:// 开放主页详情
					mcResult = APIOpenPageRequestServers.groupInfo(context, Id);
					break;
				case OPENPAGERESOURCE:// 获取开放主页动态列表
					L.d(TAG, "loadInfoTask Id=" + Id);
					mcResult = APIOpenPageRequestServers
							.openPageResourceTrends(context, "OBJ_GROUP_ALL",
									"true", Id, "false", "0", "true", "1");
					break;
				case OPENPAGEPHOTO:// 开放主页最新图片
					mcResult = APIRequestServers.groupResourceLists(context,
							Id, ResourceTypeEnum.OBJ_OPEN_PAGE_PHOTO, "0", "5",
							false);
					break;
				case OPENPAGELEAVEMESSAGE:// 获取开放主页留言
					mcResult = APIOpenPageRequestServers
							.openPageResourceTrends(context,
									"OBJ_OPEN_PAGE_LEAVEMESSAGE", "true", Id,
									"false", "0", "true", "1");
					break;
				case OPENPAGEFANS:// 获取开放主页粉丝列表
					mcResult = APIOpenPageRequestServers.openPageFansList(
							context, Id, "false", "0", "5");
					break;
				case ISATTENTION:// 是否关注
					L.d(TAG, "loadInfoTask isFans == " + isFans);
					mcResult = APIOpenPageRequestServers.attentionOpenPage(
							context, Id, !isFans);
					break;
				case EXITGROUP:// 退出圈子
					mcResult = APIRequestServers.exitGroup(context, Id);
					break;
				case CHATLEAGUERUNREADNUMBER:// 获取群聊成员未读数量
					mcResult = APIGroupChatRequestServers
							.getChatLeaguerUnreadNumber(context, Id);
					break;
				default:
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
			if (result == null) {
				L.d(TAG, "loadInfoTask label=" + label);
				showTip(T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					L.i(TAG, "loadInfoTask label=" + label);
					showTip(T.ErrStr);
				} else {
					isMethodRun(label, result);
				}
			}
		}
	}

	/**
	 * 判断获取的是那个接口的信息并执行相应的方法
	 * 
	 * @param label
	 * @param mcResult
	 */
	public void isMethodRun(ApiTypeEnum label, MCResult mcResult) {
		switch (label) {
		case GETMEMBERBASICINFO:// 获取社员基本信息
			getMemberBasicInfo(mcResult);
			break;
		case OTHERMEMBERTRENDS:// 成员的动态列表
			myTrends(mcResult, tv_dynamic);
			break;
		case SINGLEGROUPRESOURCETRENDS:// 圈子的动态列表
			myTrends(mcResult, tv_dynamic);
			break;
		case VISITFRIENDLISTFORIPHONE:// 获取最近访问人列表
			getVisitFriendList(mcResult);
			break;
		case GETMEMBERPHOTOSLISTS:// 获取成员的最新图片
			getPhotosList(mcResult);
			break;
		case GROUPINFO:// 圈子详情
			getGroupInfo(mcResult);
			break;
		case GETGROUPPHOTOSLISTS:// 获取圈子的最新图片
			getPhotosList(mcResult);
			break;
		case GROUPLEAGUERLIST:// 圈子的成员列表
			getGroupLeaguers(mcResult);
			break;
		case JOINGROUP:// 加入圈子
			T.show(this, "申请已发送，请耐心等候！");
			home_button.setVisibility(View.GONE);
			break;
		case MEMBERSKIN:// 成员皮肤
			getMemberSkin(mcResult);
			break;
		case GROUPSKIN:// 圈子皮肤
			getGroupSkin(mcResult);
			break;
		case GREETMAMBER:// 打招呼
			updateGreetMember(mcResult);
			break;
		case SQUAREMAN:// 圈人
			squareMan(mcResult);
			break;
		case OPENPAGEINFO:// 开放主页详情
			getOpenPageInfo(mcResult);
			break;
		case OPENPAGERESOURCE:// 开放主页动态列表
			myTrends(mcResult, tv_dynamic);
			break;
		case OPENPAGEPHOTO:// 开放主页最新图片
			getPhotosList(mcResult);
			break;
		case OPENPAGELEAVEMESSAGE:// 获取开放主页留言
			myTrends(mcResult, tv_leaveWord);
			break;
		case OPENPAGEFANS:// 获取开放主页粉丝列表
			getOpenPageFans(mcResult);
			break;
		case ISATTENTION:// 是否关注
			isAttention(mcResult);
			break;
		case EXITGROUP:// 退出圈子
			exitGroup(mcResult);
			break;
		case CHATLEAGUERUNREADNUMBER:
			setChatLeaguerUnreadNumber(mcResult);
			break;
		default:
			break;
		}
	}

	/**
	 * 退出圈子 0:异常 1：成功 2：是圈主或者管理员，无法退出圈子 3:社员不在圈子内
	 * 
	 * @param mcResult
	 */
	private void exitGroup(MCResult mcResult) {
		spdDialog.cancelProgressDialog(null);
		int result = (Integer) mcResult.getResult();
		if (result == 1) {
			T.show(this, "已退出！");
			finish();
			try {
				ArrayList<Integer> groupIds = new ArrayList<Integer>();
				groupIds.add(Integer.valueOf(groupId));
				GroupListService.getService(this).deleteList(groupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (result == 2) {
			T.show(this, "您圈主或者管理员，无法退出圈子");
		} else if (result == 3) {
			T.show(this, "社员不在圈子内");
		} else if (result == 0) {
			T.show(this, T.ErrStr);
		}
	}

	/**
	 * 获取群聊成员未读数量
	 * 
	 * @param mcResult
	 */
	private void setChatLeaguerUnreadNumber(MCResult mcResult) {
		ChatLeaguerUnreadNumberBean bean = (ChatLeaguerUnreadNumberBean) mcResult
				.getResult();
		int result = bean.getRESULT();
		if (result == 1) {
			int num_unRead = bean.getUNREAD_NUM();
			if (num_unRead > 0) {
				txt_GroupChat_UnreadNum.setText(num_unRead + "");
				txt_GroupChat_UnreadNum.setVisibility(View.VISIBLE);
			}
		} else if (result == 2) {
			T.show(this, "当前用户不存在");
		} else if (result == 0) {
			T.show(this, T.ErrStr);
		}
	}

	/**
	 * 操作添加关注、取消关注
	 * 
	 * @param mcResult
	 */
	private void isAttention(MCResult mcResult) {
		int num = (Integer) mcResult.getResult();
		L.d(TAG, "isAttention num ==" + num);
		if (num == 1) {
			isFans = !isFans;
			if (isFans) {
				home_button.setText("取消关注");
			} else {
				home_button.setText("我要关注");
			}
		} else {
			showTip(T.ErrStr);
		}
		spdDialog.cancelProgressDialog(null);
	}

	/**
	 * 邀请朋友
	 * 
	 * @param mcResult
	 */
	private void squareMan(MCResult mcResult) {
		GroupLeaguerPermissionBean bean = (GroupLeaguerPermissionBean) mcResult
				.getResult();
		if (bean.getIsInviteLeaguerSetValid()) {
			quanren_btn.setVisibility(View.VISIBLE);
			quanren_btn.setOnClickListener(this);
		}
	}

	private void updateGreetMember(MCResult mcResult) {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
		if (null == mcResult) {
			showTip(T.ErrStr);
		} else {
			if (1 != mcResult.getResultCode()) {
				showTip(T.ErrStr);
			} else {
				// TODO
				// showTip("打招呼成功！");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (!isFaces) {
				home_faces.setVisibility(View.GONE);
				isFaces = true;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();
			if ((tempY - startY) >= 5) {
				int i = -60 + (tempY - startY) / 4;
				if (i <= 0) {
					homepage_up.setPadding(0, i, 0, i);
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			homepage_up.setPadding(0, -65, 0, -65);
			break;
		}
		return super.onTouchEvent(event);
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// if (s[1].equals("MemberId") || s[1].equals("OpenPageId")) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// } else {
	// if (joinGroupStatus != null && !"".equals(joinGroupStatus)) {
	// if ("GROUP_OWNER".equals(joinGroupStatus)// 圈主
	// || "GROUP_MANAGER".equals(joinGroupStatus)// 圈子管理
	// || "APPLY_MANAGER".equals(joinGroupStatus)// 申请圈子管理
	// || "GROUP_LEAGUER".equals(joinGroupStatus)// 普通成员
	// || "COOPERATION_LEAGUER".equals(joinGroupStatus)// 合作圈子的成员
	// ) {
	// enterHandler.sendEmptyMessage(1);
	// } else {
	// enterHandler.sendEmptyMessage(2);
	// }
	// return;
	// }
	// new Thread() {
	// public void run() {
	// try {
	// MCResult mcResult = APIRequestServers
	// .leaguerPermissionInfo(HomePageActivity.this,
	// groupId + "");
	// if (mcResult != null && 1 == mcResult.getResultCode()) {
	// GroupLeaguerPermissionBean bean = (GroupLeaguerPermissionBean) mcResult
	// .getResult();
	// joinGroupStatus = bean.getLeaguerStatus();
	// L.d(TAG, "onRightClick joinGroupStatus="
	// + joinGroupStatus);
	// if ("GROUP_OWNER".equals(joinGroupStatus)// 圈主
	// || "GROUP_MANAGER".equals(joinGroupStatus)// 圈子管理
	// || "APPLY_MANAGER".equals(joinGroupStatus)// 申请圈子管理
	// || "GROUP_LEAGUER".equals(joinGroupStatus)) {// 普通成员
	// enterHandler.sendEmptyMessage(1);
	// } else {
	// enterHandler.sendEmptyMessage(2);
	// }
	// } else {
	// enterHandler.sendEmptyMessage(0);
	// }
	// } catch (Exception e) {
	// enterHandler.sendEmptyMessage(0);
	// e.printStackTrace();
	// }
	// };
	// }.start();
	// }
	// }

	@SuppressLint("HandlerLeak")
	private Handler enterHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				showTip(T.ErrStr);
				break;
			case 1:
				Intent intent = new Intent(HomePageActivity.this,
						CreateGroupTopicActivity.class);
				ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
				GroupEntity entity = new GroupEntity(groupId, groupName, null,
						fansNumbers + "", false, false);
				list.add(entity);
				intent.putExtra("datas", (Serializable) list);
				intent.putExtra(BundleKey.TYPE_REQUEST, Type.DEFAULT);
				startActivity(intent);
				break;
			case 2:
				showTip("您不是该圈子成员，请先加入圈子！");
				break;
			default:
				break;
			}
		};
	};

	private Drawable getBroudDrawable(int aid) {
		Drawable drawable = getResources().getDrawable(aid);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, 1 * dw / 3, 1 * dh / 3);
		return drawable;
	}

}
