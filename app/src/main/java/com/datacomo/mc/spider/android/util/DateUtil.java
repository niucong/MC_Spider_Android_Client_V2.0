/**
 * Copyright 2009 DataComo Communications Technology INC.
 * 
 * This source file is a part of M6_Wap_V1 project. 
 * date: May 4, 2009
 *
 */
package com.datacomo.mc.spider.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liguo
 * @version v1.0.0
 * @date Nov 11, 2008 10:14:51 AM
 */
public class DateUtil {
	private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
	private static String defaultDisplayDatePattern = "yyyy-MM-dd HH:mm:ss";
	private static String defaultDisplayTimeZone = "Asia/Baijing";
	private static String defaultDatePatternYmd = "yyyy-MM-dd";
	private static String defaultDatePatternMdyHm = "MM/dd/yyyy HH:mm:ss";
	
	public static String getDatePattern() {
		return defaultDatePatternYmd;
	}
	
	/**
	 * 返回 0  : 表示时间日期相同
	 * 返回 1  : 表示 当前时间>设定时间，设定的时间已过期
	 * 返回 -1 : 表示当前时间<设定时间 ，设定的时间未过期
	 * @param date1 当前时间
	 * @param date2 设定时间
	 * @return int
	 */
	public static int compareToDate(String date1,String date2){
		try {
			//返回 0 表示时间日期相同
		    //返回 1 表示日期1>日期2
		    //返回 -1 表示日期1<日期2
			SimpleDateFormat df = new SimpleDateFormat(defaultDatePattern);
			Date d1=df.parse(date1);
			Date d2=df.parse(date2);
			return d1.compareTo(d2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 *  获取到现在的时间
	 * @return String
	 */
	public static String getTime(){
		return getDateTime(null);
	}
	
	/**
	 * 当前时间
	 * MM/dd/yyyy HH:mm 如:05/18/2012 18:06:05
	 * yyyy-MM-dd HH:mm 如:2012-05-18 18:06:05
	 * @param date
	 * @return String	
	 */
	public static String getDateTime(String format) {
		Date date=new Date();
		if("yyyy-MM-dd HH:mm:ss".equals(format)){
			return format(date, "yyyy-MM-dd HH:mm:ss");
		}else if("MM/dd/yyyy HH:mm:ss".equals(format)){
			return format(date, "MM/dd/yyyy HH:mm:ss");
		}else if ("yyyyMMddHHmmss".equals(format)){
			return format(date, "yyyyMMddHHmmss");
		}else{
			return format(date, "yyyy-MM-dd HH:mm:ss");
		}
	}
	
	/**
	 * 当前时间
	 * MM/dd/yyyy HH:mm 如:05/18/2012 18:06:05
	 * @param date
	 * @return String
	 */
	public static String getDateTime() {
		Date date=new Date();
		return format(date, "MM/dd/yyyy HH:mm:ss");
	}
	
	/**
	 * 转化时间格式
	 * 将MM/dd/yyyy HH:mm:ss转化成MM/dd/yyyy HH:mm
	 * @return
	 */
	public static String getDateTimeForshort(String datetime){
		Date date = null;
		String viewtime=null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			date = df.parse(datetime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy HH:mm"); 
			viewtime = sdf2.format(date); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return viewtime;
	}
	
	/**
	 * 转化时间格式
	 * 将Long型转化成MM/dd/yyyy HH:mm
	 * @return Date
	 */
	public static String displayFormatMdyHm(Date date) {
		String viewtime=null;
		if(date != null) {
			SimpleDateFormat df = new SimpleDateFormat(defaultDatePatternMdyHm);
			df.setTimeZone(TimeZone.getTimeZone(defaultDisplayTimeZone));
			viewtime = df.format(date);
		 }
		return viewtime;
	}
	

	/**
	 * 转化时间格式
	 * 将MM/dd/yyyy HH:mm:ss转化成yyyy-MM-dd HH:mm
	 * @return
	 */
	public static String getDateTimeForshort2(String datetime){
		Date date = null;
		String viewtime=null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			date = df.parse(datetime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
			viewtime = sdf2.format(date); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return viewtime;
	}
	
	public static String displayFormat(Date date) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(defaultDisplayDatePattern);
			df.setTimeZone(TimeZone.getTimeZone(defaultDisplayTimeZone));
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	public static String displayFormatYmd(Date date) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(defaultDatePatternYmd);
			df.setTimeZone(TimeZone.getTimeZone(defaultDisplayTimeZone));
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	public static String format(Date date) {
		return format(date, getDatePattern());
	}

	public static String format(Date date, String pattern) {
		String returnValue = "";
		try {
			if (date != null) {
				SimpleDateFormat df = new SimpleDateFormat(pattern);
				returnValue = df.format(date);
			}
		} catch (Exception e) {
			return returnValue;
		}

		return (returnValue);
	}

	public static Date parse(String strDate) throws ParseException {
		return parse(strDate, getDatePattern());

	}

	public static Date parse(String strDate, String pattern)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.parse(strDate);
	}

	public static boolean validateDate(String strDate) {
		boolean flag = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat(defaultDatePatternYmd);
			df.parse(strDate);
			flag = true;
		} catch (Exception e) {
			return false;
		}

		return flag;
	}

	public static String getDate(Date date) {
		return format(date, "yyyy-MM-dd");
	}
	
	
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

/*	public static Date simpleDate(Date date) {
		String result = "";
		Date sdate = new Date(System.currentTimeMillis());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			result = sdf.format(date);
			sdate = DateUtils.parseDate(result, new String[] { "yyyy/MM/dd",
					"yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd HH:mm" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdate;
	}*/

	public static Date currentDate() {
		TimeZone t = TimeZone.getTimeZone(defaultDisplayTimeZone);
		Locale l = new Locale("zh", "CN");
		Calendar c = Calendar.getInstance(t, l);
		return new Date(c.getTimeInMillis());
	}

	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	public static Date subtractMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n * (-1));
		return cal.getTime();
	}

	public static Date addHour(Date date, long hour) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + hour * 3600 * 1000);
		return c.getTime();
	}

	public static Date subtractHour(Date date, long hour) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - hour * 3600 * 1000);
		return c.getTime();
	}

	public static Date addDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	public static Date subtractDay(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	public static Date addSecond(Date date, long second) {
		return new Date(getMillis(date) + second * 1000);
	}

	public static Date subtractSecond(Date date, long second) {
		return new Date(getMillis(date) - second * 1000);
	}

	public static Date addMillis(Date date, long millis) {
		return new Date(getMillis(date) + millis);
	}

	public static Date subtractMillis(Date date, long millis) {
		return new Date(getMillis(date) - millis);
	}

	public static Date getDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return c.getTime();
	}

	public static boolean isBetween(Date date, Date date1, Date date2) {
		long m = DateUtil.getMillis(date);
		long m1 = DateUtil.getMillis(date1);
		long m2 = DateUtil.getMillis(date2);
		if (m1 <= m && m <= m2) {
			return true;
		}
		return false;
	}


	/**
	 * 取得消息的时间格式
	 * 
	 * @param messageDate
	 * @return
	 */
	public static String getDayMessage(Date messageDate) {
		Date currentDate = new Date();
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
		String today = format3.format(currentDate);

		String yesterday = format3.format(DateUtil.subtractDay(currentDate, 1));
		String qday = format3.format(DateUtil.subtractDay(currentDate, 2));

		String messageDay = format3.format(messageDate);

		if (messageDay.equalsIgnoreCase(today)) {
			return "今天";
		}
		if (messageDay.equalsIgnoreCase(yesterday)) {
			return "昨天";
		}
		if (messageDay.equalsIgnoreCase(qday)) {
			return "前天";
		}
		return DateUtil.format(messageDate, "yyyy-MM-dd");

	}
	/**
	 * 将字符串转换为日期
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStrToDate(String strDate) throws ParseException {
		return parse(strDate, "yyyy-MM-dd");

	}
	public static Date parseStrToDate2(String strDate) throws ParseException {
		return parse(strDate, "yyyy-MM-dd HH:mm");

	}
	/**
	 * 将字符串转换为小时
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static boolean parseStrToHourStr(String strDate) throws ParseException{
		//Pattern.compile("^[0-2]{1}[0-9]{0,1}[:][0-9]{1,2}$");
		strDate=strDate.replaceAll("：",":").replaceAll(":", ":");
		Pattern pattern = Pattern.compile("^(?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]$");
		Matcher matchName = pattern.matcher(strDate);
		return matchName.find();

    }

	/**
	 * 验证年月日是否合法
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
  public static boolean validateYear(String strDate) throws ParseException{
	    Pattern pattern = Pattern.compile("^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$");
		Matcher matchName = pattern.matcher(strDate);
		return matchName.find();
  }
	
	/**
	 * 获得当前日期年月日
	 * @return
	 * @throws ParseException 
	 */
	public static long currentDateTime() throws ParseException {
		String result="";
		TimeZone t = TimeZone.getTimeZone(defaultDisplayTimeZone);
		Locale l = new Locale("zh", "CN");
		Calendar c = Calendar.getInstance(t, l);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		result = sdf.format( new Date(c.getTimeInMillis()));
		return  DateUtil.getMillis(parseStrToDate(result));
	}

	/**
	 * 获得当前日期小时
	 * @return
	 * @throws ParseException 
	 */
	public static int currentDateHour() throws ParseException {
		TimeZone t = TimeZone.getTimeZone(defaultDisplayTimeZone);
		Locale l = new Locale("zh", "CN");
		Calendar c = Calendar.getInstance(t, l);
		int hour=c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	/**
	 * 判断字符串是否是数字
	 * @return
	 * @throws ParseException 
	 * */
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]*");  
	    return pattern.matcher(str).matches();     
	} 
	
	/**
	 * 获得当前日期分钟
	 * @return
	 * @throws ParseException 
	 */
	public static int currentDateMinute() throws ParseException {
		TimeZone t = TimeZone.getTimeZone(defaultDisplayTimeZone);
		Locale l = new Locale("zh", "CN");
		Calendar c = Calendar.getInstance(t, l);
		int minute=c.get(Calendar.MINUTE);
		return minute;
	}
	
    /**
	 * 获得当前日期的前一天
	 * @return
	 * @throws ParseException 
	 */
	public static String currentTime() throws ParseException {
		TimeZone t = TimeZone.getTimeZone(defaultDisplayTimeZone);
		Locale l = new Locale("zh", "CN");
		Calendar c = Calendar.getInstance(t, l);
		c.add(Calendar.DAY_OF_YEAR, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return  sdf.format( new Date(c.getTimeInMillis()));
	}
	
	public static int getCurrentHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY-1);
	}

	public static String get(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		String time = sdf.format(date);
		return time;
	}

	
}
