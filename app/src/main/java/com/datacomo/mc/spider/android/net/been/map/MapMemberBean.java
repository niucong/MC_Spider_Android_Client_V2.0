package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.MemberBean;

public class MapMemberBean {

	/**
	 * 成员个数
	 */
	private int MEMBER_NUMBER;

	/**
	 * 成员列表
	 */
	private List<MemberBean> MEMBER_LIST;

	public int getMEMBER_NUMBER() {
		return MEMBER_NUMBER;
	}

	public void setMEMBER_NUMBER(int mEMBER_NUMBER) {
		MEMBER_NUMBER = mEMBER_NUMBER;
	}

	public List<MemberBean> getMEMBER_LIST() {
		return MEMBER_LIST;
	}

	public void setMEMBER_LIST(List<MemberBean> mEMBER_LIST) {
		MEMBER_LIST = mEMBER_LIST;
	}

}
