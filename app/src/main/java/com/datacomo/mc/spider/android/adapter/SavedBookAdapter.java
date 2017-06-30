package com.datacomo.mc.spider.android.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BackupContactsActivity;
import com.datacomo.mc.spider.android.InfoWallActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.BackupContactsInfo;
import com.datacomo.mc.spider.android.bean.ContactEntity;
import com.datacomo.mc.spider.android.dialog.SpinnerProgressDialog;
import com.datacomo.mc.spider.android.net.APIRequestServers;
import com.datacomo.mc.spider.android.util.ContactsUtil;
import com.datacomo.mc.spider.android.util.T;

public class SavedBookAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder mHolder;

	Context context;
	private ArrayList<BackupContactsInfo> infos;
	private SimpleDateFormat format;

	public SavedBookAdapter(Context context,
			ArrayList<BackupContactsInfo> infos, SimpleDateFormat format) {
		this.context = context;
		this.infos = infos;
		this.format = format;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.backup_time_item, null);
			mHolder = new ViewHolder();
			mHolder.tv_date = (TextView) convertView.findViewById(R.id.date);
			mHolder.tv_type = (TextView) convertView.findViewById(R.id.type);
			mHolder.tv_count = (TextView) convertView.findViewById(R.id.count);
			mHolder.tv_renew = (TextView) convertView.findViewById(R.id.renew);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setItemView(position);
		return convertView;
	}

	private void setItemView(int position) {
		final BackupContactsInfo entity = infos.get(position);

		mHolder.tv_date.setText("备份时间："
				+ format.format(new Date(entity.getTime())));
		mHolder.tv_type.setText("机          型：" + entity.getModel());
		mHolder.tv_count.setText("备份 " + entity.getCount() + "个联系人");

		mHolder.tv_renew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				renewContacts(entity.getBackupId());
			}
		});
	}

	class ViewHolder {
		TextView tv_date, tv_type, tv_count, tv_renew;
	}

	SpinnerProgressDialog spDialog;
	Handler updateHandler, renewHandler;

	public void init(SpinnerProgressDialog spDialog, Handler updateHandler,
			Handler renewHandler) {
		this.spDialog = spDialog;
		this.updateHandler = updateHandler;
		this.renewHandler = renewHandler;
	}

	/**
	 * 恢复通讯录
	 */
	private void renewContacts(final String pointId) {
		spDialog.showProgressDialog("正在恢复通讯录...");
		spDialog.getPd().setOnCancelListener(
				new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						T.show(context, "已转为后台恢复");
					}
				});
		if (BackupContactsActivity.isRenewRun) {
			return;
		}
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				BackupContactsActivity.isRenewRun = true;
				Message msg = new Message();
				try {
					Object[] objects = APIRequestServers.getBackupContacts(
							context, pointId);
					int resultCode = (Integer) objects[0];
					if (resultCode == 1) {
						msg.what = 1;
						ArrayList<ContactEntity> infos = (ArrayList<ContactEntity>) objects[1];
						if (infos != null && infos.size() > 0) {
							int[] counts = ContactsUtil.cogradientContact(
									context, infos, updateHandler);
							int add = counts[0];
							int update = counts[1];
							if (add > 0 && update > 0) {
								msg.obj = "已将云端通讯录恢复到手机，新增" + add + "人，修改"
										+ update + "人。";
							} else if (add > 0) {
								msg.obj = "已将云端通讯录恢复到手机，新增" + add + "人";
							} else if (update > 0) {
								msg.obj = "已将云端通讯录恢复到手机，修改" + update + "人";
							} else {
								msg.obj = "您的云端通讯录与手机一致，无任何改动。";
							}
						} else {
							msg.obj = T.ErrStr;
						}
					} else {
						msg.what = 0;
						msg.obj = T.ErrStr;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = 0;
					msg.obj = T.ErrStr;
				}
				spDialog.getPd().setOnCancelListener(null);
				renewHandler.sendMessage(msg);
				BackupContactsActivity.isRenewRun = false;

				if (InfoWallActivity.infoWallActivity != null) {
					InfoWallActivity.infoWallActivity.uploadAndGetPhone(null);
				}
			};
		}.start();
	}
}
