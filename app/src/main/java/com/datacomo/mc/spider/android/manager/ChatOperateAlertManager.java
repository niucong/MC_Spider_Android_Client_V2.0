package com.datacomo.mc.spider.android.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.datacomo.mc.spider.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.ArrayAdapter;

public class ChatOperateAlertManager {
	// private static final String TAG_LOG = "LeaguerManager";
	private Context mContext;
	private Object[] mParams;
	private HashMap<String, Integer> mMap;
	private String[] mDatas;

	public ChatOperateAlertManager(Context context) {
		mContext = context;
	}

	/**
	 * 
	 * 
	 * @param params
	 *            0:type 0 is text 1 is image 2 is audio 3 is video
	 *            ,1:OnItemClickListener
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void showAlertDialog(Object... params) {
		mParams = params;
		Object[] init = Init((Integer) mParams[0]);
		mMap = (HashMap<String, Integer>) init[0];
		mDatas = (String[]) init[1];
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
		// .setTitle("更多")
		// .setItems(mDatas, new DialogInterface.OnClickListener() {
				.setAdapter(
						new ArrayAdapter(mContext, R.layout.choice_item, mDatas),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((OnItemClickListener) mParams[1]).onItemClick(
										dialog, mMap.get(mDatas[which]));
							}
						}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

	public Object[] Init(int type) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<String> datalist = new ArrayList<String>();
		String item = "";
		if (type == 0) {
			item = "复制";
			datalist.add(item);
			map.put(item, 0);
		}
		item = "转发到朋友";
		datalist.add(item);
		map.put(item, 1);
		item = "转发到圈聊";
		datalist.add(item);
		map.put(item, 2);
		item = "转发到交流圈";
		datalist.add(item);
		map.put(item, 6);
		item = "分享到微信";
		datalist.add(item);
		map.put(item, 3);
		item = "删除";
		datalist.add(item);
		map.put(item, 4);
		// item = "取消";
		// datalist.add(item);
		// map.put(item, 5);
		String[] data = datalist.toArray(new String[0]);
		return new Object[] { map, data };
	}

	public interface OnItemClickListener {
		public void onItemClick(DialogInterface dialog, int which);
	};

}
