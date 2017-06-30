package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.url.L;

public class VersionUtil {
	private static final String TAG = "VersionUtil";

	/**
	 * 匹配版本号是否升级
	 * 
	 * 版本号格式：1.0.1 格式(主版本号、次版本号、次版本号)
	 * 
	 * 升级规则：当且仅当服务器版本号大于客户端版本号时升级
	 * 
	 * @param currVersion
	 *            客户端当前版本
	 * @param lastVersion
	 *            服务器最新版本
	 * 
	 * @return
	 */
	public static boolean isUpdate(String currVersion, String lastVersion) {

		boolean isUpdate = false;

		try {
			String[] currVersions = currVersion.replace(".", " ").split(" ");

			// 获取客户端主版本号
			int currPrimaryVersion = Integer.parseInt(currVersions[0].trim());
			// 获取客户端此版本号
			int currSubsidiaryVersion = Integer
					.parseInt(currVersions[1].trim());
			// 获取客户端此版本号
			int currSubSubsidiaryVersion = Integer.parseInt(currVersions[2]
					.trim());

			L.i(TAG, "isUpdate 客户端currPrimaryVersion=" + currPrimaryVersion
					+ ",currSubsidiaryVersion=" + currSubsidiaryVersion
					+ ",currSubSubsidiaryVersion=" + currSubSubsidiaryVersion);

			String[] lastVersions = lastVersion.replace(".", " ").split(" ");
			// 获取服务器主版本号
			int lastPrimaryVersion = Integer.parseInt(lastVersions[0].trim());
			// 获取服务器此版本号
			int lastSubsidiaryVersion = Integer
					.parseInt(lastVersions[1].trim());
			// 获取服务器此版本号
			int lastSubSubsidiaryVersion = Integer.parseInt(lastVersions[2]
					.trim());

			L.i(TAG, "isUpdate 服务器lastPrimaryVersion=" + lastPrimaryVersion
					+ ",lastSubsidiaryVersion=" + lastSubsidiaryVersion
					+ ",lastSubSubsidiaryVersion" + lastSubSubsidiaryVersion);

			if (lastPrimaryVersion > currPrimaryVersion) {
				isUpdate = true;
				return isUpdate;
			}
			if (lastPrimaryVersion == currPrimaryVersion
					&& lastSubsidiaryVersion > currSubsidiaryVersion) {
				isUpdate = true;
				return isUpdate;
			}
			if (lastPrimaryVersion == currPrimaryVersion
					&& lastSubsidiaryVersion == currSubsidiaryVersion
					&& lastSubSubsidiaryVersion > currSubSubsidiaryVersion) {
				isUpdate = true;
				return isUpdate;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUpdate;
	}
}
