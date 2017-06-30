package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.FriendBean;

public class MapFriendBean {

	/**
	 * 成员个数
	 */
	private int FRIEND_NUM;

	/**
	 * 成员列表
	 */
	private List<FriendBean> FRIEND_LIST;

	public int getFRIEND_NUM() {
		return FRIEND_NUM;
	}

	public void setFRIEND_NUM(int fRIEND_NUM) {
		FRIEND_NUM = fRIEND_NUM;
	}

	public List<FriendBean> getFRIEND_LIST() {
		return FRIEND_LIST;
	}

	public void setFRIEND_LIST(List<FriendBean> fRIEND_LIST) {
		FRIEND_LIST = fRIEND_LIST;
	}

}
