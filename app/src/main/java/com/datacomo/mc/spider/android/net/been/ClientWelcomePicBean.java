package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class ClientWelcomePicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708184739072998799L;
	/*
	 * 图片Id
	 */
	private int picId;
	private String headUrl;
	private String headPath;
	private String uploadTime;
	private String updateTime;
	private String headFormat;

	public int getPicId() {
		return picId;
	}

	public void setPicId(int picId) {
		this.picId = picId;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getHeadPath() {
		return headPath;
	}

	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getHeadFormat() {
		return headFormat;
	}

	public void setHeadFormat(String headFormat) {
		this.headFormat = headFormat;
	}

}
