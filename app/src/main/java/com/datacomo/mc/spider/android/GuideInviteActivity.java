package com.datacomo.mc.spider.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;

public class GuideInviteActivity extends InviteAddActivity {
	private static final String TAG = "GuideInviteActivity";

	public static GuideInviteActivity guideInviteActivity;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		guideInviteActivity = this;
		// setTitle("加入朋友圈", R.drawable.title_fanhui, null);
		ab.setTitle("加入朋友圈");
		
		GetDbInfoUtil.updateUserInfo(this, App.app.share.getSessionKey(),
				new UserBusinessDatabase(App.app));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		L.i(TAG, "onRestart...");
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.guide_invite_confirm:
			inviteMember();
			break;
		case R.id.guide_invite_cancel:
			bookService.delete();
			enterNextActivity(InfoWallActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 邀请加入
	 */
	private void inviteMember() {
		String address = "";
		if (adapter != null) {
			address = adapter.getNumbers();
		}
		Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
				+ address));
		it.putExtra("sms_body", "我加入了优优工作圈，希望加你为朋友，请前往确认：http://yuuquu.com[优优工作圈]");
		this.startActivityForResult(it, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == 0 && requestCode == 1) {
			enterNextActivity(InfoWallActivity.class);
		}
	}

	/**
	 * 页面跳转
	 */
	private void enterNextActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		this.startActivity(intent);
		finishGuideActivity();
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
						finishGuideActivity();
					}
				}).setNegativeButton("否", null).show();// 显示对话框
	}

	/**
	 * 关闭引导页面
	 */
	private void finishGuideActivity() {
		if (GuideNameActivity.guideNameActivity != null) {
			LogicUtil.finish((Context) GuideNameActivity.guideNameActivity);
		}
		if (GuideHeadPhotoActivity.guideHeadPhotoActivity != null) {
			LogicUtil
					.finish((Context) GuideHeadPhotoActivity.guideHeadPhotoActivity);
		}
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, GuideHeadPhotoActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// startActivity(new Intent(this, GuideHeadPhotoActivity.class));
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }
}
