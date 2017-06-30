/**
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * Copyright 2006-2011 DataComo Communications Technology INC.
 * 
 * This source file is a part of MC_Spider_Bean_1.0 project. 
 * date: 2011-5-18
 *
 */
package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

/**
 * 圈子内设置bean
 * 
 * @author cailikun
 * @date 2011-5-18 上午11:33:08
 * @version v1.0.0
 */
public class GroupSetBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4651428709295191469L;

	/**
	 * 非成员是否开放浏览话题 true：开放 false：不开放
	 */
	private boolean topicViewSet;

	/**
	 * 非成员是否开放浏览文件 true：开放 false：不开放
	 */
	private boolean fileViewSet;

	/**
	 * 非成员是否开放浏览照片 true：开放 false：不开放
	 */
	private boolean photoViewSet;

	/**
	 * 非成员是否开放浏览视频 true：开放 false：不开放
	 */
	private boolean videoViewSet;

	/**
	 * 非成员是否开放浏览成员列表 true：开放 false：不开放
	 */
	private boolean leaguerViewSet;

	// /**
	// * 邀请成员设置 true：所有成员 false：创建者和管理员
	// */
	// private boolean inviteLeaguerSet;
	/**
	 * 邀请成员设置 1：所有成员可邀请（不能申请） 2：创建者和管理员可邀请（不能申请） 3：可以申请加入(管理员审核，包括可以邀请加入)；
	 * 4：自由加入（可邀请加入） 备：外部社区时，1、2均可申请加入。
	 */
	private int inviteLeaguerSet;

	/**
	 * 圈子类型:1 - 公开 2 - 私密
	 */
	private String openStatus;

	/**
	 * 父级圈子编号
	 */
	private int fatherGroupId;

	/**
	 * 父级圈子名称
	 */
	private String fatherGroupName;

	/**
	 * 父级圈子类型:1 - 公开 2 - 私密
	 */
	private String fatherOpenStatus;

	/**
	 * 分享文件设置 true：所有成员 false：创建者和管理员
	 */
	private boolean shareFileSet;

	/**
	 * 分享照片设置 true：所有成员 false：创建者和管理员
	 */
	private boolean sharePhotoSet;

	/**
	 * 分享话题设置 true：所有成员 false：创建者和管理员
	 */
	private boolean shareTopicSet;

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public boolean getTopicViewSet() {
		return topicViewSet;
	}

	public void setTopicViewSet(boolean topicViewSet) {
		this.topicViewSet = topicViewSet;
	}

	public boolean getFileViewSet() {
		return fileViewSet;
	}

	public void setFileViewSet(boolean fileViewSet) {
		this.fileViewSet = fileViewSet;
	}

	public boolean getPhotoViewSet() {
		return photoViewSet;
	}

	public void setPhotoViewSet(boolean photoViewSet) {
		this.photoViewSet = photoViewSet;
	}

	public boolean getVideoViewSet() {
		return videoViewSet;
	}

	public void setVideoViewSet(boolean videoViewSet) {
		this.videoViewSet = videoViewSet;
	}

	public boolean getLeaguerViewSet() {
		return leaguerViewSet;
	}

	public void setLeaguerViewSet(boolean leaguerViewSet) {
		this.leaguerViewSet = leaguerViewSet;
	}

	// public boolean getInviteLeaguerSet() {
	// return inviteLeaguerSet;
	// }
	//
	// public void setInviteLeaguerSet(boolean inviteLeaguerSet) {
	// this.inviteLeaguerSet = inviteLeaguerSet;
	// }

	public int getFatherGroupId() {
		return fatherGroupId;
	}

	public int getInviteLeaguerSet() {
		return inviteLeaguerSet;
	}

	public void setInviteLeaguerSet(int inviteLeaguerSet) {
		this.inviteLeaguerSet = inviteLeaguerSet;
	}

	public void setFatherGroupId(int fatherGroupId) {
		this.fatherGroupId = fatherGroupId;
	}

	public String getFatherGroupName() {
		return fatherGroupName;
	}

	public void setFatherGroupName(String fatherGroupName) {
		this.fatherGroupName = fatherGroupName;
	}

	public String getFatherOpenStatus() {
		return fatherOpenStatus;
	}

	public void setFatherOpenStatus(String fatherOpenStatus) {
		this.fatherOpenStatus = fatherOpenStatus;
	}

	public boolean getShareFileSet() {
		return shareFileSet;
	}

	public void setShareFileSet(boolean shareFileSet) {
		this.shareFileSet = shareFileSet;
	}

	public boolean getSharePhotoSet() {
		return sharePhotoSet;
	}

	public void setSharePhotoSet(boolean sharePhotoSet) {
		this.sharePhotoSet = sharePhotoSet;
	}

	public boolean getShareTopicSet() {
		return shareTopicSet;
	}

	public void setShareTopicSet(boolean shareTopicSet) {
		this.shareTopicSet = shareTopicSet;
	}

}
