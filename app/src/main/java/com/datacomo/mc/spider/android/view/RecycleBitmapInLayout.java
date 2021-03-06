package com.datacomo.mc.spider.android.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.url.L;

/**
 * 工具类 释放布局中所有Imageview组件占用的图片，可设置是否释放背景图 用于退出时释放资源，调用完成后，请不要刷新界面
 * 
 * @author liubing
 * 
 */
public class RecycleBitmapInLayout {
	private static final String TAG = "RecycleBitmapInLayout";
	/* 是否释放背景图 true:释放;false:不释放 */
	private boolean flagWithBackgroud = false;

	/**
	 * 
	 * @param flagWithBackgroud
	 *            是否同时释放背景图
	 */
	public RecycleBitmapInLayout(boolean flagWithBackgroud) {
		this.flagWithBackgroud = flagWithBackgroud;
	}

	/**
	 * 释放Imageview占用的图片资源 用于退出时释放资源，调用完成后，请不要刷新界面
	 * 
	 * @param layout
	 *            需要释放图片的布局 *
	 */
	public void recycle(ViewGroup layout) {

		for (int i = 0; i < layout.getChildCount(); i++) {
			// 获得该布局的所有子布局
			View subView = layout.getChildAt(i);
			// 判断子布局属性，如果还是ViewGroup类型，递归回收
			if (subView instanceof ViewGroup) {
				// 递归回收
				recycle((ViewGroup) subView);
			} else {
				// 是Imageview的子例
				if (subView instanceof ImageView) {
					// 回收占用的Bitmap
					recycleImageViewBitMap((ImageView) subView);
					// 如果flagWithBackgroud为true,则同时回收背景图
					if (flagWithBackgroud) {

						recycleBackgroundBitMap((ImageView) subView);
					}
				}
			}
		}
	}

	private void recycleBackgroundBitMap(ImageView view) {
		if (view != null) {
			BitmapDrawable bd = (BitmapDrawable) view.getBackground();
			rceycleBitmapDrawable(bd);
		}
	}

	private void recycleImageViewBitMap(ImageView imageView) {
		if (imageView != null) {
			BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
			rceycleBitmapDrawable(bd);
		}
	}

	private void rceycleBitmapDrawable(BitmapDrawable bd) {
		if (bd != null) {
			Bitmap bitmap = bd.getBitmap();
			rceycleBitmap(bitmap);
		}
		bd = null;
	}

	private void rceycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			L.d(TAG, "rceycleBitmap");
			bitmap.recycle();
			bitmap = null;
		}
	}

}