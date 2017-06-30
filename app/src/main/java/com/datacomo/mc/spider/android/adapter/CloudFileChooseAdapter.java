package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.CloudFileChooseActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.url.L;

public class CloudFileChooseAdapter extends BaseAdapter {
	private static final String TAG = "SharedBlogAdapter";
	private List<FileInfoBean> beans;
	private Context context;
	private TransitionBean bean_Transition;
	private ViewHolder holder;
	private LayoutInflater inflater;
	private boolean mIsHasDeleteOwn;

	
	public CloudFileChooseAdapter(List<FileInfoBean> beans, Context context) {
		this.beans = beans;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	public int size() {
		if (mIsHasDeleteOwn)
			return beans.size() + 1;
		else
			return beans.size();
	}

	public void setIsHasDeleteOwn(boolean isHasDeleteOwn) {
		mIsHasDeleteOwn = isHasDeleteOwn;
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public List<FileInfoBean> getBeans() {
		return beans;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_file_chooser, null);
			holder.name = (TextView) arg1
					.findViewById(R.id.item_file_chooser_tv_name);
			holder.icon = (ImageView) arg1
					.findViewById(R.id.item_file_chooser_iv_icon);
			holder.check = (ImageView) arg1
					.findViewById(R.id.item_file_chooser_iv_check);
			holder.size = (TextView) arg1
					.findViewById(R.id.item_file_chooser_tv_size);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		try {
			bean_Transition = new TransitionBean(getItem(arg0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.name.setText(bean_Transition.getName());
		holder.icon.setImageResource(bean_Transition.getFileIcon());
		holder.size.setText(bean_Transition.getFileSize());
		int id = bean_Transition.getId();
		int type_Select = ((CloudFileChooseActivity) context).isSelect(id);
		if (type_Select == -1) {
			holder.check.setImageResource(R.drawable.choice_no);
		} else {
			holder.check.setImageResource(R.drawable.choice_yes);
		}
		L.d(TAG, bean_Transition.getName());
		return arg1;
	}

	public class ViewHolder {
		public TextView name;
		public TextView size;
		public ImageView icon;
		public ImageView check;
	}

	public List<?> getTransitionBean() {
		return beans;
	}

	/**
	 * 
	 * @param temp_Beans
	 * @param location
	 *            the index at which to insert when location is zero
	 * @param isRefresh
	 */
	public void add(List<FileInfoBean> temp_Beans) {
		try {
			beans.addAll(temp_Beans);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param temp_Beans
	 * @param location
	 *            the index at which to insert when location is zero
	 * @param isRefresh
	 */
	public void clear() {
		try {
			beans.clear();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
