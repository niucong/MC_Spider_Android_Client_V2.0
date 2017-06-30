package com.datacomo.mc.spider.android.util;

import java.io.File;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;

public class ViewUtil {
	private static String TAG = "ViewUtil";

	public static void setImgHeight(ImageView img, String img_url, int maxWidth) {
		File dst = new File(ConstantUtil.IMAGE_PATH
				+ img_url.substring(img_url.lastIndexOf("/") + 1,
						img_url.lastIndexOf(".")));
		if (dst.exists()) {
			BitmapFactory.Options opts = null;
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(dst.getPath(), opts);
			L.i(TAG, "  name:" + dst.getName()
					+ "  getBitmapFromFile opts.inSampleSize="
					+ opts.inSampleSize + ",outWidth=" + opts.outWidth
					+ ",outHeight=" + opts.outHeight + "     def " + maxWidth);
			float rotal = opts.outHeight / (float) opts.outWidth;
			if (opts.outWidth > maxWidth) {
				img.setLayoutParams(new LinearLayout.LayoutParams(maxWidth,
						(int) (maxWidth * rotal)));
			} else {
				img.setLayoutParams(new LinearLayout.LayoutParams(
						opts.outWidth, opts.outHeight));
			}
			img.requestLayout();
		}
	}

	public static void setImgHeight(ImageView img, File dst, int maxWidth) {
		if (dst.exists()) {
			BitmapFactory.Options opts = null;
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(dst.getPath(), opts);
			L.i(TAG, "  name:" + dst.getName()
					+ "  getBitmapFromFile opts.inSampleSize="
					+ opts.inSampleSize + ",outWidth=" + opts.outWidth
					+ ",outHeight=" + opts.outHeight + "     def " + maxWidth);
			float rotal = opts.outHeight / (float) opts.outWidth;
			if (opts.outWidth > maxWidth) {
				img.setLayoutParams(new LinearLayout.LayoutParams(maxWidth,
						(int) (maxWidth * rotal)));
			} else {
				img.setLayoutParams(new LinearLayout.LayoutParams(
						opts.outWidth, opts.outHeight));
			}
			img.requestLayout();
		}
	}

	public static void appadingIconWithText(final Context context,
			TextView textView, int resId, final String text, final double zoomX, final double zoomY, boolean iconEnd) {
		int index = 0;
		if(iconEnd){
			index = text.length() - 1;
		}
		appadingIconWithText(context, textView, resId, text, zoomX, zoomY, index);
	}
	
	public static void appadingIconWithText(final Context context,
			TextView textView, int resId, final String text, final double zoomX, final double zoomY, int iconIndex) {
		ImageGetter imageGetter = new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				int id = Integer.parseInt(source);

				// 根据id从资源文件中获取图片对象
				Drawable d = context.getResources().getDrawable(id);
				d.setBounds(0, 0, (int)(d.getIntrinsicWidth() * zoomX), (int)(d.getIntrinsicHeight() * zoomY));
				return d;
			}
		};
		
		textView.setText(Html.fromHtml(text.substring(0, iconIndex) + "<img src='"
				+ resId + "'/>" + text.substring(iconIndex, text.length()), imageGetter, null));
	}
}
