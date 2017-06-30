package com.datacomo.mc.spider.android.fragmt;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BackupContactsActivity;
import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.BaseUMengActivity;
import com.datacomo.mc.spider.android.CloudFileActivity;
import com.datacomo.mc.spider.android.FindFriendActivity;
import com.datacomo.mc.spider.android.FriendGroupActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.InterGroupActivity;
import com.datacomo.mc.spider.android.LoginActivity;
import com.datacomo.mc.spider.android.MailListActivity;
import com.datacomo.mc.spider.android.NoteActivity;
import com.datacomo.mc.spider.android.OpenHomePage;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.SearchActivity;
import com.datacomo.mc.spider.android.SelectActivity;
import com.datacomo.mc.spider.android.SettingActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

@SuppressLint("ValidFragment")
public class MenuFragment extends LinearLayout implements OnClickListener {
	private static final String TAG = "MenuFragment";

	private RelativeLayout rl_name;
	public PopupWindow pw;
	private View v;
	private TextView tv_info, tv_groups, tv_friends, tv_add, tv_search,
			tv_file, tv_note, tv_mail, tv_contact, tv_collection, tv_open,
			tv_isetting, tv_exit, tv_name;// , tv_asetting
	private ImageView iv_head;

	private UserBusinessDatabase business;
	/** 后台是否正在运行检测版本线程 */
	public static boolean versionThreadRun = false;
	private static String session_key, name, headUrlPath, mberId;

	// private SlidingMenu sm;

	private static ArrayList<MenuFragment> arryMenus = new ArrayList<MenuFragment>();

	// public MenuFragment(SlidingMenu sm) {
	// this.sm = sm;
	//
	// business = new UserBusinessDatabase(App.app);
	// }
	private Context mContext;

	public MenuFragment(Context context) {
		super(context);
		this.mContext = context;
		LinearLayout menus = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.menu, null);
		addView(menus);
		findView();
		setView();
		arryMenus.add(this);
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View root = inflater.inflate(R.layout.menu, container, false);
	// findView(root);
	// setViews();
	// return root;
	// }

	private void findView() {
		rl_name = (RelativeLayout) findViewById(R.id.menu_name_layout);
		tv_info = (TextView) findViewById(R.id.menu_info);
		tv_groups = (TextView) findViewById(R.id.menu_groups);
		tv_friends = (TextView) findViewById(R.id.menu_friends);
		tv_add = (TextView) findViewById(R.id.menu_add);
		tv_search = (TextView) findViewById(R.id.menu_search);
		tv_file = (TextView) findViewById(R.id.menu_file);
		tv_note = (TextView) findViewById(R.id.menu_note);
		tv_mail = (TextView) findViewById(R.id.menu_mail);
		tv_contact = (TextView) findViewById(R.id.menu_contact);
		tv_collection = (TextView) findViewById(R.id.menu_collection);
		tv_open = (TextView) findViewById(R.id.menu_open);

		tv_name = (TextView) findViewById(R.id.menu_name);
		iv_head = (ImageView) findViewById(R.id.menu_head);
	}

	private void setView() {
		rl_name.setOnClickListener(this);
		tv_info.setOnClickListener(this);
		tv_groups.setOnClickListener(this);
		tv_friends.setOnClickListener(this);
		tv_add.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		tv_file.setOnClickListener(this);
		tv_note.setOnClickListener(this);
		tv_mail.setOnClickListener(this);
		tv_contact.setOnClickListener(this);
		tv_collection.setOnClickListener(this);
		tv_open.setOnClickListener(this);

		iv_head.setOnClickListener(this);

		getUserInfo();
		resetName(name);
		resetHeadUrl(headUrlPath);
	}

	public static void refreshName(String reName) {
		for (MenuFragment menu : arryMenus) {
			menu.resetName(reName);
		}
	}

	private void resetName(String reName) {
		tv_name.setText(reName);
	}

	public void resetHeadUrl(String reHeadUrl) {
		MyFinalBitmap.setHeader(mContext, iv_head, ThumbnailImageUrl
				.getThumbnailHeadUrl(reHeadUrl,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY));
	}

