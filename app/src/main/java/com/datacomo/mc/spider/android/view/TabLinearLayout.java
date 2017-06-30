package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;

public class TabLinearLayout extends LinearLayout implements OnClickListener {
	private OnTabClickListener mOnTabClickListener;
	private int mCur;
	private Resources res;
	// int[] drawables = new int[] { R.drawable.blank_left_a,
	// R.drawable.blank_middle_a, R.drawable.blank_right_a,
	// R.drawable.blank_left_b, R.drawable.blank_middle_b,
	// R.drawable.blank_right_b };
	int[] drawables = new int[] { R.drawable.tab_2, R.drawable.tab_2,
			R.drawable.tab_2, R.drawable.tab_1, R.drawable.tab_1,
			R.drawable.tab_1 };

	public TabLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = context.getResources();
	}

	public interface OnTabClickListener {
		void onTabClick(View tab);
	}

	public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
		mOnTabClickListener = onTabClickListener;
	}

	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		refresh(index);
	}

	public void refresh(int index) {
		if (0 == getChildCount() || index < 0 || index >= getChildCount()) {
			return;
		}
		cutTab(index);

		if (mOnTabClickListener != null && mCur != index) {
			mOnTabClickListener.onTabClick(getChildAt(index));
		}
		mCur = index;
	}

	public void refresh(int index, boolean doRefresh) {
		if (0 == getChildCount() || index < 0 || index >= getChildCount()) {
			return;
		}
		cutTab(index);

		if (doRefresh || (mOnTabClickListener != null && mCur != index)) {
			mOnTabClickListener.onTabClick(getChildAt(index));
		}
		mCur = index;
	}

	public void refreshWithText(int index, String text, boolean doRefresh) {
		if (0 == getChildCount() || index < 0 || index >= getChildCount()) {
			return;
		}
		cutTab(index);

		if (doRefresh || (mOnTabClickListener != null && mCur != index)) {
			mOnTabClickListener.onTabClick(getChildAt(index));
		}
		if (null != text) {
			changeSpecilText(text, index);
		}
		mCur = index;
	}

	@SuppressWarnings("deprecation")
	private void cutTab(int index) {
		if (-1 == mCur) {

		} else if (0 == mCur) {
			getChildAt(mCur).setBackgroundDrawable(
					res.getDrawable(drawables[0]));
		} else if (getChildCount() - 1 == mCur) {
			getChildAt(mCur).setBackgroundDrawable(
					res.getDrawable(drawables[2]));
		} else {
			getChildAt(mCur).setBackgroundDrawable(
					res.getDrawable(drawables[1]));
		}
		((ViewGroup) getChildAt(mCur)).getChildAt(0).setVisibility(
				View.INVISIBLE);
		((TextView) (((ViewGroup) getChildAt(mCur))).getChildAt(1))
				.setTextColor(res.getColor(R.color.black));

		if (0 == index) {
			getChildAt(index).setBackgroundDrawable(
					res.getDrawable(drawables[3]));
		} else if (getChildCount() - 1 == index) {
			getChildAt(index).setBackgroundDrawable(
					res.getDrawable(drawables[5]));
		} else {
			getChildAt(index).setBackgroundDrawable(
					res.getDrawable(drawables[4]));
		}
		((ViewGroup) getChildAt(index)).getChildAt(0).setVisibility(
				View.VISIBLE);
		((TextView) (((ViewGroup) getChildAt(index))).getChildAt(1))
				.setTextColor(res.getColor(R.color.auto_link));
	}

	/**
	 * 更换指定标签内容
	 * 
	 * @param text
	 *            标签内容
	 * @param textId
	 *            标签索引
	 */
	public void changeSpecilText(String text, int index) {
		if (getChildCount() > 4) {
			if (text != null && text.contains("(")) {
				((ViewGroup) getChildAt(index)).getChildAt(2).setVisibility(
						View.VISIBLE);
				((TextView) ((ViewGroup) getChildAt(index)).getChildAt(1))
						.setText(text.subSequence(0, text.indexOf("(")));
			} else {
				((ViewGroup) getChildAt(index)).getChildAt(2).setVisibility(
						View.GONE);
			}
		} else {
			((TextView) ((ViewGroup) getChildAt(index)).getChildAt(1))
					.setText(text);
		}
	}

	/**
	 * 更换所有标签内容
	 * 
	 * @param text
	 *            标签数组,长度最小为2
	 */
	@SuppressWarnings("deprecation")
	public void changeText(String[] text) {
		if (text.length < 2) {
			throw new ArrayIndexOutOfBoundsException("传入标签内容数不合法!");
		}
		LayoutParams lp = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		for (int j = 0; j < text.length; j++) {
			LinearLayout childTab = (LinearLayout) LayoutInflater.from(
					getContext()).inflate(R.layout.item_tab, null);

			if (0 == j) {
				childTab.setBackgroundDrawable(res.getDrawable(drawables[0]));
			} else if (text.length - 1 == j) {
				childTab.setBackgroundDrawable(res.getDrawable(drawables[2]));
			}
			addView(childTab, lp);
		}
		for (int i = 0; i < text.length; i++) {
			String txt = text[i];
			if (txt != null && txt.contains("(")) {
				txt = txt.substring(0, txt.indexOf("("));
				((ViewGroup) getChildAt(i)).getChildAt(2).setVisibility(
						View.VISIBLE);
			} else {
				((ViewGroup) getChildAt(i)).getChildAt(2).setVisibility(
						View.GONE);
			}
			((TextView) ((ViewGroup) getChildAt(i)).getChildAt(1)).setText(txt);
			View child = getChildAt(i);
			child.setTag(i);
			child.setOnClickListener(this);
		}
	}

	public boolean setBgDrawable(int[] bg) {
		if (6 == bg.length) {
			drawables = bg;
			return true;
		}
		return false;
	}
}
