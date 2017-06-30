package com.datacomo.mc.spider.android.net.been;

public class GroupLeaguerPermissionBean {
	/**
	 * 圈子编号
	 */
	private int groupId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 成员与圈子之间的关系
	 */
	private String leaguerStatus;
	/**
	 * 话题列表是否可见 true：可见 false：不可见
	 */
	private boolean isTopicViewSetValid;
	/**
	 * 文件列表是否可见 true：可见 false：不可见
	 */
	private boolean isFileViewSetValid;
	/**
	 * 照片列表是否可见 true：可见 false：不可见
	 */
	private boolean isPhotoViewSetValid;
	/**
	 * 视频列表是否可见 true：可见 false：不可见
	 */
	private boolean isVideoViewSetValid;
	/**
	 * 成员列表是否可见 true：可见 false：不可见
	 */
	private boolean isLeaguerViewSetValid;
	/**
	 * 是否可以邀请成员 true：可见 false：不可见
	 */
	private boolean isInviteLeaguerSetValid;

	/**
	 * 分享文件设置 true：所有成员 false：创建者和管理员
	 */
	private boolean isShareFileSet;

	/**
	 * 分享照片设置 true：所有成员 false：创建者和管理员
	 */
	private boolean isSharePhotoSet;

	/**
	 * 分享话题设置 true：所有成员 false：创建者和管理员
	 */
	private boolean isShareTopicSet;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getLeaguerStatus() {
		return leaguerStatus;
	}

	public void setLeaguerStatus(String leaguerStatus) {
		this.leaguerStatus = leaguerStatus;
	}

	public boolean getIsTopicViewSetValid() {
		return isTopicViewSetValid;
	}

	public void setIsTopicViewSetValid(boolean isTopicViewSetValid) {
		this.isTopicViewSetValid = isTopicViewSetValid;
	}

	public boolean getIsFileViewSetValid() {
		return isFileViewSetValid;
	}

	public void setIsFileViewSetValid(boolean isFileViewSetValid) {
		this.isFileViewSetValid = isFileViewSetValid;
	}

	public boolean getIsPhotoViewSetValid() {
		return isPhotoViewSetValid;
	}

	public void setIsPhotoViewSetValid(boolean isPhotoViewSetValid) {
		this.isPhotoViewSetValid = isPhotoViewSetValid;
	}

	public boolean getTsVideoViewSetValid() {
		return isVideoViewSetValid;
	}

	public void setIsVideoViewSetValid(boolean isVideoViewSetValid) {
		this.isVideoViewSetValid = isVideoViewSetValid;
	}

	public boolean getIsLeaguerViewSetValid() {
		return isLeaguerViewSetValid;
	}

	public void setIsLeaguerViewSetValid(boolean isLeaguerViewSetValid) {
		this.isLeaguerViewSetValid = isLeaguerViewSetValid;
	}

	public boolean getIsInviteLeaguerSetValid() {
		return isInviteLeaguerSetValid;
	}

	public void setIsInviteLeaguerSetValid(boolean isInviteLeaguerSetValid) {
		this.isInviteLeaguerSetValid = isInviteLeaguerSetValid;
	}

	public boolean getShareFileSet() {
		return isShareFileSet;
	}

	public void setShareFileSet(boolean isShareFileSet) {
		this.isShareFileSet = isShareFileSet;
	}

	public boolean getSharePhotoSet() {
		return isSharePhotoSet;
	}

	public void setSharePhotoSet(boolean isSharePhotoSet) {
		this.isSharePhotoSet = isSharePhotoSet;
	}

	public boolean getShareTopicSet() {
		return isShareTopicSet;
	}

	public void setShareTopicSet(boolean isShareTopicSet) {
		this.isShareTopicSet = isShareTopicSet;
	}
}
