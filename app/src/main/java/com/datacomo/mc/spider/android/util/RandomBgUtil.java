package com.datacomo.mc.spider.android.util;

import java.util.Random;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.url.L;

public class RandomBgUtil {
	private final String TAG = "RandomBgUtil";

	public final int[] map_RandomBg = new int[] {
			R.drawable.bg_color_deeppurple, R.drawable.bg_color_green,
			R.drawable.bg_color_orange, R.drawable.bg_color_purple,
			R.drawable.bg_color_seablue,
			R.drawable.bg_color_shallowblackishgreen,
			R.drawable.bg_color_skyblue, R.drawable.bg_color_yellow,
			R.drawable.bg_olivegreen };

	public int getRandomBgId() {
		Random random = new Random();
		int temp = (int) (random.nextFloat() * 10);
		L.d(TAG, "temp" + temp);
		if (temp == 9) {
			return getRandomBgId();
		} else {
			return map_RandomBg[temp];
		}
	}
}
