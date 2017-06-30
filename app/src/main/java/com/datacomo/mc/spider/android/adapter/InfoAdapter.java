package com.datacomo.mc.spider.android.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
import com.datacomo.mc.spider.android.CreateGroupTopicActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePageActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.PhotoGalleryActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.ReceiveRecommendActivity;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.dialog.ShowToast;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.GroupBasicTrendBean;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.BundleKey;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.DensityUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.ImageDealUtil;
import com.datacomo.mc.spider.android.util.LinkDealUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.ImageGridView;
import com.datacomo.mc.spider.android.view.MixedTextView;
import com.datacomo.mc.spider.android.view.RefreshListView;

public class InfoAdapter extends BaseAdapter {
	private final String TAG = "InfoAdapter";

	private Context mContext;
	private ArrayList<ResourceTrendBean> mInfos;
	private ListView mListView;

	private String fGroupId;

	private int myId;

	private String mJoinGroupStatus = "";

	private final int mSpacing;
	private final int mScreenWidth;

	private LayoutInflater inflater;

	private Fragment fragment;

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	/**
	 * 圈子外（显示圈子海报）
	 * 
	 * @param context
	 * @param info
	 * @param listview
	 */
	public InfoAdapter(Context context, ArrayList<ResourceTrendBean> info,
			ListView listview) {
		mContext = context;
		mInfos = info;
		mListView = listview;

		inflater = LayoutInflater.from(mContext);
		myId = GetDbInfoUtil.getMemberId(App.app);
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		mSpacing = (int) (mScreenWidth * 0.01);
		// checkScreen();
	}

	/**
	 * 圈子内部（不显示圈子海报）
	 * 
	 * @param context
	 * @param info
	 * @param listview
	 * @param fGroupId
	 */
	public InfoAdapter(Context context, ArrayList<ResourceTrendBean> info,
			ListView listview, String fGroupId) {
		mContext = context;
		mInfos = info;
		mListView = listview;
		this.fGroupId = fGroupId;

		inflater = LayoutInflater.from(mContext);
		L.d(TAG, "InfoAdapter fGroupId=" + fGroupId);
		mScreenWidth = BaseData.getScreenWidth();
		mSpacing = (int) (mScreenWidth * 0.01);
	}

	/**
	 * 圈子内部（不显示圈子海报）
	 * 
	 * @param context
	 * @param info
	 * @param listview
	 * @param fGroupId
	 */
	public InfoAdapter(Context context, ArrayList<ResourceTrendBean> info,
			ListView listview, String fGroupId, String joinGroupStatus) {
		mContext = context;
		mInfos = info;
		mListView = listview;
		this.fGroupId = fGroupId;
		mJoinGroupStatus = joinGroupStatus;
		L.d(TAG, "InfoAdapter fGroupId=" + fGroupId);

		inflater = LayoutInflater.from(mContext);
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_info_wall, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.imgGv = (ImageGridView) convertView.findViewById(R.id.imggv);
			holder.leaguer_layout = (LinearLayout) convertView
					.findViewById(R.id.leaguer_layout);
			holder.more = (TextView) convertView
					.findViewById(R.id.leaguer_more);
			holder.poster = (ImageView) convertView
					.findViewById(R.id.group_img);
			holder.poster_no = (ImageView) convertView
					.findViewById(R.id.group_no);
			holder.ori_text = (MixedTextView) convertView
					.findViewById(R.id.ori_text);
			holder.res_text = (MixedTextView) convertView
					.findViewById(R.id.respond);
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
			holder.groupBox = (RelativeLayout) convertView
					.findViewById(R.id.groupbox);
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
		holder.poster_no.setVisibility(View.GONE);
		holder.imgGv.setVisibility(View.GONE);
		holder.leaguer_layout.removeAllViews();
		holder.more.setVisibility(View.GONE);
		holder.file.removeAllViews();

		recycleRes(position, 5);
		final ResourceTrendBean bean = mInfos.get(position);
		final ResourceBean resBean = bean.getResourceBean();
		final String isResPond = bean.getActionType();
		final MemberOrGroupInfoBean memberInfo = resBean.getSendMemberInfo();
		final MemberOrGroupInfoBean oriOwnerBean = resBean
				.getObjOwnerMemberInfo(); // main
		String o_content = ""; // ori content

		String o_name = memberInfo.getName(); // ori owner name
		boolean ownTrend = false;
		if (myId == memberInfo.getId()) {
			o_name = "我";
			ownTrend = true;
		}

		String o_group_name = oriOwnerBean.getName(); // group name

		String publishWay = resBean.getPublishWay();
		if ("FROM_NOTEPAD".equals(resBean.getSourceType())) {
			publishWay = resBean.getSourceType();
		}
		String o_from = SendWay.resoureSendWay(publishWay); // ori
															// from
		final int num_great = resBean.getPraiseNum();
		String o_num_great = " " + num_great; // greate num
		String o_num_respond = " " + resBean.getCommentNum(); // respond num
		String group_img = ThumbnailImageUrl.getThumbnailPostUrl(
				oriOwnerBean.getFullHeadPath(),
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY); // group
		String group2_img = "";
		String g2Id = "";
		String g2Name = "";

		final MemberOrGroupInfoBean trendSenderInfo = bean.getSendMemberInfo();
		String head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
				trendSenderInfo.getFullHeadPath(),
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY); // head img

