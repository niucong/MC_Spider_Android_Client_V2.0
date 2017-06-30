package com.datacomo.mc.spider.android.net.been.map;

import java.util.ArrayList;
import java.util.List;

import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.net.been.GroupSimpleBean;

public class MapGroupSimpleBean {

	/**
	 * 最后一次删除时间
	 */
	private long LAST_DELETE_TIME;

	/**
	 * 最后一次查询时间
	 */
	private long START_SEARCH_TIME;

	/**
	 * 最后一次更新时间
	 */
	private long LAST_UPDATE_TIME;

	/**
	 * 更新列表
	 */
	private List<GroupSimpleBean> GROUP_UPDATE_LIST;

	/**
	 * 删除列表
	 */
	private String GROUP_DELETE_LIST;

	public long getLAST_DELETE_TIME() {
		return LAST_DELETE_TIME;
	}

	public void setLAST_DELETE_TIME(long lAST_DELETE_TIME) {
		LAST_DELETE_TIME = lAST_DELETE_TIME;
	}

	public long getSTART_SEARCH_TIME() {
		return START_SEARCH_TIME;
	}

	public void setSTART_SEARCH_TIME(long sTART_SEARCH_TIME) {
		START_SEARCH_TIME = sTART_SEARCH_TIME;
	}

	public long getLAST_UPDATE_TIME() {
		return LAST_UPDATE_TIME;
	}

	public void setLAST_UPDATE_TIME(long lAST_UPDATE_TIME) {
		LAST_UPDATE_TIME = lAST_UPDATE_TIME;
	}

	public List<GroupSimpleBean> getGROUP_UPDATE_LIST() {
		return GROUP_UPDATE_LIST;
	}

	public void setGROUP_UPDATE_LIST(List<GroupSimpleBean> gROUP_UPDATE_LIST) {
		GROUP_UPDATE_LIST = gROUP_UPDATE_LIST;
	}

	public String getGROUP_DELETE_LIST() {
		return GROUP_DELETE_LIST;
	}

	public void setGROUP_DELETE_LIST(String gROUP_DELETE_LIST) {
		GROUP_DELETE_LIST = gROUP_DELETE_LIST;
	}

	public ArrayList<GroupEntity> getGroupEntityList() {
		ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
		if (GROUP_UPDATE_LIST != null) {
			for (GroupSimpleBean bean : GROUP_UPDATE_LIST) {
				GroupEntity entity = new GroupEntity(bean.getGroupId() + "",
						bean.getGroupName(), bean.getGroupPosterUrl()
								+ bean.getGroupPosterPath(),
						bean.getGroupNamePy(), bean.getGroupNameJp(),
						bean.getOpenStatus() + "");
				entity.setGroupProperty(bean.getGroupProperty());
				entity.setGroupType(bean.getGroupType());
				list.add(entity);
			}
		}
		return list;
	}

}
