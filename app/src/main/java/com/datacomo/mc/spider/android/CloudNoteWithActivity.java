package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.datacomo.mc.spider.android.adapter.CloudNoteListAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.NoteDraftBean;
import com.datacomo.mc.spider.android.db.NoteCreateService;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;
import com.datacomo.mc.spider.android.view.RefreshListView.OnRefreshListener;
import com.datacomo.mc.spider.android.view.TabLinearLayout;
import com.datacomo.mc.spider.android.view.TabLinearLayout.OnTabClickListener;

public class CloudNoteWithActivity extends BasicActionBarActivity implements
		OnTabClickListener, OnRefreshListener, OnLoadMoreListener {
	private final String TAG = "CloudNoteWithActivity";

	private int cut;

	private String[] tabContent = new String[] { "全部分享", "向您分享", "您分享的" };
	private TabLinearLayout mTabLinearLayout;

	private RefreshListView listView;
	private LinearLayout ll_no, ll_no_sharestar;
	private TextView tv_num, tv_no, tv_no_new;

	private boolean isLoading, searchState;
	private MapNoteInfoBean map_all, map_other, map_my;
	private ArrayList<NoteInfoBean> allNotes, otherNotes, myNotes, searchNotes;
	private CloudNoteListAdapter allAdapter, otherAdapter, myAdapter,
			searchAdapter;
	private LoadNotesTask task;
	private String id, mBerName;
	private boolean IsBook = false;
	private String keyWord;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_note);

		findView();
		setView();
	}

	private void findView() {
		mTabLinearLayout = (TabLinearLayout) findViewById(R.id.tabs);

		ll_no_sharestar = (LinearLayout) findViewById(R.id.note_no_sharestar);
		ll_no = (LinearLayout) findViewById(R.id.note_no);
		tv_num = (TextView) findViewById(R.id.note_num_tv);
		tv_no = (TextView) findViewById(R.id.note_no_tv);
		tv_no_new = (TextView) findViewById(R.id.note_no_tv_new);

		tv_no.setText("该笔记本里还没有笔记，现在就来新建一篇吧~");
		tv_no_new.setOnClickListener(this);

		listView = (RefreshListView) findViewById(R.id.note_listview);
		listView.setonRefreshListener(this);
		listView.setonLoadMoreListener(this);
	}

	private void setView() {
		mTabLinearLayout.changeText(tabContent);
		mTabLinearLayout.refresh(0);
		mTabLinearLayout.setOnTabClickListener(this);

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
		ab.setTitle(mBerName);
		IsBook = b.getBoolean("IsBook", false);

		allNotes = new ArrayList<NoteInfoBean>();
		otherNotes = new ArrayList<NoteInfoBean>();
		myNotes = new ArrayList<NoteInfoBean>();
		searchNotes = new ArrayList<NoteInfoBean>();

		if (IsBook) {
			addLocal();

			mTabLinearLayout.setVisibility(View.GONE);
			int num = b.getInt("num");
			if (num == 0 && allNotes.size() == 0) {
				listView.setVisibility(View.GONE);
				ll_no_sharestar.setVisibility(View.GONE);
				ll_no.setVisibility(View.VISIBLE);
				tv_num.setVisibility(View.GONE);
				tv_no.setVisibility(View.VISIBLE);
				tv_no_new.setVisibility(View.VISIBLE);
			}
		}

		allAdapter = new CloudNoteListAdapter(this, allNotes);
		otherAdapter = new CloudNoteListAdapter(this, otherNotes);
		myAdapter = new CloudNoteListAdapter(this, myNotes);
		searchAdapter = new CloudNoteListAdapter(this, searchNotes);

		listView.setAdapter(allAdapter);
	}

	private void addLocal() {
		beans = NoteCreateService.getService(this).queryNotesByBook(
				Integer.valueOf(id));
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
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.note_no_tv_new:
			Bundle b = new Bundle();
			b.putInt("id", Integer.valueOf(id));
			LogicUtil.enter(this, NoteCreateActivity.class, b, 10);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_refresh).setVisible(true);
		if (IsBook)
			menu.findItem(R.id.action_notes).setVisible(true);

		MenuItem mi = menu.findItem(R.id.action_search);
		searchView = (SearchView) mi.getActionView();
		mi.setVisible(true);
		ImageView v = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_button);
		v.setImageResource(R.drawable.action_search);
		View vp = searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_plate);
		vp.setBackgroundResource(R.drawable.edit_bg);

		final ImageView cv = (ImageView) searchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_close_btn);
		cv.setImageResource(R.drawable.search_close);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				keyWord = s;
				searchState = true;
				searchNotes.clear();
				listView.setAdapter(searchAdapter);
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
				searchState = false;
				if (IsBook) {
					setBook();
				} else {
					switch (cut) {
					case 0:
						setAll();
						break;
					case 1:
						setOther();
						break;
					case 2:
						setMy();
						break;
					default:
						break;
					}
				}
				return false;
			}
		});

		this.menu = menu;
		load(true);
		return super.onCreateOptionsMenu(menu);
	}

	private void setBook() {
		setAll();
	}

	private void setMy() {
		listView.setAdapter(myAdapter);
		if (myNotes.size() == 0) {
			tv_num.setVisibility(View.GONE);
			load(true);
		} else {
			if (map_my != null) {
				tv_num.setVisibility(View.VISIBLE);
				tv_num.setText("您向TA共分享了" + map_my.getSHARE_NOTE_NUM() + "个笔记");
			} else {
				tv_num.setVisibility(View.GONE);
				load(true);
			}
		}
	}

	private void setOther() {
		listView.setAdapter(otherAdapter);
		if (otherNotes.size() == 0) {
			tv_num.setVisibility(View.GONE);
			load(true);
		} else {
			if (map_other != null) {
				tv_num.setVisibility(View.VISIBLE);
				tv_num.setText("TA向您共分享了" + map_other.getSHARE_NOTE_NUM()
							+ "个笔记");
			} else {
				tv_num.setVisibility(View.GONE);
				load(true);
			}
		}
	}

	private void setAll() {
		listView.setAdapter(allAdapter);
		if (allNotes.size() == 0) {
			tv_num.setVisibility(View.GONE);
			load(true);
		} else {
			if (map_all != null) {
				tv_num.setVisibility(View.VISIBLE);
				if (IsBook) {
					if (beans != null) {
						tv_num.setText("共有"
								+ (map_all.getNOTENUM() + beans.size()) + "个笔记");
					} else {
						tv_num.setText("共有" + map_all.getNOTENUM() + "个笔记");
					}
				} else {
					tv_num.setText("您和TA之间共分享了" + map_all.getSHARE_NOTE_NUM()
							+ "个笔记");
				}
			} else {
				tv_num.setVisibility(View.GONE);
				load(true);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBack();
			return super.onOptionsItemSelected(item);
		case R.id.action_refresh:
			load(true);
			return true;
		case R.id.action_notes:
			Bundle b = new Bundle();
			b.putInt("id", Integer.valueOf(id));
			LogicUtil.enter(this, NoteCreateActivity.class, b, 10);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void onBack() {
		if (searchView.findViewById(
				com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
			searchView.onActionViewCollapsed();
			searchState = false;
			if (IsBook) {
				setBook();
			} else {
				switch (cut) {
				case 0:
					setAll();
					break;
				case 1:
					setOther();
					break;
				case 2:
					setMy();
					break;
				default:
					break;
				}
			}
			// } else {
			// if (IsBook) {
			// if (nb != null) {
			// Intent i = new Intent();
			// i.putExtra("NoteDraftBean", nb);
			// i.putExtra("position", position);
			// setResult(RESULT_OK, i);
			// finish();
			// }
			// }
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLoadMore() {
		if (!isLoading) {
			load(false);
		}
	}

	private void load(boolean isRefresh) {
		stopLoad();
		task = new LoadNotesTask(cut, isRefresh);
		task.execute();
	}

	private void stopLoad() {
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}

	}

	class LoadNotesTask extends AsyncTask<Void, Integer, MCResult> {
		private final int cur;
		private boolean isRefresh;

		public LoadNotesTask(int curCut, boolean isRefresh) {
			this.cur = curCut;
			isLoading = true;
			this.isRefresh = isRefresh;
			setLoadingState(true);
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult result = null;
			try {
				if (searchState) {
					result = loadSreach();
				} else {
					result = loadDate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		private MCResult loadSreach() throws Exception {
			MCResult result = null;
			int startRecord;
			if (isRefresh) {
				startRecord = 0;
			} else {
				startRecord = searchNotes.size();
			}
			if (IsBook) {
				result = APINoteRequestServers.searchCloudNote(
						CloudNoteWithActivity.this, "4", "true", id + "",
						keyWord, startRecord + "", "10");
			} else {
				if (cut == 0) {
					result = APINoteRequestServers.searchShareCloudNoteList(
							CloudNoteWithActivity.this, id + "", keyWord,
							"true", "0", startRecord + "", "10");
				} else if (cut == 1) {
					result = APINoteRequestServers.searchShareCloudNoteList(
							CloudNoteWithActivity.this, id + "", keyWord,
							"false", "2", startRecord + "", "10");
				} else {
					result = APINoteRequestServers.searchShareCloudNoteList(
							CloudNoteWithActivity.this, id + "", keyWord,
							"false", "1", startRecord + "", "10");
				}
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
					startRecord = allNotes.size();
				}
				try {
					if (IsBook) {
						mc = APINoteRequestServers.cloudNoteList(
								CloudNoteWithActivity.this, "4", "true", id
										+ "", startRecord + "", 20 + "");
					} else {
						mc = APINoteRequestServers.shareCloudNoteList(
								CloudNoteWithActivity.this, id, "true", "0",
								startRecord + "", 20 + "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = otherNotes.size();
				}
				try {
					mc = APINoteRequestServers.shareCloudNoteList(
							CloudNoteWithActivity.this, id, "false", "2",
							startRecord + "", 20 + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (isRefresh) {
					startRecord = 0;
				} else {
					startRecord = myNotes.size();
				}
				try {
					mc = APINoteRequestServers.shareCloudNoteList(
							CloudNoteWithActivity.this, id, "false", "1",
							startRecord + "", 20 + "");
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
			if (cur != cut || this.isCancelled()) {
				showTip(T.ErrStr);
				return;
			} else {
				if (null == mcResult || mcResult.getResultCode() != 1) {
					showTip(T.ErrStr);
				} else {
					switch (cur) {
					case 0:
						if (searchState) {
							setSearchResult(mcResult);
						} else {
							map_all = (MapNoteInfoBean) mcResult.getResult();
							if (map_all != null) {
								tv_num.setVisibility(View.VISIBLE);
								if (IsBook) {
									if (map_all.getNOTENUM() == 0) {
										if (beans != null && beans.size() > 0) {
											tv_num.setText("共有"
													+ (map_all.getNOTENUM() + beans
															.size()) + "个笔记");
										} else {
											tv_num.setVisibility(View.GONE);
										}
									} else {
										if (beans != null) {
											tv_num.setText("共有"
													+ (map_all.getNOTENUM() + beans
															.size()) + "个笔记");
										} else {
											tv_num.setText("共有"
													+ map_all.getNOTENUM()
													+ "个笔记");
										}
									}
								} else {
									tv_num.setText("您和TA之间共分享了"
											+ map_all.getSHARE_NOTE_NUM()
											+ "个笔记");
								}
								ArrayList<NoteInfoBean> result0 = (ArrayList<NoteInfoBean>) map_all
										.getNOTE_LIST();
								if (null == result0 || result0.size() == 0) {
									if (!isRefresh) {
										showTip("亲，最后一个笔记了哦!");
									} else {
										if (allNotes.size() == 0)
											showTip("暂无数据");
									}
								} else {
									if (isRefresh)
										allNotes.clear();
									addLocal();
									allNotes.addAll(result0);
									allAdapter.notifyDataSetChanged();
								}
							}
						}
						break;
					case 1:
						if (searchState) {
							setSearchResult(mcResult);
						} else {
							map_other = (MapNoteInfoBean) mcResult.getResult();
							if (map_other != null) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("TA向您共分享了"
										+ map_other.getSHARE_NOTE_NUM() + "个笔记");
								ArrayList<NoteInfoBean> result1 = (ArrayList<NoteInfoBean>) map_other
										.getNOTE_LIST();
								if (null == result1 || result1.size() == 0) {
									if (!isRefresh) {
										showTip("亲，最后一个笔记了哦!");
									} else {
										showTip("暂无数据");
									}
								} else {
									if (isRefresh)
										otherNotes.clear();
									otherNotes.addAll(result1);
									otherAdapter.notifyDataSetChanged();
								}
							}
						}
						break;
					case 2:
						if (searchState) {
							setSearchResult(mcResult);
						} else {
							map_my = (MapNoteInfoBean) mcResult.getResult();
							if (map_my != null) {
								tv_num.setVisibility(View.VISIBLE);
								tv_num.setText("您向TA共分享了"
										+ map_my.getSHARE_NOTE_NUM() + "个笔记");
								ArrayList<NoteInfoBean> result2 = (ArrayList<NoteInfoBean>) map_my
										.getNOTE_LIST();
								if (null == result2 || result2.size() == 0) {
									if (!isRefresh) {
										showTip("亲，最后一个笔记了哦!");
									} else {
										showTip("暂无数据");
									}
								} else {
									if (isRefresh)
										myNotes.clear();
									myNotes.addAll(result2);
									myAdapter.notifyDataSetChanged();
								}
							}
						}
						break;
					default:
						break;
					}
				}
			}
			isLoading = false;
		}

		private void setSearchResult(MCResult mcResult) {
			MapNoteInfoBean map_search = (MapNoteInfoBean) mcResult.getResult();
			if (map_search != null) {
				tv_num.setVisibility(View.VISIBLE);
				if (IsBook) {
					tv_num.setText("共有" + map_search.getNOTENUM() + "个笔记");
				} else {
					tv_num.setText("共有" + map_search.getSHARE_NOTE_NUM()
							+ "个笔记");
				}
				ArrayList<NoteInfoBean> result0 = (ArrayList<NoteInfoBean>) map_search
						.getNOTE_LIST();
				if (null == result0 || result0.size() == 0) {
					if (!isRefresh) {
						showTip("亲，最后一个笔记本了哦!");
					} else {
						showTip("暂无数据");
					}
				} else {
					if (isRefresh)
						searchNotes.clear();
					searchNotes.addAll(result0);
					searchAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		listView.onRefreshComplete();
		if (!isLoading) {
			load(true);
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
			listView.setVisibility(View.VISIBLE);
			ll_no.setVisibility(View.GONE);

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
					load(true);
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

				position = data.getIntExtra("position", -1);
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

	public ArrayList<NoteDraftBean> beans;
	NoteDraftBean nb = null;
	int position;

	class ShareFriendTask extends AsyncTask<Void, Integer, MCResult> {

		private int noteId;

		public ShareFriendTask(int noteId) {
			this.noteId = noteId;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			MCResult mc = null;
			try {
				mc = APINoteRequestServers
						.shareDiary(CloudNoteWithActivity.this, noteId + "",
								shareFriendIds);
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
				mc = APINoteRequestServers.shareDiaryToGroup(
						CloudNoteWithActivity.this, noteId + "", shareGroupIds);
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
			listView.setAdapter(allAdapter);
			if (allNotes.size() == 0) {
				tv_num.setVisibility(View.GONE);
				load(true);
			} else {
				if (map_all != null) {
					tv_num.setText("您和TA之间共分享了" + map_all.getSHARE_NOTE_NUM()
							+ "个笔记");
					tv_num.setVisibility(View.VISIBLE);
				} else {
					tv_num.setVisibility(View.GONE);
				}
			}
			break;
		case 1:
			cut = 1;
			listView.setAdapter(otherAdapter);
			if (otherNotes.size() == 0) {
				tv_num.setVisibility(View.GONE);
				load(true);
			} else {
				if (map_other != null) {
					tv_num.setText("TA向您共分享了" + map_other.getSHARE_NOTE_NUM()
							+ "个笔记");
					tv_num.setVisibility(View.VISIBLE);
				} else {
					tv_num.setVisibility(View.GONE);
				}
			}
			break;
		case 2:
			cut = 2;
			listView.setAdapter(myAdapter);
			if (myNotes.size() == 0) {
				tv_num.setVisibility(View.GONE);
				load(true);
			} else {
				if (map_my != null) {
					tv_num.setText("您向TA共分享了" + map_my.getSHARE_NOTE_NUM()
							+ "个笔记");
					tv_num.setVisibility(View.VISIBLE);
				} else {
					tv_num.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}
	}
}
