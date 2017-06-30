package com.datacomo.mc.spider.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.datacomo.mc.spider.android.adapter.SavedBookAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.BackupContactsInfo;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.util.T;

@SuppressLint("HandlerLeak")
public class BackupContactsReActivity extends BasicActionBarActivity {

	private ListView listView;
	private ArrayList<BackupContactsInfo> contacts;
	private SavedBookAdapter adapter;
	private LoadContactsTask task;

	@Override
	protected void onDestroy() {
		updateHandler.removeCallbacksAndMessages(null);
		renewHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_contacts_list);
		ab.setTitle("恢复通讯录");

		spDialog = new SpinnerProgressDialog(this);
		contacts = new ArrayList<BackupContactsInfo>();

		findView();

		loadInfo(true);
	}

	private void findView() {
		listView = (ListView) findViewById(R.id.listview);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		loadInfo(true);
		return super.onCreateOptionsMenu(menu);
	}

	void loadInfo(boolean isRefresh) {
		stopTask();
		task = new LoadContactsTask(isRefresh);
		task.execute();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadContactsTask extends
			AsyncTask<String, Integer, ArrayList<BackupContactsInfo>> {
		private boolean isRefresh;

		public LoadContactsTask(boolean isRefresh) {
			this.isRefresh = isRefresh;
			setLoadingStateVisible(true);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected ArrayList<BackupContactsInfo> doInBackground(String... params) {
			try {
				Object[] objects = APIRequestServers.getBackupTimes(
						BackupContactsReActivity.this, true, contacts.size());
				int resultCode = (Integer) objects[0];
				if (resultCode == 1) {
					if (isRefresh) {
						contacts = (ArrayList<BackupContactsInfo>) objects[1];
					} else {
						contacts.addAll((ArrayList<BackupContactsInfo>) objects[1]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return contacts;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(ArrayList<BackupContactsInfo> contacts) {
			super.onPostExecute(contacts);
			setLoadingStateVisible(false);
			if (null != contacts) {
				if (contacts.size() > 0) {
					if (adapter == null) {
						adapter = new SavedBookAdapter(
								BackupContactsReActivity.this, contacts,
								new SimpleDateFormat("yyyy-MM-dd"));
						listView.setAdapter(adapter);
						adapter.init(spDialog, updateHandler, renewHandler);
					} else {
						adapter.notifyDataSetChanged();
					}
				} else if (isRefresh) {
					T.show(App.app, "您还没有备份过通讯录哦");
					finish();
				}
			}
		}
	}

	/**
	 * 更新恢复进度
	 */
	private Handler updateHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0:
					int x = msg.arg1;
					int y = msg.arg2;
					if (x > 1 && !spDialog.getPd().isShowing()) {
						return;
					}
					spDialog.showProgressDialog("正在恢复通讯录（" + x + "/" + y
							+ "）...");
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
	 * 恢复通讯录后刷新UI
	 */
	private Handler renewHandler = new Handler() {
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			switch (msg.what) {
			case 0:

				break;
			case 1:
				Date date = new Date();
				new UserBusinessDatabase(BackupContactsReActivity.this)
						.saveBackupOrRenewContactsTime(
								App.app.share.getSessionKey(), false,
								date.getTime());
				break;
			default:
				break;
			}
			cancelProgressDialog(str);
		};
	};

	private static SpinnerProgressDialog spDialog;

	/**
	 * 关闭ProgressDialog并提示
	 * 
	 * @param str
	 */
	private void cancelProgressDialog(String str) {
		spDialog.cancelProgressDialog(str);
	}

}
