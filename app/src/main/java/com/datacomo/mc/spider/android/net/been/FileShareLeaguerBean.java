package com.datacomo.mc.spider.android.net.been;

public class FileShareLeaguerBean {
	/**
	 * 分享者id
	 */
	private int shareId;

	/**
	 * 文件编号
	 */
	private int fileId;

	/**
	 * 拥有者id
	 */
	private int ownerMemberId;

	/**
	 * 被分享者id
	 */
	private int relationMemberId;

	/**
	 * 被分享者name
	 */
	private String relationMemberName;

	/**
	 * 被分享者head Url
	 */
	private String relationMemberHeadUrl;

	/**
	 * 被分享者head path
	 */
	private String relationMemberHeadPath;

	/**
	 * 总分享文件数
	 */
	private int totalFileNum;
	/**
	 * 分享的时间内容
	 */
	private String[] shareTimeContent;

	/**
	 * 分享出去文件总数
	 */
	private int shareFileNum;

	/**
	 * 接收文件总数
	 */
	private int receiveFileNum;

	/**
	 * 新文件数，即未读文件数
	 */
	private int newShareNum;

	/**
	 * 分享创建时间
	 */
	private String createTime;
	/**
	 * 
	 */
	private int isDownload;

	/**
	 * 最后分享人名字
	 */
	private String lastShareMemberName;
	/**
	 * 最后分享文件名字
	 */
	private String lastShareFileName;
	/**
	 * 最后分享文件所在圈子名字
	 */
	private String lastShareGroupName;

	/**
	 * 最后分享时间
	 */
	private String lastShareTime;

	/**
	 * 最后更新时间
	 */
	private String updateTime;

	/**
	 * 最后下载时间
	 */
	private String lastDownloadTime;

	public int getShareId() {
		return shareId;
	}

	public void setShareId(int shareId) {
		this.shareId = shareId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
	}

	public int getRelationMemberId() {
		return relationMemberId;
	}

	public void setRelationMemberId(int relationMemberId) {
		this.relationMemberId = relationMemberId;
	}

	public String getRelationMemberName() {
		return relationMemberName;
	}

	public void setRelationMemberName(String relationMemberName) {
		this.relationMemberName = relationMemberName;
	}

	public String getRelationMemberHeadUrl() {
		return relationMemberHeadUrl;
	}

	public void setRelationMemberHeadUrl(String relationMemberHeadUrl) {
		this.relationMemberHeadUrl = relationMemberHeadUrl;
	}

	public String getRelationMemberHeadPath() {
		return relationMemberHeadPath;
	}

	public void setRelationMemberHeadPath(String relationMemberHeadPath) {
		this.relationMemberHeadPath = relationMemberHeadPath;
	}

	public int getTotalFileNum() {
		return totalFileNum;
	}

	public void setTotalFileNum(int totalFileNum) {
		this.totalFileNum = totalFileNum;
	}

	public String[] getShareTimeContent() {
		return shareTimeContent;
	}

	public void setShareTimeContent(String[] shareTimeContent) {
		this.shareTimeContent = shareTimeContent;
	}

	public int getShareFileNum() {
		return shareFileNum;
	}

	public void setShareFileNum(int shareFileNum) {
		this.shareFileNum = shareFileNum;
	}

	public int getReceiveFileNum() {
		return receiveFileNum;
	}

	public void setReceiveFileNum(int receiveFileNum) {
		this.receiveFileNum = receiveFileNum;
	}

	public int getNewShareNum() {
		return newShareNum;
	}

	public void setNewShareNum(int newShareNum) {
		this.newShareNum = newShareNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public String getLastShareMemberName() {
		return lastShareMemberName;
	}

	public void setLastShareMemberName(String lastShareMemberName) {
		this.lastShareMemberName = lastShareMemberName;
	}

	public String getLastShareFileName() {
		return lastShareFileName;
	}

	public void setLastShareFileName(String lastShareFileName) {
		this.lastShareFileName = lastShareFileName;
	}

	public String getLastShareGroupName() {
		return lastShareGroupName;
	}

	public void setLastShareGroupName(String lastShareGroupName) {
		this.lastShareGroupName = lastShareGroupName;
	}

	public String getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(String lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLastDownloadTime() {
		return lastDownloadTime;
	}

	public void setLastDownloadTime(String lastDownloadTime) {
		this.lastDownloadTime = lastDownloadTime;
	}

}
