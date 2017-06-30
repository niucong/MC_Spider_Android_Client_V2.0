package com.datacomo.mc.spider.android.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SoftPhoneInfo {
	// private final String TAG = "SoftPhoneInfo";

	private Context context;
	private TelephonyManager tm;

	public SoftPhoneInfo(Context context) {
		this.context = context;
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 网络状态
	 * 
	 * @return
	 */
	public String getNetworkType() {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		String type = null;
		if (info != null) {
			type = info.getTypeName();
		}
		connec.getNetworkInfo(0);
		return type;
	}

	/**
	 * 网络状态
	 * 
	 * @return
	 */
	public String getNetworkAPN() {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connec.getActiveNetworkInfo();
		String apn = null;
		if (info != null) {
			apn = info.getExtraInfo();
		}
		return apn;
	}

	/**
	 * 获取到手机IP地址
	 * 
	 * @return String
	 */
	public String getIPAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * IMEI设备号
	 * 
	 * @return
	 */
	public String getPhoneMark() {
		return tm.getDeviceId();
	}

	// public String getIDFA() {
	// Info adInfo = null;
	// try {
	// adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
	// } catch (IOException e) {
	// } catch (GooglePlayServicesAvailabilityException e) {
	// } catch (GooglePlayServicesNotAvailableException e) {
	// }
	// return adInfo.getId();
	// }

	/**
	 * 软件版本号
	 * 
	 * @return
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName; // 版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取手机号码：电信的可以取到、移动联通的取不到
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		// String number = tm.getLine1Number();
		// if (number == null) {
		// number = "";
		// }
		// String androidId = Secure.getString(context.getContentResolver(),
		// Secure.ANDROID_ID);
		return "";
	}

	/**
	 * SIM卡的ISMI号
	 * 
	 * @return
	 */
	public String getImsi() {
		return tm.getSubscriberId();
	}

	/**
	 * 手机型号
	 * 
	 * @return
	 */
	public String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 获得手机版本号
	 * 
	 * @return
	 */
	public String getAndroidVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * MEID
	 * 
	 * @return
	 */
	public String getMeid() {
		return "";
	}

	/**
	 * 手机序列号
	 * 
	 * @return
	 */
	public String getDeviceNumber() {
		return "";
	}

	public String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取信号强度
	 */
	// public void getPhoneState() {
	// try {
	// // 创建PhoneStateListener 对象
	// PhoneStateListener MyPhoneListener = new PhoneStateListener() {
	// @Override
	// public void onCellLocationChanged(CellLocation location) {
	// if (location instanceof GsmCellLocation) {// gsm网络
	// int CID = ((GsmCellLocation) location).getCid();
	// } else if (location instanceof CdmaCellLocation) {// 其他CDMA等网络
	// int ID = ((CdmaCellLocation) location)
	// .getBaseStationId();
	// }
	// }
	//
	// @Override
	// public void onServiceStateChanged(ServiceState serviceState) {
	// super.onServiceStateChanged(serviceState);
	// }
	//
	// @Override
	// public void onSignalStrengthsChanged(
	// SignalStrength signalStrength) {
	// int asu = signalStrength.getGsmSignalStrength();
	// int dbm = -113 + 2 * asu; // 信号强度
	// L.i(TAG, "getPhoneState asu=" + asu + ",dbm=" + dbm);
	// super.onSignalStrengthsChanged(signalStrength);
	// }
	// };
	// // 监听信号改变
	// tm.listen(MyPhoneListener,
	// PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	//
	// /*
	// * 可能需要的权限 <uses-permission
	// * android:name="android.permission.WAKE_LOCK"></uses-permission>
	// * <uses-permission
	// * android:name="android.permission.ACCESS_COARSE_LOCATION"/>
	// * <uses-permission
	// * android:name="android.permission.ACCESS_FINE_LOCATION"/>
	// * <uses-permission
	// * android:name="android.permission.READ_PHONE_STATE" />
	// * <uses-permission
	// * android:name="android.permission.ACCESS_NETWORK_STATE" />
	// */
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
