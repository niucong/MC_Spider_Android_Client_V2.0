package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class FileInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5459401688715077443L;

	/**
	 * 文件信息表主键
	 */
	private int fileId;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 社员名称
	 */
	private String memberName;

	/**
	 * 社员头像路径前缀
	 */
	private String memberHeadUrl;

	/**
	 * 社员头像路径
	 */
	private String memberHeadPath;

	/**
	 * 分享社员编号
	 */
	private int shareMemberId;

	/**
	 * 分享社员名称
	 */
	private String shareMemberName;
	/**
	 * 分享社员头像路径
	 */
	private String shareMemberHeadPath;

	/**
	 * 分享社员头像路径 前缀
	 */
	private String shareMemberHeadUrl;

	/**
	 * 文件所有者编号，根据文件的类型判断所有者是社员还是圈子
	 */
	private int fileOwnerId;

	/**
	 * 文件所有者名字
	 */
	private String fileOwnerName;

	/**
	 * 文件名字
	 */
	private String fileName;

	/**
	 * 文件格式编号
	 */
	private int formatId;

	/**
	 * 文件格式名称
	 */
	private String formatName;
	/**
	 * 分享到圈子的次数
	 */
	private int shareGroupNum;

	/**
	 * 分享到圈子的总数
	 */
	private int shareGroupAllNum;

	/**
	 * 文件格式图片路径前缀
	 */
	private String formatUrl;

	/**
	 * 文件格式图片路径
	 */
	private String formatPath;

	/**
	 * 文件大小 以KB为单位
	 */
	private long fileSize;

	/**
	 * 文件路径前缀
	 */
	private String fileUrl;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 文件本地路径
	 */
	private String fileLocal;

	/**
	 * 文件描述
	 */
	private String fileDesc;

	/**
	 * 文件附言
	 */
	private String fileRemark;

	/**
	 * 文件标签： 以英文逗号（,）分割
	 */
	private String fileTags;

	/**
	 * 文件类型 1：自己上传 2：社员分享 3：分享圈子中的文件
	 */
	private int fileType;

	/**
	 * 文件来源编号，根据文件属性判断该文件是属于圈子还是社员，当是自己时候，该字段为0
	 */
	private int sourceFileId;

	/**
	 * 访问总次数
	 */
	private int visitAllNum;

	/**
	 * 下载次数
	 */
	private int downloadAllNum;

	/**
	 * 分享人数
	 */
	private int shareMemberNum;

	/**
	 * 分享次数
	 */
	private int shareAllNum;

	/**
	 * 是否已读 1：未读 2：已读
	 */
	private int isRead;

	/**
	 * 上传时间
	 */
	private String uploadTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 编辑时间
	 */
	private String editTime;

	/**
	 * 文件来源
	 */
	private String fileSource;

	/**
	 * 使用的ua信息
	 */
	private String ua;

	/**
	 * 创建ip
	 */
	private String createIp;

	/**
	 * 文件上传的方式
	 */
	private String createWay;

	private int deleteStatus;

	private int folderId;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public int getFileOwnerId() {
		return fileOwnerId;
	}

	public void setFileOwnerId(int fileOwnerId) {
		this.fileOwnerId = fileOwnerId;
	}

	public String getFileOwnerName() {
		return fileOwnerName;
	}

	public void setFileOwnerName(String fileOwnerName) {
		this.fileOwnerName = fileOwnerName;
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

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
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

	public String getFileLocal() {
		return fileLocal;
	}

	public void setFileLocal(String fileLocal) {
		this.fileLocal = fileLocal;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public String getFileRemark() {
		return fileRemark;
	}

	public void setFileRemark(String fileRemark) {
		this.fileRemark = fileRemark;
	}

	public String[] getFileTags() {
		if (fileTags != null && fileTags.length() > 2) {
			fileTags = fileTags.substring(1, fileTags.length() - 1);
			if (fileTags.contains(",")) {
				String[] tags = fileTags.split(",");
				if (tags != null) {
					int size = tags.length;
					String[] Tags = new String[size];
					for (int i = 0; i < size; i++) {
						Tags[i] = tags[i].replace("\"", "");
					}
					return Tags;
				}
			} else if (!"".equals(fileTags)) {
				return new String[] { fileTags.replace("\"", "") };
			}
		}
		return null;
	}

	public void setFileTags(String fileTags) {
		this.fileTags = fileTags;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getSourceFileId() {
		return sourceFileId;
	}

	public void setSourceFileId(int sourceFileId) {
		this.sourceFileId = sourceFileId;
	}

	public int getVisitAllNum() {
		return visitAllNum;
	}

	public void setVisitAllNum(int visitAllNum) {
		this.visitAllNum = visitAllNum;
	}

	public int getDownloadAllNum() {
		return downloadAllNum;
	}

	public void setDownloadAllNum(int downloadAllNum) {
		this.downloadAllNum = downloadAllNum;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getShareAllNum() {
		return shareAllNum;
	}

	public void setShareAllNum(int shareAllNum) {
		this.shareAllNum = shareAllNum;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
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

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getCreateWay() {
		return createWay;
	}

	public void setCreateWay(String createWay) {
		this.createWay = createWay;
	}

	public String getShareMemberHeadPath() {
		return shareMemberHeadPath;
	}

	public void setShareMemberHeadPath(String shareMemberHeadPath) {
		this.shareMemberHeadPath = shareMemberHeadPath;
	}

	public String getShareMemberHeadUrl() {
		return shareMemberHeadUrl;
	}

	public void setShareMemberHeadUrl(String shareMemberHeadUrl) {
		this.shareMemberHeadUrl = shareMemberHeadUrl;
	}

	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}

	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}

	public String getMemberHeadPath() {
		return memberHeadPath;
	}

	public void setMemberHeadPath(String memberHeadPath) {
		this.memberHeadPath = memberHeadPath;
	}

	public int getShareGroupNum() {
		return shareGroupNum;
	}

	public void setShareGroupNum(int shareGroupNum) {
		this.shareGroupNum = shareGroupNum;
	}

	public int getShareGroupAllNum() {
		return shareGroupAllNum;
	}

	public void setShareGroupAllNum(int shareGroupAllNum) {
		this.shareGroupAllNum = shareGroupAllNum;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
}
