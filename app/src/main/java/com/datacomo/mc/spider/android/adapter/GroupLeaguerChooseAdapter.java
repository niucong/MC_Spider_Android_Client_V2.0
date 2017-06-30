package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.GroupLeaguerChooseActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class GroupLeaguerChooseAdapter extends BaseAdapter {
	private static final String TAG = "SharedBlogAdapter";
	private List<GroupLeaguerBean> beans;
	private Context context;
	private TransitionBean bean_Transition;
	private ViewHolder holder;
	private LayoutInflater inflater;
	private boolean mIsHasDeleteOwn;

	public GroupLeaguerChooseAdapter(List<GroupLeaguerBean> beans,
			Context context) {
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

	public List<GroupLeaguerBean> getBeans() {
		return beans;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_group_chooser, null);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.post = (ImageView) arg1.findViewById(R.id.head_img);
			holder.check = (ImageView) arg1.findViewById(R.id.check);
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
		int id = bean_Transition.getId();
		String head_path = bean_Transition.getPath();
		int type_Select = -1;
		if (!TransitionBean.isAllSelect())
			type_Select = ((GroupLeaguerChooseActivity) context).isSelect(id);
		else
			type_Select = ((GroupLeaguerChooseActivity) context).isUnSelect(id);
		L.d("isSelect", "adapterSelect" + type_Select);
		if (type_Select == 0) {
			holder.check.setImageResource(R.drawable.choice_no);
		} else if (type_Select == 1) {
			holder.check.setImageResource(R.drawable.choice_yes);
		}
		head_path = ThumbnailImageUrl.getThumbnailHeadUrl(head_path,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		L.d(TAG, bean_Transition.getName());
		MyFinalBitmap.setHeader(context, holder.post, head_path);
		L.d(TAG,
				arg0 + " " + bean_Transition.getName() + " "
						+ bean_Transition.getId());
		return arg1;
	}

	public class ViewHolder {
		public TextView name;
		public ImageView post, check;
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
	public void add(List<GroupLeaguerBean> temp_Beans) {
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
