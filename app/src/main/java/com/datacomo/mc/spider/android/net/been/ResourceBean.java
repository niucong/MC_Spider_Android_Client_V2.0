package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResourceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2378089399541931494L;

	/**
	 * 资源编号
	 */
	// private int resourceId;
	/**
	 * 对象编号
	 */
	private int objectId;
	/**
	 * 对象类型
	 */
	private String objectType;
	/**
	 * 对象信息
	 */
	private List<ObjectInfoBean> objectInfo;
	/**
	 * 主信息
	 */
//	 private List<ObjectInfoBean> objectSummary;

//	 private String objectInfoStr;
	/**
	 * 对象来源：MEMBER.社员 GROUP.圈子'
	 */
	// private String objectResource;

	/**
	 * 来源类型 ：FROM_CREATE 创建 FROM_notepad 来自云笔记 FROM_SHARE 来自分享
	 */
	private String sourceType;

	/**
	 * 发布方式
	 */
	private String publishWay;

	/**
	 * 资源发起者
	 */
	private int sendMemberId;
	/**
	 * 资源发起者详细信息
	 */
	private MemberOrGroupInfoBean sendMemberInfo;
	/**
	 * 接收者编号
	 */
	// private int receiveMemberId;
	/**
	 * 接收者
	 */
	// private MemberOrGroupInfoBean receiveMemberInfo;
	/**
	 * 资源所有者编号
	 */
	private int objectOwnerId;
	/**
	 * 资源所有者信息
	 */
	private MemberOrGroupInfoBean objOwnerMemberInfo;
	/**
	 * 资源评论一
	 */
	// private ResourceCommentBean objectCommentOne;
	/**
	 * 资源评论二
	 */
	// private ResourceCommentBean objectCommentTwo;
	/**
	 * 资源评论三
	 */
	// private ResourceCommentBean objectCommentThree;
	/**
	 * 标签
	 */
	// private String objTag;
	/**
	 * 分享数量
	 */
	// private int shareNum;
	/**
	 * 访问数量
	 */
	// private int visitNum;
	/**
	 * 收藏数量
	 */
	// private int collectNum;
	/**
	 * 赞数量
	 */
	private int praiseNum;
	/**
	 * 评论数量
	 */
	private int commentNum;
	/**
	 * 下载数量
	 */
	// private int downloadNum;
	/**
	 * 浏览人数
	 */
	private int visitMemberNum;
	/**
	 * 下载人数
	 */
	// private int downloadMemberNum;
	/**
	 * 分享人数
	 */
	// private int shareMemberNum;
	/**
	 * 资源的创建时间
	 */
	private String createTime;

	/**
	 * 资源的更新时间
	 */
	private String updateTime;

	/**
	 * 是否赞过
	 */
	private boolean hasPraise;
	/**
	 * 是否收藏过
	 */
	private boolean hasCollect;
	/**
	 * 是否有评论赞等的权限
	 */
	private boolean hasAuthority = true;

	/**
	 * 该资源是否已被删除（收藏列表中用到，1:未删除；2:已删除。）
	 */
	private int isDeleteResource;

	/**
	 * 留言类型：1-留言 2-咨询 3-投诉 4-表扬
	 */
	private int guestbookType;

	public boolean isHasCollect() {
		return hasCollect;
	}

	public void setHasCollect(boolean hasCollect) {
		this.hasCollect = hasCollect;
	}

	public boolean isHasAuthority() {
		return hasAuthority;
	}

	public void setHasAuthority(boolean hasAuthority) {
		this.hasAuthority = hasAuthority;
	}

	public int getIsDeleteResource() {
		return isDeleteResource;
	}

	public void setIsDeleteResource(int isDeleteResource) {
		this.isDeleteResource = isDeleteResource;
	}

	public int getGuestbookType() {
		return guestbookType;
	}

	public void setGuestbookType(int guestbookType) {
		this.guestbookType = guestbookType;
	}

	// public int getResourceId() {
	// return resourceId;
	// }
	//
	// public void setResourceId(int resourceId) {
	// this.resourceId = resourceId;
	// }

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

	public List<ObjectInfoBean> getObjectInfo() {
		if (objectInfo == null) {
			objectInfo = new ArrayList<ObjectInfoBean>();
		}
		return objectInfo;
	}

	public void setObjectInfo(List<ObjectInfoBean> objectInfo) {
		this.objectInfo = objectInfo;
	}

	// public List<ObjectInfoBean> getObjectSummary() {
	// return objectSummary;
	// }
	//
	// public void setObjectSummary(List<ObjectInfoBean> objectSummary) {
	// this.objectSummary = objectSummary;
	// }

	// public String getObjectInfoStr() {
	// return objectInfoStr;
	// }
	//
	// public void setObjectInfoStr(String objectInfoStr) {
	// this.objectInfoStr = objectInfoStr;
	// }

	// public String getObjectResource() {
	// return objectResource;
	// }
	//
	// public void setObjectResource(String objectResource) {
	// this.objectResource = objectResource;
	// }

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getPublishWay() {
		return publishWay;
	}

	public void setPublishWay(String publishWay) {
		this.publishWay = publishWay;
	}

	public int getSendMemberId() {
		return sendMemberId;
	}

	public void setSendMemberId(int sendMemberId) {
		this.sendMemberId = sendMemberId;
	}

	public MemberOrGroupInfoBean getSendMemberInfo() {
		if (sendMemberInfo == null) {
			sendMemberInfo = new MemberOrGroupInfoBean();
		}
		return sendMemberInfo;
	}

	public void setSendMemberInfo(MemberOrGroupInfoBean sendMemberInfo) {
		this.sendMemberInfo = sendMemberInfo;
	}

	// public int getReceiveMemberId() {
	// return receiveMemberId;
	// }
	//
	// public void setReceiveMemberId(int receiveMemberId) {
	// this.receiveMemberId = receiveMemberId;
	// }

	// public MemberOrGroupInfoBean getReceiveMemberInfo() {
	// return receiveMemberInfo;
	// }
	//
	// public void setReceiveMemberInfo(MemberOrGroupInfoBean receiveMemberInfo)
	// {
	// this.receiveMemberInfo = receiveMemberInfo;
	// }

	public int getObjectOwnerId() {
		return objectOwnerId;
	}

	public void setObjectOwnerId(int objectOwnerId) {
		this.objectOwnerId = objectOwnerId;
	}

	public MemberOrGroupInfoBean getObjOwnerMemberInfo() {
		if (objOwnerMemberInfo == null) {
			objOwnerMemberInfo = new MemberOrGroupInfoBean();
		}
		return objOwnerMemberInfo;
	}

	public void setObjOwnerMemberInfo(MemberOrGroupInfoBean objOwnerMemberInfo) {
		this.objOwnerMemberInfo = objOwnerMemberInfo;
	}

	// public ResourceCommentBean getObjectCommentOne() {
	// return objectCommentOne;
	// }
	//
	// public void setObjectCommentOne(ResourceCommentBean objectCommentOne) {
	// this.objectCommentOne = objectCommentOne;
	// }
	//
	// public ResourceCommentBean getObjectCommentTwo() {
	// return objectCommentTwo;
	// }
	//
	// public void setObjectCommentTwo(ResourceCommentBean objectCommentTwo) {
	// this.objectCommentTwo = objectCommentTwo;
	// }
	//
	// public ResourceCommentBean getObjectCommentThree() {
	// return objectCommentThree;
	// }
	//
	// public void setObjectCommentThree(ResourceCommentBean objectCommentThree)
	// {
	// this.objectCommentThree = objectCommentThree;
	// }

	// public String getObjTag() {
	// return objTag;
	// }
	//
	// public void setObjTag(String objTag) {
	// this.objTag = objTag;
	// }

	// public int getShareNum() {
	// return shareNum;
	// }
	//
	// public void setShareNum(int shareNum) {
	// this.shareNum = shareNum;
	// }

	// public int getVisitNum() {
	// return visitNum;
	// }
	//
	// public void setVisitNum(int visitNum) {
	// this.visitNum = visitNum;
	// }

	// public int getCollectNum() {
	// return collectNum;
	// }
	//
	// public void setCollectNum(int collectNum) {
	// this.collectNum = collectNum;
	// }

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public void addPraise() {
		setPraiseNum(getPraiseNum() + 1);
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	// public int getDownloadNum() {
	// return downloadNum;
	// }
	//
	// public void setDownloadNum(int downloadNum) {
	// this.downloadNum = downloadNum;
	// }

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	// public int getDownloadMemberNum() {
	// return downloadMemberNum;
	// }
	//
	// public void setDownloadMemberNum(int downloadMemberNum) {
	// this.downloadMemberNum = downloadMemberNum;
	// }

	// public int getShareMemberNum() {
	// return shareMemberNum;
	// }
	//
	// public void setShareMemberNum(int shareMemberNum) {
	// this.shareMemberNum = shareMemberNum;
	// }

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

	public boolean isHasPraise() {
		return hasPraise;
	}

	public void setHasPraise(boolean hasPraise) {
		this.hasPraise = hasPraise;
	}

	// public List<ResourceCommentBean> getCommentBeans(int num_Comment) {
	// List<ResourceCommentBean> beans_ResourceComment = new
	// ArrayList<ResourceCommentBean>();
	// if (num_Comment >= 3) {
	// beans_ResourceComment.add(objectCommentOne);
	// beans_ResourceComment.add(objectCommentTwo);
	// beans_ResourceComment.add(objectCommentThree);
	// } else if (num_Comment == 2) {
	// beans_ResourceComment.add(objectCommentOne);
	// beans_ResourceComment.add(objectCommentTwo);
	// } else if (num_Comment == 1) {
	// beans_ResourceComment.add(objectCommentOne);
	// }
	// return beans_ResourceComment;
	// }
}
