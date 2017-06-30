package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;

public class SignsView extends LinearLayout {
	int count;
	int index;
	int defRes = R.drawable.icon_page_unselect;
	int curRes = R.drawable.icon_page_selected;
	public SignsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SignsView(Context context) {
		super(context);
	}

	public void init(int count, int index) {
		setGravity(Gravity.CENTER);
		removeAllViews();
		if (count <= 0 || index > count - 1) {
			return;
		}
		int size=(int) (BaseData.getScreenWidth()*0.02);
		L.d("SignsView", "size:" + size);
		LayoutParams lp = new LayoutParams(size, size);
		lp.setMargins(15, 0,15, 0);
		ImageView v=null;
		for (int i = 0; i < count; i++) {
			v = new ImageView(getContext());
			v.setImageResource(defRes);
			addView(v, lp);
		}
		if (index >= 0) {
			((ImageView) getChildAt(index))
					.setImageResource(curRes);
			this.index = index;
		}
	}

	public void changeIndex(int cur) {
		if (cur >= 0 && cur != index) {
			((ImageView) getChildAt(index))
					.setImageResource(defRes);
			((ImageView) getChildAt(cur)).setImageResource(curRes);
			this.index = cur;
		}
	}
}
