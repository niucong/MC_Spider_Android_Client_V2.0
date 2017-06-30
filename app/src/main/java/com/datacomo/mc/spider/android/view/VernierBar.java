package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;
public class VernierBar extends LinearLayout{
	private Context mContext;
	private int count = 1;
	private int gap;
	private ImageView v;
	private int w = 8;
	private int last;
	private Scroller mScroller;
	public VernierBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public VernierBar(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context c){
		mContext = c;
		v = new ImageView(mContext);
		v.setBackgroundResource(R.drawable.vernier);
		addView(v, new LayoutParams(2 * w, 2 * w));
//		initBar(1);
		mScroller = new Scroller(c);
	}
	
	public void initBar(int sum){
		if(0 == sum){
			return;
		}
		count = sum;
		gap = BaseData.getScreenWidth() / count;
		mScroller.startScroll(0, 0, -gap / 2 + w, 0, 0);
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}
	
	/**
	 * 设游动点标签
	 * @param index  游动到第几个位置
	 */
	public void to(final int index){
		if(index > count -1){
			return;
		}
		mScroller.startScroll(getScrollX(), 0, (int)(- getScrollX() - (index + 0.5) * gap + w), 0, 1500);
		invalidate();
		last = index;
	}
	
	public int getIndex(){
		return last;
	}
	
}
