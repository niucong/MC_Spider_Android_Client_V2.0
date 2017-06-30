package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CloudFileWithActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.ShareLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class CloudFileMberAdapter extends BaseAdapter {
	private Context c;
	private ArrayList<ShareLeaguerBean> mbers;

	// private ListView list;

	public CloudFileMberAdapter(Context context,
			ArrayList<ShareLeaguerBean> data, ListView listView) {
		c = context;
		mbers = data;
		// list = listView;
	}

	@Override
	public int getCount() {
		return mbers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mbers.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(c).inflate(
					R.layout.item_cloudfile_mber, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.fileNum = (TextView) convertView.findViewById(R.id.file_num);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.details = (LinearLayout) convertView
					.findViewById(R.id.ll_details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ShareLeaguerBean bean = mbers.get(position);
		int _fileNum = bean.getTotalFileNum();
		holder.fileNum.setText(_fileNum + "");
		final String _name = bean.getRelationMemberName();
		holder.name.setText(_name);
		int _num = bean.getNewShareNum();
		if (_num > 0) {
			holder.num.setVisibility(View.VISIBLE);
			holder.num.setText(_num + "");
		} else {
			holder.num.setVisibility(View.GONE);
		}
		String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
				bean.getRelationMemberHeadUrl()
						+ bean.getRelationMemberHeadPath(),
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		holder.head.setTag(position + headUrl);
		MyFinalBitmap.setHeader(c, holder.head, headUrl);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("id", bean.getRelationMemberId() + "");
				b.putString("name", _name);
				LogicUtil.enter(c, CloudFileWithActivity.class, b, false);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView name, fileNum, num;
		ImageView head;
		LinearLayout details;
	}
}
