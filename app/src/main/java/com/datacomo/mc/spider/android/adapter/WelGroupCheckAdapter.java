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
import com.datacomo.mc.spider.android.net.been.TakedClientWelcomPicGroupBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class WelGroupCheckAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<TakedClientWelcomPicGroupBean> infos;

	private TakedClientWelcomPicGroupBean groupBean;

	public WelGroupCheckAdapter(Context context,
			ArrayList<TakedClientWelcomPicGroupBean> notices, ListView listview) {
		mContext = context;
		infos = notices;
	}

	public TakedClientWelcomPicGroupBean getGroupBean() {
		return groupBean;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
		// ivs.add(holder);

		holder.join.setText("加入圈子");
		final TakedClientWelcomPicGroupBean bean = infos.get(position);
		String memberName = bean.getGroupName();
		holder.name.setText(memberName);

		if (bean.getIsUsed() == 1) {
			holder.details.setImageResource(R.drawable.single2);
			groupBean = bean;
		} else {
			holder.details.setImageResource(R.drawable.single1);
		}

		String head = bean.getGroupPosterUrl() + bean.getGroupPosterPath();
		head = ThumbnailImageUrl.getThumbnailPostUrl(head,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.head.setTag(head);
		holder.join.setVisibility(View.GONE);
		MyFinalBitmap.setPoster(mContext, holder.head, head);

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("Id", String.valueOf(bean.getGroupId()));
				LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				groupBean = bean;
				bean.setIsUsed(1);
				for (int i = 0; i < infos.size(); i++) {
					if (i == position) {
						infos.remove(i);
						infos.add(i, bean);
					} else {
						TakedClientWelcomPicGroupBean wgb = infos.get(i);
						if (wgb.getIsUsed() == 1) {
							wgb.setIsUsed(0);
							infos.remove(i);
							infos.add(i, wgb);
						}
					}
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView head, details;
		TextView text, name, join;
	}
}
