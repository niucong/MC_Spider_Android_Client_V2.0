package com.datacomo.mc.spider.android.net.been;
//package com.datacomo.mc.spider.android.net.been;
//
//import java.io.Serializable;
//
//public class Date implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8323007683460832657L;
//
//	// private int nanos;// 0,
//	private long time;// 523810800000,
//
//	// private int minutes;// 0,
//	// private int seconds;// 0,
//	// private int hours;// 0,
//	// private int month;// 7,
//	// private int year;// 86,
//	// private int timezoneOffset;// -540,
//	// private int day;// 5,
//	// private int date;// 8
//
//	// public int getNanos() {
//	// return nanos;
//	// }
//	//
//	// public void setNanos(int nanos) {
//	// this.nanos = nanos;
//	// }
//
//	public long getTime() {
//		return time;
//	}
//
//	public void setTime(long time) {
//		this.time = time;
//	}
//
//	// public int getMinutes() {
//	// return minutes;
//	// }
//	//
//	// public void setMinutes(int minutes) {
//	// this.minutes = minutes;
//	// }
//	//
//	// public int getSeconds() {
//	// return seconds;
//	// }
//	//
//	// public void setSeconds(int seconds) {
//	// this.seconds = seconds;
//	// }
//	//
//	// public int getHours() {
//	// return hours;
//	// }
//	//
//	// public void setHours(int hours) {
//	// this.hours = hours;
//	// }
//	//
//	// public int getMonth() {
//	// return month;
//	// }
//	//
//	// public void setMonth(int month) {
//	// this.month = month;
//	// }
//	//
//	// public int getYear() {
//	// return year;
//	// }
//	//
//	// public void setYear(int year) {
//	// this.year = year;
//	// }
//	//
//	// public int getTimezoneOffset() {
//	// return timezoneOffset;
//	// }
//	//
//	// public void setTimezoneOffset(int timezoneOffset) {
//	// this.timezoneOffset = timezoneOffset;
//	// }
//	//
//	// public int getDay() {
//	// return day;
//	// }
//	//
//	// public void setDay(int day) {
//	// this.day = day;
//	// }
//	//
//	// public int getDate() {
//	// return date;
//	// }
//	//
//	// public void setDate(int date) {
//	// this.date = date;
//	// }
//
//	// /**
//	// * 年月日时分秒 yyyy-MM-dd HH:mm:ss
//	// *
//	// * @return
//	// */
//	// public String getYmdhmsDate() {
//	// String actionTime = ConstantUtil.YYYYMMDDHHMMSS
//	// .format(new java.util.Date(time));
//	// return actionTime;
//	// }
//
//	// /**
//	// * 年月日时分 yyyy-MM-dd HH:mm
//	// *
//	// * @return
//	// */
//	// public String getYmdhmDate() {
//	// String actionTime = ConstantUtil.YYYYMMDDHHMM
//	// .format(new java.util.Date(time));
//	// return actionTime;
//	// }
//
//	// /**
//	// * 年月日 yyyy-MM-dd
//	// *
//	// * @return
//	// */
//	// public String getYMdDate() {
//	// String actionTime = (year + 1900) + "-" + (month + 1) + "-" + date;
//	// // ConstantUtil.YYYYMMDD.format(new java.util.Date(time));
//	// return actionTime;
//	// }
//
//	// /**
//	// * 月日时分 MM-dd HH:mm
//	// *
//	// * @return
//	// */
//	// public String getMdhmDate() {
//	// String actionTime = ConstantUtil.MMDDHHMM.format(new java.util.Date(
//	// time));
//	// return actionTime;
//	// }
//	//
//	// /**
//	// * 动态时间格式
//	// *
//	// * @return
//	// */
//	// public String getTrendDate() {
//	// String actionTime = null;
//	// long currentTime = System.currentTimeMillis();
//	// long timeLag = currentTime - time;
//	// if (timeLag < 1000 * 60) {
//	// actionTime = "刚刚";
//	// return actionTime;
//	// } else if (timeLag < 1000 * 60 * 60) {
//	// int minute = (int) (timeLag / (1000 * 60));
//	// actionTime = minute + "分钟前";
//	// return actionTime;
//	// } else if (timeLag < 1000 * 60 * 60 * 24) {
//	// int hour = (int) (timeLag / (1000 * 60 * 60));
//	// actionTime = hour + "小时前";
//	// return actionTime;
//	// }
//	// SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");
//	// if (!YYYY.format(new java.util.Date(time)).equals(
//	// YYYY.format(new java.util.Date()))) {
//	// return ConstantUtil.YYYYMMDDHHMM.format(new java.util.Date(time));
//	// }
//	// return ConstantUtil.MMDDHHMM.format(new java.util.Date(time));
//	// }
// }
