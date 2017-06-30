package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class QuuboInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8041000089327794506L;
	/**
	 * 圈博编号
	 */
	private int quuboId;
	/**
	 * 圈子编号
	 */
	private int groupId;
	/**
	 * 圈子信息
	 */
	private MemberOrGroupInfoBean groupInfoBean;
	/**
	 * 发送社员编号
	 */
	private int sendMemberId;
	/**
	 * 发送社员信息
	 */
	private MemberOrGroupInfoBean sendMemberInfoBean;
	/**
	 * 话题信息
	 */
	private GroupTopicBean groupTopicBean;
	/**
	 * 资源摘要信息
	 */
	private List<ObjectInfoBean> objectInfoBeans;
	/**
	 * 圈博标签
	 */
	private String quuboTag;
	/**
	 * 圈博类型 FROM_CREATE 社员创建 FROM_NOTEPAD 来自云笔记 FROM_SHARE 社员分享
	 */
	private String posterType;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * ip
	 */
	private String posterIp;
	/**
	 * ua
	 */
	private String useHead;
	/**
	 * way
	 */
	private String publishWay;
	/**
	 * source
	 */
	private String quuboSource;
	/**
	 * 分享数量
	 */
	private int shareNum;
	/**
	 * 下载数量
	 */
	private int downloadNum;
	/**
	 * 评论数量
	 */
	private int commentNum;
	/**
	 * 赞数量
	 */
	private int praiseNum;
	/**
	 * 收藏数量
	 */
	private int collectNum;
	/**
	 * 访问次数
	 */
	private int visitNum;
	/**
	 * 下载人数
	 */
	private int downloadMemberNum;
	/**
	 * 分享人数
	 */
	private int shareMemberNum;
	/**
	 * 访问人数
	 */
	private int visitMemberNum;

	private boolean hasPraise;

	/**
	 * 是否收藏过
	 */
	private boolean hasCollect;
	
	// private List<FriendSimpleBean> atMemberInfos;
	//
	// public List<FriendSimpleBean> getAtMemberInfos() {
	// return atMemberInfos;
	// }
	//
	// public void setAtMemberInfos(List<FriendSimpleBean> atMemberInfos) {
	// this.atMemberInfos = atMemberInfos;
	// }

	public boolean isHasCollect() {
		return hasCollect;
	}

	public void setHasCollect(boolean hasCollect) {
		this.hasCollect = hasCollect;
	}

	public int getQuuboId() {
		return quuboId;
	}

	public void setQuuboId(int quuboId) {
		this.quuboId = quuboId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public MemberOrGroupInfoBean getGroupInfoBean() {
		return groupInfoBean;
	}

	public void setGroupInfoBean(MemberOrGroupInfoBean groupInfoBean) {
		this.groupInfoBean = groupInfoBean;
	}

	public int getSendMemberId() {
		return sendMemberId;
	}

	public void setSendMemberId(int sendMemberId) {
		this.sendMemberId = sendMemberId;
	}

	public MemberOrGroupInfoBean getSendMemberInfoBean() {
		return sendMemberInfoBean;
	}

	public void setSendMemberInfoBean(MemberOrGroupInfoBean sendMemberInfoBean) {
		this.sendMemberInfoBean = sendMemberInfoBean;
	}

	public GroupTopicBean getGroupTopicBean() {
		return groupTopicBean;
	}

	public void setGroupTopicBean(GroupTopicBean groupTopicBean) {
		this.groupTopicBean = groupTopicBean;
	}

	public List<ObjectInfoBean> getObjectInfoBeans() {
		return objectInfoBeans;
	}

	public void setObjectInfoBeans(List<ObjectInfoBean> objectInfoBeans) {
		this.objectInfoBeans = objectInfoBeans;
	}

	public String getQuuboTag() {
		return quuboTag;
	}

	public void setQuuboTag(String quuboTag) {
		this.quuboTag = quuboTag;
	}

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPosterIp() {
		return posterIp;
	}

	public void setPosterIp(String posterIp) {
		this.posterIp = posterIp;
	}

	public String getUseHead() {
		return useHead;
	}

	public void setUseHead(String useHead) {
		this.useHead = useHead;
	}

	public String getPublishWay() {
		return publishWay;
	}

	public void setPublishWay(String publishWay) {
		this.publishWay = publishWay;
	}

	public String getQuuboSource() {
		return quuboSource;
	}

	public void setQuuboSource(String quuboSource) {
		this.quuboSource = quuboSource;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}

	public int getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}

	public int getDownloadMemberNum() {
		return downloadMemberNum;
	}

	public void setDownloadMemberNum(int downloadMemberNum) {
		this.downloadMemberNum = downloadMemberNum;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public boolean isHasPraise() {
		return hasPraise;
	}

	public void setHasPraise(boolean hasPraise) {
		this.hasPraise = hasPraise;
	}

}
