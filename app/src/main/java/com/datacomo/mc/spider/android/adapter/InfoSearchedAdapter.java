package com.datacomo.mc.spider.android.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePageActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MembercacheGPInfo;
import com.datacomo.mc.spider.android.net.been.MembercacheMInfo;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResultMessageBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;
import com.datacomo.mc.spider.android.view.MixedTextView;

public class InfoSearchedAdapter extends BaseAdapter {
	private final String TAG = "InfoSearchedAdapter";

	private Context mContext;
	private ArrayList<ResultMessageBean> mInfos;

	private String fGroupId;
	private String mJoinGroupStatus = "";

	public InfoSearchedAdapter(Context context,
			ArrayList<ResultMessageBean> info, ListView listview) {
		mContext = context;
		mInfos = info;
	}

	public InfoSearchedAdapter(Context context,
			ArrayList<ResultMessageBean> info, ListView listview,
			String fGroupId) {
		mContext = context;
		mInfos = info;
		this.fGroupId = fGroupId;
	}

	public InfoSearchedAdapter(Context context,
			ArrayList<ResultMessageBean> info, ListView listview,
			String fGroupId, String joinGroupStatus) {
		mContext = context;
		mInfos = info;
		this.fGroupId = fGroupId;
		mJoinGroupStatus = joinGroupStatus;
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
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_info_wall, null);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			// holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.poster = (ImageView) convertView
					.findViewById(R.id.group_img);
			holder.poster_no = (ImageView) convertView
					.findViewById(R.id.group_no);
			// convertView.findViewById(R.id.ori).setVisibility(View.GONE);
			holder.ori_text = (MixedTextView) convertView
					.findViewById(R.id.ori_text);
			holder.ori_from = (TextView) convertView
					.findViewById(R.id.ori_from);
			holder.ori_time = (TextView) convertView
					.findViewById(R.id.ori_time);

			holder.res_text = (MixedTextView) convertView
					.findViewById(R.id.respond);
			holder.res_from = (TextView) convertView.findViewById(R.id.from);
			holder.res_time = (TextView) convertView.findViewById(R.id.time);

			holder.num_great = (TextView) convertView
					.findViewById(R.id.num_great);
			holder.num_respond = (TextView) convertView
					.findViewById(R.id.num_respond);
			holder.ori = (LinearLayout) convertView.findViewById(R.id.ori);
			holder.file = (LinearLayout) convertView.findViewById(R.id.file);
			convertView.findViewById(R.id.oper_res).setVisibility(View.GONE);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.poster.setVisibility(View.VISIBLE);
		holder.poster_no.setVisibility(View.GONE);
		holder.res_text.setVisibility(View.VISIBLE);
		holder.res_time.setVisibility(View.VISIBLE);
		holder.res_from.setVisibility(View.VISIBLE);
		holder.ori.setBackgroundDrawable(mContext.getResources().getDrawable(
				R.drawable.nothing));
		holder.file.removeAllViews();

		final ResultMessageBean bean = mInfos.get(position);
		final MembercacheMInfo owner = bean.getMMInfo();
		final MembercacheGPInfo group = bean.getMGPInfo();
		String o_name = owner.getMemberName(); // ori owner name
		String o_group_name = group.getGroupName(); // group name
		final String indexname = bean.getIndexname();
		L.d(TAG, "o_name=" + o_name + ",o_group_name=" + o_group_name);

