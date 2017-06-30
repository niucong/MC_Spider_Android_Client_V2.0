/*
 Copyright (c) 2013 Roman Truba

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

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.AsyncBeanDownLoad;
import com.datacomo.mc.spider.android.thread.AsyncBeanDownLoad.BeanCallback;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.HandlerNumberUtil;
import com.datacomo.mc.spider.android.util.PageSizeUtil;
import com.datacomo.mc.spider.android.view.GalleryViewPager;
import com.datacomo.mc.spider.android.view.UrlTouchImageView;

/**
 * Class wraps URLs to adapter, then it instantiates {@link UrlTouchImageView}
 * objects to paging up through them.
 */
public class UrlPagerAdapter extends BasePagerAdapter implements
		HandlerNumberUtil {
	private static final String TAG = "UrlPagerAdapter";
	private Context mContext;
	private List<Object> mBeans;
	private ObjectInfoBean bean;
	private Handler mHandler;
	private String mType;
	private int mIndex;// 当前选中项
	private int mMaxSize;// 预加载中最大加载数量
	// private int mMinSize;// 预加载中最小加载数量
	private final int mSpace = 10;
	private boolean mFromPhotoGrid;
	private boolean mIsGetDetail;// 是否预加载数据
	private boolean mCanLoad; // 是否能判定加载
	private boolean mIsOriginal = false;

	public UrlPagerAdapter(Context context, List<Object> beans, Handler handler) {
		super();
		mContext = context;
		mBeans = beans;
		mHandler = handler;
		mFromPhotoGrid = false;
		mIsGetDetail = false;
		mMaxSize = 0;
		mType = "";

	}

	public UrlPagerAdapter(Context context, List<Object> beans,
			Handler handler, boolean fromPhotoGrid, String type) {
		super();
		mContext = context;
		mBeans = beans;
		mHandler = handler;
		mFromPhotoGrid = fromPhotoGrid;
		mIsGetDetail = false;
		mMaxSize = 0;
		mType = type;
	}

	public UrlPagerAdapter(Context context, List<Object> beans,
			Handler handler, boolean fromPhotoGrid, boolean isGetDetail,
			int size, String type) {
		mContext = context;
		mBeans = beans;
		mHandler = handler;
		mFromPhotoGrid = fromPhotoGrid;
		mIsGetDetail = isGetDetail;
		mMaxSize = size;
		mType = type;
	}

	public Object getItem(int position) {
		if (mFromPhotoGrid) {
			if (mIsGetDetail && position > mBeans.size() - 1) {
				return null;
			} else {
				return mBeans.get(position);
			}
		} else {
			return mBeans.get(position);
		}
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		L.d(TAG, "setPrimaryItem");
		if (null != object) {
			((GalleryViewPager) container).mCurrentView = ((UrlTouchImageView) object)
					.getImageView();
			mIndex = position;
			L.d("index", mIndex + "");
			super.setPrimaryItem(container, position, object);
		}
	}

	@Override
	public Object instantiateItem(ViewGroup collection, final int position) {
		L.d(TAG, "instantiateItem");
		// L.d("size", "position" + position + "max" + mMaxSize);
		if (mFromPhotoGrid) {
			upData(position);
			if (mIsGetDetail && position > mBeans.size() - 1) {
				bean = getDetails();
			} else {
				try {
					ResourceBean tempBean = (ResourceBean) getItem(position);
					if (tempBean.getIsDeleteResource() != 2) {
						bean = tempBean.getObjectInfo().get(0);
					} else {
						bean = getDetails();
					}
				} catch (Exception e) {
					e.printStackTrace();
					bean = getDetails();
				}

			}
		} else {
			bean = (ObjectInfoBean) getItem(position);
		}
		String url = bean.getFullImgPath();
		UrlTouchImageView iv = null;
		if (loadCache.containsKey(url)) {
			iv = (UrlTouchImageView) loadCache.get(url);
			loadCache.remove(url);
			collection.addView(iv, 0);
			return iv;
		}
		iv = new UrlTouchImageView(mContext, mHandler);
		iv.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		iv.setUrl(url, mIsOriginal, loadCache);
		// if (!mFromPhotoGrid) {
		if (null == bean.getResourceBean()) {
			bean.setResourceBean(getDetailResoureceBean());
			final String id = String.valueOf(bean.getObjectId());
			String id_Group = String.valueOf(bean.getGroupId());
			L.d(TAG, "instantiateItem id=" + id + ",id_Group=" + id_Group);
			new AsyncBeanDownLoad(new String[] { id, id_Group, mType },
					new String[] { String.valueOf(position) },
					bean.getObjSourceType(), mContext, new BeanCallback() {

						@Override
						public void load(Object object, Object[] tags) {
							ResourceBean bean = (ResourceBean) object;
							if (bean.getObjectId() == 0) {
								try {
									bean.setObjectId(Integer.valueOf(id));
								} catch (Exception e) {
									e.printStackTrace();
								}
								bean.setObjectType(mType);
							}
							try {
								if (!mFromPhotoGrid) {
									((ObjectInfoBean) getItem(Integer
											.parseInt((String) tags[0])))
											.setResourceBean(bean);
								} else {
									((ObjectInfoBean) ((ResourceBean) getItem(Integer
											.parseInt((String) tags[0])))
											.getObjectInfo().get(0))
											.setResourceBean(bean);
								}

								if (mIndex == position) {
									L.d(TAG, "mIndex==position" + mIndex + " "
											+ position);
									Message message = mHandler.obtainMessage(
											3,
											getItem(Integer
													.parseInt((String) tags[0])));
									mHandler.sendMessage(message);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).downLoadBean();
		}
		// }
		// iv.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// L.d(TAG, "onclick");
		// mHandler.sendEmptyMessage(2);
		// }
		// });
		collection.addView(iv, 0);
		return iv;
	}

	@Override
	public int getCount() {
		L.d(TAG, "getCount:" + mIsGetDetail + mBeans.size());
		if (mIsGetDetail) {
			return mMaxSize;
		} else {
			return mBeans.size();
		}
	}

	public void addTransitionBeans(final List<Object> beans_Temp,
			final int id_Handler) {
		if (beans_Temp.size() == PageSizeUtil.SIZEPAGE_RESOURCELIST) {
			new Thread() {
				public void run() {
					try {
						mBeans.addAll(beans_Temp);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						mHandler.sendEmptyMessage(id_Handler);
					}
				}
			}.start();
		} else {
			mBeans.addAll(beans_Temp);
			mHandler.sendEmptyMessage(id_Handler);
		}
	}

	private void upData(int position) {
		if (mSpace == (mBeans.size() - position) && mBeans.size() < mMaxSize) {
			if (mCanLoad) {
				mCanLoad = false;
				mHandler.sendEmptyMessage(HANDLER_SIX);
			}
		}
	}

	public void setCanLoad(boolean canLoad) {
		mCanLoad = canLoad;
	}

	public void remove(int position) {
		if (position > 0 && position < mBeans.size()) {
			L.d(TAG, "remove");
			mBeans.remove(position);
		}
	}

	private ObjectInfoBean getDetails() {
		ObjectInfoBean bean = new ObjectInfoBean();
		bean.setObjectPath("");
		bean.setObjectUrl("");
		return bean;
	}

	private ResourceBean getDetailResoureceBean() {
		ResourceBean resourceBean = new ResourceBean();
		resourceBean.setObjectId(1);
		return resourceBean;
	}

	public void removeResourece(int index) {
		L.d(TAG, "index" + index);
		mBeans.remove(index);
	}

	public boolean isOriginal() {
		return mIsOriginal;
	}

	public void setIsOriginal(boolean isOriginal) {
		mIsOriginal = isOriginal;
	}

}
