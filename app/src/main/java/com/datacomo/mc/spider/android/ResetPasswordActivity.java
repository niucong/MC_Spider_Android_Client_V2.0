package com.datacomo.mc.spider.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberContactBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.T;

public class ResetPasswordActivity extends BasicActionBarActivity {
	private static final String TAG = "ResetPasswordActivity";

	private EditText oldPassword_et, newPassword_et, renewPassword_et;
	private Button btn;
	private TextView repassword_tip;

	private String originalPassword_str, newPassword_str;
	private UserBusinessDatabase business;

	private String phone;
	private boolean FindPassword;

	private ImageView old_password_iv, new_password_iv;
	private boolean showPsd = true;

	// private SlideMenuView slide;
	// private MenuPage menus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.repassword);
		// ((ViewGroup) layout.getParent()).removeView(layout);
		// menus = new MenuPage(this);
		// slide = new SlideMenuView(this, menus, layout);
		// slide.setGap(layout.findViewById(R.id.left), 15);
		// setContentView(slide);

		findView();
		Bundle bundle = null;
		try {
			bundle = getIntent().getExtras();
			FindPassword = bundle.getBoolean("FindPassword", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (FindPassword) {
			phone = bundle.getString("phone");
			// setTitle("设置新密码", R.drawable.title_fanhui, null);
			ab.setTitle("设置新密码");
			repassword_tip.setVisibility(View.VISIBLE);
			findViewById(R.id.old_layout).setVisibility(View.GONE);
			newPassword_et.requestFocus();
		} else {
			// setTitle("更改登录密码", R.drawable.title_fanhui, null);
			ab.setTitle("更改登录密码");
			oldPassword_et.requestFocus();
		}
		business = new UserBusinessDatabase(this);

		if (phone == null && InfoWallActivity.infoWallActivity != null) {
			phone = InfoWallActivity.infoWallActivity.userName;
			if (!CharUtil.isValidPhone(phone)) {
				new Thread() {
					public void run() {
						try {
							MCResult mcResult = APIRequestServers
									.getMemberBasicInfo(
											ResetPasswordActivity.this, "0");
							MemberBean memberBean = (MemberBean) mcResult
									.getResult();
							MemberContactBean bean_MemberContact = memberBean
									.getContactInfo();
							phone = bean_MemberContact.getMemberPhone();
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		}
	}

	private void findView() {
		repassword_tip = (TextView) findViewById(R.id.repassword_tip);

		oldPassword_et = (EditText) findViewById(R.id.repassword_oldpassword_et);
		newPassword_et = (EditText) findViewById(R.id.repassword_newpassword_et);
		renewPassword_et = (EditText) findViewById(R.id.repassword_renewpassword_et);

		btn = (Button) findViewById(R.id.repassword_btn);
		btn.setOnClickListener(this);

		old_password_iv = (ImageView) findViewById(R.id.repassword_oldpassword_show);
		old_password_iv.setOnClickListener(this);

		new_password_iv = (ImageView) findViewById(R.id.repassword_newpassword_show);
		new_password_iv.setOnClickListener(this);

		showPsd = App.app.share.getBooleanMessage("program", "showPsd", true);
		showPsw();
	}

	private boolean checkPassword() {
		originalPassword_str = oldPassword_et.getText().toString();
		newPassword_str = newPassword_et.getText().toString();
		String sRenew = renewPassword_et.getText().toString();
		L.i(TAG, "checkPassword originalPassword_str=" + originalPassword_str
				+ ",sword_str=" + newPassword_str + ",sRenew=" + sRenew);

		if (!FindPassword) {
			if (originalPassword_str.equals("") || newPassword_str.equals("")) {
				// || sRenew.equals("")) {
				updateUI("密码不能为空");
				return false;
			}

			if (!CharUtil.isValidPassword(originalPassword_str)) {
				updateUI("您输入的旧密码有误，请重新输入");
				return false;
			}

			if (!CharUtil.isSetPassword(newPassword_str)) {
				updateUI("密码为8-16位，包含字母和数字，请重新输入。");
				return false;
			}

			if (originalPassword_str.equals(newPassword_str)) {
				updateUI("新密码与旧密码相同，请重新输入");
				return false;
			}
		}

		// if (newPassword_str.equals(sRenew)) {
		if (newPassword_str != null && newPassword_str.length() == 6
				&& CharUtil.isValidPhone(phone)
				&& phone.endsWith(newPassword_str)) {
			updateUI("密码不能设置为系统默认密码（手机后六位）");
			return false;
		}
		return true;
		// } else {
		// updateUI("两次输入的新密码不一致！");
		// return false;
		// }
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI(T.ErrStr);
				break;
			case 1:
				updateUI("密码更改成功！");
				business.updateUserPassword(App.app.share.getSessionKey(),
						newPassword_str);
				finish();
				break;
			case 2:
				updateUI("旧密码输入不合法");
				break;
			case 3:
				updateUI("新密码格式错误");
				break;
			case 4:
				updateUI("操作者不存在");
				break;
			case 5:
				updateUI("旧密码不正确");
				break;
			default:
				break;
			}
		}
	};

	private void updateUI(String msg) {
		spdDialog.cancelProgressDialog(msg);
	}

	/**
	 * 创建帐户
	 */
	private void resetPsw() {
		if (!checkPassword()) {
			return;
		}
		spdDialog.showProgressDialog("修改密码请求中...");

		new Thread() {
			public void run() {
				MCResult mc = null;
				String resultStr = null;
				try {
					if (FindPassword) {
						mc = APIRequestServers.forgetResetPassword(
								ResetPasswordActivity.this, phone,
								newPassword_str);
					} else {
						mc = APIRequestServers.resetPassword(
								ResetPasswordActivity.this,
								originalPassword_str, newPassword_str);
					}
					resultStr = mc.getResult().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ("1".equals(resultStr)) {
					handler.sendEmptyMessage(1);
				} else if ("2".equals(resultStr)) {
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
			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		BaseData.hideKeyBoard(this);
		spdDialog.cancelProgressDialog(null);
		super.onDestroy();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// slide.sliding(menus);
	// return false;
	// }
	//
	// @Override
	// public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
	// {
	// slide.sliding(menus);
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// protected void onLeftClick(View v) {
	// // slide.sliding(menus);
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	//
	// }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.repassword_btn:
			BaseData.hideKeyBoard(this);
			resetPsw();
			break;
		case R.id.repassword_oldpassword_show:
		case R.id.repassword_newpassword_show:
			showPsd = !showPsd;
			showPsw();
			App.app.share.saveBooleanMessage("program", "showPsd", showPsd);
			oldPassword_et.setSelection(oldPassword_et.getText().length());
			newPassword_et.setSelection(newPassword_et.getText().length());
			break;
		default:
			break;
		}
	}

	private void showPsw() {
		if (!showPsd) {
			old_password_iv.setImageResource(R.drawable.eye_close);
			new_password_iv.setImageResource(R.drawable.eye_close);
			oldPassword_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			newPassword_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		} else {
			old_password_iv.setImageResource(R.drawable.eye_open);
			new_password_iv.setImageResource(R.drawable.eye_open);
			oldPassword_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			newPassword_et.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// switch (event.getKeyCode()) {
	// case KeyEvent.KEYCODE_BACK:
	// if (slide.isMenuOpen()) {
	// slide.sliding(menus);
	// // exitFlag = false;
	// return true;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
