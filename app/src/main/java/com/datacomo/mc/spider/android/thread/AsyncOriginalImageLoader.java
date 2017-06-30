package com.datacomo.mc.spider.android.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.datacomo.mc.spider.android.enums.ImageStateEnum;
import com.datacomo.mc.spider.android.net.HttpRequestServers;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.LogicUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;

public class AsyncOriginalImageLoader extends AsyncImageDownLoad {
	private static final String TAG = "AsyncOriginalImageLoader";
	private ImageStateEnum enum_ImageState;
	private Handler handler_extrinsic;
	private final String CACHESUFFIX_SP = ".sp";

	/**
	 * 
	 * @param urls
	 * @param tags
	 *            one or more tag
	 * @param savePath
	 * @param context
	 * @param handler
	 * @param imageCallback
	 */
	public AsyncOriginalImageLoader(String url, String[] tags, String savePath,
			Context context, Handler handler, ImageCallback imageCallback) {
		super(url, tags, TaskUtil.IMGDEFAULTLOADSTATEIMG, savePath, context,
				"photoGallry", imageCallback);
		handler_extrinsic = handler;
	}

	/**
	 * get result
	 * 
	 * @return 0:drawable;1:image download state;2:filepath
	 */
	public Object[] getResult() {
		Object[] result = (Object[]) get();
		return result;
	}

	/**
	 * 获取图片和图片状态
	 * 
	 * @return
	 */
	@Override
	protected Object get() {
		String filePath = "";
		Drawable drawable = null;
		if (null == mUrls[0] || "".equals(mUrls[0])) {
			L.d(TAG, "mUrls[0]=null");
			drawable = getFailedImage();
		} else {
			L.d(TAG, mUrls[0]);
			key = TaskUtil.getKey(mUrls[0]);
			File imgFile = new File(LogicUtil.getLocalName(mSavePath + key));
			L.d(TAG, imgFile.getAbsolutePath());
			File cacheDir = new File(LogicUtil.getLocalName(mSavePath + key)
					+ CACHESUFFIX_SP);
			filePath = imgFile.getAbsolutePath();
			if (mUrls[0].startsWith("http") && !mUrls[0].endsWith(".gif")) {
				L.d(TAG, "http!gif");
				if (drawableCache.containsKey(key)) {
					drawable = selectDrawableCache(imgFile, mUrls[0], key);
				} else if (cacheDir.exists()) {
					drawable = getLoadingImage();
					download(mUrls[0]);
				} else if (imgFile.exists()) {
					drawable = selectFile(imgFile, mUrls[0], key);
				} else {
					drawable = getLoadingImage();
					download(mUrls[0]);
				}
			} else if (mUrls[0].startsWith("http") && mUrls[0].endsWith(".gif")) {
				L.d(TAG, "http gif");
				imgFile = new File(ConstantUtil.SYSTEM_FILEPATH + key);
				filePath = imgFile.getAbsolutePath();
				if (imgFile.exists()) {
					drawable = getLoadingImage();
					enum_ImageState = ImageStateEnum.LOADED;
					L.d(TAG, "drawablegiffile succeed");
				} else {
					L.d(TAG, "drawablegiffile load");
					drawable = getLoadingImage();
					download(mUrls[0]);
				}
			} else {
				L.d(TAG, "localfile");
				L.d(TAG, "localfile:" + mUrls[0]);
				imgFile = new File(mUrls[0]);
				filePath = imgFile.getAbsolutePath();
				if (drawableCache.containsKey(key)) {
					drawable = selectLocalDrawableCache(imgFile, key);
				} else if (imgFile.exists()) {
					drawable = selectLocalFile(imgFile);
				}
			}
			if (null == drawable) {
				L.d(TAG, "failed");
				drawable = getFailedImage();
			}
		}
		Object[] result = new Object[3];
		result[0] = drawable;
		result[1] = enum_ImageState;
		result[2] = filePath;
		return result;
	}

	@Override
	protected Drawable selectDrawableCache(File cacheDir, String url, String key) {
		L.d(TAG, "drawableCache");
		Drawable drawable = null;
		SoftReference<Drawable> reference = drawableCache.get(key);
		if (reference != null)
			drawable = reference.get();
		if (null == drawable) {
			L.d(TAG, "drawableCache null");
			if (cacheDir != null && cacheDir.exists()) {
				L.d(TAG, "drawableCache selectFile");
				drawable = selectFile(cacheDir, url, key);
				if (drawable == null) {
					L.d(TAG, "drawableCache selectFile failed");
					drawable = getFailedImage();
				}
			} else {
				L.d(TAG, "drawableCache down");
				drawable = getLoadingImage();
				download(url);
			}
		} else {
			L.d(TAG, "drawableCache succeed");
			enum_ImageState = ImageStateEnum.LOADED;
		}
		return drawable;
	}

