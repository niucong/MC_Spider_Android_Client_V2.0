package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.adapter.WelGroupCheckAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.TakedClientWelcomPicGroupBean;
import com.datacomo.mc.spider.android.net.been.map.MapTakedClientWelcomPicGroupBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.JsonParseTool;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class WelSettingActivity extends BasicActionBarActivity {
	private final String TAG = "WelSettingActivity";

	private String msg = "您所在的%d个企业顶级认证圈子都为App设置了专属于企业圈子的欢迎页，您可以从中选择默认为您的欢迎页，设置成功后，重新登录即刻查看。";

	private TextView tv_msg;
	private ListView listView;

	private WelGroupCheckAdapter groupAdapter;
	private ArrayList<TakedClientWelcomPicGroupBean> groups;

	private MapTakedClientWelcomPicGroupBean bean;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.wel_setting);
		ab.setTitle("欢迎页设置");

		bean = (MapTakedClientWelcomPicGroupBean) getIntent()
				.getSerializableExtra("MapTakedClientWelcomPicGroupBean");

		findView();
		setView();

		if (bean != null)
			setInfo(false);
	}

	private void findView() {
		tv_msg = (TextView) findViewById(R.id.wel_setting_tv);
		listView = (ListView) findViewById(R.id.wel_setting_lv);
	}

	private void setView() {
		groups = new ArrayList<TakedClientWelcomPicGroupBean>();
		groupAdapter = new WelGroupCheckAdapter(this, groups, listView);
		listView.setAdapter(groupAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		MenuItem mi = menu.findItem(R.id.action_refresh);
		mi.setVisible(true);
		mi.setIcon(R.drawable.nothing);
		menu.findItem(R.id.action_send).setVisible(true);
		menu.findItem(R.id.action_send).setIcon(R.drawable.action_select_over);
		this.menu = menu;
		if (bean == null)
			new GroupListTask().execute();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send:
			int gId = groupAdapter.getGroupBean().getGroupId();
			if (gId != 0) {
				new SetWelTask(gId).execute();
			} else {
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class SetWelTask extends AsyncTask<String, Integer, MCResult> {

		private int gId;

		public SetWelTask(int gId) {
			this.gId = gId;
			spdDialog.showProgressDialog("设置中...");
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = APIRequestServers.clientWelcomePic(App.app,
						ClientWelcomePicEnum.UPDATEUSEDCLIENTWELCOMPIC, gId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog("");
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				showTip("设置失败");
			} else if (null != mcResult && 1 == mcResult.getResultCode()) {
				try {
					JSONObject object = new JSONObject(mcResult.getResult()
							.toString());
					int STATUS = object.getInt("STATUS");
					if (STATUS == 1) {
						showTip("设置成功");
						String welurl = object.getString("WELCOM_PIC");
						App.app.share.saveStringMessage("program", "welurl",
								welurl);
						App.app.share.saveStringMessage("program", "welname",
								groupAdapter.getGroupBean().getGroupName());
						// TODO addShortcut();
						WelSettingActivity.this.finish();
					} else {
						showTip("设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					showTip("设置失败");
				}
			}
		}
	}

	/**
	 * 创建快捷方式
	 */
	private void addShortcut() {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		String head = groupAdapter.getGroupBean().getGroupPosterUrl()
				+ groupAdapter.getGroupBean().getGroupPosterPath();
		head = ThumbnailImageUrl.getThumbnailPostUrl(head,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		Bitmap bmp = MyFinalBitmap.getPosterBitmap(head);
		L.d(TAG, "addShortcut head=" + head + ", " + (bmp == null));
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, groupAdapter
				.getGroupBean().getGroupName());
		// ShortcutIconResource iconRes =
		// Intent.ShortcutIconResource.fromContext(
		// this.getApplicationContext(), R.drawable.btn_face_down);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, bmp);
		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);
		// 设置桌面快捷方式的图标
		// Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
		// R.drawable.btn_face_down);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, bmp);
		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, SpiderTouchApp.class);
		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 广播通知桌面去创建
		this.sendBroadcast(shortcut);
	}

	class GroupListTask extends AsyncTask<String, Integer, MCResult> {

		public GroupListTask() {
			setLoadingState(true);
		}

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
			setLoadingState(false);
			if (null != mcResult) {
				if (1 != mcResult.getResultCode()) {
					showTip(T.ErrStr);
				} else if (null != mcResult && 1 == mcResult.getResultCode()) {
					try {
						MapTakedClientWelcomPicGroupBean bean = (MapTakedClientWelcomPicGroupBean) JsonParseTool
								.dealComplexResult(mcResult.getResult()
										.toString(),
										MapTakedClientWelcomPicGroupBean.class);
						setInfo(true);
					} catch (Exception e) {
						e.printStackTrace();
						showTip("获取失败");
					}
				}
			}
		}
	}

	private void setInfo(boolean showTip) {
		int STATUS = bean.getSTATUS();
		if (STATUS == 1) {
			ArrayList<TakedClientWelcomPicGroupBean> wpbs = (ArrayList<TakedClientWelcomPicGroupBean>) bean
					.getLIST();
			tv_msg.setText(String.format(msg, wpbs.size()));
			groups.addAll(wpbs);
			groupAdapter.notifyDataSetChanged();
		} else {
			if (showTip)
				showTip("获取失败");
		}
	}
}