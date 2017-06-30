package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class MemberOrGroupInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2396128533810052097L;

	/*
	 * 社员ID
	 */
	private int id;
	/*
	 * 社员名字
	 */
	private String name;
	/*
	 * 社员备注
	 */
	// private String markName;
	/*
	 * 社员头像URL
	 */
	private String headUrl;
	/*
	 * 社员头像Path
	 */
	private String headPath;

	/*
	 * 圈子属性 n1 - 普通圈子\r\n2 - 校园圈子\r\n3 - 企业圈子\r\n4 - 加V认证圈子
	 */
	// private int groupProperty;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public String getMarkName() {
	// return markName;
	// }
	//
	// public void setMarkName(String markName) {
	// this.markName = markName;
	// }

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getHeadPath() {
		return headPath;
	}

	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}

	// public int getGroupProperty() {
	// return groupProperty;
	// }
	//
	// public void setGroupProperty(int groupProperty) {
	// this.groupProperty = groupProperty;
	// }
	
	public String getFullHeadPath() {
		return headUrl + headPath;
	}

}
