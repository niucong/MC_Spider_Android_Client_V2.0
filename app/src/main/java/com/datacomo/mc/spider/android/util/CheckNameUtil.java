package com.datacomo.mc.spider.android.util;

/**
 * 名字限制
 */
public class CheckNameUtil {
	/**
	 * 用户——修改用户资料、引导改名、邀请加入
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkMemberName(String name) {
		// 不能为空
		if (name == null || "".equals(name.trim()))
			return false;
		// 不能纯数字
		try {
			Integer.valueOf(name);
			return false;
		} catch (Exception e) {

		}
		// 过滤特殊字符
		if (name.equals(name.replaceAll("^[A-Za-z\\d\\u4E00-\\u9FA5]+$", "")))
			return false;

		if (getMaxContinuumNum(name) > 2)
			return false;

		// // 不能为敏感词
		// for (String noStr : noStrs) {
		// if (name.equals(noStr)) {
		// return false;
		// }
		// }
		// 4-16位字符
		int size = CharUtil.getByteLen(name.trim());
		if (size > 4 && size < 18) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 交流圈——修改圈子资料、创建圈子
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkGroupName(String name) {
		if (name == null || "".equals(name.trim()))
			return false;

		// 不能纯数字
		try {
			Integer.valueOf(name);
			return false;
		} catch (Exception e) {

		}

		// 过滤特殊字符
		if (name.equals(name.replaceAll("^[A-Za-z\\d\\u4E00-\\u9FA5]+$", "")))
			return false;
		// 4-20位字符
		int size = CharUtil.getByteLen(name);
		if (size > 4 && size < 22) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 朋友圈——创建
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFriendName(String name) {
		if (name == null || "".equals(name.trim()))
			return false;

		int size = CharUtil.getByteLen(name);
		if (size > 2 && size < 22) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得最大连续字符次数
	 * 
	 * @param str
	 * @return
	 */
	private static int getMaxContinuumNum(String str) {
		@SuppressWarnings("unused")
		char c1, cMax;
		int num1, numMax;
		num1 = numMax = 0;
		char[] chs = str.toCharArray();
		c1 = cMax = chs[0];
		num1 = numMax = 1;
		for (int i = 1; i < chs.length; i++) {
			if (chs[i] == c1) {
				num1++;
			} else {
				if (num1 > numMax) {
					cMax = c1;
					numMax = num1;
				}
				c1 = chs[i];
				num1 = 1;
			}
		}
		if (num1 > numMax) {
			cMax = c1;
			numMax = num1;
		}
		return numMax;
	}
}
