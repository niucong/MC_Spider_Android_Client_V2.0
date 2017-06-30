package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class DottedLine extends View {
	private Paint mPaint;
	private Context mContext;
	private int mColor;
	private float mLong, mGap;
	public DottedLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DottedLine(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mPaint = new Paint();
		setLineColor(Color.BLACK);
		setLineLong(12f, 6f);
	}
	
	public void setLineColor(int color){
		mColor = color;
	}
	
	public void setLineLong(float lineLone, float lineGap){
		mLong = lineLone;
		mGap = lineGap;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(2);
		for (int i = 0; i * (mLong + mGap) < getWidth(); i++) {
			canvas.drawLine(i * (mLong + mGap), getHeight() / 2, i * (mLong + mGap) + mLong, getHeight() / 2, mPaint);
		}
	}

}
