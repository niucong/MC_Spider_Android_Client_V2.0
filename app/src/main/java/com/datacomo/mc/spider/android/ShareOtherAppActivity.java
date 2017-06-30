package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ThirdInfoBean;
import com.datacomo.mc.spider.android.net.been.map.MapThirdInfoListBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.ShareBox;
import com.datacomo.mc.spider.android.view.ShareItem;

public class ShareOtherAppActivity extends BasicActionBarActivity {
	private static final String TAG = "ShareOtherAppActivity";

	private EditText edit;
	private CheckBox check;
	private ShareBox box;

	private String temp = "1";

	private Button warning;
	private OnClickListener getOathListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new LoadSharedItemsTask().execute();
			v.setVisibility(View.GONE);
		}
	};

	private OnClickListener setOathListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			v.setVisibility(View.GONE);
			finish();
		}
	};

	private String shareTopic, shareImage, groupId, objectId;
	private HashMap<String, Integer> shareAccounts;

	/** 是否评论 */
	private boolean isComment = false;
	private String strComment;

	private boolean isRunning = false;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_share);
		findView();
		initView();

		Intent intent = getIntent();
		shareTopic = intent.getStringExtra("shareTopic");
		shareImage = intent.getStringExtra("shareImage");
		groupId = intent.getStringExtra("groupId");
		objectId = intent.getStringExtra("objectId");
		temp = intent.getStringExtra("temp");

	}

	private void findView() {
		edit = (EditText) findViewById(R.id.edit);
		check = (CheckBox) findViewById(R.id.check);
		box = (ShareBox) findViewById(R.id.shareBox);
		warning = (Button) findViewById(R.id.warning);
	}

	private void initView() {
		// setTitle("一键分享", R.drawable.title_fanhui, R.drawable.title_send);
		ab.setTitle("一键分享");
		check.setText("同时发表评论");
		check.setChecked(true);
		shareAccounts = new HashMap<String, Integer>();
		spdDialog.showProgressDialog("正在加载中...");
		new LoadSharedItemsTask().execute();
	}

	private void addShareItems(List<ThirdInfoBean> oths) {
		if (null == box) {
			return;
		}
		ArrayList<ShareItem> items = new ArrayList<ShareItem>();
		for (int i = 0; i < oths.size(); i++) {
			ThirdInfoBean tb = oths.get(i);
			ShareItem item = null;
			int cut = tb.getMember_source();
			if (1 == cut) {
				item = new ShareItem(this, R.drawable.icon_sina_big, "分享到新浪微博",
						null);
				shareAccounts.put("分享到新浪微博", 1);
			} else if (2 == cut) {
				item = new ShareItem(this, R.drawable.icon_qq_big, "分享到腾讯微博",
						null);
				shareAccounts.put("分享到腾讯微博", 2);
			} else if (3 == cut) {
				item = new ShareItem(this, R.drawable.icon_renren_big,
						"分享到人人网", null);
				shareAccounts.put("分享到人人网", 3);
			} else if (4 == cut) {
				item = new ShareItem(this, R.drawable.icon_kaixin_big,
						"分享到开心网", null);
				shareAccounts.put("分享到开心网", 4);
			} else if (5 == cut) {
				item = new ShareItem(this, R.drawable.icon_sohu_big, "分享到搜狐微博",
						null);
				shareAccounts.put("分享到搜狐微博", 5);
			} else if (6 == cut) {
				item = new ShareItem(this, R.drawable.icon_wangyi_big,
						"分享到网易微博", null);
				shareAccounts.put("分享到网易微博", 6);
			} else if (7 == cut) {
				item = new ShareItem(this, R.drawable.icon_douban_big,
						"分享到豆瓣网", null);
				shareAccounts.put("分享到豆瓣网", 7);
			}
			items.add(item);
		}

		box.addItems(items);
	}

	class LoadSharedItemsTask extends AsyncTask<Void, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				mc = APIRequestServers
						.thirdInfoList(ShareOtherAppActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
				mc = null;
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (null == result || 1 != result.getResultCode()) {
				recheckOath();
				return;
			}

			MapThirdInfoListBean mapBean = (MapThirdInfoListBean) result
					.getResult();
			int num = mapBean.getNUM();
			List<ThirdInfoBean> oths = mapBean.getLIST();
			if (num == 0 || null == oths || 0 == oths.size()) {
				setOath();
				return;
			}

			if (num == oths.size()) {
				addShareItems(oths);
			} else {
				recheckOath();
			}
		}

	}

	private void recheckOath() {
		warning.setText(T.ErrStr);
		warning.setOnClickListener(getOathListener);
		warning.setVisibility(View.VISIBLE);
		// right.setClickable(false);
		menu.findItem(R.id.action_send).setCheckable(false);
	}

	private void setOath() {
		warning.setText("您尚未绑定第三方账号，请登录web页面至“账户”——“账户设置”——“账户绑定”进行绑定。");
		warning.setOnClickListener(setOathListener);
		warning.setVisibility(View.VISIBLE);
		// right.setClickable(false);
		menu.findItem(R.id.action_send).setCheckable(true);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_send).setVisible(true);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send:
			shareAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// shareAll();
	// }

	private void shareAll() {
		box.performShare();
		// 评论
		if (check.isChecked()) {
			strComment = edit.getText().toString();
			if (strComment != null && !"".equals(strComment)) {
				isComment = true;
			} else {
				isComment = false;
			}
		} else {
			isComment = false;
		}

		ArrayList<Integer> cuts = new ArrayList<Integer>();
		if (!shareAccounts.isEmpty()) {
			for (Iterator<?> iterator = shareAccounts.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				if (App.app.share.getBooleanMessage(ShareItem.ShareInfoName,
						key, true)) {
					cuts.add(shareAccounts.get(key));
				}
			}
		}

		int i = cuts.size();
		if (i == 0) {
			T.show(ShareOtherAppActivity.this, "请先选择分享平台");
			return;
		}

		String[] thirdIds = new String[i];
		for (int j = 0; j < i; j++) {
			thirdIds[j] = cuts.get(j) + "";
		}

		if (!isRunning) {
			spdDialog.showProgressDialog("正在处理中...");
			// 一键分享
			new SharedThirdsTask().execute(thirdIds);
		}
	}

	class SharedThirdsTask extends AsyncTask<String[], Integer, MCResult> {

		@Override
		protected MCResult doInBackground(String[]... params) {
			isRunning = true;
			MCResult mc = null;
			try {
				mc = APIRequestServers.shareThirdNew(
						ShareOtherAppActivity.this, shareTopic, shareImage,
						params[0], temp, groupId, objectId);
			} catch (Exception e) {
				e.printStackTrace();
				mc = null;
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			isRunning = false;
			spdDialog.cancelProgressDialog(null);
			if (null == result || 1 != result.getResultCode()) {
				showTip(T.ErrStr);
				return;
			}
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> map = (HashMap<String, Integer>) result
					.getResult();
			String tip = "";
			if (map != null && !map.isEmpty()) {
				for (Iterator<?> iterator = map.keySet().iterator(); iterator
						.hasNext();) {
					String key = (String) iterator.next();
					if (map.get(key) == 0) {
						tip += key + "、";
					}
				}
			}
			L.i(TAG, "SharedThirdsTask tip=" + tip);
			if ("".equals(tip)) {
				T.show(ShareOtherAppActivity.this, "已分享！");
				ShareOtherAppActivity.this.finish();
			} else {
				tip = tip.substring(0, tip.length() - 1);
				try {
					new AlertDialog.Builder(ShareOtherAppActivity.this)
							.setTitle("提示")
							.setMessage("因网络原因或授权过期，未能转发到" + tip)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ShareOtherAppActivity.this.finish();
										}
									}).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (isComment) {
				try {
					CircleBlogDetailsActivity.circleBlogDetailsActivity
							.releaseComment(strComment);
					CircleBlogDetailsActivity.circleBlogDetailsActivity.isSelection = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