		String head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
				owner.getWholeHeadUrl(), HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);

		String gId = group.getGroupID() + "";
		if (gId.equals(fGroupId)) {
			holder.poster.setVisibility(View.GONE);
			holder.poster_no.setVisibility(View.VISIBLE);
		}

		if ("group_topic_info".equals(indexname)
				|| "group_topic_open_info".equals(indexname)
				|| "group_topic_comment_info".equals(indexname)) {
			String title = bean.getTitle();
			String o_content = bean.getContent(); // ori content
			if (o_content.getBytes().length > 239) {
				o_content = o_content + "…";
			}
			if (title != null && !"".equals(title)) {
				o_content = "【" + title + "】" + o_content;
			}
			holder.ori_text.setOriTextsss(o_name, o_group_name, o_content);
		} else if ("group_file_info".equals(indexname)
				|| "group_file_comment_info".equals(indexname)) {
			String file_Path = bean.getFilepath();
			String file_name = bean.getTitle()
					+ file_Path.substring(file_Path.lastIndexOf("."));
			holder.ori_text.setOriTextsss(o_name, o_group_name, "上传文件");

			LinearLayout file_layout = (LinearLayout) LayoutInflater.from(
					mContext).inflate(R.layout.layout_info_file, null);
			((TextView) file_layout.findViewById(R.id.file_name)).setText(Html
					.fromHtml(file_name, null, null));
			((ImageView) file_layout.findViewById(R.id.file_icon))
					.setImageResource(FileUtil.getFileIcon(file_name));
			holder.file.addView(file_layout);
		} else if ("group_photo_info".equals(indexname)
				|| "group_photo_comment_info".equals(indexname)) {
			// final String url = owner.getMemberHeadUrl() + bean.getFilepath();
			// final String img_url =
			// ThumbnailImageUrl.getThumbnailImageUrl(url,
			// ImageSizeEnum.THREE_HUNDRED);
			// holder.img.setTag("img:" + position + img_url);

			holder.ori_text.setOriTextsss(o_name, o_group_name,
					"上传照片 " + bean.getTitle());
			// final int mGroupId = group.getGroupID();
			// MyFinalBitmap.setImage(mContext, holder.img, img_url);

			// holder.img.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Bundle bundle = new Bundle();
			// bundle.putInt("groupId", mGroupId);
			// HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//
			// List<ObjectInfoBean> main_res = new ArrayList<ObjectInfoBean>();
			// ObjectInfoBean oIBean = new ObjectInfoBean();
			// oIBean.setObjectUrl(owner.getMemberHeadUrl());
			// oIBean.setObjectPath(bean.getFilepath());
			// try {
			// oIBean.setObjectId(Integer.valueOf(bean.getId()));
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// oIBean.setGroupId(mGroupId);
			// oIBean.setObjSourceType("OBJ_GROUP_PHOTO");
			// main_res.add(oIBean);
			// bundle.putString("type", "type_only");
			// hashMap.put("beans", main_res);
			// bundle.putSerializable("map", hashMap);
			// hashMap = null;
			// LogicUtil.enter(mContext, PhotoGalleryActivity.class,
			// bundle, false);
			// }
			// });
		}

		List<ResultMessageBean> childList = bean.getChildList();
		if (childList != null) {
			L.i(TAG, "getView size=" + childList.size());
			ResultMessageBean rBean = childList.get(0);
			String r_title = rBean.getTitle();
			String r_content = rBean.getContent(); // ori content
			if (r_content.getBytes().length > 239) {
				r_content = r_content + "…";
			}
			if (r_title != null && !"".equals(r_title)) {
				r_content = "【" + r_title + "】" + r_content;
			}
			MembercacheMInfo rm = rBean.getMMInfo();
			String r_name = rm.getMemberName(); // ori owner name
			head_img = ThumbnailImageUrl.getThumbnailHeadUrl(
					rm.getWholeHeadUrl(), HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			holder.res_text.setResTextsss(r_name, r_content);

			String r_from = SendWay.resoureSendWay(rBean.getCreateway()); // from
			String r_date = rBean.getDate();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				r_date = DateTimeUtil.bTimeFormat(format.parse(r_date)
						.getTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			holder.res_time.setText(r_date);
			holder.res_from.setText(r_from);

			holder.ori.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.bg_dialog));
		} else {
			holder.res_text.setVisibility(View.GONE);
			holder.res_time.setVisibility(View.GONE);
			holder.res_from.setVisibility(View.GONE);
		}

		String o_from = SendWay.resoureSendWay(bean.getCreateway()); // from
		String o_date = bean.getDate();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			o_date = DateTimeUtil.bTimeFormat(format.parse(o_date).getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		String o_num_great = "" + bean.getHot(); // greate num
		String o_num_respond = "" + bean.getTag(); // respond num
		String group_img = ThumbnailImageUrl.getThumbnailPostUrl(
				group.getWholeUrl(), PostSizeEnum.ONE_HUNDRED_AND_TWENTY); // group
																			// img

		holder.ori_text.setTag(bean);
		// holder.img.setTag(bean);
		// holder.file.setTag(bean);
		holder.ori_time.setText(o_date);
		holder.ori_from.setText(o_from);
		holder.num_great.setText(" 赞(" + o_num_great + ")");
		holder.num_respond.setText(" 评论(" + o_num_respond + ")");
		holder.poster.setTag("poster:" + position + group_img);
		MyFinalBitmap.setPosterCorner(mContext, holder.poster, group_img);

		holder.head.setTag("head:" + head_img);
		MyFinalBitmap.setHeader(mContext, holder.head, head_img);

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				b.putString("id", String.valueOf(owner.getMemberID()));
				b.putString("name", owner.getMemberName());
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});

		OnClickListener detailsListener = new OnClickListener() {

			@Override
			public void onClick(final View v) {
				new Thread() {
					public void run() {
						try {
							ResultMessageBean bean = (ResultMessageBean) v
									.getTag();
							MCResult mcResult = APIRequestServers.quuboInfo(
									mContext, bean.getGroupid(),
									bean.getParentid());
							if (mcResult != null
									&& mcResult.getResultCode() == 1) {
								QuuboInfoBean bean_QuuboInfo = (QuuboInfoBean) mcResult
										.getResult();
								Bundle b = new Bundle();
								b.putSerializable("info", bean_QuuboInfo);
								b.putString("type_From", "resultMessage");
								if ("COOPERATION_LEAGUER"
										.equals(mJoinGroupStatus))// 合作圈子的成员
									b.putBoolean("isCooperationLeaguer", true);
								LogicUtil.enter(mContext,
										CircleBlogDetailsActivity.class, b,
										false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();

			}
		};
		holder.ori_text.setOnClickListener(detailsListener);
		// holder.img.setOnClickListener(detailsListener);
		// holder.file.setOnClickListener(detailsListener);

		holder.poster.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("Id", String.valueOf(group.getGroupID()));
				if ("group_topic_open_info".equals(indexname)) {
					b.putString("To", "OpenPageId");
					LogicUtil.enter(mContext, HomePageActivity.class, b, false);
				} else {
					LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView head, poster, poster_no;// , img
		TextView ori_from, ori_time, num_great, num_respond, res_from,
				res_time;
		MixedTextView res_text, ori_text;
		LinearLayout ori, file;

		// ImageView head, poster, poster_no;
		// TextView ori_from, ori_time, num_great, num_respond, res_from,
		// res_time, star, more;
		// MixedTextView res_text, ori_text;
		// LinearLayout res, ori, file;
		// LinearLayout oper, leaguer_layout;
		// ImageGridView imgGv;
		// RelativeLayout groupBox;
	}

}