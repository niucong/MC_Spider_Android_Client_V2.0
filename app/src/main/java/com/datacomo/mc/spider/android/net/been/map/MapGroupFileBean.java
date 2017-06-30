package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupFileBean;

public class MapGroupFileBean {

	/**
	 * 成员个数
	 */
	private int FILE_TOTAL_NUM;

	/**
	 * 成员列表
	 */
	private List<GroupFileBean> GROUP_FILE_LIST;

	public int getFILE_TOTAL_NUM() {
		return FILE_TOTAL_NUM;
	}

	public void setFILE_TOTAL_NUM(int fILE_TOTAL_NUM) {
		FILE_TOTAL_NUM = fILE_TOTAL_NUM;
	}

	public List<GroupFileBean> getGROUP_FILE_LIST() {
		return GROUP_FILE_LIST;
	}

	public void setGROUP_FILE_LIST(List<GroupFileBean> gROUP_FILE_LIST) {
		GROUP_FILE_LIST = gROUP_FILE_LIST;
	}

}
