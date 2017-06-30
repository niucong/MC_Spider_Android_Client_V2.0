package com.datacomo.mc.spider.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.CheckNameUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class GuideNameActivity extends BasicActionBarActivity {
	private static final String TAG = "GuideNameActivity";

	private TextView next_tv, currency_tv;
	private EditText name_et;

	private String mail = null;

	private int num = 16;
	private final String currency_str = "*填写真实姓名，便于朋友认出您，首次修改您还将获赠%d个圈币。";

	private boolean isRename = true;

	public static GuideNameActivity guideNameActivity;

	private UserBusinessDatabase userDatabase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.guide_name);
		L.d(TAG, "onCreate...");
		guideNameActivity = this;
		mail = getIntent().getStringExtra("Email");

		findView();
		setView();
	}

	private void findView() {
		name_et = (EditText) findViewById(R.id.guide_name_et);
		next_tv = (TextView) findViewById(R.id.guide_name_next);

		currency_tv = (TextView) findViewById(R.id.guide_name_currency);
	}

	private void setView() {
		// setTitle("更改名字", null, R.drawable.title_send);
		ab.setTitle("更改名字");

		userDatabase = new UserBusinessDatabase(App.app);

		int nums = App.app.share.getIntMessage("program", "MODIFY_MEMBER_NAME",
				0);
		if (nums != 0) {
			num = nums;
		}
		currency_tv.setText(String.format(currency_str, num));

		if (CharUtil.isValidEmail(mail)) {
			String name = (String) mail.substring(0, mail.indexOf("@"));
			name_et.setText(name);
			name_et.setSelection(name.length());
			userDatabase.updateName(App.app.share.getSessionKey(), name);
			if (!CheckNameUtil.checkMemberName(name)) {
				next_tv.setVisibility(View.GONE);
			} else {
				next_tv.setVisibility(View.VISIBLE);
			}
		}

		next_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.guide_name_next:
			enterNextActivity(GuideHeadPhotoActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 修改名字
	 */
	private void setName() {
		final String name_str = name_et.getText().toString();
		L.i(TAG, "setName name_str=" + name_str);
		if (!CheckNameUtil.checkMemberName(name_str)) {
			T.show(this, "请输入规范的名字！");
		} else {
			spdDialog.showProgressDialog("正在修改中...");
			new Thread() {
				public void run() {
					MCResult response = null;
					try {
						response = APIRequestServers.resetName(
								GuideNameActivity.this, name_str);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (response != null) {
						int resultCode = response.getResultCode();
						if (resultCode == 1) {
							mHandler.sendEmptyMessage(1);
							try {
								sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							userDatabase.updateName(
									App.app.share.getSessionKey(), name_str);
							enterNextActivity(GuideHeadPhotoActivity.class);
						} else if (resultCode == 10) {
							mHandler.sendEmptyMessage(10);
						}
					} else {
						mHandler.sendEmptyMessage(0);
					}
				}
			}.start();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				if (isRename) {
					isRename = false;
					updateUI("名字更改成功，您已获得" + num + "个圈币。");
				} else {
					updateUI("名字更改成功！");
				}
				break;
			case 10:
				updateUI("名字长度或格式不对");
				break;
			default:
				break;
			}
		};
	};

	private void updateUI(String msg) {
		spdDialog.cancelProgressDialog(msg);
	}

	/**
	 * 页面跳转
	 */
	private void enterNextActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		this.startActivity(intent);
	}

	/**
	 * 捕捉键盘事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConfirmExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 退出确认 */
	public void ConfirmExit() {
		new AlertDialog.Builder(this).setTitle("退出").setMessage("是否退出软件?")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (GuideHeadPhotoActivity.guideHeadPhotoActivity != null) {
							LogicUtil
									.finish((Context) GuideHeadPhotoActivity.guideHeadPhotoActivity);
						}
						if (GuideInviteActivity.guideInviteActivity != null) {
							LogicUtil
									.finish((Context) GuideInviteActivity.guideInviteActivity);
						}
						finish();
					}
				}).setNegativeButton("否", null).show();// 显示对话框
	}

	@Override
	protected void onDestroy() {
		L.d(TAG, "onCreate...");
		mHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		spdDialog.cancelProgressDialog(null);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		MenuItem i = menu.findItem(R.id.action_more);
		i.setVisible(true);
		i.setIcon(R.drawable.title_send);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			setName();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// setName();
	// }
}
