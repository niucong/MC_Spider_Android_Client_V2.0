package com.datacomo.mc.spider.android.net.been.groupchat;

import java.util.ArrayList;
import java.util.List;

public class MapGroupChatMessage {

	/**
	 * RESULT 0:失败1:成功
	 */
	private int RESULT;
	private int LEAGUER_NUM;
	private int COUNT;
	/**
	 * 消息列表
	 */
	private List<GroupChatMessageBean> LIST;

	public int getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(int cOUNT) {
		COUNT = cOUNT;
	}

	public int getRESULT() {
		return RESULT;
	}

	public void setRESULT(int rESULT) {
		RESULT = rESULT;
	}

	public int getLEAGUER_NUM() {
		return LEAGUER_NUM;
	}

	public void setLEAGUER_NUM(int lEAGUER_NUM) {
		LEAGUER_NUM = lEAGUER_NUM;
	}

	public List<GroupChatMessageBean> getLIST() {
		List<GroupChatMessageBean> newBeans = new ArrayList<GroupChatMessageBean>();
		if (null != LIST) {
			for (GroupChatMessageBean groupChatMessageBean : LIST) {
				newBeans.add(0, groupChatMessageBean);
			}
		}
		return newBeans;
	}

	public void setLIST(List<GroupChatMessageBean> lIST) {
		LIST = lIST;
	}

}
