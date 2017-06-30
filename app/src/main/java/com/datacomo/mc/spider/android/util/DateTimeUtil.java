package com.datacomo.mc.spider.android.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {
	public static long serverTime = 0L;
	
	/**
	 * 对取到的时间进行操作A类： （1）今天：显示“今天 时：分”，如： 今天 10:26 （2）今年，且非今天：显示“x月x日 时：分”，如：
	 * 8月29日 10:26 （3）非今年：显示“x年x月x日 时：分”，如： 2010年8月29日 10:26 【注】： 年：用4位数字；
	 * 月：除10月、11月、12月外的月份用1位数字，避免出现08月； 日：1-9日用1位数字，避免出现02日； 时：统一用两位数字；
	 * 分：统一用两位数字。 以下列表页及内容页用A类规范：
	 * （1）圈子：圈博列表、圈子共享文件列表、查看圈博、查看圈子共享文件、查看圈子图片、创建圈子的时间
	 * （搜索结果页、圈子信息页）、圈子成员列表（加入圈子时间）； （2）通知/私信/招呼：通知中的各种列表、短信列表、查看和某人的往来私信、招呼列表。
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String cTimeFormat(long time) {
		String result = "";
		Date date = null;
		if (serverTime != 0L) {
			date = new Date(serverTime);
		} else {
			date = new Date();
		}

		Date date2 = new Date(time);
		if (date.getYear() == date2.getYear()) {
			if (date.getMonth() == date2.getMonth()
					&& date.getDate() == date2.getDate()) {
				result = "今天 " + new SimpleDateFormat("HH:mm").format(date2);
			} else {
				if (date2.getMonth() > 9) {
					if (date2.getDate() > 9) {
						result = new SimpleDateFormat("MM月dd日 HH:mm")
								.format(date2);
					} else {
						result = new SimpleDateFormat("MM月d日 HH:mm")
								.format(date2);
					}
				} else {
					if (date2.getDate() > 9) {
						result = new SimpleDateFormat("M月dd日 HH:mm")
								.format(date2);
					} else {
						result = new SimpleDateFormat("M月d日 HH:mm")
								.format(date2);
					}
				}
			}
		} else {
			if (date2.getMonth() > 9) {
				if (date2.getDate() > 9) {
					result = new SimpleDateFormat("yyyy年MM月dd日 HH:mm")
							.format(date2);
				} else {
					result = new SimpleDateFormat("yyyy年MM月d日 HH:mm")
							.format(date2);
				}
			} else {
				if (date2.getDate() > 9) {
					result = new SimpleDateFormat("yyyy年M月dd日 HH:mm")
							.format(date2);
				} else {
					result = new SimpleDateFormat("yyyy年M月d日 HH:mm")
							.format(date2);
				}
			}
		}
		return result;
	}

	/**
	 * 对取到的时间进行操作A类： （1）今天：显示“今天 时：分”，如： 今天 10:26 （2）今年，且非今天：显示“x月x日 时：分”，如：
	 * 8月29日 10:26 （3）非今年：显示“x年x月x日 时：分”，如： 2010年8月29日 10:26 【注】： 年：用4位数字；
	 * 月：除10月、11月、12月外的月份用1位数字，避免出现08月； 日：1-9日用1位数字，避免出现02日； 时：统一用两位数字；
	 * 分：统一用两位数字。 以下列表页及内容页用A类规范：
	 * （1）圈子：圈博列表、圈子共享文件列表、查看圈博、查看圈子共享文件、查看圈子图片、创建圈子的时间
	 * （搜索结果页、圈子信息页）、圈子成员列表（加入圈子时间）； （2）通知/私信/招呼：通知中的各种列表、短信列表、查看和某人的往来私信、招呼列表。
	 * 
	 * @param time
	 * @return
	 */
	public static String aTimeFormat(long time) {
		return mailTimeFormat(time);
		// String result = "";
		// Date date = null;
		// if (serverTime != 0L) {
		// date = new Date(serverTime);
		// } else {
		// date = new Date();
		// }
		//
		// Date date2 = new Date(time);
		// if (date.getYear() == date2.getYear()) {
		// if (date.getMonth() == date2.getMonth()
		// && date.getDate() == date2.getDate()) {
		// result = "今天 " + new SimpleDateFormat("HH:mm").format(date2);
		// } else {
		// if (date2.getMonth() > 9) {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("MM月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("MM月d日 HH:mm")
		// .format(date2);
		// }
		// } else {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("M月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("M月d日 HH:mm")
		// .format(date2);
		// }
		// }
		// }
		// } else {
		// if (date2.getMonth() > 9) {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("yyyy年MM月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("yyyy年MM月d日 HH:mm")
		// .format(date2);
		// }
		// } else {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("yyyy年M月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("yyyy年M月d日 HH:mm")
		// .format(date2);
		// }
		// }
		// }
		// return result;
	}

	/**
	 * 对取到的时间进行操作B类： （1）今天，且1-59秒前：显示“刚刚”
	 * （2）今天，且1-59分钟前：显示“x分钟前”，1-9分钟用1位数，其它用两位数，避免出现06分钟前，如：9分钟前
	 * （3）今天，非1小时内，且24点前：显示“x小时前”，1-9小时用1位数，其它用两位数，避免出现06小时前，如：9小时前
	 * （4）今年，且非今天：显示“x月x日 时：分”，如： 8月29日 10:26 （5）非今年：显示“x年x月x日 时：分”，如：
	 * 2010年8月29日 10:26 【注】（4）、（5）显示规则请参考A类的【注】 以下列表页及内容页用B类规范：
	 * 动态墙：朋友动态墙、圈子动态墙、随便看看（公开圈子动态墙）、个人动态墙（微领地）。
	 * 
	 * @param time
	 * @return
	 */
	public static String bTimeFormat(long time) {
		return mailTimeFormat(time);
		// String result = "";
		// Date date = null;
		// if (serverTime != 0L) {
		// date = new Date(serverTime);
		// } else {
		// date = new Date();
		// }
		// Date date2 = new Date(time);
		// if (date.getYear() == date2.getYear()) {
		// if (date.getMonth() == date2.getMonth()
		// && date.getDate() == date2.getDate()) {
		// long now = System.currentTimeMillis();
		// long timeLag = now - time;
		// if (timeLag < 1000 * 60) {
		// result = "刚刚";
		// } else if (timeLag < 1000 * 60 * 60) {
		// int minute = (int) (timeLag / (1000 * 60));
		// result = minute + "分钟前";
		// } else {
		// int hour = (int) (timeLag / (1000 * 60 * 60));
		// result = hour + "小时前";
		// }
		// } else {
		// if (date2.getMonth() > 9) {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("MM月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("MM月d日 HH:mm")
		// .format(date2);
		// }
		// } else {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("M月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("M月d日 HH:mm")
		// .format(date2);
		// }
		// }
		// }
		// } else {
		// if (date2.getMonth() > 9) {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("yyyy年MM月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("yyyy年MM月d日 HH:mm")
		// .format(date2);
		// }
		// } else {
		// if (date2.getDate() > 9) {
		// result = new SimpleDateFormat("yyyy年M月dd日 HH:mm")
		// .format(date2);
		// } else {
		// result = new SimpleDateFormat("yyyy年M月d日 HH:mm")
		// .format(date2);
		// }
		// }
		// }
		// return result;
	}

	/**
	 * 对取到的时间进行操作B类： （1）今天，且1-59秒前：显示“刚刚”
	 * （2）今天，且1-59分钟前：显示“x分钟前”，1-9分钟用1位数，其它用两位数，避免出现06分钟前，如：9分钟前
	 * （3）今天，非1小时内，且24点前：显示“x小时前”，1-9小时用1位数，其它用两位数，避免出现06小时前，如：9小时前
	 * （4）今年，且非今天：显示“x月x日 时：分”，如： 8月29日 10:26 （5）非今年：显示“x年x月x日 时：分”，如：
	 * 2010年8月29日 10:26 【注】（4）、（5）显示规则请参考A类的【注】 以下列表页及内容页用B类规范： 当年显示月-日，例如：04-23
	 * 往年显示年-月-日，例如：13-02-16 “刚刚，n分钟前，n小时前”等用语保持不变。
	 * 
	 * @param time
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String mailTimeFormat(long time) {
		String result = "";
		Date date = null;
		if (serverTime != 0L) {
			date = new Date(serverTime);
		} else {
			date = new Date();
		}
		Date date2 = new Date(time);
		if (date.getYear() == date2.getYear()) {
			if (date.getMonth() == date2.getMonth()
					&& date.getDate() == date2.getDate()) {
				long now = System.currentTimeMillis();
				long timeLag = now - time;
				if (timeLag < 1000 * 60) {
					result = "刚刚";
				} else if (timeLag < 1000 * 60 * 60) {
					int minute = (int) (timeLag / (1000 * 60));
					result = minute + "分钟前";
				} else {
					int hour = (int) (timeLag / (1000 * 60 * 60));
					result = hour + "小时前";
				}
			} else {
				result = new SimpleDateFormat("MM-dd").format(date2);
			}
		} else {
			result = new SimpleDateFormat("yy-MM-dd").format(date2);
		}
		return result;
	}

	public static long getLongTime(String timeStr) {
		long time = System.currentTimeMillis();
		try {
			time = Long.valueOf(timeStr);
		} catch (Exception e) {
			// e.printStackTrace();
			try {
				JSONObject json = new JSONObject(timeStr);
				time = json.getLong("time");
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
		}
		return time;
	}
}
