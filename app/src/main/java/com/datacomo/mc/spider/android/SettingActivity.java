package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.BackupContactsInfo;
import com.datacomo.mc.spider.android.db.ChatGroupMessageBeanService;
import com.datacomo.mc.spider.android.db.ChatMessageBeanService;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.db.FileUrlService;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.CheckUpdateVersion;
import com.datacomo.mc.spider.android.dialog.DialogController;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.TakedClientWelcomPicGroupBean;
import com.datacomo.mc.spider.android.net.been.map.MapTakedClientWelcomPicGroupBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CacheUtil;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class SettingActivity extends BasicActionBarActivity {
	private static String TAG = "SettingActivity";

	private TextView tv_msg, tv_psd, tv_version, tv_cache, tv_about, tv_down,
			tv_wel;

	private UserBusinessDatabase business;
	private CheckUpdateVersion checkUpdateVersion = null;
	/** 后台是否正在运行检测版本线程 */
	public static boolean versionThreadRun = false;

	private boolean showTip;
	private static String session_key;

	private MapTakedClientWelcomPicGroupBean bean;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	public SettingActivity() {
		business = new UserBusinessDatabase(App.app);
		session_key = App.app.share.getSessionKey();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.setting);
		// setTitle("设置", R.drawable.title_fanhui, null);
		ab.setTitle("设置");

		spdDialog = new SpinnerProgressDialog(this);

		initView();

		new GroupListTask().execute();
	}

	private void initView() {
		tv_msg = (TextView) findViewById(R.id.setting_msg);
		tv_psd = (TextView) findViewById(R.id.setting_psw);
		tv_version = (TextView) findViewById(R.id.setting_version);
		tv_cache = (TextView) findViewById(R.id.setting_cache);
		tv_about = (TextView) findViewById(R.id.setting_about);
		tv_down = (TextView) findViewById(R.id.setting_down);
		tv_wel = (TextView) findViewById(R.id.setting_wel);

		tv_msg.setOnClickListener(this);
		tv_psd.setOnClickListener(this);
		tv_version.setOnClickListener(this);
		tv_cache.setOnClickListener(this);
		tv_about.setOnClickListener(this);
		tv_down.setOnClickListener(this);
		tv_wel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.setting_msg:
			DialogController.getInstance().warnDialog(this).show();
			break;
		case R.id.setting_psw:
			LogicUtil.enter(this, ResetPasswordActivity.class, null, false);
			break;
		case R.id.setting_version:
			chechVersion(true);
			break;
		case R.id.setting_cache:
			spdDialog.showProgressDialog("正在处理中...");
			new CacheThread().start();
			break;
		case R.id.setting_down:
			LogicUtil.enter(this, TwoDimCodeActivity.class, null, false);
			break;
		case R.id.setting_about:
			LogicUtil.enter(this, AboutActivity.class, null, false);
			break;
		case R.id.setting_wel:
			Bundle bundle = new Bundle();
			bundle.putSerializable("MapTakedClientWelcomPicGroupBean", bean);
			LogicUtil.enter(this, WelSettingActivity.class, bundle, false);
			break;
		default:
			break;
		}
	}

	public static void cleanAccountInfo(Context context,
			UserBusinessDatabase business) {
		L.d(TAG, "cleanAccountInfo...");
		new ContactsBookService(context).delete();

		FileUrlService.getService(context).deleteAll();

		GroupListService.getService(context).delete();
		FriendListService.getService(context).deleteAll();

		ChatMessageBeanService.getService(context).deleteAll();
		ChatGroupMessageBeanService.getService(context).deleteAll();

		business.delete(App.app.share.getSessionKey());
		App.app.share.saveLongMessage("NotificationSetup",
				"startDeleteTimeFriendNew", 0);
		App.app.share.saveLongMessage("NotificationSetup",
				"startUpdateTimeFriendNew", 0);
		App.app.share.saveLongMessage("NotificationSetup",
				"startDeleteTimeGroup", 0);
		App.app.share.saveLongMessage("NotificationSetup",
				"startUpdateTimeGroup", 0);
		App.app.share.saveStringMessage("NotificationSetup", "VersionName", "");
		App.app.share.saveStringMessage("BackupContacts", "LastBackupContacts",
				"");
		App.app.share.saveStringMessage("NotificationSetup",
				"uploadcontacts_time", "");
		LocalDataService.clearAllData(context);
	}

	public void chechVersion(boolean flag) {
		L.d(TAG, "chechVersion versionThreadRun=" + versionThreadRun);
		if (!versionThreadRun) {
			showTip = flag;
			if (showTip) {
				spdDialog.showProgressDialog("正在检测版本...");
			}
			new VersionThread().start();
		} else {
			if (flag)
				T.show(App.app, "新版本正在检测或正在下载...");
		}
	}

	public void getBackupTime() {
		// new Thread() {
		// public void run() {
		// try {
		// sleep(10 * 1000);
		try {
			Object[] objects = APIRequestServers.getBackupTimes(App.app, false,
					0);
			int resultCode = (Integer) objects[0];
			if (resultCode == 1) {
				@SuppressWarnings("unchecked")
				ArrayList<BackupContactsInfo> infos = (ArrayList<BackupContactsInfo>) objects[1];
				Message msg = new Message();
				msg.what = 3;
				if (infos != null && infos.size() > 0) {
					BackupContactsInfo info = infos.get(0);
					business.saveBackupOrRenewContactsTime(session_key, true,
							info.getTime());
					msg.obj = info.getTime();
				} else {
					msg.obj = 0L;
				}
				L.d(TAG, "getBackupTime " + msg.obj);
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// };
		// }.start();
	}

	/**
	 * 检测新版本线程
	 */
	class VersionThread extends Thread {
		@Override
		public void run() {
			super.run();
			versionThreadRun = true;
			boolean isUpdate = false;
			try {
				if (tv_msg == null) {
					checkUpdateVersion = new CheckUpdateVersion(
							InfoWallActivity.infoWallActivity);
				} else {
					checkUpdateVersion = new CheckUpdateVersion(
							SettingActivity.this);
				}
				isUpdate = checkUpdateVersion.updateVersion();
				L.i(TAG, "VersionThread isUpdate = " + isUpdate);
				if (isUpdate) {
					handler.sendEmptyMessage(1);
				} else {
					versionThreadRun = false;
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				versionThreadRun = false;
				handler.sendEmptyMessage(2);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 清除缓存
	 */
	class CacheThread extends Thread {
		@Override
		public void run() {
			super.run();
			new CacheUtil().cleanCachePhoto(App.app);
			handler.sendEmptyMessage(4);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (showTip) {
					updateUI(T.ErrStr);
				} else {
					updateUI("");
				}
				break;
			case 1:
				updateUI("");
				InfoWallActivity.isReqVersion = false;
				if (checkUpdateVersion == null) {
					if (tv_msg == null) {
						checkUpdateVersion = new CheckUpdateVersion(
								InfoWallActivity.infoWallActivity);
					} else {
						checkUpdateVersion = new CheckUpdateVersion(
								SettingActivity.this);
					}
				}
				checkUpdateVersion.versionDialog();
				break;
			case 2:
				if (showTip) {
					updateUI("当前已是最新版本");
				} else {
					updateUI("");
				}
				break;
			case 3:
				try {
					DialogController.getInstance().promptBackupDialog(
							InfoWallActivity.infoWallActivity, session_key,
							(Long) msg.obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 4:
				updateUI("清除成功");
				break;
			default:
				break;
			}
		}
	};

	private void updateUI(String msg) {
		L.d(TAG, "updateUI msg=" + msg);
		spdDialog.cancelProgressDialog(msg);
	}

	class GroupListTask extends AsyncTask<String, Integer, MCResult> {

		// public GroupListTask() {
		// setLoadingState(true);
		// }

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.clientWelcomePic(App.app,
						ClientWelcomePicEnum.TAKEDCLIENTWELCOMPICGROUPS, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			// setLoadingState(false);
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
				} else if (null != mcResult && 1 == mcResult.getResultCode()) {
					try {
						bean = (MapTakedClientWelcomPicGroupBean) JsonParseTool
								.dealComplexResult(mcResult.getResult()
										.toString(),
										MapTakedClientWelcomPicGroupBean.class);
						int STATUS = bean.getSTATUS();
						if (STATUS == 1) {
							ArrayList<TakedClientWelcomPicGroupBean> wpbs = (ArrayList<TakedClientWelcomPicGroupBean>) bean
									.getLIST();
							if (wpbs.size() > 1) {
								tv_wel.setVisibility(View.VISIBLE);
								findViewById(R.id.setting_wel_line)
										.setVisibility(View.VISIBLE);
							}
						} else {
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
