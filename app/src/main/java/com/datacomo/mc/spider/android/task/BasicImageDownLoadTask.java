package com.datacomo.mc.spider.android.task;
//package com.datacomo.mc.spider.android.task;
//
//import java.io.File;
//import java.lang.ref.SoftReference;
//import java.util.LinkedHashMap;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.widget.ImageView;
//
//import com.datacomo.mc.spider.android.util.TaskUtil;
//
//public abstract class BasicImageDownLoadTask extends
//		AsyncTask<Object, Integer, Object> {
//	private static final String TAG = "ImageDownLoadNoSaveTask";
//	protected final int MLOADINGIMG;
//	protected final int MLOADFAILED;
//	private Context mContext;
//	protected Object[] mParams;
//	private Object[] mTags;
//	protected String[] mUrls;
//	protected LinkedHashMap<String, SoftReference<Drawable>> drawableCache = TaskUtil.drawableCache;
//	protected String key;
//	protected ImageCallback mImageCallback;
//
//	/**
//	 * 
//	 * @param context
//	 * @param loadStateImageId
//	 *            two state,first is loading second is failed,you can set same
//	 *            or different and you can get default Id of TaskUtil.
//	 */
//	public BasicImageDownLoadTask(Context context, int[] loadStateImageId) {
//		mContext = context;
//		MLOADINGIMG = loadStateImageId[0];
//		MLOADFAILED = loadStateImageId[1];
//	}
//
//	/**
//	 * 
//	 * @param params
//	 *            0: one or more url.1:one or more tag,if Don't need it can set
//	 *            null.2:imageCallback
//	 * @return default drawable;
//	 */
//	public Drawable start(Object... params) {
//		execute(params);
//		return getLoadingImage();
//	}
//
//	@Override
//	protected void onPostExecute(Object result) {
//		super.onPostExecute(result);
//		if (null != mImageCallback)
//			mImageCallback.load(result, mTags);
//		else if (null != (ImageView) mParams[3])
//			((ImageView) mParams[3]).setImageDrawable((Drawable) result);
//	}
//
//	protected void init(Object... params) {
//		mUrls = (String[]) params[0];
//		mTags = (String[]) params[1];
//		mImageCallback = (ImageCallback) params[2];
//	}
//
//	/**
//	 * select drawable cache
//	 * 
//	 * @param cacheDir
//	 *            cache file;
//	 * @param url
//	 *            img's url
//	 * @param key
//	 *            cache map's key use to find image from map;
//	 * @return
//	 */
//	protected abstract Drawable selectDrawableCache(File cacheDir, String url,
//			String key);
//
//	/**
//	 * select img file
//	 * 
//	 * @param cacheDir
//	 *            cache file;
//	 * @param url
//	 *            img's url
//	 * @param key
//	 *            cache map's key use to find image from map;
//	 * @return
//	 */
//	protected abstract Drawable selectFile(File cacheDir, String url, String key);
//
//	/**
//	 * download
//	 * 
//	 * @param params
//	 * @return Drawable
//	 */
//	protected abstract Drawable download(Object params);
//
//	/**
//	 * Connected to the Internet to download image by specified url
//	 * 
//	 * @param url
//	 *            img's url
//	 * @return
//	 */
//	protected abstract Drawable loadImageFromUrl(String url);
//
//	protected Drawable getLoadingImage() {
//		return mContext.getResources().getDrawable(MLOADINGIMG);
//	}
//
//	/**
//	 * 
//	 * @return Drawable
//	 */
//	protected Drawable getFailedImage() {
//		return null;
////		return mContext.getResources().getDrawable(MLOADFAILED);
//	}
//
//	/**
//	 * 
//	 * @return String[]
//	 */
//	public Object[] getAllTags() {
//		return mTags;
//	}
//
//	/**
//	 * 
//	 * @param tags
//	 *            String[]
//	 */
//	public void setAllTags(Object[] tags) {
//		this.mTags = tags;
//	}
//
//	/**
//	 * 
//	 * @param index
//	 * @return String
//	 */
//	public Object getTag(int index) {
//		return mTags[index];
//	}
//
//	/**
//	 * 
//	 * @param tags
//	 */
//	public void setTags(Object... tags) {
//		this.mTags = tags;
//	}
//
//	/**
//	 * callbakc of img
//	 * 
//	 * @author no 287
//	 * 
//	 */
//	public interface ImageCallback {
//		public void load(Object object, Object[] tags);
//	};
//}
