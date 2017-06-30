package com.datacomo.mc.spider.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.datacomo.mc.spider.android.url.L;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class PhotoUtil {
	private final static String TAG = "PhotoUtil";

	public static Bitmap compressBmpFromBmp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(CompressFormat.JPEG, options, baos);
		L.d(TAG, "compressBmpFromBmp size=" + baos.toByteArray().length);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			image.compress(CompressFormat.JPEG, options, baos);
		}
		L.i(TAG, "compressBmpFromBmp size=" + baos.toByteArray().length);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	public static int compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(CompressFormat.JPEG, 100, baos);
		L.i(TAG, "compressImage length=" + baos.toByteArray().length);
		int options = 100;
		// 循环判断如果压缩后图片是否大于sizekb,大于继续压缩
		while (baos.toByteArray().length > size * 1024 && options > 0) {
			// 重置baos即清空baos
			baos.reset();
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(CompressFormat.JPEG, options, baos);
			// 每次都减少10
			options -= 10;
		}
		L.i(TAG,
				"compressImage options=" + options + ",length="
						+ baos.toByteArray().length);
		return options;
	}

	/**
	 * 
	 * @param dst
	 * @param flag
	 * @return
	 */
	public static Bitmap getBitmapFromFile(File dst, boolean flag) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(dst.getPath(), opts);

			L.i(TAG, "getBitmapFromFile opts.inSampleSize=" + opts.inSampleSize
					+ ",outWidth=" + opts.outWidth + ",outHeight="
					+ opts.outHeight + ",flag=" + flag);
			opts.inSampleSize = 1;
			if (flag) {
				if (opts.outWidth >= 1600) {
					opts.inSampleSize = opts.outWidth / 800;
				}
			} else if (opts.outHeight >= 1600) {
				opts.inSampleSize = opts.outHeight / 800;
			}
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			L.d(TAG, "getBitmapFromFile opts.inSampleSize=" + opts.inSampleSize
					+ ",outWidth=" + opts.outWidth + ",outHeight="
					+ opts.outHeight);
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		L.d(TAG, "getBitmapFromFile null");
		return null;
	}

	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);

				L.i(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				// opts.inSampleSize = 10;
				L.d(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static File bitmap2File(Bitmap bm, File toFile) {
		if (null == bm || null == toFile) {
			return null;
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(toFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bm.compress(CompressFormat.JPEG, 70, out);
		return toFile;
	}

	public static Bitmap file2Bmp(File dst) {
		if (null != dst && dst.exists()) {
			return BitmapFactory.decodeFile(dst.getPath());
		}
		return null;
	}

	/**
	 * 获取最小大于需求高度的图片
	 * 
	 * @param dst
	 *            原始文件
	 * @param width
	 *            需求的最小宽度
	 * @param dealWithSpicelSize
	 *            是否处理很长的图片，若处理，则宽高减小一倍
	 * @return
	 */
	public static Bitmap getBitmapFromFileByRequstMinWidth(File dst, int width,
			boolean isDealWithSpicelSize) {
		Bitmap b = null;
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);

				L.i(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
				// 计算图片缩放比例
				opts.inSampleSize = 1;
				opts.inSampleSize = getZoomInt(width, opts.outWidth,
						opts.inSampleSize);
				if (isDealWithSpicelSize && opts.outHeight / opts.outWidth >= 8) { // 宽是高的八倍
					opts.inSampleSize = opts.inSampleSize * 3;
				}
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				L.d(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
			}
			try {
				b = BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} finally {
			}
		}
		return b;
	}

	private static int getZoomInt(int minWidth, int OldWidth, int zoom) {
		if (minWidth * (zoom + 1) <= OldWidth) {
			return getZoomInt(minWidth, OldWidth, zoom + 1);
		}
		return zoom;
	}

	public static Bitmap getBitmapFromFileByWidth(File dst, int width) {
		Bitmap b = null;
		int height;
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);

				L.i(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
				// 计算图片缩放比例
				final int minSideLength = width;
				height = (int) (width * (opts.outHeight / (float) opts.outWidth));
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				// opts.inSampleSize = 10;
				L.d(TAG, "getBitmapFromFile opts.inSampleSize="
						+ opts.inSampleSize + ",outWidth=" + opts.outWidth
						+ ",outHeight=" + opts.outHeight);
			}
			try {
				b = BitmapFactory.decodeFile(dst.getPath(), opts);
				return b;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} finally {
			}
		}
		return b;
	}
}
