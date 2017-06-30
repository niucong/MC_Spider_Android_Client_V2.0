package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;

public class ContactInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 197419588041612399L;

	private String name;// 名字
	private String number;// 号码
	private int contactMemberId;
	private String memberHead;

	private String registerStatus;// 注册状态： 1已注册、2未注册
	private String friendStatus;// 朋友状态：1：是朋友;2：申请加为朋友;3：非朋友

	public ContactInfo(String name, String number) {
		this.name = name;
		this.number = number;
	}

	public int getContactMemberId() {
		return contactMemberId;
	}

	public void setContactMemberId(int contactMemberId) {
		this.contactMemberId = contactMemberId;
	}

	public String getMemberHead() {
		return memberHead;
	}

	public void setMemberHead(String memberHead) {
		this.memberHead = memberHead;
	}

	public String getName() {
		if (name == null) {
			name = number;
		}
		return name;
	}

	public String getNumber() {
		return number;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}

	public String getRegisterStatus() {
		return registerStatus;
	}

	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getFriendStatus() {
		return friendStatus;
	}

	public boolean hasRegiste() {
		if ("1".equals(registerStatus)) {
			return true;
		} else {
			return false;
		}
	}
}
