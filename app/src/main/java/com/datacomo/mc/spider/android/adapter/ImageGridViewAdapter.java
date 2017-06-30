package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageGridViewAdapter extends BaseAdapter {
	private static final String LOG_TAG = "ImageGridViewAdapter";
	private Context mContext;
	private final RelativeLayout.LayoutParams ONLY = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.WRAP_CONTENT,
			RelativeLayout.LayoutParams.WRAP_CONTENT);
	private RelativeLayout.LayoutParams MORE;
	private List<ObjectInfoBean> mBeans;
	private int mMaxWH;
	private int mMinWH;
	private final int mMaxScale = 5;
	private final int mMaxZoomScale = 3;
	private int mStart;
	private int mSize;
	private boolean mHasAuthority;
	private String mIsResPond;
	private String mJoinGroupStatus;
	private ResourceBean mResourceBean;
	private LayoutInflater inflater;

	public ImageGridViewAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		try {
			Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
					TaskUtil.IMGDEFAULTLOADSTATEIMG[0]);
			L.d(LOG_TAG, "width:" + bm.getWidth());
			mMaxWH = bm.getWidth();
			mMinWH = mMaxWH / 6;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setItemWh(int itemWh) {
		MORE = new RelativeLayout.LayoutParams(itemWh, itemWh);
	}

	public void setBeans(List<ObjectInfoBean> beans) {
		mBeans = beans;
	}

	public List<ObjectInfoBean> getBeans() {
		return mBeans;
	}

	public int getStart() {
		return mStart;
	}

	public void setStart(int start) {
		mStart = start;
	}

	public int getSize() {
		return mSize;
	}

	public void setSize(int size) {
		mSize = size;
	}

	public boolean isHasAuthority() {
		return mHasAuthority;
	}

	public void setHasAuthority(boolean hasAuthority) {
		mHasAuthority = hasAuthority;
	}

	public String getIsResPond() {
		return mIsResPond;
	}

	public void setIsResPond(String isResPond) {
		mIsResPond = isResPond;
	}

	public String getJoinGroupStatus() {
		return mJoinGroupStatus;
	}

	public void setJoinGroupStatus(String joinGroupStatus) {
		mJoinGroupStatus = joinGroupStatus;
	}

	public ResourceBean getResourceBean() {
		return mResourceBean;
	}

	public void setResourceBean(ResourceBean resourceBean, boolean hasPraise,
			boolean hasAuthority) {
		mResourceBean = resourceBean;
		mResourceBean.setHasPraise(hasPraise);
		mResourceBean.setHasAuthority(hasAuthority);
	}

	public void setResourceBean(ResourceBean resourceBean, boolean hasCollect) {
		mResourceBean = resourceBean;
		mResourceBean.setHasCollect(hasCollect);
	}

	@Override
	public int getCount() {
		// L.d(LOG_TAG, "mSize:" + mSize);
		return mSize;
	}

	@Override
	public Object getItem(int position) {
		return mBeans.get(mStart + position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = (RelativeLayout) inflater.inflate(
					R.layout.item_gird_imgbox, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_grid_imgbox_img);
			holder.img.setMaxHeight(mMaxWH);
			holder.img.setMaxWidth(mMaxWH);
			holder.brand = (ImageView) convertView
					.findViewById(R.id.item_grid_imgbox_brand2);
			holder.brand.setImageResource(R.drawable.gif);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int count = getCount();
		if (count == 1) {
			holder.img.setScaleType(ScaleType.CENTER_INSIDE);
			holder.img.setLayoutParams(ONLY);
			holder.img.setAdjustViewBounds(true);
		} else {
			holder.img.setScaleType(ScaleType.CENTER_CROP);
			holder.img.setLayoutParams(MORE);
			holder.img.setAdjustViewBounds(false);
		}
		// L.d(LOG_TAG, "fSize:" + mBeans.size() + " mSize:" + mSize + " start:"
		// + mStart + " position:" + position);
		ObjectInfoBean bean = (ObjectInfoBean) getItem(position);
		// holder.img.setTag(new String[] { String.valueOf(position),
		// String.valueOf(bean.getGroupId()) });
		String url = bean.getObjectUrl() + bean.getObjectPath();
		if (url != null && url.toLowerCase().endsWith(".gif")) {
			holder.brand.setVisibility(View.VISIBLE);
		} else {
			holder.brand.setVisibility(View.GONE);
		}

		url = ThumbnailImageUrl.getThumbnailImageUrl(url,
				ImageSizeEnum.THREE_HUNDRED);
		L.i(LOG_TAG, "img_url=" + url);

		if (count == 1) {
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					L.d(LOG_TAG, "oneitemclick");
					Bundle bundle = new Bundle();
					List<ObjectInfoBean> beans = getBeans();
					ObjectInfoBean bean = beans.get(getStart());
					bundle.putInt("index", 0);
					bundle.putInt("groupId", bean.getGroupId());
					L.d(LOG_TAG, "groupId:" + bean.getGroupId());
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("beans", beans);
					bundle.putSerializable("map", hashMap);
					bundle.putString("type", "type_default");
					if (isHasAuthority())
						bundle.putInt("authority", 1);
					else
						bundle.putInt("authority", 0);
					hashMap = null;
					LogicUtil.enter(mContext, PhotoGalleryActivity.class,
							bundle, false);
				}
			});
			MyFinalBitmap.setImage(mContext, holder.img, url, 0,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// 图片原始宽高
							int iWidth = loadedImage.getWidth();
							int iHeight = loadedImage.getHeight();
							L.d(LOG_TAG, "mMaxWH:" + mMaxWH);
							L.d(LOG_TAG, "iWidth:" + iWidth + " iHeight:"
									+ iHeight);
							if (iHeight > iWidth) {
								double hwScale = (double) iHeight
										/ (double) iWidth; // 图片高宽缩放比
								// 组件宽
								int vWidth = (int) (mMaxWH / hwScale);
								L.d(LOG_TAG, "hwScale:" + hwScale + " vWidth:"
										+ vWidth);
								double mwScale = (double) mMaxWH
										/ (double) iHeight; // 原图与最大图片缩放比
								L.d(LOG_TAG, "mwScale:" + mwScale);
								if (hwScale > mMaxScale) {
									if (iWidth > mMaxWH)
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												mMaxWH, mMaxWH));
									else if (iWidth < mMinWH)
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												mMinWH, mMaxWH));
									else
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												iWidth, mMaxWH));
									((ImageView) view)
											.setScaleType(ScaleType.CENTER_CROP);
								} else if (mwScale > mMaxZoomScale) {
									int vHeight = mMaxZoomScale * iHeight;
									vWidth = (int) (vHeight / hwScale);
									L.d(LOG_TAG, "vWidth:" + vWidth
											+ " vHeight:" + vHeight);
									((ImageView) view)
											.setScaleType(ScaleType.FIT_CENTER);
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											vWidth, vHeight));
								} else {
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											vWidth, mMaxWH));
								}
							} else if (iWidth > iHeight) {
								double whScale = (double) iWidth
										/ (double) iHeight;// 图片宽高缩放比
								// 组件高
								int vHeight = (int) (mMaxWH / whScale);
								L.d(LOG_TAG, "whScale:" + whScale + " vHeight:"
										+ vHeight);
								double mwScale = (double) mMaxWH
										/ (double) iWidth; // 原图与最大图片缩放比
								if (whScale > mMaxScale) {
									if (iHeight > mMaxWH)
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												mMaxWH, mMaxWH));
									else if (iHeight < mMinWH)
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												mMinWH, mMaxWH));
									else
										view.setLayoutParams(new RelativeLayout.LayoutParams(
												mMaxWH, iHeight));
									((ImageView) view)
											.setScaleType(ScaleType.CENTER_CROP);
								} else if (mwScale > mMaxZoomScale) {
									int vWidth = mMaxZoomScale * iWidth;
									vHeight = (int) (vWidth / whScale);
									L.d(LOG_TAG, "vWidth:" + vWidth
											+ " vHeight:" + vHeight);
									((ImageView) view)
											.setScaleType(ScaleType.FIT_CENTER);
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											vWidth, vHeight));
								} else {
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											mMaxWH, vHeight));
								}
							} else {
								double whScale = (double) iWidth
										/ (double) iHeight;
								// 组件高
								int vHeight = (int) (mMaxWH / whScale);
								L.d(LOG_TAG, "whScale:" + whScale + " vHeight:"
										+ vHeight);
								double mwScale = (double) mMaxWH
										/ (double) iWidth;
								L.d(LOG_TAG, "mwScale:" + mwScale);
								if (mwScale > mMaxZoomScale) {
									L.d(LOG_TAG, "vWidth:" + mMaxZoomScale
											* iWidth + " vHeight:"
											+ mMaxZoomScale * iHeight);
									((ImageView) view)
											.setScaleType(ScaleType.FIT_CENTER);
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											mMaxZoomScale * iWidth,
											mMaxZoomScale * iHeight));
								} else {
									view.setLayoutParams(new RelativeLayout.LayoutParams(
											mMaxWH, mMaxWH));
								}
							}

						}
					});
		} else {
			MyFinalBitmap.setImage(mContext, holder.img, url);
		}
		return convertView;
	}

	public void flush(List<ObjectInfoBean> beans) {
		mBeans.clear();
		mBeans.addAll(beans);
		notifyDataSetChanged();
	}

	class ViewHolder {
		ImageView img, brand;
	}

}
