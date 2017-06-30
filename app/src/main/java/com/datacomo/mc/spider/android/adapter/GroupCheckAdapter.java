package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

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

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupCheckAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<GroupBasicBean> infos;

	private boolean singleCheck = false;

	public GroupCheckAdapter(Context context,
			ArrayList<GroupBasicBean> notices, ListView listview) {
		mContext = context;
		infos = notices;
	}

	public void setSingleCheck(boolean singleCheck) {
		this.singleCheck = singleCheck;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_search_group, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.join = (TextView) convertView.findViewById(R.id.join);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.join.setText("加入圈子");
		final GroupBasicBean bean = infos.get(position);
		String memberName = bean.getGroupName();
		String state = bean.getJoinGroupStatus();
		String head = bean.getGroupPosterUrl() + bean.getGroupPosterPath();
		head = ThumbnailImageUrl.getThumbnailPostUrl(head,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(memberName);
		holder.head.setTag(head);

		if ("COOPERATION_LEAGUER".equals(state)) {
			holder.join.setVisibility(View.VISIBLE);
		} else {
			holder.join.setVisibility(View.GONE);
		}
		MyFinalBitmap.setPoster(mContext, holder.head, head);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (singleCheck) {

				} else {
					Bundle b = new Bundle();
					b.putString("Id", String.valueOf(bean.getGroupId()));
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView head, details;
		TextView text, name, join;
	}
}
