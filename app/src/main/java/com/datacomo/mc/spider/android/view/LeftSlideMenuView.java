package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.datacomo.mc.spider.android.R;

public class LeftSlideMenuView extends FrameLayout {
	private boolean isOpen;
	Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private Context mContext;
	public LeftSlideMenuView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mScroller = new Scroller(context);
	}

	public void hardScroll(int startX, int deltaX, int duration) {
		checkIsScrolling(true);
		mScroller.startScroll(startX, 0, deltaX, 0, duration);
		invalidate();
	}
	
	public void scrollToPage(int toWhichPage, int duration) {
		checkIsScrolling(true);
		mScroller.startScroll(getScrollX(), 0, getWidth() * toWhichPage - getScrollX(), 0, duration);
		invalidate();
	}

	private boolean checkIsScrolling(boolean stop) {
		if(stop){
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			return stop;
		}
		return !mScroller.isFinished();
	}

	public void close() {
		if(checkIsScrolling(false)){
			return;
		}
		if (isOpen) {
			mScroller.startScroll(getScrollX(), 0, -getWidth(), 0, 500);
			invalidate();
			isOpen = false;
		}
	}

	public void open() {
		if(checkIsScrolling(false) && getChildCount() > 1){
			return;
		}
		if (!isOpen) {
			mScroller.startScroll(getScrollX(), 0, getWidth(), 0, 500);
			invalidate();
			isOpen = true;
		}
	}

	public void closeImdtly() {
		if (isOpen) {
			mScroller.startScroll(getScrollX(), 0, -getWidth(), 0, 0);
			invalidate();
			isOpen = false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mVelocityTracker.computeCurrentVelocity(1000);
			if (mVelocityTracker.getXVelocity() < -1000) {
				open();
			} else {
			}
			releaseVelocityTracker();
		default:
			break;
		}
		return true;
	}

//	@SuppressLint("DrawAllocation")
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		if (widthMode == MeasureSpec.UNSPECIFIED) {
//			return;
//		}
//		if (getChildCount() > 0) {
//			final View child = getChildAt(0);
//			int width = getMeasuredWidth();
//			if (child.getMeasuredWidth() <= width) {
//				final FrameLayout.LayoutParams lp = (LayoutParams) child
//						.getLayoutParams();
//
//				int childHeightMeasureSpec = getChildMeasureSpec(
//						heightMeasureSpec,
//						getPaddingLeft() + getPaddingRight(), lp.height);
//				width -= getPaddingRight();
//				width -= getPaddingLeft();
//				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
//						MeasureSpec.EXACTLY);
//				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//			}
//		}
//	}

	private void releaseVelocityTracker() {
		if (null != mVelocityTracker) {
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int height = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure(r - l, b - t);
			if (0 == i) {
				height = child.getMeasuredHeight();
			}
			child.layout(i * getWidth() + l, 0, l + (i + 1) * getWidth(),
					height);
		}
	}

	public void addShowingView(View frontView) {
		addView(frontView, 0, new LayoutParams(480, LayoutParams.WRAP_CONTENT));
	}

	
	
	public void addMenuView(View menuView, boolean isSimpleMenu) {
		if (isSimpleMenu) {
			addView(getSimpleBackView(menuView), 1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			addView(menuView, 1, new LayoutParams(430, LayoutParams.MATCH_PARENT));
		}
	}

	
	@SuppressWarnings("deprecation")
	private LinearLayout getSimpleBackView(View menuView) {
		LinearLayout ll = new LinearLayout(mContext);
		ll.setBackgroundDrawable(menuView.getBackground());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		llp.weight = 1;
		ll.addView(menuView, llp);
		ImageView iv = new ImageView(mContext);
		iv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_red));
		ll.addView(iv, new LinearLayout.LayoutParams(50, LayoutParams.MATCH_PARENT));
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				close();
			}
		});
		return ll;
	}
}
