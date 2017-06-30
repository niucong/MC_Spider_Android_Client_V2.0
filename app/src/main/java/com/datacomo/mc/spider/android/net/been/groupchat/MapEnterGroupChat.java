package com.datacomo.mc.spider.android.net.been.groupchat;


public class MapEnterGroupChat {

	/**
	 * 0:失败1:成功2:社员不属于该圈子
	 */
	private String RESULT;
	private GroupChatBasicInfoBean GroupChatInfoBean;

	public String getRESULT() {
		return RESULT;
	}

	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

	public GroupChatBasicInfoBean getGroupChatInfoBean() {
		return GroupChatInfoBean;
	}

	public void setGroupChatInfoBean(GroupChatBasicInfoBean groupChatInfoBean) {
		GroupChatInfoBean = groupChatInfoBean;
	}

}
