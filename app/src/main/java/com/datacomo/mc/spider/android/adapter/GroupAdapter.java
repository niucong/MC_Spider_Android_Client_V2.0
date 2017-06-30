package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CreateGroupTopicActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;

public class GroupAdapter extends BaseAdapter {
	private static final String TAG = "GroupAdapter";

	private Context context;
	private ArrayList<GroupEntity> mGroupInfos = null;
	private int size = 0;

	private LayoutInflater inflater;
	private ViewHolder mHolder;

	private ArrayList<GroupEntity> managerList;
	private ArrayList<GroupEntity> joinList;
	private int managerNum = 0;
	private int joinNum = 0;
	private int nearNum = 5;

	private ArrayList<String> mGroupIdList;
	private ListView listView;

	public GroupAdapter(Context context, ArrayList<String> mGroupIdList,
			ListView listView) {
		// this.context = context;
		// this.mGroupIdList = mGroupIdList;
		// this.listView = listView;
		//
		// inflater = LayoutInflater.from(context);
		// mGroupInfos = new ArrayList<GroupEntity>();
		// size =
		// CreateGroupTopicActivity.createGroupTopicActivity.groupEntityList
		// .size();
		// L.i(TAG, "GroupAdapter size=" + size);
		// if (size < nearNum) {
		// nearNum = size;
		// }
		// setTitleItem(context, "最近使用的圈子", nearNum);
		// for (int i = 0; i < nearNum; i++) {
		// mGroupInfos
		// .add(CreateGroupTopicActivity.createGroupTopicActivity.groupEntityList
		// .get(i));
		// }
		//
		// if (size > nearNum) {
		// managerList();
		// L.i(TAG, "GroupAdapter managerNum=" + managerNum);
		// if (managerNum > 0) {
		// setTitleItem(context, "我管理的圈子", managerNum);
		// mGroupInfos.addAll(managerList);
		// }
		// if (size != (nearNum + managerNum)) {
		// joinList();
		// L.i(TAG, "GroupAdapter joinNum=" + joinNum);
		// if (joinNum > 0) {
		// setTitleItem(context, "我加入的圈子", joinNum);
		// mGroupInfos.addAll(joinList);
		// }
		// }
		// }
	}

	public ArrayList<GroupEntity> getMGroupInfos() {
		return mGroupInfos;
	}

	private void setTitleItem(Context context, String name, int num) {
		mGroupInfos.add(new GroupEntity("", name, "" + num, "", true, true));
	}

	/**
	 * 获取管理的的圈子列表
	 */
	private void managerList() {
		// managerList = new ArrayList<GroupEntity>();
		// for (int i = nearNum; i < size; i++) {
		// GroupEntity groupEntity =
		// CreateGroupTopicActivity.createGroupTopicActivity.groupEntityList
		// .get(i);
		// if (groupEntity.getType()) {
		// managerNum++;
		// managerList.add(groupEntity);
		// }
		// }
	}

	/**
	 * 获取加入的圈子列表
	 */
	private void joinList() {
		// joinList = new ArrayList<GroupEntity>();
		// for (int i = nearNum; i < size; i++) {
		// GroupEntity groupEntity =
		// CreateGroupTopicActivity.createGroupTopicActivity.groupEntityList
		// .get(i);
		// if (!groupEntity.getType()) {
		// joinNum++;
		// joinList.add(groupEntity);
		// }
		// }
	}

