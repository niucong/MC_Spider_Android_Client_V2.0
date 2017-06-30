package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class ResourceCommentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4332619991110932773L;

	/**
	 * 评论编号
	 */
	private int commentId;
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
	 * 评论社员编号
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
	 * 评论内容
	 */
	private String commentContent;
	/**
	 * 评论时间
	 */
	private long commentTime;
	/**
	 * ua信息
	 */
	private String useHead;
	/**
	 * 评论ip
	 */
	private String commentIp;

	/**
	 * 评论方式1、client=web（电脑）2、client=iphone（iPhone客户端）
	 * 3、client=android（Android客户端）4、android（Android）
	 * 5、ipad（iPad）6、iphone(iPhone)7、wap(手机)
	 */
	private String commentWay;
	/**
	 * 评论来源
	 */
	private String commentSource;

	/**
	 * 评论状态：0、成功，-1、评论中，-2、失败
	 */
	private int sendStatus;

	private List<FriendSimpleBean> atMemberInfos;

	public List<FriendSimpleBean> getAtMemberInfos() {
		return atMemberInfos;
	}

	public void setAtMemberInfos(List<FriendSimpleBean> atMemberInfos) {
		this.atMemberInfos = atMemberInfos;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
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

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public long getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(long commentTime) {
		this.commentTime = commentTime;
	}

	public String getUseHead() {
		return useHead;
	}

	public void setUseHead(String useHead) {
		this.useHead = useHead;
	}

	public String getCommentIp() {
		return commentIp;
	}

	public void setCommentIp(String commentIp) {
		this.commentIp = commentIp;
	}

	public String getCommentWay() {
		return commentWay;
	}

	public void setCommentWay(String commentWay) {
		this.commentWay = commentWay;
	}

	public String getCommentSource() {
		return commentSource;
	}

	public void setCommentSource(String commentSource) {
		this.commentSource = commentSource;
	}

	public String getFullHeadPath() {
		return this.memberHeadUrl + this.memberHeadPath;
	}

	String[] receiveIds;

	public String[] getReceiveIds() {
		return receiveIds;
	}

	public void setReceiveIds(String[] receiveIds) {
		this.receiveIds = receiveIds;
	}

}
