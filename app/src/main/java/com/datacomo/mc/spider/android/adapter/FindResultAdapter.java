package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.ChooseGroupsDialogActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.ChooseGroupBean;
import com.datacomo.mc.spider.android.db.UserBusinessDatabase;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.service.UpdateFriendListThread;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class FindResultAdapter extends BaseAdapter {

	private static Context mContext;
	private ArrayList<MemberBean> noticeInfo;
	private SpinnerProgressDialog spdDialog;

	public FindResultAdapter(Context context, ArrayList<MemberBean> notices) {
		mContext = context;
		noticeInfo = notices;
		spdDialog = new SpinnerProgressDialog(mContext);
	}

	@Override
	public int getCount() {
		return noticeInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return noticeInfo.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_find_result, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.sex = (ImageView) convertView.findViewById(R.id.sex);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.btn = (Button) convertView.findViewById(R.id.operate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MemberBean bean = noticeInfo.get(position);
		int friendState = bean.getFriendStatus();

		MemberBasicBean basic = bean.getBasicInfo();
		String mName = basic.getMemberName();
		String fName = bean.getFriendRemarkName();
		if (fName != null && !"".equals(fName)) {
			if (!fName.equals(mName)) {
				holder.name.setText(fName + "（" + mName + "）");
			} else {
				holder.name.setText(fName);
			}
			mName = fName;
		} else {
			holder.name.setText(mName);
		}
		final String memberName = mName;

		MemberHeadBean basicHead = basic.getHeadImage();
		String head = basicHead.getHeadUrl() + basicHead.getHeadPath();
		head = ThumbnailImageUrl.getThumbnailHeadUrl(head,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.head.setTag(position + head);

		int sex = basic.getMemberSex();
		if (1 == sex) {
			holder.sex.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.icon_sex_boy));
		} else if (2 == sex) {
			holder.sex.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.icon_sex_girl));
		} else {
			holder.sex.setVisibility(View.GONE);
			holder.sex.setImageDrawable(null);
		}

		final String memberId = bean.getMemberId() + "";
		try {
			String mberId = new UserBusinessDatabase(App.app)
					.getMemberId(App.app.share.getSessionKey());
			if (memberId.equals(mberId)) {
				friendState = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (friendState == 1) {
			if (1 == sex) {
				holder.btn.setText("去他地盘");
			} else {
				holder.btn.setText("去她地盘");
			}
		} else if (friendState == 4) {
			holder.btn.setText("加入朋友圈");
		}
		holder.btn.setTag(friendState);
		// Drawable headDrawable = new AsyncImageDownLoad(head,
		// new String[] { head }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
		// ConstantUtil.HEAD_PATH, mContext, "findresultadapter",
		// new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// loadHeadImg(ImageDealUtil.getPosterCorner(
		// (Drawable) object, 4), position
		// + (String) tags[0]);
		// }
		// }).getDrawable();
		// holder.head.setImageDrawable(ImageDealUtil.getPosterCorner(
		// headDrawable, 4));
		// TODO
		MyFinalBitmap.setHeader(mContext, holder.head, head);
		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", memberId);
				b.putString("name", memberName);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});

		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch ((Integer) (v.getTag())) {
				case 1:
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", memberId);
					b.putString("name", memberName);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
					break;
				case 4:
					ChooseGroupBean bean = new ChooseGroupBean();
					bean.setName(new String[] { memberName }, null);
					try {
						bean.setTitle("加入朋友圈");
						bean.setMemberId(Integer.parseInt(memberId));
					} catch (Exception e) {
						e.printStackTrace();
					}
					bean.setChosenGroupMap(new HashMap<String, Object>());
					Bundle bundle = new Bundle();
					bundle.putSerializable(BundleKey.CHOOSEGROUPBEAN, bean);
					bundle.putInt(BundleKey.POSITION, position);
					LogicUtil.enter(mContext, ChooseGroupsDialogActivity.class,
							bundle, 1);

					break;
				default:
					break;
				}

			}
		});
		return convertView;
	}

	public void changeGroups(String[] chosenIds, int position, String memberId) {
		spdDialog.showProgressDialog("正在添加中...");
		new AddFriendTask(Type.ADDFRIEND).execute(position,
				String.valueOf(memberId), chosenIds);
	}

	class AddFriendTask extends AsyncTask<Object, Integer, MCResult> {
		private Object[] mParams;
		private Type mType;

		public AddFriendTask(Type type) {
			mType = type;
		}

		@Override
		protected MCResult doInBackground(Object... params) {
			MCResult result = null;
			mParams = params;
			try {
				switch (mType) {
				case ADDFRIEND:
					result = APIRequestServers.addFriendToGroup(mContext,
							(String) mParams[1], (String[]) mParams[2]);
					break;
				case ADDFRIENDTOGROUP:
					result = APIRequestServers.addFriendsToGroup(App.app,
							(String[]) mParams[1], (String[]) mParams[2],
							"false");
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult mcResult) {
			super.onPostExecute(mcResult);
			spdDialog.cancelProgressDialog(null);
			if (null == mcResult || mcResult.getResultCode() != 1) {
				T.show(App.app, T.ErrStr);
				return;
			}
			T.show(App.app, "已添加！");
			ChooseGroupsDialogActivity.setIsNeedRefresh(true);
			int index = (Integer) mParams[0];
			noticeInfo.get(index).setFriendStatus(1);
			notifyDataSetChanged();
			switch (mType) {
			case ADDFRIEND:
				new Thread(new Runnable() {
					@Override
					public void run() {
						UpdateFriendListThread.updateFriendList(mContext, null);
					}
				}).start();
				break;

			default:
				break;
			}
		}
	}

	static class ViewHolder {
		ImageView head, sex;
		TextView name;
		Button btn;
	}

}
