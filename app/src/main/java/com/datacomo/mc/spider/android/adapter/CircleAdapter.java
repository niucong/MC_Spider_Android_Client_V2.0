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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class CircleAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> ids;
	private ArrayList<String> names;
	private ArrayList<String> urls;
	private int itemWidth;

	public CircleAdapter(Context context, ArrayList<String> id,
			ArrayList<String> name, ArrayList<String> url, GridView gridView,
			int screenWidth) {
		mContext = context;
		ids = id;
		names = name;
		urls = url;
		itemWidth = screenWidth / 3 / 8 * 6;
	}

	@Override
	public int getCount() {
		return ids.size();
	}

	@Override
	public Object getItem(int position) {
		return ids.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_circle, null);
		}
		final String clubName = names.get(position);
		final String id = ids.get(position);
		final String url = ThumbnailImageUrl.getThumbnailPostUrl(
				urls.get(position), PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		ImageView img = (ImageView) convertView.findViewById(R.id.circle_img);
		img.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
		img.setTag(position + url);
		TextView v = (TextView) convertView.findViewById(R.id.circle_text);
		v.setText(clubName);
		MyFinalBitmap.setPoster(mContext, img, url);

		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				// b.putSerializable("bean", oriOwnerBean);
				b.putString("Id", String.valueOf(id));
				LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
			}
		});
		return convertView;
	}

}
