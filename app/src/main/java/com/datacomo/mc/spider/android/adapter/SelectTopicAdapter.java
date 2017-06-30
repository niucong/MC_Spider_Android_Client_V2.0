package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePageActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LinkDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.ImageGridView;
import com.datacomo.mc.spider.android.view.MixedTextView;

public class SelectTopicAdapter extends BaseAdapter {
	private final String TAG = "SelectTopicAdapter";

	private Context mContext;
	private List<ResourceBean> mInfos;
	private ListView mListView;
	private int mGroupId;

	private final int mSpacing;
	private final int mScreenWidth;

	private boolean noFrom = false;

	public SelectTopicAdapter(Context context, List<ResourceBean> info,
			ListView listview) {
		mContext = context;
		mInfos = info;
		mListView = listview;
		mScreenWidth = BaseData.getScreenWidth();
		mSpacing = (int) (mScreenWidth * 0.01);
	}

	public SelectTopicAdapter(Context context, List<ResourceBean> info,
			ListView listview, boolean noFrom) {
		mContext = context;
		mInfos = info;
		mListView = listview;
		this.noFrom = noFrom;
		mScreenWidth = BaseData.getScreenWidth();
		mSpacing = (int) (mScreenWidth * 0.01);
	}

	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mInfos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_info_wall, null);
			if (noFrom) {
				convertView.findViewById(R.id.item_head_layout).setVisibility(
						View.GONE);
				convertView.findViewById(R.id.groupbox)
						.setVisibility(View.GONE);
			}

			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.imgGv = (ImageGridView) convertView.findViewById(R.id.imggv);
			holder.poster = (ImageView) convertView
					.findViewById(R.id.group_img);
			holder.ori_text = (MixedTextView) convertView
					.findViewById(R.id.ori_text);
			holder.res_text = (MixedTextView) convertView
					.findViewById(R.id.respond);
			holder.groupBox = (RelativeLayout) convertView
					.findViewById(R.id.groupbox);
			holder.ori_from = (TextView) convertView
					.findViewById(R.id.ori_from);
			holder.res_from = (TextView) convertView.findViewById(R.id.from);
			holder.ori_time = (TextView) convertView
					.findViewById(R.id.ori_time);
			holder.res_time = (TextView) convertView.findViewById(R.id.time);
			holder.num_great = (TextView) convertView
					.findViewById(R.id.num_great);
			holder.num_respond = (TextView) convertView
					.findViewById(R.id.num_respond);
			holder.star = (TextView) convertView.findViewById(R.id.star);
			holder.res = (LinearLayout) convertView.findViewById(R.id.res);
			holder.ori = (LinearLayout) convertView.findViewById(R.id.ori);
			holder.file = (LinearLayout) convertView.findViewById(R.id.file);

			holder.oper = (LinearLayout) convertView
					.findViewById(R.id.oper_res);
			holder.imgGv.setNumColumns(3);
			holder.imgGv.setHorizontalSpacing(mSpacing);
			holder.imgGv.setVerticalSpacing(mSpacing);
			holder.imgGv.setOnItemClickListener(gvOnItemClickListener);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.poster.setVisibility(View.VISIBLE);
		holder.imgGv.setVisibility(View.GONE);
		holder.file.removeAllViews();

		final ResourceBean resBean = mInfos.get(position);
		// final String isResPond = bean.getActionType();
		final MemberOrGroupInfoBean memberInfo = resBean.getSendMemberInfo();
		final MemberOrGroupInfoBean oriOwnerBean = resBean
				.getObjOwnerMemberInfo(); // main
		String o_content = ""; // ori content

		String o_name = memberInfo.getName(); // ori owner name
		String o_group_name = oriOwnerBean.getName(); // group name
		String o_from = SendWay.resoureSendWay(resBean.getPublishWay()); // ori
																			// from
		final int num_great = resBean.getPraiseNum();
		String o_num_great = " " + num_great; // greate num
		String o_num_respond = " " + resBean.getCommentNum(); // respond num
		String group_img = ThumbnailImageUrl.getThumbnailPostUrl(
				oriOwnerBean.getFullHeadPath(),
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY); // group
		// String group2_img = "";
		// String g2Id = "";
		// String g2Name = "";

		final MemberOrGroupInfoBean mainBean = resBean.getSendMemberInfo();
		String head_img = ThumbnailImageUrl
				.getThumbnailHeadUrl(mainBean.getFullHeadPath(),
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY); // head img
		String mId = memberInfo.getId() + "";
		String gId = oriOwnerBean.getId() + "";

		final Resources res = mContext.getResources();
		final boolean isHasCollect = resBean.isHasCollect();
		Drawable drawableLeft_star;
		if (!noFrom || isHasCollect) {
			drawableLeft_star = res.getDrawable(R.drawable.star_add);
		} else {
			drawableLeft_star = res.getDrawable(R.drawable.star_delete);
		}
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawableLeft_star.setBounds(0, 0, drawableLeft_star.getMinimumWidth(),
				drawableLeft_star.getMinimumHeight());
		holder.star.setCompoundDrawables(drawableLeft_star, null, null, null); // 设置左图标

		holder.star.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						try {
							MCResult result = APIRequestServers
									.collectResource(mContext, false, String
											.valueOf(resBean.getObjectId()), ""
											+ resBean.getObjectId(), resBean
											.getObjectType());
							if (null != result && 1 == result.getResultCode()) {
								InfoWallActivity.isNeedRefresh = true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
				resBean.setHasCollect(!isHasCollect);
				collectResourceRefresh(resBean, position);
			}
		});
		if (2 == resBean.getIsDeleteResource()) {
			holder.star.setText("取消收藏");
			holder.ori_text.setText("该圈博已被删除");
			holder.poster.setImageResource(R.drawable.icon_poster_loading);
			holder.head.setBackgroundResource(R.drawable.icon_head_loading);
			holder.ori_from.setVisibility(View.GONE);
			holder.ori_time.setVisibility(View.GONE);
			holder.num_great.setVisibility(View.GONE);
			holder.num_respond.setVisibility(View.GONE);
		} else {

			holder.star.setText("");
			holder.num_great.setVisibility(View.VISIBLE);
			holder.num_respond.setVisibility(View.VISIBLE);
			holder.ori_from.setVisibility(View.VISIBLE);
			holder.ori_time.setVisibility(View.VISIBLE);
			// String goldNum = "";
			// if ("JOIN_GROUP".equals(isResPond)// 加入圈子
			// || "PRESENT_GROOUP_GOLD".equals(isResPond)// 捐赠圈币
			// || "APPOINT_MANAGER".equals(isResPond)// 任命管理员
			// || "CHANGE_POSTER".equals(isResPond)// 更换海报
			// || "COOPERATE_GROUP".equals(isResPond)// 圈子合作
			// || "BECOME_SUBGROUP".equals(isResPond)// 成为子圈
			// || "MERGER_GROUP".equals(isResPond))// 圈子合并
			// {
			// GroupBasicTrendBean groupBasicTrendBean = bean
			// .getGroupBasicTrendBean();
			// group_img = ThumbnailImageUrl.getThumbnailPostUrl(
			// groupBasicTrendBean.getGroupHeadUrl()
			// + groupBasicTrendBean.getGroupHeadPath(),
			// PostSizeEnum.EIGHTY);
			// head_img = ThumbnailImageUrl.getThumbnailPostUrl(
			// groupBasicTrendBean.getMemberHeadUrl()
			// + groupBasicTrendBean.getMemberHeadPath(),
			// PostSizeEnum.EIGHTY);
			//
			// group2_img = ThumbnailImageUrl.getThumbnailPostUrl(
			// groupBasicTrendBean.getSecondGroupHeadUrl()
			// + groupBasicTrendBean.getSecondGroupHeadPath(),
			// PostSizeEnum.EIGHTY);
			// g2Id = groupBasicTrendBean.getSecondGroupId() + "";
			// g2Name = groupBasicTrendBean.getSecondGroupName();
			//
			// o_name = groupBasicTrendBean.getMemberName();
			// o_group_name = groupBasicTrendBean.getGroupName();
			// o_from = SendWay.resoureSendWay(bean.getPublishWay());
			// mId = groupBasicTrendBean.getMemberId() + "";
			// gId = groupBasicTrendBean.getGroupId() + "";
			//
			// goldNum = groupBasicTrendBean.getGoldNum() + "";
			//
			// holder.oper.setVisibility(View.GONE);
			// } else {
			// holder.oper.setVisibility(View.VISIBLE);
			// }

			final String memberId = mId;
			final String groupId = gId;
			// final String group2Id = g2Id;
			int start = -1;
			int size = 0;
			final List<ObjectInfoBean> main_res = resBean.getObjectInfo();
			for (int i = 0; i < main_res.size(); i++) {
				ObjectInfoBean infoBean = main_res.get(i);
				String type = infoBean.getObjSourceType();
				if ("OBJ_GROUP_TOPIC".equals(type)
						|| "OBJ_OPEN_PAGE_TOPIC".equals(type)
						|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type)) { // 文字内容
					o_content = infoBean.getObjectDescription();
					if (null == o_content || "".equals(o_content)
							|| "null".equals(o_content)) {
						o_content = infoBean.getObjectName();
						if (null == o_content || "null".equals(o_content)) {
							o_content = "";
						}
					} else {
						if (o_content
								.contains(LinkDealUtil.FORMAT_LINK_START_PARAMTER)) {
							o_content = LinkDealUtil.subLink(o_content);
						}
					}
				} else if ("OBJ_GROUP_FILE".equals(type)
						|| "OBJ_OPEN_PAGE_FILE".equals(type)) { // 文件内容
					String file_name = infoBean.getObjectName();
					LinearLayout file_layout = (LinearLayout) LayoutInflater
							.from(mContext).inflate(R.layout.layout_info_file,
									null);
					((TextView) file_layout.findViewById(R.id.file_name))
							.setText(file_name);
					((ImageView) file_layout.findViewById(R.id.file_icon))
							.setImageResource(FileUtil.getFileIcon(file_name));
					holder.file.addView(file_layout);
				} else if (("OBJ_GROUP_PHOTO".equals(type) || "OBJ_OPEN_PAGE_PHOTO"
						.equals(type))) { // 图片内容
					if (start == -1)
						start = i;
					size++;
				} else if (type.startsWith("OBJ_MEMBER_RES_")) { // 朋友回复，
																	// 非交流圈内容，
																	// 不显示圈子图标
					// OBJ_MEMBER_RES_LEAVEMESSAGE & OBJ_MEMBER_RES_MODE
					o_content = infoBean.getObjectDescription();
					holder.poster.setVisibility(View.INVISIBLE);
				}
			}
			// TODO
			L.d(TAG, "start:" + start + " size:" + size);
			holder.imgGv.setTag(position);
			if (size > 0) {
				holder.imgGv.setVisibility(View.VISIBLE);
				ImageGridViewAdapter imgGvAdapter = (ImageGridViewAdapter) holder.imgGv
						.getAdapter();
				int headWidth = DensityUtil.dip2px(mContext, 40);
				int allSpace = DensityUtil.dip2px(mContext, 30);// itemSpace+groupBoxSpace
				int groupBoxMwidth = View.MeasureSpec.makeMeasureSpec(
						mScreenWidth, View.MeasureSpec.UNSPECIFIED);// groupBox's
																	// widthMeasureSpec
				holder.groupBox.measure(groupBoxMwidth, groupBoxMwidth);
				int groupBoxWidth = holder.groupBox.getMeasuredWidth();
				int width = mScreenWidth - headWidth - groupBoxWidth - allSpace;
				L.d(TAG, " width:" + width + "=" + mScreenWidth + "-"
						+ headWidth + "-" + groupBoxWidth + "-" + allSpace);
				int itemWh = ((int) width - (mSpacing * 2)) / 3;
				L.d(TAG, "spacing:" + mSpacing);
				L.d(TAG, " itemWidth:" + itemWh + "=" + width + "-" + mSpacing
						* 2);
				if (null == imgGvAdapter) {
					L.d(TAG, "imgGvAdapter=null");
					imgGvAdapter = new ImageGridViewAdapter(mContext);
					imgGvAdapter.setItemWh(itemWh);
				}
				imgGvAdapter.setBeans(main_res);
				imgGvAdapter.setStart(start);
				imgGvAdapter.setSize(size);
				if (size == 1) {
					imgGvAdapter.setResourceBean(resBean, true);
					holder.imgGv.setColumnWidth(width);
				} else {
					imgGvAdapter.setHasAuthority(false);
					holder.imgGv.setColumnWidth(itemWh);
				}
				holder.imgGv.setAdapter(imgGvAdapter);
			}

			if (o_content.length() >= 99) {
				o_content = o_content + "...";
			}

			boolean isLeaveOrMood = false;
			final String objectType = resBean.getObjectType();
			if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)
					|| "OBJ_MEMBER_RES_MOOD".equals(objectType)) {
				isLeaveOrMood = true;
				holder.star.setVisibility(View.GONE);
			} else {
				holder.star.setVisibility(View.VISIBLE);
			}
			holder.ori_text.isLeave(isLeaveOrMood);

			if (noFrom) {
				holder.ori_text.setResTextsss(o_content);
			} else {
				if (o_name != null && o_name.equals(o_group_name)) { // 表示此条主题不是发在交流圈里的，
					holder.ori_text.setResTextsss(o_name, o_content);
				} else {
					holder.ori_text.setOriTextsss(o_name, o_group_name,
							o_content);
				}
			}

			// if ("JOIN_GROUP".equals(isResPond)) {
			// // o_content = "有新成员加入";
			// holder.ori_text.setOtherText("%s 加入 %s", o_name, o_group_name);
			// } else if ("PRESENT_GROOUP_GOLD".equals(isResPond)) {
			// // o_content = "毫不吝啬地向圈子捐赠了 " + goldNum +
			// // " 个圈币，太给力了，hold住了整个圈子，有木有?";
			// holder.ori_text.setOtherText(
			// "%s 毫不吝啬地向 %s 捐赠了 %s 个圈币，太给力了，hold住了整个圈子，有木有？", o_name,
			// o_group_name, goldNum);
			// } else if ("APPOINT_MANAGER".equals(isResPond)) {
			// // o_content = "成为圈子管理员";
			// holder.ori_text.setOtherText("%s 成为 %s 管理员", o_name,
			// o_group_name);
			// } else if ("CHANGE_POSTER".equals(isResPond)) {
			// holder.poster.setVisibility(View.INVISIBLE);
			// holder.ori_text.setOtherText("%s 更换海报", o_group_name);
			// } else if ("COOPERATE_GROUP".equals(isResPond)) {
			// holder.poster.setVisibility(View.INVISIBLE);
			// holder.ori_text.setOtherText("%s 和 %s 成为合作圈", o_group_name,
			// g2Name);
			// groupOper(holder.img, group2_img, group2Id);
			// } else if ("BECOME_SUBGROUP".equals(isResPond)) {
			// holder.poster.setVisibility(View.INVISIBLE);
			// holder.ori_text.setOtherText("%s 成为 %s 的下级", o_group_name,
			// g2Name);
			// groupOper(holder.img, group2_img, group2Id);
			// } else if ("MERGER_GROUP".equals(isResPond)) {
			// holder.poster.setVisibility(View.INVISIBLE);
			// holder.ori_text.setOtherText("%s 合并了 %s", g2Name, o_group_name);
			// groupOper(holder.img, group_img, groupId);
			// }

			String time = DateTimeUtil.bTimeFormat(DateTimeUtil
					.getLongTime(resBean.getCreateTime()));
			holder.ori_time.setText(time);
			holder.ori_from.setText(o_from);
			holder.num_great.setText(o_num_great);
			holder.num_respond.setText(o_num_respond);
			holder.ori.setBackgroundResource(R.drawable.nothing);
			holder.res.setVisibility(View.GONE);

			holder.poster.setTag("poster:" + position + group_img);
			// Drawable drawable = new AsyncImageDownLoad(group_img,
			// new String[] { group_img },
			// TaskUtil.POSTERDEFAULTLOADSTATEIMG,
			// ConstantUtil.POSTER_PATH, mContext,
			// "sellecttopicadapter_2", new ImageCallback() {
			//
			// @Override
			// public void load(Object object, Object[] tags) {
			// loadImg(ImageDealUtil.getPosterCorner(
			// (Drawable) object, -1), "poster:"
			// + position + tags[0]);
			// }
			// }).getDrawable();
			// holder.poster.setImageDrawable(ImageDealUtil.getPosterCorner(
			// drawable, -1));
			// TODO
			MyFinalBitmap.setPosterCorner(mContext, holder.poster, group_img);

			// if ("CHANGE_POSTER".equals(isResPond)// 更换海报
			// || "COOPERATE_GROUP".equals(isResPond)// 圈子合作
			// || "BECOME_SUBGROUP".equals(isResPond))// 成为子圈
			// {
			// head_img = group_img;
			// } else if ("MERGER_GROUP".equals(isResPond)) {// 圈子合并
			// head_img = group2_img;
			// }

			holder.head.setTag("head:" + position + head_img);
			// Drawable headDrawable = new AsyncImageDownLoad(head_img,
			// new String[] { head_img },
			// TaskUtil.HEADDEFAULTLOADSTATEIMG, ConstantUtil.HEAD_PATH,
			// mContext, "selecttopicadapter_3", new ImageCallback() {
			//
			// @Override
			// public void load(Object object, Object[] tags) {
			// loadHeadImg(ImageDealUtil.getPosterCorner(
			// (Drawable) object, 4), "head:" + position
			// + tags[0]);
			// }
			// }).getDrawable();
			// holder.head.setImageDrawable(ImageDealUtil.getPosterCorner(
			// headDrawable, 4));
			// TODO
			MyFinalBitmap.setHeader(mContext, holder.head, head_img);

			// if (null != isResPond) {
			// String r_content = "";
			// if ("COMMENT_RESOURCE".equals(isResPond)) {
			// r_content = bean.getActinContent();
			// } else if ("PRAISE_RESOURCE".equals(isResPond)) {
			// r_content = "觉得赞";
			// }
			//
			// if (r_content != null && !"".equals(r_content)) {
			// if (r_content.length() >= 99) {
			// r_content = r_content + "...";
			// }
			//
			// MemberOrGroupInfoBean respondBean = bean.getSendMemberInfo();
			// String r_name = respondBean.getName();
			// String r_from = bean.getPublishWay();
			// String r_time = DateTimeUtil.bTimeFormat(bean.getCreateTime()
			// .getTime());
			// holder.res_time.setText(r_time);
			// holder.res_from.setText(SendWay.resoureSendWay(r_from));
			// holder.res_text.setResTextsss(r_name, r_content);
			// holder.ori.setBackgroundDrawable(mContext.getResources()
			// .getDrawable(R.drawable.bg_dialog));
			// holder.res.setVisibility(View.VISIBLE);
			// }
			// }

			holder.head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", memberId);
					b.putString("name", resBean.getSendMemberInfo().getName());
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			});

			OnClickListener detailsListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					// if ("JOIN_GROUP".equals(isResPond)
					// || "PRESENT_GROOUP_GOLD".equals(isResPond)
					// || "APPOINT_MANAGER".equals(isResPond)
					// || "CHANGE_POSTER".equals(isResPond)) {
					// return;
					// }
					Bundle b = new Bundle();
					// ResourceBean resourceBean = bean.getResourceBean();
					// L.d("hascollect", "hascollect:"+resBean.isHasCollect());
					resBean.setHasCollect(true);
					b.putSerializable("info", resBean);
					b.putString("type_From", "resource");
					LogicUtil.enter(mContext, CircleBlogDetailsActivity.class,
							b, false);
				}
			};

			holder.ori_text.setOnClickListener(detailsListener);
			holder.res_text.setOnClickListener(detailsListener);
			holder.num_respond.setOnClickListener(detailsListener);
			holder.file.setOnClickListener(detailsListener);

			Drawable drawableLeft_great;
			if (resBean.isHasPraise()) {
				drawableLeft_great = res.getDrawable(R.drawable.icon_great_2);
			} else {
				drawableLeft_great = res.getDrawable(R.drawable.icon_great);
			}
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			drawableLeft_great.setBounds(0, 0,
					drawableLeft_great.getMinimumWidth(),
					drawableLeft_great.getMinimumHeight());
			holder.num_great.setCompoundDrawables(drawableLeft_great, null,
					null, null); // 设置左图标

			holder.num_great.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!resBean.isHasAuthority()) {
						T.show(mContext, R.string.limits);
						return;
					}
					if (resBean.isHasPraise()) {
						// T.show(mContext, "您已经赞过");
					} else {
						new PraiseTask(position).execute(resBean);
						praiseResourceRefresh(resBean, position);
					}
				}
			});

			Drawable drawableLeft_respond = res
					.getDrawable(R.drawable.icon_respond);
			drawableLeft_respond.setBounds(0, 0,
					drawableLeft_respond.getMinimumWidth(),
					drawableLeft_respond.getMinimumHeight());
			holder.num_respond.setCompoundDrawables(drawableLeft_respond, null,
					null, null); // 设置左图标

			holder.poster.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					// b.putSerializable("bean", oriOwnerBean);
					b.putString("Id", groupId);
					if (objectType != null
							&& objectType.contains("OBJ_OPEN_PAGE")) {
						b.putString("To", "OpenPageId");
						LogicUtil.enter(mContext, HomePageActivity.class, b,
								false);
					} else {
						LogicUtil.enter(mContext, HomeGpActivity.class, b,
								false);
					}
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		ImageView head, poster;
		TextView ori_from, ori_time, num_great, num_respond, res_from,
				res_time, star;
		MixedTextView res_text, ori_text;
		LinearLayout res, ori, file;
		LinearLayout oper;
		ImageGridView imgGv;
		RelativeLayout groupBox;
	}

	class PraiseTask extends AsyncTask<ResourceBean, Integer, MCResult> {

		private ResourceBean resBean;

		// private int index;

		public PraiseTask(int index) {
			// this.index = index;
		}

		@Override
		protected MCResult doInBackground(ResourceBean... params) {
			resBean = params[0];
			MCResult result = null;
			try {
				result = APIRequestServers.praiseResource(mContext,
						String.valueOf(resBean.getObjectOwnerId()),
						String.valueOf(resBean.getObjectId()),
						resBean.getObjectType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(MCResult result) {
			super.onPostExecute(result);
			if (null != result && 1 == result.getResultCode()) {
				InfoWallActivity.isNeedRefresh = true;
			}
			// else {
			// T.show(mContext, T.ErrStr);
			// }
		}
	}

	/**
	 * 收藏刷新
	 * 
	 * @param resBean
	 * @param bean
	 * @param index
	 */
	private void collectResourceRefresh(ResourceBean resBean, int index) {
		mInfos.remove(index);
		// mInfos.add(index, resBean);
		notifyDataSetChanged();
	}

	/**
	 * 赞刷新
	 * 
	 * @param resBean
	 * @param bean
	 * @param index
	 */
	private void praiseResourceRefresh(ResourceBean resBean, int index) {
		resBean.addPraise();
		resBean.setHasPraise(true);
		mInfos.remove(index);
		mInfos.add(index, resBean);
		notifyDataSetChanged();
	}

	private OnItemClickListener gvOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ImageGridViewAdapter adapter = (ImageGridViewAdapter) arg0
					.getAdapter();
			Bundle bundle = new Bundle();
			switch (adapter.getSize()) {
			case 1:
				L.d(TAG, "oneitemclick");
				ResourceBean resourceBean = adapter.getResourceBean();
				bundle.putSerializable("info", resourceBean);
				bundle.putString("type_From", "resource");
				LogicUtil.enter(mContext, CircleBlogDetailsActivity.class,
						bundle, false);
				break;
			default:
				L.d(TAG, "moreitemclick");
				List<ObjectInfoBean> beans = adapter.getBeans();
				ObjectInfoBean bean = beans.get(adapter.getStart());
				L.d(TAG, "item:" + arg2);
				bundle.putInt("index", arg2);
				bundle.putInt("groupId", bean.getGroupId());
				L.d(TAG, "groupId:" + bean.getGroupId());
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("beans", beans);
				bundle.putSerializable("map", hashMap);
				bundle.putString("type", "type_default");
				if (adapter.isHasAuthority())
					bundle.putInt("authority", 1);
				else
					bundle.putInt("authority", 0);
				hashMap = null;
				LogicUtil.enter(mContext, PhotoGalleryActivity.class, bundle,
						false);
				break;
			}
		}
	};
}