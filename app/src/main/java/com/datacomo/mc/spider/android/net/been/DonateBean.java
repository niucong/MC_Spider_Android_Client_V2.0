package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class DonateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708184739072998799L;

	private int memberId;
	private int memberName;
	private int groupId;
	private String goldNumber;
	private String donateTimeDate;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberName() {
		return memberName;
	}

	public void setMemberName(int memberName) {
		this.memberName = memberName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGoldNumber() {
		return goldNumber;
	}

	public void setGoldNumber(String goldNumber) {
		this.goldNumber = goldNumber;
	}

	public String getDonateTimeDate() {
		return donateTimeDate;
	}

	public void setDonateTimeDate(String donateTimeDate) {
		this.donateTimeDate = donateTimeDate;
	}

}
