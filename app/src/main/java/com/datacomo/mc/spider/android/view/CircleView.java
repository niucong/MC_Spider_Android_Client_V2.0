package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class CircleView {
	private Context context;
	private GroupLeaguerBean groupLeaguer;
	private LayoutInflater inflater;
	private View view;
	private TextView circle_manager_name, circle_post;
	private ImageView circle_manager_head, circle_manager_sex;
	private ImageView circle_more;
	private LinearLayout circle_item;
	private int layoutId;
	private int circleMasterId;

	public CircleView(Context context, GroupLeaguerBean groupLeaguer,
			int layoutId) {
		this.context = context;
		this.groupLeaguer = groupLeaguer;
		this.layoutId = layoutId;
		inflater = LayoutInflater.from(context);
		findView();
		setView();
	}

	private void findView() {
		view = inflater.inflate(R.layout.cirinfo_item, null);
		circle_item = (LinearLayout) view.findViewById(R.id.circle_item);
		circle_manager_head = (ImageView) view
				.findViewById(R.id.circle_manager_head);
		circle_manager_name = (TextView) view
				.findViewById(R.id.circle_manager_name);
		circle_post = (TextView) view.findViewById(R.id.circle_post);
		circle_manager_sex = (ImageView) view
				.findViewById(R.id.circle_manager_sex);
		circle_more = (ImageView) view.findViewById(R.id.circle_more);
	}

	private void setView() {
		circle_item.setBackgroundResource(layoutId);
		circle_manager_name.setText(groupLeaguer.getMemberName());
		int leaguerStatus = groupLeaguer.getLeaguerStatus();
		if (leaguerStatus == 1) {
			circle_post.setText("（圈主）");
			circleMasterId = groupLeaguer.getLeaguerId();
			L.d("CircleInformationActivity", "CircleView中圈主Id=="
					+ circleMasterId);
		} else if (leaguerStatus == 2) {
			circle_post.setText("（管理员）");
			circleMasterId = 0;
		}
		circle_manager_sex.setImageResource(isSex(groupLeaguer.getMemberSex()));
		String head_Url = groupLeaguer.getMemberHeadUrl()
				+ groupLeaguer.getMemberHeadPath();
		setImageInfo(context, ThumbnailImageUrl.getThumbnailHeadUrl(head_Url,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY), circle_manager_head);
		circle_manager_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendIntent();
			}
		});
		circle_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendIntent();
			}
		});
		circle_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendIntent();
			}
		});
	}

	private void sendIntent() {
		Bundle b = new Bundle();
		b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
		b.putString("id", String.valueOf(groupLeaguer.getMemberId()));
		b.putString("name", groupLeaguer.getMemberName());
		LogicUtil.enter(context, HomePgActivity.class, b, false);
	}

	public View getView() {
		return view;
	}

	public int getGroupMasterId() {

		return circleMasterId;
	}

	/**
	 * 根据传进来的参数判断性别
	 * 
	 * @param i
	 * @return
	 */
	private int isSex(int i) {
		if (i == 1) {
			// 男
			return R.drawable.icon_sex_boy;
		} else if (i == 2) {
			// 女
			return R.drawable.icon_sex_girl;
		}
		// 男或女
		return (Integer) null;
	}

	/**
	 * 加载界面中所有的ImageView的方法
	 */
	private void setImageInfo(Context context, String head_Url,
			ImageView imageView) {

		// Drawable drawable = PublicLoadPicture.loadHead(context, head_Url,
		// imageView, "circlview");
		// imageView.setImageDrawable(drawable);
		// TODO
		MyFinalBitmap.setHeader(context, imageView, head_Url);
	}

}
