package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.FriendsChooserActivity;
import com.datacomo.mc.spider.android.QChatActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class FriendsChooserAdapter extends BaseAdapter {
	private ArrayList<FriendSimpleBean> members;
	private ListView list;
	private Context c;
	private boolean isSort, isSearch;
	public final int TAG = -1;
	// private int tagNum;

	/**
	 * 1：发邮件，2：发秘信，3：分享，4：私信、圈聊转发
	 */
	private int type = 0;
	private LinearLayout posterBar;
	private TextView btn;

	public FriendsChooserAdapter(Context context,
			ArrayList<FriendSimpleBean> mbers, ListView listView, boolean sort,
			int type) {
		this.c = context;
		isSort = sort;
		this.type = type;
		if (isSort) {
			members = format(mbers);
		} else {
			this.members = mbers;
		}
		this.list = listView;
		if (c instanceof FriendsChooserActivity) {
			posterBar = (LinearLayout) ((FriendsChooserActivity) c)
					.findViewById(R.id.content);
			btn = (TextView) ((FriendsChooserActivity) c).findViewById(R.id.ok);
		}

		// View t = new View(c);
		// t.setBackgroundResource(R.drawable.create_new_blank);
		// t.setEnabled(false);
		// addPosterToBar(t);

		if (null != FriendsChooserActivity.oriBeans
				&& FriendsChooserActivity.oriBeans.size() > 0) {
			setBtnText();
			prePosterBar();
		}
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
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
		final FriendSimpleBean bean = members.get(position);
		if (TAG == bean.getMemberId()) {
			TextView t = new TextView(c);
			t.setBackgroundResource(R.drawable.item_title);
			t.setPadding(10, 0, 0, 0);
			t.setTextColor(Color.WHITE);
			t.setText(bean.getMemberName());
			return t;
		}
		ViewHolder holder = null;
		if (null == convertView || convertView instanceof TextView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(c).inflate(
					R.layout.item_friend_chooser, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.mood = (TextView) convertView.findViewById(R.id.mood);
			holder.post = (ImageView) convertView.findViewById(R.id.head_img);
			holder.chkbox = (ViewStub) convertView.findViewById(R.id.chkbox);
			if (type != 2) {
				holder.chkbox.setInflatedId(R.id.check);
				holder.check = (ImageView) holder.chkbox.inflate();
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String name = bean.getFriendName();
		final String id = "" + bean.getMemberId();
		final String head = bean.getMemberHeadUrl() + bean.getMemberHeadPath();
		String mood = bean.getMemberMood();
		int sex = bean.getMemberSex();
		final String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(head,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		if (name == null || "".equals(name)) {
			holder.name.setText(bean.getMemberName());
		} else if (isSearch && !name.equals(bean.getMemberName())) {
			holder.name.setText(name + "（" + bean.getMemberName() + "）");
		} else {
			holder.name.setText(name);
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
		if (type != 2) {
			if (FriendsChooserActivity.oriBeans.containsKey(id)) {
				holder.check.setImageResource(R.drawable.choice_yes);
				FriendsChooserActivity.oriBeans.put(id, bean);// 数据纠正
			} else {
				holder.check.setImageResource(R.drawable.choice_no);
			}
		}
		holder.post.setTag(position + headUrl);
		MyFinalBitmap.setHeader(c, holder.post, headUrl);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseData.hideKeyBoard((Activity) c);
				if (type == 2) {
					Bundle secret = new Bundle();
					secret.putString("memberId", id);
					secret.putString("name", name);
					secret.putString("head", head);
					LogicUtil.enter(c, QChatActivity.class, secret, true);
				} else if (FriendsChooserActivity.oriBeans.containsKey(id)) {
					FriendsChooserActivity.oriBeans.remove(id);
					((ImageView) v.findViewById(R.id.check))
							.setImageResource(R.drawable.choice_no);
					setBtnText();
					cutPoster(id);
				} else {
					FriendsChooserActivity.oriBeans.put(id, bean);
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

	// private void loadHeadImg(Drawable imageDrawable, String imageUrl) {
	// if (list != null) {
	// ImageView imgView = (ImageView) list.findViewWithTag(imageUrl);
	// if (null != imgView) {
	// if (null != imageDrawable) {
	// imgView.setImageDrawable(imageDrawable);
	// }
	// }
	// }
	// }

	class ViewHolder {
		TextView name, mood;
		ImageView post, check, sex;
		ViewStub chkbox;
	}

	public String[] getSelectedIds() {
		return FriendsChooserActivity.oriBeans.keySet().toArray(new String[0]);
	}

	public LinkedHashMap<String, FriendSimpleBean> getSelected() {
		return FriendsChooserActivity.oriBeans;
	}

	public String[] getSelectedNames() {
		String[] names = new String[FriendsChooserActivity.oriBeans.size()];
		int k = 0;
		for (FriendSimpleBean bean : FriendsChooserActivity.oriBeans.values()) {
			String name = bean.getFriendName();// bean.getMemberName();
			if (null == name) {
				name = "";
			}
			names[k] = name;
			k++;
		}
		return names;
	}

	public void notifyTags() {
		members = format(members);
		notifyDataSetChanged();
	}

	@SuppressLint("UseSparseArrays")
	private ArrayList<FriendSimpleBean> format(
			ArrayList<FriendSimpleBean> members) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		HashMap<Integer, FriendSimpleBean> tags = new HashMap<Integer, FriendSimpleBean>();
		FriendSimpleBean firstEntity = new FriendSimpleBean();
		firstEntity.setMemberId(TAG);
		firstEntity.setMemberName("最近访问的");
		index.add(0);
		tags.put(0, firstEntity);
		boolean hasRecent = false;
		for (int i = 0; i < members.size(); i++) {
			if (!members.get(i).isRecentCheck()) {
				FriendSimpleBean g = checkTag(members, i);
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
		// tagNum = tags.size();
		for (int i = index.size() - 1; i >= 0; i--) {
			int k = index.get(i);
			members.add(k, tags.get(k));
		}
		return members;
	}

	private FriendSimpleBean checkTag(ArrayList<FriendSimpleBean> groups, int i) {
		if (null == groups || i < 0 || i >= groups.size()) {
			return null;
		} else if (i == 0) {
			FriendSimpleBean tagBean = new FriendSimpleBean();
			tagBean.setMemberId(TAG);
			tagBean.setMemberName(getFirstGroupName(groups.get(i)));
			return tagBean;
		} else if (i > 0 && members.get(i - 1).isRecentCheck()
				&& !members.get(i).isRecentCheck()) {
			FriendSimpleBean tagBean = new FriendSimpleBean();
			tagBean.setMemberId(TAG);
			tagBean.setMemberName(getFirstGroupName(groups.get(i)));
			return tagBean;
		} else {
			String preName = getFirstGroupName(groups.get(i - 1));
			String curName = getFirstGroupName(groups.get(i));
			if (null != preName && null != curName && !preName.equals(curName)) {
				FriendSimpleBean tagBean = new FriendSimpleBean();
				tagBean.setMemberId(TAG);
				tagBean.setMemberName(curName);
				return tagBean;
			}
		}
		return null;
	}

	private String getFirstGroupName(FriendSimpleBean group) {
		if (null == group) {
			return "#";
		}
		String f = group.getFriendNamePY();// group.getMemberNamePY();
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

	public ArrayList<FriendSimpleBean> getData() {
		return members;
	}

	public ChooseResultAdapter getChoosedAdapter(ListView list) {
		return new ChooseResultAdapter();
	}

	public class ChooseResultAdapter extends BaseAdapter {
		ArrayList<FriendSimpleBean> members;

		public ChooseResultAdapter() {
			members = (ArrayList<FriendSimpleBean>) FriendSimpleBean
					.mapToArray(FriendsChooserActivity.oriBeans);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final FriendSimpleBean bean = members.get(position);
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

			final String name = bean.getMemberName();
			final String id = "" + bean.getMemberId();
			holder.name.setText(name);
			holder.state.setVisibility(View.VISIBLE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (FriendsChooserActivity.oriBeans.containsKey(id)) {
						FriendsChooserActivity.oriBeans.remove(id);
						v.findViewById(R.id.state)
								.setVisibility(View.INVISIBLE);
					} else {
						FriendsChooserActivity.oriBeans.put(id, bean);
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

	private void prePosterBar() {
		for (final String tag : FriendsChooserActivity.oriBeans.keySet()) {
			if (!checkPosterTag(tag)) {
				FriendSimpleBean bean = FriendsChooserActivity.oriBeans
						.get(tag);
				ViewGroup barIcon = (ViewGroup) LayoutInflater.from(c).inflate(
						R.layout.img_head, null);
				ImageView poster = (ImageView) barIcon
						.findViewById(R.id.head_img);
				barIcon.setTag(tag);
				poster.setTag("poster" + tag);
				String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
						bean.getMemberHeadUrl() + bean.getMemberHeadPath(),
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
				MyFinalBitmap.setHeader(c, poster, headUrl);

				poster.setScaleType(ScaleType.FIT_XY);
				addPosterToBar(barIcon);
			}
		}
	}

	private void addPoster(String tag, String headUrl, String name) {
		if (null != posterBar) {
			ViewGroup barIcon = (ViewGroup) LayoutInflater.from(c).inflate(
					R.layout.img_head, null);
			ImageView poster = (ImageView) barIcon.findViewById(R.id.head_img);
			barIcon.setTag(tag);
			poster.setTag("poster" + tag);
			MyFinalBitmap.setHeader(c, poster, headUrl);
			poster.setScaleType(ScaleType.FIT_XY);
			addPosterToBar(barIcon);
		}
	}

	private void setBtnText() {
		if (null == btn) {
			return;
		}
		if (FriendsChooserActivity.oriBeans.size() > 0) {
			btn.setBackgroundResource(R.drawable.btn_chooser_green);
			if (type == 4) {
				btn.setText("转发(" + FriendsChooserActivity.oriBeans.size()
						+ ")");
			} else if (type == 3) {
				btn.setText("分享(" + FriendsChooserActivity.oriBeans.size()
						+ ")");
			} else {
				btn.setText("确定(" + FriendsChooserActivity.oriBeans.size()
						+ ")");
			}
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
				FriendsChooserActivity.oriBeans.remove((String) arg0.getTag());
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
