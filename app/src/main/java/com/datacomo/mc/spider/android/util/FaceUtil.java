package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.R;

/**
 * 表情
 */
public class FaceUtil {
	// private static final String TAG = "FaceUtil";
	public final static int RES_BACK = R.drawable.face_delete;
	public final static int RES_NULL = R.drawable.nothing;
	public final static int[] FACE_RES_IDS = new int[] {
			R.drawable.face_2012_01, R.drawable.face_2012_02,
			R.drawable.face_2012_03, R.drawable.face_2012_04,
			R.drawable.face_2012_05, R.drawable.face_2012_06,
			R.drawable.face_2012_07, R.drawable.face_2012_08,
			R.drawable.face_2012_09, R.drawable.face_2012_10,
			R.drawable.face_2012_11, R.drawable.face_2012_12,
			R.drawable.face_2012_13, R.drawable.face_2012_14,
			R.drawable.face_2012_15, R.drawable.face_2012_16,
			R.drawable.face_2012_17, R.drawable.face_2012_18,
			R.drawable.face_2012_19, R.drawable.face_2012_20,
			R.drawable.face_2012_21, R.drawable.face_2012_22,
			R.drawable.face_2012_23, R.drawable.face_2012_24,
			R.drawable.face_2012_25, R.drawable.face_2012_26,
			R.drawable.face_2012_27, R.drawable.face_2012_28,
			R.drawable.face_2012_29, R.drawable.face_2012_30,
			R.drawable.face_2012_31, R.drawable.face_2012_32,
			R.drawable.face_2012_33, R.drawable.face_2012_34,
			R.drawable.face_2012_35, R.drawable.face_2012_36,
			R.drawable.face_2012_37, R.drawable.face_2012_38,
			R.drawable.face_2012_39, R.drawable.face_2012_40,
			R.drawable.face_2012_41, R.drawable.face_2012_42,
			R.drawable.face_2012_43, R.drawable.face_2012_44,
			R.drawable.face_2012_45, R.drawable.face_2012_46,
			R.drawable.face_2012_47, R.drawable.face_2012_48,
			R.drawable.face_2012_49, R.drawable.face_2012_50,
			R.drawable.face_2012_51, R.drawable.face_2012_52,
			R.drawable.face_2012_53, R.drawable.face_2012_54,
			R.drawable.face_2012_55, R.drawable.face_2012_56,
			R.drawable.face_2012_57, R.drawable.face_2012_58,
			R.drawable.face_2012_59, R.drawable.face_2012_60,
			R.drawable.face_2012_61, R.drawable.face_2012_62,
			R.drawable.face_2012_63, R.drawable.face_2012_64,
			R.drawable.face_2012_65, R.drawable.face_2012_66,
			R.drawable.face_2012_67, R.drawable.face_2012_68,
			R.drawable.face_2012_69, R.drawable.face_2012_70,
			R.drawable.face_2012_71, R.drawable.face_2012_72,
			R.drawable.face_2012_73, R.drawable.face_2012_74,
			R.drawable.face_2012_75, R.drawable.face_2012_76,
			R.drawable.face_2012_77, R.drawable.face_2012_78,
			R.drawable.face_2012_79, R.drawable.face_2012_80,
			R.drawable.face_2012_81, R.drawable.face_2012_82,
			R.drawable.face_2012_83, R.drawable.face_2012_84 };

	public final static String TEXT_BACK = "[BACK]";
	public final static String TEXT_NULL = "[NULL]";
	public final static String[] FACE_TEXTS = new String[] { "[鄙视]", "[闭嘴]",
			"[大哭]", "[呆]", "[得意]", "[尴尬]", "[害羞]", "[好色]", "[坏笑]", "[惊]",
			"[可爱]", "[流泪]", "[怒]", "[怒骂]", "[亲亲]", "[偷笑]", "[吐]", "[微笑]",
			"[委屈]", "[嘘]", "[疑惑]", "[晕]", "[傲慢]", "[白眼]", "[呲牙]", "[大兵]",
			"[调皮]", "[哈欠]", "[饥饿]", "[紧张]", "[惊恐]", "[酷]", "[困]", "[冷汗]",
			"[流汗]", "[难过]", "[努力]", "[糗大了]", "[傻笑]", "[睡觉]", "[折磨]", "[衰]",
			"[骷髅]", "[敲打]", "[再见]", "[擦汗]", "[抠鼻]", "[鼓掌]", "[右哼哼]", "[快哭了]",
			"[阴险]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[咖啡]", "[饭]",
			"[猪头]", "[玫瑰]", "[凋谢]", "[示爱]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]",
			"[炸弹]", "[刀]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]",
			"[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]",
			"[NO]", "[OK]" };

	/**
	 * 表情字符替换成res标签形式。
	 * 
	 * @param str
	 * @return
	 */
	public final static String faceToHtml(String str) {
		// L.i(TAG, "faceToHtml str=" + str);
		if (str == null || "".equals(str)) {
			return "";
		}
		for (int i = 0; i < FACE_TEXTS.length; i++) {
			String face = FACE_TEXTS[i];
			// L.i(TAG, "faceToHtml face=" + face);
			if (str.contains(face)) {
				String faceStr = "<img src='" + FACE_RES_IDS[i]
						+ "' vertical-align='middle'/>";
				// L.i(TAG, "faceToHtml faceStr=" + faceStr);
				str = str.replace(face, faceStr);
			}
		}
		return str;
	}

