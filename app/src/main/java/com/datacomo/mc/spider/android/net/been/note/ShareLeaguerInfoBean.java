package com.datacomo.mc.spider.android.net.been.note;

import java.io.Serializable;

/*
 * 分享社员详细信息
 */
public class ShareLeaguerInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521869025716055838L;
	/**
	 * 分享人编号
	 */
	private int shareMemberId;
	/**
	 * 分享人名字
	 */
	private String shareMemberName;
	/**
	 * 分享人别名（即备注名）
	 */
	private String shareAliases;
	/**
	 * 分享人头像前缀
	 */
	private String shareMemberHeadUrl;
	/**
	 * 分享人头像路径
	 */
	private String shareMemberHeadPath;
	/**
	 * 分享接受者编号
	 */
	private int ownerMemberId;
	/**
	 * 分享笔记本的次数
	 */
	private int shareNotebookNum;
	/**
	 * 分享笔记的次数
	 */
	private int shareNoteNum;
	/**
	 * 分享笔记未读数量
	 */
	private int unreadNoteNum;
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

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
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

	public String getShareAliases() {
		return shareAliases;
	}

	public void setShareAliases(String shareAliases) {
		this.shareAliases = shareAliases;
	}

	public int getUnreadNoteNum() {
		return unreadNoteNum;
	}

	public void setUnreadNoteNum(int unreadNoteNum) {
		this.unreadNoteNum = unreadNoteNum;
	}

}
