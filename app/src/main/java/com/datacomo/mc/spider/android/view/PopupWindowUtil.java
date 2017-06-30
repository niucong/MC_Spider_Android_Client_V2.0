package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.BaseData;
import com.datacomo.mc.spider.android.util.DensityUtil;

public class PopupWindowUtil {

	@SuppressWarnings("deprecation")
	public static PopupWindow showSimpleItemMenu(Context c, final View v, View ancherView, View content){
			int arrawHeight = DensityUtil.dip2px(c, 10);
			LinearLayout linearLayout = new LinearLayout(c);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			int[] location = new int[2];
			v.getLocationOnScreen(location);
			int[] locationOut = new int[2];
			ancherView.getLocationOnScreen(locationOut);
			int itemHeight = ancherView.getMeasuredHeight();
			boolean showUp;
			PopupWindow ppw = new PopupWindow(linearLayout, BaseData.getScreenWidth(),
					LayoutParams.WRAP_CONTENT);
			content.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.bg_menu_simple_0));
			if (locationOut[1] >= BaseData.getScreenHeight() - itemHeight * 2 - arrawHeight) { // 向上
				showUp = true;
				View th = new View(c);
				th.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.arraw_black_down));
				LayoutParams ll = new LayoutParams(arrawHeight, arrawHeight);
				ll.leftMargin = location[0] + (v.getMeasuredWidth() - arrawHeight) / 2;
				linearLayout.addView(th, ll);
				linearLayout.addView(content, 0, new LayoutParams(LayoutParams.MATCH_PARENT, itemHeight + 5));
			} else {
				showUp = false;
				View th = new View(c);
				th.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.arraw_black_up));
				LayoutParams ll = new LayoutParams(arrawHeight, arrawHeight);
				ll.leftMargin = location[0] + (v.getMeasuredWidth() - arrawHeight) / 2;
				linearLayout.addView(th, ll);
				linearLayout.addView(content, new LayoutParams(LayoutParams.MATCH_PARENT, itemHeight + 5));
			}
			ppw.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.nothing));
			ppw.setOutsideTouchable(true);
			ppw.setFocusable(true);
			ppw.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					((ImageView) v).setImageResource(R.drawable.arrow_right_0);
				}
			});
			if (showUp) { // 向上
				ppw.setAnimationStyle(R.style.scale_menu_from_right_bottom);
				ppw.showAtLocation(ancherView, Gravity.NO_GRAVITY, locationOut[0], locationOut[1]
						- itemHeight - arrawHeight / 2 - 1);  //有问题
			} else {
				ppw.setAnimationStyle(R.style.scale_menu_from_right_top);
				ppw.showAtLocation(ancherView, Gravity.NO_GRAVITY, locationOut[0], locationOut[1]
						+ itemHeight - arrawHeight);
			}
			return ppw;
	}
}
