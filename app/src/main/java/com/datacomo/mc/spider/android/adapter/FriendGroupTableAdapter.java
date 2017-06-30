package com.datacomo.mc.spider.android.adapter;
//package com.datacomo.mc.spider.android.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.datacomo.mc.spider.android.FriendGroupActivity;
//import com.datacomo.mc.spider.android.FriendListByGroupActivity;
//import com.datacomo.mc.spider.android.R;
//import com.datacomo.mc.spider.android.bean.TableInfoBean;
//import com.datacomo.mc.spider.android.net.been.FriendGroupBean;
//import com.datacomo.mc.spider.android.util.RandomBgUtil;
//
//public class FriendGroupTableAdapter extends BaseAdapter {
//	// private static final String TAG = "FriendGroupTableAdapter";
//	private Context context;
//	private ViewHolder holder;
//	private List<TableInfoBean> beans_FriendGroupTable;
//	private TableInfoBean bean_FriendGroupTable;
//
//	public FriendGroupTableAdapter(Context context,
//			List<TableInfoBean> beans_FriendGroupTable) {
//		this.context = context;
//		this.beans_FriendGroupTable = beans_FriendGroupTable;
//	}
//
//	@Override
//	public int getCount() {
//		return beans_FriendGroupTable.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return beans_FriendGroupTable.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			holder = new ViewHolder();
//			LayoutInflater inflater = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(
//					R.layout.friendgrouptableform_tablerowform, null);
//			holder.rows = new ArrayList<LinearLayout>();
//			holder.txts = new ArrayList<TextView>();
//			holder.bgs = new ArrayList<LinearLayout>();
//			holder.rows.add((LinearLayout) convertView
//					.findViewById(R.id.friendgrouptableform_tablerowform_row1));
//			holder.rows.add((LinearLayout) convertView
//					.findViewById(R.id.friendgrouptableform_tablerowform_row2));
//			holder.rows.add((LinearLayout) convertView
//					.findViewById(R.id.friendgrouptableform_tablerowform_row3));
//			holder.txts
//					.add((TextView) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row1_txt));
//			holder.txts
//					.add((TextView) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row2_txt));
//			holder.txts
//					.add((TextView) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row3_txt));
//			holder.bgs
//					.add((LinearLayout) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row1_bg));
//			holder.bgs
//					.add((LinearLayout) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row2_bg));
//			holder.bgs
//					.add((LinearLayout) convertView
//							.findViewById(R.id.friendgrouptableform_tablerowform_row3_bg));
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		bean_FriendGroupTable = (TableInfoBean) getItem(position);
//
//		List<FriendGroupBean> beans_FriendGroup = bean_FriendGroupTable
//				.getBeans_FriendGroup();
//		int i = 0;
//		for (; i < beans_FriendGroup.size(); i++) {
//			FriendGroupBean bean_FriendGroup = beans_FriendGroup.get(i);
//
//			holder.txts.get(i).setText(
//					bean_FriendGroup.getGroupName()
//							+ bean_FriendGroup.getFriendNum() + "人");
//			holder.bgs.get(i).setBackgroundResource(
//					new RandomBgUtil().getRandomBgId());
//
//			LinearLayout row = holder.rows.get(i);
//			row.setTag(bean_FriendGroup.getGroupId() + "@"
//					+ bean_FriendGroup.getFriendNum() + "@"
//					+ bean_FriendGroup.getGroupName());
//			row.setVisibility(View.VISIBLE);
//			row.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					String temp[] = v.getTag().toString().split("@");
//					Bundle bundle = new Bundle();
//					bundle.putString("id_Group", temp[0]);
//					bundle.putInt("num_Total", Integer.valueOf(temp[1]));
//					bundle.putString("name_Group", temp[2]);
//					Intent intent = new Intent(context,
//							FriendListByGroupActivity.class);
//					intent.putExtras(bundle);
//					((FriendGroupActivity) context).startActivityForResult(
//							intent, 1003);
//				}
//			});
//		}
//		if (i < 3) {
//			for (int j = i; j < 3; j++) {
//				holder.rows.get(j).setVisibility(View.INVISIBLE);
//			}
//		}
//
//		return convertView;
//	}
//
//	public class ViewHolder {
//
//		public List<TextView> txts;
//		public List<LinearLayout> rows;
//		public List<LinearLayout> bgs;
//	}
//
//}
