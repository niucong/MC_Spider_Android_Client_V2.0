package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GroupSingleGridViewAdapter extends BaseAdapter {
	private static final String TAG_LOG = "GroupSingleGridViewAdapter";
	public static final String STR_Add = "添加圈子";
	private final RelativeLayout.LayoutParams ICON;
	private final RelativeLayout.LayoutParams TEXT;
	private final AbsListView.LayoutParams PARENT;
	private boolean mIsInit = true;
	private LayoutInflater inflater;
	private List<GroupEntity> mChooseds;
	private int mItemWidth;

	public GroupSingleGridViewAdapter(List<GroupEntity> beans, int itemWidth) {
		ICON = new RelativeLayout.LayoutParams((int) (itemWidth * 0.8),
				(int) (itemWidth * 0.8));
		ICON.addRule(RelativeLayout.CENTER_HORIZONTAL);
		TEXT = new RelativeLayout.LayoutParams((int) (itemWidth * 0.8),
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		TEXT.addRule(RelativeLayout.CENTER_HORIZONTAL);
		TEXT.addRule(RelativeLayout.BELOW, R.id.item_grid_groupbox_icon);
		PARENT = new AbsListView.LayoutParams(itemWidth, itemWidth);
		mChooseds = beans;
		mItemWidth = itemWidth;
		L.d(TAG_LOG, "pading:" + (int) (mItemWidth * 0.08));
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
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
			convertView = (LinearLayout) inflater.inflate(
					R.layout.item_gird_groupbox, null);
			convertView.setLayoutParams(PARENT);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_grid_groupbox_icon);
			holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.img.setLayoutParams(ICON);
			holder.tv = (TextView) convertView
					.findViewById(R.id.item_grid_groupbox_name);
			holder.tv.setLayoutParams(TEXT);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		L.d(TAG_LOG,
				"position:" + position + " mChooseds.size():"
						+ mChooseds.size());
		GroupEntity bean = (GroupEntity) getItem(position);
		String url = bean.getFullHeadPath();
		String name = bean.getName();
		if (STR_Add.equals(name)) {
			loadImg("", holder.img);
		} else {
			url = ThumbnailImageUrl.getThumbnailPostUrl(url,
					PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
			loadImg(url, holder.img);
		}
		L.d(TAG_LOG, "url:" + url);
		holder.tv.setText(name);
		return convertView;
	}

	public List<GroupEntity> getBean() {
		return mChooseds;
	}

	public boolean isInit() {
		if (mIsInit) {
			mIsInit = false;
			return true;
		}
		return mIsInit;
	}

	public void flush(List<GroupEntity> chooseds) {
		mChooseds.clear();
		mChooseds.addAll(chooseds);
		notifyDataSetChanged();
	}

	private void loadImg(String url, ImageView iv) {
		MyFinalBitmap.setPosterCorner(App.app, iv, url, mItemWidth,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						if ("".equals(imageUri))
							((ImageView) view)
									.setImageResource(R.drawable.btn_group_add);
						int pading = (int) (mItemWidth * 0.08);
						view.setPadding(pading, pading, pading, pading);
					}

				});
	}

	class ViewHolder {
		ImageView img;
		TextView tv;
	}

}
