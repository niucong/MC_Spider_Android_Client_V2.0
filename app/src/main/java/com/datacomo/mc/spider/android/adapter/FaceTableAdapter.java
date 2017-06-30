package com.datacomo.mc.spider.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.GreetUtil;

public class FaceTableAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ViewHolder mHolder;

	/**
	 * 
	 * @param context
	 * @param res
	 */
	public FaceTableAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return GreetUtil.GREET_TITLE.length;
	}

	@Override
	public Object getItem(int position) {
		return GreetUtil.GREET_TITLE.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_face, null);
			mHolder = new ViewHolder();
			mHolder.iv = (ImageView) convertView.findViewById(R.id.face_img);
			mHolder.tv = (TextView) convertView.findViewById(R.id.face_name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.iv.setImageResource(GreetUtil.GREET_RES_IDS[position]);

		// mHolder.iv.loadDataWithBaseURL(null,
		// GreetUtil.motionToAssetsHtml(position), "text/html", "utf-8",
		// null);
		// mHolder.iv.setFocusable(false);

		mHolder.tv.setText(GreetUtil.GREET_TITLE[position]);
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}

}
