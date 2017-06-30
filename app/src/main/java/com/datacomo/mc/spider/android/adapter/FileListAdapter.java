package com.datacomo.mc.spider.android.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.FileUtil;

public class FileListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<File> fileList = null;

	private ViewHolder mHolder;

	public FileListAdapter(Context context, ArrayList<File> fileList) {
		this.context = context;
		this.fileList = fileList;
	}

	@Override
	public int getCount() {
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.folder_item, null);
			mHolder = new ViewHolder();
			mHolder.icon = (ImageView) convertView
					.findViewById(R.id.folder_item_icon);
			mHolder.name = (TextView) convertView
					.findViewById(R.id.folder_item_name);
			mHolder.size = (TextView) convertView
					.findViewById(R.id.folder_item_size);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setItemView(position);
		return convertView;
	}

	private void setItemView(int position) {
		File file = fileList.get(position);
		if (file == null) {
			mHolder.icon.setImageResource(R.drawable.folder);
			mHolder.name.setText("返回上一级");
			mHolder.size.setVisibility(View.GONE);
		} else {
			Integer resId = null;
			String fileName = file.getName();
			if (file.isDirectory()) {
				mHolder.size.setVisibility(View.GONE);
				resId = R.drawable.folder;
			} else {
				resId = FileUtil.getFileIcon(fileName);
				mHolder.size.setVisibility(View.VISIBLE);
				mHolder.size.setText("大小："
						+ FileUtil.computeFileSize(file.length()));
			}
			mHolder.icon.setImageResource(resId);
			mHolder.name.setText(fileName);
		}
	}

	class ViewHolder {
		TextView name;
		TextView size;
		ImageView icon;
	}
}
