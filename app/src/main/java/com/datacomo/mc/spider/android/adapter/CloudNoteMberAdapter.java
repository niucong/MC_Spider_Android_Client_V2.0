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
import android.widget.TextView;

import com.datacomo.mc.spider.android.CloudNoteWithActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.note.MapShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class CloudNoteMberAdapter extends BaseAdapter {
	private final String TAG = "CloudNoteMberAdapter";

	private Context c;
	private ArrayList<MapShareLeaguerInfoBean> mapLeaguers;
	private boolean isSearch;

	public CloudNoteMberAdapter(Context context,
			ArrayList<MapShareLeaguerInfoBean> data) {
		c = context;
		mapLeaguers = data;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	@Override
	public int getCount() {
		return mapLeaguers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mapLeaguers.get(arg0);
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
			convertView.findViewById(R.id.div).setVisibility(View.VISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MapShareLeaguerInfoBean bean = mapLeaguers.get(position);
		int _fileNum = bean.getTOTLE_NUM();
		holder.fileNum.setText(_fileNum + "");
		int _num = bean.getUN_READ_NUM();
		if (_num > 0) {
			holder.num.setVisibility(View.VISIBLE);
			holder.num.setText(_num + "");
		} else {
			holder.num.setVisibility(View.GONE);
		}

		final ShareLeaguerInfoBean leager = bean.getSHARE_LEAGUER_INFO_BEAN();
		if (leager != null) {
			String mName = leager.getShareMemberName();
			String fName = leager.getShareAliases();
			L.d(TAG, "getView mName=" + mName + ",fName=" + fName
					+ ",isSearch=" + isSearch);
			if (fName != null && !"".equals(fName)) {
				if (!fName.equals(mName)) {
					if (isSearch) {
						holder.name.setText(fName + "（" + mName + "）");
					} else {
						holder.name.setText(fName);
					}
				} else {
					holder.name.setText(fName);
				}
				mName = fName;
			} else {
				holder.name.setText(mName);
			}
			final String _name = mName;

			String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
					leager.getShareMemberHeadUrl()
							+ leager.getShareMemberHeadPath(),
					HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			holder.head.setTag(position + headUrl);
			MyFinalBitmap.setHeader(c, holder.head, headUrl);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("id", leager.getShareMemberId() + "");
					b.putString("name", _name);
					LogicUtil.enter(c, CloudNoteWithActivity.class, b, false);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		TextView name, fileNum, num;
		ImageView head;
		LinearLayout details;
	}
}
