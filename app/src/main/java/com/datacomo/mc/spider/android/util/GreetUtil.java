package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.R;

/**
 * 招呼
 */
public class GreetUtil {
	// private static final String TAG = "GreetUtil";

	public final static int[] GREET_RES_IDS = new int[] {
			R.drawable.motion_2424_01, R.drawable.motion_2424_02,
			R.drawable.motion_2424_03, R.drawable.motion_2424_04,
			R.drawable.motion_2424_05, R.drawable.motion_2424_06,
			R.drawable.motion_2424_07, R.drawable.motion_2424_08,
			R.drawable.motion_2424_09, R.drawable.motion_2424_10,
			R.drawable.motion_2424_11, R.drawable.motion_2424_12,
			R.drawable.motion_2424_13, R.drawable.motion_2424_14,
			R.drawable.motion_2424_15, R.drawable.motion_2424_16,
			R.drawable.motion_2424_17, R.drawable.motion_2424_18,
			R.drawable.motion_2424_19, R.drawable.motion_2424_20 };

	public final static int[] GREET_CONFIG_ID = new int[] { 1, 2, 3, 4, 5, 6,
			7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

	public final static String[] GREET_TITLE = new String[] { "微笑", "抛媚眼",
			"做鬼脸", "飞吻", "喝杯清酒", "干杯", "做马杀鸡", "Give me 5", "摸一摸", "邀舞", "捏捏脸",
			"葵花点穴手", "挠胳肢窝", "伸臭脚丫子", "嚼大蒜亲吻", "顶您个肺", "扮贞子", "拿拖鞋拍他", "打发他点钱",
			"鄙视" };

	public final static String[] GREET_TEXTS = new String[] { "您向[name]微微一笑",
			"您冲[name]抛了个媚眼", "您冲[name]做了个鬼脸", "您向[name]抛了个飞吻", "您邀请[name]喝杯清酒",
			"您向[name]干杯", "您殷勤的向[name]做马杀鸡", "您热情地向[name]Give me five (击掌)",
			"您对[name]摸了摸头", "您向[name]邀请共舞", "您对[name]捏了捏脸",
			"您向[name]施展了:葵花点穴手", "您对[name]挠了挠胳肢窝", "您冲[name]伸出了臭脚丫子",
			"您冲[name]嚼大蒜亲吻", "您向[name]说:顶你个肺", "您对[name]扮贞子飘过",
			"您拿拖鞋狠狠拍了[name]一顿", "您大发善心打发了[name]几个大洋", "您向[name]送去了严重鄙视" };

	/**
	 * 表情字符替换成assets标签形式。
	 * 
	 * @param str
	 * @return
	 */
	public final static String motionToAssetsHtml(int i) {
		return "<img src='file:///android_asset/" + MOTION_ASSETS[i]
				+ "' style='width:24px;height:24px;' / >";
	}

	public final static String[] MOTION_ASSETS = new String[] {
			"motion_3232_01.gif", "motion_3232_02.gif", "motion_3232_03.gif",
			"motion_3232_04.gif", "motion_3232_05.gif", "motion_3232_06.gif",
			"motion_3232_07.gif", "motion_3232_08.gif", "motion_3232_09.gif",
			"motion_3232_10.gif", "motion_3232_11.gif", "motion_3232_12.gif",
			"motion_3232_13.gif", "motion_3232_14.gif", "motion_3232_15.gif",
			"motion_3232_16.gif", "motion_3232_17.gif", "motion_3232_18.gif",
			"motion_3232_19.gif", "motion_3232_20.gif" };
}
