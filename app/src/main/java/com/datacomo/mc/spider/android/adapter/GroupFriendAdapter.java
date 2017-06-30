package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupFriendAdapter extends BaseAdapter {
	private ArrayList<FriendBean> members;
	private ListView list;
	private Context c;
	public final int TAG = -1;

	public GroupFriendAdapter(Context context, ArrayList<FriendBean> mbers,
			ListView listView) {
		this.c = context;
		this.members = mbers;
		this.list = listView;
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final FriendBean bean = members.get(position);
		ViewHolder holder = null;
		if (null == convertView || convertView instanceof TextView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(c).inflate(
					R.layout.item_group_friend, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.mood = (TextView) convertView.findViewById(R.id.mood);
			holder.post = (ImageView) convertView.findViewById(R.id.head_img);
			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String name = bean.getMemberName();
		final String fName = bean.getFriendName();
		final String id = "" + bean.getMemberId();
		String mood = bean.getMoodContent();
		int sex = bean.getSex();
		String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
				bean.getMemberHeadUrl() + bean.getMemberHeadPath(),
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		if (null != fName && !fName.equals(name)) {
			holder.name.setText(fName + " （" + name + "）");
		} else {
			holder.name.setText(name + " ");
		}
		if (1 == sex) {
			holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, c
					.getResources().getDrawable(R.drawable.icon_sex_boy), null);
		} else if (2 == sex) {
			holder.name
					.setCompoundDrawablesWithIntrinsicBounds(
							null,
							null,
							c.getResources().getDrawable(
									R.drawable.icon_sex_girl), null);
		} else {
			holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null,
					null, null);
		}

		holder.mood.setText(mood);
		holder.post.setTag(position + headUrl);
		// Drawable headDrawable = new AsyncImageDownLoad(headUrl,
		// new String[] { headUrl }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
		// ConstantUtil.HEAD_PATH, c, "groupfriendadapter",new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// loadHeadImg(ImageDealUtil.getPosterCorner(
		// (Drawable) object, 4), position + (String)tags[0]);
		// }
		// }).getDrawable();
		// holder.post.setImageDrawable(ImageDealUtil.getPosterCorner(
		// headDrawable, 4));
		// TODO
		MyFinalBitmap.setPoster(c, holder.post, headUrl);

		holder.post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", id);
				b.putString("name", name);
				LogicUtil.enter(c, HomePgActivity.class, b, false);
			}
		});
		return convertView;
	}

	private void loadHeadImg(Drawable imageDrawable, String imageUrl) {
		if (list != null) {
			ImageView imgView = (ImageView) list.findViewWithTag(imageUrl);
			if (null != imgView) {
				if (null != imageDrawable) {
					imgView.setImageDrawable(imageDrawable);
				}
			}
		}
	}

	class ViewHolder {
		TextView name, mood;
		ImageView post, check, sex;
	}
}
