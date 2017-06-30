package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.ResourceCommentBean;

public class MapResourceCommentBean {

	/**
	 * 列表个数
	 */
	private int TOTALNUM;

	/**
	 * 评论
	 */
	private List<ResourceCommentBean> LIST;

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<ResourceCommentBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<ResourceCommentBean> pLIST) {
		LIST = pLIST;
	}
}