		String mId = bean.getActionMemberId() + "";
		String gId = oriOwnerBean.getId() + "";

		String goldNum = "";
		if ("JOIN_GROUP".equals(isResPond) || "REMOVED_GROUP".equals(isResPond)) {// 加入圈子、有踢出成员
			GroupBasicTrendBean groupBasicTrendBean = bean
					.getGroupBasicTrendBean();
			if (groupBasicTrendBean != null) {
				group_img = ThumbnailImageUrl.getThumbnailPostUrl(
						groupBasicTrendBean.getGroupHeadUrl()
								+ groupBasicTrendBean.getGroupHeadPath(),
						PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
				group2_img = ThumbnailImageUrl.getThumbnailHeadUrl(
						groupBasicTrendBean.getMemberHeadUrl()
								+ groupBasicTrendBean.getMemberHeadPath(),
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);

				o_name = groupBasicTrendBean.getMemberName();
				o_group_name = groupBasicTrendBean.getGroupName();

				o_from = SendWay.resoureSendWay(bean.getPublishWay());
				g2Id = groupBasicTrendBean.getMemberId() + "";
				gId = groupBasicTrendBean.getGroupId() + "";

				holder.oper.setVisibility(View.GONE);
			}
		} else if ("PRESENT_GROOUP_GOLD".equals(isResPond)// 捐赠圈币
				|| "APPOINT_MANAGER".equals(isResPond)// 任命管理员
				|| "CHANGE_POSTER".equals(isResPond)// 更换海报
				|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
				|| "BECOME_SUBGROUP".equals(isResPond)// 成为子圈
				|| "MERGER_GROUP".equals(isResPond))// 圈子合并
		{
			GroupBasicTrendBean groupBasicTrendBean = bean
					.getGroupBasicTrendBean();
			if (groupBasicTrendBean != null) {
				group_img = ThumbnailImageUrl.getThumbnailPostUrl(
						groupBasicTrendBean.getGroupHeadUrl()
								+ groupBasicTrendBean.getGroupHeadPath(),
						PostSizeEnum.ONE_HUNDRED_AND_TWENTY);
				head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
						groupBasicTrendBean.getMemberHeadUrl()
								+ groupBasicTrendBean.getMemberHeadPath(),
						HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);

				group2_img = ThumbnailImageUrl.getThumbnailPostUrl(
						groupBasicTrendBean.getSecondGroupHeadUrl()
								+ groupBasicTrendBean.getSecondGroupHeadPath(),
						PostSizeEnum.ONE_HUNDRED_AND_TWENTY);

				g2Id = groupBasicTrendBean.getSecondGroupId() + "";
				g2Name = groupBasicTrendBean.getSecondGroupName();

				o_name = groupBasicTrendBean.getMemberName();
				o_group_name = groupBasicTrendBean.getGroupName();
				o_from = SendWay.resoureSendWay(bean.getPublishWay());
				mId = groupBasicTrendBean.getMemberId() + "";
				gId = groupBasicTrendBean.getGroupId() + "";

				goldNum = groupBasicTrendBean.getGoldNum() + "";

				holder.oper.setVisibility(View.GONE);
			}
		} else {
			holder.oper.setVisibility(View.VISIBLE);
		}

