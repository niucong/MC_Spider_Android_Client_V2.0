package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberEmotionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 社员标识号
	 */
	private int memberId;

	/**
	 * 社员感情状态隐私设置
	 */
	// private PrivacyLevel viewEmotionSetting;
	private String viewEmotionSetting;

	/**
	 * 社员当前情感状态：1：单身 2：恋爱中 3：已订婚 4：结婚 5：丧偶 6：分居 7：离婚
	 */
	private int emotionStatus;

	/**
	 * 情感对象社区标识号
	 */
	private int loverId;

	/**
	 * 情感对象名字
	 */
	private String loverName;
	/**
	 * 情感对象头像
	 */
	private MemberHeadBean loverHead;
	// private Object loverHead;

	/**
	 * 情感对象周年纪念日
	 */
	private String emotionAnniversary;
	// private Object emotionAnniversary;
	/**
	 * 情感对象确认状态：1：待确认 2：确认
	 */
	private int emotionConfirmStatus;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getViewEmotionSetting() {
		return viewEmotionSetting;
	}

	public void setViewEmotionSetting(String viewEmotionSetting) {
		this.viewEmotionSetting = viewEmotionSetting;
	}

	public int getEmotionStatus() {
		return emotionStatus;
	}

	public void setEmotionStatus(int emotionStatus) {
		this.emotionStatus = emotionStatus;
	}

	public int getLoverId() {
		return loverId;
	}

	public void setLoverId(int loverId) {
		this.loverId = loverId;
	}

	public String getLoverName() {
		return loverName;
	}

	public void setLoverName(String loverName) {
		this.loverName = loverName;
	}

	public MemberHeadBean getLoverHead() {
		return loverHead;
	}

	public void setLoverHead(MemberHeadBean loverHead) {
		this.loverHead = loverHead;
	}

	public String getEmotionAnniversary() {
		return emotionAnniversary;
	}

	public void setEmotionAnniversary(String emotionAnniversary) {
		this.emotionAnniversary = emotionAnniversary;
	}

	public int getEmotionConfirmStatus() {
		return emotionConfirmStatus;
	}

	public void setEmotionConfirmStatus(int emotionConfirmStatus) {
		this.emotionConfirmStatus = emotionConfirmStatus;
	}

}
