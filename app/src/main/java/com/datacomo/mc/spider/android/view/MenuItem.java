package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;

public class MenuItem extends LinearLayout {

	public MenuItem(Context context) {
		super(context);
	}

	public MenuItem(Context context, int imgId, String text) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		TextView textV = (TextView) View.inflate(context, R.layout.file_txt,
				null);

		Drawable drawable = context.getResources().getDrawable(imgId);
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, 1 * dw / 2, 1 * dh / 2);
		textV.setCompoundDrawables(null, drawable, null, null);
		textV.setText(text);
		addView(textV);
	}

	public void setOnClickEvent(OnClickListener listener) {
		if (null != listener) {
			setOnClickListener(listener);
		}
	}

}