		final String memberId = mId;
		final String groupId = gId;
		final String group2Id = g2Id;
		int start = -1;
		int size = 0;
		final List<ObjectInfoBean> main_res = resBean.getObjectInfo();
		for (int i = 0; i < main_res.size(); i++) {
			ObjectInfoBean infoBean = main_res.get(i);
			String type = infoBean.getObjSourceType();
			// String theme = "";
			if ("OBJ_GROUP_TOPIC".equals(type)
					|| "OBJ_OPEN_PAGE_TOPIC".equals(type)) { // 文字内容
				o_content = infoBean.getObjectName();
				String mContent = infoBean.getObjectDescription();

				if (null != o_content && !"".equals(o_content)) {
					o_content = "【" + o_content + "】";
				} else {
					o_content = "";
				}

				if (mContent != null && !"".equals(mContent)) {
					if (mContent.startsWith("\"[") && mContent.endsWith("]\"")) {
						mContent = mContent.substring(1, mContent.length() - 1);
					}
					if (mContent.getBytes().length > 239) {
						mContent = mContent + "…";
					}
				} else {
					mContent = "";
				}

				o_content += mContent;
				// if (null == o_content || "".equals(o_content)
				// || "null".equals(o_content)) {
				// o_content = infoBean.getObjectName();
				// if (null == o_content || "null".equals(o_content)) {
				// o_content = "";
				// }
				// } else {
				if (o_content.contains(LinkDealUtil.FORMAT_LINK_START_PARAMTER)) {
					o_content = LinkDealUtil.subLink(o_content);
				}
				// }
			} else if ("OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(type)) {
				int guestbookType = resBean.getGuestbookType();
				// 留言类型：1-留言 2-咨询 3-投诉 4-表扬
				if (guestbookType == 1) {
					o_content = "【留言】";
				} else if (guestbookType == 2) {
					o_content = "【咨询】";
				} else if (guestbookType == 3) {
					o_content = "【投诉】";
				} else if (guestbookType == 4) {
					o_content = "【表扬】";
				}
				o_content += infoBean.getObjectDescription();
			} else if ("OBJ_GROUP_FILE".equals(type)
					|| "OBJ_OPEN_PAGE_FILE".equals(type)) { // 文件内容
				String file_name = infoBean.getObjectName();
				LinearLayout file_layout = (LinearLayout) inflater.inflate(
						R.layout.layout_info_file, null);
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
			} else if (type.startsWith("OBJ_MEMBER_RES_")) { // 朋友回复， 非交流圈内容，
																// 不显示圈子图标
				// OBJ_MEMBER_RES_LEAVEMESSAGE & OBJ_MEMBER_RES_MODE
				o_content = infoBean.getObjectDescription();
				holder.poster.setVisibility(View.GONE);
				holder.poster_no.setVisibility(View.VISIBLE);
			}
			// else if (type.startsWith("OBJ_GROUP_THEME")) { // 专题显示
			// theme += "#" + infoBean.getObjectDescription() + "#";
			// }
			// if (theme != null && !"".equals(theme)) {
			// o_content = theme + o_content;
			// }
		}
		L.d(TAG, "start:" + start + " size:" + size);
		holder.imgGv.setTag(position);
		if (size > 0) {
			holder.imgGv.setVisibility(View.VISIBLE);
			ImageGridViewAdapter imgGvAdapter = (ImageGridViewAdapter) holder.imgGv
					.getAdapter();
			int headWidth = DensityUtil.dip2px(mContext, 40);
			int allSpace = 0;
			if (null != isResPond && "CREATE_RESOURCE".equals(isResPond))
				allSpace = DensityUtil.dip2px(mContext, 40);// itemSpace+groupBoxSpace
			else
				allSpace = DensityUtil.dip2px(mContext, 50);// itemSpace+groupBoxSpace+.9Space
			int groupBoxMwidth = View.MeasureSpec.makeMeasureSpec(mScreenWidth,
					View.MeasureSpec.UNSPECIFIED);// groupBox's widthMeasureSpec
			holder.groupBox.measure(groupBoxMwidth, groupBoxMwidth);
			int groupBoxWidth = holder.groupBox.getMeasuredWidth();
			int width = mScreenWidth - headWidth - groupBoxWidth - allSpace;
			L.d(TAG, " width:" + width + "=" + mScreenWidth + "-" + headWidth
					+ "-" + groupBoxWidth + "-" + allSpace);
			int itemWh = (width - (mSpacing * 2)) / 3;
			L.d(TAG, "spacing:" + mSpacing);
			L.d(TAG, " itemWidth:" + itemWh + "=" + width + "-" + mSpacing * 2);
			if (null == imgGvAdapter) {
				L.d(TAG, "imgGvAdapter=null");
				imgGvAdapter = new ImageGridViewAdapter(mContext);
			}
			imgGvAdapter.setItemWh(itemWh);
			imgGvAdapter.setBeans(main_res);
			imgGvAdapter.setStart(start);
			imgGvAdapter.setSize(size);
			if (size == 1) {
				imgGvAdapter.setHasAuthority(bean.isHasAuthority());
				imgGvAdapter.setIsResPond(isResPond);
				imgGvAdapter.setJoinGroupStatus(mJoinGroupStatus);
				imgGvAdapter.setResourceBean(bean.getResourceBean(),
						bean.isHasGreat(), bean.isHasAuthority());
				holder.imgGv.setColumnWidth(width);
			} else {
				imgGvAdapter.setHasAuthority(bean.isHasAuthority());
				holder.imgGv.setColumnWidth(itemWh);
			}
			holder.imgGv.setAdapter(imgGvAdapter);
		}

