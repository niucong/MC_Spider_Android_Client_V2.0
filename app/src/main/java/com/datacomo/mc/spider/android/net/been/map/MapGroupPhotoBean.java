package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupPhotoBean;

public class MapGroupPhotoBean {

	/**
	 * 成员个数
	 */
	private int TOTALNUM;

	/**
	 * 成员列表
	 */
	private List<GroupPhotoBean> PHOTOLIST;

	public int getTOTALNUM() {
		return TOTALNUM;
	}

	public void setTOTALNUM(int tOTALNUM) {
		TOTALNUM = tOTALNUM;
	}

	public List<GroupPhotoBean> getPHOTOLIST() {
		return PHOTOLIST;
	}

	public void setPHOTOLIST(List<GroupPhotoBean> pHOTOLIST) {
		PHOTOLIST = pHOTOLIST;
	}

}
