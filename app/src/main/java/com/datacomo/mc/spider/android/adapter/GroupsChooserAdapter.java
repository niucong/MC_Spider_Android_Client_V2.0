package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

import com.datacomo.mc.spider.android.GroupsChooserActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupsChooserAdapter extends BaseAdapter {
	private ArrayList<GroupEntity> groups;
	private ListView list;
	private Context c;
	public final String TAG = "-1";
	private LinearLayout posterBar;
	private TextView btn;

	private String btnString;

	public GroupsChooserAdapter(Context context, ArrayList<GroupEntity> mbers,
			ListView listView, boolean isSort, String btnString) {
		this.c = context;
		if (null != btnString && !"".equals(btnString)) {
			this.btnString = btnString;
		}
		if (isSort) {
			groups = format(mbers);
		} else {
			groups = mbers;
		}
		this.list = listView;
		if (c instanceof GroupsChooserActivity) {
			posterBar = (LinearLayout) ((GroupsChooserActivity) c)
					.findViewById(R.id.content);
			btn = (TextView) ((GroupsChooserActivity) c).findViewById(R.id.ok);
		}

		// View t = new View(c);
		// t.setBackgroundResource(R.drawable.create_new_blank);
		// t.setEnabled(false);
		// addPosterToBar(t);

		if (null != GroupsChooserActivity.chooseIds
				&& 0 < GroupsChooserActivity.chooseIds.size()) {
			setBtnText();
			prePosterBar();
		}
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
		final GroupEntity bean = groups.get(position);
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
			convertView = LayoutInflater.from(c).inflate(
					R.layout.item_group_chooser, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.post = (ImageView) convertView.findViewById(R.id.head_img);
			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String name = bean.getName();
		final String id = "" + bean.getId();
		final String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getHead(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.name.setText(name);

		holder.post.setTag(position + headUrl);
		MyFinalBitmap.setPoster(c, holder.post, headUrl);

		if (GroupsChooserActivity.chooseIds.containsKey(id)) {
			holder.check.setImageResource(R.drawable.choice_yes);
		} else {
			holder.check.setImageResource(R.drawable.choice_no);
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseData.hideKeyBoard((Activity) c);
				if (GroupsChooserActivity.chooseIds.containsKey(id)) {
					GroupsChooserActivity.chooseIds.remove(id);
					((ImageView) v.findViewById(R.id.check))
							.setImageResource(R.drawable.choice_no);
					setBtnText();
					cutPoster(id);
				} else {
					GroupsChooserActivity.chooseIds.put(id, bean);
					((ImageView) v.findViewById(R.id.check))
							.setImageResource(R.drawable.choice_yes);
					setBtnText();
					addPoster(id, headUrl, name);
				}
				notifyDataSetChanged();
				list.requestFocus();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView name;
		ImageView post, check, sex;
	}

	public HashMap<String, GroupEntity> getSelectedInfo() {
		return GroupsChooserActivity.chooseIds;
	}

	public String[] getSelectedIds() {
		return GroupsChooserActivity.chooseIds.keySet().toArray(new String[0]);
	}

	public LinkedHashMap<String, GroupEntity> getSelected() {
		return GroupsChooserActivity.chooseIds;
	}

	public String[] getSelectedNames() {
		String[] names = new String[GroupsChooserActivity.chooseIds.size()];
		int k = 0;
		for (GroupEntity bean : GroupsChooserActivity.chooseIds.values()) {
			String name = bean.getName();
			if (null == name) {
				name = "";
			}
			names[k] = name;
			k++;
		}
		return names;
	}

	public void notifyTags() {
		groups = format(groups);
		notifyDataSetChanged();
	}

	@SuppressLint("UseSparseArrays")
	private ArrayList<GroupEntity> format(ArrayList<GroupEntity> groups) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		HashMap<Integer, GroupEntity> tags = new HashMap<Integer, GroupEntity>();
		GroupEntity firstEntity = new GroupEntity(TAG, "最近访问的", "", "", false,
				false);
		index.add(0);
		tags.put(0, firstEntity);
		boolean hasRecent = false;
		for (int i = 0; i < groups.size(); i++) {
			if (!groups.get(i).isRecentCheck()) {
				GroupEntity g = checkTag(groups, i);
				if (null != g) {
					index.add(i);
					tags.put(i, g);
				}
			} else {
				if (!hasRecent)
					hasRecent = true;
			}
		}
		if (!hasRecent) {
			index.remove(0);
			tags.remove(firstEntity);
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
		} else if (i > 0 && groups.get(i - 1).isRecentCheck()
				&& !groups.get(i).isRecentCheck()) {
			String curName = getFirstGroupName(groups.get(i));
			return new GroupEntity(TAG, curName, "", "", false, false);
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
		if (null == f || f.length() <= 1) {
			return "#";
		}
		char c = f.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("[a-zA-Z]{1}+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	public ArrayList<GroupEntity> getData() {
		return groups;
	}

	public ChooseResultAdapter getChoosedAdapter(ListView list) {
		return new ChooseResultAdapter();
	}

	public class ChooseResultAdapter extends BaseAdapter {
		ArrayList<GroupEntity> members;

		public ChooseResultAdapter() {
			members = (ArrayList<GroupEntity>) GroupEntity
					.mapToArray(GroupsChooserActivity.chooseIds);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final GroupEntity bean = members.get(position);
			CViewHolder holder = null;
			if (null == convertView || convertView instanceof TextView) {
				holder = new CViewHolder();
				convertView = LayoutInflater.from(c).inflate(
						R.layout.item_choose_result, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.state = (ImageView) convertView.findViewById(R.id.state);
				convertView.setTag(holder);
			} else {
				holder = (CViewHolder) convertView.getTag();
			}

			final String name = bean.getName();
			final String id = "" + bean.getId();
			holder.name.setText(name);
			holder.state.setVisibility(View.VISIBLE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (GroupsChooserActivity.chooseIds.containsKey(id)) {
						GroupsChooserActivity.chooseIds.remove(id);
						v.findViewById(R.id.state)
								.setVisibility(View.INVISIBLE);
					} else {
						GroupsChooserActivity.chooseIds.put(id, bean);
						v.findViewById(R.id.state).setVisibility(View.VISIBLE);
					}
					list.requestFocus();
				}
			});
			return convertView;
		}

		class CViewHolder {
			TextView name;
			ImageView state;
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
	}

	private void prePosterBar() {
		for (final String tag : GroupsChooserActivity.chooseIds.keySet()) {
			if (!checkPosterTag(tag)) {
				GroupEntity bean = GroupsChooserActivity.chooseIds.get(tag);
				ViewGroup barIcon = (ViewGroup) LayoutInflater.from(c).inflate(
						R.layout.img_head, null);
				ImageView poster = (ImageView) barIcon
						.findViewById(R.id.head_img);
				barIcon.setTag(tag);
				poster.setTag("poster" + tag);
				String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
						bean.getHead(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
				MyFinalBitmap.setPoster(c, poster, headUrl);

				poster.setScaleType(ScaleType.FIT_XY);
				addPosterToBar(barIcon);
			}
		}
	}

	private boolean checkPosterTag(String tag) {
		if (null == tag) {
			return true;
		}
		for (int i = 0; i < posterBar.getChildCount(); i++) {
			View child = posterBar.getChildAt(i);
			if (tag == child.getTag() || tag.equals(child.getTag())) {
				return true;
			}
		}
		return false;
	}

	private void addPoster(String tag, String headUrl, String name) {
		if (null != posterBar) {
			ViewGroup barIcon = (ViewGroup) LayoutInflater.from(c).inflate(
					R.layout.img_head, null);
			ImageView poster = (ImageView) barIcon.findViewById(R.id.head_img);
			barIcon.setTag(tag);
			poster.setTag("poster" + tag);
			// poster.setImageDrawable(drawable);
			MyFinalBitmap.setPoster(c, poster, headUrl);
			poster.setScaleType(ScaleType.FIT_XY);
			addPosterToBar(barIcon);
		}
	}

	private void setBtnText() {
		if (null == btn) {
			return;
		}
		if (GroupsChooserActivity.chooseIds.size() > 0) {
			btn.setBackgroundResource(R.drawable.btn_chooser_green);
			btn.setText(btnString + "("
					+ GroupsChooserActivity.chooseIds.size() + ")");
		} else {
			btn.setBackgroundResource(R.drawable.btn_chooser_white);
			btn.setText("取 消");
		}
	}

	private void addPosterToBar(View v) {
		LayoutParams lp = new LayoutParams(
		// posterBar.getMeasuredHeight() - 10,
		// posterBar.getMeasuredHeight() - 10);
				DensityUtil.dip2px(c, 40), DensityUtil.dip2px(c, 40));
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
				cutPoster((String) arg0.getTag());
				GroupsChooserActivity.chooseIds.remove((String) arg0.getTag());
				notifyDataSetChanged();
				setBtnText();
			}
		});
	}

	private boolean cutPoster(String tag) {
		if (null == posterBar) {
			return false;
		}
		for (int i = 0; i < posterBar.getChildCount(); i++) {
			View child = posterBar.getChildAt(i);
			if (tag == child.getTag() || tag.equals(child.getTag())) {
				posterBar.removeViewAt(i);
				return true;
			}
		}
		return false;
	}
}
