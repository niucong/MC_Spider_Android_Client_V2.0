package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.MailDetailsIndexActivity;
import com.datacomo.mc.spider.android.MailWithActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;

public class MailWithAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MailContactBean> mails;

	public MailWithAdapter(Context c, ArrayList<MailContactBean> content) {
		mContext = c;
		mails = content;
	}

	@Override
	public int getCount() {
		return mails.size();
	}

	@Override
	public Object getItem(int position) {
		return mails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_mail_with, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.file = (ImageView) convertView.findViewById(R.id.files);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			// holder.content = (TextView)
			// convertView.findViewById(R.id.mail_content);
			holder.title = (TextView) convertView.findViewById(R.id.mail_title);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MailContactBean mail = mails.get(position);
		String title = mail.getMailSubject();
		String time = DateTimeUtil.mailTimeFormat(DateTimeUtil.getLongTime(mail
				.getContactTime()));
		// String content = mail.getSimpleMailContent();
		int isRead = mail.getIsRead();
		final String name = mail.getRelationMemberName();
		// String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
		// mail.getRelationMemberUrl() + mail.getRelationMemberPath(),
		// HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		if (1 == mail.getSrStatus()) {
			holder.name.setText("我");
		} else {
			String sname = name;
			if (sname != null && sname.contains("<"))
				sname = name.substring(0, name.indexOf("<"));
			if (sname != null && sname.contains("@"))
				sname = sname.substring(0, sname.indexOf("@"));
			holder.name.setText(sname);
		}
		// holder.head.setTag(position + headUrl);
		holder.title.setText(title);
		// holder.content.setText(content);
		holder.time.setText(time);
		if (1 == isRead) {
			holder.head.setImageResource(R.drawable.new_message);
		} else if (2 == isRead) {
			if (1 == mail.getSrStatus()) {
				holder.head.setImageResource(R.drawable.message_send);
			} else {
				holder.head.setImageResource(R.drawable.message_receive);
			}
		}
		if (mail.getAttachmentNum() > 0) {
			holder.file.setVisibility(View.VISIBLE);
		} else {
			holder.file.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("mailId", "" + mail.getMailId());
				b.putString("recordId", "" + mail.getRecordId());
				if (mContext instanceof MailWithActivity) {
					b.putString("friendId",
							((MailWithActivity) mContext).memberId + "");
					b.putString("friendName",
							((MailWithActivity) mContext).memberName);
				}
				b.putInt("position", position);
				LogicUtil.enter(mContext, MailDetailsIndexActivity.class, b,
						false);
				// 未读数减一
				if (mail.getIsRead() == 1
						&& mContext instanceof MailWithActivity
						&& ((MailWithActivity) mContext).unReadNum > 0) {
					((MailWithActivity) mContext).unReadNum -= 1;
				}

				// 更新邮件已读
				mail.setIsRead(2);
				mails.remove(position);
				mails.add(position, mail);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView details, head, file;
		TextView name, title, time, num, content;
	}
}
