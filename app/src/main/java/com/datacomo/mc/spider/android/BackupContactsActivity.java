package com.datacomo.mc.spider.android;

import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.umeng.analytics.MobclickAgent;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class BackupContactsActivity extends BasicMenuActivity implements
		OnClickListener {
	private static final String TAG = "BackupContactsActivity";

	private LinearLayout backup_layout, renew_layout;

	private String sessionKey;
	private UserBusinessDatabase business;

	private long currentTime;

	private static boolean isBackupRun = false;
	public static boolean isRenewRun = false;

	public static BackupContactsActivity backupContacts;

	private boolean directBackup = false;

	private static SpinnerProgressDialog spDialog;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		backupHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "9");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_CONTACT;
		titleName = "备份通讯录";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.cloud_contacts, null);
		fl.addView(rootView);

		backupContacts = this;
		sessionKey = App.app.share.getSessionKey();
		business = new UserBusinessDatabase(this);
		spDialog = new SpinnerProgressDialog(this);

		findView();
		setView();

		directBackup = getIntent().getBooleanExtra("directBackup", false);
		if (directBackup) {
			backupContacts();
		}
	}

	private void findView() {
		backup_layout = (LinearLayout) findViewById(R.id.cloud_contacts_backup);
		renew_layout = (LinearLayout) findViewById(R.id.cloud_contacts_renew);
	}

	private void setView() {
		backup_layout.setOnClickListener(this);
		renew_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.cloud_contacts_backup:// 备份
			backupContacts();
			break;
		case R.id.cloud_contacts_renew:// 恢复
			LogicUtil.enter(this, BackupContactsReActivity.class, null, false);
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0:
					if (msg.arg1 > 1 && !spDialog.getPd().isShowing()) {
						return;
					}
					spDialog.showProgressDialog("正在备份(" + msg.arg1 + "/"
							+ msg.arg2 + ")...");
					break;
				case 1:
					if (spDialog != null && spDialog.getPd().isShowing())
						spDialog.showProgressDialog("开始备份通讯录...");
					break;
				case 3:
					long lastBackup = business.getBackupOrRenewContactsTime(
							sessionKey, true);
					L.d(TAG, "handler lastBackup=" + lastBackup);
					if (lastBackup != 0)
						break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * 备份通讯录
	 */
	private void backupContacts() {
		spDialog.showProgressDialog("准备备份通讯录...");
		spDialog.getPd().setOnCancelListener(
				new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						showTip("已转为后台备份");
					}
				});
		if (isBackupRun) {
			return;
		}
		new Thread() {
			public void run() {
				isBackupRun = true;
				currentTime = System.currentTimeMillis();
				Message msg = new Message();
				try {
					Object[] objects = APIRequestServers.backupContacts(
							BackupContactsActivity.this, handler);
					MCResult response = (MCResult) objects[0];
					int backupCount = (Integer) objects[1];
					if (response != null) {
						int resultCode = response.getResultCode();
						if (resultCode == 1) {
							// hasNewBackup = true;
							msg.what = 1;
							if (backupCount > 0) {
								msg.obj = "已将" + backupCount + "个手机联系人备份至云端。";
							}
							HashMap<String, String> newMap = ContactsUtil
									.getRawIdVersion(BackupContactsActivity.this);
							String newBackupStr = "";
							for (Iterator<?> iterator = newMap.keySet()
									.iterator(); iterator.hasNext();) {
								String key = (String) iterator.next();
								newBackupStr += key + "#" + newMap.get(key)
										+ "&";
							}
							L.getLongLog(TAG, "backupContacts", newBackupStr);
							App.app.share.saveStringMessage("BackupContacts",
									"LastBackupContacts", newBackupStr);
						} else if (resultCode == 10) {
							msg.what = 10;
							msg = unknownException(msg);
						} else {
							msg = unknownException(msg);
						}
					} else {
						msg = unknownException(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg = unknownException(msg);
				}
				try {
					spDialog.getPd().setOnCancelListener(null);
					backupHandler.sendMessage(msg);
					isBackupRun = false;
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (InfoWallActivity.infoWallActivity != null) {
					InfoWallActivity.infoWallActivity.uploadAndGetPhone(null);
				}
			};
		}.start();
	}

	/**
	 * 备份通讯录后刷新UI
	 */
	private Handler backupHandler = new Handler() {
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			switch (msg.what) {
			case 0:

				break;
			case 1:
				try {
					business.saveIntData(sessionKey, "nobackupcontacts_weeks",
							0);
					business.saveBackupOrRenewContactsTime(sessionKey, true,
							currentTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			cancelProgressDialog(str);
		};
	};

	/**
	 * 未知异常
	 * 
	 * @param msg
	 */
	private Message unknownException(Message msg) {
		msg.what = 0;
		msg.obj = T.ErrStr;
		return msg;
	}

	/**
	 * 关闭ProgressDialog并提示
	 * 
	 * @param str
	 */
	private void cancelProgressDialog(String str) {
		spDialog.cancelProgressDialog(str);
	}

	@Override
	protected void refresh() {

	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		try {
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
			menu.findItem(R.id.action_message).setVisible(drawerOpen);
			menu.findItem(R.id.action_write).setVisible(drawerOpen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
