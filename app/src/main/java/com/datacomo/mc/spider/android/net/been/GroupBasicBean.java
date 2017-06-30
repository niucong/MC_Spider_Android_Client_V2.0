package com.datacomo.mc.spider.android.net.been;

public class GroupBasicBean {

	/**
	 * 圈子编号
	 */
	private int groupId;

	/**
	 * 圈子名称
	 */
	private String groupName;

	/**
	 * 圈子海报路径前缀
	 */
	private String groupPosterUrl;

	/**
	 * 圈子海报前缀
	 */
	private String groupPosterPath;

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
	 * 圈子私密类型 1 - 公开 2 - 私密 3 - 自定义
	 */
	private int openStatus;

	/**
	 * 圈子属性: 1 - 普通圈子 2 - 校园圈子 3 - 企业圈子
	 */
	private int groupType;

	/**
	 * 圈子标签
	 */
	private String groupTag;

	/**
	 * 圈子描述
	 */
	private String groupDescription;

	/**
	 * 加入圈子状态
	 * 圈主GROUP_OWNER、圈子管理GROUP_MANAGER、申请圈子管理APPLY_MANAGER、普通成员GROUP_LEAGUER
	 * 、申请成员APPLY_LEAGUER、是合作圈子的成员，并且已申请加入本圈子
	 * COOPERATION_APPLY_LEAGUER、合作圈子的成员，并且未申请加入本圈子 COOPERATION_LEAGUER
	 */
	private String joinGroupStatus;

	/**
	 * 圈子成员数量
	 */
	private int leaguerNum;

	/**
	 * 新动态的数量
	 */
	private int newTrendNum;

	/**
	 * 成员加入圈子时间
	 */
	private String joinTime;

	/**
	 * 圈子创建时间
	 */
	private String createTime;

	/**
	 * 圈子状态：具体详见接口
	 */
	private int groupStatus;
	/**
	 * 分享中选中状态 0为未选择，1为选择 仅在交流圈成员列表以及交流圈列表中使用
	 */
	private int isSelect;

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

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getGroupTag() {
		return groupTag;
	}

	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getJoinGroupStatus() {
		return joinGroupStatus;
	}

	public void setJoinGroupStatus(String joinGroupStatus) {
		this.joinGroupStatus = joinGroupStatus;
	}

	public int getLeaguerNum() {
		return leaguerNum;
	}

	public void setLeaguerNum(int leaguerNum) {
		this.leaguerNum = leaguerNum;
	}

	public int getNewTrendNum() {
		return newTrendNum;
	}

	public void setNewTrendNum(int newTrendNum) {
		this.newTrendNum = newTrendNum;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
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
	
	public String getFullGroupPosterPath(){
		return this.groupPosterUrl+this.groupPosterPath;
	}

	public int isSelect() {
		return isSelect;
	}

	public void setSelect(int isSelect) {
		this.isSelect = isSelect;
	}
	
	
}
