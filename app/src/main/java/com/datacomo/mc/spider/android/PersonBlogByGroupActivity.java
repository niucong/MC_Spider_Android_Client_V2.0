package com.datacomo.mc.spider.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.adapter.SelectTopicAdapter;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.fragmt.GroupOrMemberSpan;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ApiDiffCoding;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.TypeUtil;

public class PersonBlogByGroupActivity extends BasicActionBarActivity {
	private static final String TAG = "PersonBlogByGroupActivity";
	// 声明变量
	private boolean start_Init;
	private final int SIZE_PAGE = PageSizeUtil.SIZEPAGE_PERSONBLOGBYGROUPACTIVITY;
	private int num_Start;
	private int id_Group;
	private int id_Member;
	private int num_Default = 500;
	// private boolean can_UpLoad;
	private boolean upload_End;
	private boolean refreshable;
	private boolean isFoot;
	// private String type_To;
	private String name_Person, groupName;
	private List<ResourceBean> beans_Resource;
	private List<ResourceBean> temp_Beans_Resource;
	// 声明引用类
	// private PersonBlogByGroupAdapter adapter_PersonBlogByGroup;
	private SelectTopicAdapter infoAdapter;
	// 声明组件
	private ListView lv_PersonBlogByGroup;
	private TextView txt_Num_Blog;
	private ImageView img_Head_Person;
	private LinearLayout pView;
	private LinearLayout lLayout_Footer;

	private String str1 = " 在 ";
	private String str2 = " 发表的圈博";
	private String str3 = " 发表了 ";
	private String str4 = " 篇圈博";

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		ApiDiffCoding.hardwareAcceleerated(this);
		setContent(R.layout.personblogbygroup);
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 **/
	private void initView() {
		lv_PersonBlogByGroup = (ListView) findViewById(R.id.personsblog_list);
		img_Head_Person = (ImageView) findViewById(R.id.head_img);
		txt_Num_Blog = (TextView) findViewById(R.id.personblogbygroup_blognum);
		pView = (LinearLayout) findViewById(R.id.loadingView);
		lLayout_Footer = (LinearLayout) getLayoutInflater().inflate(
				R.layout.foot, null);
	}

