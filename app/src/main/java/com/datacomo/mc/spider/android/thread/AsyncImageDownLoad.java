package com.datacomo.mc.spider.android.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Message;

import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;

public class AsyncImageDownLoad extends BasicAsyncImageDownLoad {

	private static final String TAG = "AsynImageDownLoad";
	private static final String TAG_FROM = "URLFROM";
	protected final String mSavePath;

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
	 * @param from
	 *            which call the asyncdownload
	 * @param imageCallback
	 */
	public AsyncImageDownLoad(String url, Object[] tags,
			int[] loadStateImageId, String savePath, Context context,
			String from, ImageCallback imageCallback) {
		super(new String[] { url }, tags, loadStateImageId, context,
				imageCallback);
		mSavePath = savePath;
		L.d(TAG_FROM, "from:" + from + " " + "url:" + mUrls[0]);
	}

	@Override
	protected Object get() {
		Drawable drawable = null;
		// mUrls[0] = null;
		if (null == mUrls[0] || "".equals(mUrls[0])) {
			L.d(TAG, "get mUrls[0]=null");
			drawable = getFailedImage();
		} else {
			L.d(TAG, "get mUrls[0]=" + mUrls[0] + " " + drawableCache.size());
			key = TaskUtil.getKey(mUrls[0]);
			File cacheDir = new File(LogicUtil.getLocalName(mSavePath + key));
			if (drawableCache.containsKey(key)) {
				drawable = selectDrawableCache(cacheDir, mUrls[0], key);
			} else if (cacheDir.exists()) {
				drawable = selectFile(cacheDir, mUrls[0], key);
			} else {
				download(mUrls[0]);
				drawable = getLoadingImage();
			}
		}
		return drawable;
	}

	@Override
	protected Drawable selectDrawableCache(File cacheDir, String url, String key) {
		L.d(TAG, "drawableCache");
		Drawable drawable = null;
		SoftReference<Drawable> reference = null;
		if (drawableCache != null)
			reference = drawableCache.get(key);
		if (reference != null)
			drawable = reference.get();
		if (null == drawable) {
			if (cacheDir != null && cacheDir.exists()) {
				drawable = selectFile(cacheDir, url, key);
				if (drawable == null) {
					L.d(TAG, "drawableCache failed");
					drawable = getFailedImage();
				}
			} else {
				L.d(TAG, "drawableCache down");
				drawable = getLoadingImage();
				download(url);
			}
		}
		return drawable;
	}

	@Override
	protected Drawable selectFile(File cacheDir, String url, String key) {
		L.d(TAG, "drawablefile");
		Drawable drawable = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(cacheDir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fis = null;
		}
		if (fis != null) {
			// if (cacheDir != null) {
			try {
				drawable = new BitmapDrawable(null, fis);
				// drawable = PhotoUtil.getBitmapFromFileByWidth(cacheDir,
				// DensityUtil.dip2px(400));
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fis) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != drawable && drawable.getIntrinsicWidth() > 0
					&& drawable.getIntrinsicHeight() > 0) {
				L.d(TAG, "drawablefile succeed");
				SoftReference<Drawable> reference = new SoftReference<Drawable>(
						drawable);
				drawableCache.put(key, reference);
			} else {
				L.d(TAG, "drawablefile down");
				drawable = getFailedImage();
				// download(url);
			}
		} else {
			L.d(TAG, "drawablefile down");
			drawable = getLoadingImage();
			download(url);
		}
		return drawable;
	}

	@Override
	protected void download(final Object params) {
		L.d(TAG, "loading");
		new Thread() {
			@Override
			public void run() {
				try {
					String url = (String) params;
					Drawable drawable = loadImageFromUrl(url);
					if (null != drawable && drawable.getIntrinsicWidth() > 0
							&& drawable.getIntrinsicHeight() > 0) {
						SoftReference<Drawable> reference = new SoftReference<Drawable>(
								drawable);
						drawableCache.put(key, reference);
					} else {
						drawable = getFailedImage();
					}
					Message message = getHandler().obtainMessage(0, drawable);
					getHandler().sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
					Message message = getHandler().obtainMessage(0,
							getFailedImage());
					getHandler().sendMessage(message);
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					Message message = getHandler().obtainMessage(0,
							getFailedImage());
					getHandler().sendMessage(message);
				}
			}
		}.start();

	}

	/**
	 * insure the SdCard can used;
	 * 
	 * @return true is can used else is not
	 */
	protected boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected Drawable loadImageFromUrl(String url) {
		URL mUrl;
		InputStream is = null;
		try {
			mUrl = new URL(url);
			HttpURLConnection conn = HttpRequestServers.getHttpURLConnection(mUrl);
			conn.connect();
			is = (InputStream) conn.getContent();
			if (mUrl == null || is == null) {
				return null;
			}
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		Drawable drawable = null;
		if (IsCanUseSdCard()) {
			L.d(TAG, "loadfromurl save and load");
			FileUtil.createFile(mSavePath);
			FileOutputStream fos = null;
			FileInputStream fis = null;
			try {
				byte[] buf = new byte[1024];
				int length = 0;
				if ((length = is.read(buf)) > 0) {
					File file = new File(
							LogicUtil.getLocalName(mSavePath + key));
					fos = new FileOutputStream(file);
					do {
						fos.write(buf, 0, length);
					} while ((length = is.read(buf)) != -1);
					fos.flush();
					L.d(TAG, "loadfromurl save and load ok");
					fis = new FileInputStream(file);
					drawable = new BitmapDrawable(null, fis);
					// drawable = PhotoUtil.getBitmapFromFileByWidth(file,
					// DensityUtil.dip2px(400));
				} else {
					return null;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
			} finally {
				try {
					if (null != fis) {
						fis.close();
					}
					if (null != fos) {
						fos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (null == drawable) {
			L.d(TAG, "loadfromurl load1");
			try {
				drawable = Drawable.createFromStream(is, "src");
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
				// e.printStackTrace();
			}
		}
		if (drawable == null) {
			L.d(TAG, "loadfromurl load2");
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 10;
				options.inJustDecodeBounds = false;
				drawable = Drawable.createFromResourceStream(null, null, is,
						"src", options);
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
				// e.printStackTrace();
			}
		}
		return drawable;
	}

}
