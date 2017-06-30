package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;
import com.datacomo.mc.spider.android.view.ImageGridView;

public class ImageChooseGridViewAdapter extends BaseAdapter {
	private static final String TAG_LOG = "ImageChooseGridViewAdapter";
	private Context mContext;
	private final RelativeLayout.LayoutParams MORE;
	// private final RelativeLayout.LayoutParams BRAND;
	private final int mNum_Init = 8;
	private final String BRANDTAG = "brand";
	private List<String> mBeans;
	private boolean mInit;
	private LayoutInflater inflater;

	public ImageChooseGridViewAdapter(Context context, int itemWidth) {
		MORE = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
		mContext = context;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setBeans(List<String> beans) {
		mBeans = beans;
	}

	public List<String> getBeans() {
		return mBeans;
	}

	public void isInit() {
		mInit = true;
	}

	public void UnInit() {
		mInit = false;
	}

	@Override
	public int getCount() {
		// L.d(LOG_TAG, "mSize:" + mSize);
		if (mInit)
			return mNum_Init;
		return mBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return mBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = (RelativeLayout) inflater.inflate(
					R.layout.item_gird_imgbox, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_grid_imgbox_img);
			holder.img.setLayoutParams(MORE);
			holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.brand = (ImageView) convertView
					.findViewById(R.id.item_grid_imgbox_brand);
			holder.brand.setVisibility(View.VISIBLE);
			// holder.brand.setLayoutParams(BRAND);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mInit) {
			holder.img.setImageBitmap(ImageDealUtil.drawableToBitmap(mContext
					.getResources().getDrawable(
							TaskUtil.IMGDEFAULTLOADSTATEIMG[0])));
			return convertView;
		}
		String item = (String) getItem(position);
		L.d(TAG_LOG, "item:" + item);
		holder.img.setTag(item);
		holder.brand.setTag(BRANDTAG + item);
		if (ImageChoosedGridViewAdapter.isChoosed(item))
			holder.brand.setImageResource(R.drawable.icon_brand_setting);
		else
			holder.brand.setImageResource(R.drawable.nothing);
		loadImg(item, holder.img);
		return convertView;
	}

	public void flush(List<String> beans) {
		mBeans.clear();
		mBeans.addAll(beans);
		notifyDataSetChanged();
	}

	public void unchoose(GridView gv, String url) {
		ImageView brand = (ImageView) gv.findViewWithTag(BRANDTAG + url);
		if (null != brand)
			brand.setImageResource(R.drawable.nothing);
	}

	public void unchoose(ImageGridView gv, List<String> urls) {
		ImageView brand = null;
		if (mBeans != null)
			if (null == urls || urls.size() == 0) {
				for (String url : mBeans) {
					brand = (ImageView) gv.findViewWithTag(BRANDTAG + url);
					if (null != brand) {
						brand.setImageResource(R.drawable.nothing);
					}
				}
			} else {
				for (String url : mBeans) {
					if (!urls.contains(url)) {
						brand = (ImageView) gv.findViewWithTag(BRANDTAG + url);
						if (null != brand) {
							brand.setImageResource(R.drawable.nothing);
						}
					} else if (urls.contains(url)) {
						brand = (ImageView) gv.findViewWithTag(BRANDTAG + url);
						if (null != brand) {
							brand.setImageResource(R.drawable.icon_brand_setting);
						}
					}
				}
			}
	}

	private void loadImg(String uri, ImageView iv) {
		MyFinalBitmap.setLocalAndDisPlayImage(mContext, iv, uri);
	}

	class ViewHolder {
		ImageView img, brand;
	}

}
