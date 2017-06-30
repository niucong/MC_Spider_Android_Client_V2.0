package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.SearchAutoComplete;
import com.datacomo.mc.spider.android.adapter.FindLocalAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.db.ContactsBookService;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.PinYin4JCn;
import com.datacomo.mc.spider.android.view.MyLetterListView;
import com.datacomo.mc.spider.android.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.SearchBar2;
import com.datacomo.mc.spider.android.view.SearchBar2.OnClearListener;
import com.datacomo.mc.spider.android.view.SearchBar2.OnSearchListener;
import com.datacomo.mc.spider.android.view.SignTextView;

public class FindResult2Activity extends BasicActionBarActivity implements
		OnClearListener, OnSearchListener, OnTouchingLetterChangedListener {
	private RefreshListView list;
	private FindLocalAdapter adapter;
	private ArrayList<ContactInfo> infos;
	private ArrayList<ContactInfo> all;
	private SetDataTask task;
	private MyLetterListView letterView;
	private SignTextView signText;
	private SearchBar2 sBar;
	private Type mType;

	// private int screenWidth;
	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_find_result2);
		ab.setTitle("从手机通讯录中添加朋友");
		setView();
	}

	private void setView() {
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// screenWidth = dm.widthPixels;

		letterView = (MyLetterListView) findViewById(R.id.letterView);
		letterView.setOnTouchingLetterChangedListener(this);
		signText = (SignTextView) findViewById(R.id.sign);
		sBar = (SearchBar2) findViewById(R.id.searchs);
		sBar.setOnsearchListener(this);
		sBar.setOnClearListener(this);
		sBar.setVisibility(View.GONE);
		list = (RefreshListView) findViewById(R.id.list);
		infos = new ArrayList<ContactInfo>();
		Bundle bundle = getBundle();
		mType = (Type) bundle.getSerializable(BundleKey.TYPE_REQUEST);
		switch (mType) {
		case ADDFRIEND:
			adapter = new FindLocalAdapter(this, infos, mType);
			break;
		case ADDFRIENDTOGROUP:
			String groupId = bundle.getString(BundleKey.ID_GROUP);
			adapter = new FindLocalAdapter(this, infos, mType, groupId);
			break;
		default:
			break;

		}
		list.setAdapter(adapter);
		startTask(false);
	}

	private Bundle getBundle() {
		Intent intent = getIntent();
		if (null != intent)
			return intent.getExtras();
		return null;
	}

	private void startTask(boolean refresh) {
		stopTask();
		task = new SetDataTask(refresh);
		task.execute();
	}

	private void stopTask() {
		if (null != task && task.getStatus() == Status.RUNNING) {
			task.cancel(true);
		}

	}

	class SetDataTask extends AsyncTask<Void, Integer, ArrayList<ContactInfo>> {
		private boolean refresh;

		public SetDataTask(boolean refresh) {
			this.refresh = refresh;
			list.showLoadFooter();
		}

		@Override
		protected ArrayList<ContactInfo> doInBackground(Void... params) {
			if (null == all) {
				all = new ArrayList<ContactInfo>();
			} else {
				all.clear();
			}
			int memberId = GetDbInfoUtil.getMemberId(FindResult2Activity.this);
			ArrayList<ContactInfo> r1 = null;
			if (!refresh)
				try {
					r1 = new ContactsBookService(FindResult2Activity.this)
							.getContactLists(memberId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (null != r1 && 0 != r1.size()) {
				all.addAll(r1);
			} else {
				try {
					APIRequestServers.uploadPhoneBook(FindResult2Activity.this,
							null, null, null);
					APIRequestServers.getaddressBook(FindResult2Activity.this);
					r1 = new ContactsBookService(FindResult2Activity.this)
							.getContactLists(memberId);
					if (null != r1 && 0 != r1.size()) {
						all.addAll(r1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return all;
		}

		@Override
		protected void onPostExecute(ArrayList<ContactInfo> result) {
			super.onPostExecute(result);
			list.showFinishLoadFooter();
			if (null == result || 0 == result.size()) {
				showTip("无法获取联系人！");
				return;
			}
			infos.addAll(all);
			adapter.notify(true);
		}
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// if ("#".equals(s)) {
		// list.setSelection(0);
		// signText.showSign(s, 2000);
		// } else {
		for (int i = 0; i < adapter.getCount(); i++) {
			ContactInfo entity = (ContactInfo) adapter.getItem(i);
			if (entity.getContactMemberId() == adapter.TAG
					&& s.equals(entity.getName())) {
				list.setSelection(i + list.getHeaderViewsCount());
				signText.showSign(s, 2000);
			}
		}
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

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

		searchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) searchView
						.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
				if (mQueryTextView.isShown()
						&& "".equals(mQueryTextView.getText().toString())) {
					cv.setVisibility(View.GONE);
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				search(s);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				search(s);
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
				clear();
				return false;
			}
		});

		menu.findItem(R.id.action_refresh).setVisible(true);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (searchView.findViewById(
					com.actionbarsherlock.R.id.abs__search_src_text).isShown()) {
				searchView.onActionViewCollapsed();
				clear();
			} else {
				setResult(RESULT_CANCELED);
				finish();
			}
			return true;
		case R.id.action_refresh:
			startTask(true);
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
				searchView.onActionViewCollapsed();
				clear();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void search(String keyWords) {
		if (keyWords != null && !"".equals(keyWords)) {
			ArrayList<ContactInfo> result = searchFriends(all, keyWords);
			if (result.size() == 0) {
				showTip("没有找到相应的联系人！");
				return;
			}
			infos.clear();
			infos.addAll(result);
			adapter.notify(false);
		} else {
			infos.clear();
			infos.addAll(all);
			adapter.notify(true);
		}
	}

	private void clear() {
		infos.clear();
		infos.addAll(all);
		adapter.notify(true);
	}

	@Override
	public void onSearch(String keyWords) {
		search(keyWords);
	}

	@Override
	public void onClear(String keyWords) {
		clear();
	}

	private ArrayList<ContactInfo> searchFriends(ArrayList<ContactInfo> lists,
			String str) {
		ArrayList<ContactInfo> searchEntities = new ArrayList<ContactInfo>();
		if (null == lists || null == str) {
			return searchEntities;
		}
		String lowerCase = str.trim().toLowerCase();
		if ("".equals(lowerCase)) {
			return lists;
		}
		for (ContactInfo bean : lists) {
			String name = bean.getName();
			if (name != null
					&& (name.toLowerCase().startsWith(str.toLowerCase()))) {
				searchEntities.add(bean);
			} else {
				boolean isStrWith = false;
				String groupNamePy = PinYin4JCn.convertPy("leaguer",
						bean.getName()).toLowerCase();
				if (groupNamePy.contains(",")) {
					String[] pys = groupNamePy.split(",");
					int size = 0;
					if (pys != null) {
						size = pys.length;
						for (int i = 0; i < size; i++) {
							String py = pys[i];
							if (py != null
									&& (py.toLowerCase().startsWith(str
											.toLowerCase()))) {
								searchEntities.add(bean);
								isStrWith = true;
								break;
							}
						}
					}
				} else {
					if (groupNamePy.toLowerCase().startsWith(str.toLowerCase())) {
						searchEntities.add(bean);
						isStrWith = true;
					}
				}

				if (!isStrWith) {
					String groupNameJp = PinYin4JCn.convertPy("leaguer",
							bean.getName()).toLowerCase();
					if (groupNameJp.contains(",")) {
						String[] jps = groupNameJp.split(",");
						int size = 0;
						if (jps != null) {
							size = jps.length;
							for (int i = 0; i < size; i++) {
								String jp = jps[i];
								if (jp != null
										&& (jp.toLowerCase().startsWith(str
												.toLowerCase()))) {
									searchEntities.add(bean);
									break;
								}
							}
						}
					} else {
						if (groupNameJp.toLowerCase().startsWith(
								str.toLowerCase())) {
							searchEntities.add(bean);
						}
					}
				}
			}
		}
		return searchEntities;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			ContactInfo info = (ContactInfo) ChooseGroupsDialogActivity
					.getContactInfo();
			Bundle bundle = data.getExtras();
			String[] chosenIds = bundle.getStringArray(BundleKey.CHOOSEDS);
			adapter.changeGroups(chosenIds, info);
		}
	}

}
