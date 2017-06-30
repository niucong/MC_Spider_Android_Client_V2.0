package com.datacomo.mc.spider.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.util.MemberContactUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

public class AboutActivity extends BasicActionBarActivity {

	private Button tel, mail, score;
	private TextView version;

	private final String tel_str = "400 660 6780";
	private final String mail_str = "kefu@yuuquu.com";

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.about);
		ab.setTitle("关于优优工作圈");
		findView();
		setView();
	}

	private void findView() {
		version = (TextView) findViewById(R.id.about_version);
		tel = (Button) findViewById(R.id.about_tel);
		mail = (Button) findViewById(R.id.about_mail);
		score = (Button) findViewById(R.id.about_score);
	}

	private void setView() {
		String currentVersion = new SoftPhoneInfo(this).getVersionName();
		if (currentVersion != null && !"".equals(currentVersion)) {
			version.setText("Android " + currentVersion + "版");
		}

		tel.setOnClickListener(this);
		tel.setText("客服电话：" + tel_str);
		mail.setOnClickListener(this);
		mail.setText("客服邮箱：" + mail_str);
		score.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.about_tel:
			new MemberContactUtil(this).callPhone(tel_str);
			break;
		case R.id.about_mail:
			Intent intent = new Intent(this, MailCreateActivity.class);
			intent.putExtra("friendName", mail_str);
			startActivity(intent);
			break;
		case R.id.about_score:
			try {
				String mAddress = "market://details?id=" + getPackageName();
				Intent marketIntent = new Intent("android.intent.action.VIEW");
				marketIntent.setData(Uri.parse(mAddress));
				startActivity(marketIntent);
			} catch (Exception e) {
				Uri uri = Uri.parse("https://market.android.com/details?id="
						+ getPackageName());
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
			break;
		default:
			break;
		}
	}
}