	private void getUserInfo() {
		try {
			session_key = App.app.share.getSessionKey();
			name = business.getName(session_key);
			headUrlPath = business.getHeadUrlPath(session_key);
			mberId = business.getMemberId(session_key);

			L.d(TAG, "getUserInfo session_key=" + session_key + ",name=" + name
					+ ",headUrlPath=" + headUrlPath + ",mberId=" + mberId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		boolean isPop = false;
		switch (v.getId()) {
		case R.id.menu_name_layout:
			isPop = true;
			showPop();
			break;
		case R.id.menu_head:
			if (mContext.getClass() == HomePgActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", mberId);
				b.putString("name", name);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
			break;
		case R.id.menu_info:
			if (mContext.getClass() == InfoWallActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, InfoWallActivity.class, null, false);
			}
			break;
		case R.id.menu_groups:
			if (mContext.getClass() == InterGroupActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil
						.enter(mContext, InterGroupActivity.class, null, false);
			}
			break;
		case R.id.menu_friends:
			if (mContext.getClass() == FriendGroupActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, FriendGroupActivity.class, null,
						false);
			}
			break;
		case R.id.menu_add:
			if (mContext.getClass() == FindFriendActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				Bundle bundle = new Bundle();
				bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.ADDFRIEND);
				LogicUtil.enter(mContext, FindFriendActivity.class, bundle,
						false);
			}
			break;
		case R.id.menu_search:
			if (mContext.getClass() == SearchActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, SearchActivity.class, null, false);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_up_in, R.anim.fuzzy_out);
			}
			break;
		case R.id.menu_file:
			if (mContext.getClass() == CloudFileActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, CloudFileActivity.class, null, false);
			}
			break;
		case R.id.menu_note:
			if (mContext.getClass() == NoteActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, NoteActivity.class, null, false);
			}
			break;
		case R.id.menu_mail:
			if (mContext.getClass() == MailListActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, MailListActivity.class, null, false);
			}
			break;
		case R.id.menu_contact:
			if (mContext.getClass() == BackupContactsActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, BackupContactsActivity.class, null,
						false);
			}
			break;
		case R.id.menu_collection:
			if (mContext.getClass() == SelectActivity.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, SelectActivity.class, null, false);
			}
			break;
		case R.id.menu_open:
			if (mContext.getClass() == OpenHomePage.class) {
				// if (sm != null)
				// sm.showContent();
			} else {
				LogicUtil.enter(mContext, OpenHomePage.class, null, false);
			}
			break;
		case R.id.menu_isetting:
			// if (mContext.getClass() == ResetPasswordActivity.class) {
			// if (sm != null)
			// sm.showContent();
			// } else {
			LogicUtil.enter(mContext, SettingActivity.class, null, false);
			// }
			break;
		// case R.id.menu_asetting:
		// DialogController.getInstance().warnDialog(mContext).show();
		// break;
		case R.id.menu_exit:
			new AlertDialog.Builder(mContext)
					.setTitle("提示")
					.setMessage("退出当前帐号？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new Thread() {
										public void run() {
											try {
												APIRequestServers
														.logout(App.app);
											} catch (Exception e) {
												e.printStackTrace();
											}
										};
									}.start();

									// cleanAccountInfo(mContext, business,
									// sharedMessage);
									((NotificationManager) mContext
											.getSystemService(Context.NOTIFICATION_SERVICE))
											.cancelAll();
									mContext.stopService(new Intent(mContext,
											NotificationService.class));

									business.updateUserPassword(
											App.app.share.getSessionKey(), "");
									App.app.share.saveSessionKey("");

									// Bundle bundle = new Bundle();
									// bundle.putString("action", "logout");
									// LogicUtil.enter(mContext,
									// InfoWallActivity.class, bundle,
									// true);

									for (Activity a : BaseUMengActivity.activityList) {
										if (a != null) {
											a.finish();
										}
									}
									Intent i = new Intent(mContext,
											LoginActivity.class);
									i.putExtra("action", "logout");
									i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
									mContext.startActivity(i);
									LogicUtil.showEnterAni(mContext);
								}
							}).setNegativeButton("取消", null).show();
			break;
		default:
			break;
		}
		if (!isPop && pw != null && pw.isShowing())
			pw.dismiss();
	}

	private void showPop() {
		if (pw == null) {
			v = LayoutInflater.from(mContext).inflate(R.layout.menu_pop, null);
			pw = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			pw.setOutsideTouchable(true);
			pw.setFocusable(false);

			tv_isetting = (TextView) v.findViewById(R.id.menu_isetting);
			// tv_asetting = (TextView) v.findViewById(R.id.menu_asetting);
			tv_exit = (TextView) v.findViewById(R.id.menu_exit);

			tv_isetting.setOnClickListener(this);
			// tv_asetting.setOnClickListener(this);
			tv_exit.setOnClickListener(this);
		}
		if (pw.isShowing()) {
			pw.dismiss();
		} else {
			// pw.showAtLocation(rl_name, Gravity.BOTTOM, 0, 0);
			pw.showAsDropDown(rl_name, -40, -10);

			Animation show = AnimationUtils.loadAnimation(mContext,
					R.anim.p_enter_up);
			v.setVisibility(View.VISIBLE);
			v.startAnimation(show);
		}
	}

	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// }
}
