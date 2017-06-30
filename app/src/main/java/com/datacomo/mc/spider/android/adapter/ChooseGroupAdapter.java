package com.datacomo.mc.spider.android.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.ChooseGroupsDialogActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.FriendGroupBean;

public class ChooseGroupAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	private HashMap<String, Object> mMap_ChosenGroup;
	private List<Object> mGroups;
	private ViewHolder mHolder;
	private FriendGroupBean mBean;

	public ChooseGroupAdapter(Context context, List<Object> groups) {
		mContext = context;
		mGroups = groups;
		mMap_ChosenGroup = new HashMap<String, Object>();
		inflater = LayoutInflater.from(mContext);
	}

	public void setChosen(HashMap<String, Object> map_ChosenGroup) {
		if (null != map_ChosenGroup) {
			mMap_ChosenGroup = map_ChosenGroup;
		}
	}

	public HashMap<String, Object> getChosen() {
		return mMap_ChosenGroup;
	}

	public String[] getChosenIds() {
		return mMap_ChosenGroup.keySet().toArray(new String[0]);
	}

	@Override
	public int getCount() {
		return mGroups.size();
	}

	@Override
	public Object getItem(int position) {
		return mGroups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView || convertView instanceof TextView) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.form_choosegroups, null);
			mHolder.name = (TextView) convertView
					.findViewById(R.id.form_choosegroups_txt_name);
			mHolder.choose = (ImageView) convertView
					.findViewById(R.id.form_choosegroups_img_check);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mBean = (FriendGroupBean) getItem(position);
		mHolder.name.setText(mBean.getGroupName() + "(" + mBean.getFriendNum()
				+ ")");
		if (mMap_ChosenGroup.containsKey(String.valueOf(mBean.getGroupId())))
			mHolder.choose.setImageResource(R.drawable.icon_checked);
		else
			mHolder.choose.setImageResource(R.drawable.icon_unchecked);
		return convertView;
	}

	public void chooseChage(FriendGroupBean bean, View convertView, int index) {
		String id = String.valueOf(bean.getGroupId());
		int initFriendNum = bean.getFriendNum();
		if (mMap_ChosenGroup.containsKey(id)) {
			mMap_ChosenGroup.remove(id);
			initFriendNum--;
			((FriendGroupBean) mGroups.get(index)).setFriendNum(initFriendNum);
			((ImageView) convertView
					.findViewById(R.id.form_choosegroups_img_check))
					.setImageResource(R.drawable.icon_unchecked);
			if (ChooseGroupsDialogActivity.choose.containsKey(index)) {
				ChooseGroupsDialogActivity.choose.put(index,
						ChooseGroupsDialogActivity.choose.get(index) + 1);
			} else {
				ChooseGroupsDialogActivity.choose.put(index, 1);
			}
		} else {
			initFriendNum++;
			((FriendGroupBean) mGroups.get(index)).setFriendNum(initFriendNum);
			mMap_ChosenGroup.put(id, bean);
			((ImageView) convertView
					.findViewById(R.id.form_choosegroups_img_check))
					.setImageResource(R.drawable.icon_checked);
			if (ChooseGroupsDialogActivity.choose.containsKey(index)) {
				ChooseGroupsDialogActivity.choose.put(index,
						ChooseGroupsDialogActivity.choose.get(index) - 1);
			} else {
				ChooseGroupsDialogActivity.choose.put(index, -1);
			}
		}
		notifyDataSetChanged();
	}

	public List<Object> refresh(FriendGroupBean bean) {
		mGroups.add(0, bean);
		notifyDataSetChanged();
		return mGroups;
	}

	public List<Object> refreshAll(List<Object> tempGroups) {
		mGroups.removeAll(mGroups);
		mGroups.addAll(tempGroups);
		notifyDataSetChanged();
		return mGroups;
	}

	class ViewHolder {
		TextView name;
		ImageView choose;
	}

}
