package com.datacomo.mc.spider.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.CommentSendService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.CreateFile;
import com.datacomo.mc.spider.android.dialog.IsNetworkConnected;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

import dev.dworks.libs.actionbartoggle.ActionBarToggle;
//import com.datacomo.mc.spider.android.xmpp.XMPPManager;

public abstract class BasicMenuActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "BasicMenuActivity";

	private LinearLayout net_change;

	private MsgNumBroadcastReceiver numReceiver;
	private IntentFilter intentFilter;

	private RelativeLayout rl_name;
	private View v;
	public PopupWindow pw;
	private TextView tv_info, tv_groups, tv_friends, tv_add, tv_search,
			tv_file, tv_note, tv_mail, tv_contact, tv_collection, tv_open,
			tv_isetting, tv_exit, tv_name;
	private ImageView iv_head;

	private UserBusinessDatabase business;
	/** 后台是否正在运行检测版本线程 */
	public static boolean versionThreadRun = false;
	private static String session_key, name, headUrlPath, mberId;

	private static ArrayList<BasicMenuActivity> arryHeads = new ArrayList<BasicMenuActivity>();

	protected DrawerLayout mDrawerLayout;
	protected LinearLayout ll_menu;

	protected ActionBar ab;
	protected ActionBarToggle mDrawerToggle;

	enum Items {
		ITEM_USER, ITEM_INFO, ITEM_GROUPS, ITEM_FRIENDS, ITEM_ADDFRIEND, ITEM_SEARCH, ITEM_FILES, ITEM_NOTES, ITEM_MAIL, ITEM_CONTACT, ITEM_COLLECTION, ITEM_OPEN
	}

	protected Items items;
	protected String titleName;
	protected Menu menu;

	private PopupWindow pw_write;
	private CreateFile cFile;

	protected boolean isFrist = true;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		business = new UserBusinessDatabase(App.app);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		ll_menu = (LinearLayout) findViewById(R.id.layout_menu);
		View menu = LayoutInflater.from(this).inflate(R.layout.menu, null);
		ll_menu.addView(menu);// new MenuFragment(this)
		menu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (pw != null && pw.isShowing())
					pw.dismiss();
				return false;
			}
		});

		final SpannableString tn = new SpannableString(titleName);

		ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		ab.setIcon(R.drawable.action_logo);
		ab.setTitle(tn);

		ab.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.title_bg));

		mDrawerToggle = new ActionBarToggle(this, mDrawerLayout,
				R.drawable.menu_icon, 0, R.string.info_wall) {
			@Override
			public void onDrawerOpened(View drawerView) {
				final SpannableString yn = new SpannableString(
						App.app.share.getStringMessage("program", "welname",
								"优优工作圈"));
				getSupportActionBar().setTitle(yn);
				invalidateOptionsMenu();
				BaseData.hideKeyBoard(BasicMenuActivity.this);
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				getSupportActionBar().setTitle(tn);
				invalidateOptionsMenu();
				if (pw != null && pw.isShowing())
					pw.dismiss();
				super.onDrawerClosed(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		findView();
		setView();

		numReceiver = new MsgNumBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.MSG_NUMBERS);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

		arryHeads.add(this);
	}

	class CustomAdapter extends ArrayAdapter<String> {

		public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View view = convertView;
			if (null == view) {
				view = LayoutInflater.from(this.getContext()).inflate(
						R.layout.actionbar_spinner_item, null);
				((TextView) view).setText(getItem(position));
			}
			final LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			TextView view = (TextView) LayoutInflater.from(this.getContext())
					.inflate(R.layout.actionbar_spinner_item, null);
			view.setTextColor(Color.BLACK);
			view.setText(" " + getItem(position));
			return view;
		}
	}

	public class MsgNumBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			L.d(TAG, "MsgNumBroadcastReceiver action=" + action);
			if (BootBroadcastReceiver.MSG_NUMBERS.equals(action)) {
				Bundle b = intent.getExtras();
				int[] num = b.getIntArray("nums");
				if (items == Items.ITEM_INFO) {
					L.i(TAG, "MsgNumBroadcastReceiver 消息提示");
					if (num[0] > 0 || num[1] > 0 || num[2] > 0 || num[4] > 0) {
						// 有消息
						if (menu != null) {
							menu.findItem(R.id.action_message).setIcon(
									R.drawable.message_2);
						}
					} else {
						if (menu != null) {
							menu.findItem(R.id.action_message).setIcon(
									R.drawable.message);
						}
					}
				}
			}
			if (ConnectivityManager.CONNECTIVITY_ACTION.endsWith(action)) {
				if (!IsNetworkConnected.checkNetworkInfo(context)) {
					net_change.setVisibility(View.VISIBLE);
				} else {
					net_change.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(numReceiver, intentFilter);

		if (!IsNetworkConnected.checkNetworkInfo(this)) {
			net_change.setVisibility(View.VISIBLE);
			// IsNetworkConnected.settingNetWork(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem mi = menu.findItem(R.id.action_message);
		if (mi != null) {
			String nums = App.app.share.getStringMessage("isOtherLogin",
					"newNums", "0#0#0#0");
			int n = CommentSendService.getService(App.app)
					.getCount(session_key);
			if (n > 0 || (nums != null && !nums.startsWith("0#0#0#"))) {
				mi.setIcon(R.drawable.message_2);
			} else {
				mi.setIcon(R.drawable.message);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_refresh:
			refresh();
			return true;
		case R.id.action_message:
			LogicUtil.enter(this, MsgActivity.class, null, false);
			return true;
		case R.id.action_write:
			View v = LayoutInflater.from(this)
					.inflate(R.layout.write_all, null);
			writeAll(v);
			pw_write = new PopupWindow(v, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			pw_write.setOutsideTouchable(true);
			// pw_write.setFocusable(true);
			pw_write.showAsDropDown(net_change, 0,
					DensityUtil.dip2px(this, -70));
			// pw_write.setAnimationStyle(R.style.pop_style);
			// mDrawerLayout.closeDrawers();

			v.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (pw_write != null && pw_write.isShowing())
						pw_write.dismiss();
					return false;
				}
			});
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected SearchView searchView;

	@SuppressLint("InlinedApi")
	protected void showBack() {
		try {
			((ImageView) ((ViewGroup) findViewById(android.R.id.home)
					.getParent()).getChildAt(0))
					.setImageResource(R.drawable.action_back);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("InlinedApi")
	protected void showMenu() {
		try {
			((ImageView) ((ViewGroup) findViewById(android.R.id.home)
					.getParent()).getChildAt(0))
					.setImageResource(R.drawable.menu_icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLoadingState(boolean refreshing) {
		try {
			if (menu != null) {
				MenuItem mProgressMenu = menu.findItem(R.id.action_refresh);
				if (refreshing) {
					mProgressMenu
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					mProgressMenu.setActionView(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeAll(View v) {
		LinearLayout quubo = (LinearLayout) v.findViewById(R.id.write_quanbo);
		LinearLayout message = (LinearLayout) v
				.findViewById(R.id.write_message);
		LinearLayout file = (LinearLayout) v.findViewById(R.id.write_file);
		LinearLayout note = (LinearLayout) v.findViewById(R.id.write_note);
		LinearLayout mail = (LinearLayout) v.findViewById(R.id.write_mail);
		LinearLayout mood = (LinearLayout) v.findViewById(R.id.write_mood);

		quubo.setOnClickListener(this);
		message.setOnClickListener(this);
		file.setOnClickListener(this);
		note.setOnClickListener(this);
		mail.setOnClickListener(this);
		mood.setOnClickListener(this);
	}

	protected abstract void refresh();

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

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

		net_change = (LinearLayout) findViewById(R.id.net_change);
		net_change.setOnClickListener(this);
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
		L.d(TAG, "setView name=" + name + ",headUrlPath=" + headUrlPath);
		resetName(name);
		resetHeadUrl(headUrlPath);
	}

	private void resetName(String reName) {
		tv_name.setText(reName);
	}

	public static void refreshInfo() {
		for (BasicMenuActivity activity : arryHeads) {
			activity.getUserInfo();
			activity.resetName(name);
			activity.resetHeadUrl(headUrlPath);
		}
	}

	public void resetHeadUrl(String reHeadUrl) {
		MyFinalBitmap.setHeader(this, iv_head, ThumbnailImageUrl
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
		if (pw_write != null && pw_write.isShowing())
			pw_write.dismiss();
		switch (v.getId()) {
		case R.id.net_change:
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			startActivity(intent);
			break;
		case R.id.write_quanbo:
			Bundle bundle = new Bundle();
			bundle.putSerializable(BundleKey.TYPE_REQUEST,
					Type.STARTCREATGROUPTOPIC);
			LogicUtil.enter(this, GroupsChooserActivity.class, bundle, false);
			break;
		case R.id.write_message:
			Bundle bm = new Bundle();
			bm.putBoolean("isSendMsg", true);
			bm.putInt("type", 2);
			LogicUtil.enter(this, FriendsChooserActivity.class, bm, false);
			break;
		case R.id.write_file:
			cFile = new CreateFile(this);
			cFile.createDialog();
			break;
		case R.id.write_note:
			LogicUtil.enter(this, NoteCreateActivity.class, null, false);
			break;
		case R.id.write_mail:
			LogicUtil.enter(this, MailCreateActivity.class, null, false);
			break;
		case R.id.write_mood:
			Intent mIntent = new Intent(this, EditActivity.class);
			Bundle mb = new Bundle();
			mb.putString("ori", "");
			mb.putInt("typedata", EditActivity.TYPE_MOOD);
			mb.putInt("type", 0);
			mIntent.putExtras(mb);
			startActivity(mIntent);
			break;
		case R.id.menu_name_layout:
			isPop = true;
			showPop();
			break;
		case R.id.menu_head:
			// if (items == Items.ITEM_USER) {
			// mDrawerLayout.closeDrawers();
			// } else {
			Bundle b = new Bundle();
			b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			b.putString("id", mberId);
			b.putString("name", name);
			LogicUtil.enter(this, HomePgActivity.class, b, false);
			// }
			break;
		case R.id.menu_info:
			if (items == Items.ITEM_INFO) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, InfoWallActivity.class, null, false);
			}
			break;
		case R.id.menu_groups:
			if (items == Items.ITEM_GROUPS) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, InterGroupActivity.class, null, false);
			}
			break;
		case R.id.menu_friends:
			if (items == Items.ITEM_FRIENDS) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, FriendGroupActivity.class, null, false);
			}
			break;
		case R.id.menu_add:
			if (items == Items.ITEM_ADDFRIEND) {
				mDrawerLayout.closeDrawers();
			} else {
				Bundle bu = new Bundle();
				bu.putSerializable(BundleKey.TYPE_REQUEST, Type.ADDFRIEND);
				LogicUtil.enter(this, FindFriendActivity.class, bu, false);
			}
			break;
		case R.id.menu_search:
			if (items == Items.ITEM_SEARCH) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, SearchActivity.class, null, false);
				overridePendingTransition(R.anim.push_up_in, R.anim.fuzzy_out);
			}
			break;
		case R.id.menu_file:
			if (items == Items.ITEM_FILES) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, CloudFileActivity.class, null, false);
			}
			break;
		case R.id.menu_note:
			if (items == Items.ITEM_NOTES) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, NoteActivity.class, null, false);
			}
			break;
		case R.id.menu_mail:
			if (items == Items.ITEM_MAIL) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, MailListActivity.class, null, false);
			}
			break;
		case R.id.menu_contact:
			if (items == Items.ITEM_CONTACT) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil
						.enter(this, BackupContactsActivity.class, null, false);
			}
			break;
		case R.id.menu_collection:
			if (items == Items.ITEM_COLLECTION) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, SelectActivity.class, null, false);
			}
			break;
		case R.id.menu_open:
			if (items == Items.ITEM_OPEN) {
				mDrawerLayout.closeDrawers();
			} else {
				LogicUtil.enter(this, OpenHomePage.class, null, false);
			}
			break;
		case R.id.menu_isetting:
			LogicUtil.enter(this, SettingActivity.class, null, false);
			break;
		case R.id.menu_exit:
			new AlertDialog.Builder(this)
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

