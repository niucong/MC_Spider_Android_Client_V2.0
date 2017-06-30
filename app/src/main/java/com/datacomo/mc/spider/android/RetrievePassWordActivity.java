package com.datacomo.mc.spider.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class RetrievePassWordActivity extends BasicActionBarActivity {
	public static final String TAG = "RetrievePassWordActivity";
	private EditText phoneEmail;
	private Button nextButton;

	private boolean IsEmail;
	private String phonemail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.retrieve_password);
		init();
	}

	public void init() {
		nextButton = (Button) findViewById(R.id.next_button);
		IsEmail = getIntent().getBooleanExtra("IsEmail", false);
		if (IsEmail) {
			phoneEmail = (EditText) findViewById(R.id.retrieve_email);
			// setTitle("填写邮箱", R.drawable.title_fanhui, null);
			ab.setTitle("填写邮箱");
			nextButton.setText("完 成");
			findViewById(R.id.textView_mail).setVisibility(View.VISIBLE);
		} else {
			phoneEmail = (EditText) findViewById(R.id.retrieve_phone);
			// setTitle("填写手机号", R.drawable.title_fanhui, null);
			ab.setTitle("填写手机号");
		}
		phoneEmail.setVisibility(View.VISIBLE);
		phonemail = getIntent().getStringExtra("phone");
		if (phonemail != null && !"".equals(phonemail)) {
			phoneEmail.setText(phonemail);
			phoneEmail.setSelection(phonemail.length());
		}

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = phoneEmail.getText().toString();
				if (IsEmail) {
					if (!CharUtil.isValidEmail(phone)) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码").setMessage("请输入正确的邮箱。")
								.setPositiveButton("重新输入", null)
								.setCancelable(false).show();
						return;
					} else if (phone.endsWith("@yuuquu.com")) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码").setMessage("请输入您绑定优优工作圈的其他邮箱！")
								.setPositiveButton("重新输入", null)
								.setCancelable(false).show();
						return;
					}
					spdDialog.showProgressDialog("正在处理中...");
				} else {
					if (!CharUtil.isValidPhone(phone)) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码").setMessage("请输入正确的手机号。")
								.setPositiveButton("重新输入", null)
								.setCancelable(false).show();
						return;
					}
					spdDialog.showProgressDialog("正在获取验证码...");
				}
				new loadInfoTask(RetrievePassWordActivity.this, phone)
						.execute();
			}
		});
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		spdDialog.cancelProgressDialog(null);
		BaseData.hideKeyBoard(this);
		super.onDestroy();
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
				if (IsEmail) {
					mcResult = APIRequestServers.retrievePasswordEmail(
							RetrievePassWordActivity.this, phone);
				} else {
					mcResult = APIRequestServers.createVerifyCodeByPhone(
							RetrievePassWordActivity.this, phone);
				}
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
				int success = 0;
				try {
					success = (Integer) result.getResult();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (IsEmail) {
					if (success == 1) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码")
								.setMessage("我们已向您的邮箱发送了验证邮件，请根据邮件中的提示，完成密码重设。")
								// "我们已将“优优工作圈 - 重置密码”邮件发送到您的邮箱69******3@qq.com，请在30分钟内查收并点击邮件中的链接重置密码。"
								.setPositiveButton("我知道了",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												LogicUtil
														.enter(RetrievePassWordActivity.this,
																LoginActivity.class,
																null, true);
											}
										}).setCancelable(false).show();
					} else if (success == 2) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码").setMessage("您填写的邮箱尚未加入优优工作圈！")
								.setPositiveButton("重新输入", null)
								.setCancelable(false).show();
					} else {
						T.show(context, T.ErrStr);
					}
				} else {
					if (success == 1) {
						Bundle bundle = new Bundle();
						bundle.putString("phone", phone);
						LogicUtil.enter(context, VerificationActivity.class,
								bundle, true);
					} else if (success == 2) {
						new AlertDialog.Builder(RetrievePassWordActivity.this)
								.setTitle("重置密码").setMessage("您填写的手机号尚未加入优优工作圈！")
								.setPositiveButton("重新输入", null)
								.setCancelable(false).show();
					} else {
						T.show(context, T.ErrStr);
					}
				}
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			LogicUtil.enter(this, LoginActivity.class, null, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// LogicUtil.enter(this, LoginActivity.class, null, true);
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	//
	// }

}
