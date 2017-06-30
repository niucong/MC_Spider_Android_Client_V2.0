package com.datacomo.mc.spider.android.view;
//package com.datacomo.mc.spider.android.view;
//
//import java.util.ArrayList;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.datacomo.mc.spider.android.AboutActivity;
//import com.datacomo.mc.spider.android.BackupContactsActivity;
//import com.datacomo.mc.spider.android.BaseMidMenuActivity;
//import com.datacomo.mc.spider.android.BaseUMengActivity;
//import com.datacomo.mc.spider.android.CloudFileActivity;
//import com.datacomo.mc.spider.android.FindFriendActivity;
//import com.datacomo.mc.spider.android.FriendGroupActivity;
//import com.datacomo.mc.spider.android.HomePgActivity;
//import com.datacomo.mc.spider.android.InfoWallActivity;
//import com.datacomo.mc.spider.android.InterGroupActivity;
//import com.datacomo.mc.spider.android.LoginActivity;
//import com.datacomo.mc.spider.android.MailListActivity;
//import com.datacomo.mc.spider.android.MsgActivity;
//import com.datacomo.mc.spider.android.NoteActivity;
//import com.datacomo.mc.spider.android.OpenHomePage;
//import com.datacomo.mc.spider.android.R;
//import com.datacomo.mc.spider.android.ResetPasswordActivity;
//import com.datacomo.mc.spider.android.SearchActivity;
//import com.datacomo.mc.spider.android.SelectActivity;
//import com.datacomo.mc.spider.android.application.App;
//import com.datacomo.mc.spider.android.bean.BackupContactsInfo;
//import com.datacomo.mc.spider.android.db.ContactsBookService;
//import com.datacomo.mc.spider.android.db.FriendListService;
//import com.datacomo.mc.spider.android.db.GroupListService;
//import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
//import com.datacomo.mc.spider.android.dialog.CheckUpdateVersion;
//import com.datacomo.mc.spider.android.dialog.DialogController;
//import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
//import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
//import com.datacomo.mc.spider.android.enums.Type;
//import com.datacomo.mc.spider.android.net.APIRequestServers;
//import com.datacomo.mc.spider.android.service.LocalDataService;
//import com.datacomo.mc.spider.android.service.NotificationService;
//import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
//import com.datacomo.mc.spider.android.util.AppSharedPreferences;
//import com.datacomo.mc.spider.android.util.BundleKey;
//import com.datacomo.mc.spider.android.util.CacheUtil;
//import com.datacomo.mc.spider.android.util.L;
//import com.datacomo.mc.spider.android.util.LogicUtil;
//import com.datacomo.mc.spider.android.util.T;
//import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
//
//public class MenuPage extends LinearLayout implements OnClickListener {
//	private final static String TAG = "MenuPage";
//	private static ArrayList<MenuPage> arryMenus = new ArrayList<MenuPage>();
//
//	enum Items {
//		// 常用
//		HEAD_USER, ITEM_INFO, ITEM_MSG, ITEM_INTER, ITEM_FRIEND, ITEM_ADDFRIEND,
//		// 应用
//		HEAD_USE, ITEM_FILES, ITEM_NOTES, ITEM_EMAIL, ITEM_COLLECT, ITEM_BACKUP,
//		// 其他
//		HEAD_ACCOUNT, ITEM_SETTINGS, ITEM_MANAGE, ITEM_MORE, ITEM_CACHE, ITEM_ABOUT, ITEM_LOGOUT, ITEM_EXIT, ITEM_CHECK, WARN_SETTING, SET_PASSWORD,
//		// 二级
//		INFO_MSG_PLETTER, INFO_MSG_QUUCHAT, INFO_MSG_NOTICE, INFO_MSG_GREET, INFO_OPEN_PAGE
//	}
//
//	private Context mContext;
//	private Resources res;
//	private TextView userName;
//	private ImageView userHead;
//	private SearchBar searchBar;
//	private LinearLayout menuView, details;
//	private ItemInfo notice, quuchat, pletter, call;
//
//	private UserBusinessDatabase business;
//	private AppSharedPreferences sharedMessage;
//	private CheckUpdateVersion checkUpdateVersion = null;
//	/** 后台是否正在运行检测版本线程 */
//	public static boolean versionThreadRun = false;
//
//	private boolean showTip;
//	private static String session_key, name, headUrlPath, mberId;
//	// 通知、私信、圈聊
//	private int nNum, lNum, pNum, gNum;
//
//	// public static boolean downloadingCloudFile = false;
//
//	private SpinnerProgressDialog spdDialog;
//
//	public MenuPage(Context context) {
//		super(context);
//		mContext = context;
//		res = mContext.getResources();
//		RelativeLayout menus = (RelativeLayout) LayoutInflater.from(mContext)
//				.inflate(R.layout.layout_menu, null);
//
//		business = new UserBusinessDatabase(App.app);
//		sharedMessage = new AppSharedPreferences(App.app);
//		spdDialog = new SpinnerProgressDialog(mContext);
//
//		addView(menus);
//		findViews();
//		setMenusView();
//		arryMenus.add(this);
//	}
//
//	private void setNewNum(int[] nums) {
//		if (nums != null
//				&& (nNum != nums[0] || lNum != nums[1] || pNum != nums[2] || gNum != nums[3])) {
//			if (nNum != nums[0]) {
//				if (nums[0] > 0) {
//					notice.setNumShow(true);
//				} else {
//					notice.setNumShow(false);
//				}
//			}
//			if (lNum != nums[1]) {
//				if (nums[1] > 0) {
//					pletter.setNumShow(true);
//				} else {
//					pletter.setNumShow(false);
//				}
//			}
//
//			if (pNum != nums[2]) {
//				if (nums[2] > 0) {
//					quuchat.setNumShow(true);
//				} else {
//					quuchat.setNumShow(false);
//				}
//			}
//
//			if (gNum != nums[3]) {
//				if (nums[3] > 0) {
//					call.setNumShow(true);
//				} else {
//					call.setNumShow(false);
//				}
//			}
//
//			nNum = nums[0];
//			lNum = nums[1];
//			pNum = nums[2];
//			gNum = nums[3];
//		}
//	}
//
//	private void findViews() {
//		searchBar = (SearchBar) findViewById(R.id.search_bar);
//		userName = (TextView) findViewById(R.id.name);
//		userHead = (ImageView) findViewById(R.id.head_img);
//		menuView = (LinearLayout) findViewById(R.id.menu_view);
//		details = (LinearLayout) findViewById(R.id.details);
//
//		searchBar.changeInputBg();
//
//		searchBar.getEditText().setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				((InputMethodManager) mContext
//						.getSystemService(Context.INPUT_METHOD_SERVICE))
//						.hideSoftInputFromWindow(((Activity) mContext)
//								.getCurrentFocus().getWindowToken(),
//								InputMethodManager.HIDE_NOT_ALWAYS);
//
//				LogicUtil.enter(mContext, SearchActivity.class, null, false);
//				((Activity) mContext).overridePendingTransition(
//						R.anim.push_up_in, R.anim.fuzzy_out);
//				return true;
//			}
//		});
//
//	}
//
//	private void getUserInfo() {
//		try {
//			session_key = sharedMessage.getSessionKey();
//			name = business.getName(session_key);
//			headUrlPath = business.getHeadUrlPath(session_key);
//			mberId = business.getMemberId(session_key);
//
//			L.d(TAG, "getUserInfo session_key=" + session_key + ",name=" + name
//					+ ",headUrlPath=" + headUrlPath + ",mberId=" + mberId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void refreshNum(int[] nums) {
//		for (MenuPage menu : arryMenus) {
//			menu.setNewNum(nums);
//		}
//	}
//
//	public static void refreshInfo() {
//		for (MenuPage menu : arryMenus) {
//			if (null != menu) {
//				menu.getUserInfo();
//				menu.resetName(name);
//				menu.resetHeadUrl(headUrlPath);
//			}
//		}
//	}
//
//	public void resetName(String reName) {
//		userName.setText(reName);
//	}
//
//	public void resetHeadUrl(String reHeadUrl) {
//		MyFinalBitmap.setHeader(mContext, userHead, ThumbnailImageUrl
//				.getThumbnailHeadUrl(reHeadUrl,
//						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY));
//	}
//
//	/**
//	 * 添加默认menu信息
//	 */
//	private void setMenusView() {
//		getUserInfo();
//		resetName(name);
//		resetHeadUrl(headUrlPath);
//
//		details.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Bundle b = new Bundle();
//				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
//				b.putString("id", mberId);
//				b.putString("name", name);
//				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
//			}
//		});
//
//		setMenuHead("常用", Items.HEAD_USER);
//		setMenu(R.drawable.menu_info, "动态墙", Items.ITEM_INFO);
//		setMenu(R.drawable.menu_msg, "消息", Items.ITEM_MSG);
//		setMenu(R.drawable.menu_inter, "交流圈", Items.ITEM_INTER);
//		setMenu(R.drawable.menu_friend, "朋友圈", Items.ITEM_FRIEND);
//		setMenu(R.drawable.menu_addfriend, "添加朋友", Items.ITEM_ADDFRIEND);
//		setMenuHead("应用", Items.HEAD_USE);
//		setMenu(R.drawable.menu_email, "邮件管家", Items.ITEM_EMAIL);
//		setMenu(R.drawable.menu_file, "云文件", Items.ITEM_FILES);
//		setMenu(R.drawable.menu_note, "云笔记", Items.ITEM_NOTES);
//		setMenu(R.drawable.menu_star, "我的收藏", Items.ITEM_COLLECT);
//		setMenu(R.drawable.menu_open, "开放主页", Items.INFO_OPEN_PAGE);
//		setMenu(R.drawable.menu_contacts, "备份通讯录", Items.ITEM_BACKUP);
//
//		// setMenuHead("账号管理", HEAD_ACCOUNT);
//		// setMenu(R.drawable.menu_settings, "设置", ITEM_SETTINGS);
//		// setMenu(R.drawable.menu_manage, "账号管理", ITEM_MANAGE);
//		// setMenu(R.drawable.menu_more, "更多", ITEM_MORE);
//
//		setMenuHead("其它", Items.HEAD_ACCOUNT);
//		setMenu(R.drawable.menu_manage, "提醒设置", Items.WARN_SETTING);
//		setMenu(R.drawable.menu_key, "修改密码", Items.SET_PASSWORD);
//		setMenu(R.drawable.menu_new, "检测版本", Items.ITEM_CHECK);
//		setMenu(R.drawable.menu_clean, "清除缓存", Items.ITEM_CACHE);
//		setMenu(R.drawable.menu_about, "关于优优工作圈", Items.ITEM_ABOUT);
//		// setMenu(R.drawable.menu_logout, "切换账号", Items.ITEM_LOGOUT);
//		setMenu(R.drawable.menu_exit, "退出", Items.ITEM_EXIT);
//
//		notice = new ItemInfo(mContext, R.drawable.submenu_notice, this,
//				Items.INFO_MSG_NOTICE);
//		pletter = new ItemInfo(mContext, R.drawable.submenu_pletter, this,
//				Items.INFO_MSG_PLETTER);
//		quuchat = new ItemInfo(mContext, R.drawable.submenu_quuchat, this,
//				Items.INFO_MSG_QUUCHAT);
//		call = new ItemInfo(mContext, R.drawable.submenu_hello, this,
//				Items.INFO_MSG_GREET);
//		refreshMenu(new ItemInfo[] { notice, pletter, quuchat, call },
//				Items.ITEM_MSG);
//	}
//
//	/**
//	 * 添加menu header
//	 * 
//	 * @param menuHeadText
//	 * @param tag
//	 */
//	private void setMenuHead(String menuHeadText, Items items) {
//		RelativeLayout menuHead = (RelativeLayout) View.inflate(mContext,
//				R.layout.menu_head, null);
//		((TextView) menuHead.findViewById(R.id.menus_head))
//				.setText(menuHeadText);
//		menuView.addView(menuHead);
//	}
//
//	/**
//	 * 添加menu item
//	 * 
//	 * @param icon
//	 * @param menuText
//	 * @param menuInfos
//	 * @param tag
//	 */
//	private void setMenu(int icon, String menuText, Items tag) {
//		LinearLayout menuItem = (LinearLayout) LayoutInflater.from(mContext)
//				.inflate(R.layout.menu_item, null);
//		((TextView) menuItem.findViewById(R.id.menu_text)).setText(menuText);
//		if (0 != icon) {
//			ImageView img = (ImageView) menuItem.findViewById(R.id.menu_img);
//			img.setImageDrawable(res.getDrawable(icon));
//		}
//		menuItem.setTag(tag);
//		menuItem.setOnClickListener(this);
//		menuView.addView(menuItem);
//	}
//
//	/**
//	 * 刷新某个item的标签信息
//	 * 
//	 * @param menuInfos
//	 * @param tag
//	 */
//	public void refreshMenu(ItemInfo[] menuInfos, Items item) {
//		LinearLayout menuItem = (LinearLayout) menuView.findViewWithTag(item);
//		LinearLayout content = (LinearLayout) menuItem
//				.findViewById(R.id.content);
//		int cCount = content.getChildCount();
//		L.i(TAG, "refreshMenu cCount=" + cCount);
//		for (int i = cCount - 1; i > 1; i--) {
//			content.removeViewAt(i);
//		}
//
//		int mCount = menuItem.getChildCount();
//		L.i(TAG, "refreshMenu mCount=" + mCount);
//		if (null != menuInfos && mCount == 1) {
//			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
//			// LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
//			// content.getMeasuredHeight(), LayoutParams.WRAP_CONTENT, 1);
//
//			int mLength = menuInfos.length;
//			L.i(TAG, "refreshMenu mLength=" + mLength);
//			for (int i = 0; i < mLength; i++) {
//				content.addView(menuInfos[i], ll);
//			}
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		String curActivityName = ((Activity) mContext).getClass()
//				.getSimpleName();
//		switch ((Items) v.getTag()) {
//		case ITEM_INFO:// 动态墙
//			InfoWallActivity.isNeedRefresh = true;
//			if (!curActivityName.equals("InfoWallActivity")) {
//				LogicUtil.enter(mContext, InfoWallActivity.class, null, false);
//			} else {
//				// ((InfoWallActivity) mContext).closeMenuPage();
//			}
//			break;
//		case ITEM_MSG:// 通知
//			if (!curActivityName.equals("MsgActivity")) {
//				LogicUtil.enter(mContext, MsgActivity.class, null, false);
//			} else {
//				((MsgActivity) mContext).closeMenuPage();
//			}
//			break;
//		case ITEM_INTER:// 交流圈
//			if (!curActivityName.equals("InterGroupActivity")) {
//				LogicUtil
//						.enter(mContext, InterGroupActivity.class, null, false);
//			} else {
//				((InterGroupActivity) mContext).closeMenuPage();
//			}
//			break;
//		case ITEM_FRIEND:// 朋友圈
//			if (!curActivityName.equals("FriendGroupActivity")) {
//				LogicUtil.enter(mContext, FriendGroupActivity.class, null,
//						false);
//			} else {
//				((FriendGroupActivity) mContext).closeMenuPage();
//			}
//			break;
//
//		case ITEM_ADDFRIEND:// 添加朋友圈
//			if (!curActivityName.equals("FindFriendActivity")) {
//				Bundle bundle = new Bundle();
//				bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.ADDFRIEND);
//				LogicUtil.enter(mContext, FindFriendActivity.class, bundle,
//						false);
//			} else {
//				((FindFriendActivity) mContext).closeMenuPage(Type.ADDFRIEND);
//			}
//			break;
//
//		case ITEM_NOTES:// 云笔记
//			if (!curActivityName.equals("NoteActivity")) {
//				LogicUtil.enter(mContext, NoteActivity.class, null, false);
//			} else {
//				((NoteActivity) mContext).closeMenuPage();
//			}
//			break;
//		case ITEM_FILES:// 云文件
//			if (!curActivityName.equals("CloudFileActivity")) {
//				LogicUtil.enter(mContext, CloudFileActivity.class, null, false);
//			} else {
//				((CloudFileActivity) mContext).closeMenuPage();
//			}
//			break;
//		// case ITEM_NOTES:
//		// break;
//		case INFO_OPEN_PAGE:
//			if (!curActivityName.equals("OpenHomePage")) {
//				LogicUtil.enter(mContext, OpenHomePage.class, null, false);
//			} else {
//				((OpenHomePage) mContext).closeMenuPage();
//			}
//			break;
//		case ITEM_EMAIL:
//			LogicUtil.enter(mContext, MailListActivity.class, null, false);
//			break;
//		case ITEM_COLLECT:
//			LogicUtil.enter(mContext, SelectActivity.class, null, false);
//			break;
//		case ITEM_BACKUP:
//			LogicUtil
//					.enter(mContext, BackupContactsActivity.class, null, false);
//			break;
//
//		// case ITEM_SETTINGS:
//		// break;
//		// case ITEM_MANAGE:
//		// break;
//		// case ITEM_MORE:
//		// break;
//		case WARN_SETTING:
//			DialogController.getInstance().warnDialog(mContext).show();
//			break;
//		case SET_PASSWORD:
//			LogicUtil.enter(mContext, ResetPasswordActivity.class, null, false);
//			break;
//		case ITEM_CHECK:
//			chechVersion(true);
//			break;
//		case ITEM_CACHE:
//			spdDialog.showProgressDialog("正在处理中...");
//			new CacheThread().start();
//			break;
//		case ITEM_ABOUT:
//			LogicUtil.enter(mContext, AboutActivity.class, null, false);
//			break;
//		case ITEM_EXIT:
//			// new AlertDialog.Builder(mContext)
//			// .setTitle("确认退出？")
//			// .setPositiveButton("确定",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(DialogInterface dialog,
//			// int which) {
//			// if (((Activity) mContext).isTaskRoot()) {
//			// ((Activity) mContext).finish();
//			// } else {
//			// Intent i = new Intent(getContext(),
//			// InfoWallActivity.class);
//			// i.putExtra("action", "exit");
//			// mContext.startActivity(i);
//			// }
//			// }
//			// }).setNegativeButton("取消", null).show();
//			// break;
//		case ITEM_LOGOUT:
//			new AlertDialog.Builder(mContext)
//					.setTitle("提示")
//					.setMessage("退出当前帐号？")
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									new Thread() {
//										public void run() {
//											try {
//												APIRequestServers
//														.logout(App.app);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										};
//									}.start();
//
//									// cleanAccountInfo(mContext, business,
//									// sharedMessage);
//									stopServiceNotification();
//
//									business.updateUserPassword(session_key, "");
//									sharedMessage.saveSessionKey("");
//
//									// Bundle bundle = new Bundle();
//									// bundle.putString("action", "logout");
//									// LogicUtil.enter(mContext,
//									// InfoWallActivity.class, bundle,
//									// true);
//
//									for (Activity a : BaseUMengActivity.activityList) {
//										if (a != null) {
//											a.finish();
//										}
//									}
//									Intent i = new Intent(getContext(),
//											LoginActivity.class);
//									i.putExtra("action", "logout");
//									i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//									mContext.startActivity(i);
//									LogicUtil.showEnterAni(mContext);
//								}
//							}).setNegativeButton("取消", null).show();
//			break;
//
//		case INFO_MSG_QUUCHAT:
//			if (!curActivityName.equals("MsgActivity")) {
//				Bundle b = new Bundle();
//				b.putInt("cut", MsgActivity.MSG_GROUPCHAT);
//				LogicUtil.enter(mContext, MsgActivity.class, b, false);
//			} else {
//				((MsgActivity) mContext).closeMenuPage();
//				((MsgActivity) getContext())
//						.performOnTabClick(MsgActivity.MSG_GROUPCHAT);
//			}
//			break;
//
//		case INFO_MSG_PLETTER:
//			if (!curActivityName.equals("MsgActivity")) {
//				Bundle b = new Bundle();
//				b.putInt("cut", 1);
//				LogicUtil.enter(mContext, MsgActivity.class, b, false);
//			} else {
//				((MsgActivity) mContext).closeMenuPage();
//				((MsgActivity) getContext())
//						.performOnTabClick(MsgActivity.MSG_PLETTER);
//			}
//			break;
//		case INFO_MSG_NOTICE:
//			if (!curActivityName.equals("MsgActivity")) {
//				LogicUtil.enter(mContext, MsgActivity.class, null, false);
//			} else {
//				((MsgActivity) mContext).closeMenuPage();
//				((MsgActivity) getContext())
//						.performOnTabClick(MsgActivity.MSG_NOTICE);
//			}
//			break;
//		case INFO_MSG_GREET:
//			if (!curActivityName.equals("MsgActivity")) {
//				Bundle b = new Bundle();
//				b.putInt("cut", 3);
//				LogicUtil.enter(mContext, MsgActivity.class, b, false);
//			} else {
//				((MsgActivity) mContext).closeMenuPage();
//				((MsgActivity) getContext())
//						.performOnTabClick(MsgActivity.MSG_GREET);
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void stopServiceNotification() {
//		((NotificationManager) mContext
//				.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
//		mContext.stopService(new Intent(mContext, NotificationService.class));
//	}
//
//	public static void cleanAccountInfo(Context context,
//			UserBusinessDatabase business, AppSharedPreferences sharedMessage) {
//		L.d(TAG, "cleanAccountInfo...");
//		new ContactsBookService(context).delete();
//
//		GroupListService.getService(context).delete();
//		FriendListService.getService(context).deleteAll();
//		business.delete(sharedMessage.getSessionKey());
//		sharedMessage.saveLongMessage("NotificationSetup",
//				"startDeleteTimeFriend", 0);
//		sharedMessage.saveLongMessage("NotificationSetup",
//				"startUpdateTimeFriend", 0);
//		sharedMessage.saveLongMessage("NotificationSetup",
//				"startDeleteTimeGroup", 0);
//		sharedMessage.saveLongMessage("NotificationSetup",
//				"startUpdateTimeGroup", 0);
//		sharedMessage.saveStringMessage("NotificationSetup", "VersionName", "");
//		sharedMessage.saveStringMessage("BackupContacts", "LastBackupContacts",
//				"");
//		sharedMessage.saveStringMessage("NotificationSetup",
//				"uploadcontacts_time", "");
//		LocalDataService.clearAllData(context);
//	}
//
//	class ItemInfo extends LinearLayout {
//
//		private TextView num;
//
//		public ItemInfo(Context context, int source, OnClickListener listener,
//				Items item) {
//			super(context);
//
//			LayoutInflater inflater = LayoutInflater.from(context);
//			View view = inflater.inflate(R.layout.item_menu_msg, null);
//			ImageView img = (ImageView) view
//					.findViewById(R.id.item_menu_msg_name);
//			// setBackgroundResource(R.drawable.msg_num_bg);
//			// getBackground().setAlpha(100);
//			img.setImageResource(source);
//
//			num = (TextView) view.findViewById(R.id.item_menu_msg_num);
//
//			img.setTag(item);
//			if (null != listener)
//				img.setOnClickListener(listener);
//			addView(view);
//		}
//
//		public void setNumShow(boolean flag) {
//			if (flag) {
//				num.setVisibility(View.VISIBLE);
//			} else {
//				num.setVisibility(View.GONE);
//			}
//		}
//	}
//
//	public void chechVersion(boolean flag) {
//		L.d(TAG, "chechVersion versionThreadRun=" + versionThreadRun);
//		if (!versionThreadRun) {
//			showTip = flag;
//			if (showTip) {
//				spdDialog.showProgressDialog("正在检测版本...");
//			}
//			new VersionThread().start();
//		} else {
//			if (flag)
//				T.show(App.app, "新版本正在检测或正在下载...");
//		}
//	}
//
//	public void getBackupTime() {
//		// new Thread() {
//		// public void run() {
//		// try {
//		// sleep(10 * 1000);
//		try {
//			Object[] objects = APIRequestServers.getBackupTimes(App.app, false);
//			int resultCode = (Integer) objects[0];
//			if (resultCode == 1) {
//				@SuppressWarnings("unchecked")
//				ArrayList<BackupContactsInfo> infos = (ArrayList<BackupContactsInfo>) objects[1];
//				Message msg = new Message();
//				msg.what = 3;
//				if (infos != null && infos.size() > 0) {
//					BackupContactsInfo info = infos.get(0);
//					business.saveBackupOrRenewContactsTime(session_key, true,
//							info.getTime());
//					msg.obj = info.getTime();
//				} else {
//					msg.obj = 0L;
//				}
//				handler.sendMessage(msg);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// } catch (InterruptedException e) {
//		// e.printStackTrace();
//		// }
//		// };
//		// }.start();
//	}
//
//	/**
//	 * 检测新版本线程
//	 */
//	class VersionThread extends Thread {
//		@Override
//		public void run() {
//			super.run();
//			versionThreadRun = true;
//			boolean isUpdate = false;
//			try {
//				checkUpdateVersion = new CheckUpdateVersion(mContext);
//				isUpdate = checkUpdateVersion.updateVersion();
//				L.i(TAG, "VersionThread isUpdate = " + isUpdate);
//				if (isUpdate) {
//					handler.sendEmptyMessage(1);
//				} else {
//					versionThreadRun = false;
//					handler.sendEmptyMessage(2);
//				}
//			} catch (Exception e) {
//				versionThreadRun = false;
//				handler.sendEmptyMessage(2);
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 清除缓存
//	 */
//	class CacheThread extends Thread {
//		@Override
//		public void run() {
//			super.run();
//			new CacheUtil().cleanCachePhoto(mContext);
//			handler.sendEmptyMessage(4);
//		}
//	}
//
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				if (showTip) {
//					updateUI(T.ErrStr);
//				} else {
//					updateUI("");
//				}
//				break;
//			case 1:
//				updateUI("");
//				InfoWallActivity.isReqVersion = false;
//				if (checkUpdateVersion == null)
//					checkUpdateVersion = new CheckUpdateVersion(mContext);
//				checkUpdateVersion.versionDialog();
//				break;
//			case 2:
//				if (showTip) {
//					updateUI("当前已是最新版本");
//				} else {
//					updateUI("");
//				}
//				break;
//			case 3:
//				try {
//					DialogController.getInstance().promptBackupDialog(mContext,
//							session_key, (Long) msg.obj);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			case 4:
//				updateUI("清除成功");
//				break;
//			default:
//				break;
//			}
//		}
//	};
//
//	private void updateUI(String msg) {
//		L.d(TAG, "updateUI msg=" + msg);
//		spdDialog.cancelProgressDialog(msg);
//	}
//
// }
