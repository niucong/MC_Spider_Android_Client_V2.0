package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;

public class LeftSlideView extends HorizontalScrollView {
	private LinearLayout ll;
	private Context c;
	private float ox, lastX;
	boolean isOpen, isActionAlive;
	private VelocityTracker mVelocityTracker;
	private final int DEF_MOVE_V = 1000;
	private final int DEF_MOVE_X = 80;
	private int mScreenWidth = BaseData.getScreenWidth();
	// private static ArrayList<LeftSlideView> collection = new
	// ArrayList<LeftSlideView>();
	private static LeftSlideView temView;

	public LeftSlideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LeftSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LeftSlideView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		c = context;
		setHorizontalFadingEdgeEnabled(false);
		setHorizontalScrollBarEnabled(false);
		initViews();
		initEvent();
	}

	public void initViews() {
		ll = new LinearLayout(c);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		addView(ll, new LayoutParams(mScreenWidth * 2,
				LayoutParams.WRAP_CONTENT));
	}

	public void initShowPage(ViewGroup showView) {
		ll.addView(showView, 0, new LinearLayout.LayoutParams(mScreenWidth,
				LayoutParams.WRAP_CONTENT));
	}

	public void initMenuPage(ViewGroup menuView, boolean isSimpleMode) {
		if (isSimpleMode) {
			ll.addView(getSimpleBackView(menuView), 1,
					new LayoutParams(mScreenWidth,
							LayoutParams.MATCH_PARENT));
		} else {
			ll.addView(menuView, 1, new LinearLayout.LayoutParams(mScreenWidth,
					LayoutParams.MATCH_PARENT));
		}
	}

	private LinearLayout getSimpleBackView(View menuView) {
		LinearLayout ll = new LinearLayout(c);
		ll.setBackgroundDrawable(menuView.getBackground());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT);
		llp.weight = 1;
		ll.addView(menuView, llp);
		ImageView iv = new ImageView(c);
		iv.setBackgroundDrawable(c.getResources().getDrawable(
				R.drawable.btn_orage));
		ll.addView(iv, new LinearLayout.LayoutParams(50,
				LayoutParams.MATCH_PARENT));
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				close();
			}
		});
		return ll;
	}

	private void initEvent() {

	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// float dx = ev.getX();
	// float dy = ev.getY();
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_MOVE:
	// if (Math.abs(dx) > Math.abs(dy)) {
	// return true;
	// }
	// break;
	// default:
	// break;
	// }
	// return super.dispatchTouchEvent(ev);
	// }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		float x = event.getRawX();
		float y = event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			ox = x;
			isActionAlive = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if ((isOpen && x < lastX) || (!isOpen && x > lastX)) {
				isActionAlive = false;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000);
			if (mVelocityTracker.getXVelocity() < -DEF_MOVE_V // 主动滑动条件：速度快或距离长
					|| (isActionAlive && ox - x > DEF_MOVE_X)) {
				open();
			} else if (mVelocityTracker.getXVelocity() > DEF_MOVE_V
					|| (isActionAlive && ox - x < -DEF_MOVE_X)) {
				close();
			} else {
				doRelease();
			}
			releaseVelocityTracker();
			return false;
		default:
			break;
		}
		lastX = x;
		return super.onTouchEvent(event);
	}

	private void releaseVelocityTracker() {
		if (null != mVelocityTracker) {
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private void doRelease() {
		int scroll = getScrollX();
		if (0 == scroll || getWidth() == scroll) {
			return;
		}
		if (scroll >= getWidth() / 2) { // 恢复
			open();
		} else {
			close();
		}
	}

	public void open() {
		smoothScrollTo(getWidth(), 0);
		isOpen = true;
		closeAll();
		// collection.add(this);
		temView = this;
	}

	public void close() {
		smoothScrollTo(0, 0);
		isOpen = false;
		// collection.remove(this);
		temView = null;
	}

	// public void closeAll(){
	// for (LeftSlideView slideView : collection) {
	// if(this != slideView){
	// slideView.close();
	// }
	// }
	// }

	public void closeAll() {
		if (null != temView && this != temView) {
			temView.close();
		}
	}

	public void closeImdtly() {
		scrollTo(0, 0);
		isOpen = false;
		// collection.remove(this);
		temView = null;
	}

}
