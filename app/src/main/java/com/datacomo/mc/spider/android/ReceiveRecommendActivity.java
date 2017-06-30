package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.ReceiveRecommendAdapter;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.map.MapGroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapRecommendFriendBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.view.RefreshListView;
import com.datacomo.mc.spider.android.view.RefreshListView.OnLoadMoreListener;

public class ReceiveRecommendActivity extends BaseActivity implements
		OnLoadMoreListener {
	private static final String TAG = "ReceiveRecommendActivity";

	private TextView title;
	private RefreshListView lv;
	private ImageView rec_back;

	private int groupId;
	private String groupName;
	private int allNum;
	private int recommendId;
	private List<FriendBean> friends;
	private ReceiveRecommendAdapter rrAdapter;

	private boolean hasMore = true;

	private int type;
	private List<GroupLeaguerBean> groupLeaguers;
	private long createTime;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.receive_recommend);
		getIntentMesage();
		init();

		LoadInfoTask loadInfoTask = new LoadInfoTask();
		loadInfoTask.execute();
	}

	public void init() {
		title = (TextView) findViewById(R.id.rec_text);
		lv = (RefreshListView) findViewById(R.id.rec_listView);
		rec_back = (ImageView) findViewById(R.id.rec_back);

		L.d(TAG, "init type=" + type);
		if (type == -1) {
			friends = new ArrayList<FriendBean>();
			rrAdapter = new ReceiveRecommendAdapter(this, friends);
		} else {
			groupLeaguers = new ArrayList<GroupLeaguerBean>();
			rrAdapter = new ReceiveRecommendAdapter(this, groupLeaguers, type);
		}
		lv.setAdapter(rrAdapter);
		lv.setonLoadMoreListener(this);

		title.setText(groupName);

		rec_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void getIntentMesage() {
		Bundle b = getIntent().getExtras();
		groupId = b.getInt("GroupId", 0);
		groupName = b.getString("GroupName");
		recommendId = b.getInt("RecommendId", 0);
		createTime = b.getLong("CreateTime");

		type = b.getInt("type", -1);
		allNum = b.getInt("allNum", 0);
	}

	/**
	 * 接口数据的线程加载类
	 * 
	 * @author zhangkai
	 * 
	 */
	class LoadInfoTask extends AsyncTask<String, Integer, MCResult> {

		@Override
		protected MCResult doInBackground(String... params) {
			MCResult mcResult = null;
			try {
				if (type == -1) {
					mcResult = APIRequestServers.recommendList(
							ReceiveRecommendActivity.this, groupId + "",
							recommendId + "", friends.size() + "", "20");
				} else {
					mcResult = APIRequestServers.leaguresNewOrRemove(
							ReceiveRecommendActivity.this, groupId + "",
							"false", type, groupLeaguers.size() + "", "20",
							createTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (result == null) {
				T.show(App.app, T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					T.show(App.app, T.ErrStr);
				} else {
					if (type == -1) {
						MapRecommendFriendBean mapFriendBean = (MapRecommendFriendBean) result
								.getResult();
						allNum = mapFriendBean.getNUM();
						title.setText(groupName + "（" + allNum + "人）");
						friends.addAll(mapFriendBean.getLIST());
						rrAdapter.notifyDataSetChanged();
						L.d(TAG, "LoadInfoTask size=" + friends.size());
						if (allNum > friends.size()) {
							hasMore = true;
						} else {
							hasMore = false;
						}
					} else {
						MapGroupLeaguerBean mapFriendBean = (MapGroupLeaguerBean) result
								.getResult();
						title.setText(groupName + "（" + allNum + "人）");
						try {
							if (mapFriendBean != null)
								if (type == 0) {
									groupLeaguers.addAll(mapFriendBean
											.getNewLeaguers());
								} else {
									groupLeaguers.addAll(mapFriendBean
											.getRemovedLeaguers());
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
						rrAdapter.notifyDataSetChanged();
						L.d(TAG, "LoadInfoTask size=" + groupLeaguers.size());
						if (allNum > groupLeaguers.size()) {
							hasMore = true;
						} else {
							hasMore = false;
						}
					}
					lv.onRefreshComplete();
				}
			}
		}

	}

	@Override
	public void onLoadMore() {
		if (hasMore) {
			LoadInfoTask loadInfoTask = new LoadInfoTask();
			loadInfoTask.execute();
		} else {
			lv.hideLoadFooter();
		}
	}

}
