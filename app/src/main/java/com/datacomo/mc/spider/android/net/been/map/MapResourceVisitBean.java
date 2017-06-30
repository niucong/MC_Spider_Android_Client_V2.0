package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.ResourceVisitBean;

public class MapResourceVisitBean {

	/**
	 * 访问人数
	 */
	private int TOTALMEMBERNUM;

	/**
	 * 总访问次数
	 */
	private int TOTALNUM;

	/**
	 * 访问列表
	 */
	private List<ResourceVisitBean> LIST;

	public int getTOTALMEMBERNUM() {
		return TOTALMEMBERNUM;
	}

	public void setTOTALMEMBERNUM(int tOTALMEMBERNUM) {
		TOTALMEMBERNUM = tOTALMEMBERNUM;
	}

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<ResourceVisitBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<ResourceVisitBean> lIST) {
		LIST = lIST;
	}

}
