package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

/**
 * 社员基本信息对象，存放社员姓名、性别、生日、登录状态等信息
 */
public class MemberBasicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员编号
	 */
	private int memberId;

	/**
	 * 社员名字
	 */
	private String memberName;

	/**
	 * 头像
	 */
	private MemberHeadBean headImage;
	// private Object headImage;

	/**
	 * 心情用语 --member_mood
	 */
	private String feelingWord;

	/**
	 * 心情用语的发布时间
	 */
	// private Date feelingWordTime;
	// private Object feelingWordTime;

	/**
	 * 心情用语的发布方式
	 */
	private String feelingWordWay;

	/**
	 * 心情用语隐私设置 -- 暂时无此设置
	 */
	// private PrivacyLevel viewFeelingSetting;
	private String viewFeelingSetting;

	/**
	 * 性别 :1 男 2：女
	 */
	private int memberSex;

	/**
	 * 生日
	 */
	private String birthday;
	// private Object birthday;

	/**
	 * 生日显示格式 1：在个人主页显示完整出生日期 2：在个人主页只显示月份和日期 3：不在个人主页上显示我的出生日期
	 */
	private int birthdayFormat;

	/**
	 * 生日隐私设置
	 */
	// private PrivacyLevel viewBirthdaySetting;
	private String viewBirthdaySetting;

	/**
	 * 社员的兴趣爱好
	 */
	private String memberHobby;

	/**
	 * 社员的兴趣爱好隐私设置
	 */
	// private PrivacyLevel viewHobbySetting;
	private String viewHobbySetting;

	/**
	 * 自我介绍
	 */
	private String introduction;

	/**
	 * 自我介绍隐私设置
	 */
	// private PrivacyLevel viewIntroSetting;
	private String viewIntroSetting;

	/**
	 * 社员性取向 1.男 2.女 3.男和女
	 */
	private int memberSexual;

	/**
	 * 性取向隐私设置
	 */
	// private PrivacyLevel viewSexualSetting;
	private String viewSexualSetting;

	/**
	 * 注册时间
	 */
	private String registerTime;
	// private Object registerTime;

	/**
	 * 社员情感状态
	 */
	private int memberEmotionStatus;

	/**
	 * 社员手机号
	 */
	private String memberPhone;

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

	public MemberHeadBean getHeadImage() {
		return headImage;
	}

	public void setHeadImage(MemberHeadBean headImage) {
		this.headImage = headImage;
	}

	public String getFeelingWord() {
		return feelingWord;
	}

	public void setFeelingWord(String feelingWord) {
		this.feelingWord = feelingWord;
	}

	// public Date getFeelingWordTime() {
	// return feelingWordTime;
	// }
	//
	// public void setFeelingWordTime(Date feelingWordTime) {
	// this.feelingWordTime = feelingWordTime;
	// }

	public String getFeelingWordWay() {
		return feelingWordWay;
	}

	public void setFeelingWordWay(String feelingWordWay) {
		this.feelingWordWay = feelingWordWay;
	}

	public String getViewFeelingSetting() {
		return viewFeelingSetting;
	}

	public void setViewFeelingSetting(String viewFeelingSetting) {
		this.viewFeelingSetting = viewFeelingSetting;
	}

	public int getMemberSex() {
		return memberSex;
	}

	public void setMemberSex(int memberSex) {
		this.memberSex = memberSex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getBirthdayFormat() {
		return birthdayFormat;
	}

	public void setBirthdayFormat(int birthdayFormat) {
		this.birthdayFormat = birthdayFormat;
	}

	public String getViewBirthdaySetting() {
		return viewBirthdaySetting;
	}

	public void setViewBirthdaySetting(String viewBirthdaySetting) {
		this.viewBirthdaySetting = viewBirthdaySetting;
	}

	public String getMemberHobby() {
		return memberHobby;
	}

	public void setMemberHobby(String memberHobby) {
		this.memberHobby = memberHobby;
	}

	public String getViewHobbySetting() {
		return viewHobbySetting;
	}

	public void setViewHobbySetting(String viewHobbySetting) {
		this.viewHobbySetting = viewHobbySetting;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getViewIntroSetting() {
		return viewIntroSetting;
	}

	public void setViewIntroSetting(String viewIntroSetting) {
		this.viewIntroSetting = viewIntroSetting;
	}

	public int getMemberSexual() {
		return memberSexual;
	}

	public void setMemberSexual(int memberSexual) {
		this.memberSexual = memberSexual;
	}

	public String getViewSexualSetting() {
		return viewSexualSetting;
	}

	public void setViewSexualSetting(String viewSexualSetting) {
		this.viewSexualSetting = viewSexualSetting;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public int getMemberEmotionStatus() {
		return memberEmotionStatus;
	}

	public void setMemberEmotionStatus(int memberEmotionStatus) {
		this.memberEmotionStatus = memberEmotionStatus;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

}
