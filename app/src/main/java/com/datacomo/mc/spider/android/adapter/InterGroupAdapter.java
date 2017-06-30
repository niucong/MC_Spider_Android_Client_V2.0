package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class InterGroupAdapter extends BaseAdapter {
	private Context c;
	private ArrayList<GroupEntity> beans;
	private boolean isSort;
	public final String TAG = "-1";

	public InterGroupAdapter(Context context, ArrayList<GroupEntity> groups,
			ListView listView, boolean isSort) {
		c = context;
		this.isSort = isSort;
		if (isSort) {
			beans = format(groups);
		} else {
			beans = groups;
		}
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final GroupEntity bean = beans.get(position);
		if (TAG.equals(bean.getId())) {
			TextView t = new TextView(c);
			t.setBackgroundResource(R.drawable.item_title);
			t.setPadding(10, 0, 0, 0);
			t.setTextColor(Color.WHITE);
			t.setText(bean.getName());
			return t;
		}
		ViewHolder holder = null;
		if (null == convertView || convertView instanceof TextView) {
			holder = new ViewHolder();
			convertView = View.inflate(c, R.layout.item_inter_group, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.poster = (ImageView) convertView.findViewById(R.id.head_img);
			holder.type = (ImageView) convertView.findViewById(R.id.group_type);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = bean.getName();
		String poster = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getFullHeadPath(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(name);
		holder.poster.setTag(position + poster);
		MyFinalBitmap.setPoster(c, holder.poster, poster);

		// 圈子类型(对应数据库中的group_type)：1-兴趣圈 2-企业圈 3-开放主页
		// 4-外部社区(对应数据库中的groupType字段)
		Resources res = c.getResources();
		// if ("2".equals(bean.getOpenStatus())) {
		// holder.type.setVisibility(View.VISIBLE);
		// } else {
		holder.type.setVisibility(View.GONE);
		// }

		Drawable drawableRight_type = null;
		int groupProperty = bean.getGroupProperty();
		if (groupProperty == 4) {
			drawableRight_type = res.getDrawable(R.drawable.group_out);
		} else {
			int groupType = bean.getGroupType();
			if (groupType == 2) {
				drawableRight_type = res.getDrawable(R.drawable.group_school);
			} else if (groupType == 3) {
				drawableRight_type = res.getDrawable(R.drawable.group_company);
			} else if (groupType == 4) {
				drawableRight_type = res
						.getDrawable(R.drawable.group_community);
			} else {
				drawableRight_type = res.getDrawable(R.drawable.nothing);
			}
		}
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawableRight_type.setBounds(0, 0,
				drawableRight_type.getMinimumWidth() / 2,
				drawableRight_type.getMinimumHeight() / 2);
		holder.name.setCompoundDrawables(null, null, drawableRight_type, null); // 设置右图标

		if (isSort)
			holder.details.setVisibility(View.GONE);
		else
			holder.details.setVisibility(View.VISIBLE);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("Id", String.valueOf(bean.getId()));
				LogicUtil.enter(c, HomeGpActivity.class, b, false);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView poster, type, details;
		TextView name;
		// LinearLayout details;
	}

	@Override
	public void notifyDataSetChanged() {
		if (isSort) {
			beans = format(beans);
		}
		super.notifyDataSetChanged();
	}

	public void ChangeTag(boolean showTag) {
		isSort = showTag;
	}

	public void notify(boolean isShowTag) {
		if (isShowTag) {
			beans = format(beans);
		}
		super.notifyDataSetChanged();
	}

	@SuppressLint("UseSparseArrays")
	private ArrayList<GroupEntity> format(ArrayList<GroupEntity> groups) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		HashMap<Integer, GroupEntity> tags = new HashMap<Integer, GroupEntity>();
		for (int i = 0; i < groups.size(); i++) {
			GroupEntity g = checkTag(groups, i);
			if (null != g) {
				index.add(i);
				tags.put(i, g);
			}
		}
		for (int i = index.size() - 1; i >= 0; i--) {
			int k = index.get(i);
			groups.add(k, tags.get(k));
		}
		return groups;
	}

	private GroupEntity checkTag(ArrayList<GroupEntity> groups, int i) {
		if (null == groups || i < 0 || i >= groups.size()) {
			return null;
		} else if (i == 0) {
			return new GroupEntity(TAG, getFirstGroupName(groups.get(i)), "",
					"", false, false);
		} else {
			String preName = getFirstGroupName(groups.get(i - 1));
			String curName = getFirstGroupName(groups.get(i));
			if (null != preName && null != curName && !preName.equals(curName)) {
				return new GroupEntity(TAG, curName, "", "", false, false);
			}
		}
		return null;
	}

	private String getFirstGroupName(GroupEntity group) {
		if (null == group) {
			return "#";
		}
		String f = group.getGroupNamePy();
		if (null == group.getGroupNamePy() || f.length() <= 1) {
			return "#";
		}
		char c = group.getGroupNamePy().trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("[a-zA-Z]{1}+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
}
