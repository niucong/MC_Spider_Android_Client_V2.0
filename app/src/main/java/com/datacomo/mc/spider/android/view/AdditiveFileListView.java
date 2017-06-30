package com.datacomo.mc.spider.android.view;

import com.datacomo.mc.spider.android.url.L;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class AdditiveFileListView extends ListView {
	private final String LOG_TAG = "AdditiveFileListView";
	private int mLastMotionY;
	private int mActivePointerId;
	private Handler mHandler;
	private final int HIDING = 0;
	private final int TOHIDE = 1;
	private final int SHOWING = 2;
	private int state;

	public AdditiveFileListView(Context context) {
		super(context);
	}

	public AdditiveFileListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setHandle(Handler handler) {
		mHandler = handler;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (GroupChoosedsHorScrollView.isShow()) {
			L.d(LOG_TAG, "onTouchEvent GroupChoosedsHorScrollView.isAdded():"
					+ GroupChoosedsHorScrollView.isShow());
			final int action = ev.getAction();
			switch (action & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				final float y = ev.getY();

				// Remember where the motion event started
				mLastMotionY = (int) y;
				mActivePointerId = ev.getPointerId(0);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				// Scroll to follow the motion event
				final int activePointerIndex = ev
						.findPointerIndex(mActivePointerId);
				final float y = ev.getY(activePointerIndex);
				final int deltaY = (int) (mLastMotionY - y);
				L.d(LOG_TAG, "MotionEvent.ACTION_MOVE deltaY：" + mLastMotionY
						+ "-" + y + "=" + deltaY);
				mLastMotionY = (int) y;
				L.d(LOG_TAG, "MotionEvent.ACTION_MOVE mLastMotionY："
						+ mLastMotionY);
				if (deltaY > 0 && state != TOHIDE) {
					L.d(LOG_TAG, "MotionEvent.ACTION_MOVE scroll");
					state = TOHIDE;
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = 1;
					mHandler.sendMessage(msg);
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				final float y = ev.getY();
				final int deltaY = (int) (mLastMotionY - y);
				L.d(LOG_TAG, "MotionEvent.ACTION_UP deltaY：" + mLastMotionY
						+ "-" + y + "=" + deltaY);
				if (deltaY > 0 && state != TOHIDE) {
					L.d(LOG_TAG, "MotionEvent.ACTION_UP scroll");
					state = TOHIDE;
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = 1;
					mHandler.sendMessage(msg);
				}
				break;
			}
			}
			L.d(LOG_TAG, "MotionEvent.ACTION_MOVE return true");
			return true;
		}
		state = HIDING;
		L.d(LOG_TAG,
				"MotionEvent.ACTION_MOVE return 1" + super.onTouchEvent(ev));
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	

}
