package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;

public class ImageBucketAdapter extends BaseAdapter {
	// TAG
	private static final String TAG_LOG = "ImageBuchketAdapter";
	// variable
	private LinkedHashMap<String, List<String>> mDataMap;
	private Context mContext;
	private LayoutInflater inflater;
	private String[] mBuckets;

	// import class
	// view
	public ImageBucketAdapter(Context context,
			LinkedHashMap<String, List<String>> dataMap) {
		mContext = context;
		mDataMap = dataMap;
		Set<String> keyset = mDataMap.keySet();
		mBuckets = keyset.toArray(new String[mDataMap.size()]);
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public List<String> getData(String key) {
		return mDataMap.get(key);
	}

	@Override
	public int getCount() {
		// L.d(TAG_LOG, "getCount:" + mDataMap.size());
		L.d(TAG_LOG, "getCount:" + mBuckets.length);
		return mBuckets.length;
	}

	@Override
	public Object getItem(int position) {
		// L.d(TAG_LOG,"getItem"+ mTerator.next());
		return mBuckets[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// L.d(TAG_LOG, "getView");
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_imgbuckut, null);
			holder.img_First = (ImageView) convertView
					.findViewById(R.id.item_imgbucket_img_first);
			holder.txt_Buchket = (TextView) convertView
					.findViewById(R.id.item_imgbucket_txt_buchket);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String bucket = (String) getItem(position);
		List<String> data = mDataMap.get(bucket);
		holder.txt_Buchket.setTag(bucket);
		bucket = bucket.substring(bucket.lastIndexOf("/") + 1);
		holder.txt_Buchket.setText(bucket + "(" + data.size() + ")");
		L.d(TAG_LOG, "data.size():" + data.size());
		String item = data.get(0);
		L.d(TAG_LOG, "item:" + item);
		data = null;
		holder.img_First.setTag(item);
		loadImg(item, holder.img_First);
		return convertView;
	}

	public void flush(HashMap<String, List<String>> dataMap) {
		try {
			mDataMap.clear();
			mDataMap.putAll(dataMap);
			L.d(TAG_LOG, "mDataMap:" + mDataMap.size());
			Set<String> keyset = mDataMap.keySet();
			mBuckets = keyset.toArray(new String[mDataMap.size()]);
			// keyset = null;
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		// L.d(TAG_LOG, "clear");
		mDataMap.clear();
		mDataMap = null;
		mBuckets = null;
	}

	private void loadImg(String uri, ImageView iv) {
		MyFinalBitmap.setLocalAndDisPlayImage(mContext, iv, uri);
	}

	class ViewHolder {
		ImageView img_First;
		TextView txt_Buchket;
	}

}
