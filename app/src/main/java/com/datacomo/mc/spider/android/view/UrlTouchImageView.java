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

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.ImageStateEnum;
import com.datacomo.mc.spider.android.thread.AsyncOriginalImageLoader;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.thread.BasicAsyncImageDownLoad.ImageCallback;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.AnimationFrameUtil;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class UrlTouchImageView extends RelativeLayout implements
		OnPreDrawListener {
	private static final String TAG = "UrlTouchImageView";

	String mFilePath;
	@SuppressWarnings("deprecation")
	private final LayoutParams LOADEDPARAMS = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	private final LayoutParams FAILEDPARAMS = new LayoutParams(200, 200);
	/**
	 * 图片状态
	 */
	private ImageStateEnum mEnum_ImageState;
	// 引用类
	private AnimationDrawable mAnimationDrawable;
	// 声明组件
	protected LinearLayout mProgress;
	protected PhotoView mImageView;
	protected TextView mTxt_Progress;
	private Handler handler_extrinsic;
	protected LayoutInflater mInflater;
	private String mUrl;

	protected Context mContext;

	public UrlTouchImageView(Context ctx, Handler handler) {
		super(ctx);
		mContext = ctx;
		handler_extrinsic = handler;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();

	}

	public UrlTouchImageView(Context ctx, AttributeSet attrs, Handler handler) {
		super(ctx, attrs);
		mContext = ctx;
		handler_extrinsic = handler;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}

	public PhotoView getImageView() {
		return mImageView;
	}

	public String getUrl() {
		return mUrl;
	}

	protected void init() {
		mImageView = new PhotoView(mContext);
		mImageView.setLayoutParams(LOADEDPARAMS);
		mImageView.setVisibility(GONE);
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				L.d(TAG, "onclick");
				handler_extrinsic.sendEmptyMessage(2);
			}
		});
		this.addView(mImageView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mProgress = (LinearLayout) mInflater.inflate(R.layout.form_progress,
				null);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mTxt_Progress = (TextView) mProgress
				.findViewById(R.id.layout_form_progress_txt_progress);
		FAILEDPARAMS.addRule(CENTER_IN_PARENT);
		this.addView(mProgress, params);
	}

	public void setUrl(String originalUrl, boolean isOriginal,
			HashMap<String, Object> map) {
		L.d(TAG, "setUrl" + originalUrl);
		if (mEnum_ImageState == ImageStateEnum.LOADING)
			return;
		mUrl = originalUrl;
		mImageView.setTag(new Object[] { originalUrl, ImageStateEnum.DEFAULT,
				map });
		if (!originalUrl.endsWith(".gif")) {
			mTxt_Progress.setText("加载中...");
			if (originalUrl.startsWith("http")) {
				if (!isOriginal)
					originalUrl = ThumbnailImageUrl.getThumbnailImageUrl(
							originalUrl, ImageSizeEnum.EIGHT_HUNDRED);
				L.d(TAG, "setUrl" + originalUrl);

				// mImageView.setScaleType(ScaleType.CENTER_CROP);
				// mImageView.setScaleType(ScaleType.CENTER);

				// mImageView.setMaxWidth(BaseData.getScreenWidth());
				// mImageView.setMaxHeight(BaseData.getScreenHeight());
				// mImageView.setAdjustViewBounds(true);
				MyFinalBitmap.setProgressImage(mImageView, originalUrl,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								L.d(TAG, "onLoadingStarted...");
								Object[] temp = (Object[]) mImageView.getTag();
								mEnum_ImageState = ImageStateEnum.LOADING;
								mProgress.setVisibility(View.VISIBLE);
								mImageView.setVisibility(View.GONE);
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState, temp[2] });
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								L.d(TAG,
										"onLoadingFailed:"
												+ failReason.getType());
								Object[] temp = (Object[]) mImageView.getTag();
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) temp[2];
								if (map.containsKey(mUrl)) {
									L.d(TAG, "remove" + mUrl);
									map.remove(mUrl);
								}
								view.setLayoutParams(FAILEDPARAMS);
								mImageView.setImageBitmap(BitmapFactory
										.decodeStream(getResources()
												.openRawResource(
														TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
								mProgress.setVisibility(View.GONE);
								mImageView.setVisibility(View.VISIBLE);
								mEnum_ImageState = ImageStateEnum.FAILED;
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState });
							}

							@Override
							public void onStartProgress() {
								L.d(TAG, "onStartProgress 加载中..." + 0 + "%");
								Message msg = Message.obtain();
								msg.what = 0;
								msg.arg1 = 0;
								mHandler.sendMessage(msg);
							}

							@Override
							public void onLoadingProgress(int progress,
									int load, int len) {
								L.d(TAG, "onLoadingProgress 加载中..." + progress
										+ "%");
								Message msg = Message.obtain();
								msg.what = 0;
								msg.arg1 = load;
								msg.arg2 = len;
								mHandler.sendMessage(msg);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								L.d(TAG, "onLoadingComplete...");
								Object[] temp = (Object[]) mImageView.getTag();
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) temp[2];
								if (map.containsKey(mUrl)) {
									L.d(TAG, "onLoadingComplete remove" + mUrl);
									map.remove(mUrl);
								}
								mImageView.setLayoutParams(LOADEDPARAMS);
								mProgress.setVisibility(View.GONE);
								mImageView.setVisibility(View.VISIBLE);
								mEnum_ImageState = ImageStateEnum.LOADED;
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState });
							}

						});
			} else if (originalUrl.startsWith(ConstantUtil.SDCARD_PATH)) {
				MyFinalBitmap.setLoaclImage(mImageView, originalUrl,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								L.d(TAG, "start");
								Object[] temp = (Object[]) mImageView.getTag();
								mEnum_ImageState = ImageStateEnum.LOADING;
								mProgress.setVisibility(View.VISIBLE);
								mImageView.setVisibility(View.GONE);
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState, temp[2] });
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								L.d(TAG, "Fail:" + failReason.getType());
								Object[] temp = (Object[]) mImageView.getTag();
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) temp[2];
								if (map.containsKey(mUrl)) {
									L.d(TAG, "remove" + mUrl);
									map.remove(mUrl);
								}
								view.setLayoutParams(FAILEDPARAMS);
								mImageView.setImageBitmap(BitmapFactory
										.decodeStream(getResources()
												.openRawResource(
														TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
								mProgress.setVisibility(View.GONE);
								mImageView.setVisibility(View.VISIBLE);
								mEnum_ImageState = ImageStateEnum.FAILED;
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState });
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								L.d(TAG, "end");
								Object[] temp = (Object[]) mImageView.getTag();
								@SuppressWarnings("unchecked")
								HashMap<String, Object> map = (HashMap<String, Object>) temp[2];
								if (map.containsKey(mUrl)) {
									L.d(TAG, "remove" + mUrl);
									map.remove(mUrl);
								}
								mImageView.setLayoutParams(LOADEDPARAMS);
								mProgress.setVisibility(View.GONE);
								mImageView.setVisibility(View.VISIBLE);
								mEnum_ImageState = ImageStateEnum.LOADED;
								mImageView.setTag(new Object[] { temp[0],
										mEnum_ImageState });
							}
						});
			}
		} else {
			mTxt_Progress.setText("加载中...");
			Object[] result = (Object[]) new AsyncOriginalImageLoader(
					originalUrl, new String[] { originalUrl },
					ConstantUtil.IMAGE_PATH, mContext, mHandler,
					new ImageCallback() {

						@Override
						public void load(Object object, Object[] tags) {
							int tag = Integer.parseInt((String) tags[1]);
							Object[] temp = (Object[]) mImageView.getTag();
							@SuppressWarnings("unchecked")
							HashMap<String, Object> map = (HashMap<String, Object>) temp[2];
							if (map.containsKey(mUrl)) {
								L.d(TAG, "remove" + mUrl);
								map.remove(mUrl);
							}
							switch (tag) {
							case 1:// gif
								mImageView.setLayoutParams(LOADEDPARAMS);
								mEnum_ImageState = ImageStateEnum.LOADED;
								mAnimationDrawable = AnimationFrameUtil
										.getFrame(mFilePath, mImageView, 1);
								if (null == mAnimationDrawable) {
									mEnum_ImageState = ImageStateEnum.FAILED;
									mImageView.setLayoutParams(FAILEDPARAMS);
									mImageView.setImageBitmap(BitmapFactory
											.decodeStream(getResources()
													.openRawResource(
															TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
									mImageView.getViewTreeObserver()
											.removeOnPreDrawListener(
													UrlTouchImageView.this);
									mProgress.setVisibility(View.GONE);
									mImageView.setVisibility(View.VISIBLE);
								} else {
									mImageView.getViewTreeObserver()
											.addOnPreDrawListener(
													UrlTouchImageView.this);
									mProgress.setVisibility(View.GONE);
									mImageView.setVisibility(View.VISIBLE);
								}
								break;
							case 2:// 图片加载失败
								mEnum_ImageState = ImageStateEnum.FAILED;
								mImageView.setLayoutParams(FAILEDPARAMS);
								mImageView.setImageBitmap(BitmapFactory
										.decodeStream(getResources()
												.openRawResource(
														TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
								mImageView.getViewTreeObserver()
										.removeOnPreDrawListener(
												UrlTouchImageView.this);
								mProgress.setVisibility(View.GONE);
								mImageView.setVisibility(View.VISIBLE);
								break;
							}
							mImageView.setTag(new Object[] { temp[0],
									mEnum_ImageState });
						}
					}).getResult();
			mFilePath = (String) result[2];
			mEnum_ImageState = (ImageStateEnum) result[1];
			Object[] temp = (Object[]) mImageView.getTag();
			switch (mEnum_ImageState) {
			case FAILED:
				if (map.containsKey(mUrl)) {
					L.d(TAG, "remove" + mUrl);
					map.remove(mUrl);
				}
				mImageView.setLayoutParams(FAILEDPARAMS);
				mImageView.setImageBitmap(BitmapFactory
						.decodeStream(getResources().openRawResource(
								TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
				mImageView.getViewTreeObserver().removeOnPreDrawListener(
						UrlTouchImageView.this);
				// mImageView.setUnFocus(true);
				mImageView.setTag(new Object[] { temp[0], mEnum_ImageState });
				break;
			case LOADING:
				L.d(TAG, "loading");
				mTxt_Progress.setText("0%");
				mImageView.setLayoutParams(FAILEDPARAMS);
				mImageView.setImageBitmap(BitmapFactory
						.decodeStream(getResources().openRawResource(
								TaskUtil.IMGDEFAULTLOADSTATEIMG2[0])));
				mProgress.setVisibility(View.VISIBLE);
				mImageView.setVisibility(View.GONE);
				mImageView
						.setTag(new Object[] { temp[0], mEnum_ImageState, map });
				break;
			case LOADED:
				if (map.containsKey(mUrl)) {
					L.d(TAG, "remove" + mUrl);
					map.remove(mUrl);
				}
				L.d(TAG, "load");
				mEnum_ImageState = ImageStateEnum.LOADED;
				mImageView.setLayoutParams(LOADEDPARAMS);
				L.d(TAG, "mFilePath" + mFilePath);
				mAnimationDrawable = AnimationFrameUtil.getFrame(mFilePath,
						mImageView, 1);
				if (null == mAnimationDrawable) {
					L.d(TAG, "mAnimationDrawable+null");
					mImageView.setLayoutParams(FAILEDPARAMS);
					mImageView.setImageBitmap(BitmapFactory
							.decodeStream(getResources().openRawResource(
									TaskUtil.IMGDEFAULTLOADSTATEIMG2[1])));
					mImageView.getViewTreeObserver().removeOnPreDrawListener(
							this);
					mEnum_ImageState = ImageStateEnum.FAILED;
				} else {
					L.d(TAG, "mImageView." + mImageView.getVisibility());
					mImageView.getViewTreeObserver().addOnPreDrawListener(this);
				}
				mProgress.setVisibility(View.GONE);
				mImageView.setVisibility(View.VISIBLE);
				mImageView.setTag(new Object[] { temp[0], mEnum_ImageState });
				break;
			default:
				break;

			}
		}
	}

	public ImageStateEnum getImageState() {
		return mEnum_ImageState;
	}

	public void setImageState() {
		mEnum_ImageState = ImageStateEnum.LOADED;
	}

	public void recycle() {
		ImageDealUtil.releaseImageDrawable(mImageView, false);
	}

	@Override
	public boolean onPreDraw() {
		if (mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
		return true;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			L.d(TAG, "mHandler load=" + msg.arg1 + ",len=" + msg.arg2);
			switch (msg.what) {
			case 0:
				// mTxt_Progress.setText("加载中..." + msg.arg1 + "%");
				mTxt_Progress.setText("加载中...("
						+ FileUtil.computeFileSize(msg.arg1) + "/"
						+ FileUtil.computeFileSize(msg.arg2) + ")");
				break;

			default:
				break;
			}
		}
	};

}
