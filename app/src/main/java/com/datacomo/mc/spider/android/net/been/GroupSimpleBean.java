package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

import com.datacomo.mc.spider.android.util.PinYin4JCn;

public class GroupSimpleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	private int groupId;
	/**
	 * 名字
	 */
	private String groupName;
	/**
	 * 名字拼音
	 */
	private String groupNamePy;
	/**
	 * 名字简拼
	 */
	private String groupNameJp;

	/**
	 * 头像前缀
	 */
	private String groupPosterUrl;

	/**
	 * 头像路径
	 */
	private String groupPosterPath;

	/**
	 * 圈子类型 1 - 公开 2 - 私密 3 - 自定义
	 */
	private int openStatus;

	/**
	 * 圈子属性:1-没有认证圈子 2-校园认证圈子 3-企业认证圈子 4-其它认证圈子
	 */
	private int groupProperty;

	/**
	 * 圈子类型：1-兴趣圈 2-企业圈 3-开放主页 4-外部社区
	 */
	private int groupType;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNamePy() {
		// String groupNamePy = null;
		if (groupNamePy == null || "".equals(groupNamePy)) {
			if (groupName != null && !"".equals(groupName)) {
				try {
					groupNamePy = PinYin4JCn.convertPy("group", groupName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {
					e.printStackTrace();
				} catch (ExceptionInInitializerError e) {
					e.printStackTrace();
				}
			}
		}
		// TODO
		if (groupNamePy != null && groupNamePy.contains(",")) {
			groupNamePy = groupNamePy.substring(groupNamePy.indexOf(",") + 1)
					+ "," + groupNamePy.substring(0, groupNamePy.indexOf(","));
		}
		if (groupNamePy == null || "".equals(groupNamePy))
			groupNamePy = "#";
		return groupNamePy.toLowerCase();
	}

	public void setGroupNamePy(String groupNamePy) {
		this.groupNamePy = groupNamePy;
	}

	public String getGroupNameJp() {
		if (groupNameJp == null || "".equals(groupNameJp)) {
			if (groupName != null && !"".equals(groupName)) {
				try {
					groupNameJp = PinYin4JCn.convertJp("group", groupName);
				} catch (Exception e) {
					e.printStackTrace();
				} catch (NoClassDefFoundError e) {

				} catch (ExceptionInInitializerError e) {

				}
			}
		}
		if (groupNameJp == null || "".equals(groupNameJp))
			groupNameJp = "#";
		return groupNameJp.toLowerCase();
	}

	public void setGroupNameJp(String groupNameJp) {
		this.groupNameJp = groupNameJp;
	}

	public String getGroupPosterUrl() {
		return groupPosterUrl;
	}

	public void setGroupPosterUrl(String groupPosterUrl) {
		this.groupPosterUrl = groupPosterUrl;
	}

	public String getGroupPosterPath() {
		return groupPosterPath;
	}

	public void setGroupPosterPath(String groupPosterPath) {
		this.groupPosterPath = groupPosterPath;
	}

	public int getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(int openStatus) {
		this.openStatus = openStatus;
	}

	public int getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(int groupProperty) {
		this.groupProperty = groupProperty;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

}
