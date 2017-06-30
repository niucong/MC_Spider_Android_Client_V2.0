package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.RemoteImageView;

public class ViewPagerAdapter extends PagerAdapter {
	private ArrayList<RemoteImageView> linearLayouts;
	private ArrayList<String> urls;

	public ViewPagerAdapter(ArrayList<RemoteImageView> linearLayouts,
			ArrayList<String> urls) {
		super();
		this.linearLayouts = linearLayouts;
		this.urls = urls;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(linearLayouts.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return linearLayouts.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		RemoteImageView v = linearLayouts.get(arg1);
		if (v.getParent() != null) {

		} else {
			String url = ThumbnailImageUrl.getThumbnailImageUrl(urls.get(arg1),
					ImageSizeEnum.THREE_HUNDRED);
			 v.setDefaultImage(R.drawable.icon_other_loading);
			v.setImageUrl(url);
			((ViewPager) arg0).addView(v, 0);
		}
		return v;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// String viewName=arg0.getClass().getSimpleName();
		// String xviewName=arg1.getClass().getSimpleName();
		// return viewName.equals(xviewName);
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
