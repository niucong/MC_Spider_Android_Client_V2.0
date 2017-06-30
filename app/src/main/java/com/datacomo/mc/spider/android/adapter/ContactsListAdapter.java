package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.ContactInfo;
import com.datacomo.mc.spider.android.url.L;

public class ContactsListAdapter extends BaseAdapter {
	private static final String TAG = "ContactsListAdapter";

	private ArrayList<ContactInfo> contactInfos = null;

	private LayoutInflater inflater;
	private ViewHolder mHolder;

	public HashMap<Integer, Boolean> map = null;

	public ContactsListAdapter(Context context,
			ArrayList<ContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
		inflater = LayoutInflater.from(context);

		map = new HashMap<Integer, Boolean>();
		for (int i = 0; i < contactInfos.size(); i++) {
			map.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return contactInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return contactInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.contacts_item, null);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) convertView
					.findViewById(R.id.contacts_item_name);
			mHolder.number = (TextView) convertView
					.findViewById(R.id.contacts_item_number);
			mHolder.cb = (CheckBox) convertView
					.findViewById(R.id.contacts_item_checkBox);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setItemView(position);
		return convertView;
	}

	private void setItemView(final int position) {
		final ContactInfo contactInfo = contactInfos.get(position);
		mHolder.name.setText(contactInfo.getName());
		mHolder.number.setText(contactInfo.getNumber());

		// 根据HashMap中的当前行的状态布尔值，设定该CheckBox是否选中
		mHolder.cb.setChecked(map.get(position));
	}

	/**
	 * 获得所选号码个数
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int getCheckedSize() {
		Iterator iter = map.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			boolean val = (Boolean) entry.getValue();
			if (val) {
				++i;
			}
		}
		L.d(TAG, "getCheckedSize i=" + i);
		return i;
	}

	/**
	 * 获得所选号码
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getNumbers() {
		String strs = "";
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			int key = (Integer) entry.getKey();
			boolean val = (Boolean) entry.getValue();
			if (val) {
				strs += contactInfos.get(key).getNumber() + ";";
			}
		}
		return strs;
	}

	class ViewHolder {
		TextView name;
		TextView number;
		CheckBox cb;
	}
}
