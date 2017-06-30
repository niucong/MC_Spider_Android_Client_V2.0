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
package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.datacomo.mc.spider.android.enums.ImageStateEnum;
import com.datacomo.mc.spider.android.view.GalleryViewPager;
import com.datacomo.mc.spider.android.view.UrlTouchImageView;

/**
 * Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b>
 * objects to paging up through them.
 */
public abstract class BasePagerAdapter extends PagerAdapter {
	private static final String TAG = "BasePagerAdapter";
	protected final Context mContext;
	protected HashMap<String, Object> loadCache;

	public BasePagerAdapter() {
		mContext = null;
		loadCache = new HashMap<String, Object>();
	}

	public BasePagerAdapter(Context context) {
		this.mContext = context;
		loadCache = new HashMap<String, Object>();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Log.d(TAG, "setPrimaryItem");
		// GalleryViewPager galleryContainer = ((GalleryViewPager) container);
		// if (null != object)
		// ((UrlTouchImageView) object).refreshUrl();
		// if (null != galleryContainer.mCurrentView)
		// galleryContainer.mCurrentView.resetScale();
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		Log.d(TAG, "destroyItem");
		UrlTouchImageView urlImage = (UrlTouchImageView) view;
		if (urlImage.getImageState() == ImageStateEnum.LOADING) {
			String key = urlImage.getUrl();
			Log.d("loadCache", "set" + key);
			loadCache.put(key, view);
		}
		((UrlTouchImageView) view).recycle();
		collection.removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		Log.d(TAG, "isViewFromObject");
		return view.equals(object);
	}

	@Override
	public void finishUpdate(ViewGroup arg0) {
		Log.d(TAG, "finishUpdate");
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		Log.d(TAG, "restoreState");
	}

	@Override
	public Parcelable saveState() {
		Log.d(TAG, "saveState");
		return null;
	}

	@Override
	public void startUpdate(ViewGroup arg0) {
		Log.d(TAG, "startUpdate");
	}

}
