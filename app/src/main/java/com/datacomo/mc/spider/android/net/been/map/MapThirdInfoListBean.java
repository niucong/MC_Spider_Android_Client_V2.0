package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.ThirdInfoBean;

/**
 * 获取第三方应用列表
 * 
 * @author datacomo-160
 * 
 */
public class MapThirdInfoListBean {

	/**
	 * 个数
	 */
	private int NUM;

	/**
	 * 列表
	 */
	private List<ThirdInfoBean> LIST;

	public int getNUM() {
		return NUM;
	}

	public void setNUM(int nUM) {
		NUM = nUM;
	}

	public List<ThirdInfoBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<ThirdInfoBean> lIST) {
		LIST = lIST;
	}

}
