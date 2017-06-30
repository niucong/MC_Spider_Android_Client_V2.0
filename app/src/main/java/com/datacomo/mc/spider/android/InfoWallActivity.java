package com.datacomo.mc.spider.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.InfoAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.db.UpdateContactHeadService;
import com.datacomo.mc.spider.android.enums.ClientWelcomePicEnum;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.CharUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("SimpleDateFormat")
public class InfoWallActivity extends BasicMenuActivity implements
		OnTabClickListener, OnLoadMoreListener {
	private final String TAG = "InfoWallActivity";

	private TabLinearLayout mTabLinearLayout;
	private RefreshListView listView;
	private ArrayList<ResourceTrendBean> infos, friends, opens;
	private InfoAdapter infoAdapter, friendAdapter, openAdapter;
	private int cut;
	public static boolean isNeedRefresh = false;
	private String trendId_info, trendId_friend, trendId_open, createTime;
	private boolean isLoading;
	private loadInfoTask task;

	private TextView textView;

	public static InfoWallActivity infoWallActivity = null;

	public static boolean isReqVersion = true;
	public static String versionDate = null;

	// public int fristInfoId, fristFriendId, fristOpenId;

	// private Intent service;

	private boolean isLoadingData = false;

	public String userName, password;

	// private long lastRefershTime;
	private String phone;
	private boolean FindPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_INFO;
		titleName = "动态墙";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);

		if (ConstantUtil.isCreateInfo) {
			ConstantUtil.isCreateInfo = false;
			checkRequest(getIntent());
		}
		infoWallActivity = this;

		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_info_wall, null);
		fl.addView(rootView);

		findView(rootView);
		setView();

		startNService(false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "1");
		L.d(TAG, "onStart");
		supportInvalidateOptionsMenu();
	}

	/**
	 * 找回密码提示及其他终端掉线
	 * 
	 * @param intent
	 */
	private void checkRequest(Intent intent) {
		L.d(TAG, "checkRequest...");
		if (null != intent) {

			FindPassword = intent.getBooleanExtra("FindPassword", false);
			if (FindPassword) {
				phone = intent.getStringExtra("phone");
				new Builder(InfoWallActivity.this).setTitle("优优工作圈")
						.setMessage("为方便下次登录，请设置新密码。")
						.setPositiveButton("设置新密码", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Bundle bundle = new Bundle();
								bundle.putBoolean("FindPassword", true);
								bundle.putString("phone", phone);
								LogicUtil.enter(InfoWallActivity.this,
										ResetPasswordActivity.class, bundle,
										false);
							}
						}).show();
				return;
			}

			userName = intent.getStringExtra("userName");
			password = intent.getStringExtra("password");
			if (password != null && password.length() == 6
					&& CharUtil.isValidPhone(userName)
					&& userName.endsWith(password)) {
				if (infoWallActivity != null)
					new Builder(this)
							.setTitle("优优工作圈")
							.setMessage("您当前密码是系统默认密码，为防止密码泄漏，建议您即刻修改密码！")
							.setPositiveButton("马上修改",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LogicUtil
													.enter(InfoWallActivity.this,
															ResetPasswordActivity.class,
															null, false);
										}
									}).setNegativeButton("暂不修改", null).show();
				return;
			}

			String way = intent.getStringExtra("way");
			String time = intent.getStringExtra("time");
			L.d(TAG, "checkRequest way=" + way);
			if (way != null && !"".equals(way) && time != null
					&& !"".equals(time)) {
				final AlertDialog dialog = new Builder(
						InfoWallActivity.this)
						.setTitle("优优工作圈")
						.setMessage(
								"为确保帐号安全，您已从另一处下线（" + way + "）。登录时间：" + time)
						// .setPositiveButton("我知道了", null)
						.show();
				// 设定定时器并在设定时间后使对话框关闭
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, 3 * 1000);
			}
		}
	}

	private void findView(View rootView) {
		textView = (TextView) rootView.findViewById(R.id.textView);
		mTabLinearLayout = (TabLinearLayout) rootView.findViewById(R.id.tabs);
		listView = (RefreshListView) rootView.findViewById(R.id.listview);
	}

	private void setView() {
		mTabLinearLayout.changeText(new String[] { "圈子动态", "朋友动态" });// , "随便看看"
		mTabLinearLayout.refresh(0);
		mTabLinearLayout.setOnTabClickListener(this);
		mTabLinearLayout.setVisibility(View.GONE);

		listView.setonLoadMoreListener(this);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadInfo(cut, true);
			}
		});
	}

	private void setInterGroupInfo() {
		if (infoAdapter == null) {
			try {
				// infos = ReadLocData.getLocTrends(this, "0");
				infos = LocalDataService.getInstense().getLocTrends(App.app,
						LocalDataService.TXT_INFOWALL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (infos == null)
				infos = new ArrayList<ResourceTrendBean>();
			infoAdapter = new InfoAdapter(this, infos, listView);
		}
		listView.setAdapter(infoAdapter);
		if (infos.size() == 0) {
			listView.refreshUI();
		}
		loadInfo(0, true);
	}

	private void setFriendGroupInfo() {
		if (friendAdapter == null) {
			// try {
			// // friends = ReadLocData.getLocTrends(this, "1");
			// friends = LocalDataService.getInstense().getLocTrends(App.app,
			// LocalDataService.TXT_FRIENDGROUP);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			if (friends == null) {
				friends = new ArrayList<ResourceTrendBean>();
			}
			friendAdapter = new InfoAdapter(this, friends, listView);
		}
		listView.setAdapter(friendAdapter);
		if (friends.size() == 0) {
			listView.refreshUI();
		}
		loadInfo(1, true);
	}

	private void setOpenGroupInfo() {
		if (openAdapter == null) {
			// try {
			// // opens = ReadLocData.getLocTrends(this, "2");
			// opens = LocalDataService.getInstense().getLocTrends(App.app,
			// LocalDataService.TXT_OPENGROUP);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			if (opens == null) {
				opens = new ArrayList<ResourceTrendBean>();
			}
			openAdapter = new InfoAdapter(this, opens, listView);
		}
		listView.setAdapter(openAdapter);
		if (opens.size() == 0) {
			listView.refreshUI();
		}
		loadInfo(2, true);
	}

	@Override
	protected void refresh() {
		L.d(TAG, "refresh " + isLoading);
		if (!isLoading) {
			listView.refreshUI();
			loadInfo(cut, true);
		}
		listView.setSelection(0);
	}

	private void loadInfo(int which, boolean isRefresh) {
		cut = which;
		stopTask();
		task = new loadInfoTask(which, isRefresh);
		task.execute();
	}

	@SuppressLint("NewApi")
	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	@SuppressLint("NewApi")
	class loadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public loadInfoTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;
			setLoadingState(true);
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
			textView.setVisibility(View.GONE);
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				result = loadDate(cur, isRefresh, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}

			if (null == mcResult) {
				// showTip(T.ErrStr);
			} else {
				if (1 != mcResult.getResultCode()) {
					// showTip(T.ErrStr);
				} else {
					L.d(TAG, "loadInfoTask cur=" + cur);
					switch (cur) {
					case 0:
						ArrayList<ResourceTrendBean> rInfos = (ArrayList<ResourceTrendBean>) mcResult
								.getResult();
						if (null == rInfos || rInfos.size() == 0) {
							if (infos.size() == 0) {
								textView.setVisibility(View.VISIBLE);
							} else {
								showTip("最后一页");
							}
						} else {
							if (isRefresh || null == trendId_info
									|| "".equals(trendId_info)
									|| "0".equals(trendId_info)) {
								infos.clear();

								infos.addAll(rInfos);
								infoAdapter.notifyDataSetChanged();
								listView.setSelection(0);
							} else {
								infos.addAll(rInfos);
								infoAdapter.notifyDataSetChanged();
							}
							// }
							trendId_info = String.valueOf(infos.get(
									infos.size() - 1).getTrendId());
						}
						break;
					case 1:
						ArrayList<ResourceTrendBean> rFriends = (ArrayList<ResourceTrendBean>) mcResult
								.getResult();
						if (null == rFriends || rFriends.size() == 0) {
							if (friends.size() == 0) {
								textView.setVisibility(View.VISIBLE);
							} else {
								showTip("最后一页");
							}
						} else {
							if (isRefresh || null == trendId_friend
									|| "".equals(trendId_friend)) {
								friends.clear();
							}
							friends.addAll(rFriends);
							friendAdapter.notifyDataSetChanged();
							ResourceTrendBean rBean = friends.get(friends
									.size() - 1);
							trendId_friend = rBean.getTrendId() + "";
							createTime = DateTimeUtil.getLongTime(rBean
									.getCreateTime()) + "";
						}
						break;
					case 2:
						ArrayList<ResourceTrendBean> rOpens = (ArrayList<ResourceTrendBean>) mcResult
								.getResult();
						if (null == rOpens || rOpens.size() == 0) {
							if (opens.size() == 0) {
								textView.setVisibility(View.VISIBLE);
							} else {
								showTip("最后一页");
							}
						} else {
							if (isRefresh || null == trendId_open
									|| "".equals(trendId_open)) {
								opens.clear();
							}
							opens.addAll(rOpens);
							openAdapter.notifyDataSetChanged();
							trendId_open = String.valueOf(opens.get(
									opens.size() - 1).getTrendId());
						}
						break;
					default:
						break;
					}
					L.d(TAG, "loadInfoTask isRefresh=" + isRefresh);
				}
			}
			try {
				isLoading = false;
				if (isRefresh) {
					listView.onRefreshComplete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_message).setVisible(true);
		menu.findItem(R.id.action_write).setVisible(true);
		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			setInterGroupInfo();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MsgActivity.msgActivity != null) {
			MsgActivity.msgActivity.finish();
		}

		isLoading = false;

		if (isNeedRefresh) {
			isNeedRefresh = false;
			cut = 0;
			mTabLinearLayout.refresh(0);
			try {
				loadInfo(cut, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!isLoadingData) {
			uploadContacts();
		}

		if (!notificationServiceIsStart()) {
			startNService(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!notificationServiceIsStart()) {
			startNService(false);
		}
	}

	private MCResult loadDate(int which, boolean refresh,
			@SuppressWarnings("rawtypes") AsyncTask a) throws Exception {
		MCResult mcResult = null;
		switch (which) {
		case 0:
			if (refresh) {
				trendId_info = "0";
			}
			mcResult = APIRequestServers.getAllGroupTrendList(App.app,
					trendId_info, "10", true, a);
			break;
		case 1:
			if (refresh) {
				trendId_friend = "0";
				createTime = "0";
			}
			mcResult = APIRequestServers.myFriendsTrends(App.app,
					trendId_friend, "10", createTime, true, a);
			break;
		case 2:
			if (refresh) {
				trendId_open = "0";
			}
			mcResult = APIRequestServers.openGroupResourceTrends(App.app,
					trendId_open, "10", true, a);
			break;
		default:
			break;
		}
		return mcResult;
	}

	@Override
	protected void onDestroy() {
		welHandler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		stopTask();
		infoWallActivity = null;
		super.onDestroy();
	}

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		listView.onRefreshComplete();
		// isLoading = false;
		switch (cut) {
		case 0:
			setInterGroupInfo();
			break;
		case 1:
			setFriendGroupInfo();
			break;
		case 2:
			setOpenGroupInfo();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			loadInfo(cut, false);
		}
	}

	/**
	 * 上传通信录线程-登录后
	 */
	private void uploadContacts() {
		isLoadingData = true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		final String newDate = format.format(new Date());

		final SettingActivity sa = new SettingActivity();
		// 有新版本点击稍后再说——软件不退出当日不在自动检测版本
		if (isReqVersion && !newDate.equals(versionDate))
			sa.chechVersion(false);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(15 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					String lastDate = App.app.share.getStringMessage(
							"NotificationSetup", "uploadcontacts_time", "");
					L.d(TAG, "uploadContacts lastDate=" + lastDate
							+ ",newDate=" + newDate);
					if (lastDate != null && lastDate.equals(newDate)) {
						isLoadingData = false;
					} else {
						uploadAndGetPhone(newDate);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!isReqVersion && !newDate.equals(versionDate))
					sa.chechVersion(false);

				sa.getBackupTime();
				isLoadingData = false;

				showWelTip();
			}
		}.start();
	}

	private void showWelTip() {
		try {
			MCResult mc = APIRequestServers.clientWelcomePic(App.app,
					ClientWelcomePicEnum.NEEDCLIENTWELCOMEPICGUIDE, 0);
			if ((Integer) mc.getResult() == 1) {
				welHandler.sendEmptyMessage(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler welHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Builder builder = new Builder(InfoWallActivity.this)
					.setTitle("提示").setMessage("您可以为App设置一个专属的企业圈子欢迎页啦。")
					.setPositiveButton("去设置", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							LogicUtil.enter(InfoWallActivity.this,
									WelSettingActivity.class, null, false);
						}
					});
			AlertDialog ad = builder.create();
			ad.setCanceledOnTouchOutside(true);
			try {
				ad.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	public void uploadAndGetPhone(String newDate) {
		if (newDate == null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			newDate = format.format(new Date());
		}

		try {
			MCResult response = APIRequestServers.uploadPhoneBook(App.app,
					null, null, null);
			if (response != null && response.getResultCode() == 1) {
				try {
					App.app.share.saveStringMessage("NotificationSetup",
							"uploadcontacts_time", newDate);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					APIRequestServers.getaddressBook(App.app);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 最新获取的头像
				HashMap<String, String[]> map = new ContactsBookService(App.app)
						.getContactHeads();
				if (map != null && map.size() > 0) {
					UpdateContactHeadService contactHeadService = new UpdateContactHeadService(
							App.app);
					// 已更新过的头像
					HashMap<String, String> mapLast = contactHeadService
							.getContactHeads();
					boolean b = mapLast != null;
					for (Iterator<?> iterator = map.keySet().iterator(); iterator
							.hasNext();) {
						String key = (String) iterator.next();
						String value = map.get(key)[0];
						value = ThumbnailImageUrl.getThumbnailHeadUrl(value,
								HeadSizeEnum.THREE_HUNDRED_AND_SIXTY);
						if (value != null && !"".equals(value)) {
							if (b && mapLast.containsKey(key)) {
								if (!value.equals(mapLast.get(key))) {
									try {
										ContactsUtil.changeMemberHead(App.app,
												map.get(key)[1], key, value);
										contactHeadService.updateHead(key,
												value);
									} catch (Exception e) {
										e.printStackTrace();
									} catch (OutOfMemoryError e) {
									}
								}
							} else {
								try {
									ContactsUtil.changeMemberHead(App.app,
											map.get(key)[1], key, value);
									contactHeadService.save(key, value);
								} catch (Exception e) {
									e.printStackTrace();
								} catch (OutOfMemoryError e) {
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		if (resultCode == RESULT_OK) {
			if (requestCode == 10) {
				int position = data.getIntExtra("position", -1);
				if (position != -1) {
					ResourceTrendBean tBean = (ResourceTrendBean) data
							.getSerializableExtra("Trend");
					if (tBean != null) {
						switch (cut) {
						case 0:
							infos.remove(position);
							infos.add(position, tBean);
							infoAdapter.notifyDataSetChanged();
							break;
						case 1:
							friends.remove(position);
							friends.add(position, tBean);
							friendAdapter.notifyDataSetChanged();
							break;
						case 2:
							opens.remove(position);
							opens.add(position, tBean);
							openAdapter.notifyDataSetChanged();
							break;
						default:
							break;
						}
					}
				}
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

}
