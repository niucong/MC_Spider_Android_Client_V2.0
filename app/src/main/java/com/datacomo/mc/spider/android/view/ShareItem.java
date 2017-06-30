package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.application.App;

public class ShareItem extends LinearLayout {
	private Context c;
	private ImageView icon, checkButton;
	private TextView name;
	private boolean isShareble;
	private Resources res;
	private ShareCallBack share;
	public static String ShareInfoName = "ShareInfo";

	public ShareItem(Context context, int drawble, String text,
			ShareCallBack callBack) {
		super(context);
		c = context;
		share = callBack;
		initInfo(drawble, text, callBack);
	}

	public ShareItem(Context context) {
		super(context);
		c = context;
		init();
	}

	private void initInfo(int drawble, String text, ShareCallBack callBack) {
		init();
		setInfo(drawble, text, callBack);
	}

	private void init() {
		res = c.getResources();
		setPadding(10, 5, 10, 5);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		icon = new ImageView(c);
		icon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		name = new TextView(c);
		name.setTextColor(Color.BLACK);
		name.setTextSize(14.0f);
		name.setSingleLine();
		name.setEllipsize(TruncateAt.END);
		name.setPadding(10, 0, 10, 0);
		checkButton = new ImageView(c);
		checkButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		setShareble(false);
		addView(icon, 45, 45);
		addView(name, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		addView(checkButton, 80, 30);
		LayoutParams ll = (LayoutParams) name.getLayoutParams();
		name.setLayoutParams(ll);
		ll.weight = 1;
		name.setLayoutParams(ll);
	}

	public void setInfo(int drawable, String nameStr, ShareCallBack callBack) {
		if (null != nameStr)
			name.setText(nameStr);
		if (0 != drawable)
			icon.setBackgroundDrawable(res.getDrawable(drawable));
		checkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setShareble(!getShareble());
			}
		});
		setShareble(App.app.share.getBooleanMessage(ShareInfoName, nameStr,
				true));
	}

	public String getName() {
		return name.getText().toString();
	}

	public void setShareble(boolean canBeShare) {
		isShareble = canBeShare;
		if (isShareble) {
			checkButton.setBackgroundDrawable(res
					.getDrawable(R.drawable.icon_shareble));
		} else {
			checkButton.setBackgroundDrawable(res
					.getDrawable(R.drawable.icon_unshareble));
		}
		App.app.share.saveBooleanMessage(ShareInfoName, getName(), isShareble);
	}

	public boolean getShareble() {
		return isShareble;
	}

	public void execute() {
		if (null != share && isShareble) {
			share.share();
		}
	}

	interface ShareCallBack {
		void share();
	}

}
