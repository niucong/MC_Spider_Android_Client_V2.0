package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.MailContactBean;

public class MapMailsByLeaguer {

	// private MemberBasicBean RELATIONBEAN;

	/**
	 * 成员个数
	 */
	private int TOTALNUM;

	/**
	 * 成员列表
	 */
	private List<MailContactBean> LIST;

	// public MemberBasicBean getRELATIONBEAN() {
	// return RELATIONBEAN;
	// }
	//
	// public void setRELATIONBEAN(MemberBasicBean rELATIONBEAN) {
	// RELATIONBEAN = rELATIONBEAN;
	// }

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<MailContactBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<MailContactBean> lIST) {
		LIST = lIST;
	}

}
