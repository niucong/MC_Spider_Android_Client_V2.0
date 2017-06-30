package com.datacomo.mc.spider.android.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.datacomo.mc.spider.android.url.L;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class ImageDealUtil {

	/**
	 * 处理圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {

		}
		return bitmap;
	}

	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (null == drawable) {
			L.d("ImageDealUtil", "drawableToBitmap: drawable=null");
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight(), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmap);
			// canvas.setBitmap(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// e.printStackTrace();
		}

		return bitmap;

	}

	/**
	 * Bitmap转Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Bitmap转为Drawable 据测个别图片转过去会变小，若变小用此方法
	 * 
	 * @param bitmap
	 * @return
	 */
	// public static Drawable bitmapToRealDrawable(Bitmap bitmap) {
	// return new BitmapDrawable(zoomBitmap(bitmap, bitmap.getWidth() * 3 / 2,
	// bitmap.getHeight() * 3 / 2));
	// }

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// /**
	// * 获得圆角图片
	// *
	// * @param drawable
	// * @return
	// */
	// public static Drawable getPosterCorner(Drawable drawable, int radius) {
	// try {
	// Bitmap bitmap = drawableToBitmap(drawable);
	// if (-1 == radius) {
	// drawable = bitmapToDrawable(toRoundCorner(bitmap,
	// bitmap.getHeight() + bitmap.getWidth()));
	// } else {
	// if (bitmap != null)
	// drawable = bitmapToDrawable(toRoundCorner(bitmap, radius));
	// }
	// if (bitmap != null && !bitmap.isRecycled()) {
	// bitmap = null;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	//
	// }
	// return drawable;
	// }

	/**
	 * 
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		if (bmp == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得ImageView的bitmap
	 * 
	 * @return
	 */
	public static Bitmap getBitMap(ImageView shareImg) {
		if (shareImg == null)
			return null;
		try {
			return drawableToBitmap(shareImg.getDrawable());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
	 * 
	 * @param bitmap
	 * @param paramBoolean
	 * @return
	 */
	public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
		if (bitmap == null)
			return new byte[] {};
		Bitmap localBitmap = Bitmap.createBitmap(80, 80, Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		int i;
		int j;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			i = bitmap.getWidth();
			j = bitmap.getWidth();
		} else {
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
		while (true) {
			localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
					80, 80), null);
			if (paramBoolean)
				bitmap.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
				e.printStackTrace();
			}
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
	}

	/**
	 * 获取sp转dp图片
	 * 
	 * @param context
	 * @param aid
	 * @return
	 */
	public static Drawable getPxToDpDrawable(Context context, int aid) {
		Drawable drawable = context.getResources().getDrawable(aid);
		int dw = drawable.getIntrinsicWidth();
		DisplayMetrics display = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(display);
		drawable.setBounds(0, 0, (int) (dw / display.density),
				(int) (dw / display.density));
		return drawable;
	}

	public static float getSizeByDisplay(Context context, int size) {
		DisplayMetrics display = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(display);
		return size / display.density;
	}

	public static void releaseImageDrawable(ImageView view, boolean isResycle) {
		try {
			Drawable d = (BitmapDrawable) view.getDrawable();
			if (null != d) {
				d.setCallback(null);
			}
			if (d instanceof BitmapDrawable && isResycle) {
				((BitmapDrawable) d).getBitmap().recycle();
			}
			view.setImageDrawable(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void releaseImageBackground(View view, boolean isResycle) {
		try {
			Drawable d = (BitmapDrawable) view.getBackground();
			if (null != d) {
				d.setCallback(null);
			}
			if (d instanceof BitmapDrawable && isResycle) {
				((BitmapDrawable) d).getBitmap().recycle();
			}
			view.setBackgroundDrawable(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void releaseImageDrawables(ArrayList<ImageView> views,
			boolean isResycle) {
		if (null != views)
			for (ImageView view : views) {
				releaseImageDrawable(view, isResycle);
			}
	}

}
