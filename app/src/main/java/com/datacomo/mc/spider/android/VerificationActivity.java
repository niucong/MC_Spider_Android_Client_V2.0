package com.datacomo.mc.spider.android;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.UserBean;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.LoginJustByPhoneBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.service.UpdateGroupListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.datacomo.mc.spider.android.util.T;

public class VerificationActivity extends BasicActionBarActivity {
	protected static final String TAG = "VerificationActivity";
	private Button button;
	private TextView prompt_tv;
	private TextView timer_tv;
	private TextView again_tv;
	private EditText verifyCode;
	private String phone;

	private UserBusinessDatabase business;
	private String session_key, way, time;

	private Timer mTimer;
	private int mCountdown;
	private boolean mIsInit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.verification_code);
		business = new UserBusinessDatabase(App.app);

		init();
		// setTitle("填验证码", R.drawable.title_fanhui, null);
		ab.setTitle("填验证码");
	}

	private void init() {
		verifyCode = (EditText) findViewById(R.id.verifyCode);
		button = (Button) findViewById(R.id.next_button);
		timer_tv = (TextView) findViewById(R.id.textView_timer);
		prompt_tv = (TextView) findViewById(R.id.prompt_tv);
		again_tv = (TextView) findViewById(R.id.again_tv);
		again_tv.setEnabled(false);
		again_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				L.d(TAG, phone);
				mIsInit = true;
				again_tv.setEnabled(false);
				spdDialog.showProgressDialog("正在获取验证码...");
				new loadInfoTask(VerificationActivity.this, phone).execute();
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String verifyCodeInfo = verifyCode.getText().toString();
				L.d(TAG, verifyCodeInfo);
				if (verifyCodeInfo != null && !"".equals(verifyCodeInfo)) {
					spdDialog.showProgressDialog("正在检测验证码...");
					new loginLoadInfoTask(VerificationActivity.this, phone,
							verifyCodeInfo).execute();
				} else {
					T.show(VerificationActivity.this, "请输入短信验证码");
				}
			}
		});
		getMasBundle();
		prompt_tv.setText("我们已向您的手机号 " + phone
				+ " 发送了一条短信验证码，请把短信中的数字填写到下面的输入框中。");

		mIsInit = true;
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				if (mIsInit) {
					mIsInit = false;
					mCountdown = 60;
				}
				Message message = Message.obtain();
				if (mCountdown >= 0) {
					message.what = 0;
					message.arg1 = mCountdown;
					reGetHandler.sendMessage(message);
					mCountdown--;
				}
			}
		};
		mTimer = new Timer();
		mTimer.schedule(timerTask, 1000, 1000);
	}

	@Override
	protected void onDestroy() {
		reGetHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		spdDialog.cancelProgressDialog(null);
		mTimer.cancel();
		BaseData.hideKeyBoard(this);
		super.onDestroy();
	}

	/**
	 * UI
	 */
	@SuppressLint("HandlerLeak")
	private Handler reGetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.arg1 == 60)
					timer_tv.setText("未收到短信？ " + 1 + " 分钟后可");
				else
					timer_tv.setText("未收到短信？ " + msg.arg1 + " 秒钟后可");
				if (msg.arg1 == 0) {
					again_tv.setEnabled(true);
				}
				break;
			default:
				break;
			}
		}
	};

	private void getMasBundle() {
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			phone = bundle.getString("phone");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;
		private String phone;

		public loadInfoTask(Context context, String phone) {
			this.context = context;
			this.phone = phone;
		}

		@Override
		protected MCResult doInBackground(String... arg0) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.createVerifyCodeByPhone(
						VerificationActivity.this, phone);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result != null && result.getResultCode() == 1) {
				int success = (Integer) result.getResult();
				if (success == 1) {
					T.show(context, "验证码已发送，请注意查收！");
				} else if (success == 2) {
					T.show(context, "手机号码不合法");
				} else if (success == 3) {
					T.show(context, "验证码已下发，特定时间内只能下发一次");
				} else {

				}
			} else {
				T.show(context, T.ErrStr);
			}
		}
	}

	class loginLoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private Context context;
		private String phone;
		private String verifyCodeInfo;

		public loginLoadInfoTask(Context context, String phone,
				String verifyCodeInfo) {
			this.context = context;
			this.phone = phone;
			this.verifyCodeInfo = verifyCodeInfo;
		}

		@Override
		protected MCResult doInBackground(String... arg0) {
			MCResult mcResult = null;
			try {
				mcResult = APIRequestServers.loginJustByPhone(context, phone,
						verifyCodeInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			spdDialog.cancelProgressDialog(null);
			if (result == null) {
				T.show(context, T.ErrStr);
			} else {
				int success = result.getResultCode();
				if (success == 1) {
					LoginJustByPhoneBean bean = (LoginJustByPhoneBean) result
							.getResult();
					session_key = bean.getSession_key();
					App.app.share.saveSessionKey(session_key);
					try {
						MCResult mc = APIRequestServers
								.singleInfo(VerificationActivity.this);
						JSONObject jsonObject = new JSONObject(mc.getResult()
								.toString());

						String deviceToken = jsonObject.getString("sessionId");
						if (deviceToken != null
								&& !deviceToken.equals("")
								&& !deviceToken.equals(new SoftPhoneInfo(
										VerificationActivity.this)
										.getPhoneMark())) {
							way = SendWay.loginWay(jsonObject
									.getString("loginVisitWay"));
							time = DateTimeUtil.aTimeFormat(jsonObject
									.getLong("creatTime"));
						}

						ConstantUtil.isCreateInfo = true;
						loginSuccess(bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * 登录成功
	 */
	private void loginSuccess(LoginJustByPhoneBean bean) {
		if (bean == null) {
			return;
		}

		if (session_key != null && !session_key.equals(bean.getSession_key())) {
			SettingActivity.cleanAccountInfo(this, business);
		}
		session_key = bean.getSession_key();

		L.i(TAG, "clickButton session_key : " + session_key);
		if (session_key != null && !"".equals(session_key)) {
			business.delete(session_key);
			UserBean userBean = new UserBean(phone, "", session_key, "yes");
			int memberId = bean.getMEMBER_ID();
			userBean.setMemberId(memberId + "");
			String name = bean.getMEMBER_NAME();
			userBean.setName(name);
			userBean.setHeadUrlPath(bean.getMEMBER_HEAD_URL()
					+ bean.getMEMBER_HEAD_PATH());
			business.insert(userBean);
			business.updateUserMark(session_key);
			enterMainActivity();
		}
	}

	/**
	 * 进入主界面
	 */
	private void enterMainActivity() {
		App.app.share.saveSessionKey(session_key);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(5 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					UpdateGroupListThread.updateGroupList(App.app, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					UpdateFriendListThread.updateFriendList(App.app, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		closeLogin();
	}

	private void closeLogin() {
		Intent intent = new Intent(VerificationActivity.this,
				InfoWallActivity.class);
		intent.putExtra("FindPassword", true);
		intent.putExtra("phone", phone);

		intent.putExtra("way", way);
		intent.putExtra("time", time);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, RetrievePassWordActivity.class);
			intent.putExtra("phone", phone);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// LogicUtil.enter(this, RetrievePassWordActivity.class, null, true);
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	//
	// }

}
