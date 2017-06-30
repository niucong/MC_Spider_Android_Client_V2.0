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

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupThemeBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupTopicAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<GroupThemeBean> beans;
	public GroupTopicAdapter(Context context, ArrayList<GroupThemeBean> data) {
		mContext = context;
		beans = data;
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
		if(null == convertView){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_topic, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.topic = (TextView) convertView.findViewById(R.id.topic);
			holder.publishInfo = (TextView) convertView.findViewById(R.id.publishdate);
			holder.quuboNum = (TextView) convertView.findViewById(R.id.quuboNum);
			holder.unReadNum = (TextView) convertView.findViewById(R.id.unreadNum);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final GroupThemeBean bean = beans.get(position);
		final int ownerId = bean.getMemberId();
		
		String name = bean.getMemberName();
		if(ownerId == GetDbInfoUtil.getMemberId(mContext)){
			name = "我";
		}
		holder.name.setText(name + " 发起");
		
		String headUrl = ThumbnailImageUrl.getThumbnailPostUrl(
				bean.getMemberFullPath(),
				PostSizeEnum.ONE_HUNDRED_AND_TWENTY); // group
		MyFinalBitmap.setHeader(mContext, holder.head, headUrl);
		holder.head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("id", ownerId + "");
				b.putString("name", bean.getMemberName());
				b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			}
		});
		
		String date = DateTimeUtil.aTimeFormat(DateTimeUtil.getLongTime(bean.getCreateTime()));
		holder.publishInfo.setText(date);
		
		String content = bean.getThemeContent();
		holder.topic.setText("#" + content + "#");
		
		holder.quuboNum.setText(bean.getQuuboNum() + " 圈博");
		
		int unread = bean.getNoReadNum();
		if(unread > 0){
			holder.unReadNum.setText(unread + "");
			holder.unReadNum.setVisibility(View.VISIBLE);
		}else{
			holder.unReadNum.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	
	private class ViewHolder{
		private TextView topic, name, publishInfo, unReadNum, quuboNum;
		private ImageView head;
	}

}
