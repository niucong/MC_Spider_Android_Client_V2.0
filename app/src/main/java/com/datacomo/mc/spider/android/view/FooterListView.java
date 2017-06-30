package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FooterListView extends ListView implements OnScrollListener {
	private final String TAG = "FooterListView";

	private LayoutInflater inflater;

	private LinearLayout footerView, defFooterView;
	private View tmpFooterView;

	private int firstItemIndex, visibleCount;

	private OnLoadMoreListener moreListener;

	public boolean isRefreshable;

	public FooterListView(Context context) {
		super(context);
		init(context);
	}

	public FooterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		setSelector(context.getResources().getDrawable(R.drawable.nothing));
		setDivider(context.getResources().getDrawable(R.drawable.list_divider));
		inflater = LayoutInflater.from(context);

		footerView = new LinearLayout(getContext());
		footerView.setOrientation(LinearLayout.VERTICAL);
		defFooterView = (LinearLayout) inflater.inflate(R.layout.foot, null);
		setOnScrollListener(this);
		isRefreshable = false;
		setFastScrollEnabled(true);
		footerView.addView(defFooterView);
		addFooterView(footerView,null,false);
	}

	public void setFooter(View footer) {
		setFooter(footer, false);
	}

	public void setFooter(View footer, boolean isPaddingFooter) {
		if (getFooterViewsCount() <= 0) {
			addFooterView(footerView,null,false);
		}
		if (null == getFooterViewContent()) {
			if (!isPaddingFooter) {
				footerView.removeAllViews();
			}
			tmpFooterView = footer;
			footerView.addView(tmpFooterView);
			measureView(footerView);
			footerView.invalidate();
		}
	}

	public void addFooter(View footer, boolean isPaddingFooter) {
		if (getFooterViewsCount() <= 0) {
			addFooterView(footerView,null,false);
		}
		if (!isPaddingFooter) {
			footerView.removeAllViews();
		}
		tmpFooterView = footer;
		footerView.addView(tmpFooterView);
		measureView(footerView);
		footerView.invalidate();
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
		visibleCount = arg2;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		switch (arg1) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			ImageLoader.getInstance().resume();
			if (getAdapter().getCount() > 10
					&& firstItemIndex + visibleCount >= getAdapter().getCount() - 1) {
				if (null != moreListener) {
					moreListener.onLoadMore();
				}
			}
			L.d(TAG, "OnScrollListener.SCROLL_STATE_IDLE");
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			L.d(TAG, "OnScrollListener.SCROLL_STATE_TOUCH_SCROLL");
			ImageLoader.getInstance().pause();
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			L.d(TAG, "OnScrollListener.SCROLL_STATE_FLING");
			ImageLoader.getInstance().pause();
			break;
		}
	}

	public View getFooterViewContent() {
		return tmpFooterView;
	}

	public void removeFooterView() {
		removeFooterView(footerView);
	}

	public void showLoadFooter() {
		if (getFooterViewsCount() <= 0) {
			addFooterView(footerView, null, false);
		}
		if (null != tmpFooterView)
			tmpFooterView.setVisibility(View.GONE);
		defFooterView.setVisibility(View.VISIBLE);
	}

	public void hideLoadFooter() {
		defFooterView.setVisibility(View.GONE);
	}

	public void showTempFooter() {
		defFooterView.setVisibility(View.GONE);
		tmpFooterView.setVisibility(View.VISIBLE);
	}

	public void hideTempFooter() {
		tmpFooterView.setVisibility(View.GONE);
	}

	public void setFirstItem(int firstVisiableItem) {
		firstItemIndex = firstVisiableItem;
	}

	public int getFirstItem() {
		return firstItemIndex;
	}
	public void setonLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.moreListener = loadMoreListener;
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public int getItemCount() {
		Adapter ad = getAdapter();
		if (null != ad) {
			return ad.getCount();
		}
		return 0;
	}

	// 此方法是估算headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

}
