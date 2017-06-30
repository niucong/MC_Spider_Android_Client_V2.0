package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class ShareLeaguerRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 记录主键
	 */
	int recordId;
	/**
	 * 所有者编号
	 */
	int ownerMemberId;
	/**
	 * 成员编号
	 */
	int leaguerId;
	/**
	 * 文件编号
	 */
	int fileId;
	/**
	 * 文件名字
	 */
	String fileName;
	/**
	 * 文件格式编号
	 */
	int formatId;
	/**
	 * 文件格式名字
	 */
	String formatName;
	/**
	 * 文件格式图片路径前缀
	 */
	String formatUrl;
	/**
	 * 文件格式图片路径
	 */
	String formatPath;
	/**
	 * 文件描述
	 */
	String fileDescription;
	/**
	 * 文件大小
	 */
	int fileSize;
	/**
	 * 文件路径前缀
	 */
	String fileUrl;
	/**
	 * 文件路径
	 */
	String filePath;
	/**
	 * 文件标签
	 */
	String[] fileTag;
	/**
	 * 分享社员编号
	 */
	int shareMemberId;
	/**
	 * 分享社员的名字
	 */
	String shareMemberName;
	/**
	 * 分享社员头像的路径前缀
	 */
	String shareMemberUrl;
	/**
	 * 分享社员头像的路径
	 */
	String shareMemberPath;
	/**
	 * 接收分享社员编号
	 */
	int receiveMemberId;
	/**
	 * 接收社员的名字
	 */
	String receiveMemberName;
	/**
	 * 分享类型 1/分享给社员， 2/分享给人员
	 */
	int shareType;
	/**
	 * 分享附言
	 */
	String shareRemark;
	/**
	 * 分享时间
	 */
	String shareTime;
	/**
	 * 文件来源的圈子编号
	 */
	int sourceGroupId;
	/**
	 * 文件来源的圈子名称
	 */
	String sourceGroupName;
	/**
	 * 来源文件的文件编号
	 */
	int sourceFileId;
	/**
	 * 本次分享，分享人的数量
	 */
	int shareMemberNum;
	/**
	 * 分享记录编号
	 */
	int receiverId;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
	}

	public int getLeaguerId() {
		return leaguerId;
	}

	public void setLeaguerId(int leaguerId) {
		this.leaguerId = leaguerId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFormatId() {
		return formatId;
	}

	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public String getFormatUrl() {
		return formatUrl;
	}

	public void setFormatUrl(String formatUrl) {
		this.formatUrl = formatUrl;
	}

	public String getFormatPath() {
		return formatPath;
	}

	public void setFormatPath(String formatPath) {
		this.formatPath = formatPath;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String[] getFileTag() {
		return fileTag;
	}

	public void setFileTag(String[] fileTag) {
		this.fileTag = fileTag;
	}

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

	public String getShareMemberUrl() {
		return shareMemberUrl;
	}

	public void setShareMemberUrl(String shareMemberUrl) {
		this.shareMemberUrl = shareMemberUrl;
	}

	public String getShareMemberPath() {
		return shareMemberPath;
	}

	public void setShareMemberPath(String shareMemberPath) {
		this.shareMemberPath = shareMemberPath;
	}

	public int getReceiveMemberId() {
		return receiveMemberId;
	}

	public void setReceiveMemberId(int receiveMemberId) {
		this.receiveMemberId = receiveMemberId;
	}

	public String getReceiveMemberName() {
		return receiveMemberName;
	}

	public void setReceiveMemberName(String receiveMemberName) {
		this.receiveMemberName = receiveMemberName;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public String getShareRemark() {
		return shareRemark;
	}

	public void setShareRemark(String shareRemark) {
		this.shareRemark = shareRemark;
	}

	public String getShareTime() {
		return shareTime;
	}

	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}

	public int getSourceGroupId() {
		return sourceGroupId;
	}

	public void setSourceGroupId(int sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}

	public String getSourceGroupName() {
		return sourceGroupName;
	}

	public void setSourceGroupName(String sourceGroupName) {
		this.sourceGroupName = sourceGroupName;
	}

	public int getSourceFileId() {
		return sourceFileId;
	}

	public void setSourceFileId(int sourceFileId) {
		this.sourceFileId = sourceFileId;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

}
