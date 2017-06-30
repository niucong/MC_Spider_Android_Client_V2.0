package com.datacomo.mc.spider.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.FaceUtil;

public class FaceAdapter extends BaseAdapter {

	private int[] res;
	private LayoutInflater inflater;
	private ViewHolder mHolder;
	private int resSize = LayoutParams.MATCH_PARENT;
	/**
	 * 
	 * @param context
	 * @param res
	 */
	public FaceAdapter(Context context, int[] res) {
		this.res = res;
		inflater = LayoutInflater.from(context);
	}
	
	public FaceAdapter(Context context, int[] res, int size) {
		this.res = res;
		inflater = LayoutInflater.from(context);
		resSize = size;
	}

	@Override
	public int getCount() {
		return res.length;
	}

	@Override
	public Object getItem(int position) {
		return res[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.face_item, null);
			mHolder = new ViewHolder();
			mHolder.iv = (ImageView) convertView;
			mHolder.iv.setLayoutParams(new GridView.LayoutParams(resSize,
					resSize));
			mHolder.iv.setAdjustViewBounds(false);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.iv.setImageResource(res[position]);
		if(FaceUtil.RES_NULL != res[position]){
			mHolder.iv.setBackgroundResource(R.drawable.bg_item_black);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
	}

}
