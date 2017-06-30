package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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

public class ImageChoosedGridViewAdapter extends BaseAdapter {
	private static final String TAG_LOG = "ImageChoosedGridViewAdapter";
	private Context mContext;
	private final RelativeLayout.LayoutParams MORE;
	private LayoutInflater inflater;
	private static final int mNum_CanChoosed = 9;
	private static List<String> mChooseds;

	public ImageChoosedGridViewAdapter(Context context, int itemWidth) {
		MORE = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
		mContext = context;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setUrls(List<String> beans) {
		mChooseds = beans;
	}

	public List<String> getUrls() {
		return mChooseds;
	}

	@Override
	public int getCount() {
		// L.d(LOG_TAG, "mSize:" + mSize);
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
		String item = (String) getItem(position);
		L.d(TAG_LOG, "item:" + item);
		holder.img.setTag(item);
		loadImg(item,holder.img);
		return convertView;
	}

	public void choose(String item) {
		if (null == item)
			return;
		mChooseds.add(0, item);
		notifyDataSetChanged();
	}

	public void unChoose(String item) {
		if (null == item)
			return;
//		LocalImageDownLoadTask.removeCache(item);
		mChooseds.remove(item);
		notifyDataSetChanged();
	}
	
	private void loadImg(String uri, ImageView iv) {
			MyFinalBitmap.setLocalAndDisPlayImage(mContext, iv, uri);
	}

	class ViewHolder {
		ImageView img;
	}

	public static Boolean isChoosed(String item) {
		L.d(TAG_LOG, item);
		if (null != mChooseds)
			return mChooseds.contains(item);
		return false;
	}

	public static void initChooseds(List<String> checkeds) {
		mChooseds = new ArrayList<String>();
		if (null != checkeds && checkeds.size() > 0)
			mChooseds.addAll(checkeds);
	}

	public static void cleanChooseds() {
		if(null!=mChooseds){
			mChooseds.clear();
			mChooseds = null;
		}
	}

	public static boolean CanChoosed() {
		L.d(TAG_LOG,
				"CanChoosed:" + " mChooseds.size():" + mChooseds.size()
						+ " mNum_CanChoosed" + mNum_CanChoosed + " "
						+ (mChooseds.size() <= mNum_CanChoosed));
		return mChooseds.size() < mNum_CanChoosed;
	}

	public static List<String> getChoosed() {
		return mChooseds;
	}

	public static int getCanChoosedNum() {
		return mNum_CanChoosed;
	}
	
}
