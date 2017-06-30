package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.MailContactBean;

public class MapMailContactBean {

	/**
	 * 成员个数
	 */
	private int TOTALNUM;

	/**
	 * 未读邮件个数
	 */
	private int UNREAD_EMAILS_NUM;

	/**
	 * 邮件总个数
	 */
	private int EMAILS_NUM;

	/**
	 * 成员列表
	 */
	private List<MailContactBean> CONTACTLIST;

	public int getUNREAD_EMAILS_NUM() {
		return UNREAD_EMAILS_NUM;
	}

	public void setUNREAD_EMAILS_NUM(int uNREAD_EMAILS_NUM) {
		UNREAD_EMAILS_NUM = uNREAD_EMAILS_NUM;
	}

	public int getEMAILS_NUM() {
		return EMAILS_NUM;
	}

	public void setEMAILS_NUM(int eMAILS_NUM) {
		EMAILS_NUM = eMAILS_NUM;
	}

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<MailContactBean> getCONTACTLIST() {
		return CONTACTLIST;
	}

	public void setCONTACTLIST(List<MailContactBean> cONTACTLIST) {
		CONTACTLIST = cONTACTLIST;
	}

}
