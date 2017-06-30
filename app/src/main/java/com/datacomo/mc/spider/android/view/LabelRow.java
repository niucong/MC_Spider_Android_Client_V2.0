package com.datacomo.mc.spider.android.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;

public class LabelRow extends LinearLayout{
	private Context mContext;
	private ArrayList<TextView> arrLabels;
	private int lastIndex;
	private int colorBase, colorChoise;
	private int mHeight = 65;
	private OnLabelClickListener listener;
	public LabelRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public LabelRow(Context context) {
		super(context);
		init(context);
	}
	
	public LabelRow(Context context, int height) {
		super(context);
		setLabelHeight(height);
		init(context);
	}
	
	private void init(Context c){
		mContext = c;
		mHeight = BaseData.getHeaderHeight((Activity) mContext) - 10;
		colorChoise = mContext.getResources().getColor(R.color.white);
		colorBase = mContext.getResources().getColor(R.color.whitesecond);
		setBackgroundColor(mContext.getResources().getColor(R.color.transblack));
		arrLabels = new ArrayList<TextView>();
	}
	
	/**
	 * 设标签
	 * 根据view.getId() = index;传入监听
	 * @param labels  内容
	 */
	public void setLabels(String[] labels){
		setLabels(labels, false, false);
	}
	
	/**
	 * 设标签
	 * 根据view.getId() = index;传入监听
	 * @param labels  内容
	 * @param leftLine  左边界线
	 * @param rightLine 右边界限
	 */
	public void setLabels(String[] labels, boolean leftLine, boolean rightLine){
		removeAllViews();
		if(leftLine){
			addGap();
		}
		LayoutParams lp = new LayoutParams(0, mHeight);
		lp.weight = 1;
		for (int i = 0; i < labels.length; i++) {
			TextView label = new TextView(mContext);
			label.setSingleLine();
			label.setFocusable(true);
			label.setFocusableInTouchMode(true);
			label.setEllipsize(TruncateAt.MARQUEE);
			label.setMarqueeRepeatLimit(-1);
			label.setTextColor(colorBase);
			label.setTextSize(17);
			label.setGravity(Gravity.CENTER);
			label.setText(labels[i]);
			label.setId(i);
			label.setPadding(0, 5, 0, 8);
			label.setClickable(true);
			label.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						onLableChoise(v.getId());
					}
				}
			});
			addView(label, lp);
			arrLabels.add(label);
			if(i < labels.length - 1 || rightLine){
				addGap();
			}
		}
		setChoise(0);
	}
	
	private void addGap(){
		LayoutParams lpGap = new LayoutParams(2, LayoutParams.MATCH_PARENT);
		lpGap.setMargins(5, 10, 5, 10);
		View gap = new View(mContext);
		gap.setLayoutParams(lpGap);
		gap.setBackgroundColor(mContext.getResources().getColor(R.color.transwhite));
		addView(gap);
	}
	
	public void changeLabel(int which, String labelText){
		((TextView)findViewById(which)).setText(labelText);
	}
	
	public void onLableChoise(int which){
		setChoise(which);
		View v = findViewById(which);
		v.requestFocus();
		if(null != listener){
			listener.onLabelClick(which, v);
		}
	}
	
	private void setChoise(int which){
		unChoise(arrLabels.get(lastIndex));
		doChoise(arrLabels.get(which));
		lastIndex = which;
	}
	
	public int getCurIndex(){
		return lastIndex;
	}
	
	
	private void doChoise(TextView label) {
		label.setTextSize(18);
		label.setTextColor(colorChoise);
//		label.setEnabled(false);
	}
	
	private void unChoise(TextView label) {
		label.setTextSize(17);
		label.setTextColor(colorBase);
//		label.setEnabled(true);
	}
	
	public void setLabelHeight(int height){
		mHeight = height;
	}

	public int getLabelHeight(){
		return mHeight;
	}
	
	public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener){
		this.listener = onLabelClickListener;
	}
	
	public interface OnLabelClickListener{
		void onLabelClick(int index, View view);
	}
}
