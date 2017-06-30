package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class GroupAutoCompleteTextView extends AutoCompleteTextView {

	public GroupAutoCompleteTextView(Context context) {
		super(context);
		setThreshold(1);
	}

	public GroupAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setThreshold(1);
		setFrame(0, 10, getWidth(), getHeight() + 10);
	}

	@Override
	protected void replaceText(CharSequence text) {

	}

	// // 判断输入文字列长度是否满足现实候补列表的要求的方法
	// @Override
	// public boolean enoughToFilter() {
	// return true;
	// }
	//
	// // 当控件获得焦点时让其显示候补列表
	// @Override
	// protected void onFocusChanged(boolean focused, int direction,
	// Rect previouslyFocusedRect) {
	// super.onFocusChanged(focused, direction, previouslyFocusedRect);
	// performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
	// }

}
