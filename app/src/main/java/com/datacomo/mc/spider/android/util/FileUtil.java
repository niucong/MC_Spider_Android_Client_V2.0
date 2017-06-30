package com.datacomo.mc.spider.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;

public class FileUtil {
	private static final String TAG = "FileUtil";

	/**
	 * 创建文件夹
	 * 
	 * @param filePath
	 */
	public static void createFile(String filePath) {
		// L.i(TAG, "createFile filePath = " + filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 创建一个新文件
	 * 
	 * @param filePath
	 * @return true 创建成功 false 创建失败或文件已存在
	 */
	public boolean createNewFile(String filePath) {
		File file = new File(filePath);
		boolean result = false;
		if (!file.exists()) {
			try {
				file.createNewFile();
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 复制文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @param rewrite
	 */
	public void copyfile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}
		L.i(TAG, "copyfile fromFile=" + fromFile.getPath() + ",toFile="
				+ toFile.getPath());

		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			fosfrom.close();
			fosto.close();

			L.d(TAG, "copyfile toFile=" + toFile.length());
		} catch (Exception e) {
			e.fillInStackTrace();
		}

	}

	public boolean isOpenFile(Context context, String fileUrl, Handler handler) {
		final String tempName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		File myFile = new File(ConstantUtil.CLOUD_PATH + tempName);
		if (myFile != null && myFile.exists()) {
			openFile(context, myFile);
			if (handler != null)
				handler.sendEmptyMessage(1);
			return true;
		} else if (ConstantUtil.downloadingList.contains(tempName)) {
			T.show(context, R.string.downloading);
			if (handler != null)
				handler.sendEmptyMessage(1);
			return true;
		}
		ConstantUtil.downloadingList.add(tempName);
		return false;
	}

	/**
	 * 打开文件
	 * 
	 * @param context
	 * @param file
	 */
	public void openFile(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		L.i(TAG, "openFile type = " + type);
		// 设置intent的data和Type属性。
		intent.setDataAndType(Uri.fromFile(file), type);
		try {
			// 跳转
			context.startActivity(intent);
		} catch (Exception e) {
			type = "*/*";
			intent.setDataAndType(Uri.fromFile(file), type);
			context.startActivity(intent);
		}
	}

	public void openFile(Context context, File file, String type) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		L.i(TAG, "openFile type = " + type);
		// 设置intent的data和Type属性。
		intent.setDataAndType(Uri.fromFile(file), type);
		try {
			// 跳转
			context.startActivity(intent);
		} catch (Exception e) {
			type = "*/*";
			intent.setDataAndType(Uri.fromFile(file), type);
			context.startActivity(intent);
		}
	}

	/**
	 * 删除文件夹、文件
	 * 
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		L.i(TAG, "deleteFile filePath = " + filePath);
		File file = null;
		try {
			file = new File(filePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (file != null && file.exists()) {
			// file.delete();
			try {
				Runtime.getRuntime().exec("rm -r " + filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileHtmlPhoto(File file) {
		// 生成上传文件图片
		StringBuffer html = new StringBuffer();
		html.append("<br/><notefile><table width=\"145\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		html.append("<tr>");
		html.append("<td border=\"0\" align=\"left\" rowspan=\"2\">&nbsp;<img src=\"{url}\"/></td>");
		html.append("<td style=\"font-family:'宋体';font-size:8px;\">{title}</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"font-family:'宋体';font-size:10px;\">{size}</td>");
		html.append("</tr>");
		html.append("</table></notefile><br/>");
		String s = html.toString();
		String title = "";
		String uploadName = file.getName();
		if (uploadName.length() >= 14) {
			title = uploadName.substring(0, 8)
					+ "..."
					+ uploadName.substring(uploadName.length() - 5,
							uploadName.length());
		}
		s = s.replace("{url}", getFileIcon(uploadName) + "")
				.replace("{title}", title)
				.replace("{size}", computeFileSize(file.length()));
		return s;
	}

	/**
	 * 计算文件大小
	 * 
	 * @param len
	 * @return
	 */
	public static String computeFileSize(long len) {
		String size = "1K";
		float len2 = len;
		DecimalFormat df = new DecimalFormat("#0.#");
		if (len > 1024) {
			len2 = len2 / 1024;
		} else {
			return size;
		}
		if (len2 > 1024) {
			len2 = len2 / 1024;
		} else {
			size = df.format(len2) + "K";
			return size;
		}
		if (len2 > 1024) {
			len2 = len2 / 1024;
			size = df.format(len2) + "G";
		} else {
			size = df.format(len2) + "M";
		}
		return size;
	}

