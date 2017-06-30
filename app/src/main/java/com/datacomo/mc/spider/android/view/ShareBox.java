package com.datacomo.mc.spider.android.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.R;

public class ShareBox extends LinearLayout {
	private Context c;
	private Resources res;

	public ShareBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		init();
	}

	public ShareBox(Context context) {
		super(context);
		c = context;
		init();
	}

	private void init() {
		res = c.getResources();
		setOrientation(LinearLayout.VERTICAL);
	}

	@SuppressWarnings("deprecation")
	public void bgRefresh() {
		int size = getChildCount();
		try {
			if (size == 0) {
				return;
			} else if (1 == size) {
				ShareItem item = (ShareItem) getChildAt(0);
				item.setBackgroundDrawable(res
						.getDrawable(R.drawable.share_body));
			} else {
				for (int i = 0; i < size; i++) {
					ShareItem item = (ShareItem) getChildAt(i);
					if (0 == i) {
						item.setBackgroundDrawable(res
								.getDrawable(R.drawable.share_head));
					} else if (size - 1 == i) {
						item.setBackgroundDrawable(res
								.getDrawable(R.drawable.share_foot));
					} else {
						item.setBackgroundDrawable(res
								.getDrawable(R.drawable.share_body));
					}
				}
			}
		} catch (OutOfMemoryError e) {
		}
	}

	public void addItems(ArrayList<ShareItem> items) {
		for (int i = 0; i < items.size(); i++) {
			ShareItem item = items.get(i);
			addView(item, LayoutParams.WRAP_CONTENT, 80);
		}
		bgRefresh();
	}

	public void addItem(ShareItem item) {
		addView(item, 65, LayoutParams.WRAP_CONTENT);
		bgRefresh();
	}

	public int getSize() {
		return getChildCount();
	}

	public void performShare() {
		for (int i = 0; i < getChildCount(); i++) {
			ShareItem child = (ShareItem) getChildAt(i);
			child.execute();
		}
	}
}
