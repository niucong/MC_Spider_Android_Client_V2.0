package com.datacomo.mc.spider.android.net.been.map;

import java.io.Serializable;
import java.util.List;

import com.datacomo.mc.spider.android.net.been.TakedClientWelcomPicGroupBean;

public class MapTakedClientWelcomPicGroupBean implements Serializable {

	/**
	 * 设置过客户端欢迎页的圈子
	 */
	private static final long serialVersionUID = -5086745131445847807L;
	/**
	 * 社员编号
	 */
	private int STATUS;
	/**
	 * 圈子编号
	 */
	private List<TakedClientWelcomPicGroupBean> LIST;

	public int getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(int sTATUS) {
		STATUS = sTATUS;
	}

	public List<TakedClientWelcomPicGroupBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<TakedClientWelcomPicGroupBean> lIST) {
		LIST = lIST;
	}

}
