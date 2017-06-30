package com.datacomo.mc.spider.android.enums;

/**
 * 图片类型——默认DEFAULTIMAGE、缩略THUMBNAILIMAGE、原始ORIGINALIMAGE
 * 
 * @author datacomo-
 * 
 */
public enum ImageTypeEnum {

	/**
	 * 默认图片
	 */
	DEFAULTIMAGE, 
	/**
	 * 需要下载的图片
	 */
	ORIGINALIMAGEDOWNLOAD, 
	/**
	 * 原始图片
	 */
	ORIGINALIMAGE, 
	/**
	 * 图片加载失败
	 */
	FAILED
}
