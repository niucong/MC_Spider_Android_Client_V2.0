package com.datacomo.mc.spider.android.view;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.HandlerUtil;

public class GroupHomeScrollView extends ScrollView {
	private final int TAG_LONGPRESS_TIME = 1000;
	private final int TAG_PUSHUP = 2;

	public int TOP_OFFSET = -100;
	public int TOP_MIDDLE;

	/**
	 * LabelHieght 应该和labelView
	 * {@link LabelRow} 高度一致
	 */
	private int LabelHieght = 60; // 默认的游标栏高度是60

	private Context mContext;
	private boolean isTop = true;
	private float lastx, lasty, lastImgY;
	private boolean enableTrack;
	private int trackIndex; // 滑动计数 长按计数
	private Scroller mScroller;
	private int fullHeight, fullWidth; // 状态栏高度
	private boolean longClickFlag;
	private ImageView img;
	private float defZoom;
	private float zoom = 1;
	private SetMatixHandler mHandler;
	private Bitmap skinBmp;
	private HomeScrollListener listener;

	public GroupHomeScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public GroupHomeScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public GroupHomeScrollView(Context context) {
		super(context, null, 0);
		init(context);
	}

	private void init(Context c) {
		mContext = c;
		LabelHieght = BaseData.getHeaderHeight((Activity) mContext) - 10;
		fullHeight = getFullHeight();
		mScroller = new Scroller(mContext);
		mHandler = new SetMatixHandler();

		addView(initViews(), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		checkTop(TOP_OFFSET);
	}

	public void setOnHomeScrollListener(HomeScrollListener homeScrollListener) {
		listener = homeScrollListener;
	}

	private ViewGroup initViews() {
		final LinearLayout content = (LinearLayout) LayoutInflater.from(
				mContext).inflate(R.layout.layout_group_home, null);
		img = (ImageView) content.findViewById(R.id.skin);
		FrameLayout frame = (FrameLayout) content.findViewById(R.id.container);
		RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) img
				.getLayoutParams();
		lp1.height = fullHeight / 2 + LabelHieght;
		img.setLayoutParams(lp1);
		// setSkinTouchEvent();
		TOP_OFFSET = -BaseData.getHeaderHeight(mContext);
		TOP_MIDDLE = -(lp1.height - LabelHieght);
		LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) frame
				.getLayoutParams();
		lp2.height = fullHeight - LabelHieght;
		frame.setLayoutParams(lp2);
		frame.setBackgroundResource(R.drawable.bg_main_white);

		View poster = content.findViewById(R.id.poster);
		RelativeLayout.LayoutParams posterlp = (RelativeLayout.LayoutParams) poster
				.getLayoutParams();
		posterlp.width = fullWidth / 4;
		posterlp.height = fullWidth / 4;
		posterlp.topMargin = BaseData.getHeaderHeight(mContext)
				+ DensityUtil.dip2px(mContext, 15);
		poster.setLayoutParams(posterlp);

