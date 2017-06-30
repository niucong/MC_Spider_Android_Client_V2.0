package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;

public class GroupNameView {

	private View view;
	private TextView name_tv;
	private LayoutInflater inflater;

	public GroupNameView(final Context context, String name) {
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.group_name, null);
		name_tv = (TextView) view.findViewById(R.id.group_name);
		name_tv.setText(name);

		// Drawable drawableLeft = context.getResources().getDrawable(
		// R.drawable.group_icon);
		// // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		// drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
		// drawableLeft.getMinimumHeight());
		Drawable drawableRight = context.getResources().getDrawable(
				R.drawable.tip_del);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
				drawableRight.getMinimumHeight());
		name_tv.setCompoundDrawables(null, null, drawableRight, null); // 设置左图标
	}

	public View getView() {
		return view;
	}
}
