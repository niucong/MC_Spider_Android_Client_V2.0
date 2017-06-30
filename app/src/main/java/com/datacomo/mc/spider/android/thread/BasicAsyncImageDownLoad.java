package com.datacomo.mc.spider.android.thread;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.datacomo.mc.spider.android.util.TaskUtil;

public abstract class BasicAsyncImageDownLoad extends AsyncDownLoad {

	@SuppressWarnings("unused")
	private static final String TAG = "AsynImageDownLoad";
	protected final int MLOADINGIMG;
	protected final int MLOADFAILED;
	protected LinkedHashMap<String, SoftReference<Drawable>> drawableCache = TaskUtil.drawableCache;
	protected String key;
	protected String[] mUrls;
	protected Context mContext;

	/**
	 * 
	 * @param urls
	 *            one or more url
	 * @param tags
	 *            one or more tag,if Don't need it can set null
	 * @param loadStateImageId
	 *            two state,first is loading second is failed,you can set same
	 *            or different and you can get default Id of TaskUtil.
	 * @param savePath
	 * @param context
	 * @param imageCallback
	 */
	public BasicAsyncImageDownLoad(String[] urls, Object[] tags,
			int[] loadStateImageId, Context context, ImageCallback imageCallback) {
		super(tags, imageCallback);
		MLOADINGIMG = loadStateImageId[0];
		MLOADFAILED = loadStateImageId[1];
		this.mUrls = urls;
		this.mContext = context;
	}

	/**
	 * get drawable;
	 * 
	 * @return Drawable
	 */
	public Drawable getDrawable() {
		Drawable drawable = (Drawable) get();
		return drawable;
	}

	/**
	 * select drawable cache
	 * 
	 * @param cacheDir
	 *            cache file;
	 * @param url
	 *            img's url
	 * @param key
	 *            cache map's key use to find image from map;
	 * @return Drawable
	 */
	protected abstract Drawable selectDrawableCache(File cacheDir, String url,
			String key);

	/**
	 * select img file
	 * 
	 * @param cacheDir
	 *            cache file;
	 * @param url
	 *            img's url
	 * @param key
	 *            cache map's key use to find image from map;
	 * @return Drawable
	 */
	protected abstract Drawable selectFile(File cacheDir, String url, String key);

	/**
	 * Connected to the Internet to download image by specified url
	 * 
	 * @param url
	 *            img's url
	 * @return Drawable
	 */
	protected abstract Drawable loadImageFromUrl(String url);

	/**
	 * 
	 * @return Drawable
	 */
	protected Drawable getLoadingImage() {
		try {
			return mContext.getResources().getDrawable(MLOADINGIMG);
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
		return null;
	}

	/**
	 * 
	 * @return Drawable
	 */
	protected Drawable getFailedImage() {
		try {
			return mContext.getResources().getDrawable(MLOADFAILED);
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		}
		return null;
	}

	/**
	 * callback of img
	 * 
	 * @author no 287
	 * 
	 */
	public interface ImageCallback extends Callback {
	};
}
