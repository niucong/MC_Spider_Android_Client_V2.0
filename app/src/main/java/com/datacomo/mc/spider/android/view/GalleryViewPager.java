/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * This class implements method to help <b>TouchImageView</b> fling, draggin and
 * scaling.
 */
public class GalleryViewPager extends ViewPager {
	// private final String TAG = "GalleryViewPager";

	PointF last;
	public PhotoView mCurrentView;

	public GalleryViewPager(Context context) {
		super(context);
	}

	public GalleryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// private float[] handleMotionEvent(MotionEvent event) {
	// switch (event.getAction() & MotionEvent.ACTION_MASK) {
	// case MotionEvent.ACTION_DOWN:
	// last = new PointF(event.getX(0), event.getY(0));
	// break;
	// case MotionEvent.ACTION_MOVE:
	// case MotionEvent.ACTION_UP:
	// PointF curr = new PointF(event.getX(0), event.getY(0));
	// return new float[] { curr.x - last.x, curr.y - last.y };
	//
	// }
	// return null;
	// }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// if ((event.getAction() & MotionEvent.ACTION_MASK) ==
		// MotionEvent.ACTION_UP) {
		// L.d(TAG, "vp_onTouchEvent");
		// super.onTouchEvent(event);
		// }
		//
		// boolean temp = true;
		// if (null != mCurrentView)
		// temp = mCurrentView.pagerCanScroll();
		// L.d(TAG, "onTouchEvent temp=" + temp + ",Action=" +
		// event.getAction());
		// if (temp) {
		return super.onTouchEvent(event);
		// } else {
		// float[] difference = handleMotionEvent(event);
		// if (difference != null // && mCurrentView.onRightSide
		// && difference[0] < 0) // move right
		// {
		// return super.onTouchEvent(event);
		// }
		// if (difference != null // && mCurrentView.onLeftSide
		// && difference[0] > 0) // move left
		// {
		// return super.onTouchEvent(event);
		// }
		// if (difference == null) {
		// // && (mCurrentView.onLeftSide || mCurrentView.onRightSide)
		// return super.onTouchEvent(event);
		// }
		// }
		//
		// return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// L.d(TAG, "onInterceptTouchEvent");
		// if ((event.getAction() & MotionEvent.ACTION_MASK) ==
		// MotionEvent.ACTION_UP) {
		// super.onInterceptTouchEvent(event);
		// }
		//
		// boolean temp = true;
		// if (null != mCurrentView)
		// temp = mCurrentView.pagerCanScroll();
		// L.d(TAG,
		// "onInterceptTouchEvent temp=" + temp + ",Action="
		// + event.getAction());
		// if (temp) {
		try {
			return super.onInterceptTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// } else {
		// float[] difference = handleMotionEvent(event);
		// if (difference != null // && mCurrentView.onRightSide
		// && difference[0] < 0) // move right
		// {
		// return super.onInterceptTouchEvent(event);
		// }
		// if (difference != null // && mCurrentView.onLeftSide
		// && difference[0] > 0) // move left
		// {
		// return super.onInterceptTouchEvent(event);
		// }
		// if (difference == null) {
		// // && (mCurrentView.onLeftSide || mCurrentView.onRightSide)
		// return super.onInterceptTouchEvent(event);
		// }
		// }
		// return false;
		return false;
	}
}
