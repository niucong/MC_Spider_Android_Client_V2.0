package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;

public class SelectFileAdapter extends FileAdapter {

	Context context;

	public SelectFileAdapter(Context context, List<ResourceBean> text) {
		super(context, text);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_file, null);
			holder.file = (TextView) convertView.findViewById(R.id.file);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.from = (TextView) convertView.findViewById(R.id.from);
			holder.owner = (TextView) convertView.findViewById(R.id.owner);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.share = (Button) convertView.findViewById(R.id.share);
			holder.open = (Button) convertView.findViewById(R.id.open);
			holder.details = (LinearLayout) convertView
					.findViewById(R.id.ll_details);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ResourceBean bean = beans.get(position);
		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				new AlertDialog.Builder(context)
						.setTitle("提示")
						.setMessage("是否取消收藏")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											APIRequestServers.collectResource(
													mContext, false,
													String.valueOf(bean
															.getObjectId()),
													"" + bean.getObjectId(),
													bean.getObjectType());
										} catch (Exception e) {
											e.printStackTrace();
										}
										bean.setHasCollect(false);
										collectResourceRefresh(bean, position);
									}
								}).setNegativeButton("否", null).show();
				return false;
			}
		});

		if (2 == bean.getIsDeleteResource()) {
			holder.file.setText("该文件已被删除。");
			holder.size.setVisibility(View.GONE);
			holder.from.setVisibility(View.GONE);
			holder.details.setVisibility(View.GONE);
			holder.size.setVisibility(View.GONE);
			holder.time.setVisibility(View.GONE);
			holder.open.setVisibility(View.GONE);
			holder.share.setVisibility(View.GONE);
			holder.img.setImageResource(R.drawable.file_other);
		} else {
			final MemberOrGroupInfoBean owner = bean.getSendMemberInfo();
			List<ObjectInfoBean> beans = bean.getObjectInfo();
			if (beans == null || beans.size() == 0) {
				return convertView;
			}
			final ObjectInfoBean info = beans.get(0);
			holder.from.setVisibility(View.VISIBLE);
			holder.size.setVisibility(View.VISIBLE);
			holder.details.setVisibility(View.VISIBLE);
			holder.time.setVisibility(View.VISIBLE);
			holder.open.setVisibility(View.GONE);
			holder.share.setVisibility(View.VISIBLE);
			String name = info.getObjectName();
			String ownerName = owner.getName();
			String size = FileUtil.computeFileSize(info.getObjectSize());
			String publishWay = SendWay.resoureSendWay(bean.getPublishWay());
			String time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(bean.getCreateTime()));
			holder.file.setText(name);
			holder.size.setText(size);
			holder.from.setText(publishWay);
			holder.owner.setText(ownerName);
			holder.owner.setVisibility(View.GONE);
			holder.time.setText(time);

			holder.img.setImageResource(FileUtil.getFileIcon(name));

			holder.share.setVisibility(View.GONE);
			holder.share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showShareDia(mContext, "分享给：", new String[] { "圈子", "朋友" });
				}
			});

			holder.file.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openFile(info);
				}
			});

			OnClickListener detailsListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					bean.setHasCollect(true);
					b.putSerializable("info", bean);
					b.putString("type_From", "resource");
					LogicUtil.enter(mContext, CircleBlogDetailsActivity.class,
							b, false);
				}
			};
			holder.details.setOnClickListener(detailsListener);
		}
		return convertView;
	}

	private void collectResourceRefresh(ResourceBean resBean, int index) {
		beans.remove(index);
		notifyDataSetChanged();
	}

}
