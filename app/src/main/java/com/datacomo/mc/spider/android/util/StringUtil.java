package com.datacomo.mc.spider.android.util;

public class StringUtil {

	/***
	 * 去掉字符串前后的空间，中间的空格保留
	 * 
	 * @param str
	 * @return
	 */
	public static String trimInnerSpaceStr(String str) {
		if (str == null)
			return "";
		// str = str.trim();
		// while (str.startsWith(" ") || str.endsWith("\n")) {
		// str = str.substring(1, str.length()).trim();
		// }
		while (str.endsWith(" ") || str.endsWith("\n")) {
			str = str.substring(0, str.length() - 1);// .trim();
		}
		return str;
	}

	public static String merge(Object... params) {
		StringBuffer strBuffer = new StringBuffer();
		for (Object str : params) {
			if (null != str)
				strBuffer.append(str);
		}
		String str = strBuffer.toString();
		int length = strBuffer.length();
		strBuffer.delete(0, length);
		strBuffer = null;
		return str;
	}

	/**
	 * 
	 * @param Original
	 * @param start
	 * @param end
	 * @param startOffset
	 *            + 向右偏移，-向左偏移
	 * @param endOffset
	 *            + 向右偏移，-向左偏移
	 * @return
	 */
	public static String subString(String Original, String start, String end,
			int startOffset, int endOffset) {
		if (null == Original)
			return null;
		if (null == start && null == end) {
			return Original;
		}
		int index_Start = 0;
		int index_End = 0;
		if (null == start) {
			index_End = Original.lastIndexOf(end) + endOffset;
			index_Start = 0;
			if (startOffset >= 0)
				index_Start += startOffset;
		} else if (null == end) {
			index_Start = Original.lastIndexOf(start) + startOffset;
			index_End = Original.length();
			if (endOffset <= 0)
				index_End += endOffset;
		} else {
			index_Start = Original.lastIndexOf(start) + startOffset;
			index_End = Original.lastIndexOf(end) + endOffset;
		}
		if (index_End > Original.length())
			index_End = Original.length();
		if (index_End < 0)
			index_End = 0;
		if (index_Start > Original.length())
			index_Start = Original.length();
		if (index_Start < 0)
			index_Start = 0;
		if (index_Start >= index_End)
			return Original;
		return Original.substring(index_Start, index_End);
	}

}