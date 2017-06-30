package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.ShareLeaguerBean;

public class MapShareLeaguerBean {

	/**
	 * 
	 */
	private List<ShareLeaguerBean> SHARE_MEMBER_LIST;

	/**
	 * 
	 */
	private int TOTAL_NUM;

	public List<ShareLeaguerBean> getSHARE_MEMBER_LIST() {
		return SHARE_MEMBER_LIST;
	}

	public void setSHARE_MEMBER_LIST(List<ShareLeaguerBean> sHARE_MEMBER_LIST) {
		SHARE_MEMBER_LIST = sHARE_MEMBER_LIST;
	}

	public int getTOTAL_NUM() {
		return TOTAL_NUM;
	}

	public void setTOTAL_NUM(int tOTAL_NUM) {
		TOTAL_NUM = tOTAL_NUM;
	}

}
