package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.SimpleUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class FriendsAdapter extends BaseAdapter {
	private ArrayList<FriendBean> members;
	private Context c;
	private OnClickListener onMenuTipListener;
	private PopupWindow ppw;
	private String curId;

	private boolean isSearch;

	public FriendsAdapter(Context context, ArrayList<FriendBean> mbers,
			ListView listView, OnClickListener menuTipListener) {
		this.c = context;
		this.members = mbers;
		onMenuTipListener = menuTipListener;
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
		final FriendBean bean = members.get(position);
		ViewHolder holder = null;
		if (null == convertView || convertView instanceof TextView) {
			holder = new ViewHolder();
			LinearLayout content = (LinearLayout) LayoutInflater.from(c)
					.inflate(R.layout.item_friend_chooser, null);
			holder.name = (TextView) content.findViewById(R.id.name);
			holder.mood = (TextView) content.findViewById(R.id.mood);
			holder.post = (ImageView) content.findViewById(R.id.head_img);
			holder.enter = (ImageView) content.findViewById(R.id.enter);
			convertView = content;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String name = bean.getMemberName();
		final String fName = bean.getFriendName();
		final String id = "" + bean.getMemberId();
		holder.menu = initItemMenu(holder);
		holder.enter.setVisibility(View.VISIBLE);
		holder.enter.setTag(holder.menu);
		holder.enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((ImageView) arg0).setImageResource(R.drawable.arrow_rights);

				showMenu(arg0, (View) arg0.getParent());
				curId = id;
			}
		});
		String mood = bean.getMoodContent();
		int sex = bean.getSex();
		String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
				bean.getMemberHeadUrl() + bean.getMemberHeadPath(),
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		if (null != fName && !fName.equals(name)) {
			if (isSearch) {
				holder.name.setText(fName + " （" + name + "）");
			} else {
				holder.name.setText(fName);
			}
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
		MyFinalBitmap.setHeader(c, holder.post, headUrl);
		try {
			TransitionBean tBean = new TransitionBean(bean);
			View v = convertView.findViewById(R.id.content);
			v.setTag(tBean);
			holder.post.setTag(tBean);
			holder.call.setTag(tBean);
			holder.mail.setTag(tBean);
			holder.greet.setTag(tBean);
			SimpleUtil.setAllOnClickLisener(onMenuTipListener, holder.post, v,
					holder.call, holder.greet, holder.mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@SuppressWarnings("deprecation")
	private void showMenu(final View v, View ancherView) {
		int arrawHeight = DensityUtil.dip2px(c, 10);
		LinearLayout linearLayout = new LinearLayout(c);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int[] locationOut = new int[2];
		ancherView.getLocationOnScreen(locationOut);
		LinearLayout menuView = (LinearLayout) v.getTag();
		ViewGroup parent = (ViewGroup) menuView.getParent();
		int itemHeight = ancherView.getMeasuredHeight();
		if (null != parent) {
			parent.removeView(menuView);
		}
		boolean showUp;
		ppw = new PopupWindow(linearLayout, BaseData.getScreenWidth(),
				LayoutParams.WRAP_CONTENT);
		if (locationOut[1] >= BaseData.getScreenHeight() - itemHeight * 2
				- arrawHeight) { // 向上
			showUp = true;
			View th = new View(c);
			th.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.arraw_black_down));
			LayoutParams ll = new LayoutParams(
					arrawHeight, arrawHeight);
			ll.leftMargin = location[0] + (v.getMeasuredWidth() - arrawHeight)
					/ 2;
			linearLayout.addView(th, ll);
			linearLayout.addView(menuView, 0, new LayoutParams(
					LayoutParams.MATCH_PARENT, itemHeight + 5));
		} else {
			showUp = false;
			View th = new View(c);
			th.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.arraw_black_up));
			LayoutParams ll = new LayoutParams(
					arrawHeight, arrawHeight);
			ll.leftMargin = location[0] + (v.getMeasuredWidth() - arrawHeight)
					/ 2;
			linearLayout.addView(th, ll);
			linearLayout.addView(menuView, new LayoutParams(
					LayoutParams.MATCH_PARENT, itemHeight + 5));
		}
		ppw.setBackgroundDrawable(c.getResources().getDrawable(
				R.drawable.nothing));
		ppw.setOutsideTouchable(true);
		ppw.setFocusable(true);
		ppw.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				((ImageView) v).setImageResource(R.drawable.arrow_down_0);
			}
		});
		if (showUp) { // 向上
			ppw.setAnimationStyle(R.style.scale_menu_from_right_bottom);
			ppw.showAtLocation(ancherView, Gravity.NO_GRAVITY, locationOut[0],
					locationOut[1] - itemHeight - arrawHeight / 2 - 1); // 有问题
		} else {
			ppw.setAnimationStyle(R.style.scale_menu_from_right_top);
			ppw.showAtLocation(ancherView, Gravity.NO_GRAVITY, locationOut[0],
					locationOut[1] + itemHeight - arrawHeight);
		}
	}

	private LinearLayout initItemMenu(ViewHolder holder) {
		LinearLayout back = (LinearLayout) LayoutInflater.from(c).inflate(
				R.layout.menu_friend_simple, null);
		holder.call = (ImageView) back.findViewById(R.id.menu_friend_phone);
		holder.greet = (ImageView) back.findViewById(R.id.menu_friend_greet);
		holder.mail = (ImageView) back.findViewById(R.id.menu_friend_mail);
		return back;
	}

	class ViewHolder {
		TextView name, mood;
		ImageView post, sex, enter, call, greet, mail;
		LinearLayout menu;
	}

	public ArrayList<FriendBean> getData() {
		return members;
	}

	public void ppwDismiss() {
		if (null != ppw && ppw.isShowing()) {
			ppw.dismiss();
		}
	}

	public String getCurId() {
		return curId;
	}
}
