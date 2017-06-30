package com.datacomo.mc.spider.android.net.been;

import java.util.List;

public class GroupBean {

	/**
	 * 圈子ＩＤ
	 */
	private int groupId;

	/**
	 * 圈子名称
	 */
	private String groupName;

	/**
	 * 圈子类型 1 - 普通圈子 2 - 校园圈子 3 - 企业圈子 4 - 其它
	 */
	private int groupType;

	/**
	 * 圈子创建者ＩＤ
	 */
	private int memberId;

	/**
	 * 圈子创建者名称
	 */
	private String memberName;

	/**
	 * 圈子创建者头像url
	 */
	private String memberHeadUrl;

	/**
	 * 圈子创建者头像path
	 */
	private String memberHeadPath;

	/**
	 * 圈子类型 1 - 公开 2 - 私密 3 - 自定义
	 */
	private int openStatus;

	/**
	 * 圈子海报url
	 */
	private String groupPosterUrl;

	/**
	 * 圈子海报path
	 */
	private String groupPosterPath;

	/**
	 * 圈子短号
	 */
	private int groupShort;

	/**
	 * 圈子描述
	 */
	private String groupDescription;

	/**
	 * 圈子标签
	 */
	private String groupTag;

	/**
	 * 上级圈子个数
	 */
	private int upGroupNum;

	/**
	 * 下级圈子个数
	 */
	private int downGroupNum;

	/**
	 * 合作圈子个数
	 */
	private int cooGroupNum;

	/**
	 * 话题数量
	 */
	private int topicNum;

	/**
	 * 视频数量
	 */
	private int videoNum;

	/**
	 * 照片数量
	 */
	private int photoNum;

	/**
	 * 文件数量
	 */
	private int fileNum;

	/**
	 * 圈子魔币
	 */
	private int goldNum;

	/**
	 * 最大的社员数量
	 */
	private int maxMemberNum;

	/**
	 * 圈子成员数量
	 */
	private int leaguerNum;

	/**
	 * 群发消息数量　
	 */
	private int messageNum;

	/**
	 * 访问圈子总人数
	 */
	private int visitMemberNum;

	/**
	 * 访问圈子总次数
	 */
	private int visitAllNum;

	/**
	 * 加入圈子状态
	 * // 圈主
	GROUP_OWNER(1),
	// 圈子管理
	GROUP_MANAGER(2),
	// 申请圈子管理
	APPLY_MANAGER(3),
	// 普通成员
	GROUP_LEAGUER(4),
	// 申请成员
	APPLY_LEAGUER(5),
	// 是合作圈子的成员，并且已申请加入本圈子
	COOPERATION_APPLY_LEAGUER(5),
	// 合作圈子的成员，并且未申请加入本圈子
	COOPERATION_LEAGUER(6),	
	// 无关系
	NO_RELATION(6),
	// 开放主页粉丝
	OPEN_PAGE_FANS(7);
	 */
	private String joinGroupStatus;

	/**
	 * 创建方式 1：web 2：wap 3：iphone 4：android 5：SMS
	 */
	private String createWay;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 圈子状态 1：正常使用 2：封杀
	 */
	private int groupStatus;

	/**
	 * 圈子的未读动态数量
	 */
	private int newsNum;

	/**
	 * 成员加入圈子时间
	 */
	private String joinTime;

	/**
	 * 是否是粉丝（开放主页）
	 */
	private boolean isFans;

	/**
	 * 圈博数量
	 */
	private int posterNum;

	/**
	 * 粉丝数量
	 */
	private int fansNum;

	private GroupBackGroundBean groupBackGroundBean;

	/**
	 * 圈博中主资源信息
	 */
	private List<ObjectInfoBean> objectSummary;

	/**
	 * 圈子类型(对应数据库中的group_type)：1-兴趣圈 2-企业圈 3-开放主页
	 */
	private int groupProperty;

	/**
	 * 申请认证的审核状态：1.审核中2.审核通过3.审核失败4.未申请认证
	 */
	private int approveStatus;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
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

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	public String getGroupPosterUrl() {
		return groupPosterUrl;
	}

	public void setGroupPosterUrl(String groupPosterUrl) {
		this.groupPosterUrl = groupPosterUrl;
	}

	public String getGroupPosterPath() {
		return groupPosterPath;
	}

	public void setGroupPosterPath(String groupPosterPath) {
		this.groupPosterPath = groupPosterPath;
	}

	public int getGroupShort() {
		return groupShort;
	}

	public void setGroupShort(int groupShort) {
		this.groupShort = groupShort;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupTag() {
		return groupTag;
	}

	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}

	public int getUpGroupNum() {
		return upGroupNum;
	}

	public void setUpGroupNum(int upGroupNum) {
		this.upGroupNum = upGroupNum;
	}

	public int getDownGroupNum() {
		return downGroupNum;
	}

	public void setDownGroupNum(int downGroupNum) {
		this.downGroupNum = downGroupNum;
	}

	public int getCooGroupNum() {
		return cooGroupNum;
	}

	public void setCooGroupNum(int cooGroupNum) {
		this.cooGroupNum = cooGroupNum;
	}

	public int getTopicNum() {
		return topicNum;
	}

	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	}

	public int getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(int videoNum) {
		this.videoNum = videoNum;
	}

	public int getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public int getMaxMemberNum() {
		return maxMemberNum;
	}

	public void setMaxMemberNum(int maxMemberNum) {
		this.maxMemberNum = maxMemberNum;
	}

	public int getLeaguerNum() {
		return leaguerNum;
	}

	public void setLeaguerNum(int leaguerNum) {
		this.leaguerNum = leaguerNum;
	}

	public int getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public int getVisitAllNum() {
		return visitAllNum;
	}

	public void setVisitAllNum(int visitAllNum) {
		this.visitAllNum = visitAllNum;
	}

	public String getJoinGroupStatus() {
		return joinGroupStatus;
	}

	public void setJoinGroupStatus(String joinGroupStatus) {
		this.joinGroupStatus = joinGroupStatus;
	}

	public String getCreateWay() {
		return createWay;
	}

	public void setCreateWay(String createWay) {
		this.createWay = createWay;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(int groupStatus) {
		this.groupStatus = groupStatus;
	}

	public int getNewsNum() {
		return newsNum;
	}

	public void setNewsNum(int newsNum) {
		this.newsNum = newsNum;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public boolean isFans() {
		return isFans;
	}

	public void setIsFans(boolean isFans) {
		this.isFans = isFans;
	}

	public int getPosterNum() {
		return posterNum;
	}

	public void setPosterNum(int posterNum) {
		this.posterNum = posterNum;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	public GroupBackGroundBean getGroupBackGroundBean() {
		return groupBackGroundBean;
	}

	public void setGroupBackGroundBean(GroupBackGroundBean groupBackGroundBean) {
		this.groupBackGroundBean = groupBackGroundBean;
	}

	public List<ObjectInfoBean> getObjectSummary() {
		return objectSummary;
	}

	public void setObjectSummary(List<ObjectInfoBean> objectSummary) {
		this.objectSummary = objectSummary;
	}

	public int getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(int groupProperty) {
		this.groupProperty = groupProperty;
	}

	public int getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(int approveStatus) {
		this.approveStatus = approveStatus;
	}
}
