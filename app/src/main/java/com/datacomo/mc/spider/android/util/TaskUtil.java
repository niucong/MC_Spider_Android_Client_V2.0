package com.datacomo.mc.spider.android.util;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.drawable.Drawable;

import com.datacomo.mc.spider.android.R;

public class TaskUtil {
	/**
	 * 图片缓存
	 */
	public static LinkedHashMap<String, SoftReference<Drawable>> drawableCache = new LinkedHashMap<String, SoftReference<Drawable>>(
			1, 0.75f, true) {
		private static final long serialVersionUID = 6040103833179403725L;

		@Override
		protected boolean removeEldestEntry(
				Entry<String, SoftReference<Drawable>> eldest) {
			if (size() > 1) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 默认图片下载状态图片
	 */
	public final static int[] IMGDEFAULTLOADSTATEIMG = new int[] {
			R.drawable.icon_image_loading, R.drawable.icon_image_fail };

	/**
	 * 默认图片下载状态图片
	 */
	public final static int[] IMGDEFAULTLOADSTATEIMG2 = new int[] {
			R.drawable.icon_image_loading, R.drawable.icon_image_fail2 };

	/**
	 * 默认头像下载状态图片
	 */
	public final static int[] HEADDEFAULTLOADSTATEIMG = new int[] {
			R.drawable.icon_head_loading, R.drawable.icon_head_fail };
	/**
	 * 默认海报下载状态图片
	 */
	public final static int[] POSTERDEFAULTLOADSTATEIMG = new int[] {
			R.drawable.icon_poster_loading, R.drawable.icon_poster_loading };

	public final static int[] TRANSPARENTDEFAULTLOADSTATEIMG = new int[] {
			R.drawable.nothing, R.drawable.nothing };

	/**
	 * 截取索取要的key
	 * 
	 * @param url
	 *            要截取的URL
	 * @return 截取好的key
	 */
	public static String getKey(String url) {
		String key = url.substring(url.lastIndexOf("/") + 1);
		return key;
	}
}
