package com.datacomo.mc.spider.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.FriendListAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class FriendListByGroupActivity extends BaseAct implements
		OnItemClickListener {
	private static final String TAG = "FriendListByGroupActivity";

	private final int SIZE_PAGE = 20;
	private int num_Total;
	private int num_Start;
	private int num_Page;
	private String id_Group;
	private boolean start_Init;
	private boolean can_UpLoad;
	private boolean upload_End;
	private boolean refreshable;

	private TextView textView;

	private List<Object> objects_FriendList;
	private List<Object> temp_objects_FriendList;

	private FriendListAdapter adapter_FriendList;

	private ListView lv_FriendList;
	private LinearLayout pView;

	@Override
	protected void onDestroy() {
		handler_FriendList.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.friendlist);
		initView();
		setTitle("", R.drawable.title_fanhui, R.drawable.title_home);
		initData();
		bindListener();
	}

	private void initView() {
		textView = (TextView) findViewById(R.id.textView);
		lv_FriendList = (ListView) findViewById(R.id.layout_friend_group_friendlist);
		lv_FriendList.setOnItemClickListener(this);

		// pView = (LinearLayout)
		// LayoutInflater.from(this).inflate(R.layout.foot,
		// null);
		pView = (LinearLayout) findViewById(R.id.loadingView);
	}

	/**
	 * 初始化数据
	 **/
	private void initData() {
		Bundle bundle = getIntentMsg();
		num_Total = bundle.getInt("num_Total");
		id_Group = bundle.getString("id_Group");
		String name_Group = bundle.getString("name_Group");
		// 显示title
		setTitle(name_Group, R.drawable.title_fanhui, R.drawable.title_home);
		if (num_Total > 0) {
			textView.setVisibility(View.GONE);
			refreshable = true;
			startRequest();
		} else {
			textView.setText("该朋友圈中还没有您的朋友！");
			textView.setVisibility(View.VISIBLE);
			pView.setVisibility(View.GONE);
			refreshable = false;
		}
	}

	/**
	 * 绑定监听事件
	 **/
	private void bindListener() {
		lv_FriendList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount == totalItemCount)
						&& (totalItemCount != 0)) {
					if (refreshable) {
						if (upload_End) {
							refreshable = false;
						}
						uploadFriendList();
					}
				}
			}
		});
	}

	/**
	 * 获取intent
	 * 
	 * @return String[]
	 **/
	private Bundle getIntentMsg() {
		Bundle bundle = null;
		Intent intent = getIntent();
		if (intent != null) {
			bundle = intent.getExtras();
		}
		return bundle;
	}

	/**
	 * 创建Handler消息队列
	 * 
	 * @return Handler
	 **/
	private Handler handler_FriendList = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// try {
			L.d(TAG, "requestUploadInfo 加载完成……");
			pView.setVisibility(View.GONE);
			lv_FriendList.removeFooterView(pView);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			switch (msg.what) {
			case 0: //
				showToast((String) msg.obj);
				break;
			case 1: // 更新朋友列表
				num_Page++;
				if (start_Init) {
					objects_FriendList = (List<Object>) msg.obj;
					L.i(TAG, objects_FriendList.size() + "");
					if (objects_FriendList.size() == 0) {
						textView.setText("该朋友圈中还没有您的朋友！");
						textView.setVisibility(View.VISIBLE);
					} else {
						showToast("亲，没有更多数据了哦!");
					}
					adapter_FriendList = new FriendListAdapter(
							FriendListByGroupActivity.this, objects_FriendList,
							lv_FriendList);
					lv_FriendList.setAdapter(adapter_FriendList);
					upload_End = true;
					uploadFriendList();
				} else {
					temp_objects_FriendList = (List<Object>) msg.obj;
					upload_End = true;
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 更新朋友列表信息
	 **/
	private void uploadFriendList() {
		if (upload_End) {
			upload_End = false;
			if (start_Init) {
				if (can_UpLoad) {
					requestUploadInfo();
					refreshable = true;
				}
				start_Init = false;
			} else {

				if (temp_objects_FriendList != null) {
					objects_FriendList.addAll(temp_objects_FriendList);
					adapter_FriendList.notifyDataSetChanged();
					temp_objects_FriendList = null;
					if (can_UpLoad) {
						refreshable = true;
						requestUploadInfo();
					} else {
						showToast("亲，没有更多数据了哦!");
					}
				} else {
					if (objects_FriendList.size() == 0) {
						textView.setText("该朋友圈中还没有您的朋友！");
						textView.setVisibility(View.VISIBLE);
					} else {
						showToast("亲，没有更多数据了哦!");
					}

				}
			}
		}
	}

	/**
	 * 联网获取数据
	 */
	private void requestUploadInfo() {
		// try {
		L.d(TAG, "requestUploadInfo 加载中……");
		pView.setVisibility(View.VISIBLE);
		// lv_FriendList.addFooterView(pView);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		requestFriendListByGroup();
	}

	/**
	 * 请求更多朋友数据
	 **/
	private void requestFriendListByGroup() {
		if (num_Page < (num_Total / SIZE_PAGE)) {
			getFriendListByGroup(num_Start, SIZE_PAGE);
			num_Start = num_Start + SIZE_PAGE;
			can_UpLoad = true;
		} else {
			getFriendListByGroup(num_Start, num_Total % SIZE_PAGE);
			can_UpLoad = false;
		}
	}

	/**
	 * 获取朋友列表
	 * 
	 * @param startRecord
	 * @param maxResults
	 */
	private void getFriendListByGroup(final int startRecord,
			final int maxResults) {
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.friendListByGroup(
							FriendListByGroupActivity.this,
							String.valueOf(id_Group),
							String.valueOf(startRecord),
							String.valueOf(maxResults));
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						@SuppressWarnings("unchecked")
						List<Object> objects = (List<Object>) mcResult
								.getResult();
						L.i(TAG,
								"getFriendListByGroup objects.size="
										+ objects.size());
						sendHandlerObjMsg(1, objects);
					} else {
						sendHandlerErrStrMsg();
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendHandlerErrStrMsg();
				}
			}

		}.start();
	}

	private void startRequest() {
		num_Page = 0;
		num_Start = 0;
		start_Init = true;
		can_UpLoad = false;
		upload_End = false;
		requestUploadInfo();
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerErrStrMsg() {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = T.ErrStr;
		handler_FriendList.sendMessage(msg);
	}

	/**
	 * 发送对象信息
	 * 
	 * @param object
	 *            需要发送的对象信息
	 **/
	private void sendHandlerObjMsg(int id, Object object) {
		Message msg = Message.obtain();
		msg.what = id;
		msg.obj = object;
		handler_FriendList.sendMessage(msg);
	}

	/**
	 * 获取string文件中的文字 返回值：获取到得文字
	 * 
	 * @param id
	 *            int類型 string文件中的id
	 * @return String
	 **/
	private String getResourcesString(int id) {
		return getResources().getString(id);
	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 *            需要显示的信息
	 **/
	private void showToast(String text) {
		T.show(FriendListByGroupActivity.this, text);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FriendBean fb = (FriendBean) objects_FriendList.get(position);
		Bundle b = new Bundle();
		b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
		b.putString("id", fb.getMemberId() + "");
		b.putString("name", fb.getFriendName());
		LogicUtil.enter(this, HomePgActivity.class, b, false);
	}

	@Override
	protected void onLeftClick(View v) {
		finish();
	}

	@Override
	protected void onRightClick(View v) {
		LogicUtil.enter(this, InfoWallActivity.class, null, true);
	}
}
