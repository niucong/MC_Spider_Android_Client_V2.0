/*
 Copyright (c) 2012 Robert Foss, Roman Truba

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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.enums.ImageStateEnum;
import com.datacomo.mc.spider.android.url.L;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class TouchImageView extends ImageView {
	private static final String TAG = "TouchImageView";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	static final long DOUBLE_PRESS_INTERVAL = 600;
	static final float FRICTION = 0.9f;

	// We can be in one of these 4 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int CLICK = 10;
	int mode = NONE;

	float redundantXSpace, redundantYSpace;
	float right, bottom, origWidth, origHeight, bmWidth, bmHeight;
	float width, height;
	PointF last = new PointF();
	PointF mid = new PointF();
	PointF start = new PointF();
	float[] m;
	float matrixX, matrixY;

	float saveScale = 1f;
	float minScale = 1f;
	float maxScale = 5f;
	float oldDist = 1f;

	PointF lastDelta = new PointF(0, 0);
	float velocity = 0;

	long lastPressTime = 0, lastDragTime = 0;
	boolean allowInert = false;
	private boolean mUnFocus = false;

	private Context mContext;
	private Timer mClickTimer;
	private OnClickListener mOnClickListener;
	private Object mScaleDetector;
	private Handler mTimerHandler = null;

	private ImageStateEnum mEnum_ImageState;

	// Scale mode on DoubleTap
	private boolean zoomToOriginalSize = false;

	public void setImageStateEnum(ImageStateEnum enum_ImageState) {
		mEnum_ImageState = enum_ImageState;
	}

	public ImageStateEnum getImageTypeEnum() {
		if (null == mEnum_ImageState)
			mEnum_ImageState = ImageStateEnum.DEFAULT;
		return mEnum_ImageState;
	}

	public boolean isZoomToOriginalSize() {
		return this.zoomToOriginalSize;
	}

	public void setZoomToOriginalSize(boolean zoomToOriginalSize) {
		this.zoomToOriginalSize = zoomToOriginalSize;
	}

	public boolean onLeftSide = false, onTopSide = false, onRightSide = false,
			onBottomSide = false;

	public TouchImageView(Context context) {
		super(context);
		super.setClickable(true);
		this.mContext = context;

		init();
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setClickable(true);
		this.mContext = context;

		init();
	}

	protected void init() {
		mTimerHandler = new TimeHandler(this);
		matrix.setTranslate(1f, 1f);// 初始状态应该这里？
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);
		if (Build.VERSION.SDK_INT >= 8) {
			mScaleDetector = new ScaleGestureDetector(mContext,
					new ScaleListener());
		}
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent rawEvent) {
				L.d(TAG, "init ontouch");
				if (mUnFocus) {
					return false;
				}
				WrapMotionEvent event = WrapMotionEvent.wrap(rawEvent);
				if (mScaleDetector != null) {
					((ScaleGestureDetector) mScaleDetector)
							.onTouchEvent(rawEvent);
				}
				fillMatrixXY();
				PointF curr = new PointF(event.getX(), event.getY());

				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "init ACTION_DOWN");
					allowInert = false;
					savedMatrix.set(matrix);
					last.set(event.getX(), event.getY());
					start.set(last);
					mode = DRAG;

					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					Log.d(TAG, "ACTION_POINTER_DOWN");
					oldDist = spacing(event);
					Log.d(TAG, "init oldDist=" + oldDist);
					if (oldDist > 10f) {
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
						// Log.d(TAG, "mode=ZOOM");
					}
					break;
				case MotionEvent.ACTION_UP:
					Log.d(TAG, "init ACTION_UP");
					allowInert = true;
					mode = NONE;
					int xDiff = (int) Math.abs(event.getX() - start.x);
					int yDiff = (int) Math.abs(event.getY() - start.y);
					Log.d(TAG, "init xDiff=" + xDiff + ",yDiff=" + yDiff
							+ ",CLICK=" + CLICK);
					if (xDiff < CLICK && yDiff < CLICK) {
						// Perform scale on double click
						long pressTime = System.currentTimeMillis();
						if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
							Log.d(TAG, "init pressTime - lastPressTime="
									+ (pressTime - lastPressTime));
							if (mClickTimer != null)
								mClickTimer.cancel();
							if (saveScale == 1) {
								L.i(TAG, "init maxScale=" + maxScale
										+ ",saveScale=" + saveScale
										+ ",start.x=" + start.x + ",start.y="
										+ start.y);
								final float targetScale = maxScale / saveScale;
								matrix.postScale(targetScale, targetScale,
										start.x, start.y);
								saveScale = maxScale;
							} else {
								L.i(TAG, "init minScale=" + minScale
										+ ",saveScale=" + saveScale + ",width="
										+ width + ",height=" + height);
								final float targetScale = minScale / saveScale;
								matrix.postScale(targetScale, targetScale,
										width / 2, height / 2);
								saveScale = minScale;
							}
							calcPadding();
							checkAndSetTranslate(0, 0);
							lastPressTime = 0;
						} else {
							lastPressTime = pressTime;
							mClickTimer = new Timer();
							mClickTimer.schedule(new Task(), 300);
						}
						if (saveScale == minScale) {
							scaleMatrixToBounds();
						}
					}

					break;

				case MotionEvent.ACTION_POINTER_UP:
					Log.d(TAG, "init ACTION_POINTER_UP");
					mode = NONE;
					velocity = 0;
					savedMatrix.set(matrix);
					oldDist = spacing(event);
					Log.d(TAG, "init oldDist=" + oldDist);
					break;

				case MotionEvent.ACTION_MOVE:
					Log.d(TAG, "init ACTION_MOVE");
					allowInert = false;
					if (mode == DRAG) {
						float deltaX = curr.x - last.x;
						float deltaY = curr.y - last.y;

						long dragTime = System.currentTimeMillis();

						velocity = (float) distanceBetween(curr, last)
								/ (dragTime - lastDragTime) * FRICTION;
						lastDragTime = dragTime;

						checkAndSetTranslate(deltaX, deltaY);
						lastDelta.set(deltaX, deltaY);
						last.set(curr.x, curr.y);
					} else if (mScaleDetector == null && mode == ZOOM) {
						float newDist = spacing(event);
						if (rawEvent.getPointerCount() < 2)
							break;
						if (10 > Math.abs(oldDist - newDist)
								|| Math.abs(oldDist - newDist) > 50)
							break;
						float mScaleFactor = newDist / oldDist;
						oldDist = newDist;

						float origScale = saveScale;
						saveScale *= mScaleFactor;

						Log.d(TAG, "init ACTION_MOVE saveScale=" + saveScale);
						if (saveScale > maxScale) {
							saveScale = maxScale;
							mScaleFactor = maxScale / origScale;
						} else if (saveScale < minScale) {
							saveScale = minScale;
							mScaleFactor = minScale / origScale;
						}

						calcPadding();
						Log.d(TAG, "init ACTION_MOVE origWidth=" + origWidth
								+ ",width=" + width + ",origHeight="
								+ origHeight + ",height=" + height);
						if (origWidth * saveScale <= width
								|| origHeight * saveScale <= height) {
							matrix.postScale(mScaleFactor, mScaleFactor,
									width / 2, height / 2);
							if (mScaleFactor < 1) {
								fillMatrixXY();
								if (mScaleFactor < 1) {
									scaleMatrixToBounds();
								}
							}
						} else {
							PointF mid = midPointF(event);
							matrix.postScale(mScaleFactor, mScaleFactor, mid.x,
									mid.y);
							fillMatrixXY();
							if (mScaleFactor < 1) {
								if (matrixX < -right)
									matrix.postTranslate(-(matrixX + right), 0);
								else if (matrixX > 0)
									matrix.postTranslate(-matrixX, 0);
								if (matrixY < -bottom)
									matrix.postTranslate(0, -(matrixY + bottom));
								else if (matrixY > 0)
									matrix.postTranslate(0, -matrixY);
							}
						}
						checkSiding();
					}
					break;
				}

				setImageMatrix(matrix);
				invalidate();
				return false;
			}
		});
	}

	public void setUnFocus(boolean focus) {
		mUnFocus = focus;
	}

	public Boolean getUnFocus() {
		return mUnFocus;
	}

	public void resetScale() {
		fillMatrixXY();
		matrix.postScale(minScale / saveScale, minScale / saveScale, width / 2,
				height / 2);
		saveScale = minScale;

		calcPadding();
		checkAndSetTranslate(0, 0);

		scaleMatrixToBounds();

		setImageMatrix(matrix);
		invalidate();
	}

	public boolean pagerCanScroll() {
		L.d(TAG, "pagerCanScroll mUnFocus:" + mUnFocus);
		if (mUnFocus)
			return true;

		L.d(TAG, "pagerCanScroll mode != NONE" + (mode != NONE));
		if (mode != NONE)
			return false;

		L.d(TAG, "pagerCanScroll saveScale == minScale"
				+ (saveScale == minScale));
		return saveScale == minScale;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!allowInert)
			return;
		final float deltaX = lastDelta.x * velocity, deltaY = lastDelta.y
				* velocity;
		if (deltaX > width || deltaY > height) {
			return;
		}
		velocity *= FRICTION;
		if (Math.abs(deltaX) < 0.1 && Math.abs(deltaY) < 0.1)
			return;
		checkAndSetTranslate(deltaX, deltaY);
		setImageMatrix(matrix);
	}

	private void checkAndSetTranslate(float deltaX, float deltaY) {
		float scaleWidth = Math.round(origWidth * saveScale);
		float scaleHeight = Math.round(origHeight * saveScale);
		fillMatrixXY();
		if (scaleWidth < width) {
			deltaX = 0;
			if (matrixY + deltaY > 0)
				deltaY = -matrixY;
			else if (matrixY + deltaY < -bottom)
				deltaY = -(matrixY + bottom);
		} else if (scaleHeight < height) {
			deltaY = 0;
			if (matrixX + deltaX > 0)
				deltaX = -matrixX;
			else if (matrixX + deltaX < -right)
				deltaX = -(matrixX + right);
		} else {
			if (matrixX + deltaX > 0)
				deltaX = -matrixX;
			else if (matrixX + deltaX < -right)
				deltaX = -(matrixX + right);

			if (matrixY + deltaY > 0)
				deltaY = -matrixY;
			else if (matrixY + deltaY < -bottom)
				deltaY = -(matrixY + bottom);
		}
		matrix.postTranslate(deltaX, deltaY);
		checkSiding();
	}

	private void checkSiding() {
		Log.i(TAG, "checkSiding x=" + matrixX + ",y=" + matrixY + ",left="
				+ right / 2 + ",top=" + bottom / 2);
		fillMatrixXY();
		float scaleWidth = Math.round(origWidth * saveScale);
		float scaleHeight = Math.round(origHeight * saveScale);
		onLeftSide = onRightSide = onTopSide = onBottomSide = false;
		if (-matrixX < 10.0f)
			onLeftSide = true;
		Log.d(TAG, String.format("checkSiding ScaleW: %f; W: %f, MatrixX: %f",
				scaleWidth, width, matrixX));

		if ((scaleWidth >= width && (matrixX + scaleWidth - width) < 10)
				|| (scaleWidth <= width && -matrixX + scaleWidth <= width))
			onRightSide = true;
		if (-matrixY < 10.0f)
			onTopSide = true;
		if (Math.abs(-matrixY + height - scaleHeight) < 10.0f)
			onBottomSide = true;
	}

	private void calcPadding() {
		Log.i(TAG, "calcPadding right=" + right + ",bottom=" + bottom);
		right = width * saveScale - width - (2 * redundantXSpace * saveScale);
		bottom = height * saveScale - height
				- (2 * redundantYSpace * saveScale);
		Log.d(TAG, "calcPadding right=" + right + ",bottom=" + bottom);
	}

	private void fillMatrixXY() {
		matrix.getValues(m);
		matrixX = m[Matrix.MTRANS_X];
		matrixY = m[Matrix.MTRANS_Y];
	}

	private void scaleMatrixToBounds() {
		if (Math.abs(matrixX + right / 2) > 0.5f)
			matrix.postTranslate(-(matrixX + right / 2), 0);
		if (Math.abs(matrixY + bottom / 2) > 0.5f)
			matrix.postTranslate(0, -(matrixY + bottom / 2));
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		L.d(TAG, "w" + bmWidth + " " + "h" + bmHeight);
		bmWidth = bm.getWidth();
		bmHeight = bm.getHeight();
		L.d(TAG, "w" + bmWidth + " " + "h" + bmHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// TODO
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		// Fit to screen.
		float scale;
		float scaleX = width / bmWidth;
		float scaleY = height / bmHeight;
		scale = Math.min(scaleX, scaleY);
		matrix.setScale(scale, scale);
		// minScale = scale;
		setImageMatrix(matrix);
		saveScale = 1f;

		// Center the image
		redundantYSpace = height - (scale * bmHeight);
		redundantXSpace = width - (scale * bmWidth);
		redundantYSpace /= (float) 2;
		redundantXSpace /= (float) 2;

		matrix.postTranslate(redundantXSpace, redundantYSpace);

		origWidth = width - 2 * redundantXSpace;
		origHeight = height - 2 * redundantYSpace;
		calcPadding();
		setImageMatrix(matrix);
	}

	private double distanceBetween(PointF left, PointF right) {
		return Math.sqrt(Math.pow(left.x - right.x, 2)
				+ Math.pow(left.y - right.y, 2));
	}

	/** Determine the space between the first two fingers */
	private float spacing(WrapMotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, WrapMotionEvent event) {
		// ...
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private PointF midPointF(WrapMotionEvent event) {
		// ...
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		return new PointF(x / 2, y / 2);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		mOnClickListener = l;
	}

	private class Task extends TimerTask {
		public void run() {
			mTimerHandler.sendEmptyMessage(0);
		}
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mode = ZOOM;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float mScaleFactor = (float) Math.min(
					Math.max(.95f, detector.getScaleFactor()), 1.05);
			float origScale = saveScale;
			saveScale *= mScaleFactor;
			if (saveScale > maxScale) {
				saveScale = maxScale;
				mScaleFactor = maxScale / origScale;
			} else if (saveScale < minScale) {
				saveScale = minScale;
				mScaleFactor = minScale / origScale;
			}
			right = width * saveScale - width
					- (2 * redundantXSpace * saveScale);
			bottom = height * saveScale - height
					- (2 * redundantYSpace * saveScale);
			if (origWidth * saveScale <= width
					|| origHeight * saveScale <= height) {
				matrix.postScale(mScaleFactor, mScaleFactor, width / 2,
						height / 2);
				if (mScaleFactor < 1) {
					matrix.getValues(m);
					float x = m[Matrix.MTRANS_X];
					float y = m[Matrix.MTRANS_Y];
					if (mScaleFactor < 1) {
						if (Math.round(origWidth * saveScale) < width) {
							if (y < -bottom)
								matrix.postTranslate(0, -(y + bottom));
							else if (y > 0)
								matrix.postTranslate(0, -y);
						} else {
							if (x < -right)
								matrix.postTranslate(-(x + right), 0);
							else if (x > 0)
								matrix.postTranslate(-x, 0);
						}
					}
				}
			} else {
				matrix.postScale(mScaleFactor, mScaleFactor,
						detector.getFocusX(), detector.getFocusY());
				matrix.getValues(m);
				float x = m[Matrix.MTRANS_X];
				float y = m[Matrix.MTRANS_Y];
				if (mScaleFactor < 1) {
					if (x < -right)
						matrix.postTranslate(-(x + right), 0);
					else if (x > 0)
						matrix.postTranslate(-x, 0);
					if (y < -bottom)
						matrix.postTranslate(0, -(y + bottom));
					else if (y > 0)
						matrix.postTranslate(0, -y);
				}
			}
			return true;

		}
	}

	static class TimeHandler extends Handler {
		private final WeakReference<TouchImageView> mService;

		TimeHandler(TouchImageView view) {
			mService = new WeakReference<TouchImageView>(view);

		}

		@Override
		public void handleMessage(Message msg) {
			mService.get().performClick();
			if (mService.get().mOnClickListener != null)
				mService.get().mOnClickListener.onClick(mService.get());
		}
	}
}