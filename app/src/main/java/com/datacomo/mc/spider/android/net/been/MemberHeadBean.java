package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberHeadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// /**
	// * 社员编号
	// */
	// private int memberId;
	// /**
	// * 头像表主键
	// */
	// private int headId;
	/**
	 * 头像前缀
	 */
	private String headUrl;
	/**
	 * 原图路径
	 */
	private String headPath;
	private String fullPath;

	// /**
	// * 头像格式
	// */
	// private String headFormat;
	// /**
	// * 是否为默认头像:1：是 2：不是
	// */
	// private int headStatus;
	// /**
	// * 上传时间
	// */
	// private String uploadTime;
	// /**
	// * 更新时间
	// */
	// private String updateTime;

	// public int getMemberId() {
	// return memberId;
	// }
	//
	// public void setMemberId(int memberId) {
	// this.memberId = memberId;
	// }
	//
	// public int getHeadId() {
	// return headId;
	// }
	//
	// public void setHeadId(int headId) {
	// this.headId = headId;
	// }

	public String getHeadUrl() {
		if (headUrl == null) {
			headUrl = "";
		}
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

	// public String getHeadFormat() {
	// return headFormat;
	// }
	//
	// public void setHeadFormat(String headFormat) {
	// this.headFormat = headFormat;
	// }
	//
	// public int getHeadStatus() {
	// return headStatus;
	// }
	//
	// public void setHeadStatus(int headStatus) {
	// this.headStatus = headStatus;
	// }
	//
	// public String getUploadTime() {
	// return uploadTime;
	// }
	//
	// public void setUploadTime(String uploadTime) {
	// this.uploadTime = uploadTime;
	// }
	//
	// public String getUpdateTime() {
	// return updateTime;
	// }
	//
	// public void setUpdateTime(String updateTime) {
	// this.updateTime = updateTime;
	// }

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFullHeadPath() {
		if (fullPath != null) {
			return fullPath;
		}
		return headUrl + headPath;
	}

}