//											try {
//												XMPPManager.getConnection()
//														.disconnect();
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
										};
									}.start();

									// cleanAccountInfo(mContext, business,
									// sharedMessage);
									((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
											.cancelAll();
									stopService(new Intent(
											BasicMenuActivity.this,
											NotificationService.class));

									business.updateUserPassword(
											App.app.share.getSessionKey(), "");
									App.app.share.saveSessionKey("");
									App.app.share.saveStringMessage(
											"isOtherLogin", "newNums",
											"0#0#0#0");
									// App.app.share.saveStringMessage("program",
									// "welurl", "");

									for (Activity a : BaseUMengActivity.activityList) {
										if (a != null) {
											a.finish();
										}
									}
									Intent i = new Intent(
											BasicMenuActivity.this,
											LoginActivity.class);
									i.putExtra("action", "logout");
									i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
									startActivity(i);
									LogicUtil
											.showEnterAni(BasicMenuActivity.this);
								}
							}).setNegativeButton("取消", null).show();
			break;
		default:
			break;
		}
		if (!isPop && pw != null && pw.isShowing())
			pw.dismiss();
	}

	/**
	 * 退出
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			if (pw_write != null && pw_write.isShowing()) {
				pw_write.dismiss();
				return false;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			String filePath = null;
			switch (requestCode) {
			case CreateFile.IMAGE:
			case CreateFile.VIDEO:
			case CreateFile.AUDIO:
				// case CreateFile.TEXT:
				if (data != null) {
					Uri uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (uri != null) {
						Cursor cursor = getContentResolver().query(
								uri,
								new String[] { "_data", "_display_name",
										"_size", "mime_type" }, null, null,
								null);
						if (cursor != null) {
							cursor.moveToFirst();
							filePath = cursor.getString(0); // 图片文件路径
							cursor.close();
						} else {
							filePath = uri.getPath();
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, cFile.pictureName);
						uri = Uri.fromFile(picture);
						filePath = uri.getPath();
					}
				} else {
					L.d(TAG, "onActivityResult data=null");
					File saveFile = new File(ConstantUtil.CAMERA_PATH);
					File picture = new File(saveFile, cFile.pictureName);
					Uri uri = Uri.fromFile(picture);
					filePath = uri.getPath();
				}
				break;
			case CreateFile.OTHAR:
				filePath = data.getStringExtra("filePath");
				break;
			}
			L.i(TAG, "onActivityResult filePath=" + filePath);
			if (filePath != null && !"".equals(filePath)) {
				File file = new File(filePath);
				if (file.exists()) {
					if (ConstantUtil.uploadingList.contains(filePath)) {
						showTip("该文件正在上传中");
					} else {
						ConstantUtil.uploadingList.add(filePath);
						APIRequestServers.uploadFile(this, file,
								UploadMethodEnum.UPLOADFILE, null);
					}
				} else {
					showTip("找不到文件，请换其他方式");
				}
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mDrawerLayout.closeDrawers();
			}
		}, 500);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (pw != null && pw.isShowing())
			pw.dismiss();

		unregisterReceiver(numReceiver);
		net_change.setVisibility(View.GONE);
	}

	private void showPop() {
		if (pw == null) {
			v = LayoutInflater.from(this).inflate(R.layout.menu_pop, null);
			pw = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			pw.setOutsideTouchable(true);
			pw.setFocusable(false);

			tv_isetting = (TextView) v.findViewById(R.id.menu_isetting);
			tv_exit = (TextView) v.findViewById(R.id.menu_exit);

			tv_isetting.setOnClickListener(this);
			tv_exit.setOnClickListener(this);
		}
		if (pw.isShowing()) {
			pw.dismiss();
		} else {
			pw.showAsDropDown(rl_name, -40, -10);

			Animation show = AnimationUtils.loadAnimation(this,
					R.anim.p_enter_up);
			v.setVisibility(View.VISIBLE);
			v.startAnimation(show);
		}
	}

	public void showTip(String text) {
		T.show(this, text);
	}
}
