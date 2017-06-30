package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class IconFly extends RelativeLayout {
	private ImageView img;
	private View leftWing, rightWing;
	private LinearLayout.LayoutParams lp;
	private LayoutParams lpWing;
	private LayoutParams lpName;
	private Context c;
	private TextView name;
	private int r;
	private int centerId;
	private LinearLayout content, infos;
	static public int CIRCLE = -1;
	static public int DEFAULT = 0;
	private int type;

	public IconFly(Context context, int width) {
		super(context);
		c = context;
		r = width;
		// init();
	}

	public IconFly(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
	}

	public IconFly(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		c = context;
	}

	public void setWidth(int width) {
		r = width;
	}

	private void init() {
		removeAllViews();
		setGravity(Gravity.CENTER_HORIZONTAL);
		content = new LinearLayout(c);
		content.setGravity(Gravity.CENTER_VERTICAL);
		content.setOrientation(LinearLayout.HORIZONTAL);
		// content.setPadding(2, 0, 2, 0);
		img = new ImageView(c);
		img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		img.setScaleType(ScaleType.FIT_CENTER);
		img.setImageDrawable(c.getResources().getDrawable(
				R.drawable.icon_poster_loading));
		lp = new LinearLayout.LayoutParams(r, r);

		leftWing = new View(c);
		leftWing.setBackgroundColor(c.getResources().getColor(
				R.color.green_arraw));
		rightWing = new View(c);
		rightWing.setBackgroundColor(c.getResources().getColor(
				R.color.green_arraw));
		lpWing = new LayoutParams(10, 5);

		infos = new LinearLayout(c);
		infos.setOrientation(LinearLayout.VERTICAL);
		infos.addView(img, lp);

		name = new TextView(c);
		name.setGravity(Gravity.CENTER);
		name.setSingleLine();
		name.setEllipsize(TruncateAt.END);
		name.setTextSize(10);
		name.setTextColor(c.getResources().getColor(R.color.graytext));
		name.setVisibility(View.GONE);
		infos.addView(name);

		content.addView(leftWing, lpWing);
		content.addView(infos);
		content.addView(rightWing, lpWing);
		content.setId(centerId);
		addView(content);
	}

	// private void setInfo(final GroupBasicBean bean) {
	// setInfo(bean, DEFAULT);
	// }

	private void setName(String text) {
		if (type == DEFAULT) {
			name.setVisibility(View.GONE);
			lpName = new LayoutParams(r, LayoutParams.WRAP_CONTENT);
			// lpName.addRule(CENTER_HORIZONTAL, RelativeLayout.TRUE);
			lpName.addRule(BELOW, centerId);
			TextView xName = new TextView(c);
			xName.setGravity(Gravity.CENTER);
			xName.setSingleLine();
			xName.setEllipsize(TruncateAt.END);
			xName.setTextSize(10);
			xName.setText(text);
			xName.setBackgroundColor(Color.BLACK);
			xName.setTextColor(c.getResources().getColor(R.color.white));
			addView(xName, lpName);
		} else if (type == CIRCLE) {
			name.setVisibility(View.VISIBLE);
			name.setText(text);
		}
		requestLayout();
	}

	public void setInfo(final GroupBasicBean bean, final int isCircle) {
		type = isCircle;
		init();
		String url = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getFullGroupPosterPath(),
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setPoster(c, img, url);

		setClickListener("" + bean.getGroupId());
		setName(bean.getGroupName());
	}

	public void setInfo(String id, String circleName, String url,
			final int isCircle) {
		type = isCircle;
		init();
		url = ThumbnailImageUrl.getThumbnailPostUrl(url,
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
		MyFinalBitmap.setPoster(c, img, url);

		setClickListener(id);
		setName(circleName);
	}

	private void setClickListener(final String id) {
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("Id", id);
				LogicUtil.enter(c, HomeGpActivity.class, b, false);
			}
		});
	}

	public void hideLeftWing() {
		leftWing.setVisibility(View.GONE);
		img.requestLayout();
		content.requestLayout();
		requestLayout();
	}

	public void hideRightWing() {
		rightWing.setVisibility(View.GONE);
		img.requestLayout();
		content.requestLayout();
		requestLayout();
	}

	public void hideWing() {
		hideLeftWing();
		hideRightWing();
		img.requestLayout();
		content.requestLayout();
		requestLayout();
	}

	public String getGroupName() {
		return name.getText().toString();
	}
}
