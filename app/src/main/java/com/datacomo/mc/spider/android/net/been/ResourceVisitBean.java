package com.datacomo.mc.spider.android.net.been;

public class ResourceVisitBean {

	/**
	 * 访问记录主键
	 */
	private int visitId;
	/**
	 * 资源编号
	 */
	private int resourceId;
	/**
	 * 对象编号
	 */
	private int objectId;
	/**
	 * 对象类型
	 */
	private String objectType;
	/**
	 * 访问社员编号
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
	/**
	 * 访问数量
	 */
	private int visitNum;
	/**
	 * 访问时间
	 */
	private String visitTime;
	/**
	 * 最后访问时间
	 */
	private String lastVisitTime;

	public int getVisitId() {
		return visitId;
	}

	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
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

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
	public String getFullHeadPath(){
		return this.memberHeadUrl+this.memberHeadPath;
	}
}
