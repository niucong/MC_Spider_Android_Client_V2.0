package com.datacomo.mc.spider.android.net.been;

public class MembercacheMInfo {

	/**
	 * 用户id
	 */
	private int memberID;
	/**
	 * 用户名字
	 */
	private String memberName;
	/**
	 * 用户头像前缀
	 */
	private String memberHeadUrl;
	/**
	 * 用户头像路径
	 */
	private String memberheadNowPath;
	/**
	 * 用户手机号
	 */
	private String memberphone;
	/**
	 * 用户性别
	 */
	private int memberSex;
	/**
	 * 用户生日
	 */
	private String memberBirthday;
	/**
	 * 用户邮箱
	 */
	private String memberMail;
	/**
	 * 用户圈币
	 */
	private String memberMood;
	/**
	 * 用户注册时间
	 */
	private String registerTime;

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
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

	public String getMemberheadNowPath() {
		return memberheadNowPath;
	}

	public void setMemberheadNowPath(String memberheadNowPath) {
		this.memberheadNowPath = memberheadNowPath;
	}

	public String getMemberphone() {
		return memberphone;
	}

	public void setMemberphone(String memberphone) {
		this.memberphone = memberphone;
	}

	public int getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(int memberSex) {
		this.memberSex = memberSex;
	}

	public String getMemberBirthday() {
		return memberBirthday;
	}

	public void setMemberBirthday(String memberBirthday) {
		this.memberBirthday = memberBirthday;
	}

	public String getMemberMail() {
		return memberMail;
	}

	public void setMemberMail(String memberMail) {
		this.memberMail = memberMail;
	}

	public String getMemberMood() {
		return memberMood;
	}

	public void setMemberMood(String memberMood) {
		this.memberMood = memberMood;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
 
	public String getWholeHeadUrl(){
		return getMemberHeadUrl() + getMemberheadNowPath();
	}
}
