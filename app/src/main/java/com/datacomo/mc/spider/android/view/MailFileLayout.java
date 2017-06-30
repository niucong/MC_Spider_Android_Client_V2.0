package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;

public class MailFileLayout extends LinearLayout {
	private final String TAG = "MailFileLLayout";

	private LayoutParams mlp = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

	public MailFileLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mlp.setMargins(0, 0, 0, 3);
		setOrientation(LinearLayout.VERTICAL);
	}

	public void clearFiles() {
		removeAllViews();
	}

	public void clearFile(int index) {
		removeViewAt(index);
	}

	public void addFile(MailFile mFile) {
		L.d(TAG, "addFile...");
		if (mFile.isOpen()) {
			mFile.openOrClose();
		}
		addView(mFile, mlp);
		setBg();
	}

	private void setBg() {
		int count = getFileCount();
		if (0 >= count) {
			return;
		} else if (1 == count) {
			getChildAt(0).setBackgroundResource(R.drawable.shape_round_all);
		} else {
			for (int i = 0; i < count; i++) {
				if (0 == i) {
					getChildAt(i).setBackgroundResource(
							R.drawable.shape_round_header);
				} else if (count - 1 == i) {
					getChildAt(i).setBackgroundResource(
							R.drawable.shape_round_footer);
				} else {
					getChildAt(i).setBackgroundResource(
							R.drawable.shape_round_body);
				}
			}
		}
	}

	public void refresh(int index) {
		if (0 == getFileCount()) {
			return;
		} else {
			for (int i = 0; i < getChildCount(); i++) {
				MailFile f = (MailFile) getChildAt(index);
				if (i == index) {
					f.open();
				} else {
					f.close();
				}
			}
		}
	}

	public int getFileCount() {
		return getChildCount();
	}
}
