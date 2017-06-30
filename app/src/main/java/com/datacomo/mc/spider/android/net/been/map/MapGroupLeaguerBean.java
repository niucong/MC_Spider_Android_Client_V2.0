package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;

public class MapGroupLeaguerBean {

	/**
	 * 成员个数
	 */
	private int SEARCHNUM;

	/**
	 * 成员列表
	 */
	private List<GroupLeaguerBean> SEARCHLIST;
	
	private int status;
	private List<GroupLeaguerBean> removedLeaguers;
	private List<GroupLeaguerBean> newLeaguers;

	public int getSEARCHNUM() {
		return SEARCHNUM;
	}

	public void setSEARCHNUM(int sEARCHNUM) {
		SEARCHNUM = sEARCHNUM;
	}

	public List<GroupLeaguerBean> getSEARCHLIST() {
		return SEARCHLIST;
	}

	public void setSEARCHLIST(List<GroupLeaguerBean> sEARCHLIST) {
		SEARCHLIST = sEARCHLIST;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<GroupLeaguerBean> getRemovedLeaguers() {
		return removedLeaguers;
	}

	public void setRemovedLeaguers(List<GroupLeaguerBean> removedLeaguers) {
		this.removedLeaguers = removedLeaguers;
	}

	public List<GroupLeaguerBean> getNewLeaguers() {
		return newLeaguers;
	}

	public void setNewLeaguers(List<GroupLeaguerBean> newLeaguers) {
		this.newLeaguers = newLeaguers;
	}

}
