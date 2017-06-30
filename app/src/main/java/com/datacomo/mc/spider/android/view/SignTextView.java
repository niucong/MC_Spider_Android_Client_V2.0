package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class SignTextView extends TextView{
	private ShowHandler h = new ShowHandler();
	private int flag;
	public SignTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SignTextView(Context context) {
		super(context);
	}
	
	public void showSign(String s, long delay){
		if(0 > delay){
			delay = 2000;
		}
		setText(s);
		setVisibility(View.VISIBLE);
		h.postDelayed(null, delay);
		flag++;
	}
	
	class ShowHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			flag--;
			if(flag <= 0){
				flag = 0;
				setVisibility(View.INVISIBLE);
			}
		}
	}

}
