package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;

public class MapMessageNoticeBean {

	/**
	 * 总访问次数
	 */
	private int TOTAL_NUM;

	/**
	 * 访问列表
	 */
	private List<MessageNoticeBean> LIST;

	public int getTOTAL_NUM() {
		return TOTAL_NUM;
	}

	public void setTOTAL_NUM(int tOTAL_NUM) {
		TOTAL_NUM = tOTAL_NUM;
	}

	public List<MessageNoticeBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<MessageNoticeBean> lIST) {
		LIST = lIST;
	}

}
