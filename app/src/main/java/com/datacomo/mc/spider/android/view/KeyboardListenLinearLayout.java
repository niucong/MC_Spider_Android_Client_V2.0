package com.datacomo.mc.spider.android.view;

import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.BaseData;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class KeyboardListenLinearLayout extends LinearLayout {

	private static final String TAG = "KeyboardListenLinearLayout";

	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;

	private boolean mHasInit = false;
	private boolean mHasKeyboard = false;
	private int mHeight;
	private OnKeyboardStateChangedListener onKeyboardStateChangedListener;

	public KeyboardListenLinearLayout(Context context) {
		super(context);
	}

	public KeyboardListenLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public void setOnKeyboardStateChangedListener(
			OnKeyboardStateChangedListener onKeyboardStateChangedListener) {
		this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		L.d(TAG, "onLayout changed："+changed+" l:"+l+" t:"+t+" r:"+r+" b:"+b+" ScreenHeight:"+BaseData.getScreenHeight());
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (onKeyboardStateChangedListener != null) {
				onKeyboardStateChangedListener
						.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
			}
		}else if(changed){
			if(b<(mHeight*0.6)){
				if (onKeyboardStateChangedListener != null) {
					onKeyboardStateChangedListener
							.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
				}
			}else if(b==mHeight){
				if (onKeyboardStateChangedListener != null) {
					onKeyboardStateChangedListener
							.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
				}
			}
		}

	}

	public interface OnKeyboardStateChangedListener {
		public void onKeyboardStateChanged(int state);
	}
}
