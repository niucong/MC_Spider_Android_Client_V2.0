package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class GroupTopicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4368794402978780638L;
	/**
	 * 话题编号
	 */
	private int topicId;
	/**
	 * 圈子编号
	 */
	private int groupId;
	/**
	 * 创建者编号
	 */
	private int memberId;
	/**
	 * 创建者名字
	 */
	private String memberName;
	/**
	 * 创建者头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 创建者头像路径
	 */
	private String memberHeadPath;
	/**
	 * 话题标题
	 */
	private String topicTitle;
	/**
	 * 话题文本内容
	 */
	private String topicContent;
	/**
	 * 附加文件数量
	 */
	private int fileNum;
	/**
	 * 照片数量
	 */
	private int photoNum;
	/**
	 * 视频数量
	 */
	private int videoNum;
	/**
	 * 访问社员数量
	 */
	private int visitMemberNum;
	/**
	 * 访问次数
	 */
	private int visitNum;
	/**
	 * 赞次数
	 */
	private int greatNum;
	/**
	 * 评论数量
	 */
	private int commentNum;
	/**
	 * 魅力值
	 */
	private int charmNum;
	/**
	 * 话题标签
	 */
	// private String[] topicTag;
	/**
	 * 话题类型 1：leaguer_send - 成员发表 2：messages_send - 群发成话题 3：rss_send rss-导入成话题
	 * 4: 应用中导入 5: 来自云笔记
	 */
	private int topicType;
	/**
	 * 创建方式
	 */
	private String visitWay;
	/**
	 * 是否赞过话题：true 赞过 false未赞过
	 */
	private boolean isGreatStatus;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 被分享人数
	 */
	private int shareMemberNum;
	/**
	 * 总分享数
	 */
	private int shareAllNum;
	/**
	 * 分享者人数
	 */
	private int shareNum;

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

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

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public String getTopicContent() {
		return topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public int getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

	public int getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(int videoNum) {
		this.videoNum = videoNum;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public int getGreatNum() {
		return greatNum;
	}

	public void setGreatNum(int greatNum) {
		this.greatNum = greatNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getCharmNum() {
		return charmNum;
	}

	public void setCharmNum(int charmNum) {
		this.charmNum = charmNum;
	}

	// public String[] getTopicTag() {
	// return topicTag;
	// }
	//
	// public void setTopicTag(String[] topicTag) {
	// this.topicTag = topicTag;
	// }

	public int getTopicType() {
		return topicType;
	}

	public void setTopicType(int topicType) {
		this.topicType = topicType;
	}

	public String getVisitWay() {
		return visitWay;
	}

	public void setVisitWay(String visitWay) {
		this.visitWay = visitWay;
	}

	public boolean isGreatStatus() {
		return isGreatStatus;
	}

	public void setGreatStatus(boolean isGreatStatus) {
		this.isGreatStatus = isGreatStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}
}
