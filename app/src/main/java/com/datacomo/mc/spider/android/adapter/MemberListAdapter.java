package com.datacomo.mc.spider.android.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.BaseMidMenuActivity;
import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.GroupLeaguerManageEnum;
import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.thread.MyFinalBitmap;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.T;
import com.datacomo.mc.spider.android.util.ThumbnailImageUrl;

public class MemberListAdapter extends BaseAdapter {
	private static final String TAG = "MemberListAdapter";
	private Context context;
	private List<Object> beans;
	private ViewHolder holder;

	private LayoutInflater inflater;
	private TransitionBean bean_Transition;
	private Type mType;

	private boolean isReply;

	public MemberListAdapter(Context context, List<Object> beans, Type type) {
		this.beans = beans;
		this.context = context;
		mType = type;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MemberListAdapter(Context context, List<Object> beans, Type type,
			boolean isReply) {
		this.beans = beans;
		this.context = context;
		mType = type;
		this.isReply = isReply;
		inflater = (LayoutInflater) App.app
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_memberlist, null);
			holder.head_img = (ImageView) arg1.findViewById(R.id.head_img);
			holder.name = (TextView) arg1
					.findViewById(R.id.memberlistbyblog_itemform_name);
			holder.time = (TextView) arg1
					.findViewById(R.id.memberlistbyblog_itemform_time);
			if (mType == Type.NOTESHAREGROUP)
				arg1.findViewById(R.id.head_img_bg).setVisibility(View.GONE);
			holder.arrowBox = (LinearLayout) arg1
					.findViewById(R.id.memberlist_item_arrowbox);
			holder.mood = (TextView) arg1
					.findViewById(R.id.memberlistbyblog_itemform_mood);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		setView(arg0, arg1);
		return arg1;
	}

	private void setView(int position, final View convertView) {
		try {
			bean_Transition = new TransitionBean(getItem(position), mType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final String name = bean_Transition.getName();
		holder.name.setText(name);
		L.d(TAG, "setView type:" + mType);
		if (mType == Type.GROUPLEAGUER) {
			Resources res = context.getResources();
			// 成员角色1：圈主 2：管理员 3：申请管理员 4：普通成员 5:申请加入圈子普通成员
			int leaguerStatus = 0;
			try {
				leaguerStatus = bean_Transition.getLeaguerStatus();
			} catch (Exception e) {
				e.printStackTrace();
			}
			L.d(TAG, "setView " + bean_Transition.getName() + ",leaguerStatus="
					+ leaguerStatus);
			Drawable drawableRight_admin = null;
			if (leaguerStatus == 1) {
				drawableRight_admin = res.getDrawable(R.drawable.group_owner);
				holder.mood.setText("（圈主）");
			} else if (leaguerStatus == 2) {
				drawableRight_admin = res.getDrawable(R.drawable.group_admin);
				holder.mood.setText("（管理员）");
			} else {
				drawableRight_admin = res.getDrawable(R.drawable.nothing);
				holder.mood.setText("");
			}
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			drawableRight_admin.setBounds(0, 0,
					drawableRight_admin.getMinimumWidth() / 3,
					drawableRight_admin.getMinimumHeight() / 3);
			holder.mood.setCompoundDrawables(null, null, drawableRight_admin,
					null); // 设置右图标
		}

		String time = bean_Transition.getTime();
		holder.time.setText(time);

		String head_path = bean_Transition.getPath();
		head_path = ThumbnailImageUrl.getThumbnailHeadUrl(head_path,
				HeadSizeEnum.ONE_HUNDRED_AND_TWENTY);
		// 对头像进行异步加载。
		if (mType == Type.NOTESHAREGROUP) {
			MyFinalBitmap.setPosterCorner(context, holder.head_img, head_path);
			holder.head_img.setTag(bean_Transition.getId());
			holder.head_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String temp = v.getTag().toString();
					Bundle b = new Bundle();
					b.putString("Id", temp);
					LogicUtil.enter(context, HomeGpActivity.class, b, false);
				}
			});
		} else {
			MyFinalBitmap.setHeader(context, holder.head_img, head_path);
			holder.head_img.setTag(bean_Transition.getId());
			holder.head_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String temp = v.getTag().toString();
					Bundle b = new Bundle();
					b.putInt("type", BaseMidMenuActivity.TYPE_MBER);
					b.putString("id", temp);
					b.putString("name", name);
					LogicUtil.enter(context, HomePgActivity.class, b, false);
				}
			});
		}
		if (isReply)
			holder.head_img.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Intent i = new Intent();
					i.putExtra("id", v.getTag().toString());
					i.putExtra("name", name);
					((Activity) context).setResult(Activity.RESULT_OK, i);
					((Activity) context).finish();
					return true;
				}
			});
	}

	/**
	 * 
	 * @param temp_Beans
	 */
	public void add(List<Object> temp_Beans) {
		try {
			beans.addAll(temp_Beans);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		beans.clear();
		notifyDataSetChanged();
	}

	public List<Object> getBeans() {
		return beans;
	}

	public class ViewHolder {

		public ImageView head_img, contact;
		public TextView name, comment, time, mood;
		public LinearLayout arrowBox;
	}

	/**
	 * 删除成员
	 */
	public void deleteMember(TransitionBean bean) {
		GroupLeaguerBean groupLeaguerBean = (GroupLeaguerBean) bean.getObject();
		if (null != groupLeaguerBean) {
			beans.remove(groupLeaguerBean);
			notifyDataSetChanged();
		}
		T.show(App.app, bean.getName() + "被移出圈子");
	}

	public void changeLeaguerStatus(GroupLeaguerManageEnum type,
			TransitionBean bean, int position) {
		String show = "";
		switch (type) {
		case APPOINTGROUPMANAGER:// 认命管理
			((GroupLeaguerBean) beans.get(position)).setLeaguerStatus(2);
			notifyDataSetChanged();
			show = bean.getName() + "被任命为管理员";
			break;
		case REVOKEMANAGER:// 撤销管理
			((GroupLeaguerBean) beans.get(position)).setLeaguerStatus(4);
			notifyDataSetChanged();
			show = bean.getName() + "被撤销管理员";
			break;
		default:
			break;
		}
		T.show(context, show);
	}

	public Context getContext() {
		return context;
	}
}
