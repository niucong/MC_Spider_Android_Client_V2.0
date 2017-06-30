package com.datacomo.mc.spider.android.net.been.map;

public class MapGroupChatCurrentStatus {

	/**
	 * 1、在圈聊中 2、不在圈聊中
	 */
	private int VISITSTATUS;
	/**
	 * 1、圈主 2、管理员 3、4、普通成员 5、申请成为普通成员
	 */
	private int VISITROLE;
	/**
	 * 当前圈聊人数
	 */
	private int MEMBERNUM;

	public int getVISITSTATUS() {
		return VISITSTATUS;
	}

	public void setVISITSTATUS(int vISITSTATUS) {
		VISITSTATUS = vISITSTATUS;
	}

	public int getVISITROLE() {
		return VISITROLE;
	}

	public void setVISITROLE(int vISITROLE) {
		VISITROLE = vISITROLE;
	}

	public int getMEMBERNUM() {
		return MEMBERNUM;
	}

	public void setMEMBERNUM(int mEMBERNUM) {
		MEMBERNUM = mEMBERNUM;
	}

}
