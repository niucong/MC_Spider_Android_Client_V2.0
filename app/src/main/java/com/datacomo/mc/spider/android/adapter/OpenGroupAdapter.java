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

import com.datacomo.mc.spider.android.HomePageActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class OpenGroupAdapter extends BaseAdapter {
	private ArrayList<GroupBean> groups;
	private ListView list;
	private Context c;

	public OpenGroupAdapter(Context context, ArrayList<GroupBean> mbers,
			ListView listView) {
		this.c = context;
		this.groups = mbers;
		this.list = listView;
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
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
			convertView = LayoutInflater.from(c).inflate(
					R.layout.item_open_group, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.post = (ImageView) convertView.findViewById(R.id.head_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GroupBean bean = groups.get(position);
		final String name = bean.getGroupName();
		final String id = "" + bean.getGroupId();
		String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getGroupPosterUrl() + bean.getGroupPosterPath(),
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(name);

		holder.post.setTag(position + headUrl);
		// Drawable headDrawable = new AsyncImageDownLoad(headUrl,
		// new String[] { headUrl }, TaskUtil.POSTERDEFAULTLOADSTATEIMG,
		// ConstantUtil.POSTER_PATH, c,"opengroupadapter", new ImageCallback() {
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

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("Id", id);
				b.putString("To", "OpenPageId");
				LogicUtil.enter(c, HomePageActivity.class, b, false);

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
		TextView name;
		ImageView post;
	}

}