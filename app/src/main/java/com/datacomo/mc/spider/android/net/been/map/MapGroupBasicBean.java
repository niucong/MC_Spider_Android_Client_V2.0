package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupBasicBean;

public class MapGroupBasicBean {

	/**
	 * 个数
	 */
	private int TOTALNUM;

	/**
	 * 列表
	 */
	private List<GroupBasicBean> COLLABORATEGROUPLIST;

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<GroupBasicBean> getCOLLABORATEGROUPLIST() {
		return COLLABORATEGROUPLIST;
	}

	public void setCOLLABORATEGROUPLIST(
			List<GroupBasicBean> cOLLABORATEGROUPLIST) {
		COLLABORATEGROUPLIST = cOLLABORATEGROUPLIST;
	}

}
