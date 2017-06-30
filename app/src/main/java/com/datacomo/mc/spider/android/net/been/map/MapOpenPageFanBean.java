package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;

public class MapOpenPageFanBean {

	/**
	 * 成员个数
	 */
	private int OPEN_PAGE_FANS_NUM;

	/**
	 * 成员列表
	 */
	private List<GroupLeaguerBean> OPEN_PAGE_FANS_LIST;

	public int getOPEN_PAGE_FANS_NUM() {
		return OPEN_PAGE_FANS_NUM;
	}

	public void setOPEN_PAGE_FANS_NUM(int oPEN_PAGE_FANS_NUM) {
		OPEN_PAGE_FANS_NUM = oPEN_PAGE_FANS_NUM;
	}

	public List<GroupLeaguerBean> getOPEN_PAGE_FANS_LIST() {
		return OPEN_PAGE_FANS_LIST;
	}

	public void setOPEN_PAGE_FANS_LIST(
			List<GroupLeaguerBean> oPEN_PAGE_FANS_LIST) {
		OPEN_PAGE_FANS_LIST = oPEN_PAGE_FANS_LIST;
	}

}
