package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class GroupThemeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5394059628642803543L;
	/**
	 * 主题编号
	 */
	private int themeId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 社员名字
	 */
	private String memberName;
	/**
	 * 社员URL
	 */
	private String memberUrl;
	/**
	 * 社员Path
	 */
	private String memberPath;
	/**
	 * 圈子编号
	 */
	private int groupId;
	/**
	 * 主题内容
	 */
	private String themeContent;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 圈博数量
	 */
	private int quuboNum;
	/**
	 * 未读圈博数量
	 */
	private int noReadNum;
	/**
	 * 是否加关注过[true:关注过, false:没关注]
	 */
	private boolean hasFocus = false;
	/**
	 * 关注数量
	 */
	private int focusNum;
	/**
	 * 浏览数量
	 */
	private int browseNum;
	/**
	 * 分享数量
	 */
	private int shareNum;
	/**
	 * 操作者和圈子的关系
	 */
	private String groupLeaguerStatus;

	public int getNoReadNum() {
		return noReadNum;
	}

	public void setNoReadNum(int noReadNum) {
		this.noReadNum = noReadNum;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getThemeContent() {
		return themeContent;
	}

	public void setThemeContent(String themeContent) {
		this.themeContent = themeContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	public int getQuuboNum() {
		return quuboNum;
	}

	public void setQuuboNum(int quuboNum) {
		this.quuboNum = quuboNum;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isHasFocus() {
		return hasFocus;
	}

	public void setHasFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberUrl() {
		return memberUrl;
	}

	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}

	public String getMemberPath() {
		return memberPath;
	}

	public void setMemberPath(String memberPath) {
		this.memberPath = memberPath;
	}
	
	public String getMemberFullPath(){
		return memberUrl + memberPath;
	}

	public int getFocusNum() {
		return focusNum;
	}

	public void setFocusNum(int focusNum) {
		this.focusNum = focusNum;
	}

	public int getBrowseNum() {
		return browseNum;
	}

	public void setBrowseNum(int browseNum) {
		this.browseNum = browseNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public String getGroupLeaguerStatus() {
		return groupLeaguerStatus;
	}

	public void setGroupLeaguerStatus(String groupLeaguerStatus) {
		this.groupLeaguerStatus = groupLeaguerStatus;
	}

}
