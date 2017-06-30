package com.datacomo.mc.spider.android.adapter;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CircleBlogDetailsActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.net.been.MCResult;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.service.DownLoadFileThread;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.SendWay;
import com.datacomo.mc.spider.android.util.T;

public class FileAdapter extends BaseAdapter {
	Context mContext;
	protected List<ResourceBean> beans;
	private String mJoinGroupStatus = "";
	private boolean openPage = false;
	private SpinnerProgressDialog spdDialog;

	public FileAdapter(Context context, List<ResourceBean> text) {
		mContext = context;
		this.beans = text;
		spdDialog = new SpinnerProgressDialog(context);
	}

	public FileAdapter(Context context, List<ResourceBean> text,
			String joinGroupStatus) {
		mContext = context;
		this.beans = text;
		mJoinGroupStatus = joinGroupStatus;
		spdDialog = new SpinnerProgressDialog(context);
	}

	public FileAdapter(Context context, List<ResourceBean> text,
			String joinGroupStatus, boolean openPage) {
		mContext = context;
		this.beans = text;
		mJoinGroupStatus = joinGroupStatus;
		this.openPage = openPage;
		spdDialog = new SpinnerProgressDialog(context);
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
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_file, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.file = (TextView) convertView.findViewById(R.id.file);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.from = (TextView) convertView.findViewById(R.id.from);
			holder.owner = (TextView) convertView.findViewById(R.id.owner);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.details = (LinearLayout) convertView
					.findViewById(R.id.ll_details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ResourceBean bean = beans.get(position);
		final MemberOrGroupInfoBean owner = bean.getSendMemberInfo();
		final ObjectInfoBean info = bean.getObjectInfo().get(0);
		String name = info.getObjectName();
		String ownerName = owner.getName();
		String size = FileUtil.computeFileSize(info.getObjectSize());
		String publishWay = SendWay.resoureSendWay(bean.getPublishWay());
		String time = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean
				.getCreateTime()));
		holder.img.setImageResource(FileUtil.getFileIcon(name));
		holder.file.setText(name);
		holder.size.setText(size);
		holder.from.setText(publishWay);
		holder.owner.setText(ownerName);
		holder.owner.setVisibility(View.GONE);
		holder.time.setText(time);
		convertView.findViewById(R.id.info).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ("GROUP_OWNER".equals(mJoinGroupStatus)
								|| "GROUP_MANAGER".equals(mJoinGroupStatus)
								|| "APPLY_MANAGER".equals(mJoinGroupStatus)
								|| "GROUP_LEAGUER".equals(mJoinGroupStatus)
								|| openPage) {
							openFile(info);
						} else {
							T.show(App.app, "您不是圈子的成员，没有权限下载");
						}
					}
				});

		OnClickListener detailsListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("GROUP_OWNER".equals(mJoinGroupStatus)
						|| "GROUP_MANAGER".equals(mJoinGroupStatus)
						|| "APPLY_MANAGER".equals(mJoinGroupStatus)
						|| "GROUP_LEAGUER".equals(mJoinGroupStatus) || openPage) {
					Bundle b = new Bundle();
					b.putSerializable("info", bean);
					b.putString("type_From", "resource");
					if ("COOPERATION_LEAGUER".equals(mJoinGroupStatus))// 合作圈子的成员
						b.putBoolean("isCooperationLeaguer", true);
					LogicUtil.enter(mContext, CircleBlogDetailsActivity.class,
							b, false);
				} else {
					T.show(App.app, "您不是圈子的成员，没有权限查看详情");
				}
			}
		};
		holder.details.setOnClickListener(detailsListener);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView file, size, time, owner, from;
		LinearLayout details;
		Button open, share;
	}

	@SuppressLint("HandlerLeak")
	void openFile(final ObjectInfoBean info) {
		spdDialog.showProgressDialog("正在处理中...");
		final Handler openHandler = new Handler() {
			public void handleMessage(Message msg) {
				spdDialog.cancelProgressDialog(null);
				switch (msg.what) {
				case 0:
					T.show(mContext, T.ErrStr);
					break;
				case 1:
					break;
				case 2:
					T.show(App.app, "您不是圈子的成员，没有权限下载");
					break;
				case 3:
					T.show(mContext, R.string.downloading);
					break;
				default:
					break;
				}
			};
		};
		new Thread() {
			public void run() {
				try {
					MCResult mcResult = APIRequestServers.fileDownloadPath(
							mContext, info.getGroupId() + "",
							info.getObjectId() + "");
					if (mcResult != null && mcResult.getResultCode() == 1) {
						String fileUrl = mcResult.getResult().toString();
						if (fileUrl == null || "".equals(fileUrl)) {
							openHandler.sendEmptyMessage(0);
							return;
						}

						if ("2".equals(fileUrl)) {
							openHandler.sendEmptyMessage(2);
							return;
						}
						String tempName = fileUrl.substring(fileUrl
								.lastIndexOf("/") + 1);
						File myFile = new File(ConstantUtil.CLOUD_PATH
								+ tempName);
						if (myFile != null && myFile.exists()) {
							new FileUtil().openFile(mContext, myFile);
							openHandler.sendEmptyMessage(1);
							return;
						} else if (ConstantUtil.downloadingList
								.contains(tempName)) {
							openHandler.sendEmptyMessage(3);
							return;
						}

						ConstantUtil.downloadingList.add(tempName);
						new DownLoadFileThread(mContext, fileUrl,
								info.getObjectSize(), info.getObjectName(),
								true).start();
						openHandler.sendEmptyMessage(1);
					} else {
						openHandler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					openHandler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	/* 显示读取中对话框 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showShareDia(Context context, String title,
			String text[]) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				// .setItems(text, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(context, R.layout.choice_item, text),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichcountry) {
								switch (whichcountry) {
								case 0:
									break;
								case 1:
									break;
								default:
									break;
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int which) {
						d.dismiss();
					}
				}).show();
	}
}
