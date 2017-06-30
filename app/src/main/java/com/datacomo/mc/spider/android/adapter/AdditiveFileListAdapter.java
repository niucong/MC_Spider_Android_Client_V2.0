package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.url.L;

public class AdditiveFileListAdapter extends BaseAdapter {
	private final String TAG = "AdditiveFileListAdapter";

	private final int MEDIA = 0;
	private final int OTHERWISE = 1;
	private boolean mIsInit = true;
	private List<Object> mFiles;
	private List<Object> mCloudFile;
	private List<Object> mAdditiveFile;
	private LayoutInflater inflater;

	private Handler hideFile;

	// private Context mContext;

	public AdditiveFileListAdapter(Context context, List<Object> files) {
		mFiles = files;
		// mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setHideFile(Handler hideFile) {
		this.hideFile = hideFile;
	}

	@Override
	public int getCount() {
		return mFiles.size();
	}

	@Override
	public Object getItem(int position) {
		return mFiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public boolean isInit() {
		if (mIsInit) {
			mIsInit = false;
			return true;
		}
		return mIsInit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			int type = getItemViewType(position);
			convertView = inflater.inflate(R.layout.item_media, null);
			viewHolder.tv_Name = (TextView) convertView
					.findViewById(R.id.item_media_tv_name);
			viewHolder.tv_Size = (TextView) convertView
					.findViewById(R.id.item_media_tv_size);
			viewHolder.tv_Time = (TextView) convertView
					.findViewById(R.id.item_media_tv_time);
			viewHolder.iv_Icon = (ImageView) convertView
					.findViewById(R.id.item_media_iv_icon);
			viewHolder.iv_Delete = (ImageView) convertView
					.findViewById(R.id.item_media_iv_delete);
			switch (type) {
			case MEDIA:
				viewHolder.tv_Time.setVisibility(View.VISIBLE);
				break;
			case OTHERWISE:
				viewHolder.tv_Time.setVisibility(View.GONE);
				break;
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TransitionBean bean = null;
		try {
			bean = new TransitionBean(getItem(position));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		viewHolder.iv_Icon.setImageResource(bean.getFileIcon());
		if (null != viewHolder.tv_Time) {
			switch (bean.getType()) {
			case CLOUDFILE:
				viewHolder.tv_Time.setText("00:00");
				break;
			case ADDITIVEFILE:
				viewHolder.tv_Time.setText(bean.getTime());
				break;
			default:
				break;
			}
		}
		viewHolder.tv_Name.setText(bean.getName());
		viewHolder.tv_Size.setText(bean.getFileSize());
		viewHolder.iv_Delete.setTag(position);
		if (bean.getType() == Type.QUUBOFILE)
			viewHolder.iv_Delete.setVisibility(View.GONE);
		viewHolder.iv_Delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = Integer.parseInt(v.getTag().toString());
				try {
					deleteDialog(position);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		int icon = 0;
		try {
			icon = new TransitionBean(mFiles.get(position)).getFileIcon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (icon == R.drawable.file_video || icon == R.drawable.file_music)
			return MEDIA;
		else
			return OTHERWISE;

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	// public boolean isInit() {
	// if (mIsInit) {
	// mIsInit = false;
	// return true;
	// }
	// return mIsInit;
	// }

	public List<Object> getBeans() {
		return mFiles;
	}

	public List<Object> getCloudFile(Boolean flush) throws Exception {
		if (flush) {
			if (null == mCloudFile) {
				mCloudFile = new ArrayList<Object>();
			}
			mCloudFile.clear();
			for (Object file : mFiles) {
				if (new TransitionBean(file).getType() == Type.CLOUDFILE)
					mCloudFile.add(file);
			}
		} else {
			if (null == mCloudFile) {
				mCloudFile = new ArrayList<Object>();
				for (Object file : mFiles) {
					if (new TransitionBean(file).getType() == Type.CLOUDFILE)
						mCloudFile.add(file);
				}
			}
		}
		return mCloudFile;
	}

	public List<Object> getAdditiveFile(Boolean flush) throws Exception {
		if (flush) {
			if (null == mAdditiveFile) {
				mAdditiveFile = new ArrayList<Object>();
			}
			mAdditiveFile.clear();
			for (Object file : mFiles) {
				if (new TransitionBean(file).getType() == Type.ADDITIVEFILE)
					mAdditiveFile.add(file);
			}
		} else {
			if (null == mAdditiveFile) {
				mAdditiveFile = new ArrayList<Object>();
				for (Object file : mFiles) {
					if (new TransitionBean(file).getType() == Type.ADDITIVEFILE)
						mAdditiveFile.add(file);
				}
			}
		}
		return mAdditiveFile;
	}

	public void classify() throws Exception {
		if (null == mCloudFile) {
			mCloudFile = new ArrayList<Object>();
		}
		if (null == mAdditiveFile) {
			mAdditiveFile = new ArrayList<Object>();
		}
		mCloudFile.clear();
		mAdditiveFile.clear();
		for (Object file : mFiles) {
			switch (new TransitionBean(file).getType()) {
			case CLOUDFILE:
				mCloudFile.add(file);
				break;
			case ADDITIVEFILE:
				mAdditiveFile.add(file);
				break;
			default:
				break;

			}
		}

	}

	public void flush(List<Object> beans) {
		L.d(TAG, "flush...");
		mFiles.clear();
		mFiles.addAll(beans);
		notifyDataSetChanged();
	}

	public void addAtFirst(Object file) {
		L.d(TAG, "addAtFirst...");
		mFiles.add(0, file);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		L.d(TAG, "remove...");
		mFiles.remove(position);
		notifyDataSetChanged();
		if (mFiles.size() == 0 && hideFile != null) {
			hideFile.sendEmptyMessage(0);
		}
	}

	public int contains(Object key) {
		int index = -1;
		for (Object file : mFiles) {
			index++;
			TransitionBean bean = null;
			try {
				bean = new TransitionBean(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Type type = bean.getType();
			switch (type) {
			case CLOUDFILE:
				if (key instanceof Integer) {
					if (bean.getId() == (Integer) key)
						return index;
				}
				break;
			case ADDITIVEFILE:
				if (key instanceof String) {
					if (bean.getPath().equals((String) key))
						return index;
				}
				break;
			default:
				break;
			}
		}
		return -1;
	}

	/**
	 * 删除对话框
	 * 
	 * @param i
	 * @param str
	 * @param view
	 */
	private void deleteDialog(final int position) {
		// TransitionBean bean = null;
		// try {
		// bean = new TransitionBean(mFiles.get(position));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// String filename = bean.getName();
		// String msg = StringUtil.merge("是否删除附件", "“", filename, "”？");
		// new AlertDialog.Builder(mContext).setTitle("提示").setMessage(msg)
		// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		remove(position);
		// }
		// }).setNegativeButton("取消", null).show();
	}

	class ViewHolder {
		TextView tv_Name;
		TextView tv_Size;
		TextView tv_Time;
		ImageView iv_Icon;
		ImageView iv_Delete;
	}

}
