package com.datacomo.mc.spider.android.net.been;

import java.util.ArrayList;
import java.util.List;

public class FriendBean {

	/**
	 * 朋友所有者编号
	 */
	private int ownerMemberId;
	/**
	 * 朋友编号
	 */
	private int memberId;
	/**
	 * 朋友昵称
	 */
	private String memberName;
	/**
	 * 朋友头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 朋友头像路径
	 */
	private String memberHeadPath;

	/**
	 * 朋友在某分组的表示状态：0不在 1在
	 */
	private int isInGroup;
	/**
	 * 社员所在的朋友分组
	 */
	private List<FriendGroupBean> groupList;
	/**
	 * 朋友备注
	 */
	private String friendName;
	/**
	 * 朋友备注拼音表示
	 */
	private String friendNamePY;
	/**
	 * 性别：1：男 2：女
	 */
	private int sex;
	/**
	 * 情感状态：1：单身 2：恋爱中 3：已订婚 4：结婚 5：丧偶 6：分居 7：离婚',
	 */
	private int loveStatus;
	/**
	 * 注册时间
	 */
	private String registerTime;
	/**
	 * 心情用语
	 */
	private String moodContent;
	/**
	 * 在线状态： 0 ：不在线 1 ：在线
	 */
	private int online;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 所有者对朋友的动态条数
	 */
	private int ownerMemberNewsNum;
	/**
	 * 朋友对所有者的动态条数
	 */
	private int memberNewsNum;
	/**
	 * 所有者对朋友的动态的最后时间
	 */
	private String ownerMemberNewUpdateTime;
	/**
	 * 朋友对所有者的动态最后时间
	 */
	private String memberNewsUpdateTime;

	/**
	 * 朋友状态\n1：朋友\n2：不是朋友 \n3:自己
	 */
	private int friendStatus;
	/**
	 * 朋友手机号
	 */
	private String memberPhone;
	/**
	 * 手机号是否可见
	 */
	/**
	 * 账户信息
	 */
	private MemberContactBean contactInfo;

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
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

	public int getIsInGroup() {
		return isInGroup;
	}

	public void setIsInGroup(int isInGroup) {
		this.isInGroup = isInGroup;
	}

	public List<FriendGroupBean> getGroupList() {
		if (groupList == null) {
			groupList = new ArrayList<FriendGroupBean>();
		}
		return groupList;
	}

	public void setGroupList(List<FriendGroupBean> groupList) {
		this.groupList = groupList;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendNamePY() {
		return friendNamePY;
	}

	public void setFriendNamePY(String friendNamePY) {
		this.friendNamePY = friendNamePY;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getLoveStatus() {
		return loveStatus;
	}

	public void setLoveStatus(int loveStatus) {
		this.loveStatus = loveStatus;
	}

	public String getRegisterTime() {
		if (registerTime == null) {
			registerTime = new String();
		}
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getMoodContent() {
		return moodContent;
	}

	public void setMoodContent(String moodContent) {
		this.moodContent = moodContent;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public String getCreateTime() {
		if (createTime == null) {
			createTime = new String();
		}
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOwnerMemberNewsNum() {
		return ownerMemberNewsNum;
	}

	public void setOwnerMemberNewsNum(int ownerMemberNewsNum) {
		this.ownerMemberNewsNum = ownerMemberNewsNum;
	}

	public int getMemberNewsNum() {
		return memberNewsNum;
	}

	public void setMemberNewsNum(int memberNewsNum) {
		this.memberNewsNum = memberNewsNum;
	}

	public String getOwnerMemberNewUpdateTime() {
		if (ownerMemberNewUpdateTime == null) {
			ownerMemberNewUpdateTime = new String();
		}
		return ownerMemberNewUpdateTime;
	}

	public void setOwnerMemberNewUpdateTime(String ownerMemberNewUpdateTime) {
		this.ownerMemberNewUpdateTime = ownerMemberNewUpdateTime;
	}

	public String getMemberNewsUpdateTime() {
		if (memberNewsUpdateTime == null) {
			memberNewsUpdateTime = new String();
		}
		return memberNewsUpdateTime;
	}

	public void setMemberNewsUpdateTime(String memberNewsUpdateTime) {
		this.memberNewsUpdateTime = memberNewsUpdateTime;
	}

	public int getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(int friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public MemberContactBean getContactInfo() {
		if (contactInfo == null) {
			contactInfo = new MemberContactBean();
		}
		return contactInfo;
	}

	public void setContactInfo(MemberContactBean contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getFullHeadPath() {
		return this.memberHeadUrl + this.memberHeadPath;
	}
}
