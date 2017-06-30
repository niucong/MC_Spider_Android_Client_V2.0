package com.datacomo.mc.spider.android.net.been.map;

import java.util.ArrayList;
import java.util.List;

import com.datacomo.mc.spider.android.net.been.ResourceBean;

public class MapResourceBean {

	/**
	 * 总访问次数
	 */
	private int TOTALNUM;

	/**
	 * 访问列表
	 */
	private List<ResourceBean> LIST;

	private String RELATION;

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<ResourceBean> getLIST() {
		if (LIST == null) {
			LIST = new ArrayList<ResourceBean>();
		}
		return LIST;
	}

	public void setLIST(List<ResourceBean> lIST) {
		LIST = lIST;
	}

	public String getRELATION() {
		return RELATION;
	}

	public void setRELATION(String rELATION) {
		RELATION = rELATION;
	}

}
