package com.datacomo.mc.spider.android.view;

import java.util.List;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.adapter.FaceTableAdapter;

public class PopMenu {
	private Context mContext;
	private LayoutInflater inflater;
	// private HashMap<String, Object> mContentMap;
	private PopupWindow mPw;
	private Timer mTimer;
	// private TimerTask mTimerTask;
	private Handler mHandler;
	private int mTime = 0;
	private int mHigh;
	private int mWidth;

	public PopMenu(Context context) {
		mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// mContentMap = new HashMap<String, Object>();
		mPw = new PopupWindow(mContext);
		mHandler = new TimeHandler();
	}

	private LinearLayout CreatMoreContent(List<String> infos, String key,
			OnClickListener listener) {
		LinearLayout.LayoutParams imgp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imgp.gravity = Gravity.RIGHT;
		int pwidth = mWidth / 4;
		int offset = pwidth - (int) (mWidth * 0.03) - (pwidth / 2)
				- (int) (mWidth * 0.04);
		imgp.setMargins(0, 0, offset, 0);
		return CreatContent(infos, key, imgp, listener);
	}

	private LinearLayout CreatShareContent(List<String> infos, String key,
			OnClickListener listener) {
		LinearLayout.LayoutParams imgp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imgp.gravity = Gravity.CENTER_HORIZONTAL;
		return CreatContent(infos, key, imgp, listener);
	}

	private LinearLayout CreatContent(List<String> infos, String key,
			LinearLayout.LayoutParams imgp, OnClickListener listener) {
		LinearLayout father = (LinearLayout) inflater.inflate(
				R.layout.layout_popmenu, null);
		LinearLayout bar = (LinearLayout) father
				.findViewById(R.id.layout_popmenu_menu);
		ImageView img = (ImageView) father
				.findViewById(R.id.layout_popmenu_trigonum);
		img.setLayoutParams(imgp);
		LinearLayout child = null;
		LinearLayout.LayoutParams childp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		childp.weight = 1;
		for (int i = 0; i < infos.size(); i++) {
			child = (LinearLayout) inflater
					.inflate(R.layout.item_popmenu, null);
			TextView txt = (TextView) child
					.findViewById(R.id.item_popmenu_tv_text);
			txt.setText(infos.get(i));
			txt.setTag(i);
			txt.setOnClickListener(listener);
			if (i == infos.size() - 1)
				child.findViewById(R.id.item_popmenu_llayout_line)
						.setVisibility(View.GONE);
			bar.addView(child, childp);
		}
		// mContentMap.put(key, father);
		return father;
	}

	private LinearLayout CreatSmailContent(String info, String key) {
		LinearLayout father = (LinearLayout) inflater.inflate(
				R.layout.layout_smailpopmenu, null);
		LinearLayout bar = (LinearLayout) father
				.findViewById(R.id.layout_popmenu_menu);
		TextView textView = (TextView) bar
				.findViewById(R.id.item_popmenu_tv_text);
		textView.setText(info);
		// mContentMap.put(key, father);
		return bar;
	}

	private GridView CreatGridFace(String key,
			OnItemClickListener onItemClickListener) {
		GridView face = (GridView) inflater.inflate(R.layout.form_face, null);
		face.setAdapter(new FaceTableAdapter(mContext));
		face.setOnItemClickListener(onItemClickListener);
		// mContentMap.put(key, face);
		return face;
	}

	@SuppressWarnings("deprecation")
	public void showMorePop(String key, View anchor, List<String> infos,
			OnClickListener listener) {
		LinearLayout bar = CreatMoreContent(infos, key, listener); // (LinearLayout)
																	// mContentMap.get(key);
		mPw.setContentView(bar);
		int width = (int) (mWidth * 0.52);
		mPw.setWidth(width);
		mPw.setHeight((int) (mHigh * 0.09 * infos.size()));
		mPw.setOutsideTouchable(true);
		mPw.setFocusable(true);
		mPw.setAnimationStyle(R.style.pop_style);
		mPw.setBackgroundDrawable(new BitmapDrawable());
		int pwidth = mWidth / 2;// 4
		int offset = width - pwidth + (int) (mWidth * 0.155);// 0.03
		mPw.showAsDropDown(anchor, -offset, 0);
	}

	// @SuppressWarnings("deprecation")
	// private void showPraisePop(String key, View anchor) {
	// LinearLayout bar = (LinearLayout) mContentMap.get(key);
	// mPw.setContentView(bar);
	// int width = (int) (mWidth * 0.31);
	// mPw.setWidth(width);
	// mPw.setHeight((int) (mHigh * 0.087));
	// mPw.setOutsideTouchable(false);
	// mPw.setFocusable(false);
	// mPw.setAnimationStyle(R.style.pop_style);
	// mPw.setBackgroundDrawable(new BitmapDrawable());
	// int pwidth = mWidth / 4;
	// int offset = (width / 2) - (pwidth / 2);
	// mPw.showAsDropDown(anchor, -offset, 0);
	// mTimer = new Timer();
	// TimerTask mTimerTask = new TimerTask() {
	//
	// @Override
	// public void run() {
	// mTime++;
	// if (mTime >= 1) {
	// mTime = 0;
	// mHandler.sendEmptyMessage(0);
	// }
	// }
	// };
	// mTimer.schedule(mTimerTask, 1000, 1000);
	// }

	@SuppressWarnings("deprecation")
	public void showSharePop(String key, View anchor, List<String> infos,
			OnClickListener listener) {
		LinearLayout bar = CreatShareContent(infos, key, listener); // (LinearLayout)
																	// mContentMap.get(key);
		mPw.setContentView(bar);
		int width = (int) (mWidth * 0.52);
		mPw.setWidth(width);
		mPw.setHeight((int) (mHigh * 0.09 * infos.size()));
		mPw.setOutsideTouchable(true);
		mPw.setFocusable(true);
		mPw.setAnimationStyle(R.style.pop_style);
		mPw.setBackgroundDrawable(new BitmapDrawable());
		int pwidth = mWidth / 4;
		int offset = (width / 2) - (pwidth / 2);
		mPw.showAsDropDown(anchor, -offset, 0);
	}

	@SuppressWarnings("deprecation")
	public void showGridFace(String key, int layoutId,
			OnItemClickListener onItemClickListener) {
		GridView face = CreatGridFace(key, onItemClickListener); // (GridView)
																	// mContentMap.get(key);
		LinearLayout parent = (LinearLayout) inflater.inflate(layoutId, null);
		mPw.setContentView(face);
		mPw.setWidth(LayoutParams.FILL_PARENT);
		mPw.setHeight(LayoutParams.WRAP_CONTENT);
		mPw.setOutsideTouchable(true);
		mPw.setFocusable(true);
		mPw.setAnimationStyle(R.style.pop_style2);
		mPw.setBackgroundDrawable(new BitmapDrawable());
		mPw.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}

	public void popDismiss() {
		if (null != mPw)
			mPw.dismiss();
	}

	// public Object getContent(String key) {
	// return mContentMap.get(key);
	// }
	//
	// public void setContent(String key, Object content) {
	// mContentMap.put(key, content);
	// }

	public int getHigh() {
		return mHigh;
	}

	public void setHigh(int high) {
		mHigh = high;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		mWidth = width;
	}

	@SuppressLint("HandlerLeak")
	private class TimeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mPw.dismiss();
			mTimer.cancel();
		}
	}

}
