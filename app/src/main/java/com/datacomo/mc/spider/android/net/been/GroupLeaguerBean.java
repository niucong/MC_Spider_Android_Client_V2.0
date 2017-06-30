package com.datacomo.mc.spider.android.net.been;

import com.datacomo.mc.spider.android.url.L;

public class GroupLeaguerBean {

	/**
	 * 圈子成员ＩＤ
	 */
	private int leaguerId;

	/**
	 * 圈子ＩＤ
	 */
	private int groupId;

	/**
	 * 社员ＩＤ
	 */
	private int memberId;

	/**
	 * 社员名称
	 */
	private String memberName;

	/**
	 * 社员头像URL
	 */
	private String memberHeadUrl;

	/**
	 * 社员头像path
	 */
	private String memberHeadPath;

	/**
	 * 社员手机号
	 */
	private String memberPhone;

	/**
	 * 成员角色1：圈主 2：管理员 3：申请管理员 4：普通成员 5:申请加入圈子普通成员
	 */
	private int leaguerStatus;

	/**
	 * 圈子动态数量
	 */
	private int newsNum;

	/**
	 * 群发消息数量
	 */
	private int messageNum;

	/**
	 * 发起话题数量
	 */
	private int topicNum;

	/**
	 * 上传资源数量
	 */
	private int fileNum;

	/**
	 * 上传视频数量
	 */
	private int videoNum;

	/**
	 * 成员照片的数量
	 */
	private int photoNum;

	/**
	 * 社员加入群组的方式 1.apply - 申请加入 2.invite - 邀请加入 3.creates - 创建加入
	 */
	private String memberJoinGroupWay;

	/**
	 * 社员在此群组中星标话题的数量
	 */
	private int importantTopicNum;

	/**
	 * 审核人编号
	 */
	private int agreeMemberId;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 申请时间
	 */
	private String applyTime;

	/**
	 * 加入时间
	 */
	private String joinTime;

	/**
	 * 社员性别
	 */
	private int memberSex;

	/**
	 * 社员心情
	 */
	private String memberMood;

	/**
	 * 朋友状态（是否是朋友）0 非朋友1申请中 2朋友 3朋友的朋友
	 */
	private String friendStatus;

	/**
	 * 是否分享过文件
	 */
	private boolean shareStatus;

	/**
	 * 分享中选中状态 0为未选择，1为选择 仅在交流圈成员列表以及交流圈列表中使用
	 */
	private int isSelect;

	/**
	 * 分享全选时 选中状态 -1 初始为全选 0为未选择，1为选择 仅在交流圈成员列表以及交流圈列表中使用
	 */
	private int mIsTempSelect;

	public GroupLeaguerBean() {
		mIsTempSelect = -1;
		L.d("isTempSelect", "isTempSelect:" + mIsTempSelect);
	}

	public int getLeaguerId() {
		return leaguerId;
	}

	public void setLeaguerId(int leaguerId) {
		this.leaguerId = leaguerId;
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

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public int getLeaguerStatus() {
		return leaguerStatus;
	}

	public void setLeaguerStatus(int leaguerStatus) {
		this.leaguerStatus = leaguerStatus;
	}

	public int getNewsNum() {
		return newsNum;
	}

	public void setNewsNum(int newsNum) {
		this.newsNum = newsNum;
	}

	public int getMessageNum() {
		return messageNum;
	}

	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}

	public int getTopicNum() {
		return topicNum;
	}

	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	}

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
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

	public String getMemberJoinGroupWay() {
		return memberJoinGroupWay;
	}

	public void setMemberJoinGroupWay(String memberJoinGroupWay) {
		this.memberJoinGroupWay = memberJoinGroupWay;
	}

	public int getImportantTopicNum() {
		return importantTopicNum;
	}

	public void setImportantTopicNum(int importantTopicNum) {
		this.importantTopicNum = importantTopicNum;
	}

	public int getAgreeMemberId() {
		return agreeMemberId;
	}

	public void setAgreeMemberId(int agreeMemberId) {
		this.agreeMemberId = agreeMemberId;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public int getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(int memberSex) {
		this.memberSex = memberSex;
	}

	public String getMemberMood() {
		return memberMood;
	}

	public void setMemberMood(String memberMood) {
		this.memberMood = memberMood;
	}

	public String getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}

	public boolean isShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(boolean shareStatus) {
		this.shareStatus = shareStatus;
	}

	public String getFullHeadPath() {
		return this.memberHeadUrl + this.memberHeadPath;
	}

	public int isSelect() {
		return isSelect;
	}

	public void setSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public int isTempSelect() {
		L.d("isTempSelect", "isTempSelect:" + mIsTempSelect);
		return mIsTempSelect;
	}

	public void setTempSelect(int isTempSelect) {
		if (1 == isTempSelect)
			L.d("setTempSelect", "isTempSelect:" + 1);
		else if (0 == isTempSelect)
			L.d("setTempSelect", "isTempSelect:" + 0);
		else
			L.d("setTempSelect", "isTempSelect:" + -1);
		mIsTempSelect = isTempSelect;
	}

}
