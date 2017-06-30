package com.datacomo.mc.spider.android.view;
//package com.datacomo.mc.spider.android.view;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.DecelerateInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.widget.LinearLayout;
//
//import com.datacomo.mc.spider.android.util.BaseData;
//
//public class SlideMenuView extends ViewGroup {
//	private int leftMargin;
//	private int gap;
//	private ViewGroup fV, bV;
//	private int lastX, lastY;
//	private boolean menuOpen = false;
//	private boolean isAniPosting;
//
//	private final int DEF_MAX_FAST_COUNT = 7;
//	private final int DEF_MIN_FAST_DX = 25;
//
//	private int sumDx;
//	private int scrollCount;
//	private boolean scrollEnable = true; // 是否允许手动滑动view以打开或者闭合menu
//
//	public SlideMenuView(Context context) {
//		super(context);
//	}
//
//	public SlideMenuView(Context context, ViewGroup backView,
//			ViewGroup frontView) {
//		super(context);
//		setContent(backView, frontView);
//	}
//
//	public void setContent(ViewGroup backView, ViewGroup frontView) {
//		removeAllViews();
//		LayoutParams lp = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		bV = backView;
//		fV = new MLinearLayout(getContext());
//		fV.addView(frontView);
//		addView(bV, lp);
//		addView(fV, lp);
//	}
//
//	public SlideMenuView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		checkChildCount();
//		for (int i = 0; i < getChildCount(); i++) {
//			try {
//				View child = getChildAt(i);
//				int height = child.getMeasuredHeight();
//				int width = child.getMeasuredWidth();
//				if (i == 1) {
//					child.layout(leftMargin, 0, leftMargin + width, height);
//				} else {
//					child.layout(0, 0, width, height);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		// if (widthMode != MeasureSpec.EXACTLY) {
//		// throw new IllegalStateException(
//		// "ScrollLayout only canmCurScreen run at EXACTLY mode!");
//		// }
//		// final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		// if (heightMode != MeasureSpec.EXACTLY) {
//		// throw new IllegalStateException(
//		// "ScrollLayout only can run at EXACTLY mode!");
//		// }
//		final int count = getChildCount();
//		for (int i = 0; i < count; i++) {
//			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
//		}
//	}
//
//	/**
//	 * 使用之前先传入需要留下间隔的宽度。或者调用setGap(final View measureView)�?
//	 * 
//	 * @param gap
//	 *            间隔宽度
//	 */
//	public void setGap(int gap) {
//		this.gap = gap;
//		// invalidate();
//	}
//
//	/**
//	 * 使用之前先传入要留下的空间.或调用setGap(int gap).
//	 * 
//	 * @param measureView
//	 *            间隔宽度
//	 * @param gap
//	 *            间隔修正值，无需修正传入0
//	 */
//	public void setGap(final View measureView, final int resize) {
//		ViewTreeObserver vto = measureView.getViewTreeObserver();
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				int width = measureView.getMeasuredWidth();
//				gap = width;
//				getChildAt(0).setPadding(0, 0, gap + resize, 0);
//				setGap(gap + resize);
//			}
//		});
//	}
//
//	private void checkChildCount() {
//		if (2 < getChildCount()) {
//			throw new IndexOutOfBoundsException("只允许拥2个子View!");
//		}
//	}
//
//	private void setLeftMargin(int leftMargin) {
//		this.leftMargin = leftMargin;
//	}
//
//	private void ani(final int from, final int to, final int left, long duration) {
//		if (isAniPosting) {
//			return;
//		}
//		isAniPosting = true;
//		final View v = getChildAt(1);
//		TranslateAnimation translateAnimation = new TranslateAnimation(from,
//				to, 0, 0);
//		translateAnimation.setInterpolator(new DecelerateInterpolator());
//		translateAnimation.setFillAfter(true);
//		translateAnimation.setDuration(duration);
//		translateAnimation.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				setLeftMargin(left);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				v.clearAnimation();
//				v.layout(left, 0, left + getWidth(), v.getHeight());
//				isAniPosting = false;
//				BaseData.hideKeyBoard((Activity) getContext());
//			}
//		});
//		v.startAnimation(translateAnimation);
//		if (from > to) {
//			menuOpen = false;
//		} else if (from < to) {
//			menuOpen = true;
//		}
//	}
//
//	private void ani(final int from, final int to, final int left) {
//		ani(from, to, left, 300);
//	}
//
//	public boolean isMenuOpen() {
//		return menuOpen;
//	}
//
//	public void sliding(MenuPage menus) {
//		if (isMenuOpen()) {
//			ani(0, gap - getWidth(), 0);
//		} else {
//			ani(0, getWidth() - gap, getWidth() - gap);
//			// menus.setNewNumber();
//		}
//	}
//
//	private void sliding(int from) {
//		if (isMenuOpen()) {
//			ani(0, -from, 0, 200);
//		} else {
//			ani(0, getWidth() - from - gap, getWidth() - gap, 200);
//		}
//	}
//
//	public void close() {
//		if (isMenuOpen()) {
//			ani(0, gap - getWidth(), 0, 0);
//		}
//	}
//
//	public class MLinearLayout extends LinearLayout {
//
//		// public MLinearLayout(Context context, AttributeSet attrs) {
//		// super(context, attrs);
//		// }
//
//		public MLinearLayout(Context context) {
//			super(context);
//		}
//
//		@Override
//		public boolean onInterceptTouchEvent(MotionEvent ev) {
//			int action = ev.getAction();
//			float rx = ev.getRawX();
//			float ry = ev.getRawY();
//			if (!scrollEnable) {
//				return false;
//			}
//			switch (action & MotionEvent.ACTION_MASK) {
//
//			case MotionEvent.ACTION_DOWN: {
//				lastX = (int) rx;
//				lastY = (int) ry;
//				break;
//			}
//
//			case MotionEvent.ACTION_MOVE: {
//				int dx = (int) (lastX - rx);
//				int dy = (int) (lastY - ry);
//				if (Math.abs(dx) > Math.abs(dy) + 5
//						&& ((dx > 0 && isMenuOpen()) || dx < 0)) {
//					reSetScrollRecords();
//					lastX = (int) rx;
//					lastY = (int) ry;
//					return true;
//				}
//				break;
//			}
//			case MotionEvent.ACTION_UP: {
//				break;
//			}
//			}
//			return super.onInterceptTouchEvent(ev);
//		}
//
//		@Override
//		public boolean onTouchEvent(MotionEvent event) {
//			if (null != MLinearLayout.this.getAnimation()) {
//				event.setAction(MotionEvent.ACTION_CANCEL);
//				return true;
//			}
//			float rx = event.getRawX();
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				reSetScrollRecords();
//				lastX = (int) rx;
//				return true;
//			case MotionEvent.ACTION_MOVE:
//				int dx = (int) (lastX - rx);
//				if (getLeft() - dx < 0
//						|| getLeft() - dx > getMeasuredWidth() - gap) {
//					// return true;
//				} else {
//					layout((int) (getLeft() - dx), 0, getMeasuredWidth()
//							+ (int) (getLeft() - dx), getMeasuredHeight());
//					lastX = (int) rx;
//					record(dx);
//				}
//				break;
//			case MotionEvent.ACTION_UP:
//				int left = getLeft();
//				if (isFastSroll()) {
//					sliding(left);
//					break;
//				}
//
//				if (left * 2 > getMeasuredWidth()) {
//					ani(0, getWidth() - left - gap, getWidth() - gap, 250);
//				} else {
//					ani(0, -left, 0, 250);
//				}
//				lastX = (int) rx;
//				break;
//			}
//			return super.onTouchEvent(event);
//		}
//
//		private boolean isFastSroll() {
//			if ((scrollCount > 0 && scrollCount < DEF_MAX_FAST_COUNT && sumDx
//					/ scrollCount >= DEF_MIN_FAST_DX)
//					|| (scrollCount == 0 && isMenuOpen())) {
//				return true;
//			}
//			return false;
//		}
//
//		private void reSetScrollRecords() {
//			scrollCount = 0;
//			sumDx = 0;
//		}
//
//		private void record(int dx) {
//			if (dx < 0) {
//				dx = -dx;
//			}
//			sumDx += dx;
//			scrollCount++;
//		}
//	}
//
//	public void setScrollEnable(boolean scrollEnable) {
//		this.scrollEnable = scrollEnable;
//	}
// }
