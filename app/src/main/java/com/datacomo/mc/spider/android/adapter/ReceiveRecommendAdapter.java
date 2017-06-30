package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class ReceiveRecommendAdapter extends BaseAdapter {
	private Context context;
	private List<FriendBean> friends;

	private List<GroupLeaguerBean> groupLeaguers;

	public ReceiveRecommendAdapter(Context context, List<FriendBean> friends) {
		this.context = context;
		this.friends = friends;
	}

	public ReceiveRecommendAdapter(Context context,
			List<GroupLeaguerBean> groupLeaguers, int type) {
		this.context = context;
		this.groupLeaguers = groupLeaguers;
	}

	@Override
	public int getCount() {
		if (friends != null) {
			return friends.size();
		} else {
			return groupLeaguers.size();
		}
	}

	@Override
	public Object getItem(int location) {
		if (friends != null) {
			return friends.get(location);
		} else {
			return groupLeaguers.get(location);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_receive, null);
			holder = new ViewHolder();
			holder.head_img = (ImageView) convertView
					.findViewById(R.id.head_img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.sex = (ImageView) convertView.findViewById(R.id.sex);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String headUrl, name;
		int sex, id;

		if (friends != null) {
			FriendBean friendBean = friends.get(position);
			headUrl = friendBean.getMemberHeadUrl()
					+ friendBean.getMemberHeadPath();
			name = friendBean.getMemberName();
			sex = friendBean.getSex();
			id = friendBean.getMemberId();
		} else {
			GroupLeaguerBean friendBean = groupLeaguers.get(position);
			headUrl = friendBean.getMemberHeadUrl()
					+ friendBean.getMemberHeadPath();
			name = friendBean.getMemberName();
			sex = friendBean.getMemberSex();
			id = friendBean.getMemberId();
		}
		headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(headUrl,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setHeader(context, holder.head_img, headUrl);
		holder.name.setText(name);
		if (1 == sex) {
			holder.sex.setVisibility(View.VISIBLE);
			holder.sex.setImageResource(R.drawable.icon_sex_boy);
		} else if (2 == sex) {
			holder.sex.setVisibility(View.VISIBLE);
			holder.sex.setImageResource(R.drawable.icon_sex_girl);
		} else {
			holder.sex.setVisibility(View.INVISIBLE);
		}

		final int id0 = id;

		holder.head_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("id", id0 + "");
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				LogicUtil.enter(context, HomePgActivity.class, b, false);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("id", id0 + "");
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				LogicUtil.enter(context, HomePgActivity.class, b, false);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView name;
		ImageView sex;
		TextView address;
		ImageView head_img;
	}

}
