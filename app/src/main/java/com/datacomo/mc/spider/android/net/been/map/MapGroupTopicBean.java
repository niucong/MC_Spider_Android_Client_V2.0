package com.datacomo.mc.spider.android.net.been.map;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupTopicBean;

public class MapGroupTopicBean {

	/**
	 * 成员个数
	 */
	private int GROUP_TOPIC_NUM;

	/**
	 * 成员列表
	 */
	private List<GroupTopicBean> GROUP_TOPIC_LIST;

	public int getGROUP_TOPIC_NUM() {
		return GROUP_TOPIC_NUM;
	}

	public void setGROUP_TOPIC_NUM(int gROUP_TOPIC_NUM) {
		GROUP_TOPIC_NUM = gROUP_TOPIC_NUM;
	}

	public List<GroupTopicBean> getGROUP_TOPIC_LIST() {
		return GROUP_TOPIC_LIST;
	}

	public void setGROUP_TOPIC_LIST(List<GroupTopicBean> gROUP_TOPIC_LIST) {
		GROUP_TOPIC_LIST = gROUP_TOPIC_LIST;
	}

}