	/**
	 * 初始化数据
	 **/
	private void initData() {
		Bundle bundle = getIntentMsg();
		name_Person = bundle.getString("name_Person");
		groupName = bundle.getString("groupName");
		String path_Head = bundle.getString("path_Head");
		id_Member = bundle.getInt("id_Member");
		id_Group = bundle.getInt("id_Group");
		MyFinalBitmap.setHeader(this, img_Head_Person, path_Head);

		// setTitle(name_Person, R.drawable.title_fanhui,
		// R.drawable.title_home);
		ab.setTitle(name_Person);

		if (groupName == null || "".equals(groupName)) {
			groupName = "本圈";
		}
		int j1 = name_Person.length();
		int j2 = j1 + str1.length();
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(name_Person + str1 + groupName + str2);
		ssb.setSpan(new GroupOrMemberSpan(this, "MemberId#" + id_Member), 0,
				j1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int j3 = j2 + groupName.length();
		ssb.setSpan(new GroupOrMemberSpan(this, "GroupId#" + id_Group), j2, j3,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		L.d(TAG, "creatHandler ssb=" + ssb);
		txt_Num_Blog.setText(ssb);
		txt_Num_Blog.setMovementMethod(LinkMovementMethod.getInstance());
		// txt_Num_Blog.setText(name_Person
		// + getResourcesString(R.string.personblogbygroup_info));

		L.d(TAG, "initData path_Head=" + path_Head);
		startRequest();
	}

	/**
	 * 绑定监听事件
	 **/
	private void bindListener() {
		lv_PersonBlogByGroup.setOnScrollListener(new OnScrollListener() {
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
							upload_End = false;
							uploadInfo();
						} else {
							isFoot = true;
						}
					}
				}
			}
		});
		lv_PersonBlogByGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == id) {
					ResourceBean bean_Resource = (ResourceBean) parent
							.getItemAtPosition(position);
					Bundle bundle = new Bundle();
					bundle.putSerializable("info", bean_Resource);
					bundle.putString("type_From", TypeUtil.type_Resource);
					enter(CircleBlogDetailsActivity.class, bundle, false, 0,
							false);
				}
			}
		});
	}

	/**
	 * 获取intent
	 * 
	 * @return Bundle
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
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: //
				showTip((String) msg.obj);
				break;
			case 1: // 接收返回资源数据
				if (start_Init) {
					start_Init = false;
					if (beans_Resource != null)
						beans_Resource.clear();
					MapResourceBean mapBean = (MapResourceBean) msg.obj;
					beans_Resource = mapBean.getLIST();
					boolean isHasMore = beans_Resource.size() >= SIZE_PAGE;
					int num = mapBean.getTOTALNUM();
					String numStr = "";
					if (num < num_Default) {
						numStr += num;
					} else {
						numStr = num_Default + "+";
					}
					int j1 = name_Person.length();
					int j2 = j1 + str1.length();
					SpannableStringBuilder ssb = new SpannableStringBuilder();
					ssb.append(name_Person + str1 + groupName + str3 + numStr
							+ str4);
					ssb.setSpan(new GroupOrMemberSpan(
							PersonBlogByGroupActivity.this, "MemberId#"
									+ id_Member), 0, j1,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					int j3 = j2 + groupName.length();
					ssb.setSpan(new GroupOrMemberSpan(
							PersonBlogByGroupActivity.this, "GroupId#"
									+ id_Group), j2, j3,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					L.d(TAG, "creatHandler ssb=" + ssb);
					txt_Num_Blog.setText(ssb);
					txt_Num_Blog.setMovementMethod(LinkMovementMethod
							.getInstance());

					// adapter_PersonBlogByGroup = new
					// PersonBlogByGroupAdapter(
					// beans_Resource, PersonBlogByGroupActivity.this,
					// getHandler());

					if (isHasMore) {
						lv_PersonBlogByGroup.addFooterView(lLayout_Footer);
					}
					if (infoAdapter == null) {
						infoAdapter = new SelectTopicAdapter(
								PersonBlogByGroupActivity.this, beans_Resource,
								lv_PersonBlogByGroup, true);
						lv_PersonBlogByGroup.setAdapter(infoAdapter);
					}
					bindListener();
					pView.setVisibility(View.GONE);
					lv_PersonBlogByGroup.setVisibility(View.VISIBLE);
					if (isHasMore) {
						refreshable = true;
						requestUploadInfo();
					}
					infoAdapter.notifyDataSetChanged();
				} else {
					temp_Beans_Resource = ((MapResourceBean) msg.obj).getLIST();
					if (isFoot) {
						isFoot = false;
						refreshable = false;
						uploadInfo();
					} else {
						upload_End = true;
					}
				}
				break;
			case 2: // 刷新适配器
				infoAdapter.notifyDataSetChanged();
			default:
				break;
			}
		}
	};

	/**
	 * 更新数据
	 **/
	private void uploadInfo() {
		L.i(TAG, "uploadInfo start_Init=" + start_Init);
		if (null != temp_Beans_Resource) {
			// adapter_PersonBlogByGroup.addTransitionBeans(temp_Beans_Resource);
			beans_Resource.addAll(temp_Beans_Resource);
			if (temp_Beans_Resource.size() == SIZE_PAGE) {
				refreshable = true;
				requestUploadInfo();
			} else {
				L.d(TAG, "uploadInfo size=" + beans_Resource.size());
				lv_PersonBlogByGroup.removeFooterView(lLayout_Footer);
				free(lLayout_Footer);
				showTip("最后一页");
			}
			temp_Beans_Resource = null;
		} else {
			if (null != lLayout_Footer) {
				lv_PersonBlogByGroup.removeFooterView(lLayout_Footer);
				free(lLayout_Footer);
			}
			showTip("最后一页");
		}
		infoAdapter.notifyDataSetChanged();
	}

	/**
	 * 请求更新
	 **/
	private void requestUploadInfo() {
		requestInfo();
	}

	/**
	 * 请求圈子成员列表
	 **/
	private void requestInfo() {
		L.i(TAG, "requestInfo num_Start=" + num_Start + ",SIZE_PAGE="
				+ SIZE_PAGE);
		getInfo(num_Start, SIZE_PAGE);
		num_Start = num_Start + SIZE_PAGE;
	}

	/**
	 * 获取个人圈博列表
	 * 
	 * @param startRecord
	 *            final int类型 开始位置
	 * @param startRecord
	 *            final int类型 结束位置
	 **/
	private void getInfo(final int startRecord, final int maxResults) {
		pView.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.getGroupQuuBoLists(
							PersonBlogByGroupActivity.this,
							String.valueOf(id_Member),
							String.valueOf(id_Group),
							String.valueOf(startRecord),
							String.valueOf(maxResults), false);
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						MapResourceBean mapBean = (MapResourceBean) mcResult
								.getResult();
						sendHandlerObjMsg(1, mapBean);
					} else {
						sendHandlerStrMsg();
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendHandlerStrMsg();
				}
			}

		}.start();
	}

	/**
	 * 初始化标记
	 * 
	 * @param num_Start
	 * @param start_Init
	 */
	private void startRequest() {
		// num_Page = 0;
		num_Start = 0;
		start_Init = true;
		upload_End = false;
		refreshable = false;
		isFoot = false;
		requestUploadInfo();
	}

	/**
	 * 跳转界面
	 * 
	 * @param cls
	 * @param bundle
	 * @param isFinish
	 */
	protected void enter(Class<?> cls, Bundle bundle, Boolean isReturn,
			int code, boolean isFinish) {
		Intent intent = new Intent(this, cls);
		if (null != bundle)
			intent.putExtras(bundle);
		if (isReturn) {
			startActivityForResult(intent, code);
		} else {
			startActivity(intent);
			if (isFinish) {
				finish();
			}
		}
	}

	/**
	 * 发送反馈的文字信息
	 * 
	 * @param msg
	 *            需要反馈的文字信息
	 **/
	private void sendHandlerStrMsg() {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.obj = T.ErrStr;
		handler.sendMessage(msg);
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
		handler.sendMessage(msg);
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// LogicUtil.enter(this, InfoWallActivity.class, null, true);
	// }

	/**
	 * 释放对象
	 * 
	 * @param object
	 */
	private void free(Object object) {
		object = null;
	}
}
