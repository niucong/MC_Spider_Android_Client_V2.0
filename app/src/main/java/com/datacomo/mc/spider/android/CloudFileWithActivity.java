package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.CloudFileByLeaguerAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ShareLeaguerRecord;
import com.datacomo.mc.spider.android.net.been.map.MapShareLeaguerRecord;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;

public class CloudFileWithActivity extends BasicActionBarActivity implements
		OnTabClickListener, OnRefreshListener, OnLoadMoreListener {
	// private static final String TAG = "CloudFileWithActivity";
	private String[] tabContent = new String[] { "全部分享", "向您分享", "您分享的" };
	// private MenuPage menus;
	// private SlideMenuView slide;
	private int cut;
	private TextView tv_num;
	private RefreshListView listView;
	private TabLinearLayout tabs;
	private boolean isLoading;
	private ArrayList<ShareLeaguerRecord> allFiles, othersFiles, myFiles;
	private CloudFileByLeaguerAdapter allAdapter, otherAdapter, myAdapter;
	private loadFileTask task;
	private String id, mBerName;
	public static ShareLeaguerRecord chosenBean;
	private boolean flag_friendSharing;
	private boolean flag_groupSharing;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		chosenBean = null;
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_files_main);

		findView();
		setView();
	}

	String num0 = " ", num1 = " ", num2 = " ";

	private void findView() {
		findViewById(R.id.search_file).setVisibility(View.GONE);
		tv_num = (TextView) findViewById(R.id.file_num_tv);
		tv_num.setText("共有" + num0 + "个文件");
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		tabs = (TabLinearLayout) findViewById(R.id.tabs);
		tabs.changeText(tabContent);
		tabs.refresh(0);
		tabs.setOnTabClickListener(this);
	}

	private void setView() {
		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		if (null == b || "".equals(b)) {
			showTip(T.ErrStr);
			finish();
		}
		mBerName = b.getString("name");
		if (null == mBerName || "".equals(mBerName)) {
			showTip(T.ErrStr);
			finish();
		}
		// setTitle(mBerName, R.drawable.title_fanhui, true, null,
		// R.drawable.title_file);
		ab.setTitle(mBerName);
		allFiles = new ArrayList<ShareLeaguerRecord>();
		othersFiles = new ArrayList<ShareLeaguerRecord>();
		myFiles = new ArrayList<ShareLeaguerRecord>();
		allAdapter = new CloudFileByLeaguerAdapter(this, allFiles);
		otherAdapter = new CloudFileByLeaguerAdapter(this, othersFiles);
		myAdapter = new CloudFileByLeaguerAdapter(this, myFiles);
		listView.setAdapter(allAdapter);
		load(true);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_refresh).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			load(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		switch (cut) {
		case 0:
			tv_num.setText("共有" + num0 + "个文件");
			listView.setAdapter(allAdapter);
			break;
		case 1:
			tv_num.setText("共有" + num1 + "个文件");
			listView.setAdapter(otherAdapter);
			break;
		case 2:
			tv_num.setText("共有" + num2 + "个文件");
			listView.setAdapter(myAdapter);
			break;
		default:
			break;
		}
		if (0 == getOriDate().size()) {
			load(true);
		}
	}

	private ArrayList<ShareLeaguerRecord> getOriDate() {
		switch (cut) {
		case 0:
			return allFiles;
		case 1:
			return othersFiles;
		case 2:
			return myFiles;
		}
		return null;
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			load(false);
		}
	}

	private void load(boolean isRefresh) {
		stopLoad();
		task = new loadFileTask(cut, isRefresh);
		task.execute();
	}

	private void stopLoad() {
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
			listView.onRefreshComplete();
		}

	}

	class loadFileTask extends AsyncTask<Void, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public loadFileTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;
			if (isRefresh) {
				listView.refreshing();
			} else {
				listView.showLoadFooter();
			}
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult result = null;
			try {
				result = loadDate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		private MCResult loadDate() {
			MCResult mc = null;
			int startRecord;
			switch (cur) {
			case 0:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = allFiles.size();
				}
				try {
					mc = APIFileRequestServers.shareFilesByMemberId(
							CloudFileWithActivity.this, id,
							FileListTypeEnum.ALL_FILE, startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = othersFiles.size();
				}
				try {
					mc = APIFileRequestServers.shareFilesByMemberId(
							CloudFileWithActivity.this, id,
							FileListTypeEnum.RECEIVE_FILE, startRecord, 30,
							false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = myFiles.size();
				}
				try {
					mc = APIFileRequestServers
							.shareFilesByMemberId(CloudFileWithActivity.this,
									id, FileListTypeEnum.SHARE_FILE,
									startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			if (isRefresh) {
				listView.refreshed();
			} else {
				listView.showFinishLoadFooter();
			}
			if (cur != cut || this.isCancelled()) {
				showTip(T.ErrStr);
				return;
			} else {
				if (null == mcResult || mcResult.getResultCode() != 1) {
					showTip(T.ErrStr);
				} else {
					switch (cur) {
					case 0:
						if (isRefresh) {
							allFiles.clear();
						}
						MapShareLeaguerRecord request0 = (MapShareLeaguerRecord) mcResult
								.getResult();
						num0 = request0.getTOTAL_NUM() + "";
						ArrayList<ShareLeaguerRecord> result0 = (ArrayList<ShareLeaguerRecord>) request0
								.getRECORD_LIST();
						if (null == result0 || result0.size() == 0) {
							if (!isRefresh) {
								showTip("亲，最后一个文件了哦!");
							} else {
								showTip("暂无数据");
							}
						} else {
							allFiles.addAll(result0);
							allAdapter.notifyDataSetChanged();

							// if (num0 > 0) {
							tv_num.setVisibility(View.VISIBLE);
							tv_num.setText("共有" + num0 + "个文件");
							// } else {
							// tv_num.setVisibility(View.GONE);
							// }
							// tabs.changeSpecilText(tabContent[0] + "(" + num0
							// + ")", 0);
						}
						break;
					case 1:
						if (isRefresh) {
							othersFiles.clear();
						}
						MapShareLeaguerRecord request1 = (MapShareLeaguerRecord) mcResult
								.getResult();
						num1 = request1.getTOTAL_NUM() + "";
						ArrayList<ShareLeaguerRecord> result1 = (ArrayList<ShareLeaguerRecord>) request1
								.getRECORD_LIST();
						if (null == result1 || result1.size() == 0) {
							if (!isRefresh) {
								showTip("亲，最后一个文件了哦!");
							} else {
								showTip("暂无数据");
							}
						} else {
							othersFiles.addAll(result1);
							otherAdapter.notifyDataSetChanged();
							// if (num1 > 0) {
							tv_num.setVisibility(View.VISIBLE);
							tv_num.setText("共有" + num1 + "个文件");
							// } else {
							// tv_num.setVisibility(View.GONE);
							// }
							// tabs.changeSpecilText(tabContent[1] + "(" + num1
							// + ")", 1);
						}
						break;
					case 2:
						if (isRefresh) {
							myFiles.clear();
						}
						MapShareLeaguerRecord request2 = (MapShareLeaguerRecord) mcResult
								.getResult();
						num2 = request2.getTOTAL_NUM() + "";
						ArrayList<ShareLeaguerRecord> result2 = (ArrayList<ShareLeaguerRecord>) request2
								.getRECORD_LIST();
						if (null == result2 || result2.size() == 0) {
							if (!isRefresh) {
								showTip("亲，最后一个文件了哦!");
							} else {
								showTip("暂无数据");
							}
						} else {
							myFiles.addAll(result2);
							myAdapter.notifyDataSetChanged();
							// if (num2 > 0) {
							tv_num.setVisibility(View.VISIBLE);
							tv_num.setText("共有" + num2 + "个文件");
							// } else {
							// tv_num.setVisibility(View.GONE);
							// }
							// tabs.changeSpecilText(tabContent[2] + "(" + num2
							// + ")", 2);
						}
						break;
					default:
						break;
					}
				}
			}
			if (isRefresh) {
				listView.setSelection(0);
			}
			listView.onRefreshComplete();
			isLoading = false;
		}
	}

	@Override
	public void onRefresh() {
		load(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case FriendsChooserActivity.RESCODE:
				String[] shareFriendIds = data.getStringArrayExtra("ids");
				if (null == shareFriendIds || 0 == shareFriendIds.length) {
					// showTip("您没有选择任何朋友！");
				} else if (flag_friendSharing) {
					showTip("正在分享！");
				} else {
					new ShareFriendTask(shareFriendIds).execute();
				}
				break;

			case GroupsChooserActivity.RESCODE:
				String[] shareGroupIds = data.getStringArrayExtra("ids");
				if (null == shareGroupIds || 0 == shareGroupIds.length) {
					// showTip("您没有选择任何圈子！");
				} else if (flag_groupSharing) {
					showTip("正在分享！");
				} else {
					new ShareGroupTask(shareGroupIds).execute();
				}
				break;
			}
		}
	}

	class ShareFriendTask extends AsyncTask<Object, Integer, MCResult> {
		String[] shareFriendIds;

		public ShareFriendTask(String[] ids) {
			shareFriendIds = ids;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult mc = null;
			flag_friendSharing = true;
			try {
				mc = APIFileRequestServers.shareFile(
						CloudFileWithActivity.this, chosenBean.getFileId(),
						shareFriendIds, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(CloudFileWithActivity.this)
						.setTitle("分享失败!")
						.setPositiveButton("重新分享", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new ShareFriendTask(shareFriendIds).execute();
							}
						}).setNegativeButton("取消", null).show();
			} else {
				showTip("已分享！");
				CloudFileActivity.isNeedRefresh = true;
				// new LoadInfoTask(CloudFileActivity.this,
				// chosenBean.getFileId() + "").execute();
			}
			flag_friendSharing = false;
		}

	}

	class ShareGroupTask extends AsyncTask<Object, Integer, MCResult> {
		String[] shareGroupIds;

		public ShareGroupTask(String[] ids) {
			shareGroupIds = ids;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult mc = null;
			flag_groupSharing = true;
			try {
				mc = APIFileRequestServers.shareFileToGroup(
						CloudFileWithActivity.this, chosenBean.getFileId(),
						shareGroupIds, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(CloudFileWithActivity.this)
						.setTitle("分享失败!")
						.setPositiveButton("重新分享", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new ShareGroupTask(shareGroupIds).execute();
							}
						}).setNegativeButton("取消", null).show();
			} else {
				InfoWallActivity.isNeedRefresh = true;
				showTip("已分享！");
			}
			flag_groupSharing = false;
		}

	}
}
