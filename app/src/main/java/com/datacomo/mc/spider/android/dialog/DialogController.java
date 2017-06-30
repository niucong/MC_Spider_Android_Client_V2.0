package com.datacomo.mc.spider.android.dialog;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BackupContactsActivity;
import com.datacomo.mc.spider.android.BaseActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ContactsUtil;

public class DialogController {
	private static final String TAG = "DialogController";

	private static DialogController instance = null;
	public static ProgressDialog processDia;

	private DialogController() {
	}

	public static DialogController getInstance() {
		if (null == instance) {
			instance = new DialogController();
			return instance;
		}
		return instance;
	}

	/**
	 * 提示备份通讯录对话框
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public void promptBackupDialog(final Context context,
			final String sessionKey, long lastBackup) {
		long weekTime = 1000 * 60 * 60 * 24 * 7;// 一周
		// long weekTime = 1000 * 60 * 3;// 五分钟测试用
		final UserBusinessDatabase business = new UserBusinessDatabase(App.app);
		// long lastBackup = business.getBackupOrRenewContactsTime(sessionKey,
		// true);
		long currentTime = System.currentTimeMillis();
		L.i(TAG, "promptBackupDialog lastBackup=" + lastBackup
				+ ",currentTime=" + currentTime);

		String msg = "";
		int weeks;
		if (lastBackup == 0) {
			long fristLogin = business.getLongData(sessionKey,
					"firstlogin_time");

			int newWeeks = (int) ((currentTime - fristLogin) / weekTime);
			weeks = business.getIntData(sessionKey, "firstlogin_weeks");
			L.i(TAG, "promptBackupDialog fristLogin=" + fristLogin
					+ ",newWeeks=" + newWeeks + ",weeks=" + weeks);
			if (fristLogin == 0) {
				business.saveLongData(sessionKey, "firstlogin_time",
						currentTime);
			} else if (newWeeks > weeks) {
				business.saveIntData(sessionKey, "firstlogin_weeks", newWeeks);
			} else {
				return;
			}
			// msg = "您还没有备份过本机通讯录";
			msg = "手机通讯录安全备份，不再担心数据丢失。";
		} else {
			weeks = business.getIntData(sessionKey, "nobackupcontacts_weeks");
			int newWeeks = (int) ((currentTime - lastBackup) / weekTime);
			L.i(TAG, "promptBackupDialog weeks=" + weeks + ",newWeeks="
					+ newWeeks);
			if (newWeeks > weeks) {
				String lastBackupStr = App.app.share.getStringMessage(
						"BackupContacts", "LastBackupContacts", "");
				L.i(TAG, "promptBackupDialog lastBackupStr=" + lastBackupStr);
				HashMap<String, String> oldMap = new HashMap<String, String>();
				if (lastBackupStr != null && !"".equals(lastBackupStr)) {
					String[] contacts = lastBackupStr.split("&");
					for (String contact : contacts) {
						oldMap.put(contact.substring(0, contact.indexOf("#")),
								contact.substring(contact.indexOf("#") + 1));
					}
				}
				HashMap<String, String> newMap = ContactsUtil
						.getRawIdVersion(context);
				if (ContactsUtil.isContactsChange(oldMap, newMap)) {
					// msg = "检测到您的联系人有变化，您已经 " + newWeeks + " 周没有备份通讯录了";
					msg = "您的通讯录有更新，请及时备份，以免丢失。";
					business.saveIntData(sessionKey, "nobackupcontacts_weeks",
							newWeeks);
				} else {
					return;
				}
			} else {
				return;
			}
		}

		new Builder(context)
				.setTitle("提示")
				.setMessage(msg)
				.setPositiveButton("备份", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(context,
								BackupContactsActivity.class);
						intent.putExtra("directBackup", true);
						context.startActivity(intent);
					}
				})
				.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setCancelable(false).show();
	}

	/**
	 * 编辑对话框
	 * 
	 * @param c
	 * @param dialogCallBack
	 * @param text
	 */
	public void showEditDlg(Context c, final DialogCallBack dialogCallBack,
			String text) {
		View content = LayoutInflater.from(c).inflate(R.layout.dlg_pletter,
				null);
		TextView tv = (TextView) content.findViewById(R.id.text);
		tv.setText(text);
		final EditText edit = (EditText) content.findViewById(R.id.edit);
		new Builder(c).setView(content)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (null != dialogCallBack) {
							dialogCallBack.onDlgCallBack(edit);
						}
					}
				}).setNegativeButton("取消", null).show();
	}

	public interface DialogCallBack {
		public void onDlgCallBack(Object... objects);
	}

	private boolean[] flags = new boolean[] { false, false };// 初始复选情况

	public Dialog warnDialog(final Context c) {
		Dialog dialog = null;
		String notificationSetupType = App.app.share.getStringMessage(
				"NotificationSetup", "type", "all");
		// 根据在配置文件中读出的数据来确定复选框的结果
		if ("all".equals(notificationSetupType)) {
			flags = new boolean[] { true, true };
		} else if ("ring".equals(notificationSetupType)) {
			flags = new boolean[] { true, false };
		} else if ("vibrate".equals(notificationSetupType)) {
			flags = new boolean[] { false, true };
		} else {
			flags = new boolean[] { false, false };
		}
		Builder builder = new Builder(c);
		builder.setTitle("消息提醒设置");
		builder.setMultiChoiceItems(new String[] { "铃声", "振动" }, flags,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						flags[which] = isChecked;
						L.i(TAG, "flags[0] : " + flags[0] + ", flags[[1] : "
								+ flags[1]);
					}
				});
		// 添加一个确定按钮
		builder.setPositiveButton("确   定",
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						L.i(TAG, "flags[0] : " + flags[0] + ", flags[[1] : "
								+ flags[1]);
						if (flags[0] == true && flags[1] == true) {
							App.app.share.saveStringMessage(
									"NotificationSetup", "type", "all");
						} else if (flags[0] == true && flags[1] == false) {
							App.app.share.saveStringMessage(
									"NotificationSetup", "type", "ring");
						} else if (flags[0] == false && flags[1] == true) {
							App.app.share.saveStringMessage(
									"NotificationSetup", "type", "vibrate");
						} else if (flags[0] == false && flags[1] == false) {
							App.app.share.saveStringMessage(
									"NotificationSetup", "type", "null");
						}

						// NotificationService.isChat = false;
						((BaseActivity) c).startNService(false);
					}
				});
		// 创建一个复选框对话框
		dialog = builder.create();
		return dialog;
	}

	public void showPromptDialog(Context context, String msg) {
		new Builder(context).setTitle("提示").setMessage(msg)
				.setPositiveButton("知道了", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}
