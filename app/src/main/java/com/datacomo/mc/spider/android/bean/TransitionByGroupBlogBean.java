package com.datacomo.mc.spider.android.bean;

import java.util.List;

import com.datacomo.mc.spider.android.net.been.GroupTopicBean;
import com.datacomo.mc.spider.android.net.been.MemberOrGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.QuuboInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.util.TypeUtil;

public class TransitionByGroupBlogBean {
	// 圈博类
	private QuuboInfoBean bean_QuuboInfo;
	// 资源类
	private ResourceBean bean_Resource;
	// 标示
	private String type;
	/**
	 * 圈博编号
	 */
	private int quuboId;
	/**
	 * 圈子编号
	 */
	private int groupId;
	/**
	 * 对象类型
	 */
	private String objectType;
	/**
	 * 圈博信息
	 */
	private String info_Blog;
	/**
	 * 圈博信息
	 */
	private String title_Blog;
	/**
	 * 发送社员信息
	 */
	private MemberOrGroupInfoBean sendMemberInfoBean;
	/**
	 * 资源摘要信息
	 */
	private List<ObjectInfoBean> objectInfoBeans;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * way
	 */
	private String publishWay;
	/**
	 * 赞数量
	 */
	private int praiseNum;
	/**
	 * 访问人数
	 */
	private int visitMemberNum;
	/**
	 * 是否赞过
	 */
	private boolean hasPraise;
	/**
	 * 评论数量
	 */
	private int commentNum;

	private String type_Resource = TypeUtil.type_Resource;
	private String type_ResultMessage = TypeUtil.type_ResultMessage;

	public TransitionByGroupBlogBean(String type, Object bean) {
		this.type = type;
		if (type_Resource.equals(this.type)) {
			bean_Resource = (ResourceBean) bean;
		} else if (type_ResultMessage.equals(this.type)) {
			bean_QuuboInfo = (QuuboInfoBean) bean;
		}
	}

	public int getQuuboId() {
		quuboId = 0;
		if (type_Resource.equals(type)) {
			quuboId = bean_Resource.getObjectId();
		} else if (type_ResultMessage.equals(type)) {
			quuboId = bean_QuuboInfo.getQuuboId();
		}
		return quuboId;
	}

	public int getGroupId() {
		groupId = 0;
		if (type_Resource.equals(type)) {
			groupId = bean_Resource.getObjOwnerMemberInfo().getId();
		} else if (type_ResultMessage.equals(type)) {
			groupId = bean_QuuboInfo.getGroupId();
		}
		return groupId;
	}

	public String getObjectType() {
		objectType = null;
		if (type_Resource.equals(type)) {
			objectType = bean_Resource.getObjectType();
		} else if (type_ResultMessage.equals(type)) {
			objectType = "OBJ_GROUP_QUUBO";
		}
		return objectType;
	}

	public String getInfo_Blog() {
		if (type_ResultMessage.equals(type)) {
			GroupTopicBean bean = bean_QuuboInfo.getGroupTopicBean();
			if (bean != null) {
				info_Blog = bean.getTopicContent();
			}
			if (info_Blog == null || "".equals(info_Blog)) {
				info_Blog = "";
				List<ObjectInfoBean> objectInfoBeans = bean_QuuboInfo
						.getObjectInfoBeans();
				if (objectInfoBeans != null && objectInfoBeans.size() > 0) {
					ObjectInfoBean infoBean = objectInfoBeans.get(0);
					info_Blog += infoBean.getObjectDescription();
					if (info_Blog == null || "".equals(info_Blog)) {
						info_Blog = "";
					}
				}
			}
		}
		return info_Blog;
	}

	public String getTitle_Blog() {
		if (type_ResultMessage.equals(type)) {
			GroupTopicBean bean = bean_QuuboInfo.getGroupTopicBean();
			if (bean != null) {
				title_Blog = bean.getTopicTitle();
			}
			if (title_Blog == null || "".equals(title_Blog)) {
				List<ObjectInfoBean> objectInfoBeans = bean_QuuboInfo
						.getObjectInfoBeans();
				if (objectInfoBeans != null && objectInfoBeans.size() > 0) {
					ObjectInfoBean infoBean = objectInfoBeans.get(0);
					title_Blog = infoBean.getObjectName();
					if (title_Blog == null) {
						title_Blog = "";
					}
				}
			}
		}
		return title_Blog;
	}

