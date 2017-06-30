package com.datacomo.mc.spider.android.net.been.map;

import java.util.ArrayList;
import java.util.List;

import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;

public class MapResourceGreatBean {

	/**
	 * 列表个数
	 */
	private int TOTALNUM;
	private int RESULT;
	private int COUNT;

	/**
	 * 赞
	 */
	private List<ResourceGreatBean> PRAISELIST;
	private List<ResourceGreatBean> LIST;

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<ResourceGreatBean> getPRAISELIST() {
		if (PRAISELIST == null) {
			PRAISELIST = new ArrayList<ResourceGreatBean>();
		}
		return PRAISELIST;
	}

	public void setPRAISELIST(List<ResourceGreatBean> pRAISELIST) {
		PRAISELIST = pRAISELIST;
	}

	public int getRESULT() {
		return RESULT;
	}

	public void setRESULT(int rESULT) {
		RESULT = rESULT;
	}

	public int getCOUNT() {
		return COUNT;
	}

	public void setCOUNT(int cOUNT) {
		COUNT = cOUNT;
	}

	public List<ResourceGreatBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<ResourceGreatBean> lIST) {
		LIST = lIST;
	}

}
