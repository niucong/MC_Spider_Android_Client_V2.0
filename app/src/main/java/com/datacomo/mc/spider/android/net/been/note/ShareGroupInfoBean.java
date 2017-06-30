package com.datacomo.mc.spider.android.net.been.note;

import java.io.Serializable;

public class ShareGroupInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7018800614897646416L;

	/**
	 * 分享人编号
	 */
	private int shareMemberId;
	/**
	 * 分享人名字
	 */
	private String shareMemberName;
	/**
	 * 分享人头像前缀
	 */
	private String shareMemberHeadUrl;
	/**
	 * 分享人头像路径
	 */
	private String shareMemberHeadPath;
	/**
	 * 分享接受圈子编号
	 */
	private int receiveGroupId;
	/**
	 * 分享圈子名字
	 */
	private String receiveGroupName;
	/**
	 * 分享圈子头像前缀
	 */
	private String receiveGroupHeadUrl;
	/**
	 * 分享圈子头像路径
	 */
	private String receiveGroupHeadPath;
	/**
	 * 分享笔记的次数
	 */
	private int shareNotebookNum;
	/**
	 * 分享笔记的次数
	 */
	private int shareNoteNum;
	/**
	 * 最后分享时间
	 */
	private String lastShareTime;
	/**
	 * 创建时间
	 */
	private String createTime;

	public int getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(int shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public String getShareMemberName() {
		return shareMemberName;
	}

	public void setShareMemberName(String shareMemberName) {
		this.shareMemberName = shareMemberName;
	}

	public String getShareMemberHeadUrl() {
		return shareMemberHeadUrl;
	}

	public void setShareMemberHeadUrl(String shareMemberHeadUrl) {
		this.shareMemberHeadUrl = shareMemberHeadUrl;
	}

	public String getShareMemberHeadPath() {
		return shareMemberHeadPath;
	}

	public void setShareMemberHeadPath(String shareMemberHeadPath) {
		this.shareMemberHeadPath = shareMemberHeadPath;
	}

	public int getReceiveGroupId() {
		return receiveGroupId;
	}

	public void setReceiveGroupId(int receiveGroupId) {
		this.receiveGroupId = receiveGroupId;
	}

	public String getReceiveGroupName() {
		return receiveGroupName;
	}

	public void setReceiveGroupName(String receiveGroupName) {
		this.receiveGroupName = receiveGroupName;
	}

	public String getReceiveGroupHeadUrl() {
		return receiveGroupHeadUrl;
	}

	public void setReceiveGroupHeadUrl(String receiveGroupHeadUrl) {
		this.receiveGroupHeadUrl = receiveGroupHeadUrl;
	}

	public String getReceiveGroupHeadPath() {
		return receiveGroupHeadPath;
	}

	public void setReceiveGroupHeadPath(String receiveGroupHeadPath) {
		this.receiveGroupHeadPath = receiveGroupHeadPath;
	}

	public int getShareNotebookNum() {
		return shareNotebookNum;
	}

	public void setShareNotebookNum(int shareNotebookNum) {
		this.shareNotebookNum = shareNotebookNum;
	}

	public int getShareNoteNum() {
		return shareNoteNum;
	}

	public void setShareNoteNum(int shareNoteNum) {
		this.shareNoteNum = shareNoteNum;
	}

	public String getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(String lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
