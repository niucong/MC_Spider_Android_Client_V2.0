package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;
import java.util.List;

public class ResourceTrendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2724831914074200577L;

	/**
	 * 动态Id
	 */
	private int trendId;
	/**
	 * 资源Id
	 */
	// private int resourceId;
	/**
	 * 所有者Id 可以为圈子 可以为社员
	 */
	// private int ownerMemberId;
	/**
	 * 该资源是否是开放主页的
	 */
	// private Boolean isOpenPage;
	/**
	 * 动态发起者Id
	 */
	private int actionMemberId;
	/**
	 * 动态发起者信息
	 */
	private MemberOrGroupInfoBean sendMemberInfo;
	/**
	 * 用来放评论
	 */
	private String actinContent;
	/**
	 * 动态的发布方式
	 */
	private String publishWay;
	/**
	 * 动态发布时间
	 */
	private String createTime;
	/**
	 * 动态类型 CREATE_RESOURCE原创、COMMENT_RESOURCE评论、PRAISE_RESOURCE赞
	 */
	private String actionType;
	/**
	 * 此动态针对的资源
	 */
	private ResourceBean resourceBean;
	/**
	 * 是否有权限操作
	 */
	private boolean hasAuthority = true;

	/**
	 * 是否赞过此资源
	 */
	private boolean hasGreat = false;

	/**
	 * 圈子基础信息（非资源）变动动态Bean，对应app_tr_trend_group_info.action_content中的json字符串
	 */
	private GroupBasicTrendBean groupBasicTrendBean;
	
	private List<GroupBasicTrendBean> groupBasicTrendBeanList;

	public List<GroupBasicTrendBean> getGroupBasicTrendBeanList() {
		return groupBasicTrendBeanList;
	}

	public void setGroupBasicTrendBeanList(
			List<GroupBasicTrendBean> groupBasicTrendBeanList) {
		this.groupBasicTrendBeanList = groupBasicTrendBeanList;
	}

	public GroupBasicTrendBean getGroupBasicTrendBean() {
		return groupBasicTrendBean;
	}

	public void setGroupBasicTrendBean(GroupBasicTrendBean groupBasicTrendBean) {
		this.groupBasicTrendBean = groupBasicTrendBean;
	}

	// public Boolean getIsOpenPage() {
	// return isOpenPage;
	// }
	//
	// public void setIsOpenPage(Boolean isOpenPage) {
	// this.isOpenPage = isOpenPage;
	// }

	public int getTrendId() {
		return trendId;
	}

	public void setTrendId(int trendId) {
		this.trendId = trendId;
	}

	// public int getResourceId() {
	// return resourceId;
	// }
	//
	// public void setResourceId(int resourceId) {
	// this.resourceId = resourceId;
	// }

	// public int getOwnerMemberId() {
	// return ownerMemberId;
	// }
	//
	// public void setOwnerMemberId(int ownerMemberId) {
	// this.ownerMemberId = ownerMemberId;
	// }

	public int getActionMemberId() {
		return actionMemberId;
	}

	public void setActionMemberId(int actionMemberId) {
		this.actionMemberId = actionMemberId;
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

	public String getActinContent() {
		return actinContent;
	}

	public void setActinContent(String actinContent) {
		this.actinContent = actinContent;
	}

	public String getPublishWay() {
		return publishWay;
	}

	public void setPublishWay(String publishWay) {
		this.publishWay = publishWay;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * CREATE_RESOURCE原创、COMMENT_RESOURCE评论、PRAISE_RESOURCE赞
	 * 
	 * @return 动态类型
	 */
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public ResourceBean getResourceBean() {
		if (resourceBean == null) {
			resourceBean = new ResourceBean();
		}
		return resourceBean;
	}

	public void setResourceBean(ResourceBean resourceBean) {
		this.resourceBean = resourceBean;
	}

	public boolean isHasAuthority() {
		return hasAuthority;
	}

	public void setHasAuthority(boolean hasAuthority) {
		this.hasAuthority = hasAuthority;
	}

	public boolean isHasGreat() {
		return hasGreat;
	}

	public void setHasGreat(boolean hasGreat) {
		this.hasGreat = hasGreat;
	}

}
