package com.datacomo.mc.spider.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CirclesActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.util.LogicUtil;

public class CircleFamily {
	LinearLayout juniorCircles;
	IconFly circle, superCircle;
	Context c;
	private String nameStr;
	private int main = 100;
	private int up = 101;
	private int w;

	private View convertView;
	private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
	private TextView down_tv;

	public CircleFamily(Context context, int width) {
		c = context;
		w = width;

		convertView = LayoutInflater.from(context).inflate(
				R.layout.circle_family, null);
		relativeLayout1 = (RelativeLayout) convertView
				.findViewById(R.id.circle_family_relativeLayout1);
		relativeLayout2 = (RelativeLayout) convertView
				.findViewById(R.id.circle_family_relativeLayout2);
		relativeLayout3 = (RelativeLayout) convertView
				.findViewById(R.id.circle_family_relativeLayout3);
		down_tv = (TextView) convertView.findViewById(R.id.circle_family_down);
	}

	public View getView() {
		return convertView;
	}

	/**
	 * 本圈子
	 * 
	 * @param id
	 * @param name
	 * @param url
	 */
	public void addCircle(String id, String name, String url) {
		nameStr = name;
		RelativeLayout.LayoutParams lp0 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp0.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		circle = new IconFly(c, w / 4);
		circle.setInfo(id, name, url, IconFly.DEFAULT);
		circle.setId(main);
		circle.hideWing();
		relativeLayout2.addView(circle, lp0);
	}

	/**
	 * 上级圈子
	 */
	private void addSuperArraws() {
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(25,
				w / 5);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp1.addRule(RelativeLayout.ABOVE, main);
		View superArraw = new View(c);
		superArraw.setId(up);
		superArraw.setBackgroundResource(R.drawable.icon_arraw_up);
		relativeLayout1.addView(superArraw, lp1);
	}

	/**
	 * 下级圈子
	 */
	private void addJuniorArraws() {
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(25,
				w / 5);
		lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		lp2.addRule(RelativeLayout.BELOW, main);
		View juniorArraw = new View(c);
		juniorArraw.setId(2);
		juniorArraw.setBackgroundResource(R.drawable.icon_arraw_down);
		relativeLayout3.addView(juniorArraw, lp2);
	}

	/**
	 * 上级圈子
	 */
	public void addSuperCircle(GroupBasicBean superGroup) {
		// addSuperArraws();
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// lp.addRule(RelativeLayout.ABOVE, up_iv.getId());
		superCircle = (IconFly) convertView
				.findViewById(R.id.circle_family_upIconFly);
		// superCircle = new IconFly(c, w / 5);
		superCircle.setWidth(w / 4);
		superCircle.setInfo(superGroup, IconFly.DEFAULT);
		superCircle.setVisibility(View.VISIBLE);
		superCircle.hideWing();
		// superCircle.setId(3);
		// relativeLayout1.addView(superCircle, lp);

		// RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// lp1.addRule(RelativeLayout.ABOVE, main);
		// lp1.setMargins(0, 0, 0, w / 15);
		// TextView superGuide = new TextView(c);
		// superGuide.setTextSize(10);
		// superGuide.setBackgroundResource(R.drawable.bg_normal_small);
		// superGuide.setTextColor(c.getResources().getColor(R.color.graytext));
		// superGuide.setText("上级圈子");
		// relativeLayout1.addView(superGuide, lp1);

		relativeLayout1.setVisibility(View.VISIBLE);
	}

