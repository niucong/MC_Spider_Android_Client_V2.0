package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class FriendGroupBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5526054192551031966L;
	/**
	 * 组编号
	 */
	private int groupId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 组名称
	 */
	private String groupName;
	/**
	 * 组内成员数量
	 */
	private int friendNum;
	/**
	 * 组类型：1：默认 2：社员创建
	 */
	private int groupType;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 是否是该组成员状态：0不在 1在
	 */
	private int isInStatus;
	/**
	 * Whether group is open
	 */
	private boolean open;
	/**
	 * Friends list in the group
	 */
	private List<FriendBean> friends;

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
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

	public int getIsInStatus() {
		return isInStatus;
	}

	public void setIsInStatus(int isInStatus) {
		this.isInStatus = isInStatus;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<FriendBean> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendBean> friends) {
		this.friends = friends;
	}

}
