package com.datacomo.mc.spider.android;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.CloudFileAdapter;
import com.datacomo.mc.spider.android.adapter.CloudFileLeaguerAdapter;
import com.datacomo.mc.spider.android.adapter.CloudFileMberAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.dialog.CreateFile;
import com.datacomo.mc.spider.android.enums.FileListTypeEnum;
import com.datacomo.mc.spider.android.enums.UploadMethodEnum;
import com.datacomo.mc.spider.android.net.APIFileRequestServers;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileInfoBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapShareLeaguerBean;
import com.datacomo.mc.spider.android.receiver.BootBroadcastReceiver;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.SearchBar;
import com.datacomo.mc.spider.android.view.SearchBar.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar.OnSearchListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class CloudFileActivity extends BasicMenuActivity implements
		OnTabClickListener, OnRefreshListener, OnLoadMoreListener,
		OnClearListener, OnSearchListener {
	private static final String TAG = "CloudFileActivity";

	private String[] tabContent = new String[] { "全部文件", "我上传的", "分享文件" };
	private int cut;
	private TextView tv_num;
	private RefreshListView listView;
	private SearchBar sBar;
	private TabLinearLayout tabs;
	private boolean isLoading, isSearchState;
	public static boolean isNeedRefresh = false;
	private ArrayList<FileInfoBean> allFiles, myFiles;
	private ArrayList<ShareLeaguerBean> mbersResult;
	private ArrayList<FileShareLeaguerBean> mbers;
	private CloudFileAdapter allAdapter, myAdapter;
	private CloudFileLeaguerAdapter lAdapter;
	private CloudFileMberAdapter mAdapter;
	private loadFileTask task;
	private String key = "";

	private CreateFile cFile;
	private boolean flag_friendSharing;
	private boolean flag_groupSharing;

	private RefershBroadcastReceiver receiver;
	private IntentFilter intentFilter;
	public static FileInfoBean chosenBean;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		chosenBean = null;
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "6");
		L.d(TAG, "onStart");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_FILES;
		titleName = "云文件";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(
				R.layout.layout_files_main, null);
		fl.addView(rootView);

		findView();
		setView();

		receiver = new RefershBroadcastReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(BootBroadcastReceiver.REFERSH_ACTION);
		registerReceiver(receiver, intentFilter);
	}

	private void findView() {
		tv_num = (TextView) findViewById(R.id.file_num_tv);
		tv_num.setText("共有" + num0 + "个文件");
		listView = (RefreshListView) findViewById(R.id.listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
		sBar = (SearchBar) findViewById(R.id.search_file);
		sBar.setOnClearListener(this);
		sBar.setOnSearchListener(this);
		sBar.setHint("搜索文件");
		sBar.setVisibility(View.GONE);
		tabs = (TabLinearLayout) findViewById(R.id.tabs);
		tabs.changeText(tabContent);
		tabs.refresh(0);
		tabs.setOnTabClickListener(this);
	}

	private void setView() {
		try {
			allFiles = (ArrayList<FileInfoBean>) LocalDataService.getInstense()
					.getLocFiles(this, LocalDataService.TXT_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == allFiles)
			allFiles = new ArrayList<FileInfoBean>();
		allAdapter = new CloudFileAdapter(this, allFiles);
		listView.setAdapter(allAdapter);

		try {
			myFiles = (ArrayList<FileInfoBean>) LocalDataService.getInstense()
					.getLocFiles(this, LocalDataService.TXT_FILE_MY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == myFiles)
			myFiles = new ArrayList<FileInfoBean>();
		myAdapter = new CloudFileAdapter(this, myFiles);

		try {
			mbers = (ArrayList<FileShareLeaguerBean>) LocalDataService
					.getInstense().getLocFilesMember(this,
							LocalDataService.TXT_FILE_MEMBER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == mbers)
			mbers = new ArrayList<FileShareLeaguerBean>();
		lAdapter = new CloudFileLeaguerAdapter(this, mbers, listView);

		mbersResult = new ArrayList<ShareLeaguerBean>();
		mAdapter = new CloudFileMberAdapter(this, mbersResult, listView);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (isNeedRefresh) {
			isNeedRefresh = false;
			load(true);
		}

		registerReceiver(receiver, intentFilter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);

		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);
		searchView
				.setOnSearchClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
								.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
						if (mQueryTextView.isShown()
								&& "".equals(mQueryTextView.getText()
										.toString())) {
							cv.setVisibility(View.GONE);
							showBack();
						}
					}
				});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				key = s;
				isSearchState = true;
				load(true);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if ("".equals(s)) {
					cv.setVisibility(View.GONE);
				} else {
					cv.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {

			@Override
			public boolean onClose() {
				isSearchState = false;
				load(true);
				showMenu();
				return false;
			}
		});

		this.menu = menu;
		if (isFrist) {
			isFrist = false;
			load(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_files).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		menu.findItem(R.id.action_message).setVisible(drawerOpen);
		menu.findItem(R.id.action_write).setVisible(drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				isSearchState = false;
				load(true);
				return false;
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_files:
			if (cFile == null) {
				cFile = new CreateFile(this);
			}
			cFile.createDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				isSearchState = false;
				load(true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d(TAG, "onActivityResult requestCode=" + requestCode + ",resultCode="
				+ resultCode);
		String filePath = null;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CreateFile.IMAGE:
			case CreateFile.VIDEO:
			case CreateFile.AUDIO:
				// case CreateFile.TEXT:
				if (data != null) {
					Uri uri = data.getData();
					L.i(TAG, "onActivityResult uri=" + uri);
					if (uri != null) {
						Cursor cursor = getContentResolver().query(
								uri,
								new String[] { "_data", "_display_name",
										"_size", "mime_type" }, null, null,
								null);
						if (cursor != null) {
							cursor.moveToFirst();
							filePath = cursor.getString(0);
							cursor.close();
						} else {
							filePath = uri.getPath();
						}
					} else {
						File saveFile = new File(ConstantUtil.CAMERA_PATH);
						File picture = new File(saveFile, cFile.pictureName);
						uri = Uri.fromFile(picture);
						filePath = uri.getPath();
					}
				} else {
					L.d(TAG, "onActivityResult data=null");
					File saveFile = new File(ConstantUtil.CAMERA_PATH);
					File picture = new File(saveFile, cFile.pictureName);
					Uri uri = Uri.fromFile(picture);
					filePath = uri.getPath();
				}
				break;
			case CreateFile.OTHAR:
				filePath = data.getStringExtra("filePath");
				break;
			case FriendsChooserActivity.RESCODE:
				String[] shareFriendIds = data.getStringArrayExtra("ids");
				if (null == shareFriendIds || 0 == shareFriendIds.length) {
					// showTip("您没有选择任何朋友！");
				} else if (flag_friendSharing) {
					showTip("正在分享！");
				} else {
					new ShareFriendTask(shareFriendIds).execute();
				}
				return;
			case GroupsChooserActivity.RESCODE:
				String[] shareGroupIds = data.getStringArrayExtra("ids");
				if (null == shareGroupIds || 0 == shareGroupIds.length) {
					// showTip("您没有选择任何圈子！");
				} else if (flag_groupSharing) {
					showTip("正在分享！");
				} else {
					new ShareGroupTask(shareGroupIds).execute();
				}
				return;
			case 10:
				int position = data.getIntExtra("position", -1);
				if (position != -1) {
					int isChange = data.getIntExtra("isChange", 0);
					if (cut == 0) {
						allFiles.remove(position);
						if (isChange == 1) {
							FileInfoBean fileInfoBean = (FileInfoBean) data
									.getSerializableExtra("fileInfoBean");
							allFiles.add(position, fileInfoBean);
						}
						allAdapter.notifyDataSetChanged();
					} else if (cut == 1) {
						myFiles.remove(position);
						if (isChange == 1) {
							FileInfoBean fileInfoBean = (FileInfoBean) data
									.getSerializableExtra("fileInfoBean");
							myFiles.add(position, fileInfoBean);
						}
						myAdapter.notifyDataSetChanged();
					}
				}
				return;
			}
			L.i(TAG, "onActivityResult filePath=" + filePath);
			if (filePath != null && !"".equals(filePath)) {
				File file = new File(filePath);
				if (file.exists()) {
					if (ConstantUtil.uploadingList.contains(filePath)) {
						showTip("该文件正在上传中");
					} else {
						ConstantUtil.uploadingList.add(filePath);
						APIRequestServers.uploadFile(this, file,
								UploadMethodEnum.UPLOADFILE, null);
					}
				} else {
					showTip("找不到文件，请换其他方式");
				}
			}
		}
	}

	String num0 = " ", num1 = " ", num2 = " ", num3 = " ";

	@Override
	public void onTabClick(View tab) {
		cut = (Integer) tab.getTag();
		cleanTabNum();
		switch (cut) {
		case 0:
			tv_num.setText("共有" + num0 + "个文件");
			sBar.setHint("搜索文件");
			listView.setAdapter(allAdapter);
			break;
		case 1:
			tv_num.setText("共上传" + num1 + "个文件");
			sBar.setHint("搜索文件");
			listView.setAdapter(myAdapter);
			break;
		case 2:
			tv_num.setText("共与" + num3 + "人分享过");
			sBar.setHint("搜索分享人");
			listView.setAdapter(lAdapter);
			break;
		default:
			break;
		}
		load(true);
	}

	@Override
	public void onSearch(String keyWords) {
		isSearchState = true;
		key = getKeyWords();
		load(true);
	}

	@Override
	public void onClear(String keyWords) {
		isSearchState = false;
		load(true);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			load(false);
		}
	}

	private String getKeyWords() {
		return sBar.getKeyWords();
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
			setLoadingState(true);
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
				if (!isSearchState) {
					result = loadDate();
				} else {
					result = loadSearchDate();
				}
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
					mc = APIFileRequestServers.getFileList(
							CloudFileActivity.this, FileListTypeEnum.ALL_FILE,
							startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = myFiles.size();
				}
				try {
					mc = APIFileRequestServers.getFileList(
							CloudFileActivity.this, FileListTypeEnum.MY_FILE,
							startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = mbers.size();
				}
				try {
					mc = APIFileRequestServers.shareFilesByMember(
							CloudFileActivity.this, startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mc;
		}

		private MCResult loadSearchDate() {
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
					mc = APIFileRequestServers.searchAllFileList(
							CloudFileActivity.this, key,
							FileListTypeEnum.ALL_FILE, startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = myFiles.size();
				}
				try {
					mc = APIFileRequestServers.searchAllFileList(
							CloudFileActivity.this, key,
							FileListTypeEnum.MY_FILE, startRecord, 30, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = mbersResult.size();
				}
				try {
					mc = APIFileRequestServers
							.searchShareRelationMembers(CloudFileActivity.this,
									key, startRecord, 30, false);
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
			setLoadingState(false);
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
						MapFileInfoBean request0 = (MapFileInfoBean) mcResult
								.getResult();
						num0 = request0.getTOTAL_NUM() + "";
						ArrayList<FileInfoBean> result0 = (ArrayList<FileInfoBean>) request0
								.getFILE_LIST();
						if (null == result0 || result0.size() == 0) {
							if (allFiles.size() > 0) {
								showTip("亲，最后一个文件了哦!");
							} else {
								if (isSearchState) {
									showTip("没有搜索到相关文件!");
								} else {
									showTip("您还没有任何文件!");
								}
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

							// if (num0 > 0) {
							// tabs.changeSpecilText(tabContent[0] + "("
							// + num0 + ")", 0);
							// } else {
							// tabs.changeSpecilText(tabContent[0], 0);
							// }
							// tabs.changeSpecilText(tabContent[1], 1);
							// tabs.changeSpecilText(tabContent[2], 2);
						}
						break;
					case 1:
						if (isRefresh) {
							myFiles.clear();
						}
						MapFileInfoBean request1 = (MapFileInfoBean) mcResult
								.getResult();
						num1 = request1.getTOTAL_NUM() + "";
						ArrayList<FileInfoBean> result1 = (ArrayList<FileInfoBean>) request1
								.getFILE_LIST();
						if (null == result1 || result1.size() == 0) {
							if (myFiles.size() > 0) {
								showTip("亲，最后一个文件了哦!");
							} else {
								if (isSearchState) {
									showTip("没有搜索到相关文件!");
								} else {
									showTip("您还没有上传过文件!");
								}
							}
						} else {
							myFiles.addAll(result1);
							myAdapter.notifyDataSetChanged();

							// if (num1 > 0) {
							tv_num.setVisibility(View.VISIBLE);
							tv_num.setText("共上传" + num1 + "个文件");
							// } else {
							// tv_num.setVisibility(View.GONE);
							// }
							// if (num1 > 0) {
							// tabs.changeSpecilText(tabContent[1] + "("
							// + num1 + ")", 1);
							// } else {
							// tabs.changeSpecilText(tabContent[1], 1);
							// }
							// tabs.changeSpecilText(tabContent[0], 0);
							// tabs.changeSpecilText(tabContent[2], 2);
						}
						break;
					case 2:
						if (isSearchState) {
							if (isRefresh) {
								listView.setAdapter(mAdapter);
								mbersResult.clear();
							}
							MapShareLeaguerBean request2 = (MapShareLeaguerBean) mcResult
									.getResult();
							num2 = request2.getTOTAL_NUM() + "";
							ArrayList<ShareLeaguerBean> result2 = (ArrayList<ShareLeaguerBean>) request2
									.getSHARE_MEMBER_LIST();
							if (null == result2 || result2.size() == 0) {
								if (mbersResult.size() > 0) {
									showTip("亲，最后一个人了哦!");
								} else {
									showTip("没有搜到相关分享记录!");
								}
							} else {
								mbersResult.addAll(result2);
								mAdapter.notifyDataSetChanged();

								// if (num2 > 0) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("共分享" + num2 + "个文件");
								// } else {
								// tv_num.setVisibility(View.GONE);
								// }

								// if (num2 > 0) {
								// tabs.changeSpecilText(tabContent[2] + "("
								// + num2 + ")", 2);
								// } else {
								// tabs.changeSpecilText(tabContent[2], 2);
								// }
								// tabs.changeSpecilText(tabContent[0], 0);
								// tabs.changeSpecilText(tabContent[1], 1);
							}
						} else if (!isSearchState) {
							if (isRefresh) {
								listView.setAdapter(lAdapter);
								mbers.clear();
							}
							MapFileShareLeaguerBean request2 = (MapFileShareLeaguerBean) mcResult
									.getResult();
							num3 = request2.getLISTSIZE() + "";
							ArrayList<FileShareLeaguerBean> result2 = (ArrayList<FileShareLeaguerBean>) request2
									.getFILESHARELIST();
							if (null == result2 || result2.size() == 0) {
								if (mbers.size() > 0) {
									showTip("亲，最后一个人了哦!");
								} else {
									showTip("没有分享记录!");
								}
							} else {
								mbers.addAll(result2);
								lAdapter.notifyDataSetChanged();

								// if (num2 > 0) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("共与" + num3 + "人分享过");
								// } else {
								// tv_num.setVisibility(View.GONE);
								// }
								// if (num2 > 0) {
								// tabs.changeSpecilText(tabContent[2] + "("
								// + num2 + ")", 2);
								// } else {
								// tabs.changeSpecilText(tabContent[2], 2);
								// }
								// tabs.changeSpecilText(tabContent[0], 0);
								// tabs.changeSpecilText(tabContent[1], 1);
							}
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
			isSearchState = false;
		}
	}

	private void cleanTabNum() {
		for (int i = 0; i < 2; i++) {
			tabs.changeSpecilText(tabContent[i], i);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLoading = false;
		closeMenuPage();
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

	public void closeMenuPage() {
		// slide.close();
	}

	@Override
	public void onRefresh() {
		load(true);
	}

	public class RefershBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BootBroadcastReceiver.REFERSH_ACTION.equals(action)) {
				if (cut == 0 || cut == 1) {
					load(true);
				}
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
				mc = APIFileRequestServers.shareFile(CloudFileActivity.this,
						chosenBean.getFileId(), shareFriendIds, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				new AlertDialog.Builder(CloudFileActivity.this)
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
						CloudFileActivity.this, chosenBean.getFileId(),
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
				new AlertDialog.Builder(CloudFileActivity.this)
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

	@Override
	protected void refresh() {
		load(true);
	}
}
