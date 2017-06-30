package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.datacomo.mc.spider.android.MailCreateActivity;
import com.datacomo.mc.spider.android.url.L;

public class GroupView extends ViewGroup {
	private static final String TAG = "GroupView";

	private final static int VIEW_MARGIN = 0;
	private int VIEW_WIDTH;
	public GroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		L.i(TAG, "onMeasure widthMeasureSpec=" + widthMeasureSpec
				+ ",heightMeasureSpec=" + heightMeasureSpec);
		int sumWidth = 0;
		int sumHeight = 0;
		int a = 1;
		int lineWidth = 0;
		VIEW_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int w = child.getMeasuredWidth();
			if(w > VIEW_WIDTH - 10){
				LayoutParams lp = child.getLayoutParams();
				lp.width = VIEW_WIDTH;
				child.setLayoutParams(lp);
				int ww = MeasureSpec.makeMeasureSpec(VIEW_WIDTH, MeasureSpec.AT_MOST);
				int hh = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
				child.measure(ww, hh);
			}
			sumWidth = sumWidth + child.getMeasuredWidth() + VIEW_MARGIN;
			lineWidth = lineWidth + child.getMeasuredWidth() + VIEW_MARGIN;
			if (lineWidth > VIEW_WIDTH) {
				lineWidth = child.getMeasuredWidth() + VIEW_MARGIN;
				a++;
			}
			sumHeight = (child.getMeasuredHeight()) * a + 5;
		}
		int mW = resolveSize(sumWidth, widthMeasureSpec);
		int mH = resolveSize(sumHeight, heightMeasureSpec);
		setMeasuredDimension(mW, mH);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		L.d(TAG, "onLayout changed = " + changed + " left = " + l + " top = "
				+ t + " right = " + r + " botom = " + b);
		int count = getChildCount();
		int row = 1;
		int lengthX = l;
		int lengthY = t;
		for (int i = 0; i < count; i++) {
			View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += width + VIEW_MARGIN;
			lengthY = row * (height + VIEW_MARGIN) + t;

			if (lengthX > r) {
				lengthX = width + VIEW_MARGIN + l;
				row++;
				lengthY = row * (height + VIEW_MARGIN) + t;
			}
			child.layout(lengthX - width - l, lengthY - height - t,
					lengthX - l, lengthY - t);
		}
	}

	/**
	 * 添加View
	 * 
	 * @param position
	 * @param textView
	 * @param iv
	 */
	public void addView(final GroupNameView groupNameView, final int which) {
		final View gView = groupNameView.getView();
		gView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MailCreateActivity.getOri(which).remove(v.getTag());
				removeView(gView);
			}
		});
		addView(gView);
	}
}