		setImgByRes(R.drawable.nothing); // 设置skin 默认透明图片
		return content;
	}

	private void setSkinTouchEvent() {
		img.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				try {
					ImageView view = (ImageView) v;
					float y = ev.getRawY();
					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastImgY = y;
						break;
					case MotionEvent.ACTION_MOVE:
						int dY = (int) ((lastImgY - y) / 5); // 移动距离是5分之一
						int toTop = (int) (getScrollY() + dY);
						if (isTop && toTop <= -TOP_OFFSET && toTop >= 0) {
							if (toTop < 0) {
								toTop = 0;
							}
							if (toTop > -TOP_OFFSET) {
								toTop = -TOP_OFFSET;
							}

							if (!mScroller.isFinished()) {
								mScroller.abortAnimation();
							}
							scrollTo(0, toTop);

							Matrix mx = new Matrix();
							zoom += -(float) dY / 2500; // skin的变化速率
							if (zoom < defZoom) {
								zoom = defZoom;
							}
							mx.setScale(zoom, zoom);
							mx.preTranslate(
									(fullWidth - zoom * skinBmp.getWidth()) / 2,
									(fullHeight / 2 - zoom * skinBmp.getHeight()) / 2);
							view.setImageMatrix(mx);
						}
						lastImgY = y;
						break;
					case MotionEvent.ACTION_UP:
						if (isTop && getScrollY() < -TOP_OFFSET) { // 海报拉伸后放手复原
							checkTop(TOP_OFFSET);
						}
						new Thread() {
							public void run() {
								postImgScale(img);
							};
						}.start();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
	}

	private void postImgScale(ImageView view) {
		while (zoom > defZoom) {
			zoom -= 0.001;
			img.setTag(getNewMatrix(zoom));
			HandlerUtil.sendMsgToHandler(mHandler, 0, img, 0);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSkinImage(Bitmap bmp) {
		skinBmp = bmp;
		if (null == skinBmp) {
			// setImgByRes(R.drawable.default_skin);
			return;
		}

		int bmpW = bmp.getWidth();
		int bmpH = bmp.getHeight();

		int imgW = fullWidth;
		int imgH = fullHeight / 2;

		float zoomX = imgW / (float) bmpW;
		float zoomY = imgH / (float) bmpH;
		zoom = Math.max(zoomX, zoomY);

		defZoom = zoom;
		img.setImageBitmap(bmp);
		img.setImageMatrix(getNewMatrix(zoom));

		setSkinTouchEvent();
	}

	private void setImgByRes(int imgResId) {
		InputStream is = mContext.getResources().openRawResource(imgResId);
		Bitmap defBmp = BitmapFactory.decodeStream(is);
		setSkinImage(defBmp);
	}

	private Matrix getNewMatrix(float zoom) {
		if (null == skinBmp) {
			return null;
		}
		Matrix mx = new Matrix();
		mx.setScale(zoom, zoom);
		mx.preTranslate((fullWidth - zoom * skinBmp.getWidth()) / 2,
				(fullHeight / 2 - zoom * skinBmp.getHeight()) / 2); // 居中靠下
		return mx;
	}

	private int getFullHeight() {
		fullWidth = BaseData.getScreenWidth();
		return BaseData.getScreenHeight()
				- BaseData.getStateBarHeight(mContext)
				- BaseData.getHeaderHeight(mContext); // 减去状态栏高度 和
		// title高度
	}

	public void checkTop(int requestTop) {
		if (!mScroller.isFinished()) {
			return;
		}
		mScroller.startScroll(0, getScrollY(), 0, -(requestTop + getScrollY()),
				1500);
		invalidate();
		if (TOP_OFFSET == requestTop) {
			isTop = true;
		} else if (TOP_MIDDLE == requestTop) {
			isTop = false;
		}
		if (null != listener)
			listener.onCheckTop(isTop);
	}

	public boolean isTop() {
		return isTop;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float x = ev.getRawX();
		float y = ev.getRawY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// long cur = System.currentTimeMillis();
			// if (cur - lastDown < 300) {
			// checkTop(TOP_OFFSET); // 双击，滚动到顶部,
			// }
			// lastDown = cur;

			longClickFlag = true;

			enableTrack = true;
			lastx = x;
			lasty = y;
			break;
		case MotionEvent.ACTION_MOVE:
			if (enableTrack && isTop) { // 判断上推
				if (y <= lasty) {
					if (y < lasty)
						trackIndex++;
				} else {
					trackIndex = 0;
				}
				if (trackIndex > TAG_PUSHUP) {
					checkTop(TOP_MIDDLE);
					ev.setAction(MotionEvent.ACTION_CANCEL);
					resetIndex();
				}
			} else {
				resetIndex();
			}

			// if (!isTop) { //舍弃 判断长按 三星手机认定按住不动的时候没有任何事件。
			// if (y - lasty <= 2 && x - lastx <= 2) {
			// holdIndex++;
			// } else {
			// holdIndex = 0;
			// }
			// if (holdIndex > TAG_LONGPRESS) {
			// checkTop(TOP_OFFSET);
			// return false;
			// }
			// }

			if (Math.abs(y - lasty) > 5 || Math.abs(x - lastx) > 5) { // 明显滑动
																		// 取消长按监听
				longClickFlag = false;
			}
			if (longClickFlag
					&& !isTop()
					&& ev.getEventTime() - ev.getDownTime() > TAG_LONGPRESS_TIME) {
				longClickFlag = false;
				ev.setAction(MotionEvent.ACTION_CANCEL);
				checkTop(TOP_OFFSET);
			}

			lastx = x;
			lasty = y;
			break;
		case MotionEvent.ACTION_UP:
			resetIndex();
			longClickFlag = false;
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void resetIndex() {
		trackIndex = 0;
		enableTrack = false;
	}

	@SuppressLint("HandlerLeak")
	class SetMatixHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ImageView imgView = (ImageView) msg.obj;
				imgView.setImageMatrix((Matrix) imgView.getTag());
				break;
			}
		}
	}

	public interface HomeScrollListener {
		void onCheckTop(boolean isTop);
	}
}
