package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.GroupFriendActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;

public class FriendClubAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FriendGroupBean> datas;
	private int itemWidth;
	private static int[] bg = { R.drawable.bg_color_deeppurple,
			R.drawable.bg_color_green, R.drawable.bg_color_orange,
			R.drawable.bg_color_purple, R.drawable.bg_color_seablue,
			R.drawable.bg_color_shallowblackishgreen,
			R.drawable.bg_color_skyblue, R.drawable.bg_color_yellow };

	public FriendClubAdapter(Context context, ArrayList<FriendGroupBean> clubs,
			int screenWidth) {
		mContext = context;
		this.datas = clubs;
		itemWidth = screenWidth / 3 / 8 * 6;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_club, null);
			convertView.setLayoutParams(new GridView.LayoutParams(itemWidth,
					itemWidth));
		}
		FriendGroupBean bean = datas.get(position);
		final String clubName = bean.getGroupName();
		final int num = bean.getFriendNum();
		final int id = bean.getGroupId();
		TextView name_tv = (TextView) convertView
				.findViewById(R.id.item_club_name);
		name_tv.setText(clubName);
		TextView num_tv = (TextView) convertView
				.findViewById(R.id.item_club_num);
		num_tv.setText(num + "人");

		convertView.setBackgroundResource(bg[position % bg.length]);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(0 == num){
					T.show(mContext, "该圈子中还没有加入朋友！");
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString("id_Group", id + "");
				bundle.putInt("num_Total", num);
				bundle.putString("name_Group", clubName);
				LogicUtil.enter(mContext, GroupFriendActivity.class,
						bundle, false);
			}
		});
		return convertView;
	}

}
