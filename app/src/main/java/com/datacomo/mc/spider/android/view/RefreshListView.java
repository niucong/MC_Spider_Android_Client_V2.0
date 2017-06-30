package com.datacomo.mc.spider.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RefreshListView extends ListView implements OnScrollListener {
	// private final String TAG = "RefreshListView";

	private final int RELEASE_To_REFRESH = 0;
	private final int PULL_To_REFRESH = 1;
	private final int REFRESHING = 2;
	private final int DONE = 3;
	private final int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final int RATIO = 2;
	// 允许拉伸的最大的header的倍数
	private double MAXPULL = 2;

	private LayoutInflater inflater;

	private LinearLayout headView, footerView, defFooterView;
	private View tmpFooterView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;

	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	// private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex, visibleCount;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;
	private OnSizeChangeListener sizeListener;
	private OnLoadMoreListener moreListener;

	public boolean isRefreshable;

	public RefreshListView(Context context) {
		super(context);
		init(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * Position the element at about 1/3 of the list height
	 */
	private static final float PREFERRED_SELECTION_OFFSET_FROM_TOP = 0.33f;

	private int mRequestedScrollPosition = -1;
	private boolean mSmoothScrollRequested;

	/**
	 * Brings the specified position to view by optionally performing a
	 * jump-scroll maneuver: first it jumps to some position near the one
	 * requested and then does a smooth scroll to the requested position. This
	 * creates an impression of full smooth scrolling without actually
	 * traversing the entire list. If smooth scrolling is not requested,
	 * instantly positions the requested item at a preferred offset.
	 */
	public void requestPositionToScreen(int position, boolean smoothScroll) {
		mRequestedScrollPosition = position;
		mSmoothScrollRequested = smoothScroll;
		requestLayout();
	}

	@SuppressLint("NewApi")
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (mRequestedScrollPosition == -1) {
			return;
		}

		final int position = mRequestedScrollPosition;
		mRequestedScrollPosition = -1;

		int firstPosition = getFirstVisiblePosition() + 1;
		int lastPosition = getLastVisiblePosition();
		if (position >= firstPosition && position <= lastPosition) {
			return; // Already on screen
		}

		final int offset = (int) (getHeight() * PREFERRED_SELECTION_OFFSET_FROM_TOP);
		if (!mSmoothScrollRequested) {
			setSelectionFromTop(position, offset);

			// Since we have changed the scrolling position, we need to redo
			// child layout
			// Calling "requestLayout" in the middle of a layout pass has no
			// effect,
			// so we call layoutChildren explicitly
			super.layoutChildren();

		} else {
			// We will first position the list a couple of screens before or
			// after
			// the new selection and then scroll smoothly to it.
			int twoScreens = (lastPosition - firstPosition) * 2;
			int preliminaryPosition;
			if (position < firstPosition) {
				preliminaryPosition = position + twoScreens;
				if (preliminaryPosition >= getCount()) {
					preliminaryPosition = getCount() - 1;
				}
				if (preliminaryPosition < firstPosition) {
					setSelection(preliminaryPosition);
					super.layoutChildren();
				}
			} else {
				preliminaryPosition = position - twoScreens;
				if (preliminaryPosition < 0) {
					preliminaryPosition = 0;
				}
				if (preliminaryPosition > lastPosition) {
					setSelection(preliminaryPosition);
					super.layoutChildren();
				}
			}
			smoothScrollToPositionFromTop(position, offset);
		}
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		setSelector(context.getResources().getDrawable(R.drawable.nothing));
		setDivider(context.getResources().getDrawable(R.drawable.note_div));
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.head1, null);
		footerView = new LinearLayout(getContext());
		footerView.setOrientation(LinearLayout.VERTICAL);
		defFooterView = (LinearLayout) inflater.inflate(R.layout.foot, null);
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		// headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;

		setFastScrollEnabled(true);
		footerView.addView(defFooterView);
		// addFooterView(footerView, null, false);
	}

	public void setFooter(View footer) {
		setFooter(footer, false);
	}

	public void setFooter(View footer, boolean isPaddingFooter) {
		if (getFooterViewsCount() <= 0) {
			addFooterView(footerView, null, false);
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
			addFooterView(footerView, null, false);
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
		if (arg3 > 10 && firstItemIndex + visibleCount == arg3) {
			ImageLoader.getInstance().resume();
			if (null != moreListener) {
				moreListener.onLoadMore();
			}
		}
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		switch (arg1) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			ImageLoader.getInstance().resume();
			// if (getAdapter().getCount() > 10
			// && firstItemIndex + visibleCount >= getAdapter().getCount() - 1)
			// {
			// if (null != moreListener) {
			// setSelection(getLastVisiblePosition() + 1);
			// moreListener.onLoadMore();
			// }
			// }
			// L.d(TAG, "OnScrollListener.SCROLL_STATE_IDLE");
			break;
		// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// L.d(TAG, "OnScrollListener.SCROLL_STATE_TOUCH_SCROLL");
		// ImageLoader.getInstance().pause();
		// break;
		case OnScrollListener.SCROLL_STATE_FLING:
			// L.d(TAG, "OnScrollListener.SCROLL_STATE_FLING");
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
		// smoothScrollToPosition(getItemCount() + 1);
		// ((TextView)
		// defFooterView.findViewById(R.id.text)).setText("正在加载中...");
		// defFooterView.findViewById(R.id.progress).setVisibility(View.VISIBLE);
	}

	public void hideLoadFooter() {
		// ((TextView) defFooterView.findViewById(R.id.text)).setText("");
		// defFooterView.findViewById(R.id.progress).setVisibility(View.GONE);
		defFooterView.setVisibility(View.GONE);
	}

	public void showTempFooter() {
		// ((TextView) defFooterView.findViewById(R.id.text)).setText("");
		// defFooterView.findViewById(R.id.progress).setVisibility(View.GONE);
		defFooterView.setVisibility(View.GONE);
		tmpFooterView.setVisibility(View.VISIBLE);
	}

	public void hideTempFooter() {
		tmpFooterView.setVisibility(View.GONE);
	}

	public void showFinishLoadFooter() {
		defFooterView.setVisibility(View.GONE);
		// ((TextView) defFooterView.findViewById(R.id.text)).setText("");
		// defFooterView.findViewById(R.id.progress).setVisibility(View.GONE);
	}

	public void setFirstItem(int firstVisiableItem) {
		firstItemIndex = firstVisiableItem;
	}

	public int getFirstItem() {
		return firstItemIndex;
	}

	/**
	 * 开始加载页面
	 * 
	 * 是否刷新数据
	 */
	public void refreshUI() {
		isRefreshable = false;
		// state = REFRESHING;
		// changeHeaderViewByState();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					// Log.v(TAG, "在down时候记录当前位置  ");
				}
				startY = (int) event.getY();
				break;

			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					// if (state == DONE) {
					// // 什么都不做
					// }
					// if (state == PULL_To_REFRESH) {
					// state = DONE;
					// changeHeaderViewByState();
					//
					// Log.v(TAG, "由下拉刷新状态，到done状态");
					// }
					// else if (state == RELEASE_To_REFRESH) {
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

						// Log.v(TAG, "由松开刷新状态，到done状态");
					} else {
						state = DONE;
						changeHeaderViewByState();

						// Log.v(TAG, "由下拉刷新状态，到done状态");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					// Log.v(TAG, "在move时候记录当前位置  ");
					startY = (int) event.getY();
					event.setAction(MotionEvent.ACTION_DOWN);
					break;
				}
				// if (state != REFRESHING && state != LOADING) {
				if (state != REFRESHING && isRecored && state != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

							// Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

							// Log.v(TAG, "由松开刷新状态转变到done状态");
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

							// Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

							// Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}

					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						int requestPadding = (tempY - startY) / RATIO;
						if (requestPadding <= MAXPULL * headContentHeight) {
							headView.setPadding(0, requestPadding
									- headContentHeight, 0, 0);
						} else {
							headView.setPadding(0, (int) (MAXPULL
									* headContentHeight - headContentHeight),
									0, 0);
						}
					}

				}

				break;
			}
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.GONE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			// Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			// Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:
			refreshing();
			// Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			refreshed();
			// Log.v(TAG, "当前状态，done");
			break;
		}
	}

	public void refreshed() {
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		progressBar.setVisibility(View.GONE);
		arrowImageView.clearAnimation();
		arrowImageView.setImageResource(R.drawable.icon_arrow);
		tipsTextview.setText("下拉刷新");
		lastUpdatedTextView.setVisibility(View.GONE);

		App.app.share.saveLongMessage("RefreshTime", "RefreshTime",
				System.currentTimeMillis());
	}

	public void refreshing() {
		headView.setPadding(0, 0, 0, 0);

		progressBar.setVisibility(View.VISIBLE);
		arrowImageView.clearAnimation();
		arrowImageView.setVisibility(View.GONE);
		tipsTextview.setText("正在刷新...");

		long time = App.app.share.getLongMessage("RefreshTime", "RefreshTime",
				System.currentTimeMillis());
		lastUpdatedTextView
				.setText("最近更新：" + DateTimeUtil.mailTimeFormat(time));
		lastUpdatedTextView.setVisibility(View.VISIBLE);
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void setonSizeChangeListener(OnSizeChangeListener sizeChangeListener) {
		this.sizeListener = sizeChangeListener;
	}

	public void setonLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.moreListener = loadMoreListener;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public interface OnSizeChangeListener {
		public void onResize(int w, int h, int oldw, int oldh);
	}

	public void onRefreshComplete() {
		isRefreshable = true;
		state = DONE;
		changeHeaderViewByState();
		if (getItemCount() < 10) {
			try {
				removeFooterView();
			} catch (NullPointerException e) {
			}
		}
	}

	/**
	 * 
	 * @param isRemoveFooter
	 */
	public void onRefreshComplete(boolean isRemoveFooter) {
		isRefreshable = true;
		state = DONE;
		// long currentTime = System.currentTimeMillis();
		// long time = App.app.share.getLongMessage("RefreshTime",
		// "RefreshTime",
		// currentTime);
		// L.d("RefreshListView", "onRefreshComplete time=" + time
		// + ",currentTime=" + currentTime);
		// App.app.share
		// .saveLongMessage("RefreshTime", "RefreshTime", currentTime);
		// lastUpdatedTextView
		// .setText("最近更新：" + DateTimeUtil.mailTimeFormat(time));
		changeHeaderViewByState();
		if (isRemoveFooter) {
			try {
				removeFooterView();
			} catch (NullPointerException e) {
			}
		}
	}

	public int getItemCount() {
		Adapter ad = getAdapter();
		if (null != ad) {
			return ad.getCount();
		}
		return 0;
	}

	private void onRefresh() {
		// L.i(TAG, "refresh");
		if (refreshListener != null) {
			refreshListener.onRefresh();
			showFinishLoadFooter();
		}
	}

	// 此方法是估算headView的width以及height
	@SuppressWarnings("deprecation")
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

	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText("最近更新："
				+ DateTimeUtil.mailTimeFormat(System.currentTimeMillis()));
		super.setAdapter(adapter);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (null != sizeListener) {
			sizeListener.onResize(w, h, oldw, oldh);
		}
	}

}
