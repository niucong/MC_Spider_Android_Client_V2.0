package com.datacomo.mc.spider.android.thread;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.widget.ImageView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.PhotoUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;
import com.datacomo.mc.spider.android.util.ThumbnailImgUtil;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.LoadAndDisplay;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyFinalBitmap {
	private static final String TAG = "MyFinalBitmap";

	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions HEADER;
	private static DisplayImageOptions POSTER;
	private static DisplayImageOptions POSTERCORNER;
	private static DisplayImageOptions IMAGE;
	private static DisplayImageOptions SKIN;
	private static DisplayImageOptions DETAILIMAGE;
	private static DisplayImageOptions LOCAL;
	private static DisplayImageOptions options;

	public static void setHeader(Context context, ImageView iv, String url) {
		try {
			if (null == HEADER) {
				HEADER = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.HEADDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.HEADDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.HEADDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setPoster(Context context, ImageView iv, String url) {
		try {
			if (null == POSTER) {
				POSTER = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, POSTER);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setPosterCorner(Context context, ImageView iv, String url) {
		try {
			if (null == POSTERCORNER) {
				float scale = context.getResources().getDisplayMetrics().density;
				int s = (int) (35 * scale + 0.5f);
				POSTERCORNER = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.displayer(new RoundedBitmapDisplayer(s)) // 图片圆角显示，值为整数
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, POSTERCORNER);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setPosterCorner(Context context, ImageView iv,
			String url, int roundPixels, SimpleImageLoadingListener listener) {
		try {
			if (null == POSTERCORNER) {
				float scale = context.getResources().getDisplayMetrics().density;
				int s = Math.round(35 * scale);
				POSTERCORNER = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.displayer(new RoundedBitmapDisplayer(s)) // 图片圆角显示，值为整数
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, POSTERCORNER, listener,
					LoadAndDisplay.DEFAULT, null);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setImage(Context context, ImageView iv, String url) {
		try {
			if (null == IMAGE) {
				IMAGE = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, IMAGE);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setImage(Context context, ImageView iv, String url,
			int requestWidth, SimpleImageLoadingListener listener) {
		try {
			if (null == IMAGE) {
				IMAGE = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, IMAGE, listener,
					LoadAndDisplay.DEFAULT, null);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setSkin(Context context, ImageView iv, String url) {
		try {
			if (null == SKIN) {
				SKIN = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.nothing)
						.showImageForEmptyUri(R.drawable.nothing)
						.showImageOnFail(R.drawable.nothing).cacheInMemory()
						.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
						.build();
			}
			imageLoader.displayImage(url, iv, SKIN);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setGroupSkin(Context context, ImageView iv, String url,
			ImageLoadingListener loadListener) {
		try {
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.nothing)
					.showImageForEmptyUri(R.drawable.nothing)
					.showImageOnFail(R.drawable.nothing).cacheInMemory()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
					.build();
			imageLoader.displayImage(url, iv, options, loadListener,
					LoadAndDisplay.DEFAULT, null);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setCirclePoster(Context context, ImageView iv,
			String url, ImageLoadingListener loadListener) {
		try {
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.nothing)
					// .showImageForEmptyUri(R.drawable.image_progress)
					// .showImageOnFail(R.drawable.image_progress)
					.cacheInMemory()
					.displayer(
							new RoundedBitmapDisplayer(
									iv.getMeasuredHeight() / 2)).cacheOnDisc()
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			imageLoader.displayImage(url, iv, options, loadListener,
					LoadAndDisplay.DEFAULT, null);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setLoaclImage(ImageView iv, String url,
			SimpleImageLoadingListener listener) {
		try {
			L.d(TAG, "url:" + url);
			if (null == DETAILIMAGE) {
				DETAILIMAGE = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(url, iv, DETAILIMAGE, listener,
					LoadAndDisplay.LOCAL, null);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setProgressImage(ImageView iv, String url,
			SimpleImageLoadingListener listener) {
		if (null == DETAILIMAGE) {
			DETAILIMAGE = new DisplayImageOptions.Builder()
					.showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
					.showImageForEmptyUri(TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
					.cacheInMemory().cacheOnDisc()
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		}
		imageLoader.displayImage(url, iv, DETAILIMAGE, listener,
				LoadAndDisplay.PROGRESS, null);
	}

	public static void setLocalImage(Context context, ImageView iv, String id) {
		try {
			if (ThumbnailImgUtil.isIdEmpty(id)) {
				iv.setImageResource(TaskUtil.IMGDEFAULTLOADSTATEIMG[1]);
				return;
			}
			Bitmap bitmap = Images.Thumbnails.getThumbnail(
					context.getContentResolver(), Long.parseLong(id), 3, null);
			iv.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void setLocalAndDisPlayImage(Context context, ImageView iv,
			String uri) {
		L.d(TAG, "setLocalAndDisPlayImage uri=" + uri);
		try {
			if (null == LOCAL) {
				LOCAL = new DisplayImageOptions.Builder()
						.showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
						.showImageForEmptyUri(
								TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.showImageOnFail(TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
						.cacheInMemory().cacheOnDisc()
						.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			}
			imageLoader.displayImage(uri, iv, LOCAL, null,
					LoadAndDisplay.LOCALTHUMBNAIL, context);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void clearMemoryCache() {
		L.d(TAG, "clearMemoryCache...");
		try {
			imageLoader.clearMemoryCache();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void cacheDiscImage(Context c, String key, File oldFile)
			throws IOException {
		try {
			ImageLoaderConfiguration config = imageLoader
					.getImageLoaderConfiguration();
			DiscCacheAware discCache = config.getDiscMemryCache();
			FileNameGenerator generator = config.getFileNameGenerator();
			File checkFile = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/com.datacomo.mc.spider.android/cache/");
			if (!checkFile.exists()) {
				checkFile.mkdirs();
			}
			File f = new File(checkFile, generator.generate(key));
			new FileUtil().copyfile(oldFile, f, true);
			discCache.put(key, f);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static void cacheDiscImage(Context c, String key, File oldFile,
			ImageSize imageSize) throws IOException {
		try {
			ImageLoaderConfiguration config = imageLoader
					.getImageLoaderConfiguration();
			DiscCacheAware discCache = config.getDiscMemryCache();
			FileNameGenerator generator = config.getFileNameGenerator();
			File checkFile = new File(ConstantUtil.CACHE_PHOTO_PATH);
			if (!checkFile.exists()) {
				checkFile.mkdirs();
			}
			File f = new File(checkFile, generator.generate(key));
			Bitmap bmap = PhotoUtil.getBitmapFromFile(oldFile,
					imageSize.getWidth(), imageSize.getHeight());
			PhotoUtil.bitmap2File(bmap, f);
			discCache.put(key, f);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
	}

	public static Bitmap getPosterBitmap(String key) {
		try {
			ImageLoaderConfiguration config = imageLoader
					.getImageLoaderConfiguration();
			FileNameGenerator generator = config.getFileNameGenerator();
			String path = ConstantUtil.CACHE_PHOTO_PATH
					+ generator.generate(key);
			L.i(TAG, "getPosterBitmap key=" + key + ",path=" + path);
			return BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
		}
		return null;
	}
}
