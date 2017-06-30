package com.datacomo.mc.spider.android.net.been.groupchat;

public class MemberSimpleBean {

	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 社员名字
	 */
	private String memberName;
	/**
	 * 社员头像URL
	 */
	private String memberHeadUrl;
	/**
	 * 社员头像Path
	 */
	private String memberHeadPath;
	/**
	 * 社员性别
	 */
	private String memberSex;
	/**
	 * 社员心情用语
	 */
	private String memberMood;

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

	public String getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}

	public String getMemberMood() {
		return memberMood;
	}

	public void setMemberMood(String memberMood) {
		this.memberMood = memberMood;
	}
	
	public String getHeadFullPath(){
		return memberHeadUrl+memberHeadPath;
	}

}
