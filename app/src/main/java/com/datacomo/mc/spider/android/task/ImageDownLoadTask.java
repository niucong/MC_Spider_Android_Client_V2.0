package com.datacomo.mc.spider.android.task;
//package com.datacomo.mc.spider.android.task;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.ref.SoftReference;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import android.app.Application;
//import android.content.Context;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//
//import com.datacomo.mc.spider.android.application.App;
//import com.datacomo.mc.spider.android.util.DensityUtil;
//import com.datacomo.mc.spider.android.util.FileUtil;
//import com.datacomo.mc.spider.android.util.L;
//import com.datacomo.mc.spider.android.util.LogicUtil;
//import com.datacomo.mc.spider.android.util.PhotoUtil;
//import com.datacomo.mc.spider.android.util.TaskUtil;
//
//public class ImageDownLoadTask extends BasicImageDownLoadTask {
//	private static final String TAG = "ImageDownLoadTask";
//	protected final String mSavePath;
//
//	/**
//	 * 
//	 * @param context
//	 * @param loadStateImageId
//	 *            two state,first is loading second is failed,you can set same
//	 *            or different and you can get default Id of TaskUtil.
//	 * @param savePath
//	 */
//	public ImageDownLoadTask(Context context, int[] loadStateImageId,
//			String savePath) {
//		super(context, loadStateImageId);
//		mSavePath = savePath;
//	}
//
//	/**
//	 * 
//	 * @param params
//	 *            0: url.1:one or more tag,if Don't need it can set
//	 *            null.2:imageCallback
//	 * @return default Drawable;
//	 */
//	@Override
//	public Drawable start(Object... params) {
//		params[0] = new String[] { (String) params[0] };
//		return super.start(params);
//	}
//
//	@Override
//	protected Object doInBackground(Object... params) {
//		mParams = params;
//		init(mParams);
//		Drawable drawable = null;
//		// mUrls[0] = null;
//		if (null == mUrls[0] || "".equals(mUrls[0])) {
//			L.d(TAG, "doInBackground mUrls[0]=null");
//			drawable = getFailedImage();
//		} else {
//			L.d(TAG,
//					"doInBackground mUrls[0]=" + mUrls[0] + ""
//							+ drawableCache.size());
//			key = TaskUtil.getKey(mUrls[0]);
//			File cacheDir = new File(LogicUtil.getLocalName(mSavePath + key));
//			if (drawableCache.containsKey(key)) {
//				drawable = selectDrawableCache(cacheDir, mUrls[0], key);
//			} else if (cacheDir.exists()) {
//				drawable = selectFile(cacheDir, mUrls[0], key);
//			} else {
//				drawable = getLoadingImage();
//				drawable = download(mUrls[0]);
//			}
//			if (null == drawable) {
//				drawable = getFailedImage();
//			}
//		}
//		return drawable;
//	}
//
//	@Override
//	protected Drawable selectDrawableCache(File cacheDir, String url, String key) {
//		L.d(TAG, "drawableCache");
//		Drawable drawable = null;
//		SoftReference<Drawable> reference = drawableCache.get(key);
//		drawable = reference.get();
//		if (null == drawable) {
//			if (cacheDir.exists()) {
//				drawable = selectFile(cacheDir, url, key);
//				if (drawable == null) {
//					L.d(TAG, "drawableCache failed");
//					drawable = getFailedImage();
//				}
//			} else {
//				L.d(TAG, "drawableCache down");
//				drawable = download(url);
//			}
//		}
//		return drawable;
//	}
//
//	/**
//	 * 
//	 * @param cacheDir
//	 *            缓存文件路径
//	 * @return
//	 */
//	@Override
//	protected Drawable selectFile(File cacheDir, String url, String key) {
//		L.d(TAG, "selectFile");
//		Drawable drawable = null;
//		FileInputStream fileInputStream = null;
//		try {
//			fileInputStream = new FileInputStream(cacheDir);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			fileInputStream = null;
//		}
//		if (fileInputStream != null) {
////		if (cacheDir != null) {
//			try {
////				drawable = PhotoUtil.getBitmapFromFileByWidth(cacheDir, 400);
//				drawable = new BitmapDrawable(null, fileInputStream);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} catch (OutOfMemoryError e) {
//				e.printStackTrace();
//			}
//			if (null != drawable && drawable.getIntrinsicWidth() > 0
//					&& drawable.getIntrinsicHeight() > 0) {
//				L.d(TAG, "selectFile succeed");
//				SoftReference<Drawable> reference = new SoftReference<Drawable>(
//						drawable);
//				drawableCache.put(key, reference);
//			} else {
//				L.d(TAG, "selectFile failed");
//				// drawable = download(url);
//				drawable = getFailedImage();
//			}
//		} else {
//			L.d(TAG, "selectFile down");
//			drawable = download(url);
//		}
//		return drawable;
//	}
//	
//	@Override
//	protected Drawable download(Object params) {
//		Drawable drawable = null;
//		try {
//			String url = (String) params;
//			drawable = loadImageFromUrl(url);
//			if (null != drawable && drawable.getIntrinsicWidth() > 0
//					&& drawable.getIntrinsicHeight() > 0) {
//				SoftReference<Drawable> reference = new SoftReference<Drawable>(
//						drawable);
//				drawableCache.put(key, reference);
//			} else {
//				drawable = getFailedImage();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			drawable = getFailedImage();
//		} catch (OutOfMemoryError e) {
//			e.printStackTrace();
//			drawable = getFailedImage();
//		}
//		return drawable;
//	}
//
//	/**
//	 * insure the SdCard can used;
//	 * 
//	 * @return true is can used else is not
//	 */
//	protected boolean IsCanUseSdCard() {
//		try {
//			return Environment.getExternalStorageState().equals(
//					Environment.MEDIA_MOUNTED);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	protected Drawable loadImageFromUrl(String url) {
//		URL mUrl;
//		InputStream is = null;
//		try {
//			mUrl = new URL(url);
//			HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
//			conn.connect();
//			App.saveTextData(url.length());
//			L.i(TAG,
//					"loadImageFromUrl ContentLength=" + conn.getContentLength());
//			App.saveFileData(conn.getContentLength());
//			is = conn.getInputStream();
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Drawable drawable = null;
//		if (null == is)
//			return null;
//		if (IsCanUseSdCard()) {
//			L.d(TAG, "loadfromurl save and load");
//			FileUtil.createFile(mSavePath);
//			FileOutputStream fos = null;
//			FileInputStream fis = null;
//			try {
//				byte[] buf = new byte[1024];
//				int length = 0;
//				if ((length = is.read(buf)) > 0) {
//					File file = new File(
//							LogicUtil.getLocalName(mSavePath + key));
//					fos = new FileOutputStream(file);
//					do {
//						fos.write(buf, 0, length);
//					} while ((length = is.read(buf)) != -1);
//					fos.flush();
//					L.d(TAG, "loadfromurl save and load ok");
//					fis = new FileInputStream(file);
//					drawable = new BitmapDrawable(null, fis);
////					drawable = PhotoUtil.getBitmapFromFileByWidth(file, DensityUtil.dip2px(400));
//				} else {
//					return null;
//				}
//			} catch (Exception e) {
//				L.d(TAG, "err1" + e.toString());
//				e.printStackTrace();
//			} catch (OutOfMemoryError e) {
//				L.d(TAG, "err2" + e.toString());
//			} finally {
//				try {
//					if (null != fis) {
//						fis.close();
//					}
//					if (null != fos) {
//						fos.close();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		if (null == drawable) {
//			L.d(TAG, "loadfromurl load1");
//			try {
//				drawable = Drawable.createFromStream(is, "src");
//			} catch (Exception e) {
//				e.printStackTrace();
//			} catch (OutOfMemoryError e) {
//				e.printStackTrace();
//			}
//		}
//		if (drawable == null) {
//			L.d(TAG, "loadfromurl load2");
//			try {
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 10;
//				options.inJustDecodeBounds = false;
//				drawable = Drawable.createFromResourceStream(null, null, is,
//						"src", options);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} catch (OutOfMemoryError e) {
//				e.printStackTrace();
//			}
//		}
//		return drawable;
//	}
//
//}
