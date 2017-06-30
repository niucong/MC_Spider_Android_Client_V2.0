package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIThemeRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceGreatBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class DissertationInfoActivity extends BasicActionBarActivity implements
		OnClickListener {
	private static final String LOG_TAG = "DisstertationInfoActivity";

	// 声明变量
	private final int MaxPersenNum = 99;// 最大人数
	private String id_Dissertaion;
	private int mId_Member;
	// 声明引用类
	// 声明组件
	private TextView mTxt_Name;
	private TextView mTxt_CreateTime;
	private TextView mTxt_UpdateTime;
	private TextView mTxt_NullBrowse;
	private TextView mTxt_NullRegard;
	private TextView mTxt_NullShared;
	private TextView mTxt_BrowseNum;
	private TextView mTxt_RegardNum;
	private TextView mTxt_SharedNum;
	private ImageView mIv_Head;
	private List<ImageView> mIv_Browses;
	private List<ImageView> mIv_Regards;
	private List<ImageView> mIv_Shareds;
	private LinearLayout mLayout_BrowseBox;
	private LinearLayout mLayout_RegardBox;
	private LinearLayout mLayout_SharedBox;
	private RelativeLayout mLayout_HeadBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.layout_dissertationinfo);
		// setTitle("专题信息", R.drawable.title_fanhui, null);
		ab.setTitle("专题信息");
		initView();
		initData();

	}

	private void initView() {
		mTxt_Name = (TextView) findViewById(R.id.layout_dissertation_txt_name);
		mTxt_CreateTime = (TextView) findViewById(R.id.layout_dissertation_txt_createtime);
		mTxt_UpdateTime = (TextView) findViewById(R.id.layout_dissertation_txt_updatetime);
		mTxt_NullBrowse = (TextView) findViewById(R.id.layout_dissertation_txt_nullbrowse);
		mTxt_NullRegard = (TextView) findViewById(R.id.layout_dissertation_txt_nullregard);
		mTxt_NullShared = (TextView) findViewById(R.id.layout_dissertation_txt_nullshared);
		mTxt_BrowseNum = (TextView) findViewById(R.id.layout_dissertation_txt_browse);
		mTxt_RegardNum = (TextView) findViewById(R.id.layout_dissertation_txt_regard);
		mTxt_SharedNum = (TextView) findViewById(R.id.layout_dissertation_txt_shared);
		mIv_Head = (ImageView) findViewById(R.id.layout_dissertation_img_head);
		List<Integer> ids = new ArrayList<Integer>();
		mIv_Browses = new ArrayList<ImageView>();
		ids.add(R.id.layout_dissertation_img_browse1);
		ids.add(R.id.layout_dissertation_img_browse2);
		ids.add(R.id.layout_dissertation_img_browse3);
		ids.add(R.id.layout_dissertation_img_browse4);
		ids.add(R.id.layout_dissertation_img_browse5);
		initImageList(mIv_Browses, ids);
		ids.clear();
		mIv_Regards = new ArrayList<ImageView>();
		ids.add(R.id.layout_dissertation_img_regard1);
		ids.add(R.id.layout_dissertation_img_regard2);
		ids.add(R.id.layout_dissertation_img_regard3);
		ids.add(R.id.layout_dissertation_img_regard4);
		ids.add(R.id.layout_dissertation_img_regard5);
		initImageList(mIv_Regards, ids);
		ids.clear();
		mIv_Shareds = new ArrayList<ImageView>();
		ids.add(R.id.layout_dissertation_img_shared1);
		ids.add(R.id.layout_dissertation_img_shared2);
		ids.add(R.id.layout_dissertation_img_shared3);
		ids.add(R.id.layout_dissertation_img_shared4);
		ids.add(R.id.layout_dissertation_img_shared5);
		initImageList(mIv_Shareds, ids);
		ids.clear();
		ids = null;
		mLayout_BrowseBox = (LinearLayout) findViewById(R.id.layout_dissertation_llayout_browsebox);
		mLayout_RegardBox = (LinearLayout) findViewById(R.id.layout_dissertation_llayout_regardbox);
		mLayout_SharedBox = (LinearLayout) findViewById(R.id.layout_dissertation_llayout_sharedbox);
		mLayout_HeadBox = (RelativeLayout) findViewById(R.id.layout_dissertation_llayout_headbox);
	}

	private void initData() {
		id_Dissertaion = getIntent().getExtras().getString(
				BundleKey.ID_DISSERTATION);
		spdDialog.showProgressDialog("加载中...");
		new RequestTask(0).execute(id_Dissertaion);
	}

	private void bindListener() {
		mLayout_HeadBox.setOnClickListener(this);
		mLayout_BrowseBox.setOnClickListener(this);
		mLayout_RegardBox.setOnClickListener(this);
		mLayout_SharedBox.setOnClickListener(this);
	}

	private void initImageList(List<ImageView> images, List<Integer> ids) {
		for (int id : ids) {
			images.add((ImageView) findViewById(id));
		}
	}

	@Override
	protected void onDestroy() {
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	// @Override
	// protected void onLeftClick(View v) {
	// finish();
	// }
	//
	// @Override
	// protected void onRightClick(View v) {
	// }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Bundle bundle;
		switch (v.getId()) {
		case R.id.layout_dissertation_llayout_headbox:
			bundle = new Bundle();
			bundle.putInt("type", BaseMidMenuActivity.TYPE_MBER);
			bundle.putString("id", String.valueOf(mId_Member));
			bundle.putString("name", mTxt_Name.getText().toString());
			LogicUtil.enter(DissertationInfoActivity.this,
					HomePgActivity.class, bundle, false);
			break;
		case R.id.layout_dissertation_llayout_browsebox:
			bundle = new Bundle();
			bundle.putString(BundleKey.ID_DISSERTATION, id_Dissertaion);
			bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.BROWSE);
			LogicUtil.enter(DissertationInfoActivity.this,
					MemberListActivity.class, bundle, false);
			break;
		case R.id.layout_dissertation_llayout_regardbox:
			bundle = new Bundle();
			bundle.putString(BundleKey.ID_DISSERTATION, id_Dissertaion);
			bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.REGARD);
			LogicUtil.enter(DissertationInfoActivity.this,
					MemberListActivity.class, bundle, false);
			break;
		case R.id.layout_dissertation_llayout_sharedbox:
			bundle = new Bundle();
			bundle.putString(BundleKey.ID_DISSERTATION, id_Dissertaion);
			bundle.putSerializable(BundleKey.TYPE_REQUEST, Type.SHARED);
			LogicUtil.enter(DissertationInfoActivity.this,
					MemberListActivity.class, bundle, false);
			break;
		}
	}

	class RequestTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;
		private int mType;

		/**
		 * @param type
		 *            0 request info,1 request visitor,2 request regard,3
		 *            request shared
		 */
		public RequestTask(int type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			mParams = params;
			MCResult mcResult = null;
			switch (mType) {
			case 0:
				try {
					mcResult = APIThemeRequestServers.themeInfo(App.app,
							(String) mParams[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
			case 2:
			case 3:
				try {
					mcResult = APIThemeRequestServers
							.themeFocusOrBrowseOrShareList(App.app,
									(String) mParams[0], (String) mParams[1],
									"0", (String) mParams[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			return mcResult;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			switch (mType) {
			case 0:
				spdDialog.cancelProgressDialog(null);
				if (null == result || result.getResultCode() != 1) {
					T.show(App.app, T.ErrStr);
					return;
				}
				GroupThemeBean groupThemeBean = (GroupThemeBean) result
						.getResult();
				if (null == groupThemeBean) {
					T.show(App.app, T.ErrStr);
					return;
				}
				mId_Member = groupThemeBean.getMemberId();
				mTxt_Name.setText(groupThemeBean.getMemberName());
				String time = "";
				L.d(LOG_TAG, groupThemeBean.getCreateTime());
				time = DateTimeUtil.aTimeFormat(DateTimeUtil
						.getLongTime(groupThemeBean.getCreateTime()));
				mTxt_CreateTime.setText(time);
				L.d(LOG_TAG, groupThemeBean.getUpdateTime());
				time = groupThemeBean.getUpdateTime();
				if (null == time || "".equals(time))
					time = DateTimeUtil.aTimeFormat(DateTimeUtil
							.getLongTime(groupThemeBean.getCreateTime()));
				else
					time = DateTimeUtil.aTimeFormat(DateTimeUtil
							.getLongTime(time));
				mTxt_UpdateTime.setText(time);
				String url = groupThemeBean.getMemberUrl()
						+ groupThemeBean.getMemberPath();
				url = ThumbnailImageUrl.getThumbnailHeadUrl(url,
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				MyFinalBitmap.setHeader(App.app, mIv_Head, url);
				new RequestTask(1).execute(id_Dissertaion, String.valueOf(2),
						String.valueOf(5));
				new RequestTask(2).execute(id_Dissertaion, String.valueOf(1),
						String.valueOf(5));
				new RequestTask(3).execute(id_Dissertaion, String.valueOf(3),
						String.valueOf(5));
				bindListener();
				break;
			case 1:
			case 2:
			case 3:
				if (null == result || result.getResultCode() != 1) {
					return;
				}
				MapResourceGreatBean mapResourceGreatBean = (MapResourceGreatBean) result
						.getResult();
				if (null == mapResourceGreatBean) {
					return;
				}
				List<ResourceGreatBean> list = mapResourceGreatBean.getLIST();
				StringBuffer strBuffer = new StringBuffer();
				int size = 0;
				size = mapResourceGreatBean.getCOUNT();
				L.d(LOG_TAG, "onPostExecute size:" + size);
				int length = 0;
				String path = null;
				int index = 0;
				switch (mType) {
				case 1:
					if (size == 0) {
						mTxt_NullBrowse.setText("暂时还没有人看过");
						return;
					}
					mLayout_BrowseBox.setVisibility(View.VISIBLE);
					mTxt_NullBrowse.setVisibility(View.GONE);
					strBuffer.append("浏览");
					strBuffer.append(" ");
					strBuffer.append("(");
					if (size > MaxPersenNum) {
						strBuffer.append(MaxPersenNum);
						strBuffer.append("+");
					} else {
						strBuffer.append(size);
					}
					strBuffer.append(")");
					mTxt_BrowseNum.setText(strBuffer.toString().trim());
					length = strBuffer.length();
					strBuffer.delete(0, length);
					path = "";
					index = 0;
					for (ResourceGreatBean bean : list) {
						mIv_Browses.get(index).setVisibility(View.VISIBLE);
						path = bean.getMemberHeadUrl()
								+ bean.getMemberHeadPath();
						path = ThumbnailImageUrl.getThumbnailHeadUrl(path,
								HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
						MyFinalBitmap.setHeader(App.app,
								mIv_Browses.get(index), path);
						index++;
					}
					break;
				case 2:
					if (size == 0) {
						mTxt_NullRegard.setText("暂时还没有人关注");
						return;
					}
					mLayout_RegardBox.setVisibility(View.VISIBLE);
					mTxt_NullRegard.setVisibility(View.GONE);
					strBuffer.append("关注");
					strBuffer.append(" ");
					strBuffer.append("(");
					if (size > MaxPersenNum) {
						strBuffer.append(MaxPersenNum);
						strBuffer.append("+");
					} else {
						strBuffer.append(size);
					}
					strBuffer.append(")");
					mTxt_RegardNum.setText(strBuffer.toString().trim());
					length = strBuffer.length();
					strBuffer.delete(0, length);
					path = "";
					index = 0;
					for (ResourceGreatBean bean : list) {
						mIv_Regards.get(index).setVisibility(View.VISIBLE);
						path = bean.getMemberHeadUrl()
								+ bean.getMemberHeadPath();
						path = ThumbnailImageUrl.getThumbnailHeadUrl(path,
								HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
						MyFinalBitmap.setHeader(App.app,
								mIv_Regards.get(index), path);
						index++;
					}
					break;
				case 3:
					if (size == 0) {
						mTxt_NullShared.setText("暂时还没有分享过");
						return;
					}
					mLayout_SharedBox.setVisibility(View.VISIBLE);
					mTxt_NullShared.setVisibility(View.GONE);
					strBuffer.append("向");
					if (size > MaxPersenNum) {
						strBuffer.append(MaxPersenNum);
						strBuffer.append("+");
					} else {
						strBuffer.append(size);
					}
					strBuffer.append("人分享过");
					mTxt_SharedNum.setText(strBuffer.toString().trim());
					length = strBuffer.length();
					strBuffer.delete(0, length);
					path = "";
					index = 0;
					for (ResourceGreatBean bean : list) {
						mIv_Shareds.get(index).setVisibility(View.VISIBLE);
						path = bean.getMemberHeadUrl()
								+ bean.getMemberHeadPath();
						path = ThumbnailImageUrl.getThumbnailHeadUrl(path,
								HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
						MyFinalBitmap.setHeader(App.app,
								mIv_Shareds.get(index), path);
						index++;
					}
					break;
				}
				break;
			}
		}
	}

}
