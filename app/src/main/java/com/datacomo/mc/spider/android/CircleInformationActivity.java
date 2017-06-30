package com.datacomo.mc.spider.android;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.db.GroupListService;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.CircleView;

public class CircleInformationActivity extends BasicActionBarActivity implements
		OnClickListener {
	private final static String TAG = "CircleInformationActivity";

	private MCResult mcResult;
	private String groupId;
	private GroupBean groupBean;
	private TextView circle_name, circle_attribute, circle_time, circle_loc,
			circle_groupShort, circle_article_number, circle_pic_number,
			circle_file_number;

	private TextView circle_introduction, circle_lable;
	private LinearLayout ci_llItem;
	private LinearLayout circle_blog, circle_pic, circle_file,
			circle_groupShort_layout;

	private TextView tv_circleName, tv_circleAttribute, tv_circleTime,
			tv_circleIntroduction, tv_circleLable;

	private View view;
	private CircleView cv;

	private String groupShortFull = "";
	// private int groupMasterId;
	private String[] s;

	private boolean noOpen;

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.circle_information);
		// setTitle("", R.drawable.title_fanhui, null);
		ab.setTitle("");
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		s = getIntentMsg();
		groupId = s[0];
		if (s[1].equals("OpenPageId")) {
			tv_circleName.setText("主页名称：");
			tv_circleAttribute.setText("主页属性：");
			tv_circleTime.setText("创建时间：");
			tv_circleIntroduction.setText("主页描述：");
			tv_circleLable.setText("主页标签：");
			circle_groupShort_layout.setVisibility(View.GONE);
		} else {
			findViewById(R.id.circle_introduction2).setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化方法
	 */
	private void init() {

		circle_name = (TextView) findViewById(R.id.circle_name);
		circle_attribute = (TextView) findViewById(R.id.circle_attribute);
		circle_time = (TextView) findViewById(R.id.circle_time);
		circle_loc = (TextView) findViewById(R.id.circle_loc);
		circle_groupShort = (TextView) findViewById(R.id.circle_groupShort);
		circle_article_number = (TextView) findViewById(R.id.circle_article_number);
		circle_pic_number = (TextView) findViewById(R.id.circle_pic_number);
		circle_file_number = (TextView) findViewById(R.id.circle_file_number);
		circle_introduction = (TextView) findViewById(R.id.circle_introduction);
		circle_lable = (TextView) findViewById(R.id.circle_lable);
		ci_llItem = (LinearLayout) findViewById(R.id.ci_llItem);
		circle_blog = (LinearLayout) findViewById(R.id.circle_blog);
		circle_pic = (LinearLayout) findViewById(R.id.circle_pic);
		circle_file = (LinearLayout) findViewById(R.id.circle_file);

		tv_circleName = (TextView) findViewById(R.id.tv_circleName);
		tv_circleAttribute = (TextView) findViewById(R.id.tv_circleAttribute);
		tv_circleTime = (TextView) findViewById(R.id.tv_circleTime);
		tv_circleIntroduction = (TextView) findViewById(R.id.tv_circleIntroduction);
		tv_circleLable = (TextView) findViewById(R.id.tv_circleLable);

		circle_groupShort_layout = (LinearLayout) findViewById(R.id.circle_groupShort_layout);
		circle_groupShort_layout.setOnClickListener(this);

		circle_blog.setOnClickListener(this);
		circle_pic.setOnClickListener(this);
		circle_file.setOnClickListener(this);
	}

	/**
	 * 获取圈子的基本信息
	 * 
	 * @param context
	 *            上下文；
	 * @param groupId
	 *            圈子ID；
	 * @throws Exception
	 */
	private void getCircleInfo(MCResult mcResult) {
		// mcResult = APIRequestServers.groupInfo(context, groupId);
		groupBean = (GroupBean) mcResult.getResult();
		setTextInfo();
	}

	/**
	 * 根据获取到的bean去显示相关的文本数据
	 */
	private void setTextInfo() {
		circle_name.setText(groupBean.getGroupName());

		if (groupBean.getOpenStatus() == 1) {
			circle_attribute.setText("公开");
		} else if (groupBean.getOpenStatus() == 2) {
			circle_attribute.setText("私密");
		} else {
			circle_attribute.setText("自定义");
		}

		try {
			circle_time.setText(ConstantUtil.YYYYMMDD.format(DateTimeUtil
					.getLongTime(groupBean.getCreateTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String loc = GroupListService.getService(this).getLocation(groupId);
		if (loc != null && !"".equals(loc))
			circle_loc.setText(loc);

		groupShortFull = ConstantUtil.GROUPSHORT + groupBean.getGroupShort();
		circle_groupShort.setText(groupShortFull);

		circle_article_number.setText(groupBean.getPosterNum() + "");
		circle_pic_number.setText(groupBean.getPhotoNum() + "");
		circle_file_number.setText(groupBean.getFileNum() + "");
		String info = groupBean.getGroupDescription();
		if (info != null) {
			info = groupBean.getGroupDescription().replace("&nbsp;", "");
			circle_introduction.setText(info);
		}

		circle_lable.setText(groupBean.getGroupTag());
		if (s[1].equals("OpenPageId")) {
			// if (groupBean.getMemberId() == GetDbInfoUtil
			// .getMemberId(CircleInformationActivity.this)) {
			// setTitle("主页信息", R.drawable.title_fanhui, R.drawable.title_edit);
			// } else {
			// setTitle("主页信息", R.drawable.title_fanhui, null);
			ab.setTitle("主页信息");
			// }
		} else {
			String joinGroupStatus = groupBean.getJoinGroupStatus();
			L.d(TAG, "setTextInfo joinGroupStatus=" + joinGroupStatus);
			if ("GROUP_OWNER".equals(joinGroupStatus)
					|| "GROUP_MANAGER".equals(joinGroupStatus)) {
				menu.findItem(R.id.action_edit).setIcon(R.drawable.action_edit);
				// setTitle("圈子信息", R.drawable.title_fanhui,
				// R.drawable.title_edit);
				// } else {
				// setTitle("圈子信息", R.drawable.title_fanhui, null);
			}
			ab.setTitle("圈子信息");
		}

	}

	/**
	 * 获取圈子管理员的信息
	 * 
	 * @param mcResult
	 */
	private void getGroupManager(MCResult mcResult) {
		@SuppressWarnings("unchecked")
		ArrayList<Object> objectList = (ArrayList<Object>) mcResult.getResult();
		if (objectList != null) {
			// ciAdapter = new
			// CircleInformationAdapter(CircleInformationActivity.this,objectList);
			// ci_listView.setAdapter(ciAdapter);
			// setListViewHeightBasedOnChildren(ci_listView);
			int glSize = objectList.size();
			if (glSize == 1 && cv == null) {
				GroupLeaguerBean groupLeaguer = (GroupLeaguerBean) objectList
						.get(0);
				cv = new CircleView(CircleInformationActivity.this,
						groupLeaguer, R.drawable.pl_foot);
				view = cv.getView();
				ci_llItem.addView(view);
				// groupMasterId = cv.getGroupMasterId();

			} else if (cv == null) {
				for (int i = 0; i < glSize; i++) {
					GroupLeaguerBean groupLeaguer = (GroupLeaguerBean) objectList
							.get(i);
					if (i == 0) {
						cv = new CircleView(CircleInformationActivity.this,
								groupLeaguer, R.drawable.kuangshang);
					} else if (i == glSize - 1) {
						cv = new CircleView(CircleInformationActivity.this,
								groupLeaguer, R.drawable.kuangxia);
					} else {
						cv = new CircleView(CircleInformationActivity.this,
								groupLeaguer, R.drawable.kuangzhong);
					}
					view = cv.getView();
					ci_llItem.addView(view);
					// if (cv.getGroupMasterId() != 0) {
					// groupMasterId = cv.getGroupMasterId();
					// }
				}
			}
		}
	}

	/**
	 * 算出ListView的总高度并进行设置。
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;// if without this statement,the listview will be a
							// little short
		listView.setLayoutParams(params);
	}

	/**
	 * 获取intent
	 * 
	 * @return String
	 **/
	private String[] getIntentMsg() {
		String[] s = new String[2];
		Bundle b = getIntent().getExtras();
		String Id = b.getString("Id");
		String To = b.getString("To");
		noOpen = b.getBoolean("noOpen");
		s[0] = Id;
		s[1] = To;
		return s;
	}

	class loadInfoTask extends AsyncTask<Void, Void, MCResult> {
		private Context context;
		private String groupId;
		private int label;

		public loadInfoTask(Context context, String groupId, int label) {
			this.context = context;
			this.groupId = groupId;
			this.label = label;
		}

		@Override
		protected MCResult doInBackground(Void... params) {
			if (label == 1) {
				try {
					mcResult = APIRequestServers.groupInfo(context, groupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (label == 2) {
				try {
					mcResult = APIRequestServers.groupManagerList(context,
							groupId, "0", "10", false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (result == null) {
				showTip(T.ErrStr);
			} else {
				if (result.getResultCode() != 1) {
					showTip(T.ErrStr);
				} else {
					isMethodRun(label, result);
				}
			}
		}

	}

	private void isMethodRun(int label, MCResult mcResult) {
		try {
			if (label == 1) {
				getCircleInfo(mcResult);
			} else if (label == 2) {
				getGroupManager(mcResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showTip(T.ErrStr);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.circle_groupShort_layout:// 对讲频率
			if (!"".equals(groupShortFull)
					&& !"groupShort".equals(groupShortFull)) {
				Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
						+ groupShortFull.trim()));
				this.startActivity(it);
			}
			break;
		case R.id.circle_blog:// 圈子动态
			sendIntent(0);
			break;
		case R.id.circle_file:// 圈子文件
			sendIntent(1);
			break;
		case R.id.circle_pic:// 圈子照片
			sendIntent(2);
			break;
		default:
			break;
		}
	}

	private void sendIntent(int type) {
		if (noOpen) {
			Intent i = new Intent();
			i.putExtra("type", type);
			setResult(RESULT_OK, i);
			finish();
		} else {
			Bundle b_file = new Bundle();
			b_file.putString("id", String.valueOf(s[0]));
			b_file.putString("to", s[1]);
			b_file.putInt("type", type);
			b_file.putString("name", groupBean.getGroupName());
			LogicUtil.enter(CircleInformationActivity.this,
					SubGroupActivity.class, b_file, false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		MenuItem mi = menu.findItem(R.id.action_edit);
		mi.setVisible(true);
		mi.setIcon(R.drawable.nothing);
		this.menu = menu;
		// getCircleInfo(CircleInformationActivity.this, "7160");
		new loadInfoTask(CircleInformationActivity.this, groupId, 1).execute();
		new loadInfoTask(CircleInformationActivity.this, groupId, 2).execute();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			Bundle b = new Bundle();
			b.putString("id", groupId);
			b.putString("name", groupBean.getGroupName());
			b.putString("introduction", groupBean.getGroupDescription());
			b.putString("label", groupBean.getGroupTag());
			String groupPost = groupBean.getGroupPosterUrl()
					+ groupBean.getGroupPosterPath();
			String groupPostTh = ThumbnailImageUrl.getThumbnailPostUrl(
					groupPost, PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
			b.putString("grouPost", groupPostTh);
			LogicUtil.enter(CircleInformationActivity.this,
					CircleEditorActivity.class, b, false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// Bundle b = new Bundle();
	// b.putString("id", groupId);
	// b.putString("name", groupBean.getGroupName());
	// b.putString("introduction", groupBean.getGroupDescription());
	// b.putString("label", groupBean.getGroupTag());
	// String groupPost = groupBean.getGroupPosterUrl()
	// + groupBean.getGroupPosterPath();
	// String groupPostTh = ThumbnailImageUrl.getThumbnailPostUrl(groupPost,
	// PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
	// b.putString("grouPost", groupPostTh);
	// LogicUtil.enter(CircleInformationActivity.this,
	// CircleEditorActivity.class, b, false);
	// }

}
