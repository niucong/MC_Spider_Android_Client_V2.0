package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.DiaryInfoBean;
import com.datacomo.mc.spider.android.util.DateTimeUtil;

public class NoteAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<DiaryInfoBean> datas;
	private int itemWidth;

	public NoteAdapter(Context context, ArrayList<DiaryInfoBean> info,
			int screenWidth) {
		mContext = context;
		this.datas = info;
		itemWidth = screenWidth / 3 / 8 * 7;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			LinearLayout ll = (LinearLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.item_note, null);
			ll.setLayoutParams(new GridView.LayoutParams(itemWidth, itemWidth));
			convertView = ll;
			holder.bg = (LinearLayout) convertView.findViewById(R.id.bg);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.auther = (TextView) convertView.findViewById(R.id.from);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final DiaryInfoBean bean = datas.get(position);
		String title = bean.getTitle();
		int id = bean.getDiaryId();
		String auther = bean.getShareMemberName();
		if (!"".equals(auther) && null != auther) {
			holder.auther.setText("Fr: " + auther);
			holder.auther.setVisibility(View.VISIBLE);
		} else {
			holder.auther.setVisibility(View.GONE);
		}
		holder.date.setText(DateTimeUtil.aTimeFormat(DateTimeUtil
				.getLongTime(bean.getEditTime())));
		holder.title.setText(title);
		try {
			holder.bg.setBackgroundResource(getBgRes(id % 4));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private int getBgRes(int key) {
		switch (key) {
		case 0:
			return R.drawable.bg_note_green;
		case 1:
			return R.drawable.bg_note_blue;
		case 2:
			return R.drawable.bg_note_yellow;
		default:
			return R.drawable.bg_note_red;
		}
	}

	class ViewHolder {
		LinearLayout bg;
		TextView title, date, auther;
	}
}
