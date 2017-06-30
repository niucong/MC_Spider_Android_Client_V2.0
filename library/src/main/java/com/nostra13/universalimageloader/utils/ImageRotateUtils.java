package com.nostra13.universalimageloader.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageRotateUtils {
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {

		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);

			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	public static Bitmap rotatingImage(Bitmap bitmap, int rotate) {
		if (bitmap == null) {
			return null;
		}
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.setRotate(rotate);
		Bitmap rotateBitmap = null;
		try {
			rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
			return bitmap;
		}
		bitmap.recycle();
		bitmap = null;
		return rotateBitmap;
	}
}
