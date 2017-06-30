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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.DottedLine;

public class GroupQuuboAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ResourceBean> beans;
	private int imgWidth, imgHeight;
	private String mJoinGroupStatus;

	public GroupQuuboAdapter(Context context, ArrayList<ResourceBean> data,
			String joinState) {
		mContext = context;
		beans = data;
		int width = BaseData.getScreenWidth();
		imgWidth = width / 4;
		imgHeight = width / 5;
		mJoinGroupStatus = joinState;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewParent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_group_quubo, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.topic = (TextView) convertView.findViewById(R.id.topic);
			holder.publishInfo = (TextView) convertView
					.findViewById(R.id.publishinfo);
			holder.imgNum = (TextView) convertView.findViewById(R.id.imgnum);
			holder.fileNum = (TextView) convertView.findViewById(R.id.filenum);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.blank1 = convertView.findViewById(R.id.blank1);
			holder.blank2 = convertView.findViewById(R.id.blank2);
			holder.images = (RelativeLayout) convertView
					.findViewById(R.id.images);
			holder.files = (LinearLayout) convertView.findViewById(R.id.files);
			((DottedLine) convertView.findViewById(R.id.dottedline))
					.setLineColor(mContext.getResources().getColor(
							R.color.gray_light));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.images.setVisibility(View.GONE);
		holder.blank1.setVisibility(View.GONE);
		holder.blank2.setVisibility(View.GONE);
		holder.files.setVisibility(View.GONE);
		while (holder.files.getChildCount() > 1) { // 清理文件
			holder.files.removeViewAt(1);
		}

		final ResourceBean bean = beans.get(position);
		final MemberOrGroupInfoBean owner = bean.getSendMemberInfo();
		final List<ObjectInfoBean> res = bean.getObjectInfo();
		final int ownerId = owner.getId();

		String name = owner.getName();
		if (ownerId == GetDbInfoUtil.getMemberId(mContext)) {
			name = "我";
		}
		holder.name.setText(name);

		String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
				owner.getFullHeadPath(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY); // group
		MyFinalBitmap.setHeader(mContext, holder.head, headUrl);
		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("id", ownerId + "");
				b.putString("name", owner.getName());
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});

		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getCreateTime()));
		String way = SendWay.resoureSendWay(bean.getPublishWay());
		holder.publishInfo.setText(date + " " + way);

		String topic = null;
		String imgUrl = null;
		int topicNum = 0;
		int imgNum = 0;
		int fileNum = 0;
		for (ObjectInfoBean resBean : res) {
			if ("OBJ_GROUP_PHOTO".equals(resBean.getObjSourceType())) {
				imgNum++;
				if (null == imgUrl) {
					imgUrl = resBean.getFullImgPath();
				}
			} else if ("OBJ_GROUP_TOPIC".equals(resBean.getObjSourceType())) {
				topicNum++;
				topic = resBean.getObjectName();
				if (null == topic || "".equals(topic.trim())) {
					String description = resBean.getObjectDescription();
					if (null != description) {
						if (description.length() < 20) {
							topic = description;
						} else {
							topic = description.substring(0, 20);
						}
					}
				}
				if (null == topic || "".equals(topic.trim())) {
					topic = "主题";
				}
			} else if ("OBJ_GROUP_FILE".equals(resBean.getObjSourceType())) {
				fileNum++;
				String file_name = resBean.getObjectName();
				LinearLayout file_layout = (LinearLayout) LayoutInflater.from(
						mContext).inflate(R.layout.layout_info_file, null);
				((TextView) file_layout.findViewById(R.id.file_name))
						.setText(file_name);
				((ImageView) file_layout.findViewById(R.id.file_icon))
						.setImageResource(FileUtil.getFileIcon(file_name));
				holder.files.addView(file_layout);
				holder.files.setVisibility(View.VISIBLE);
			}
		}
		if (topicNum == 0) {
			if (imgNum > 0) {
				topic = "分享照片";
			} else if (fileNum > 0) {
				topic = "分享文件";
			} else {
				topic = "主题";
			}
		}
		holder.topic.setText(topic);

		if (imgNum > 0) {
			holder.images.setVisibility(View.VISIBLE);
			holder.imgNum.setText(imgNum + "张");
			LayoutParams lp = (LayoutParams) holder.img
					.getLayoutParams();
			lp.width = imgWidth;
			lp.height = imgHeight;
			holder.img.setLayoutParams(lp);
			MyFinalBitmap.setImage(mContext, holder.img, imgUrl);
			if (imgNum > 1) {
				showBlankImg(holder);
			}
		}

		holder.fileNum.setText(fileNum + "个文件");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putSerializable("info", bean);
				b.putString("type_From", "resource");
				if ("COOPERATION_LEAGUER".equals(mJoinGroupStatus))// 合作圈子的成员
					b.putBoolean("isCooperationLeaguer", true);
				LogicUtil.enter(mContext, CircleBlogDetailsActivity.class, b,
						false);

			}
		});
		return convertView;
	}

	private void showBlankImg(ViewHolder holder) {
		holder.blank1.setVisibility(View.VISIBLE);
		holder.blank2.setVisibility(View.VISIBLE);

		LayoutParams lp1 = (LayoutParams) holder.blank1
				.getLayoutParams();
		lp1.width = imgWidth;
		lp1.height = imgHeight;
		holder.blank1.setLayoutParams(lp1);

		LayoutParams lp2 = (LayoutParams) holder.blank2
				.getLayoutParams();
		lp2.width = imgWidth;
		lp2.height = imgHeight;
		holder.blank2.setLayoutParams(lp2);
	}

	private class ViewHolder {
		private TextView topic, name, publishInfo, imgNum, fileNum;
		private ImageView head, img;
		private View blank1, blank2;
		private LinearLayout files;
		private RelativeLayout images;
	}

}
