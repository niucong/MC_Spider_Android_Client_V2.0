package com.datacomo.mc.spider.android.enums;

/**
 * 图片类型——默认DEFAULTIMAGE、缩略THUMBNAILIMAGE、原始ORIGINALIMAGE
 * 
 * @author datacomo-
 * 
 */
public enum ImageStateEnum {

	/**
	 * 图片正在加载
	 */
	LOADING,
	/**
	 * 图片加载完成
	 */
	LOADED,
	/**
	 * 图片加载失败
	 */
	FAILED,
	/**
	 * 默认加载状态
	 */
	DEFAULT
}
