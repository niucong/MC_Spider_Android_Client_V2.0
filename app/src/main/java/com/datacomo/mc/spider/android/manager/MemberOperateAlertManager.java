package com.datacomo.mc.spider.android.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.bean.TransitionBean;
import com.datacomo.mc.spider.android.enums.Type;

public class MemberOperateAlertManager {
	// private static final String TAG_LOG = "LeaguerManager";
	private Context mContext;
	private Object[] mParams;
	private HashMap<String, Integer> mMap;
	private String[] mDatas;
	private final int GROUPMANAGER = 2;
	private final int GROUPLeaguer = 4;
	private Animation[] mAnims;

	public MemberOperateAlertManager(Context context) {
		mContext = context;
	}

	/**
	 * 
	 * @param params
	 *            0:type 0 is groupOwner, 1 is friends, -1 is member, 2 is
	 *            manager; 1:TransitionBean; 2:ImageView; 3:anims the specific
	 *            anim will be use in arrow rotate; 4:OnItemClickListener
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showAlertDialog(Object... params) {
		mParams = params;
		mAnims = (Animation[]) mParams[3];
		Object[] init = init((Type) mParams[0], (TransitionBean) mParams[1]);
		mMap = (HashMap<String, Integer>) init[0];
		mDatas = (String[]) init[1];
		((ImageView) mParams[2]).startAnimation(mAnims[0]);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle(((TransitionBean) mParams[1]).getName())
				// .setItems(mDatas, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(mContext, R.layout.choice_item, mDatas),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((ImageView) mParams[2])
										.startAnimation(mAnims[1]);
								((OnItemClickListener) mParams[4]).onItemClick(
										dialog, mMap.get(mDatas[which]));
							}
						}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						((ImageView) mParams[2]).startAnimation(mAnims[1]);
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	public Object[] init(Type type, TransitionBean bean_Transition) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<String> datalist = new ArrayList<String>();
		String item = "";
		String tel = "";
		switch (type) {
		case OWNER:
			item = "进TA主页";
			datalist.add(item);
			map.put(item, 3);
			// item = "取消";
			// datalist.add(item);
			// map.put(item, 4);
			break;
		case GROUPMANAGER:
		case GROUPOWNER:
			tel = bean_Transition.getPhone();
			if (null != tel && !"".equals(tel)) {
				item = "拨打";
				datalist.add(item);
				map.put(item, 0);
			}
			item = "打招呼";
			datalist.add(item);
			map.put(item, 1);
			item = "发私信";
			datalist.add(item);
			map.put(item, 2);
			item = "进TA主页";
			datalist.add(item);
			map.put(item, 3);
			// item = "加入朋友圈";
			// datalist.add(item);
			// map.put(item,5);
			int leaguerStatus = bean_Transition.getLeaguerStatus();
			switch (leaguerStatus) {
			case GROUPMANAGER:
				if (type == Type.GROUPOWNER) {
					item = "撤销管理员";
					datalist.add(item);
					map.put(item, 6);
				}
				break;
			case GROUPLeaguer:
				if (type == Type.GROUPOWNER) {
					item = "任命管理员";
					datalist.add(item);
					map.put(item, 7);
				}
				item = "踢出圈子";
				datalist.add(item);
				map.put(item, 8);
				break;
			}
			// item = "取消";
			// datalist.add(item);
			// map.put(item, 4);
			break;
		case FRIEND:
			tel = bean_Transition.getPhone();
			if (null != tel && !"".equals(tel)) {
				item = "拨打";
				datalist.add(item);
				map.put(item, 0);
			}
			item = "打招呼";
			datalist.add(item);
			map.put(item, 1);
			item = "发私信";
			datalist.add(item);
			map.put(item, 2);
			item = "进TA主页";
			datalist.add(item);
			map.put(item, 3);
			item = "向TA引荐朋友";
			datalist.add(item);
			map.put(item, 5);
			item = "删除朋友";
			datalist.add(item);
			map.put(item, 6);
			// item = "取消";
			// datalist.add(item);
			// map.put(item, 4);
			break;
		default:
			tel = bean_Transition.getPhone();
			if (null != tel && !"".equals(tel)) {
				item = "拨打";
				datalist.add(item);
				map.put(item, 0);
			}
			item = "打招呼";
			datalist.add(item);
			map.put(item, 1);
			item = "发私信";
			datalist.add(item);
			map.put(item, 2);
			item = "进TA主页";
			datalist.add(item);
			map.put(item, 3);
			// item = "加入朋友圈";
			// datalist.add(item);
			// map.put(item,5);
			// item = "取消";
			// datalist.add(item);
			// map.put(item, 4);
			break;
		}
		String[] data = datalist.toArray(new String[0]);
		return new Object[] { map, data };
	}

	public interface OnItemClickListener {
		public void onItemClick(DialogInterface dialog, int which);
	};

}
