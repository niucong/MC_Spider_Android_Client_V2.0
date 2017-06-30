package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.T;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageSingleGridViewAdapter extends BaseAdapter {
	private static final String TAG_LOG = "ImageSingleGridViewAdapter";
	private final RelativeLayout.LayoutParams MORE;
	private boolean mIsInit = true;
	private LayoutInflater inflater;
	private List<String> mChooseds;
	private int mItemWidth;

	public ImageSingleGridViewAdapter(List<String> chooseds,int itemWidth) {
		MORE = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
		mItemWidth = itemWidth;
		mChooseds=chooseds;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		L.d(TAG_LOG, "mSize:" + mChooseds.size());
		if (mChooseds.size() > 0 && mChooseds.size() < 9)
			return mChooseds.size() + 1;
		else
			return mChooseds.size();
	}

	@Override
	public Object getItem(int position) {
		return mChooseds.get(position);
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
			holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.img.setLayoutParams(MORE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		L.d(TAG_LOG,
				"position:" + position + " mChooseds.size():"
						+ mChooseds.size());
		if (position > mChooseds.size() - 1) {
			holder.img.setTag("");
			loadNull(holder.img);
		} else {
			String item = (String) getItem(position);
			L.d(TAG_LOG, "item:" + item);
			holder.img.setTag(item);
			holder.img.setPadding(0, 0, 0, 0);
			loadImg(item, holder.img);
		}
		return convertView;
	}
	
	public void setUrls(List<String> beans) {
		mChooseds = beans;
	}

	public List<String> getUrls() {
		return mChooseds;
	}

	public boolean isInit() {
		if (mIsInit) {
			mIsInit = false;
			return true;
		}
		return mIsInit;
	}

	public void add(String choosed) {
		if (mChooseds.contains(choosed))
			T.show(App.app, "该图片已添加");
		mChooseds.add(0, choosed);
		notifyDataSetChanged();
	}

	public void flush(List<String> chooseds) {
		mChooseds.clear();
		mChooseds.addAll(chooseds);
		notifyDataSetChanged();
	}

	public void remove(String choosed) {
		mChooseds.remove(choosed);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		mChooseds.remove(position);
		notifyDataSetChanged();
	}

	private void loadImg(String uri, ImageView iv) {
		MyFinalBitmap.setLocalAndDisPlayImage(null, iv, uri);
	}

	private void loadNull(ImageView iv) {
		MyFinalBitmap.setLoaclImage(iv, "", new SimpleImageLoadingListener() {

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				int x = (int) (mItemWidth * 0.3);
				L.d(TAG_LOG, "x:" + x);
				view.setPadding(x, x, x, x);
				((ImageView) view).setImageResource(R.drawable.btn_add);
			}

		});
	}

	class ViewHolder {
		ImageView img;
	}

}
