package com.datacomo.mc.spider.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.FaceUtil;

public class MixedTextView extends TextView {
	private final String TAG = "MixedTextView";

	String delta = "";
	String colon = "： ";

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String id) {
			Drawable drawable = null;
			try {
				drawable = getContext().getResources().getDrawable(
						Integer.valueOf(id));
				int width = drawable.getIntrinsicWidth();
				int height = drawable.getIntrinsicHeight();
				drawable.setBounds(0, 0, width / 2, height / 2);
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
				// e.printStackTrace();
			}
			return drawable;
		}
	};

	public MixedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		delta = " ▶ ";
		// setLineSpacing(0, 1.2f);
		setLinkTextColor(getContext().getResources()
				.getColor(R.color.auto_link));
		// getPaint().setUnderlineText(false);
	}

	public MixedTextView(Context context) {
		super(context);
		delta = " ▶ ";
		// setLineSpacing(0, 1.2f);
		setLinkTextColor(getContext().getResources()
				.getColor(R.color.auto_link));
		// getPaint().setUnderlineText(false);
	}

	public void isLeave(boolean flag) {
		if (flag) {
			delta = " @ ";
		} else {
			delta = " ▶ ";
		}
	}

	/**
	 * 动态墙圈博内容
	 * 
	 * @param name
	 * @param group
	 * @param text
	 */
	public void setOriTextsss(String name, String group, String text) {
		if (null == group || "".equals(group) || "null".equals(group)) {
			group = "";
		}
		if (text == null)
			text = "";
		String ss = name + "<font color='#19a97b'>" + delta + "</font>" + group
				+ colon + FaceUtil.faceToHtml(text.replaceAll(" ", "\t"));

		setText(Html.fromHtml(ss, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 动态墙圈博内容
	 * 
	 * @param name
	 * @param group
	 */
	public void setOriTxtss(String name, String group) {
		if (null == group || "".equals(group) || "null".equals(group)) {
			group = "";
		}

		setText(Html.fromHtml(name + "<font color='#19a97b'>" + delta
				+ "</font>" + group, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 不显示圈子名的动态墙圈博
	 * 
	 * @param name
	 * @param text
	 */
	public void setResTextsss(String name, String text) {
		if (text == null)
			text = "";
		String ss = name + colon
				+ FaceUtil.faceToHtml(text.replaceAll(" ", "\t"));
		setText(Html.fromHtml(ss, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * 不显示圈子名、发布者的动态墙圈博
	 * 
	 * @param name
	 * @param text
	 */
	public void setResTextsss(String text) {
		if (text == null)
			text = "";
		String ss = FaceUtil.faceToHtml(text.replaceAll(" ", "\t"));
		setText(Html.fromHtml(ss, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * xxx @xxx：
	 * 
	 * @param name
	 * @param text
	 */
	public void setResTextPraise(String name, String text) {
		if (text == null)
			text = "";
		String ss = name + " " + FaceUtil.faceToHtml(text);
		setText(Html.fromHtml(ss, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	public void setOtherText(String str, Object... args) {
		setText(String.format(str, args));
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	// 表情字符替换成标签形式。
	public void setFaceText(String str) {
		if (str == null)
			str = "";
		L.d(TAG, "setFaceText str=" + str);
		str = FaceUtil.faceToHtml(str.replaceAll(" ", "\t").replaceAll("\n",
				"<br />"));
		setText(Html.fromHtml(str, imageGetter, null));
		setMovementMethod(LinkMovementMethod.getInstance());
	}
}
