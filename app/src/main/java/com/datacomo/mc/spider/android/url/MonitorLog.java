package com.datacomo.mc.spider.android.url;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.util.ConstantUtil;
import com.datacomo.mc.spider.android.util.GetDbInfoUtil;
import com.datacomo.mc.spider.android.util.SoftPhoneInfo;

/**
 * 监控日志
 */
public class MonitorLog {
	private static final String TAG = "MonitorLog";

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat yMdHms = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 日志的产生时间

	/**
	 * 保存监控日志
	 * 
	 * @param url
	 * @param type
	 *            异常类型和超时时间
	 */
	public static void saveLog(String url, String type) {
		L.i(TAG, "saveLog url=" + url);
		try {
			Date nowtime = new Date();
			String needWriteMessage = yMdHms.format(nowtime);

			// 时间&类名|method|id&异常类型&地点|纬度|经度&手机型号&最大频率|最小频率|扩展频率&剩余内存|总内存&系统版本号&网络类型&IMSI&软件版本号
			String result = needWriteMessage;

			String param = url.substring(url.indexOf("?") + 1);
			String[] ps = param.split("&");
			HashMap<String, String> map = new HashMap<String, String>();
			for (String pstr : ps) {
				if (pstr != null && pstr.contains("=")) {
					map.put(pstr.substring(0, pstr.indexOf("=")),
							pstr.substring(pstr.indexOf("=") + 1));
				}
			}

			int id = GetDbInfoUtil.getMemberId(App.app);
			// 类名|method|session_key
			String json = url.substring(url.lastIndexOf("/") + 1,
					url.indexOf("?"));
			result += "&" + json.substring(0, json.indexOf(".")) + "|"
					+ map.get("method") + "|" + id;
			result += "&" + type;

			// LocationClient mLocClient = App.app.mLocationClient;
			// Vibrator mVibrator = (Vibrator) App.app
			// .getSystemService(Service.VIBRATOR_SERVICE);
			// App.app.mVibrator = mVibrator;
			// setLocationOption(mLocClient);
			// mLocClient.start();
			// result += "&" + App.app.loc + "|" + App.app.mlat + "|"
			// + App.app.mLon;
			result += "&0|0|null";
			// mLocClient.stop();

			result += "&" + android.os.Build.MODEL;
			result += "&" + getMaxCpuFreq() + "|" + getMinCpuFreq() + "|"
					+ getCurCpuFreq();
			result += "&" + getAvailMemory() + "|" + getTotalMemory();
			result += "&" + android.os.Build.VERSION.RELEASE;

			SoftPhoneInfo spi = new SoftPhoneInfo(App.app);
			int NetworkType = 0;
			if ("mobile".equals(spi.getNetworkType().toLowerCase())) {
				NetworkType = 1;
			} else if ("wifi".equals(spi.getNetworkType().toLowerCase())) {
				NetworkType = 2;
			}
			result += "&" + NetworkType;
			result += "&" + spi.getImsi();
			result += "&" + spi.getVersionName() + "\n\n";

			String path = ConstantUtil.TEMP_PATH
					+ needWriteMessage.substring(0,
							needWriteMessage.indexOf(" ")).replaceAll("-", "")
					+ "_" + id + "_" + spi.getPhoneMark() + ".txt";
			L.i(TAG, "saveLog path=" + path);
			L.d(TAG, "saveLog result=" + result);

			// L.i(TAG, "saveLog MaxCpuFreq=" + getMaxCpuFreq() + ",MinCpuFreq="
			// + getMinCpuFreq() + ",CurCpuFreq=" + getCurCpuFreq()
			// + ",CpuName=" + getCpuName());
			// L.i(TAG, "saveLog AvailMemory=" + getAvailMemory()
			// + ",TotalMemory=" + getTotalMemory()
			// + ",SDcardTotalMemory=" + getSDcardTotalMemory()
			// + ",SDcardAvailableMemroy=" + getSDcardAvailableMemroy());

			// TODO
			// File file = new File(path);
			// if (!file.exists()) {
			// try {
			// file.createNewFile();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// try {
			// FileWriter filerWriter = new FileWriter(file, true);
			// BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			// bufWriter.write(result);
			// bufWriter.newLine();
			// bufWriter.close();
			// filerWriter.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置相关参数
	 */
	private static void setLocationOption(LocationClient mLocClient) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPoiNumber(10);
		option.disableCache(true);
		mLocClient.setLocOption(option);
	}

	/**
	 * 获取手机可用内存
	 * 
	 * @return
	 */
	private static String getAvailMemory() {
		ActivityManager am = (ActivityManager) App.app
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(App.app, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 获取手机总内存
	 * 
	 * @return
	 */
	private static String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				L.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (Exception e) {
		}
		return Formatter.formatFileSize(App.app, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * Sdcard剩余内存
	 * 
	 * @return
	 */
	// private static long getSDcardAvailableMemroy() {
	// // Available rom memory
	// File path = Environment.getDataDirectory();
	// StatFs stat = new StatFs(path.getPath());
	// long blockSize = stat.getBlockSize();
	// long availableBlocks = stat.getAvailableBlocks();
	// return blockSize * availableBlocks;
	// }

	/**
	 * Sdcard总内存
	 * 
	 * @return
	 */
	// private static long getSDcardTotalMemory() {
	// File path = Environment.getDataDirectory();
	// StatFs stat = new StatFs(path.getPath());
	// long blockSize = stat.getBlockSize();
	// long totalBlocks = stat.getBlockCount();
	// return totalBlocks * blockSize;
	// }

	private static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	private static String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	private static String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader(
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// private static String getCpuName() {
	// try {
	// FileReader fr = new FileReader("/proc/cpuinfo");
	// @SuppressWarnings("resource")
	// BufferedReader br = new BufferedReader(fr);
	// String text = br.readLine();
	// String[] array = text.split(":\\s+", 2);
	// for (int i = 0; i < array.length; i++) {
	// }
	// return array[1];
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

}
