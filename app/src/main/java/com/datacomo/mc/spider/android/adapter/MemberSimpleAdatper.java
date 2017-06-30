package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.groupchat.MemberSimpleBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class MemberSimpleAdatper extends BaseAdapter {
	private List<Object> mBeans;
	private List<String> mCheckedIds;
	private Context mContext;
	private ViewHolder mHolder;
	private MemberSimpleBean mBean;

	private LayoutInflater inflater;

	public MemberSimpleAdatper(Context context, List<Object> beans) {
		mContext = context;
		mBeans = beans;
		mCheckedIds = new ArrayList<String>();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MemberSimpleAdatper(Context context, List<Object> beans,
			List<String> checkedIds) {
		mContext = context;
		mBeans = beans;
		mCheckedIds = new ArrayList<String>();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setCheckIds(List<String> checkedIds) {
		mCheckedIds = checkedIds;
	}

	public List<String> getCheckIds() {
		if (null == mCheckedIds) {
			mCheckedIds = new ArrayList<String>();
		}
		return mCheckedIds;
	}

	@Override
	public int getCount() {
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

	public String[] getChosenIds() {
		return mCheckedIds.toArray(new String[0]);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_inviteleaguer, null);
			mHolder.name = (TextView) convertView
					.findViewById(R.id.item_inviteleaguer_txt_name);
			mHolder.mood = (TextView) convertView
					.findViewById(R.id.item_inviteleaguer_txt_info);
			mHolder.head_img = (ImageView) convertView
					.findViewById(R.id.head_img);
			mHolder.choose = (ImageView) convertView
					.findViewById(R.id.item_inviteleaguer_img_check);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mBean = (MemberSimpleBean) getItem(position);
		String id = String.valueOf(mBean.getMemberId());
		final String name = mBean.getMemberName();
		if (mCheckedIds.contains(id))
			mHolder.choose.setImageResource(R.drawable.icon_checked);
		else
			mHolder.choose.setImageResource(R.drawable.icon_unchecked);
		String headUrl = mBean.getHeadFullPath();
		headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(headUrl,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		int memberId = mBean.getMemberId();
		mHolder.head_img.setTag(memberId);
		// Drawable drawable = new AsyncImageDownLoad(headUrl,
		// new String[] { headUrl }, TaskUtil.HEADDEFAULTLOADSTATEIMG,
		// ConstantUtil.HEAD_PATH, mContext,"membersimpleadapter", new
		// ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// ImageView img = (ImageView) mListView
		// .findViewWithTag(tags[0]);
		// Drawable imageDrawable = (Drawable) object;
		// if (null != img) {
		// if (null != imageDrawable) {
		// img.setImageDrawable(ImageDealUtil
		// .getPosterCorner(imageDrawable, 4));
		// } else {
		// imageDrawable = mContext
		// .getResources()
		// .getDrawable(
		// TaskUtil.HEADDEFAULTLOADSTATEIMG[1]);
		// img.setImageDrawable(ImageDealUtil
		// .getPosterCorner(imageDrawable, 4));
		// }
		// }
		// }
		// }).getDrawable();
		// mHolder.head_img.setImageDrawable(ImageDealUtil.getPosterCorner(
		// drawable, 4));
		// TODO
		MyFinalBitmap.setHeader(mContext, mHolder.head_img, headUrl);

		mHolder.head_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", v.getTag().toString());
				b.putString("name", name);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});
		mHolder.name.setText(mBean.getMemberName());
		mHolder.mood.setText(mBean.getMemberMood());
		return convertView;
	}

	public void chooseChage(MemberSimpleBean bean, View convertView) {
		String id = String.valueOf(bean.getMemberId());
		if (mCheckedIds.contains(id)) {
			mCheckedIds.remove(id);
			((ImageView) convertView
					.findViewById(R.id.item_inviteleaguer_img_check))
					.setImageResource(R.drawable.icon_unchecked);
		} else {
			mCheckedIds.add(id);
			((ImageView) convertView
					.findViewById(R.id.item_inviteleaguer_img_check))
					.setImageResource(R.drawable.icon_checked);

		}
	}

	class ViewHolder {
		ImageView head_img, choose;
		TextView name, mood;
	}
}