	@Override
	public int getCount() {
		return mGroupInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mGroupInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.groups_item, null);
			mHolder = new ViewHolder();
			mHolder.type_iv = (ImageView) convertView
					.findViewById(R.id.groups_item_type);
			mHolder.name = (TextView) convertView
					.findViewById(R.id.groups_item_name);
			mHolder.select_iv = (ImageView) convertView
					.findViewById(R.id.groups_item_select);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setItemView(position, convertView);
		return convertView;
	}

	/**
	 * 
	 * @param position
	 * @param convertView
	 */
	private void setItemView(final int position, View convertView) {
		L.d(TAG, "setItemView position=" + position + ",Count=" + getCount());
		final GroupEntity groupEntity = mGroupInfos.get(position);
		if (groupEntity.getIsTitle()) {
			convertView.setPadding(10, 0, 10, 0);
			convertView.setFocusable(true);
			convertView.setBackgroundResource(R.drawable.item_title);
			mHolder.type_iv.setVisibility(View.GONE);
			mHolder.select_iv.setVisibility(View.GONE);
			mHolder.name.setText(groupEntity.getName());
			mHolder.name.setTextColor(context.getResources().getColor(
					R.color.white));
		} else {
			convertView.setPadding(15, 10, 15, 10);
			convertView.setFocusable(false);
			convertView.setBackgroundResource(R.drawable.item_bg);
			mHolder.type_iv.setVisibility(View.VISIBLE);

			if (groupEntity.getType()) {
				// mHolder.type_iv.setImageResource(R.drawable.group_manager);
				loadHead(groupEntity.getThumbnailHead(),
						R.drawable.group_manager);
			} else {
				// mHolder.type_iv.setImageResource(R.drawable.group_join);
				loadHead(groupEntity.getThumbnailHead(), R.drawable.group_join);
			}
			mHolder.name.setText(groupEntity.getName());
			mHolder.name.setTextColor(context.getResources().getColor(
					R.color.black));
			// 根据HashMap中的当前行的状态布尔值，设定该CheckBox是否选中
			if (mGroupIdList.contains(groupEntity.getId())) {
				L.i(TAG, "setItemView VISIBLE...");
				mHolder.select_iv.setVisibility(View.VISIBLE);
				// mHolder.select_iv.setImageResource(R.drawable.choice_selected);
			} else {
				L.i(TAG, "setItemView GONE...");
				mHolder.select_iv.setVisibility(View.GONE);
				// mHolder.select_iv.setImageResource(R.drawable.choice);
			}
		}
	}

	private void loadHead(String imgUrl, final int srcId) {
		mHolder.type_iv.setTag(imgUrl);
		// mHolder.type_iv.setImageDrawable(new AsyncImageLoader(imgUrl,
		// context,
		// srcId, ConstantUtil.HEAD_PATH, new ImageCallback() {
		//
		// @Override
		// public void imageLoaded(呢我);
		// mHolder.type_iv.setImageDrawable(new AsyncImageLoader(imgUrl, new
		// String()[]{imgUrl},
		// srcId, ConstantUtil.HEAD_PATH, new ImageCallback() {
		//
		// @Override
		// public void imageLoaded(Drawable imageDrawable,
		// String imageUrl) {
		// ImageView imageViewByTag = (ImageView) listView
		// .findViewWithTag(imageUrl);
		// if (imageViewByTag != null) {
		// if (null != imageDrawable) {
		// imageViewByTag.setImageDrawable(imageDrawable);
		// } else {
		// imageViewByTag.setImageResource(srcId);
		// }
		// }
		// }
		// }).loadDrawable());
		// mHolder.type_iv.setImageDrawable(new AsyncImageDownLoad(imgUrl,
		// new String[] { imgUrl }, new int[]{srcId,srcId},
		// ConstantUtil.POSTER_PATH, context,"groupadapter", new ImageCallback()
		// {
		//
		// @Override
		// public void load(Object object, Object[] tags) {
		// ImageView imageViewByTag = (ImageView) listView
		// .findViewWithTag(tags[0]);
		// if (imageViewByTag != null) {
		// Drawable imageDrawable = (Drawable) object;
		// if (null != imageDrawable) {
		// imageViewByTag.setImageDrawable(imageDrawable);
		// } else {
		// imageViewByTag.setImageResource(srcId);
		// }
		// }
		// }
		// }).getDrawable());
		MyFinalBitmap.setPoster(context, mHolder.type_iv, imgUrl);
	}

	class ViewHolder {
		ImageView type_iv;
		TextView name;
		ImageView select_iv;
	}
}