	/**
	 * 下级圈子
	 */
	public void addJuniorCircles(final List<GroupBasicBean> groups) {
		if (null == groups || groups.size() == 0) {
			return;
		}

		// addJuniorArraws();

		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.FILL_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// lp.addRule(RelativeLayout.BELOW, 2);
		// juniorCircles = new LinearLayout(c);
		// juniorCircles.setOrientation(LinearLayout.HORIZONTAL);
		// juniorCircles.setPadding(5, 5, 5, 5);
		// juniorCircles.setGravity(Gravity.CENTER);
		// juniorCircles.setBackgroundDrawable(c.getResources().getDrawable(
		// R.drawable.bg_normal));
		// juniorCircles.setId(4);
		// relativeLayout3.addView(juniorCircles, lp);

		juniorCircles = (LinearLayout) convertView
				.findViewById(R.id.circle_family_down_layout);
		juniorCircles.setVisibility(View.VISIBLE);

		LayoutParams lpItem = new LayoutParams(w / 5,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int max = Math.min(4, groups.size());
		for (int i = 0; i < max; i++) {
			IconFly item = new IconFly(c, w / 7);
			item.setInfo(groups.get(i), IconFly.CIRCLE);
			item.hideWing();
			juniorCircles.addView(item, lpItem);
			relativeLayout3.invalidate();
		}
		if (groups.size() > 4) {
			ImageView more = new ImageView(c);
			more.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			more.setScaleType(ScaleType.CENTER_INSIDE);
			more.setImageDrawable(c.getResources().getDrawable(
					R.drawable.icon_more_gray));
			juniorCircles.addView(more, new RelativeLayout.LayoutParams(w / 10,
					w / 10));

			more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Bundle b = new Bundle();
					ArrayList<String> ids = new ArrayList<String>();
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < groups.size(); i++) {
						GroupBasicBean group = groups.get(i);
						if (null != group) {
							ids.add("" + group.getGroupId());
							names.add(group.getGroupName());
							urls.add(group.getFullGroupPosterPath());
						}
					}
					b.putStringArrayList("ids", ids);
					b.putStringArrayList("names", names);
					b.putStringArrayList("urls", urls);
					b.putString("groupName", nameStr);
					b.putInt("type", 0);
					LogicUtil.enter(c, CirclesActivity.class, b, false);
				}
			});
		}

		// RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// lp1.addRule(RelativeLayout.BELOW, main);
		// lp1.setMargins(0, w / 15, 0, 0);
		// TextView juniorGuide = new TextView(c);
		// down_tv.setTextSize(10);
		// down_tv.setBackgroundResource(R.drawable.bg_normal_small);
		// down_tv.setTextColor(c.getResources().getColor(R.color.graytext));
		down_tv.setText("下级圈子（" + groups.size() + "）");
		// relativeLayout3.addView(juniorGuide, lp1);
		relativeLayout3.setVisibility(View.VISIBLE);
	}

	/**
	 * 合作圈子
	 * 
	 * @param groups
	 */
	public void addCpCircles(final List<GroupBasicBean> groups) {
		if (null == groups || groups.size() == 0) {
			return;
		}

		int max = Math.min(groups.size(), 3);
		for (int i = 0; i < max; i++) {
			IconFly cpCircle = new IconFly(c, w / 8);
			cpCircle.setInfo(groups.get(i), IconFly.CIRCLE);
			addCpCircle(cpCircle, i + 1);
		}
		trim(max);

		if (groups.size() > 3) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					w / 10, w / 10);
			lp.addRule(RelativeLayout.RIGHT_OF, main + 1);
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			lp.setMargins(10, 0, 0, 0);
			ImageView more = new ImageView(c);
			more.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			more.setScaleType(ScaleType.CENTER_INSIDE);
			more.setImageDrawable(c.getResources().getDrawable(
					R.drawable.icon_more_gray));
			relativeLayout2.addView(more, lp);
			more.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					ArrayList<String> ids = new ArrayList<String>();
					ArrayList<String> names = new ArrayList<String>();
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < groups.size(); i++) {
						GroupBasicBean group = groups.get(i);
						if (null != group) {
							ids.add("" + group.getGroupId());
							names.add(group.getGroupName());
							urls.add(group.getFullGroupPosterPath());
						}
					}
					b.putStringArrayList("ids", ids);
					b.putStringArrayList("names", names);
					b.putStringArrayList("urls", urls);
					b.putString("groupName", nameStr);
					b.putInt("type", 1);
					LogicUtil.enter(c, CirclesActivity.class, b, false);
				}
			});
		}

	}

	/**
	 * 合作圈子
	 * 
	 * @param cpCircle
	 * @param index
	 */
	private void addCpCircle(IconFly cpCircle, int index) {
		if (null == cpCircle) {
			return;
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (1 == index) {
			lp.addRule(RelativeLayout.LEFT_OF, main);
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			cpCircle.setId(main - 1);
		} else if (2 == index) {
			lp.addRule(RelativeLayout.RIGHT_OF, main);
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			cpCircle.setId(main + 1);
		} else if (3 == index) {
			lp.addRule(RelativeLayout.LEFT_OF, main - 1);
			lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			cpCircle.setId(main - 2);
		}
		relativeLayout2.addView(cpCircle, lp);

	}

	private void trim(int count) {
		if (0 == count) {
		} else if (1 == count) {
			((IconFly) relativeLayout2.findViewById(main - 1)).hideLeftWing();
		} else if (2 == count) {
			((IconFly) relativeLayout2.findViewById(main - 1)).hideLeftWing();
			((IconFly) relativeLayout2.findViewById(main + 1)).hideRightWing();
		} else if (3 == count) {
			((IconFly) relativeLayout2.findViewById(main - 2)).hideLeftWing();
			((IconFly) relativeLayout2.findViewById(main + 1)).hideRightWing();
		} else if (4 == count) {
			((IconFly) relativeLayout2.findViewById(main - 2)).hideLeftWing();
			((IconFly) relativeLayout2.findViewById(main + 2)).hideRightWing();
		}

	}
}
