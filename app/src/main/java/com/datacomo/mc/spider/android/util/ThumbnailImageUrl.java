package com.datacomo.mc.spider.android.util;

import com.datacomo.mc.spider.android.enums.HeadSizeEnum;
import com.datacomo.mc.spider.android.enums.ImageSizeEnum;
import com.datacomo.mc.spider.android.enums.PostSizeEnum;

public class ThumbnailImageUrl {

	/**
	 * 获得头像缩略图Url
	 * 
	 * @param headUrl
	 * @param headSizeEnum
	 * @return
	 */
	public static String getThumbnailHeadUrl(String headUrl,
			HeadSizeEnum headSizeEnum) {
		if (null == headUrl || "".equals(headUrl) || !headUrl.contains(".")) {
			return headUrl;
		}
		String sizeStr = "_120_120";
		switch (headSizeEnum) {
		// case TWENTY_FOUR:
		// sizeStr = "_24_24";
		// break;
		// case THIRTY_TWO:
		// sizeStr = "_32_32";
		// break;
		// case FORTY:
		// sizeStr = "_40_40";
		// break;
		// case SIXTY:
		// sizeStr = "_60_60";
		// break;
		// case EIGHTY:
		// sizeStr = "_80_80";
		// break;
		// case ONE_HUNDRED:
		// sizeStr = "_100_100";
		// break;
		// case ONE_HUNDRED_AND_TWENTY:
		// sizeStr = "_120_120";
		// break;
		// case ONE_HUNDRED_AND_EIGHTY:
		// sizeStr = "_180_0";
		// break;
		case THREE_HUNDRED_AND_SIXTY:
			sizeStr = "_360_360";
			break;
		default:
			break;
		}
		headUrl = headUrl.substring(0, headUrl.lastIndexOf(".")) + sizeStr
				+ ".jpg";
		return headUrl;
	}

	/**
	 * 获得圈子海报缩略图Url
	 * 
	 * @param postUrl
	 * @param postSizeEnum
	 * @return
	 */
	public static String getThumbnailPostUrl(String postUrl,
			PostSizeEnum postSizeEnum) {
		if (null == postUrl || "".equals(postUrl) || !postUrl.contains(".")) {
			return postUrl;
		}
		String sizeStr = "_120_120";
		// switch (postSizeEnum) {
		// case TWENTY_FOUR:
		// sizeStr = "_24_24";
		// break;
		// case THIRTY_TWO:
		// sizeStr = "_32_32";
		// break;
		// case FORTY:
		// sizeStr = "_40_40";
		// break;
		// case SIXTY:
		// sizeStr = "_60_60";
		// break;
		// case EIGHTY:
		// sizeStr = "_80_80";
		// break;
		// case ONE_HUNDRED_AND_TWENTY:
		// sizeStr = "_120_120";
		// break;
		// case ONE_HUNDRED_AND_EIGHTY:
		// sizeStr = "_180_0";
		// break;
		// default:
		// break;
		// }
		postUrl = postUrl.substring(0, postUrl.lastIndexOf(".")) + sizeStr
				+ ".jpg";
		return postUrl;
	}

	/**
	 * 获得照片缩略图Url
	 * 
	 * @param imageUrl
	 * @param imageSizeEnum
	 * @return
	 */
	public static String getThumbnailImageUrl(String imageUrl,
			ImageSizeEnum imageSizeEnum) {
		if (null == imageUrl || "".equals(imageUrl) || !imageUrl.contains(".")) {
			return "";
		}
		String sizeStr = "_72_72";
		switch (imageSizeEnum) {
		// case SIXTY:
		// sizeStr = "_60_0";
		// break;
		case SEVENTY_TWO:
			sizeStr = "_72_72";
			break;
		// case ONE_HUNDRED_AND_TWENTY:
		// sizeStr = "_120_0";
		// break;
		// case ONE_HUNDRED_AND_EIGHTY:
		// sizeStr = "_180_0";
		// break;
		// case TWO_HUNDRED_AND_TWENTY:
		// sizeStr = "_220_0";
		// break;
		// case TWO_HUNDRED_AND_EIGHTY:
		// sizeStr = "_280_0";
		// break;
		case THREE_HUNDRED:
			sizeStr = "_300_0";
			break;
		// case FOUR_HUNDRED_AND_SIXTY:
		// sizeStr = "_460_0";
		// break;
		case EIGHT_HUNDRED:
			sizeStr = "_800_0";
			break;
		case ONE_THOUSAND_AND_TWENTY:
			sizeStr = "_1020_0";
			break;
		default:
			break;
		}
		imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(".")) + sizeStr
				+ ".jpg";
		return imageUrl;
	}

	/**
	 * 获得圈子海报缩略图Url
	 * 
	 * @param postUrl
	 * @param postSizeEnum
	 * @return
	 */
	public static String getThumbnailNoteUrl(String postUrl) {
		if (null == postUrl || "".equals(postUrl) || !postUrl.contains(".")) {
			return postUrl;
		}
		String sizeStr = "_680_0";
		postUrl = postUrl.substring(0, postUrl.lastIndexOf(".")) + sizeStr
				+ ".jpg";
		return postUrl;
	}

}
