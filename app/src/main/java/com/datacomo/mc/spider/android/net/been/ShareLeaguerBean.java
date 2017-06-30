package com.datacomo.mc.spider.android.net.been;

public class ShareLeaguerBean {

	/**
	 * 
	 */
	private int leaguerId;
	/**
	 * 所有者社员编号
	 */
	private int ownerMemberId;
	/**
	 * 分享的社员编号
	 */
	private int relationMemberId;
	/**
	 * 分享的社员名字
	 */
	private String relationMemberName;
	/**
	 * 分享的社员头像路径前缀
	 */
	private String relationMemberHeadUrl;
	/**
	 * 分享的社员头像路径
	 */
	private String relationMemberHeadPath;
	/**
	 * 总文件的数量，即分享的和被分享的数量的总和
	 */
	private int totalFileNum;
	/**
	 * 分享文件的总数量
	 */
	private int shareFileNum;
	/**
	 * 被分享的文件数量
	 */
	private int receiveFileNum;
	/**
	 * 新分享文件的数量，即未读分享的数量
	 */
	private int newShareNum;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 最后分享时间
	 */
	private String lastShareTime;
	/**
	 * 最后分享的社员
	 */
	private String lastShareMemberName;
	/**
	 * 最后分享的文件名字
	 */
	private String lastShareFileName;
	/**
	 * 最后分享圈子的名字
	 */
	private String lastShareGroupName;

	public int getLeaguerId() {
		return leaguerId;
	}

	public void setLeaguerId(int leaguerId) {
		this.leaguerId = leaguerId;
	}

	public int getOwnerMemberId() {
		return ownerMemberId;
	}

	public void setOwnerMemberId(int ownerMemberId) {
		this.ownerMemberId = ownerMemberId;
	}

	public int getRelationMemberId() {
		return relationMemberId;
	}

	public void setRelationMemberId(int relationMemberId) {
		this.relationMemberId = relationMemberId;
	}

	public String getRelationMemberName() {
		return relationMemberName;
	}

	public void setRelationMemberName(String relationMemberName) {
		this.relationMemberName = relationMemberName;
	}

	public String getRelationMemberHeadUrl() {
		return relationMemberHeadUrl;
	}

	public void setRelationMemberHeadUrl(String relationMemberHeadUrl) {
		this.relationMemberHeadUrl = relationMemberHeadUrl;
	}

	public String getRelationMemberHeadPath() {
		return relationMemberHeadPath;
	}

	public void setRelationMemberHeadPath(String relationMemberHeadPath) {
		this.relationMemberHeadPath = relationMemberHeadPath;
	}

	public int getTotalFileNum() {
		return totalFileNum;
	}

	public void setTotalFileNum(int totalFileNum) {
		this.totalFileNum = totalFileNum;
	}

	public int getShareFileNum() {
		return shareFileNum;
	}

	public void setShareFileNum(int shareFileNum) {
		this.shareFileNum = shareFileNum;
	}

	public int getReceiveFileNum() {
		return receiveFileNum;
	}

	public void setReceiveFileNum(int receiveFileNum) {
		this.receiveFileNum = receiveFileNum;
	}

	public int getNewShareNum() {
		return newShareNum;
	}

	public void setNewShareNum(int newShareNum) {
		this.newShareNum = newShareNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(String lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public String getLastShareMemberName() {
		return lastShareMemberName;
	}

	public void setLastShareMemberName(String lastShareMemberName) {
		this.lastShareMemberName = lastShareMemberName;
	}

	public String getLastShareFileName() {
		return lastShareFileName;
	}

	public void setLastShareFileName(String lastShareFileName) {
		this.lastShareFileName = lastShareFileName;
	}

	public String getLastShareGroupName() {
		return lastShareGroupName;
	}

	public void setLastShareGroupName(String lastShareGroupName) {
		this.lastShareGroupName = lastShareGroupName;
	}

}
