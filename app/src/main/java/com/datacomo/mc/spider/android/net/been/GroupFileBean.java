package com.datacomo.mc.spider.android.net.been;

public class GroupFileBean {

	/**
	 * 文件编号
	 */
	private int fileId;
	/**
	 * 圈子编号
	 */
	private int groupId;

	/**
	 * 圈子信息
	 */
	private MemberOrGroupInfoBean groupInfoBean;
	/**
	 * 上传者编号
	 */
	private int memberId;
	/**
	 * 上传者名字
	 */
	private String memberName;
	/**
	 * 上传者头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 上传者头像路径
	 */
	private String memberHeadPath;
	/**
	 * 文件名字
	 */
	private String fileName;
	/**
	 * 文件格式编号
	 */
	private int formatId;
	/**
	 * 文件格式名字
	 */
	private String formatName;
	/**
	 * 文件格式图片路径前缀
	 */
	private String formatUrl;
	/**
	 * 文件格式图片路径
	 */
	private String formatPath;
	/**
	 * 文件大小
	 */
	private int fileSize;
	/**
	 * 文件路径前缀
	 */
	private String fileUrl;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件描述信息
	 */
	private String fileDescription;
	/**
	 * 文件标签
	 */
	private String fileTags;
	/**
	 * 文件的浏览次数
	 */
	private int visitAllNum;
	/**
	 * 文件的浏览人数
	 */
	private int visitMemberNum;
	/**
	 * 觉得此文件赞的人数
	 */
	private int greatNum;
	/**
	 * 下载次数
	 */
	private int downloadAllNum;
	/**
	 * 下载人数
	 */
	private int downloadMemberNum;
	/**
	 * 魅力值
	 */
	private int charmNum;
	/**
	 * 上传方式
	 */
	private String uploadWay;
	/**
	 * 是否赞过：true 赞过 false没有赞过
	 */
	private boolean isGreatFile;

	/**
	 * 是否分享过：true 分享过 false分享过
	 */
	private boolean isShareFile;
	/**
	 * 上传时间
	 */
	private String uploadTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	// /**
	// * 文件来源:\nMC_WEB : 公社web\nMC_WAP : 公社wap\nMC_I/A :公社iphone/android\nMC_SMS
	// * :公社短信\nHUBEI_WEB :湖北web\nHUNAN_WEB :湖南web
	// */
	// private String fileSource;
	//
	// /**
	// * 用户头信息
	// */
	// private String useHead;
	//
	// /**
	// * 创建ip
	// */
	// private String createIp;

	/**
	 * 文件上传的方式\n1：web\n2：wap\n3：iphone\n4：android\n5：SMS
	 * ',方式\n1：web\n2：wap\n3：iphone\n4：android\n5：SMS
	 */
	private String createWay;

	/**
	 * 分享的成员数量
	 */
	private int shareMemberNum;

	/**
	 * 分享的次数
	 */
	private int shareAllNum;
	/**
	 * 文件评论数量
	 */
	private int commentNum;
	/**
	 * 文件最后评论时间
	 */
	private String lastCommentTime;
	/**
	 * 文件来源类型 1：上传 2：来自云文件
	 */
	private int fileType;
	/**
	 * 是否收藏过
	 */
	private boolean hasCollect;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public MemberOrGroupInfoBean getGroupInfoBean() {
		return groupInfoBean;
	}

	public void setGroupInfoBean(MemberOrGroupInfoBean groupInfoBean) {
		this.groupInfoBean = groupInfoBean;
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

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getFileTags() {
		return fileTags;
	}

	public void setFileTags(String fileTags) {
		this.fileTags = fileTags;
	}

	public int getVisitAllNum() {
		return visitAllNum;
	}

	public void setVisitAllNum(int visitAllNum) {
		this.visitAllNum = visitAllNum;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public int getGreatNum() {
		return greatNum;
	}

	public void setGreatNum(int greatNum) {
		this.greatNum = greatNum;
	}

	public int getDownloadAllNum() {
		return downloadAllNum;
	}

	public void setDownloadAllNum(int downloadAllNum) {
		this.downloadAllNum = downloadAllNum;
	}

	public int getDownloadMemberNum() {
		return downloadMemberNum;
	}

	public void setDownloadMemberNum(int downloadMemberNum) {
		this.downloadMemberNum = downloadMemberNum;
	}

	public int getCharmNum() {
		return charmNum;
	}

	public void setCharmNum(int charmNum) {
		this.charmNum = charmNum;
	}

	public String getUploadWay() {
		return uploadWay;
	}

	public void setUploadWay(String uploadWay) {
		this.uploadWay = uploadWay;
	}

	public boolean isGreatFile() {
		return isGreatFile;
	}

	public void setGreatFile(boolean isGreatFile) {
		this.isGreatFile = isGreatFile;
	}

	public boolean isShareFile() {
		return isShareFile;
	}

	public void setShareFile(boolean isShareFile) {
		this.isShareFile = isShareFile;
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

	// public String getFileSource() {
	// return fileSource;
	// }
	//
	// public void setFileSource(String fileSource) {
	// this.fileSource = fileSource;
	// }
	//
	// public String getUseHead() {
	// return useHead;
	// }
	//
	// public void setUseHead(String useHead) {
	// this.useHead = useHead;
	// }
	//
	// public String getCreateIp() {
	// return createIp;
	// }
	//
	// public void setCreateIp(String createIp) {
	// this.createIp = createIp;
	// }

	public String getCreateWay() {
		return createWay;
	}

	public void setCreateWay(String createWay) {
		this.createWay = createWay;
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

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getLastCommentTime() {
		return lastCommentTime;
	}

	public void setLastCommentTime(String lastCommentTime) {
		this.lastCommentTime = lastCommentTime;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public boolean isHasCollect() {
		return hasCollect;
	}

	public void setHasCollect(boolean hasCollect) {
		this.hasCollect = hasCollect;
	}

}
