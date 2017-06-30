package com.datacomo.mc.spider.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.application.ScreenManager;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APINoteRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.note.NoteInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class NoteCloudDetailsActivity extends BasicActionBarActivity {
	private static String TAG = "NoteCloudDetailsActivity";

	private TextView tv_book, tv_create, tv_update, tv_share, tv_friend_num,
			tv_group_num;

	private NoteInfoBean bean;

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		ScreenManager.getInctance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ScreenManager.getInctance().pushActivity(this);
		super.onCreate(savedInstanceState);
		setContent(R.layout.note_cloud_details);
		ab.setTitle("笔记详情");

		Bundle b = getIntent().getExtras();
		bean = (NoteInfoBean) b.getSerializable("diaryInfoBean");

		setData();
	}

	private void setData() {
		tv_book = (TextView) findViewById(R.id.note_cloud_details_book);
		tv_create = (TextView) findViewById(R.id.note_cloud_details_create);
		tv_update = (TextView) findViewById(R.id.note_cloud_details_update);
		tv_share = (TextView) findViewById(R.id.note_cloud_details_share);
		tv_friend_num = (TextView) findViewById(R.id.note_cloud_details_friend_num);
		tv_group_num = (TextView) findViewById(R.id.note_cloud_details_group_num);

		if (bean.getNotebookId() > 0)
			tv_book.setText(bean.getNoteBookName());
		tv_create.setText(DateTimeUtil.cTimeFormat(DateTimeUtil
				.getLongTime(bean.getCreateTime())));
		tv_update.setText(DateTimeUtil.cTimeFormat(DateTimeUtil
				.getLongTime(bean.getUpdateTime())));
		String name = bean.getShareMemberName();
		if (name != null && !"".equals(name)) {
			tv_share.setText(name);
		} else {
			findViewById(R.id.note_cloud_details_share_ll).setVisibility(
					View.GONE);
		}
		int mNum = bean.getShareMemberNum();
		if (mNum > 0) {
			tv_friend_num.setText("您向" + mNum + "人分享过");
			getMemberInfo();
		} else {
			findViewById(R.id.note_cloud_details_friend_ll).setVisibility(
					View.GONE);
		}
		int gNum = bean.getShareGroupNum();
		if (gNum > 0) {
			tv_group_num.setText("您分享过" + gNum + "个圈子");
			getGroupInfo();
		} else {
			findViewById(R.id.note_cloud_details_group_ll).setVisibility(
					View.GONE);
		}

		iv_member_more = (ImageView) findViewById(R.id.note_cloud_details_friend_more);
		iv_group_more = (ImageView) findViewById(R.id.note_cloud_details_group_more);
		iv_member_more.setOnClickListener(this);
		iv_group_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.note_cloud_details_friend_more:
			if (bean.getShareMemberNum() > 0) {
				Bundle b = new Bundle();
				b.putSerializable(BundleKey.TYPE_REQUEST, Type.NOTESHAREFRIEND);
				b.putString(BundleKey.ID_FILEMEMBERS, bean.getNoteId() + "");
				LogicUtil.enter(NoteCloudDetailsActivity.this,
						MemberListActivity.class, b, false);
			}
			break;
		case R.id.note_cloud_details_group_more:
			if (bean.getShareMemberNum() > 0) {
				Bundle b = new Bundle();
				b.putSerializable(BundleKey.TYPE_REQUEST, Type.NOTESHAREGROUP);
				b.putString(BundleKey.ID_FILEMEMBERS, bean.getNoteId() + "");
				LogicUtil.enter(NoteCloudDetailsActivity.this,
						MemberListActivity.class, b, false);
			}
			break;
		default:
			break;
		}
	}

	private void getGroupInfo() {
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APINoteRequestServers.shareGroupList(
							NoteCloudDetailsActivity.this, bean.getNoteId()
									+ "", "0", "3");
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						@SuppressWarnings("unchecked")
						ArrayList<ShareGroupInfoBean> objectList = (ArrayList<ShareGroupInfoBean>) mcResult
								.getResult();
						Message msg = new Message();
						msg.what = 2;
						msg.obj = objectList;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void getMemberInfo() {
		new Thread() {
			@Override
			public void run() {
				try {
					MCResult mcResult = APINoteRequestServers.shareMemberList(
							NoteCloudDetailsActivity.this, bean.getNoteId()
									+ "", "0", "3");
					int Code_result = mcResult.getResultCode();
					if (Code_result == 1) {
						@SuppressWarnings("unchecked")
						ArrayList<ShareLeaguerInfoBean> objectList = (ArrayList<ShareLeaguerInfoBean>) mcResult
								.getResult();
						Message msg = new Message();
						msg.what = 1;
						msg.obj = objectList;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setMemberInfo((ArrayList<ShareLeaguerInfoBean>) msg.obj);
				break;
			case 2:
				setGroupInfo((ArrayList<ShareGroupInfoBean>) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	private List<ImageView> ivs_member, ivs_group;
	private ImageView iv_member_more, iv_group_more;

	private void setGroupInfo(ArrayList<ShareGroupInfoBean> memberList) {
		if (memberList != null) {
			int mSize = memberList.size();
			if (mSize > 0) {
				ImageView iv1 = (ImageView) findViewById(R.id.note_cloud_details_group1);
				ImageView iv2 = (ImageView) findViewById(R.id.note_cloud_details_group2);
				ImageView iv3 = (ImageView) findViewById(R.id.note_cloud_details_group3);
				ivs_group = new ArrayList<ImageView>();
				ivs_group.add(iv1);
				ivs_group.add(iv2);
				ivs_group.add(iv3);

				if (mSize > 3)
					mSize = 3;
				for (int i = 0; i < mSize; i++) {
					final ShareGroupInfoBean bean = memberList.get(i);
					String u = ThumbnailImageUrl.getThumbnailHeadUrl(
							bean.getReceiveGroupHeadUrl()
									+ bean.getReceiveGroupHeadPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
					ivs_group.get(i).setVisibility(View.VISIBLE);
					if (!ivs_group.get(i).isDrawingCacheEnabled())
						MyFinalBitmap.setPosterCorner(
								NoteCloudDetailsActivity.this,
								ivs_group.get(i), u);
					ivs_group.get(i).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle b = new Bundle();
							b.putString("Id", bean.getReceiveGroupId() + "");
							LogicUtil.enter(NoteCloudDetailsActivity.this,
									HomeGpActivity.class, b, false);
						}
					});
					ivs_group.get(i).setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void setMemberInfo(ArrayList<ShareLeaguerInfoBean> memberList) {
		if (memberList != null) {
			int mSize = memberList.size();
			if (mSize > 0) {
				ImageView iv1 = (ImageView) findViewById(R.id.note_cloud_details_friend1);
				ImageView iv2 = (ImageView) findViewById(R.id.note_cloud_details_friend2);
				ImageView iv3 = (ImageView) findViewById(R.id.note_cloud_details_friend3);
				ivs_member = new ArrayList<ImageView>();
				ivs_member.add(iv1);
				ivs_member.add(iv2);
				ivs_member.add(iv3);

				if (mSize > 3)
					mSize = 3;
				for (int i = 0; i < mSize; i++) {
					final ShareLeaguerInfoBean bean = memberList.get(i);
					String u = ThumbnailImageUrl.getThumbnailHeadUrl(
							bean.getShareMemberHeadUrl()
									+ bean.getShareMemberHeadPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
					ivs_member.get(i).setVisibility(View.VISIBLE);
					if (!ivs_member.get(i).isDrawingCacheEnabled())
						MyFinalBitmap.setHeader(NoteCloudDetailsActivity.this,
								ivs_member.get(i), u);
					ivs_member.get(i).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle b = new Bundle();
							b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
							b.putString("id", bean.getShareMemberId() + "");
							b.putString("name", bean.getShareMemberName());
							LogicUtil.enter(NoteCloudDetailsActivity.this,
									HomePgActivity.class, b, false);
						}
					});
					ivs_member.get(i).setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