	public static String computeMediaTime(Long time) {
		int totalSeconds = (int) (time / 1000);

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		StringBuilder mFormatBuilder = new StringBuilder();
		@SuppressWarnings("resource")
		Formatter mFormatter = new Formatter(mFormatBuilder,
				Locale.getDefault());
		mFormatBuilder.setLength(0);
		if (hours > 0) {
			// 超过60分钟的，显示出小时
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			// 不足60分钟的，只显示分和秒
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	public static boolean checkImage(File file) {
		try {
			// 从SDCARD下读取一个文件
			@SuppressWarnings("resource")
			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[2];
			// 文件类型代码
			String filecode = "";
			// 文件类型
			@SuppressWarnings("unused")
			String fileType = "";
			// 通过读取出来的前两个字节来判断文件类型
			if (inputStream.read(buffer) != -1) {
				for (int i = 0; i < buffer.length; i++) {
					// 获取每个字节与0xFF进行与运算来获取高位，这个读取出来的数据不是出现负数并转换成字符串
					filecode += Integer.toString((buffer[i] & 0xFF));
				}
				// 把字符串再转换成Integer进行类型判断
				switch (Integer.parseInt(filecode)) {
				case 7790:
					fileType = "exe";
					break;
				case 7784:
					fileType = "midi";
					break;
				case 8297:
					fileType = "rar";
					break;
				case 8075:
					fileType = "zip";
					break;
				case 255216:
					fileType = "jpg";
					return true;
				case 7173:
					fileType = "gif";
					return true;
				case 6677:
					fileType = "bmp";
					return true;
				case 13780:
					fileType = "png";
					return true;
				default:
					fileType = "unknown type: " + filecode;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	public static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	private static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" },
			// {".rar", "application/octet-stream"},
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

	public static int getFileIcon(String suffix) {
		suffix = suffix.toLowerCase();
		if (suffix.endsWith("tar") || suffix.endsWith("iso")
				|| suffix.endsWith("jar") || suffix.endsWith("rar")
				|| suffix.endsWith("cab") || suffix.endsWith("arj")
				|| suffix.endsWith("lzh") || suffix.endsWith("ace")
				|| suffix.endsWith("gzip") || suffix.endsWith("war")
				|| suffix.endsWith("zip")) {
			return R.drawable.file_compress;
		} else if (suffix.endsWith("doc") || suffix.endsWith("docx")
				|| suffix.endsWith("dot") || suffix.endsWith("dotx")) {
			return R.drawable.file_word;
		} else if (suffix.endsWith("xlt") || suffix.endsWith("xltx")
				|| suffix.endsWith("xls") || suffix.endsWith("xlsx")) {
			return R.drawable.file_excel;
		} else if (suffix.endsWith("pptx") || suffix.endsWith("ppt")
				|| suffix.endsWith("pot") || suffix.endsWith("pps")) {
			return R.drawable.file_ppt;
		} else if (suffix.endsWith("access")) {
			return R.drawable.file_access;
		} else if (suffix.endsWith("txt")) {
			return R.drawable.file_txt;
		} else if (suffix.endsWith("pdf")) {
			return R.drawable.file_pdf;
		} else if (suffix.endsWith("exe")) {
			return R.drawable.file_exe;
		} else if (suffix.endsWith("gif") || suffix.endsWith("jpg")
				|| suffix.endsWith("png") || suffix.endsWith("jpeg")
				|| suffix.endsWith("bmp") || suffix.endsWith("pcx")
				|| suffix.endsWith("tiff")) {
			return R.drawable.file_image;
		} else if (suffix.endsWith("ai")) {
			return R.drawable.file_ai;
		} else if (suffix.endsWith("psd")) {
			return R.drawable.file_psd;
		} else if (suffix.endsWith("rm") || suffix.endsWith("avi")
				|| suffix.endsWith("mpg") || suffix.endsWith("mpeg")
				|| suffix.endsWith("ram") || suffix.endsWith("mp4")
				|| suffix.endsWith("rmvb") || suffix.endsWith("wmv")
				|| suffix.endsWith("3gp")) {
			return R.drawable.file_video;
		} else if (suffix.endsWith("wma") || suffix.endsWith("mp3")
				|| suffix.endsWith("amr")) {
			return R.drawable.file_music;
		} else if (suffix.endsWith("swf") || suffix.endsWith("flv")) {
			return R.drawable.file_flash;
		} else if (suffix.endsWith("bak")) {
			return R.drawable.file_backup;
		} else if (suffix.endsWith("chm")) {
			return R.drawable.file_help;
		} else if (suffix.endsWith("rss")) {
			return R.drawable.file_rss;
		} else if (suffix.endsWith("htm") || suffix.endsWith("html")) {
			return R.drawable.file_html;
		} else if (suffix.endsWith("jsp")) {
			return R.drawable.file_jsp;
		} else if (suffix.endsWith("asp")) {
			return R.drawable.file_asp;
		} else if (suffix.endsWith("php")) {
			return R.drawable.file_php;
			// } else if (suffix.endsWith("aspx")) {
			// return R.drawable.file_csharp;
		} else if (suffix.endsWith("java")) {
			return R.drawable.file_java;
		} else if (suffix.endsWith("cpp") || suffix.endsWith("dsp")
				|| suffix.endsWith("clw") || suffix.endsWith("ncb")
				|| suffix.endsWith("opt") || suffix.endsWith("bsc")
				|| suffix.endsWith("dsp") || suffix.endsWith("dsw")
				|| suffix.endsWith("mak") || suffix.endsWith("aps")
				|| suffix.endsWith("plg")) {
			return R.drawable.file_cplusplus;
		} else if (suffix.endsWith("run")) {
			return R.drawable.file_ruby;
		} else if (suffix.endsWith("css")) {
			return R.drawable.file_css;
		} else if (suffix.endsWith("js")) {
			return R.drawable.file_js;
		} else if (suffix.endsWith("xml")) {
			return R.drawable.file_xml;
		} else if (suffix.endsWith("wps") || suffix.endsWith("vsd")
				|| suffix.endsWith("lnk") || suffix.endsWith("mpp")
				|| suffix.endsWith("sis") || suffix.endsWith("chk")
				|| suffix.endsWith("dll") || suffix.endsWith("tlx")
				|| suffix.endsWith("eap") || suffix.endsWith("gz")
				|| suffix.endsWith("torrent") || suffix.endsWith("mmap")
				|| suffix.endsWith("rp") || suffix.endsWith("7z")
				|| suffix.endsWith("z") || suffix.endsWith("mht")
				|| suffix.endsWith("zargo") || suffix.endsWith("rtf")
				|| suffix.endsWith("m") || suffix.endsWith("h")
				|| suffix.endsWith("class") || suffix.endsWith("amr")
				|| suffix.endsWith("eml") || suffix.endsWith("vcf")
				|| suffix.endsWith("out") || suffix.endsWith("umd")
				|| suffix.endsWith("potx") || suffix.endsWith("rv")
				|| suffix.endsWith("rdb") || suffix.endsWith("txd")
				|| suffix.endsWith("graffle") || suffix.endsWith("vdx")) {
			return R.drawable.file_default;
		} else if (suffix.endsWith("apk")) {
			return R.drawable.file_apk;
		}
		return R.drawable.file_other;
	}

	public static boolean checkInstantApp(Context c, String packageName) {
		List<PackageInfo> pcgs = c.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < pcgs.size(); i++) {
			PackageInfo pi = pcgs.get(i);
			if (packageName.equals(pi.packageName)) {
				return true;
			}
		}
		return false;
	}

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			L.d(TAG, "orientation:" + orientation);
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

	/**
	 * 将图片的旋转角度置为0
	 * 
	 * @param path
	 */
	public static void setPictureDegreeZero(String path, int degree) {
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			// 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
			// 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
			exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
			exifInterface.saveAttributes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getThumbnailBitmap(String path, int inSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	public static BitmapFactory.Options getTempBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		return options;
	}

	public static Bitmap setOriginalBitmap(Context mContext, String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		float realWidth = options.outWidth;
		float realHeight = options.outHeight;
		L.d(TAG, "setView realWidth=" + realWidth + ",realHeight=" + realHeight);
		options.inSampleSize = 10;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		Matrix matrix = new Matrix();

		matrix.postScale(10.0f, 10.0f); // 长和宽放大缩小的比例
		int rotate = getExifOrientation(filePath);
		matrix.setRotate(rotate);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth() * 10, bitmap.getHeight() * 10, matrix, true);
		File dir = new File(ConstantUtil.IMAGE_PATH + "text1.jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(dir);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		resizeBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		return resizeBmp;
	}

	public static void setOrientation(Context mContext, String filePath,
			int rotate) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
			if (null != exif) {
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
				exif.saveAttributes();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap rotatingImage(Bitmap bitmap, int rotate) {
		if (bitmap == null) {
			L.i(TAG, "rotaingImage null");
			return null;
		}
		Bitmap rotateBitmap = null;
		try {
			// 旋转图片 动作
			Matrix matrix = new Matrix();
			matrix.setRotate(rotate);
			rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return rotateBitmap;
	}

	public static String ChageImage(final Context context,
			final Handler handler, final String path) {
		L.d(TAG, "ChageImage path=" + path);
		final int rotate = FileUtil.getExifOrientation(path);
		L.d(TAG, "ChageImage rotate=" + rotate);
		if (rotate > 0) {
			String name = path.substring(path.lastIndexOf("/") + 1);
			final String newpath = ConstantUtil.TEMP_PATH + name;
			new Thread() {
				public void run() {
					FileOutputStream fos = null;
					try {
						File dir = new File(ConstantUtil.TEMP_PATH);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File file = new File(newpath);
						if (!file.exists()) {
							file.createNewFile();
						}
						fos = new FileOutputStream(file);
						Bitmap bitmap_Thumbnail = null;

						switch (rotate) {
						case 90:
							bitmap_Thumbnail = rotatingImage(
									PhotoUtil.getBitmapFromFile(new File(path),
											false), rotate);
							break;
						case 180:
							bitmap_Thumbnail = rotatingImage(
									PhotoUtil.getBitmapFromFile(new File(path),
											true), rotate);
							break;
						case 270:
							bitmap_Thumbnail = rotatingImage(
									PhotoUtil.getBitmapFromFile(new File(path),
											false), rotate);
							break;
						}
						bitmap_Thumbnail.compress(Bitmap.CompressFormat.JPEG,
								100, fos);
						context.sendBroadcast(new Intent(
								Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
										.fromFile(file)));
						Message message = handler.obtainMessage(0, newpath);
						handler.sendMessage(message);
						L.d(TAG, "ChageImage newpath=" + newpath);
					} catch (OutOfMemoryError e) {
						L.i(TAG, "ChageImage OutOfMemoryError");
						Message message = handler.obtainMessage(1, path);
						handler.sendMessage(message);
					} catch (Exception e) {
						L.i(TAG, "ChageImage Exception");
						e.printStackTrace();
						Message message = handler.obtainMessage(1, path);
						handler.sendMessage(message);
					} finally {
						try {
							if (fos != null)
								fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		} else {
			Message message = handler.obtainMessage(1, path);
			handler.sendMessage(message);
			L.i(TAG, "ChageImage path=" + path);
		}
		return path;
	}

	public static File ChangeImage(File file, boolean isOriginal) {
		FileUtil.createFile(ConstantUtil.TEMP_PATH);
		File tempFile = new File(ConstantUtil.TEMP_PATH
				+ System.currentTimeMillis() + ".jpg");
		try {
			int rotate = FileUtil.getExifOrientation(file.getPath());
			Bitmap bm = null;
			Bitmap temp = null;
			L.d(TAG, "startThread rotate:" + rotate);
			if (!file.getPath().endsWith(".gif") && !isOriginal) {
				switch (rotate) {
				case 0:
					bm = PhotoUtil.getBitmapFromFile(file, true);
					break;
				case 90:
					bm = PhotoUtil.getBitmapFromFile(file, false);
					break;
				case 180:
					bm = PhotoUtil.getBitmapFromFile(file, true);
					break;
				case 270:
					bm = PhotoUtil.getBitmapFromFile(file, false);
					break;
				}
				if (rotate > 0) {
					temp = FileUtil.rotatingImage(bm, rotate);
				}
				FileOutputStream fos = new FileOutputStream(tempFile);
				if (null == temp) {
					try {
						bm.compress(Bitmap.CompressFormat.JPEG,
								PhotoUtil.compressImage(bm, 200), fos);
						file = tempFile;
					} catch (Exception e) {
						bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						file = tempFile;
					} catch (OutOfMemoryError e) {
						bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						file = tempFile;
					}
				} else {
					bm.recycle();
					bm = null;
					try {
						temp.compress(Bitmap.CompressFormat.JPEG,
								PhotoUtil.compressImage(temp, 200), fos);
						file = tempFile;
					} catch (Exception e) {
						temp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						file = tempFile;
					}
				}
			} else if (!file.getPath().endsWith(".gif")) {
				if (rotate > 0) {
					switch (rotate) {
					case 90:
						bm = PhotoUtil.getBitmapFromFile(file, false);
						break;
					case 180:
						bm = PhotoUtil.getBitmapFromFile(file, true);
						break;
					case 270:
						bm = PhotoUtil.getBitmapFromFile(file, false);
						break;
					}
					temp = FileUtil.rotatingImage(bm, rotate);
					FileOutputStream fos = new FileOutputStream(tempFile);
					if (null == temp) {
						try {
							bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							file = tempFile;
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						bm.recycle();
						bm = null;
						try {
							temp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							file = tempFile;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tempFile = null;
		return file;
	}

}
