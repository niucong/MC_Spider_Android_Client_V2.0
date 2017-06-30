package com.datacomo.mc.spider.android.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;
import android.os.Message;

public class ConstantUtil {

	/** 手机类型:iphone,andriod */
	public static final String VERSION_TYPE = "android";
	/** 手机类型:iphone,andriod */
	public static final String CLIENT_FLAG = "android";

	/** 应用的APP_KEY app_api_yuuquu*/
	public static final String APP_KEY = "app_api_android_youyou";
	
	/** 应用密钥SECRET_KEY app_api_datacomo*/
	public static final String SECRET_KEY = "app_api_niucong";
	public static final String ANDROID_COMMON = APP_KEY;
	public static final String APP_PINYIN = "YuuQuu";
	/** API版本号 */
	public static final String V = "1.5";

	/** 短信找回密码地址 */
	public static final String REGISTER_ADDRESS = "106901337896";
	/** 短信圈号地址——短信圈圈 */
	public static final String GROUPSHORT = "1069 0133 7897 ";

	public static final String SYSTEM_FILEPATH = "/data/data/com.datacomo.mc.spider.android/"
			+ APP_PINYIN + "/";

	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().toString();
	public static final String ROOT_PATH = SDCARD_PATH + "/" + APP_PINYIN + "/";
	/** 头像文件夹 */
	public static final String HEAD_PATH = ROOT_PATH + "head/";
	/** 图片文件夹 */
	public static final String IMAGE_PATH = ROOT_PATH + "image/";
	/** 海报文件夹 */
	public static final String POSTER_PATH = ROOT_PATH + "poster/";
	/** 语音文件夹 */
	public static final String VOICE_PATH = ROOT_PATH + "voice/";
	/** 拍照上传文件夹 */
	public static final String CAMERA_PATH = ROOT_PATH + "camera/";
	/** 下载文件夹 */
	public static final String DOWNLOAD_PATH = ROOT_PATH + "download/";
	/** 临时文件夹 */
	public static final String TEMP_PATH = ROOT_PATH + "temp/";
	/** 缓存文件夹 */
	public static final String CACHE_PATH = ROOT_PATH + "Cache/";
	/** 缓存图片 */
	public static final String CACHE_PHOTO_PATH = SDCARD_PATH
			+ "/Android/data/com.datacomo.mc.spider.android/cache/";
	/** 云文件文件夹 */
	public static final String CLOUD_PATH = ROOT_PATH + "CloudFile/";

	public static final String UPLOAD_HEAD_PATH = CAMERA_PATH + "headimg.jpg";

	public static final String PHONE_SEPARATOR = "#MC_API#";

	public static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat MMDDHHMM = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static final String[] AUTOMAIL = { "@yuuquu.com", "@gmail.com",
			"@qq.com", "@163.com", "@126.com", "@yahoo.cn", "@sina.com",
			"@sohu.com", "@139.com", "@wo.com.cn", "@189.cn" };

	public static final String APP_ID = "wx135c739c4da38136";

	public static final ArrayList<String> downloadingList = new ArrayList<String>();
	public static final ArrayList<String> uploadingList = new ArrayList<String>();
	
	/**
	 * String关键字 Message.arg1=1正在下载中，0已停止下载、Message.arg2已下载大小
	 */
	public static final HashMap<String, Message> downloadingMap = new HashMap<String, Message>();

	public static boolean isCreateInfo = false;

	/**
	 * insure the SdCard can used;
	 * 
	 * @return true is can used else is not
	 */
	public static boolean IsCanUseSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
