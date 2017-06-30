package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.FriendsIdActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class FriendsIdAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<GroupLeaguerBean> allObjectList;
	// private ArrayList<Integer> index = new ArrayList<Integer>();
	@SuppressLint("UseSparseArrays")
	private LinkedHashMap<Integer, GroupLeaguerBean> map = new LinkedHashMap<Integer, GroupLeaguerBean>();

	public FriendsIdAdapter(Context context,
			ArrayList<GroupLeaguerBean> allObjectList, ListView listView) {
		this.context = context;
		this.allObjectList = allObjectList;

		if (context instanceof FriendsIdActivity) {
			posterBar = (LinearLayout) ((FriendsIdActivity) context)
					.findViewById(R.id.content);
			btn = (TextView) ((FriendsIdActivity) context)
					.findViewById(R.id.ok);
		}
	}

	@Override
	public int getCount() {
		return allObjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_friends_chooser, null);
			holder.head_img = (ImageView) convertView
					.findViewById(R.id.head_img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.sex = (ImageView) convertView.findViewById(R.id.sex);
			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GroupLeaguerBean bean = allObjectList.get(position);
		final String name = bean.getMemberName();
		int sex = bean.getMemberSex();
		final String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
				bean.getFullHeadPath(), HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
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

		holder.head_img.setTag(position + headUrl);
		MyFinalBitmap.setHeader(context, holder.head_img, headUrl);
		final int mId = bean.getMemberId();
		if (map.containsKey(mId)) {
			holder.check.setVisibility(View.VISIBLE);
		} else {
			holder.check.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (map.containsKey(mId)) {
					map.remove(mId);
					v.findViewById(R.id.check).setVisibility(View.GONE);
					setBtnText();
					cutPoster(mId);
				} else {
					map.put(mId, bean);
					v.findViewById(R.id.check).setVisibility(View.VISIBLE);
					setBtnText();
					addPoster(mId, headUrl, name);
				}
			}
		});

		return convertView;
	}

	public LinkedHashMap<Integer, GroupLeaguerBean> getMap() {
		return map;
	}

	// public ArrayList<Integer> getIndexList() {
	// Object[] sort = index.toArray();
	// Arrays.sort(sort);
	// ArrayList<Integer> newList = new ArrayList<Integer>();
	// for (int i = 0; i < sort.length; i++) {
	// newList.add((Integer) sort[i]);
	// }
	// return newList;
	// }

	public void clearIndexList() {
		map.clear();
	}

	class ViewHolder {
		ImageView head_img;
		TextView name;
		ImageView sex;
		ImageView check;
	}

	private LinearLayout posterBar;
	private TextView btn;

	// private boolean checkPosterTag(String tag) {
	// if (null == tag) {
	// return true;
	// }
	// for (int i = 0; i < posterBar.getChildCount(); i++) {
	// View child = posterBar.getChildAt(i);
	// if (tag == child.getTag() || tag.equals(child.getTag())) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private void prePosterBar() {
	// for (final int tag : map.keySet()) {
	// if (!checkPosterTag(tag + "")) {
	// GroupLeaguerBean bean = map.get(tag);
	// ViewGroup barIcon = (ViewGroup) LayoutInflater.from(context)
	// .inflate(R.layout.img_head, null);
	// ImageView poster = (ImageView) barIcon
	// .findViewById(R.id.head_img);
	// barIcon.setTag(tag);
	// poster.setTag("poster" + tag);
	// String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
	// bean.getMemberHeadUrl() + bean.getMemberHeadPath(),
	// HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
	// MyFinalBitmap.setHeader(context, poster, headUrl);
	//
	// poster.setScaleType(ScaleType.FIT_XY);
	// addPosterToBar(barIcon);
	// }
	// }
	// }

	private void addPoster(int tag, String headUrl, String name) {
		if (null != posterBar) {
			ViewGroup barIcon = (ViewGroup) LayoutInflater.from(context)
					.inflate(R.layout.img_head, null);
			ImageView poster = (ImageView) barIcon.findViewById(R.id.head_img);
			barIcon.setTag(tag);
			poster.setTag("poster" + tag);
			MyFinalBitmap.setHeader(context, poster, headUrl);
			poster.setScaleType(ScaleType.FIT_XY);
			addPosterToBar(barIcon);
		}
	}

	private void setBtnText() {
		if (null == btn) {
			return;
		}
		if (map.size() > 0) {
			btn.setBackgroundResource(R.drawable.btn_chooser_green);
			btn.setText("确定(" + map.size() + ")");
		} else {
			btn.setBackgroundResource(R.drawable.btn_chooser_white);
			btn.setText("取 消");
		}
	}

	private void addPosterToBar(View v) {
		LayoutParams lp = new LayoutParams(DensityUtil.dip2px(
				context, 40), DensityUtil.dip2px(context, 40));
		lp.setMargins(4, 5, 4, 5);
		if (posterBar.getChildCount() > 0) {
			posterBar.addView(v, posterBar.getChildCount() - 1, lp);
		} else {
			posterBar.addView(v, lp);
		}
		posterBar.requestLayout();
		HorizontalScrollView hView = ((HorizontalScrollView) posterBar
				.getParent());
		posterBar.measure(
				MeasureSpec.EXACTLY + posterBar.getWidth()
						+ hView.getMeasuredHeight(), MeasureSpec.EXACTLY
						+ posterBar.getHeight());
		posterBar.layout(0, 0, posterBar.getMeasuredWidth(),
				posterBar.getMeasuredHeight());
		hView.scrollBy(posterBar.getMeasuredWidth(), 0);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cutPoster((Integer) arg0.getTag());
				map.remove((Integer) arg0.getTag());
				notifyDataSetChanged();
				setBtnText();
			}
		});
	}

	private boolean cutPoster(int tag) {
		if (null == posterBar) {
			return false;
		}
		for (int i = 0; i < posterBar.getChildCount(); i++) {
			View child = posterBar.getChildAt(i);
			if (tag == (Integer) child.getTag()) {
				posterBar.removeViewAt(i);
				return true;
			}
		}
		return false;
	}

}
