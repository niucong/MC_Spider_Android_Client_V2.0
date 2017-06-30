package com.datacomo.mc.spider.android;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.fragmt.BaseGroupFragment;
import com.datacomo.mc.spider.android.fragmt.EnterGroupChat;
import com.datacomo.mc.spider.android.fragmt.GroupFileFragment;
import com.datacomo.mc.spider.android.fragmt.GroupImgFragment;
import com.datacomo.mc.spider.android.fragmt.GroupInfoFragment;
import com.datacomo.mc.spider.android.fragmt.GroupQboFragment;
import com.datacomo.mc.spider.android.fragmt.GroupTopicFragment;
import com.datacomo.mc.spider.android.net.APIGroupChatRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupInfoBean;
import com.datacomo.mc.spider.android.net.been.GroupSetBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.groupchat.ChatLeaguerUnreadNumberBean;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.util.ViewUtil;
import com.datacomo.mc.spider.android.view.GroupHomeScrollView;
import com.datacomo.mc.spider.android.view.GroupHomeScrollView.HomeScrollListener;
import com.datacomo.mc.spider.android.view.LabelRow;
import com.datacomo.mc.spider.android.view.LabelRow.OnLabelClickListener;
import com.datacomo.mc.spider.android.view.VernierBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("UseSparseArrays")
public class HomeGpActivity extends BasicActionBarActivity implements
		OnLabelClickListener {
	private final String TAG = "HomeGpActivity";

	private GroupHomeScrollView mScroll;
	private ImageView poster;
	private LabelRow mLabels;
	private VernierBar mVernier;
	private LinearLayout menuLayout;
	private PopupWindow ppw;
	private String mId;
	private String joinGroupStatus;
	private String[] content = { "动态", "圈博", "文件", "图库", "专题" };
	private GroupInfoBean infoBean;
	private HashMap<Integer, Fragment> hm = new HashMap<Integer, Fragment>();
	private WorkHandler mHandler;
	private SimpleReceiver sReceiver;

	private final int ZERO = 0;
	private final int LABEL_INFO = ZERO;
	private final int LABEL_QBO = LABEL_INFO + 1;
	private final int LABEL_FILE = LABEL_INFO + 2;
	private final int LABEL_IMG = LABEL_INFO + 3;
	private final int LABEL_TOPIC = LABEL_INFO + 4;

	private final int WORK_UNREAD_QUUCHAT = ZERO;
	private final int WORK_APPLY_JOIN_IN = WORK_UNREAD_QUUCHAT + 1;
	// private final int WORK_CHECK_TOP = WORK_UNREAD_QUUCHAT + 2;
	private final int WORK_EXIT_GROUP = WORK_UNREAD_QUUCHAT + 3;
	private final int WORK_SHOW_TIP = WORK_UNREAD_QUUCHAT + 4;
	private final int WORK_REFRESH_CUR_FRAGMENT = WORK_UNREAD_QUUCHAT + 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		mScroll = new GroupHomeScrollView(this);
		setContent(mScroll);
		init();

		sReceiver = new SimpleReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				String action = arg1.getAction();
				if (SimpleReceiver.RECEIVER_ADD_MEMBER.equals(action)) {
					Bundle b = arg1.getExtras();
					if (b.getInt(BundleKey.ID_GROUP) == infoBean
							.getGROUP_INFO().getGroupId()) {
						infoBean.getGROUP_INFO().setLeaguerNum(
								infoBean.getGROUP_INFO().getLeaguerNum()
										+ b.getInt(BundleKey.SIZE));
						initMenuLayout(infoBean);
						hm.remove(0);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								HandlerUtil.sendMsgToHandler(mHandler,
										WORK_REFRESH_CUR_FRAGMENT, null, 0);
							}
						}).start();
					}
				} else if (SimpleReceiver.RECEIVER_EDIT_DESCRIPTION
						.equals(action)) {
					Bundle b = arg1.getExtras();
					GroupBean gBean = infoBean.getGROUP_INFO();
					gBean.setGroupDescription(b
							.getString(BundleKey.DESCRIPTION));
					setGroupDescripTion(gBean);
				}
			}
		};

		SimpleReceiver receiver = new SimpleReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (SimpleReceiver.RECEIVER_DATA_CHANGED.equals(arg1
						.getAction())) {
					Bundle b = arg1.getExtras();
					GroupBean gBean = infoBean.getGROUP_INFO();
					String _name = b.getString("name");
					if (null != _name) {
						gBean.setGroupName(_name);
						setGroupName(_name);
					}
					String _des = b.getString("description");
					if (null != _des) {
						gBean.setGroupDescription(_des);
						setGroupDescripTion(gBean);
					}
					boolean _poster = b.getBoolean("poster");
					if (_poster) {
						new LoadGroupTask(HomeGpActivity.this).execute();
					}

				}

			}
		};
		try {
			SimpleReceiver.registerReceiver(this, receiver,
					SimpleReceiver.RECEIVER_DATA_CHANGED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "14");
	}

	@Override
	protected void onResume() {
		super.onResume();
		SimpleReceiver.registerReceiver(this, sReceiver,
				SimpleReceiver.RECEIVER_ADD_MEMBER,
				SimpleReceiver.RECEIVER_EDIT_DESCRIPTION);
	}

	private void init() {
		// setTitle("交流圈", R.drawable.title_fanhui, false, null, null);
		ab.setTitle("交流圈");
		hm = new HashMap<Integer, Fragment>();
		mHandler = new WorkHandler();
		mLabels = (LabelRow) findViewById(R.id.labelrow);
		mLabels.setLabels(content);
		mLabels.setOnLabelClickListener(this);
		mVernier = (VernierBar) findViewById(R.id.vernier);
		mVernier.initBar(content.length);
	}

	private void initMenuLayout(final GroupInfoBean groupInfoBean) {
		if (null == groupInfoBean) {
			return;
		}
		final GroupBean gBean = groupInfoBean.getGROUP_INFO();
		if (null == gBean) {
			return;
		}
		menuLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.menu_home_group, null);
		String str[] = { "圈子图谱", "开放主页",
				"成员" + "(" + gBean.getLeaguerNum() + ")", "圈子信息" };
		int openpageSrc = R.drawable.group_openpage_disable;
		if (1 == infoBean.getOPENPAGEPERMMISSION()
				|| 6 == infoBean.getOPENPAGEPERMMISSION()) {
			openpageSrc = R.drawable.group_openpage;
		}
		int icon[] = { R.drawable.group_family, openpageSrc,
				R.drawable.group_leager, R.drawable.group_infomation };
		LinearLayout item[] = {
				(LinearLayout) menuLayout.findViewById(R.id.item0),
				(LinearLayout) menuLayout.findViewById(R.id.item1),
				(LinearLayout) menuLayout.findViewById(R.id.item2),
				(LinearLayout) menuLayout.findViewById(R.id.item3) };

		OnClickListener itemClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ppw.dismiss();
				Bundle b = new Bundle();
				Integer tag = (Integer) (v.getTag());
				if (null != tag) {
					switch ((Integer) (v.getTag())) {
					case 0:// 图谱
						b.putString("groupId", gBean.getGroupId() + "");
						b.putString("groupName", gBean.getGroupName());
						b.putString("groupPost", gBean.getGroupPosterUrl()
								+ gBean.getGroupPosterPath());
						b.putInt("upGroupNum", gBean.getUpGroupNum());
						b.putInt("downGroupNum", gBean.getDownGroupNum());
						b.putInt("cooGroupNum", gBean.getCooGroupNum());
						LogicUtil.enter(HomeGpActivity.this,
								CircleFamilyActivity.class, b, false);
						break;
					case 1:// 开放主页
						if (1 == infoBean.getOPENPAGEPERMMISSION()
								|| 6 == infoBean.getOPENPAGEPERMMISSION()) {
							b.putString("Id", infoBean.getOPEN_PAGE_ID() + "");
							b.putString("To", "OpenPageId");
							LogicUtil.enter(HomeGpActivity.this,
									HomePageActivity.class, b, false);
						}
						break;
					case 2:// 成员
						b.putSerializable(BundleKey.TYPE_REQUEST,
								Type.GROUPLEAGUER);
						b.putString(BundleKey.ID_GROUP, gBean.getGroupId() + "");
						b.putString(BundleKey.JOINGROUPSTATUS, joinGroupStatus);
						b.putInt(BundleKey.NUM, gBean.getLeaguerNum());
						LogicUtil.enter(HomeGpActivity.this,
								MemberListActivity.class, b, false);
						break;
					case 3:// 圈子信息
						b.putString("Id", gBean.getGroupId() + "");
						b.putString("To", "GroupId");
						b.putBoolean("noOpen", true);
						LogicUtil.enter(HomeGpActivity.this,
								CircleInformationActivity.class, b, 0);
						break;
					default:
						break;
					}
				} else {
					switch (v.getId()) {
					case R.id.btn_1:
						GroupSetBean groupSetBean = infoBean.getGROUP_SET();
						int invitePermission = groupSetBean
								.getInviteLeaguerSet();
						if (1 == invitePermission
								|| 3 == invitePermission
								|| 4 == invitePermission
								|| (2 == invitePermission && ("GROUP_OWNER"
										.equals(joinGroupStatus) || "GROUP_MANAGER"
										.equals(joinGroupStatus)))) {
							Intent intent = new Intent(HomeGpActivity.this,
									SquareManActivity.class);
							intent.putExtra("groupId", gBean.getGroupId() + "");
							startActivity(intent);
							overridePendingTransition(R.anim.push_up_in,
									R.anim.friendgroupbg);
						} else {
							showTip("您没有邀请权限！");
						}
						break;
					case R.id.btn_2:
						new EnterGroupChat(HomeGpActivity.this,
								gBean.getGroupId() + "", gBean.getGroupName(),
								gBean.getGroupPosterUrl()
										+ gBean.getGroupPosterPath());
						menuLayout.findViewById(R.id.num_unread_chat)
								.setVisibility(View.GONE);
						break;
					case R.id.btn_quit:
						if (!IsNetworkConnected
								.checkNetworkInfo(HomeGpActivity.this)) {
							showTip(T.ErrStr);
							return;
						}
						showQuitApplyDialog(joinGroupStatus, gBean);
						break;
					default:
						break;
					}
				}
			}

			private void showQuitApplyDialog(String state, GroupBean groupBean) {
				if ("GROUP_LEAGUER".equals(state)
						|| "APPLY_MANAGER".equals(state)) {
					new AlertDialog.Builder(HomeGpActivity.this)
							.setTitle("退出圈子")
							.setMessage("确定要退出 " + gBean.getGroupName() + " 吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											new Thread(new Runnable() {
												@Override
												public void run() {
													MCResult mcResult = null;
													try {
														mcResult = APIRequestServers
																.exitGroup(
																		HomeGpActivity.this,
																		gBean.getGroupId()
																				+ "");
													} catch (Exception e) {
														e.printStackTrace();
													}
													if (null != mcResult) {
														HandlerUtil
																.sendMsgToHandler(
																		mHandler,
																		WORK_EXIT_GROUP,
																		mcResult.getResultCode(),
																		R.id.btn_quit,
																		0);
													} else {
														HandlerUtil
																.sendMsgToHandler(
																		mHandler,
																		WORK_SHOW_TIP,
																		T.ErrStr,
																		0);
													}
												}
											}).start();
										}
									}).setNegativeButton("取消", null).show();
				} else if ("GROUP_MANAGER".equals(state)) {
					new AlertDialog.Builder(HomeGpActivity.this)
							.setTitle("退出圈子")
							.setMessage("您需要联系圈主撤销您的管理员身份，成为普通成员，方可退出圈子！")
							.setPositiveButton("联系圈主",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											chatWithQOwner(gBean);
										}
									}).setNegativeButton("知道了", null).show();
				} else if ("GROUP_OWNER".equals(state)) {
					new AlertDialog.Builder(HomeGpActivity.this)
							.setTitle("退出圈子")
							.setMessage("只有普通成员可以退出圈子！\n您可以电脑登陆优优工作圈，把该圈子转让，方可退出。")
							.setNegativeButton("知道了", null).show();
				}
			}
		};

		LayoutParams lpIv = new LayoutParams(
				BaseData.getScreenWidth() / 15, BaseData.getScreenWidth() / 15);
		LayoutParams lpTv = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < item.length; i++) {
			item[i].setTag(i);
			item[i].setOrientation(LinearLayout.VERTICAL);
			if (ApiDiffCoding.IsSdkNewerThan(14)) {
				item[i].setBackgroundResource(R.drawable.bg_blank11);
			}
			item[i].setLayoutParams(new LayoutParams(BaseData
					.getScreenWidth() / 2, BaseData.getScreenWidth() * 3 / 10));
			item[i].setOnClickListener(itemClickListener);

			ImageView iv = new ImageView(this);
			iv.setImageResource(icon[i]);
			item[i].addView(iv, lpIv);

			TextView tv = new TextView(this);
			tv.setText(str[i]);
			tv.setTextSize(15f);
			tv.setTextColor(res.getColor(R.color.graytext));

			if (1 == i) {
				if (1 == infoBean.getOPENPAGEPERMMISSION()
						|| 6 == infoBean.getOPENPAGEPERMMISSION()) {
					tv.setTextColor(res.getColor(R.color.graytext));
					item[i].setEnabled(true);
				} else {
					tv.setTextColor(res.getColor(R.color.gray_light));
					item[i].setEnabled(false);
				}
			}
			item[i].addView(tv, lpTv);
		}
		TextView msg = (TextView) menuLayout.findViewById(R.id.msm_id);
		msg.setText("圈子短号：" + ConstantUtil.GROUPSHORT + gBean.getGroupShort());
		View pickLeagure = menuLayout.findViewById(R.id.btn_1);
		pickLeagure.setOnClickListener(itemClickListener);
		View quuChat = menuLayout.findViewById(R.id.btn_2);

		if (!"NO_RELATION".equals(joinGroupStatus)) {
			menuLayout.findViewById(R.id.chat_layout).setVisibility(
					View.VISIBLE);
			quuChat.setOnClickListener(itemClickListener);
			updateUnreadChat(gBean.getGroupId() + "");
		}

		if (!ApiDiffCoding.IsSdkNewerThan(14)) { // 4.0之前 ppw控件加背景状态 点击会变透明
			pickLeagure.setBackgroundResource(R.drawable.btn_white_1);
			quuChat.setBackgroundResource(R.drawable.btn_white_1);
		}

		if ("COOPERATION_LEAGUER".equals(joinGroupStatus)
				|| "NO_RELATION".equals(joinGroupStatus)
				|| "APPLY_LEAGUER".equals(joinGroupStatus)
				|| "COOPERATION_APPLY_LEAGUER".equals(joinGroupStatus)) {
			menuLayout.findViewById(R.id.btn_quit).setVisibility(View.GONE);
		}
		menuLayout.findViewById(R.id.btn_quit).setOnClickListener(
				itemClickListener);
	}

	private void updateUnreadChat(final String groupId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MCResult mcResult = null;
				try {
					mcResult = APIGroupChatRequestServers
							.getChatLeaguerUnreadNumber(HomeGpActivity.this,
									groupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null != mcResult && mcResult.getResultCode() == 1) {
					ChatLeaguerUnreadNumberBean bean = (ChatLeaguerUnreadNumberBean) mcResult
							.getResult();
					int result = bean.getRESULT();
					if (result == 1) {
						int num_unRead = bean.getUNREAD_NUM();
						if (num_unRead > 0) {
							HandlerUtil.sendMsgToHandler(
									mHandler,
									WORK_UNREAD_QUUCHAT,
									menuLayout
											.findViewById(R.id.num_unread_chat),
									num_unRead);
						}
					}
				}
			}
		}).start();
	}

	void initData() {
		L.d(TAG, "initData...");
		initIntentMsg();
		new LoadGroupTask(this).execute();
	}

	private void initIntentMsg() {
		try {
			Bundle b = getIntent().getExtras();
			mId = b.getString("Id");
			// String To = b.getString("To");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLabelClick(int index, View view) {
		indexTo(view.getId());
		mScroll.checkTop(mScroll.TOP_MIDDLE);
	}

	private void vernierTo(int targetLabelIndex) {
		mVernier.to(targetLabelIndex);
	}

	private void setGroupSkin(String groupSkin) {
		MyFinalBitmap.setGroupSkin(HomeGpActivity.this,
				(ImageView) findViewById(R.id.skin), groupSkin,
				new ImageLoadingListener() {
					@Override
					public void setProgressMax(int arg0) {
					}

					@Override
					public void onStartProgress() {
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingProgress(int arg0, int load, int len) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						mScroll.setSkinImage(null);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						mScroll.setSkinImage(arg2);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}

					@Override
					public int getProgressMax() {
						return 0;
					}
				});
	}

	class LoadGroupTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;

		public LoadGroupTask(Context context) {
			this.context = context;
			setLoadingState(true);
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.detailGroupInfo(context, mId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			setLoadingState(false);
			if (null == result || result.getResultCode() != 1) {
				// finish();
				return;
			}
			setGroupInfo(result);
			vernierTo(LABEL_INFO);
		}
	}

	private void setGroupInfo(MCResult mcResult) {
		infoBean = (GroupInfoBean) mcResult.getResult();
		final GroupBean groupBean = infoBean.getGROUP_INFO();
		joinGroupStatus = groupBean.getJoinGroupStatus();
		initMenuLayout(infoBean);
		setGroupSkin(infoBean.getGROUP_BG());
		getGroupMainInfo(groupBean);

		int openStatus = groupBean.getOpenStatus();
		String joinGroupStatus = groupBean.getJoinGroupStatus();
		L.d(TAG, "setGroupInfo openStatus=" + openStatus + ",joinGroupStatus="
				+ joinGroupStatus);
		if (openStatus == 1 || !"NO_RELATION".equals(joinGroupStatus)) {
			mScroll.findViewById(R.id.container).setVisibility(View.VISIBLE);
			mScroll.findViewById(R.id.labelrow).setVisibility(View.VISIBLE);
			mScroll.findViewById(R.id.vernier).setVisibility(View.VISIBLE);
			@SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ABOVE, R.id.labelrow);
			mScroll.findViewById(R.id.description)
					.setLayoutParams(layoutParams);
			indexTo(0);
		} else {
			mScroll.findViewById(R.id.container).setVisibility(View.GONE);
			mScroll.findViewById(R.id.labelrow).setVisibility(View.GONE);
			mScroll.findViewById(R.id.vernier).setVisibility(View.GONE);
			@SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.skin);
			mScroll.findViewById(R.id.description)
					.setLayoutParams(layoutParams);
		}

		TextView joinBtn = (TextView) mScroll.findViewById(R.id.join);
		if (openStatus == 2) {
			if ("APPLY_LEAGUER".equals(joinGroupStatus)
					|| "COOPERATION_LEAGUER".equals(joinGroupStatus)
					|| "NO_RELATION".equals(joinGroupStatus)) {
				showTip("该圈子是私密圈子，您没有浏览权限，如需加入，请联系圈主。");
				joinBtn.setText("联系圈主");
				joinBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						chatWithQOwner(groupBean);
					}
				});
				joinBtn.setVisibility(View.VISIBLE);
			} else if ("COOPERATION_APPLY_LEAGUER".equals(joinGroupStatus)) {
				joinBtn.setText("审核中");
				joinBtn.setVisibility(View.VISIBLE);
			}
		} else if (openStatus == 1) {
			GroupSetBean groupSetBean = infoBean.getGROUP_SET();
			final int applySet = groupSetBean.getInviteLeaguerSet();
			if (3 == applySet || 4 == applySet) {
				if ("NO_RELATION".equals(joinGroupStatus)
						|| "COOPERATION_LEAGUER".equals(joinGroupStatus)) {
					joinBtn.setText("申请加入");
					joinBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(final View v) {
							if (3 == applySet) {
								((TextView) v).setText("审核中");
								vernierTo(mVernier.getIndex()); // 有个bug
								// 改变joinBtn的文字，
								// vernier会游到最左。
							} else {
								v.setVisibility(View.GONE);
							}
							new Thread(new Runnable() {
								@Override
								public void run() {
									MCResult mcResult = null;
									try {
										mcResult = APIRequestServers.joinGroup(
												HomeGpActivity.this,
												groupBean.getGroupId() + "");
									} catch (Exception e) {
										e.printStackTrace();
									}
									v.setTag(mcResult);
									HandlerUtil.sendMsgToHandler(mHandler,
											WORK_APPLY_JOIN_IN, v, applySet);
								}
							}).start();
							T.show(HomeGpActivity.this, "申请已发送，请耐心等候！");
							v.setClickable(false);
						}
					});
					joinBtn.setVisibility(View.VISIBLE);
				} else if ("APPLY_MANAGER".equals(joinGroupStatus)
						|| "APPLY_LEAGUER".equals(joinGroupStatus)
						|| "COOPERATION_APPLY_LEAGUER".equals(joinGroupStatus)) {
					joinBtn.setText("审核中");
					joinBtn.setVisibility(View.VISIBLE);
				} else {
					joinBtn.setVisibility(View.GONE);
				}
			}
		}

		TextView money = (TextView) findViewById(R.id.group_money);
		if (groupBean.getGroupType() == 1) {
			money.setVisibility(View.GONE);
		} else {
			int moneyInt = groupBean.getGoldNum();
			if (null != money && moneyInt >= 0) {
				String moneyStr = null;
				if (moneyInt > 9999) {
					moneyStr = "9999+";
				} else {
					moneyStr = moneyInt + "";
				}
				money.setText("圈币(" + moneyStr + ")");
				money.setVisibility(View.VISIBLE);
			}
		}
	}

	private void chatWithQOwner(GroupBean groupBean) {
		Bundle secret = new Bundle();
		secret.putString("memberId", groupBean.getMemberId() + "");
		secret.putString("name", groupBean.getMemberName());
		secret.putString("head",
				groupBean.getMemberHeadUrl() + groupBean.getMemberHeadPath());
		LogicUtil
				.enter(HomeGpActivity.this, QChatActivity.class, secret, false);
	}

	/**
	 * 设定主要信息
	 * 
	 * @param groupBean
	 */
	private void getGroupMainInfo(final GroupBean groupBean) {
		final String groupName = groupBean.getGroupName();
		addSearch();
		MenuItem mi = menu.findItem(R.id.action_more);
		mi.setIcon(R.drawable.action_more);
		menu.findItem(R.id.action_write).setVisible(true);
		mScroll.setOnHomeScrollListener(new HomeScrollListener() {
			@Override
			public void onCheckTop(boolean isTop) {
				if (isTop) {
					ab.setTitle("交流圈");
				} else {
					ab.setTitle(groupName);
				}
			}
		});
		setGroupName(groupName);
		((TextView) findViewById(R.id.group_name)).setVisibility(View.VISIBLE);
		mLabels.setVisibility(View.VISIBLE);
		mVernier.setVisibility(View.VISIBLE);
		String groupPost = groupBean.getGroupPosterUrl()
				+ groupBean.getGroupPosterPath();
		final String groupPostTh = ThumbnailImageUrl.getThumbnailPostUrl(
				groupPost, PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		poster = (ImageView) findViewById(R.id.poster);
		if ("GROUP_OWNER".equals(joinGroupStatus)
				|| "GROUP_MANAGER".equals(joinGroupStatus))
			poster.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showUpHeadDialog();
				}
			});
		MyFinalBitmap.setCirclePoster(this, poster, groupPostTh,
				new ImageLoadingListener() {

					@Override
					public void setProgressMax(int arg0) {
					}

					@Override
					public void onStartProgress() {
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingProgress(int arg0, int load, int len) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						InputStream is = res
								.openRawResource(R.drawable.icon_poster_loading);
						Bitmap defBmp = BitmapFactory.decodeStream(is);
						onLoadingComplete(arg0, arg1, defBmp);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						arg1.setVisibility(View.VISIBLE);
						vernierTo(mVernier.getIndex());
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						arg1.setVisibility(View.VISIBLE);
					}

					@Override
					public int getProgressMax() {
						return 0;
					}
				});

		// 显示圈子描述
		setGroupDescripTion(groupBean);
		findViewById(R.id.description).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String description = (String) arg0.getTag();
						if ("GROUP_OWNER".equals(joinGroupStatus)
								|| "GROUP_MANAGER".equals(joinGroupStatus)) {
							Bundle b = new Bundle();
							b.putString("id", infoBean.getGROUP_INFO()
									.getGroupId() + "");
							b.putString("ori", infoBean.getGROUP_INFO()
									.getGroupDescription());
							b.putInt("typedata", EditActivity.TYPE_DESCRIPTION);
							LogicUtil.enter(HomeGpActivity.this,
									EditActivity.class, b, false);
						} else if (description != null
								&& !description.equals("")) {
							showDescription(groupPostTh, groupName,
									description.trim());
						}
					}
				});

		// 圈子类型
		Drawable groupPropertyIcon = null;
		int property = groupBean.getGroupProperty();
		if (property == 4) {
			groupPropertyIcon = res.getDrawable(R.drawable.group_out);
		} else {
			int groupType = groupBean.getGroupType();
			if (groupType == 2) {
				groupPropertyIcon = res.getDrawable(R.drawable.group_school);
			} else if (groupType == 3) {
				groupPropertyIcon = res.getDrawable(R.drawable.group_company);
			} else if (groupType == 4) {
				groupPropertyIcon = res.getDrawable(R.drawable.group_community);
			} else {
				groupPropertyIcon = res.getDrawable(R.drawable.nothing);
			}
		}
		((ImageView) findViewById(R.id.group_tag))
				.setImageDrawable(groupPropertyIcon);
	}

	private static final int FROM_GALLERY = 3;
	private static final int FROM_CAMERA = 1;
	private static final int FROM_CROP = 2;

	private String headimg_file = ConstantUtil.HEAD_PATH;
	private String headimg_name = "headimg.jpg";
	private Bitmap headimgbit;
	private File headPic;

	/**
	 * 显示修改头像对话框
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showUpHeadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("修改海报");
		String[] dialogMsg = new String[] {
				getResources().getString(R.string.selectImgdialog_fromlocality),
				getResources().getString(R.string.selectImgdialog_fromcamera)
		// , getResources().getString(R.string.selectImgdialog_cancel)
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

	private void setGroupName(String groupName) {
		((TextView) findViewById(R.id.group_name)).setText(groupName);
	}

	private void setGroupDescripTion(GroupBean groupBean) {
		String groupDescription = groupBean.getGroupDescription();
		TextView description = (TextView) findViewById(R.id.description);
		description.setVisibility(View.VISIBLE);
		description.setTag(groupDescription);
		if (groupDescription != null && !groupDescription.equals("")) {
			ViewUtil.appadingIconWithText(this, description,
					R.drawable.arraw_right_white, groupDescription.trim(),
					0.61, 0.61, false);
		} else {
			description.setText("  管理员还没有填写简介。简介可以是介绍圈子的一句话，也可以是共同目标或口号哦。");
		}
	}

	private void showDescription(String posterUri, String groupName,
			String description) {
		if (null == description.trim() || "".equals(description)) {
			return;
		}
		ViewGroup content = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.ppw_group_desprition, null);
		ImageView circlePoster = (ImageView) content.findViewById(R.id.icon);
		ImageView delete = (ImageView) content.findViewById(R.id.dismiss);
		TextView circleName = (TextView) content.findViewById(R.id.group_name);
		TextView circleDescription = (TextView) content.findViewById(R.id.text);
		if (null != groupName && !"".equals(groupName)) {
			circleName.setText(groupName);
		}
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != ppw && ppw.isShowing()) {
					ppw.dismiss();
				}
			}
		});
		if (null == posterUri || "".equals(posterUri)) {
			circlePoster.setVisibility(View.GONE);
		} else {
			MyFinalBitmap.setCirclePoster(this, circlePoster, posterUri,
					new ImageLoadingListener() {

						@Override
						public void setProgressMax(int arg0) {
						}

						@Override
						public void onStartProgress() {
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingProgress(int arg0, int load,
								int len) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							arg1.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							arg1.setVisibility(View.GONE);
						}

						@Override
						public int getProgressMax() {
							return 0;
						}
					});
		}
		circleDescription.setText(description);
		ppw = new PopupWindow(content, BaseData.getScreenWidth() - 30,
				LayoutParams.WRAP_CONTENT);
		@SuppressWarnings("deprecation")
		BitmapDrawable bd = new BitmapDrawable(res);
		ppw.setBackgroundDrawable(bd);
		ppw.setFocusable(true);
		ppw.setOutsideTouchable(true);
		ppw.setAnimationStyle(R.style.pop_style);
		ppw.showAtLocation(findViewById(R.id.out), Gravity.CENTER, 0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		menu.findItem(R.id.action_refresh).setVisible(true);
		MenuItem mi = menu.findItem(R.id.action_more);
		mi.setVisible(true);
		mi.setIcon(R.drawable.nothing);
		this.menu = menu;
		initData();
		return super.onCreateOptionsMenu(menu);
	}

	private void addSearch() {
		MenuItem smi = menu.findItem(R.id.action_search);
		smi.setVisible(true);
		searchView = (SearchView) smi.getActionView();
		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);

		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);
		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
						.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
				if (mQueryTextView.isShown()
						&& "".equals(mQueryTextView.getText().toString())) {
					cv.setVisibility(View.GONE);
				}
				mScroll.checkTop(mScroll.TOP_MIDDLE);
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				L.d(TAG, "addSearch s=" + s);
				((BaseGroupFragment) hm.get(itemP)).search(s);
				BaseData.hideKeyBoard(HomeGpActivity.this);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
				} else {
					cv.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				((BaseGroupFragment) hm.get(itemP)).search("");
				return false;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			try {
				if (searchView.findViewById(
						com.actionbarsherlock.R.id.abs__search_src_text)
						.isShown()) {
					searchView.onActionViewCollapsed();
					((BaseGroupFragment) hm.get(itemP)).search("");
					((TextView) searchView
							.findViewById(com.actionbarsherlock.R.id.abs__search_src_text))
							.setText("");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_refresh:
			try {
				((BaseGroupFragment) (hm.get(itemP))).onFragmentRefresh(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		case R.id.action_more:
			menuShow();
			try {
				updateUnreadChat(infoBean.getGROUP_INFO().getGroupId() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		case R.id.action_write:
			if (joinGroupStatus != null && !"".equals(joinGroupStatus)) {
				if ("GROUP_OWNER".equals(joinGroupStatus)// 圈主
						|| "GROUP_MANAGER".equals(joinGroupStatus)// 圈子管理
						|| "APPLY_MANAGER".equals(joinGroupStatus)// 申请圈子管理
						|| "GROUP_LEAGUER".equals(joinGroupStatus)// 普通成员
						|| "COOPERATION_LEAGUER".equals(joinGroupStatus)// 合作圈子的成员
				) {
					GroupBean groupBean = infoBean.getGROUP_INFO();
					Intent intent = new Intent(HomeGpActivity.this,
							CreateGroupTopicActivity.class);
					ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
					GroupEntity entity = new GroupEntity(groupBean.getGroupId()
							+ "", groupBean.getGroupName(), null,
							groupBean.getFansNum() + "", false, false);
					list.add(entity);
					intent.putExtra("datas", (Serializable) list);
					intent.putExtra(BundleKey.TYPE_REQUEST, Type.DEFAULT);
					startActivity(intent);
				} else {
					showTip("您不是该圈子成员，请先加入圈子！");
				}
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			try {
				if (searchView.findViewById(
						com.actionbarsherlock.R.id.abs__search_src_text)
						.isShown()) {
					searchView.onActionViewCollapsed();
					((BaseGroupFragment) hm.get(itemP)).search("");
					((TextView) searchView
							.findViewById(com.actionbarsherlock.R.id.abs__search_src_text))
							.setText("");
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			File saveFile = null;
			try {
				switch (requestCode) {
				case 0:
					int type = data.getIntExtra("type", 0);
					indexTo(type + 1);
					break;
				case FROM_CAMERA:
					saveFile = new File(headimg_file);
					File picture = new File(saveFile, headimg_name);
					uri = Uri.fromFile(picture);
					startPhotoZoom(uri);
					break;
				case FROM_GALLERY:
					uri = data.getData();
					startPhotoZoom(uri);
					break;
				case FROM_CROP:
					new AlertDialog.Builder(HomeGpActivity.this)
							.setTitle(
									getResources()
											.getString(
													R.string.circle_uploadimgdialog_title))
							.setMessage(
									getResources()
											.getString(
													R.string.circle_uploadimgdialog_confirmupload))
							.setPositiveButton(
									getResources().getString(
											R.string.alertdialog_confirm),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											File saveFile = new File(
													headimg_file);
											headPic = new File(saveFile,
													headimg_name);
											getHeadFilePath(Uri
													.fromFile(headPic));
											poster.setImageBitmap(ImageDealUtil
													.toRoundCorner(
															headimgbit,
															DensityUtil
																	.dip2px(HomeGpActivity.this,
																			poster.getHeight())));
											uploadHead(headPic);
										}
									})
							.setNegativeButton(
									getResources().getString(
											R.string.alertdialog_cancel), null)
							.show();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 图片路径并上传
	 * 
	 * @param uriString
	 **/
	private void uploadHead(final File file) {
		if (file != null && file.exists())
			APIRequestServers.uploadFile(HomeGpActivity.this, file,
					UploadMethodEnum.UPLOADPOSTER, mId);
	}

	private String imgPath;

	/**
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	private void getHeadFilePath(Uri uri) {
		String sUri = uri.toString();
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				imgPath = cursor.getString(1); // 图片文件路径
			} else {

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

		}
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
			headimgbit = bitmap;
		} catch (OutOfMemoryError e) {
		}
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
		// cropIntent.putExtra("outputX", 120);
		// cropIntent.putExtra("outputY", 120);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(headimg_file);
		File picture = new File(saveFile, headimg_name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, FROM_CROP);
	}

	int itemP;

	private void indexTo(int which) {
		itemP = which;
		if (this.isFinishing()) {
			return;
		}
		vernierTo(which);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = hm.get(which);
		if (null == fragment) {
			Bundle bd = new Bundle();
			switch (which) {
			case LABEL_INFO:
				fragment = new GroupInfoFragment();
				bd.putString("groupId", mId);
				bd.putString("memberState", joinGroupStatus);
				fragment.setArguments(bd);
				break;
			case LABEL_QBO:
				fragment = new GroupQboFragment();
				bd.putString("groupId", mId);
				bd.putString("memberState", joinGroupStatus);
				fragment.setArguments(bd);
				break;
			case LABEL_FILE:
				fragment = new GroupFileFragment();
				bd.putString("groupId", mId);
				bd.putString("memberState", joinGroupStatus);
				fragment.setArguments(bd);
				break;
			case LABEL_IMG:
				fragment = new GroupImgFragment();
				bd.putString("groupId", mId);
				bd.putString("memberState", joinGroupStatus);
				fragment.setArguments(bd);
				break;
			case LABEL_TOPIC:
				fragment = new GroupTopicFragment();
				bd.putString("groupId", mId);
				bd.putString("memberState", joinGroupStatus);
				fragment.setArguments(bd);
				break;
			default:
				break;
			}
			hm.put(which, fragment);
		}
		ft.setCustomAnimations(R.anim.scale_in_mid, R.anim.scale_out_mid);
		ft.replace(R.id.container, fragment);
		ft.commitAllowingStateLoss();
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
		if (ppw != null) {
			ppw.dismiss();
			ppw = null;
		}
		unregisterReceiver(sReceiver);
		hm.clear();
		hm = null;
	}

	private void menuShow() {
		if (null == menuLayout) {
			return;
		}
		ppw = new PopupWindow(menuLayout, BaseData.getScreenWidth(),
				LayoutParams.WRAP_CONTENT);
		ppw.setBackgroundDrawable(res.getDrawable(R.drawable.nothing));
		ppw.setFocusable(true);
		ppw.setOutsideTouchable(true);
		// ppw.setAnimationStyle(R.style.menu_ani); 动画会遮住header
		ppw.showAsDropDown(findViewById(R.id.header), 0, 0);
		menuLayout.setVisibility(View.VISIBLE);
		Animation show = AnimationUtils.loadAnimation(this, R.anim.p_enter_up);
		menuLayout.startAnimation(show);
	}

	@SuppressLint("HandlerLeak")
	class WorkHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case WORK_UNREAD_QUUCHAT:
				((TextView) msg.obj).setText(msg.arg1 + "");
				((TextView) msg.obj).setVisibility(View.VISIBLE);
				break;
			case WORK_APPLY_JOIN_IN:
				MCResult mcResult = (MCResult) ((TextView) msg.obj).getTag();
				if (null == mcResult || 1 != mcResult.getResultCode()) {
					((TextView) msg.obj).setText("申请加入");
					showTip("申请加入异常，请重新申请！");
					((TextView) msg.obj).setClickable(true);
				} else if (4 == msg.arg1) { // arg1 =4 即自由加入，无需审核，刷新数据。
					initData();
				}
				break;
			// case WORK_CHECK_TOP:
			// mScroll.checkTop(mScroll.TOP_OFFSET);
			// break;
			case WORK_EXIT_GROUP:
				View btn = menuLayout.findViewById(msg.arg1);
				int resultCode = (Integer) msg.obj;
				if (resultCode == 1) {
					btn.setVisibility(View.GONE);
					showTip("退出成功");
					finish();
					try {
						ArrayList<Integer> groupIds = new ArrayList<Integer>();
						groupIds.add(Integer.valueOf(infoBean.getGROUP_INFO()
								.getGroupId() + ""));
						GroupListService.getService(HomeGpActivity.this)
								.deleteList(groupIds);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					btn.setBackgroundResource(R.drawable.btn_red_pressed);
					btn.setEnabled(false);
					if (resultCode == 2) {
						showTip("您是圈主或者管理员，无法退出圈子");
					} else if (resultCode == 3) {
						showTip("您不在圈子内");
					} else if (resultCode == 0) {
						showTip(T.ErrStr);
					}
				}
				break;
			case WORK_SHOW_TIP:
				showTip((String) msg.obj);
				break;
			case WORK_REFRESH_CUR_FRAGMENT:
				mLabels.onLableChoise(0);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!mScroll.isTop()) {
			mScroll.checkTop(mScroll.TOP_OFFSET);
		} else {
			super.onBackPressed();
		}
	}
}
