package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.From;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class RemoteAdapter extends BaseAdapter {
	private static final String TAG = "RemoteAdapter";

	private Context mContext;
	private List<ResourceBean> datas;
	private int itemWidth;
	private int mAmount;
	private From mFrom;
	private int mId;// 个人id 和 圈子id
	private boolean mIsOpenPage;
	private String mJoinGroupStatus = "";
	private ImageSizeEnum imgSize = ImageSizeEnum.SEVENTY_TWO;
	private ScaleType scaleType = ScaleType.FIT_XY;

	public RemoteAdapter(Context context, List<ResourceBean> photos,
			int screenWidth, GridView gridView, boolean isOpenPage) {
		mContext = context;
		this.datas = photos;
		itemWidth = screenWidth / 4 / 15 * 14;
		mIsOpenPage = isOpenPage;
	}

	public RemoteAdapter(Context context, List<ResourceBean> photos,
			int screenWidth, GridView gridView, boolean isOpenPage,
			String joinGroupStatus) {
		mContext = context;
		this.datas = photos;
		itemWidth = screenWidth / 4 / 10 * 9;
		mIsOpenPage = isOpenPage;
		mJoinGroupStatus = joinGroupStatus;
	}

	/**
	 * 
	 * @param context
	 * @param photos
	 * @param screenWidth
	 * @param gridView
	 * @param isOpenPage
	 * @param joinGroupStatus
	 * @param numCol
	 *            列数
	 */
	public RemoteAdapter(Context context, List<ResourceBean> photos,
			int screenWidth, GridView gridView, boolean isOpenPage,
			String joinGroupStatus, int numCol, ImageSizeEnum size,
			ScaleType imgScaleType) {
		mContext = context;
		this.datas = photos;
		itemWidth = (screenWidth - numCol - 1) / numCol;
		mIsOpenPage = isOpenPage;
		mJoinGroupStatus = joinGroupStatus;
		if (null != size)
			imgSize = size;
		scaleType = imgScaleType;
	}

	/**
	 * 获取总数
	 * 
	 * @param amount
	 */
	public void setAamount(int amount) {
		L.d("RemoteAdapter", "amount" + amount);
		this.mAmount = amount;
	}

	/**
	 * 获取来源
	 * 
	 * @param isMember
	 */
	public void setFrom(From from) {
		mFrom = from;
	}

	public void setId(String id) {
		try {
			this.mId = Integer.valueOf(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public View getView(final int position, View convertView, ViewGroup root) {
		if (null == convertView) {
			// convertView = new ImageView(mContext);
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_gird_imgbox, null);
		}
		ImageView v = (ImageView) convertView
				.findViewById(R.id.item_grid_imgbox_img);
		v.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
		v.setAdjustViewBounds(false);
		v.setScaleType(scaleType);
		v.setImageResource(R.drawable.icon_image_loading);
		ImageView brand = (ImageView) convertView
				.findViewById(R.id.item_grid_imgbox_brand2);
		brand.setImageResource(R.drawable.gif);
		final ResourceBean resBean = datas.get(position);
		L.d("isHasAuthority",
				"position+isHasAuthority" + resBean.isHasAuthority());
		if (2 == resBean.getIsDeleteResource()) {
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						APIRequestServers.collectResource(mContext, false,
								String.valueOf(resBean.getObjectId()), ""
										+ resBean.getObjectId(),
								resBean.getObjectType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					resBean.setHasCollect(false);
					datas.remove(resBean);
					notifyDataSetChanged();
				}
			});
		} else {
			List<ObjectInfoBean> info = resBean.getObjectInfo();
			if (info != null && info.size() > 0) {
				ObjectInfoBean bean = info.get(0);
				final String imgUri = bean.getObjectUrl()
						+ bean.getObjectPath();
				L.i(TAG, position + " uri " + imgUri);
				if (imgUri != null && imgUri.toLowerCase().endsWith(".gif")) {
					L.i(TAG, position + " visible");
					brand.setVisibility(View.VISIBLE);
				} else {
					brand.setVisibility(View.GONE);
				}
				final String url = ThumbnailImageUrl.getThumbnailImageUrl(
						imgUri, imgSize);
				v.setTag(position + url);
				MyFinalBitmap.setImage(mContext, v, url);

				v.setTag(R.id.tag_second,
						new String[] { imgUri, String.valueOf(position) });
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] temps = (String[]) v.getTag(R.id.tag_second);
						Bundle bundle = new Bundle();
						bundle.putInt("groupId", mId);
						bundle.putSerializable("from", mFrom);
						int index = 0;
						try {
							if (null != temps[1] && !"".equals(temps[1])) {
								index = Integer.parseInt(temps[1]);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						bundle.putInt("index", (index));
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("beans", datas);
						bundle.putSerializable("map", hashMap);
						bundle.putInt("amount", mAmount);
						bundle.putString("type", "type_more");
						bundle.putBoolean("isOpenPage", mIsOpenPage);
						if ("COOPERATION_LEAGUER".equals(mJoinGroupStatus))// 合作圈子的成员
							bundle.putBoolean("isCooperationLeaguer", true);
						LogicUtil.enter(mContext, PhotoGalleryActivity.class,
								bundle, false);
					}
				});
			}
		}
		return convertView;
	}

}