	public MemberOrGroupInfoBean getSendMemberInfoBean() {
		sendMemberInfoBean = null;
		if (type_Resource.equals(type)) {
			sendMemberInfoBean = bean_Resource.getSendMemberInfo();
		} else if (type_ResultMessage.equals(type)) {
			sendMemberInfoBean = bean_QuuboInfo.getSendMemberInfoBean();
		}
		return sendMemberInfoBean;
	}

	public MemberOrGroupInfoBean getObjOwnerMemberInfoBean() {
		MemberOrGroupInfoBean objOwnerMemberInfo = null;
		if (type_Resource.equals(type)) {
			objOwnerMemberInfo = bean_Resource.getObjOwnerMemberInfo();
		} else if (type_ResultMessage.equals(type)) {
			objOwnerMemberInfo = bean_QuuboInfo.getGroupInfoBean();
		}
		return objOwnerMemberInfo;
	}

	public List<ObjectInfoBean> getObjectInfoBeans() {
		objectInfoBeans = null;
		if (type_Resource.equals(type)) {
			objectInfoBeans = bean_Resource.getObjectInfo();
		} else if (type_ResultMessage.equals(type)) {
			objectInfoBeans = bean_QuuboInfo.getObjectInfoBeans();
		}
		return objectInfoBeans;
	}

	public String getUpdateTime() {
		updateTime = null;
		if (type_Resource.equals(type)) {
			updateTime = bean_Resource.getUpdateTime();
		} else if (type_ResultMessage.equals(type)) {
			updateTime = bean_QuuboInfo.getUpdateTime();
		}
		return updateTime;
	}

	public String getPublishWay() {
		publishWay = null;

		if (type_Resource.equals(type)) {
			if ("FROM_NOTEPAD".equals(bean_Resource.getSourceType())) {
				return bean_Resource.getSourceType();
			}
			publishWay = bean_Resource.getPublishWay();
		} else if (type_ResultMessage.equals(type)) {
			if ("FROM_NOTEPAD".equals(bean_QuuboInfo.getPosterType())) {
				return bean_QuuboInfo.getPosterType();
			}
			publishWay = bean_QuuboInfo.getPublishWay();
		}
		return publishWay;
	}

	public int getPraiseNum() {
		praiseNum = 0;
		if (type_Resource.equals(type)) {
			praiseNum = bean_Resource.getPraiseNum();
		} else if (type_ResultMessage.equals(type)) {
			praiseNum = bean_QuuboInfo.getPraiseNum();
		}
		return praiseNum;
	}

	public int getVisitMemberNum() {
		visitMemberNum = 0;
		if (type_Resource.equals(type)) {
			visitMemberNum = bean_Resource.getVisitMemberNum();
		} else if (type_ResultMessage.equals(type)) {
			visitMemberNum = bean_QuuboInfo.getVisitMemberNum();
		}
		return visitMemberNum;
	}

	public boolean isHasPraise() {
		hasPraise = true;
		if (type_Resource.equals(type)) {
			hasPraise = bean_Resource.isHasPraise();
		} else if (type_ResultMessage.equals(type)) {
			hasPraise = bean_QuuboInfo.isHasPraise();
		}
		return hasPraise;
	}

	public int getCommentNum() {
		commentNum = 0;
		if (type_Resource.equals(type)) {
			commentNum = bean_Resource.getCommentNum();
		} else if (type_ResultMessage.equals(type)) {
			commentNum = bean_QuuboInfo.getCommentNum();
		}
		return commentNum;
	}

	public int getGuestbookType() {
		int guestbookType = 0;
		if (type_Resource.equals(type)) {
			guestbookType = bean_Resource.getGuestbookType();
		}
		return guestbookType;
	}
}