		boolean isLeaveOrMood = false;
		final String objectType = resBean.getObjectType();
		if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)
				|| "OBJ_MEMBER_RES_MOOD".equals(objectType)
				|| "OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(objectType)) {
			isLeaveOrMood = true;
			holder.star.setVisibility(View.GONE);
		} else {
			holder.star.setVisibility(View.VISIBLE);
		}
		holder.ori_text.isLeave(isLeaveOrMood);

		if ("OBJ_MEMBER_RES_LEAVEMESSAGE".equals(objectType)
				&& myId == oriOwnerBean.getId()) {
			o_group_name = "我";
		}

		if (o_name == null || o_name.equals(""))
			o_name = " ";
		if (o_group_name == null || o_group_name.equals(""))
			o_group_name = " ";

		if ((o_name != null && o_name.equals(o_group_name))
				|| "OBJ_MEMBER_RES_MOOD".equals(objectType)) { // 表示此条主题不是发在交流圈里的，
			holder.ori_text.setResTextsss(o_name, o_content);
		} else {
			holder.ori_text.setOriTextsss(o_name, o_group_name, o_content);
		}

		L.i(TAG, "getView groupId=" + groupId + ",fGroupId=" + fGroupId);
		if (groupId.equals(fGroupId)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
			if (!"OBJ_OPEN_PAGE_LEAVEMESSAGE".equals(objectType)) {
				holder.ori_text.setResTextsss(o_name, o_content);
			}
		}

		if ("JOIN_GROUP".equals(isResPond) || "REMOVED_GROUP".equals(isResPond)) {
			joinGroupInfo(holder, bean, o_group_name, isResPond, groupId,
					o_name, group2_img, group2Id);
		} else if ("PRESENT_GROOUP_GOLD".equals(isResPond)) {
			// o_content = "毫不吝啬地向圈子捐赠了 " + goldNum +
			// " 个圈币，太给力了，hold住了整个圈子，有木有?";
			holder.ori_text.setOtherText(
					"%s 毫不吝啬地向 %s 捐赠了 %s 个圈币，太给力了，hold住了整个圈子，有木有？", o_name,
					o_group_name, goldNum);
		} else if ("APPOINT_MANAGER".equals(isResPond)) {
			// o_content = "成为圈子管理员";
			holder.ori_text.setOtherText("%s 成为 %s 管理员", o_name, o_group_name);
		} else if ("CHANGE_POSTER".equals(isResPond)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
			holder.ori_text.setOtherText("%s 更换海报", o_group_name);
		} else if ("COOPERATE_GROUP".equals(isResPond)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
			holder.ori_text.setOtherText("%s 和 %s 合作", o_group_name, g2Name);
			LinearLayout view = (LinearLayout) inflater.inflate(
					R.layout.info_leager, null);
			ImageView iv = (ImageView) view.findViewById(R.id.info_leager_head);
			holder.leaguer_layout.addView(view);
			groupOper(iv, group2_img, group2Id, isResPond, true);
		} else if ("BECOME_SUBGROUP".equals(isResPond)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
			holder.ori_text
					.setOtherText("%s 成为 %s 的下级圈子", o_group_name, g2Name);
			LinearLayout view = (LinearLayout) inflater.inflate(
					R.layout.info_leager, null);
			ImageView iv = (ImageView) view.findViewById(R.id.info_leager_head);
			holder.leaguer_layout.addView(view);
			groupOper(iv, group2_img, group2Id, isResPond, true);
		} else if ("MERGER_GROUP".equals(isResPond)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
			holder.ori_text.setOtherText("%s 合并了 %s", g2Name, o_group_name);
			// groupOper(holder.img, group_img, groupId);
		}

		String time = DateTimeUtil.bTimeFormat(DateTimeUtil.getLongTime(resBean
				.getCreateTime()));
		holder.ori_time.setText(time);
		holder.ori_from.setText(o_from);
		holder.num_great.setText(o_num_great);
		holder.num_respond.setText(o_num_respond);
		holder.ori.setBackgroundDrawable(mContext.getResources().getDrawable(
				R.drawable.nothing));
		holder.res.setVisibility(View.GONE);

		final Resources res = mContext.getResources();

		final boolean isHasCollect = resBean.isHasCollect();
		Drawable drawableLeft_star;
		if (isHasCollect) {
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
							APIRequestServers.collectResource(App.app,
									!isHasCollect,
									String.valueOf(resBean.getObjectId()), ""
											+ resBean.getObjectId(),
									resBean.getObjectType());
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
				resBean.setHasCollect(!isHasCollect);
				collectResourceRefresh(resBean, bean, position);
			}
		});
		boolean isPoster = false;
		if ("JOIN_GROUP".equals(isResPond)// 有新成员
				|| "REMOVED_GROUP".equals(isResPond)// 有踢出成员
				|| "CHANGE_POSTER".equals(isResPond)// 更换海报
				|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
				|| "BECOME_SUBGROUP".equals(isResPond))// 成为子圈
		{
			head_img = group_img;
			isPoster = true;
		} else if ("MERGER_GROUP".equals(isResPond)) {// 圈子合并
			head_img = group2_img;
			isPoster = true;
		}

		holder.head.setTag("head:" + position + head_img);

		if (!isPoster) {
			MyFinalBitmap.setHeader(mContext, holder.head, head_img);
		} else {
			MyFinalBitmap.setPoster(mContext, holder.head, head_img);
		}

		if (null != isResPond) {
			String r_content = "";
			if ("COMMENT_RESOURCE".equals(isResPond)) {
				r_content = bean.getActinContent();
			} else if ("PRAISE_RESOURCE".equals(isResPond)) {
				r_content = "觉得赞";
			}

			if (r_content != null && !"".equals(r_content)) {
				if (r_content.startsWith("\"[") && r_content.endsWith("]\"")) {
					r_content = r_content.substring(1, r_content.length() - 1);
				}

				// if (r_content.getBytes().length > 239) {
				// r_content = r_content + "…";
				// }

				String r_name = trendSenderInfo.getName();
				if (myId == trendSenderInfo.getId()) {
					r_name = "我";
					ownTrend = true;
				} else {
					ownTrend = false;
				}
				String r_from = bean.getPublishWay();
				String r_time = DateTimeUtil.bTimeFormat(DateTimeUtil
						.getLongTime(bean.getCreateTime()));
				holder.res_time.setText(r_time);
				holder.res_from.setText(SendWay.resoureSendWay(r_from));
				if ("PRAISE_RESOURCE".equals(isResPond)) {
					holder.res_text.setResTextPraise(r_name, r_content);
				} else {
					if (r_content != null && r_content.startsWith("@")
							&& r_content.contains("：")) {
						holder.res_text.setResTextPraise(r_name, r_content);
					} else {
						holder.res_text.setResTextsss(r_name, r_content);
					}
				}
				holder.ori.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.drawable.bg_dialog));
				holder.res.setVisibility(View.VISIBLE);
			}
		}

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				if ("JOIN_GROUP".equals(isResPond)// 有新成员
						|| "REMOVED_GROUP".equals(isResPond)// 有踢出成员
						|| "CHANGE_POSTER".equals(isResPond)// 更换海报
						|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
						|| "BECOME_SUBGROUP".equals(isResPond))// 成为子圈
				{
					b.putString("Id", groupId);
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				} else if ("MERGER_GROUP".equals(isResPond)) {// 圈子合并
					b.putString("Id", group2Id);
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				} else {
					b.putString("id", memberId);
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			}
		});

		final String repName = trendSenderInfo.getName();
		final int repId = trendSenderInfo.getId();
		final boolean oTrend = ownTrend;
		holder.head.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (!bean.isHasAuthority()) {
					T.show(App.app, R.string.limits);
					return true;
				}
				if (oTrend) {
					ShowToast.getToast(App.app).show("试试长按别人的头像");
				} else {
					if ("JOIN_GROUP".equals(isResPond)// 有新成员
							|| "REMOVED_GROUP".equals(isResPond)// 有踢出成员
							|| "CHANGE_POSTER".equals(isResPond)// 更换海报
							|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
							|| "BECOME_SUBGROUP".equals(isResPond))// 成为子圈
					{
					} else if ("MERGER_GROUP".equals(isResPond)) {// 圈子合并
					} else {
						Intent intent = new Intent(mContext,
								CreateGroupTopicActivity.class);
						ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
						GroupEntity entity = new GroupEntity(groupId,
								oriOwnerBean.getName(), oriOwnerBean
										.getFullHeadPath(), "", "", "");
						list.add(entity);
						intent.putExtra("datas", (Serializable) list);
						intent.putExtra(Intent.EXTRA_TEXT, "@" + repName + "：");
						intent.putExtra("repId", repId);
						intent.putExtra(BundleKey.TYPE_REQUEST, Type.DEFAULT);
						mContext.startActivity(intent);
					}
				}
				return true;
			}
		});

		OnClickListener detailsListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("JOIN_GROUP".equals(isResPond)
						|| "REMOVED_GROUP".equals(isResPond)// 有踢出成员
						|| "APPOINT_MANAGER".equals(isResPond)// 任命管理员
						|| "CHANGE_POSTER".equals(isResPond)// 更换海报
						|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
						|| "BECOME_SUBGROUP".equals(isResPond)// 成为子圈
						|| "MERGER_GROUP".equals(isResPond)) {// 圈子合并
					return;
				}
				Bundle b = new Bundle();
				ResourceBean resourceBean = bean.getResourceBean();
				resourceBean.setHasPraise(bean.isHasGreat());
				resourceBean.setHasAuthority(bean.isHasAuthority());
				b.putSerializable("info", resourceBean);
				b.putInt("position", position);
				b.putString("type_From", "resource");
				if ("COOPERATION_LEAGUER".equals(mJoinGroupStatus))// 合作圈子的成员
					b.putBoolean("isCooperationLeaguer", true);

				if (fragment == null) {
					LogicUtil.enter(mContext, CircleBlogDetailsActivity.class,
							b, 10);
				} else {
					Intent intent = new Intent(mContext,
							CircleBlogDetailsActivity.class);
					intent.putExtras(b);
					fragment.startActivityForResult(intent, 10);
				}
			}
		};
		holder.ori.setOnClickListener(detailsListener);
		holder.ori_text.setOnClickListener(detailsListener);
		holder.res_text.setOnClickListener(detailsListener);
		holder.num_respond.setOnClickListener(detailsListener);
		holder.file.setOnClickListener(detailsListener);
		// holder.imgGv.setOnClickListener(detailsListener);
		Drawable drawableLeft_great;
		if (bean.isHasGreat()) {
			drawableLeft_great = res.getDrawable(R.drawable.icon_great_2);
		} else {
			drawableLeft_great = res.getDrawable(R.drawable.icon_great);
		}
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		drawableLeft_great.setBounds(0, 0,
				drawableLeft_great.getMinimumWidth(),
				drawableLeft_great.getMinimumHeight());
		holder.num_great.setCompoundDrawables(drawableLeft_great, null, null,
				null); // 设置左图标

		holder.num_great.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!bean.isHasAuthority()) {
					T.show(App.app, R.string.limits);
					return;
				}
				if (bean.isHasGreat()) {
					// T.show(mContext, "您已经赞过");
				} else {
					new PraiseTask(position).execute(bean);
					praiseResourceRefresh(resBean, bean, position);
				}
			}
		});

		holder.poster.setTag("poster:" + position + group_img);
		// Drawable drawable = new AsyncImageDownLoad(group_img,
		// new String[] { group_img }, TaskUtil.POSTERDEFAULTLOADSTATEIMG,
		// ConstantUtil.POSTER_PATH, mContext, "infoadapter_4" + " "
		// + position, new ImageCallback() {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// loadImg(ImageDealUtil.getPosterCorner(
		// (Drawable) object, -1), "poster:" + position
		// + tags[0]);
		//
		// }
		// }).getDrawable();
		// holder.poster.setImageDrawable(ImageDealUtil.getPosterCorner(drawable,
		// -1));
		L.d(TAG, "setPoster group_img=" + group_img);
		MyFinalBitmap.setPosterCorner(mContext, holder.poster, group_img);

		holder.poster.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				// b.putSerializable("bean", oriOwnerBean);
				b.putString("Id", groupId);
				if (objectType != null && objectType.contains("OBJ_OPEN_PAGE")) {
					b.putString("To", "OpenPageId");
					LogicUtil.enter(mContext, HomePageActivity.class, b, false);
				} else {
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				}
			}
		});
		return convertView;
	}

	/**
	 * 有新成员加入
	 * 
	 * @param holder
	 * @param bean
	 * @param o_group_name
	 * @param isResPond
	 * @param groupId
	 * @param o_name
	 * @param group2_img
	 * @param group2Id
	 */
	private void joinGroupInfo(ViewHolder holder, final ResourceTrendBean bean,
			String o_group_name, final String isResPond, final String groupId,
			String o_name, final String group2_img, final String group2Id) {
		holder.poster.setVisibility(View.GONE);
		holder.poster_no.setVisibility(View.VISIBLE);
		List<GroupBasicTrendBean> groupBasicTrendBeanList = bean
				.getGroupBasicTrendBeanList();
		if (groupBasicTrendBeanList != null) {
			int sizeL = groupBasicTrendBeanList.size();
			L.d(TAG, "sizeL=" + sizeL);
			if (sizeL > 4) {
				final int newNum = groupBasicTrendBeanList.get(4)
						.getNewGroupLeaguerNum();
				if ("JOIN_GROUP".equals(isResPond)) {
					holder.ori_text.setOtherText("%s 有%s个新成员 ", o_group_name,
							newNum + "");
				} else if ("REMOVED_GROUP".equals(isResPond)) {
					holder.ori_text.setOtherText("%s 有%s个成员被踢出 ", o_group_name,
							newNum + "");
				}
				for (int i = 1; i < sizeL - 1; i++) {
					final GroupBasicTrendBean gbtBean = groupBasicTrendBeanList
							.get(i);
					LinearLayout view = (LinearLayout) inflater.inflate(
							R.layout.info_leager, null);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle b = new Bundle();
							b.putString("id", gbtBean.getMemberId() + "");
							b.putString("name", gbtBean.getMemberName());
							b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
							LogicUtil.enter(mContext, HomePgActivity.class, b,
									false);
						}
					});
					ImageView iv = (ImageView) view
							.findViewById(R.id.info_leager_head);
					((TextView) view.findViewById(R.id.info_leager_name))
							.setText(gbtBean.getMemberName());
					holder.leaguer_layout.addView(view);
					groupOper(iv, ThumbnailImageUrl.getThumbnailHeadUrl(
							gbtBean.getMemberHeadUrl()
									+ gbtBean.getMemberHeadPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY),
							gbtBean.getMemberId() + "", isResPond, false);
				}
				holder.more.setVisibility(View.VISIBLE);
				holder.more.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle b = new Bundle();
						if ("JOIN_GROUP".equals(isResPond)) {
							b.putInt("type", 0);
							// b.putSerializable(BundleKey.TYPE_REQUEST,
							// Type.GROUPLEAGUER);
							// b.putString(BundleKey.ID_GROUP, groupId);
							// b.putString(BundleKey.JOINGROUPSTATUS, "true");
							// b.putInt(BundleKey.NUM, gLeaguerNum);
							// LogicUtil.enter(mContext,
							// MemberListActivity.class,
							// b, false);
						} else if ("REMOVED_GROUP".equals(isResPond)) {
							b.putInt("type", 1);
						}
						// TODO
						b.putLong("CreateTime",
								DateTimeUtil.getLongTime(bean.getCreateTime()));
						b.putString("GroupName", "成员");
						try {
							b.putInt("GroupId", Integer.valueOf(groupId));
						} catch (Exception e) {
							e.printStackTrace();
						}
						b.putInt("allNum", newNum);
						LogicUtil.enter(mContext,
								ReceiveRecommendActivity.class, b, false);
					}
				});
			} else {
				if ("JOIN_GROUP".equals(isResPond)) {
					holder.ori_text.setOtherText("%s 有%s个新成员 ", o_group_name,
							sizeL - 1 + "");
				} else if ("REMOVED_GROUP".equals(isResPond)) {
					holder.ori_text.setOtherText("%s 有%s个成员被踢出 ", o_group_name,
							sizeL - 1 + "");
				}
				for (int i = 1; i < sizeL; i++) {
					final GroupBasicTrendBean gbtBean = groupBasicTrendBeanList
							.get(i);
					LinearLayout view = (LinearLayout) inflater.inflate(
							R.layout.info_leager, null);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Bundle b = new Bundle();
							b.putString("id", gbtBean.getMemberId() + "");
							b.putString("name", gbtBean.getMemberName());
							b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
							LogicUtil.enter(mContext, HomePgActivity.class, b,
									false);
						}
					});
					ImageView iv = (ImageView) view
							.findViewById(R.id.info_leager_head);
					((TextView) view.findViewById(R.id.info_leager_name))
							.setText(gbtBean.getMemberName());
					holder.leaguer_layout.addView(view);
					groupOper(iv, ThumbnailImageUrl.getThumbnailHeadUrl(
							gbtBean.getMemberHeadUrl()
									+ gbtBean.getMemberHeadPath(),
							HeadSizeEnum.ONE_HUNDRED_AND_TWENTY),
							gbtBean.getMemberId() + "", isResPond, false);
				}
			}
		} else {
			LinearLayout view = (LinearLayout) inflater.inflate(
					R.layout.info_leager, null);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("id", group2Id);
					b.putString("name", group2_img);
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			});
			ImageView iv = (ImageView) view.findViewById(R.id.info_leager_head);
			((TextView) view.findViewById(R.id.info_leager_name))
					.setText(o_name);
			holder.leaguer_layout.addView(view);
			if ("JOIN_GROUP".equals(isResPond)) {
				holder.ori_text.setOtherText("%s 有%s个新成员 ", o_group_name, "1");
			} else if ("REMOVED_GROUP".equals(isResPond)) {
				holder.ori_text
						.setOtherText("%s 有%s个成员被踢出 ", o_group_name, "1");
			}
			groupOper(iv, group2_img, group2Id, isResPond, false);
		}
	}

	/**
	 * 圈子操作动态
	 * 
	 * @param iv
	 * @param url
	 * @param id
	 */
	private void groupOper(ImageView iv, String url, final String id,
			final String objectType, final boolean isGroup) {
		L.d(TAG, "groupOper url=" + url + ",id=" + id + ",objectType="
				+ objectType + " " + isGroup);

		iv.setVisibility(View.VISIBLE);
		iv.setTag("img:" + url);
		// Drawable drawable = null;

		if (!isGroup) {
			MyFinalBitmap.setHeader(mContext, iv, url);
		} else {
			MyFinalBitmap.setPoster(mContext, iv, url);
		}
		// iv.setImageDrawable(ImageDealUtil.getPosterCorner(drawable, 4));

		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				if (objectType != null) {
					if (!isGroup) {
						b.putString("id", id);
						b.putString("name", "");
						b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
						// TODO
						LogicUtil.enter(mContext, HomePgActivity.class, b,
								false);
						return;
					} else {
						b.putString("Id", id);
						if (objectType.contains("OBJ_OPEN_PAGE")) {
							b.putString("To", "OpenPageId");
							LogicUtil.enter(mContext, HomePageActivity.class,
									b, false);
						} else {
							LogicUtil.enter(mContext, HomeGpActivity.class, b,
									false);
						}
					}
				}
			}
		});
	}

	class ViewHolder {
		ImageView head, poster, poster_no;
		TextView ori_from, ori_time, num_great, num_respond, res_from,
				res_time, star, more;
		MixedTextView res_text, ori_text;
		LinearLayout res, ori, file;
		LinearLayout oper, leaguer_layout;
		ImageGridView imgGv;
		RelativeLayout groupBox;
	}

	// private void loadHeadImg(Drawable imageDrawable, String imageUrl) {
	// if (mListView != null) {
	// ImageView imgView = (ImageView) mListView.findViewWithTag(imageUrl);
	// if (null != imgView) {
	// if (null != imageDrawable) {
	// imgView.setImageDrawable(imageDrawable);
	// }
	// }
	// }
	// }

	@SuppressLint("NewApi")
	class PraiseTask extends AsyncTask<ResourceTrendBean, Integer, MCResult> {

		private ResourceBean resBean;
		private ResourceTrendBean bean;

		// private int index;

		public PraiseTask(int index) {
			// this.index = index;
		}

		@Override
		protected MCResult doInBackground(ResourceTrendBean... params) {
			bean = params[0];
			resBean = bean.getResourceBean();
			MCResult result = null;
			try {
				result = APIRequestServers.praiseResource(App.app,
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
			// if (null != result && 1 == result.getResultCode()) {
			// praiseResourceRefresh(resBean, bean, index);
			// } else {
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
	private void collectResourceRefresh(ResourceBean resBean,
			ResourceTrendBean bean, int index) {
		bean.setResourceBean(resBean);
		mInfos.remove(index);
		mInfos.add(index, bean);
		notifyDataSetChanged();
	}

	/**
	 * 赞刷新
	 * 
	 * @param resBean
	 * @param bean
	 * @param index
	 */
	private void praiseResourceRefresh(ResourceBean resBean,
			ResourceTrendBean bean, int index) {
		resBean.addPraise();
		bean.setResourceBean(resBean);
		bean.setHasGreat(true);
		mInfos.remove(index);
		mInfos.add(index, bean);
		notifyDataSetChanged();
	}

	private void recycleRes(int cur, int napIndex) {
		int requestIndex = 0;
		if (cur < ((RefreshListView) mListView).getFirstItem()) {
			requestIndex = cur + napIndex;
		} else {
			requestIndex = cur - napIndex;
		}
		ImageView requestView = (ImageView) mListView
				.findViewById(requestIndex);
		if (requestView != null) {
			ImageDealUtil.releaseImageDrawable(requestView, true);
			// requestView.setImageResource(TaskUtil.IMGDEFAULTLOADSTATEIMG[0]);
		}
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
				String isResPond = adapter.getIsResPond();
				if ("JOIN_GROUP".equals(isResPond)
						|| "REMOVED_GROUP".equals(isResPond)// 有踢出成员
						|| "APPOINT_MANAGER".equals(isResPond)// 任命管理员
						|| "CHANGE_POSTER".equals(isResPond)// 更换海报
						|| "COOPERATE_GROUP".equals(isResPond)// 圈子合作
						|| "BECOME_SUBGROUP".equals(isResPond)// 成为子圈
						|| "MERGER_GROUP".equals(isResPond)) {// 圈子合并
					return;
				}
				ResourceBean resourceBean = adapter.getResourceBean();
				bundle.putSerializable("info", resourceBean);
				bundle.putString("type_From", "resource");
				if ("COOPERATION_LEAGUER".equals(adapter.getJoinGroupStatus()))// 合作圈子的成员
					bundle.putBoolean("isCooperationLeaguer", true);
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