	@Override
	protected Drawable selectFile(File imgFile, String url, String key) {
		L.d(TAG, "drawablefile");
		Drawable drawable = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(imgFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fileInputStream = null;
		}
		if (fileInputStream != null) {
			try {
				drawable = new BitmapDrawable(null, fileInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				System.gc();
				try {
					drawable = new BitmapDrawable(null, fileInputStream);
				} catch (Exception e1) {
					e1.printStackTrace();
				} catch (OutOfMemoryError e1) {
					e1.printStackTrace();
				}
			}
			if (null != drawable && drawable.getIntrinsicWidth() > 0
					&& drawable.getIntrinsicHeight() > 0) {
				L.d(TAG, "drawablefile succeed");
				SoftReference<Drawable> reference = new SoftReference<Drawable>(
						drawable);
				drawableCache.put(key, reference);
				enum_ImageState = ImageStateEnum.LOADED;
			} else {
				L.d(TAG, "drawablefile down");
				imgFile.delete();
				drawable = getLoadingImage();
				download(url);
			}
		} else {
			L.d(TAG, "drawablefile down");
			drawable = getLoadingImage();
			download(url);
		}
		return drawable;
	}

	/**
	 * select local Drawable cache
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	protected Drawable selectLocalDrawableCache(File cacheDir, String key) {
		L.d(TAG, "drawableLoacalCache");
		Drawable drawable = null;
		SoftReference<Drawable> reference = drawableCache.get(key);
		drawable = reference.get();
		if (null == drawable) {
			L.d(TAG, "drawableLoaclCache null");
			if (cacheDir.exists()) {
				L.d(TAG, "drawableLoacalCache selectLocalFile");
				drawable = selectLocalFile(cacheDir);
				if (drawable == null) {
					L.d(TAG, "drawableLoacalCache selectLocalFile failed");
					drawable = getFailedImage();
				}
			} else {
				L.d(TAG, "drawableLoacalCache failed");
				drawable = getFailedImage();
			}
		} else {
			enum_ImageState = ImageStateEnum.LOADED;
		}
		return drawable;
	}

	/**
	 * select file from local
	 * 
	 * @param imgFile
	 * @return
	 */
	protected Drawable selectLocalFile(File imgFile) {
		L.d(TAG, "drawableLocalfile");
		Drawable drawable = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(imgFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fileInputStream = null;
		}
		if (fileInputStream != null) {
			try {
				drawable = new BitmapDrawable(null, fileInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			if (null != drawable && drawable.getIntrinsicWidth() > 0
					&& drawable.getIntrinsicHeight() > 0) {
				L.d(TAG, "drawableLocalfile succeed");
				SoftReference<Drawable> reference = new SoftReference<Drawable>(
						drawable);
				drawableCache.put(key, reference);
				enum_ImageState = ImageStateEnum.LOADED;
			} else {
				L.d(TAG, "drawableLocalfile failed");
				imgFile.delete();
				drawable = getFailedImage();
			}
		} else {
			L.d(TAG, "drawableLocalfile failed");
			drawable = getFailedImage();
		}
		return drawable;
	}

	@Override
	protected void download(final Object params) {
		L.d(TAG, "loading");
		enum_ImageState = ImageStateEnum.LOADING;
		new Thread() {
			@Override
			public void run() {
				try {
					String url = (String) params;
					Drawable drawable = downLoadImageFromUrl(url);
					if (null != drawable && drawable.getIntrinsicWidth() > 0
							&& drawable.getIntrinsicHeight() > 0) {
						L.d(TAG, "loading succeed");
						Message message = null;
						if (url.endsWith(".gif")) {
							setTags(getTag(0), "1");
							L.d(TAG, "loading succeed gif");
						} else {
							SoftReference<Drawable> reference = new SoftReference<Drawable>(
									drawable);
							drawableCache.put(key, reference);
							setTags(getTag(0), "0");
							L.d(TAG, "loading succeed image");
						}
						message = getHandler().obtainMessage(0, drawable);
						getHandler().sendMessage(message);
					} else {
						L.d(TAG, "loading failed");
						setTags(getTag(0), "2");
						getHandler().sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					L.d(TAG, "loading err");
					setTags(getTag(0), "2");
					getHandler().sendEmptyMessage(0);
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					L.d(TAG, "loading err");
					setTags(getTag(0), "2");
					getHandler().sendEmptyMessage(0);
				}
			}
		}.start();
	}

	/**
	 * Connected to the Internet to download image by specified url
	 * 
	 * @param url
	 *            img's url
	 * @return
	 */
	@SuppressWarnings("resource")
	protected Drawable downLoadImageFromUrl(String url) {
		int currentSize = 0;
		Log.d(TAG, "downLoadImageFromUrl run fileUrl = " + url);
		InputStream fisFileInputStream = null;
		HttpURLConnection hrc = null;
		BufferedInputStream is = null;
		FileOutputStream fos = null;
		File proFile = null;
		Drawable drawable = null;
		try {
			URL mUrl = new URL(url);
			hrc = HttpRequestServers.getHttpURLConnection(mUrl);

			hrc.setRequestProperty("User-Agent", "NetBear");
			hrc.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			hrc.setRequestProperty("connection", "Keep-Alive");
			if (currentSize > 0) {
				hrc.setRequestProperty("RANGE", "bytes = " + currentSize + "-");
			}
			hrc.setConnectTimeout(20 * 1000);
			hrc.setReadTimeout(30 * 1000);
			hrc.connect();

			fisFileInputStream = hrc.getInputStream();
			File myFile = null;
			boolean IsCanUseSdCard = IsCanUseSdCard();
			if (IsCanUseSdCard) {
				String filepath = "";
				String savePath = "";
				if (mUrls[0].startsWith("http") && !mUrls[0].endsWith(".gif")) {
					savePath = mSavePath;
					filepath = LogicUtil.getLocalName(mSavePath
							+ TaskUtil.getKey(url));
				} else if (mUrls[0].startsWith("http")
						&& mUrls[0].endsWith(".gif")) {
					savePath = ConstantUtil.SYSTEM_FILEPATH;
					filepath = savePath + TaskUtil.getKey(url);

				}
				FileUtil.createFile(savePath);
				new FileUtil().createNewFile(filepath);
				proFile = new File(filepath + CACHESUFFIX_SP);
				myFile = new File(filepath);
				fos = new FileOutputStream(proFile);
				int fileLength = hrc.getContentLength();
				if (fileLength <= 0) {
					return null;
				}
				is = new BufferedInputStream(fisFileInputStream);
				int progress = 0;
				int downloadSize = 0;
				int length = 0;
				byte buf[] = new byte[1024];
				while ((length = is.read(buf)) != -1) {
					fos.write(buf, 0, length);
					downloadSize += length;
					int i = (int) (downloadSize * 100 / fileLength);
					L.d(TAG, "downLoadImageFromUrl i=" + i);
					if (i > progress) {
						progress = i;
						sendProgress(progress);
					}
				}
				fos.flush();

				if (proFile != null && proFile.exists()) {
					proFile.renameTo(myFile);
				}
				if (url.endsWith(".gif")) {
					return getLoadingImage();
				} else {
					FileInputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(filepath);
					} catch (FileNotFoundException e) {

					}
					try {
						drawable = new BitmapDrawable(null, fileInputStream);
					} catch (OutOfMemoryError e) {
						L.d(TAG, "OutOfMemoryError:" + e.toString());
						drawable = loadImageFromUrl(url);
					} catch (Exception e) {
						e.printStackTrace();
						drawable = loadImageFromUrl(url);
					}
				}
			}
		} catch (Exception e) {
			if (proFile != null && proFile.exists()) {
				proFile.delete();
			}
			e.printStackTrace();
			L.d(TAG, "download failed");
			drawable = null;
		} finally {
			try {
				if (is != null)
					is.close();
				if (hrc != null)
					hrc.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return drawable;
	}

	/**
	 * 检查sd卡是否存在
	 * 
	 * @return
	 */
	public boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void sendProgress(int percent) {
		Message message = Message.obtain();
		// L.d(TAG, "percent" + " " + percent);
		message.what = 0;
		message.arg1 = percent;
		handler_extrinsic.sendMessage(message);
	}

	@Override
	protected Drawable getLoadingImage() {
		L.d(TAG, "getLoading");
		// enum_ImageType = ImageTypeEnum.DEFAULTIMAGE;
		return super.getLoadingImage();
	}

	@Override
	protected Drawable getFailedImage() {
		L.d(TAG, "getFailed");
		enum_ImageState = ImageStateEnum.FAILED;
		return super.getFailedImage();
	}

	public interface OnProgressChange {
		public void onChaged(int type, int num);
	}

}
