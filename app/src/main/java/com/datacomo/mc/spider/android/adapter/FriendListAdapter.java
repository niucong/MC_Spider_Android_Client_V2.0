package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.MemberBasicBean;
import com.datacomo.mc.spider.android.net.been.MemberBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class FriendListAdapter extends BaseAdapter {
	private static final String TAG = "FriendListAdapter";
	private Context context;
	private List<Object> objects;
	private ListView listView;
	private FriendBean bean_Friend;
	private MemberBean bean_member;
	private ViewHolder holder;
	private HashMap<String, String> map_Tag;

	public FriendListAdapter(Context context, List<Object> objects,
			ListView listView) {
		this.context = context;
		this.objects = objects;
		this.listView = listView;
		this.map_Tag = new HashMap<String, String>();
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.friendgrouplistform_itemform, null);
			holder.img_Head = (ImageView) convertView
					.findViewById(R.id.head_img);
			holder.txt_Name = (TextView) convertView
					.findViewById(R.id.friendgrouplistform_itemform_name);
			holder.txt_feel = (TextView) convertView
					.findViewById(R.id.friendgrouplistform_itemform_feel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Object object = getItem(position);
		setView(object);

		return convertView;
	}

	private void setView(Object object) {
		String head_path = "";
		String memberId = "";
		String memberName = "";
		String moodContent = "";

		if (object instanceof FriendBean) {
			bean_Friend = (FriendBean) object;
			head_path = bean_Friend.getFullHeadPath();
			memberId = bean_Friend.getMemberId() + "";
			memberName = bean_Friend.getMemberName();
			moodContent = bean_Friend.getMoodContent();
		} else if (object instanceof MemberBean) {
			bean_member = (MemberBean) object;
			MemberBasicBean mBean = bean_member.getBasicInfo();
			if (mBean != null) {
				MemberHeadBean mHeadBean = mBean.getHeadImage();
				if (mHeadBean != null)
					head_path = mHeadBean.getHeadUrl()
							+ mHeadBean.getHeadPath();
				memberId = mBean.getMemberId() + "";
				memberName = mBean.getMemberName();
				moodContent = mBean.getFeelingWord();
			}
		}

		head_path = ThumbnailImageUrl.getThumbnailHeadUrl(head_path,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.img_Head.setTag(head_path);
		map_Tag.put(head_path, memberId);
		// holder.img_Head.setBackgroundDrawable(new
		// AsyncImageDownLoad(head_path,
		// new String[] { head_path }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
		// ConstantUtil.HEAD_PATH, context,"friendlistadapter", new
		// ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// ImageView imageViewByTag = (ImageView) listView
		// .findViewWithTag(tags[0]);
		// if (imageViewByTag != null) {
		// Drawable imageDrawable = (Drawable) object;
		// if (null != imageDrawable) {
		// imageViewByTag
		// .setBackgroundDrawable(imageDrawable);
		// } else {
		// imageViewByTag
		// .setBackgroundDrawable(context
		// .getResources()
		// .getDrawable(
		// TaskUtil.HEADDEFAULTLOADSTATEIMG[1]));
		// }
		// }
		// }
		// }).getDrawable());
		// TODO
		MyFinalBitmap.setHeader(context, holder.img_Head, head_path);

		final String name = memberName;
		holder.img_Head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String temp = map_Tag.get(v.getTag().toString());
				L.d(TAG, temp);
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", temp);
				b.putString("name", name);
				LogicUtil.enter(context, HomePgActivity.class, b, false);
			}
		});
		holder.txt_Name.setText(memberName);
		holder.txt_feel.setText(moodContent);
	}

	public class ViewHolder {
		public ImageView img_Head;
		public TextView txt_Name;
		public TextView txt_feel;
	}
}
