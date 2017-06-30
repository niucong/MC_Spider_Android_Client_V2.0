package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.ChatGroupChooseActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class ChatGroupChooseAdapter extends BaseAdapter {
	private static final String TAG = "SharedBlogAdapter";
	private List<Object> beans;
	private Context context;
	private TransitionBean bean_Transition;
	private ViewHolder holder;
	private LayoutInflater inflater;
	private boolean mIsHasDeleteOwn;

	public ChatGroupChooseAdapter(List<Object> beans, Context context) {
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

	public List<Object> getBeans() {
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
		// if (!TransitionBean.getType_IsAllSelect())
		type_Select = ((ChatGroupChooseActivity) context).isSelect(id);
		// else
		// type_Select = ((ChatGroupChooseActivity) context).isUnSelect(id);
		L.d("isSelect", "adapterSelect" + type_Select);
		if (type_Select == 0) {
			holder.check.setImageResource(R.drawable.choice_no);
		} else if (type_Select == 1) {
			holder.check.setImageResource(R.drawable.choice_yes);
		}
		head_path = ThumbnailImageUrl.getThumbnailHeadUrl(head_path,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		L.d(TAG, bean_Transition.getName());
		// 对头像进行异步加载。
		// TODO
//		holder.post.setTag(bean_Transition.getId());
		MyFinalBitmap.setHeader(context, holder.post, head_path);
//		holder.post.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String temp = v.getTag().toString();
//				L.d(TAG, temp);
//				Bundle bundle = new Bundle();
//				bundle.putString("id", temp);
//				bundle.putString("name", bean_Transition.getName());
//				bundle.putInt("type", BaseMidMenuActivity.TYPE_MBER);
//				LogicUtil.enter(context, HomePgActivity.class, bundle, false);
//				Intent intent = new Intent(context, HomePageActivity.class);
//				intent.putExtras(bundle);
//				context.startActivity(intent);
//			}
//		});
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
	public void add(List<Object> temp_Beans) {
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
