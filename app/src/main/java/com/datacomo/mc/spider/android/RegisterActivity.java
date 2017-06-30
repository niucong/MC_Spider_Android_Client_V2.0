package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.RegisterInfoDatabase;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class RegisterActivity extends BasicActionBarActivity {
	private static final String TAG = "RegisterActivity";

	private TextView login_tv;
	private EditText password_et, phone_et;
	private AutoCompleteTextView mail_et;
	private ImageView password_iv;// mail_iv, phone_iv,
	private Button btn_Register;

	// private boolean isAbleEmail = false, isAblePsw = false,
	// isAblePhone = false;

	private ArrayList<String> arrayList = null;

	private String account_str = null;
	private String password_str = null;
	private String phone_str = null;

	private RegisterInfoDatabase infoDatabase = null;

	private boolean showPsd = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.register);

		infoDatabase = new RegisterInfoDatabase(App.app);
		infoDatabase.delete();

		arrayList = new ArrayList<String>();
		findView();
		setView();

		goldNumByGuideType();
	}

	private void findView() {
		login_tv = (TextView) findViewById(R.id.register_login);
		mail_et = (AutoCompleteTextView) findViewById(R.id.register_mail);
		password_et = (EditText) findViewById(R.id.register_password);
		phone_et = (EditText) findViewById(R.id.register_phone);
		btn_Register = (Button) findViewById(R.id.layout_register_btn_register);
		// mail_iv = (ImageView) findViewById(R.id.register_mail_able);
		password_iv = (ImageView) findViewById(R.id.register_password_show);
		// phone_iv = (ImageView) findViewById(R.id.register_phone_able);
	}

	private void setView() {
		// setTitle("优优工作圈", R.drawable.title_fanhui, null);

		login_tv.setOnClickListener(this);
		btn_Register.setOnClickListener(this);
		mail_et.addTextChangedListener(watcher);
		password_et.addTextChangedListener(watcher);
		phone_et.addTextChangedListener(watcher);

		showPsd = App.app.share.getBooleanMessage("program", "showPsd", true);
		showPsw();
		password_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPsd = !showPsd;
				showPsw();
				password_et.setSelection(password_et.getText().length());
				App.app.share.saveBooleanMessage("program", "showPsd", showPsd);
			}
		});
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

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (mail_et.hasFocus()) {
				account_str = s.toString();
				if (account_str != null && !account_str.equals("")
						&& !account_str.contains("@")) {
					arrayList.clear();
					for (String str : ConstantUtil.AUTOMAIL) {
						arrayList.add(account_str + str);
					}
					autoText();
				}
				// if (CharUtil.isValidEmail(account_str)) {
				// isAbleEmail = true;
				// mail_iv.setBackgroundResource(R.drawable.checked);
				// } else {
				// isAbleEmail = false;
				// mail_iv.setBackgroundResource(0);
				// }
			} else if (password_et.hasFocus()) {
				password_str = s.toString();
				// if (CharUtil.isValidPassword(password_str)) {
				// isAblePsw = true;
				// password_iv.setBackgroundResource(R.drawable.checked);
				// } else {
				// isAblePsw = false;
				// password_iv.setBackgroundResource(0);
				// }
			} else if (phone_et.hasFocus()) {
				phone_str = s.toString();
				// if (CharUtil.isValidPhone(phone_str)) {
				// isAblePhone = true;
				// phone_iv.setBackgroundResource(R.drawable.checked);
				// } else {
				// isAblePhone = false;
				// phone_iv.setBackgroundResource(0);
				// }
			}
		}
	};

	private void autoText() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arrayList);
		mail_et.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		BaseData.hideKeyBoard(this);
		switch (v.getId()) {
		case R.id.layout_register_btn_register:
			createAccount();
			break;
		default:
			break;
		}
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
				// updateUI("已将验证码短信发送给您，请注意查收");
				break;
			case 2:
				updateUI("手机号码不合法");
				// phone_iv.setBackgroundResource(R.drawable.unchecked);
				break;
			case 3:
				updateUI("验证码已下发，特定时间内只能下发一次");
				break;
			case 4:
				updateUI("该邮箱已注册或邮箱格式错误");
				// mail_iv.setBackgroundResource(R.drawable.unchecked);
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
	private void createAccount() {
		account_str = mail_et.getText().toString();
		password_str = password_et.getText().toString();
		phone_str = phone_et.getText().toString();

		L.d(TAG, "createAccount account_str=" + account_str + ",password_str="
				+ password_str + ",phone_str=" + phone_str);
		if (!CharUtil.isValidEmail(account_str)) {
			T.show(getApplicationContext(), "请输入合法邮箱");
			return;
		} else if (!CharUtil.isSetPassword(password_str)) {
			T.show(getApplicationContext(), "密码为8-16位，包含字母和数字，请重新输入。");
			return;
		} else if (!CharUtil.isValidPhone(phone_str)) {
			T.show(getApplicationContext(), "请输入合法手机号");
			return;
		}

		spdDialog.showProgressDialog("正在获取验证码...");
		new Thread() {
			public void run() {
				try {
					MCResult response = APIRequestServers.register(
							RegisterActivity.this, account_str, password_str,
							phone_str);
					if (response != null) {
						String resultStr = response.getResult().toString();
						if ("1".equals(resultStr)) {
							infoDatabase.insert(account_str, password_str,
									phone_str);
							// handler.sendEmptyMessage(1);
							enterActivity(RegisterCheckActivity.class);
						} else if ("2".equals(resultStr)) {
							handler.sendEmptyMessage(2);
						} else if ("3".equals(resultStr)) {
							handler.sendEmptyMessage(3);
						} else if ("4".equals(resultStr)) {
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

	/**
	 * Activity跳转
	 */
	private void enterActivity(Class<?> cls) {
		Intent intent = new Intent(RegisterActivity.this, cls);
		Bundle bundle = new Bundle();
		bundle.putBoolean("OnlyCheck", false);
		intent.putExtras(bundle);
		startActivityForResult(intent, 0);
		LogicUtil.finish(RegisterActivity.this);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		spdDialog.cancelProgressDialog(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	/**
	 * 获取引导操作圈币数量
	 */
	private void goldNumByGuideType() {
		new Thread() {
			public void run() {
				String[] guideTypes = { "MODIFY_MEMBER_NAME",
						"MODIFY_MEMBER_HEAD" };
				for (String guideType : guideTypes) {
					try {
						MCResult response = APIRequestServers
								.goldNumByGuideType(App.app, guideType);
						if (response != null) {
							int num = Integer.parseInt(response.getResult()
									.toString());
							if (num != 0) {
								App.app.share.saveIntMessage("program",
										guideType, num);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent request = new Intent(RegisterActivity.this,
					LoginActivity.class);
			startActivity(request);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// Intent request = new Intent(RegisterActivity.this, LoginActivity.class);
	// startActivity(request);
	// this.finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	//
	// }

}
