package com.datacomo.mc.spider.android;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.fragmt.MailDetail;
import com.datacomo.mc.spider.android.net.APIMailRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MailBean;
import com.datacomo.mc.spider.android.receiver.SimpleReceiver;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.T;
import com.umeng.analytics.MobclickAgent;

public class MailDetailsIndexActivity extends BaseFragAct {
	private final String TAG = "MailDetailsIndexActivity";
	private String friendId, friendName, mId, rId;
	private ImageView reMail, delete, toLeft, toRight;
	private MailBean mailBean;
	private int nextMId, preMId, nextRId, preRId, position;
	private HashMap<String, MailBean> stored = new HashMap<String, MailBean>();

	public static MailDetailsIndexActivity mailDetailsIndexActivity;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		mailDetailsIndexActivity = null;
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "18");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		mailDetailsIndexActivity = this;
		setContent(R.layout.layout_mail_details_index);
		Bundle b = getIntent().getExtras();
		if (null == b) {
			finish();
			return;
		} else {
			mId = b.getString("mailId");
			rId = b.getString("recordId");
			friendId = b.getString("friendId");
			friendName = b.getString("friendName");
			position = b.getInt("position", -1);
		}
		findViews();
		showMail(true);
	}

	private void findViews() {
		// setTitle("邮件详情", R.drawable.title_fanhui,
		// R.drawable.title_header_more);
		ab.setTitle("邮件详情");
		reMail = (ImageView) findViewById(R.id.huifu);
		delete = (ImageView) findViewById(R.id.delete);
		toLeft = (ImageView) findViewById(R.id.pre);
		toRight = (ImageView) findViewById(R.id.next);
		reMail.setOnClickListener(this);
		delete.setOnClickListener(this);
		toLeft.setOnClickListener(this);
		toRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.huifu:
			reMail();
			break;
		case R.id.delete:
			deleteMail();
			break;
		case R.id.pre:
			nextMail(false);
			break;
		case R.id.next:
			nextMail(true);
			break;
		default:
			break;
		}
	}

	private boolean checkMailEmpty(boolean showTip) {
		if (null == mailBean) {
			if (showTip) {
				showTip("正在获取邮件详情！");
			}
			return true;
		}
		return false;
	}

	private void nextMail(boolean right) {
		if (!checkMailEmpty(false)) {
			if (right) {
				if (0 != nextMId && 0 != nextRId) {
					mId = nextMId + "";
					rId = nextRId + "";
					if (!toLeft.isEnabled()) {
						toLeft.setEnabled(true);
						toLeft.setImageResource(R.drawable.icon_left);
					}
					position++;
				}
			} else {
				if (0 != preMId && 0 != preRId) {
					mId = preMId + "";
					rId = preRId + "";
					if (!toRight.isEnabled()) {
						toRight.setEnabled(true);
						toRight.setImageResource(R.drawable.icon_right);
					}
					position--;
				}
			}
		}
		showMail(right);
	}

	public int getCur() {
		return position;
	}

	private void checkIndex(MailBean bean) {
		nextMId = bean.getNextEmailId();
		preMId = bean.getPreviousEmailId();
		nextRId = bean.getNextEmailRecordId();
		preRId = bean.getPreviousEmailRecordId();
		if (0 == nextMId || 0 == nextRId) {
			toRight.setEnabled(false);
			// toRight.setImageResource(R.drawable.icon_right);
		}
		if (0 == preMId || 0 == preRId) {
			toLeft.setEnabled(false);
			// toLeft.setImageResource(R.drawable.icon_left);
		}
	}

	private void showMail(boolean right) {
		try {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			mailBean = stored.get(rId);
			// MailDetail f = new MailDetail(position, mId, rId, mailBean);
			MailDetail f = new MailDetail().newInstance(position, mId, rId,
					mailBean);
			if (null != mailBean) {
				checkIndex(mailBean);
			}
			if (right) {
				ft.setCustomAnimations(R.anim.push_rihgt_in,
						R.anim.push_left_out);
			} else {
				ft.setCustomAnimations(R.anim.push_left_in,
						R.anim.push_rihgt_out);
			}
			ft.replace(R.id.container, f);
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void putMap(String key, MailBean b) {
		mailBean = b;
		checkIndex(mailBean);
		stored.put(key, b);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_more).setVisible(true);
		MenuItem mi = menu.findItem(R.id.action_refresh);
		mi.setVisible(true);
		mi.setIcon(R.drawable.nothing);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			showMenuDiolog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void showMenuDiolog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MailDetailsIndexActivity.this)
		// .setItems(
		// // "回复全部",
		// new CharSequence[] { "回复", "回复全部", "转发", "删除" },
		// new DialogInterface.OnClickListener() {
				.setAdapter(new ArrayAdapter(this, R.layout.choice_item,
						new String[] { "回复", "回复全部", "转发", "删除" }),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										MailDetailsIndexActivity.this,
										MailCreateActivity.class);
								switch (which) {
								case 0:// 回复
									reMail();
									break;
								case 2:// 转发
									if (!checkMailEmpty(true)) {
										intent.putExtra("sendType", 3);
										intent.putExtra("MailBean", mailBean);
										startActivity(intent);
									}
									break;
								case 1:// 回复全部
									reMailAll();
									break;
								case 3:// 删除
									deleteMail();
									break;
								}
							}
						});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	/**
	 * 回复
	 */
	private void reMail() {
		if (!checkMailEmpty(true)) {
			Intent intent = new Intent(MailDetailsIndexActivity.this,
					MailCreateActivity.class);
			intent = intent.putExtra("friendName", friendName);
			try {
				intent.putExtra("friendId", Integer.valueOf(friendId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			intent.putExtra("sendType", 2);
			intent.putExtra("MailBean", mailBean);
			startActivity(intent);
		}
	}

	/**
	 * 回复全部
	 */
	private void reMailAll() {
		if (!checkMailEmpty(true)) {
			Intent intent = new Intent(MailDetailsIndexActivity.this,
					MailCreateActivity.class);
			intent = intent.putExtra("friendName", friendName);
			try {
				intent.putExtra("friendId", Integer.valueOf(friendId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			intent.putExtra("sendType", 4);
			intent.putExtra("MailBean", mailBean);
			startActivity(intent);
		}
	}

	/**
	 * 删除邮件
	 */
	private void deleteMail() {
		if (!checkMailEmpty(true)) {
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("您确定要删除此邮件吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new Thread() {
										@Override
										public void run() {
											try {
												MCResult mcResult = APIMailRequestServers
														.mailDeleteToRecycle(
																MailDetailsIndexActivity.this,
																new String[] { rId });
												int Code_result = mcResult
														.getResultCode();
												if (Code_result == 1) {
													handler.sendEmptyMessage(1);
													if (position == 0) {
														try {
															APIMailRequestServers
																	.contactLeaguers(
																			MailDetailsIndexActivity.this,
																			"0",
																			"20");
														} catch (Exception e) {
															e.printStackTrace();
														}
													}
												} else {
													handler.sendEmptyMessage(0);
												}
											} catch (Exception e) {
												e.printStackTrace();
												handler.sendEmptyMessage(0);
											}
										}
									}.start();
								}
							}).setNegativeButton("取消", null).show();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showTip(T.ErrStr);
				break;
			case 1:
				showTip("已删除！");
				L.d(TAG, "handler position=" + position);
				if (position != -1) {
					MailWithActivity.index = position;
				}
				SimpleReceiver.sendBoardcast(MailDetailsIndexActivity.this,
						MailListActivity.REFRESH);
				SimpleReceiver.sendBoardcast(MailDetailsIndexActivity.this,
						MailWithActivity.REFRESH);
				finish();
				break;
			default:
				break;
			}
		};
	};
}
