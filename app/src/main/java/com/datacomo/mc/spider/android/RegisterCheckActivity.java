package com.datacomo.mc.spider.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.UserBean;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.db.RegisterInfoDatabase;
import com.datacomo.mc.spider.android.db.UpdateContactHeadService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.service.UpdateGroupListThread;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class RegisterCheckActivity extends BasicActionBarActivity {
	private static final String TAG = "RegisterCheckActivity";

	private TextView login_tv, recode_tv;
	private TextView mTxt_SendAgain;
	private TextView mTxt_Phonenum;
	private EditText code_et;
	private Timer mTimer;
	private Button btnActivate;

	private String code_str = null;
	private String session_key;
	private int mCountdown;
	private boolean mIsInit;

	private RegisterInfoDatabase infoDatabase = null;
	private UserBusinessDatabase userDatabase = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.register_check);
		infoDatabase = new RegisterInfoDatabase(App.app);
		userDatabase = new UserBusinessDatabase(App.app);

		findView();
		setView();

		SettingActivity.cleanAccountInfo(App.app, userDatabase);
	}

	private void findView() {
		btnActivate = (Button) findViewById(R.id.layout_registercheck__btn_activate);
		recode_tv = (TextView) findViewById(R.id.register_check_reget);
		login_tv = (TextView) findViewById(R.id.register_check_login);
		code_et = (EditText) findViewById(R.id.register_check);
		mTxt_SendAgain = (TextView) findViewById(R.id.layout_registercheck_txt_sendagain);
		mTxt_Phonenum = (TextView) findViewById(R.id.layout_registercheck_txt_phonenum);
	}

	private void setView() {
		// setTitle("优优工作圈", R.drawable.title_fanhui, null);

		ArrayList<String> info = infoDatabase.queryInfo();
		String phoneNum = info.get(2);
		mTxt_Phonenum.setText(phoneNum);
		btnActivate.setOnClickListener(this);
		mTxt_SendAgain.setOnClickListener(this);
		mTxt_SendAgain.setEnabled(false);

		login_tv.setOnClickListener(this);
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

	private Handler checkHandler = new Handler() {
		// 0:系统异常 1. 注册成功 2.手机号已被绑定 更新name address email 性别 6.手机号不合法
		// 7.验证码和手机号不匹配或验证码过期"}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				setResult(1);
				finish();
				break;
			case 2:
				updateUI("手机号已注册过，将要更新您帐户信息");
				break;
			case 6:
				updateUI("手机号不合法");
				break;
			case 7:
				updateUI("验证码和手机号不匹配或验证码过期");
				break;
			default:
				break;
			}
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			code_et.setText("");
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				updateUI("验证码已发送，请注意查收！");
				break;
			case 2:
				updateUI("手机号码不合法");
				infoDatabase.delete();
				LogicUtil.enter(RegisterCheckActivity.this,
						RegisterActivity.class, null, true);
				break;
			case 3:
				updateUI("验证码已下发，特定时间内只能下发一次");
				break;
			case 4:
				updateUI("该邮箱已注册或邮箱格式错误");
				infoDatabase.delete();
				LogicUtil.enter(RegisterCheckActivity.this,
						RegisterActivity.class, null, true);
				break;
			default:
				break;
			}
		}
	};

	private void updateUI(String msg) {
		spdDialog.cancelProgressDialog(msg);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.layout_registercheck__btn_activate:
			createAccount();
			break;
		case R.id.layout_registercheck_txt_sendagain:
			mTxt_SendAgain.setEnabled(false);
			mIsInit = true;
			reGetVerifyCode();
			break;
		case R.id.register_check_login:
			Intent intent = new Intent(RegisterCheckActivity.this,
					LoginActivity.class);
			intent.putExtra("addUser", true);
			startActivity(intent);
			LogicUtil.finish(RegisterCheckActivity.this);
			break;
		default:
			break;
		}
	}

	/**
	 * UI
	 */
	private Handler reGetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.arg1 == 60)
					recode_tv.setText("未收到短信？ " + 1 + " 分钟后可");
				else
					recode_tv.setText("未收到短信？ " + msg.arg1 + " 秒钟后可");
				if (msg.arg1 == 0) {
					mTxt_SendAgain.setEnabled(true);
				}
				break;
			default:
				break;
			}
		}
	};

	private void reGetVerifyCode() {
		final ArrayList<String> info = infoDatabase.queryInfo();
		if (info != null) {
			spdDialog.showProgressDialog("正在获取验证码...");
			new Thread() {
				public void run() {
					try {
						MCResult response = APIRequestServers.register(
								RegisterCheckActivity.this, info.get(0),
								info.get(1), info.get(2));
						if (response != null) {
							String result = response.getResult().toString();
							if ("1".equals(result)) {
								handler.sendEmptyMessage(1);
							} else if ("2".equals(result)) {
								handler.sendEmptyMessage(2);
							} else if ("3".equals(result)) {
								handler.sendEmptyMessage(3);
							} else if ("4".equals(result)) {
								handler.sendEmptyMessage(4);
							} else {
								handler.sendEmptyMessage(0);
							}
						} else {
							handler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(0);
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	/**
	 * 创建帐户
	 */
	private void createAccount() {
		code_str = code_et.getText().toString();
		if (code_et == null || code_et.equals("")) {
			showTip("请先输入验证码");
			return;
		}
		BaseData.hideKeyBoard(this);
		spdDialog.showProgressDialog("正在检测验证码...");
		new Thread() {
			public void run() {
				ArrayList<String> info = infoDatabase.queryInfo();
				if (info != null) {
					String phone = info.get(2);
					try {
						MCResult response = APIRequestServers.verifyCodeCheck(
								App.app, phone, code_str);
						if (response != null) {
							int resultCode = response.getResultCode();
							if (resultCode == 1) {
								session_key = response.getResult().toString();
								App.app.share.saveSessionKey(session_key);

								try {
									MCResult mcResult = APIRequestServers
											.getMemberBasicInfo(App.app, "0");
									if (mcResult.getResultCode() == 1
											&& session_key != null
											&& !"".equals(session_key)) {
										MemberBean memberBean = (MemberBean) mcResult
												.getResult();
										int memberId = memberBean.getMemberId();
										MemberBasicBean basicInfo = memberBean
												.getBasicInfo();
										UserBean bean = new UserBean(phone,
												info.get(1), session_key, "yes");
										bean.setName(basicInfo.getMemberName());
										bean.setMemberId(memberId + "");
										MemberHeadBean mhb = basicInfo
												.getHeadImage();
										bean.setHeadUrlPath(mhb.getHeadUrl()
												+ mhb.getHeadPath());
										L.i(TAG, "createAccount memberId="
												+ memberId);
										userDatabase.insert(bean);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								try {
									APIRequestServers
											.singleInfo(RegisterCheckActivity.this);
									// 新用户默认加入微博圈
									APIRequestServers.joinGroup(
											RegisterCheckActivity.this, "7269");
								} catch (Exception e) {
									e.printStackTrace();
								}
								loginSuccess(info.get(0));
							} else {
								String result = response.getResult().toString();
								if ("2".equals(result)) {
									checkHandler.sendEmptyMessage(2);
								} else if ("6".equals(result)) {
									checkHandler.sendEmptyMessage(6);
								} else if ("7".equals(result)) {
									checkHandler.sendEmptyMessage(7);
								} else {
									checkHandler.sendEmptyMessage(0);
								}
							}
						} else {
							checkHandler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						checkHandler.sendEmptyMessage(0);
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 登录成功
	 * 
	 * @param phone
	 * @param password
	 * @param session_key
	 */
	private void loginSuccess(String mail) {
		saveUserInfo();
		infoDatabase.delete();
		checkHandler.sendEmptyMessage(1);

		Intent intent = new Intent(RegisterCheckActivity.this,
				GuideNameActivity.class);
		intent.putExtra("Email", mail);
		startActivity(intent);
		finish();
	}

	/**
	 * 保存用户登录信息
	 * 
	 * @param username
	 * @param password
	 */
	private void saveUserInfo() {
		new Thread() {
			public void run() {
				try {
					MCResult response = APIRequestServers.uploadPhoneBook(
							RegisterCheckActivity.this, null, null, null);
					if (response != null && response.getResultCode() == 1) {
						try {
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd");
							final String newDate = format.format(new Date());
							userDatabase.updateTime(session_key, newDate);
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							APIRequestServers
									.getaddressBook(RegisterCheckActivity.this);
						} catch (Exception e) {
							e.printStackTrace();
						}

						HashMap<String, String[]> map = new ContactsBookService(
								RegisterCheckActivity.this).getContactHeads();
						if (map != null && map.size() > 0) {
							UpdateContactHeadService contactHeadService = new UpdateContactHeadService(
									App.app);
							HashMap<String, String> mapLast = contactHeadService
									.getContactHeads();
							boolean b = mapLast != null;
							for (Iterator<?> iterator = map.keySet().iterator(); iterator
									.hasNext();) {
								try {
									String key = (String) iterator.next();
									String value = map.get(key)[0];
									value = ThumbnailImageUrl
											.getThumbnailHeadUrl(
													value,
													HeadSizeEnum.THREE_HUNDRED_AND_SIXTY);
									if (value != null && !"".equals(value)) {
										if (b && mapLast.containsKey(key)) {
											if (!value.equals(mapLast.get(key))) {
												ContactsUtil.changeMemberHead(
														App.app,
														map.get(key)[1], key,
														value);
												contactHeadService.updateHead(
														key, value);
											}
										} else {
											ContactsUtil.changeMemberHead(
													App.app, map.get(key)[1],
													key, value);
											contactHeadService.save(key, value);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								} catch (OutOfMemoryError e) {
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					UpdateGroupListThread.updateGroupList(
							RegisterCheckActivity.this, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					UpdateFriendListThread.updateFriendList(
							RegisterCheckActivity.this, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		checkHandler.removeCallbacksAndMessages(null);
		handler.removeCallbacksAndMessages(null);
		reGetHandler.removeCallbacksAndMessages(null);

		ScreenManager.getInctance().removeActivity(this);
		BaseData.hideKeyBoard(this);
		mTimer.cancel();
		spdDialog.cancelProgressDialog(null);
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			infoDatabase.delete();
			LogicUtil.enter(RegisterCheckActivity.this, RegisterActivity.class,
					null, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
