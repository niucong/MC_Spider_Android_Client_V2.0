package com.datacomo.mc.spider.android.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtil {
	// private static final String TAG = "CharUtil";

	/**
	 * 去除手机号码前的国家代码和IP号
	 * 
	 * @param phone
	 * @return
	 */
	public static String cleanIP(String phone) {
		String[] ips = { "+86", "12593", "17951", "17911", "10193" };
		for (String ip : ips) {
			if (phone.startsWith(ip)) {
				phone = phone.replace(ip, "");
			}
		}
		phone = phone.replaceAll("-", "").replaceAll(" ", "");
		return phone;
	}

	/**
	 * 验证邮箱合法性
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidEmail(String paramString) {
		// L.i(TAG, "isValidEmail Email=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		return paramString.matches("\\w+[\\w]*@[\\w]+\\.[\\w]+$")
				|| paramString.matches("\\w+[\\w]*@[\\w]+\\.[\\w]+\\.[\\w]+$");
		// return paramString
		// .matches("[a-zA-Z0-9._-]*@([a-zA-Z0-9-_]+\\.)+(com|gov|net|org|com\\.cn|edu\\.cn)$");
	}

	/**
	 * 验证密码是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidPassword(String paramString) {
		// L.i(TAG, "isValidPassword Password=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		// return paramString.matches("[a-zA-Z0-9]{6,16}+$");String regex =
		// "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
		return paramString.matches("[^\u4e00-\u9fa5]{6,16}+$");
	}

	/**
	 * 密码为8-16位，包含字母和数字
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isSetPassword(String paramString) {
		if (paramString == null || paramString.equals(""))
			return false;
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
		return paramString.matches(regex);
	}

	/**
	 * 手机号是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isValidPhone(String paramString) {
		// L.i(TAG, "isValidPhone phone=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		paramString = cleanIP(paramString);
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(paramString);
		return m.matches();
	}

	public static String getUrl(String strContent) {
		strContent = strContent.replaceAll(
				"(?is)(?<!')((http|ftp|https)://[/\\.\\w]+)",
				"<a style='color:#19a97b;' href='$1'>$1</a>");
		return strContent;
	}

	public static boolean isNumber(String id) {
		try {
			Integer.valueOf(id);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 文本中含有URL的内容，画面表示为链接
	 * 
	 * @author Boyer
	 * @param note
	 * @return
	 */
	public static String textToLinks(String note) {
		// 转换的思想为把文本中不是链接("/a>"和"<a "之间)的内容逐个进行转换
		// 把字符串中的"\"和"$"加上转义符，避免appendReplacement替换字符串的时候将它们作为特殊字符处理
		int noteLength = note.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < noteLength; ++i) {
			char c = note.charAt(i);
			if (c == '\\' || c == '$') {
				buffer.append("\\").append(c);
			} else {
				buffer.append(c);
			}
		}
		String linkNote = "/a>" + buffer.toString() + "<a ";
		String regexp = "(?<=/a>).*?(?=<a )";
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(linkNote);
		StringBuffer stringbuffer = new StringBuffer();
		while (matcher.find()) {
			String tempString = urlToLink(matcher.group());
			matcher.appendReplacement(stringbuffer, tempString);
		}
		matcher.appendTail(stringbuffer);
		String result = stringbuffer.toString();
		// 返回的结果去掉加入的"/a>" 和"<a "
		return result.substring(3, result.length() - 3);
	}

	/**
	 * URL转换为链接
	 * 
	 * @author Boyer
	 * @param urlText
	 * @return String
	 */
	public static String urlToLink(String urlText) {
		// 匹配的条件选项为结束为空格(半角和全角)、换行符、字符串的结尾或者遇到其他格式的文本
		String regexp = "(((http|ftp|https|file)://)|((?<!((http|ftp|https|file)://))www\\.))" // 以http...或www开头
				+ ".*?" // 中间为任意内容，惰性匹配
				+ "(?=(&nbsp;|\\s|　|<br />|$|[<>]))"; // 结束条件
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(urlText);
		StringBuffer stringbuffer = new StringBuffer();
		while (matcher.find()) {
			String url = matcher.group().substring(0, 3).equals("www") ? "http://"
					+ matcher.group()
					: matcher.group();
			String tempString = "<a href=\"" + url + "\">" + matcher.group()
					+ "</a>";
			// 这里对tempString中的"\"和"$"进行一次转义，因为下面对它替换的过程中appendReplacement将"\"和"$"作为特殊字符处理
			int tempLength = tempString.length();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < tempLength; ++i) {
				char c = tempString.charAt(i);
				if (c == '\\' || c == '$') {
					buffer.append("\\").append(c);
				} else {
					buffer.append(c);
				}
			}
			tempString = buffer.toString();
			matcher.appendReplacement(stringbuffer, tempString);
		}
		matcher.appendTail(stringbuffer);
		return stringbuffer.toString();
	}

	/**
	 * 页面过滤 标签，javascript，style
	 * 
	 * @param inputString
	 * @return
	 */
	public static String filterText(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		if (htmlStr == null) {
			htmlStr = "";
			return "";
		}
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		Pattern p_html1;
		Matcher m_html1;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			//
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
		}

		return textStr;// 返回文本字符串
	}

	/**
	 * 把html中的&nbsp;替换为空格
	 * 
	 * @param content
	 * @return
	 */
	public static String formartResultContent(String content) {
		if (null == content || "null".equals(content.trim())) {
			content = "";
		}
		return content.replace("&nbsp;", " ");
	}

	/**
	 * 返回字符长度
	 * 
	 * @param val
	 * @return
	 */
	public static int getByteLen(String val) {
		int len = 0;
		String[] valsArr = val.split("");
		String info = "[\u4E00-\u9FA5]";
		for (int i = 0; i < valsArr.length; i++) {
			if (valsArr[i].matches(info)) // 全角
				len += 2;
			else
				len += 1;
		}
		return len;
	}

}