	/**
	 * 表情字符替换成assets标签形式。
	 * 
	 * @param str
	 * @return
	 */
	public final static String faceToAssetsHtml(String str) {
		// L.i(TAG, "faceToHtml str=" + str);
		if (str == null || "".equals(str)) {
			return "";
		}
		for (int i = 0; i < FACE_TEXTS.length; i++) {
			String face = FACE_TEXTS[i];
			// L.i(TAG, "faceToHtml face=" + face);
			if (str.contains(face)) {
				// String faceStr = "<img src='file:///android_asset/"
				// + FACE_ASSETS[i]
				// + "' width='24' height='24' vertical-align='middle'/>";
				String faceStr = "<img src='http://img.yuuquu.com/m6/default/face/"
						+ FACE_URL[i]
						+ "' style='align:center;vertical-align: middle;'/>";
				str = str.replace(face, faceStr);
			}
		}
		return str;
	}

	public final static String[] FACE_URL = new String[] { "face_2012_04.gif",
			"face_2012_05.gif", "face_2012_09.gif", "face_2012_11.gif",
			"face_2012_12.gif", "face_2012_13.gif", "face_2012_15.gif",
			"face_2012_17.gif", "face_2012_18.gif", "face_2012_20.gif",
			"face_2012_21.gif", "face_2012_22.gif", "face_2012_24.gif",
			"face_2012_25.gif", "face_2012_28.gif", "face_2012_31.gif",
			"face_2012_33.gif", "face_2012_34.gif", "face_2012_35.gif",
			"face_2012_36.gif", "face_2012_37.gif", "face_2012_38.gif",
			"face_2012_41.gif", "face_2012_42.gif", "face_2012_43.gif",
			"face_2012_44.gif", "face_2012_45.gif", "face_2012_46.gif",
			"face_2012_47.gif", "face_2012_48.gif", "face_2012_49.gif",
			"face_2012_50.gif", "face_2012_51.gif", "face_2012_52.gif",
			"face_2012_53.gif", "face_2012_54.gif", "face_2012_55.gif",
			"face_2012_56.gif", "face_2012_57.gif", "face_2012_58.gif",
			"face_2012_59.gif", "face_2012_61.gif", "face_2012_62.gif",
			"face_2012_63.gif", "face_2012_64.gif", "face_2012_65.gif",
			"face_2012_66.gif", "face_2012_67.gif", "face_2012_68.gif",
			"face_2012_69.gif", "face_2012_70.gif", "face_2012_71.gif",
			"face_2012_72.gif", "face_2012_73.gif", "face_2012_74.gif",
			"face_2012_75.gif", "face_2012_76.gif", "face_2012_77.gif",
			"face_2012_78.gif", "face_2012_79.gif", "face_2012_80.gif",
			"face_2012_81.gif", "face_2012_82.gif", "face_2012_83.gif",
			"face_2012_84.gif", "face_2012_85.gif", "face_2012_86.gif",
			"face_2012_77.gif", "face_2012_88.gif", "face_2012_89.gif",
			"face_2012_90.gif", "face_2012_91.gif", "face_2012_92.gif",
			"face_2012_93.gif", "face_2012_94.gif", "face_2012_95.gif",
			"face_2012_96.gif", "face_2012_97.gif", "face_2012_98.gif",
			"face_2012_99.gif", "face_2012_100.gif", "face_2012_101.gif",
			"face_2012_102.gif", "face_2012_103.gif" };

	public final static String[] FACE_ASSETS = new String[] {
			"face_2012_01.gif", "face_2012_02.gif", "face_2012_03.gif",
			"face_2012_04.gif", "face_2012_05.gif", "face_2012_06.gif",
			"face_2012_07.gif", "face_2012_08.gif", "face_2012_09.gif",
			"face_2012_10.gif", "face_2012_11.gif", "face_2012_12.gif",
			"face_2012_13.gif", "face_2012_14.gif", "face_2012_15.gif",
			"face_2012_16.gif", "face_2012_17.gif", "face_2012_18.gif",
			"face_2012_19.gif", "face_2012_20.gif", "face_2012_21.gif",
			"face_2012_22.gif", "face_2012_23.gif", "face_2012_24.gif",
			"face_2012_25.gif", "face_2012_26.gif", "face_2012_27.gif",
			"face_2012_28.gif", "face_2012_29.gif", "face_2012_30.gif",
			"face_2012_31.gif", "face_2012_32.gif", "face_2012_33.gif",
			"face_2012_34.gif", "face_2012_35.gif", "face_2012_36.gif",
			"face_2012_37.gif", "face_2012_38.gif", "face_2012_39.gif",
			"face_2012_40.gif", "face_2012_41.gif", "face_2012_42.gif",
			"face_2012_43.gif", "face_2012_44.gif", "face_2012_45.gif",
			"face_2012_46.gif", "face_2012_47.gif", "face_2012_48.gif",
			"face_2012_49.gif", "face_2012_50.gif", "face_2012_51.gif",
			"face_2012_52.gif", "face_2012_53.gif", "face_2012_54.gif",
			"face_2012_55.gif", "face_2012_56.gif", "face_2012_57.gif",
			"face_2012_58.gif", "face_2012_59.gif", "face_2012_60.gif",
			"face_2012_61.gif", "face_2012_62.gif", "face_2012_63.gif",
			"face_2012_64.gif", "face_2012_65.gif", "face_2012_66.gif",
			"face_2012_67.gif", "face_2012_68.gif", "face_2012_69.gif",
			"face_2012_70.gif", "face_2012_71.gif", "face_2012_72.gif",
			"face_2012_73.gif", "face_2012_74.gif", "face_2012_75.gif",
			"face_2012_76.gif", "face_2012_77.gif", "face_2012_78.gif",
			"face_2012_79.gif", "face_2012_80.gif", "face_2012_81.gif",
			"face_2012_82.gif", "face_2012_83.gif", "face_2012_84.gif" };
}
