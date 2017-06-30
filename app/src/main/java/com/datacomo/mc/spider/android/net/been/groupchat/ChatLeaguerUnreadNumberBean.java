package com.datacomo.mc.spider.android.net.been.groupchat;

public class ChatLeaguerUnreadNumberBean {

	/**
	 *  0:失败1:成功2:用户不存在
	 */
	private int RESULT;
	
	/**
	 *  未读数量
	 */
	private int UNREAD_NUM;

	public int getRESULT() {
		return RESULT;
	}

	public void setRESULT(int rESULT) {
		RESULT = rESULT;
	}

	public int getUNREAD_NUM() {
		return UNREAD_NUM;
	}

	public void setUNREAD_NUM(int uNREAD_NUM) {
		UNREAD_NUM = uNREAD_NUM;
	}
	
	
}
