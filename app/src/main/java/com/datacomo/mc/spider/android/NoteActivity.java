package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.CloudNoteBookAdapter;
import com.datacomo.mc.spider.android.adapter.CloudNoteListAdapter;
import com.datacomo.mc.spider.android.adapter.CloudNoteMberAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.db.NoteCreateService;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.MapNoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean;
import com.datacomo.mc.spider.android.net.been.note.MapShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.net.been.note.NoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.service.LocalDataService;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;
import com.umeng.analytics.MobclickAgent;

public class NoteActivity extends BasicMenuActivity implements
		OnTabClickListener, OnRefreshListener, OnLoadMoreListener {
	private static final String TAG = "NoteActivity";

	private String[] tabContent = new String[] { "所有", "笔记本", "分享的", "星标" };
	private TabLinearLayout mTabLinearLayout;

	private boolean isLoading, searchState;
	private LoadInfoTask task;
	private int cut;
	public static boolean needRefresh = true;

	private String keyWord;

	private MapNoteInfoBean map_all, map_star;
	public ArrayList<NoteInfoBean> allNotes;
	private ArrayList<NoteInfoBean> starNotes, searchNotes;
	private CloudNoteListAdapter allAdapter, starAdapter, searchAdapter;

	private ArrayList<MapShareLeaguerInfoBean> shareLeaguers, searchShares;
	private CloudNoteMberAdapter leagerAdapter, searchShareAdapter;

	private MapNoteBookBean map_book;
	private ArrayList<NoteBookBean> noteBooks, searchBooks;
	private CloudNoteBookAdapter bookAdapter, searchBookAdapter;

	private LinearLayout ll_no, ll_no_sharestar;
	private TextView tv_num, tv_no, tv_no_new;
	private RefreshListView rlv;
	private ImageView iv_no_head, iv_no_mid, iv_no_sharesatr;

	private boolean fristShare = true;
	private boolean fristBook = true;
	private boolean fristStar = true;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		MobclickAgent.onEvent(this, "7");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		items = Items.ITEM_NOTES;
		titleName = "云笔记";
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		View rootView = LayoutInflater.from(this).inflate(R.layout.layout_note,
				null);
		fl.addView(rootView);

		findViews();
		setView();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.note_no_tv_new:
			LogicUtil.enter(NoteActivity.this, NoteCreateActivity.class, null,
					10);
			break;
		default:
			break;
		}
	}

	private void setAll() {
		rlv.setAdapter(allAdapter);
		if (allNotes.size() == 0) {
			tv_num.setVisibility(View.GONE);
			tv_num.setText("共有0个笔记");
			loadInfo(cut, true);
		} else {
			if (map_all != null) {
				tv_num.setVisibility(View.VISIBLE);
				if (beans != null) {
					tv_num.setText("共有"
							+ (map_all.getALLNOTENUM() + beans.size()) + "个笔记");
				} else {
					tv_num.setText("共有" + map_all.getALLNOTENUM() + "个笔记");
				}
			} else {
				tv_num.setText("共有" + beans.size() + "个笔记");
			}
		}
	}

	private void setBook() {
		rlv.setAdapter(bookAdapter);
		if (noteBooks.size() == 0) {
			tv_num.setVisibility(View.GONE);
			tv_num.setText("共有0个笔记本");
			loadInfo(cut, true);
		} else {
			if (map_book != null) {
				tv_num.setVisibility(View.VISIBLE);
				tv_num.setText("共有" + map_book.getNOTEBOOKNUM() + "个笔记本");
				if (fristBook) {
					fristBook = false;
					loadInfo(cut, true);
				}
			} else {
				tv_num.setVisibility(View.INVISIBLE);
				loadInfo(cut, true);
			}
		}
	}

	private void setShare() {
		rlv.setAdapter(leagerAdapter);
		if (shareLeaguers.size() == 0) {
			tv_num.setVisibility(View.GONE);
			loadInfo(cut, true);
		} else {
			// if (map_start != null) {
			// tv_num.setText("共有" + map_start.getALLNOTENUM() + "个笔记");
			// tv_num.setVisibility(View.VISIBLE);
			// } else {
			tv_num.setVisibility(View.GONE);
			if (fristShare) {
				fristShare = false;
				loadInfo(cut, true);
			}
			// }
		}
	}

	private void setStar() {
		rlv.setAdapter(starAdapter);
		if (starNotes.size() == 0) {
			tv_num.setVisibility(View.GONE);
			tv_num.setText("共有0个笔记");
			loadInfo(cut, true);
		} else {
			if (map_star != null) {
				tv_num.setVisibility(View.VISIBLE);
				tv_num.setText("共有" + map_star.getALLNOTENUM() + "个笔记");
				if (fristStar) {
					fristStar = false;
					loadInfo(cut, true);
				}
			} else {
				tv_num.setVisibility(View.INVISIBLE);
				loadInfo(cut, true);
			}
		}
	}

	private void findViews() {
		mTabLinearLayout = (TabLinearLayout) findViewById(R.id.tabs);

		tv_num = (TextView) findViewById(R.id.note_num_tv);
		rlv = (RefreshListView) findViewById(R.id.note_listview);
		rlv.setDivider(getResources().getDrawable(R.drawable.nothing));
		rlv.setonRefreshListener(this);
		rlv.setonLoadMoreListener(this);

		ll_no = (LinearLayout) findViewById(R.id.note_no);
		ll_no_sharestar = (LinearLayout) findViewById(R.id.note_no_sharestar);
		iv_no_head = (ImageView) findViewById(R.id.note_no_sharestar_head);
		iv_no_mid = (ImageView) findViewById(R.id.note_no_sharestar_mid);
		iv_no_sharesatr = (ImageView) findViewById(R.id.note_no_sharestar_icon);
		tv_no = (TextView) findViewById(R.id.note_no_tv);
		tv_no_new = (TextView) findViewById(R.id.note_no_tv_new);
		tv_no_new.setOnClickListener(this);
	}

	public ArrayList<NoteDraftBean> beans;

	private void setView() {
		mTabLinearLayout.changeText(tabContent);
		mTabLinearLayout.refresh(0);
		mTabLinearLayout.setOnTabClickListener(this);

		allNotes = new ArrayList<NoteInfoBean>();
		try {
			map_all = LocalDataService.getInstense().getLocNotes(this,
					LocalDataService.TXT_ALLDIARY);
			if (map_all != null) {
				allNotes.addAll((ArrayList<NoteInfoBean>) map_all
						.getNOTE_LIST());
				tv_num.setText("共有" + map_all.getALLNOTENUM() + "个笔记");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		beans = NoteCreateService.getService(this).queryNotes();
		if (beans != null && beans.size() > 0) {
			for (NoteDraftBean nb : beans) {
				NoteInfoBean bean = new NoteInfoBean();
				bean.setNoteId(nb.getNoteId());
				bean.setNoteTitle(nb.getTitle());
				bean.setNoteContent(nb.getContent());
				bean.setUpdateTime(nb.getTime() + "");
				try {
					JSONArray array = new JSONArray(nb.getFilePaths());
					int j = 0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInt("type") == 1) {
							j++;
						} else {
							bean.setFirstPhotoUrl("");
							bean.setFirstPhotoPath(json.getString("path"));
						}
					}
					L.i(TAG, "setView j=" + j);
					bean.setAdjunctNum(j);
				} catch (Exception e) {
					e.printStackTrace();
				}
				allNotes.add(0, bean);
			}

			if (map_all != null) {
				tv_num.setText("共有" + (map_all.getALLNOTENUM() + beans.size())
						+ "个笔记");
			} else {
				tv_num.setText("共有" + beans.size() + "个笔记");
			}
		}

		allAdapter = new CloudNoteListAdapter(this, allNotes);
		rlv.setAdapter(allAdapter);

		try {
			map_star = LocalDataService.getInstense().getLocNotes(this,
					LocalDataService.TXT_OTHERDIARY);
			if (map_star != null)
				starNotes = (ArrayList<NoteInfoBean>) map_star.getNOTE_LIST();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (starNotes == null) {
			starNotes = new ArrayList<NoteInfoBean>();
		}
		starAdapter = new CloudNoteListAdapter(this, starNotes);

		if (searchNotes == null) {
			searchNotes = new ArrayList<NoteInfoBean>();
		}
		searchAdapter = new CloudNoteListAdapter(this, searchNotes);

		try {
			shareLeaguers = LocalDataService.getInstense().getLocNoteLeagers(
					this, LocalDataService.TXT_MYDIARY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (shareLeaguers == null) {
			shareLeaguers = new ArrayList<MapShareLeaguerInfoBean>();
		}
		leagerAdapter = new CloudNoteMberAdapter(this, shareLeaguers);
		if (searchShares == null) {
			searchShares = new ArrayList<MapShareLeaguerInfoBean>();
		}
		searchShareAdapter = new CloudNoteMberAdapter(this, searchShares);
		searchShareAdapter.setSearch(true);

		try {
			map_book = LocalDataService.getInstense().getLocNoteBooks(this,
					LocalDataService.TXT_MYBOOK);
			if (map_book != null)
				noteBooks = (ArrayList<NoteBookBean>) map_book
						.getNOTEBOOKLIST();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (noteBooks == null) {
			noteBooks = new ArrayList<NoteBookBean>();
		}
		bookAdapter = new CloudNoteBookAdapter(this, noteBooks);
		if (searchBooks == null) {
			searchBooks = new ArrayList<NoteBookBean>();
		}
		searchBookAdapter = new CloudNoteBookAdapter(this, searchBooks);
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
		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
						.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
				if (mQueryTextView.isShown()
						&& "".equals(mQueryTextView.getText().toString())) {
					cv.setVisibility(View.GONE);
					showBack();
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				keyWord = s;
				searchState = true;
				BaseData.hideKeyBoard(NoteActivity.this);
				if (cut == 1) {
					searchBooks.clear();
					rlv.setAdapter(searchBookAdapter);
				} else if (cut == 2) {
					searchShares.clear();
					rlv.setAdapter(searchShareAdapter);
				} else {
					searchNotes.clear();
					rlv.setAdapter(searchAdapter);
				}
				loadInfo(cut, true);
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
				searchState = false;
				switch (cut) {
				case 0:
					setAll();
					break;
				case 1:
					setBook();
					break;
				case 2:
					setShare();
					break;
				case 3:
					setStar();
					break;
				default:
					break;
				}
				showMenu();
				return false;
			}
		});

		this.menu = menu;
		loadInfo(0, true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(ll_menu);
		menu.findItem(R.id.action_notes).setVisible(!drawerOpen);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
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
				searchState = false;
				switch (cut) {
				case 0:
					setAll();
					break;
				case 1:
					setBook();
					break;
				case 2:
					setShare();
					break;
				case 3:
					setStar();
					break;
				default:
					break;
				}
				return false;
			}
			return super.onOptionsItemSelected(item);
		case R.id.action_notes:
			if (cut == 1) {
				createBook();
			} else {
				LogicUtil.enter(NoteActivity.this, NoteCreateActivity.class,
						null, 10);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			spdDialog.cancelProgressDialog(null);
			switch (msg.what) {
			case 0:
				showTip(T.ErrStr);
				break;
			case 1:
				// {"notebookId":1958,"noteBookName":"优优工作圈升级","STATUS":1}
				try {
					JSONObject json = new JSONObject(msg.obj.toString());
					if (json.getInt("STATUS") == 1) {
						NoteBookBean book = new NoteBookBean();
						book.setNoteBookId(json.getInt("notebookId"));
						book.setTitle(json.getString("noteBookName"));
						noteBooks.add(1, book);
						bookAdapter.notifyDataSetChanged();
						map_book.setNOTEBOOKNUM(map_book.getNOTEBOOKNUM() + 1);
						tv_num.setText("共有" + map_book.getNOTEBOOKNUM()
								+ "个笔记本");
					} else if (json.getInt("STATUS") == 4) {
						showTip("存在同名笔记本");
					} else {
						showTip("创建失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private void createBook() {
		View view = LayoutInflater.from(this).inflate(R.layout.edit_file, null);
		final EditText editText = (EditText) view
				.findViewById(R.id.editfile_name);
		TextView textView = (TextView) view.findViewById(R.id.edit_fromat);
		textView.setVisibility(View.GONE);

		new AlertDialog.Builder(this)
				.setTitle("新建笔记本")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						final String noteBookName = editText.getEditableText()
								.toString();
						BaseData.hideKeyViewBoard(NoteActivity.this, editText);
						if (noteBookName != null && !noteBookName.equals("")) {
							spdDialog.showProgressDialog("正在处理中...");
							new Thread() {
								public void run() {
									Message msg = new Message();
									try {
										MCResult mcResult = APINoteRequestServers
												.createCloudNoteBook(
														NoteActivity.this,
														noteBookName);
										if (mcResult != null
												&& mcResult.getResultCode() == 1) {
											msg.what = 1;
											msg.obj = mcResult.getResult();
										} else {
											msg.what = 0;
										}
									} catch (Exception e) {
										msg.what = 0;
										e.printStackTrace();
									}
									handler.sendMessage(msg);
								};
							}.start();
						} else {
							showTip("名字不能为空");
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BaseData.hideKeyViewBoard(NoteActivity.this, editText);
					}
				}).show();
	}

	public void deleteNoteBook(NoteBookBean book) {
		noteBooks.remove(book);
		bookAdapter.notifyDataSetChanged();
		map_book.setNOTEBOOKNUM(map_book.getNOTEBOOKNUM() - 1);
		tv_num.setText("共有" + map_book.getNOTEBOOKNUM() + "个笔记本");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				showMenu();
				searchView.onActionViewCollapsed();
				searchState = false;
				switch (cut) {
				case 0:
					setAll();
					break;
				case 1:
					setBook();
					break;
				case 2:
					setShare();
					break;
				case 3:
					setStar();
					break;
				default:
					break;
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void refresh() {
		loadInfo(cut, true);
	}

	public void loadInfo(int which, boolean isRefresh) {
		cut = which;
		stopTask();
		task = new LoadInfoTask(which, isRefresh);
		task.execute();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public LoadInfoTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;

			setLoadingState(true);
		}

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult result = null;
			try {
				if (!searchState) {
					result = loadDate(cur, isRefresh);
				} else {
					result = loadSearch(cur, isRefresh);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		private MCResult loadDate(int cur2, boolean isRefresh2) {
			MCResult result = null;
			String startRecord;
			switch (cur2) {
			case 0:
				if (isRefresh2) {
					startRecord = "0";
				} else {
					startRecord = allNotes.size() + "";
				}
				try {
					result = APINoteRequestServers.cloudNoteList(
							NoteActivity.this, "4", "false", "0", startRecord,
							"10");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh2) {
					startRecord = "0";
				} else {
					startRecord = "20";
				}
				try {
					result = APINoteRequestServers.cloudNoteBookList(
							NoteActivity.this, startRecord, "10");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh2) {
					startRecord = "0";
				} else {
					startRecord = shareLeaguers.size() + "";
				}
				try {
					result = APINoteRequestServers.shareFriendList(
							NoteActivity.this, startRecord, "10");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				if (isRefresh2) {
					startRecord = "0";
				} else {
					startRecord = starNotes.size() + "";
				}
				try {
					result = APINoteRequestServers.cloudNoteList(
							NoteActivity.this, "5", "false", "0", startRecord,
							"10");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return result;
		}

		private MCResult loadSearch(int cur2, boolean isRefresh2) {
			MCResult result = null;
			String startRecord;
			if (isRefresh2) {
				startRecord = "0";
			} else {
				if (cut == 1) {
					startRecord = searchBooks.size() + "";
				} else if (cut == 2) {
					startRecord = searchShares.size() + "";
				} else {
					startRecord = searchNotes.size() + "";
				}
			}
			String noteType;
			try {
				if (cut == 1) {
					result = APINoteRequestServers.searchCloudNoteBookList(
							NoteActivity.this, keyWord, startRecord, "10");
				} else if (cut == 2) {
					result = APINoteRequestServers.searchShareFriendList(
							NoteActivity.this, keyWord, startRecord, "10");
				} else {
					if (cut == 0) {
						noteType = "4";
					} else {
						noteType = "5";
					}
					result = APINoteRequestServers.searchCloudNote(
							NoteActivity.this, noteType, "false", "0", keyWord,
							startRecord, "10");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			setLoadingState(false);
			if (null == mcResult || 1 != mcResult.getResultCode()) {
				// showTip(T.ErrStr);
				if (cur == 0 && isRefresh) {
					allNotes.clear();
					beans = NoteCreateService.getService(NoteActivity.this)
							.queryNotes();
					if (beans != null && beans.size() > 0) {
						for (NoteDraftBean nb : beans) {
							NoteInfoBean bean = new NoteInfoBean();
							bean.setNoteId(nb.getNoteId());
							bean.setNoteTitle(nb.getTitle());
							bean.setNoteContent(nb.getContent());
							bean.setUpdateTime(nb.getTime() + "");
							try {
								JSONArray array = new JSONArray(
										nb.getFilePaths());
								int j = 0;
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);
									if (json.getInt("type") == 1) {
										j++;
									} else {
										bean.setFirstPhotoUrl("");
										bean.setFirstPhotoPath(json
												.getString("path"));
									}
								}
								L.d(TAG, "LoadInfoTask j=" + j);
								bean.setAdjunctNum(j);
							} catch (Exception e) {
								e.printStackTrace();
							}
							allNotes.add(bean);
						}
					}
					if (map_all != null) {
						tv_num.setVisibility(View.VISIBLE);
						if (beans != null) {
							tv_num.setText("共有"
									+ (map_all.getALLNOTENUM() + beans.size())
									+ "个笔记");
						} else {
							tv_num.setText("共有" + map_all.getALLNOTENUM()
									+ "个笔记");
						}

						L.d(TAG,
								"LoadInfoTask ALLNOTENUM="
										+ map_all.getALLNOTENUM());
						ArrayList<NoteInfoBean> allInfos = (ArrayList<NoteInfoBean>) map_all
								.getNOTE_LIST();

						if (null == allInfos || allInfos.size() == 0) {
							if (isRefresh) {
								if (allNotes.size() == 0) {
									// showTip("您还没有写过笔记！");
									rlv.setVisibility(View.GONE);
									ll_no.setVisibility(View.VISIBLE);
									ll_no_sharestar.setVisibility(View.GONE);
									tv_num.setVisibility(View.GONE);
									tv_no.setText("您还没有笔记，现在就来写笔记吧~");
									tv_no_new.setVisibility(View.VISIBLE);
								}
							} else {
								showTip("没有更多笔记！");
							}
						} else {
							L.d(TAG, "LoadInfoTask size=" + allInfos.size());
							allNotes.addAll(allInfos);
						}
					}
					allAdapter.notifyDataSetChanged();
				}
			} else {
				L.d(TAG, "LoadInfoTask cur=" + cur + ",cut=" + cut);
				if (cur == cut) {
					switch (cur) {
					case 0:
						if (!searchState) {
							if (isRefresh) {
								allNotes.clear();
								beans = NoteCreateService.getService(
										NoteActivity.this).queryNotes();
								if (beans != null && beans.size() > 0) {
									for (NoteDraftBean nb : beans) {
										NoteInfoBean bean = new NoteInfoBean();
										bean.setNoteId(nb.getNoteId());
										bean.setNoteTitle(nb.getTitle());
										bean.setNoteContent(nb.getContent());
										bean.setUpdateTime(nb.getTime() + "");
										try {
											JSONArray array = new JSONArray(
													nb.getFilePaths());
											int j = 0;
											for (int i = 0; i < array.length(); i++) {
												JSONObject json = array
														.getJSONObject(i);
												if (json.getInt("type") == 1) {
													j++;
												} else {
													bean.setFirstPhotoUrl("");
													bean.setFirstPhotoPath(json
															.getString("path"));
												}
											}
											L.d(TAG, "LoadInfoTask j=" + j);
											bean.setAdjunctNum(j);
										} catch (Exception e) {
											e.printStackTrace();
										}
										allNotes.add(bean);
									}
								}
							}

							map_all = (MapNoteInfoBean) mcResult.getResult();
							if (map_all != null) {
								tv_num.setVisibility(View.VISIBLE);
								if (beans != null) {
									tv_num.setText("共有"
											+ (map_all.getALLNOTENUM() + beans
													.size()) + "个笔记");
								} else {
									tv_num.setText("共有"
											+ map_all.getALLNOTENUM() + "个笔记");
								}
								ArrayList<NoteInfoBean> allInfos = (ArrayList<NoteInfoBean>) map_all
										.getNOTE_LIST();

								if (null == allInfos || allInfos.size() == 0) {
									if (isRefresh) {
										if (allNotes.size() == 0) {
											// showTip("您还没有写过笔记！");
											rlv.setVisibility(View.GONE);
											ll_no.setVisibility(View.VISIBLE);
											ll_no_sharestar
													.setVisibility(View.GONE);
											tv_num.setVisibility(View.GONE);
											tv_no.setText("您还没有笔记，现在就来写笔记吧~");
											tv_no_new
													.setVisibility(View.VISIBLE);
										}
									} else {
										showTip("没有更多笔记！");
									}
								} else {
									allNotes.addAll(allInfos);
								}
							}
							allAdapter.notifyDataSetChanged();
						} else {
							setSearch(mcResult);
						}
						break;
					case 1:
						if (!searchState) {
							map_book = (MapNoteBookBean) mcResult.getResult();
							if (map_book != null) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("共有" + map_book.getNOTEBOOKNUM()
										+ "个笔记本");
								ArrayList<NoteBookBean> allInfos = (ArrayList<NoteBookBean>) map_book
										.getNOTEBOOKLIST();

								if (null == allInfos || allInfos.size() == 0) {
									if (isRefresh) {
										// showTip("您还没有创建过笔记本！");
									} else {
										showTip("没有更多笔记本！");
									}
								}
								// else {
								if (isRefresh) {
									noteBooks.clear();
									NoteBookBean book = new NoteBookBean();
									book.setTitle("默认笔记本");
									book.setNoteBookId(map_book
											.getDEFAULTNOTEBOOKID());
									book.setNotesNum(map_book.getDEFAULTNOTE());
									noteBooks.add(book);
								}
								if (null != allInfos && allInfos.size() != 0)
									noteBooks.addAll(allInfos);
								// }
							}
							bookAdapter.notifyDataSetChanged();
						} else {
							setSearch(mcResult);
						}
						break;
					case 2:
						if (!searchState) {
							@SuppressWarnings("unchecked")
							ArrayList<MapShareLeaguerInfoBean> allInfos = (ArrayList<MapShareLeaguerInfoBean>) mcResult
									.getResult();

							if (null == allInfos || allInfos.size() == 0) {
								if (isRefresh) {
									// showTip("您还没有分享！");
									rlv.setVisibility(View.GONE);
									ll_no.setVisibility(View.VISIBLE);
									ll_no_sharestar.setVisibility(View.VISIBLE);
									iv_no_head.setVisibility(View.VISIBLE);
									MyFinalBitmap
											.setHeader(
													NoteActivity.this,
													iv_no_head,
													ThumbnailImageUrl
															.getThumbnailHeadUrl(
																	new UserBusinessDatabase(
																			App.app)
																			.getHeadUrlPath(App.app.share
																					.getSessionKey()),
																	HeadSizeEnum.ONE_HUNDRED_AND_TWENTY));
									iv_no_mid.setVisibility(View.VISIBLE);
									iv_no_mid
											.setImageResource(R.drawable.note_no_mid);
									iv_no_sharesatr.setVisibility(View.VISIBLE);
									iv_no_sharesatr
											.setImageResource(R.drawable.note_no_share);
									tv_no.setText("您还未与任何人分享过笔记");
									tv_no_new.setVisibility(View.GONE);
									tv_num.setVisibility(View.GONE);
								} else {
									showTip("没有更多分享！");
								}
							} else {
								if (isRefresh)
									shareLeaguers.clear();
								shareLeaguers.addAll(allInfos);
							}
							leagerAdapter.notifyDataSetChanged();
						} else {
							setSearch(mcResult);
						}
						break;
					case 3:
						if (!searchState) {
							if (isRefresh) {
								starNotes.clear();
							}
							map_star = (MapNoteInfoBean) mcResult.getResult();
							if (map_star != null) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("共有" + map_star.getALLNOTENUM()
										+ "个笔记");
								ArrayList<NoteInfoBean> allInfos = (ArrayList<NoteInfoBean>) map_star
										.getNOTE_LIST();

								if (null == allInfos || allInfos.size() == 0) {
									if (isRefresh) {
										// showTip("您还没有星标笔记！");
										rlv.setVisibility(View.GONE);
										ll_no.setVisibility(View.VISIBLE);
										ll_no_sharestar
												.setVisibility(View.VISIBLE);
										iv_no_head.setVisibility(View.GONE);
										iv_no_mid.setVisibility(View.GONE);
										iv_no_sharesatr
												.setVisibility(View.VISIBLE);
										iv_no_sharesatr
												.setImageResource(R.drawable.note_no_star);
										tv_no.setText("您还没有星标笔记");
										tv_no_new.setVisibility(View.GONE);
										tv_num.setVisibility(View.GONE);
									} else {
										showTip("没有更多星标笔记！");
									}
								} else {
									tv_num.setVisibility(View.VISIBLE);
									if (isRefresh)
										starNotes.clear();
									starNotes.addAll(allInfos);
								}
							}
							starAdapter.notifyDataSetChanged();
						} else {
							setSearch(mcResult);
						}
						break;
					default:
						break;
					}
				}
			}
			isLoading = false;
		}

		private void setSearch(MCResult mcResult) {
			if (cut == 1) {
				MapNoteBookBean mapbook = (MapNoteBookBean) mcResult
						.getResult();
				if (mapbook != null) {
					tv_num.setVisibility(View.VISIBLE);
					tv_num.setText("共有" + (mapbook.getNOTEBOOKNUM() - 1)
							+ "个笔记本");
					ArrayList<NoteBookBean> allInfos = (ArrayList<NoteBookBean>) mapbook
							.getNOTEBOOKLIST();
					if (null == allInfos || allInfos.size() == 0) {
						if (isRefresh) {
							showTip("没有和 " + keyWord + " 有关的笔记本！");
						} else {
							showTip("没有更多笔记本！");
						}
					} else {
						if (isRefresh) {
							searchBooks.clear();
						}
						searchBooks.addAll(allInfos);
					}
				}
				searchBookAdapter.notifyDataSetChanged();
			} else if (cut == 2) {
				@SuppressWarnings("unchecked")
				ArrayList<MapShareLeaguerInfoBean> allInfos = (ArrayList<MapShareLeaguerInfoBean>) mcResult
						.getResult();
				if (null == allInfos || allInfos.size() == 0) {
					if (isRefresh) {
						showTip("没有和 " + keyWord + " 有关的分享联系人！");
					} else {
						showTip("没有更多分享！");
					}
				} else {
					if (isRefresh)
						searchShares.clear();
					searchShares.addAll(allInfos);
				}
				searchShareAdapter.notifyDataSetChanged();
			} else {
				MapNoteInfoBean map = (MapNoteInfoBean) mcResult.getResult();
				if (map != null) {
					tv_num.setVisibility(View.VISIBLE);
					tv_num.setText("共有" + map.getALLNOTENUM() + "个笔记");
					ArrayList<NoteInfoBean> allInfos = (ArrayList<NoteInfoBean>) map
							.getNOTE_LIST();
					if (null == allInfos || allInfos.size() == 0) {
						if (isRefresh) {
							showTip("没有和 " + keyWord + " 有关的笔记！");
						} else {
							showTip("没有更多笔记！");
						}
					} else {
						if (isRefresh)
							searchNotes.clear();
						searchNotes.addAll(allInfos);
					}
				}
				searchAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			loadInfo(cut, false);
		}
	}

	@Override
	public void onRefresh() {
		rlv.onRefreshComplete();
		if (!isLoading) {
			loadInfo(cut, true);
		}
	}

	private String[] shareFriendIds, shareGroupIds;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case FriendsChooserActivity.RESCODE:
			shareFriendIds = data.getStringArrayExtra("ids");
			new ShareFriendTask(data.getIntExtra("id", 0)).execute();
			break;
		case GroupsChooserActivity.RESCODE:
			shareGroupIds = data.getStringArrayExtra("ids");
			new ShareGroupTask(data.getIntExtra("id", 0)).execute();
			break;
		case 10:
			rlv.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);
			NoteDraftBean nb = null;
			try {
				nb = (NoteDraftBean) data.getSerializableExtra("NoteDraftBean");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (nb == null) {
				NoteInfoBean nibean = null;
				try {
					nibean = (NoteInfoBean) data
							.getSerializableExtra("NoteInfoBean");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (nibean == null) {
					loadInfo(0, true);
				} else {
					int position = data.getIntExtra("position", -1);
					if (position > -1) {
						allNotes.remove(position);
						allNotes.add(position, nibean);
					} else {
						allNotes.add(0, nibean);
					}
					allAdapter.notifyDataSetChanged();
				}
			} else {
				NoteInfoBean bean = new NoteInfoBean();
				bean.setNoteId(nb.getNoteId());
				bean.setNoteTitle(nb.getTitle());
				bean.setNoteContent(nb.getContent());
				bean.setCreateTime(nb.getTime() + "");
				try {
					JSONArray array = new JSONArray(nb.getFilePaths());
					int j = 0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInt("type") == 1) {
							j++;
						} else {
							bean.setFirstPhotoUrl("");
							bean.setFirstPhotoPath(json.getString("path"));
						}
					}
					bean.setAdjunctNum(j);
				} catch (Exception e) {
					e.printStackTrace();
				}

				int position = data.getIntExtra("position", -1);
				L.i(TAG, "onActivityResult position=" + position);
				if (beans == null)
					beans = new ArrayList<NoteDraftBean>();
				if (position > -1) {
					allNotes.remove(position);
					allNotes.add(position, bean);

					beans.remove(position);
					beans.add(position, nb);
				} else {
					allNotes.add(0, bean);

					beans.add(0, nb);
				}
				allAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	class ShareFriendTask extends AsyncTask<Void, Integer, MCResult> {

		private int noteId;

		public ShareFriendTask(int noteId) {
			this.noteId = noteId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				mc = APINoteRequestServers.shareDiary(NoteActivity.this, noteId
						+ "", shareFriendIds);
			} catch (Exception e) {

				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				showTip("分享失败!");
			} else {
				showTip("已分享！");
			}
		}

	}

	class ShareGroupTask extends AsyncTask<Void, Integer, MCResult> {

		private int noteId;

		public ShareGroupTask(int noteId) {
			this.noteId = noteId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				mc = APINoteRequestServers.shareDiaryToGroup(NoteActivity.this,
						noteId + "", shareGroupIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mc;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null == result || 1 != result.getResultCode()) {
				showTip("分享失败!");
			} else {
				InfoWallActivity.isNeedRefresh = true;
				showTip("已分享！");
			}
		}

	}

	@Override
	public void onTabClick(View tab) {
		switch ((Integer) tab.getTag()) {
		case 0:
			cut = 0;

			rlv.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);
			tv_num.setVisibility(View.VISIBLE);
			setAll();
			break;
		case 1:
			cut = 1;

			rlv.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);
			tv_num.setVisibility(View.VISIBLE);
			setBook();
			break;
		case 2:
			cut = 2;

			rlv.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);
			tv_num.setVisibility(View.VISIBLE);
			setShare();
			break;
		case 3:
			cut = 3;

			rlv.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);
			tv_num.setVisibility(View.VISIBLE);
			setStar();
			break;
		default:
			break;
		}
	}
}
