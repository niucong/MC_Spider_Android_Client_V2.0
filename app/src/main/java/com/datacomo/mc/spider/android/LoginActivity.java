package com.datacomo.mc.spider.android;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.bean.UserBean;
import com.datacomo.mc.spider.android.db.FriendListService;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.LoginBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.service.NotificationService;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.service.UpdateGroupListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.weibo.TenCentOAuthActivity;
import com.datacomo.mc.spider.android.weibo.WeiBoUtil;
import com.tencent.weibo.utils.Configuration;
import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener,
		OnFocusChangeListener {
	private static final String TAG = "LoginActivity";

	private EditText accound_et, password_et;
	private ImageView accound_iv, password_iv;//
	private Button login_btn;
	private TextView find_tv, title_tv, register_tv;
	private ImageView qq_iv, sina_iv;

	private MCResult loginResponse;

	private UserBusinessDatabase business;
	private UserBean userBean;

	private SoftPhoneInfo softPhoneInfo;

	private String userName;
	private String password;
	private String session_key;

	// private boolean addUser;
	// private boolean isAbleAccound, isAblePsw;
	public static boolean enterCreateGroupTopic;

	private String way, time;

	public static LoginActivity loginActivity;

	private boolean showPsd = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginActivity = this;

		enterCreateGroupTopic = getIntent().getBooleanExtra("CreateGroupTopic",
				false);
		softPhoneInfo = new SoftPhoneInfo(App.app);

		findView();
		setView();

		startLogin();
		showPsd = App.app.share.getBooleanMessage("program", "showPsd", false);
		showPsw();
	}

	/**
	 * 判断是直接登录还是输入帐号密码
	 */
	private void startLogin() {
		try {
			business = new UserBusinessDatabase(App.app);
			List<UserBean> users = business.selectUserByMark("yes");
			boolean isAuto_login = App.app.share.getBooleanMessage("program",
					"auto_login", true);
			L.d(TAG, "startLogin isAuto_login=" + isAuto_login);
			Intent intent = getIntent();
			// addUser = intent.getBooleanExtra("addUser", false);
			// L.d(TAG, "startLogin addUser=" + addUser);
			if (users == null || users.size() == 0) {
				accound_et.requestFocus();
				stopServiceNotification();
				return;
			} else {
				for (UserBean bean : users) {
					if ("yes".equals(bean.getMark())) {
						userBean = bean;
					}
				}
				String accountType = userBean.getAccountType();
				if (accountType == null || "".equals(accountType)
						|| "0".equals(accountType)) {
					userName = userBean.getUsername();
					password = userBean.getPassword();
					if (userName != null && !"".equals(userName)) {
						accound_et.setText(userName);
						accound_iv.setVisibility(View.VISIBLE);
						if (password != null && !"".equals(password)) {
							password_et.setText(password);
							password_et.setSelection(password.length());
						} else {
							accound_et.setSelection(userName.length());
						}
						password_et.requestFocus();
					} else {
						accound_et.requestFocus();
					}
					// checkAccountValid();
					// checkPasswordValid();
				}
				session_key = userBean.getSession_key();
				userBean.setMemberId(business.getMemberId(session_key));
				L.i(TAG, "startLogin userName : " + userName + ", password : "
						+ password + ",session_key=" + session_key);
				if (isAuto_login) {
					if ((userName != null && !"".equals(userName)
							&& password != null && !"".equals(password))) {
						String sessionKey = App.app.share.getSessionKey();
						if (sessionKey != null
								&& sessionKey.equals(session_key)) {
							enterMainActivity();
						} else {
							stopServiceNotification();
							clickButton();
						}
					} else if (WeiBoUtil.SINA.equals(accountType)
							|| WeiBoUtil.QQ.equals(accountType)) {
						String sessionKey = App.app.share.getSessionKey();
						if (sessionKey != null
								&& sessionKey.equals(session_key)) {
							enterMainActivity();
						}
					}
				} else {
					stopServiceNotification();
					boolean otherLogin = intent.getBooleanExtra("otherLogin",
							false);
					if (otherLogin) {
						String loginWay = SendWay.loginWay(App.app.share
								.getStringMessage("isOtherLogin", "loginWay",
										""));
						password_et.setText("");
						business.updateUserPassword(session_key, "");
						password_et.requestFocus();
						new AlertDialog.Builder(this)
								.setTitle("提示")
								.setMessage(
										"您的帐号在另一处登录（"
												+ loginWay
												+ "），为确保帐号安全，您已下线。若不是本人操作，请登录修改密码。")
								.setPositiveButton("重新登录",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												password_et.requestFocus();
											}
										}).setCancelable(true).show();
					}
				}
			}
			App.app.share.saveBooleanMessage("isOtherLogin", "isOtherLogin",
					false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPsw() {
		if (!showPsd) {
			password_iv.setImageResource(R.drawable.eye_close);
			password_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		} else {
			password_iv.setImageResource(R.drawable.eye_open);
			password_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}

	private void stopServiceNotification() {
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
				.cancelAll();
		stopService(new Intent(this, NotificationService.class));
	}

	private void findView() {
		findViewById(R.id.activity_header_right).setVisibility(View.GONE);
		login_btn = (Button) findViewById(R.id.login_btn);
		title_tv = (TextView) findViewById(R.id.activity_header_title);

		accound_et = (EditText) findViewById(R.id.login_account);
		password_et = (EditText) findViewById(R.id.login_password);
		accound_iv = (ImageView) findViewById(R.id.login_account_delete);
		password_iv = (ImageView) findViewById(R.id.login_password_show);

		find_tv = (TextView) findViewById(R.id.login_find);

		qq_iv = (ImageView) findViewById(R.id.t_qq);
		sina_iv = (ImageView) findViewById(R.id.t_sina);

		register_tv = (TextView) findViewById(R.id.login_register);
	}

	private void setView() {
		findViewById(R.id.activity_header_left).setVisibility(View.GONE);

		title_tv.setText(App.app.share.getStringMessage("program", "welname",
				"优优工作圈"));
		// 为login按钮添加单击事件
		login_btn.setOnClickListener(this);

		// accound_et.addTextChangedListener(watcher);
		// password_et.addTextChangedListener(watcher);

		accound_et.setOnFocusChangeListener(this);
		password_et.setOnFocusChangeListener(this);

		// 为find_pwd添加单击事件
		find_tv.setOnClickListener(this);
		register_tv.setOnClickListener(this);

		qq_iv.setOnClickListener(this);
		sina_iv.setOnClickListener(this);

		// showPsd = sharedMessage.getBooleanMessage("program", "showPsd",
		// true);
		// showPsw();
		password_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPsd = !showPsd;
				showPsw();
				password_et.setSelection(password_et.getText().length());
				App.app.share.saveBooleanMessage("program", "showPsd", showPsd);
				// password_et.setText("");
				// BaseData.showKeyBoard(LoginActivity.this, password_et);
			}
		});

		accound_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				accound_et.setText("");
				BaseData.showKeyBoard(LoginActivity.this, accound_et);
				// accound_iv.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.login_account:
			if (hasFocus) {
				accound_iv.setVisibility(View.VISIBLE);
				// password_iv.setVisibility(View.GONE);
			} else {
				accound_iv.setVisibility(View.GONE);
			}
			break;
		case R.id.login_password:
			if (hasFocus) {
				// password_iv.setVisibility(View.VISIBLE);
				accound_iv.setVisibility(View.GONE);
			} else {
				// password_iv.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onClick(View v) {
		BaseData.hideKeyBoard(LoginActivity.this);
		switch (v.getId()) {
		case R.id.login_btn:
			clickButton(); // 登录方法
			break;
		case R.id.login_find:// 找回密码
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setTitle("重置密码")
					// .setItems(new String[] { "通过手机号重置密码", "通过邮箱重置密码" },
					// new DialogInterface.OnClickListener() {
					.setAdapter(
							new ArrayAdapter(this, R.layout.choice_item,
									new String[] { "通过手机号重置密码", "通过邮箱重置密码" }),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									userName = accound_et.getText().toString();
									Intent intent = new Intent(
											LoginActivity.this,
											RetrievePassWordActivity.class);
									switch (which) {
									case 0:// 通过手机号重置密码
										intent.putExtra("IsEmail", false);
										if (CharUtil.isValidPhone(userName))
											intent.putExtra("phone", userName);
										break;
									case 1:// 通过邮箱重置密码
										intent.putExtra("IsEmail", true);
										if (CharUtil.isValidEmail(userName))
											intent.putExtra("phone", userName);
										break;
									}
									startActivity(intent);
									LoginActivity.this.finish();
								}
							});// .setNegativeButton("取消", null).show();
			AlertDialog ad = builder.create();
			ad.setCanceledOnTouchOutside(true);
			ad.show();
			break;
		case R.id.login_register:
			Intent request = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(request);
			this.finish();
			break;
		case R.id.t_qq:// 腾讯微博帐号登录
			Configuration.wifiIp = softPhoneInfo.getIPAddress();
			startActivity(new Intent(LoginActivity.this,
					TenCentOAuthActivity.class));
			this.finish();
			break;
		case R.id.t_sina:// 新浪微博帐号登录
			Weibo weibo = Weibo.getInstance();
			weibo.setupConsumerConfig(WeiBoUtil.sina_AppKey,
					WeiBoUtil.sina_AppSecret);
			// Oauth2.0
			// 隐式授权认证方式
			weibo.setRedirectUrl("http://yuuquu.com");
			// 此处回调页内容应该替换为与appkey对应的应用回调页
			// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
			// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
			// 应用回调页不可为空
			weibo.authorize(this, new AuthDialogListener());
			break;
		default:
			break;
		}
	}

	/**
	 * 单击button事件
	 * 
	 * @param name
	 * @param password
	 */
	private void clickButton() {
		userName = accound_et.getText().toString();
		password = password_et.getText().toString();
		L.i(TAG, "clickButton name : " + userName + ", password : " + password);

		if (!CharUtil.isValidPhone(userName)
				&& !CharUtil.isValidEmail(userName)) {
			T.show(getApplicationContext(), "请输入合法的手机号或邮箱");
			return;
		} else if (!CharUtil.isValidPassword(password)) {
			T.show(getApplicationContext(), "您输入的密码有误，请重新输入");
			return;
		}

		if (userName == null || "".equals(userName)) {
			T.show(getApplicationContext(), "请输入手机号或邮箱");
		} else if (userName != null && !"".equals(userName) && password != null
				&& !"".equals(password)) {
			spdDialog.showProgressDialog("正在登录...");

			new Thread() {
				public void run() {
					// 调用登录方法，返回登录的状态和添加登录记录
					try {
						// loginResponse = APIRequestServers.login(
						// LoginActivity.this, userName, password);
						loginResponse = APIRequestServers.memberLogin(App.app,
								userName, password);
						if (loginResponse != null) {
							int resultCode = loginResponse.getResultCode();
							if (resultCode == 1) {
								App.app.share.saveBooleanMessage("program",
										"auto_login", true);
								LoginBean bean = (LoginBean) loginResponse
										.getResult();
								String sessionkey = bean.getSession_key();
								App.app.share.saveSessionKey(sessionkey);
								L.d(TAG, "clickButton sessionkey=" + sessionkey
										+ ",session_key=" + session_key);
								if (session_key != null
										&& !session_key.equals(sessionkey)) {
									SettingActivity.cleanAccountInfo(App.app,
											business);
								}
								try {
									MCResult mc = APIRequestServers
											.singleInfo(App.app);
									JSONObject jsonObject = new JSONObject(mc
											.getResult().toString());

									String loginWay = jsonObject
											.getString("loginVisitWay");
									L.i(TAG, "clickButton loginWay : "
											+ loginWay);
									if (loginWay != null
											&& !"".equals(loginWay)) {
										String deviceToken = null;
										try {
											deviceToken = jsonObject
													.getString("deviceToken");
										} catch (Exception e) {
											e.printStackTrace();
										}
										L.i(TAG, "clickButton deviceToken : "
												+ deviceToken);
										if ("web".equals(loginWay)
												|| (deviceToken != null
														&& !deviceToken
																.equals("") && !deviceToken
															.equals(softPhoneInfo
																	.getPhoneMark()))) {
											way = SendWay.loginWay(loginWay);
											time = DateTimeUtil
													.aTimeFormat(jsonObject
															.getLong("creatTime"));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								ConstantUtil.isCreateInfo = true;
								handler.sendEmptyMessage(1);
							} else if (resultCode == 0) {
								String resultStr = loginResponse.getResult()
										.toString();
								if ("2".equals(resultStr)) {
									handler.sendEmptyMessage(2);
								} else if ("3".equals(resultStr)) {
									handler.sendEmptyMessage(3);
								} else if ("4".equals(resultStr)) {
									handler.sendEmptyMessage(4);
								} else if ("5".equals(resultStr)) {
									handler.sendEmptyMessage(5);
								} else {
									handler.sendEmptyMessage(0);
								}
							} else {
								handler.sendEmptyMessage(0);
							}
						} else {
							handler.sendEmptyMessage(0);
						}
					} catch (SocketTimeoutException e) {
						handler.sendEmptyMessage(10);
						e.printStackTrace();
					} catch (Exception e) {
						handler.sendEmptyMessage(0);
						e.printStackTrace();
					}
				};
			}.start();
		} else {
			T.show(getApplicationContext(), "请输入您的登录密码");
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 注：登录状态：1=登录成功；2=登录失败(登录名非手机号或邮箱/找不到该手机或邮箱对应的社员)；
			// 3=登录失败(用户名正确，密码错误)；4=登录失败(用户名密码都正确，但该用户为封杀状态)；
			// 5=登录失败（用户名密码都正确，但该用户为未激活状态）}
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				loginSuccess();
				break;
			case 2:
				updateUI("该帐号还未注册,请先注册");
				// isAbleAccound = false;
				// accound_iv.setBackgroundResource(R.drawable.unchecked);
				accound_et.requestFocus();
				break;
			case 3:
				updateUI("您输入的密码有误，请重新输入");
				// isAblePsw = false;
				// password_iv.setBackgroundResource(R.drawable.unchecked);
				BaseData.showKeyBoard(LoginActivity.this, password_et);
				break;
			case 4:
				updateUI("该帐号已被封");
				accound_et.requestFocus();
				break;
			case 5:
				updateUI("该帐号未激活");
				accound_et.requestFocus();
				break;
			case 10:
				updateUI(T.TimeOutStr);
				break;
			case 100:
				updateUI("");
				Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
						+ msg.obj));
				it.putExtra("sms_body", "密码");
				LoginActivity.this.startActivity(it);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 提示
	 * 
	 * @param msg
	 */
	private void updateUI(String msg) {
		spdDialog.cancelProgressDialog(msg);
	}

	/**
	 * 登录成功
	 */
	private void loginSuccess() {
		// session_key = loginResponse.getResult().toString();
		LoginBean bean = (LoginBean) loginResponse.getResult();
		if (bean == null)
			return;

		String sessionkey = bean.getSession_key();
		if (sessionkey == null)
			return;

		L.i(TAG, "clickButton session_key : " + session_key);
		if (!sessionkey.equals(session_key)) {
			if (session_key != null && !"".equals(session_key))
				business.delete(session_key);
			session_key = sessionkey;
			userBean = new UserBean(userName, password, session_key, "yes");
			int memberId = bean.getMEMBER_ID();
			userBean.setMemberId(memberId + "");
			String name = bean.getMEMBER_NAME();
			userBean.setName(name);
			userBean.setHeadUrlPath(bean.getMEMBER_HEAD_URL()
					+ bean.getMEMBER_HEAD_PATH());
			business.insert(userBean);
		} else {
			business.updateUserPassword(session_key, password);
		}
		enterMainActivity();
	}

	/**
	 * 进入主界面
	 */
	private void enterMainActivity() {
		App.app.share.saveSessionKey(session_key);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(10 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					UpdateGroupListThread.updateGroupList(App.app, null);

					Object[] objects = APIRequestServers.commGroupList(App.app,
							"", "0", "10", false);
					if ((Integer) objects[0] == 1) {
						@SuppressWarnings("unchecked")
						ArrayList<GroupEntity> lists = (ArrayList<GroupEntity>) objects[1];
						int size = lists.size();
						String[] ids = new String[size];
						for (int i = 0; i < size; i++) {
							ids[size - i - 1] = lists.get(i).getId() + "";
						}
						GroupListService.getService(App.app).saveContactTime(
								ids);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					UpdateFriendListThread.updateFriendList(App.app, null);

					MCResult result = APIRequestServers.friendList(App.app,
							"0", "0", "10");
					@SuppressWarnings("unchecked")
					List<FriendBean> objects = (List<FriendBean>) result
							.getResult();
					int size = objects.size();
					String[] ids = new String[size];
					for (int i = 0; i < size; i++) {
						ids[size - i - 1] = objects.get(i).getMemberId() + "";
					}
					FriendListService.getService(App.app).saveContactTime(ids);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		closeLogin();
	}

	private void closeLogin() {
		L.d(TAG, "closeLogin...");
		if (enterCreateGroupTopic) {
			LoginActivity.this.finish();
			return;
		}

		// Intent intent = new Intent(LoginActivity.this,
		// InfoWallActivity.class);
		// intent.putExtra("userName", userName);
		// intent.putExtra("password", password);
		// intent.putExtra("way", way);
		// intent.putExtra("time", time);
		// startActivity(intent);
		// LoginActivity.this.finish();

		L.d(TAG, "closeLogin..." + way);

		Bundle b = new Bundle();
		b.putString("userName", userName);
		b.putString("password", password);
		b.putString("way", way);
		b.putString("time", time);
		LogicUtil.enter(this, InfoWallActivity.class, b, true);
	}

	/**
	 * 找回密码
	 */
	// protected void findPassword() {
	// // 从给定的上下文获取LayoutInflater
	// LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
	// // 从指定的 xml 文件填充视图结构
	// View registerView = factory.inflate(R.layout.find_password, null);
	//
	// new AlertDialog.Builder(LoginActivity.this).setTitle("一键找回密码")
	// .setView(registerView)
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// SmsManager smsManager = SmsManager.getDefault();
	// /** 短信内容是有限制的，如果超过70个汉字需要拆分短信 **/
	// List<String> contents = smsManager.divideMessage("密码");
	// for (String text : contents) {
	// L.i(TAG, text);
	// smsManager.sendTextMessage(
	// ConstantUtil.REGISTER_ADDRESS, null, text,
	// null, null);
	// }
	// }
	// }).setNegativeButton("取消", null).create().show();
	// }

	/**
	 * 单击返回键时执行该方法
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		sinaHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		spdDialog.cancelProgressDialog(null);
		super.onDestroy();
	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			L.i(TAG, values.toString());
			final String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			final String uid = values.getString("uid");
			L.d(TAG, "AuthDialogListener access_token=" + token
					+ ",expires_in=" + expires_in + ",uid=" + uid);
			AccessToken accessToken = new AccessToken(token,
					WeiBoUtil.sina_AppSecret);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			spdDialog.showProgressDialog("授权成功，正在登录...");

			new Thread() {
				public void run() {
					String msgInfo = null;
					try {
						msgInfo = getMyInfo(uid);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (msgInfo == null) {
						sinaHandler.sendEmptyMessage(0);
						return;
					}

					try {
						JSONObject dataInfo = new JSONObject(msgInfo);
						// 获取参数
						String openId = dataInfo.getString("id");
						String name = dataInfo.getString("name");
						String sex = dataInfo.getString("gender") == "m" ? "1"
								: "2";
						String headUrlPath = dataInfo
								.getString("profile_image_url");

						// API注册
						loginResponse = APIRequestServers.registerByThirdParty(
								App.app, openId, WeiBoUtil.SINA, name, sex,
								headUrlPath, token, token);
						if (loginResponse.getResultCode() == 1) {
							String session_keys = loginResponse.getResult()
									.toString();
							if (session_key != null
									&& !session_key.equals(session_keys)) {
								SettingActivity.cleanAccountInfo(App.app,
										business);
							}
							L.i(TAG, "loginYuuQuu session_keys=" + session_keys);

							userBean = new UserBean("", "", session_keys, "yes");
							userBean.setAccountType(WeiBoUtil.SINA);
							userBean.setOpenId(openId);
							userBean.setName(name);
							userBean.setSex(sex);
							userBean.setHeadUrlPath(headUrlPath);
							userBean.setAccess_Token(token);
							userBean.setAccess_Token_secret(token);

							weiboSaveInfo(session_keys);
							// sinaHandler.sendEmptyMessage(1);
						} else {
							L.i(TAG, "loginYuuQuu 登录失败。。。");
							sinaHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						sinaHandler.sendEmptyMessage(0);
					}

					try {
						attentionUser(token, null, WeiBoUtil.sina_YuuQuu);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}

		@Override
		public void onError(DialogError e) {
			L.d(TAG, "AuthDialogListener Auth error : " + e.getMessage());
		}

		@Override
		public void onCancel() {
			L.d(TAG, "AuthDialogListener Auth cancel...");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			L.d(TAG, "AuthDialogListener Auth exception : " + e.getMessage());
		}

	}

	public void weiboSaveInfo(String session_keys) throws Exception {

		App.app.share.saveSessionKey(session_keys);
		App.app.share.saveBooleanMessage("program", "auto_login", true);

		MCResult mcResult = APIRequestServers.getMemberBasicInfo(App.app, "0");
		if (mcResult.getResultCode() == 1 && session_keys != null
				&& !"".equals(session_keys)) {
			business.delete(session_keys);

			MemberBean memberBean = (MemberBean) mcResult.getResult();
			int memberId = memberBean.getMemberId();
			userBean.setMemberId(memberId + "");
			MemberBasicBean basicInfo = memberBean.getBasicInfo();
			String name = basicInfo.getMemberName();
			userBean.setName(name);
			MemberHeadBean mhb = basicInfo.getHeadImage();
			userBean.setHeadUrlPath(mhb.getHeadUrl() + mhb.getHeadPath());
			L.i(TAG, "weiboSaveInfo memberId=" + memberId + ",name=" + name
					+ ",headurl=" + mhb.getHeadUrl() + mhb.getHeadPath());

			business.insert(userBean);
		}

		try {
			MCResult mc = APIRequestServers.singleInfo(App.app);
			JSONObject jsonObject = new JSONObject(mc.getResult().toString());
			String deviceToken = jsonObject.getString("sessionId");
			if (deviceToken != null
					&& !deviceToken.equals("")
					&& !deviceToken.equals(new SoftPhoneInfo(App.app)
							.getPhoneMark())) {
				way = SendWay.loginWay(jsonObject.getString("loginVisitWay"));
				time = DateTimeUtil
						.aTimeFormat(jsonObject.getLong("creatTime"));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ConstantUtil.isCreateInfo = true;

		try {
			// 新用户默认加入微博圈
			APIRequestServers.joinGroup(App.app, "7269");
		} catch (Exception e) {
			e.printStackTrace();
		}
		session_key = session_keys;
		enterMainActivity();
	}

	public Handler sinaHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				// updateUI("登录成功");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 获取用户信息
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	private String getMyInfo(String uid) throws Exception {
		Weibo weibo = Weibo.getInstance();
		String url = Weibo.SERVER + "users/show.json";
		L.d(TAG, "getMyInfo url=" + url);
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("source", Weibo.getAppKey());
		parameters.add("uid", uid);
		String msg = weibo.request(this, url, parameters, "GET",
				weibo.getAccessToken());
		L.d(TAG, "getMyInfo msg=" + msg);
		return msg;
	}

	/**
	 * 加关注
	 * 
	 * @param access_token
	 * @param uid
	 * @param screen_name
	 * @return
	 * @throws Exception
	 */
	private String attentionUser(String access_token, String uid,
			String screen_name) throws Exception {
		Weibo weibo = Weibo.getInstance();
		String url = Weibo.SERVER + "friendships/create.json";
		L.d(TAG, "attentionUser url=" + url);
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("source", Weibo.getAppKey());

		if (uid != null) {
			parameters.add("uid", uid);
		}

		if (screen_name != null) {
			parameters.add("screen_name", screen_name);
		}

		String msg = weibo.request(this, url, parameters, "POST",
				weibo.getAccessToken());
		L.d(TAG, "attentionUser msg=" + msg);
		return msg;
	}
}
