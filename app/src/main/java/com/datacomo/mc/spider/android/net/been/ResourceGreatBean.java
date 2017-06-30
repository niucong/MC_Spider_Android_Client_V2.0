package com.datacomo.mc.spider.android.net.been;

public class ResourceGreatBean {

	/**
	 * 赞主键
	 */
	private int greatId;
	/**
	 * 资源编号
	 */
	private int resourceId;
	/**
	 * 社员编号
	 */
	private int memberId;
	/**
	 * 社员名字
	 */
	private String memberName;
	/**
	 * 社员头像url
	 */
	private String memberHeadUrl;
	/**
	 * 社员头像Path
	 */
	private String memberHeadPath;

	private String fullHeadPath;
	/**
	 * 对象编号
	 */
	private int objectId;
	/**
	 * 对象类型
	 */
	private String objectType;
	/**
	 * 创建时间
	 */
	private String createTime;
	private String lastVisitTime;
	private String lastShareTime;

	public ResourceGreatBean() {

	}

	public ResourceGreatBean(int memberId, String fullHeadPath) {
		this.memberId = memberId;
		this.fullHeadPath = fullHeadPath;
	}

	public String getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(String lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public String getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public int getGreatId() {
		return greatId;
	}

	public void setGreatId(int greatId) {
		this.greatId = greatId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}

	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}

	public String getMemberHeadPath() {
		return memberHeadPath;
	}

	public void setMemberHeadPath(String memberHeadPath) {
		this.memberHeadPath = memberHeadPath;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFullHeadPath() {
		if (fullHeadPath != null && !"".equals(fullHeadPath)) {
			return fullHeadPath;
		}
		return this.memberHeadUrl + this.memberHeadPath;
	}

}
