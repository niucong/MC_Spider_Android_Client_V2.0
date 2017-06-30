package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

/**
 * 圈子基础信息（非资源）变动动态Bean，对应app_tr_trend_group_info.action_content中的json字符串
 * 
 * @author datacomo-160
 * 
 */
public class GroupBasicTrendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -780814382303730372L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 社员名字
	 */
	private String memberName;

	/**
	 * 社员头像url
	 */
	private String memberHeadUrl;

	/**
	 * 社员头像path
	 */
	private String memberHeadPath;

	/**
	 * 圈子编号
	 */
	private int groupId;

	/**
	 * 圈子名字
	 */
	private String groupName;

	/**
	 * 圈子头像url
	 */
	private String groupHeadUrl;

	/**
	 * 圈子头像path
	 */
	private String groupHeadPath;

	/**
	 * 第二个圈子编号
	 */
	private int secondGroupId;

	/**
	 * 第二个圈子名字
	 */
	private String secondGroupName;

	/**
	 * 第二个圈子头像url
	 */
	private String secondGroupHeadUrl;

	/**
	 * 第二个圈子头像path
	 */
	private String secondGroupHeadPath;

	/**
	 * 圈币数量
	 */
	private int goldNum;

	/**
	 * 圈子基础信息（非资源）变动类型
	 */
	// private String groupBasicTrendType;
	/** * 当天新加入圈子的成员数量，新版动态中当天加入圈子人数大于5时用 */
	private int newGroupLeaguerNum;

	public int getNewGroupLeaguerNum() {
		return newGroupLeaguerNum;
	}

	public void setNewGroupLeaguerNum(int newGroupLeaguerNum) {
		this.newGroupLeaguerNum = newGroupLeaguerNum;
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

	public String getGroupHeadUrl() {
		return groupHeadUrl;
	}

	public void setGroupHeadUrl(String groupHeadUrl) {
		this.groupHeadUrl = groupHeadUrl;
	}

	public String getGroupHeadPath() {
		return groupHeadPath;
	}

	public void setGroupHeadPath(String groupHeadPath) {
		this.groupHeadPath = groupHeadPath;
	}

	public int getSecondGroupId() {
		return secondGroupId;
	}

	public void setSecondGroupId(int secondGroupId) {
		this.secondGroupId = secondGroupId;
	}

	public String getSecondGroupName() {
		return secondGroupName;
	}

	public void setSecondGroupName(String secondGroupName) {
		this.secondGroupName = secondGroupName;
	}

	public String getSecondGroupHeadUrl() {
		return secondGroupHeadUrl;
	}

	public void setSecondGroupHeadUrl(String secondGroupHeadUrl) {
		this.secondGroupHeadUrl = secondGroupHeadUrl;
	}

	public String getSecondGroupHeadPath() {
		return secondGroupHeadPath;
	}

	public void setSecondGroupHeadPath(String secondGroupHeadPath) {
		this.secondGroupHeadPath = secondGroupHeadPath;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	// public String getGroupBasicTrendType() {
	// return groupBasicTrendType;
	// }
	//
	// public void setGroupBasicTrendType(String groupBasicTrendType) {
	// this.groupBasicTrendType = groupBasicTrendType;
	// }
}
