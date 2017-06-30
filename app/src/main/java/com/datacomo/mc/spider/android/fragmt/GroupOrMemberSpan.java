package com.datacomo.mc.spider.android.fragmt;

import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.datacomo.mc.spider.android.HomeGpActivity;
import com.datacomo.mc.spider.android.HomePgActivity;
import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.util.LogicUtil;

public class GroupOrMemberSpan extends ClickableSpan {

	private Context mContext;
	private String mSpan;

	public GroupOrMemberSpan(Context context, String span) {
		mContext = context;
		mSpan = span;
	}

	@Override
	public void onClick(View widget) {
		// 链接被点击 这里可以做一些自己定义的操作
		if (mSpan != null && mSpan.contains("#")) {
			String[] typeId = mSpan.split("#");
			Bundle b = new Bundle();
			b.putString("To", typeId[0]);
			if ("MemberId".equals(typeId[0])) {
				b.putString("id", typeId[1]);
				LogicUtil.enter(mContext, HomePgActivity.class, b, false);
			} else {
				b.putString("Id", typeId[1]);
				LogicUtil.enter(mContext, HomeGpActivity.class, b, false);
			}
		}
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		super.updateDrawState(tp);
		// 设置超链接字体颜色
		tp.setColor(mContext.getResources().getColor(R.color.auto_link));
		// 设置取消超链接下划线
		tp.setUnderlineText(false);
	}
}