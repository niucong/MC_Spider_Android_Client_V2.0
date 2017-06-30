package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.FriendSimpleBean;

public class MapFriendSimpleBean {

	/**
	 * 最后一次删除时间
	 */
	private long LAST_DELETE_TIME;

	/**
	 * 最后一次查询时间
	 */
	private long START_SEARCH_TIME;

	/**
	 * 最后一次更新时间
	 */
	private long LAST_UPDATE_TIME;

	/**
	 * 更新列表
	 */
	private List<FriendSimpleBean> FRIEND_UPDATE_LIST;

	/**
	 * 删除列表
	 */
	private String FRIEND_DELETE_LIST;

	public long getLAST_DELETE_TIME() {
		return LAST_DELETE_TIME;
	}

	public void setLAST_DELETE_TIME(long lAST_DELETE_TIME) {
		LAST_DELETE_TIME = lAST_DELETE_TIME;
	}

	public long getSTART_SEARCH_TIME() {
		return START_SEARCH_TIME;
	}

	public void setSTART_SEARCH_TIME(long sTART_SEARCH_TIME) {
		START_SEARCH_TIME = sTART_SEARCH_TIME;
	}

	public long getLAST_UPDATE_TIME() {
		return LAST_UPDATE_TIME;
	}

	public void setLAST_UPDATE_TIME(long lAST_UPDATE_TIME) {
		LAST_UPDATE_TIME = lAST_UPDATE_TIME;
	}

	public List<FriendSimpleBean> getFRIEND_UPDATE_LIST() {
		return FRIEND_UPDATE_LIST;
	}

	public void setFRIEND_UPDATE_LIST(List<FriendSimpleBean> fRIEND_UPDATE_LIST) {
		FRIEND_UPDATE_LIST = fRIEND_UPDATE_LIST;
	}

	public String getFRIEND_DELETE_LIST() {
		return FRIEND_DELETE_LIST;
	}

	public void setFRIEND_DELETE_LIST(String fRIEND_DELETE_LIST) {
		FRIEND_DELETE_LIST = fRIEND_DELETE_LIST;
	}

}
