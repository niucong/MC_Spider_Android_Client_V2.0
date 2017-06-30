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
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.MailWithActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class MailListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MailContactBean> mails;

	private boolean isLocal;

	public MailListAdapter(Context c, ArrayList<MailContactBean> content,
			ListView list) {
		mContext = c;
		mails = content;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
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
					R.layout.item_mail_list, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.num_all = (TextView) convertView.findViewById(R.id.num_all);
			holder.head = (ImageView) convertView.findViewById(R.id.head_img);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.file = (ImageView) convertView.findViewById(R.id.files);
			holder.title = (TextView) convertView.findViewById(R.id.mail_title);
			holder.details = (ImageView) convertView.findViewById(R.id.details);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MailContactBean mail = mails.get(position);
		String title = mail.getMailSubject();
		String time = DateTimeUtil.mailTimeFormat(DateTimeUtil.getLongTime(mail
				.getLastContactTime()));
		int newNum = mail.getNewNum();
		final String name = mail.getRelationMemberName();
		String path = mail.getRelationMemberPath();
		if (path != null && "/m6/default/email/email.jpg".equals(path)) {
			holder.head.setImageResource(R.drawable.icon_mail);
		} else {
			String headUrl = ThumbnailImageUrl.getThumbnailHeadUrl(
					mail.getRelationMemberUrl() + path,
					HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
			holder.head.setTag(position + headUrl);
			MyFinalBitmap.setHeader(mContext, holder.head, headUrl);
		}

		String sname = name;
		if (sname != null && sname.contains("<"))
			sname = name.substring(0, name.indexOf("<"));
		if (sname != null && sname.contains("@"))
			sname = sname.substring(0, sname.indexOf("@"));
		holder.name.setText(sname);
		holder.title.setText(title);
		holder.time.setText(time);
		if (newNum > 0 && !isLocal) {
			holder.num.setVisibility(View.VISIBLE);
			if (newNum > 99) {
				holder.num.setText("n");
			} else {
				holder.num.setText(newNum + "");
			}
		} else {
			holder.num.setVisibility(View.GONE);
		}
		int allNum = mail.getTotalNum();
		holder.num_all.setVisibility(View.VISIBLE);
		holder.num_all.setText(allNum + "");
		if (mail.getAttachmentNum() > 0) {
			holder.file.setVisibility(View.VISIBLE);
		} else {
			holder.file.setVisibility(View.GONE);
		}

		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = mail.getRelationMemberId();
				if (id != 0) {
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", id + "");
					b.putString("name", name);
					LogicUtil.enter(mContext, HomePgActivity.class, b, false);
				}
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("name", name);
				b.putInt("memberId", mail.getRelationMemberId());
				b.putString("leaguerId", "" + mail.getLeaguerId());
				LogicUtil.enter(mContext, MailWithActivity.class, b, false);

				mail.setIsRead(2);
				mail.setNewNum(0);
				mails.remove(position);
				mails.add(position, mail);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView details, head, file;
		TextView name, title, time, num, num_all;
	}

}
